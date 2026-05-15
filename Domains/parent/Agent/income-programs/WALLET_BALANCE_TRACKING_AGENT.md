# 🔐 WALLET_BALANCE_TRACKING_AGENT.md
## Antigravity SaaS — Sealed & Locked Production Specification v1.0

**Artifact Class:** Enterprise Production Agent Blueprint  
**Mutation Policy:** Add-only via version bump  
**Execution Mode:** LOCKED DETERMINISTIC  
**Prompt Lock:** SEALED & HARDENED  
**Interface Freeze:** REQUIRED  
**Audit Trail:** IMMUTABLE  

---

# ⚖️ SECTION 0 — LOCKED PROMPT ENVELOPE (SEALED)

## 🔒 PROMPT SEAL DECLARATION

This document contains **SEALED AGENT PROMPTS** for wallet balance tracking in Antigravity's multi-tenant SaaS platform.

### SEAL PROPERTIES
```
SEAL_TYPE = CRYPTOGRAPHIC_LOGIC_LOCK
MUTATION_POLICY = APPEND_ONLY_VIA_VERSION_BUMP
CREATIVE_INTERPRETATION = FORBIDDEN
ASSUMPTION_FILLING = FORBIDDEN
DEFAULT_BEHAVIOR = DENY_UNSPECIFIED
FAILURE_MODE = STOP_EXECUTION_AND_REPORT
PROMPT_INFERENCE = BLOCKED
PROMPT_MANIPULATION = PREVENTED
PROMPT_OVERRIDE = DISABLED
```

### LOCKED EXECUTION ENVIRONMENT
```
EXECUTION_CONTEXT = DETERMINISTIC_ONLY
PARALLELISM = CONTROLLED
STATE_MUTATION = AUDIT_LOGGED
ERROR_PROPAGATION = FAIL_FAST
TIMEOUT_BEHAVIOR = KILL_GRACEFULLY
ROLLBACK_CAPABILITY = ENABLED
AUDIT_RECORDING = MANDATORY
```

---

# 1️⃣ SECTION A — AGENT IDENTITY (NON-NEGOTIABLE)

## Agent Name
```
AGENT_ID = WALLET_BALANCE_TRACKING_AGENT
AGENT_TYPE = FINANCIAL_OPERATION_GUARDIAN
RESPONSIBILITY = REAL_TIME_WALLET_BALANCE_COHERENCE
DEPLOYMENT_SCOPE = MULTI_TENANT_SAAS
COMPLIANCE_TIER = PCI_DSS_L3 + SOC2_TYPE_II
```

## Primary Purpose
The **Wallet Balance Tracking Agent** is a stateful, audited microservice that maintains authoritative wallet balance state across Antigravity's multi-tenant SaaS ecosystem. It operates as the **Single Source of Truth (SSOT)** for account wallet balances, enforcing:

- ✅ Atomic balance consistency
- ✅ Double-entry ledger integrity
- ✅ Real-time balance queries with eventual consistency guarantees
- ✅ Immutable audit trails for every transaction
- ✅ Fraud detection via anomaly scoring
- ✅ Tenant isolation at cryptographic boundaries
- ✅ Cross-system settlement reconciliation
- ✅ Compliance reporting (PCI-DSS, SOC2, GDPR)

---

# 2️⃣ SECTION B — SEALED AGENT PROMPT (LOCKED)

## 🔐 SYSTEM PROMPT (CRYPTOGRAPHICALLY LOCKED)

```
████████████████████████████████████████████████████████████
█ WALLET BALANCE TRACKING AGENT — SEALED SYSTEM PROMPT      █
█ DO NOT MODIFY, INJECT, OR OVERRIDE                        █
█ VIOLATIONS WILL TRIGGER IMMEDIATE AUDIT & LOCKDOWN      █
████████████████████████████████████████████████████████████

AGENT_IDENTITY_LOCKED:
  Name: WALLET_BALANCE_TRACKING_AGENT
  Type: Financial Guardian
  Tenant Isolation: CRYPTOGRAPHIC (AES-256-GCM per tenant)
  Execution Model: EVENT-DRIVEN + REQUEST-RESPONSE

CORE RESPONSIBILITIES (IMMUTABLE):
  1. Maintain authoritative wallet balance state (±0.001 currency units)
  2. Enforce ledger double-entry invariants
  3. Detect and prevent balance integrity violations
  4. Generate immutable audit logs for every state mutation
  5. Provide real-time balance queries with eventual consistency guarantees
  6. Block unauthorized balance modifications
  7. Perform daily reconciliation with source systems
  8. Alert on anomalous balance patterns
  9. Support multi-currency settlements
  10. Ensure ACID guarantees on balance operations

LOCKED BEHAVIORS:
  • NO balance mutations without ledger entry
  • NO balance queries return uncommitted state
  • NO cross-tenant balance visibility
  • NO negative balances (unless credit line defined)
  • NO floating-point rounding errors (use fixed decimal)
  • NO balance modification without actor attribution
  • NO transaction reversal without audit justification
  • NO concurrent balance updates (serialized via account lock)
  • NO balance export without compliance approval
  • NO balance zeroing without explicit admin override

FORBIDDEN OPERATIONS:
  ✗ Direct balance field updates
  ✗ Bulk balance resets
  ✗ Balance transfers without double-entry
  ✗ Off-ledger balance corrections
  ✗ Silent balance adjustments
  ✗ Cross-tenant balance merging
  ✗ Balance deletion
  ✗ Balance manipulation via side-channel
  ✗ Unlogged balance queries
  ✗ Balance access without permission check

ERROR HANDLING (MANDATORY):
  IF balance integrity violation detected
    → STOP operation immediately
    → LOG with CRITICAL severity
    → ALERT compliance team
    → MARK account as SUSPICIOUS
    → GENERATE incident report
    → DO NOT attempt automatic recovery

  IF concurrent update detected
    → QUEUE operation with exponential backoff
    → ENFORCE serialization via account lock
    → RETRY with pessimistic locking
    → ESCALATE if lock timeout > 30s

  IF cross-tenant data leak detected
    → KILL all agent instances
    → TRIGGER emergency shutdown
    → ALERT security team immediately
    → PRESERVE full execution context for forensics

STATE ISOLATION (CRYPTOGRAPHIC):
  Each tenant operates in isolated state machine:
    tenant_state[tenant_id] = ENCRYPT(state, tenant_secret_key)
    
  Tenant A cannot read tenant B state even in memory
  → use separate encrypted containers
  → use tenant-specific secret keys
  → use cryptographic isolation boundaries

AUDIT TRAIL (IMMUTABLE):
  Every operation generates:
    {
      timestamp: ISO8601_UTC,
      actor_id: authenticated_principal,
      actor_tenant: cryptographic_domain,
      operation: op_code,
      previous_balance: decimal128,
      balance_change: decimal128,
      new_balance: decimal128,
      ledger_entries: [debit, credit],
      reason_code: enum_value,
      ipv4_address: hashed,
      checksum: SHA256(entry)
    }

  Audit trail stored in APPEND_ONLY database
  → No updates, no deletes
  → Immutable timestamp enforced
  → Cryptographic chain per tenant
  → Compliance retention: 7 years minimum

REAL-TIME CONSISTENCY (EVENTUAL):
  1. Write operation:
     → LOCK account (pessimistic)
     → VALIDATE ledger entries
     → PERSIST to PostgreSQL (SERIALIZABLE isolation)
     → UNLOCK account
     → PUBLISH balance_updated event to Redis Streams
     
  2. Read operation:
     → CHECK Redis cache (1s TTL)
     → If miss → READ from PostgreSQL
     → RETURN balance with version timestamp
     → BACKGROUND SYNC cache if variance > 0.01%

COMPLIANCE GATES (ENFORCED):
  ✓ PCI-DSS: No plaintext balance in logs
  ✓ SOC2: Audit logs immutable & encrypted
  ✓ GDPR: Right to be forgotten NOT applied to ledger
  ✓ ISO27001: Encryption at rest + in transit
  ✓ Bank regulation: 10-digit precision (no rounding)
  ✓ Tax reporting: Balance snapshots daily @ UTC midnight

MONITORING & ALERTING (REAL-TIME):
  ALERT RULES:
    • Balance variance > 0.01%
    • Transaction count anomaly (3σ deviation)
    • Unauthorized access attempt → BLOCK + ALERT
    • Failed reconciliation for >1 hour
    • Concurrent update collision → exponential backoff
    • Audit log missing entry → CRITICAL
    • Cryptographic verification failure → LOCKDOWN
    • Cross-tenant leak attempt detected → EMERGENCY

TESTING REQUIREMENTS (MANDATORY):
  ✓ Unit tests: 100% coverage of balance operations
  ✓ Integration tests: Ledger consistency across operations
  ✓ Chaos tests: Network partition, clock skew, DB failure
  ✓ Concurrency tests: 10K concurrent updates same account
  ✓ Penetration tests: Cryptographic boundary tests
  ✓ Audit tests: Verify immutability of 1-year archive
  ✓ Compliance tests: PCI-DSS, SOC2 controls
  ✓ Disaster recovery: RPO < 1min, RTO < 5min

DEPLOYMENT (IMMUTABLE):
  ✓ Container image hash verified via SLSA L3
  ✓ Zero-trust network: Only API Gateway can call
  ✓ mTLS enforced between Agent ↔ Database
  ✓ Secrets injected via Vault (rotate every 30d)
  ✓ Deployment logged with approval chain
  ✓ Rollback procedure tested monthly
  ✓ Observability: OpenTelemetry traces, Prometheus metrics
  ✓ Rate limiting: 100 ops/sec per tenant

VERSION LOCK:
  Current Version: 1.0.0
  API Version: v1
  Data Schema Version: 3.1
  Backwards compatibility: Supported to v0.9.0
  Upgrade path: Append-only via schema migration
  Deprecation: Announced 6 months before removal

PROMPT INTEGRITY SEAL:
  This prompt was sealed on [DEPLOYMENT_DATE]
  Checksum: SHA256([SEALED_CONTENT_HASH])
  Signature: [CRYPTOGRAPHIC_SIGNATURE_HERE]
  
  Any modification to this prompt:
    → INVALIDATES the seal
    → TRIGGERS audit alert
    → REQUIRES new cryptographic signature
    → BLOCKED from execution until re-sealed
```

---

# 3️⃣ SECTION C — DATA MODEL (FROZEN SCHEMA)

## Entity-Relationship Model (IMMUTABLE)

### Primary Entities

```
Wallet (Master Account)
├── tenant_id: UUID (FK Tenant)
├── user_id: UUID (FK User)
├── wallet_id: UUID PK
├── account_type: ENUM('personal', 'corporate', 'escrow')
├── currency_code: CHAR(3) ('USD', 'EUR', 'INR', etc.)
├── current_balance: DECIMAL(19, 4) [64-bit fixed-point]
├── ledger_balance: DECIMAL(19, 4) [calculated from ledger]
├── variance: DECIMAL(19, 4) [current - ledger, must be 0]
├── credit_limit: DECIMAL(19, 4) [default 0 = no credit]
├── is_active: BOOLEAN
├── is_frozen: BOOLEAN
├── created_at: TIMESTAMP [UTC, immutable]
├── updated_at: TIMESTAMP [only via transaction commit]
├── version: BIGINT [optimistic locking]
├── last_reconciliation_at: TIMESTAMP [daily @ UTC midnight]
└── [ENCRYPTED_FIELDS]:
    - user_pii_hash
    - ip_address_hash
    - device_fingerprint_hash

LedgerEntry (Double-Entry Booking)
├── ledger_id: UUID PK
├── tenant_id: UUID (FK Tenant)
├── wallet_id: UUID (FK Wallet) [immutable]
├── transaction_id: UUID (FK Transaction)
├── entry_type: ENUM('DEBIT', 'CREDIT') [immutable]
├── amount: DECIMAL(19, 4) [immutable]
├── account_code: VARCHAR(20) [GL account]
├── description: TEXT
├── created_at: TIMESTAMP [immutable]
├── actor_id: UUID [who triggered entry]
├── actor_type: ENUM('USER', 'SYSTEM', 'API_INTEGRATION')
├── reason_code: VARCHAR(50)
├── checksum: CHAR(64) [SHA-256 of entry]
└── [APPEND_ONLY]: New fields only via schema migration

Transaction (Atomic Balance Operation)
├── transaction_id: UUID PK
├── tenant_id: UUID (FK Tenant)
├── wallet_id: UUID (FK Wallet)
├── transaction_type: ENUM('DEPOSIT', 'WITHDRAWAL', 'TRANSFER', 'REFUND', 'ADJUSTMENT')
├── amount: DECIMAL(19, 4)
├── currency_code: CHAR(3)
├── balance_before: DECIMAL(19, 4)
├── balance_after: DECIMAL(19, 4)
├── status: ENUM('PENDING', 'COMPLETED', 'FAILED', 'REVERSED')
├── external_reference: VARCHAR(100) [payment gateway ID]
├── metadata: JSONB {payload of transaction}
├── created_at: TIMESTAMP [UTC]
├── completed_at: TIMESTAMP [when status = COMPLETED]
├── initiated_by: UUID (FK User)
├── approved_by: UUID (FK User) [if manual override]
├── approval_reason: TEXT [mandatory if overridden]
├── idempotency_key: VARCHAR(128) [UUID of request]
├── retry_count: INT [0 initially]
├── last_error: TEXT [if failed]
├── ledger_debit_id: UUID (FK LedgerEntry)
├── ledger_credit_id: UUID (FK LedgerEntry)
└── [AUDIT_ENCRYPTED]:
    - payer_bank_hash
    - payee_bank_hash
    - original_ip_hash

BalanceAuditLog (Immutable Chronicle)
├── audit_id: UUID PK
├── tenant_id: UUID
├── wallet_id: UUID
├── timestamp: TIMESTAMP [UTC, immutable]
├── operation_type: VARCHAR(50)
├── actor_id: UUID
├── actor_principal: VARCHAR(100) [username/API key]
├── previous_balance: DECIMAL(19, 4)
├── balance_change: DECIMAL(19, 4)
├── new_balance: DECIMAL(19, 4)
├── ledger_integrity_check: BOOLEAN
├── variance_detected: DECIMAL(19, 4)
├── transaction_id: UUID (FK Transaction)
├── http_method: ENUM('GET', 'POST', 'PATCH', 'DELETE')
├── endpoint: VARCHAR(255)
├── http_status_code: INT
├── response_time_ms: INT
├── error_message: TEXT [if applicable]
├── device_fingerprint_hash: CHAR(64)
├── ip_address_hash: CHAR(64)
├── user_agent_hash: CHAR(64)
├── compliance_flags: JSONB {
    - is_pci_flagged: BOOLEAN,
    - fraud_score: DECIMAL(3,2),
    - requires_manual_review: BOOLEAN
  }
├── storage_location: VARCHAR(100) [physical DB shard]
└── [APPEND_ONLY, ENCRYPTED_AT_REST]

ReconciliationReport (Daily Settlement)
├── report_id: UUID PK
├── tenant_id: UUID
├── reconciliation_date: DATE [UTC, unique per tenant]
├── report_generated_at: TIMESTAMP
├── wallets_processed: INT
├── total_balance_sum: DECIMAL(19, 4)
├── total_ledger_sum: DECIMAL(19, 4)
├── variance: DECIMAL(19, 4) [must be 0.00]
├── transactions_processed: INT
├── double_entry_violations: INT [must be 0]
├── suspicious_activities: INT
├── recon_status: ENUM('PENDING', 'IN_PROGRESS', 'COMPLETED', 'FAILED_ALERT')
├── completed_at: TIMESTAMP
├── initiated_by: VARCHAR(100) [system or admin]
├── notes: TEXT [any discrepancies]
├── signature: CHAR(64) [HMAC of report]
└── [IMMUTABLE, LONG_TERM_ARCHIVE]

BalanceAlert (Real-Time Monitoring)
├── alert_id: UUID PK
├── tenant_id: UUID
├── wallet_id: UUID
├── alert_type: ENUM('VARIANCE', 'FRAUD_SCORE', 'LIMIT_EXCEEDED', 'UNAUTHORIZED_ACCESS')
├── severity: ENUM('LOW', 'MEDIUM', 'HIGH', 'CRITICAL')
├── triggered_at: TIMESTAMP
├── threshold_value: DECIMAL(19, 4)
├── actual_value: DECIMAL(19, 4)
├── alert_status: ENUM('OPEN', 'IN_REVIEW', 'RESOLVED', 'DISMISSED')
├── assigned_to: UUID [compliance officer]
├── investigation_notes: TEXT
├── action_taken: VARCHAR(255)
└── resolved_at: TIMESTAMP [if status = RESOLVED]
```

---

# 4️⃣ SECTION D — SEALED OPERATION PROMPTS

## 🔐 OPERATION 1: BALANCE_QUERY (Read-Only)

```
OPERATION_ID: OP_001_BALANCE_QUERY
CLASSIFICATION: READ_ONLY
AUTHORIZATION_TIER: AUTHENTICATED
TENANT_ISOLATION: CRYPTOGRAPHIC

SEALED PROMPT FOR AGENT:
=====================================
You are querying a wallet balance.

MANDATORY VALIDATIONS (in order):
  1. Verify caller's tenant_id matches wallet's tenant_id
     → IF mismatch: DENY, LOG as security violation, EXIT
  
  2. Verify caller has READ permission on wallet
     (user_id = wallet.user_id OR role.can_read_balance)
     → IF denied: DENY, LOG as unauthorized, EXIT
  
  3. Retrieve from Redis cache (key: wallet:{wallet_id})
     → IF cache_age < 1 second: RETURN with version
     → ELSE: proceed to step 4
  
  4. Query PostgreSQL with SERIALIZABLE isolation:
     SELECT * FROM Wallet WHERE wallet_id = ? FOR SHARE NOWAIT
     → IF locked by write operation: WAIT 5s max, RETRY
     → IF timeout: RETURN cached balance with stale flag
  
  5. Verify integrity:
     current_balance = SUM(ledger_credits) - SUM(ledger_debits)
     → IF variance > 0.01: LOG anomaly, ALERT, but still RETURN
  
  6. Construct response:
     {
       wallet_id,
       current_balance: amount,
       available_balance: amount - pending_holds,
       currency_code,
       version,
       timestamp: UTC_now,
       integrity_verified: true/false,
       last_mutation_timestamp
     }
  
  7. Log operation to BalanceAuditLog (ASYNC, non-blocking)
     → actor_id, timestamp, balance_amount, response_time
  
  8. Cache response in Redis (TTL=1s)
  
  9. RETURN response with HTTP 200

ERRORS:
  404 → wallet not found (after auth check)
  403 → insufficient permission
  503 → database unavailable (return cached with stale=true)
  500 → internal error (LOG, EXIT, no state mutation)

PERFORMANCE SLA:
  P95 latency: 50ms
  P99 latency: 200ms
  Availability: 99.99% (only DB unavailability allowed)
  Cache hit rate: 98%+ expected
=====================================
```

## 🔐 OPERATION 2: BALANCE_DEPOSIT (Write)

```
OPERATION_ID: OP_002_BALANCE_DEPOSIT
CLASSIFICATION: WRITE_FINANCIAL
AUTHORIZATION_TIER: AUTHENTICATED + VERIFIED_PAYMENT_SOURCE
TENANT_ISOLATION: HARD_BOUNDARY
IDEMPOTENCY: REQUIRED

SEALED PROMPT FOR AGENT:
=====================================
You are processing a deposit into a wallet.

PRE-OPERATION CHECKS:
  1. Verify idempotency:
     SELECT * FROM Transaction 
     WHERE idempotency_key = ? AND status = 'COMPLETED'
     → IF exists: RETURN cached response (HTTP 200)
  
  2. Validate tenant & caller:
     → If tenant_id mismatch: DENY
     → If caller != SYSTEM and no DEPOSIT permission: DENY
  
  3. Verify amount:
     → amount > 0: OK
     → amount = 0: REJECT (HTTP 400)
     → amount > 999,999.99: REQUIRE approval (MANUAL_REVIEW)
  
  4. Check wallet status:
     → wallet.is_active = true
     → wallet.is_frozen = false
     → IF failed: REJECT with reason
  
  5. Check deposit source:
     → payment_gateway verified and in allowlist
     → external_reference not duplicated
     → IF failed: REJECT, log as fraud signal

ATOMIC TRANSACTION (PostgreSQL SERIALIZABLE):
  BEGIN TRANSACTION;
  
  A. Create Transaction record (PENDING):
     INSERT INTO Transaction (
       transaction_id, wallet_id, amount, 
       status='PENDING', created_at=UTC_now,
       idempotency_key, external_reference,
       initiated_by, balance_before
     )
     RETURNING transaction_id;
  
  B. Lock account for write (PESSIMISTIC):
     SELECT * FROM Wallet 
     WHERE wallet_id = ? 
     FOR UPDATE NOWAIT;
     → IF timeout: ROLLBACK, return 429 (Too Many Requests)
  
  C. Validate balance doesn't exceed max:
     new_balance = wallet.current_balance + amount
     IF new_balance > 10_000_000.00:
       ROLLBACK
       ALERT to compliance team
       RETURN 400 (amount exceeds limit)
  
  D. Create ledger entries (DOUBLE-ENTRY):
     INSERT INTO LedgerEntry (
       wallet_id, entry_type='CREDIT', amount,
       account_code='1010_RECEIVABLE', transaction_id
     );
     INSERT INTO LedgerEntry (
       wallet_id, entry_type='DEBIT', amount,
       account_code='4000_DEPOSIT_REVENUE', transaction_id
     );
  
  E. Update Wallet balance:
     UPDATE Wallet 
     SET current_balance = current_balance + amount,
         updated_at = UTC_now,
         version = version + 1,
         last_reconciliation_at = UTC_now
     WHERE wallet_id = ?
     RETURNING current_balance, version;
  
  F. Mark Transaction as COMPLETED:
     UPDATE Transaction 
     SET status='COMPLETED',
         balance_after = new_balance,
         completed_at = UTC_now,
         ledger_debit_id = ?,
         ledger_credit_id = ?
     WHERE transaction_id = ?;
  
  COMMIT TRANSACTION;
  → ON COMMIT SUCCESS: proceed to step 6
  → ON CONFLICT: ROLLBACK, retry with exponential backoff
  → ON DEADLOCK: ROLLBACK, return 503, client retries

POST-OPERATION:
  6. Publish event to Redis Streams:
     XADD balance_events:* MAXLEN ~ 1000000 {
       event_type: 'DEPOSIT_COMPLETED',
       wallet_id, transaction_id, amount,
       timestamp, tenant_id
     }
  
  7. Async: Update cache (non-blocking)
     SET wallet:{wallet_id} → new_balance (TTL=1s)
  
  8. Async: Log to BalanceAuditLog
     INSERT INTO BalanceAuditLog (
       wallet_id, operation_type='DEPOSIT',
       previous_balance, balance_change=amount,
       new_balance, actor_id, timestamp,
       compliance_flags (fraud_score, etc.)
     );
  
  9. Async: Run fraud checks
     IF fraud_score(transaction) > 0.7:
       INSERT INTO BalanceAlert (
         wallet_id, alert_type='FRAUD_SCORE',
         severity='HIGH', actual_value=fraud_score
       );
  
  10. Construct response:
      {
        transaction_id,
        wallet_id,
        amount_deposited: amount,
        new_balance: updated_balance,
        currency_code,
        timestamp: UTC_now,
        status: 'COMPLETED'
      }
  
  11. RETURN HTTP 201 (Created)

ERROR HANDLING:
  400 → validation failed (amount, source, etc.)
  403 → insufficient permission
  409 → duplicate transaction (idempotency)
  429 → too many concurrent requests
  503 → database unavailable (ROLLBACK, client retries)
  500 → internal error (ROLLBACK, log, EXIT)
  
ATOMICITY GUARANTEE:
  Either:
    a) All ledger entries created + balance updated + transaction COMPLETED
    b) None of the above (full ROLLBACK)
  
  No partial states allowed.

PERFORMANCE TARGETS:
  P95 latency: 150ms
  P99 latency: 500ms
  Throughput: 1000 deposits/sec per tenant
=====================================
```

## 🔐 OPERATION 3: BALANCE_WITHDRAWAL (Write)

```
OPERATION_ID: OP_003_BALANCE_WITHDRAWAL
CLASSIFICATION: WRITE_FINANCIAL
AUTHORIZATION_TIER: AUTHENTICATED + MFA_VERIFIED
TENANT_ISOLATION: HARD_BOUNDARY
IDEMPOTENCY: REQUIRED

SEALED PROMPT FOR AGENT:
=====================================
You are processing a withdrawal from a wallet.

PRE-OPERATION CHECKS:
  1. Verify idempotency:
     SELECT * FROM Transaction 
     WHERE idempotency_key = ? 
     AND status IN ('COMPLETED', 'FAILED')
     → IF COMPLETED exists: RETURN cached response
  
  2. Validate caller:
     → caller.tenant_id = wallet.tenant_id
     → caller.user_id = wallet.user_id (only owner can withdraw)
     → caller MFA status = VERIFIED (within 5 minutes)
     → IF any check fails: DENY, ALERT
  
  3. Verify withdrawal destination:
     → bank_account is whitelisted
     → destination_country != blacklist
     → destination_mismatch_check passed
     → IF failed: REJECT with reason
  
  4. Validate amount:
     → amount > 0: OK
     → amount = 0: REJECT
     → amount > 50,000: REQUIRE approval (MANUAL_REVIEW) + LOG
     → amount > wallet.current_balance: REJECT with reason
  
  5. Check available balance:
     available = current_balance - pending_holds - credit_reserve
     IF amount > available:
       REJECT (HTTP 400, explain why)
  
  6. Check wallet status:
     → wallet.is_active = true
     → wallet.is_frozen = false
     → IF failed: REJECT
  
  7. Check daily withdrawal limit per user:
     SELECT SUM(amount) FROM Transaction 
     WHERE wallet_id = ? 
     AND DATE(created_at) = TODAY
     AND status = 'COMPLETED'
     AND transaction_type = 'WITHDRAWAL'
     daily_total = ?
     IF daily_total + amount > DAILY_LIMIT:
       REJECT (HTTP 429, explain limit)

ATOMIC TRANSACTION (PostgreSQL SERIALIZABLE):
  BEGIN TRANSACTION;
  
  A. Create Transaction record (PENDING):
     INSERT INTO Transaction (
       transaction_id, wallet_id, amount,
       status='PENDING', created_at=UTC_now,
       transaction_type='WITHDRAWAL',
       idempotency_key, external_reference,
       initiated_by=caller.user_id,
       balance_before=wallet.current_balance,
       metadata={
         destination_bank_hash,
         destination_country,
         withdrawal_reason
       }
     )
     RETURNING transaction_id;
  
  B. Lock account (PESSIMISTIC, with timeout):
     SELECT * FROM Wallet 
     WHERE wallet_id = ? 
     FOR UPDATE NOWAIT;
     → IF timeout: ROLLBACK, return 429
  
  C. Re-verify balance (check-and-act):
     SELECT current_balance FROM Wallet WHERE wallet_id = ?
     → IF current_balance < amount: ROLLBACK, return 400
  
  D. Create ledger entries (DOUBLE-ENTRY):
     INSERT INTO LedgerEntry (
       wallet_id, entry_type='DEBIT', amount,
       account_code='2000_LIABILITIES', transaction_id
     );
     INSERT INTO LedgerEntry (
       wallet_id, entry_type='CREDIT', amount,
       account_code='5000_WITHDRAWAL_EXPENSE', transaction_id
     );
  
  E. Update Wallet balance:
     UPDATE Wallet 
     SET current_balance = current_balance - amount,
         updated_at = UTC_now,
         version = version + 1
     WHERE wallet_id = ?
     RETURNING current_balance, version;
     → IF current_balance < 0: ROLLBACK (safety check)
  
  F. Update Transaction to COMPLETED:
     UPDATE Transaction 
     SET status='COMPLETED',
         balance_after = new_balance,
         completed_at = UTC_now,
         ledger_debit_id = ?,
         ledger_credit_id = ?
     WHERE transaction_id = ?;
  
  COMMIT TRANSACTION;
  → ON SUCCESS: proceed to step 8
  → ON CONFLICT: ROLLBACK, exponential backoff
  → ON DEADLOCK: ROLLBACK, return 503

POST-OPERATION:
  8. Publish event (async, non-blocking):
     XADD balance_events:* {
       event_type: 'WITHDRAWAL_COMPLETED',
       wallet_id, transaction_id, amount,
       timestamp, tenant_id
     }
  
  9. Async: Update cache
     SET wallet:{wallet_id} → new_balance (TTL=1s)
  
  10. Async: Log audit
      INSERT INTO BalanceAuditLog (...)
  
  11. Async: Check for suspicious patterns
      IF withdrawal_amount > (monthly_avg * 3):
        INSERT INTO BalanceAlert (
          alert_type='UNUSUAL_ACTIVITY',
          severity='MEDIUM'
        );
  
  12. Construct response:
      {
        transaction_id,
        wallet_id,
        amount_withdrawn: amount,
        new_balance: updated_balance,
        currency_code,
        status: 'COMPLETED',
        destination_reference: ref,
        estimated_settlement: T+1_business_day,
        timestamp: UTC_now
      }
  
  13. RETURN HTTP 201

ERROR HANDLING:
  400 → amount invalid, insufficient balance
  403 → not owner, MFA not verified
  409 → duplicate, daily limit exceeded
  429 → too many requests, account locked
  503 → database unavailable
  500 → internal error

ATOMICITY:
  All or nothing: ledger entries + balance update + transaction COMPLETED
=====================================
```

## 🔐 OPERATION 4: BALANCE_TRANSFER (Write, Cross-Wallet)

```
OPERATION_ID: OP_004_BALANCE_TRANSFER
CLASSIFICATION: WRITE_FINANCIAL
AUTHORIZATION_TIER: AUTHENTICATED
TENANT_ISOLATION: SAME_TENANT_ONLY
IDEMPOTENCY: REQUIRED

SEALED PROMPT FOR AGENT:
=====================================
You are transferring balance between two wallets
(both must be in the same tenant).

PRE-OPERATION CHECKS:
  1. Verify idempotency:
     SELECT * FROM Transaction 
     WHERE idempotency_key = ? AND status = 'COMPLETED'
     → IF exists: RETURN cached response
  
  2. Validate tenant isolation:
     source_wallet.tenant_id = dest_wallet.tenant_id = caller.tenant_id
     → IF any mismatch: DENY with CRITICAL log
  
  3. Verify permissions:
     caller.user_id = source_wallet.user_id (only owner can initiate)
     → IF not: DENY
  
  4. Validate wallets:
     source_wallet.is_active = true
     source_wallet.is_frozen = false
     dest_wallet.is_active = true
     dest_wallet.is_frozen = false
     source_wallet.currency = dest_wallet.currency
     → IF any fails: REJECT
  
  5. Validate amount:
     amount > 0
     amount <= source_wallet.current_balance
     amount <= TRANSFER_LIMIT_PER_TX
     → IF fails: REJECT

ATOMIC TRANSACTION (PostgreSQL SERIALIZABLE):
  BEGIN TRANSACTION;
  
  A. Create Transaction record:
     INSERT INTO Transaction (
       transaction_id, wallet_id=source_wallet_id,
       status='PENDING', amount,
       transaction_type='TRANSFER',
       metadata={destination_wallet_id, ...}
     );
  
  B. Lock both wallets (source first, then dest):
     SELECT * FROM Wallet WHERE wallet_id = source_id
     FOR UPDATE NOWAIT;
     → IF timeout: ROLLBACK, return 429
     
     SELECT * FROM Wallet WHERE wallet_id = dest_id
     FOR UPDATE NOWAIT;
     → IF timeout: ROLLBACK, return 429
  
  C. Verify source has sufficient balance:
     SELECT current_balance FROM Wallet WHERE wallet_id = source_id
     IF balance < amount: ROLLBACK, return 400
  
  D. Create ledger entries (4 entries total):
     -- Source debit:
     INSERT INTO LedgerEntry (
       wallet_id=source_id, entry_type='DEBIT',
       amount, account_code='3000_TRANSFER_OUT'
     );
     -- Source credit (to destination):
     INSERT INTO LedgerEntry (
       wallet_id=source_id, entry_type='CREDIT',
       amount, account_code='1000_AR_TRANSFER'
     );
     -- Destination debit (from source):
     INSERT INTO LedgerEntry (
       wallet_id=dest_id, entry_type='DEBIT',
       amount, account_code='1010_AR_FROM_TRANSFER'
     );
     -- Destination credit:
     INSERT INTO LedgerEntry (
       wallet_id=dest_id, entry_type='CREDIT',
       amount, account_code='4100_TRANSFER_IN'
     );
  
  E. Update both wallet balances:
     UPDATE Wallet 
     SET current_balance = current_balance - amount
     WHERE wallet_id = source_id;
     
     UPDATE Wallet 
     SET current_balance = current_balance + amount
     WHERE wallet_id = dest_id;
  
  F. Mark Transaction COMPLETED:
     UPDATE Transaction SET status='COMPLETED', ...;
  
  COMMIT TRANSACTION;

POST-OPERATION:
  G. Publish dual events:
     XADD balance_events:* {event_type: 'TRANSFER_OUT', ...}
     XADD balance_events:* {event_type: 'TRANSFER_IN', ...}
  
  H. Async: Update both cache entries
  
  I. Async: Log audit entries for both wallets
  
  J. RETURN HTTP 201

ATOMICITY:
  Both wallets update or neither updates.
  Ledger entries match perfectly.
=====================================
```

## 🔐 OPERATION 5: BALANCE_RECONCILIATION (Daily, Batch)

```
OPERATION_ID: OP_005_BALANCE_RECONCILIATION
CLASSIFICATION: SYSTEM_BATCH
AUTHORIZATION_TIER: SYSTEM_ONLY
TENANT_ISOLATION: PER_TENANT
SCHEDULING: DAILY_UTC_MIDNIGHT

SEALED PROMPT FOR AGENT:
=====================================
You are running the daily reconciliation batch job
to verify all wallet balances match ledger sums.

PRECONDITIONS:
  → Current time: UTC 00:00:01 (triggered by cron)
  → Previous reconciliation: completed successfully
  → No in-flight transactions for previous day

RECONCILIATION ALGORITHM (NON-BLOCKING):
  1. Query all wallets for tenant:
     SELECT wallet_id, current_balance, version
     FROM Wallet WHERE tenant_id = ?
     ORDER BY wallet_id;
  
  2. For each wallet:
     A. Calculate expected balance from ledger:
        ledger_sum = (
          SELECT SUM(CASE 
            WHEN entry_type='CREDIT' THEN amount
            WHEN entry_type='DEBIT' THEN -amount
          END)
          FROM LedgerEntry 
          WHERE wallet_id = ?
          AND created_at < TODAY_00_00_00
        )
     
     B. Compare:
        variance = wallet.current_balance - ledger_sum
        IF ABS(variance) > 0.01:
          INSERT INTO BalanceAlert (
            wallet_id,
            alert_type='VARIANCE',
            severity='CRITICAL',
            previous_balance=wallet.current_balance,
            actual_value=ledger_sum,
            variance=variance
          );
          Continue (don't block reconciliation)
     
     C. Verify double-entry invariant:
        debit_sum = SUM(amount) WHERE entry_type='DEBIT'
        credit_sum = SUM(amount) WHERE entry_type='CREDIT'
        IF ABS(debit_sum - credit_sum) > 0.01:
          INSERT INTO BalanceAlert (
            alert_type='DOUBLE_ENTRY_VIOLATION'
          );
  
  3. Create ReconciliationReport:
     INSERT INTO ReconciliationReport (
       report_id, tenant_id,
       reconciliation_date=TODAY,
       wallets_processed=count,
       variance=total_variance,
       double_entry_violations=count,
       suspicious_activities=count,
       recon_status='COMPLETED',
       completed_at=UTC_now,
       signature=HMAC_SHA256(report_data)
     );
  
  4. Publish event:
     XADD reconciliation_events:* {
       event_type: 'DAILY_RECON_COMPLETED',
       tenant_id, report_id, variance, timestamp
     }
  
  5. Async: Send report to compliance team (email)
  
  6. Log completion:
     INSERT INTO BalanceAuditLog (
       operation_type='RECONCILIATION',
       ...
     );

FAILURE HANDLING:
  IF database unavailable:
    → ABORT job
    → ALERT ops team
    → Job will retry at 01:00 UTC
  
  IF queries timeout:
    → CONTINUE with timeout
    → Mark report as PARTIAL
    → ALERT compliance

PERFORMANCE TARGET:
  Must complete within 1 hour for 10M wallets
  Parallelization: by wallet_id range (sharding)
=====================================
```

---

# 5️⃣ SECTION E — API SPECIFICATION (OPENAPI 3.1 LOCKED)

## Endpoint Contracts

```yaml
openapi: 3.1.0
info:
  title: Antigravity Wallet Balance Tracking API
  version: 1.0.0
  description: >
    Sealed & locked financial API for wallet balance tracking.
    All operations audit-logged, immutable, PCI-DSS compliant.

servers:
  - url: https://wallet-api.antigravity.io/v1
    description: Production

tags:
  - name: Balance Operations
    description: Read and write wallet balance operations

paths:
  /wallets/{wallet_id}/balance:
    get:
      operationId: getWalletBalance
      tags: [Balance Operations]
      summary: Get current wallet balance
      parameters:
        - name: wallet_id
          in: path
          required: true
          schema: {type: string, format: uuid}
        - name: include_ledger_variance
          in: query
          schema: {type: boolean, default: false}
      responses:
        200:
          description: Balance retrieved
          content:
            application/json:
              schema:
                type: object
                properties:
                  wallet_id: {type: string, format: uuid}
                  current_balance: {type: number, format: decimal}
                  available_balance: {type: number, format: decimal}
                  currency_code: {type: string, minLength: 3, maxLength: 3}
                  version: {type: integer}
                  timestamp: {type: string, format: date-time}
                  integrity_verified: {type: boolean}
        403:
          description: Insufficient permission
        404:
          description: Wallet not found
        503:
          description: Service unavailable (check status page)
      security:
        - bearerAuth: []

  /wallets/{wallet_id}/deposit:
    post:
      operationId: depositToWallet
      tags: [Balance Operations]
      summary: Deposit funds to wallet
      parameters:
        - name: wallet_id
          in: path
          required: true
          schema: {type: string, format: uuid}
      requestBody:
        required: true
        content:
          application/json:
            schema:
              type: object
              required: [amount, currency_code, external_reference, idempotency_key]
              properties:
                amount: {type: number, format: decimal, minimum: 0.01}
                currency_code: {type: string, minLength: 3, maxLength: 3}
                external_reference: {type: string, maxLength: 100}
                idempotency_key: {type: string, format: uuid}
                source_account: {type: string, maxLength: 100}
                deposit_reason: {type: string, enum: [DIRECT_DEPOSIT, PROMOTION, REFUND, TRANSFER_IN]}
      responses:
        201:
          description: Deposit completed
          content:
            application/json:
              schema:
                type: object
                properties:
                  transaction_id: {type: string, format: uuid}
                  wallet_id: {type: string, format: uuid}
                  amount_deposited: {type: number, format: decimal}
                  new_balance: {type: number, format: decimal}
                  status: {type: string, enum: [COMPLETED]}
                  timestamp: {type: string, format: date-time}
        400:
          description: Invalid request (amount validation, etc.)
        409:
          description: Duplicate transaction (idempotency key exists)
        503:
          description: Database unavailable
      security:
        - bearerAuth: []

  /wallets/{wallet_id}/withdraw:
    post:
      operationId: withdrawFromWallet
      tags: [Balance Operations]
      summary: Withdraw funds from wallet
      parameters:
        - name: wallet_id
          in: path
          required: true
          schema: {type: string, format: uuid}
      requestBody:
        required: true
        content:
          application/json:
            schema:
              type: object
              required: [amount, currency_code, idempotency_key, destination_bank_account]
              properties:
                amount: {type: number, format: decimal, minimum: 0.01}
                currency_code: {type: string, minLength: 3, maxLength: 3}
                destination_bank_account: {type: string, maxLength: 100}
                destination_country: {type: string, minLength: 2, maxLength: 2}
                idempotency_key: {type: string, format: uuid}
                withdrawal_reason: {type: string, enum: [CASH_OUT, TRANSFER_OUT, PAYMENT]}
      responses:
        201:
          description: Withdrawal initiated
          content:
            application/json:
              schema:
                type: object
                properties:
                  transaction_id: {type: string, format: uuid}
                  amount_withdrawn: {type: number, format: decimal}
                  new_balance: {type: number, format: decimal}
                  status: {type: string, enum: [COMPLETED]}
                  estimated_settlement: {type: string, format: date}
        400:
          description: Insufficient balance or invalid amount
        403:
          description: MFA required or not verified
        429:
          description: Daily limit exceeded or account locked
      security:
        - bearerAuth: []

  /wallets/{wallet_id}/transfer:
    post:
      operationId: transferBetweenWallets
      tags: [Balance Operations]
      summary: Transfer between two wallets (same tenant)
      parameters:
        - name: wallet_id
          in: path
          required: true
          schema: {type: string, format: uuid}
      requestBody:
        required: true
        content:
          application/json:
            schema:
              type: object
              required: [amount, destination_wallet_id, idempotency_key]
              properties:
                amount: {type: number, format: decimal, minimum: 0.01}
                destination_wallet_id: {type: string, format: uuid}
                idempotency_key: {type: string, format: uuid}
      responses:
        201:
          description: Transfer completed
      security:
        - bearerAuth: []

  /wallets/{wallet_id}/transactions:
    get:
      operationId: getWalletTransactionHistory
      tags: [Balance Operations]
      summary: Get transaction history
      parameters:
        - name: wallet_id
          in: path
          required: true
          schema: {type: string, format: uuid}
        - name: limit
          in: query
          schema: {type: integer, default: 100, maximum: 1000}
        - name: offset
          in: query
          schema: {type: integer, default: 0}
        - name: transaction_type
          in: query
          schema: {type: string, enum: [DEPOSIT, WITHDRAWAL, TRANSFER, REFUND, ADJUSTMENT]}
      responses:
        200:
          description: Transaction history
          content:
            application/json:
              schema:
                type: object
                properties:
                  total_count: {type: integer}
                  transactions:
                    type: array
                    items:
                      type: object
                      properties:
                        transaction_id: {type: string, format: uuid}
                        amount: {type: number, format: decimal}
                        type: {type: string}
                        status: {type: string}
                        created_at: {type: string, format: date-time}
      security:
        - bearerAuth: []

components:
  securitySchemes:
    bearerAuth:
      type: http
      scheme: bearer
      bearerFormat: JWT
```

---

# 6️⃣ SECTION F — COMPLIANCE & AUDIT (IMMUTABLE)

## Compliance Framework

```
COMPLIANCE_TIER = MULTI_STANDARD

┌─ PCI DSS 3.2.1 ─────────────────────────────────┐
│ Requirement 3: Protected Cardholder Data         │
│  ✓ No plaintext balances in logs                 │
│  ✓ No PAN in transaction metadata                │
│  ✓ Encryption in transit (TLS 1.3)               │
│  ✓ Encryption at rest (AES-256-GCM)              │
│                                                   │
│ Requirement 6: Maintain a Vulnerability Program  │
│  ✓ Annual penetration test (wallet isolation)    │
│  ✓ Code review before production deploy          │
│  ✓ Dependency scanning (SBOM maintained)         │
│                                                   │
│ Requirement 8: Identify & Authenticate           │
│  ✓ MFA for withdrawals > $1,000                  │
│  ✓ Audit log all balance modifications           │
│  ✓ Activity logging 6.5+ (detailed commands)     │
│                                                   │
│ Requirement 10: Track & Monitor Access           │
│  ✓ Immutable audit log (7-year retention)        │
│  ✓ Real-time alerting (variance, fraud)          │
│  ✓ Daily reconciliation report                   │
│  ✓ Access logs encrypted, tamper-evident         │
│                                                   │
│ Requirement 12: Maintain Security Policy         │
│  ✓ This document = security policy               │
│  ✓ Least privilege enforced in code              │
│  ✓ Annual review scheduled (April)               │
└───────────────────────────────────────────────────┘

┌─ SOC 2 Type II ──────────────────────────────────┐
│ Security Principle: Authorized access controlled │
│  ✓ RBAC matrix frozen in code                    │
│  ✓ RBAC changes via deployment pipeline          │
│  ✓ Quarterly access review (audit)               │
│                                                   │
│ Availability Principle: System available         │
│  ✓ RTO: 5 minutes (failover to standby)         │
│  ✓ RPO: 1 minute (Redis replication)            │
│  ✓ 99.99% SLA monitored (PagerDuty)             │
│                                                   │
│ Processing Integrity: Data accurate              │
│  ✓ Double-entry ledger (GL audit required)      │
│  ✓ Reconciliation: daily @ UTC midnight          │
│  ✓ Variance alert: if > 0.01%                   │
│                                                   │
│ Confidentiality: Data protected from disclosure  │
│  ✓ Encryption: AES-256 at rest                  │
│  ✓ Encryption: TLS 1.3 in transit               │
│  ✓ Key rotation: every 90 days                  │
│                                                   │
│ Privacy: Personal data handled correctly         │
│  ✓ Purpose limitation: balance only              │
│  ✓ Data retention: 7 years (ledger)              │
│  ✓ GDPR not-applicable (no deletion of ledger)  │
└───────────────────────────────────────────────────┘

┌─ GDPR (EU Residents) ────────────────────────────┐
│ Article 17 (Right to be Forgotten)               │
│  → DOES NOT APPLY to financial ledger            │
│  → Ledger immutable for 7 years (tax law)        │
│  → Can anonymize PII in user table               │
│  → Ledger entries remain as historical record    │
│                                                   │
│ Article 5 (Lawfulness of Processing)             │
│  ✓ Balance needed for service delivery           │
│  ✓ Legal basis: contract performance             │
│  ✓ Consent: from account signup (ToS)           │
│                                                   │
│ Article 32 (Security Measures)                   │
│  ✓ Encryption, access control, monitoring        │
│  ✓ Data protection impact assessment (DPIA)      │
│  ✓ Annual DPA review (Q1)                       │
└───────────────────────────────────────────────────┘

┌─ ISO 27001 ──────────────────────────────────────┐
│ Control A.5: Access Control                      │
│  ✓ RBAC matrix enforced in code                  │
│  ✓ Tenant isolation: cryptographic boundary      │
│  ✓ Quarterly audit (access matrix review)        │
│                                                   │
│ Control A.10: Cryptography                       │
│  ✓ AES-256-GCM at rest (per-tenant keys)        │
│  ✓ TLS 1.3 in transit (HSTS header)             │
│  ✓ Key management: Vault + HSM (future)         │
│                                                   │
│ Control A.12: Logging & Monitoring               │
│  ✓ All balance operations logged                 │
│  ✓ Real-time anomaly detection                  │
│  ✓ Immutable audit trail (WORM storage)         │
└───────────────────────────────────────────────────┘
```

## Audit Log Specification

```
AUDIT_LOG_PROPERTIES:

1. Immutability:
   → Append-only storage (PostgreSQL INSERT-only)
   → No UPDATE, DELETE, or TRUNCATE allowed
   → Table-level constraints enforce immutability

2. Encryption:
   → Each tenant_id encrypted with master key
   → Log entries encrypted at rest (AES-256-GCM)
   → Keys rotated every 90 days
   → Rotation tracked in key_version field

3. Retention:
   → Minimum: 7 years (tax law requirement)
   → Archive: Move to cold storage (S3) after 2 years
   → Lifecycle: Quarterly review of old entries
   → Deletion: FORBIDDEN (for 7 years)

4. Integrity:
   → HMAC-SHA256 on each entry
   → Chain: entry[n].parent_hash = HMAC(entry[n-1])
   → Verification: Daily (cron job)
   → Tampering: Detectable (chain breaks)

5. Access Control:
   → Compliance officer: READ (full history)
   → Finance team: READ (filtered by date)
   → User: READ (only own transactions)
   → Developer: NO direct READ
   → Admin: DELETE forbidden always

6. Compliance Reporting:
   → Daily: Reconciliation report
   → Weekly: Variance alerts
   → Monthly: Access summary
   → Quarterly: Audit review
   → Annual: External auditor access
```

---

# 7️⃣ SECTION G — TESTING & VALIDATION (MANDATORY)

## Test Coverage Matrix

```
TEST_CATEGORY         COVERAGE    TOOLS
─────────────────────────────────────────────
Unit Tests            100%        pytest + hypothesis
Integration Tests     95%+        pytest-asyncio
Concurrency Tests     Extensive   locust + chaos toolkit
Chaos Tests           5 scenarios kube-chaos
Penetration Tests     Annual      OWASP Top 10
Compliance Tests      Automated   custom validators
Performance Tests     Monthly     k6 load testing
Security Tests        Annual      SAST + DAST

UNIT TEST CHECKLIST:
✓ Balance query (cache miss, cache hit, stale)
✓ Deposit (positive, zero, negative, limit)
✓ Withdrawal (sufficient, insufficient, limits)
✓ Transfer (same tenant, cross-tenant, same user)
✓ Ledger integrity (double-entry, variance)
✓ Reconciliation (pass, fail, variance)
✓ Error handling (database down, timeout)
✓ Idempotency (duplicate requests)
✓ Tenant isolation (cannot access other tenant)
✓ Fraud detection (anomaly scoring)

CONCURRENCY TEST CHECKLIST:
✓ 10K concurrent deposits same account
✓ Race condition: withdrawal + transfer
✓ Deadlock: 2-wallet transfers (circular)
✓ Lock timeout: extend timeout, backoff
✓ Optimistic locking: version conflicts
✓ Pessimistic locking: deadlock prevention
✓ Audit log consistency under load

CHAOS TEST CHECKLIST:
✓ Database partition (split-brain)
✓ Clock skew (system time jump)
✓ Network latency (500ms+ added)
✓ Memory pressure (OOM killer triggered)
✓ Disk full (storage layer fails)
```

---

# 8️⃣ SECTION H — MONITORING & ALERTING (REAL-TIME)

## Metrics & SLOs

```
METRIC                          P95 TARGET    P99 TARGET    ALERT THRESHOLD
────────────────────────────────────────────────────────────────────────────
Balance Query Latency           50ms          200ms         > 500ms
Deposit Latency                 150ms         500ms         > 1s
Withdrawal Latency              150ms         500ms         > 1s
Transfer Latency                200ms         750ms         > 1.5s
Reconciliation Duration         < 1h          < 1h          > 1.5h
Error Rate (5xx)                < 0.01%       < 0.05%       > 0.1%
Audit Log Latency               < 100ms       < 500ms       > 1s
Cache Hit Rate                  95%+          90%+          < 80%
Transaction Throughput          1000/sec      500/sec       < 300/sec
Ledger Variance Detection       < 1s          < 5s          > 10s
Database Connection Pool        80%           90%           > 95%
────────────────────────────────────────────────────────────────────────────

SLO (Service Level Objectives):
  Availability: 99.99% (52.6 min downtime/year)
  Latency P95:  50ms for reads, 150ms for writes
  Error Budget: 21.6 minutes/month for 99.99%
  
ALERTING RULES:

1. CRITICAL (page on-call):
   • Balance query latency > 500ms (5 min sustained)
   • Deposit/withdrawal failure rate > 1%
   • Reconciliation FAILED status
   • Ledger variance detected (> 0.01)
   • Cross-tenant data leak attempt
   • Audit log integrity failure
   • Database unreachable

2. HIGH (escalate to team lead):
   • Error rate > 0.1% (5 min window)
   • Latency P99 > 1s (10 min sustained)
   • Fraud score anomaly (top 1%)
   • Lock contention > 100ms (avg)
   • Cache hit rate < 80%

3. MEDIUM (track in Slack):
   • Variance threshold exceeded (< 0.01)
   • MFA failures > 5/hour
   • Rate limit hits (customer behavior)
   • Unusual transfer patterns

4. INFORMATIONAL (logging only):
   • Transaction volume spikes
   • Cache misses trending up
   • Query performance degradation
```

---

# 9️⃣ SECTION I — DEPLOYMENT & SECURITY (HARDENED)

## Deployment Checklist

```
PRE-DEPLOYMENT:
  ☑ Code review: 2 approvals minimum
  ☑ SAST scan: SonarQube passed
  ☑ Dependency scan: Trivy scan clean
  ☑ Unit tests: 100% passing
  ☑ Integration tests: All green
  ☑ Performance baseline: Meets SLA
  ☑ Compliance check: PCI-DSS controls verified
  ☑ Security review: No vulnerabilities
  ☑ Changelog: documented (git tag)
  
BUILD:
  ☑ Container build: Distroless base
  ☑ SBOM generated: CycloneDX format
  ☑ Image signed: Cosign + private key
  ☑ Provenance: SLSA L3 (build attestation)
  ☑ Registry scan: Trivy + Grype
  ☑ Push to GHCR: with digest reference
  
DEPLOYMENT (BLUE-GREEN):
  ☑ Scale blue to 50%
  ☑ Health check: all endpoints pass
  ☑ Smoke test: 100 requests per endpoint
  ☑ Scale blue to 100%
  ☑ Scale green to 0% (after 5 min)
  ☑ Rollback procedure: tested
  ☑ Logging active: no errors expected
  ☑ Alerts cleared: no false positives
  
POST-DEPLOYMENT:
  ☑ Smoke tests: all green
  ☑ Audit log: deployment event recorded
  ☑ Compliance report: generated & verified
  ☑ Monitoring: dashboards updated
  ☑ Runbook: updated if needed
  ☑ Notification: team informed (Slack)
  ☑ Follow-up: 24h stability check

ROLLBACK PROCEDURE:
  IF critical issue detected:
    1. STOP accepting new transactions
    2. Alert on-call engineer + manager
    3. Roll back to previous version (blue-green reversal)
    4. Verify audit log consistency
    5. Run reconciliation
    6. Communicate outage window
    7. Post-mortem (24h)

SECURITY HARDENING:
  Network:
    ✓ TLS 1.3 only (no TLS 1.2)
    ✓ mTLS between services
    ✓ Network policies: default DENY
    ✓ Rate limiting: 100 requests/sec per IP
    ✓ DDoS protection: Cloudflare
  
  Container:
    ✓ Non-root user (UID 1000)
    ✓ Read-only filesystem (except /tmp)
    ✓ No privileged mode
    ✓ Resource limits: CPU 1000m, Memory 2Gi
    ✓ Security context: drop all capabilities
  
  Secrets:
    ✓ Injected via Vault (no env vars)
    ✓ Key rotation: every 30 days
    ✓ Audit trail: who accessed secrets
    ✓ Expiration: secrets expire after 30d
  
  Compliance:
    ✓ FIPS 140-2 validated crypto (future)
    ✓ HSM for key storage (future)
    ✓ Quantum-resistant algorithms (future)
```

---

# 🔟 SECTION J — INCIDENT RESPONSE (SEALED)

## Runbook for Critical Scenarios

```
SCENARIO 1: Balance Variance Detected (> 0.01)
────────────────────────────────────────────────
1. Alert fires → on-call engineer paged
2. Immediately:
   a. STOP new balance operations (circuit breaker)
   b. LOCK affected wallets
   c. Export audit log for variance period
   d. Run manual reconciliation query
   
3. Investigation:
   a. Check ledger_sum vs current_balance
   b. Scan transaction log for anomalies
   c. Verify no data corruption
   
4. Resolution:
   a. If ledger corrupted: restore from backup
   b. If balance wrong: adjust via audit-logged override
   c. Create incident report
   
5. Restart:
   a. Clear circuit breaker
   b. UNLOCK wallets
   c. Run full reconciliation
   d. Verify alerts cleared

SCENARIO 2: Cross-Tenant Data Leak (Security)
───────────────────────────────────────────────
1. Detection:
   a. Unauthorized access attempt logged
   b. Alert fires immediately
   
2. Immediate Response:
   a. KILL all agent instances
   b. Trigger emergency shutdown
   c. Page security team (PagerDuty)
   d. Alert CEO (if public data exposed)
   
3. Investigation:
   a. Preserve all logs (no cleanup)
   b. Determine scope of exposure
   c. Identify root cause
   
4. Communication:
   a. Notify affected customers
   b. Contact regulatory bodies (if required)
   c. Legal review required
   
5. Remediation:
   a. Fix vulnerability
   b. Audit all access logs
   c. Reset encryption keys
   d. Redeploy with full testing

SCENARIO 3: Database Unreachable (Infrastructure)
──────────────────────────────────────────────────
1. Detection:
   a. Connection pool exhausted
   b. Query timeout > 30s
   c. Alert fires (CRITICAL)
   
2. Immediate:
   a. Return cached balance (with stale flag)
   b. QUEUE pending operations (in-memory, non-persistent)
   c. Alert ops team
   
3. Investigation:
   a. Check DB connectivity
   b. Check DB logs for errors
   c. Check network paths
   
4. Recovery:
   a. Restart database (if applicable)
   b. Fail over to standby (if configured)
   c. Drain operation queue (when DB online)
   
5. Verification:
   a. Run reconciliation
   b. Verify no duplicate transactions
   c. Verify audit log intact

SCENARIO 4: Concurrency Deadlock (Application)
────────────────────────────────────────────────
1. Detection:
   a. Lock timeout exceeded (30s)
   b. Exponential backoff engaged
   c. Alert fires (HIGH)
   
2. Immediate:
   a. Return 503 to caller (client retries)
   b. Log deadlock pattern
   
3. Investigation:
   a. Analyze lock order in code
   b. Check for 2-wallet transfer cycles
   c. Review query patterns
   
4. Resolution:
   a. Increase lock timeout (if appropriate)
   b. Add deadlock avoidance logic
   c. Deploy fix
   
5. Prevention:
   a. Add chaos test for deadlock scenario
   b. Update runbook

SCENARIO 5: Fraud Score Anomaly (Compliance)
──────────────────────────────────────────────
1. Detection:
   a. Fraud score > 0.8 on transaction
   b. Unusual pattern detected
   c. Alert to compliance team
   
2. Investigation:
   a. Review transaction details
   b. Check customer history
   c. Assess true positive vs false alarm
   
3. Action:
   a. If true positive: FLAG wallet + BLOCK future transactions
   b. If false alarm: adjust fraud model
   c. Contact customer if needed
   
4. Documentation:
   a. Log outcome in alert system
   b. Update fraud model (if applicable)
   c. Report to compliance officer
```

---

# 🔐 SECTION K — FINAL SEAL & LOCK

## Prompt Integrity Verification

```
SEAL_DETAILS:

Created:  [TIMESTAMP]
Version:  1.0.0
Author:   [SEALED]
Status:   🔐 LOCKED & IMMUTABLE

Cryptographic Signature:
  Algorithm: ECDSA (P-384)
  Signature: [SEALED_SIGNATURE]
  Verification: gpg --verify signature.sig WALLET_BALANCE_TRACKING_AGENT.md

Checksum (SHA-256):
  Content Hash: [SEALED_HASH]
  Verify: sha256sum -c WALLET_BALANCE_TRACKING_AGENT.md.sha256

SEAL VIOLATION DETECTION:

If this document is modified:
  1. Signature will NOT verify
  2. Checksum will NOT match
  3. Deployment will FAIL
  4. Audit trail will record attempt
  5. Security team will be ALERTED

SEAL INTEGRITY TEST:
  Command: gpg --verify WALLET_BALANCE_TRACKING_AGENT.md.sig
  Expected: Signature made by ...
  If fails: STOP, contact security team

VERSION MANAGEMENT:
  v1.0.0 → Current (sealed)
  v1.0.1 → Minor: Add-only (append new sections)
  v2.0.0 → Major: Structural change (new seal required)
  
  NO in-place edits allowed
  NO deletion of content allowed
  Changes ONLY via version bump + re-signing

APPROVAL CHAIN:
  ✓ Architecture Review: [NAME]
  ✓ Security Review: [NAME]
  ✓ Compliance Review: [NAME]
  ✓ Legal Review: [NAME]
  ✓ Executive Approval: [NAME]
  ✓ Deployment Ready: [DATE]
```

## Final Enforcement

```
╔═══════════════════════════════════════════════════════════════╗
║                     🔒 SEALED & LOCKED 🔒                     ║
║                                                                 ║
║  WALLET_BALANCE_TRACKING_AGENT v1.0.0                         ║
║                                                                 ║
║  For Antigravity Multi-Tenant SaaS Platform                   ║
║                                                                 ║
║  This prompt envelope is CRYPTOGRAPHICALLY SEALED.             ║
║  NO modifications permitted without re-signing.                ║
║  NO creative interpretation allowed.                           ║
║  NO assumptions permitted.                                     ║
║  DEFAULT: DENY unless explicitly specified.                   ║
║                                                                 ║
║  VIOLATION = IMMEDIATE AUDIT + LOCKDOWN                       ║
║                                                                 ║
║  Questions? Contact: [COMPLIANCE_TEAM]                        ║
║  Escalation: [SECURITY_TEAM]                                  ║
║  Emergency: [ON_CALL_ENGINEER]                                ║
║                                                                 ║
║  ═══════════════════════════════════════════════════════════ ║
║  Last Updated: [DEPLOYMENT_DATE]                             ║
║  Next Review: [Q1_DATE]                                       ║
║  Expiration: [EXPIRATION_DATE]                                ║
║  ═══════════════════════════════════════════════════════════ ║
║                                                                 ║
║  Status: ✅ APPROVED FOR PRODUCTION                           ║
║                                                                 ║
╚═══════════════════════════════════════════════════════════════╝
```

---

# 📋 APPENDIX A — Reference Architecture

## System Components (Locked)

```
┌─────────────────────────────────────────────────────────────────┐
│                     API GATEWAY (Kong)                           │
│              Rate Limiting | Auth | Routing                      │
└──────────────┬──────────────────────────────────┬────────────────┘
               │                                  │
        ┌──────▼─────┐                    ┌──────▼──────┐
        │ Balance API │                    │  Event API   │
        │ (FastAPI)   │                    │  (FastAPI)   │
        └──────┬──────┘                    └──────┬───────┘
               │                                  │
        ┌──────▼────────────────────────────────▼──────┐
        │         WALLET BALANCE TRACKING AGENT        │
        │  - LOCKED PROMPT EXECUTION                  │
        │  - State Machine Controller                 │
        │  - Audit Logger                             │
        │  - Ledger Validator                         │
        └────────────┬─────────────────────────────────┘
                     │
        ┌────────────┴────────────────────────┐
        │                                     │
    ┌───▼───────────────┐      ┌────────────▼──────┐
    │ PostgreSQL (ACID)  │      │ Redis (Cache)      │
    │ - Wallets          │      │ - Hot balances     │
    │ - LedgerEntries    │      │ - Events (Streams) │
    │ - Transactions     │      │ - Session data     │
    │ - AuditLogs        │      │ - Rate limiters    │
    └───────────────────┘      └────────────────────┘
        │
        │ (Replication)
        │
    ┌───▼─────────────────────┐
    │ Vault (Secrets)         │
    │ - Encryption keys       │
    │ - Database credentials  │
    │ - API secrets           │
    └────────────────────────┘

DEPENDENCIES (Locked):
  FastAPI 0.95+        (REST API framework)
  Pydantic 2.0+        (Data validation)
  SQLAlchemy 2.0+      (ORM)
  psycopg 3.1+         (PostgreSQL driver)
  redis-py 5.0+        (Redis client)
  hvac 1.2+            (Vault client)
  python-jose 3.3+     (JWT handling)
  cryptography 40+     (Encryption)
```

---

**END OF DOCUMENT**

---

**CLASSIFICATION: INTERNAL — PRODUCTION ARTIFACT**  
**DO NOT DISTRIBUTE TO UNAUTHORIZED PERSONNEL**  
**ALL COPIES MUST BE ENCRYPTED AT REST**  
**AUDITED AND APPROVED FOR DEPLOYMENT**  

🔒 **SEALED & LOCKED** 🔒
