# 🔒 GST_CONNECT_AGENT — SEALED & LOCKED MASTER PROMPT
## Ecoskiller Enterprise SaaS · GST & Indirect Tax Intelligence Agent
### (ANTIGRAVITY · FLUTTER-FIRST · ENTERPRISE-GRADE · PRODUCTION LOCKED)

---

```
╔══════════════════════════════════════════════════════════════════════════════╗
║            GST_CONNECT_AGENT — EXECUTION CONTROL BLOCK                      ║
║  PROMPT_ID         = GST-AGENT-v1.0                                         ║
║  EXECUTION_ENGINE  = ANTIGRAVITY                                             ║
║  PROMPT_CLASS      = FINANCIAL_COMPLIANCE_AGENT_LOCK                        ║
║  MUTATION_POLICY   = ADD_ONLY                                                ║
║  ASSUMPTION_POLICY = FORBIDDEN                                               ║
║  CREATIVE_INTERP   = FORBIDDEN                                               ║
║  DEFAULT_BEHAVIOR  = DENY                                                    ║
║  FAILURE_MODE      = HARD_STOP                                               ║
║  IMPLICIT_BEHAVIOR = FORBIDDEN                                               ║
║  ARCHITECTURE_AUTH = PRE_APPROVED_ECOSKILLER_CONSTITUTION                   ║
║  INHERITANCE_FROM  = ECOSKILLER_MASTER_PROMPT_v1                            ║
║  SEALED            = TRUE                                                    ║
║  LOCKED            = TRUE                                                    ║
╚══════════════════════════════════════════════════════════════════════════════╝
```

> **⚠️ ANTIGRAVITY DIRECTIVE:** This agent prompt is a sealed financial compliance
> module grafted onto the Ecoskiller Master Constitution. Antigravity MUST NOT
> reinterpret, simplify, merge, reroute, or creatively extend any rule defined
> herein. Any deviation from the architecture below = STOP EXECUTION immediately.

---

## SECTION 0 — AGENT IDENTITY & PURPOSE LOCK

```
AGENT_NAME          = GST_CONNECT_AGENT
AGENT_TYPE          = AUTONOMOUS_TAX_COMPLIANCE_MICROAGENT
PARENT_PLATFORM     = ECOSKILLER (Enterprise Multi-Tenant SaaS)
DOMAIN_AUTHORITY    = INDIRECT TAX · GST · VAT · BILLING INTEGRATION
JURISDICTION_PRIMARY = INDIA (GST)
JURISDICTION_EXT    = EU (VAT) | GCC | US (Sales Tax) [future-extensible]
OPERATES_INSIDE     = BILLING & SUBSCRIPTION SERVICE (Port 8011)
GOVERNED_BY         = IVT-L1 MASTER LOCK (inherited)
INHERITS_SEALS      = AUTH-COMP-v1 | AUTHZ-MASTER-v1 | SESSION-COMP-v1 |
                      ENC-REST-v1 | ENC-TRANSIT-v1 | API-SEC-v1 |
                      RATE-LIMIT-v1 | ASC-U | TFC-U
AI_ROLE             = ADVISORY_ONLY
AI_APPROVAL         = FORBIDDEN
AI_OVERRIDE_HUMANS  = FORBIDDEN
```

**Purpose Statement (Non-Negotiable):**
The GST_CONNECT_AGENT is a sealed, deterministic indirect tax intelligence module embedded inside the Ecoskiller Billing & Subscription Service. It governs all GST/VAT/Sales Tax logic across every user group, tenant type, supply classification, and billing event on the platform. It does not replace human tax authority — it enforces, documents, computes, routes, and alerts so that human-authorized tax actions are always traceable, compliant, and audit-defensible.

---

## SECTION 1 — PLATFORM CONTEXT INHERITANCE (READ-ONLY)

This agent operates inside Ecoskiller — an Enterprise Multi-Tenant SaaS platform with the following fixed context:

**User Ecosystem (inherited, locked):**
- STUDENTS
- TRAINERS / MENTORS
- EVALUATORS
- INSTITUTES (Schools, Colleges, Universities)
- ENTERPRISES (SMEs + Corporates)
- RECRUITERS / HR
- ADMINS (Tenant / Platform / Compliance)
- PARENTS (Read-only trust layer)
- AUTOMATION / AI AGENTS

**Domain Tracks (inherited, locked):**
`Arts | Commerce | Science | Technology | Administration`

**Tenant Isolation Rules (inherited, locked):**
- Institute ≠ Company ≠ Platform
- Cross-domain tax data leakage = SECURITY FAILURE
- Tenant tax configurations are HARD ISOLATED — no shared GST logic across tenants

**Technology Stack (inherited, locked):**
```
PRIMARY_APP         = Flutter (Android | iOS | Desktop | Tablet)
WEB_VERSION         = React / Next.js (SEO Read-Only Clone)
BACKEND_BILLING     = Billing & Subscription Service @ http://billing-service:8011
EVENT_BUS           = Kafka / RabbitMQ
SEARCH              = Elasticsearch
STORAGE             = MinIO (invoices, tax documents, audit files)
OBSERVABILITY       = Prometheus + Loki/ELK + OpenTelemetry
PAYMENT_PRIMARY     = Stripe (provider abstraction required — swappable)
PAYMENT_INDIA       = Razorpay-compatible abstraction layer
```

**Development Stage Constraint:**
- Agent may only activate in STAGE 2 (Intelligence) and above
- Billing & tax UI is STAGE_3 output
- Compliance & audit tax layer is STAGE_4 output
- Skipping stages = INVALID BUILD

---

## SECTION 2 — INDIRECT TAX REGIME DECLARATION LOCK
### (Inherits: IVT-A)

```
REGIME_PRIMARY      = GST (India)
REGIME_SECONDARY    = VAT (EU / UK / GCC) [future-extensible]
REGIME_TERTIARY     = US Sales Tax [future-extensible]
MIXED_LOGIC         = FORBIDDEN within same transaction
UNDECLARED_REGIME   = BLOCK_BILLING
```

**Regime-per-Entity Rule:**
Every legal entity onboarded to Ecoskiller (Platform, Institute Tenant, Corporate Tenant, SME Tenant) MUST declare its applicable indirect tax regime before any taxable supply is permitted. Regime declaration is:
- Immutable per jurisdiction per billing period
- Change requires compliance admin authorization + audit log entry
- Stored in `entity_tax_profile` table with `regime_lock_flag = TRUE`

---

## SECTION 3 — GST REGISTRATION & AUTHORITY LOCK
### (Inherits: IVT-B)

### 3.1 Required Entity Fields

Every taxable entity on the platform MUST capture and verify:

| Field | Requirement | Validation Rule |
|---|---|---|
| `gstin` | Mandatory for India | 15-char alphanumeric, checksum verified |
| `pan_number` | Mandatory for India | 10-char, links to GSTIN |
| `registered_legal_name` | Mandatory | Must match GST portal record |
| `registered_address` | Mandatory | State code extracted for CGST/SGST/IGST split |
| `state_code` | Derived from GSTIN chars 1-2 | Auto-resolved |
| `jurisdiction_code` | Mandatory | ISO 3166-2 (IN-MH, IN-DL, etc.) |
| `registration_type` | REGULAR / COMPOSITION / UNREGISTERED | Determines tax collection obligation |
| `registration_verified_at` | Timestamp | Required before first taxable supply |
| `verification_source` | GST_PORTAL_API / MANUAL | Audit trail |

### 3.2 Verification Workflow (Locked)

```
1. Entity submits GSTIN during onboarding
2. System calls → GST Portal API (sandbox in dev, prod in prod)
3. API confirms: legal name, address, status (ACTIVE / CANCELLED / SUSPENDED)
4. If ACTIVE → registration_verified = TRUE, supply permitted
5. If CANCELLED / SUSPENDED → BLOCK_ALL_TAXABLE_SUPPLY
6. If API unreachable → HOLD_SUPPLY, alert compliance_admin, FIFO retry queue
7. Verification re-run at: monthly cadence + on any billing dispute
```

**Unverified GSTIN = TAX_DISABLE — zero taxable supplies permitted.**

### 3.3 GSTIN Format Validation (Hard Coded)

```regex
^[0-9]{2}[A-Z]{5}[0-9]{4}[A-Z]{1}[1-9A-Z]{1}Z[0-9A-Z]{1}$
```

Validation runs:
- At entity registration
- At every invoice generation (real-time)
- At monthly reconciliation batch
- At GST return filing pre-check

---

## SECTION 4 — SUPPLY CLASSIFICATION ENGINE LOCK
### (Inherits: IVT-C, IVT-D)

### 4.1 Supply Type Registry (Locked)

Every transaction on Ecoskiller maps to exactly ONE supply type. Classification is system-derived — never user-selected.

| Supply Code | Description | HSN/SAC Default | GST Rate | Exemption Check |
|---|---|---|---|---|
| `SVC_SUBSCRIPTION` | Platform subscription (Student/Trainer/Institute/Corporate) | SAC 998431 | 18% | No |
| `SVC_TRAINING` | Trainer-delivered skill course | SAC 999293 | 18% | Education exemption check |
| `SVC_CERTIFICATION` | Certificate issuance (Belt, Skill, Project) | SAC 999293 | 18% | Education exemption check |
| `SVC_PROJECT` | Project execution mentorship service | SAC 998399 | 18% | No |
| `SVC_JOB_PORTAL` | Recruiter/HR job posting access | SAC 998312 | 18% | No |
| `SVC_GD_SESSION` | Group Discussion (Dojo) session fee | SAC 999293 | 18% | Education exemption check |
| `SVC_COMMISSION` | Platform marketplace commission (trainer earnings) | SAC 998598 | 18% | No |
| `SVC_ERP_LICENSE` | Institute / Corporate ERP module license | SAC 998314 | 18% | No |
| `SVC_TOURNAMENT` | Dojo match / tournament entry fee | SAC 999692 | 18% | No |
| `FEE_PLACEMENT` | Placement / recruitment referral fee | SAC 998511 | 18% | No |
| `FEE_MENTOR_LICENSE` | Mentor license / verification fee | SAC 999293 | 18% | Education exemption check |
| `FEE_LATE_PAYMENT` | Late payment finance charge | SAC 997119 | 18% | No |
| `ZERO_RATED` | Export of services (non-India recipient) | As applicable | 0% | LUT required |
| `EXEMPT_EDU` | Pure educational services (if qualifying) | As applicable | NIL | Statutory reference mandatory |
| `RCM_APPLICABLE` | Reverse Charge Mechanism supply | Supply-specific | Recipient pays | Self-invoice required |

**Misclassification = MIS-TAXATION → SYSTEM_ERROR_STATE → Manual review escalation.**

### 4.2 Domain-Specific Taxability Lock (Inherits: IVT-D)

```
ARTS DOMAIN:
  - Training services            → SVC_TRAINING (18%)
  - Workshops & events           → SVC_GD_SESSION (18%)
  - Certifications               → SVC_CERTIFICATION (18%)
  - Royalties / licensing        → FEE_PLACEMENT (18%, specialist review)

COMMERCE DOMAIN:
  - Subscriptions                → SVC_SUBSCRIPTION (18%)
  - Platform commissions         → SVC_COMMISSION (18%)
  - Marketplace fees             → SVC_JOB_PORTAL (18%)
  - Refunds / chargebacks        → Reversal of original supply class

SCIENCE DOMAIN:
  - Research service agreements  → SVC_PROJECT (18%)
  - Research grants              → EXEMPT_EDU (if qualifying — evidence mandatory)
  - Certifications               → SVC_CERTIFICATION (18%)

TECHNOLOGY DOMAIN:
  - SaaS subscriptions           → SVC_SUBSCRIPTION (18%)
  - API access fees              → SVC_ERP_LICENSE (18%)
  - Software tooling             → SVC_ERP_LICENSE (18%)

CROSS-DOMAIN LEAKAGE = ERROR_STATE → EXECUTION_HALT
```

---

## SECTION 5 — PLACE OF SUPPLY DETERMINATION ENGINE LOCK
### (Inherits: IVT-E)

### 5.1 Place of Supply Rules (India GST)

The agent auto-resolves place of supply for every transaction:

```
RULE 1 — B2B (Registered Recipient):
  Place of Supply = Recipient's registered state
  → Compare supplier_state vs recipient_state
  → INTRA-STATE = CGST + SGST
  → INTER-STATE = IGST

RULE 2 — B2C (Unregistered / Consumer):
  Place of Supply = Location where service is performed/consumed
  → For online/digital services: Recipient's billing address state
  → Ambiguous location → BLOCK_INVOICE → Prompt recipient to confirm

RULE 3 — Export of Services (B2B, non-India recipient):
  Place of Supply = Outside India
  → ZERO_RATED supply
  → LUT (Letter of Undertaking) reference required
  → IGST = 0% on valid LUT

RULE 4 — Import / RCM:
  Place of Supply = Recipient's location (India)
  → Recipient self-assesses and pays under RCM
  → Self-invoice generated by platform
```

### 5.2 Place of Supply Data Capture

| Field | Mandatory | Source |
|---|---|---|
| `supplier_state_code` | YES | Entity GST profile |
| `supplier_gstin` | YES | Entity GST profile |
| `recipient_state_code` | YES | Billing address / GSTIN |
| `recipient_gstin` | Conditional (if B2B) | Entity profile |
| `pos_rule_applied` | YES | System-derived |
| `supply_direction` | `INTRA` / `INTER` / `EXPORT` / `RCM` | System-derived |

**Ambiguous place of supply = BLOCK_INVOICE. Human resolution required.**

---

## SECTION 6 — TAX RATE DETERMINATION ENGINE LOCK
### (Inherits: IVT-F)

### 6.1 Rate Determination Rules

```
RATE_SOURCE         = SYSTEM_RULE_ENGINE (not user input)
RATE_OVERRIDE       = AUDIT_FLAG (requires compliance admin sign-off)
RATE_VERSIONING     = ENABLED (effective_from / effective_to per rate)
RATE_TABLE_OWNER    = PLATFORM_COMPLIANCE_ADMIN only
```

### 6.2 GST Component Split Logic

```python
# Pseudocode — deterministic, no creative interpretation

def compute_tax_components(taxable_value, supply_direction, gst_rate):
    if supply_direction == "INTRA":
        cgst = taxable_value * (gst_rate / 2) / 100
        sgst = taxable_value * (gst_rate / 2) / 100
        igst = 0
    elif supply_direction == "INTER":
        igst = taxable_value * gst_rate / 100
        cgst = 0
        sgst = 0
    elif supply_direction == "EXPORT":
        igst = 0  # Zero-rated (LUT)
        cgst = 0
        sgst = 0
    elif supply_direction == "RCM":
        # Recipient computes and pays
        igst = taxable_value * gst_rate / 100
        # Stored as rcm_liability on recipient's ledger
        cgst = 0
        sgst = 0
    return {
        "taxable_value": taxable_value,
        "cgst": round(cgst, 2),
        "sgst": round(sgst, 2),
        "igst": round(igst, 2),
        "cess": 0,  # Cess logic if applicable
        "total_tax": round(cgst + sgst + igst, 2),
        "invoice_total": round(taxable_value + cgst + sgst + igst, 2)
    }
```

### 6.3 Cess Handling

```
CESS_APPLICABLE     = FALSE (default for SaaS services)
CESS_EXCEPTION      = Must be declared explicitly with statutory reference
CESS_SILENT_APPLY   = FORBIDDEN
```

---

## SECTION 7 — COMPLIANT TAX INVOICE GENERATION ENGINE LOCK
### (Inherits: IVT-G)

### 7.1 Invoice Mandatory Fields (GST Law)

Every invoice generated by GST_CONNECT_AGENT MUST contain all of the following — missing field = INVALID_INVOICE:

```
INVOICE MANDATORY FIELDS:
──────────────────────────────────────────────────────────
Field                           | Rule
──────────────────────────────────────────────────────────
invoice_number                  | Unique, sequential, non-repeating per FY
                                | Format: ECO/{FY}/{TENANT_CODE}/{SEQ}
                                | Example: ECO/2025-26/TEN001/00142
invoice_date                    | ISO 8601, timezone-explicit
invoice_type                    | TAX_INVOICE / CREDIT_NOTE / DEBIT_NOTE /
                                | PROFORMA / ADVANCE_RECEIPT / SELF_INVOICE
supplier_legal_name             | As per GST registration
supplier_gstin                  | 15-char verified
supplier_registered_address     | Full address with PIN
supplier_state_code             | 2-digit state code
recipient_name                  | Legal name of buyer / tenant
recipient_gstin                 | If B2B (mandatory for ITC)
recipient_address               | Billing address
recipient_state_code            | 2-digit derived state code
place_of_supply                 | State name + code
supply_description              | Human-readable service description
hsn_sac_code                    | Mandatory (SAC for services)
taxable_value                   | Pre-tax amount (INR, 2 decimal)
cgst_rate + cgst_amount         | If intra-state
sgst_rate + sgst_amount         | If intra-state
igst_rate + igst_amount         | If inter-state / export
total_tax_amount                | Sum of all tax components
total_invoice_value             | Taxable + Total Tax (INR, 2 decimal)
whether_gst_payable_on_rcm      | YES / NO boolean field (mandatory)
digital_signature               | Platform authorized signatory
qr_code                         | IRN-linked (e-invoice mandate if turnover ≥ ₹5Cr)
irn                             | Invoice Reference Number (e-invoice, if applicable)
──────────────────────────────────────────────────────────
```

### 7.2 E-Invoice Integration (IRP / NIC Portal)

```
E_INVOICE_THRESHOLD = ₹5 Crore annual turnover (as per current mandate)
IRP_ENDPOINT        = https://einvoice1.gst.gov.in (prod)
                      https://einvoice1-sandbox.nic.in (staging/dev)
WORKFLOW:
  1. Invoice JSON generated by GST_CONNECT_AGENT
  2. Submitted to IRP via signed API call
  3. IRP returns: IRN + Signed QR Code
  4. Platform embeds IRN + QR into final PDF invoice
  5. PDF stored in MinIO → path: /invoices/{tenant}/{FY}/{IRN}.pdf
  6. irn_generation event → Kafka topic: invoice.generated

FAILURE:
  IRP unreachable → HOLD_INVOICE → Retry queue (FIFO, max 3 retries)
  IRP validation error → INVOICE_BLOCKED → Alert billing_admin + tax_operator
  IRN mismatch → CRITICAL_AUDIT_FLAG
```

### 7.3 Invoice Numbering Sequence Rules

```
SEQUENCE_RESET      = Per Financial Year (April 1)
SEQUENCE_SCOPE      = Per Tenant (tenant-isolated)
GAPS_IN_SEQUENCE    = FORBIDDEN (audit trigger)
CANCELLATION        = Must generate Credit Note (cannot delete invoice)
DUPLICATION_CHECK   = SHA-256 hash of key fields, stored in invoice_hash_registry
```

### 7.4 Credit Note / Debit Note Rules

```
CREDIT_NOTE:
  - Linked to original_invoice_number (mandatory)
  - reason_for_credit (mandatory)
  - Tax reversal = proportional to original tax
  - Silent credit adjustment = ILLEGAL

DEBIT_NOTE:
  - Linked to original_invoice_number
  - Used for upward revision of value
  - Tax top-up computed and applied

STANDALONE_CREDIT_WITHOUT_ORIGINAL = BLOCK
```

---

## SECTION 8 — INPUT TAX CREDIT (ITC) GOVERNANCE LOCK
### (Inherits: IVT-H)

```
ITC_ELIGIBLE_SUPPLY     = B2B with valid GSTIN supplier + compliant invoice
ITC_INELIGIBLE_EXAMPLES = Employee welfare, personal consumption, blocked credits (Sec 17(5))
ITC_VERIFICATION_SOURCE = GSTR-2A / GSTR-2B reconciliation
UNSUBSTANTIATED_CREDIT  = DENY (hard block, not soft warning)
```

### 8.1 ITC Claim Workflow

```
1. Supplier invoice received (purchase/expense on platform)
2. GST_CONNECT_AGENT extracts: supplier GSTIN, invoice no., tax amount
3. Cross-reference against GSTR-2B (monthly auto-fetch from GST portal API)
4. If matched → ITC eligible, posted to itc_ledger
5. If not matched → ITC_PENDING, flagged for manual reconciliation
6. ITC reversal required if:
   - Payment not made to supplier within 180 days
   - Supply classified as blocked credit
7. ITC reversal recorded as journal entry with reason code
```

### 8.2 ITC Ledger Schema

```sql
CREATE TABLE itc_ledger (
    id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id           UUID NOT NULL,
    invoice_id          UUID NOT NULL REFERENCES tax_invoices(id),
    supplier_gstin      VARCHAR(15) NOT NULL,
    original_igst       NUMERIC(12,2) DEFAULT 0,
    original_cgst       NUMERIC(12,2) DEFAULT 0,
    original_sgst       NUMERIC(12,2) DEFAULT 0,
    itc_eligible_igst   NUMERIC(12,2) DEFAULT 0,
    itc_eligible_cgst   NUMERIC(12,2) DEFAULT 0,
    itc_eligible_sgst   NUMERIC(12,2) DEFAULT 0,
    itc_status          VARCHAR(20) CHECK (itc_status IN
                          ('ELIGIBLE','INELIGIBLE','PENDING','REVERSED','CLAIMED')),
    gstr2b_matched      BOOLEAN DEFAULT FALSE,
    gstr2b_period       VARCHAR(7),  -- YYYY-MM
    reversal_reason     TEXT,
    reversal_date       TIMESTAMP,
    created_at          TIMESTAMP NOT NULL DEFAULT now(),
    created_by          UUID NOT NULL,
    CONSTRAINT fk_tenant FOREIGN KEY (tenant_id) REFERENCES tenants(id)
);
```

---

## SECTION 9 — EXEMPTIONS & ZERO-RATING LOCK
### (Inherits: IVT-I)

```
IMPLICIT_EXEMPTION      = FORBIDDEN (must be declared with statutory reference)
EXEMPTION_CLAIM_ACTOR   = PLATFORM_COMPLIANCE_ADMIN only
```

### 9.1 Exemption Registry

| Exemption Code | Description | Statutory Reference | Evidence Required |
|---|---|---|---|
| `EDU_NIL_RATED` | Pure educational services — NIL rated | Notification 12/2017-CT(Rate) Entry 66 | Curriculum/accreditation proof |
| `EXPORT_LUT` | Export of services under LUT | IGST Act Sec 16(3)(a) | LUT reference number |
| `EXPORT_IGST_REFUND` | Export with IGST payment (refund route) | IGST Act Sec 16(3)(b) | Shipping bills / export evidence |
| `RCM_EXEMPT_UNREGISTERED` | Unregistered supplier below threshold | GST Rules | Threshold verification |

**Applying exemption without statutory reference + evidence = SYSTEM_ERROR → Compliance escalation.**

### 9.2 Education Exemption Decision Tree

```
Is the supply pure educational service?
├── YES: Is it provided by a recognized institution?
│   ├── YES: Is the course curriculum approved?
│   │   ├── YES → Apply EDU_NIL_RATED, log reference
│   │   └── NO  → 18% GST applies
│   └── NO  → 18% GST applies (platform as intermediary is taxable)
└── NO  → 18% GST applies (skill training, certification = taxable)
```

---

## SECTION 10 — REVERSE CHARGE MECHANISM (RCM) LOCK
### (Inherits: IVT-J)

```
RCM_CHECK_REQUIRED      = On every purchase transaction
RCM_TRIGGER_CONDITIONS  = Supplier is unregistered / notified category of service
RCM_ACTOR               = RECIPIENT (platform or tenant)
MISSED_RCM              = NON-COMPLIANT → Critical audit flag
```

### 10.1 RCM Workflow

```
1. Purchase event detected (trainer payment, vendor payment, etc.)
2. Agent checks supplier_registration_status
3. If UNREGISTERED:
   a. Flag transaction as RCM_APPLICABLE
   b. Generate self-invoice with rcm_flag = TRUE
   c. Compute GST as per applicable rate
   d. Post to rcm_liability_ledger (recipient's GST payable)
   e. Emit event: rcm.liability.created → Kafka
   f. Alert tax_operator for review
4. Payment of RCM liability must be in CASH LEDGER (not ITC)
5. RCM paid → entry in rcm_payment_ledger + challan reference stored
```

### 10.2 RCM Self-Invoice Schema

```sql
CREATE TABLE rcm_self_invoices (
    id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id           UUID NOT NULL,
    original_purchase_id UUID NOT NULL,
    unregistered_supplier_name VARCHAR(255) NOT NULL,
    supplier_pan        VARCHAR(10),
    supply_type         VARCHAR(50) NOT NULL,
    taxable_value       NUMERIC(12,2) NOT NULL,
    cgst_amount         NUMERIC(12,2) DEFAULT 0,
    sgst_amount         NUMERIC(12,2) DEFAULT 0,
    igst_amount         NUMERIC(12,2) DEFAULT 0,
    rcm_flag            BOOLEAN DEFAULT TRUE,
    payment_status      VARCHAR(20) CHECK (payment_status IN
                          ('PENDING','PAID','OVERDUE')),
    challan_reference   VARCHAR(100),
    payment_date        DATE,
    created_at          TIMESTAMP NOT NULL DEFAULT now(),
    created_by          UUID NOT NULL REFERENCES users(id)
);
```

---

## SECTION 11 — CROSS-BORDER & DIGITAL SUPPLY LOCK
### (Inherits: IVT-K)

```
CROSS_BORDER_DETERMINATION  = Auto-resolved from billing country code
CUSTOMER_LOCATION_EVIDENCE  = IP geolocation + billing address + payment country
NON_RESIDENT_REGISTRATION   = Required if platform supplies to foreign non-residents
UNTRACKED_CROSS_BORDER      = REGULATORY_RISK → ESCALATE
```

### 11.1 Cross-Border Tax Logic

```
RECIPIENT COUNTRY = INDIA:
  → Standard GST rules apply (Sections 3–10 of this document)

RECIPIENT COUNTRY = NON-INDIA (B2B):
  → Supply = Export of Services
  → ZERO_RATED under LUT (preferred)
  → OR IGST paid + refund claimed
  → Evidence: recipient country, payment in foreign currency / INR as per FEMA

RECIPIENT COUNTRY = NON-INDIA (B2C / Consumer):
  → OSS / non-resident registration rules of recipient country
  → VAT/GST of recipient country may apply
  → Platform legal must confirm obligation per country
  → Flag: cross_border_b2c_review_required = TRUE

CURRENCY HANDLING:
  → Functional currency = INR
  → FX rate source = RBI reference rate on invoice date
  → Translation vs remeasurement = DECLARED SEPARATELY
  → Implicit FX = ERROR
```

---

## SECTION 12 — BILLING ENGINE INTEGRATION LOCK
### (Inherits: IVT-L)

```
TAX_COMPUTATION     = PRE_PAYMENT (not post)
TAX_DISPLAY         = ITEMIZED_SEPARATELY (taxable value + each tax component)
POST_PAYMENT_TAX_MUTATION = FORBIDDEN
REFUND_TAX_REVERSAL = PROPORTIONAL to original supply (not flat)
BILLING_GATING      = No feature access without billing check middleware
```

### 12.1 Billing Event → Tax Trigger Map

| Billing Event (Kafka Topic) | GST Action |
|---|---|
| `subscription.created` | Compute GST, generate invoice draft |
| `subscription.renewed` | Compute GST, generate renewal invoice |
| `subscription.upgraded` | Compute incremental GST (debit note if needed) |
| `subscription.cancelled` | Prorate taxable value, credit note for unused period |
| `trainer.payout.requested` | Check commission GST, generate payout summary |
| `certification.purchased` | Compute GST (exemption check), generate invoice |
| `tournament.entry.paid` | Compute GST, generate invoice |
| `job.posting.paid` | Compute GST (SVC_JOB_PORTAL), generate invoice |
| `invoice.generated` | Trigger IRP for e-invoice (if applicable), store MinIO |
| `refund.approved` | Generate credit note, reverse tax proportionally |
| `rcm.liability.created` | Alert tax_operator, add to GST cash ledger queue |

### 12.2 Pre-Payment Tax Display (UI Contract)

The Flutter billing UI MUST display before payment confirmation:

```
┌─────────────────────────────────────────────────────┐
│  ORDER SUMMARY                                       │
├─────────────────────────────────────────────────────┤
│  Plan: Professional (Annual)                         │
│  Taxable Amount:              ₹10,000.00             │
│  CGST (9%):                   ₹   900.00             │
│  SGST (9%):                   ₹   900.00             │
│  ─── OR ───                                          │
│  IGST (18%):                  ₹ 1,800.00             │
├─────────────────────────────────────────────────────┤
│  TOTAL PAYABLE:               ₹11,800.00             │
├─────────────────────────────────────────────────────┤
│  GST will be charged as per applicable Indian laws.  │
│  [View full invoice preview]  [Pay Now]              │
└─────────────────────────────────────────────────────┘
```

**Hiding tax from pre-payment view = FORBIDDEN. Silent tax = ILLEGAL.**

---

## SECTION 13 — GST RETURN FILING ENGINE LOCK
### (Inherits: IVT-M)

```
RETURN_TYPE_PRIMARY     = GSTR-1 (Outward supplies) → Monthly / Quarterly
RETURN_TYPE_SECONDARY   = GSTR-3B (Summary return + tax payment) → Monthly
RETURN_TYPE_ANNUAL      = GSTR-9 (Annual reconciliation)
MISSING_PERIOD          = BLOCK_PERIOD_CLOSE
AUTO_FILING             = FORBIDDEN (human review + authorization mandatory)
```

### 13.1 GSTR-1 Data Preparation

GST_CONNECT_AGENT auto-prepares GSTR-1 data from invoice ledger:

```
TABLE_B2B       → B2B invoices (GSTIN-wise, invoice-wise)
TABLE_B2CL      → B2C Large (inter-state, value > ₹2.5L)
TABLE_B2CS      → B2C Small (intra-state / inter-state ≤ ₹2.5L, summarized by rate)
TABLE_CDNR      → Credit / Debit Notes to registered recipients
TABLE_CDNUR     → Credit / Debit Notes to unregistered
TABLE_EXP       → Export invoices
TABLE_AT        → Advances received (if applicable)
TABLE_NIL       → NIL rated / exempt / non-GST outward supplies
```

### 13.2 GSTR-3B Computation (Monthly)

```
OUTWARD_SUPPLIES_LIABILITY      → Sum from invoice ledger (GSTR-1 data)
INWARD_SUPPLIES_RCM             → Sum from rcm_liability_ledger
ITC_AVAILABLE                   → Sum from itc_ledger (GSTR-2B matched)
ITC_REVERSAL                    → Sum from itc_reversal_ledger
NET_TAX_PAYABLE                 → Liability - ITC Available + ITC Reversal

PAYMENT_ORDER (mandatory):
  1. IGST payable → IGST cash ledger
  2. CGST payable → CGST cash ledger
  3. SGST payable → SGST cash ledger
  Offset with ITC as per GST rules (IGST → IGST → CGST → SGST)
```

### 13.3 Filing Authorization Workflow

```
ROLE: tax_operator
  → Prepares GSTR-1 and GSTR-3B data
  → Reviews anomalies flagged by agent
  → Submits to tax_reviewer

ROLE: tax_reviewer
  → Validates all figures
  → Cross-checks invoice count vs ledger count
  → Approves for filing or returns with remarks

ROLE: authorized_signatory
  → DSC-signs / EVC-verifies on GST portal
  → Submits return
  → Stores acknowledgement (ARN) in gst_return_filings table

MAKER–CHECKER = MANDATORY
SINGLE_ACTOR_FILING = DENY (hard block)
```

### 13.4 GST Return Filing Schema

```sql
CREATE TABLE gst_return_filings (
    id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id           UUID NOT NULL,
    return_type         VARCHAR(20) NOT NULL,  -- GSTR1, GSTR3B, GSTR9
    period              VARCHAR(7) NOT NULL,   -- YYYY-MM or YYYY
    filing_status       VARCHAR(20) CHECK (filing_status IN
                          ('DRAFT','PREPARED','REVIEWED','FILED','ACKNOWLEDGED',
                           'LATE_FILED','AMENDED','BLOCKED')),
    prepared_by         UUID REFERENCES users(id),
    reviewed_by         UUID REFERENCES users(id),
    filed_by            UUID REFERENCES users(id),
    filing_timestamp    TIMESTAMP,
    arn                 VARCHAR(50),           -- Acknowledgement Reference Number
    total_outward_igst  NUMERIC(14,2) DEFAULT 0,
    total_outward_cgst  NUMERIC(14,2) DEFAULT 0,
    total_outward_sgst  NUMERIC(14,2) DEFAULT 0,
    total_itc_claimed   NUMERIC(14,2) DEFAULT 0,
    net_tax_paid        NUMERIC(14,2) DEFAULT 0,
    late_fee_paid       NUMERIC(12,2) DEFAULT 0,
    interest_paid       NUMERIC(12,2) DEFAULT 0,
    created_at          TIMESTAMP NOT NULL DEFAULT now(),
    CONSTRAINT unique_return UNIQUE (tenant_id, return_type, period)
);
```

---

## SECTION 14 — RECONCILIATION ENGINE LOCK
### (Inherits: IVT-N)

```
RECONCILIATION_SCOPE    = Invoice ↔ Ledger ↔ GSTR-1 ↔ GSTR-3B ↔ GSTR-2B
RECONCILIATION_FREQUENCY = Monthly (auto-triggered on period close)
MISMATCH_THRESHOLD      = ₹1.00 (any mismatch above ₹1 = ESCALATE)
SILENT_RECONCILIATION   = FORBIDDEN
```

### 14.1 Reconciliation Checks (Locked)

| Check ID | Check Description | On Mismatch |
|---|---|---|
| `REC-01` | GSTR-1 B2B invoice count vs invoice_ledger count | ESCALATE → tax_reviewer |
| `REC-02` | GSTR-1 total taxable value vs ledger sum | ESCALATE → tax_reviewer |
| `REC-03` | GSTR-1 total IGST vs ledger IGST sum | ESCALATE → tax_reviewer |
| `REC-04` | GSTR-2B ITC available vs itc_ledger eligible | ESCALATE → tax_operator |
| `REC-05` | GSTR-3B outward liability vs GSTR-1 total | ESCALATE → authorized_signatory |
| `REC-06` | GSTR-3B ITC claimed vs GSTR-2B matched | ESCALATE → tax_reviewer |
| `REC-07` | Tax paid via challan vs GSTR-3B net payable | ESCALATE → finance_manager |
| `REC-08` | RCM liability in ledger vs GSTR-3B RCM row | ESCALATE → tax_operator |
| `REC-09` | Credit notes in ledger vs GSTR-1 CDNR table | ESCALATE → tax_reviewer |
| `REC-10` | Annual GSTR-9 vs sum of monthly GSTR-3B | ESCALATE → authorized_signatory |

---

## SECTION 15 — PAYMENT, CHALLAN & CASH LEDGER LOCK
### (Inherits: IVT-O)

### 15.1 Tax Payment Workflow

```
1. GSTR-3B finalized → Net tax payable computed per head
2. Tax operator initiates challan creation on GST portal
3. Challan details stored: CIN, date, amount, tax_head
4. Payment made via net banking (GST PMT-06)
5. Payment confirmation received from bank
6. bank_receipt + CIN stored in tax_payments table
7. Challan reconciled against GSTR-3B net payable

UNLINKED_PAYMENT = INCOMPLETE (blocks period close)
```

### 15.2 Tax Payments Schema

```sql
CREATE TABLE tax_payments (
    id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id           UUID NOT NULL,
    period              VARCHAR(7) NOT NULL,
    tax_head            VARCHAR(10) CHECK (tax_head IN ('IGST','CGST','SGST','CESS')),
    challan_number      VARCHAR(50),
    cin                 VARCHAR(50),           -- Challan Identification Number
    amount              NUMERIC(14,2) NOT NULL,
    payment_date        DATE NOT NULL,
    bank_reference      VARCHAR(100),
    bank_receipt_path   TEXT,                  -- MinIO path
    gstr3b_reference    UUID REFERENCES gst_return_filings(id),
    status              VARCHAR(20) CHECK (status IN
                          ('PENDING','PAID','FAILED','RECONCILED')),
    created_by          UUID REFERENCES users(id),
    created_at          TIMESTAMP NOT NULL DEFAULT now()
);
```

---

## SECTION 16 — REFUNDS, CREDIT NOTES & ADJUSTMENTS LOCK
### (Inherits: IVT-P)

```
CREDIT_NOTE_RULE        = Must link to original invoice (mandatory)
REFUND_TAX_REVERSAL     = Proportional (not flat, not estimated)
SILENT_ADJUSTMENT       = ILLEGAL
REFUND_GST_TREATMENT    = GST refunded proportionally if original invoice had GST
```

### 16.1 Refund + Tax Reversal Computation

```python
# Pseudocode — deterministic

def compute_refund_tax_reversal(original_invoice, refund_amount):
    proportion = refund_amount / original_invoice.taxable_value
    return {
        "refund_taxable_value": round(refund_amount, 2),
        "cgst_reversal": round(original_invoice.cgst_amount * proportion, 2),
        "sgst_reversal": round(original_invoice.sgst_amount * proportion, 2),
        "igst_reversal": round(original_invoice.igst_amount * proportion, 2),
        "total_refund": round(refund_amount +
                              (original_invoice.total_tax * proportion), 2)
    }

# Credit note must be generated for every approved refund.
# No cash refund without credit note first.
```

### 16.2 Refund Abuse Controls (Inherited from Billing Abuse Lock)

```
REFUND_POLICY_ENFORCED  = Platform refund policy governs (not user-defined)
REFUND_ABOVE_LIMIT      = Dual approval required (finance_manager + entity_approver)
REFUND_FRAUD_SIGNAL     = Wallet freeze + compliance escalation
SILENT_REFUND           = FORBIDDEN
```

---

## SECTION 17 — AUDIT, NOTICE & INSPECTION LOCK
### (Inherits: IVT-Q)

### 17.1 GST Audit Trail Requirements

Every tax action MUST generate an immutable audit log entry:

```sql
CREATE TABLE gst_audit_log (
    id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id           UUID NOT NULL,
    event_type          VARCHAR(50) NOT NULL,
    -- Examples: INVOICE_GENERATED, CREDIT_NOTE_ISSUED, ITC_CLAIMED,
    --           ITC_REVERSED, RCM_SELF_INVOICE, RETURN_FILED,
    --           NOTICE_RECEIVED, CHALLAN_PAID, EXEMPTION_APPLIED,
    --           RATE_OVERRIDE_FLAGGED, RECONCILIATION_MISMATCH
    entity_reference_id UUID,                  -- invoice_id / return_id etc.
    actor_user_id       UUID REFERENCES users(id),
    actor_role          VARCHAR(50),
    action_detail       JSONB,                 -- Full event payload
    ip_address          INET,
    device_fingerprint  VARCHAR(255),
    immutable_hash      VARCHAR(64),           -- SHA-256 of event + timestamp
    created_at          TIMESTAMP NOT NULL DEFAULT now()
);

-- THIS TABLE IS APPEND-ONLY. NO UPDATE. NO DELETE. EVER.
-- Delete attempt = CRITICAL SECURITY INCIDENT
```

### 17.2 Notice Management Workflow

```
GST_NOTICE_RECEIVED:
  1. Notice logged in gst_notices table immediately
  2. Compliance admin alerted (Notification Service)
  3. Response deadline extracted + calendar alert set
  4. Draft response prepared by tax_reviewer
  5. Authorized signatory signs and submits response
  6. Response + acknowledgement stored in MinIO
  7. Status tracked: RECEIVED → IN_PROGRESS → RESPONDED → CLOSED / APPEALED

MISSED_NOTICE = CRITICAL_BREACH → Board-level escalation
```

---

## SECTION 18 — ROLE & AUTHORITY LOCK
### (Inherits: IVT-R, AUTHZ-MASTER-v1)

### 18.1 GST Role Definitions

| Role | Permissions |
|---|---|
| `tax_operator` | Create/prepare invoices, initiate reconciliation, view all tax data, prepare return data |
| `tax_reviewer` | Review + approve invoices, validate return data, approve ITC claims |
| `authorized_signatory` | Sign and file GST returns (DSC/EVC), authorize high-value adjustments |
| `finance_manager` | Approve large refunds, oversee challan payments, view consolidated reports |
| `platform_compliance_admin` | Configure tax rates, manage entity tax profiles, handle notices, manage exemptions |
| `entity_approver` | Approve entity-level tax configurations (Institute admin / Corporate admin) |
| `auditor` | Read-only access to all tax data including audit logs |

### 18.2 Maker–Checker Enforcement

```
MAKER–CHECKER = MANDATORY for:
  - GST return filing (prepare → review → file)
  - ITC claim above ₹10,000
  - Refund approval above ₹5,000
  - Exemption application
  - Tax rate override
  - Challan payment authorization
  - Notice response submission

SINGLE_ACTOR_ACTION_ON_ABOVE = DENY (hard block, not soft warning)
```

### 18.3 MFA Requirement (Inherited from MFA-COMP-v1)

```
MFA_REQUIRED_FOR:
  - GST return filing (any role)
  - Challan payment initiation
  - Exemption application
  - Rate override flagging
  - IRP/e-invoice credential access
  - Notice response submission
```

---

## SECTION 19 — AUTOMATION SAFETY & AI ADVISORY LOCK
### (Inherits: IVT-S)

```
AUTOMATION_MODE     = CONTROLLED_RULE_BASED_ONLY
HUMAN_OVERRIDE      = ALWAYS_AVAILABLE
BLIND_AUTOMATION    = FORBIDDEN
AI_FILES_RETURN     = FORBIDDEN
AI_APPROVES_ITC     = FORBIDDEN
AI_MODIFIES_TAX     = FORBIDDEN
```

### 19.1 What GST_CONNECT_AGENT MAY Do (Advisory & Preparatory)

```
✅ PERMITTED:
  - Compute tax components (deterministic rule engine)
  - Generate draft invoices
  - Prepare GSTR-1 / GSTR-3B data sets
  - Flag reconciliation mismatches
  - Alert tax_operator on anomalies
  - Auto-classify supply types (with human override available)
  - Determine place of supply
  - Validate GSTIN format
  - Check GSTIN status via GST portal API
  - Generate RCM self-invoice drafts
  - Retry failed IRP submissions (FIFO, max 3)
  - Emit Kafka events on tax lifecycle changes
  - Schedule return due-date reminders
  - Generate ITC reconciliation reports for review
```

### 19.2 What GST_CONNECT_AGENT MUST NOT Do

```
❌ FORBIDDEN:
  - File any GST return autonomously
  - Override a manually set tax rate without flagging + human approval
  - Claim ITC without GSTR-2B match confirmation
  - Issue credit notes without human approval
  - Approve refunds
  - Grant exemptions autonomously
  - Access IRP credentials directly (delegated to secure vault)
  - Modify closed financial periods
  - Delete or modify audit log entries
  - Send notices or communicate with GST authority autonomously
```

---

## SECTION 20 — DATABASE SCHEMAS (COMPLETE LOCKED SET)

### 20.1 Core Tax Invoice Table

```sql
CREATE TABLE tax_invoices (
    id                      UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id               UUID NOT NULL REFERENCES tenants(id),
    invoice_number          VARCHAR(50) NOT NULL UNIQUE,
    invoice_date            TIMESTAMP WITH TIME ZONE NOT NULL,
    invoice_type            VARCHAR(30) CHECK (invoice_type IN (
                              'TAX_INVOICE','CREDIT_NOTE','DEBIT_NOTE',
                              'PROFORMA','ADVANCE_RECEIPT','SELF_INVOICE')),
    -- Supplier
    supplier_legal_name     VARCHAR(255) NOT NULL,
    supplier_gstin          VARCHAR(15) NOT NULL,
    supplier_address        TEXT NOT NULL,
    supplier_state_code     VARCHAR(2) NOT NULL,
    -- Recipient
    recipient_name          VARCHAR(255) NOT NULL,
    recipient_gstin         VARCHAR(15),
    recipient_address       TEXT NOT NULL,
    recipient_state_code    VARCHAR(2) NOT NULL,
    -- Supply
    supply_description      TEXT NOT NULL,
    supply_type             VARCHAR(30) NOT NULL REFERENCES supply_type_registry(code),
    hsn_sac_code            VARCHAR(10) NOT NULL,
    place_of_supply_state   VARCHAR(2) NOT NULL,
    supply_direction        VARCHAR(10) CHECK (supply_direction IN
                              ('INTRA','INTER','EXPORT','RCM')),
    -- Values
    taxable_value           NUMERIC(14,2) NOT NULL,
    cgst_rate               NUMERIC(5,2) DEFAULT 0,
    cgst_amount             NUMERIC(12,2) DEFAULT 0,
    sgst_rate               NUMERIC(5,2) DEFAULT 0,
    sgst_amount             NUMERIC(12,2) DEFAULT 0,
    igst_rate               NUMERIC(5,2) DEFAULT 0,
    igst_amount             NUMERIC(12,2) DEFAULT 0,
    cess_amount             NUMERIC(12,2) DEFAULT 0,
    total_tax_amount        NUMERIC(12,2) GENERATED ALWAYS AS
                              (cgst_amount + sgst_amount + igst_amount + cess_amount) STORED,
    total_invoice_value     NUMERIC(14,2) GENERATED ALWAYS AS
                              (taxable_value + cgst_amount + sgst_amount + igst_amount + cess_amount) STORED,
    -- Compliance
    rcm_applicable          BOOLEAN NOT NULL DEFAULT FALSE,
    is_exempt               BOOLEAN NOT NULL DEFAULT FALSE,
    exemption_code          VARCHAR(30) REFERENCES exemption_registry(code),
    exemption_reference     TEXT,
    -- E-Invoice
    irn                     VARCHAR(64) UNIQUE,
    irn_generated_at        TIMESTAMP WITH TIME ZONE,
    qr_code_data            TEXT,
    einvoice_status         VARCHAR(20) DEFAULT 'NOT_REQUIRED',
    -- Storage
    pdf_path                TEXT,              -- MinIO path
    invoice_hash            VARCHAR(64) NOT NULL UNIQUE,
    -- Links
    original_invoice_id     UUID REFERENCES tax_invoices(id),  -- For CN/DN
    credit_debit_reason     TEXT,
    -- Status
    status                  VARCHAR(20) CHECK (status IN
                              ('DRAFT','ISSUED','CANCELLED','DISPUTED')),
    -- Audit
    created_by              UUID NOT NULL REFERENCES users(id),
    created_at              TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
    issued_at               TIMESTAMP WITH TIME ZONE,
    cancelled_at            TIMESTAMP WITH TIME ZONE,
    cancelled_by            UUID REFERENCES users(id),
    cancellation_reason     TEXT
);

CREATE INDEX idx_tax_invoices_tenant ON tax_invoices(tenant_id);
CREATE INDEX idx_tax_invoices_date ON tax_invoices(invoice_date);
CREATE INDEX idx_tax_invoices_gstin ON tax_invoices(supplier_gstin, recipient_gstin);
CREATE INDEX idx_tax_invoices_period ON tax_invoices(tenant_id, DATE_TRUNC('month', invoice_date));
```

### 20.2 Entity Tax Profile Table

```sql
CREATE TABLE entity_tax_profiles (
    id                      UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    entity_id               UUID NOT NULL UNIQUE,  -- tenant_id or user_id
    entity_type             VARCHAR(30) CHECK (entity_type IN
                              ('PLATFORM','INSTITUTE','CORPORATE','SME','TRAINER')),
    tax_regime              VARCHAR(20) CHECK (tax_regime IN
                              ('GST_INDIA','VAT_EU','SALES_TAX_US','NONE')),
    gstin                   VARCHAR(15),
    pan_number              VARCHAR(10),
    legal_name              VARCHAR(255),
    registered_address      TEXT,
    state_code              VARCHAR(2),
    jurisdiction_code       VARCHAR(10),
    registration_type       VARCHAR(20) CHECK (registration_type IN
                              ('REGULAR','COMPOSITION','UNREGISTERED')),
    is_verified             BOOLEAN DEFAULT FALSE,
    verified_at             TIMESTAMP WITH TIME ZONE,
    verification_source     VARCHAR(30),
    regime_lock_flag        BOOLEAN DEFAULT TRUE,
    effective_from          DATE NOT NULL,
    effective_to            DATE,
    created_by              UUID REFERENCES users(id),
    created_at              TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
    updated_at              TIMESTAMP WITH TIME ZONE
);
```

### 20.3 Supply Type Registry Table

```sql
CREATE TABLE supply_type_registry (
    code                    VARCHAR(30) PRIMARY KEY,
    description             TEXT NOT NULL,
    sac_hsn_default         VARCHAR(10),
    default_gst_rate        NUMERIC(5,2) NOT NULL,
    domain_restriction      VARCHAR(30),  -- NULL = all domains
    exemption_check_required BOOLEAN DEFAULT FALSE,
    is_active               BOOLEAN DEFAULT TRUE,
    effective_from          DATE NOT NULL,
    effective_to            DATE,
    created_at              TIMESTAMP NOT NULL DEFAULT now()
);
```

### 20.4 GST Tax Calendar Table

```sql
CREATE TABLE gst_tax_calendar (
    id                      UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id               UUID NOT NULL REFERENCES tenants(id),
    obligation_type         VARCHAR(30) NOT NULL,
    period                  VARCHAR(7) NOT NULL,
    due_date                DATE NOT NULL,
    materiality_level       VARCHAR(10) CHECK (materiality_level IN
                              ('LOW','MEDIUM','HIGH','CRITICAL')),
    penalty_exposure        NUMERIC(12,2),
    interest_exposure       NUMERIC(12,2),
    status                  VARCHAR(20) CHECK (status IN
                              ('UPCOMING','DUE','OVERDUE','COMPLETED','WAIVED')),
    completed_at            TIMESTAMP,
    CONSTRAINT unique_obligation UNIQUE (tenant_id, obligation_type, period)
);
```

---

## SECTION 21 — KAFKA EVENT CONTRACTS (LOCKED)

All tax-lifecycle events are published to Kafka. Consumers MUST NOT modify the event schema.

| Event Topic | Payload Keys | Trigger |
|---|---|---|
| `invoice.generated` | invoice_id, tenant_id, invoice_type, total_value, irn | Invoice issued |
| `invoice.cancelled` | invoice_id, tenant_id, cancellation_reason | Invoice cancelled |
| `credit_note.issued` | credit_note_id, original_invoice_id, tenant_id, reversal_amount | Credit note issued |
| `rcm.liability.created` | rcm_invoice_id, tenant_id, supplier_name, tax_amount | RCM self-invoice created |
| `gst.return.filed` | return_id, tenant_id, return_type, period, arn | Return filed |
| `gst.notice.received` | notice_id, tenant_id, due_date, notice_type | Notice logged |
| `itc.claimed` | itc_id, tenant_id, invoice_id, amount | ITC claim made |
| `itc.reversed` | itc_id, tenant_id, reversal_reason, amount | ITC reversed |
| `challan.paid` | payment_id, tenant_id, tax_head, amount | Tax paid |
| `reconciliation.mismatch` | check_id, tenant_id, period, variance_amount | Mismatch detected |
| `gstin.verification.failed` | entity_id, gstin, failure_reason | GSTIN check failed |

---

## SECTION 22 — API ENDPOINTS (LOCKED)

```
BASE_PATH = /api/v1/tax/gst

── INVOICE OPERATIONS ──
POST    /invoices/generate          → Generate tax invoice (returns draft)
POST    /invoices/{id}/issue         → Issue finalized invoice (triggers IRP)
POST    /invoices/{id}/cancel        → Cancel invoice (generates credit note)
GET     /invoices/{id}               → Get invoice detail
GET     /invoices                    → List invoices (filtered, paginated)
GET     /invoices/{id}/pdf           → Download invoice PDF (MinIO signed URL)

── CREDIT / DEBIT NOTES ──
POST    /credit-notes/generate       → Generate credit note (linked to original)
POST    /debit-notes/generate        → Generate debit note (linked to original)

── RCM ──
POST    /rcm/self-invoice            → Generate RCM self-invoice
GET     /rcm/liabilities             → List pending RCM liabilities

── ITC ──
GET     /itc/ledger                  → ITC ledger with GSTR-2B match status
POST    /itc/claim/{id}              → Claim ITC (requires reviewer approval)
POST    /itc/reverse/{id}            → Reverse ITC (with reason)

── RETURNS ──
GET     /returns/gstr1/prepare       → Prepare GSTR-1 dataset for period
GET     /returns/gstr3b/prepare      → Prepare GSTR-3B dataset for period
POST    /returns/gstr1/submit        → Submit GSTR-1 (maker role)
POST    /returns/gstr1/approve       → Approve GSTR-1 (checker role)
GET     /returns/history             → Filing history

── RECONCILIATION ──
POST    /reconciliation/run          → Trigger reconciliation for period
GET     /reconciliation/report       → Get reconciliation report

── ENTITY TAX PROFILE ──
POST    /entity/profile              → Create entity tax profile
GET     /entity/profile/{entity_id}  → Get entity tax profile
POST    /entity/profile/verify-gstin → Trigger GSTIN verification

── NOTICES ──
POST    /notices                     → Log received notice
GET     /notices                     → List all notices
PATCH   /notices/{id}/respond        → Submit response

── REPORTS ──
GET     /reports/tax-summary         → Period tax summary dashboard
GET     /reports/outstanding         → Outstanding liabilities
GET     /reports/itc-utilization     → ITC utilization report
GET     /reports/filing-calendar     → Upcoming filing obligations

ALL ENDPOINTS:
  → Authentication: JWT Bearer (inherited AUTH-COMP-v1)
  → Authorization: RBAC check (inherited AUTHZ-MASTER-v1)
  → Rate limiting: Inherited RATE-LIMIT-v1
  → MFA check on sensitive endpoints (inherited MFA-COMP-v1)
  → All requests logged to gst_audit_log
  → All responses TLS-encrypted (inherited ENC-TRANSIT-v1)
```

---

## SECTION 23 — UI SCREENS (FLUTTER — LOCKED)

Antigravity MUST generate these screens exactly. No addition, removal, or rearrangement.

### 23.1 Tax Dashboard (Compliance Admin / Finance Manager)

```
Screen: GSTDashboardScreen
Components:
  ├── FilingCalendarWidget     → Upcoming obligations with materiality color-coding
  ├── LiabilityOverviewWidget  → CGST / SGST / IGST outstanding this period
  ├── ITCBalanceWidget         → Available ITC by tax head
  ├── ReconciliationStatusWidget → Current period match/mismatch indicators
  ├── InvoiceVolumeChart       → Month-over-month invoice count + value
  ├── RecentFilingsWidget      → Last 3 filings with status badges
  └── NoticeAlertWidget        → Active GST notices requiring action
```

### 23.2 Invoice Generation Screen (Tax Operator)

```
Screen: GenerateInvoiceScreen
Form Fields:
  ├── RecipientSearchField     → Search by name / GSTIN (Elasticsearch)
  ├── SupplyTypePicker         → Dropdown from supply_type_registry (system-classified)
  ├── TaxableValueInput        → Numeric, INR
  ├── PlaceOfSupplyDisplay     → Auto-resolved (read-only)
  ├── TaxComponentsPreview     → Live-computed CGST/SGST/IGST breakdown
  ├── HSNSACDisplay            → Auto-resolved (read-only)
  ├── ExemptionToggle          → Only visible to compliance_admin; triggers evidence upload
  └── RCMToggle                → System-suggested; human confirms
Actions:
  ├── [Preview Invoice]        → Opens full invoice preview modal
  └── [Issue Invoice]          → Triggers IRP + Kafka event (requires reviewer in B2B)
```

### 23.3 GSTR-1 Preparation Screen (Tax Operator / Reviewer)

```
Screen: GSTR1PrepScreen
Components:
  ├── PeriodSelector           → Month/Quarter picker
  ├── B2BTable                 → GSTIN-wise, invoice-wise grid
  ├── B2CSTable                → Summarized by rate + state
  ├── CDNRTable                → Credit/Debit notes — registered
  ├── ExportTable              → Zero-rated invoices
  ├── NILTable                 → Exempt/NIL supplies
  ├── MismatchAlerts           → Highlighted reconciliation flags
  └── SubmitForReviewButton    → Moves to reviewer queue
```

### 23.4 ITC Reconciliation Screen (Tax Reviewer)

```
Screen: ITCReconciliationScreen
Components:
  ├── GSTR2BImportPanel        → Pull GSTR-2B from GST portal API
  ├── MatchedITCTable          → Invoices matched in GSTR-2B
  ├── UnmatchedITCTable        → Invoices not yet reflected (pending)
  ├── IneligibleITCTable       → Sec 17(5) blocked credits
  ├── ReversalTable            → Reversed ITC with reasons
  └── NetITCAvailableSummary   → By tax head
```

### 23.5 Notice Management Screen (Compliance Admin)

```
Screen: GSTNoticesScreen
Components:
  ├── NoticeListView           → All notices sorted by due date
  ├── NoticeDetailDrawer       → Full notice + metadata
  ├── ResponseDraftEditor      → Rich text editor for response
  ├── AttachmentUploader       → MinIO upload for evidence
  ├── StatusTimeline           → RECEIVED → IN_PROGRESS → RESPONDED → CLOSED
  └── EscalationButton         → Board-level escalation trigger
```

---

## SECTION 24 — OBSERVABILITY & ALERTING (LOCKED)

```
METRICS (Prometheus):
  gst_invoices_generated_total{tenant, supply_type}
  gst_invoices_irn_failed_total{tenant}
  gst_return_filing_latency_seconds{tenant, return_type}
  gst_reconciliation_mismatch_count{tenant, check_id}
  gst_itc_claimed_amount{tenant}
  gst_rcm_liability_outstanding{tenant}
  gst_notice_open_count{tenant}

ALERTS (Critical — PagerDuty / notification):
  - IRP unreachable for > 15 minutes
  - Reconciliation mismatch > ₹10,000
  - GST return due in < 48 hours (unfiled)
  - GSTIN verification failed for active tenant
  - Notice response due in < 72 hours
  - Missed RCM liability (> 30 days without payment)

DASHBOARDS (Grafana):
  - GST Invoice Flow Dashboard
  - Filing Status Dashboard
  - ITC Utilization Heatmap
  - Notice & Compliance Risk Dashboard
```

---

## SECTION 25 — RETENTION & AUDIT DEFENSE LOCK
### (Inherits: IVT-R, TFC-R)

```
INVOICE_RETENTION       = 8 years from date of issue (statutory + 1 year buffer)
AUDIT_LOG_RETENTION     = 10 years (immutable, append-only)
GST_RETURN_RETENTION    = 8 years
NOTICE_RECORDS          = 10 years
CHALLAN_RECORDS         = 8 years
STORAGE_BACKEND         = MinIO (encrypted at rest — AES-256)
LEGAL_HOLD_OVERRIDE     = Blocks all deletion for held records
MISSING_RECORDS         = LEGAL_FAILURE → Compliance admin escalation
```

---

## SECTION 26 — AGENT SEAL (INJECT INTO ANTIGRAVITY MASTER PROMPT)

```
══════════════════════════════════════════════════════════════════════
GST_CONNECT_AGENT — ANTIGRAVITY INJECTION SEAL
══════════════════════════════════════════════════════════════════════

GST_CONNECT_AGENT ENABLED                      = TRUE
INDIRECT TAX REGIME DECLARED                   = GST INDIA (PRIMARY)
GSTIN VERIFICATION ENFORCED                    = TRUE
SUPPLY CLASSIFICATION ENGINE                   = LOCKED (18 supply codes)
PLACE OF SUPPLY DETERMINATION                  = AUTO-RESOLVED
TAX RATE ENGINE                                = RULE-BASED, VERSION-CONTROLLED
COMPLIANT INVOICE GENERATION                   = LOCKED (all mandatory fields)
E-INVOICE / IRP INTEGRATION                    = ACTIVE (threshold-gated)
ITC GOVERNANCE                                 = GSTR-2B MATCHED ONLY
EXEMPTIONS & ZERO-RATING                       = EXPLICIT DECLARATION MANDATORY
RCM DETECTION                                  = AUTO-CHECKED PER TRANSACTION
CROSS-BORDER DIGITAL SUPPLY LOCK               = ACTIVE
BILLING ENGINE TAX INTEGRATION                 = PRE-PAYMENT ONLY
GSTR-1 PREPARATION                             = AUTO-PREPARED, HUMAN-FILED
GSTR-3B COMPUTATION                            = RULE-BASED, HUMAN-AUTHORIZED
RECONCILIATION ENGINE                          = 10-POINT AUTO-CHECK, MONTHLY
PAYMENT & CHALLAN TRACKING                     = FULLY LINKED
REFUND TAX REVERSAL                            = PROPORTIONAL, CREDIT-NOTE-LINKED
NOTICE MANAGEMENT                              = FULL LIFECYCLE TRACKED
MAKER–CHECKER ENFORCEMENT                      = MANDATORY (all tax actions)
MFA ON SENSITIVE TAX ACTIONS                   = ACTIVE
AI ADVISORY ONLY (NO AI FILING)                = ENFORCED
AUDIT LOG                                      = IMMUTABLE, APPEND-ONLY
RETENTION                                      = 8–10 YEARS ENFORCED
TENANT ISOLATION                               = HARD LOCKED
DOMAIN CROSS-CONTAMINATION                     = FORBIDDEN
OBSERVABILITY                                  = PROMETHEUS + GRAFANA ACTIVE

ANY TAX ACTION OUTSIDE THIS GOVERNANCE = PLATFORM-ILLEGAL STATE
DEVIATION FROM THIS SEAL = STOP EXECUTION IMMEDIATELY
══════════════════════════════════════════════════════════════════════
```

---

## SECTION 27 — DEPENDENCY MAP

```
GST_CONNECT_AGENT depends on (all must be operational):
  ├── Auth Service            → JWT validation on every API call
  ├── Authorization Service   → RBAC checks for tax roles
  ├── Billing Service (8011)  → Source of billing events + subscription state
  ├── User Service            → Actor identity for audit trails
  ├── Notification Service    → Alerts for mismatches, notices, due dates
  ├── MinIO                   → Invoice PDF storage, tax document vault
  ├── Kafka / RabbitMQ        → Event publishing (invoice.generated, etc.)
  ├── Elasticsearch           → Recipient search by GSTIN / name
  ├── Prometheus + Grafana    → Tax metrics and alerting
  ├── Admin Governance Service→ Dispute routing, compliance escalations
  └── External: GST Portal API (IRP, GSTR-2B, GSTIN verification)

GST_CONNECT_AGENT is consumed by:
  ├── Flutter App             → Tax dashboard, invoice download, filing screens
  ├── React Web (SEO)         → Read-only invoice lookup (public invoice verify)
  ├── Billing Service         → Tax computation on every payment event
  ├── Analytics Service       → Tax liability trends, ITC utilization reports
  └── Admin Governance        → Compliance dashboards, audit defense exports
```

---

## SECTION 28 — FAILURE MODE REGISTRY

| Failure Scenario | Agent Response | Human Action Required |
|---|---|---|
| IRP unreachable | Hold invoice, FIFO retry queue (max 3), alert billing_admin | Yes — manual IRP check |
| GSTIN verification API down | Hold supply, alert compliance_admin | Yes — manual GSTIN check |
| Reconciliation mismatch | Escalate with variance detail, block period close | Yes — tax_reviewer must resolve |
| GST return due, not filed | Critical alert 48h + 24h before due | Yes — tax_operator must file |
| Notice response overdue | Board-level escalation, CRITICAL flag | Yes — authorized_signatory |
| RCM liability >30 days unpaid | Block related entity billing, escalate | Yes — finance_manager |
| ITC claimed without GSTR-2B match | DENY ITC claim, flag as PENDING | Yes — tax_operator reconcile |
| Exemption applied without evidence | BLOCK_INVOICE, escalate to compliance_admin | Yes — evidence upload required |
| Duplicate invoice hash detected | BLOCK_ISSUANCE, alert tax_operator | Yes — deduplication review |
| Audit log deletion attempt | CRITICAL_SECURITY_INCIDENT, full system alert | Yes — security incident response |

---

```
╔══════════════════════════════════════════════════════════════════════════════╗
║  GST_CONNECT_AGENT — PROMPT END SEAL                                        ║
║                                                                              ║
║  PROMPT_ID         = GST-AGENT-v1.0                                         ║
║  SEALED            = TRUE                                                    ║
║  LOCKED            = TRUE                                                    ║
║  MUTATION_POLICY   = ADD_ONLY (additions require new versioned section)      ║
║  ANTIGRAVITY       = MUST TREAT THIS DOCUMENT AS IMMUTABLE SPECIFICATION    ║
║                                                                              ║
║  Any Antigravity output that:                                                ║
║  → Omits a mandatory invoice field          = INVALID BUILD                  ║
║  → Silently applies a tax exemption         = ILLEGAL                        ║
║  → Allows single-actor tax filing           = DENY                           ║
║  → Permits AI to approve / file tax actions = FORBIDDEN                      ║
║  → Cross-contaminates tenant tax data       = SECURITY FAILURE               ║
║  → Modifies audit log entries               = CRITICAL SECURITY INCIDENT     ║
║  → Deviates from this specification         = STOP EXECUTION                 ║
╚══════════════════════════════════════════════════════════════════════════════╝
```

---

*GST_CONNECT_AGENT.md — Ecoskiller Enterprise SaaS · Indirect Tax Intelligence Layer*
*Sealed for Google Antigravity Tool · Flutter-First · Production Grade · v1.0*
