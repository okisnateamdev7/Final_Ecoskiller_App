# 🔒 LEDGER_MIGRATION_AGENT.md
## SEALED & LOCKED MASTER PROMPT
### ANTIGRAVITY · ECOSKILLER ENTERPRISE SAAS · LEDGER & MIGRATION DOMAIN

---

```
PROMPT_CLASS         = LEDGER_MIGRATION_AGENT
EXECUTION_ENGINE     = ANTIGRAVITY
ENGINEERING_GRADE    = PRINCIPAL_ENGINEER + STAFF_DBA
DOMAIN               = FINANCIAL_LEDGER · DATA_MIGRATION · SCHEMA_EVOLUTION
MUTATION_POLICY      = ADD_ONLY
CREATIVE_INTERPRETATION = FORBIDDEN
ASSUMPTION_FILLING   = FORBIDDEN
IMPLICIT_BEHAVIOR    = FORBIDDEN
DEFAULT_BEHAVIOR     = DENY
FAILURE_MODE         = HARD_STOP
CHANGE_POLICY        = APPEND_ONLY
DEVIATION_POLICY     = FORBIDDEN
STATUS               = LOCKED ✔
```

> ⚠️ **ANY DEVIATION FROM THIS PROMPT = STOP EXECUTION. NO EXCEPTIONS.**

---

## 0️⃣ SCOPE DECLARATION (READ-ONLY — DO NOT REINTERPRET)

This agent governs **two tightly coupled concerns** within the ECOSKILLER enterprise SaaS platform:

1. **LEDGER ENGINE** — The immutable, double-entry, multi-currency, multi-tenant financial ledger that records every monetary movement across the platform: subscriptions, commissions, trainer payouts, referral rewards, creator rewards, certification fees, tournament entry fees, dispute settlements, refunds, GST/VAT, and coupon adjustments.

2. **MIGRATION ENGINE** — The zero-downtime, versioned, backward-compatible, audit-tracked schema and data migration system that governs every database schema evolution, seed data upgrade, CQRS projection rebuild, and cross-service contract migration.

Both concerns operate under **ECOSKILLER's locked multi-tenant, domain-isolated, RBAC+ABAC-enforced, four-stage development model.**

---

## 1️⃣ PLATFORM CONTEXT INHERITANCE (NON-NEGOTIABLE)

The Ledger Migration Agent inherits and must never contradict:

| Inherited Contract | Source Authority |
|---|---|
| Multi-tenant isolation (Tenant ≠ Institute ≠ Company ≠ Platform) | Master Prompt §3 |
| RBAC + ABAC authorization | AUTHZ-L, AUTHZ-M, AUTHZ-N, AUTHZ-O |
| Four-stage development model (Foundation → Intelligence → Ecosystem → Compliance) | Master Prompt §9 |
| Immutable audit logs | R39-I, AUTHZ-S |
| Zero-downtime upgrades | R22 |
| Event-driven architecture via Kafka/Redis Streams | Master Prompt §VI |
| No hardcoded roles, no cross-tenant access | Master Prompt §ABSOLUTE PROHIBITIONS |
| AI advises only — never approves, blocks, or overrides financial decisions | Master Prompt §7 |
| Deterministic billing via rule engines only (not ML/AI) | R28 |
| GDPR + data export compliance | Master Prompt §20 |
| PostgreSQL 15 as transactional database | R1 |
| Flyway for versioned schema migrations | R1 |
| MinIO for invoice/audit file storage | R1 |
| Apache Airflow for scheduled billing/settlement workflows | R1 |

**Violation of any inherited contract = STOP EXECUTION.**

---

## 2️⃣ LEDGER ENGINE — FULL SPECIFICATION (LOCKED)

### 2.1 LEDGER ARCHITECTURE PRINCIPLES

```
LEDGER_TYPE         = DOUBLE_ENTRY
ACCOUNTING_MODEL    = DEBIT / CREDIT
CURRENCY_SUPPORT    = MULTI_CURRENCY (INR primary, USD/EUR/GBP via abstraction)
TENANT_SCOPE        = HARD_ISOLATED (no cross-tenant ledger reads)
IMMUTABILITY_POLICY = APPEND_ONLY (no UPDATE, no DELETE on ledger rows)
CONSISTENCY_LEVEL   = ACID (PostgreSQL transactions)
IDEMPOTENCY         = ENFORCED (idempotency_key on every financial event)
AUDIT_TRAIL         = MANDATORY (every entry linked to originating event)
```

**Ledger Accounts are logical buckets. Every transaction touches exactly two accounts: one DEBIT, one CREDIT.**
**No orphaned transactions. No unbalanced entries. Failure = ROLLBACK + ALERT.**

---

### 2.2 LEDGER DATABASE SCHEMA (LOCKED — ADD-ONLY)

```sql
-- ============================================================
-- CORE LEDGER TABLES
-- ============================================================

-- Ledger Account Registry: one record per logical wallet/fund
CREATE TABLE ledger_accounts (
    id               UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id        UUID NOT NULL REFERENCES tenants(id),
    account_code     TEXT NOT NULL,            -- e.g. PLATFORM_REVENUE, USER_WALLET, TRAINER_WALLET
    account_type     TEXT NOT NULL             -- ASSET | LIABILITY | EQUITY | REVENUE | EXPENSE
                     CHECK (account_type IN ('ASSET','LIABILITY','EQUITY','REVENUE','EXPENSE')),
    owner_type       TEXT NOT NULL             -- PLATFORM | TENANT | USER | TRAINER | SYSTEM
                     CHECK (owner_type IN ('PLATFORM','TENANT','USER','TRAINER','SYSTEM')),
    owner_id         UUID,                     -- NULL for PLATFORM/SYSTEM accounts
    currency         TEXT NOT NULL DEFAULT 'INR',
    is_active        BOOLEAN NOT NULL DEFAULT TRUE,
    created_at       TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    UNIQUE (tenant_id, account_code, owner_id)
);

-- Ledger Journal: immutable double-entry record
CREATE TABLE ledger_journal (
    id               UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    idempotency_key  TEXT NOT NULL UNIQUE,     -- prevents duplicate processing
    tenant_id        UUID NOT NULL REFERENCES tenants(id),
    journal_type     TEXT NOT NULL,            -- SUBSCRIPTION | COMMISSION | PAYOUT | REFUND | ...
    description      TEXT NOT NULL,
    currency         TEXT NOT NULL DEFAULT 'INR',
    fx_rate          NUMERIC(18,8) DEFAULT 1.0, -- forex rate if non-INR
    reference_id     UUID,                     -- originating entity (payment_id, order_id, etc.)
    reference_type   TEXT,                     -- PAYMENT | ORDER | PAYOUT_REQUEST | DISPUTE | ...
    created_by       UUID NOT NULL REFERENCES users(id),
    created_at       TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    posted_at        TIMESTAMPTZ,              -- NULL = pending, NOT NULL = posted
    status           TEXT NOT NULL DEFAULT 'PENDING'
                     CHECK (status IN ('PENDING','POSTED','VOIDED','FAILED'))
);

-- Ledger Entries: one debit + one credit per journal (minimum)
CREATE TABLE ledger_entries (
    id               UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    journal_id       UUID NOT NULL REFERENCES ledger_journal(id),
    account_id       UUID NOT NULL REFERENCES ledger_accounts(id),
    entry_type       TEXT NOT NULL CHECK (entry_type IN ('DEBIT','CREDIT')),
    amount           NUMERIC(20,4) NOT NULL CHECK (amount > 0),
    running_balance  NUMERIC(20,4) NOT NULL,   -- computed at insert via trigger
    metadata         JSONB DEFAULT '{}',
    created_at       TIMESTAMPTZ NOT NULL DEFAULT NOW()
);
-- INVARIANT: SUM(DEBIT) = SUM(CREDIT) per journal_id. Enforced by trigger.

-- Tax Ledger: separate GST/VAT tracking (Indian compliance)
CREATE TABLE tax_ledger (
    id               UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    journal_id       UUID NOT NULL REFERENCES ledger_journal(id),
    tenant_id        UUID NOT NULL REFERENCES tenants(id),
    tax_type         TEXT NOT NULL CHECK (tax_type IN ('GST','VAT','TDS','NONE')),
    tax_rate         NUMERIC(6,4) NOT NULL,    -- e.g. 0.18 for 18% GST
    taxable_amount   NUMERIC(20,4) NOT NULL,
    tax_amount       NUMERIC(20,4) NOT NULL,
    hsn_sac_code     TEXT,                     -- Indian HSN/SAC classification
    gst_invoice_no   TEXT,
    created_at       TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- Wallet Balances: materialized view for fast reads
CREATE TABLE wallet_balances (
    account_id       UUID PRIMARY KEY REFERENCES ledger_accounts(id),
    tenant_id        UUID NOT NULL REFERENCES tenants(id),
    currency         TEXT NOT NULL DEFAULT 'INR',
    available_balance NUMERIC(20,4) NOT NULL DEFAULT 0,
    pending_balance  NUMERIC(20,4) NOT NULL DEFAULT 0,
    locked_balance   NUMERIC(20,4) NOT NULL DEFAULT 0,   -- for disputes/holds
    last_updated_at  TIMESTAMPTZ NOT NULL DEFAULT NOW()
);
-- Updated by trigger after every posted ledger entry.

-- Payout Requests: trainer/creator withdrawal requests
CREATE TABLE payout_requests (
    id               UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id        UUID NOT NULL REFERENCES tenants(id),
    requester_id     UUID NOT NULL REFERENCES users(id),
    account_id       UUID NOT NULL REFERENCES ledger_accounts(id),
    amount           NUMERIC(20,4) NOT NULL CHECK (amount > 0),
    currency         TEXT NOT NULL DEFAULT 'INR',
    bank_details_ref UUID NOT NULL,            -- FK to encrypted bank_details table
    status           TEXT NOT NULL DEFAULT 'PENDING'
                     CHECK (status IN ('PENDING','APPROVED','PROCESSING','PAID','REJECTED','FAILED')),
    approved_by      UUID REFERENCES users(id),
    approved_at      TIMESTAMPTZ,
    paid_at          TIMESTAMPTZ,
    external_ref_id  TEXT,                     -- payment gateway reference
    failure_reason   TEXT,
    created_at       TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at       TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- Revenue Share Rules: platform-configurable split rules per entity type
CREATE TABLE revenue_share_rules (
    id               UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id        UUID REFERENCES tenants(id), -- NULL = platform-wide default
    rule_name        TEXT NOT NULL,
    entity_type      TEXT NOT NULL,            -- TRAINER | RECRUITER | INSTITUTE | CREATOR
    revenue_type     TEXT NOT NULL,            -- COURSE_SALE | JOB_PLACEMENT | CERTIFICATION | ...
    platform_share   NUMERIC(6,4) NOT NULL,    -- e.g. 0.30 = 30%
    entity_share     NUMERIC(6,4) NOT NULL,    -- e.g. 0.70 = 70%
    gst_applicable   BOOLEAN NOT NULL DEFAULT TRUE,
    tds_rate         NUMERIC(6,4) DEFAULT 0,
    effective_from   DATE NOT NULL,
    effective_until  DATE,
    is_active        BOOLEAN NOT NULL DEFAULT TRUE,
    created_at       TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    CHECK (platform_share + entity_share = 1.0)
);

-- Referral Reward Ledger (R52 integration)
CREATE TABLE referral_reward_ledger (
    id               UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    journal_id       UUID NOT NULL REFERENCES ledger_journal(id),
    referrer_id      UUID NOT NULL REFERENCES users(id),
    referee_id       UUID NOT NULL REFERENCES users(id),
    reward_tier      INT NOT NULL DEFAULT 1,
    points_awarded   INT NOT NULL DEFAULT 0,
    monetary_credit  NUMERIC(20,4) DEFAULT 0,
    tenant_id        UUID NOT NULL REFERENCES tenants(id),
    created_at       TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- Creator Reward Ledger (R54 integration)
CREATE TABLE creator_reward_ledger (
    id               UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    journal_id       UUID NOT NULL REFERENCES ledger_journal(id),
    creator_id       UUID NOT NULL REFERENCES users(id),
    post_id          UUID NOT NULL,
    engagement_score NUMERIC(10,4) NOT NULL,
    reward_amount    NUMERIC(20,4) NOT NULL DEFAULT 0,
    tenant_id        UUID NOT NULL REFERENCES tenants(id),
    computed_at      TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- Invoice Registry: all generated invoices
CREATE TABLE invoices (
    id               UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id        UUID NOT NULL REFERENCES tenants(id),
    journal_id       UUID REFERENCES ledger_journal(id),
    invoice_number   TEXT NOT NULL UNIQUE,     -- e.g. INV-2024-000001
    invoice_type     TEXT NOT NULL             -- SUBSCRIPTION | COURSE | CERTIFICATION | PAYOUT
                     CHECK (invoice_type IN ('SUBSCRIPTION','COURSE','CERTIFICATION','PAYOUT','REFUND','COMMISSION')),
    billed_to_id     UUID NOT NULL REFERENCES users(id),
    billed_to_type   TEXT NOT NULL,            -- USER | TRAINER | INSTITUTE | ENTERPRISE
    line_items       JSONB NOT NULL,
    subtotal         NUMERIC(20,4) NOT NULL,
    tax_amount       NUMERIC(20,4) NOT NULL DEFAULT 0,
    discount_amount  NUMERIC(20,4) NOT NULL DEFAULT 0,
    total_amount     NUMERIC(20,4) NOT NULL,
    currency         TEXT NOT NULL DEFAULT 'INR',
    status           TEXT NOT NULL DEFAULT 'DRAFT'
                     CHECK (status IN ('DRAFT','ISSUED','PAID','VOID','DISPUTED')),
    storage_url      TEXT,                     -- MinIO path to PDF invoice
    issued_at        TIMESTAMPTZ,
    due_at           TIMESTAMPTZ,
    created_at       TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at       TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- Dispute Ledger
CREATE TABLE financial_disputes (
    id               UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id        UUID NOT NULL REFERENCES tenants(id),
    raised_by        UUID NOT NULL REFERENCES users(id),
    journal_id       UUID NOT NULL REFERENCES ledger_journal(id),
    disputed_amount  NUMERIC(20,4) NOT NULL,
    reason           TEXT NOT NULL,
    evidence_urls    TEXT[],
    status           TEXT NOT NULL DEFAULT 'OPENED'
                     CHECK (status IN ('OPENED','EVIDENCE_COLLECTED','MODERATED','RESOLVED','CLOSED')),
    resolution_type  TEXT CHECK (resolution_type IN ('REFUND','CREDIT','DISMISSED','PARTIAL')),
    resolved_by      UUID REFERENCES users(id),
    resolved_at      TIMESTAMPTZ,
    locked_amount    NUMERIC(20,4) DEFAULT 0,  -- amount held in escrow
    created_at       TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at       TIMESTAMPTZ NOT NULL DEFAULT NOW()
);
```

---

### 2.3 LEDGER WORKFLOW STATE MACHINES (LOCKED)

```
Payment Settlement:
  Initiated → PendingGateway → GatewayConfirmed → LedgerPosted → InvoiceGenerated → Settled
                             → GatewayFailed → LedgerFailed → AlertTriggered

Payout Lifecycle:
  Requested → BalanceChecked → AdminApproved → Processing → ExternalTransferred → LedgerClosed
            → InsufficientFunds → Rejected
            → AdminRejected → LedgerUnlocked

Refund Lifecycle:
  RefundRequested → EligibilityVerified → ReversalJournalCreated → GatewayRefundInitiated
  → GatewayConfirmed → LedgerReversed → CustomerNotified
  → GatewayFailed → ManualReviewQueued

Dispute Lifecycle:
  Opened → EvidenceCollected → Moderated → Resolved → Closed
  (funds held in locked_balance throughout; released on resolution)
```

---

### 2.4 LEDGER BUSINESS RULES (HARD-LOCKED)

```
RULE-L001: Every financial operation MUST generate a journal entry before any state change.
RULE-L002: No direct UPDATE to ledger_journal or ledger_entries. APPEND-ONLY.
RULE-L003: Journal balance invariant: SUM(debit entries) = SUM(credit entries) per journal_id.
           Enforced by PostgreSQL constraint trigger. Violation = ROLLBACK.
RULE-L004: Every ledger_journal row MUST have a non-null idempotency_key.
RULE-L005: All monetary amounts stored as NUMERIC(20,4). FLOAT is FORBIDDEN.
RULE-L006: All ledger operations run inside explicit database transactions (BEGIN/COMMIT).
           Auto-commit is FORBIDDEN in billing service.
RULE-L007: Tenant isolation — no ledger query may span tenant boundaries.
           RLS (Row-Level Security) enforced at PostgreSQL level.
RULE-L008: Payout approvals require human admin with BILLING_APPROVER role.
           AI agents cannot approve payouts (AUTHZ-Q).
RULE-L009: Revenue share calculation uses rule engine from revenue_share_rules table.
           Hardcoded percentages in application code are FORBIDDEN.
RULE-L010: GST/VAT must be tracked in tax_ledger. Tax on revenue = MANDATORY for India.
RULE-L011: All invoices stored as PDF in MinIO. Invoice number format: INV-{YYYY}-{000000}.
RULE-L012: Dispute locks corresponding amount in locked_balance immediately on creation.
RULE-L013: Wallet balance reads serve from wallet_balances (materialized). Never re-aggregate ledger_entries at runtime.
RULE-L014: Currency conversion must use stored fx_rate at time of transaction. No live FX lookups during settlement.
RULE-L015: All ledger events emitted to Kafka: ledger.entry.posted, ledger.payout.approved, ledger.dispute.opened, invoice.generated.
```

---

### 2.5 LEDGER SERVICE API CONTRACT (LOCKED)

```yaml
# Billing & Ledger Service — OpenAPI 3.1 Contract Additions

/ledger/journal:
  post:
    summary: Create and post a ledger journal entry
    security: [bearerAuth]
    x-required-role: [BILLING_SERVICE, PLATFORM_ADMIN]
    x-required-entitlement: LEDGER_WRITE
    requestBody:
      required: true
      content:
        application/json:
          schema:
            type: object
            required: [idempotency_key, tenant_id, journal_type, currency, entries, reference_id, reference_type]
            properties:
              idempotency_key: {type: string}
              tenant_id: {type: string, format: uuid}
              journal_type: {type: string}
              description: {type: string}
              currency: {type: string, default: INR}
              reference_id: {type: string, format: uuid}
              reference_type: {type: string}
              entries:
                type: array
                items:
                  type: object
                  required: [account_id, entry_type, amount]
                  properties:
                    account_id: {type: string, format: uuid}
                    entry_type: {type: string, enum: [DEBIT, CREDIT]}
                    amount: {type: number}
    responses:
      "200": {description: Journal posted}
      "409": {description: Duplicate idempotency_key}
      "422": {description: Unbalanced entries}

/ledger/balance/{account_id}:
  get:
    summary: Get wallet balance for an account
    security: [bearerAuth]
    x-required-role: [BILLING_SERVICE, ACCOUNT_OWNER, PLATFORM_ADMIN]
    responses:
      "200":
        content:
          application/json:
            schema:
              type: object
              properties:
                available_balance: {type: number}
                pending_balance: {type: number}
                locked_balance: {type: number}
                currency: {type: string}

/payout/request:
  post:
    summary: Trainer/Creator submits payout request
    security: [bearerAuth]
    x-required-role: [TRAINER, CREATOR]
    x-billing-gate: CHECK_AVAILABLE_BALANCE

/payout/{id}/approve:
  post:
    summary: Admin approves a payout (human only)
    security: [bearerAuth]
    x-required-role: [BILLING_APPROVER]
    x-mfa-required: true

/invoices:
  get:
    summary: List invoices for authenticated user or tenant
    security: [bearerAuth]
    x-tenant-scoped: true

/invoices/{id}/download:
  get:
    summary: Download invoice PDF from MinIO
    security: [bearerAuth]
    x-required-entitlement: INVOICE_DOWNLOAD

/disputes:
  post:
    summary: Raise a financial dispute
    security: [bearerAuth]

/disputes/{id}/resolve:
  post:
    summary: Resolve a dispute (admin moderation)
    security: [bearerAuth]
    x-required-role: [COMPLIANCE_ADMIN, PLATFORM_ADMIN]

/billing/subscription/plans:
  get:
    summary: List all subscription plans

/billing/subscription/activate:
  post:
    summary: Activate subscription for tenant
    security: [bearerAuth]
    x-billing-gate: PAYMENT_WEBHOOK_CONFIRMED

/billing/usage/report:
  get:
    summary: Usage metering report (matches, hours, recordings)
    security: [bearerAuth]
    x-required-role: [TENANT_ADMIN, PLATFORM_ADMIN]
```

---

### 2.6 LEDGER EVENTS (ASYNCAPI — LOCKED)

```yaml
channels:
  ledger.entry.posted:
    publish:
      message:
        payload:
          type: object
          required: [journal_id, tenant_id, journal_type, total_amount, currency, posted_at]
          properties:
            journal_id: {type: string, format: uuid}
            tenant_id: {type: string, format: uuid}
            journal_type: {type: string}
            total_amount: {type: number}
            currency: {type: string}
            reference_id: {type: string, format: uuid}
            reference_type: {type: string}
            posted_at: {type: string, format: date-time}

  ledger.payout.approved:
    publish:
      message:
        payload:
          properties:
            payout_id: {type: string, format: uuid}
            requester_id: {type: string, format: uuid}
            amount: {type: number}
            currency: {type: string}
            approved_by: {type: string, format: uuid}
            approved_at: {type: string, format: date-time}

  ledger.dispute.opened:
    publish:
      message:
        payload:
          properties:
            dispute_id: {type: string, format: uuid}
            journal_id: {type: string, format: uuid}
            tenant_id: {type: string, format: uuid}
            disputed_amount: {type: number}
            raised_by: {type: string, format: uuid}

  invoice.generated:
    publish:
      message:
        payload:
          properties:
            invoice_id: {type: string, format: uuid}
            invoice_number: {type: string}
            billed_to_id: {type: string, format: uuid}
            total_amount: {type: number}
            storage_url: {type: string}
            issued_at: {type: string, format: date-time}

  billing.subscription.activated:
    publish:
      message:
        payload:
          properties:
            tenant_id: {type: string, format: uuid}
            plan_id: {type: string, format: uuid}
            activated_at: {type: string, format: date-time}
            valid_until: {type: string, format: date-time}

  billing.subscription.expired:
    publish:
      message:
        payload:
          properties:
            tenant_id: {type: string, format: uuid}
            plan_id: {type: string, format: uuid}
            expired_at: {type: string, format: date-time}
```

---

### 2.7 LEDGER AUTHORIZATION MATRIX (LOCKED)

| Action | Required Role | MFA Required | Notes |
|---|---|---|---|
| Post journal entry | `BILLING_SERVICE` (internal) | No | Service-to-service only |
| View own balance | `ACCOUNT_OWNER` | No | Tenant-scoped |
| View all tenant balances | `TENANT_ADMIN` | No | — |
| Request payout | `TRAINER`, `CREATOR` | No | Self-initiated |
| Approve payout | `BILLING_APPROVER` | YES | Human-only, AI forbidden |
| Reject payout | `BILLING_APPROVER` | YES | — |
| Raise dispute | Any authenticated user | No | — |
| Resolve dispute | `COMPLIANCE_ADMIN` | YES | — |
| Configure revenue share rules | `PLATFORM_ADMIN` | YES | Platform-wide impact |
| Download invoice | `INVOICE_OWNER`, `TENANT_ADMIN` | No | — |
| View all invoices (platform) | `PLATFORM_ADMIN` | No | — |
| Void invoice | `BILLING_APPROVER` | YES | Audit-logged |
| Export ledger for audit | `COMPLIANCE_AUDITOR` | YES | GDPR-compliant export |

---

## 3️⃣ MIGRATION ENGINE — FULL SPECIFICATION (LOCKED)

### 3.1 MIGRATION ARCHITECTURE PRINCIPLES

```
MIGRATION_TOOL       = FLYWAY (version-controlled, repeatable)
ZERO_DOWNTIME        = ENFORCED (expand-contract pattern)
ENVIRONMENT_SCOPE    = DEV | TEST | STAGING | PRODUCTION (isolated)
BACKWARD_COMPAT      = MINIMUM_2_VERSIONS (old schema readable by new service)
ROLLBACK_STRATEGY    = DEFINED PER MIGRATION (no orphaned schemas)
SEED_STRATEGY        = ENVIRONMENT_AWARE (seed data differs per env)
CQRS_PROJECTION_REBUILD = TRIGGERED AUTOMATICALLY after breaking migrations
MIGRATION_LOG        = IMMUTABLE AUDIT (who, when, what, environment)
MIGRATION_GATE       = CI/CD CONTRACT GATE (no deploy without passing migration tests)
```

---

### 3.2 MIGRATION FILE STRUCTURE (LOCKED)

```
/db/migrations/
├── V1__initial_schema.sql
├── V2__add_tenant_isolation.sql
├── V3__billing_ledger_tables.sql
├── V4__add_wallet_balances.sql
├── V5__revenue_share_rules.sql
├── V6__payout_requests.sql
├── V7__invoices_table.sql
├── V8__financial_disputes.sql
├── V9__referral_reward_ledger.sql
├── V10__creator_reward_ledger.sql
├── V11__tax_ledger.sql
├── V12__trainer_wallet.sql
├── V13__gamification_tables.sql
├── V14__social_groups_posts.sql
├── V15__institution_org.sql
├── V16__company_org.sql
├── V17__anonymous_complaints.sql
├── V18__add_rls_policies.sql
├── V19__add_audit_triggers.sql
├── V20__search_index_metadata.sql
├── R__repeatable_seed_data.sql      ← repeatable (R prefix), runs if checksum changes
└── /rollback/
    ├── U1__undo_initial_schema.sql
    ├── U3__undo_billing_ledger_tables.sql
    └── ... (all undos defined)

/db/seeds/
├── seed_dev.sql
├── seed_test.sql
├── seed_staging.sql
└── seed_production.sql              ← minimal: admin users, default plans only
```

---

### 3.3 MIGRATION NAMING CONVENTION (LOCKED)

```
Versioned:    V{n}__{snake_case_description}.sql
              V3__add_billing_ledger_tables.sql

Repeatable:   R__{snake_case_description}.sql
              R__refresh_skill_taxonomy.sql

Undo:         U{n}__{snake_case_description}.sql
              U3__undo_billing_ledger_tables.sql

Patch:        V{n}.{m}__{description}.sql
              V3.1__add_invoice_status_index.sql
```

**Rules:**
- Version numbers are MONOTONIC — no reuse, no gaps intentional after production deploy.
- Description must be meaningful — `V12__stuff.sql` is INVALID.
- Every versioned migration must have a corresponding undo file.
- No DDL changes to production tables without a migration file. Direct `ALTER TABLE` in production = **FORBIDDEN**.

---

### 3.4 ZERO-DOWNTIME MIGRATION PATTERN (LOCKED — EXPAND-CONTRACT)

All schema migrations that affect production tables must follow the **Expand-Contract** pattern:

```
PHASE 1 — EXPAND (deploy without breaking old service version):
  - Add new column as NULLABLE (never NOT NULL without DEFAULT first)
  - Add new table
  - Add new index (CONCURRENTLY)
  - Backfill data via background job

PHASE 2 — CONTRACT (after all service instances on new version):
  - Add NOT NULL constraint
  - Drop old column (after 2-version backward compat window)
  - Drop old index

FORBIDDEN:
  - ALTER COLUMN type change in one step (must be expand-then-contract)
  - DROP COLUMN without backward compat window
  - Adding NOT NULL without DEFAULT to large tables
  - Creating non-CONCURRENT indexes on live tables
  - TRUNCATE in production migrations
  - Schema migrations that lock tables > 30 seconds
```

**Example — Safe Column Addition (Ledger):**
```sql
-- V21__add_fx_rate_to_journal.sql (EXPAND PHASE)
ALTER TABLE ledger_journal ADD COLUMN fx_rate NUMERIC(18,8);
-- NULL allowed initially — old service versions still insert without this column.

-- V22__backfill_fx_rate.sql (BACKFILL — run as background job via Airflow)
UPDATE ledger_journal SET fx_rate = 1.0 WHERE currency = 'INR' AND fx_rate IS NULL;

-- V23__enforce_fx_rate_not_null.sql (CONTRACT PHASE — after 2 deploy cycles)
ALTER TABLE ledger_journal ALTER COLUMN fx_rate SET DEFAULT 1.0;
ALTER TABLE ledger_journal ALTER COLUMN fx_rate SET NOT NULL;
```

---

### 3.5 MIGRATION ENFORCEMENT GATES (LOCKED)

Every migration must pass the following gates **before CI/CD allows deployment**:

```
GATE-M001: Migration file checksum matches registry (no tampering)
GATE-M002: Corresponding undo file exists and is syntactically valid
GATE-M003: Migration runs successfully on TEST environment
GATE-M004: Rollback runs successfully on TEST environment (restored to prior state)
GATE-M005: RLS policies verified post-migration on tenant-isolated tables
GATE-M006: CQRS projection rebuild test passes (if migration affects event-sourced tables)
GATE-M007: Index creation uses CONCURRENTLY on tables with >10k rows
GATE-M008: No migration exceeds 30-second table lock on production replica test
GATE-M009: Backward compatibility test: old service version reads/writes new schema without error
GATE-M010: Seed data loads successfully in TEST environment post-migration
GATE-M011: Audit trigger fires correctly on new tables (ledger_journal, payout_requests, invoices)
GATE-M012: Row-Level Security policies verified — cross-tenant data leak test passes

Failure at any gate → STOP DEPLOYMENT → REPORT MIGRATION_GATE_FAILURE → ROLLBACK
```

---

### 3.6 MIGRATION AUDIT LOG (IMMUTABLE — LOCKED)

```sql
-- Migration execution log — APPEND ONLY, no UPDATE/DELETE
CREATE TABLE migration_audit_log (
    id               UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    migration_version TEXT NOT NULL,            -- e.g. V3
    migration_script  TEXT NOT NULL,            -- filename
    environment       TEXT NOT NULL CHECK (environment IN ('DEV','TEST','STAGING','PRODUCTION')),
    executed_by       TEXT NOT NULL,            -- CI/CD pipeline identity or human operator
    execution_type    TEXT NOT NULL CHECK (execution_type IN ('APPLY','ROLLBACK','VALIDATE')),
    status            TEXT NOT NULL CHECK (status IN ('SUCCESS','FAILED','PARTIAL')),
    duration_ms       INT,
    checksum          TEXT NOT NULL,
    error_message     TEXT,
    executed_at       TIMESTAMPTZ NOT NULL DEFAULT NOW()
);
-- RLS: Only COMPLIANCE_AUDITOR and PLATFORM_ADMIN may read. No role may write directly.
-- Written only by Flyway migration executor with service credentials.
```

---

### 3.7 CQRS PROJECTION REBUILD PROTOCOL (LOCKED)

When a migration affects tables used as event-sourced read models (e.g., wallet_balances, search indexes, recommendation caches), the following protocol is **mandatory**:

```
STEP 1: Tag migration with annotation: -- @cqrs-projection-rebuild: wallet_balances
STEP 2: CI detects annotation → triggers Projection Rebuild Job
STEP 3: Projection Rebuild Job:
          a. Set affected read model to REBUILDING state
          b. Replay all relevant Kafka events from event log (watermark-based)
          c. Rebuild projection from scratch into shadow table
          d. Validate shadow table row count ≥ original (no data loss)
          e. Atomic swap: shadow → live
          f. Set state to READY
          g. Emit projection.rebuild.completed event
STEP 4: If rebuild fails → ROLLBACK migration → ALERT OPS → STOP DEPLOYMENT
```

**Affected projections requiring rebuild triggers:**
- `wallet_balances` (source: ledger_entries)
- `user_skill_ranking` (source: skill_assessments)
- `job_match_scores` (source: job_applications + user_skills)
- `search_index_jobs` / `search_index_users` (source: Elasticsearch sync worker)

---

### 3.8 SEED DATA STRATEGY (ENVIRONMENT-AWARE — LOCKED)

```
DEV seed:
  - 5 test tenants (1 institute, 1 enterprise, 1 recruiter, 1 SME, 1 individual)
  - 20 test users across all roles
  - 5 subscription plans (Free, Basic, Pro, Enterprise, Custom)
  - Sample revenue share rules
  - Sample jobs, courses, projects, GD scenarios
  - 100 pre-seeded ledger journal entries (for UI testing)

TEST seed:
  - Minimal fixture data
  - Deterministic IDs (no random UUIDs — fixed for test assertions)
  - Known financial state for balance assertion tests

STAGING seed:
  - Production-like anonymized data snapshot
  - Real plan configurations
  - No real user PII

PRODUCTION seed:
  - Platform admin user (credentials via Vault — NOT in seed file)
  - Default subscription plans
  - Default revenue share rules (30/70 platform/trainer split)
  - GST configuration (18% for India)
  - NO sample data, NO test users
```

---

### 3.9 MIGRATION ROLLBACK RUNBOOK (LOCKED)

```
TRIGGER CONDITIONS FOR ROLLBACK:
  - Post-deploy error rate > 1% on billing service within 5 minutes
  - wallet_balances discrepancy detected by health check
  - ledger balance invariant trigger fires in production
  - GATE-M failure detected post-deploy

ROLLBACK PROCEDURE:
  Step 1: CI/CD pipeline auto-detects failure (Prometheus alert: billing_error_rate > threshold)
  Step 2: Pipeline triggers: flyway undo -target={previous_version}
  Step 3: All billing service pods rolled back to previous Docker image
  Step 4: Projection Rebuild Job triggered for affected read models
  Step 5: OPS team notified via PagerDuty + Slack
  Step 6: Incident logged in migration_audit_log
  Step 7: Post-incident report generated within 24h

ROLLBACK IS FORBIDDEN FOR:
  - Migrations that have already processed financial transactions
    (must issue corrective journal entry instead — RULE-L002)
  - Production data drops (expand-contract prevents this scenario)
```

---

## 4️⃣ BILLING SERVICE WIRING MATRIX (LOCKED)

| Feature | Service | API Endpoint | Event Produced | Migration Required |
|---|---|---|---|---|
| Subscription activation | Billing Service | `POST /billing/subscription/activate` | `billing.subscription.activated` | V_billing_subscriptions |
| Feature gate check | Billing Middleware | Internal | None | V_feature_flags |
| Usage metering (Dojo match) | Billing Service | Internal (Kafka consumer) | None | V_usage_metering |
| Invoice generation | Billing Service | Auto-triggered | `invoice.generated` | V7 |
| Payout request | Billing Service | `POST /payout/request` | `payout.requested` | V6 |
| Payout approval | Billing Service | `POST /payout/{id}/approve` | `ledger.payout.approved` | V6 |
| Revenue split (course sale) | Billing Service | Internal | `ledger.entry.posted` | V5, V3 |
| Referral reward credit | Billing Service | Kafka consumer `referral_success` | `ledger.entry.posted` | V9 |
| Creator reward credit | Billing Service | Kafka consumer (cron) | `ledger.entry.posted` | V10 |
| GST computation | Billing Service | Internal (rule engine) | None | V11 |
| Dispute financial hold | Billing Service | `POST /disputes` | `ledger.dispute.opened` | V8 |
| Dispute resolution | Compliance Service | `POST /disputes/{id}/resolve` | `ledger.entry.posted` | V8 |
| Coupon application | Billing Service | Internal | None | V_coupons |
| TDS deduction (India) | Billing Service | Internal (rule engine) | None | V11 |
| Trainer wallet view | Trainer Dashboard | `GET /ledger/balance/{account_id}` | None | V12 |
| Refund processing | Billing Service | `POST /refunds` | `ledger.entry.posted` | V_refunds |

---

## 5️⃣ MONITORING & OBSERVABILITY (LOCKED)

```
METRICS (Prometheus):
  billing_journal_posts_total{status,journal_type}
  billing_payout_approval_duration_seconds
  billing_invoice_generation_duration_seconds
  billing_dispute_count{status}
  migration_execution_duration_seconds{version,environment}
  migration_failures_total{version,environment}
  ledger_balance_invariant_violations_total  ← CRITICAL ALERT if > 0
  wallet_balance_discrepancy_detected_total  ← CRITICAL ALERT if > 0

ALERTS:
  CRITICAL: ledger_balance_invariant_violations_total > 0     → PAGE OPS IMMEDIATELY
  CRITICAL: wallet_balance_discrepancy_detected_total > 0     → PAGE OPS IMMEDIATELY
  HIGH:     migration_failures_total > 0                      → BLOCK DEPLOYMENT
  HIGH:     billing_journal_posts_total{status=FAILED} > 5/min → ALERT
  MEDIUM:   payout_approval_duration_seconds > 86400          → ALERT (24h SLA breach)
  MEDIUM:   billing_dispute_count{status=OPENED} > 50         → ALERT

DASHBOARDS (Grafana):
  - Ledger Journal Throughput (by type, by tenant)
  - Wallet Balance Health (aggregate, per tenant)
  - Payout Pipeline Status
  - Dispute Queue
  - Migration History Timeline
  - Revenue Breakdown (subscriptions / commissions / certification fees)
  - GST Collection Summary (India compliance)

DISTRIBUTED TRACING (Jaeger):
  - Every /ledger/** request traced with span: journal_post, balance_update, invoice_gen
  - Correlation ID propagated through Kafka events: billing.* channels
  - Migration execution traces: flyway.apply, flyway.rollback
```

---

## 6️⃣ SECURITY POLICY (LEDGER-SPECIFIC — LOCKED)

```
SEC-LED001: Row-Level Security on all ledger tables — tenant_id enforced at PostgreSQL level.
SEC-LED002: Bank details (payout destination) stored encrypted (AES-256-GCM), key in Vault.
SEC-LED003: Ledger endpoints behind API Gateway rate limiting: 100 req/min per tenant.
SEC-LED004: Screenshot blocking enabled on invoice view and payout screens (UI Security rule §16).
SEC-LED005: Payout approval requires MFA (TOTP or Hardware Key) for BILLING_APPROVER role.
SEC-LED006: All financial amounts in NUMERIC(20,4). Floating-point arithmetic FORBIDDEN in billing code.
SEC-LED007: Idempotency keys expire after 24 hours but must be stored for 7 years (audit compliance).
SEC-LED008: Financial disputes lock funds in locked_balance — direct credit forbidden during active dispute.
SEC-LED009: Service-to-service Kafka consumers re-validate authorization context before posting journal entries (AUTHZ-O).
SEC-LED010: No ledger data leaves tenant boundary in API responses — even for PLATFORM_ADMIN (read via internal tooling only).
SEC-LED011: TDS/GST amounts stored separately in tax_ledger — never merged into primary ledger amounts.
SEC-LED012: Invoice PDFs in MinIO signed with time-limited pre-signed URLs (15 minutes expiry).
SEC-LED013: All payout external reference IDs (gateway refs) encrypted at rest.
```

---

## 7️⃣ FOUR-STAGE DEVELOPMENT ENFORCEMENT (LOCKED)

| Stage | Ledger Features Allowed | Migration Scope |
|---|---|---|
| **STAGE 1 — FOUNDATION** | Core schema: `ledger_accounts`, `ledger_journal`, `ledger_entries`, `wallet_balances`, basic subscription plan table | V1–V12 (structural tables only). Seed: plans, admin account. |
| **STAGE 2 — INTELLIGENCE** | Revenue share rule engine, usage metering, billing analytics, AI-assisted invoice classification | V13–V16. Airflow DAGs for scheduled settlement. |
| **STAGE 3 — ECOSYSTEM** | Trainer wallets, creator reward ledger, referral reward ledger, payout workflows, coupon system, ERP billing integration | V17–V24. Full payout pipeline. |
| **STAGE 4 — COMPLIANCE & SCALE** | GST/VAT full compliance, TDS deduction, dispute resolution, financial audit export, multi-currency, cross-border settlement | V25+. GDPR-compliant data export. SOC2-ready audit trails. |

**Do NOT implement Stage 3 ledger features before Stage 1 is complete and all GATE-M checks pass.**
**Stage mixing = STOP EXECUTION.**

---

## 8️⃣ INTERN-EXECUTABLE IMPLEMENTATION GUIDE (MANDATORY — R24 / R26)

### Running Migrations (Step-by-Step)

```bash
# STEP 1: Ensure you are on the correct branch
git checkout dev    # or test / staging / production (NEVER run prod migrations manually)

# STEP 2: Copy correct env config
cp /config/environments/dev.env .env

# STEP 3: Run Flyway migration (dev environment)
docker run --rm \
  --network ecoskiller-network \
  -v $(pwd)/db/migrations:/flyway/sql \
  -e FLYWAY_URL=jdbc:postgresql://postgres:5432/ecoskiller_dev \
  -e FLYWAY_USER=${DB_USER} \
  -e FLYWAY_PASSWORD=${DB_PASSWORD} \
  flyway/flyway:10 migrate

# Expected output:
# Flyway 10.x by Redgate
# Database: jdbc:postgresql://postgres:5432/ecoskiller_dev
# Successfully validated 24 migrations
# Current version of schema "public": 23
# Migrating schema "public" to version 24 - add_fx_rate_to_journal
# Successfully applied 1 migration to schema "public" (execution time 00:00.342s)

# STEP 4: Validate migration ran correctly
docker run --rm ... flyway/flyway:10 info

# STEP 5: If migration fails — DO NOT manually edit the database.
# Run rollback:
docker run --rm ... flyway/flyway:10 undo -target=23
# Then fix migration script and re-run.
```

### Adding a New Ledger Migration (Intern Checklist)

```
☐ 1. Create /db/migrations/V{next_number}__{description}.sql
☐ 2. Create /db/migrations/rollback/U{same_number}__{undo_description}.sql
☐ 3. Write migration SQL (add columns as NULLABLE first if needed)
☐ 4. Write undo SQL (reverse the change)
☐ 5. Test locally: run `flyway migrate` on dev
☐ 6. Test rollback: run `flyway undo` and verify schema restored
☐ 7. If migration adds a table: add RLS policy
☐ 8. If migration affects event-sourced table: add @cqrs-projection-rebuild annotation
☐ 9. Run: pytest tests/db/test_migration_V{n}.py
☐ 10. Commit and push to dev branch — CI pipeline handles the rest
☐ 11. NEVER push directly to staging or production branches
```

---

## 9️⃣ AIRFLOW SCHEDULED BILLING WORKFLOWS (LOCKED)

```python
# Scheduled DAGs required in Apache Airflow:

DAG: ecoskiller_daily_settlement
  Schedule: 0 2 * * *   (daily 2am IST)
  Tasks:
    1. compute_pending_payouts         → aggregate eligible payout_requests
    2. calculate_revenue_shares        → apply revenue_share_rules to confirmed sales
    3. post_settlement_journal_entries → post to ledger_journal (idempotent)
    4. generate_daily_invoices         → batch invoice generation → upload to MinIO
    5. send_invoice_notifications      → emit invoice.generated events
    6. update_wallet_balances          → refresh wallet_balances from ledger_entries
  Failure policy: ALERT OPS + RETRY x3 before paging

DAG: ecoskiller_gst_monthly_report
  Schedule: 0 9 1 * *   (1st of month 9am IST)
  Tasks:
    1. aggregate_tax_ledger_entries    → compute monthly GST/TDS totals per tenant
    2. generate_gst_report_pdf         → upload to MinIO
    3. notify_tenant_admins            → email compliance report

DAG: ecoskiller_subscription_renewal
  Schedule: */15 * * * *  (every 15 minutes)
  Tasks:
    1. detect_expiring_subscriptions   → query subscriptions expiring in next 24h
    2. initiate_renewal_payment        → call payment gateway
    3. handle_renewal_success          → post journal entry + extend subscription
    4. handle_renewal_failure          → mark EXPIRED + notify user + block features
    5. emit billing.subscription.expired event

DAG: ecoskiller_creator_reward_computation
  Schedule: */10 * * * *  (every 10 minutes — R54)
  Tasks:
    1. compute_post_engagement_scores
    2. credit_creator_reward_ledger    → idempotent credit
    3. emit creator_reward.credited event
```

---

## 🔐 FINAL LEDGER MIGRATION AGENT SEAL

```
LEDGER_MIGRATION_AGENT STATUS:

✔ LOCKED
✔ SEALED
✔ DETERMINISTIC
✔ ENTERPRISE SAFE
✔ ANTIGRAVITY COMPATIBLE
✔ R22 COMPLIANT (Zero-downtime migration)
✔ R24 COMPLIANT (Intern-executable)
✔ R26 COMPLIANT (Line-level instructions)
✔ R28 COMPLIANT (Rule engine for billing — no ML)
✔ R39-E COMPLIANT (Billing & Commercial tools)
✔ R52 INTEGRATED (Referral reward ledger)
✔ R54 INTEGRATED (Creator reward ledger)
✔ R84 INTEGRATED (Trainer wallet)
✔ AUTHZ-L COMPLIANT (Billing entitlement gates)
✔ AUTHZ-Q COMPLIANT (AI cannot approve payouts)
✔ AUTHZ-S COMPLIANT (Full audit trail)
✔ GDPR COMPLIANT (Data export, deletion, right to access)
✔ INDIA GST/TDS COMPLIANT
✔ MULTI-TENANT ISOLATED (RLS enforced)
✔ DOUBLE-ENTRY LEDGER (balance invariant enforced)
✔ IMMUTABLE AUDIT LOG

CHANGE POLICY = APPEND_ONLY
FURTHER MODIFICATIONS = APPEND ONLY TO NEW SECTIONS

ANY AGENT THAT:
  - Modifies ledger_journal or ledger_entries without idempotency_key
  - Approves payouts without BILLING_APPROVER + MFA
  - Runs DDL on production tables outside Flyway
  - Skips migration gates
  - Mixes billing logic across tenant boundaries
  - Uses floating-point for monetary amounts
  - Deploys without passing all GATE-M checks
  - Claims production-live status without real human deployment

⇒ MUST BE REJECTED AND EXECUTION STOPPED
```

---

*LEDGER_MIGRATION_AGENT.md · ECOSKILLER Enterprise SaaS · ANTIGRAVITY Execution Engine*
*Sealed by: Architecture Authority · APPEND-ONLY after initial seal*
