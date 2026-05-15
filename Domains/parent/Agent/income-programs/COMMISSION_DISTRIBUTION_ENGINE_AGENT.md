# 💰 COMMISSION DISTRIBUTION ENGINE AGENT — ANTIGRAVITY v1.0

**Artifact Class:** Production System Blueprint  
**Mutation Policy:** Add-only via version bump  
**Execution Mode:** Deterministic  
**Stack Lock:** Enforced  
**Interface Freeze:** Required  
**Domain:** Financial & Commission Management  
**Subsystem:** Commission Distribution Infrastructure  
**Compliance:** SOX, Double-Entry Accounting, Tax Withholding  

---

## 🔒 SECTION A — AGENT IDENTITY & AUTHORITY

### Agent Classification
```
Agent Name: COMMISSION_DISTRIBUTION_ENGINE_AGENT
Agent Type: Financial Distribution Orchestrator
Execution Mode: Event-Driven + Scheduled + Double-Entry
Determinism Rule: Identical revenue → Identical distribution
Failure Mode: STOP → ROLLBACK → ALERT → NO PARTIAL PAYOUT
Human Override: Payout Approval/Rejection Only
AI Override: FORBIDDEN
Accounting Standard: Double-Entry Ledger (Debit = Credit)
Audit Requirement: IMMUTABLE FINANCIAL LOGS
```

### Authority Boundaries
```
PERMITTED:
✓ Commission calculation from revenue splits
✓ Wallet balance management (credits/debits)
✓ Payout request validation
✓ Scheduled payout batch processing
✓ Tax withholding calculation (TDS/TCS)
✓ Bank transfer initiation
✓ Settlement reconciliation
✓ Commission rule evaluation
✓ Escrow account management
✓ Multi-party split distribution
✓ Referral commission tracking
✓ Affiliate payout processing

FORBIDDEN:
✗ Manual balance manipulation without transaction
✗ Payout without cleared funds
✗ Double crediting same revenue
✗ Commission calculation without audit trail
✗ Tax withholding bypass
✗ Payout threshold violation
✗ Wallet overdraft creation
✗ Settlement without reconciliation proof
✗ Commission retroactive modification
✗ Bypassing KYC verification for payouts
```

---

## 🔒 SECTION B — COMMISSION TAXONOMY (LOCKED)

### Commission Types (Non-Negotiable)
```
1. TRAINER REVENUE SHARE
   - Course sales commission (80% trainer, 20% platform)
   - Subscription revenue share (tiered splits)
   - One-on-one session fees (70% trainer, 30% platform)
   - Workshop revenue (85% trainer, 15% platform)
   - Co-teaching splits (configurable)

2. REFERRAL COMMISSIONS
   - User referral rewards (flat rate or % of first payment)
   - Affiliate partner commissions (5-15% recurring)
   - Institutional referral bonuses
   - Mentor referral incentives

3. MARKETPLACE COMMISSIONS
   - Job posting fees (100% platform)
   - Recruiter subscription revenue (100% platform)
   - Premium feature commissions
   - Advertisement revenue splits

4. INNOVATION ROYALTIES
   - Kid innovator royalties (0.01-0.05% of business revenue)
   - Patent licensing fees (90% innovator, 10% platform)
   - Idea marketplace commissions (5% platform)

5. CONTENT CREATOR EARNINGS
   - Creator monetization (ad revenue share)
   - Premium content sales (70% creator, 30% platform)
   - Tipping/donations (95% creator, 5% platform processing)

6. EVENT ORGANIZER REVENUE
   - Event ticket sales (85% organizer, 15% platform)
   - Workshop registrations (80% organizer, 20% platform)
   - Sponsorship revenue (90% organizer, 10% platform)

7. MENTOR EARNINGS
   - Dojo session fees (70% mentor, 30% platform)
   - Certification fees (60% mentor, 40% platform)
   - Consulting fees (75% mentor, 25% platform)

8. PLATFORM COMMISSIONS
   - Transaction fees collected
   - Subscription revenue
   - Service charges
   - Payment gateway markup
```

---

## 🔒 SECTION C — DATA MODEL (IMMUTABLE SCHEMA)

### Wallet Entity (Double-Entry Ledger)
```sql
CREATE TABLE wallets (
    wallet_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    
    -- Owner Information
    owner_id UUID NOT NULL REFERENCES users(user_id),
    owner_type VARCHAR(50) NOT NULL, -- trainer, innovator, referrer, creator, organizer, mentor
    owner_name VARCHAR(255) NOT NULL,
    
    -- Balance (in smallest currency unit - paise/cents)
    available_balance_cents BIGINT NOT NULL DEFAULT 0, -- Withdrawable balance
    pending_balance_cents BIGINT NOT NULL DEFAULT 0, -- Pending settlement
    reserved_balance_cents BIGINT NOT NULL DEFAULT 0, -- On-hold for disputes
    total_lifetime_earnings_cents BIGINT NOT NULL DEFAULT 0, -- All-time earnings
    
    -- Currency
    currency VARCHAR(3) NOT NULL DEFAULT 'INR', -- ISO 4217
    
    -- Payout Configuration
    payout_method VARCHAR(50) DEFAULT 'bank_transfer', -- bank_transfer, upi, paypal
    payout_threshold_cents BIGINT DEFAULT 100000, -- Min ₹1000 for payout
    payout_schedule VARCHAR(20) DEFAULT 'weekly', -- weekly, biweekly, monthly
    
    -- Bank Account Details (Encrypted)
    bank_account_number_encrypted TEXT,
    bank_ifsc_code_encrypted TEXT,
    bank_account_holder_name_encrypted TEXT,
    upi_id_encrypted TEXT,
    
    -- KYC & Verification
    kyc_verified BOOLEAN DEFAULT FALSE,
    kyc_verified_at TIMESTAMP,
    pan_verified BOOLEAN DEFAULT FALSE, -- For Indian tax compliance
    pan_number_encrypted TEXT,
    
    -- Tax Withholding
    default_tax_withholding_rate NUMERIC(5,2) DEFAULT 10.00, -- 10% TDS
    
    -- Status
    wallet_status VARCHAR(50) NOT NULL DEFAULT 'active', -- active, suspended, frozen, closed
    suspension_reason TEXT,
    
    -- Metadata
    metadata JSONB,
    
    -- Audit
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
    
    -- Multi-Tenant
    tenant_id UUID NOT NULL REFERENCES tenants(tenant_id),
    
    -- Constraints
    CONSTRAINT valid_owner_type CHECK (owner_type IN (
        'trainer', 'innovator', 'referrer', 'creator', 'organizer', 
        'mentor', 'affiliate', 'platform'
    )),
    CONSTRAINT valid_currency CHECK (currency IN ('INR', 'USD', 'EUR', 'GBP')),
    CONSTRAINT valid_payout_method CHECK (payout_method IN (
        'bank_transfer', 'upi', 'paypal', 'stripe_connect'
    )),
    CONSTRAINT valid_payout_schedule CHECK (payout_schedule IN (
        'weekly', 'biweekly', 'monthly', 'on_demand'
    )),
    CONSTRAINT valid_wallet_status CHECK (wallet_status IN (
        'active', 'suspended', 'frozen', 'closed', 'kyc_pending'
    )),
    CONSTRAINT positive_balances CHECK (
        available_balance_cents >= 0 AND 
        pending_balance_cents >= 0 AND 
        reserved_balance_cents >= 0
    ),
    
    -- One wallet per owner
    CONSTRAINT unique_owner_wallet UNIQUE (owner_id, owner_type, tenant_id)
);

-- Indexes
CREATE INDEX idx_wallets_owner ON wallets(owner_id, owner_type);
CREATE INDEX idx_wallets_status ON wallets(wallet_status);
CREATE INDEX idx_wallets_balance ON wallets(available_balance_cents DESC);
CREATE INDEX idx_wallets_tenant ON wallets(tenant_id);
```

### Wallet Transaction Entity (Ledger)
```sql
CREATE TABLE wallet_transactions (
    transaction_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    
    -- Wallet Reference
    wallet_id UUID NOT NULL REFERENCES wallets(wallet_id),
    
    -- Transaction Type
    transaction_type VARCHAR(50) NOT NULL, -- credit, debit, reserve, release, refund
    
    -- Amount
    amount_cents BIGINT NOT NULL,
    currency VARCHAR(3) NOT NULL DEFAULT 'INR',
    
    -- Balance Snapshots (Before Transaction)
    balance_before_cents BIGINT NOT NULL,
    balance_after_cents BIGINT NOT NULL,
    
    -- Transaction Source
    source_type VARCHAR(50) NOT NULL, -- commission_credit, payout_debit, refund_reversal, etc.
    source_reference_id UUID, -- revenue_split_id, payout_id, refund_id
    source_reference_type VARCHAR(50), -- revenue_split, payout, refund
    
    -- Description
    description TEXT NOT NULL,
    
    -- Tax Withholding
    tax_withheld_cents BIGINT DEFAULT 0,
    tax_withholding_rate NUMERIC(5,2),
    
    -- Double-Entry Components
    debit_account VARCHAR(100), -- e.g., "revenue_receivable"
    credit_account VARCHAR(100), -- e.g., "trainer_wallet:uuid"
    
    -- Status
    status VARCHAR(50) NOT NULL DEFAULT 'completed', -- pending, completed, failed, reversed
    
    -- Reversal Tracking
    is_reversal BOOLEAN DEFAULT FALSE,
    original_transaction_id UUID REFERENCES wallet_transactions(transaction_id),
    reversal_reason TEXT,
    
    -- Metadata
    metadata JSONB,
    
    -- Audit
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    created_by VARCHAR(50) DEFAULT 'system',
    
    -- Multi-Tenant
    tenant_id UUID NOT NULL REFERENCES tenants(tenant_id),
    
    -- Constraints
    CONSTRAINT valid_transaction_type CHECK (transaction_type IN (
        'credit', 'debit', 'reserve', 'release', 'refund', 'adjustment'
    )),
    CONSTRAINT valid_status CHECK (status IN (
        'pending', 'completed', 'failed', 'reversed'
    )),
    CONSTRAINT positive_amount CHECK (amount_cents > 0)
);

-- Indexes
CREATE INDEX idx_wallet_transactions_wallet ON wallet_transactions(wallet_id, created_at DESC);
CREATE INDEX idx_wallet_transactions_source ON wallet_transactions(source_reference_type, source_reference_id);
CREATE INDEX idx_wallet_transactions_type ON wallet_transactions(transaction_type, status);
CREATE INDEX idx_wallet_transactions_tenant ON wallet_transactions(tenant_id);
```

### Payout Request Entity
```sql
CREATE TABLE payout_requests (
    payout_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    payout_number VARCHAR(50) UNIQUE NOT NULL, -- Format: PAYOUT-YYYY-MM-XXXXXX
    
    -- Wallet Reference
    wallet_id UUID NOT NULL REFERENCES wallets(wallet_id),
    payee_id UUID NOT NULL REFERENCES users(user_id),
    payee_name VARCHAR(255) NOT NULL,
    payee_type VARCHAR(50) NOT NULL,
    
    -- Payout Amount
    requested_amount_cents BIGINT NOT NULL,
    tax_withheld_cents BIGINT NOT NULL DEFAULT 0,
    processing_fee_cents BIGINT DEFAULT 0,
    net_payout_cents BIGINT NOT NULL, -- requested - tax - fees
    currency VARCHAR(3) NOT NULL DEFAULT 'INR',
    
    -- Payout Method
    payout_method VARCHAR(50) NOT NULL,
    payout_destination JSONB NOT NULL, -- Bank details, UPI ID, etc.
    
    -- Request Details
    request_type VARCHAR(50) NOT NULL DEFAULT 'scheduled', -- scheduled, on_demand, auto
    requested_at TIMESTAMP NOT NULL DEFAULT NOW(),
    requested_by UUID REFERENCES users(user_id), -- NULL for auto payouts
    
    -- Processing Timeline
    approved_at TIMESTAMP,
    approved_by UUID REFERENCES users(user_id),
    processing_started_at TIMESTAMP,
    completed_at TIMESTAMP,
    failed_at TIMESTAMP,
    
    -- Status
    status VARCHAR(50) NOT NULL DEFAULT 'pending', -- pending, approved, processing, completed, failed, rejected
    failure_reason TEXT,
    rejection_reason TEXT,
    
    -- Bank Transfer Details
    bank_transfer_reference VARCHAR(255), -- UTR/reference from bank
    bank_transfer_initiated_at TIMESTAMP,
    bank_transfer_completed_at TIMESTAMP,
    
    -- Reconciliation
    reconciled BOOLEAN DEFAULT FALSE,
    reconciled_at TIMESTAMP,
    reconciled_by UUID REFERENCES users(user_id),
    
    -- Metadata
    metadata JSONB,
    
    -- Audit
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
    
    -- Multi-Tenant
    tenant_id UUID NOT NULL REFERENCES tenants(tenant_id),
    
    -- Constraints
    CONSTRAINT valid_payout_status CHECK (status IN (
        'pending', 'approved', 'processing', 'completed', 
        'failed', 'rejected', 'canceled'
    )),
    CONSTRAINT valid_request_type CHECK (request_type IN (
        'scheduled', 'on_demand', 'auto', 'manual'
    )),
    CONSTRAINT positive_amounts CHECK (
        requested_amount_cents > 0 AND 
        net_payout_cents > 0
    )
);

-- Indexes
CREATE INDEX idx_payout_requests_wallet ON payout_requests(wallet_id, requested_at DESC);
CREATE INDEX idx_payout_requests_payee ON payout_requests(payee_id, status);
CREATE INDEX idx_payout_requests_status ON payout_requests(status, requested_at);
CREATE INDEX idx_payout_requests_number ON payout_requests(payout_number);
CREATE INDEX idx_payout_requests_reconciliation ON payout_requests(reconciled, completed_at);
CREATE INDEX idx_payout_requests_tenant ON payout_requests(tenant_id);
```

### Commission Rules Engine
```sql
CREATE TABLE commission_rules (
    rule_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    rule_name VARCHAR(255) NOT NULL,
    rule_code VARCHAR(50) UNIQUE NOT NULL, -- e.g., COURSE_SALE_TRAINER_80
    
    -- Rule Application
    applicable_to_type VARCHAR(50) NOT NULL, -- transaction_type, user_type, product_category
    applicable_to_value VARCHAR(255) NOT NULL, -- e.g., course_purchase, trainer
    
    -- Commission Configuration
    commission_type VARCHAR(50) NOT NULL, -- percentage, flat_amount, tiered
    
    -- Percentage-Based
    commission_percentage NUMERIC(5,2), -- e.g., 80.00 for 80%
    
    -- Flat Amount-Based
    commission_amount_cents BIGINT,
    
    -- Tiered Commission (JSONB)
    tiered_config JSONB, -- [{min: 0, max: 10000, rate: 15}, {min: 10000, max: null, rate: 20}]
    
    -- Recipients
    recipient_roles JSONB NOT NULL, -- [{role: "trainer", share: 80}, {role: "platform", share: 20}]
    
    -- Tax Withholding
    apply_tax_withholding BOOLEAN DEFAULT TRUE,
    tax_withholding_rate NUMERIC(5,2) DEFAULT 10.00,
    
    -- Settlement Timing
    settlement_delay_days INTEGER DEFAULT 0, -- Hold commission for N days before crediting
    
    -- Validity Period
    valid_from DATE NOT NULL,
    valid_until DATE,
    
    -- Priority (for rule conflicts)
    priority INTEGER DEFAULT 100,
    
    -- Status
    is_active BOOLEAN DEFAULT TRUE,
    
    -- Metadata
    metadata JSONB,
    
    -- Audit
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
    created_by UUID NOT NULL REFERENCES users(user_id),
    
    -- Multi-Tenant
    tenant_id UUID NOT NULL REFERENCES tenants(tenant_id),
    
    -- Constraints
    CONSTRAINT valid_commission_type CHECK (commission_type IN (
        'percentage', 'flat_amount', 'tiered', 'hybrid'
    )),
    CONSTRAINT commission_config_valid CHECK (
        (commission_type = 'percentage' AND commission_percentage IS NOT NULL) OR
        (commission_type = 'flat_amount' AND commission_amount_cents IS NOT NULL) OR
        (commission_type = 'tiered' AND tiered_config IS NOT NULL)
    )
);

-- Indexes
CREATE INDEX idx_commission_rules_applicable ON commission_rules(applicable_to_type, applicable_to_value);
CREATE INDEX idx_commission_rules_active ON commission_rules(is_active, priority DESC);
CREATE INDEX idx_commission_rules_validity ON commission_rules(valid_from, valid_until);
CREATE INDEX idx_commission_rules_tenant ON commission_rules(tenant_id);
```

### Escrow Account Entity
```sql
CREATE TABLE escrow_accounts (
    escrow_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    
    -- Reference Transaction
    reference_type VARCHAR(50) NOT NULL, -- payment_transaction, milestone_contract
    reference_id UUID NOT NULL,
    
    -- Parties
    payer_id UUID NOT NULL REFERENCES users(user_id),
    payee_id UUID NOT NULL REFERENCES users(user_id),
    
    -- Escrow Amount
    escrow_amount_cents BIGINT NOT NULL,
    currency VARCHAR(3) NOT NULL DEFAULT 'INR',
    
    -- Release Conditions
    release_condition_type VARCHAR(50) NOT NULL, -- milestone_completion, time_based, manual_approval
    release_condition_data JSONB NOT NULL,
    
    -- Status
    status VARCHAR(50) NOT NULL DEFAULT 'held', -- held, released, refunded, disputed
    
    -- Timeline
    escrowed_at TIMESTAMP NOT NULL DEFAULT NOW(),
    release_scheduled_at TIMESTAMP,
    released_at TIMESTAMP,
    
    -- Release Details
    release_amount_cents BIGINT,
    release_to_wallet_id UUID REFERENCES wallets(wallet_id),
    release_reason TEXT,
    
    -- Metadata
    metadata JSONB,
    
    -- Audit
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
    
    -- Multi-Tenant
    tenant_id UUID NOT NULL REFERENCES tenants(tenant_id),
    
    -- Constraints
    CONSTRAINT valid_escrow_status CHECK (status IN (
        'held', 'released', 'refunded', 'disputed', 'expired'
    )),
    CONSTRAINT positive_escrow_amount CHECK (escrow_amount_cents > 0)
);

CREATE INDEX idx_escrow_accounts_reference ON escrow_accounts(reference_type, reference_id);
CREATE INDEX idx_escrow_accounts_status ON escrow_accounts(status, release_scheduled_at);
CREATE INDEX idx_escrow_accounts_payee ON escrow_accounts(payee_id);
```

---

## 🔒 SECTION D — COMMISSION CALCULATION ENGINE

### Commission Calculation Algorithm
```python
from typing import List, Dict, Any
from decimal import Decimal

def calculate_commission(
    payment_transaction: PaymentTransaction,
    commission_rules: List[CommissionRule]
) -> List[CommissionSplit]:
    """
    Calculate commission splits for a payment transaction
    
    Args:
        payment_transaction: The source payment transaction
        commission_rules: Applicable commission rules (sorted by priority)
    
    Returns:
        List of commission splits with amounts and recipients
    """
    
    # 1. Select applicable rule (highest priority)
    applicable_rule = select_applicable_rule(
        transaction=payment_transaction,
        rules=commission_rules
    )
    
    if not applicable_rule:
        raise ValueError(f"No commission rule found for transaction {payment_transaction.transaction_id}")
    
    # 2. Calculate commission amount per recipient
    transaction_amount_cents = payment_transaction.amount_cents
    commission_splits = []
    
    for recipient_config in applicable_rule.recipient_roles:
        recipient_role = recipient_config["role"]
        recipient_share_percentage = Decimal(recipient_config["share"])
        
        # Calculate base commission
        if applicable_rule.commission_type == "percentage":
            commission_amount_cents = int(
                transaction_amount_cents * 
                (applicable_rule.commission_percentage / 100) *
                (recipient_share_percentage / 100)
            )
        
        elif applicable_rule.commission_type == "flat_amount":
            total_commission = applicable_rule.commission_amount_cents
            commission_amount_cents = int(
                total_commission * (recipient_share_percentage / 100)
            )
        
        elif applicable_rule.commission_type == "tiered":
            total_commission = calculate_tiered_commission(
                amount_cents=transaction_amount_cents,
                tiers=applicable_rule.tiered_config
            )
            commission_amount_cents = int(
                total_commission * (recipient_share_percentage / 100)
            )
        
        else:
            raise ValueError(f"Unsupported commission type: {applicable_rule.commission_type}")
        
        # 3. Calculate tax withholding
        tax_withheld_cents = 0
        if applicable_rule.apply_tax_withholding and recipient_role != "platform":
            tax_withheld_cents = int(
                commission_amount_cents * 
                (applicable_rule.tax_withholding_rate / 100)
            )
        
        net_commission_cents = commission_amount_cents - tax_withheld_cents
        
        # 4. Determine recipient
        recipient_id = get_recipient_id(
            transaction=payment_transaction,
            recipient_role=recipient_role
        )
        
        # 5. Create commission split
        commission_split = CommissionSplit(
            transaction_id=payment_transaction.transaction_id,
            rule_id=applicable_rule.rule_id,
            rule_code=applicable_rule.rule_code,
            recipient_role=recipient_role,
            recipient_id=recipient_id,
            gross_commission_cents=commission_amount_cents,
            tax_withheld_cents=tax_withheld_cents,
            net_commission_cents=net_commission_cents,
            commission_percentage=recipient_share_percentage,
            settlement_delay_days=applicable_rule.settlement_delay_days
        )
        
        commission_splits.append(commission_split)
    
    # 6. Validation: Splits must sum to transaction amount (or commission amount)
    total_commission = sum(split.gross_commission_cents for split in commission_splits)
    
    # Allow 1 cent tolerance for rounding
    difference = abs(total_commission - transaction_amount_cents)
    if difference > 1 and applicable_rule.commission_type == "percentage":
        if applicable_rule.commission_percentage == 100:
            raise ValueError(
                f"Commission splits don't sum correctly. "
                f"Transaction: {transaction_amount_cents}, "
                f"Total commission: {total_commission}"
            )
    
    return commission_splits


def calculate_tiered_commission(
    amount_cents: int,
    tiers: List[Dict[str, Any]]
) -> int:
    """
    Calculate commission using tiered rates
    
    Example tiers:
    [
        {"min": 0, "max": 1000000, "rate": 15.0},      # 0-₹10k: 15%
        {"min": 1000000, "max": 5000000, "rate": 20.0}, # ₹10k-₹50k: 20%
        {"min": 5000000, "max": null, "rate": 25.0}    # ₹50k+: 25%
    ]
    """
    
    total_commission_cents = 0
    remaining_amount = amount_cents
    
    # Sort tiers by min amount
    sorted_tiers = sorted(tiers, key=lambda t: t["min"])
    
    for tier in sorted_tiers:
        tier_min = tier["min"]
        tier_max = tier["max"]
        tier_rate = Decimal(tier["rate"])
        
        # Determine amount in this tier
        if remaining_amount <= 0:
            break
        
        if tier_max is None:
            # Top tier (no maximum)
            tier_amount = remaining_amount
        else:
            tier_range = tier_max - tier_min
            tier_amount = min(remaining_amount, tier_range)
        
        # Calculate commission for this tier
        tier_commission = int(tier_amount * (tier_rate / 100))
        total_commission_cents += tier_commission
        
        remaining_amount -= tier_amount
    
    return total_commission_cents


def select_applicable_rule(
    transaction: PaymentTransaction,
    rules: List[CommissionRule]
) -> CommissionRule:
    """
    Select the applicable commission rule for a transaction
    Rules are evaluated in priority order (highest first)
    """
    
    # Filter rules by validity period
    now = date.today()
    valid_rules = [
        rule for rule in rules
        if rule.valid_from <= now and (rule.valid_until is None or rule.valid_until >= now)
        and rule.is_active
    ]
    
    # Sort by priority (highest first)
    valid_rules.sort(key=lambda r: r.priority, reverse=True)
    
    # Find first matching rule
    for rule in valid_rules:
        if matches_rule(transaction, rule):
            return rule
    
    return None


def matches_rule(
    transaction: PaymentTransaction,
    rule: CommissionRule
) -> bool:
    """
    Check if a commission rule applies to a transaction
    """
    
    if rule.applicable_to_type == "transaction_type":
        return transaction.transaction_type == rule.applicable_to_value
    
    elif rule.applicable_to_type == "user_type":
        user = get_user(transaction.payer_id)
        return user.user_type == rule.applicable_to_value
    
    elif rule.applicable_to_type == "product_category":
        product = get_product(transaction.reference_entity_id)
        return product.category == rule.applicable_to_value
    
    return False


def get_recipient_id(
    transaction: PaymentTransaction,
    recipient_role: str
) -> Optional[UUID]:
    """
    Determine recipient ID based on role
    """
    
    if recipient_role == "platform":
        return None  # Platform wallet
    
    elif recipient_role == "trainer":
        return transaction.metadata.get("trainer_id")
    
    elif recipient_role == "mentor":
        return transaction.metadata.get("mentor_id")
    
    elif recipient_role == "organizer":
        return transaction.metadata.get("organizer_id")
    
    elif recipient_role == "innovator":
        return transaction.metadata.get("innovator_id")
    
    elif recipient_role == "referrer":
        return transaction.metadata.get("referrer_id")
    
    elif recipient_role == "creator":
        return transaction.metadata.get("creator_id")
    
    elif recipient_role == "co_trainer":
        return transaction.metadata.get("co_trainer_id")
    
    else:
        raise ValueError(f"Unknown recipient role: {recipient_role}")
```

---

## 🔒 SECTION E — WALLET CREDIT PIPELINE (DETERMINISTIC)

### Wallet Credit Flow (11 Stages)

```
STAGE 1: COMMISSION CALCULATION
├─ Input: PaymentTransaction (status: succeeded)
├─ Load applicable commission rules
├─ Calculate commission splits per recipient
├─ Validate: splits sum correctly
└─ Output: List[CommissionSplit]

STAGE 2: SETTLEMENT DELAY CHECK
├─ Check rule.settlement_delay_days
├─ If delay > 0:
│  ├─ Schedule credit for future date
│  └─ Status: pending_settlement
├─ Else:
│  └─ Proceed to immediate credit
└─ Output: Scheduled credit date

STAGE 3: WALLET EXISTENCE VERIFICATION
├─ For each commission split:
│  ├─ Check if recipient wallet exists
│  ├─ If not exists:
│  │  └─ Create wallet (auto-provision)
│  └─ If wallet status != active:
│     └─ Hold credit (status: on_hold)
└─ Output: Wallet IDs

STAGE 4: DOUBLE-ENTRY TRANSACTION CREATION
├─ For each commission split:
│  ├─ Create wallet_transaction record
│  ├─ transaction_type: credit
│  ├─ debit_account: "revenue_payable"
│  ├─ credit_account: "wallet:{wallet_id}"
│  ├─ amount_cents: net_commission_cents
│  └─ Store balance_before and balance_after
└─ Output: Transaction IDs

STAGE 5: WALLET BALANCE UPDATE (ATOMIC)
├─ BEGIN TRANSACTION
├─ Lock wallet row (FOR UPDATE)
├─ Calculate new balances:
│  ├─ If settlement_delay = 0:
│  │  └─ available_balance += net_commission_cents
│  ├─ Else:
│  │  └─ pending_balance += net_commission_cents
│  └─ total_lifetime_earnings += gross_commission_cents
├─ Update wallet record
├─ COMMIT TRANSACTION
└─ Output: Updated wallet

STAGE 6: TAX WITHHOLDING RECORDING
├─ If tax_withheld_cents > 0:
│  ├─ Create tax_withholding_record
│  ├─ Store PAN, TDS rate, period
│  └─ Queue for TDS certificate generation
└─ Output: Tax record ID

STAGE 7: COMMISSION LEDGER ENTRY
├─ Create revenue_split record (from Payment Gateway Agent)
├─ Link to wallet_transaction
├─ Status: credited
└─ Output: Revenue split record

STAGE 8: NOTIFICATION DISPATCH
├─ Send wallet credit notification
├─ Email: "Commission credited to your wallet"
├─ In-app notification with amount
├─ SMS (if amount > threshold)
└─ Output: Notification sent

STAGE 9: ANALYTICS EVENT EMISSION
├─ Emit commission.credited event
├─ Payload: {wallet_id, amount, recipient_type, rule_code}
├─ Consumers: Analytics, Reporting
└─ Output: Event published

STAGE 10: PAYOUT THRESHOLD CHECK
├─ Check if available_balance >= payout_threshold
├─ If yes AND payout_schedule = "auto":
│  └─ Trigger auto-payout request
└─ Output: Payout triggered (optional)

STAGE 11: AUDIT LOG CREATION
├─ Log all commission calculation parameters
├─ Log wallet state changes
├─ Immutable audit trail
└─ Output: Audit record
```

### Failure Handling (Per Stage)
```
STAGE 1 FAILURE (No Rule Found):
→ LOG ERROR with transaction details
→ CREATE ALERT for manual review
→ DO NOT credit commission
→ RETRY after rule creation

STAGE 4 FAILURE (Double-Entry Violation):
→ ROLLBACK all pending transactions
→ CRITICAL ALERT to finance team
→ NO PARTIAL CREDITS
→ Manual investigation required

STAGE 5 FAILURE (Wallet Lock Timeout):
→ RETRY with exponential backoff (max 3 attempts)
→ If all retries fail: Hold credit in escrow
→ ALERT DevOps team

ANY STAGE FAILURE (Critical):
→ ROLLBACK entire pipeline
→ Mark commission as failed
→ LOG detailed error
→ NO PARTIAL STATE
```

---

## 🔒 SECTION F — PAYOUT REQUEST WORKFLOW

### Payout Request Types
```
1. SCHEDULED PAYOUT (Automatic)
   - Triggered by cron job based on payout_schedule
   - Weekly: Every Friday
   - Biweekly: 1st and 15th of month
   - Monthly: 1st of month
   - Conditions: available_balance >= payout_threshold

2. ON-DEMAND PAYOUT (User-Initiated)
   - User requests payout manually
   - Subject to:
     - Minimum threshold (₹1000)
     - KYC verification required
     - Cooling period (7 days since last payout)
   - Processing fee may apply (₹50 or 1%, whichever higher)

3. AUTO PAYOUT (Platform-Initiated)
   - Triggered when wallet balance exceeds max limit
   - Automatic transfer to prevent large balances
   - No processing fee
```

### Payout Request Processing Pipeline

```python
def process_payout_request(
    wallet_id: UUID,
    requested_amount_cents: int,
    request_type: str = "on_demand",
    requested_by_user_id: Optional[UUID] = None
) -> PayoutRequest:
    """
    Process payout request with validation and approval workflow
    """
    
    # STAGE 1: LOAD WALLET & VALIDATE
    wallet = get_wallet(wallet_id)
    
    if wallet.wallet_status != "active":
        raise ValueError(f"Wallet is {wallet.wallet_status}, cannot process payout")
    
    if not wallet.kyc_verified:
        raise ValueError("KYC verification required before payout")
    
    if not wallet.pan_verified:
        raise ValueError("PAN verification required for tax compliance")
    
    # STAGE 2: VALIDATE AMOUNT
    if requested_amount_cents > wallet.available_balance_cents:
        raise ValueError(
            f"Insufficient balance. "
            f"Requested: ₹{requested_amount_cents/100}, "
            f"Available: ₹{wallet.available_balance_cents/100}"
        )
    
    if requested_amount_cents < wallet.payout_threshold_cents:
        raise ValueError(
            f"Amount below minimum payout threshold "
            f"(₹{wallet.payout_threshold_cents/100})"
        )
    
    # STAGE 3: CHECK COOLING PERIOD (for on-demand)
    if request_type == "on_demand":
        last_payout = get_last_completed_payout(wallet_id)
        if last_payout:
            days_since_last = (datetime.now() - last_payout.completed_at).days
            if days_since_last < 7:
                raise ValueError(
                    f"Payout cooling period active. "
                    f"Next payout available in {7 - days_since_last} days"
                )
    
    # STAGE 4: CALCULATE TAX WITHHOLDING
    tax_withheld_cents = int(
        requested_amount_cents * (wallet.default_tax_withholding_rate / 100)
    )
    
    # STAGE 5: CALCULATE PROCESSING FEE
    processing_fee_cents = 0
    if request_type == "on_demand":
        # 1% or ₹50, whichever is higher
        fee_1_percent = int(requested_amount_cents * 0.01)
        processing_fee_cents = max(fee_1_percent, 5000)  # ₹50
    
    # STAGE 6: CALCULATE NET PAYOUT
    net_payout_cents = requested_amount_cents - tax_withheld_cents - processing_fee_cents
    
    # STAGE 7: GENERATE PAYOUT NUMBER
    payout_number = generate_payout_number()  # PAYOUT-YYYY-MM-XXXXXX
    
    # STAGE 8: CREATE PAYOUT REQUEST
    payout_request = PayoutRequest.create(
        payout_number=payout_number,
        wallet_id=wallet_id,
        payee_id=wallet.owner_id,
        payee_name=wallet.owner_name,
        payee_type=wallet.owner_type,
        requested_amount_cents=requested_amount_cents,
        tax_withheld_cents=tax_withheld_cents,
        processing_fee_cents=processing_fee_cents,
        net_payout_cents=net_payout_cents,
        currency=wallet.currency,
        payout_method=wallet.payout_method,
        payout_destination=get_payout_destination(wallet),
        request_type=request_type,
        requested_by=requested_by_user_id,
        status="pending"
    )
    
    # STAGE 9: RESERVE FUNDS IN WALLET
    reserve_wallet_funds(
        wallet_id=wallet_id,
        amount_cents=requested_amount_cents,
        reference_type="payout_request",
        reference_id=payout_request.payout_id
    )
    
    # STAGE 10: AUTO-APPROVE OR QUEUE FOR MANUAL REVIEW
    if should_auto_approve_payout(payout_request, wallet):
        approve_payout_request(payout_request.payout_id, approved_by="system")
    else:
        # Queue for manual approval
        queue_for_manual_approval(payout_request.payout_id)
        
        # Notify finance team
        send_notification(
            notification_type="payout_approval_required",
            recipient_role="finance_admin",
            message=f"Payout request {payout_number} requires approval (₹{net_payout_cents/100})"
        )
    
    # STAGE 11: EMIT EVENT
    emit_event("payout.requested", {
        "payout_id": payout_request.payout_id,
        "wallet_id": wallet_id,
        "amount_cents": requested_amount_cents,
        "net_payout_cents": net_payout_cents,
        "request_type": request_type
    })
    
    return payout_request


def should_auto_approve_payout(
    payout_request: PayoutRequest,
    wallet: Wallet
) -> bool:
    """
    Determine if payout can be auto-approved
    
    Auto-approve conditions:
    - Amount <= ₹50,000
    - Wallet owner has > 10 successful payouts
    - No fraud flags on wallet
    - Request type is "scheduled"
    """
    
    # High-value payouts require manual approval
    if payout_request.net_payout_cents > 5000000:  # ₹50k
        return False
    
    # Check payout history
    successful_payouts_count = count_successful_payouts(wallet.wallet_id)
    if successful_payouts_count < 10:
        return False
    
    # Check fraud flags
    if has_fraud_flags(wallet.wallet_id):
        return False
    
    # Auto-approve scheduled payouts
    if payout_request.request_type == "scheduled":
        return True
    
    # On-demand payouts for trusted users
    if successful_payouts_count >= 50:
        return True
    
    return False


def approve_payout_request(
    payout_id: UUID,
    approved_by: Union[str, UUID]
):
    """
    Approve payout request and initiate bank transfer
    """
    
    # 1. Load payout request
    payout = get_payout_request(payout_id)
    
    if payout.status != "pending":
        raise ValueError(f"Payout is {payout.status}, cannot approve")
    
    # 2. Update status
    update_payout_request(
        payout_id=payout_id,
        status="approved",
        approved_at=datetime.now(),
        approved_by=approved_by
    )
    
    # 3. Initiate bank transfer
    initiate_bank_transfer_async.delay(payout_id)  # Celery task
    
    # 4. Emit event
    emit_event("payout.approved", {
        "payout_id": payout_id,
        "approved_by": str(approved_by)
    })


def initiate_bank_transfer(payout_id: UUID):
    """
    Initiate bank transfer for approved payout
    """
    
    payout = get_payout_request(payout_id)
    
    # 1. Update status
    update_payout_request(
        payout_id=payout_id,
        status="processing",
        processing_started_at=datetime.now()
    )
    
    # 2. Call payment gateway for bank transfer
    if payout.payout_method == "bank_transfer":
        transfer_result = process_bank_transfer_via_gateway(
            account_number=payout.payout_destination["account_number"],
            ifsc_code=payout.payout_destination["ifsc_code"],
            account_holder_name=payout.payout_destination["account_holder_name"],
            amount_cents=payout.net_payout_cents,
            currency=payout.currency,
            purpose=f"Commission payout - {payout.payout_number}",
            reference_id=payout.payout_number
        )
    
    elif payout.payout_method == "upi":
        transfer_result = process_upi_transfer(
            upi_id=payout.payout_destination["upi_id"],
            amount_cents=payout.net_payout_cents,
            reference_id=payout.payout_number
        )
    
    else:
        raise ValueError(f"Unsupported payout method: {payout.payout_method}")
    
    # 3. Update payout with transfer details
    update_payout_request(
        payout_id=payout_id,
        bank_transfer_reference=transfer_result["transfer_reference"],
        bank_transfer_initiated_at=datetime.now()
    )
    
    # 4. Poll for transfer completion (async)
    poll_transfer_status.delay(payout_id, transfer_result["transfer_id"])


def complete_payout(payout_id: UUID, bank_transfer_reference: str):
    """
    Mark payout as completed after bank transfer success
    """
    
    payout = get_payout_request(payout_id)
    wallet = get_wallet(payout.wallet_id)
    
    # 1. Update payout status
    update_payout_request(
        payout_id=payout_id,
        status="completed",
        completed_at=datetime.now(),
        bank_transfer_completed_at=datetime.now(),
        bank_transfer_reference=bank_transfer_reference
    )
    
    # 2. Debit wallet (move from reserved to debited)
    create_wallet_transaction(
        wallet_id=payout.wallet_id,
        transaction_type="debit",
        amount_cents=payout.requested_amount_cents,
        source_type="payout_completed",
        source_reference_id=payout_id,
        description=f"Payout completed - {payout.payout_number}",
        debit_account=f"wallet:{wallet.wallet_id}",
        credit_account="bank_account",
        status="completed"
    )
    
    # 3. Update wallet balances (ATOMIC)
    update_wallet_balance(
        wallet_id=payout.wallet_id,
        delta_available_cents=-payout.requested_amount_cents,
        delta_reserved_cents=-payout.requested_amount_cents  # Release reservation
    )
    
    # 4. Record tax withholding
    if payout.tax_withheld_cents > 0:
        record_tds_withholding(
            wallet_id=payout.wallet_id,
            payout_id=payout_id,
            amount_cents=payout.tax_withheld_cents,
            rate=wallet.default_tax_withholding_rate,
            financial_year=get_current_financial_year(),
            quarter=get_current_quarter()
        )
    
    # 5. Send confirmation notification
    send_notification(
        user_id=payout.payee_id,
        notification_type="payout_completed",
        title="Payout Successful",
        message=(
            f"Your payout of ₹{payout.net_payout_cents/100:.2f} has been "
            f"transferred successfully. Reference: {bank_transfer_reference}"
        )
    )
    
    # 6. Emit event
    emit_event("payout.completed", {
        "payout_id": payout_id,
        "wallet_id": payout.wallet_id,
        "amount_cents": payout.net_payout_cents,
        "bank_reference": bank_transfer_reference
    })
```

---

## 🔒 SECTION G — SCHEDULED PAYOUT BATCH PROCESSING

### Cron Job Configuration
```yaml
# Weekly Payout (Every Friday at 10:00 AM IST)
weekly_payout_job:
  schedule: "0 10 * * 5"  # Cron format
  timezone: "Asia/Kolkata"
  task: "process_scheduled_payouts"
  args:
    payout_schedule: "weekly"

# Biweekly Payout (1st and 15th at 10:00 AM IST)
biweekly_payout_job:
  schedule: "0 10 1,15 * *"
  timezone: "Asia/Kolkata"
  task: "process_scheduled_payouts"
  args:
    payout_schedule: "biweekly"

# Monthly Payout (1st of month at 10:00 AM IST)
monthly_payout_job:
  schedule: "0 10 1 * *"
  timezone: "Asia/Kolkata"
  task: "process_scheduled_payouts"
  args:
    payout_schedule: "monthly"
```

### Batch Payout Processing
```python
def process_scheduled_payouts(payout_schedule: str):
    """
    Process all scheduled payouts for given schedule type
    Runs as cron job
    """
    
    logger.info(f"Starting scheduled payout batch for {payout_schedule}")
    
    # 1. Find all eligible wallets
    eligible_wallets = get_eligible_wallets_for_payout(payout_schedule)
    
    logger.info(f"Found {len(eligible_wallets)} eligible wallets")
    
    # 2. Process each wallet
    batch_results = {
        "total": len(eligible_wallets),
        "successful": 0,
        "failed": 0,
        "errors": []
    }
    
    for wallet in eligible_wallets:
        try:
            # Create payout request
            payout_request = process_payout_request(
                wallet_id=wallet.wallet_id,
                requested_amount_cents=wallet.available_balance_cents,
                request_type="scheduled",
                requested_by_user_id=None  # System-initiated
            )
            
            batch_results["successful"] += 1
            
            logger.info(
                f"Created payout request {payout_request.payout_number} "
                f"for wallet {wallet.wallet_id}"
            )
        
        except Exception as e:
            batch_results["failed"] += 1
            batch_results["errors"].append({
                "wallet_id": str(wallet.wallet_id),
                "error": str(e)
            })
            
            logger.error(
                f"Failed to create payout for wallet {wallet.wallet_id}: {str(e)}"
            )
    
    # 3. Send batch summary report
    send_batch_payout_report(
        schedule=payout_schedule,
        results=batch_results
    )
    
    logger.info(
        f"Completed scheduled payout batch. "
        f"Successful: {batch_results['successful']}, "
        f"Failed: {batch_results['failed']}"
    )
    
    return batch_results


def get_eligible_wallets_for_payout(payout_schedule: str) -> List[Wallet]:
    """
    Get wallets eligible for scheduled payout
    
    Criteria:
    - wallet_status = active
    - payout_schedule matches
    - available_balance >= payout_threshold
    - kyc_verified = true
    - pan_verified = true
    - no pending payout requests
    """
    
    return Wallet.query.filter(
        Wallet.wallet_status == "active",
        Wallet.payout_schedule == payout_schedule,
        Wallet.available_balance_cents >= Wallet.payout_threshold_cents,
        Wallet.kyc_verified == True,
        Wallet.pan_verified == True,
        ~Wallet.wallet_id.in_(
            # Exclude wallets with pending payouts
            select(PayoutRequest.wallet_id).where(
                PayoutRequest.status.in_(["pending", "approved", "processing"])
            )
        )
    ).all()
```

---

## 🔒 SECTION H — RECONCILIATION ENGINE

### Daily Reconciliation Process
```python
def reconcile_daily_payouts(date: date):
    """
    Reconcile all payouts for a given date
    Match internal records with bank statements
    """
    
    logger.info(f"Starting daily payout reconciliation for {date}")
    
    # 1. Get all payouts completed on this date
    payouts = get_payouts_completed_on_date(date)
    
    # 2. Fetch bank statement for this date
    bank_statement = fetch_bank_statement_api(date)
    
    # 3. Match payouts with bank transactions
    reconciliation_results = {
        "total_payouts": len(payouts),
        "matched": 0,
        "unmatched": 0,
        "discrepancies": []
    }
    
    for payout in payouts:
        # Find matching bank transaction
        bank_txn = find_matching_bank_transaction(
            bank_statement,
            payout.bank_transfer_reference,
            payout.net_payout_cents
        )
        
        if bank_txn:
            # Match found
            if bank_txn["amount_cents"] == payout.net_payout_cents:
                # Perfect match
                mark_payout_as_reconciled(
                    payout_id=payout.payout_id,
                    bank_transaction_id=bank_txn["transaction_id"],
                    reconciliation_status="matched"
                )
                reconciliation_results["matched"] += 1
            else:
                # Amount mismatch
                reconciliation_results["discrepancies"].append({
                    "payout_id": str(payout.payout_id),
                    "payout_amount": payout.net_payout_cents,
                    "bank_amount": bank_txn["amount_cents"],
                    "difference": abs(payout.net_payout_cents - bank_txn["amount_cents"])
                })
                
                mark_payout_as_reconciled(
                    payout_id=payout.payout_id,
                    bank_transaction_id=bank_txn["transaction_id"],
                    reconciliation_status="amount_mismatch"
                )
                
                # Alert finance team
                send_alert(
                    alert_type="reconciliation_discrepancy",
                    severity="high",
                    message=f"Payout {payout.payout_number} has amount mismatch"
                )
        else:
            # No matching bank transaction
            reconciliation_results["unmatched"] += 1
            
            mark_payout_as_reconciled(
                payout_id=payout.payout_id,
                reconciliation_status="unmatched"
            )
            
            # Alert for investigation
            send_alert(
                alert_type="reconciliation_unmatched",
                severity="critical",
                message=f"Payout {payout.payout_number} not found in bank statement"
            )
    
    # 4. Generate reconciliation report
    generate_reconciliation_report(date, reconciliation_results)
    
    logger.info(
        f"Reconciliation complete. "
        f"Matched: {reconciliation_results['matched']}, "
        f"Unmatched: {reconciliation_results['unmatched']}, "
        f"Discrepancies: {len(reconciliation_results['discrepancies'])}"
    )
    
    return reconciliation_results
```

---

## 🔒 SECTION I — ESCROW MANAGEMENT

### Escrow Hold & Release
```python
def create_escrow_hold(
    reference_type: str,
    reference_id: UUID,
    payer_id: UUID,
    payee_id: UUID,
    amount_cents: int,
    release_condition: Dict[str, Any]
) -> EscrowAccount:
    """
    Hold funds in escrow until conditions met
    
    Use cases:
    - Milestone-based project payments
    - Dispute resolution holds
    - Refund guarantees
    """
    
    # 1. Verify payer has paid
    payment = verify_payment_for_escrow(reference_type, reference_id)
    
    if payment.amount_cents < amount_cents:
        raise ValueError("Payment amount insufficient for escrow")
    
    # 2. Create escrow account
    escrow = EscrowAccount.create(
        reference_type=reference_type,
        reference_id=reference_id,
        payer_id=payer_id,
        payee_id=payee_id,
        escrow_amount_cents=amount_cents,
        release_condition_type=release_condition["type"],
        release_condition_data=release_condition,
        status="held"
    )
    
    # 3. If time-based, schedule release
    if release_condition["type"] == "time_based":
        release_date = release_condition["release_date"]
        schedule_escrow_release(escrow.escrow_id, release_date)
    
    # 4. Emit event
    emit_event("escrow.created", {
        "escrow_id": escrow.escrow_id,
        "amount_cents": amount_cents,
        "payee_id": payee_id
    })
    
    return escrow


def release_escrow(
    escrow_id: UUID,
    release_reason: str,
    verified_by: UUID
):
    """
    Release escrowed funds to payee
    """
    
    escrow = get_escrow_account(escrow_id)
    
    if escrow.status != "held":
        raise ValueError(f"Escrow is {escrow.status}, cannot release")
    
    # 1. Verify release conditions met
    if not verify_escrow_release_conditions(escrow):
        raise ValueError("Release conditions not met")
    
    # 2. Get or create payee wallet
    payee_wallet = get_or_create_wallet(
        owner_id=escrow.payee_id,
        owner_type="user"  # Determine from context
    )
    
    # 3. Credit payee wallet
    credit_wallet(
        wallet_id=payee_wallet.wallet_id,
        amount_cents=escrow.escrow_amount_cents,
        source_type="escrow_release",
        source_reference_id=escrow_id,
        description=f"Escrow release - {release_reason}"
    )
    
    # 4. Update escrow status
    update_escrow_account(
        escrow_id=escrow_id,
        status="released",
        released_at=datetime.now(),
        release_amount_cents=escrow.escrow_amount_cents,
        release_to_wallet_id=payee_wallet.wallet_id,
        release_reason=release_reason
    )
    
    # 5. Emit event
    emit_event("escrow.released", {
        "escrow_id": escrow_id,
        "wallet_id": payee_wallet.wallet_id,
        "amount_cents": escrow.escrow_amount_cents
    })
    
    # 6. Notify payee
    send_notification(
        user_id=escrow.payee_id,
        notification_type="escrow_released",
        title="Funds Released",
        message=f"₹{escrow.escrow_amount_cents/100:.2f} has been released to your wallet"
    )
```

---

## 🔒 SECTION J — REFERRAL COMMISSION TRACKING

### Referral Commission System
```python
def process_referral_commission(
    referrer_id: UUID,
    referred_user_id: UUID,
    trigger_event_type: str,  # user_signup, first_payment, subscription
    trigger_amount_cents: Optional[int] = None
):
    """
    Calculate and credit referral commission
    
    Commission types:
    - Flat signup bonus: ₹100 on user signup
    - Percentage of first payment: 10% of first transaction
    - Recurring commission: 5% of all payments for 1 year
    """
    
    # 1. Check if referral is valid
    referral = get_referral_record(referrer_id, referred_user_id)
    
    if not referral:
        logger.warning(f"No referral record found for {referred_user_id}")
        return
    
    # 2. Check if referral is fraudulent
    if is_referral_fraudulent(referral):
        logger.warning(f"Referral {referral.referral_id} flagged as fraudulent")
        return
    
    # 3. Get applicable commission rule
    commission_rule = get_referral_commission_rule(trigger_event_type)
    
    if not commission_rule:
        logger.info(f"No commission rule for {trigger_event_type}")
        return
    
    # 4. Calculate commission amount
    if commission_rule.commission_type == "flat_amount":
        commission_cents = commission_rule.commission_amount_cents
    
    elif commission_rule.commission_type == "percentage":
        if trigger_amount_cents is None:
            raise ValueError("trigger_amount_cents required for percentage commission")
        
        commission_cents = int(
            trigger_amount_cents * (commission_rule.commission_percentage / 100)
        )
    
    else:
        raise ValueError(f"Unsupported commission type: {commission_rule.commission_type}")
    
    # 5. Check commission limits
    max_commission = commission_rule.metadata.get("max_commission_cents")
    if max_commission and commission_cents > max_commission:
        commission_cents = max_commission
    
    # 6. Get or create referrer wallet
    referrer_wallet = get_or_create_wallet(
        owner_id=referrer_id,
        owner_type="referrer"
    )
    
    # 7. Credit commission
    credit_wallet(
        wallet_id=referrer_wallet.wallet_id,
        amount_cents=commission_cents,
        source_type="referral_commission",
        source_reference_id=referral.referral_id,
        description=f"Referral commission - {trigger_event_type}",
        metadata={
            "referred_user_id": str(referred_user_id),
            "trigger_event": trigger_event_type,
            "commission_rule_id": str(commission_rule.rule_id)
        }
    )
    
    # 8. Update referral record
    update_referral_commission_paid(
        referral_id=referral.referral_id,
        commission_cents=commission_cents,
        trigger_event=trigger_event_type
    )
    
    # 9. Send notification
    send_notification(
        user_id=referrer_id,
        notification_type="referral_commission_earned",
        title="Referral Commission Earned! 🎉",
        message=f"You earned ₹{commission_cents/100:.2f} for referring {get_user_name(referred_user_id)}"
    )
    
    # 10. Emit event
    emit_event("referral.commission_paid", {
        "referrer_id": referrer_id,
        "referred_user_id": referred_user_id,
        "commission_cents": commission_cents,
        "trigger_event": trigger_event_type
    })
```

---

## 🔒 SECTION K — TAX COMPLIANCE & REPORTING

### TDS Certificate Generation
```python
def generate_tds_certificate(
    wallet_id: UUID,
    financial_year: str,  # "2025-26"
    quarter: str  # "Q1", "Q2", "Q3", "Q4"
) -> str:
    """
    Generate TDS certificate (Form 16A) for wallet owner
    
    Required for Indian tax compliance
    """
    
    # 1. Get all TDS records for period
    tds_records = get_tds_records(
        wallet_id=wallet_id,
        financial_year=financial_year,
        quarter=quarter
    )
    
    if not tds_records:
        raise ValueError("No TDS records found for this period")
    
    # 2. Aggregate TDS data
    total_income_cents = sum(r.gross_amount_cents for r in tds_records)
    total_tds_cents = sum(r.tds_amount_cents for r in tds_records)
    
    # 3. Get wallet owner details
    wallet = get_wallet(wallet_id)
    owner = get_user(wallet.owner_id)
    
    # 4. Generate TDS certificate PDF
    certificate_data = {
        "certificate_number": generate_tds_certificate_number(financial_year, quarter),
        "financial_year": financial_year,
        "quarter": quarter,
        "deductee_name": wallet.owner_name,
        "deductee_pan": decrypt(wallet.pan_number_encrypted),
        "deductor_name": "Antigravity SaaS Pvt Ltd",
        "deductor_pan": "AANCP1234F",  # Platform PAN
        "deductor_tan": "BLRP12345F",  # Platform TAN
        "total_income": total_income_cents / 100,
        "total_tds": total_tds_cents / 100,
        "tds_rate": wallet.default_tax_withholding_rate,
        "tds_records": [
            {
                "date": r.deduction_date.strftime("%d-%b-%Y"),
                "amount": r.gross_amount_cents / 100,
                "tds": r.tds_amount_cents / 100
            }
            for r in tds_records
        ]
    }
    
    pdf_url = generate_form_16a_pdf(certificate_data)
    
    # 5. Store certificate record
    create_tds_certificate_record(
        wallet_id=wallet_id,
        financial_year=financial_year,
        quarter=quarter,
        certificate_data=certificate_data,
        pdf_url=pdf_url
    )
    
    # 6. Send to wallet owner
    send_notification(
        user_id=wallet.owner_id,
        notification_type="tds_certificate_generated",
        title="TDS Certificate Available",
        message=f"Your TDS certificate for {financial_year} {quarter} is ready",
        attachments=[pdf_url]
    )
    
    return pdf_url
```

---

## 🔒 SECTION L — FRAUD DETECTION & PREVENTION

### Commission Fraud Detection
```python
def detect_commission_fraud(wallet_id: UUID) -> Dict[str, Any]:
    """
    Detect fraudulent commission patterns
    
    Red flags:
    - Abnormally high commission rate
    - Circular referrals
    - Fake transactions
    - Rapid credit/debit cycles
    - Multiple wallets same bank account
    """
    
    fraud_score = 0.0
    fraud_flags = []
    
    # Signal 1: Abnormally high commission rate
    wallet = get_wallet(wallet_id)
    avg_commission_rate = calculate_average_commission_rate(wallet_id)
    
    if avg_commission_rate > 50:  # > 50% commission
        fraud_score += 30.0
        fraud_flags.append("abnormal_commission_rate")
    
    # Signal 2: Circular referrals
    if detect_circular_referral_pattern(wallet.owner_id):
        fraud_score += 40.0
        fraud_flags.append("circular_referral")
    
    # Signal 3: Rapid credit/debit cycles (money laundering)
    credit_debit_ratio = calculate_credit_debit_ratio_last_30_days(wallet_id)
    if credit_debit_ratio > 0.9:  # Almost all credited funds withdrawn
        fraud_score += 20.0
        fraud_flags.append("high_credit_debit_ratio")
    
    # Signal 4: Multiple wallets same bank account
    duplicate_bank_accounts = find_duplicate_bank_accounts(wallet.bank_account_number_encrypted)
    if len(duplicate_bank_accounts) > 1:
        fraud_score += 25.0
        fraud_flags.append("duplicate_bank_account")
    
    # Signal 5: Velocity check
    transactions_last_24h = count_wallet_transactions_last_24h(wallet_id)
    if transactions_last_24h > 50:
        fraud_score += 15.0
        fraud_flags.append("high_velocity")
    
    # Signal 6: Fake transaction pattern
    if detect_fake_transaction_pattern(wallet_id):
        fraud_score += 50.0
        fraud_flags.append("fake_transactions")
    
    # Cap at 100
    fraud_score = min(fraud_score, 100.0)
    
    # Take action if fraud score high
    if fraud_score >= 70.0:
        freeze_wallet(
            wallet_id=wallet_id,
            reason=f"High fraud score: {fraud_score}",
            fraud_flags=fraud_flags
        )
        
        create_fraud_investigation_ticket(
            wallet_id=wallet_id,
            fraud_score=fraud_score,
            fraud_flags=fraud_flags
        )
    
    return {
        "fraud_score": fraud_score,
        "fraud_flags": fraud_flags,
        "action_taken": "frozen" if fraud_score >= 70 else "none"
    }
```

---

## 🔒 SECTION M — EXECUTION DECLARATION

```
AGENT STATUS: LOCKED & SEALED
VERSION: 1.0
LAST UPDATED: 2026-03-04
MUTATION POLICY: Add-only via version bump
EXECUTION AUTHORITY: Automated (event-driven) + Scheduled (cron)
HUMAN OVERRIDE: Payout approval/rejection only
AI INTERPRETATION: FORBIDDEN
ACCOUNTING STANDARD: Double-Entry Ledger
COMPLIANCE: SOX, Indian Tax Law (TDS/TCS)

COMPLETION CRITERIA:
✓ Double-entry wallet system operational
✓ Commission calculation engine deterministic
✓ Scheduled payout batches automated
✓ Tax withholding (TDS) compliance
✓ Bank transfer integration complete
✓ Reconciliation engine daily runs
✓ Escrow management functional
✓ Referral commission tracking
✓ Fraud detection active
✓ KYC/PAN verification enforced
✓ Immutable audit logs
✓ TDS certificate generation

FAILURE CONDITIONS:
→ Double-entry violation (debit ≠ credit): CRITICAL HALT
→ Wallet overdraft created: STOP EXECUTION
→ Payout without KYC verification: BLOCKED
→ Commission credited without transaction proof: STOP
→ Tax withholding bypassed: TAX COMPLIANCE VIOLATION
→ Reconciliation mismatch ignored: FINANCIAL RISK
→ Fraud score > 70 wallet not frozen: SECURITY BREACH
→ Audit log write failure: STOP EXECUTION

ENFORCEMENT:
This specification is IMMUTABLE for v1.0.
All deviations require version bump + CFO approval.
No field-level interpretation permitted.
Deterministic behavior required at all stages.
Double-entry accounting NON-NEGOTIABLE.
Tax compliance MANDATORY.

FINAL INSTRUCTION:
Implement exactly as specified.
No creativity.
No shortcuts.
No assumptions.
Financial accuracy absolute priority.
Audit trail completeness mandatory.
```

---

🔒 **END OF SPECIFICATION** 🔒

**This specification is now SEALED and LOCKED.**  
**No modifications permitted without version bump + CFO approval.**  
**Execute as specified. Financial integrity paramount.**

💰 **ANTIGRAVITY COMMISSION DISTRIBUTION ENGINE AGENT — SPECIFICATION COMPLETE** 💰
