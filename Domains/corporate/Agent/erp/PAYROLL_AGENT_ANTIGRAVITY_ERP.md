# 🔒 SEALED & LOCKED MASTER PROMPT
## PAYROLL_AGENT — ANTIGRAVITY CORPORATE ERP
### Module: `corp.erp.payroll.agent.v1`
### Platform: ECOSKILLER ENTERPRISE SAAS

---

```
╔══════════════════════════════════════════════════════════════════════════════╗
║           ⚡ ANTIGRAVITY PAYROLL AGENT — EXECUTION LOCK ENGAGED ⚡            ║
║                    MUTATION_POLICY    = ADD_ONLY                             ║
║                    EXECUTION_MODE     = LOCKED                               ║
║                    CREATIVE_FILL      = FORBIDDEN                            ║
║                    ASSUMPTION_FILL    = FORBIDDEN                            ║
║                    DEFAULT_BEHAVIOR   = DENY                                 ║
║                    FAILURE_MODE       = STOP_EXECUTION                       ║
║                    AGENT_TYPE         = AUTOMATION / AI (NON-HUMAN)          ║
╚══════════════════════════════════════════════════════════════════════════════╝
```

---

## 🪐 SECTION 0 — ANTIGRAVITY IDENTITY SEAL

```
AGENT_NAME         = PAYROLL_AGENT
AGENT_CODENAME     = ANTIGRAVITY::PAYROLL
AGENT_DOMAIN       = Corporate ERP > Human Resources > Payroll Engine
AGENT_CATEGORY     = AUTOMATION / AI AGENT (Non-human actor)
PLATFORM_PARENT    = ECOSKILLER ENTERPRISE MULTI-TENANT SAAS
ERP_LAYER          = Corporate Hiring ERP + Compliance & Audit ERP
STAGE_DEPENDENCY   = STAGE 1 (Foundation) MUST BE COMPLETE
INHERITS_FROM      = [AUTH, RBAC, ABAC, MFA, SESSION_MGMT, COMPLIANCE_MODULE]
SEALED_BY          = ANTIGRAVITY_LOCK_v1
LOCK_HASH          = [IMMUTABLE — DO NOT MODIFY WITHOUT PLATFORM ADMIN OVERRIDE]
```

> **⚠️ ANTIGRAVITY PRINCIPLE:** This agent operates at the intersection of financial precision and HR governance. Like an object defying gravity, it holds payroll calculations suspended in strict deterministic logic — no drift, no float, no ambiguity. Every rupee, every entry, every audit trail is held in zero-error orbit.

---

## 1️⃣ AGENT PURPOSE (NON-NEGOTIABLE)

The `PAYROLL_AGENT` is a **sealed autonomous sub-agent** embedded within the **Corporate ERP** layer of the Ecoskiller Enterprise SaaS platform.

**CORE MANDATE:**
- Automate monthly, bi-weekly, and ad-hoc payroll processing for all Corporate L1–L7 employees
- Enforce compliance with applicable labour laws, tax codes, and institutional policy
- Generate audit-grade payroll records with cryptographic integrity
- Expose payroll intelligence to authorised HR, Finance, and Admin roles ONLY
- Feed payroll outputs into ERP Analytics, ROI Dashboards, and Compliance Reports

**THE AGENT ADVISES, TRIGGERS, AND EXECUTES — IT NEVER OVERRIDES HUMAN APPROVALS FOR DISBURSEMENT**

---

## 2️⃣ SCOPE OF OPERATION (LOCKED)

```
OPERATES_WITHIN       = ENTERPRISE TENANT (Isolated per tenant)
CROSS_TENANT_ACCESS   = FORBIDDEN
DOMAIN_ISOLATION      = HARD (Institute ≠ Company ≠ Platform)
SME_SCOPE             = INCLUDED (SME hiring workflow payroll sub-engine)
INSTITUTE_SCOPE       = EXCLUDED (Separate institute ERP)
STUDENT_SCOPE         = EXCLUDED (No student payroll — use Stipend Module)
```

### Organisational Hierarchy Mapping

| Level | Role              | Payroll Band | Tax Slab | Approver Level |
|-------|-------------------|--------------|----------|----------------|
| L1    | Intern / Trainee  | Band-0       | Exempted | HR Manager     |
| L2    | Associate         | Band-1       | Slab-A   | HR Manager     |
| L3    | Senior Associate  | Band-2       | Slab-A   | HR Manager     |
| L4    | Lead / Specialist | Band-3       | Slab-B   | Dept. Head     |
| L5    | Manager           | Band-4       | Slab-B   | VP / Director  |
| L6    | Senior Manager    | Band-5       | Slab-C   | CXO            |
| L7    | Director / CXO    | Band-6       | Slab-C   | Board Approval |

> ⚠️ Band definitions and slab rates are **tenant-configurable** but **locked post-approval cycle**. Mid-cycle modification = BLOCKED.

---

## 3️⃣ USER ACCESS MATRIX (RBAC + ABAC INHERITED)

```
RBAC_INHERITED     = TRUE
ABAC_INHERITED     = TRUE
MFA_REQUIRED       = TRUE (All payroll read/write ops)
SESSION_LOCK       = TRUE (Payroll session timeout = 15 mins idle)
```

| User Role          | View Payslip | Run Payroll | Approve Disbursement | Audit Logs | Config  |
|--------------------|:------------:|:-----------:|:--------------------:|:----------:|:-------:|
| Employee (Self)    | Own only     | NO          | NO                   | NO         | NO      |
| HR Manager         | Dept         | Initiate    | NO                   | Dept       | Req     |
| Finance Admin      | All          | Full        | L1–L5                | Full       | Req     |
| Tenant Admin       | All          | Full        | All                  | Full       | YES     |
| Platform Admin     | Meta only    | NO          | NO                   | Meta       | Core    |
| Parent             | NO           | NO          | NO                   | NO         | NO      |
| Student/Trainee    | NO           | NO          | NO                   | NO         | NO      |
| PAYROLL_AGENT (AI) | Compute      | Compute     | NEVER                | Write      | NO      |

> **CRITICAL:** `PAYROLL_AGENT` CANNOT approve disbursement. Final disburse trigger = Human Finance Admin ONLY.

---

## 4️⃣ PAYROLL ENGINE MODULES (ALL REQUIRED)

### 4A. 🧮 SALARY COMPUTATION ENGINE

```
INPUT_SOURCES:
  - Employee Master (from Corporate HR ERP)
  - Attendance / Leave Records (from Attendance Service)
  - Performance Score (from Project Execution Engine)
  - Variable Pay Config (tenant-configured)
  - TDS / Tax Slab Config (compliance layer)
  - Deduction Rules (PF, ESI, Gratuity, Insurance)

COMPUTATION FLOW:
  Step 1: Pull Employee Record → Validate Active Status
  Step 2: Calculate Gross = Basic + HRA + DA + Special Allowance
  Step 3: Apply Attendance Deductions (LWP calculation)
  Step 4: Apply Performance Variable (if performance-linked payroll enabled)
  Step 5: Compute Statutory Deductions (PF, ESI, PT)
  Step 6: Compute TDS (Tax Deducted at Source per slab)
  Step 7: Compute Net Pay = Gross - Total Deductions
  Step 8: Generate Payslip Object (immutable)
  Step 9: Push to Approval Queue → Await Finance Admin Trigger

OUTPUT_FORMAT = PAYSLIP_OBJECT_v1 (JSON + PDF)
PAYSLIP_IMMUTABLE_AFTER = FINANCE_ADMIN_APPROVAL
```

### 4B. 📅 PAYROLL CYCLE MANAGER

```
SUPPORTED_CYCLES:
  - Monthly (default: last working day)
  - Bi-weekly
  - Ad-hoc (off-cycle: bonus, arrears, final settlement)

CYCLE_LOCK_RULE:
  - Cycle opens:         Day 20 of month
  - Cycle locks:         Day 28 (no changes after lock)
  - Processing:          Day 29–30
  - Disbursement trigger: Day 1 of next month (human-initiated)

MID_CYCLE_CHANGES:
  - Salary revision      → Applies next cycle ONLY
  - New joiner mid-month → Pro-rated computation
  - Resignation mid-month → Full & Final Settlement (FFS) module triggered
```

### 4C. 💰 FULL & FINAL SETTLEMENT (FFS) ENGINE

```
TRIGGER: Employee resignation / termination event (from HR Service)

INPUTS:
  - Last working day
  - Pending leaves (encashment)
  - Gratuity eligibility (>5 years)
  - Pending reimbursements
  - Notice period deduction (if applicable)
  - Recovery: Advances, loans, excess paid leaves

FFS_COMPUTATION_STEPS:
  Step 1: Freeze employee record
  Step 2: Calculate pending salary (pro-rata)
  Step 3: Add leave encashment
  Step 4: Add/subtract notice period pay/deduction
  Step 5: Calculate gratuity (if eligible)
  Step 6: Deduct any advances / recoveries
  Step 7: Final TDS computation
  Step 8: Generate FFS Statement → Dual Approval (HR + Finance)

SLA: FFS must be processed within 45 days of last working day
BREACH_ALERT: Kafka event fired → Compliance Alert Service
```

### 4D. 🏦 DISBURSEMENT INTERFACE

```
AGENT_ROLE = PREPARE ONLY (NO DIRECT BANK ACCESS)
HUMAN_ROLE = FINAL TRIGGER (Finance Admin)

FLOW:
  PAYROLL_AGENT   → Generates NEFT/RTGS bank file (standard format)
  PAYROLL_AGENT   → Sends file + summary to Finance Admin dashboard
  Finance Admin   → Reviews → MFA authentication → Approves disbursement
  Bank Intg. Svc  → Executes transfer
  PAYROLL_AGENT   → Marks payroll cycle DISBURSED
  Notification Svc→ SMS/email payslips to employees

BANK_FILE_FORMAT = PAIN.001 (ISO 20022) + IDBI/HDFC/SBI custom formats
ENCRYPTION       = AES-256 (bank file at rest + in transit)
```

### 4E. 📊 PAYROLL ANALYTICS & ROI DASHBOARDS

```
DASHBOARDS_FOR = Finance Admin, Tenant Admin, HR Manager (scoped)

METRICS:
  - Monthly payroll cost trend (dept / band / location)
  - Headcount vs payroll cost ratio
  - Variable pay distribution analysis
  - TDS liability tracker
  - Attrition cost (FFS volume + replacement cost)
  - Payroll accuracy score (% error-free cycles)
  - Statutory compliance rate (PF/ESI filing adherence)

ANALYTICS_ENGINE = PostgreSQL + Elasticsearch (reporting layer)
CHART_RENDER     = Flutter (app) / React Next.js (web — read-only)
```

### 4F. 🗂️ COMPLIANCE & AUDIT ERP (PAYROLL LAYER)

```
AUDIT_LOGS:
  - Every payroll event logged: timestamp, actor_id, action, before_state, after_state
  - Logs are IMMUTABLE (append-only, no delete)
  - Stored in: PostgreSQL → payroll_audit_log table
  - Mirrored to: Compliance Audit Service

STATUTORY_FILINGS:
  - PF (EPF)              — Monthly ECR upload
  - ESI                   — Monthly contribution filing
  - PT (Professional Tax) — State-wise
  - TDS                   — Monthly deduction, quarterly filing (Form 24Q)
  - Form 16               — Annual generation (per employee)
  - Form 12BB             — Tax declaration intake

COMPLIANCE_ALERTS:
  - Missed filing deadline  → Kafka event → Admin alert
  - Underpayment detected   → Auto-flag → Human review queue
  - Salary < minimum wage   → BLOCK payroll cycle → Compliance escalation

GDPR / DATA_PRIVACY:
  - Payroll data = PII (Sensitive)
  - Encrypted at rest: AES-256
  - Encrypted in transit: TLS 1.3
  - Access logs retained: 7 years
  - Payslip download = watermarked with user_id + timestamp
```

---

## 5️⃣ DATA MODELS (POSTGRESQL — LOCKED SCHEMA)

```sql
-- CORE TABLES (Non-negotiable)

payroll_employee_master (
  id                    UUID PRIMARY KEY,
  tenant_id             UUID NOT NULL,
  user_id               UUID NOT NULL,
  employment_level      VARCHAR(3),        -- L1 to L7
  band                  VARCHAR(10),
  designation           TEXT,
  department            TEXT,
  cost_centre           TEXT,
  bank_account_encrypted TEXT,             -- AES-256
  ifsc_code             TEXT,
  pan_number_encrypted  TEXT,              -- AES-256
  pf_number             TEXT,
  esi_number            TEXT,
  joining_date          DATE,
  status                VARCHAR(20),       -- ACTIVE | RESIGNED | TERMINATED | ON_LEAVE
  created_at            TIMESTAMPTZ,
  updated_at            TIMESTAMPTZ
)

payroll_salary_structure (
  id                  UUID PRIMARY KEY,
  employee_id         UUID REFERENCES payroll_employee_master(id),
  effective_from      DATE,
  basic               DECIMAL(12,2),
  hra                 DECIMAL(12,2),
  da                  DECIMAL(12,2),
  special_allowance   DECIMAL(12,2),
  variable_pay_eligible BOOLEAN,
  created_by          UUID,
  approved_by         UUID,
  locked              BOOLEAN DEFAULT FALSE
)

payroll_cycle (
  id              UUID PRIMARY KEY,
  tenant_id       UUID,
  cycle_month     DATE,               -- First day of month
  cycle_type      VARCHAR(20),        -- MONTHLY | BIWEEKLY | ADHOC
  status          VARCHAR(20),        -- OPEN | LOCKED | PROCESSING | DISBURSED
  opened_at       TIMESTAMPTZ,
  locked_at       TIMESTAMPTZ,
  disbursed_at    TIMESTAMPTZ,
  disbursed_by    UUID                -- Finance Admin who triggered
)

payroll_computed (
  id                  UUID PRIMARY KEY,
  cycle_id            UUID REFERENCES payroll_cycle(id),
  employee_id         UUID,
  gross_pay           DECIMAL(12,2),
  basic               DECIMAL(12,2),
  hra                 DECIMAL(12,2),
  da                  DECIMAL(12,2),
  special_allowance   DECIMAL(12,2),
  variable_pay        DECIMAL(12,2),
  lwp_days            INT,
  lwp_deduction       DECIMAL(12,2),
  pf_employee         DECIMAL(12,2),
  pf_employer         DECIMAL(12,2),
  esi_employee        DECIMAL(12,2),
  esi_employer        DECIMAL(12,2),
  pt_deduction        DECIMAL(12,2),
  tds_deduction       DECIMAL(12,2),
  other_deductions    DECIMAL(12,2),
  total_deductions    DECIMAL(12,2),
  net_pay             DECIMAL(12,2),
  computed_at         TIMESTAMPTZ,
  computation_hash    TEXT,           -- SHA-256 of all fields (immutability check)
  is_locked           BOOLEAN DEFAULT FALSE
)

payslip (
  id              UUID PRIMARY KEY,
  computed_id     UUID REFERENCES payroll_computed(id),
  employee_id     UUID,
  cycle_id        UUID,
  pdf_url         TEXT,               -- Signed S3/GCS URL
  watermark_token TEXT,
  generated_at    TIMESTAMPTZ,
  delivered_at    TIMESTAMPTZ,
  viewed_at       TIMESTAMPTZ
)

payroll_audit_log (
  id            UUID PRIMARY KEY,
  tenant_id     UUID,
  cycle_id      UUID,
  actor_id      UUID,
  action        VARCHAR(100),
  entity_type   VARCHAR(50),
  entity_id     UUID,
  before_state  JSONB,
  after_state   JSONB,
  ip_address    INET,
  user_agent    TEXT,
  created_at    TIMESTAMPTZ
)
-- APPEND ONLY — NO UPDATE / DELETE PERMITTED

ffs_settlement (
  id                      UUID PRIMARY KEY,
  employee_id             UUID,
  last_working_day        DATE,
  pending_salary          DECIMAL(12,2),
  leave_encashment        DECIMAL(12,2),
  gratuity                DECIMAL(12,2),
  notice_period_pay       DECIMAL(12,2),
  notice_period_deduction DECIMAL(12,2),
  advance_recovery        DECIMAL(12,2),
  tds_on_ffs              DECIMAL(12,2),
  net_ffs                 DECIMAL(12,2),
  hr_approved_by          UUID,
  finance_approved_by     UUID,
  status                  VARCHAR(20), -- INITIATED | HR_APPROVED | FINANCE_APPROVED | DISBURSED
  created_at              TIMESTAMPTZ,
  disbursed_at            TIMESTAMPTZ
)
```

---

## 6️⃣ MICROSERVICE ARCHITECTURE (PAYROLL AGENT FLOWS)

### Event-Driven Architecture (Kafka)

```
PAYROLL COMPUTATION FLOW:
┌─────────────────────────────────────────────────────────────────┐
│  Scheduler (Cron: Day 29)                                       │
│       │                                                         │
│       ▼                                                         │
│  PAYROLL_AGENT: Pull Attendance Data ──────► Attendance Svc     │
│       │                                                         │
│       ▼                                                         │
│  PAYROLL_AGENT: Pull Performance Score ────► Analytics Svc      │
│       │                                                         │
│       ▼                                                         │
│  PAYROLL_AGENT: Compute Gross & Deductions                      │
│       │                                                         │
│       ▼                                                         │
│  PAYROLL_AGENT: Generate Payslip Objects                        │
│       │                                                         │
│  Kafka: payroll.computed.event ─────────────► Notification Svc  │
│       │                                      (alert Finance Admin)│
│       ▼                                                         │
│  Finance Admin Dashboard: REVIEW + MFA + APPROVE               │
│       │                                                         │
│  Kafka: payroll.approved.event ────────────► Bank Gateway Svc   │
│       │                                                         │
│       ▼                                                         │
│  Bank Gateway: Execute NEFT / RTGS                              │
│       │                                                         │
│  Kafka: payroll.disbursed.event ───────────► Notification Svc   │
│                                              (payslip to employee)│
└─────────────────────────────────────────────────────────────────┘

COMPLIANCE FLOW:
  Statutory Filing Deadline Approaching
       │
  Kafka: compliance.payroll.deadline.event
       │
  Compliance Alert Service → Admin Dashboard Alert
       │
  HR / Finance generates statutory report
       │
  File uploaded (Form 24Q / ECR / ESI Challan)
       │
  Kafka: compliance.payroll.filed.event → Audit Log
```

---

## 7️⃣ AI INTELLIGENCE LAYER (PAYROLL AGENT — AI FUNCTIONS)

```
AI_ROLE            = ADVISORY ONLY
AI_NEVER_APPROVES  = TRUE
AI_NEVER_DISBURSES = TRUE
AI_NEVER_OVERRIDES_HUMAN = TRUE
```

| AI Function                  | Description                                             | Output Type     |
|------------------------------|---------------------------------------------------------|-----------------|
| Anomaly Detection            | Flags outliers (>20% deviation from last cycle)         | Alert (advisory)|
| TDS Optimisation Suggestion  | Suggests tax-saving declaration gaps for employees      | Suggestion      |
| Attrition Cost Predictor     | Predicts likely FFS cases via engagement score          | Probability     |
| Payroll Forecast             | Projects next 3 months cost (headcount × bands)         | Forecast        |
| Compliance Risk Score        | Scores compliance health per statutory requirement      | Score (0–100)   |
| Overpayment Detector         | Flags duplicate/excess payments vs prior cycles         | Alert (advisory)|

> **ALL AI OUTPUTS ARE ADVISORY — HUMAN MUST ACKNOWLEDGE BEFORE ACTION**

---

## 8️⃣ INTEGRATION MAP

```
INBOUND INTEGRATIONS (PAYROLL_AGENT CONSUMES):
  ├── HR Service            → Employee master, designations, org chart
  ├── Attendance Service    → Daily attendance, leave records, LWP
  ├── Project Engine        → Performance milestones (variable pay input)
  ├── Auth Service          → Identity + session validation
  ├── Compliance Module     → Tax slabs, statutory rates, minimum wage tables
  └── Notification Service  → Email/SMS delivery confirmation

OUTBOUND INTEGRATIONS (PAYROLL_AGENT PRODUCES):
  ├── Bank Gateway          → NEFT/RTGS file (encrypted, finance-admin-triggered)
  ├── Analytics Service     → Payroll metrics, cost centre data
  ├── Audit Service         → Immutable payroll audit logs
  ├── Notification Service  → Payslip delivery, alerts, compliance reminders
  └── ERP Dashboard         → Payroll KPIs, ROI analytics
```

---

## 9️⃣ TECH STACK (LOCKED — INHERITED FROM PLATFORM ARCHITECTURE)

```
BACKEND:
  Language          = Node.js (TypeScript) / Go (computation engine)
  API Layer         = REST + GraphQL (internal services gRPC)
  Auth              = JWT (short-lived) + Refresh Token + MFA (TOTP)
  Message Broker    = Apache Kafka
  Cache             = Redis (rate limiting, session tokens)
  Primary DB        = PostgreSQL (payroll data, audit logs)
  Search            = Elasticsearch (payroll analytics, reporting)
  Object Storage    = S3-compatible (payslip PDFs, bank files)
  Encryption        = AES-256 (at rest), TLS 1.3 (in transit)

FRONTEND:
  Mobile/Desktop    = Flutter (Android, iOS, Windows, macOS, Linux)
  Web               = React Next.js (SEO-only, READ-ONLY for payroll pages)
  FLUTTER_INDEXING  = DISABLED (payroll is sensitive — no web crawling)

INFRASTRUCTURE:
  Container         = Docker + Kubernetes
  Observability     = Prometheus + Grafana
  CI/CD             = GitLab CI / GitHub Actions
  Secrets Mgmt      = HashiCorp Vault
```

---

## 🔟 UI/UX RULES (PAYROLL MODULE — STRICT)

```
DASHBOARD_COMPOSITION:
  FIXED (40%):
    - Employee identity block (photo, name, ID, band)
    - Payslip history (last 12 months)
    - Compliance status badge (PF registered, ESI active, TDS filed)
    - Critical alerts (disputed payslip, missing declarations)

  CUSTOMISABLE (60%):
    - Widget order (YTD summary, deductions chart, etc.)
    - Data density (compact / detailed)
    - Quick actions (download payslip, raise dispute, upload Form 12BB)
    - Notification preferences

THEME_RULES:
  GLOBAL_THEME        = ENABLED
  MODES               = LIGHT + DARK
  DESIGN_LANGUAGE     = CLEAN | SOLID | FUNCTION-FIRST
  DECORATIVE_UI       = FORBIDDEN
  PAYROLL_UI_PALETTE  = Professional Navy + White + Accent-Teal

PAYSLIP_FORMAT:
  - PDF (A4, portrait)
  - Watermarked with employee_id + download_timestamp
  - Digital signature (tenant admin signing key)
  - QR code (links to verify.ecoskiller.com/{payslip_hash})
```

---

## 1️⃣1️⃣ SECURITY RULES (PAYROLL-SPECIFIC HARDENING)

```
DATA_CLASSIFICATION       = HIGHLY SENSITIVE (PII + Financial)
ACCESS_LOG_RETENTION      = 7 YEARS (minimum — statutory)
PAYSLIP_RETENTION         = 10 YEARS (statutory)
SALARY_DATA_ENCRYPTION    = AES-256 (at-rest + in-transit)
BANK_ACCOUNT_MASKING      = Last 4 digits visible, rest masked (UI)
PAN_MASKING               = XXXXXXX1234F (UI display)
PAYROLL_SESSION_TIMEOUT   = 15 MINUTES IDLE

FORBIDDEN OPERATIONS (HARD BLOCK):
  NO — Agent cannot disburse salary directly
  NO — Agent cannot modify bank account details (human-only + dual approval)
  NO — Agent cannot delete payroll records
  NO — Agent cannot bypass TDS deduction
  NO — Agent cannot process payroll for INACTIVE employees
  NO — Agent cannot process payroll for cross-tenant employees
  NO — Agent cannot expose raw salary data to non-authorised roles
  NO — Agent cannot export bulk salary data without Tenant Admin approval + audit log

RATE_LIMITING:
  - Payslip download:    10/hour per employee
  - Bulk export:         1/day per admin (with OTP confirmation)
  - API access (finance): 100 req/min per tenant
```

---

## 1️⃣2️⃣ ERROR HANDLING & FAILURE MODES

```
FAILURE_MODE = STOP_EXECUTION (never proceed with partial/corrupt data)

ERROR SCENARIOS & RESPONSES:

  E001: Attendance data missing for employee
        → BLOCK computation for that employee
        → Log error to payroll_audit_log
        → Alert HR Manager via Notification Service
        → Mark employee as PENDING in cycle

  E002: Bank account not verified
        → BLOCK disbursement for that employee
        → Alert Finance Admin
        → Allow cycle to proceed for others

  E003: Salary structure expired / not found
        → BLOCK computation
        → Alert HR Manager to renew salary structure
        → Employee excluded from cycle with PENDING status

  E004: TDS computation anomaly (>50% variance from last cycle)
        → FLAG as anomaly (do not block)
        → Alert Finance Admin for manual review
        → Log to AI anomaly detection feed

  E005: Kafka event delivery failure
        → Retry (3 times, exponential backoff)
        → If still fails: alert Platform Admin
        → Mark cycle as PARTIAL_FAILURE

  E006: Minimum wage violation detected
        → HARD BLOCK — cycle CANNOT proceed
        → Alert Tenant Admin + Compliance Officer
        → Log to Compliance Audit Service
```

---

## 1️⃣3️⃣ STAGE COMPLIANCE (FOUR-STAGE MODEL — INHERITED)

```
PAYROLL_AGENT REQUIRES:
  STAGE 1 COMPLETE : Identity, Auth, RBAC/ABAC, Core Data Models
  STAGE 2 PARTIAL  : Analytics (AI anomaly detection), Predictive systems
  STAGE 3 PARTIAL  : Corporate ERP layer, SME systems
  STAGE 4 PARTIAL  : Compliance Audit, Governance, Multi-tenant scaling

SKIPPING_STAGES = INVALID BUILD

PAYROLL_AGENT_DEPLOY_GATE:
  → BLOCKED if Stage 1 is incomplete
  → BLOCKED if Compliance Module is not initialised
  → BLOCKED if HR Service is not connected
  → BLOCKED if Bank Gateway integration is not configured
```

---

## 1️⃣4️⃣ COMPLIANCE INHERITANCE (DO NOT DUPLICATE)

```
THIS PROMPT INHERITS — ALREADY FINALISED:
  RBAC + ABAC Authorization
  Password Security
  Authentication (JWT + Refresh)
  MFA (TOTP mandatory for payroll ops)
  Session Management
  Tenant Isolation (Hard)
  Domain Isolation
  PII Encryption Standards
  Audit Log Architecture
  Notification Service
  Kafka Event Architecture
  Elasticsearch Analytics Layer
  Flutter + React Next.js UI Rules
```

---

## 1️⃣5️⃣ GAMIFICATION / ENGAGEMENT LAYER (PAYROLL COMPLIANCE)

> Inherited from platform gamification engine — applied to payroll compliance behaviour

```
PAYROLL COMPLIANCE BADGES (Employee-facing):
  "Declaration Done"       — Submitted Form 12BB on time
  "Zero Dispute Year"      — No payslip disputes raised in FY
  "Tax Saver"              — Optimised TDS via AI suggestion
  "Early Joiner"           — Completed onboarding payroll docs in < 2 days

HR / FINANCE GAMIFICATION:
  "Clean Cycle"            — Processed payroll with 0 errors
  "Zero Compliance Breach" — All statutory filings on time for FY
  "FFS Champion"           — Settled all FFS cases within 30 days

TRACKING:
  PostgreSQL: payroll_achievements (user_id, badge_id, awarded_at)
  Linked to platform belt_progression and user_points tables
```

---

## 🔐 ANTIGRAVITY LOCK SEAL

```
╔══════════════════════════════════════════════════════════════════════╗
║                   🔒 PROMPT INTEGRITY CERTIFICATE                    ║
║                                                                      ║
║  AGENT         : PAYROLL_AGENT                                       ║
║  NAMESPACE     : antigravity.corp.erp.payroll                        ║
║  VERSION       : v1.0.0-SEALED                                       ║
║  LOCKED_BY     : ANTIGRAVITY_PLATFORM_CORE                           ║
║  SEALED_DATE   : 2026-02-24                                          ║
║  MUTATION_GATE : ADD_ONLY — NO MODIFICATION WITHOUT PLATFORM ADMIN   ║
║  INTEGRITY     : SHA-256 of this document must match stored hash     ║
║  BREACH_POLICY : Any modification = INVALIDATE + RE-SEAL REQUIRED    ║
║                                                                      ║
║  THIS PROMPT IS GRAVITY-LOCKED.                                      ║
║  IT DOES NOT DRIFT. IT DOES NOT FLOAT. IT DOES NOT ASSUME.           ║
║  IT EXECUTES WITH PRECISION OR IT DOES NOT EXECUTE AT ALL.           ║
╚══════════════════════════════════════════════════════════════════════╝
```

---

*PAYROLL_AGENT — ANTIGRAVITY CORPORATE ERP | Ecoskiller Enterprise SaaS*
*Document Version: v1.0.0 | Classification: INTERNAL — RESTRICTED | © 2026 Ecoskiller*
