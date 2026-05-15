# 💳 PAYMENT GATEWAY INTEGRATION AGENT — ANTIGRAVITY v1.0

**Artifact Class:** Production System Blueprint  
**Mutation Policy:** Add-only via version bump  
**Execution Mode:** Deterministic  
**Stack Lock:** Enforced  
**Interface Freeze:** Required  
**Domain:** Financial & Commission Management  
**Subsystem:** Payment Gateway Infrastructure  
**Compliance:** PCI-DSS Level 1, ASC 606, IFRS 15, SOX  

---

## 🔒 SECTION A — AGENT IDENTITY & AUTHORITY

### Agent Classification
```
Agent Name: PAYMENT_GATEWAY_INTEGRATION_AGENT
Agent Type: Financial Transaction Orchestrator
Execution Mode: Event-Driven + Idempotent
Determinism Rule: Identical payment intent → Identical outcome
Failure Mode: STOP → ROLLBACK → ALERT → NO PARTIAL CHARGE
Human Override: Refund/Dispute Resolution Only
AI Override: FORBIDDEN
Security Standard: PCI-DSS Level 1 Certified
Audit Requirement: IMMUTABLE FINANCIAL LOGS
```

### Authority Boundaries
```
PERMITTED:
✓ Payment intent creation
✓ Payment gateway API orchestration
✓ Transaction status polling
✓ Webhook signature verification
✓ Idempotency key generation
✓ Refund processing
✓ Commission calculation
✓ Royalty distribution
✓ Revenue recognition
✓ Invoice generation
✓ Subscription lifecycle management
✓ Payment method tokenization
✓ Fraud signal detection

FORBIDDEN:
✗ Direct card data storage (PCI violation)
✗ Payment gateway credential exposure
✗ Double charging same payment intent
✗ Revenue recognition before payment settlement
✗ Commission payout before funds cleared
✗ Royalty calculation without revenue proof
✗ Refund without original transaction reference
✗ Subscription cancellation without grace period
✗ Manual transaction record modification
✗ Bypassing fraud checks
```

---

## 🔒 SECTION B — PAYMENT FLOW TAXONOMY (LOCKED)

### Payment Flow Categories (Non-Negotiable)
```
1. DIRECT STUDENT PAYMENTS
   - Course enrollment fees
   - Event registration fees
   - Dojo belt certification fees
   - Premium feature subscriptions
   - Intelligence assessment fees
   - Certificate issuance fees

2. RECRUITER/COMPANY PAYMENTS
   - Job posting fees
   - Candidate access subscriptions
   - GD session fees
   - Interview platform fees
   - Bulk recruitment packages

3. TRAINER REVENUE FLOWS
   - Course sales (revenue share with platform)
   - Subscription revenue (tiered splits)
   - One-on-one session fees
   - Workshop revenue
   - Co-teaching revenue splits

4. INNOVATION ROYALTY FLOWS
   - Kid innovator royalty payments (0.01-0.05% of revenue)
   - Parent trust account management
   - Business licensing fees
   - Quarterly royalty distributions
   - Ownership transfer at age 18

5. PLATFORM COMMISSIONS
   - Marketplace transaction fees (5-15%)
   - Referral commission payouts
   - Affiliate revenue shares
   - Partner integration fees

6. SUBSCRIPTION MODELS
   - Student Pro subscriptions (monthly/annual)
   - Recruiter Team plans
   - Trainer Creator plans
   - Institution Enterprise licenses

7. REFUNDS & CHARGEBACKS
   - Student refund requests (course, event)
   - Chargeback dispute handling
   - Partial refunds
   - Subscription cancellation refunds
```

---

## 🔒 SECTION C — DATA MODEL (IMMUTABLE SCHEMA)

### Core Payment Transaction Entity
```sql
CREATE TABLE payment_transactions (
    transaction_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    idempotency_key VARCHAR(255) UNIQUE NOT NULL, -- Client-generated, prevents duplicates
    
    -- Payment Intent
    payment_intent_id VARCHAR(255) UNIQUE NOT NULL, -- Gateway payment intent ID
    payment_gateway VARCHAR(50) NOT NULL, -- razorpay, stripe, paypal
    payment_method VARCHAR(50) NOT NULL, -- card, upi, netbanking, wallet
    
    -- Transaction Details
    amount_cents BIGINT NOT NULL, -- Amount in smallest currency unit (paise, cents)
    currency VARCHAR(3) NOT NULL DEFAULT 'INR', -- ISO 4217
    description TEXT NOT NULL,
    
    -- Payer Information
    payer_id UUID NOT NULL REFERENCES users(user_id),
    payer_email VARCHAR(255) NOT NULL,
    payer_phone VARCHAR(20),
    payer_name VARCHAR(255) NOT NULL,
    
    -- Payee Information (Revenue Attribution)
    payee_type VARCHAR(50) NOT NULL, -- platform, trainer, innovator, referrer
    payee_id UUID, -- trainer_id, innovator_id, etc.
    
    -- Transaction Context
    transaction_type VARCHAR(50) NOT NULL, -- course_purchase, subscription, certification, etc.
    reference_entity_type VARCHAR(50), -- course, event, job_posting, etc.
    reference_entity_id UUID, -- course_id, event_id, etc.
    
    -- Status Tracking
    status VARCHAR(50) NOT NULL DEFAULT 'pending', -- pending, processing, succeeded, failed, refunded
    failure_code VARCHAR(50),
    failure_message TEXT,
    
    -- Gateway Response
    gateway_transaction_id VARCHAR(255), -- Gateway's transaction/charge ID
    gateway_order_id VARCHAR(255), -- Gateway's order ID
    gateway_response JSONB, -- Full gateway response
    
    -- Payment Timeline
    initiated_at TIMESTAMP NOT NULL DEFAULT NOW(),
    processing_started_at TIMESTAMP,
    succeeded_at TIMESTAMP,
    failed_at TIMESTAMP,
    refunded_at TIMESTAMP,
    
    -- Refund Details
    refund_amount_cents BIGINT,
    refund_reason TEXT,
    refund_initiated_by UUID REFERENCES users(user_id),
    
    -- Revenue Recognition
    revenue_recognized BOOLEAN DEFAULT FALSE,
    revenue_recognized_at TIMESTAMP,
    
    -- Commission & Split
    platform_commission_cents BIGINT,
    trainer_share_cents BIGINT,
    innovator_royalty_cents BIGINT,
    referrer_commission_cents BIGINT,
    
    -- Security & Fraud
    fraud_score NUMERIC(5,2), -- 0.00 to 100.00
    fraud_check_passed BOOLEAN DEFAULT TRUE,
    risk_flags JSONB, -- Array of risk indicators
    
    -- Metadata
    metadata JSONB, -- Additional context data
    
    -- Audit
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
    
    -- Multi-Tenant
    tenant_id UUID NOT NULL REFERENCES tenants(tenant_id),
    
    -- Constraints
    CONSTRAINT valid_status CHECK (status IN (
        'pending', 'processing', 'succeeded', 'failed', 
        'refunded', 'partially_refunded', 'disputed'
    )),
    CONSTRAINT valid_payment_gateway CHECK (payment_gateway IN (
        'razorpay', 'stripe', 'paypal', 'phonepe', 'paytm', 'test_gateway'
    )),
    CONSTRAINT valid_currency CHECK (currency IN ('INR', 'USD', 'EUR', 'GBP')),
    CONSTRAINT positive_amount CHECK (amount_cents > 0),
    CONSTRAINT valid_refund CHECK (
        refund_amount_cents IS NULL OR 
        (refund_amount_cents > 0 AND refund_amount_cents <= amount_cents)
    )
);

-- Indexes
CREATE INDEX idx_payment_transactions_payer ON payment_transactions(payer_id, created_at DESC);
CREATE INDEX idx_payment_transactions_status ON payment_transactions(status, created_at DESC);
CREATE INDEX idx_payment_transactions_idempotency ON payment_transactions(idempotency_key);
CREATE INDEX idx_payment_transactions_gateway ON payment_transactions(payment_gateway, gateway_transaction_id);
CREATE INDEX idx_payment_transactions_type ON payment_transactions(transaction_type, status);
CREATE INDEX idx_payment_transactions_reference ON payment_transactions(reference_entity_type, reference_entity_id);
CREATE INDEX idx_payment_transactions_revenue ON payment_transactions(revenue_recognized, succeeded_at);
CREATE INDEX idx_payment_transactions_tenant ON payment_transactions(tenant_id);
```

### Subscription Management Entity
```sql
CREATE TABLE subscriptions (
    subscription_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    
    -- Subscriber Information
    subscriber_id UUID NOT NULL REFERENCES users(user_id),
    subscriber_type VARCHAR(50) NOT NULL, -- student, recruiter, trainer, institution
    
    -- Subscription Plan
    plan_id UUID NOT NULL REFERENCES subscription_plans(plan_id),
    plan_name VARCHAR(255) NOT NULL,
    plan_tier VARCHAR(50) NOT NULL, -- free, basic, pro, team, enterprise
    
    -- Pricing
    billing_amount_cents BIGINT NOT NULL,
    currency VARCHAR(3) NOT NULL DEFAULT 'INR',
    billing_cycle VARCHAR(20) NOT NULL, -- monthly, quarterly, annual
    
    -- Gateway Subscription
    gateway_subscription_id VARCHAR(255) UNIQUE, -- Gateway's subscription ID
    payment_gateway VARCHAR(50) NOT NULL,
    
    -- Status
    status VARCHAR(50) NOT NULL DEFAULT 'active', -- active, past_due, canceled, paused
    
    -- Lifecycle Dates
    trial_start_date DATE,
    trial_end_date DATE,
    subscription_start_date DATE NOT NULL,
    current_period_start DATE NOT NULL,
    current_period_end DATE NOT NULL,
    cancel_at_period_end BOOLEAN DEFAULT FALSE,
    canceled_at TIMESTAMP,
    cancellation_reason TEXT,
    
    -- Payment History
    last_payment_date DATE,
    next_billing_date DATE,
    failed_payment_count INTEGER DEFAULT 0,
    
    -- Entitlements (Feature Access)
    entitlements JSONB NOT NULL, -- List of enabled features
    usage_limits JSONB, -- API calls, storage, users, etc.
    
    -- Metadata
    metadata JSONB,
    
    -- Audit
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
    created_by UUID REFERENCES users(user_id),
    
    -- Multi-Tenant
    tenant_id UUID NOT NULL REFERENCES tenants(tenant_id),
    
    -- Constraints
    CONSTRAINT valid_subscription_status CHECK (status IN (
        'active', 'past_due', 'canceled', 'paused', 'unpaid', 'trialing'
    )),
    CONSTRAINT valid_billing_cycle CHECK (billing_cycle IN (
        'monthly', 'quarterly', 'annual'
    ))
);

CREATE INDEX idx_subscriptions_subscriber ON subscriptions(subscriber_id, status);
CREATE INDEX idx_subscriptions_status ON subscriptions(status, next_billing_date);
CREATE INDEX idx_subscriptions_gateway ON subscriptions(payment_gateway, gateway_subscription_id);
CREATE INDEX idx_subscriptions_tenant ON subscriptions(tenant_id);
```

### Commission & Revenue Split Entity
```sql
CREATE TABLE revenue_splits (
    split_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    transaction_id UUID NOT NULL REFERENCES payment_transactions(transaction_id),
    
    -- Split Configuration
    split_type VARCHAR(50) NOT NULL, -- platform_commission, trainer_share, royalty, referral
    
    -- Recipient
    recipient_type VARCHAR(50) NOT NULL, -- platform, trainer, innovator, referrer
    recipient_id UUID, -- NULL for platform
    recipient_name VARCHAR(255),
    
    -- Amount
    split_amount_cents BIGINT NOT NULL,
    split_percentage NUMERIC(5,2), -- For audit trail
    currency VARCHAR(3) NOT NULL DEFAULT 'INR',
    
    -- Payout Status
    payout_status VARCHAR(50) NOT NULL DEFAULT 'pending', -- pending, processing, paid, failed
    payout_scheduled_date DATE,
    payout_completed_date DATE,
    payout_reference_id VARCHAR(255), -- Bank transfer reference
    
    -- Tax Withholding
    tax_withheld_cents BIGINT DEFAULT 0,
    tax_withholding_rate NUMERIC(5,2),
    
    -- Metadata
    metadata JSONB,
    
    -- Audit
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
    
    -- Multi-Tenant
    tenant_id UUID NOT NULL REFERENCES tenants(tenant_id),
    
    -- Constraints
    CONSTRAINT valid_split_type CHECK (split_type IN (
        'platform_commission', 'trainer_share', 'innovator_royalty', 
        'referrer_commission', 'affiliate_share', 'co_trainer_share'
    )),
    CONSTRAINT valid_payout_status CHECK (payout_status IN (
        'pending', 'processing', 'paid', 'failed', 'on_hold'
    )),
    CONSTRAINT positive_split_amount CHECK (split_amount_cents > 0)
);

CREATE INDEX idx_revenue_splits_transaction ON revenue_splits(transaction_id);
CREATE INDEX idx_revenue_splits_recipient ON revenue_splits(recipient_id, payout_status);
CREATE INDEX idx_revenue_splits_payout ON revenue_splits(payout_status, payout_scheduled_date);
CREATE INDEX idx_revenue_splits_tenant ON revenue_splits(tenant_id);
```

### Royalty Account (Innovation Kids)
```sql
CREATE TABLE royalty_accounts (
    account_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    
    -- Kid Innovator
    innovator_id UUID NOT NULL REFERENCES users(user_id),
    innovator_name VARCHAR(255) NOT NULL,
    date_of_birth DATE NOT NULL,
    
    -- Parent/Guardian
    guardian_id UUID NOT NULL REFERENCES users(user_id),
    guardian_name VARCHAR(255) NOT NULL,
    guardian_relationship VARCHAR(50) NOT NULL, -- parent, legal_guardian
    
    -- Licensing Agreement
    licensed_idea_id UUID NOT NULL REFERENCES ideas(idea_id),
    licensing_company_id UUID NOT NULL REFERENCES companies(company_id),
    licensing_company_name VARCHAR(255) NOT NULL,
    royalty_percentage NUMERIC(5,4) NOT NULL, -- 0.01% to 0.05% (0.0001 to 0.0005)
    licensing_start_date DATE NOT NULL,
    licensing_end_date DATE NOT NULL, -- 10-15 years from start
    
    -- Account Balance
    total_royalty_earned_cents BIGINT DEFAULT 0,
    total_royalty_paid_cents BIGINT DEFAULT 0,
    available_balance_cents BIGINT DEFAULT 0,
    pending_balance_cents BIGINT DEFAULT 0,
    
    -- Payout Configuration
    payout_threshold_cents BIGINT DEFAULT 100000, -- Min ₹1000 before payout
    payout_frequency VARCHAR(20) DEFAULT 'quarterly', -- monthly, quarterly, annual
    
    -- Trust & Legal
    trust_account_number VARCHAR(100), -- Bank account held in trust
    ownership_transferred BOOLEAN DEFAULT FALSE,
    ownership_transfer_date DATE, -- When kid turns 18
    
    -- Status
    account_status VARCHAR(50) NOT NULL DEFAULT 'active', -- active, suspended, transferred
    
    -- Audit
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
    
    -- Multi-Tenant
    tenant_id UUID NOT NULL REFERENCES tenants(tenant_id),
    
    -- Constraints
    CONSTRAINT valid_royalty_percentage CHECK (
        royalty_percentage >= 0.0001 AND royalty_percentage <= 0.0005
    ),
    CONSTRAINT valid_account_status CHECK (account_status IN (
        'active', 'suspended', 'transferred', 'closed'
    ))
);

CREATE INDEX idx_royalty_accounts_innovator ON royalty_accounts(innovator_id);
CREATE INDEX idx_royalty_accounts_guardian ON royalty_accounts(guardian_id);
CREATE INDEX idx_royalty_accounts_company ON royalty_accounts(licensing_company_id);
CREATE INDEX idx_royalty_accounts_status ON royalty_accounts(account_status);
```

### Revenue Recognition Log (ASC 606 Compliance)
```sql
CREATE TABLE revenue_recognition_log (
    recognition_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    transaction_id UUID NOT NULL REFERENCES payment_transactions(transaction_id),
    
    -- ASC 606 5-Step Model
    step_1_contract_identified BOOLEAN NOT NULL, -- Contract with customer exists
    step_2_obligations_identified JSONB NOT NULL, -- Performance obligations
    step_3_price_determined BIGINT NOT NULL, -- Transaction price in cents
    step_4_price_allocated JSONB NOT NULL, -- Price allocation to obligations
    step_5_revenue_recognized_on VARCHAR(50) NOT NULL, -- control_transfer, over_time
    
    -- Revenue Recognition
    revenue_amount_cents BIGINT NOT NULL,
    recognition_date DATE NOT NULL,
    recognition_method VARCHAR(50) NOT NULL, -- point_in_time, over_time
    
    -- Deferred Revenue
    deferred_revenue_cents BIGINT DEFAULT 0,
    deferred_recognition_schedule JSONB, -- Future recognition dates
    
    -- Accounting Period
    accounting_period VARCHAR(7) NOT NULL, -- YYYY-MM format
    fiscal_year INTEGER NOT NULL,
    fiscal_quarter VARCHAR(2), -- Q1, Q2, Q3, Q4
    
    -- Reversal/Adjustment
    is_reversal BOOLEAN DEFAULT FALSE,
    original_recognition_id UUID REFERENCES revenue_recognition_log(recognition_id),
    reversal_reason TEXT,
    
    -- Audit
    recognized_at TIMESTAMP NOT NULL DEFAULT NOW(),
    recognized_by VARCHAR(50) DEFAULT 'system',
    
    -- Multi-Tenant
    tenant_id UUID NOT NULL REFERENCES tenants(tenant_id)
);

CREATE INDEX idx_revenue_recognition_transaction ON revenue_recognition_log(transaction_id);
CREATE INDEX idx_revenue_recognition_date ON revenue_recognition_log(recognition_date);
CREATE INDEX idx_revenue_recognition_period ON revenue_recognition_log(accounting_period);
```

### Invoice Entity
```sql
CREATE TABLE invoices (
    invoice_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    invoice_number VARCHAR(50) UNIQUE NOT NULL, -- Format: INV-YYYY-MM-XXXXXX
    
    -- Invoice Parties
    billed_to_id UUID NOT NULL REFERENCES users(user_id),
    billed_to_name VARCHAR(255) NOT NULL,
    billed_to_email VARCHAR(255) NOT NULL,
    billed_to_address JSONB,
    
    billed_by_name VARCHAR(255) NOT NULL DEFAULT 'Antigravity SaaS',
    billed_by_address JSONB NOT NULL,
    billed_by_gstin VARCHAR(15), -- GST identification number
    
    -- Invoice Details
    invoice_date DATE NOT NULL,
    due_date DATE NOT NULL,
    
    -- Line Items
    line_items JSONB NOT NULL, -- Array of {description, quantity, rate, amount}
    
    -- Amounts
    subtotal_cents BIGINT NOT NULL,
    tax_amount_cents BIGINT NOT NULL,
    discount_amount_cents BIGINT DEFAULT 0,
    total_amount_cents BIGINT NOT NULL,
    currency VARCHAR(3) NOT NULL DEFAULT 'INR',
    
    -- Tax Breakdown
    cgst_cents BIGINT, -- Central GST (India)
    sgst_cents BIGINT, -- State GST (India)
    igst_cents BIGINT, -- Integrated GST (India)
    
    -- Payment Status
    payment_status VARCHAR(50) NOT NULL DEFAULT 'unpaid', -- unpaid, paid, partially_paid, overdue
    paid_amount_cents BIGINT DEFAULT 0,
    payment_date DATE,
    payment_transaction_id UUID REFERENCES payment_transactions(transaction_id),
    
    -- Invoice Type
    invoice_type VARCHAR(50) NOT NULL, -- standard, proforma, credit_note, debit_note
    
    -- Related Documents
    related_invoice_id UUID REFERENCES invoices(invoice_id), -- For credit notes
    
    -- PDF Generation
    pdf_url TEXT,
    pdf_generated_at TIMESTAMP,
    
    -- Metadata
    notes TEXT,
    metadata JSONB,
    
    -- Audit
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
    
    -- Multi-Tenant
    tenant_id UUID NOT NULL REFERENCES tenants(tenant_id),
    
    -- Constraints
    CONSTRAINT valid_payment_status CHECK (payment_status IN (
        'unpaid', 'paid', 'partially_paid', 'overdue', 'canceled'
    )),
    CONSTRAINT valid_invoice_type CHECK (invoice_type IN (
        'standard', 'proforma', 'credit_note', 'debit_note', 'recurring'
    ))
);

CREATE INDEX idx_invoices_billed_to ON invoices(billed_to_id, invoice_date DESC);
CREATE INDEX idx_invoices_number ON invoices(invoice_number);
CREATE INDEX idx_invoices_status ON invoices(payment_status, due_date);
CREATE INDEX idx_invoices_date ON invoices(invoice_date DESC);
```

---

## 🔒 SECTION D — PAYMENT GATEWAY INTEGRATION (MULTI-PROVIDER)

### Supported Payment Gateways
```
PRIMARY (India):
- Razorpay (Recommended for INR)
- PhonePe
- Paytm

PRIMARY (International):
- Stripe (Global)
- PayPal (Global)

TESTING:
- Test Gateway (Sandbox)
```

### Gateway Abstraction Layer (Interface)
```python
from abc import ABC, abstractmethod
from typing import Dict, Any, Optional

class PaymentGatewayInterface(ABC):
    """
    Abstract base class for all payment gateway integrations
    Enforces consistent interface across providers
    """
    
    @abstractmethod
    def create_payment_intent(
        self,
        amount_cents: int,
        currency: str,
        description: str,
        customer_email: str,
        metadata: Dict[str, Any],
        idempotency_key: str
    ) -> Dict[str, Any]:
        """
        Create payment intent/order with gateway
        
        Returns:
        {
            "payment_intent_id": str,
            "client_secret": str,  # For client-side confirmation
            "status": str,
            "gateway_order_id": str
        }
        """
        pass
    
    @abstractmethod
    def capture_payment(
        self,
        payment_intent_id: str,
        amount_cents: Optional[int] = None  # Partial capture
    ) -> Dict[str, Any]:
        """
        Capture authorized payment
        
        Returns:
        {
            "status": "succeeded" | "failed",
            "transaction_id": str,
            "captured_amount_cents": int
        }
        """
        pass
    
    @abstractmethod
    def get_payment_status(self, payment_intent_id: str) -> Dict[str, Any]:
        """
        Retrieve current payment status
        
        Returns:
        {
            "status": str,
            "amount_cents": int,
            "currency": str,
            "payment_method": str,
            "failure_reason": Optional[str]
        }
        """
        pass
    
    @abstractmethod
    def create_refund(
        self,
        transaction_id: str,
        refund_amount_cents: int,
        reason: str,
        idempotency_key: str
    ) -> Dict[str, Any]:
        """
        Create refund for transaction
        
        Returns:
        {
            "refund_id": str,
            "status": str,
            "refunded_amount_cents": int
        }
        """
        pass
    
    @abstractmethod
    def verify_webhook_signature(
        self,
        payload: str,
        signature: str,
        webhook_secret: str
    ) -> bool:
        """
        Verify webhook came from gateway
        
        Returns:
        bool: True if signature valid
        """
        pass
    
    @abstractmethod
    def create_subscription(
        self,
        plan_id: str,
        customer_email: str,
        payment_method_token: str,
        trial_period_days: Optional[int] = None
    ) -> Dict[str, Any]:
        """
        Create recurring subscription
        
        Returns:
        {
            "subscription_id": str,
            "status": str,
            "current_period_end": datetime
        }
        """
        pass
    
    @abstractmethod
    def cancel_subscription(
        self,
        subscription_id: str,
        cancel_at_period_end: bool = True
    ) -> Dict[str, Any]:
        """
        Cancel subscription
        
        Returns:
        {
            "subscription_id": str,
            "status": str,
            "canceled_at": datetime
        }
        """
        pass
    
    @abstractmethod
    def tokenize_payment_method(
        self,
        customer_id: str,
        payment_method_details: Dict[str, Any]
    ) -> Dict[str, Any]:
        """
        Tokenize payment method for future use (PCI compliant)
        
        Returns:
        {
            "payment_method_token": str,
            "payment_method_type": str,
            "last_4_digits": str
        }
        """
        pass
```

### Razorpay Implementation
```python
import razorpay
import hmac
import hashlib

class RazorpayGateway(PaymentGatewayInterface):
    """
    Razorpay payment gateway implementation
    """
    
    def __init__(self, api_key: str, api_secret: str, webhook_secret: str):
        self.client = razorpay.Client(auth=(api_key, api_secret))
        self.webhook_secret = webhook_secret
    
    def create_payment_intent(
        self,
        amount_cents: int,
        currency: str,
        description: str,
        customer_email: str,
        metadata: Dict[str, Any],
        idempotency_key: str
    ) -> Dict[str, Any]:
        """
        Create Razorpay order
        """
        try:
            order = self.client.order.create({
                "amount": amount_cents,  # Razorpay expects paise
                "currency": currency,
                "receipt": idempotency_key,
                "notes": metadata
            })
            
            return {
                "payment_intent_id": order["id"],
                "client_secret": None,  # Razorpay doesn't use client_secret
                "status": order["status"],
                "gateway_order_id": order["id"]
            }
        
        except razorpay.errors.BadRequestError as e:
            raise PaymentGatewayError(f"Razorpay order creation failed: {str(e)}")
    
    def capture_payment(
        self,
        payment_intent_id: str,
        amount_cents: Optional[int] = None
    ) -> Dict[str, Any]:
        """
        Capture Razorpay payment
        """
        try:
            payment = self.client.payment.capture(
                payment_intent_id,
                amount_cents
            )
            
            return {
                "status": "succeeded" if payment["status"] == "captured" else "failed",
                "transaction_id": payment["id"],
                "captured_amount_cents": payment["amount"]
            }
        
        except razorpay.errors.BadRequestError as e:
            raise PaymentGatewayError(f"Razorpay capture failed: {str(e)}")
    
    def get_payment_status(self, payment_intent_id: str) -> Dict[str, Any]:
        """
        Fetch payment status from Razorpay
        """
        try:
            payment = self.client.payment.fetch(payment_intent_id)
            
            return {
                "status": payment["status"],
                "amount_cents": payment["amount"],
                "currency": payment["currency"],
                "payment_method": payment["method"],
                "failure_reason": payment.get("error_description")
            }
        
        except razorpay.errors.BadRequestError as e:
            raise PaymentGatewayError(f"Razorpay status fetch failed: {str(e)}")
    
    def create_refund(
        self,
        transaction_id: str,
        refund_amount_cents: int,
        reason: str,
        idempotency_key: str
    ) -> Dict[str, Any]:
        """
        Create refund in Razorpay
        """
        try:
            refund = self.client.payment.refund(
                transaction_id,
                {
                    "amount": refund_amount_cents,
                    "notes": {"reason": reason, "idempotency_key": idempotency_key}
                }
            )
            
            return {
                "refund_id": refund["id"],
                "status": refund["status"],
                "refunded_amount_cents": refund["amount"]
            }
        
        except razorpay.errors.BadRequestError as e:
            raise PaymentGatewayError(f"Razorpay refund failed: {str(e)}")
    
    def verify_webhook_signature(
        self,
        payload: str,
        signature: str,
        webhook_secret: str
    ) -> bool:
        """
        Verify Razorpay webhook signature
        """
        try:
            self.client.utility.verify_webhook_signature(
                payload,
                signature,
                webhook_secret
            )
            return True
        except razorpay.errors.SignatureVerificationError:
            return False
    
    def create_subscription(
        self,
        plan_id: str,
        customer_email: str,
        payment_method_token: str,
        trial_period_days: Optional[int] = None
    ) -> Dict[str, Any]:
        """
        Create Razorpay subscription
        """
        subscription_data = {
            "plan_id": plan_id,
            "customer_notify": 1,
            "total_count": 12,  # For annual billing
            "notes": {"customer_email": customer_email}
        }
        
        if trial_period_days:
            subscription_data["trial_period"] = trial_period_days
        
        try:
            subscription = self.client.subscription.create(subscription_data)
            
            return {
                "subscription_id": subscription["id"],
                "status": subscription["status"],
                "current_period_end": subscription["current_end"]
            }
        
        except razorpay.errors.BadRequestError as e:
            raise PaymentGatewayError(f"Razorpay subscription creation failed: {str(e)}")
    
    def cancel_subscription(
        self,
        subscription_id: str,
        cancel_at_period_end: bool = True
    ) -> Dict[str, Any]:
        """
        Cancel Razorpay subscription
        """
        try:
            subscription = self.client.subscription.cancel(
                subscription_id,
                cancel_at_cycle_end=1 if cancel_at_period_end else 0
            )
            
            return {
                "subscription_id": subscription["id"],
                "status": subscription["status"],
                "canceled_at": subscription.get("ended_at")
            }
        
        except razorpay.errors.BadRequestError as e:
            raise PaymentGatewayError(f"Razorpay subscription cancellation failed: {str(e)}")
    
    def tokenize_payment_method(
        self,
        customer_id: str,
        payment_method_details: Dict[str, Any]
    ) -> Dict[str, Any]:
        """
        Create Razorpay customer token
        """
        # Razorpay handles tokenization via their checkout
        # Return saved payment method
        try:
            customer = self.client.customer.fetch(customer_id)
            tokens = customer.get("tokens", [])
            
            if tokens:
                token = tokens[0]
                return {
                    "payment_method_token": token["id"],
                    "payment_method_type": token["method"],
                    "last_4_digits": token.get("card", {}).get("last4")
                }
            
            raise PaymentGatewayError("No saved payment methods found")
        
        except razorpay.errors.BadRequestError as e:
            raise PaymentGatewayError(f"Razorpay tokenization failed: {str(e)}")


class PaymentGatewayError(Exception):
    """Custom exception for payment gateway errors"""
    pass
```

### Gateway Factory (Strategy Pattern)
```python
class PaymentGatewayFactory:
    """
    Factory for creating payment gateway instances
    """
    
    _gateways = {}
    
    @classmethod
    def register_gateway(cls, name: str, gateway_class):
        """Register a gateway implementation"""
        cls._gateways[name] = gateway_class
    
    @classmethod
    def create_gateway(cls, gateway_name: str, config: Dict[str, Any]) -> PaymentGatewayInterface:
        """
        Create gateway instance based on name
        
        Args:
            gateway_name: 'razorpay', 'stripe', 'paypal', etc.
            config: Gateway-specific configuration
        
        Returns:
            PaymentGatewayInterface implementation
        """
        gateway_class = cls._gateways.get(gateway_name)
        
        if not gateway_class:
            raise ValueError(f"Unknown payment gateway: {gateway_name}")
        
        return gateway_class(**config)


# Register gateways
PaymentGatewayFactory.register_gateway('razorpay', RazorpayGateway)
PaymentGatewayFactory.register_gateway('stripe', StripeGateway)
PaymentGatewayFactory.register_gateway('paypal', PayPalGateway)
PaymentGatewayFactory.register_gateway('test_gateway', TestGateway)
```

---

## 🔒 SECTION E — PAYMENT ORCHESTRATION PIPELINE (DETERMINISTIC)

### Payment Flow Stages (Non-Negotiable Order)

```
STAGE 1: PAYMENT INTENT CREATION
├─ Input: Payment request from user
├─ Generate idempotency_key (UUID v4)
├─ Validate: amount > 0, currency valid, user authenticated
├─ Check: duplicate payment intent (idempotency check)
├─ Create payment_transactions record (status: pending)
└─ Output: transaction_id, payment_intent_id

STAGE 2: GATEWAY PAYMENT INTENT CREATION
├─ Select gateway based on currency/region
├─ Call gateway.create_payment_intent()
├─ Store gateway response (payment_intent_id, client_secret)
├─ Update transaction status: processing
└─ Output: client_secret (return to frontend)

STAGE 3: FRONTEND PAYMENT CONFIRMATION
├─ User completes payment on frontend
├─ Frontend receives payment_method from gateway
├─ Frontend sends confirmation to backend
└─ Backend validates and captures

STAGE 4: PAYMENT CAPTURE
├─ Verify payment_intent belongs to user (security)
├─ Call gateway.capture_payment()
├─ Update transaction status: succeeded | failed
├─ Store gateway_transaction_id
└─ Emit payment.succeeded | payment.failed event

STAGE 5: FRAUD DETECTION
├─ Calculate fraud_score (0-100)
├─ Check: velocity limits, suspicious patterns
├─ If fraud_score > FRAUD_THRESHOLD:
│  ├─ Flag transaction
│  ├─ Hold funds
│  └─ Notify admin
└─ Update transaction.fraud_score, fraud_check_passed

STAGE 6: REVENUE SPLIT CALCULATION
├─ Load revenue split rules (based on transaction_type)
├─ Calculate platform_commission_cents
├─ Calculate trainer_share_cents (if applicable)
├─ Calculate innovator_royalty_cents (if applicable)
├─ Calculate referrer_commission_cents (if applicable)
├─ Create revenue_splits records
└─ Update transaction with split amounts

STAGE 7: REVENUE RECOGNITION (ASC 606)
├─ Identify performance obligations
├─ Determine transaction price
├─ Allocate price to obligations
├─ Determine revenue recognition timing
├─ Create revenue_recognition_log record
├─ Update transaction.revenue_recognized = TRUE
└─ Emit revenue.recognized event

STAGE 8: INVOICE GENERATION
├─ Generate invoice_number (INV-YYYY-MM-XXXXXX)
├─ Create invoice record with line items
├─ Calculate taxes (CGST, SGST, IGST)
├─ Generate invoice PDF
├─ Upload PDF to storage
├─ Update invoice.pdf_url
└─ Emit invoice.generated event

STAGE 9: NOTIFICATION ORCHESTRATION
├─ Send payment confirmation email to payer
├─ Attach invoice PDF
├─ Send in-app notification
├─ Send SMS confirmation (if enabled)
├─ Notify trainer/innovator of revenue split (if applicable)
└─ Log notification delivery

STAGE 10: ENTITLEMENT PROVISIONING
├─ Grant access to purchased resource (course, event, feature)
├─ Update subscription status (if subscription payment)
├─ Update user permissions
├─ Trigger onboarding workflow (if first purchase)
└─ Emit entitlement.granted event

STAGE 11: ANALYTICS & REPORTING
├─ Update revenue metrics (daily, monthly, annual)
├─ Update commission metrics
├─ Update royalty metrics
├─ Update payer lifetime value
└─ Emit analytics.payment_completed event
```

### Failure Handling (Per Stage)
```
STAGE 1 FAILURE (Idempotency Violation):
→ Return existing transaction (no duplicate created)

STAGE 2 FAILURE (Gateway API Error):
→ Mark transaction as failed
→ Store failure_code and failure_message
→ Retry with exponential backoff (max 3 attempts)
→ If all retries fail: Notify admin, refund user if charged

STAGE 4 FAILURE (Capture Failed):
→ Mark transaction as failed
→ DO NOT provision entitlement
→ Emit payment.failed event
→ Notify user of failure

STAGE 5 FAILURE (Fraud Detected):
→ Hold transaction (status: on_hold)
→ DO NOT recognize revenue
→ DO NOT provision entitlement
→ Notify admin for manual review
→ Create fraud investigation ticket

ANY STAGE FAILURE (Critical):
→ ROLLBACK partial state
→ LOG detailed error with stack trace
→ ALERT DevOps team
→ NO PARTIAL ENTITLEMENT
```

---

## 🔒 SECTION F — COMMISSION & REVENUE SPLIT ENGINE

### Revenue Split Rules (by Transaction Type)

```python
REVENUE_SPLIT_RULES = {
    "course_purchase": {
        "platform_commission_percentage": 20.0,  # 20%
        "trainer_share_percentage": 80.0,         # 80%
        "split_recipients": ["platform", "trainer"]
    },
    
    "subscription_monthly_student_pro": {
        "platform_commission_percentage": 100.0,  # Platform keeps all
        "split_recipients": ["platform"]
    },
    
    "subscription_monthly_trainer_creator": {
        "platform_commission_percentage": 100.0,
        "split_recipients": ["platform"]
    },
    
    "event_registration": {
        "platform_commission_percentage": 15.0,   # 15%
        "organizer_share_percentage": 85.0,       # 85%
        "split_recipients": ["platform", "organizer"]
    },
    
    "dojo_belt_certification": {
        "platform_commission_percentage": 30.0,   # 30%
        "mentor_share_percentage": 70.0,          # 70%
        "split_recipients": ["platform", "mentor"]
    },
    
    "job_posting_fee": {
        "platform_commission_percentage": 100.0,
        "split_recipients": ["platform"]
    },
    
    "innovation_licensing_fee": {
        "platform_commission_percentage": 10.0,   # 10%
        "innovator_share_percentage": 90.0,       # 90% (goes to trust account)
        "split_recipients": ["platform", "innovator"]
    },
    
    "referral_commission": {
        "platform_commission_percentage": 0.0,    # Platform doesn't take cut
        "referrer_commission_percentage": 100.0,  # 100% to referrer
        "split_recipients": ["referrer"]
    },
    
    "co_teaching_course": {
        "platform_commission_percentage": 20.0,   # 20%
        "primary_trainer_percentage": 40.0,       # 40%
        "co_trainer_percentage": 40.0,            # 40%
        "split_recipients": ["platform", "primary_trainer", "co_trainer"]
    }
}
```

### Revenue Split Calculation Algorithm
```python
def calculate_revenue_splits(
    transaction: PaymentTransaction,
    split_rules: Dict[str, Any]
) -> List[RevenueSplit]:
    """
    Calculate revenue splits for a transaction
    
    Returns:
        List of RevenueSplit objects
    """
    
    splits = []
    transaction_amount_cents = transaction.amount_cents
    
    # Platform Commission
    if "platform_commission_percentage" in split_rules:
        platform_commission_cents = int(
            transaction_amount_cents * 
            split_rules["platform_commission_percentage"] / 100
        )
        
        splits.append(RevenueSplit(
            transaction_id=transaction.transaction_id,
            split_type="platform_commission",
            recipient_type="platform",
            recipient_id=None,
            recipient_name="Antigravity Platform",
            split_amount_cents=platform_commission_cents,
            split_percentage=split_rules["platform_commission_percentage"],
            payout_status="paid",  # Platform receives immediately
            payout_completed_date=datetime.now()
        ))
    
    # Trainer Share
    if "trainer_share_percentage" in split_rules:
        trainer_share_cents = int(
            transaction_amount_cents * 
            split_rules["trainer_share_percentage"] / 100
        )
        
        # Get trainer_id from transaction metadata
        trainer_id = transaction.metadata.get("trainer_id")
        
        splits.append(RevenueSplit(
            transaction_id=transaction.transaction_id,
            split_type="trainer_share",
            recipient_type="trainer",
            recipient_id=trainer_id,
            recipient_name=get_trainer_name(trainer_id),
            split_amount_cents=trainer_share_cents,
            split_percentage=split_rules["trainer_share_percentage"],
            payout_status="pending",
            payout_scheduled_date=calculate_payout_date("weekly")
        ))
    
    # Co-Trainer Share
    if "co_trainer_percentage" in split_rules:
        co_trainer_share_cents = int(
            transaction_amount_cents * 
            split_rules["co_trainer_percentage"] / 100
        )
        
        co_trainer_id = transaction.metadata.get("co_trainer_id")
        
        splits.append(RevenueSplit(
            transaction_id=transaction.transaction_id,
            split_type="co_trainer_share",
            recipient_type="trainer",
            recipient_id=co_trainer_id,
            recipient_name=get_trainer_name(co_trainer_id),
            split_amount_cents=co_trainer_share_cents,
            split_percentage=split_rules["co_trainer_percentage"],
            payout_status="pending",
            payout_scheduled_date=calculate_payout_date("weekly")
        ))
    
    # Innovator Royalty (for innovation licensing)
    if "innovator_share_percentage" in split_rules:
        innovator_share_cents = int(
            transaction_amount_cents * 
            split_rules["innovator_share_percentage"] / 100
        )
        
        innovator_id = transaction.metadata.get("innovator_id")
        
        splits.append(RevenueSplit(
            transaction_id=transaction.transaction_id,
            split_type="innovator_royalty",
            recipient_type="innovator",
            recipient_id=innovator_id,
            recipient_name=get_innovator_name(innovator_id),
            split_amount_cents=innovator_share_cents,
            split_percentage=split_rules["innovator_share_percentage"],
            payout_status="pending",
            payout_scheduled_date=calculate_payout_date("quarterly")
        ))
    
    # Referrer Commission
    if "referrer_commission_percentage" in split_rules:
        referrer_commission_cents = int(
            transaction_amount_cents * 
            split_rules["referrer_commission_percentage"] / 100
        )
        
        referrer_id = transaction.metadata.get("referrer_id")
        
        if referrer_id:  # Only if referral exists
            splits.append(RevenueSplit(
                transaction_id=transaction.transaction_id,
                split_type="referrer_commission",
                recipient_type="referrer",
                recipient_id=referrer_id,
                recipient_name=get_user_name(referrer_id),
                split_amount_cents=referrer_commission_cents,
                split_percentage=split_rules["referrer_commission_percentage"],
                payout_status="pending",
                payout_scheduled_date=calculate_payout_date("monthly")
            ))
    
    # Validation: Splits must sum to 100% (within 1 cent tolerance for rounding)
    total_split_cents = sum(split.split_amount_cents for split in splits)
    difference = abs(total_split_cents - transaction_amount_cents)
    
    if difference > 1:
        raise ValueError(
            f"Revenue splits don't sum to transaction amount. "
            f"Transaction: {transaction_amount_cents}, "
            f"Splits total: {total_split_cents}, "
            f"Difference: {difference}"
        )
    
    # Adjust largest split to account for rounding
    if difference == 1:
        largest_split = max(splits, key=lambda s: s.split_amount_cents)
        largest_split.split_amount_cents += (transaction_amount_cents - total_split_cents)
    
    return splits


def calculate_payout_date(frequency: str) -> date:
    """
    Calculate next payout date based on frequency
    
    Args:
        frequency: 'weekly', 'monthly', 'quarterly'
    
    Returns:
        Next payout date
    """
    today = date.today()
    
    if frequency == "weekly":
        # Next Friday
        days_until_friday = (4 - today.weekday()) % 7
        if days_until_friday == 0:
            days_until_friday = 7
        return today + timedelta(days=days_until_friday)
    
    elif frequency == "monthly":
        # 1st of next month
        if today.month == 12:
            return date(today.year + 1, 1, 1)
        else:
            return date(today.year, today.month + 1, 1)
    
    elif frequency == "quarterly":
        # End of current quarter + 15 days
        quarter_end_months = [3, 6, 9, 12]
        current_quarter_end = next(m for m in quarter_end_months if m >= today.month)
        quarter_end_date = date(today.year, current_quarter_end, 1) + timedelta(days=32)
        quarter_end_date = quarter_end_date.replace(day=1) - timedelta(days=1)
        return quarter_end_date + timedelta(days=15)
    
    else:
        raise ValueError(f"Invalid payout frequency: {frequency}")
```

---

## 🔒 SECTION G — ROYALTY MANAGEMENT (INNOVATION KIDS)

### Royalty Calculation Engine
```python
def calculate_innovation_royalty(
    licensing_agreement: RoyaltyAccount,
    business_revenue_cents: int,
    reporting_period: str  # 'YYYY-QQ' format (e.g., '2026-Q1')
) -> Dict[str, Any]:
    """
    Calculate royalty payment for innovation licensing
    
    Args:
        licensing_agreement: RoyaltyAccount with royalty_percentage
        business_revenue_cents: Reported revenue from business using idea
        reporting_period: Reporting quarter
    
    Returns:
        Royalty calculation details
    """
    
    # Calculate royalty amount
    royalty_amount_cents = int(
        business_revenue_cents * licensing_agreement.royalty_percentage
    )
    
    # Check minimum royalty clause (if exists)
    min_royalty_cents = licensing_agreement.metadata.get("min_royalty_cents", 0)
    if royalty_amount_cents < min_royalty_cents:
        royalty_amount_cents = min_royalty_cents
    
    # Tax withholding (TDS in India: 10% for minors)
    tax_withheld_cents = int(royalty_amount_cents * 0.10)
    net_royalty_cents = royalty_amount_cents - tax_withheld_cents
    
    # Update royalty account
    update_royalty_account(
        account_id=licensing_agreement.account_id,
        delta_earned_cents=royalty_amount_cents,
        delta_pending_cents=net_royalty_cents
    )
    
    # Create revenue split record
    create_revenue_split(
        split_type="innovator_royalty",
        recipient_id=licensing_agreement.innovator_id,
        split_amount_cents=royalty_amount_cents,
        tax_withheld_cents=tax_withheld_cents,
        payout_status="pending",
        metadata={
            "licensing_agreement_id": licensing_agreement.account_id,
            "business_revenue_cents": business_revenue_cents,
            "royalty_percentage": float(licensing_agreement.royalty_percentage),
            "reporting_period": reporting_period
        }
    )
    
    # Send notification to parent/guardian
    send_notification(
        user_id=licensing_agreement.guardian_id,
        notification_type="royalty_calculated",
        title=f"Royalty Earned - {reporting_period}",
        message=(
            f"Your child {licensing_agreement.innovator_name} earned "
            f"₹{net_royalty_cents / 100:.2f} in royalties for {reporting_period}."
        )
    )
    
    return {
        "royalty_amount_cents": royalty_amount_cents,
        "tax_withheld_cents": tax_withheld_cents,
        "net_royalty_cents": net_royalty_cents,
        "business_revenue_cents": business_revenue_cents,
        "royalty_percentage": float(licensing_agreement.royalty_percentage),
        "reporting_period": reporting_period,
        "payout_scheduled_date": calculate_payout_date("quarterly")
    }


def process_royalty_payout(account_id: UUID):
    """
    Process quarterly royalty payout to innovator trust account
    """
    
    account = get_royalty_account(account_id)
    
    # Check payout threshold
    if account.available_balance_cents < account.payout_threshold_cents:
        logger.info(
            f"Royalty account {account_id} below payout threshold. "
            f"Available: ₹{account.available_balance_cents / 100}, "
            f"Threshold: ₹{account.payout_threshold_cents / 100}"
        )
        return {"status": "below_threshold"}
    
    # Verify guardian approval
    if not verify_guardian_approval(account.guardian_id, account_id):
        raise ValueError("Guardian approval required for royalty payout")
    
    # Transfer to trust account (bank transfer)
    payout_amount_cents = account.available_balance_cents
    
    try:
        # Initiate bank transfer
        transfer_result = initiate_bank_transfer(
            account_number=account.trust_account_number,
            amount_cents=payout_amount_cents,
            description=f"Royalty payout for {account.innovator_name}",
            reference_id=f"ROYALTY-{account_id}-{datetime.now().strftime('%Y%m%d')}"
        )
        
        # Update royalty account
        update_royalty_account(
            account_id=account_id,
            delta_paid_cents=payout_amount_cents,
            delta_available_cents=-payout_amount_cents
        )
        
        # Update revenue_splits records
        mark_splits_as_paid(
            recipient_id=account.innovator_id,
            split_type="innovator_royalty",
            payout_amount_cents=payout_amount_cents,
            payout_reference_id=transfer_result["transfer_id"]
        )
        
        # Send confirmation
        send_notification(
            user_id=account.guardian_id,
            notification_type="royalty_payout_completed",
            title="Royalty Payout Successful",
            message=(
                f"₹{payout_amount_cents / 100:.2f} has been transferred to "
                f"{account.innovator_name}'s trust account."
            )
        )
        
        return {
            "status": "success",
            "payout_amount_cents": payout_amount_cents,
            "transfer_id": transfer_result["transfer_id"]
        }
    
    except BankTransferError as e:
        logger.error(f"Royalty payout failed for account {account_id}: {str(e)}")
        
        # Notify guardian of failure
        send_notification(
            user_id=account.guardian_id,
            notification_type="royalty_payout_failed",
            title="Royalty Payout Failed",
            message=(
                f"Payout of ₹{payout_amount_cents / 100:.2f} failed. "
                f"Please contact support. Error: {str(e)}"
            )
        )
        
        return {"status": "failed", "error": str(e)}


def transfer_ownership_at_18(innovator_id: UUID):
    """
    Transfer royalty account ownership when innovator turns 18
    """
    
    account = get_royalty_account_by_innovator(innovator_id)
    innovator = get_user(innovator_id)
    
    # Calculate age
    age = calculate_age(innovator.date_of_birth)
    
    if age < 18:
        raise ValueError(f"Innovator is only {age} years old. Must be 18+.")
    
    # Create new bank account for innovator (if not exists)
    if not account.metadata.get("adult_account_number"):
        # Trigger KYC process
        initiate_adult_kyc(innovator_id)
        return {"status": "kyc_pending"}
    
    # Transfer ownership
    update_royalty_account(
        account_id=account.account_id,
        ownership_transferred=True,
        ownership_transfer_date=date.today(),
        account_status="transferred",
        metadata={
            **account.metadata,
            "transferred_from_guardian": account.guardian_id,
            "transfer_date": date.today().isoformat()
        }
    )
    
    # Send notifications
    send_notification(
        user_id=innovator_id,
        notification_type="royalty_ownership_transferred",
        title="Royalty Account Ownership Transferred",
        message=(
            f"Congratulations! You now have full ownership of your royalty account. "
            f"Balance: ₹{account.available_balance_cents / 100:.2f}"
        )
    )
    
    send_notification(
        user_id=account.guardian_id,
        notification_type="royalty_guardianship_ended",
        title="Guardianship Ended",
        message=(
            f"{innovator.name} has turned 18 and now has full ownership "
            f"of their royalty account."
        )
    )
    
    return {"status": "transferred", "new_owner_id": innovator_id}
```

---

## 🔒 SECTION H — SUBSCRIPTION LIFECYCLE MANAGEMENT

### Subscription Creation Flow
```python
def create_subscription(
    user_id: UUID,
    plan_id: UUID,
    payment_method_token: str,
    trial_period_days: Optional[int] = None
) -> Subscription:
    """
    Create new subscription
    """
    
    # 1. Load plan details
    plan = get_subscription_plan(plan_id)
    
    # 2. Check user eligibility
    existing_subscription = get_active_subscription(user_id, plan.subscriber_type)
    if existing_subscription:
        raise ValueError(
            f"User already has active {plan.subscriber_type} subscription"
        )
    
    # 3. Create gateway subscription
    gateway = PaymentGatewayFactory.create_gateway(
        gateway_name=plan.payment_gateway,
        config=get_gateway_config(plan.payment_gateway)
    )
    
    gateway_subscription = gateway.create_subscription(
        plan_id=plan.gateway_plan_id,
        customer_email=get_user_email(user_id),
        payment_method_token=payment_method_token,
        trial_period_days=trial_period_days
    )
    
    # 4. Create subscription record
    subscription = Subscription.create(
        subscriber_id=user_id,
        subscriber_type=plan.subscriber_type,
        plan_id=plan_id,
        plan_name=plan.plan_name,
        plan_tier=plan.tier,
        billing_amount_cents=plan.price_cents,
        currency=plan.currency,
        billing_cycle=plan.billing_cycle,
        gateway_subscription_id=gateway_subscription["subscription_id"],
        payment_gateway=plan.payment_gateway,
        status="trialing" if trial_period_days else "active",
        trial_start_date=date.today() if trial_period_days else None,
        trial_end_date=(
            date.today() + timedelta(days=trial_period_days)
            if trial_period_days else None
        ),
        subscription_start_date=date.today(),
        current_period_start=date.today(),
        current_period_end=calculate_period_end(date.today(), plan.billing_cycle),
        next_billing_date=calculate_next_billing_date(
            date.today(), trial_period_days, plan.billing_cycle
        ),
        entitlements=plan.entitlements,
        usage_limits=plan.usage_limits
    )
    
    # 5. Grant entitlements
    grant_subscription_entitlements(user_id, plan.entitlements)
    
    # 6. Emit event
    emit_event("subscription.created", {
        "subscription_id": subscription.subscription_id,
        "user_id": user_id,
        "plan_id": plan_id,
        "status": subscription.status
    })
    
    return subscription


def process_subscription_renewal(subscription_id: UUID):
    """
    Process subscription renewal (triggered by webhook or cron)
    """
    
    subscription = get_subscription(subscription_id)
    
    # 1. Check if renewal due
    if date.today() < subscription.next_billing_date:
        return {"status": "not_due"}
    
    # 2. Attempt payment
    gateway = PaymentGatewayFactory.create_gateway(
        gateway_name=subscription.payment_gateway,
        config=get_gateway_config(subscription.payment_gateway)
    )
    
    try:
        # Gateway handles automatic charging
        payment_result = gateway.get_payment_status(
            subscription.gateway_subscription_id
        )
        
        if payment_result["status"] == "succeeded":
            # 3. Create payment transaction record
            create_payment_transaction(
                idempotency_key=f"SUB-{subscription_id}-{date.today().isoformat()}",
                payment_intent_id=payment_result["payment_intent_id"],
                payment_gateway=subscription.payment_gateway,
                amount_cents=subscription.billing_amount_cents,
                currency=subscription.currency,
                description=f"Subscription renewal: {subscription.plan_name}",
                payer_id=subscription.subscriber_id,
                transaction_type="subscription_renewal",
                reference_entity_id=subscription_id,
                status="succeeded"
            )
            
            # 4. Update subscription
            update_subscription(
                subscription_id=subscription_id,
                last_payment_date=date.today(),
                current_period_start=subscription.current_period_end + timedelta(days=1),
                current_period_end=calculate_period_end(
                    subscription.current_period_end + timedelta(days=1),
                    subscription.billing_cycle
                ),
                next_billing_date=calculate_next_billing_date(
                    subscription.current_period_end,
                    None,
                    subscription.billing_cycle
                ),
                failed_payment_count=0,
                status="active"
            )
            
            # 5. Emit event
            emit_event("subscription.renewed", {
                "subscription_id": subscription_id,
                "user_id": subscription.subscriber_id,
                "amount_cents": subscription.billing_amount_cents
            })
            
            return {"status": "renewed"}
        
        else:
            # Payment failed
            handle_subscription_payment_failure(subscription)
            return {"status": "payment_failed"}
    
    except Exception as e:
        logger.error(f"Subscription renewal failed: {str(e)}")
        handle_subscription_payment_failure(subscription)
        return {"status": "error", "error": str(e)}


def handle_subscription_payment_failure(subscription: Subscription):
    """
    Handle failed subscription payment
    """
    
    failed_count = subscription.failed_payment_count + 1
    
    # Update subscription
    update_subscription(
        subscription_id=subscription.subscription_id,
        failed_payment_count=failed_count,
        status="past_due"
    )
    
    # Send notification
    send_notification(
        user_id=subscription.subscriber_id,
        notification_type="subscription_payment_failed",
        title="Subscription Payment Failed",
        message=(
            f"Your {subscription.plan_name} subscription payment failed. "
            f"Please update your payment method to avoid service interruption."
        )
    )
    
    # Grace period: 3 failed attempts
    if failed_count >= 3:
        # Cancel subscription
        cancel_subscription(
            subscription_id=subscription.subscription_id,
            cancellation_reason="Payment failure (3 attempts)",
            cancel_immediately=True
        )


def cancel_subscription(
    subscription_id: UUID,
    cancellation_reason: str,
    cancel_immediately: bool = False
):
    """
    Cancel subscription
    
    Args:
        subscription_id: Subscription to cancel
        cancellation_reason: Why subscription is being canceled
        cancel_immediately: If True, cancel now; else cancel at period end
    """
    
    subscription = get_subscription(subscription_id)
    
    # 1. Cancel at gateway
    gateway = PaymentGatewayFactory.create_gateway(
        gateway_name=subscription.payment_gateway,
        config=get_gateway_config(subscription.payment_gateway)
    )
    
    gateway.cancel_subscription(
        subscription_id=subscription.gateway_subscription_id,
        cancel_at_period_end=not cancel_immediately
    )
    
    # 2. Update subscription
    update_subscription(
        subscription_id=subscription_id,
        status="canceled",
        cancel_at_period_end=not cancel_immediately,
        canceled_at=datetime.now(),
        cancellation_reason=cancellation_reason
    )
    
    # 3. Revoke entitlements (if immediate cancellation)
    if cancel_immediately:
        revoke_subscription_entitlements(
            subscription.subscriber_id,
            subscription.entitlements
        )
    
    # 4. Emit event
    emit_event("subscription.canceled", {
        "subscription_id": subscription_id,
        "user_id": subscription.subscriber_id,
        "cancel_immediately": cancel_immediately,
        "reason": cancellation_reason
    })
    
    # 5. Send notification
    send_notification(
        user_id=subscription.subscriber_id,
        notification_type="subscription_canceled",
        title="Subscription Canceled",
        message=(
            f"Your {subscription.plan_name} subscription has been canceled. "
            f"{'Access revoked immediately.' if cancel_immediately else 'Access until ' + subscription.current_period_end.isoformat()}"
        )
    )
```

---

## 🔒 SECTION I — REFUND & DISPUTE MANAGEMENT

### Refund Processing
```python
def process_refund(
    transaction_id: UUID,
    refund_amount_cents: int,
    refund_reason: str,
    initiated_by_user_id: UUID,
    refund_type: str = "full"  # full, partial
) -> Dict[str, Any]:
    """
    Process refund for a transaction
    
    Args:
        transaction_id: Original transaction to refund
        refund_amount_cents: Amount to refund
        refund_reason: Reason for refund
        initiated_by_user_id: Admin or user requesting refund
        refund_type: 'full' or 'partial'
    
    Returns:
        Refund result
    """
    
    # 1. Load original transaction
    transaction = get_payment_transaction(transaction_id)
    
    # 2. Validate refund
    if transaction.status != "succeeded":
        raise ValueError(f"Cannot refund transaction with status: {transaction.status}")
    
    if refund_amount_cents > transaction.amount_cents:
        raise ValueError(
            f"Refund amount ({refund_amount_cents}) exceeds "
            f"transaction amount ({transaction.amount_cents})"
        )
    
    if transaction.refunded_at:
        raise ValueError("Transaction already refunded")
    
    # 3. Check refund window (e.g., 30 days)
    days_since_payment = (datetime.now() - transaction.succeeded_at).days
    if days_since_payment > 30:
        raise ValueError(
            f"Refund window expired. Transaction is {days_since_payment} days old."
        )
    
    # 4. Generate idempotency key for refund
    refund_idempotency_key = f"REFUND-{transaction_id}-{datetime.now().isoformat()}"
    
    # 5. Process refund at gateway
    gateway = PaymentGatewayFactory.create_gateway(
        gateway_name=transaction.payment_gateway,
        config=get_gateway_config(transaction.payment_gateway)
    )
    
    try:
        refund_result = gateway.create_refund(
            transaction_id=transaction.gateway_transaction_id,
            refund_amount_cents=refund_amount_cents,
            reason=refund_reason,
            idempotency_key=refund_idempotency_key
        )
        
        # 6. Update transaction
        update_payment_transaction(
            transaction_id=transaction_id,
            status="refunded" if refund_type == "full" else "partially_refunded",
            refund_amount_cents=refund_amount_cents,
            refund_reason=refund_reason,
            refunded_at=datetime.now(),
            refund_initiated_by=initiated_by_user_id,
            metadata={
                **transaction.metadata,
                "refund_id": refund_result["refund_id"],
                "refund_type": refund_type
            }
        )
        
        # 7. Reverse revenue recognition (if already recognized)
        if transaction.revenue_recognized:
            create_revenue_recognition_reversal(
                original_recognition_id=get_revenue_recognition_id(transaction_id),
                reversal_amount_cents=refund_amount_cents,
                reversal_reason=refund_reason
            )
        
        # 8. Reverse commission splits (if paid)
        reverse_revenue_splits(transaction_id, refund_amount_cents)
        
        # 9. Revoke entitlement (if applicable)
        revoke_entitlement_for_refund(transaction)
        
        # 10. Emit event
        emit_event("payment.refunded", {
            "transaction_id": transaction_id,
            "refund_amount_cents": refund_amount_cents,
            "refund_type": refund_type,
            "initiated_by": initiated_by_user_id
        })
        
        # 11. Send notification
        send_notification(
            user_id=transaction.payer_id,
            notification_type="refund_processed",
            title="Refund Processed",
            message=(
                f"Your refund of ₹{refund_amount_cents / 100:.2f} has been processed. "
                f"It will appear in your account within 5-7 business days."
            )
        )
        
        return {
            "status": "success",
            "refund_id": refund_result["refund_id"],
            "refund_amount_cents": refund_amount_cents
        }
    
    except PaymentGatewayError as e:
        logger.error(f"Refund failed: {str(e)}")
        
        # Create refund failure record
        create_refund_failure_log(
            transaction_id=transaction_id,
            refund_amount_cents=refund_amount_cents,
            failure_reason=str(e),
            initiated_by=initiated_by_user_id
        )
        
        return {"status": "failed", "error": str(e)}


def reverse_revenue_splits(transaction_id: UUID, refund_amount_cents: int):
    """
    Reverse commission splits for refunded transaction
    """
    
    splits = get_revenue_splits(transaction_id)
    
    for split in splits:
        # Calculate proportional reversal
        split_refund_cents = int(
            refund_amount_cents * (split.split_amount_cents / sum(s.split_amount_cents for s in splits))
        )
        
        if split.payout_status == "paid":
            # Deduct from next payout
            create_revenue_split_reversal(
                original_split_id=split.split_id,
                reversal_amount_cents=split_refund_cents,
                reversal_reason="Transaction refunded"
            )
            
            # Notify recipient
            if split.recipient_id:
                send_notification(
                    user_id=split.recipient_id,
                    notification_type="commission_reversed",
                    title="Commission Reversed",
                    message=(
                        f"A commission of ₹{split_refund_cents / 100:.2f} has been "
                        f"reversed due to a refund. This will be deducted from your next payout."
                    )
                )
        
        elif split.payout_status == "pending":
            # Simply cancel the pending split
            update_revenue_split(
                split_id=split.split_id,
                payout_status="canceled",
                metadata={
                    **split.metadata,
                    "canceled_reason": "Transaction refunded"
                }
            )
```

---

## 🔒 SECTION J — WEBHOOK HANDLING (SECURITY-CRITICAL)

### Webhook Verification & Processing
```python
from flask import Flask, request, jsonify
import hmac
import hashlib

app = Flask(__name__)

@app.route('/webhooks/razorpay', methods=['POST'])
def handle_razorpay_webhook():
    """
    Handle webhooks from Razorpay
    SECURITY: Must verify signature before processing
    """
    
    # 1. Extract webhook data
    payload = request.get_data(as_text=True)
    signature = request.headers.get('X-Razorpay-Signature')
    
    if not signature:
        logger.warning("Razorpay webhook missing signature")
        return jsonify({"error": "Missing signature"}), 401
    
    # 2. Verify signature
    webhook_secret = get_config("RAZORPAY_WEBHOOK_SECRET")
    
    gateway = PaymentGatewayFactory.create_gateway(
        'razorpay',
        get_gateway_config('razorpay')
    )
    
    if not gateway.verify_webhook_signature(payload, signature, webhook_secret):
        logger.error("Razorpay webhook signature verification failed")
        return jsonify({"error": "Invalid signature"}), 401
    
    # 3. Parse webhook event
    import json
    event_data = json.loads(payload)
    event_type = event_data.get('event')
    
    logger.info(f"Received Razorpay webhook: {event_type}")
    
    # 4. Route to appropriate handler (IDEMPOTENT)
    try:
        if event_type == 'payment.captured':
            handle_payment_captured(event_data['payload']['payment']['entity'])
        
        elif event_type == 'payment.failed':
            handle_payment_failed(event_data['payload']['payment']['entity'])
        
        elif event_type == 'subscription.charged':
            handle_subscription_charged(event_data['payload']['subscription']['entity'])
        
        elif event_type == 'subscription.cancelled':
            handle_subscription_cancelled(event_data['payload']['subscription']['entity'])
        
        elif event_type == 'refund.created':
            handle_refund_created(event_data['payload']['refund']['entity'])
        
        else:
            logger.warning(f"Unhandled Razorpay webhook event: {event_type}")
        
        return jsonify({"status": "processed"}), 200
    
    except Exception as e:
        logger.error(f"Error processing Razorpay webhook: {str(e)}", exc_info=True)
        # Return 200 to prevent retries for unrecoverable errors
        return jsonify({"status": "error", "message": str(e)}), 200


def handle_payment_captured(payment_data: Dict[str, Any]):
    """
    Handle payment.captured webhook
    IDEMPOTENT: Multiple calls with same payment_id should not duplicate
    """
    
    payment_id = payment_data['id']
    order_id = payment_data['order_id']
    amount_cents = payment_data['amount']
    
    # Find transaction by order_id (idempotency)
    transaction = get_transaction_by_gateway_order_id(order_id)
    
    if not transaction:
        logger.error(f"Transaction not found for order_id: {order_id}")
        return
    
    # Idempotency check
    if transaction.status == "succeeded":
        logger.info(f"Transaction {transaction.transaction_id} already processed")
        return
    
    # Update transaction
    update_payment_transaction(
        transaction_id=transaction.transaction_id,
        status="succeeded",
        gateway_transaction_id=payment_id,
        succeeded_at=datetime.now(),
        gateway_response=payment_data
    )
    
    # Trigger post-payment processing
    process_successful_payment(transaction.transaction_id)


def handle_subscription_charged(subscription_data: Dict[str, Any]):
    """
    Handle subscription.charged webhook (recurring payment)
    """
    
    subscription_id = subscription_data['id']
    payment_id = subscription_data.get('latest_payment_id')
    
    # Find subscription
    subscription = get_subscription_by_gateway_id(subscription_id)
    
    if not subscription:
        logger.error(f"Subscription not found: {subscription_id}")
        return
    
    # Process renewal
    process_subscription_renewal(subscription.subscription_id)
```

---

## 🔒 SECTION K — FRAUD DETECTION

### Fraud Scoring Algorithm
```python
def calculate_fraud_score(transaction: PaymentTransaction) -> float:
    """
    Calculate fraud risk score (0-100, higher = more risky)
    
    Returns:
        Fraud score between 0.00 and 100.00
    """
    
    fraud_score = 0.0
    risk_flags = []
    
    # Signal 1: Unusual transaction amount
    user_avg_transaction = get_user_average_transaction(transaction.payer_id)
    if user_avg_transaction > 0:
        amount_deviation = abs(transaction.amount_cents - user_avg_transaction) / user_avg_transaction
        if amount_deviation > 3.0:  # 3x deviation
            fraud_score += 25.0
            risk_flags.append("unusual_amount")
    
    # Signal 2: Velocity check (multiple transactions in short time)
    recent_transaction_count = count_user_transactions_last_hour(transaction.payer_id)
    if recent_transaction_count > 5:
        fraud_score += 20.0
        risk_flags.append("high_velocity")
    
    # Signal 3: New account (< 7 days old)
    account_age_days = get_account_age_days(transaction.payer_id)
    if account_age_days < 7:
        fraud_score += 15.0
        risk_flags.append("new_account")
    
    # Signal 4: Mismatched billing location
    user_location = get_user_location(transaction.payer_id)
    payment_location = extract_location_from_ip(request.remote_addr)
    if user_location and payment_location:
        if calculate_distance(user_location, payment_location) > 1000:  # km
            fraud_score += 20.0
            risk_flags.append("location_mismatch")
    
    # Signal 5: Failed payment attempts
    failed_attempts_24h = count_failed_payments_24h(transaction.payer_id)
    if failed_attempts_24h > 3:
        fraud_score += 15.0
        risk_flags.append("multiple_failures")
    
    # Signal 6: Email/phone not verified
    user = get_user(transaction.payer_id)
    if not user.email_verified:
        fraud_score += 10.0
        risk_flags.append("email_unverified")
    if not user.phone_verified:
        fraud_score += 10.0
        risk_flags.append("phone_unverified")
    
    # Signal 7: VPN/Proxy detected
    if detect_vpn_proxy(request.remote_addr):
        fraud_score += 15.0
        risk_flags.append("vpn_detected")
    
    # Signal 8: Device fingerprint mismatch
    stored_fingerprint = get_user_device_fingerprint(transaction.payer_id)
    current_fingerprint = extract_device_fingerprint(request.headers)
    if stored_fingerprint and stored_fingerprint != current_fingerprint:
        fraud_score += 10.0
        risk_flags.append("device_mismatch")
    
    # Cap score at 100
    fraud_score = min(fraud_score, 100.0)
    
    # Update transaction
    update_payment_transaction(
        transaction_id=transaction.transaction_id,
        fraud_score=fraud_score,
        fraud_check_passed=(fraud_score < FRAUD_THRESHOLD),
        risk_flags=risk_flags
    )
    
    return fraud_score


FRAUD_THRESHOLD = 50.0  # Transactions above this score are flagged


def handle_high_fraud_score(transaction: PaymentTransaction):
    """
    Handle transaction flagged as high fraud risk
    """
    
    # 1. Hold transaction
    update_payment_transaction(
        transaction_id=transaction.transaction_id,
        status="on_hold",
        metadata={
            **transaction.metadata,
            "fraud_hold_reason": "High fraud score",
            "fraud_hold_timestamp": datetime.now().isoformat()
        }
    )
    
    # 2. Create fraud investigation ticket
    create_fraud_investigation_ticket(
        transaction_id=transaction.transaction_id,
        fraud_score=transaction.fraud_score,
        risk_flags=transaction.risk_flags,
        priority="high"
    )
    
    # 3. Notify fraud team
    send_alert(
        alert_type="fraud_detected",
        severity="high",
        message=f"Transaction {transaction.transaction_id} flagged with fraud score {transaction.fraud_score}",
        metadata={"transaction_id": str(transaction.transaction_id)}
    )
    
    # 4. Request additional verification from user
    send_notification(
        user_id=transaction.payer_id,
        notification_type="additional_verification_required",
        title="Additional Verification Required",
        message=(
            "We need to verify your recent transaction. "
            "Please check your email for verification steps."
        )
    )
```

---

## 🔒 SECTION L — INVOICE GENERATION

### Invoice PDF Generation
```python
from reportlab.lib.pagesizes import A4
from reportlab.lib.units import inch
from reportlab.pdfgen import canvas
from datetime import datetime

def generate_invoice_pdf(invoice: Invoice) -> str:
    """
    Generate invoice PDF
    
    Returns:
        URL to generated PDF
    """
    
    # 1. Create PDF canvas
    pdf_filename = f"invoice_{invoice.invoice_number}.pdf"
    pdf_path = f"/tmp/{pdf_filename}"
    
    c = canvas.Canvas(pdf_path, pagesize=A4)
    width, height = A4
    
    # 2. Header
    c.setFont("Helvetica-Bold", 20)
    c.drawString(1*inch, height - 1*inch, "INVOICE")
    
    c.setFont("Helvetica", 10)
    c.drawString(1*inch, height - 1.3*inch, f"Invoice Number: {invoice.invoice_number}")
    c.drawString(1*inch, height - 1.5*inch, f"Invoice Date: {invoice.invoice_date.strftime('%d %b %Y')}")
    c.drawString(1*inch, height - 1.7*inch, f"Due Date: {invoice.due_date.strftime('%d %b %Y')}")
    
    # 3. Billed By (Antigravity)
    c.setFont("Helvetica-Bold", 12)
    c.drawString(1*inch, height - 2.2*inch, "Billed By:")
    c.setFont("Helvetica", 10)
    c.drawString(1*inch, height - 2.4*inch, invoice.billed_by_name)
    
    billed_by_address = invoice.billed_by_address
    c.drawString(1*inch, height - 2.6*inch, billed_by_address.get('line1', ''))
    c.drawString(1*inch, height - 2.8*inch, 
                 f"{billed_by_address.get('city', '')}, {billed_by_address.get('state', '')} {billed_by_address.get('pincode', '')}")
    
    if invoice.billed_by_gstin:
        c.drawString(1*inch, height - 3.0*inch, f"GSTIN: {invoice.billed_by_gstin}")
    
    # 4. Billed To
    c.setFont("Helvetica-Bold", 12)
    c.drawString(4.5*inch, height - 2.2*inch, "Billed To:")
    c.setFont("Helvetica", 10)
    c.drawString(4.5*inch, height - 2.4*inch, invoice.billed_to_name)
    c.drawString(4.5*inch, height - 2.6*inch, invoice.billed_to_email)
    
    # 5. Line Items Table
    y_position = height - 4*inch
    
    c.setFont("Helvetica-Bold", 10)
    c.drawString(1*inch, y_position, "Description")
    c.drawString(4*inch, y_position, "Quantity")
    c.drawString(5*inch, y_position, "Rate")
    c.drawString(6*inch, y_position, "Amount")
    
    c.line(1*inch, y_position - 0.1*inch, 7*inch, y_position - 0.1*inch)
    
    y_position -= 0.3*inch
    c.setFont("Helvetica", 10)
    
    for line_item in invoice.line_items:
        c.drawString(1*inch, y_position, line_item['description'])
        c.drawString(4*inch, y_position, str(line_item.get('quantity', 1)))
        c.drawString(5*inch, y_position, f"₹{line_item['rate_cents'] / 100:.2f}")
        c.drawString(6*inch, y_position, f"₹{line_item['amount_cents'] / 100:.2f}")
        y_position -= 0.2*inch
    
    # 6. Totals
    y_position -= 0.3*inch
    c.line(4.5*inch, y_position, 7*inch, y_position)
    
    y_position -= 0.3*inch
    c.drawString(4.5*inch, y_position, "Subtotal:")
    c.drawString(6*inch, y_position, f"₹{invoice.subtotal_cents / 100:.2f}")
    
    if invoice.cgst_cents:
        y_position -= 0.2*inch
        c.drawString(4.5*inch, y_position, "CGST:")
        c.drawString(6*inch, y_position, f"₹{invoice.cgst_cents / 100:.2f}")
    
    if invoice.sgst_cents:
        y_position -= 0.2*inch
        c.drawString(4.5*inch, y_position, "SGST:")
        c.drawString(6*inch, y_position, f"₹{invoice.sgst_cents / 100:.2f}")
    
    if invoice.igst_cents:
        y_position -= 0.2*inch
        c.drawString(4.5*inch, y_position, "IGST:")
        c.drawString(6*inch, y_position, f"₹{invoice.igst_cents / 100:.2f}")
    
    if invoice.discount_amount_cents:
        y_position -= 0.2*inch
        c.drawString(4.5*inch, y_position, "Discount:")
        c.drawString(6*inch, y_position, f"-₹{invoice.discount_amount_cents / 100:.2f}")
    
    y_position -= 0.3*inch
    c.setFont("Helvetica-Bold", 12)
    c.drawString(4.5*inch, y_position, "Total:")
    c.drawString(6*inch, y_position, f"₹{invoice.total_amount_cents / 100:.2f}")
    
    # 7. Payment Status
    y_position -= 0.5*inch
    c.setFont("Helvetica", 10)
    if invoice.payment_status == "paid":
        c.drawString(1*inch, y_position, f"Payment Status: PAID on {invoice.payment_date.strftime('%d %b %Y')}")
    else:
        c.drawString(1*inch, y_position, f"Payment Status: {invoice.payment_status.upper()}")
    
    # 8. Footer
    c.setFont("Helvetica", 8)
    c.drawString(1*inch, 1*inch, "Thank you for your business!")
    c.drawString(1*inch, 0.8*inch, "For queries, contact: billing@antigravity.com")
    
    # 9. Save PDF
    c.save()
    
    # 10. Upload to storage
    pdf_url = upload_to_storage(
        file_path=pdf_path,
        destination_path=f"invoices/{invoice.tenant_id}/{invoice.invoice_date.year}/{invoice.invoice_date.month}/{pdf_filename}"
    )
    
    # 11. Update invoice record
    update_invoice(
        invoice_id=invoice.invoice_id,
        pdf_url=pdf_url,
        pdf_generated_at=datetime.now()
    )
    
    return pdf_url
```

---

## 🔒 SECTION M — DEPLOYMENT & SECURITY

### Environment Variables
```bash
# Payment Gateways
RAZORPAY_API_KEY=<from_vault>
RAZORPAY_API_SECRET=<from_vault>
RAZORPAY_WEBHOOK_SECRET=<from_vault>

STRIPE_API_KEY=<from_vault>
STRIPE_WEBHOOK_SECRET=<from_vault>

PAYPAL_CLIENT_ID=<from_vault>
PAYPAL_CLIENT_SECRET=<from_vault>

# Database
DB_HOST=postgres.default.svc.cluster.local
DB_PORT=5432
DB_NAME=antigravity
DB_USER=payment_service
DB_PASSWORD=<from_vault>

# Fraud Detection
FRAUD_THRESHOLD=50.0
MAX_TRANSACTIONS_PER_HOUR=10

# Feature Flags
ENABLE_RAZORPAY=true
ENABLE_STRIPE=true
ENABLE_PAYPAL=false
ENABLE_FRAUD_DETECTION=true
ENABLE_REVENUE_SPLITS=true
ENABLE_ROYALTY_PAYMENTS=true
```

### PCI-DSS Compliance Checklist
```
CRITICAL REQUIREMENTS:

1. NEVER STORE CARD DATA
   ✓ Use payment gateway tokenization
   ✓ No card numbers in logs
   ✓ No CVV storage ever

2. SECURE TRANSMISSION
   ✓ TLS 1.2+ for all payment APIs
   ✓ Certificate pinning
   ✓ Webhook signature verification

3. ACCESS CONTROL
   ✓ Payment service isolated network segment
   ✓ Least privilege database access
   ✓ MFA for admin access

4. LOGGING & MONITORING
   ✓ All payment events logged
   ✓ PII redacted in logs
   ✓ Real-time fraud monitoring
   ✓ Anomaly detection alerts

5. AUDIT TRAIL
   ✓ Immutable financial logs
   ✓ 7-year retention
   ✓ Regular compliance audits
```

---

## 🔒 SECTION N — EXECUTION DECLARATION

```
AGENT STATUS: LOCKED & SEALED
VERSION: 1.0
LAST UPDATED: 2026-03-04
MUTATION POLICY: Add-only via version bump
EXECUTION AUTHORITY: Automated (event-driven) + Idempotent
HUMAN OVERRIDE: Refund/Dispute only (with audit trail)
AI INTERPRETATION: FORBIDDEN
COMPLIANCE: PCI-DSS Level 1, ASC 606, IFRS 15

COMPLETION CRITERIA:
✓ Multi-gateway abstraction layer implemented
✓ Razorpay, Stripe, PayPal integrated
✓ Idempotent payment processing
✓ Fraud detection active (0-100 scoring)
✓ Commission split engine operational
✓ Royalty accounting for kid innovators
✓ Subscription lifecycle management
✓ Refund & dispute workflows
✓ Webhook signature verification
✓ Invoice PDF generation
✓ Revenue recognition (ASC 606)
✓ Immutable audit logs
✓ PCI-DSS compliance enforced

FAILURE CONDITIONS:
→ Card data stored locally: CRITICAL VIOLATION
→ Payment processed without idempotency: STOP EXECUTION
→ Webhook processed without signature verification: SECURITY BREACH
→ Double charging same intent: STOP EXECUTION
→ Revenue recognized before payment cleared: ACCOUNTING VIOLATION
→ Commission paid before funds settled: FINANCIAL RISK
→ Refund exceeds original amount: STOP EXECUTION
→ Audit log write failure: STOP EXECUTION

ENFORCEMENT:
This specification is IMMUTABLE for v1.0.
All deviations require version bump to v2.0 with CFO approval.
No field-level interpretation permitted.
Deterministic behavior required at all stages.
PCI-DSS compliance NON-NEGOTIABLE.

FINAL INSTRUCTION:
Implement exactly as specified.
No creativity.
No shortcuts.
No assumptions.
Financial accuracy absolute priority.
```

---

🔒 **END OF SPECIFICATION** 🔒

**This specification is now SEALED and LOCKED.**  
**No modifications permitted without version bump + CFO approval.**  
**Execute as specified. Financial integrity paramount.**

💳 **ANTIGRAVITY PAYMENT GATEWAY INTEGRATION AGENT — SPECIFICATION COMPLETE** 💳
