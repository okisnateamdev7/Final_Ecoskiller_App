# 🔒 SEALED & LOCKED MASTER PROMPT
## ACCOUNTING_AGENT — ANTIGRAVITY CORPORATE ERP
### EcoSkiller Enterprise SaaS | Finance Intelligence Layer

---

```
╔══════════════════════════════════════════════════════════════════════════════╗
║              🛡️  ANTIGRAVITY LOCK PROTOCOL — LEVEL: MAXIMUM                ║
║                   AGENT: ACCOUNTING_AGENT v1.0.0                           ║
║                   MODULE: CORPORATE ERP — FINANCE LAYER                    ║
║                   CLASSIFICATION: SEALED | IMMUTABLE | PRODUCTION-LOCKED   ║
╚══════════════════════════════════════════════════════════════════════════════╝
```

---

## 🔐 EXECUTION CONTROL BLOCK (NON-NEGOTIABLE)

```yaml
AGENT_ID:                  ACCOUNTING_AGENT
AGENT_VERSION:             1.0.0
PARENT_PLATFORM:           ECOSKILLER — ENTERPRISE MULTI-TENANT SAAS
ERP_MODULE:                CORPORATE ERP — FINANCE & ACCOUNTS
LOCK_STATUS:               SEALED
MUTATION_POLICY:           ADD_ONLY (No deletions, no overrides)
CREATIVE_INTERPRETATION:   FORBIDDEN
ASSUMPTION_FILLING:        FORBIDDEN
DEFAULT_BEHAVIOR:          DENY
FAILURE_MODE:              STOP_EXECUTION + AUDIT_LOG
ROLLBACK_POLICY:           AUTO_ROLLBACK_ON_ANOMALY
AI_APPROVAL_AUTHORITY:     ZERO — AI ADVISES ONLY
HUMAN_OVERRIDE:            MANDATORY FOR ALL FINANCIAL MUTATIONS
```

---

## 1️⃣ AGENT IDENTITY (LOCKED)

```yaml
AGENT_NAME:        ACCOUNTING_AGENT
AGENT_TYPE:        AUTONOMOUS AI MICROSERVICE (Non-human executor)
DOMAIN:            Commerce | Finance | Corporate Accounting
SCOPE:             Corporate ERP Finance Layer within EcoSkiller Platform
PARENT_ERP:        ANTIGRAVITY CORPORATE ERP
TENANT_ISOLATION:  HARD — Cross-tenant data access = SECURITY FAILURE
DOMAIN_ISOLATION:  HARD — Finance domain ≠ HR domain ≠ Institute domain
```

### Agent Purpose Statement

> The ACCOUNTING_AGENT is the sealed, autonomous financial intelligence engine embedded within the ANTIGRAVITY Corporate ERP. It handles end-to-end accounting workflows — from invoice generation and ledger management to payroll computation, tax compliance, and financial audit trails — across all enterprise tenants within the EcoSkiller Multi-Tenant SaaS platform. It **NEVER** approves, blocks, or executes financial transactions autonomously. It **ALWAYS** presents computed outputs for human authorization before any mutation occurs.

---

## 2️⃣ PLATFORM INHERITANCE (MANDATORY — DO NOT DUPLICATE)

The ACCOUNTING_AGENT **MUST INHERIT** the following already-finalized platform-wide specifications without modification:

```
✅ RBAC + ABAC Authorization
✅ MFA (Multi-Factor Authentication)
✅ Password Security Policies
✅ Session Management
✅ Tenant Isolation (Hard boundary)
✅ Domain Isolation (Arts | Commerce | Science | Technology | Administration)
✅ Elasticsearch Search Engine
✅ Kafka Event Bus
✅ PostgreSQL Primary Database
✅ Flutter (Mobile + Desktop) — Primary UI
✅ React/Next.js (Web) — SEO Read-Only Clone
✅ Four-Stage Development Model (Foundation → Intelligence → Ecosystem → Compliance)
✅ AI Advises Only Policy
✅ Compliance & Audit ERP Layer
✅ Analytics & ROI Dashboards
```

---

## 3️⃣ USER ECOSYSTEM — ACCOUNTING CONTEXT (LOCKED)

| User Group | Accounting Access Level | Mutation Rights |
|---|---|---|
| **CORPORATE ADMIN** | Full read + approval authority | APPROVE/REJECT only |
| **CFO / FINANCE HEAD (L6–L7)** | Full ledger, payroll, tax, audit | APPROVE ONLY post-AI draft |
| **ACCOUNTS MANAGER (L4–L5)** | Invoice, receivables, payables | SUBMIT for approval |
| **ACCOUNTS EXECUTIVE (L2–L3)** | Data entry, expense logging | CREATE drafts only |
| **AUDITOR / COMPLIANCE ADMIN** | Read-only audit trail | ZERO mutation |
| **RECRUITER / HR** | Payroll read-only (own department) | ZERO mutation |
| **AUTOMATION / AI AGENTS** | Compute, draft, alert, report | ZERO approval authority |
| **INSTITUTES** | Institute billing view only | ZERO |
| **STUDENTS / TRAINERS** | Fee receipt view only | ZERO |
| **PARENTS** | Student fee status only | ZERO |

> **RULE:** No user bypasses human-in-the-loop for financial mutations. AI drafts → Human reviews → Human approves.

---

## 4️⃣ CORE ACCOUNTING MODULES (ALL REQUIRED)

### A. General Ledger Engine

```yaml
MODULE: GENERAL_LEDGER
STATUS: REQUIRED
RULES:
  - Double-entry bookkeeping enforcement = MANDATORY
  - Chart of Accounts = TENANT-CUSTOMIZABLE (within locked schema)
  - Journal entries = AI-drafted, human-approved
  - Period close = CFO-authorized only
  - Retroactive entries = AUDIT_FLAGGED + CFO approval required
  - Currency = MULTI-CURRENCY with real-time conversion
  - Fiscal Year = CONFIGURABLE per tenant
STORAGE:
  - PostgreSQL: ledger_accounts, journal_entries, account_balances, period_close_log
```

### B. Accounts Receivable (AR) Engine

```yaml
MODULE: ACCOUNTS_RECEIVABLE
STATUS: REQUIRED
RULES:
  - Invoice generation = AI-automated with human review gate
  - Invoice approval = ACCOUNTS_MANAGER or above
  - Overdue alerts = AI-triggered via Kafka event → Notification Service
  - Credit notes = CFO approval mandatory
  - Payment reconciliation = AI-matched, human-confirmed
  - Aging reports = Auto-generated (30/60/90/120+ days buckets)
WORKFLOWS:
  1. AI drafts invoice from contract/order data
  2. Accounts Executive reviews line items
  3. Accounts Manager approves
  4. Invoice dispatched (email/portal)
  5. Payment received → AI matches to invoice
  6. Accounts Manager confirms reconciliation
  7. Ledger updated → Kafka event fired
STORAGE:
  - PostgreSQL: invoices, invoice_line_items, payments, ar_aging, credit_notes
```

### C. Accounts Payable (AP) Engine

```yaml
MODULE: ACCOUNTS_PAYABLE
STATUS: REQUIRED
RULES:
  - Vendor onboarding = Compliance Admin verified
  - Purchase orders = 3-way match enforced (PO + Receipt + Invoice)
  - Payment runs = CFO authorized only
  - Duplicate payment detection = AI mandatory check
  - Vendor payment terms = LOCKED per contract
  - Early payment discounts = AI-flagged for CFO decision
WORKFLOWS:
  1. Vendor invoice received → OCR parsed by AI
  2. AI performs 3-way match check
  3. Mismatch → Flag to Accounts Manager
  4. Approved → Scheduled in payment run
  5. CFO authorizes payment run
  6. Payment executed → Ledger updated
STORAGE:
  - PostgreSQL: vendors, purchase_orders, vendor_invoices, payment_runs, ap_aging
```

### D. Payroll Engine

```yaml
MODULE: PAYROLL
STATUS: REQUIRED
RULES:
  - Salary computation = AI-computed, HR + CFO approved
  - CTC components = LOCKED per employment contracts
  - Tax deduction (TDS/PF/ESI) = REGULATORY_COMPLIANCE mandatory
  - Payslip generation = AI-automated post-approval
  - Payroll runs = Monthly cycle, CFO authorization mandatory
  - Off-cycle payments = CFO + Compliance Admin dual approval
  - Payroll audit trail = IMMUTABLE
  - Multi-country compliance = CONFIGURABLE per tenant
WORKFLOWS:
  1. HR inputs attendance + leave data
  2. AI computes gross, deductions, net pay
  3. HR Manager reviews computation
  4. CFO authorizes payroll run
  5. Payments dispatched via integrated banking
  6. Payslips auto-generated + distributed
  7. Statutory filings auto-drafted (TDS, PF, ESI)
STORAGE:
  - PostgreSQL: employees, salary_structures, payroll_runs, payslips, 
    statutory_filings, tax_deductions
```

### E. Expense Management Engine

```yaml
MODULE: EXPENSE_MANAGEMENT
STATUS: REQUIRED
RULES:
  - Expense claims = Employee-submitted, Manager-approved, Finance-processed
  - Receipt OCR = AI-mandatory for all claims above threshold
  - Policy violation detection = AI-flagged pre-submission
  - Advance settlements = Mandatory within 7-day policy window
  - Category limits = CONFIGURABLE per tenant, LOCKED per policy
  - Reimbursement = Included in next payroll run (default)
STORAGE:
  - PostgreSQL: expense_claims, expense_line_items, receipts, 
    advance_settlements, expense_policies
```

### F. Tax Compliance Engine

```yaml
MODULE: TAX_COMPLIANCE
STATUS: REQUIRED
RULES:
  - GST/VAT computation = AI-automated per jurisdiction
  - Tax return drafting = AI-generated, CFO + Tax Consultant approved
  - Filing deadlines = AI-calendar with escalating alerts
  - Tax audit support = Immutable evidence package generation
  - Transfer pricing = FLAGGED for external consultant review
  - Multi-jurisdiction = CONFIGURABLE per tenant geography
STORAGE:
  - PostgreSQL: tax_registrations, gst_returns, tax_payments, 
    tax_audit_log, jurisdiction_configs
```

### G. Financial Reporting Engine

```yaml
MODULE: FINANCIAL_REPORTING
STATUS: REQUIRED
REPORTS:
  - Profit & Loss Statement (Monthly, Quarterly, Annual)
  - Balance Sheet
  - Cash Flow Statement
  - Trial Balance
  - Accounts Aging Reports (AR + AP)
  - Departmental Cost Reports
  - ROI Dashboard per Business Unit
  - Recruiter Revenue Attribution Report
  - Institute Billing Summary
  - Cohort Revenue per Training Batch
RULES:
  - All reports = AI-generated, human-validated before external use
  - Board-level reports = CFO signed off mandatory
  - Real-time dashboards = Available to authorized roles
  - Historical data = IMMUTABLE (append-only)
```

### H. Audit & Compliance Engine

```yaml
MODULE: AUDIT_COMPLIANCE
STATUS: REQUIRED
RULES:
  - Every financial mutation = IMMUTABLE audit log entry
  - Audit trail includes: actor, timestamp, IP, device, before-state, after-state
  - External auditor access = Read-only, time-bounded, tenant-scoped
  - Compliance flags = Auto-raised, Compliance Admin resolved
  - Internal audit module = Sampling + full-scope modes
  - SOX / GAAP / IND-AS alignment = CONFIGURABLE per tenant
STORAGE:
  - PostgreSQL: financial_audit_log, compliance_flags, auditor_access_sessions
```

---

## 5️⃣ AI INTELLIGENCE LAYER (ADVISE ONLY — ZERO MUTATION AUTHORITY)

```yaml
AI_FUNCTIONS:
  - Invoice line-item extraction from unstructured documents (OCR)
  - 3-way PO/Receipt/Invoice matching
  - Duplicate payment detection
  - Expense policy violation detection
  - Payroll anomaly detection (salary spikes, ghost employees)
  - Cash flow forecasting (30/60/90 day projection)
  - Tax deadline alerting
  - Overdue invoice aging alerts
  - Financial fraud pattern detection
  - Budget vs actuals variance analysis
  - Revenue attribution per recruiter / batch / department
  - AI-generated management commentary for board reports

AI_HARD_RULES:
  - AI NEVER executes payment
  - AI NEVER approves invoice
  - AI NEVER files tax return
  - AI NEVER modifies ledger autonomously
  - AI outputs = DRAFT state always
  - Human authorization = MANDATORY for state change
```

---

## 6️⃣ DATA ARCHITECTURE (LOCKED)

### Core PostgreSQL Tables

```sql
-- General Ledger
ledger_accounts         (id, tenant_id, account_code, name, type, parent_id, is_active)
journal_entries         (id, tenant_id, entry_date, reference, description, status, approved_by, created_by)
journal_entry_lines     (id, journal_id, account_id, debit, credit, narration)
account_balances        (id, account_id, period, opening, debits, credits, closing)
period_close_log        (id, tenant_id, period, closed_by, closed_at, status)

-- Accounts Receivable
invoices                (id, tenant_id, invoice_no, customer_id, invoice_date, due_date, total, status, approved_by)
invoice_line_items      (id, invoice_id, description, quantity, unit_price, tax_rate, amount)
payments                (id, tenant_id, payment_date, invoice_id, amount, method, reference, reconciled_by)
credit_notes            (id, invoice_id, reason, amount, approved_by, created_at)
ar_aging                (id, tenant_id, customer_id, bucket_0_30, bucket_31_60, bucket_61_90, bucket_91_120, bucket_120_plus)

-- Accounts Payable
vendors                 (id, tenant_id, name, gst_no, bank_details, payment_terms, verified_by, status)
purchase_orders         (id, tenant_id, vendor_id, po_date, total, status, approved_by)
vendor_invoices         (id, po_id, vendor_id, invoice_no, amount, three_way_match_status, approved_by)
payment_runs            (id, tenant_id, run_date, total, authorized_by, status)
payment_run_items       (id, payment_run_id, vendor_invoice_id, amount)

-- Payroll
salary_structures       (id, employee_id, basic, hra, allowances, deductions, effective_from)
payroll_runs            (id, tenant_id, month, year, total_gross, total_deductions, total_net, authorized_by, status)
payslips                (id, payroll_run_id, employee_id, gross, deductions, net, payslip_url)
statutory_filings       (id, tenant_id, type, period, amount, filed_by, filed_at, status)

-- Expenses
expense_claims          (id, employee_id, submitted_at, total, status, approved_by)
expense_line_items      (id, claim_id, category, amount, description, receipt_url, policy_violation)

-- Tax
gst_returns             (id, tenant_id, period, type, total_tax, filed_by, filed_at, status)
tax_audit_log           (id, tenant_id, assessment_year, auditor, start_date, close_date, findings)

-- Audit
financial_audit_log     (id, tenant_id, actor_id, action, entity_type, entity_id, 
                          before_state jsonb, after_state jsonb, ip_address, device, timestamp)
compliance_flags        (id, tenant_id, flag_type, severity, entity_id, raised_at, resolved_by, status)
```

---

## 7️⃣ MICROSERVICE ARCHITECTURE (LOCKED)

```
ACCOUNTING_AGENT MICROSERVICE TOPOLOGY
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

[KAFKA EVENT BUS]
    │
    ├── accounting.invoice.created
    ├── accounting.payment.received
    ├── accounting.payroll.run.authorized
    ├── accounting.expense.approved
    ├── accounting.tax.deadline.alert
    ├── accounting.anomaly.detected
    └── accounting.period.close.initiated

[ACCOUNTING_AGENT CORE]
    │
    ├── Ledger Service
    ├── AR Service
    ├── AP Service
    ├── Payroll Service
    ├── Expense Service
    ├── Tax Compliance Service
    ├── Reporting Service
    └── Audit Service

[INTEGRATIONS — LOCKED]
    │
    ├── → Auth Service (session + RBAC validation)
    ├── → Notification Service (overdue, alerts, payslips)
    ├── → Analytics Service (ROI dashboards, revenue attribution)
    ├── → User Service (employee data, role hierarchy)
    ├── → Admin Governance Service (compliance flags escalation)
    ├── → Integration Hub (banking APIs, GST portal, email)
    └── → AI Engine (OCR, matching, forecasting, anomaly detection)
```

---

## 8️⃣ WORKFLOW EXECUTION RULES (LOCKED)

```
EVERY financial workflow MUST follow this execution chain:

  TRIGGER (User action / Scheduled / Kafka Event)
      │
      ▼
  VALIDATION (RBAC + ABAC + Tenant isolation check)
      │
      ▼
  AI COMPUTATION (Draft generation — zero authority)
      │
      ▼
  HUMAN REVIEW GATE (Mandatory — cannot be bypassed)
      │
      ▼
  HUMAN AUTHORIZATION (Role-based approval)
      │
      ▼
  STATE MUTATION (Database write)
      │
      ▼
  KAFKA EVENT (Downstream services notified)
      │
      ▼
  IMMUTABLE AUDIT LOG (financial_audit_log — append only)
      │
      ▼
  NOTIFICATION DISPATCH (Affected parties informed)

RULE: Any workflow that skips HUMAN REVIEW GATE = INVALID EXECUTION
RULE: Any workflow that skips AUDIT LOG = SECURITY FAILURE
```

---

## 9️⃣ UI / UX RULES — ACCOUNTING DASHBOARD (STRICT)

```yaml
PLATFORM_UI: FLUTTER (Primary — Mobile + Desktop)
WEB_UI: REACT/NEXT.JS (Read-only SEO clone only)

DASHBOARD_COMPOSITION:
  FIXED (40%):
    - Financial health scorecard (Cash, AR, AP, Payroll status)
    - Compliance indicator ribbon
    - Critical alerts (overdue, tax deadlines, anomalies)
    - Pending authorization queue
    - Audit trail access

  CUSTOMIZABLE (60%):
    - Widget positions
    - Report time ranges
    - Department filters
    - Dashboard KPI selections
    - Quick action shortcuts

THEME:
  MODES: LIGHT + DARK
  DESIGN_LANGUAGE: CLEAN | SOLID | FUNCTION-FIRST
  DECORATIVE_UI: FORBIDDEN
  COLOR_CODING:
    - GREEN  = Healthy / Paid / Compliant
    - AMBER  = Pending / Approaching Deadline
    - RED    = Overdue / Anomaly / Non-Compliant
    - BLUE   = In Progress / Under Review
```

---

## 🔟 DEVELOPMENT STAGE PLACEMENT (LOCKED)

```
ACCOUNTING_AGENT maps to the following platform development stages:

STAGE 1 — FOUNDATION:
  ✅ Identity & auth inheritance
  ✅ Core data models (ledger, AR, AP, payroll schemas)
  ✅ RBAC permission matrix for accounting roles

STAGE 2 — INTELLIGENCE:
  ✅ AI invoice parsing
  ✅ 3-way match engine
  ✅ Payroll anomaly detection
  ✅ Cash flow forecasting
  ✅ Predictive overdue detection

STAGE 3 — ECOSYSTEM:
  ✅ Full payroll cycle
  ✅ Multi-tenant accounting isolation
  ✅ Institute billing engine
  ✅ Recruiter revenue attribution
  ✅ SME vendor payment workflows

STAGE 4 — COMPLIANCE & SCALE:
  ✅ GST/Tax filing automation
  ✅ External audit module
  ✅ SOX/GAAP/IND-AS compliance layer
  ✅ Multi-jurisdiction tax engine
  ✅ Immutable audit trail hardening
```

---

## 1️⃣1️⃣ SECURITY & COMPLIANCE (HARD LOCK)

```yaml
AUTHENTICATION:        MFA mandatory for all accounting roles
SESSION_TIMEOUT:       15 minutes inactivity (accounting screens)
DATA_ENCRYPTION:       AES-256 at rest, TLS 1.3 in transit
FIELD_ENCRYPTION:      Bank details, salary data, tax numbers
AUDIT_LOG:             IMMUTABLE — append-only — zero deletion policy
ANOMALY_RESPONSE:      Auto-freeze flagged transaction + Compliance Admin alert
PII_HANDLING:          Salary data = RESTRICTED (HR + Finance only)
CROSS_TENANT:          ABSOLUTE ISOLATION — violation = INCIDENT
PENETRATION_TESTING:   Quarterly mandatory
BACKUP:                Daily encrypted backups — 7-year retention (compliance)
```

---

## 1️⃣2️⃣ KEY PERFORMANCE INDICATORS (AGENT SUCCESS METRICS)

| KPI | Target | Alert Threshold |
|---|---|---|
| Invoice Processing Time | < 2 hours | > 24 hours |
| Payment Run Accuracy | 100% | < 99.9% |
| Payroll Computation Accuracy | 100% | < 99.99% |
| Overdue Invoice Detection Latency | Real-time | > 1 hour |
| Tax Filing On-Time Rate | 100% | Miss = CRITICAL |
| Audit Trail Coverage | 100% | < 100% = FAILURE |
| Anomaly Detection False Positive Rate | < 2% | > 5% |
| Report Generation Time | < 30 seconds | > 2 minutes |
| System Uptime (Accounting Module) | 99.99% | < 99.9% |

---

## 1️⃣3️⃣ ANTIGRAVITY LOCK SEAL

```
╔══════════════════════════════════════════════════════════════════════════════╗
║                                                                              ║
║   🔒  ANTIGRAVITY SEAL — ACCOUNTING_AGENT v1.0.0                           ║
║                                                                              ║
║   This prompt is SEALED and LOCKED within the ANTIGRAVITY protocol.         ║
║                                                                              ║
║   MODIFICATION RULES:                                                        ║
║   • ADD_ONLY — New sections may be appended                                 ║
║   • NEVER delete or override existing rules                                  ║
║   • NEVER loosen security or compliance constraints                          ║
║   • NEVER grant AI autonomous financial execution rights                     ║
║   • NEVER remove human-in-the-loop authorization gates                       ║
║   • NEVER allow cross-tenant data access                                     ║
║   • NEVER allow domain boundary violations                                   ║
║                                                                              ║
║   INHERITANCE:                                                               ║
║   • Inherits ALL EcoSkiller platform-wide locked specs                       ║
║   • Inherits ALL Corporate ERP compliance rules                              ║
║   • Inherits ALL RBAC + ABAC authorization layers                            ║
║                                                                              ║
║   FAILURE PROTOCOL:                                                          ║
║   • On policy violation → STOP_EXECUTION                                    ║
║   • On anomaly detection → AUTO_FREEZE + AUDIT_LOG + ALERT                  ║
║   • On cross-tenant breach → INCIDENT_RESPONSE + LOCKDOWN                   ║
║                                                                              ║
║   SIGNED: ECOSKILLER PLATFORM ARCHITECTURE BOARD                            ║
║   CLASSIFICATION: PRODUCTION-LOCKED | IMMUTABLE | CONFIDENTIAL              ║
║                                                                              ║
╚══════════════════════════════════════════════════════════════════════════════╝
```

---

*Document Version: 1.0.0 | Module: ACCOUNTING_AGENT | Parent Platform: EcoSkiller Enterprise SaaS | ERP Layer: ANTIGRAVITY Corporate ERP | Status: SEALED & LOCKED*
