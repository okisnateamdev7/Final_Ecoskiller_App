# 📊 REVENUE SPLIT AUTOMATION AGENT — ANTIGRAVITY v1.0

**Artifact Class:** Production System Blueprint  
**Mutation Policy:** Add-only via version bump  
**Execution Mode:** Deterministic  
**Stack Lock:** Enforced  
**Interface Freeze:** Required  
**Domain:** Financial & Commission Management  
**Subsystem:** Revenue Split & Recognition Infrastructure  
**Compliance:** ASC 606, IFRS 15, SOX, GAAP  

---

## 🔒 SECTION A — AGENT IDENTITY & AUTHORITY

### Agent Classification
```
Agent Name: REVENUE_SPLIT_AUTOMATION_AGENT
Agent Type: Revenue Allocation & Recognition Orchestrator
Execution Mode: Event-Driven + Rule-Based + Auditable
Determinism Rule: Identical contract → Identical split allocation
Failure Mode: STOP → ROLLBACK → ALERT → NO PARTIAL RECOGNITION
Human Override: Revenue Recognition Override (with CFO approval)
AI Override: FORBIDDEN
Accounting Standard: ASC 606 / IFRS 15 Compliant
Audit Requirement: IMMUTABLE REVENUE LOGS
```

### Authority Boundaries
```
PERMITTED:
✓ Revenue split calculation based on contracts
✓ Performance obligation identification
✓ Transaction price allocation
✓ Multi-party revenue distribution
✓ Deferred revenue scheduling
✓ Revenue recognition event processing
✓ Contract modification handling
✓ Principal vs Agent classification
✓ Stand-alone selling price (SSP) determination
✓ Co-teaching agreement processing
✓ Marketplace commission allocation
✓ Partner split orchestration
✓ Refund revenue reversal

FORBIDDEN:
✗ Revenue recognition without contract
✗ Manual revenue booking without evidence
✗ Billing-driven revenue recognition (cash basis)
✗ Split allocation without performance obligation mapping
✗ Revenue recognition before control transfer
✗ Deferred revenue modification without journal entry
✗ Split ratio modification post-transaction
✗ Revenue reversal without audit trail
✗ Bypassing collectability assessment
✗ Gross recognition when acting as agent
```

---

## 🔒 SECTION B — REVENUE SPLIT TAXONOMY (LOCKED)

### Revenue Split Scenarios (Non-Negotiable)
```
1. SINGLE-PARTY REVENUE (100% Platform)
   - Job posting fees
   - Recruiter subscriptions
   - Premium features
   - Advertisement revenue
   
2. TWO-PARTY SPLIT (Platform + Service Provider)
   - Course sales: 80% trainer, 20% platform
   - Event tickets: 85% organizer, 15% platform
   - Dojo sessions: 70% mentor, 30% platform
   - Consulting fees: 75% consultant, 25% platform

3. THREE-PARTY SPLIT (Co-Teaching)
   - Course revenue: 40% trainer1, 40% trainer2, 20% platform
   - Workshop: 35% expert1, 35% expert2, 30% platform
   - Collaborative content: Configurable splits

4. FOUR-PARTY SPLIT (With Referral)
   - Course sale via referral:
     * 75% trainer
     * 15% platform
     * 5% referrer
     * 5% affiliate

5. TIERED SPLIT (Volume-Based)
   - Revenue 0-₹10k: 80% creator, 20% platform
   - Revenue ₹10k-₹50k: 85% creator, 15% platform
   - Revenue >₹50k: 90% creator, 10% platform

6. TIME-BASED SPLIT (Subscription)
   - Month 1-6: 70% partner, 30% platform
   - Month 7-12: 75% partner, 25% platform
   - Month 13+: 80% partner, 20% platform

7. PERFORMANCE-BASED SPLIT
   - Base: 70% creator
   - +5% bonus if completion rate >80%
   - +5% bonus if rating >4.5 stars
   - Platform gets remainder

8. MILESTONE-BASED SPLIT (Project Revenue)
   - Payment on milestone 1: 100% held in escrow
   - Milestone 1 complete: 30% to developer, 70% held
   - Milestone 2 complete: 40% to developer, 30% held
   - Milestone 3 complete: 30% to developer, 0% held
```

---

## 🔒 SECTION C — DATA MODEL (IMMUTABLE SCHEMA)

### Revenue Contract Entity
```sql
CREATE TABLE revenue_contracts (
    contract_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    contract_number VARCHAR(50) UNIQUE NOT NULL, -- Format: RC-YYYY-MM-XXXXXX
    
    -- Contract Parties
    customer_id UUID NOT NULL REFERENCES users(user_id),
    customer_name VARCHAR(255) NOT NULL,
    customer_type VARCHAR(50) NOT NULL, -- student, company, institution
    
    -- Contract Details
    contract_type VARCHAR(50) NOT NULL, -- course_purchase, subscription, event_ticket, etc.
    contract_date DATE NOT NULL,
    contract_start_date DATE NOT NULL,
    contract_end_date DATE,
    
    -- Transaction Price (ASC 606 Step 3)
    total_contract_value_cents BIGINT NOT NULL,
    currency VARCHAR(3) NOT NULL DEFAULT 'INR',
    
    -- Variable Consideration
    has_variable_consideration BOOLEAN DEFAULT FALSE,
    variable_consideration_type VARCHAR(50), -- discount, refund_right, performance_bonus
    variable_consideration_estimate_cents BIGINT,
    constraint_applied BOOLEAN DEFAULT FALSE, -- Revenue constraint to avoid reversal
    
    -- Payment Terms
    payment_terms VARCHAR(50) NOT NULL, -- upfront, installment, milestone, subscription
    collectability_assessment VARCHAR(50) NOT NULL, -- high, medium, low, uncertain
    
    -- Contract Status
    contract_status VARCHAR(50) NOT NULL DEFAULT 'active', -- draft, active, fulfilled, terminated, disputed
    
    -- Performance Obligations Count
    total_obligations INTEGER NOT NULL DEFAULT 1,
    fulfilled_obligations INTEGER DEFAULT 0,
    
    -- Revenue Recognition Summary
    total_revenue_recognized_cents BIGINT DEFAULT 0,
    total_deferred_revenue_cents BIGINT DEFAULT 0,
    
    -- Modification Tracking
    is_modified BOOLEAN DEFAULT FALSE,
    original_contract_id UUID REFERENCES revenue_contracts(contract_id),
    modification_type VARCHAR(50), -- separate_contract, prospective, retrospective
    
    -- Principal vs Agent (ASC 606 Marketplace)
    platform_role VARCHAR(20) NOT NULL, -- principal, agent
    
    -- Metadata
    metadata JSONB,
    
    -- Audit
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
    created_by UUID NOT NULL REFERENCES users(user_id),
    
    -- Multi-Tenant
    tenant_id UUID NOT NULL REFERENCES tenants(tenant_id),
    
    -- Constraints
    CONSTRAINT valid_contract_type CHECK (contract_type IN (
        'course_purchase', 'subscription', 'event_ticket', 'certification_fee',
        'consulting_service', 'dojo_session', 'innovation_license', 'job_posting'
    )),
    CONSTRAINT valid_collectability CHECK (collectability_assessment IN (
        'high', 'medium', 'low', 'uncertain'
    )),
    CONSTRAINT valid_platform_role CHECK (platform_role IN ('principal', 'agent')),
    CONSTRAINT valid_contract_status CHECK (contract_status IN (
        'draft', 'active', 'fulfilled', 'terminated', 'disputed'
    ))
);

-- Indexes
CREATE INDEX idx_revenue_contracts_customer ON revenue_contracts(customer_id, contract_date DESC);
CREATE INDEX idx_revenue_contracts_type ON revenue_contracts(contract_type, contract_status);
CREATE INDEX idx_revenue_contracts_status ON revenue_contracts(contract_status, contract_date);
CREATE INDEX idx_revenue_contracts_tenant ON revenue_contracts(tenant_id);
```

### Performance Obligation Entity (ASC 606 Step 2)
```sql
CREATE TABLE performance_obligations (
    obligation_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    contract_id UUID NOT NULL REFERENCES revenue_contracts(contract_id),
    
    -- Obligation Details
    obligation_sequence INTEGER NOT NULL, -- 1, 2, 3... for ordering
    obligation_description TEXT NOT NULL,
    obligation_type VARCHAR(50) NOT NULL, -- course_delivery, certification_issuance, support_service
    
    -- Distinctness (ASC 606 Requirement)
    is_distinct BOOLEAN NOT NULL, -- Can be sold separately
    bundled_with_obligation_id UUID REFERENCES performance_obligations(obligation_id),
    
    -- Stand-Alone Selling Price (SSP) - ASC 606 Step 4
    ssp_cents BIGINT NOT NULL, -- Stand-alone selling price
    ssp_determination_method VARCHAR(50) NOT NULL, -- observable, adjusted_market, expected_cost_plus_margin, residual
    ssp_last_reviewed_at DATE NOT NULL,
    
    -- Allocated Transaction Price (ASC 606 Step 4)
    allocated_price_cents BIGINT NOT NULL,
    allocation_percentage NUMERIC(5,2) NOT NULL, -- % of total contract value
    
    -- Revenue Recognition Timing (ASC 606 Step 5)
    recognition_timing VARCHAR(50) NOT NULL, -- point_in_time, over_time
    
    -- Point-in-Time Criteria
    control_transfer_event VARCHAR(50), -- course_completion, certificate_issued, access_granted
    
    -- Over-Time Criteria
    over_time_method VARCHAR(50), -- input_method, output_method, time_elapsed
    over_time_duration_days INTEGER,
    over_time_start_date DATE,
    over_time_end_date DATE,
    
    -- Progress Measurement (for over-time)
    progress_percentage NUMERIC(5,2) DEFAULT 0.00, -- 0 to 100
    
    -- Fulfillment Status
    fulfillment_status VARCHAR(50) NOT NULL DEFAULT 'pending', -- pending, in_progress, fulfilled, canceled
    fulfillment_date DATE,
    fulfillment_evidence JSONB, -- Proof of delivery/completion
    
    -- Revenue Recognition Status
    revenue_recognized_cents BIGINT DEFAULT 0,
    deferred_revenue_cents BIGINT DEFAULT 0,
    
    -- Metadata
    metadata JSONB,
    
    -- Audit
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
    
    -- Multi-Tenant
    tenant_id UUID NOT NULL REFERENCES tenants(tenant_id),
    
    -- Constraints
    CONSTRAINT valid_recognition_timing CHECK (recognition_timing IN (
        'point_in_time', 'over_time'
    )),
    CONSTRAINT valid_ssp_method CHECK (ssp_determination_method IN (
        'observable', 'adjusted_market', 'expected_cost_plus_margin', 'residual'
    )),
    CONSTRAINT valid_fulfillment_status CHECK (fulfillment_status IN (
        'pending', 'in_progress', 'fulfilled', 'canceled'
    )),
    CONSTRAINT positive_allocation CHECK (allocated_price_cents >= 0),
    
    -- Unique obligation per contract sequence
    CONSTRAINT unique_contract_obligation UNIQUE (contract_id, obligation_sequence)
);

-- Indexes
CREATE INDEX idx_performance_obligations_contract ON performance_obligations(contract_id, obligation_sequence);
CREATE INDEX idx_performance_obligations_status ON performance_obligations(fulfillment_status);
CREATE INDEX idx_performance_obligations_timing ON performance_obligations(recognition_timing);
CREATE INDEX idx_performance_obligations_tenant ON performance_obligations(tenant_id);
```

### Revenue Split Configuration
```sql
CREATE TABLE revenue_split_configs (
    split_config_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    config_name VARCHAR(255) NOT NULL,
    config_code VARCHAR(50) UNIQUE NOT NULL, -- e.g., COURSE_80_20, COTEACH_40_40_20
    
    -- Applicability
    applies_to_contract_type VARCHAR(50) NOT NULL,
    applies_to_conditions JSONB, -- Additional matching conditions
    
    -- Split Parties
    split_parties JSONB NOT NULL, -- Array of {role, percentage, min_amount_cents, max_amount_cents}
    
    -- Split Logic Type
    split_type VARCHAR(50) NOT NULL, -- fixed_percentage, tiered, performance_based, milestone_based
    
    -- Tiered Configuration
    tiered_config JSONB, -- [{revenue_min, revenue_max, splits: [...]}]
    
    -- Performance-Based Configuration
    performance_metrics JSONB, -- [{metric, threshold, bonus_percentage}]
    
    -- Milestone-Based Configuration
    milestone_config JSONB, -- [{milestone_name, split_percentages}]
    
    -- Timing
    split_timing VARCHAR(50) NOT NULL DEFAULT 'on_recognition', -- on_recognition, on_payment, on_settlement
    
    -- Priority (for conflict resolution)
    priority INTEGER DEFAULT 100,
    
    -- Validity Period
    valid_from DATE NOT NULL,
    valid_until DATE,
    
    -- Status
    is_active BOOLEAN DEFAULT TRUE,
    
    -- Audit
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
    created_by UUID NOT NULL REFERENCES users(user_id),
    
    -- Multi-Tenant
    tenant_id UUID NOT NULL REFERENCES tenants(tenant_id),
    
    -- Constraints
    CONSTRAINT valid_split_type CHECK (split_type IN (
        'fixed_percentage', 'tiered', 'performance_based', 'milestone_based', 'time_based'
    )),
    CONSTRAINT valid_split_timing CHECK (split_timing IN (
        'on_recognition', 'on_payment', 'on_settlement'
    ))
);

-- Indexes
CREATE INDEX idx_revenue_split_configs_type ON revenue_split_configs(applies_to_contract_type);
CREATE INDEX idx_revenue_split_configs_active ON revenue_split_configs(is_active, priority DESC);
CREATE INDEX idx_revenue_split_configs_tenant ON revenue_split_configs(tenant_id);
```

### Revenue Allocation Record
```sql
CREATE TABLE revenue_allocations (
    allocation_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    
    -- Source
    contract_id UUID NOT NULL REFERENCES revenue_contracts(contract_id),
    obligation_id UUID NOT NULL REFERENCES performance_obligations(obligation_id),
    payment_transaction_id UUID REFERENCES payment_transactions(transaction_id),
    
    -- Allocation Configuration
    split_config_id UUID REFERENCES revenue_split_configs(split_config_id),
    split_config_code VARCHAR(50) NOT NULL,
    
    -- Recipient
    recipient_role VARCHAR(50) NOT NULL, -- trainer, platform, co_trainer, organizer, mentor, referrer
    recipient_id UUID, -- NULL for platform
    recipient_name VARCHAR(255),
    
    -- Allocation Amounts
    gross_allocation_cents BIGINT NOT NULL, -- Before any deductions
    platform_fee_cents BIGINT DEFAULT 0, -- If recipient is not platform
    tax_withheld_cents BIGINT DEFAULT 0,
    net_allocation_cents BIGINT NOT NULL, -- After deductions
    allocation_percentage NUMERIC(5,2) NOT NULL,
    
    -- Revenue Recognition Status
    recognition_status VARCHAR(50) NOT NULL DEFAULT 'deferred', -- deferred, recognized, reversed
    recognized_amount_cents BIGINT DEFAULT 0,
    deferred_amount_cents BIGINT DEFAULT 0,
    
    -- Recognition Timeline
    recognition_scheduled_date DATE,
    recognition_completed_date DATE,
    
    -- Deferred Revenue Schedule (for over-time recognition)
    deferred_schedule JSONB, -- [{period, amount_cents, recognized}]
    
    -- Commission/Payout Status
    payout_status VARCHAR(50) DEFAULT 'pending', -- pending, credited, paid
    payout_reference_id UUID, -- Links to wallet_transaction or payout_request
    
    -- Metadata
    metadata JSONB,
    
    -- Audit
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
    
    -- Multi-Tenant
    tenant_id UUID NOT NULL REFERENCES tenants(tenant_id),
    
    -- Constraints
    CONSTRAINT valid_recognition_status CHECK (recognition_status IN (
        'deferred', 'partially_recognized', 'recognized', 'reversed'
    )),
    CONSTRAINT valid_payout_status CHECK (payout_status IN (
        'pending', 'credited', 'paid', 'on_hold'
    )),
    CONSTRAINT positive_allocation CHECK (gross_allocation_cents >= 0)
);

-- Indexes
CREATE INDEX idx_revenue_allocations_contract ON revenue_allocations(contract_id);
CREATE INDEX idx_revenue_allocations_obligation ON revenue_allocations(obligation_id);
CREATE INDEX idx_revenue_allocations_recipient ON revenue_allocations(recipient_id, recognition_status);
CREATE INDEX idx_revenue_allocations_recognition ON revenue_allocations(recognition_status, recognition_scheduled_date);
CREATE INDEX idx_revenue_allocations_payout ON revenue_allocations(payout_status);
CREATE INDEX idx_revenue_allocations_tenant ON revenue_allocations(tenant_id);
```

### Deferred Revenue Schedule
```sql
CREATE TABLE deferred_revenue_schedule (
    schedule_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    allocation_id UUID NOT NULL REFERENCES revenue_allocations(allocation_id),
    
    -- Period
    accounting_period VARCHAR(7) NOT NULL, -- YYYY-MM format
    fiscal_year INTEGER NOT NULL,
    fiscal_quarter VARCHAR(2), -- Q1, Q2, Q3, Q4
    
    -- Schedule Details
    period_start_date DATE NOT NULL,
    period_end_date DATE NOT NULL,
    
    -- Amount
    scheduled_recognition_cents BIGINT NOT NULL,
    
    -- Status
    recognition_status VARCHAR(50) NOT NULL DEFAULT 'pending', -- pending, recognized, adjusted
    recognized_date DATE,
    recognized_amount_cents BIGINT DEFAULT 0,
    
    -- Adjustments
    adjustment_amount_cents BIGINT DEFAULT 0,
    adjustment_reason TEXT,
    
    -- Metadata
    metadata JSONB,
    
    -- Audit
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
    
    -- Multi-Tenant
    tenant_id UUID NOT NULL REFERENCES tenants(tenant_id),
    
    -- Constraints
    CONSTRAINT valid_schedule_status CHECK (recognition_status IN (
        'pending', 'recognized', 'adjusted', 'canceled'
    ))
);

-- Indexes
CREATE INDEX idx_deferred_schedule_allocation ON deferred_revenue_schedule(allocation_id);
CREATE INDEX idx_deferred_schedule_period ON deferred_revenue_schedule(accounting_period);
CREATE INDEX idx_deferred_schedule_status ON deferred_revenue_schedule(recognition_status, period_start_date);
CREATE INDEX idx_deferred_schedule_tenant ON deferred_revenue_schedule(tenant_id);
```

### Revenue Recognition Event Log
```sql
CREATE TABLE revenue_recognition_events (
    event_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    
    -- Source
    contract_id UUID NOT NULL REFERENCES revenue_contracts(contract_id),
    obligation_id UUID NOT NULL REFERENCES performance_obligations(obligation_id),
    allocation_id UUID NOT NULL REFERENCES revenue_allocations(allocation_id),
    
    -- Event Details
    event_type VARCHAR(50) NOT NULL, -- initial_recognition, periodic_recognition, full_recognition, reversal
    event_date DATE NOT NULL,
    
    -- Recognition Amount
    recognition_amount_cents BIGINT NOT NULL,
    
    -- Accounting Period
    accounting_period VARCHAR(7) NOT NULL,
    fiscal_year INTEGER NOT NULL,
    fiscal_quarter VARCHAR(2),
    
    -- Journal Entry Reference
    journal_entry_id UUID, -- Reference to accounting system journal entry
    
    -- Trigger
    trigger_type VARCHAR(50) NOT NULL, -- control_transfer, time_elapsed, milestone_completed, manual_override
    trigger_reference_id UUID,
    trigger_evidence JSONB,
    
    -- Approval (for manual overrides)
    requires_approval BOOLEAN DEFAULT FALSE,
    approved_by UUID REFERENCES users(user_id),
    approved_at TIMESTAMP,
    approval_reason TEXT,
    
    -- Reversal Tracking
    is_reversal BOOLEAN DEFAULT FALSE,
    original_event_id UUID REFERENCES revenue_recognition_events(event_id),
    reversal_reason TEXT,
    
    -- Metadata
    metadata JSONB,
    
    -- Audit
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    created_by VARCHAR(50) DEFAULT 'system',
    
    -- Multi-Tenant
    tenant_id UUID NOT NULL REFERENCES tenants(tenant_id),
    
    -- Constraints
    CONSTRAINT valid_event_type CHECK (event_type IN (
        'initial_recognition', 'periodic_recognition', 'full_recognition', 
        'reversal', 'adjustment'
    )),
    CONSTRAINT valid_trigger_type CHECK (trigger_type IN (
        'control_transfer', 'time_elapsed', 'milestone_completed', 
        'progress_update', 'manual_override', 'contract_modification'
    ))
);

-- Indexes
CREATE INDEX idx_revenue_events_contract ON revenue_recognition_events(contract_id, event_date DESC);
CREATE INDEX idx_revenue_events_obligation ON revenue_recognition_events(obligation_id);
CREATE INDEX idx_revenue_events_allocation ON revenue_recognition_events(allocation_id);
CREATE INDEX idx_revenue_events_period ON revenue_recognition_events(accounting_period);
CREATE INDEX idx_revenue_events_tenant ON revenue_recognition_events(tenant_id);
```

---

## 🔒 SECTION D — ASC 606 FIVE-STEP MODEL IMPLEMENTATION

### Step 1: Contract Identification
```python
def identify_contract(
    customer_id: UUID,
    contract_type: str,
    contract_details: Dict[str, Any],
    payment_transaction_id: Optional[UUID] = None
) -> RevenueContract:
    """
    ASC 606 Step 1: Identify the contract with customer
    
    Criteria (ALL must be met):
    1. Parties have approved and committed
    2. Rights & obligations identified
    3. Payment terms clear
    4. Contract has commercial substance
    5. Collection is probable
    """
    
    # 1. Validate parties committed
    if not validate_contract_approval(customer_id, contract_details):
        raise ValueError("Contract approval not obtained from all parties")
    
    # 2. Assess collectability
    collectability = assess_collectability(
        customer_id=customer_id,
        contract_value_cents=contract_details["total_value_cents"]
    )
    
    if collectability == "uncertain":
        raise ValueError(
            "Contract collectability uncertain. "
            "Cannot recognize revenue until collection becomes probable."
        )
    
    # 3. Validate commercial substance
    if not has_commercial_substance(contract_type, contract_details):
        raise ValueError("Contract lacks commercial substance")
    
    # 4. Determine platform role (Principal vs Agent)
    platform_role = determine_platform_role(contract_type, contract_details)
    
    # 5. Create contract
    contract = RevenueContract.create(
        contract_number=generate_contract_number(),
        customer_id=customer_id,
        customer_name=get_user_name(customer_id),
        customer_type=get_user_type(customer_id),
        contract_type=contract_type,
        contract_date=date.today(),
        contract_start_date=contract_details.get("start_date", date.today()),
        contract_end_date=contract_details.get("end_date"),
        total_contract_value_cents=contract_details["total_value_cents"],
        currency=contract_details.get("currency", "INR"),
        payment_terms=contract_details["payment_terms"],
        collectability_assessment=collectability,
        platform_role=platform_role,
        contract_status="active",
        metadata={
            "payment_transaction_id": str(payment_transaction_id) if payment_transaction_id else None,
            "contract_details": contract_details
        }
    )
    
    # 6. Emit event
    emit_event("revenue.contract_identified", {
        "contract_id": contract.contract_id,
        "customer_id": customer_id,
        "contract_value_cents": contract.total_contract_value_cents
    })
    
    return contract


def assess_collectability(customer_id: UUID, contract_value_cents: int) -> str:
    """
    Assess probability of collecting payment
    
    Returns: 'high', 'medium', 'low', 'uncertain'
    """
    
    # Check customer payment history
    payment_history = get_customer_payment_history(customer_id)
    
    if payment_history["failed_payments"] == 0 and payment_history["total_payments"] > 5:
        return "high"
    
    elif payment_history["failed_payments"] / max(payment_history["total_payments"], 1) < 0.1:
        return "medium"
    
    elif payment_history["failed_payments"] / max(payment_history["total_payments"], 1) < 0.3:
        return "low"
    
    else:
        return "uncertain"


def determine_platform_role(contract_type: str, contract_details: Dict[str, Any]) -> str:
    """
    Determine if platform is Principal or Agent (ASC 606 Revenue Recognition)
    
    Principal indicators:
    - Platform controls service before transfer to customer
    - Platform sets pricing
    - Platform bears inventory risk
    
    Agent indicators:
    - Third party controls service
    - Platform earns commission/fee
    - No inventory risk
    """
    
    # Job postings: Platform is principal (controls job board)
    if contract_type == "job_posting":
        return "principal"
    
    # Platform subscriptions: Platform is principal
    if contract_type == "subscription":
        return "principal"
    
    # Course purchases: Platform is AGENT (trainer controls course)
    if contract_type == "course_purchase":
        return "agent"
    
    # Event tickets: Platform is AGENT (organizer controls event)
    if contract_type == "event_ticket":
        return "agent"
    
    # Default: Agent
    return "agent"
```

### Step 2: Identify Performance Obligations
```python
def identify_performance_obligations(
    contract: RevenueContract
) -> List[PerformanceObligation]:
    """
    ASC 606 Step 2: Identify performance obligations in the contract
    
    Performance obligation = Promise to transfer distinct good/service
    
    Distinct if BOTH:
    1. Customer can benefit from it on its own or with other available resources
    2. Promise is separately identifiable from other promises
    """
    
    obligations = []
    
    # Parse contract to identify promises
    if contract.contract_type == "course_purchase":
        # Obligation 1: Course access and delivery
        obligations.append({
            "description": "Course access and instructional content delivery",
            "type": "course_delivery",
            "is_distinct": True,
            "recognition_timing": "point_in_time",  # Upon course completion
            "control_transfer_event": "course_completed"
        })
        
        # Check if certificate is bundled
        if contract.metadata.get("includes_certificate"):
            # Obligation 2: Certificate issuance
            obligations.append({
                "description": "Certification upon course completion",
                "type": "certification_issuance",
                "is_distinct": True,  # Can be valued separately
                "recognition_timing": "point_in_time",
                "control_transfer_event": "certificate_issued"
            })
    
    elif contract.contract_type == "subscription":
        # Obligation 1: Subscription access (recognized over time)
        contract_duration_days = (contract.contract_end_date - contract.contract_start_date).days
        
        obligations.append({
            "description": f"Platform subscription access for {contract_duration_days} days",
            "type": "subscription_access",
            "is_distinct": True,
            "recognition_timing": "over_time",
            "over_time_method": "time_elapsed",
            "over_time_duration_days": contract_duration_days,
            "over_time_start_date": contract.contract_start_date,
            "over_time_end_date": contract.contract_end_date
        })
    
    elif contract.contract_type == "event_ticket":
        # Obligation 1: Event attendance right
        obligations.append({
            "description": "Event attendance and participation",
            "type": "event_attendance",
            "is_distinct": True,
            "recognition_timing": "point_in_time",
            "control_transfer_event": "event_completed"
        })
    
    elif contract.contract_type == "dojo_session":
        # Obligation 1: Dojo session delivery
        obligations.append({
            "description": "Dojo training session with mentor",
            "type": "dojo_session_delivery",
            "is_distinct": True,
            "recognition_timing": "point_in_time",
            "control_transfer_event": "session_completed"
        })
    
    else:
        # Default: Single obligation
        obligations.append({
            "description": f"Delivery of {contract.contract_type}",
            "type": "service_delivery",
            "is_distinct": True,
            "recognition_timing": "point_in_time",
            "control_transfer_event": "service_completed"
        })
    
    # Create performance obligation records
    created_obligations = []
    
    for i, obligation_data in enumerate(obligations, start=1):
        # Determine SSP (Step 4 preparation)
        ssp_cents, ssp_method = determine_ssp(
            contract=contract,
            obligation_type=obligation_data["type"]
        )
        
        obligation = PerformanceObligation.create(
            contract_id=contract.contract_id,
            obligation_sequence=i,
            obligation_description=obligation_data["description"],
            obligation_type=obligation_data["type"],
            is_distinct=obligation_data["is_distinct"],
            ssp_cents=ssp_cents,
            ssp_determination_method=ssp_method,
            ssp_last_reviewed_at=date.today(),
            allocated_price_cents=0,  # Will be set in Step 4
            allocation_percentage=0,
            recognition_timing=obligation_data["recognition_timing"],
            control_transfer_event=obligation_data.get("control_transfer_event"),
            over_time_method=obligation_data.get("over_time_method"),
            over_time_duration_days=obligation_data.get("over_time_duration_days"),
            over_time_start_date=obligation_data.get("over_time_start_date"),
            over_time_end_date=obligation_data.get("over_time_end_date"),
            fulfillment_status="pending"
        )
        
        created_obligations.append(obligation)
    
    # Update contract
    update_revenue_contract(
        contract_id=contract.contract_id,
        total_obligations=len(created_obligations)
    )
    
    return created_obligations


def determine_ssp(
    contract: RevenueContract,
    obligation_type: str
) -> Tuple[int, str]:
    """
    Determine Stand-Alone Selling Price (SSP) for performance obligation
    
    Methods (in order of preference):
    1. Observable price (actual price charged when sold separately)
    2. Adjusted market assessment (competitor prices)
    3. Expected cost plus margin
    4. Residual approach (only if SSP highly variable or uncertain)
    """
    
    # Check for observable price
    observable_ssp = get_observable_ssp(obligation_type)
    if observable_ssp:
        return observable_ssp, "observable"
    
    # Check market data
    market_ssp = get_market_adjusted_ssp(obligation_type)
    if market_ssp:
        return market_ssp, "adjusted_market"
    
    # Cost plus margin approach
    expected_cost = estimate_obligation_cost(obligation_type)
    margin_rate = get_standard_margin_rate(obligation_type)
    cost_plus_ssp = int(expected_cost * (1 + margin_rate))
    
    return cost_plus_ssp, "expected_cost_plus_margin"
```

### Step 3 & 4: Determine Transaction Price & Allocate
```python
def allocate_transaction_price(
    contract: RevenueContract,
    obligations: List[PerformanceObligation]
) -> List[PerformanceObligation]:
    """
    ASC 606 Step 3: Determine transaction price
    ASC 606 Step 4: Allocate transaction price to performance obligations
    
    Allocation based on relative stand-alone selling prices (SSP)
    """
    
    # Step 3: Determine transaction price
    transaction_price_cents = contract.total_contract_value_cents
    
    # Adjust for variable consideration
    if contract.has_variable_consideration:
        # Apply constraint to avoid significant revenue reversal
        if contract.constraint_applied:
            constrained_amount = estimate_constrained_variable_consideration(contract)
            transaction_price_cents += constrained_amount
    
    # Step 4: Allocate based on SSP
    total_ssp = sum(ob.ssp_cents for ob in obligations)
    
    if total_ssp == 0:
        raise ValueError("Total SSP cannot be zero")
    
    allocated_sum = 0
    
    for obligation in obligations:
        # Calculate allocation percentage
        allocation_percentage = (obligation.ssp_cents / total_ssp) * 100
        
        # Calculate allocated amount
        allocated_cents = int(transaction_price_cents * (obligation.ssp_cents / total_ssp))
        
        # Track for rounding adjustment
        allocated_sum += allocated_cents
        
        # Update obligation
        update_performance_obligation(
            obligation_id=obligation.obligation_id,
            allocated_price_cents=allocated_cents,
            allocation_percentage=allocation_percentage,
            deferred_revenue_cents=allocated_cents  # Initially all deferred
        )
    
    # Adjust for rounding (give difference to largest obligation)
    rounding_difference = transaction_price_cents - allocated_sum
    if rounding_difference != 0:
        largest_obligation = max(obligations, key=lambda o: o.ssp_cents)
        update_performance_obligation(
            obligation_id=largest_obligation.obligation_id,
            allocated_price_cents=largest_obligation.allocated_price_cents + rounding_difference
        )
    
    # Reload obligations with updated values
    return [get_performance_obligation(ob.obligation_id) for ob in obligations]
```

### Step 5: Recognize Revenue
```python
def recognize_revenue_on_fulfillment(
    obligation: PerformanceObligation,
    fulfillment_event: Dict[str, Any]
) -> RevenueRecognitionEvent:
    """
    ASC 606 Step 5: Recognize revenue when (or as) performance obligation satisfied
    
    Timing:
    - Point-in-time: When control transfers to customer
    - Over-time: As performance obligation satisfied over time
    """
    
    contract = get_revenue_contract(obligation.contract_id)
    
    # Validate fulfillment
    if not validate_fulfillment_evidence(obligation, fulfillment_event):
        raise ValueError("Fulfillment evidence insufficient")
    
    # Calculate recognition amount
    if obligation.recognition_timing == "point_in_time":
        # Recognize full allocated amount
        recognition_amount_cents = obligation.allocated_price_cents
        
        # Update obligation status
        update_performance_obligation(
            obligation_id=obligation.obligation_id,
            fulfillment_status="fulfilled",
            fulfillment_date=date.today(),
            fulfillment_evidence=fulfillment_event,
            revenue_recognized_cents=recognition_amount_cents,
            deferred_revenue_cents=0,
            progress_percentage=100.00
        )
    
    elif obligation.recognition_timing == "over_time":
        # Calculate progress
        progress = calculate_progress(obligation, fulfillment_event)
        
        # Calculate incremental recognition
        total_to_recognize = int(obligation.allocated_price_cents * (progress / 100))
        recognition_amount_cents = total_to_recognize - obligation.revenue_recognized_cents
        
        # Update obligation
        update_performance_obligation(
            obligation_id=obligation.obligation_id,
            progress_percentage=progress,
            revenue_recognized_cents=total_to_recognize,
            deferred_revenue_cents=obligation.allocated_price_cents - total_to_recognize,
            fulfillment_status="in_progress" if progress < 100 else "fulfilled",
            fulfillment_date=date.today() if progress == 100 else None
        )
    
    # Create revenue recognition event
    event = RevenueRecognitionEvent.create(
        contract_id=contract.contract_id,
        obligation_id=obligation.obligation_id,
        allocation_id=None,  # Will be set when creating allocations
        event_type="full_recognition" if obligation.recognition_timing == "point_in_time" else "periodic_recognition",
        event_date=date.today(),
        recognition_amount_cents=recognition_amount_cents,
        accounting_period=get_current_accounting_period(),
        fiscal_year=get_current_fiscal_year(),
        fiscal_quarter=get_current_fiscal_quarter(),
        trigger_type=fulfillment_event["trigger_type"],
        trigger_reference_id=fulfillment_event.get("reference_id"),
        trigger_evidence=fulfillment_event
    )
    
    # Update contract summary
    update_contract_revenue_summary(contract.contract_id)
    
    # Emit event
    emit_event("revenue.recognized", {
        "contract_id": contract.contract_id,
        "obligation_id": obligation.obligation_id,
        "recognition_amount_cents": recognition_amount_cents,
        "recognition_event_id": event.event_id
    })
    
    return event


def calculate_progress(
    obligation: PerformanceObligation,
    progress_update: Dict[str, Any]
) -> float:
    """
    Calculate progress percentage for over-time recognition
    """
    
    if obligation.over_time_method == "time_elapsed":
        # Simple time-based progress
        total_days = (obligation.over_time_end_date - obligation.over_time_start_date).days
        elapsed_days = (date.today() - obligation.over_time_start_date).days
        
        progress = min((elapsed_days / total_days) * 100, 100)
        return round(progress, 2)
    
    elif obligation.over_time_method == "output_method":
        # Based on units delivered
        units_delivered = progress_update.get("units_delivered", 0)
        total_units = progress_update.get("total_units", 1)
        
        progress = (units_delivered / total_units) * 100
        return round(progress, 2)
    
    elif obligation.over_time_method == "input_method":
        # Based on costs incurred
        costs_incurred = progress_update.get("costs_incurred_cents", 0)
        estimated_total_costs = progress_update.get("estimated_total_costs_cents", 1)
        
        progress = (costs_incurred / estimated_total_costs) * 100
        return round(progress, 2)
    
    else:
        raise ValueError(f"Unknown over_time_method: {obligation.over_time_method}")
```

---

## 🔒 SECTION E — REVENUE SPLIT ORCHESTRATION PIPELINE

### Revenue Split Execution Flow (13 Stages)

```
STAGE 1: CONTRACT & OBLIGATION VALIDATION
├─ Input: Revenue recognition event
├─ Load contract and performance obligation
├─ Validate: obligation fulfilled, revenue recognized
└─ Output: Validated contract + obligation

STAGE 2: SPLIT CONFIGURATION SELECTION
├─ Query: Find applicable split config
├─ Match: contract_type, conditions
├─ Priority: Select highest priority if multiple match
└─ Output: Split configuration

STAGE 3: SPLIT PARTIES IDENTIFICATION
├─ Parse split_parties from config
├─ For each party:
│  ├─ Resolve recipient_id (trainer_id, organizer_id, etc.)
│  └─ Validate recipient exists and active
└─ Output: List of recipients

STAGE 4: GROSS ALLOCATION CALCULATION
├─ For each recipient:
│  ├─ Calculate percentage-based allocation
│  ├─ OR calculate tiered allocation
│  ├─ OR calculate performance-based allocation
│  └─ OR calculate milestone-based allocation
└─ Output: Gross allocation amounts

STAGE 5: PLATFORM ROLE ADJUSTMENT (Principal vs Agent)
├─ If platform_role = "agent":
│  ├─ Recognize only platform commission (net)
│  └─ Do NOT recognize full transaction price
├─ If platform_role = "principal":
│  └─ Recognize full transaction price (gross)
└─ Output: Adjusted recognition amounts

STAGE 6: TAX WITHHOLDING CALCULATION
├─ For non-platform recipients:
│  ├─ Calculate TDS based on recipient tax profile
│  └─ Deduct from allocation
└─ Output: Tax withholding amounts

STAGE 7: NET ALLOCATION CALCULATION
├─ net_allocation = gross_allocation - platform_fee - tax
└─ Output: Net allocation amounts

STAGE 8: REVENUE ALLOCATION RECORD CREATION
├─ For each recipient:
│  ├─ Create revenue_allocations record
│  ├─ Set recognition_status = "recognized" (if point-in-time)
│  └─ Set recognition_status = "deferred" (if over-time)
└─ Output: Allocation records

STAGE 9: DEFERRED REVENUE SCHEDULING (Over-Time Only)
├─ If recognition_timing = "over_time":
│  ├─ Calculate monthly/quarterly schedule
│  ├─ Create deferred_revenue_schedule records
│  └─ Set recognition dates
└─ Output: Deferred schedule

STAGE 10: COMMISSION CREDIT (Immediate Recognition)
├─ If recognition_status = "recognized":
│  ├─ Credit recipient wallet
│  └─ Create wallet_transaction
└─ Output: Wallet credited

STAGE 11: ACCOUNTING JOURNAL ENTRY
├─ Create double-entry journal:
│  ├─ Debit: Accounts Receivable / Cash
│  ├─ Credit: Revenue (platform share)
│  ├─ Credit: Revenue Payable (partner share)
└─ Output: Journal entry reference

STAGE 12: NOTIFICATION DISPATCH
├─ For each recipient:
│  ├─ Send allocation notification
│  └─ Show revenue share breakdown
└─ Output: Notifications sent

STAGE 13: ANALYTICS & AUDIT
├─ Emit revenue.split_completed event
├─ Update revenue analytics
├─ Create immutable audit log
└─ Output: Audit record
```

---

## 🔒 SECTION F — CO-TEACHING REVENUE SPLIT

### Co-Teaching Agreement Processing
```python
def process_coteaching_revenue_split(
    contract: RevenueContract,
    obligation: PerformanceObligation,
    coteaching_agreement: Dict[str, Any]
) -> List[RevenueAllocation]:
    """
    Handle revenue split for co-teaching scenarios
    
    Example splits:
    - 2 trainers: 40% each, 20% platform
    - 3 trainers: 30% each, 10% platform
    - Custom: As per agreement
    """
    
    # 1. Validate co-teaching agreement
    trainers = coteaching_agreement["trainers"]
    split_percentages = coteaching_agreement["split_percentages"]
    
    if len(trainers) != len(split_percentages):
        raise ValueError("Trainer count must match split percentage count")
    
    if sum(split_percentages.values()) != 100:
        raise ValueError("Split percentages must sum to 100%")
    
    # 2. Calculate allocations
    allocations = []
    recognized_amount = obligation.revenue_recognized_cents
    
    for trainer_id, percentage in split_percentages.items():
        if trainer_id == "platform":
            recipient_id = None
            recipient_name = "Antigravity Platform"
        else:
            recipient_id = UUID(trainer_id)
            recipient_name = get_user_name(recipient_id)
        
        gross_allocation_cents = int(recognized_amount * (percentage / 100))
        
        # Tax withholding for trainers
        if recipient_id:
            tax_withheld_cents = int(gross_allocation_cents * 0.10)  # 10% TDS
        else:
            tax_withheld_cents = 0
        
        net_allocation_cents = gross_allocation_cents - tax_withheld_cents
        
        # Create allocation
        allocation = RevenueAllocation.create(
            contract_id=contract.contract_id,
            obligation_id=obligation.obligation_id,
            split_config_code="COTEACH_CUSTOM",
            recipient_role="co_trainer" if recipient_id else "platform",
            recipient_id=recipient_id,
            recipient_name=recipient_name,
            gross_allocation_cents=gross_allocation_cents,
            tax_withheld_cents=tax_withheld_cents,
            net_allocation_cents=net_allocation_cents,
            allocation_percentage=percentage,
            recognition_status="recognized",
            recognized_amount_cents=gross_allocation_cents,
            recognition_completed_date=date.today(),
            metadata={"coteaching_agreement_id": coteaching_agreement.get("agreement_id")}
        )
        
        allocations.append(allocation)
        
        # Credit wallet (if not platform)
        if recipient_id:
            credit_wallet_from_revenue_split(
                recipient_id=recipient_id,
                allocation=allocation
            )
    
    return allocations
```

---

## 🔒 SECTION G — DEFERRED REVENUE AUTOMATION

### Monthly Revenue Recognition Cron Job
```python
def process_monthly_deferred_revenue():
    """
    Process scheduled deferred revenue recognition
    Runs on 1st of each month
    """
    
    logger.info("Starting monthly deferred revenue recognition")
    
    current_period = get_current_accounting_period()  # "YYYY-MM"
    
    # 1. Get all pending deferred schedule entries for current period
    pending_schedules = get_pending_deferred_schedules(current_period)
    
    logger.info(f"Found {len(pending_schedules)} pending schedules for {current_period}")
    
    results = {
        "total": len(pending_schedules),
        "recognized": 0,
        "failed": 0,
        "errors": []
    }
    
    # 2. Process each schedule
    for schedule in pending_schedules:
        try:
            # Get allocation
            allocation = get_revenue_allocation(schedule.allocation_id)
            
            # Recognize revenue
            recognize_deferred_revenue(
                schedule=schedule,
                allocation=allocation
            )
            
            results["recognized"] += 1
            
        except Exception as e:
            results["failed"] += 1
            results["errors"].append({
                "schedule_id": str(schedule.schedule_id),
                "error": str(e)
            })
            
            logger.error(f"Failed to recognize schedule {schedule.schedule_id}: {str(e)}")
    
    # 3. Send summary report
    send_deferred_revenue_report(current_period, results)
    
    logger.info(
        f"Completed monthly deferred revenue recognition. "
        f"Recognized: {results['recognized']}, Failed: {results['failed']}"
    )
    
    return results


def recognize_deferred_revenue(
    schedule: DeferredRevenueSchedule,
    allocation: RevenueAllocation
):
    """
    Recognize scheduled deferred revenue
    """
    
    # 1. Update schedule status
    update_deferred_schedule(
        schedule_id=schedule.schedule_id,
        recognition_status="recognized",
        recognized_date=date.today(),
        recognized_amount_cents=schedule.scheduled_recognition_cents
    )
    
    # 2. Update allocation
    new_recognized_total = allocation.recognized_amount_cents + schedule.scheduled_recognition_cents
    new_deferred_total = allocation.deferred_amount_cents - schedule.scheduled_recognition_cents
    
    update_revenue_allocation(
        allocation_id=allocation.allocation_id,
        recognized_amount_cents=new_recognized_total,
        deferred_amount_cents=new_deferred_total,
        recognition_status="recognized" if new_deferred_total == 0 else "partially_recognized"
    )
    
    # 3. Create revenue recognition event
    create_revenue_recognition_event(
        contract_id=allocation.contract_id,
        obligation_id=allocation.obligation_id,
        allocation_id=allocation.allocation_id,
        event_type="periodic_recognition",
        recognition_amount_cents=schedule.scheduled_recognition_cents,
        trigger_type="time_elapsed",
        trigger_evidence={"schedule_id": str(schedule.schedule_id)}
    )
    
    # 4. Credit wallet (if not already credited)
    if allocation.payout_status == "pending" and allocation.recipient_id:
        credit_wallet_from_revenue_split(
            recipient_id=allocation.recipient_id,
            allocation=allocation,
            amount_cents=schedule.scheduled_recognition_cents
        )
    
    # 5. Create journal entry
    create_journal_entry(
        entry_type="deferred_revenue_recognition",
        debit_account="deferred_revenue",
        credit_account="revenue",
        amount_cents=schedule.scheduled_recognition_cents,
        reference_id=schedule.schedule_id,
        accounting_period=schedule.accounting_period
    )
```

---

## 🔒 SECTION H — CONTRACT MODIFICATION HANDLING

### Contract Modification Types (ASC 606)
```python
def handle_contract_modification(
    original_contract_id: UUID,
    modification_details: Dict[str, Any]
) -> RevenueContract:
    """
    Handle contract modifications per ASC 606
    
    Three types:
    1. Separate contract: New distinct goods/services at SSP
    2. Prospective: Adjust remaining obligations going forward
    3. Retrospective: Catch-up adjustment as if modified from start
    """
    
    original_contract = get_revenue_contract(original_contract_id)
    
    # 1. Classify modification
    modification_type = classify_modification(original_contract, modification_details)
    
    if modification_type == "separate_contract":
        # Create new contract
        new_contract = create_revenue_contract(
            customer_id=original_contract.customer_id,
            contract_type=modification_details["new_contract_type"],
            contract_details=modification_details
        )
        
        # No impact on original contract
        return new_contract
    
    elif modification_type == "prospective":
        # Update remaining obligations
        return apply_prospective_modification(
            original_contract=original_contract,
            modification_details=modification_details
        )
    
    elif modification_type == "retrospective":
        # Catch-up adjustment
        return apply_retrospective_modification(
            original_contract=original_contract,
            modification_details=modification_details
        )
    
    else:
        raise ValueError(f"Unknown modification type: {modification_type}")


def apply_prospective_modification(
    original_contract: RevenueContract,
    modification_details: Dict[str, Any]
) -> RevenueContract:
    """
    Apply prospective modification (adjust going forward)
    
    Example: Subscription upgraded mid-period
    """
    
    # 1. Create modified contract record
    modified_contract = RevenueContract.create(
        contract_number=generate_contract_number(),
        customer_id=original_contract.customer_id,
        customer_name=original_contract.customer_name,
        customer_type=original_contract.customer_type,
        contract_type=original_contract.contract_type,
        contract_date=date.today(),
        contract_start_date=date.today(),
        contract_end_date=modification_details.get("new_end_date", original_contract.contract_end_date),
        total_contract_value_cents=modification_details["new_total_value_cents"],
        is_modified=True,
        original_contract_id=original_contract.contract_id,
        modification_type="prospective",
        metadata={"modification_reason": modification_details.get("reason")}
    )
    
    # 2. Terminate original contract
    update_revenue_contract(
        contract_id=original_contract.contract_id,
        contract_status="terminated",
        metadata={
            **original_contract.metadata,
            "termination_reason": "contract_modified",
            "modified_contract_id": str(modified_contract.contract_id)
        }
    )
    
    # 3. Identify new performance obligations
    new_obligations = identify_performance_obligations(modified_contract)
    
    # 4. Allocate transaction price
    allocate_transaction_price(modified_contract, new_obligations)
    
    return modified_contract
```

---

## 🔒 SECTION I — PRINCIPAL VS AGENT CLASSIFICATION

### Revenue Recognition Based on Platform Role
```python
def recognize_revenue_based_on_platform_role(
    contract: RevenueContract,
    obligation: PerformanceObligation,
    payment_transaction: PaymentTransaction
) -> List[RevenueAllocation]:
    """
    Recognize revenue differently based on Principal vs Agent role
    
    PRINCIPAL (Gross Recognition):
    - Record full transaction amount as revenue
    - Record service provider payment as COGS
    - Example: Job postings (platform controls job board)
    
    AGENT (Net Recognition):
    - Record only commission as revenue
    - Do NOT record full transaction amount
    - Example: Course sales (trainer controls course)
    """
    
    if contract.platform_role == "principal":
        # GROSS RECOGNITION
        allocations = []
        
        # 1. Platform recognizes full revenue
        platform_allocation = RevenueAllocation.create(
            contract_id=contract.contract_id,
            obligation_id=obligation.obligation_id,
            payment_transaction_id=payment_transaction.transaction_id,
            split_config_code="PRINCIPAL_GROSS",
            recipient_role="platform",
            recipient_id=None,
            recipient_name="Antigravity Platform",
            gross_allocation_cents=payment_transaction.amount_cents,
            net_allocation_cents=payment_transaction.amount_cents,
            allocation_percentage=100.00,
            recognition_status="recognized",
            recognized_amount_cents=payment_transaction.amount_cents
        )
        allocations.append(platform_allocation)
        
        # 2. If there's a service provider, their payment is COGS (not revenue)
        if payment_transaction.metadata.get("service_provider_id"):
            create_cogs_record(
                payment_transaction_id=payment_transaction.transaction_id,
                service_provider_id=payment_transaction.metadata["service_provider_id"],
                cogs_amount_cents=payment_transaction.metadata.get("service_provider_payment_cents", 0)
            )
        
        return allocations
    
    elif contract.platform_role == "agent":
        # NET RECOGNITION (Commission Only)
        allocations = []
        
        # Get split configuration
        split_config = get_split_config_for_contract(contract)
        
        # 1. Platform recognizes only commission
        platform_percentage = next(
            (party["percentage"] for party in split_config.split_parties if party["role"] == "platform"),
            20.0  # Default 20%
        )
        
        platform_commission_cents = int(payment_transaction.amount_cents * (platform_percentage / 100))
        
        platform_allocation = RevenueAllocation.create(
            contract_id=contract.contract_id,
            obligation_id=obligation.obligation_id,
            payment_transaction_id=payment_transaction.transaction_id,
            split_config_code=split_config.config_code,
            recipient_role="platform",
            recipient_id=None,
            recipient_name="Antigravity Platform",
            gross_allocation_cents=platform_commission_cents,
            net_allocation_cents=platform_commission_cents,
            allocation_percentage=platform_percentage,
            recognition_status="recognized",
            recognized_amount_cents=platform_commission_cents
        )
        allocations.append(platform_allocation)
        
        # 2. Service provider receives remainder (NOT platform revenue)
        service_provider_percentage = 100 - platform_percentage
        service_provider_payment_cents = payment_transaction.amount_cents - platform_commission_cents
        
        service_provider_allocation = RevenueAllocation.create(
            contract_id=contract.contract_id,
            obligation_id=obligation.obligation_id,
            payment_transaction_id=payment_transaction.transaction_id,
            split_config_code=split_config.config_code,
            recipient_role="service_provider",
            recipient_id=payment_transaction.metadata.get("service_provider_id"),
            recipient_name=payment_transaction.metadata.get("service_provider_name"),
            gross_allocation_cents=service_provider_payment_cents,
            tax_withheld_cents=int(service_provider_payment_cents * 0.10),
            net_allocation_cents=int(service_provider_payment_cents * 0.90),
            allocation_percentage=service_provider_percentage,
            recognition_status="recognized",
            recognized_amount_cents=service_provider_payment_cents,
            payout_status="pending"
        )
        allocations.append(service_provider_allocation)
        
        # 3. Credit service provider wallet
        credit_wallet_from_revenue_split(
            recipient_id=service_provider_allocation.recipient_id,
            allocation=service_provider_allocation
        )
        
        return allocations
    
    else:
        raise ValueError(f"Unknown platform_role: {contract.platform_role}")
```

---

## 🔒 SECTION J — REFUND REVENUE REVERSAL

### Refund Impact on Revenue Recognition
```python
def reverse_revenue_on_refund(
    refund_transaction: PaymentTransaction,
    original_transaction: PaymentTransaction
):
    """
    Reverse revenue recognition upon refund
    
    Steps:
    1. Find original revenue allocations
    2. Create reversal allocations
    3. Update deferred revenue if applicable
    4. Create reversal journal entries
    """
    
    # 1. Find original contract and allocations
    contract = get_contract_by_payment_transaction(original_transaction.transaction_id)
    
    if not contract:
        logger.warning(f"No contract found for transaction {original_transaction.transaction_id}")
        return
    
    original_allocations = get_allocations_by_transaction(original_transaction.transaction_id)
    
    # 2. Calculate refund proportion
    refund_proportion = refund_transaction.refund_amount_cents / original_transaction.amount_cents
    
    # 3. Create reversal for each allocation
    for original_allocation in original_allocations:
        reversal_amount_cents = int(original_allocation.gross_allocation_cents * refund_proportion)
        
        # Create reversal allocation
        reversal_allocation = RevenueAllocation.create(
            contract_id=contract.contract_id,
            obligation_id=original_allocation.obligation_id,
            payment_transaction_id=refund_transaction.transaction_id,
            split_config_code=original_allocation.split_config_code,
            recipient_role=original_allocation.recipient_role,
            recipient_id=original_allocation.recipient_id,
            recipient_name=original_allocation.recipient_name,
            gross_allocation_cents=-reversal_amount_cents,  # Negative for reversal
            net_allocation_cents=-int(original_allocation.net_allocation_cents * refund_proportion),
            allocation_percentage=original_allocation.allocation_percentage,
            recognition_status="reversed",
            metadata={
                "reversal_of_allocation_id": str(original_allocation.allocation_id),
                "refund_transaction_id": str(refund_transaction.transaction_id)
            }
        )
        
        # 4. Create revenue recognition reversal event
        create_revenue_recognition_event(
            contract_id=contract.contract_id,
            obligation_id=original_allocation.obligation_id,
            allocation_id=reversal_allocation.allocation_id,
            event_type="reversal",
            recognition_amount_cents=-reversal_amount_cents,
            trigger_type="refund",
            trigger_reference_id=refund_transaction.transaction_id,
            is_reversal=True,
            reversal_reason=f"Refund processed: {refund_transaction.refund_reason}"
        )
        
        # 5. Debit recipient wallet (if not platform)
        if original_allocation.recipient_id:
            debit_wallet_for_reversal(
                recipient_id=original_allocation.recipient_id,
                allocation=reversal_allocation
            )
```

---

## 🔒 SECTION K — EXECUTION DECLARATION

```
AGENT STATUS: LOCKED & SEALED
VERSION: 1.0
LAST UPDATED: 2026-03-04
MUTATION POLICY: Add-only via version bump
EXECUTION AUTHORITY: Automated (event-driven) + Rule-based
HUMAN OVERRIDE: Revenue recognition override only (CFO approval required)
AI INTERPRETATION: FORBIDDEN
ACCOUNTING STANDARD: ASC 606 / IFRS 15 Compliant
COMPLIANCE: SOX, GAAP

COMPLETION CRITERIA:
✓ ASC 606 5-step model implemented
✓ Contract identification automated
✓ Performance obligation decomposition
✓ SSP determination (4 methods)
✓ Transaction price allocation
✓ Point-in-time recognition
✓ Over-time recognition (3 methods)
✓ Multi-party split orchestration
✓ Co-teaching revenue allocation
✓ Principal vs Agent classification
✓ Deferred revenue scheduling
✓ Monthly recognition automation
✓ Contract modification handling
✓ Refund revenue reversal
✓ Immutable audit trail

FAILURE CONDITIONS:
→ Revenue recognized without contract: STOP EXECUTION
→ Performance obligation not identified: STOP EXECUTION
→ SSP not determined: STOP EXECUTION
→ Allocation does not sum to 100%: STOP EXECUTION
→ Recognition before control transfer: ACCOUNTING VIOLATION
→ Deferred schedule skipped: COMPLIANCE VIOLATION
→ Principal/Agent misclassified: REVENUE MISSTATEMENT
→ Refund reversal not processed: FINANCIAL ERROR
→ Audit log write failure: STOP EXECUTION

ENFORCEMENT:
This specification is IMMUTABLE for v1.0.
All deviations require version bump + CFO + External Auditor approval.
No field-level interpretation permitted.
Deterministic behavior required at all stages.
ASC 606 compliance NON-NEGOTIABLE.
Revenue recognition timing MUST be evidence-based.

FINAL INSTRUCTION:
Implement exactly as specified.
No creativity.
No shortcuts.
No assumptions.
Revenue recognition accuracy absolute priority.
ASC 606 compliance mandatory.
Audit trail completeness required.
```

---

🔒 **END OF SPECIFICATION** 🔒

**This specification is now SEALED and LOCKED.**  
**No modifications permitted without version bump + CFO + External Auditor approval.**  
**Execute as specified. ASC 606 compliance paramount.**

📊 **ANTIGRAVITY REVENUE SPLIT AUTOMATION AGENT — SPECIFICATION COMPLETE** 📊
