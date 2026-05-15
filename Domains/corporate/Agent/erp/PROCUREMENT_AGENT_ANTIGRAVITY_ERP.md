# 🔒 SEALED & LOCKED MASTER PROMPT
## PROCUREMENT_AGENT — ANTIGRAVITY CORPORATE ERP
### Module: `corp.erp.procurement.agent.v1`
### Platform: ECOSKILLER ENTERPRISE SAAS
### Execution Engine: ANTIGRAVITY

---

```
╔═══════════════════════════════════════════════════════════════════════════════════╗
║         ⚡ ANTIGRAVITY PROCUREMENT AGENT — EXECUTION LOCK ENGAGED ⚡              ║
║                                                                                   ║
║   AGENT_NAME           = PROCUREMENT_AGENT                                        ║
║   AGENT_CODENAME       = ANTIGRAVITY::PROCUREMENT                                 ║
║   EXECUTION_MODE       = LOCKED                                                   ║
║   MUTATION_POLICY      = ADD_ONLY                                                 ║
║   CREATIVE_FILL        = FORBIDDEN                                                ║
║   ASSUMPTION_FILL      = FORBIDDEN                                                ║
║   IMPLICIT_BEHAVIOR    = FORBIDDEN                                                ║
║   DEFAULT_BEHAVIOR     = DENY                                                     ║
║   FAILURE_MODE         = STOP_EXECUTION                                           ║
║   AGENT_CLASS          = AUTOMATION / AI AGENT (NON-HUMAN ACTOR)                 ║
║   ARCHITECTURE_AUTH    = PRE_APPROVED_CONSTITUTION                                ║
║   COMPLIANCE_MODE      = ENABLED                                                  ║
║   CONFLICT_POLICY      = DENY                                                     ║
║   DUPLICATION_POLICY   = FORBIDDEN                                                ║
╚═══════════════════════════════════════════════════════════════════════════════════╝
```

---

## 🪐 SECTION 0 — ANTIGRAVITY IDENTITY SEAL

```
AGENT_NAME              = PROCUREMENT_AGENT
AGENT_CODENAME          = ANTIGRAVITY::PROCUREMENT
AGENT_NAMESPACE         = antigravity.corp.erp.procurement
AGENT_DOMAIN            = Corporate ERP › Finance › Procurement & Supply Chain
AGENT_CATEGORY          = AUTOMATION / AI AGENT (Non-human actor)
PLATFORM_PARENT         = ECOSKILLER ENTERPRISE MULTI-TENANT SAAS
ERP_LAYER               = Corporate ERP + Compliance & Audit ERP + SME Hiring Workflow
STAGE_DEPENDENCY        = STAGE 1 COMPLETE + STAGE 3 PARTIAL
INHERITS_FROM           = [AUTH, RBAC, ABAC, MFA, SESSION_MGMT,
                           COMPLIANCE_MODULE, AUDIT_SERVICE,
                           NOTIFICATION_SERVICE, KAFKA_BUS]
SEALED_BY               = ANTIGRAVITY_LOCK_v1
VERSION                 = v1.0.0-SEALED
SEALED_DATE             = 2026-02-24
LOCK_HASH               = [IMMUTABLE — DO NOT MODIFY WITHOUT PLATFORM ADMIN OVERRIDE]
```

---

> ### ⚡ ANTIGRAVITY PRINCIPLE — PROCUREMENT EDITION
>
> Procurement is the gravitational core of every enterprise supply chain.
> Money, vendors, contracts, inventory — all orbit this agent.
> Like an object held in antigravity suspension, every purchase order,
> every vendor bid, every contract clause is held in **deterministic,
> audit-proof, zero-assumption orbit**.
>
> **No free-floating approvals. No gravity-less spend. No vendor favours.**
> The PROCUREMENT_AGENT either executes with full precision —
> or it **STOPS COMPLETELY.**

---

## 1️⃣ AGENT PURPOSE (NON-NEGOTIABLE)

The `PROCUREMENT_AGENT` is a **sealed, autonomous sub-agent** embedded in the **Corporate ERP** layer of the Ecoskiller Enterprise SaaS platform. It governs the full procurement lifecycle — from purchase requisition to vendor payment release — across all enterprise tenants.

### CORE MANDATE

```
MANDATE_01 = Automate end-to-end Purchase Requisition → Purchase Order lifecycle
MANDATE_02 = Manage vendor registration, qualification, and performance scoring
MANDATE_03 = Run competitive bidding, RFQ, and e-auction workflows
MANDATE_04 = Enforce multi-level purchase approval routing (by value + category)
MANDATE_05 = Track inventory, goods receipt, and three-way matching
MANDATE_06 = Generate compliant contracts with version control and e-signatures
MANDATE_07 = Process vendor invoices and trigger payment to Finance/AP module
MANDATE_08 = Feed all procurement data into Compliance Audit and Analytics ERP
MANDATE_09 = Enforce spend limits, budget checks, and policy controls
MANDATE_10 = Serve AI-advisory intelligence on vendor risk, spend, and forecasting
```

### IRON LAW

```
THE AGENT PREPARES, ROUTES, COMPUTES, AND ADVISES.
IT NEVER INDEPENDENTLY APPROVES PURCHASE ORDERS.
IT NEVER DIRECTLY RELEASES VENDOR PAYMENTS.
IT NEVER SELECTS A VENDOR WITHOUT HUMAN DECISION.
ALL FINAL APPROVALS = AUTHORISED HUMAN ACTORS ONLY.
```

---

## 2️⃣ SCOPE OF OPERATION (LOCKED)

```
OPERATES_WITHIN           = ENTERPRISE TENANT (Isolated per tenant)
CROSS_TENANT_ACCESS       = FORBIDDEN (absolute)
DOMAIN_ISOLATION          = HARD
  Institute               ≠ Company ≠ Platform
  Arts | Commerce | Science | Technology | Administration — no cross-leak
SME_SCOPE                 = INCLUDED (SME procurement sub-workflow)
INSTITUTE_SCOPE           = EXCLUDED (Institute ERP has separate procurement)
STUDENT_SCOPE             = EXCLUDED
PARENT_SCOPE              = EXCLUDED
RECRUITER_SCOPE           = EXCLUDED
```

### Enterprise Organisational Scope — Procurement Authority

| Level | Role                  | Purchase Limit (Single PO) | Approval Route            |
|-------|-----------------------|---------------------------|---------------------------|
| L1    | Intern / Trainee      | ZERO — cannot raise PR     | N/A                       |
| L2    | Associate             | Up to ₹5,000               | L4+ approval              |
| L3    | Senior Associate      | Up to ₹25,000              | L4+ approval              |
| L4    | Lead / Specialist     | Up to ₹1,00,000            | L5+ approval              |
| L5    | Manager               | Up to ₹5,00,000            | L6+ approval              |
| L6    | Senior Manager        | Up to ₹25,00,000           | L7 + Finance approval     |
| L7    | Director / CXO        | Up to ₹1,00,00,000         | Board + Finance approval  |
| ADMIN | Finance / Procurement Admin | Unlimited (policy-bound) | Dual approval required |

> ⚠️ Limits are **tenant-configurable** at platform setup. Mid-cycle limit changes = BLOCKED until current POs resolve.

---

## 3️⃣ USER ACCESS MATRIX (RBAC + ABAC INHERITED)

```
RBAC_INHERITED            = TRUE
ABAC_INHERITED            = TRUE
MFA_REQUIRED              = TRUE (All procurement write operations)
SESSION_LOCK              = TRUE (Procurement session timeout = 20 mins idle)
ATTRIBUTE_CONTEXT         = department + cost_centre + budget_owner + vendor_category
```

| Role                    | Raise PR | View PO | Approve PO | Vendor Mgmt | Contract Sign | Payment Trigger | Audit Logs |
|-------------------------|:--------:|:-------:|:----------:|:-----------:|:-------------:|:---------------:|:----------:|
| Employee (L2–L3)        | YES      | Own     | NO         | NO          | NO            | NO              | NO         |
| Lead / Specialist (L4)  | YES      | Dept    | Up to limit| NO          | NO            | NO              | NO         |
| Manager (L5)            | YES      | Dept    | Up to limit| View only   | NO            | NO              | Dept       |
| Sr. Manager / Dir (L6+) | YES      | All     | Up to limit| YES         | YES           | NO              | Full       |
| Procurement Admin       | YES      | All     | All        | YES         | YES           | Prepare only    | Full       |
| Finance Admin           | YES      | All     | Policy     | NO          | YES           | YES (trigger)   | Full       |
| Tenant Admin            | YES      | All     | All        | YES         | YES           | YES             | Full       |
| Platform Admin          | NO       | Meta    | NO         | NO          | NO            | NO              | Meta only  |
| Vendor (External Portal)| NO       | Own bids| NO         | Own profile | NO            | NO              | NO         |
| PROCUREMENT_AGENT (AI)  | Compute  | All     | NEVER      | Score only  | NEVER         | NEVER           | Write      |

> **CRITICAL LOCK:** `PROCUREMENT_AGENT` role column — NEVER approves, NEVER signs, NEVER pays.

---

## 4️⃣ PROCUREMENT ENGINE MODULES (ALL REQUIRED)

---

### 4A. 📋 PURCHASE REQUISITION (PR) ENGINE

```
PURPOSE = Structured internal demand capture before any spend is committed

PR_LIFECYCLE_STATES:
  DRAFT → SUBMITTED → BUDGET_CHECK → ROUTING → APPROVED → PO_CREATED
                                    ↘ REJECTED → REVISION_REQUESTED

INPUT_FIELDS (Mandatory):
  - Requestor identity (user_id, department, cost_centre)
  - Item description + specifications
  - Quantity + unit of measure
  - Estimated unit cost + total estimated value
  - Required delivery date
  - Justification / business need
  - Budget code (linked to Budget Service)
  - Category (IT / Infrastructure / Services / Consumables / Capex)
  - Preferred vendor (optional — advisory only, not binding)

VALIDATION_RULES:
  - Budget availability check: MANDATORY before routing
  - If estimated value > L5 limit: auto-escalate approval chain
  - Duplicate PR detection: flag if same item + dept + month (AI advisory)
  - Capex PR: requires Finance Admin co-approval always

AGENT_ACTIONS:
  - Run budget availability check (read-only against Budget Service)
  - Determine approval routing tree based on value + category + level
  - Flag anomalies (AI advisory — duplicate, over-budget, unusual spec)
  - Generate PR reference number (format: PR-{TENANT}-{YYYYMM}-{SEQ})
  - Push to Approval Queue
```

---

### 4B. 🏗️ PURCHASE ORDER (PO) ENGINE

```
PURPOSE = Legally binding procurement document issued to selected vendor

PO_LIFECYCLE_STATES:
  DRAFT → PR_LINKED → VENDOR_SELECTED → APPROVED → ISSUED → ACKNOWLEDGED
  → GOODS_IN_TRANSIT → GRN_PENDING → GRN_DONE → INVOICE_MATCHED → CLOSED
                                              ↘ DISPUTED → RESOLUTION

PO_CREATION_RULES:
  - PO can only be created AFTER PR is APPROVED
  - Vendor must be QUALIFIED and ACTIVE in vendor master
  - PO value must be within approved PR estimate ±10% (breach = re-approval)
  - PO number format: PO-{TENANT}-{YYYYMM}-{SEQ}
  - Each PO line item must map to PR line item (1:1 or consolidated)

PO_FIELDS (Mandatory):
  - Linked PR reference(s)
  - Vendor ID (from vendor master)
  - Line items (description, qty, unit, rate, tax code, total)
  - Delivery address
  - Delivery schedule (per line or consolidated)
  - Payment terms (net 30/45/60 — per contract)
  - Applicable taxes (GST CGST/SGST/IGST — auto-computed)
  - Digital signature slot (approver)
  - Terms & Conditions (linked to contract or standard T&C template)

AGENT_ACTIONS:
  - Auto-populate PO from approved PR data
  - Compute tax amounts per HSN/SAC code (read from tax config)
  - Validate vendor status before PO generation
  - Route PO for human approval per approval matrix
  - On approval: issue PO to vendor via Vendor Portal + email
  - Track vendor acknowledgement (SLA: 48 hours — breach = alert)
```

---

### 4C. 🏭 VENDOR MANAGEMENT ENGINE

```
PURPOSE = Centralised vendor master, onboarding, qualification, and performance

VENDOR_LIFECYCLE_STATES:
  REGISTERED → DOCUMENT_SUBMISSION → VERIFICATION → QUALIFIED
  → ACTIVE → PERFORMANCE_REVIEW → SUSPENDED | BLACKLISTED | REACTIVATED

VENDOR_ONBOARDING_FIELDS (Mandatory):
  - Legal name + trade name
  - GST number (verified via GSTIN API)
  - PAN number
  - Company registration number
  - Bank details (AES-256 encrypted)
  - MSME/Udyam registration (if applicable)
  - Category of supply (IT / Services / Goods / Infra / Consulting)
  - Contact person + email + phone
  - Registered address
  - Document uploads (GST cert, PAN, bank letter, incorporation cert)

VENDOR_QUALIFICATION_CHECKLIST:
  - GST verification: MANDATORY (auto-verify via API)
  - PAN verification: MANDATORY
  - Bank account verification: MANDATORY
  - Document completeness: MANDATORY
  - Compliance check (blacklist screening): MANDATORY
  - Financial health score (optional AI advisory)
  - Reference checks (optional)

VENDOR_PERFORMANCE_SCORING (AI Advisory):
  Parameters:
    - On-time delivery rate (weight: 30%)
    - Quality acceptance rate (weight: 25%)
    - Invoice accuracy rate (weight: 20%)
    - Responsiveness score (weight: 15%)
    - Compliance adherence (weight: 10%)
  Output: Score 0–100 → Grade: A / B / C / D / SUSPENDED
  Frequency: Computed per PO closure + quarterly rollup
  Human Action Required: Grade D → Suspension decision (human only)
  AI Role: Compute and present. NEVER suspend without human approval.

VENDOR_PORTAL (External Facing):
  - Vendor self-service login (separate auth, scoped access)
  - Submit bids / quotations
  - View own PO status
  - Upload invoices
  - View payment status
  - Update own profile (pending re-verification on sensitive fields)
  PORTAL_ISOLATION: Vendor sees ONLY own data. Cross-vendor access = FORBIDDEN.
```

---

### 4D. 🎯 RFQ / BIDDING ENGINE

```
PURPOSE = Competitive procurement to ensure best value and transparency

SUPPORTED_MODES:
  - RFQ (Request for Quotation) — standard competitive quote
  - RFP (Request for Proposal) — technical + commercial evaluation
  - Reverse Auction (e-Auction) — live competitive bidding
  - Direct PO (Single Source) — REQUIRES justification + senior approval

RFQ_LIFECYCLE_STATES:
  DRAFT → PUBLISHED → BID_OPEN → BID_CLOSED → EVALUATION
  → NEGOTIATION → VENDOR_SELECTED → PO_TRIGGERED

RFQ_CREATION_RULES:
  - Linked to approved PR (mandatory)
  - Minimum bidders: 3 (for any value > ₹50,000) — configurable per policy
  - Single source justification: requires L6+ approval + documented reason
  - Bid validity period: minimum 30 days
  - Bid submission: encrypted until deadline (no early reveal)
  - Late bids: REJECTED (system-enforced, no override)

EVALUATION_MATRIX (AI Advisory — Human Decides):
  - Technical compliance score (pass/fail gates)
  - Commercial score (L1 = lowest valid bid)
  - Vendor performance score (from Vendor Management Engine)
  - Weighted composite score (weights tenant-configurable)
  AI outputs ranked recommendations. Human evaluator selects winner.
  AI selection = FORBIDDEN.

REVERSE AUCTION RULES:
  - Minimum decrement: configurable (default 0.5%)
  - Auction duration: configurable (default 60 minutes)
  - Auto-extend: 5 minutes if bid received in last 2 minutes
  - Bid anonymity: bidder names hidden during live auction
  - Audit trail: every bid timestamped with bidder_id + amount
```

---

### 4E. 📦 GOODS RECEIPT NOTE (GRN) & THREE-WAY MATCHING ENGINE

```
PURPOSE = Verify delivery against PO before any payment is authorised

GRN_LIFECYCLE_STATES:
  PO_ISSUED → GOODS_EXPECTED → PARTIAL_RECEIPT | FULL_RECEIPT
  → GRN_CREATED → THREE_WAY_MATCH → MATCH_PASSED | MATCH_FAILED
  → INVOICE_CLEARED | DISPUTE_RAISED

GRN_CREATION_RULES:
  - Only authorised Warehouse / Receiving officer can create GRN
  - GRN must reference PO number + line items
  - Partial delivery: partial GRN created, PO remains OPEN
  - Quality rejection: QC_FAILED items excluded from GRN, return order raised
  - GRN timestamp = evidence of physical receipt (legally binding)

THREE-WAY MATCHING (AUTOMATED BY AGENT):
  Match Points:
    1. PO (what was ordered)
    2. GRN (what was received)
    3. Vendor Invoice (what is being billed)

  MATCH_RULES:
    - Quantity tolerance: ±2% (configurable)
    - Price tolerance: 0% (price must match PO exactly)
    - Tax tolerance: 0% (must match PO tax computation)

  MATCH_OUTCOMES:
    - FULL_MATCH    → Invoice cleared → Payment queue
    - QUANTITY_MISMATCH → Alert Procurement Admin → Human resolution
    - PRICE_MISMATCH    → Alert + BLOCK payment → Dispute raised
    - TAX_MISMATCH      → Alert + BLOCK payment → Finance review

  AGENT_ROLE = Run matching logic. Flag deviations. NEVER clear disputed invoices.
```

---

### 4F. 📜 CONTRACT MANAGEMENT ENGINE

```
PURPOSE = Centralised, version-controlled, legally compliant contract lifecycle

CONTRACT_TYPES:
  - Purchase Contract (goods + services)
  - Framework Agreement (rate contract — multiple POs against one contract)
  - NDA (Non-Disclosure Agreement)
  - AMC (Annual Maintenance Contract)
  - SLA Agreement (service level commitments)

CONTRACT_LIFECYCLE_STATES:
  DRAFT → REVIEW → NEGOTIATION → LEGAL_CLEARANCE
  → APPROVED → ACTIVE → RENEWAL_DUE → EXPIRED | TERMINATED | RENEWED

CONTRACT_FIELDS (Mandatory):
  - Contract ID (format: CON-{TENANT}-{YYYYMM}-{SEQ})
  - Parties (Tenant entity + Vendor entity)
  - Scope of work / supply
  - Contract value + currency
  - Start date + end date
  - Payment terms + milestones
  - Penalty clauses (SLA breach, late delivery)
  - Governing law + jurisdiction
  - Confidentiality clauses
  - Termination conditions
  - Version number + change log

VERSION_CONTROL:
  - Every edit creates a new version (immutable history)
  - Diff view available between any two versions
  - Version approval required before superseding active version

E-SIGNATURE WORKFLOW:
  - DocuSign / eSign integration (tenant-configurable)
  - Signing sequence: Tenant authorised signatory → Vendor signatory
  - Signed PDF archived immediately (immutable, SHA-256 hash recorded)
  - AI NEVER signs. Agent prepares signature packet, routes to human.

CONTRACT ALERTS:
  - 90 days before expiry → Renewal alert → Procurement Admin
  - 30 days before expiry → Escalation → L6+
  - Day of expiry (no renewal) → Auto-status: EXPIRED → Block new POs
  - SLA breach detected → Penalty clause flagged → Human decision
```

---

### 4G. 💳 ACCOUNTS PAYABLE (AP) INTERFACE

```
PURPOSE = Handoff from Procurement to Finance for vendor payment

AGENT_ROLE = PREPARE PAYMENT BATCH + SUBMIT FOR HUMAN APPROVAL
AGENT_CANNOT = Trigger bank transfer directly

AP_FLOW:
  Three-Way Match PASSED
       │
  PROCUREMENT_AGENT: Generate Payment Advice
       │
  Payment Advice pushed to Finance AP Queue
       │
  Finance Admin: Reviews → MFA → Approves payment batch
       │
  AP Service → Bank Gateway → Execute NEFT/RTGS/IMPS
       │
  Vendor notified (payment confirmation + UTR number)
       │
  PO/Invoice marked PAID in procurement system

PAYMENT_TERMS_ENFORCEMENT:
  - Net 30 / Net 45 / Net 60 (per contract) — agent tracks due dates
  - Early payment discount: flagged for Finance Admin decision
  - Overdue payment: escalation alert to Finance Admin + Procurement Admin
  - Advance payment: requires separate approval + bank guarantee check

TDS ON VENDOR PAYMENTS:
  - Agent computes TDS at applicable rates (Section 194C, 194J, etc.)
  - TDS certificate (Form 16A) generated quarterly
  - TDS liability fed to Payroll/Tax compliance module
```

---

### 4H. 🗃️ INVENTORY & ASSET TRACKING ENGINE

```
PURPOSE = Track physical goods post-GRN into internal inventory

INVENTORY_ACTIONS (AGENT):
  - On GRN creation: auto-update inventory stock levels
  - Low stock alert: configurable reorder point → auto-raise PR (advisory)
  - Asset tagging: Capex items assigned asset ID on receipt
  - Location tracking: warehouse / floor / department assignment

INVENTORY_DATA_POINTS:
  - Item master (SKU, description, UOM, category, HSN code)
  - Stock levels (on-hand, reserved, in-transit, in-inspection)
  - Valuation method: FIFO / Weighted Average (tenant-configurable)
  - Expiry tracking (for perishables / software licences)
  - Asset depreciation schedule (Capex items, linked to Finance ERP)

REORDER LOGIC (AI Advisory):
  - Analyse consumption rate (last 6 months)
  - Predict stockout date
  - Suggest reorder quantity and timing
  - Human raises PR; agent does NOT auto-raise PO
```

---

### 4I. 📊 PROCUREMENT ANALYTICS & ROI DASHBOARDS

```
DASHBOARDS_FOR = Procurement Admin, Finance Admin, Tenant Admin, L5+ (scoped)

METRICS:
  Spend Analytics:
    - Total procurement spend by category / department / vendor / month
    - Maverick spend (POs raised without PR) — compliance flag
    - Savings achieved vs budget (negotiated savings tracking)
    - Spend concentration risk (% spend with top 3 vendors)
    - PO cycle time (PR to PO issuance average days)

  Vendor Analytics:
    - Vendor performance scorecards (per vendor, per category)
    - On-time delivery trends
    - Quality rejection rate trends
    - Vendor payment days (DPO — Days Payable Outstanding)

  Process Analytics:
    - PR to PO conversion rate
    - Pending approvals aging report
    - Contract expiry pipeline (next 90 / 180 days)
    - Three-way match exception rate
    - RFQ participation rate by vendor category

  Compliance Analytics:
    - POs without PR (policy violations)
    - Single-source justification ratio
    - Overdue payment report
    - GST reconciliation status (GSTR-2A vs purchase register)

ANALYTICS_ENGINE  = PostgreSQL + Elasticsearch
CHART_RENDER      = Flutter (app) / React Next.js (web — read-only)
EXPORT_FORMATS    = PDF, XLSX, CSV (admin-gated, audit-logged)
```

---

### 4J. 🗂️ COMPLIANCE & AUDIT ERP (PROCUREMENT LAYER)

```
AUDIT_LOG_POLICY:
  - Every procurement event logged: actor_id, action, entity, before_state,
    after_state, timestamp, IP, user_agent
  - Logs are IMMUTABLE (append-only, no update / delete ever)
  - Storage: PostgreSQL → procurement_audit_log
  - Mirror: Compliance Audit Service (platform-level)
  - Retention: 10 years (statutory minimum for financial records)

STATUTORY_COMPLIANCE:
  - GST Input Tax Credit (ITC) reconciliation (GSTR-2A vs purchase register)
  - TDS on vendor payments (Section 194C / 194J / 194I / 26Q)
  - MSME payment compliance (payment within 45 days — statutory)
  - E-invoicing mandate (IRN generation for vendors > threshold)

COMPLIANCE_ALERTS (Kafka-driven):
  - MSME vendor unpaid beyond 45 days → ESCALATION → Finance + Admin alert
  - GST ITC mismatch detected → FLAG → Finance review
  - E-invoice IRN not generated for eligible invoice → BLOCK matching
  - Single source > policy threshold without justification → COMPLIANCE BREACH
  - PO without PR detected → MAVERICK SPEND ALERT → Admin + Audit log

GDPR / DATA PRIVACY:
  - Vendor bank details: AES-256 at rest + TLS 1.3 in transit
  - Vendor PAN / GST: encrypted storage, role-gated display
  - Contract PDFs: encrypted object storage, access-logged
  - Bulk export: Tenant Admin only, dual confirmation, full audit trail
```

---

## 5️⃣ DATA MODELS (POSTGRESQL — LOCKED SCHEMA)

```sql
-- ═══════════════════════════════════════════════════════
-- CORE PROCUREMENT TABLES (Non-negotiable, append-aware)
-- ═══════════════════════════════════════════════════════

vendor_master (
  id                        UUID PRIMARY KEY,
  tenant_id                 UUID NOT NULL,
  legal_name                TEXT NOT NULL,
  trade_name                TEXT,
  gst_number                TEXT UNIQUE,
  pan_number_encrypted      TEXT,                   -- AES-256
  company_reg_number        TEXT,
  msme_udyam_number         TEXT,
  category                  VARCHAR(50),             -- IT | GOODS | SERVICES | INFRA
  status                    VARCHAR(20),             -- REGISTERED | QUALIFIED | ACTIVE
                                                     -- | SUSPENDED | BLACKLISTED
  performance_score         DECIMAL(5,2),            -- 0.00 to 100.00
  performance_grade         CHAR(1),                 -- A | B | C | D
  bank_account_encrypted    TEXT,                    -- AES-256
  ifsc_code                 TEXT,
  created_at                TIMESTAMPTZ,
  updated_at                TIMESTAMPTZ
)

purchase_requisition (
  id                        UUID PRIMARY KEY,
  tenant_id                 UUID NOT NULL,
  pr_number                 TEXT UNIQUE,             -- PR-{TENANT}-{YYYYMM}-{SEQ}
  requestor_id              UUID NOT NULL,
  department                TEXT,
  cost_centre               TEXT,
  budget_code               TEXT,
  category                  VARCHAR(50),
  estimated_value           DECIMAL(14,2),
  justification             TEXT,
  required_by_date          DATE,
  status                    VARCHAR(30),             -- DRAFT | SUBMITTED | BUDGET_CHECK
                                                     -- | ROUTING | APPROVED | REJECTED
                                                     -- | PO_CREATED
  budget_check_result       VARCHAR(20),             -- PASSED | FAILED | FLAGGED
  approved_by               UUID,
  approved_at               TIMESTAMPTZ,
  created_at                TIMESTAMPTZ,
  updated_at                TIMESTAMPTZ
)

pr_line_items (
  id                        UUID PRIMARY KEY,
  pr_id                     UUID REFERENCES purchase_requisition(id),
  line_number               INT,
  description               TEXT,
  specification             TEXT,
  quantity                  DECIMAL(10,3),
  unit_of_measure           VARCHAR(20),
  estimated_unit_price      DECIMAL(12,2),
  estimated_line_total      DECIMAL(14,2),
  hsn_sac_code              TEXT,
  preferred_vendor_id       UUID REFERENCES vendor_master(id)   -- optional, advisory
)

purchase_order (
  id                        UUID PRIMARY KEY,
  tenant_id                 UUID NOT NULL,
  po_number                 TEXT UNIQUE,             -- PO-{TENANT}-{YYYYMM}-{SEQ}
  pr_id                     UUID REFERENCES purchase_requisition(id),
  vendor_id                 UUID REFERENCES vendor_master(id),
  contract_id               UUID,                    -- optional FK to contracts
  total_value               DECIMAL(14,2),
  currency                  CHAR(3) DEFAULT 'INR',
  tax_amount                DECIMAL(12,2),
  grand_total               DECIMAL(14,2),
  payment_terms             VARCHAR(20),             -- NET30 | NET45 | NET60
  delivery_address          TEXT,
  delivery_date             DATE,
  status                    VARCHAR(30),             -- DRAFT | APPROVED | ISSUED
                                                     -- | ACKNOWLEDGED | GRN_PENDING
                                                     -- | GRN_DONE | INVOICE_MATCHED
                                                     -- | CLOSED | DISPUTED
  approved_by               UUID,
  approved_at               TIMESTAMPTZ,
  issued_at                 TIMESTAMPTZ,
  vendor_ack_at             TIMESTAMPTZ,
  created_at                TIMESTAMPTZ,
  updated_at                TIMESTAMPTZ
)

po_line_items (
  id                        UUID PRIMARY KEY,
  po_id                     UUID REFERENCES purchase_order(id),
  pr_line_id                UUID REFERENCES pr_line_items(id),
  line_number               INT,
  description               TEXT,
  quantity                  DECIMAL(10,3),
  unit_of_measure           VARCHAR(20),
  unit_price                DECIMAL(12,2),
  line_total                DECIMAL(14,2),
  tax_rate                  DECIMAL(5,2),
  tax_amount                DECIMAL(12,2),
  hsn_sac_code              TEXT,
  delivery_date             DATE
)

rfq_header (
  id                        UUID PRIMARY KEY,
  tenant_id                 UUID NOT NULL,
  rfq_number                TEXT UNIQUE,             -- RFQ-{TENANT}-{YYYYMM}-{SEQ}
  pr_id                     UUID REFERENCES purchase_requisition(id),
  rfq_type                  VARCHAR(20),             -- RFQ | RFP | AUCTION | DIRECT
  title                     TEXT,
  bid_open_date             TIMESTAMPTZ,
  bid_close_date            TIMESTAMPTZ,
  minimum_bidders           INT DEFAULT 3,
  status                    VARCHAR(20),             -- DRAFT | PUBLISHED | BID_OPEN
                                                     -- | BID_CLOSED | EVALUATION
                                                     -- | VENDOR_SELECTED | CLOSED
  selected_vendor_id        UUID,                    -- SET ONLY BY HUMAN EVALUATOR
  created_by                UUID,
  created_at                TIMESTAMPTZ,
  updated_at                TIMESTAMPTZ
)

rfq_bids (
  id                        UUID PRIMARY KEY,
  rfq_id                    UUID REFERENCES rfq_header(id),
  vendor_id                 UUID REFERENCES vendor_master(id),
  bid_amount                DECIMAL(14,2),
  technical_score           DECIMAL(5,2),            -- SET BY HUMAN EVALUATOR
  commercial_score          DECIMAL(5,2),            -- COMPUTED BY AGENT
  composite_score           DECIMAL(5,2),            -- AI ADVISORY — human decides
  submitted_at              TIMESTAMPTZ,
  is_valid                  BOOLEAN,
  disqualification_reason   TEXT
)

goods_receipt_note (
  id                        UUID PRIMARY KEY,
  tenant_id                 UUID NOT NULL,
  grn_number                TEXT UNIQUE,             -- GRN-{TENANT}-{YYYYMM}-{SEQ}
  po_id                     UUID REFERENCES purchase_order(id),
  received_by               UUID,
  receipt_date              DATE,
  delivery_note_number      TEXT,
  status                    VARCHAR(20),             -- PARTIAL | FULL | QC_PENDING
                                                     -- | QC_PASSED | QC_FAILED
  created_at                TIMESTAMPTZ,
  updated_at                TIMESTAMPTZ
)

grn_line_items (
  id                        UUID PRIMARY KEY,
  grn_id                    UUID REFERENCES goods_receipt_note(id),
  po_line_id                UUID REFERENCES po_line_items(id),
  received_quantity         DECIMAL(10,3),
  accepted_quantity         DECIMAL(10,3),
  rejected_quantity         DECIMAL(10,3),
  rejection_reason          TEXT
)

vendor_invoice (
  id                        UUID PRIMARY KEY,
  tenant_id                 UUID NOT NULL,
  invoice_number            TEXT,
  vendor_id                 UUID REFERENCES vendor_master(id),
  po_id                     UUID REFERENCES purchase_order(id),
  grn_id                    UUID,
  invoice_date              DATE,
  invoice_amount            DECIMAL(14,2),
  tax_amount                DECIMAL(12,2),
  grand_total               DECIMAL(14,2),
  irn_number                TEXT,                    -- E-invoice IRN
  match_status              VARCHAR(20),             -- PENDING | FULL_MATCH
                                                     -- | PARTIAL_MATCH | DISPUTED
  match_checked_at          TIMESTAMPTZ,
  cleared_for_payment       BOOLEAN DEFAULT FALSE,
  cleared_by                UUID,                    -- HUMAN ONLY
  cleared_at                TIMESTAMPTZ,
  created_at                TIMESTAMPTZ
)

contracts (
  id                        UUID PRIMARY KEY,
  tenant_id                 UUID NOT NULL,
  contract_number           TEXT UNIQUE,             -- CON-{TENANT}-{YYYYMM}-{SEQ}
  contract_type             VARCHAR(30),             -- PURCHASE | FRAMEWORK | NDA
                                                     -- | AMC | SLA
  vendor_id                 UUID REFERENCES vendor_master(id),
  title                     TEXT,
  scope                     TEXT,
  value                     DECIMAL(14,2),
  currency                  CHAR(3) DEFAULT 'INR',
  start_date                DATE,
  end_date                  DATE,
  payment_terms             TEXT,
  status                    VARCHAR(20),             -- DRAFT | REVIEW | NEGOTIATION
                                                     -- | APPROVED | ACTIVE
                                                     -- | RENEWAL_DUE | EXPIRED
                                                     -- | TERMINATED
  version_number            INT DEFAULT 1,
  signed_pdf_url            TEXT,                    -- Encrypted S3 URL
  signed_pdf_hash           TEXT,                    -- SHA-256 immutability proof
  tenant_signatory_id       UUID,                    -- HUMAN ONLY
  vendor_signatory_name     TEXT,
  signed_at                 TIMESTAMPTZ,
  created_at                TIMESTAMPTZ,
  updated_at                TIMESTAMPTZ
)

procurement_audit_log (
  id                        UUID PRIMARY KEY,
  tenant_id                 UUID NOT NULL,
  actor_id                  UUID,
  actor_type                VARCHAR(20),             -- HUMAN | AGENT | SYSTEM
  action                    VARCHAR(100),
  entity_type               VARCHAR(50),             -- PR | PO | RFQ | GRN | INVOICE
                                                     -- | CONTRACT | VENDOR
  entity_id                 UUID,
  before_state              JSONB,
  after_state               JSONB,
  ip_address                INET,
  user_agent                TEXT,
  created_at                TIMESTAMPTZ
  -- APPEND ONLY. NO UPDATE. NO DELETE. EVER.
)

inventory_master (
  id                        UUID PRIMARY KEY,
  tenant_id                 UUID NOT NULL,
  item_code                 TEXT UNIQUE,
  description               TEXT,
  uom                       VARCHAR(20),
  category                  VARCHAR(50),
  hsn_code                  TEXT,
  reorder_point             DECIMAL(10,3),
  reorder_quantity          DECIMAL(10,3),
  stock_on_hand             DECIMAL(10,3),
  stock_reserved            DECIMAL(10,3),
  stock_in_transit          DECIMAL(10,3),
  valuation_method          VARCHAR(20),             -- FIFO | WEIGHTED_AVERAGE
  last_purchase_price       DECIMAL(12,2),
  updated_at                TIMESTAMPTZ
)
```

---

## 6️⃣ MICROSERVICE ARCHITECTURE & EVENT FLOWS

### Kafka Event-Driven Flows

```
══════════════════════════════════════════════════════
FLOW A — PR TO PO LIFECYCLE
══════════════════════════════════════════════════════

  Employee raises PR (Flutter app)
         │
  Kafka: procurement.pr.submitted
         │
  PROCUREMENT_AGENT: Run budget check (Budget Service API)
         │
  Budget PASSED ──────────────────────────► Route for approval
  Budget FAILED ──────────────────────────► Alert requestor + block
         │
  Approval routed to correct level (RBAC routing matrix)
         │
  Approver: MFA → Approve / Reject / Request Revision
         │
  Kafka: procurement.pr.approved
         │
  PROCUREMENT_AGENT: RFQ check
  (value > threshold → RFQ mandatory │ value ≤ threshold → direct PO eligible)
         │
  [If RFQ] → RFQ Engine triggered (Flow B)
  [If Direct] → PO Engine triggered
         │
  PO Created → Approval routing → Human Approves → PO Issued
         │
  Kafka: procurement.po.issued
         │
  Vendor Portal: PO visible to vendor (acknowledgement due in 48h)
         │
  Kafka: procurement.po.acknowledged
         │
  Delivery → GRN → Three-Way Match (Flow C)


══════════════════════════════════════════════════════
FLOW B — RFQ / BIDDING
══════════════════════════════════════════════════════

  RFQ Published
         │
  Kafka: procurement.rfq.published
         │
  Vendor Portal: Notifications sent to qualified vendors (category match)
         │
  Vendors submit bids (encrypted until close date)
         │
  Bid close date reached → Bids unsealed
         │
  Kafka: procurement.rfq.closed
         │
  PROCUREMENT_AGENT: Compute commercial scores + composite ranking (AI advisory)
         │
  Evaluation summary presented to human evaluator
         │
  Human evaluator: Technical scoring + vendor selection
         │
  Kafka: procurement.rfq.vendor_selected
         │
  PO Engine triggered for selected vendor


══════════════════════════════════════════════════════
FLOW C — GRN → INVOICE → PAYMENT
══════════════════════════════════════════════════════

  Goods delivered → Receiving officer creates GRN (Flutter app)
         │
  Kafka: procurement.grn.created
         │
  PROCUREMENT_AGENT: Run three-way match (PO ↔ GRN ↔ Invoice)
         │
  FULL_MATCH ─────────────────────────────► Payment advice generated
  MISMATCH ───────────────────────────────► BLOCK + Alert + Dispute raised
         │
  Payment advice → Finance AP Queue
         │
  Kafka: procurement.invoice.cleared
         │
  Finance Admin: MFA → Approve payment batch
         │
  Bank Gateway: Execute transfer
         │
  Kafka: procurement.payment.completed
         │
  Vendor notified (UTR + payment details)
         │
  PO marked CLOSED in procurement system


══════════════════════════════════════════════════════
FLOW D — CONTRACT EXPIRY MANAGEMENT
══════════════════════════════════════════════════════

  Daily cron: PROCUREMENT_AGENT scans active contracts
         │
  90 days to expiry → Kafka: procurement.contract.renewal_alert_90d
         │
  Notification Service → Procurement Admin (renewal action due)
         │
  30 days to expiry → Kafka: procurement.contract.renewal_alert_30d
         │
  Escalation → L6+ + Finance Admin
         │
  Expiry day (no renewal) → Kafka: procurement.contract.expired
         │
  PO creation against this contract → BLOCKED
         │
  Renewal initiated → New contract version → Human sign-off
```

---

## 7️⃣ AI INTELLIGENCE LAYER

```
AI_ROLE                 = ADVISORY ONLY
AI_NEVER_APPROVES       = TRUE (hard lock)
AI_NEVER_SELECTS_VENDOR = TRUE (hard lock)
AI_NEVER_SIGNS          = TRUE (hard lock)
AI_NEVER_PAYS           = TRUE (hard lock)
AI_OVERRIDES_HUMAN      = FORBIDDEN (absolute)
```

| AI Function | Input | Output | Human Action Required |
|---|---|---|---|
| Duplicate PR Detector | PR data vs history | Flag + similarity score | Requestor confirms need |
| Spend Anomaly Detector | PO vs budget + history | Anomaly alert | Approver acknowledges |
| Vendor Risk Scorer | Performance history + market signals | Risk grade (A–D) | Procurement Admin reviews |
| RFQ Bid Ranker | Bid prices + vendor scores | Ranked recommendation | Human evaluator selects winner |
| Stockout Predictor | Consumption rate + stock levels | Reorder alert + suggested qty | Human raises PR |
| Contract Expiry Forecaster | Contract dates + renewal history | Renewal risk dashboard | Procurement Admin acts |
| Savings Opportunity Detector | Price vs market benchmark | Savings potential % | Buyer negotiates |
| Budget Overrun Predictor | Committed spend vs budget | Overrun probability | Finance Admin reviews |
| Maverick Spend Identifier | POs vs PR linkage | Compliance breach report | Admin investigates |

---

## 8️⃣ INTEGRATION MAP

```
INBOUND (PROCUREMENT_AGENT CONSUMES):
  ├── Auth Service             → Identity, session, MFA validation
  ├── HR Service               → Org hierarchy, approval routing, L1–L7 limits
  ├── Finance/Budget Service   → Budget availability, cost centres, ledger codes
  ├── Tax Config Service       → GST rates, HSN/SAC codes, TDS sections
  ├── Compliance Module        → Blacklist check, MSME flag, e-invoice mandate
  ├── Notification Service     → Email/SMS/in-app alerts
  └── Vendor Portal (external) → Bids, invoices, profile updates

OUTBOUND (PROCUREMENT_AGENT PRODUCES):
  ├── Finance AP Service       → Payment advice for cleared invoices
  ├── Asset Management Service → Capex item tagging post-GRN
  ├── Analytics/ERP Dashboard  → Spend metrics, vendor scorecards, ROI
  ├── Audit Service            → Immutable procurement audit log stream
  ├── Notification Service     → Vendor alerts, approval notifications
  └── Compliance Audit Service → Breach alerts, statutory compliance feeds
```

---

## 9️⃣ TECH STACK (LOCKED — PLATFORM ARCHITECTURE INHERITED)

```
BACKEND:
  Language                = Node.js (TypeScript) / Go (matching + scoring engine)
  API Layer               = REST + GraphQL (internal gRPC)
  Auth                    = JWT (short-lived) + Refresh Token + TOTP MFA
  Message Broker          = Apache Kafka
  Cache                   = Redis (session, approval state, bid encryption)
  Primary DB              = PostgreSQL (all procurement entities)
  Search / Analytics      = Elasticsearch
  Object Storage          = S3-compatible (contracts, invoices, GRN docs)
  Encryption              = AES-256 (at rest) · TLS 1.3 (in transit)
  Document Signing        = DocuSign API / eSign integration
  GST Verification        = GSTIN API integration (auto-verify on onboarding)
  E-Invoice               = IRP (Invoice Registration Portal) API

FRONTEND:
  Mobile / Desktop        = Flutter (Android · iOS · Windows · macOS · Linux)
  Web                     = React Next.js (SEO read-only clone)
  Vendor Portal           = Flutter Web (isolated tenant, scoped auth)
  FLUTTER_INDEXING        = DISABLED (procurement = sensitive financial data)

INFRASTRUCTURE:
  Container               = Docker + Kubernetes
  Observability           = Prometheus + Grafana
  Secrets Management      = HashiCorp Vault
  CI / CD                 = GitLab CI / GitHub Actions
  Scheduler               = Kubernetes CronJob (contract expiry, stockout check)
```

---

## 🔟 UI / UX RULES (PROCUREMENT MODULE — STRICT)

```
DASHBOARD_COMPOSITION:
  FIXED (40%):
    - Identity block (name, role, department, cost centre)
    - Compliance status (POs without PR rate, MSME payment health)
    - Critical alerts (approvals pending, contract expiry imminent, disputes)
    - Trust badges (GST-verified vendors active count)

  CUSTOMISABLE (60%):
    - Widget order (spend trends, pending approvals, vendor scorecards)
    - Data density (compact / detailed view)
    - Quick actions (raise PR, view open POs, check vendor, download report)
    - Notification preferences per event type

THEME_RULES:
  GLOBAL_THEME             = ENABLED
  MODES                    = LIGHT + DARK
  DESIGN_LANGUAGE          = CLEAN | SOLID | FUNCTION-FIRST
  DECORATIVE_UI            = FORBIDDEN
  PROCUREMENT_UI_PALETTE   = Professional Slate + White + Accent-Amber

DOCUMENT_DISPLAY:
  - PO / Contract PDFs: watermarked (user_id + view_timestamp)
  - Bid amounts: masked during active auction (revealed on close only)
  - Vendor bank details: masked UI (last 4 digits, role-gated full reveal)
  - Contract value: visible only to L5+ and Procurement/Finance Admin
```

---

## 1️⃣1️⃣ SECURITY RULES (PROCUREMENT-SPECIFIC HARDENING)

```
DATA_CLASSIFICATION         = HIGHLY SENSITIVE (Financial + Commercial + Legal)
AUDIT_LOG_RETENTION         = 10 YEARS (statutory financial records)
CONTRACT_RETENTION          = PERPETUAL (legal requirement)
VENDOR_BANK_ENCRYPTION      = AES-256 (at rest + in transit)
PAN_DISPLAY_MASKING         = XXXXXXX{last4}{alpha} (UI)
GST_DISPLAY                 = Full (required for tax compliance display)
SESSION_TIMEOUT             = 20 MINUTES IDLE (procurement sessions)

FORBIDDEN OPERATIONS — HARD BLOCK:
  NO — Agent approves any PO, RFQ, or contract
  NO — Agent selects a vendor from RFQ bidding
  NO — Agent signs any contract
  NO — Agent triggers any vendor payment
  NO — Agent modifies vendor bank account details
  NO — Agent creates PO without a linked approved PR
  NO — Agent processes PO for blacklisted vendor
  NO — Agent processes cross-tenant procurement
  NO — Agent exports bulk contract data without Tenant Admin + OTP
  NO — Agent overrides three-way match dispute
  NO — Agent bypasses budget check

RATE LIMITING:
  - PO download: 20/hour per user
  - Bulk contract export: 1/day per admin (OTP-gated, audit-logged)
  - Vendor portal bid submit: 1 bid per RFQ line per session
  - API (external integrations): 200 req/min per tenant

BID INTEGRITY:
  - Bids encrypted (AES-256) from submission until close date
  - No bid amount visible to any internal user until bid_close_date passes
  - Bid reveal: system-automated, timestamped, audit-logged
  - Late bid: HARD BLOCK at the second bid_close_date passes (no human override)
```

---

## 1️⃣2️⃣ ERROR HANDLING & FAILURE MODES

```
FAILURE_MODE = STOP_EXECUTION
(Never proceed with partial, corrupt, or assumption-filled data)

ERROR CATALOGUE:

  E001 — Budget Check Service Unavailable
         BLOCK PR submission until service restored
         Alert requestor + Procurement Admin
         Do NOT proceed with assumptions on budget

  E002 — Vendor GST Verification API Timeout
         Mark vendor as PENDING_GST_VERIFY
         Block PO issuance to that vendor
         Alert Procurement Admin with manual verification option

  E003 — Minimum Bidder Threshold Not Met (RFQ)
         BLOCK RFQ from closing
         Alert Procurement Admin
         Extend bid period OR escalate to single-source justification workflow

  E004 — Three-Way Match Price Mismatch Detected
         HARD BLOCK on invoice clearance
         Raise dispute entity
         Alert Finance Admin + Procurement Admin
         Vendor notified of dispute raised

  E005 — Contract Expired — PO Attempted
         BLOCK PO creation
         Alert requestor + Procurement Admin
         Suggest renewal workflow

  E006 — MSME Vendor Payment Overdue >45 Days
         ESCALATION alert to Finance Admin + Tenant Admin
         Kafka: compliance.msme.payment_overdue
         Log to Compliance Audit Service
         No automatic payment (human must act)

  E007 — Kafka Event Delivery Failure
         Retry: 3 times with exponential backoff
         If persistent: alert Platform Admin
         Mark entity as EVENT_DELIVERY_FAILED for reconciliation

  E008 — PO Raised Without Linked PR (Maverick Spend Attempt)
         HARD BLOCK
         Alert Procurement Admin + Tenant Admin
         Log as COMPLIANCE_BREACH in audit log

  E009 — Vendor Blacklist Match Detected
         BLOCK all PO / RFQ / contract actions for that vendor
         Alert Procurement Admin + Compliance Officer
         Existing open POs with vendor → human review required

  E010 — E-Invoice IRN Generation Failed (Eligible Invoice)
         BLOCK three-way match clearance
         Alert Finance Admin
         Vendor notified to regenerate IRN
```

---

## 1️⃣3️⃣ STAGE COMPLIANCE (FOUR-STAGE MODEL INHERITED)

```
PROCUREMENT_AGENT DEPLOY GATES:

  STAGE 1 — MUST BE COMPLETE:
    Identity + Authentication + RBAC/ABAC + Core Data Models
    → BLOCKED if Stage 1 incomplete

  STAGE 2 — PARTIAL REQUIREMENT:
    Analytics Service active (for AI advisory functions)
    Predictive systems online (spend forecasting, vendor risk)

  STAGE 3 — PARTIAL REQUIREMENT:
    Corporate ERP layer active
    SME workflow module active
    Finance AP Service connected

  STAGE 4 — PARTIAL REQUIREMENT:
    Compliance Audit Service active
    Governance module active
    Multi-tenant scaling verified

SKIPPING STAGES = INVALID BUILD
DEPLOYMENT WITHOUT GATES CLEARED = STOP EXECUTION
```

---

## 1️⃣4️⃣ COMPLIANCE INHERITANCE (DO NOT DUPLICATE)

```
THIS PROMPT INHERITS — ALREADY FINALISED IN MASTER PROMPT:
  RBAC + ABAC Authorization framework
  Password security standards
  JWT Authentication + Refresh token policy
  TOTP MFA (mandatory for all procurement write ops)
  Session management + timeout rules
  AES-256 encryption at rest
  TLS 1.3 encryption in transit
  Hard tenant isolation architecture
  Domain isolation rules (Arts | Commerce | Science | Tech | Admin)
  Audit log immutability architecture
  Kafka event bus architecture
  Elasticsearch analytics layer
  Flutter (primary) + React Next.js (SEO web) UI rules
  Notification Service architecture
  Compliance module inheritance
  Four-stage development model enforcement
```

---

## 1️⃣5️⃣ GAMIFICATION / ENGAGEMENT LAYER (PROCUREMENT COMPLIANCE)

```
PLATFORM GAMIFICATION ENGINE INHERITED — APPLIED TO PROCUREMENT BEHAVIOUR

EMPLOYEE PROCUREMENT BADGES:
  "First PR"               — Raised first purchase requisition correctly
  "Budget Guardian"        — Raised PRs always within budget (FY)
  "Zero Maverick"          — No POs without PR in entire FY
  "Fast Approver"          — Approved PRs within 4 hours average (for managers)

PROCUREMENT ADMIN BADGES:
  "Clean Cycle"            — Zero compliance breaches in a quarter
  "Vendor Champion"        — Onboarded 10+ qualified vendors
  "Savings Hero"           — Achieved >10% savings vs budget in a quarter
  "SLA Master"             — All vendor SLAs met, zero overdue payments (quarter)

VENDOR PORTAL BADGES (Vendor-facing):
  "On-Time Performer"      — 95%+ on-time delivery over 12 months
  "Invoice Accuracy"       — 100% invoice match rate (no disputes) in a quarter
  "Preferred Vendor"       — Top performance grade (A) for 2 consecutive quarters

TRACKING:
  PostgreSQL: procurement_achievements (entity_id, entity_type, badge_id, awarded_at)
  entity_type: USER | VENDOR
  Linked to platform belt_progression and user_points tables
  Vendor scores fed back to vendor_master.performance_score
```

---

## 1️⃣6️⃣ SME-SPECIFIC PROCUREMENT WORKFLOW

```
SME_SCOPE = INCLUDED (as stated in SCOPE OF OPERATION)
SME_SIMPLIFICATION = FORBIDDEN (no hierarchy reduction)

SME PROCUREMENT DIFFERENCES vs CORPORATE:
  - Approval levels: simplified to 2 tiers (Requestor → Owner/Director)
  - PR mandatory: YES (even for SMEs — no bypass)
  - RFQ threshold: configurable (default ₹25,000 for SMEs vs ₹50,000 corporate)
  - Vendor minimum bidders: 2 (vs 3 for corporate)
  - Contract required: above ₹1,00,000 (vs ₹5,00,000 corporate)
  - All compliance rules: IDENTICAL (no relaxation)
  - Audit log: IDENTICAL (no relaxation)

SME_MODULE_FLAG = is_sme_tenant (boolean in tenant_master)
SME_ROUTING = conditional logic in approval matrix service
NO_SHARED_TENANT = SME tenants remain fully isolated
```

---

## 🔐 ANTIGRAVITY LOCK SEAL

```
╔══════════════════════════════════════════════════════════════════════════════╗
║                    🔒 PROMPT INTEGRITY CERTIFICATE                           ║
║                                                                              ║
║   AGENT            : PROCUREMENT_AGENT                                       ║
║   NAMESPACE        : antigravity.corp.erp.procurement                        ║
║   VERSION          : v1.0.0-SEALED                                           ║
║   PARENT_PLATFORM  : ECOSKILLER ENTERPRISE SAAS                              ║
║   LOCKED_BY        : ANTIGRAVITY_PLATFORM_CORE                               ║
║   SEALED_DATE      : 2026-02-24                                              ║
║   MUTATION_GATE    : ADD_ONLY                                                ║
║                      NO MODIFICATION WITHOUT PLATFORM ADMIN OVERRIDE         ║
║   INTEGRITY_CHECK  : SHA-256 of document must match stored hash              ║
║   BREACH_POLICY    : Any unauthorised edit = INVALIDATE + RE-SEAL REQUIRED   ║
║   CONFLICT_POLICY  : DENY                                                    ║
║   DUPLICATION      : FORBIDDEN                                               ║
║   STAGE_GATE       : STAGES 1–3 COMPLETE REQUIRED BEFORE DEPLOY              ║
║                                                                              ║
║   THIS PROMPT IS GRAVITY-LOCKED.                                             ║
║                                                                              ║
║   SPEND DOES NOT FLOAT.                                                      ║
║   VENDORS ARE NOT ASSUMED.                                                   ║
║   APPROVALS ARE NOT BYPASSED.                                                ║
║   CONTRACTS DO NOT DRIFT.                                                    ║
║   AUDIT TRAILS DO NOT BREAK.                                                 ║
║                                                                              ║
║   THE PROCUREMENT_AGENT EXECUTES WITH FULL PRECISION                        ║
║   OR IT DOES NOT EXECUTE AT ALL.                                             ║
╚══════════════════════════════════════════════════════════════════════════════╝
```

---

*PROCUREMENT_AGENT — ANTIGRAVITY CORPORATE ERP | Ecoskiller Enterprise SaaS*
*Document Version: v1.0.0 | Classification: INTERNAL — RESTRICTED*
*Inherits: MASTER_PROMPT_LOCK_v1 | © 2026 Ecoskiller*
