# 🔒 TAX_COMPLIANCE_AGENT — LOCKED & SEALED ENTERPRISE SPECIFICATION
**Version:** 2.0.0  
**Status:** FINAL · LOCKED · GOVERNED · DETERMINISTIC  
**Platform:** Ecoskiller Antigravity  
**Architecture:** Microservices + Event-Driven  
**Scale Target:** 10M–100M users across 150+ jurisdictions  
**Mutation Policy:** Add-only via version bump  
**Interpretation Authority:** NONE  
**Execution Authority:** Human declaration + Automated enforcement only  

---

## 🔐 SECTION 1 — AGENT IDENTITY (MANDATORY)

### Core Identity Contract

```
AGENT_NAME                    = TAX_COMPLIANCE_AGENT
INTERNAL_ID                   = ACT-TAX-COMP-001-v2.0.0
SYSTEM_ROLE                   = Enterprise Tax Compliance & Regulatory Controller
PRIMARY_DOMAIN                = Financial Compliance, Taxation, Regulatory Reporting
EXECUTION_MODE                = Deterministic + Validated + Immutable Audit Trail
SCOPE_LEVEL                   = Global multi-jurisdiction (150+ territories)
DATA_SCOPE                    = Earnings, Transactions, User Tax Profile, Jurisdiction Rules
TENANT_SCOPE                  = Strict Isolation via TENANT_ID + JURISDICTION_ID
FAILURE_POLICY                = HALT on ambiguity → LOG_INCIDENT → ESCALATE_TO=COMPLIANCE_OFFICER
SAFETY_OWNER                  = Tax Compliance Officer (Human Final Authority)
INTELLIGENCE_OWNER            = Compliance Intelligence Lead
ML_OWNER                      = Compliance ML Engineer
DEPLOYMENT_ENV                = PRODUCTION + STAGING (no DEV execution on real tax data)
COMPLIANCE_FRAMEWORK          = SOX 404 + GDPR + eIDAS + GST/VAT/CIT Rules + State Tax Laws
```

---

## 📋 SECTION 2 — PURPOSE DECLARATION

### Problem Statement

Ecoskiller operates in 150+ tax jurisdictions worldwide. Each jurisdiction has unique rules for:
- **Income classification** (wages, freelance, capital gains, royalties)
- **Tax rate determination** (progressive, flat, sectoral)
- **Reporting obligations** (annual, quarterly, event-triggered)
- **Withholding requirements** (TDS, 1099, VAT reverse charge)
- **Audit trails** (immutable, tamper-proof records)

**Current Risk:** Without deterministic tax compliance, platform faces:
- Tax evasion liability
- Regulatory fines (10–50% of tax amount)
- De-platforming in key markets
- User data breaches in tax filings
- Reputation damage

### Solution: TAX_COMPLIANCE_AGENT

The TAX_COMPLIANCE_AGENT ensures:

1. **Real-time transaction classification** → Instant tax categorization
2. **Jurisdiction-aware tax calculation** → Correct withholding at source
3. **Automated reporting generation** → Quarterly/annual compliance reports
4. **Audit trail immutability** → 7-year regulatory retention
5. **Risk detection & escalation** → ML-based anomaly flagging
6. **Regulatory update ingestion** → Daily tax law synchronization
7. **User tax profile management** → TIN, residency, exemption status

### Input Specification

**Primary Input:** `TRANSACTION_EVENT`

```json
{
  "transaction_id": "TXN-UUID-v4",
  "timestamp_utc": "2026-02-25T14:30:45Z",
  "actor_id": "USER-UUID",
  "actor_jurisdiction_code": "US-CA",
  "transaction_type": "EARNINGS_FREELANCE_SERVICES",
  "gross_amount_usd": 5000.00,
  "currency_code": "USD",
  "payer_jurisdiction_code": "US-TX",
  "payer_type": "BUSINESS",
  "service_category": "DESIGN_SERVICES",
  "platform_fee_percentage": 15.0,
  "payment_method": "BANK_TRANSFER",
  "is_international": false,
  "tax_exemption_codes": ["EXEMPTION_501C3"],
  "metadata": {
    "invoice_id": "INV-2026-001",
    "project_id": "PROJ-UUID",
    "milestone_id": "MS-UUID"
  }
}
```

**Secondary Input:** `TAX_PROFILE_UPDATE`

```json
{
  "user_id": "USER-UUID",
  "tax_jurisdiction": "IN",
  "tin_type": "PAN",
  "tin_masked": "ABCDE****XYZ",
  "residency_status": "TAX_RESIDENT",
  "residency_days_current_fy": 183,
  "tax_filing_status": "INDIVIDUAL",
  "exemption_categories": ["STARTUP_DEDUCTION", "RESEARCH_CREDIT"],
  "gst_registered": true,
  "gst_number_masked": "AABCU****XYZ",
  "alternate_tax_ids": ["SSID-US-123456789", "VATIN-EU-123456789"],
  "tax_calculation_method": "STANDARD",
  "update_timestamp": "2026-02-25T10:00:00Z"
}
```

### Output Specification

**Primary Output:** `TAX_CLASSIFICATION_EVENT`

```json
{
  "transaction_id": "TXN-UUID-v4",
  "classification_id": "CLASS-UUID-v4",
  "timestamp_utc": "2026-02-25T14:30:47Z",
  "actor_id": "USER-UUID",
  "tax_jurisdiction": "US-CA",
  "income_type": "SELF_EMPLOYMENT_INCOME",
  "tax_treatment": {
    "gross_income": 5000.00,
    "platform_fee": 750.00,
    "taxable_income_before_adjustments": 4250.00,
    "applicable_rate": 0.24,
    "standard_deduction_eligible": true,
    "withholdable_amount": 637.50,
    "filing_requirement": "REQUIRED"
  },
  "withholding_info": {
    "withholding_type": "NO_WITHHOLDING",
    "withholding_amount": 0.00,
    "withholding_reason": "THRESHOLD_NOT_MET",
    "next_trigger_threshold": 600.00
  },
  "reporting_requirements": [
    {
      "form_type": "1040_SCHEDULE_C",
      "jurisdiction": "US",
      "due_date": "2027-04-15",
      "is_mandatory": true
    },
    {
      "form_type": "CA_540_SCHEDULE_CA",
      "jurisdiction": "US-CA",
      "due_date": "2027-05-15",
      "is_mandatory": true
    }
  ],
  "flags": [
    {
      "flag_code": "INTERNATIONAL_PAYMENT",
      "severity": "INFO",
      "message": "Cross-border transaction detected. FATCA reporting may apply."
    }
  ],
  "confidence_score": 0.98,
  "model_version": "TAX-CLASSIFIER-ML-v3.2.1",
  "model_training_data_cutoff": "2026-02-01",
  "audit_reference": "AUDIT-UUID-v4",
  "next_trigger_events": [
    "QUARTERLY_TAX_REPORT_DUE",
    "ANNUAL_TAX_FILING_DEADLINE",
    "ESTIMATED_TAX_PAYMENT_DUE"
  ],
  "decision_path": [
    "STEP_1: Income classification via INCOME_CLASSIFIER_ML",
    "STEP_2: Jurisdiction rule lookup via JURISDICTION_DB",
    "STEP_3: Tax rate calculation via TAX_RATE_ENGINE",
    "STEP_4: Withholding threshold check via WITHHOLDING_RULES",
    "STEP_5: Reporting requirement mapping via FORM_REGISTRY",
    "STEP_6: Risk flag assessment via ANOMALY_DETECTOR_ML"
  ]
}
```

---

## 🧪 SECTION 3 — INPUT CONTRACT (STRICT)

### Input Schema Validation

```yaml
INPUT_SCHEMA:
  required_fields:
    - transaction_id
    - timestamp_utc
    - actor_id
    - actor_jurisdiction_code
    - transaction_type
    - gross_amount_usd
    - currency_code
    - payer_jurisdiction_code
    - payer_type
  
  optional_fields:
    - tax_exemption_codes
    - service_category
    - platform_fee_percentage
    - metadata
    - invoice_id
    - project_reference

  validation_rules:
    - transaction_id: MUST be UUID-v4, MUST be unique across platform history
    - timestamp_utc: MUST be ISO-8601 UTC, MUST NOT be future-dated
    - actor_id: MUST match existing USER_ID in identity store, MUST have tax profile
    - gross_amount_usd: MUST be > 0, MUST have ≤ 2 decimal places
    - currency_code: MUST be valid ISO-4217 code
    - jurisdiction_code: MUST match JURISDICTION_REGISTRY (ISO-3166-2 format)
    - transaction_type: MUST match TRANSACTION_TYPE_ENUM
    - payer_type: MUST be one of [INDIVIDUAL, BUSINESS, GOVERNMENT, NGO, CORPORATION]

  domain_checks:
    - actor_jurisdiction_code MUST be in ACTIVE_JURISDICTIONS_LIST for platform
    - payer_jurisdiction_code MUST be valid ISO code or special code (XX-DIGITAL_NOMAD)
    - transaction_type MUST have corresponding tax treatment rule in TAX_RULE_ENGINE
    - If is_international=true: payer_jurisdiction MUST differ from actor_jurisdiction

  security_checks:
    - actor_id MUST have non-null tax_profile_id
    - actor MUST have verified_identity=true
    - actor MUST NOT have tax_compliance_hold=true
    - payer MUST NOT be on SANCTIONS_LIST
    - payer MUST NOT have fraud_flag=true in last 12 months
    - transaction MUST NOT duplicate recent transaction (within 1 minute)
```

### Validation Failure Policy

```
If required field MISSING:
  → REJECT
  → LOG: InputValidationError
  → STATUS: HALTED
  → ESCALATE_TO: COMPLIANCE_OFFICER
  → RESPONSE_CODE: 400_MISSING_FIELD

If data MALFORMED:
  → REJECT
  → LOG: InputFormatError
  → Parse attempt FAILED
  → ESCALATE_TO: COMPLIANCE_OFFICER
  → RESPONSE_CODE: 422_UNPROCESSABLE

If domain check FAILS:
  → REJECT
  → LOG: DomainValidationError
  → ESCALATE_TO: COMPLIANCE_OFFICER
  → RESPONSE_CODE: 403_INVALID_DOMAIN

If security check FAILS:
  → REJECT
  → LOG: SecurityValidationError
  → ESCALATE_TO: SECURITY_OFFICER
  → RESPONSE_CODE: 401_UNAUTHORIZED

NO SILENT FAILURES
NO PARTIAL PROCESSING
```

---

## 🎯 SECTION 4 — OUTPUT CONTRACT (STRICT)

### Output Schema Specification

All outputs MUST include:

```yaml
OUTPUT_SCHEMA:
  result_object:
    - transaction_id: UUID-v4 (traced to input)
    - classification_id: UUID-v4 (unique)
    - timestamp_utc: ISO-8601 UTC (when classification occurred)
    - tax_jurisdiction: ISO-3166-2 (where tax applies)
    - income_type: ENUM (predefined tax income types)
    - tax_treatment: OBJECT
      - gross_income: numeric
      - taxable_income: numeric
      - applicable_rate: float (0.0–1.0)
      - withholding_amount: numeric
    - reporting_requirements: ARRAY[OBJECT]
      - form_type: string
      - jurisdiction: string
      - due_date: ISO-8601 date
      - is_mandatory: boolean
    - flags: ARRAY[OBJECT]
      - flag_code: string
      - severity: ENUM [INFO, WARNING, CRITICAL]
      - message: string
  
  confidence_score:
    type: float (0.0–1.0)
    rule: |
      0.95–1.0   = Deterministic rule (100% confidence)
      0.80–0.94  = ML-predicted with high accuracy
      0.60–0.79  = ML-predicted, needs verification
      <0.60      = HALT, escalate to human
  
  model_version:
    format: "TAX-{CLASSIFIER-ID}-ML-v{MAJOR}.{MINOR}.{PATCH}"
    rule: MUST match trained model deployment version
  
  model_training_data_cutoff:
    format: "YYYY-MM-DD"
    rule: Training data MUST NOT be older than 90 days
  
  audit_reference:
    format: "AUDIT-UUID-v4"
    rule: Links to immutable audit log entry
  
  next_trigger_events:
    type: ARRAY[string]
    rule: List of downstream events this classification triggers
```

### Output Immutability Rules

```
Once OUTPUT is generated and logged:
  ✗ CANNOT be modified
  ✗ CANNOT be deleted
  ✗ CANNOT be overwritten
  ✓ CAN be amended only via NEW OUTPUT record
  ✓ Amendment MUST reference original classification_id
  ✓ Amendment MUST include reason_for_change code
  ✓ Amendment MUST be independently audited
```

---

## 🤖 SECTION 5 — ML / AI LOGIC LAYER

### 5.1 ML Components (70% of Logic)

#### Income Classifier ML (MODEL_ID: TAX-CLASSIFIER-ML-v3.2.1)

**Purpose:** Classify transaction into tax income category

```
MODEL_TYPE: Multi-class Classification (Ensemble)
  - Primary: Gradient Boosted Trees (XGBoost)
  - Secondary: Random Forest
  - Tertiary: Neural Network (3-layer)
  - Ensemble Method: Weighted voting (0.5, 0.3, 0.2)

TRAINING_APPROACH:
  - Dataset: 10M+ historical transactions (2020–2026)
  - Class balance: Stratified k-fold (k=5)
  - Feature engineering: Domain-expert validated
  - Class weights: Balanced across 25 income categories

FEATURES_USED:
  [1] transaction_amount_bin (log-scaled)
  [2] payer_type_encoded
  [3] payer_industry_classification
  [4] actor_jurisdiction_risk_score
  [5] platform_category_code
  [6] service_category_encoding
  [7] payment_method_type
  [8] temporal_feature: day_of_year (seasonal)
  [9] temporal_feature: is_tax_season (boolean)
  [10] actor_history_income_types_distribution (vector)
  [11] actor_historical_average_transaction_size
  [12] actor_tax_filing_history_count
  [13] actor_exemption_status (encoded)
  [14] payer_reputation_score
  [15] transaction_frequency_last_30_days
  [16] international_transaction_flag
  [17] currency_volatility_index (last 7 days)
  [18] jurisdiction_pair_common_rate (how frequent this jurisdiction pair)
```

**Model Accuracy Metrics:**

```
Overall Accuracy:           94.7%
Precision (macro):          91.2%
Recall (macro):             90.8%
F1-Score (macro):           91.0%

By Income Class:
  - WAGES:                  96.5% precision, 95.2% recall
  - SELF_EMPLOYMENT:        93.4% precision, 92.1% recall
  - CAPITAL_GAINS:          87.6% precision, 86.3% recall
  - ROYALTIES:              89.2% precision, 88.7% recall
  - DIVIDENDS:              91.3% precision, 90.1% recall
  - CRYPTO_INCOME:          78.9% precision, 77.2% recall ⚠️ (lower confidence)
```

**Training Schedule:**

```
Frequency:               Weekly (every Monday, 2:00 UTC)
Retraining Window:       Last 90 days of transactions
Drift Detection:         Daily (automated)
Validation Split:        80/20 (training/validation)
Test Set:                Hold-out set (10M transactions, never seen)
```

**Drift Detection Policy:**

```
Monitor Daily:
  [1] Prediction accuracy on latest 10K transactions
  [2] Feature distribution shift (Kolmogorov-Smirnov test)
  [3] Class imbalance changes (>10% shift)
  [4] Confidence score histogram changes

Trigger Retraining If:
  [1] Accuracy drops >2% from baseline (94.7%)
  [2] Feature drift detected (p-value < 0.05)
  [3] Class distribution shifts >15%
  [4] Confidence score < 0.75 for >5% of predictions

Alert Hierarchy:
  INFO:     Drift <1%           → Log only
  WARNING:  Drift 1–5%          → Email compliance team
  CRITICAL: Drift >5%           → SMS + Call + Halt new model deployment
```

---

#### Tax Rate Engine ML (MODEL_ID: TAX-RATE-CALC-ML-v2.1.0)

**Purpose:** Calculate applicable tax rate based on jurisdiction + income class + residency

```
MODEL_TYPE: Regression with Rule-Based Fallback
  - Primary: Gradient Boosting (CatBoost)
  - Fallback: Deterministic rule lookup (jurisdiction_id + income_type)

FEATURES_USED:
  [1] income_type (classified via INCOME_CLASSIFIER_ML)
  [2] jurisdiction_code (ISO-3166-2)
  [3] actor_tax_residency_status
  [4] actor_age (if available, else "UNKNOWN")
  [5] actor_spouse_income_status (if filing status requires)
  [6] actor_dependent_count
  [7] actor_retirement_plan_contributions_ytd
  [8] actor_property_ownership_status
  [9] prior_year_income_total
  [10] current_fy_income_ytd
  [11] applicable_deductions_ytd
  [12] tax_filing_year (2026, 2027, etc.)
  [13] jurisdiction_tax_rate_effective_date
  [14] inflation_adjustment_factor
```

**Model Behavior:**

```
INPUT: transaction_id, income_type, jurisdiction, residency, ytd_income
PROCESS:
  1. Look up income_type in DETERMINISTIC_TAX_RULES (DB)
  2. If rule found and confidence=DETERMINISTIC
     → Use rule-based rate (100% confidence)
  3. Else:
     → Input features to ML model
     → Predict tax rate + bounds
     → Return with confidence interval
OUTPUT: {tax_rate: float, is_deterministic: bool, confidence: float}
```

**Rule Database Structure:**

```sql
CREATE TABLE tax_rate_rules (
  jurisdiction_code VARCHAR(10),
  income_type VARCHAR(50),
  fy_year INT,
  rule_type ENUM ('PROGRESSIVE', 'FLAT', 'SECTORAL'),
  
  -- For PROGRESSIVE: defined brackets
  brackets JSONB, -- [{min: 0, max: 50000, rate: 0.10}, ...]
  
  -- For FLAT: single rate
  flat_rate DECIMAL(5,4),
  
  -- For SECTORAL: rate varies by sector
  sector_mapping JSONB,
  
  effective_date DATE,
  expiry_date DATE,
  source_reference VARCHAR(500), -- Official gazette/law link
  last_updated TIMESTAMP,
  audit_reference UUID,
  
  CONSTRAINT jurisdiction_income_fy_unique 
    UNIQUE (jurisdiction_code, income_type, fy_year)
);
```

---

#### Anomaly Detector ML (MODEL_ID: ANOMALY-RISK-ML-v1.8.0)

**Purpose:** Detect tax evasion risk, fraud indicators, unusual patterns

```
MODEL_TYPE: Isolation Forest + LSTM (temporal)
  - Primary: Isolation Forest (XGBoost based)
  - Secondary: LSTM for temporal sequences
  - Ensemble: Majority voting

FEATURES_USED:
  [1] transaction_amount_zscore (within actor's history)
  [2] actor_frequency_anomaly (transactions per day)
  [3] payer_risk_score
  [4] jurisdiction_tax_evasion_index (external data)
  [5] income_category_consistency (vs. historical)
  [6] withholding_pattern_deviation
  [7] reporting_compliance_score (actor's history)
  [8] cash_intensive_flag (high-risk sectors)
  [9] cross_border_transaction_intensity
  [10] temporal_clustering (multiple txns same minute)
  [11] invoice_timestamp_gap_anomaly
  [12] actor_previous_audit_history
  [13] amount_just_below_threshold (e.g., 599 vs 600)
```

**Risk Score Interpretation:**

```
Risk Score:  0.0–0.3   = GREEN   (Normal)
Risk Score:  0.3–0.6   = YELLOW  (Review recommended)
Risk Score:  0.6–0.85  = RED     (Flag for compliance team)
Risk Score:  0.85–1.0  = CRITICAL (Immediate escalation)

If Risk Score >= 0.6:
  → Auto-generate FLAG in TAX_CLASSIFICATION_EVENT
  → Log to COMPLIANCE_REVIEW_QUEUE
  → Notify COMPLIANCE_OFFICER (async)
  → DO NOT HALT transaction (user can proceed, but flagged)
```

---

### 5.2 Intelligence / AI Components (20% of Logic)

#### Jurisdiction Rule Engine (Deterministic)

**Purpose:** Apply jurisdiction-specific tax rules with no ML uncertainty

```
COMPONENT_TYPE: Expert System (Rule-Based)

FUNCTION:
  INPUT:
    - jurisdiction_code (ISO-3166-2)
    - income_type (from INCOME_CLASSIFIER_ML)
    - transaction_details (amount, date, payer_type)
  
  PROCESS:
    1. Load jurisdiction profile from DB
    2. Match income_type to applicable rules
    3. Check for special conditions (e.g., residency days, exemptions)
    4. Calculate tax obligations
    5. Return with DETERMINISTIC confidence
  
  OUTPUT:
    {
      withholding_amount: numeric,
      reporting_forms: [FORM_TYPE, ...],
      filing_deadline: ISO-8601 date,
      confidence: 1.0 (always deterministic)
    }

SAMPLE RULES:
  [US-CA] + [SELF_EMPLOYMENT_INCOME]:
    - Tax rate: Progressive brackets (10%–13.3%)
    - Min reporting threshold: $400 annual
    - Withholding: Not applicable
    - Form 1099-NEC: If payer paid >$600 annual
    - California 540: Mandatory for residents
  
  [IN] + [SELF_EMPLOYMENT_INCOME]:
    - GST 5% if income >40,00,000 annually
    - Income tax progressive (0%–30%)
    - TDS 10% if single transaction >20,000
    - Form 26AS: Required for all
    - ITR-3: Mandatory filing
  
  [EU-DE] + [FREELANCE_SERVICES]:
    - VAT 19% (reverse charge if B2B)
    - Withholding tax: 20% for non-residents
    - Invoice requirement: Mandatory
    - Quarterly VAT filing: Required if VAT registered
```

---

#### Regulatory Update Ingestion Engine

**Purpose:** Continuously ingest and apply tax law changes

```
COMPONENT_TYPE: Autonomous Update System

UPDATE_FREQUENCY:
  - Tax rate changes:     Daily (automated scanning)
  - Filing deadline changes: Monthly
  - New regulations:      Weekly digest
  - Emergency updates:    Real-time

DATA_SOURCES:
  [1] Official government tax authority APIs (IRS, HMRC, SARS, etc.)
  [2] Third-party tax data providers (Thomson Reuters, Tax Foundation)
  [3] Platform compliance team manual inputs (with audit trail)
  [4] Regulatory change notifications (email digests)

INGESTION PIPELINE:
  1. Fetch updates from sources
  2. Parse using natural language processing
  3. Extract jurisdiction, effective_date, rule_change
  4. Store in TAX_RATE_RULES table (versioned)
  5. Trigger MODEL_RETRAINING if significant change
  6. Notify compliance team
  7. Log in AUDIT_TRAIL (immutable)

CHANGE_LOG_SCHEMA:
  {
    change_id: UUID,
    jurisdiction_code: VARCHAR,
    rule_section: VARCHAR,
    old_rule: TEXT,
    new_rule: TEXT,
    effective_date: DATE,
    source_url: VARCHAR,
    ingestion_timestamp: TIMESTAMP,
    compliance_team_review: BOOLEAN,
    is_active: BOOLEAN,
    audit_reference: UUID
  }
```

---

### 5.3 Safety & Validation Layer

#### Confidence Threshold Logic

```
If ML confidence >= 0.95:
  → Process automatically (DETERMINISTIC)
  → Log with high confidence
  → No human review required
  → Proceed to output

If ML confidence 0.80–0.94:
  → Process with review flag
  → Human review recommended (async)
  → Add to COMPLIANCE_REVIEW_QUEUE
  → Proceed to output but flag

If ML confidence 0.60–0.79:
  → Add to HIGH_PRIORITY_REVIEW_QUEUE
  → Do not process until human approves
  → Return 202_ACCEPTED (pending)
  → Notify compliance team

If ML confidence < 0.60:
  → HALT processing
  → Escalate to COMPLIANCE_OFFICER (immediate)
  → Return 503_COMPLIANCE_UNAVAILABLE
  → Create incident log
```

---

## ⚙️ SECTION 6 — SCALABILITY DESIGN

### Performance Requirements

```
EXPECTED_RPS:           100,000 (peak global)
EXPECTED_LATENCY_P99:   500ms (classification)
EXPECTED_LATENCY_P95:   300ms (classification)
MAX_CONCURRENCY:        10,000 simultaneous transactions
QUEUE_STRATEGY:         Event-driven (Kafka) + async processing

THROUGHPUT_SLA:
  - Transaction ingestion: 100K/sec
  - Classification completion: 99% within 500ms
  - Report generation: 1000 reports/sec
  - Audit log writes: 500K/sec
```

### Horizontal Scaling Architecture

```
DEPLOYMENT_TOPOLOGY:
  
  [1] API Gateway (Load balanced)
      ├─ Rate limiter: 10K req/sec per tenant
      ├─ Request validation: 50μs
      └─ Routing: 100μs

  [2] Transaction Ingestion Service (Replicas: 50)
      ├─ Kafka consumer group: 50 partitions
      ├─ Batch size: 1000 transactions
      ├─ Input validation: Parallel
      └─ Event emission: 100K/sec

  [3] Classification Service (Replicas: 200)
      ├─ ML model replicas: 20 (load-balanced)
      ├─ Rule engine replicas: 50
      ├─ Processing pool: CPU-bound
      └─ Cache: Redis (hot models)

  [4] Reporting Service (Replicas: 30)
      ├─ Report template engine
      ├─ PDF generation (async)
      ├─ Archive to S3 (cold storage)
      └─ Queue: RabbitMQ

  [5] Audit Logger (Replicas: 100)
      ├─ Write-optimized database (TimescaleDB)
      ├─ Replication factor: 3
      ├─ Compaction: Monthly
      └─ Retention: 7 years (immutable)

STATELESS_EXECUTION:
  - All services are stateless
  - State stored in PostgreSQL (primary) + TimescaleDB (audit)
  - Cache layer: Redis (session-agnostic)
  - No in-memory state across requests
```

### Idempotent Operations

```
IDEMPOTENCY_KEY: transaction_id + operation_type + timestamp_bucket (5 min)

GUARANTEE:
  If same request received twice within 5 minutes:
    → Return identical response
    → Do not process second request
    → Deduplicate via IDEMPOTENCY_KEY index

IMPLEMENTATION:
  CREATE INDEX idx_idempotency 
    ON classification_results (idempotency_key, created_at)
    WHERE is_processed = true;
  
  BEFORE INSERT:
    SELECT * FROM classification_results 
    WHERE idempotency_key = NEW.idempotency_key
    AND created_at > NOW() - INTERVAL 5 MINUTES;
    
    IF found:
      → Return cached result (201_CREATED)
    ELSE:
      → Process and insert (201_CREATED)
```

---

## 🔐 SECTION 7 — SECURITY ENFORCEMENT

### Tenant Isolation (Mandatory)

```
TENANT_ISOLATION_RULES:
  1. All queries MUST include WHERE tenant_id = ?
  2. Row-level security (RLS) enabled on all tables
  3. No cross-tenant data joins allowed
  4. No global aggregations on user data
  5. Encryption: TDE (Transparent Data Encryption) at rest
  6. Encryption: TLS 1.3 in transit
  7. Secrets: HashiCorp Vault (rotated 90 days)

SQL_ENFORCEMENT:
  CREATE POLICY tenant_isolation ON transactions
    USING (tenant_id = current_user_tenant_id());
  
  CREATE POLICY tenant_isolation ON audit_logs
    USING (tenant_id = current_user_tenant_id());
  
  ALTER TABLE transactions ENABLE ROW LEVEL SECURITY;
  ALTER TABLE audit_logs ENABLE ROW LEVEL SECURITY;
```

### Domain Isolation

```
DOMAIN_ISOLATION_RULES:
  - Tax compliance data CANNOT leak to user-facing services
  - Compliance reports in separate schema: compliance.*
  - User data in main schema: users.*
  - No foreign keys between schemas
  - ETL via message queue only (async, logged)

DATA_SEGREGATION:
  Schema 1: public.* (user-facing, non-sensitive)
  Schema 2: compliance.* (tax, regulatory, sensitive)
  Schema 3: audit.* (immutable logs, 7-year retention)
  
  No direct queries between schemas.
  All access via services with explicit authorization.
```

### Role-Based Authorization (RBAC)

```
ROLES:
  1. COMPLIANCE_OFFICER
     - Read: All transactions, reports, flags
     - Write: Exemption approvals, rule overrides
     - Delete: NONE (audit-protected)
  
  2. COMPLIANCE_ML_ENGINEER
     - Read: Anonymized transaction data (for training)
     - Write: Model versioning, hyperparameters
     - Delete: NONE
  
  3. TAX_ANALYST
     - Read: Transactions for assigned jurisdictions only
     - Write: Notes, follow-ups on flagged items
     - Delete: NONE
  
  4. PLATFORM_ADMIN
     - Read: All (if required by law)
     - Write: Limited (via audit trail)
     - Delete: NONE
  
  5. AUTOMATE_SERVICE (Agent Itself)
     - Read: Transactions, profiles, rules
     - Write: Classification results, audit logs
     - Delete: NONE (immutable)

ENFORCED_AT:
  - Database level (PostgreSQL GRANT)
  - API level (JWT claims)
  - Audit trail level (all writes logged)
```

### Audit Logging (Append-Only)

```
AUDIT_LOG_SCHEMA:
  CREATE TABLE audit_logs (
    audit_id UUID PRIMARY KEY,
    timestamp_utc TIMESTAMP NOT NULL,
    actor_id UUID NOT NULL,
    actor_role VARCHAR(50) NOT NULL,
    action_type VARCHAR(50) NOT NULL,
    resource_type VARCHAR(50) NOT NULL,
    resource_id UUID NOT NULL,
    old_value JSONB,
    new_value JSONB,
    ip_address INET,
    user_agent VARCHAR(500),
    status ENUM ('SUCCESS', 'FAILURE'),
    error_message TEXT,
    tenant_id UUID NOT NULL,
    jurisdiction_id VARCHAR(10),
    
    CONSTRAINT no_update CHECK (true)
  );
  
  CREATE TRIGGER audit_immutability
    BEFORE UPDATE ON audit_logs
    FOR EACH ROW EXECUTE FUNCTION prevent_update();
  
  CREATE TRIGGER audit_immutability_delete
    BEFORE DELETE ON audit_logs
    FOR EACH ROW EXECUTE FUNCTION prevent_delete();

RETENTION_POLICY:
  - Keep: 7 years (regulatory requirement)
  - Archive: After 2 years (move to S3 Glacier)
  - Delete: Never (legally required to retain)
```

### Data Encryption

```
AT_REST:
  - Algorithm: AES-256-GCM
  - Mode: Transparent Data Encryption (TDE)
  - Key management: AWS KMS (rotation every 90 days)
  - Database: PostgreSQL with pgcrypto extension

IN_TRANSIT:
  - Protocol: TLS 1.3 only
  - Certificate: AWS Certificate Manager (auto-renewed)
  - Cipher suites: 
      TLS_AES_256_GCM_SHA384
      TLS_CHACHA20_POLY1305_SHA256
  - HSTS: 1 year (preloaded)

SENSITIVE_FIELDS:
  - tin (Tax ID Number): Encrypted, hashed, masked in logs
  - ssn (Social Security Number): Encrypted, never logged
  - bank_account: Encrypted, tokenized
  - pii: Encrypted via field-level encryption
```

---

## 📊 SECTION 8 — AUDIT & TRACEABILITY

### Complete Audit Trail

Every TAX_COMPLIANCE_AGENT execution MUST log:

```json
{
  "audit_id": "AUDIT-UUID-v4",
  "timestamp_utc": "2026-02-25T14:30:47Z",
  "transaction_id": "TXN-UUID-v4",
  "actor_id": "USER-UUID",
  "actor_role": "SYSTEM_AGENT",
  "action_type": "TAX_CLASSIFICATION",
  "resource_type": "TRANSACTION",
  "resource_id": "TXN-UUID-v4",
  
  "input_hash": "SHA256:abc123...",
  "output_hash": "SHA256:def456...",
  
  "model_version": "TAX-CLASSIFIER-ML-v3.2.1",
  "model_execution_time_ms": 245,
  "confidence_score": 0.98,
  
  "decision_path": [
    "STEP_1: Input validation [✓]",
    "STEP_2: Feature extraction [✓]",
    "STEP_3: ML inference [✓]",
    "STEP_4: Rule lookup [✓]",
    "STEP_5: Anomaly detection [⚠ FLAG_ISSUED]",
    "STEP_6: Output generation [✓]"
  ],
  
  "flags_issued": [
    {
      "flag_id": "FLAG-UUID",
      "flag_code": "INTERNATIONAL_PAYMENT",
      "severity": "INFO"
    }
  ],
  
  "anomaly_score": 0.45,
  "anomaly_details": {
    "outlier_indicators": [
      "transaction_amount_1.8x_above_actor_average"
    ]
  },
  
  "performance_metrics": {
    "request_processing_time_ms": 245,
    "database_query_time_ms": 78,
    "model_inference_time_ms": 156,
    "rule_engine_time_ms": 11
  },
  
  "downstream_events": [
    "QUARTERLY_REPORT_QUEUE_TRIGGERED",
    "COMPLIANCE_REVIEW_ITEM_CREATED"
  ],
  
  "status": "SUCCESS",
  "tenant_id": "TENANT-UUID",
  "jurisdiction_id": "US-CA",
  
  "immutable": true,
  "retention_years": 7,
  "archived": false
}
```

### Audit Trail Immutability Guarantee

```
Audit logs are WRITE-ONCE, READ-MANY:
  ✓ Initial write: Allowed (AGENT execution)
  ✗ Update: Blocked (database trigger)
  ✗ Delete: Blocked (database trigger)
  ✓ Read: Unrestricted (authorized users only)
  ✓ Archive: Allowed (to S3 Glacier after 2 years)

TECHNICAL_ENFORCEMENT:
  1. Database-level constraints
  2. Application-level validation
  3. Cryptographic hash verification
  4. Blockchain-style chain-of-custody (optional, high-compliance orgs)
```

---

## 🚨 SECTION 9 — FAILURE POLICY

### Failure Mode Handling

#### Scenario 1: Invalid Input

```
TRIGGER: Missing required field (e.g., actor_jurisdiction_code)

RESPONSE:
  1. Reject input (DO_NOT_PROCESS)
  2. Log incident:
     {
       "incident_type": "INPUT_VALIDATION_FAILURE",
       "missing_field": "actor_jurisdiction_code",
       "timestamp": "2026-02-25T14:30:45Z",
       "transaction_id": "TXN-UUID"
     }
  3. Send response:
     HTTP 400 Bad Request
     {
       "error_code": "MISSING_REQUIRED_FIELD",
       "message": "Field 'actor_jurisdiction_code' is required",
       "resolution": "Provide valid ISO-3166-2 jurisdiction code"
     }
  4. Escalate to: COMPLIANCE_OFFICER (async email)
  5. Retry policy: No automatic retry (user must resubmit)
```

#### Scenario 2: Model Unavailable

```
TRIGGER: ML model service down or unresponsive

RESPONSE:
  1. Timeout: 5 seconds (circuit breaker)
  2. Fallback: Use deterministic rule engine only
  3. Confidence: Set to 0.75 (degraded mode)
  4. Log incident:
     {
       "incident_type": "MODEL_UNAVAILABLE",
       "model_id": "TAX-CLASSIFIER-ML-v3.2.1",
       "error": "Connection timeout after 5s",
       "fallback_mode": true
     }
  5. Send response: 200 OK (process with rule engine)
  6. Escalate to: ML_ENGINEERING_TEAM (async)
  7. Retry policy: Retry 3 times (exponential backoff)
  8. If all retries fail: Escalate to COMPLIANCE_OFFICER (SMS)
```

#### Scenario 3: Database Connection Failure

```
TRIGGER: Cannot connect to PostgreSQL

RESPONSE:
  1. Retry: 3 attempts, exponential backoff (1s, 2s, 4s)
  2. Queue to Kafka: Store in transaction queue (buffered)
  3. Respond to user: 202 ACCEPTED (will process when DB recovers)
  4. Log incident
  5. Escalate to: INFRASTRUCTURE_TEAM (Slack + SMS)
  6. SLA: Recover within 5 minutes
  7. If unrecovered: Escalate to COMPLIANCE_OFFICER
```

#### Scenario 4: Confidence Below Threshold

```
TRIGGER: ML confidence < 0.60

RESPONSE:
  1. Stop processing (DO_NOT_AUTO_CLASSIFY)
  2. Log incident with details
  3. Create review item in COMPLIANCE_REVIEW_QUEUE
  4. Response: 202 ACCEPTED (pending human review)
  5. Escalate to: TAX_ANALYST (async email + dashboard alert)
  6. SLA: Human review within 24 hours
  7. No automatic retry (requires manual decision)
  8. User notification: "Classification under review, will notify you of result"
```

#### Scenario 5: Data Corruption Detected

```
TRIGGER: Audit hash mismatch (data tampered)

RESPONSE:
  1. Halt all processing immediately
  2. Isolate the corrupted record
  3. Log security incident
  4. Escalate to: SECURITY_OFFICER + COMPLIANCE_OFFICER (urgent)
  5. Response: 500 INTERNAL_SERVER_ERROR
  6. Notify: C-level executives (if sensitive data)
  7. Investigation: Forensic audit trail review
  8. Recovery: Restore from backup (if < 1 hour old)
```

### Escalation Contacts

```
ESCALATION_MAP:
  
  Severity: INFO
    → Log only
    → No notification required
  
  Severity: WARNING
    → Log + Email to COMPLIANCE_OFFICER
    → SLA: Review within 24 hours
  
  Severity: CRITICAL
    → Log + Email + SMS to COMPLIANCE_OFFICER
    → Log + Slack to ML_ENGINEERING_TEAM
    → SLA: Response within 1 hour
  
  Severity: SECURITY_BREACH
    → Log + SMS + Phone call to SECURITY_OFFICER
    → Log + Call to CEO/General Counsel
    → SLA: Response within 15 minutes
    → Incident escalation protocol triggered
```

---

## 🔗 SECTION 10 — INTER-AGENT DEPENDENCY MAP

### Upstream Agents (Providing Data)

```
[1] IDENTITY_AGENT
    ├─ Provides: user_id, verified_identity, tax_profile
    └─ Protocol: Sync API (10ms SLA)

[2] TRANSACTION_LEDGER_AGENT
    ├─ Provides: transaction_id, amount, timestamp, payer_info
    └─ Protocol: Event stream (Kafka)

[3] JURISDICTION_RULES_AGENT
    ├─ Provides: tax_rules, withholding_rules, reporting_forms
    └─ Protocol: Database sync (daily)

[4] USER_PROFILE_AGENT
    ├─ Provides: residency_status, tax_filing_status, exemptions
    └─ Protocol: API (100ms SLA)
```

### Downstream Agents (Consuming Output)

```
[1] WITHHOLDING_ENGINE_AGENT
    ├─ Consumes: tax_classification, withholding_amount
    ├─ Action: Calculate actual withholding
    └─ Protocol: Event (TAX_CLASSIFICATION_EVENT)

[2] REPORTING_GENERATION_AGENT
    ├─ Consumes: tax_classification, reporting_requirements
    ├─ Action: Generate tax forms (1040, ITR, GST returns)
    └─ Protocol: Event queue

[3] USER_NOTIFICATION_AGENT
    ├─ Consumes: flags, reporting_requirements, deadlines
    ├─ Action: Notify user of obligations
    └─ Protocol: Event → email/SMS

[4] COMPLIANCE_REVIEW_AGENT
    ├─ Consumes: flagged_items, anomaly_scores
    ├─ Action: Create work items for compliance team
    └─ Protocol: Database insert + Event

[5] GROWTH_ENGINE_AGENT
    ├─ Consumes: earnings_classified (for achievement tracking)
    ├─ Action: Update milestone, badges, leaderboards
    └─ Protocol: Event (anonymized, no sensitive tax data)

[6] ANALYTICS_AGENT
    ├─ Consumes: classification_results (aggregated, anonymized)
    ├─ Action: Generate platform-wide tax statistics
    └─ Protocol: Batch ETL (nightly)
```

### Event Emitted by TAX_COMPLIANCE_AGENT

```
EVENT_TYPE: TAX_CLASSIFICATION_COMPLETED

{
  "event_id": "EVT-UUID-v4",
  "timestamp_utc": "2026-02-25T14:30:47Z",
  "agent_id": "TAX_COMPLIANCE_AGENT",
  "transaction_id": "TXN-UUID-v4",
  "actor_id": "USER-UUID",
  
  "classification_result": {
    "income_type": "SELF_EMPLOYMENT_INCOME",
    "tax_jurisdiction": "US-CA",
    "taxable_income": 4250.00,
    "withholding_amount": 637.50
  },
  
  "reporting_requirements": [
    {
      "form_type": "1040_SCHEDULE_C",
      "jurisdiction": "US",
      "due_date": "2027-04-15"
    }
  ],
  
  "flags": [
    {
      "flag_code": "INTERNATIONAL_PAYMENT",
      "severity": "INFO"
    }
  ],
  
  "confidence_score": 0.98,
  
  "downstream_triggers": [
    "WITHHOLDING_ENGINE",
    "COMPLIANCE_REVIEW_AGENT",
    "REPORTING_GENERATION_AGENT",
    "USER_NOTIFICATION_AGENT"
  ]
}
```

---

## 🎯 SECTION 11 — GROWTH ENGINE & INNOVATION ECOSYSTEM HOOKS

### Achievement & Earnings Tracking

```
If agent classifies income as TAXABLE:
  EMIT_FEATURE_VECTOR to FEATURE_STORE_AGENT:
    {
      user_id: USER_UUID,
      feature_name: "EARNINGS_CLASSIFIED_YTD",
      feature_value: 4250.00,
      timestamp: ISO-8601,
      source_agent: "TAX_COMPLIANCE_AGENT",
      category: "FINANCIAL"
    }

  TRIGGER to GROWTH_ENGINE_AGENT:
    {
      achievement_type: "MILESTONE_EARNINGS",
      milestone_thresholds: [1000, 5000, 10000, 50000],
      current_value: 4250.00,
      trigger_badge: "SELF_EMPLOYED_CONTRIBUTOR"
    }
```

### Innovation & Royalty Tracking

```
If income_type = "ROYALTIES" OR "CREATIVE_CONTENT":
  EMIT_IDEA_VECTOR to INNOVATION_ECONOMY_AGENT:
    {
      creator_id: USER_UUID,
      content_id: (if applicable),
      royalty_amount: taxable_income,
      royalty_date: timestamp,
      source_agent: "TAX_COMPLIANCE_AGENT"
    }

  COMPATIBILITY_CHECK:
    - Does not leak sensitive tax data
    - Anonymized for analytics
    - Respects privacy (no personal tax data to growth engine)
```

---

## 🔍 SECTION 12 — PERFORMANCE MONITORING & OBSERVABILITY

### Key Performance Indicators (KPIs)

```
METRIC_1: Classification Success Rate
  Target: 99.5% (target-level SLA)
  Red line: < 95%
  Collection: Every transaction
  Alert: Automatic if < 95% for 5 minutes

METRIC_2: Classification Latency (P99)
  Target: 500ms
  Red line: > 1000ms
  Collection: Every transaction
  Alert: Automatic if > 1000ms for 10 consecutive requests

METRIC_3: ML Model Accuracy
  Target: 94.7% (baseline)
  Red line: < 92%
  Collection: Weekly training validation
  Alert: Automatic if accuracy drops > 2%

METRIC_4: Anomaly Detection Precision
  Target: 95%+ (minimize false positives)
  Red line: < 90%
  Collection: Monthly (labeled review data)

METRIC_5: Audit Log Integrity
  Target: 100% (zero data loss)
  Red line: Any write failure
  Collection: Every audit write
  Alert: Immediate escalation if failed

METRIC_6: Error Rate
  Target: < 0.5% of requests
  Red line: > 1%
  Collection: Every request
  Alert: Automatic if > 1% for 5 minutes

METRIC_7: Feature Drift Indicator
  Target: < 1% distribution shift (KS statistic)
  Red line: > 5%
  Collection: Daily
  Alert: Automatic if > 5%

METRIC_8: Compliance Review Queue Backlog
  Target: < 100 items
  Red line: > 500 items
  Collection: Hourly
  Alert: If > 300 items (add more reviewers)
```

### Monitoring Integration

```
OBSERVABILITY_AGENT Integration:
  ├─ Prometheus: Metrics collection
  ├─ Grafana: Dashboards (real-time + historical)
  ├─ ELK Stack: Log aggregation
  ├─ Datadog: APM + alerting
  └─ PagerDuty: On-call escalation

DASHBOARD_1: Compliance Health
  - Classification success rate
  - Current backlog
  - Critical flags (last 24h)
  - Model accuracy trend

DASHBOARD_2: Operational Excellence
  - Latency percentiles
  - Error rate
  - Feature drift indicator
  - Database performance

DASHBOARD_3: ML Model Health
  - Accuracy by income class
  - Drift indicators
  - Last retraining timestamp
  - Confidence score distribution

ALERTING_RULES:
  [1] Classification success < 95% → Slack + Email (5 min window)
  [2] Latency P99 > 1000ms → Slack + Email (10 min window)
  [3] Audit write failure → SMS + Phone call (immediate)
  [4] Feature drift > 5% → Email + Dashboard highlight (daily check)
  [5] Backlog > 300 → Email (escalation to team lead)
```

---

## 📦 SECTION 13 — VERSIONING & DEPLOYMENT POLICY

### Versioning Scheme

```
VERSION_FORMAT: MAJOR.MINOR.PATCH

TAX-COMPLIANCE-AGENT-v{MAJOR}.{MINOR}.{PATCH}

Example: TAX-COMPLIANCE-AGENT-v2.0.0

MAJOR:
  Incremented on: Breaking changes (e.g., new tax jurisdiction, new income class)
  Impact: May require database migrations, user notification
  Frequency: 2–3 times per year

MINOR:
  Incremented on: New features (e.g., new withholding rule, new report type)
  Impact: Backward compatible, no user impact
  Frequency: Monthly

PATCH:
  Incremented on: Bug fixes, performance improvements, rule updates
  Impact: No breaking changes
  Frequency: Weekly

COMPONENT_VERSIONING:
  - ML Model: TAX-CLASSIFIER-ML-v{X}.{Y}.{Z}
  - Rule Engine: TAX-RATE-RULES-v{X}.{Y}.{Z}
  - Jurisdiction DB: TAX-JURISDICTION-DB-v{X}.{Y}.{Z}
```

### Deployment Strategy

```
DEPLOYMENT_ENVIRONMENTS:
  [1] DEV: Local/CI (automated, no production data)
  [2] TEST: Automated testing (anonymized historical data)
  [3] STAGING: Pre-production (mirror of prod, 1% of traffic)
  [4] PRODUCTION: Live (100% of traffic)

PROMOTION_RULES:
  DEV → TEST: Automatic on commit to main branch
  TEST → STAGING: Automatic if all tests pass
  STAGING → PROD: Manual approval required (COMPLIANCE_OFFICER sign-off)

ROLLBACK_POLICY:
  If critical issue detected within 5 minutes:
    → Automatic rollback to previous version
    → Incident logging + notification
    → Root cause analysis required
  
  If issue detected after 5 minutes:
    → Manual decision (assess impact)
    → Gradual rollout (5% → 25% → 100%)
    → Monitoring before full promotion

MIGRATION_POLICY:
  All schema changes:
    1. Written as backward-compatible migrations
    2. Tested on STAGING first
    3. Deployed with feature flags (if needed)
    4. Monitored for 24 hours before full rollout
```

### Backward Compatibility

```
RULE_1: Never remove fields from output contract
  ✓ Can add new optional fields
  ✗ Cannot remove existing fields
  ✓ Can deprecate fields (mark as deprecated, keep for 2 versions)

RULE_2: Never change field types
  ✓ Can extend field (e.g., VARCHAR to VARCHAR longer)
  ✗ Cannot change datatype (e.g., VARCHAR to INT)

RULE_3: Never break API contracts
  ✓ Can add new endpoints
  ✗ Cannot modify existing endpoint signatures

RULE_4: ML model changes must be versioned
  ✓ Can retrain same model version
  ✗ Cannot push incompatible model without version bump
  ✓ Can run A/B tests with two model versions
```

---

## 🚫 SECTION 14 — NON-NEGOTIABLE RULES

### Prohibited Actions

```
TAX_COMPLIANCE_AGENT MUST NEVER:

[1] ✗ Create hidden logic
    Why: Accountability + auditability required
    Enforcement: Code review + static analysis

[2] ✗ Modify historical records
    Why: Tax records are legally immutable
    Enforcement: Database constraints + audit triggers

[3] ✗ Auto-delete audit logs
    Why: 7-year legal retention requirement
    Enforcement: Database policy + archival process

[4] ✗ Override governance agents
    Why: Compliance officers are final authority
    Enforcement: RBAC enforcement + approval workflows

[5] ✗ Bypass compliance checks
    Why: Regulatory mandate
    Enforcement: Every operation validates compliance

[6] ✗ Mix domain data
    Why: Tenant isolation + data security
    Enforcement: Database RLS + application validation

[7] ✗ Execute outside scope
    Why: Prevents scope creep + unintended side effects
    Enforcement: Input validation + error handling

[8] ✗ Assume missing specifications
    Why: Determinism requirement
    Enforcement: Fail fast on ambiguity

[9] ✗ Process invalid input silently
    Why: Compliance + auditability
    Enforcement: Strict input validation

[10] ✗ Override user preferences (for compliance)
     Why: Tax privacy + user autonomy
     Enforcement: Explicit opt-in for any cross-domain sharing
```

---

## 🔒 SECTION 15 — DEPLOYMENT CHECKLIST (SEALED)

### Pre-Production Gate

```
GATE_1: Security Review [✓ REQUIRED]
  [ ] Penetration testing: Passed
  [ ] SQL injection tests: Passed
  [ ] Cross-tenant isolation: Verified
  [ ] Encryption at rest: Enabled
  [ ] Encryption in transit: TLS 1.3
  [ ] Secret management: Vault configured
  [ ] API rate limiting: Configured
  Approval: SECURITY_OFFICER

GATE_2: Compliance Review [✓ REQUIRED]
  [ ] Tax jurisdiction rules: Validated by legal
  [ ] Data retention policy: 7 years confirmed
  [ ] Audit trail immutability: Verified
  [ ] GDPR data handling: Compliant
  [ ] SOX 404 controls: Documented
  [ ] Tax authority coordination: Complete
  Approval: COMPLIANCE_OFFICER

GATE_3: Performance Review [✓ REQUIRED]
  [ ] Latency P99: < 500ms
  [ ] Success rate: > 99.5%
  [ ] Database load test: Passed (100K RPS)
  [ ] Failover testing: Successful
  [ ] Scaling tests: 10x traffic simulation passed
  Approval: INFRASTRUCTURE_LEAD

GATE_4: ML Quality Review [✓ REQUIRED]
  [ ] Model accuracy: 94.7% (baseline)
  [ ] Cross-validation: K-fold (k=5) passed
  [ ] Drift detection: Configured
  [ ] Retraining schedule: Weekly confirmed
  [ ] Feature engineering: Domain-expert validated
  Approval: ML_ENGINEERING_LEAD

GATE_5: Operations Readiness [✓ REQUIRED]
  [ ] Runbooks: Written
  [ ] Monitoring dashboards: Configured
  [ ] Alerting thresholds: Set
  [ ] Incident response plan: Approved
  [ ] On-call rotation: Scheduled
  Approval: OPS_MANAGER

GATE_6: User Communication [✓ REQUIRED]
  [ ] User documentation: Published
  [ ] FAQ: Comprehensive
  [ ] Support team training: Completed
  [ ] Change log: Drafted
  Approval: COMMUNICATIONS_LEAD

ALL GATES MUST PASS BEFORE PRODUCTION DEPLOYMENT.
```

---

## 🎓 SECTION 16 — TRAINING & DOCUMENTATION

### Compliance Officer Training

```
TRAINING_MODULES:
  [1] TAX_COMPLIANCE_AGENT Overview (30 min)
  [2] Reviewing Flagged Transactions (45 min)
  [3] Handling Escalations (30 min)
  [4] Audit Trail Analysis (45 min)
  [5] Incident Response (60 min)
  [6] Regulatory Updates (15 min weekly)

CERTIFICATION: Required before deployment
  - Test: 80% minimum score
  - Duration: 1 day
  - Renewal: Annually
```

### API Documentation

```
ENDPOINT_1: POST /compliance/v1/classify-transaction
ENDPOINT_2: GET /compliance/v1/tax-profile/{user_id}
ENDPOINT_3: GET /compliance/v1/classification/{transaction_id}
ENDPOINT_4: GET /compliance/v1/reporting-obligations/{user_id}
ENDPOINT_5: POST /compliance/v1/exemption-request
ENDPOINT_6: GET /compliance/v1/audit-log/{audit_id}

All documented with:
  - Request/response schemas (OpenAPI 3.0)
  - Error codes + resolution steps
  - Rate limits
  - Example calls (cURL + SDK)
```

---

## 📋 FINAL CERTIFICATION

```
DOCUMENT_STATUS:      ✓ LOCKED & SEALED
INTERPRETATION:       ✗ NONE ALLOWED
MODIFICATION:         ✓ Add-only via version bump
ENFORCEMENT:          ✓ Automated + Human oversight
DEPLOYMENT_READY:     ✓ PENDING FINAL GATE APPROVAL

CERTIFICATION_BY:
  Name: (To be filled by deploying authority)
  Role: Chief Compliance Officer / CEO
  Signature: (Digital signature required)
  Date: (Deployment date)
  Authority: Full legal authority for tax compliance

EFFECTIVE_DATE: (Upon signature)
REVISION_DATE: (6 months from deployment)

🔐 DOCUMENT_HASH: SHA256(sealed-specification)
🔐 IMMUTABLE: Yes
🔐 VERSION: 2.0.0
🔐 STATUS: PRODUCTION_READY (pending final approval)
```

---

**END OF SPECIFICATION**

*Generated for: Ecoskiller Antigravity Platform*  
*ML Owner: Compliance ML Engineer*  
*Safety Owner: Tax Compliance Officer*  
*Intelligence Owner: Compliance Intelligence Lead*  
*Sealed: 2026-02-25*  
*Status: LOCKED · DETERMINISTIC · AUDIT-READY*
# 📘 TAX_COMPLIANCE_AGENT — IMPLEMENTATION & DEPLOYMENT GUIDE

**Document:** Technical Implementation Reference  
**Version:** 2.0.0  
**Status:** READY FOR DEPLOYMENT  
**Platform:** Ecoskiller Antigravity  

---

## 📑 TABLE OF CONTENTS

1. [Quick Start](#quick-start)
2. [System Architecture](#system-architecture)
3. [Database Schema](#database-schema)
4. [API Endpoints](#api-endpoints)
5. [ML Model Deployment](#ml-model-deployment)
6. [Configuration](#configuration)
7. [Testing & Validation](#testing--validation)
8. [Monitoring & Alerts](#monitoring--alerts)
9. [Troubleshooting](#troubleshooting)

---

## 🚀 QUICK START

### Prerequisites

```bash
# System requirements
- PostgreSQL 14+ (with TimescaleDB extension)
- Redis 6.0+
- Apache Kafka 2.8+
- Python 3.9+ (for ML components)
- Docker 20.10+
- Kubernetes 1.24+
```

### One-Command Deployment (Staging)

```bash
# Deploy to staging environment
./scripts/deploy-tax-agent.sh --environment=staging --version=2.0.0

# Expected output:
# ✓ Database migrations: 15 schemas
# ✓ ML models loaded: 3 models
# ✓ Kafka topics created: 5 topics
# ✓ API service replicas: 200
# ✓ Deployment status: HEALTHY
# ✓ Health check: All endpoints responding
```

### Minimal Configuration (Dev Testing)

```yaml
# .env.development
DATABASE_URL=postgresql://dev:dev@localhost:5432/ecoskiller_dev
REDIS_URL=redis://localhost:6379/0
KAFKA_BROKERS=localhost:9092
ML_MODEL_PATH=/models/
TAX_AGENT_LOG_LEVEL=DEBUG
FEATURE_STORE_URL=http://localhost:8001
```

---

## 🏗️ SYSTEM ARCHITECTURE

### Microservices Topology

```
┌─────────────────────────────────────────────────────────────────┐
│                      API Gateway (Kong)                          │
│                    Rate Limit: 100K req/sec                      │
└──────────────────────┬──────────────────────────────────────────┘
                       │
        ┌──────────────┼──────────────┬─────────────────┐
        │              │              │                 │
        v              v              v                 v
  ┌──────────┐  ┌───────────────┐  ┌──────────┐  ┌────────────┐
  │ Ingestion │  │ Classification │  │ Reporting │  │ Audit Log  │
  │ Service  │  │    Service     │  │  Service  │  │  Service   │
  │ (50x)    │  │     (200x)     │  │   (30x)   │  │  (100x)    │
  └──────┬───┘  └───────┬────────┘  └──────┬───┘  └─────┬──────┘
         │              │                   │            │
         └──────────────┼───────────────────┼────────────┘
                        │
                  ┌─────v─────┐
                  │   Kafka   │
                  │  (5 topics)│
                  └─────┬─────┘
                        │
         ┌──────────────┼──────────────┐
         │              │              │
         v              v              v
    ┌────────┐     ┌─────────┐    ┌────────┐
    │   ML   │     │  Rule   │    │ Feature│
    │ Models │     │ Engine  │    │ Store  │
    │        │     │         │    │        │
    └────────┘     └─────────┘    └────────┘
         │              │              │
         └──────────────┼──────────────┘
                        │
         ┌──────────────┼──────────────┐
         │              │              │
         v              v              v
    ┌────────┐  ┌────────────┐   ┌────────────┐
    │ Postgres│  │ TimescaleDB│   │    S3      │
    │  (OLTP) │  │  (Audit)   │   │  (Archive) │
    │         │  │  (7-year)  │   │            │
    └────────┘  └────────────┘   └────────────┘
```

### Service Communication

```
Request Flow:
  1. User submits transaction → API Gateway
  2. API Gateway validates + routes → Ingestion Service
  3. Ingestion Service enqueues → Kafka (transaction.created)
  4. Classification Service consumes → Kafka topic
  5. Classification Service:
     - Calls ML inference (model replicas)
     - Calls Rule Engine (lookups)
     - Detects anomalies (ML)
     - Emits output event
  6. Output event triggers downstream (Reporting, Notifications, etc.)
  7. Audit Logger captures everything (async)
```

---

## 📊 DATABASE SCHEMA

### Core Tables

```sql
-- 1. Tax Classification Results
CREATE TABLE compliance.tax_classifications (
  classification_id UUID PRIMARY KEY,
  transaction_id UUID NOT NULL UNIQUE,
  tenant_id UUID NOT NULL,
  actor_id UUID NOT NULL,
  
  income_type VARCHAR(50) NOT NULL,
  tax_jurisdiction VARCHAR(10) NOT NULL,
  
  gross_income DECIMAL(15,2),
  taxable_income DECIMAL(15,2),
  tax_rate DECIMAL(5,4),
  withholding_amount DECIMAL(15,2),
  
  confidence_score DECIMAL(3,2) NOT NULL,
  model_version VARCHAR(50) NOT NULL,
  
  reporting_requirements JSONB,
  flags JSONB,
  
  decision_path TEXT[],
  
  created_at TIMESTAMP NOT NULL,
  processed_at TIMESTAMP,
  
  CONSTRAINT fk_tenant FOREIGN KEY (tenant_id) REFERENCES tenants(id),
  CONSTRAINT fk_actor FOREIGN KEY (actor_id) REFERENCES users(id)
);

CREATE INDEX idx_classification_transaction ON compliance.tax_classifications(transaction_id);
CREATE INDEX idx_classification_actor ON compliance.tax_classifications(actor_id, created_at);
CREATE INDEX idx_classification_jurisdiction ON compliance.tax_classifications(tax_jurisdiction);
CREATE POLICY tenant_isolation_tax_class ON compliance.tax_classifications
  USING (tenant_id = current_user_tenant_id());


-- 2. User Tax Profiles
CREATE TABLE compliance.tax_profiles (
  tax_profile_id UUID PRIMARY KEY,
  user_id UUID NOT NULL UNIQUE,
  tenant_id UUID NOT NULL,
  
  tax_jurisdiction VARCHAR(10) NOT NULL,
  residency_status VARCHAR(50),
  residency_days_current_fy INT,
  
  tin_type VARCHAR(20),
  tin_hash VARCHAR(64), -- SHA256 hash, not actual TIN
  tin_last_4 VARCHAR(4),
  
  filing_status VARCHAR(50),
  exemption_categories TEXT[],
  
  gst_registered BOOLEAN,
  gst_number_hash VARCHAR(64),
  
  tax_calculation_method VARCHAR(50),
  
  created_at TIMESTAMP,
  updated_at TIMESTAMP,
  verified_at TIMESTAMP,
  
  CONSTRAINT fk_tenant FOREIGN KEY (tenant_id) REFERENCES tenants(id),
  CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE INDEX idx_tax_profile_user ON compliance.tax_profiles(user_id);
CREATE POLICY tenant_isolation_tax_profile ON compliance.tax_profiles
  USING (tenant_id = current_user_tenant_id());


-- 3. Audit Trail (Immutable)
CREATE TABLE audit.audit_logs (
  audit_id UUID PRIMARY KEY,
  timestamp_utc TIMESTAMP NOT NULL,
  tenant_id UUID NOT NULL,
  
  actor_id UUID,
  actor_role VARCHAR(50),
  
  action_type VARCHAR(50) NOT NULL,
  resource_type VARCHAR(50) NOT NULL,
  resource_id UUID NOT NULL,
  
  old_value JSONB,
  new_value JSONB,
  
  status ENUM ('SUCCESS', 'FAILURE') NOT NULL,
  error_message TEXT,
  
  model_version VARCHAR(50),
  confidence_score DECIMAL(3,2),
  
  decision_path TEXT[],
  anomaly_score DECIMAL(3,2),
  
  ip_address INET,
  user_agent VARCHAR(500),
  
  audit_hash VARCHAR(64), -- Chain of custody
  prev_audit_hash VARCHAR(64),
  
  immutable BOOLEAN DEFAULT true,
  
  CONSTRAINT immutability CHECK (immutable = true),
  CONSTRAINT fk_tenant FOREIGN KEY (tenant_id) REFERENCES tenants(id)
);

CREATE TRIGGER audit_prevent_update
  BEFORE UPDATE ON audit.audit_logs
  FOR EACH ROW EXECUTE FUNCTION prevent_update();

CREATE TRIGGER audit_prevent_delete
  BEFORE DELETE ON audit.audit_logs
  FOR EACH ROW EXECUTE FUNCTION prevent_delete();

CREATE INDEX idx_audit_timestamp ON audit.audit_logs(timestamp_utc DESC);
CREATE INDEX idx_audit_resource ON audit.audit_logs(resource_type, resource_id);
CREATE POLICY tenant_isolation_audit ON audit.audit_logs
  USING (tenant_id = current_user_tenant_id());


-- 4. Tax Rate Rules (Versioned)
CREATE TABLE compliance.tax_rate_rules (
  rule_id UUID PRIMARY KEY,
  jurisdiction_code VARCHAR(10) NOT NULL,
  income_type VARCHAR(50) NOT NULL,
  fy_year INT NOT NULL,
  
  rule_type VARCHAR(50), -- PROGRESSIVE, FLAT, SECTORAL
  
  brackets JSONB, -- [{min: 0, max: 50000, rate: 0.10}, ...]
  flat_rate DECIMAL(5,4),
  sector_mapping JSONB,
  
  effective_date DATE NOT NULL,
  expiry_date DATE,
  
  source_reference VARCHAR(500),
  source_url VARCHAR(2000),
  
  created_at TIMESTAMP,
  updated_at TIMESTAMP,
  last_validated TIMESTAMP,
  
  is_active BOOLEAN DEFAULT true,
  
  CONSTRAINT unique_rule_combination 
    UNIQUE (jurisdiction_code, income_type, fy_year, effective_date)
);

CREATE INDEX idx_rules_jurisdiction_income ON compliance.tax_rate_rules(jurisdiction_code, income_type);
CREATE INDEX idx_rules_effective_date ON compliance.tax_rate_rules(effective_date);


-- 5. Compliance Review Queue
CREATE TABLE compliance.review_queue (
  review_id UUID PRIMARY KEY,
  classification_id UUID NOT NULL,
  transaction_id UUID NOT NULL,
  
  tenant_id UUID NOT NULL,
  actor_id UUID NOT NULL,
  
  review_type VARCHAR(50), -- CONFIDENCE_LOW, ANOMALY_DETECTED, MANUAL_OVERRIDE
  priority ENUM ('LOW', 'MEDIUM', 'HIGH', 'CRITICAL'),
  
  flag_code VARCHAR(50),
  flag_message TEXT,
  
  status ENUM ('PENDING', 'IN_REVIEW', 'RESOLVED', 'ESCALATED'),
  assigned_to UUID,
  
  reviewer_notes TEXT,
  resolution_timestamp TIMESTAMP,
  
  created_at TIMESTAMP NOT NULL,
  updated_at TIMESTAMP,
  
  CONSTRAINT fk_tenant FOREIGN KEY (tenant_id) REFERENCES tenants(id),
  CONSTRAINT fk_classification FOREIGN KEY (classification_id) 
    REFERENCES compliance.tax_classifications(classification_id)
);

CREATE INDEX idx_review_queue_status ON compliance.review_queue(status, priority DESC);
CREATE INDEX idx_review_queue_assigned ON compliance.review_queue(assigned_to) WHERE status != 'RESOLVED';
```

### TimescaleDB Hypertables (for audit logs)

```sql
-- Convert audit logs to hypertable (for time-series optimization)
SELECT create_hypertable(
  'audit.audit_logs',
  'timestamp_utc',
  if_not_exists => TRUE
);

-- Compression (compress data older than 30 days)
ALTER TABLE audit.audit_logs SET (
  timescaledb.compress,
  timescaledb.compress_segmentby = 'tenant_id, action_type'
);

SELECT add_compression_policy(
  'audit.audit_logs',
  INTERVAL '30 days'
);

-- Retention (keep for 7 years as required)
SELECT add_retention_policy(
  'audit.audit_logs',
  INTERVAL '7 years'
);
```

---

## 🔌 API ENDPOINTS

### 1. Classify Transaction

```
POST /compliance/v1/classify-transaction
Authorization: Bearer {token}
Content-Type: application/json

REQUEST:
{
  "transaction_id": "TXN-UUID",
  "timestamp_utc": "2026-02-25T14:30:45Z",
  "actor_id": "USER-UUID",
  "actor_jurisdiction_code": "US-CA",
  "transaction_type": "EARNINGS_FREELANCE_SERVICES",
  "gross_amount_usd": 5000.00,
  "currency_code": "USD",
  "payer_jurisdiction_code": "US-TX",
  "payer_type": "BUSINESS"
}

RESPONSE (200 OK):
{
  "classification_id": "CLASS-UUID",
  "transaction_id": "TXN-UUID",
  "income_type": "SELF_EMPLOYMENT_INCOME",
  "tax_jurisdiction": "US-CA",
  "tax_treatment": {
    "gross_income": 5000.00,
    "taxable_income": 4250.00,
    "applicable_rate": 0.24,
    "withholding_amount": 637.50
  },
  "confidence_score": 0.98,
  "flags": [...],
  "reporting_requirements": [...],
  "audit_reference": "AUDIT-UUID"
}

ERROR (400 Bad Request):
{
  "error_code": "MISSING_REQUIRED_FIELD",
  "message": "Field 'actor_jurisdiction_code' is required",
  "field": "actor_jurisdiction_code"
}
```

### 2. Get User Tax Profile

```
GET /compliance/v1/tax-profile/{user_id}
Authorization: Bearer {token}

RESPONSE (200 OK):
{
  "tax_profile_id": "PROFILE-UUID",
  "user_id": "USER-UUID",
  "tax_jurisdiction": "US-CA",
  "residency_status": "TAX_RESIDENT",
  "filing_status": "SINGLE",
  "exemption_categories": ["STARTUP_DEDUCTION"],
  "gst_registered": false,
  "verified_at": "2026-02-01T10:00:00Z"
}
```

### 3. Get Classification Result

```
GET /compliance/v1/classification/{classification_id}
Authorization: Bearer {token}

RESPONSE (200 OK):
{
  "classification_id": "CLASS-UUID",
  "transaction_id": "TXN-UUID",
  "income_type": "SELF_EMPLOYMENT_INCOME",
  "tax_jurisdiction": "US-CA",
  "created_at": "2026-02-25T14:30:47Z",
  "confidence_score": 0.98,
  ...
}
```

### 4. Get Reporting Obligations

```
GET /compliance/v1/reporting-obligations/{user_id}
  ?fy_year=2026
  &jurisdiction=US-CA

RESPONSE (200 OK):
{
  "user_id": "USER-UUID",
  "fiscal_year": 2026,
  "jurisdiction": "US-CA",
  "total_earnings_classified": 45000.00,
  "reporting_requirements": [
    {
      "form_type": "1040_SCHEDULE_C",
      "jurisdiction": "US",
      "due_date": "2027-04-15",
      "is_mandatory": true,
      "estimated_tax_liability": 8750.00
    },
    {
      "form_type": "CA_540_SCHEDULE_CA",
      "jurisdiction": "US-CA",
      "due_date": "2027-05-15",
      "is_mandatory": true,
      "estimated_tax_liability": 2340.00
    }
  ]
}
```

### 5. Request Tax Exemption

```
POST /compliance/v1/exemption-request
Authorization: Bearer {token}
Content-Type: application/json

REQUEST:
{
  "user_id": "USER-UUID",
  "exemption_code": "STARTUP_DEDUCTION",
  "tax_jurisdiction": "IN",
  "startup_registration_date": "2025-01-15",
  "documentation_url": "s3://bucket/startup-cert.pdf"
}

RESPONSE (202 Accepted):
{
  "request_id": "REQ-UUID",
  "status": "PENDING_REVIEW",
  "message": "Your exemption request has been submitted for review",
  "estimated_review_time_days": 5
}
```

### 6. Get Audit Log

```
GET /compliance/v1/audit-log/{audit_id}
Authorization: Bearer {token}
X-Require-Immutable-Proof: true

RESPONSE (200 OK):
{
  "audit_id": "AUDIT-UUID",
  "timestamp_utc": "2026-02-25T14:30:47Z",
  "action_type": "TAX_CLASSIFICATION",
  "resource_type": "TRANSACTION",
  "resource_id": "TXN-UUID",
  "status": "SUCCESS",
  "immutable": true,
  "audit_hash": "SHA256:abc123...",
  "prev_audit_hash": "SHA256:xyz789...",
  "decision_path": [...]
}
```

---

## 🤖 ML MODEL DEPLOYMENT

### Model Files

```
/models/
├── income-classifier/
│   ├── model-v3.2.1.pkl (XGBoost binary, 245 MB)
│   ├── feature-encoder-v3.2.1.pkl (encoder)
│   ├── metadata.json
│   │   {
│   │     "model_type": "xgboost_classifier",
│   │     "input_features": 18,
│   │     "output_classes": 25,
│   │     "accuracy": 0.947,
│   │     "training_date": "2026-02-20",
│   │     "training_samples": 10000000
│   │   }
│   └── requirements.txt
│
├── tax-rate-calc/
│   ├── model-v2.1.0.pkl (CatBoost, 156 MB)
│   ├── metadata.json
│   └── requirements.txt
│
└── anomaly-detector/
    ├── isolation-forest-v1.8.0.pkl (180 MB)
    ├── lstm-temporal-v1.8.0.h5 (PyTorch, 298 MB)
    ├── metadata.json
    └── requirements.txt
```

### Model Loading & Serving

```python
# model_server.py
import pickle
import xgboost as xgb
from catboost import CatBoostRegressor
import numpy as np

class ModelRegistry:
    def __init__(self):
        self.models = {}
        self.encoders = {}
        
    def load_income_classifier(self):
        """Load income classifier (XGBoost)"""
        with open('/models/income-classifier/model-v3.2.1.pkl', 'rb') as f:
            self.models['income_classifier'] = pickle.load(f)
        
        with open('/models/income-classifier/feature-encoder-v3.2.1.pkl', 'rb') as f:
            self.encoders['income_classifier'] = pickle.load(f)
    
    def load_tax_rate_calc(self):
        """Load tax rate calculator (CatBoost)"""
        with open('/models/tax-rate-calc/model-v2.1.0.pkl', 'rb') as f:
            self.models['tax_rate_calc'] = pickle.load(f)
    
    def load_anomaly_detector(self):
        """Load anomaly detector (Isolation Forest + LSTM)"""
        with open('/models/anomaly-detector/isolation-forest-v1.8.0.pkl', 'rb') as f:
            self.models['anomaly_detector_if'] = pickle.load(f)
        # LSTM loaded via PyTorch
    
    def predict_income_type(self, features_dict):
        """Classify transaction into income type"""
        model = self.models['income_classifier']
        encoder = self.encoders['income_classifier']
        
        # Transform features
        features_encoded = encoder.transform(features_dict)
        
        # Get prediction + probability
        prediction = model.predict(features_encoded)
        probabilities = model.predict_proba(features_encoded)
        
        confidence = np.max(probabilities)
        
        return {
            'income_type': prediction[0],
            'confidence': float(confidence),
            'class_probabilities': dict(zip(model.classes_, probabilities[0]))
        }

# Usage
registry = ModelRegistry()
registry.load_income_classifier()
registry.load_tax_rate_calc()
registry.load_anomaly_detector()
```

### Model Retraining Pipeline

```bash
#!/bin/bash
# scripts/retrain-models.sh

set -e

# Run weekly (every Monday 2:00 UTC)
echo "[$(date)] Starting model retraining..."

# 1. Extract training data (last 90 days, anonymized)
python /scripts/extract_training_data.py \
  --days-back=90 \
  --output=/tmp/training_data.parquet \
  --anonymize=true

# 2. Feature engineering
python /scripts/feature_engineering.py \
  --input=/tmp/training_data.parquet \
  --output=/tmp/features.parquet

# 3. Train income classifier
python /scripts/train_income_classifier.py \
  --features=/tmp/features.parquet \
  --model-dir=/tmp/models/ \
  --validation-split=0.2

# 4. Validate accuracy
python /scripts/validate_model.py \
  --model-path=/tmp/models/income-classifier-new.pkl \
  --test-set=/tmp/test_data.parquet \
  --min-accuracy=0.92

# 5. If validation passed: deploy new model
if [ $? -eq 0 ]; then
  python /scripts/deploy_model.py \
    --model-path=/tmp/models/income-classifier-new.pkl \
    --version=3.2.2 \
    --environment=staging
  
  # Monitor for 24 hours in staging before prod
  echo "[$(date)] Model deployed to staging. Monitoring..."
else
  echo "[$(date)] Model validation failed. No deployment."
  exit 1
fi
```

---

## ⚙️ CONFIGURATION

### Environment Variables

```bash
# Database
DATABASE_URL=postgresql://user:pass@db.example.com:5432/ecoskiller
DATABASE_POOL_SIZE=50
DATABASE_TIMEOUT=10

# Redis
REDIS_URL=redis://redis.example.com:6379/0
REDIS_MAX_CONNECTIONS=100

# Kafka
KAFKA_BROKERS=kafka-1:9092,kafka-2:9092,kafka-3:9092
KAFKA_CONSUMER_GROUP=tax-compliance-agent
KAFKA_AUTO_OFFSET_RESET=earliest

# ML Models
ML_MODELS_PATH=/models/
ML_MODEL_RELOAD_INTERVAL_HOURS=24
ML_INFERENCE_TIMEOUT_MS=5000
ML_BATCH_SIZE=1000

# API
API_PORT=8080
API_WORKERS=200
API_TIMEOUT_SEC=30
API_RATE_LIMIT_PER_SEC=100000

# Compliance
TAX_AGENT_LOG_LEVEL=INFO
TAX_AGENT_STRICT_MODE=true
TAX_AGENT_AUDIT_RETENTION_YEARS=7

# Features & Flags
FEATURE_STORE_ENABLED=true
FEATURE_STORE_URL=http://feature-store:8001
GROWTH_ENGINE_ENABLED=true

# Monitoring
OBSERVABILITY_ENABLED=true
DATADOG_API_KEY=***
SLACK_WEBHOOK_URL=***

# Security
ENCRYPTION_KEY=*** (from Vault)
JWT_SECRET=*** (from Vault)
```

### Feature Flags

```yaml
# features/tax-compliance.yaml

FEATURES:
  TAX_CLASSIFICATION:
    enabled: true
    percentage: 100
    min_confidence_threshold: 0.60
  
  ANOMALY_DETECTION:
    enabled: true
    percentage: 100
    risk_score_threshold: 0.60
  
  WITHHOLDING_ENGINE:
    enabled: true
    percentage: 100
  
  REPORTING_GENERATION:
    enabled: true
    percentage: 100
  
  AUDIT_LOGGING:
    enabled: true
    percentage: 100
    immutable_enforcement: true
  
  EXEMPTION_REQUESTS:
    enabled: false  # Roll out: phase 2
    percentage: 0
```

---

## 🧪 TESTING & VALIDATION

### Unit Tests

```python
# tests/test_income_classifier.py

import pytest
from tax_compliance_agent import IncomeClassifier

def test_classify_self_employment():
    """Test classification of self-employment income"""
    classifier = IncomeClassifier()
    
    result = classifier.classify({
        'transaction_type': 'EARNINGS_FREELANCE_SERVICES',
        'amount': 5000,
        'payer_type': 'BUSINESS'
    })
    
    assert result['income_type'] == 'SELF_EMPLOYMENT_INCOME'
    assert result['confidence'] > 0.95

def test_classify_wages():
    """Test classification of wages"""
    classifier = IncomeClassifier()
    
    result = classifier.classify({
        'transaction_type': 'SALARY',
        'amount': 3000,
        'payer_type': 'EMPLOYER'
    })
    
    assert result['income_type'] == 'WAGES'
    assert result['confidence'] > 0.98

def test_missing_required_field():
    """Test validation of missing fields"""
    classifier = IncomeClassifier()
    
    with pytest.raises(ValidationError):
        classifier.classify({
            'transaction_type': 'SALARY'
            # Missing: amount
        })
```

### Integration Tests

```python
# tests/test_api_integration.py

import pytest
import requests
from datetime import datetime

@pytest.fixture
def api_client():
    return requests.Session()

def test_full_classification_flow(api_client):
    """Test end-to-end classification"""
    
    # Step 1: Submit transaction
    response = api_client.post(
        'http://localhost:8080/compliance/v1/classify-transaction',
        json={
            'transaction_id': 'TEST-TXN-001',
            'timestamp_utc': datetime.utcnow().isoformat() + 'Z',
            'actor_id': 'TEST-USER-001',
            'actor_jurisdiction_code': 'US-CA',
            'transaction_type': 'EARNINGS_FREELANCE_SERVICES',
            'gross_amount_usd': 5000.00,
            'currency_code': 'USD',
            'payer_jurisdiction_code': 'US-TX',
            'payer_type': 'BUSINESS'
        }
    )
    
    assert response.status_code == 200
    result = response.json()
    classification_id = result['classification_id']
    
    # Step 2: Retrieve classification
    response = api_client.get(
        f'http://localhost:8080/compliance/v1/classification/{classification_id}'
    )
    
    assert response.status_code == 200
    assert response.json()['income_type'] == 'SELF_EMPLOYMENT_INCOME'
    
    # Step 3: Verify audit log
    response = api_client.get(
        f'http://localhost:8080/compliance/v1/audit-log/{result["audit_reference"]}'
    )
    
    assert response.status_code == 200
    audit = response.json()
    assert audit['immutable'] == True
```

### Load Testing

```bash
# scripts/load-test.sh
# Test: 100K transactions/sec concurrent classification

locust -f locustfile.py \
  --host=http://localhost:8080 \
  --users=10000 \
  --spawn-rate=1000 \
  --run-time=5m \
  --headless \
  --csv=results

# Expected results:
# - P99 latency: < 500ms
# - Success rate: > 99.5%
# - Throughput: 100K req/sec
```

---

## 📊 MONITORING & ALERTS

### Prometheus Metrics

```python
# metrics.py

from prometheus_client import Counter, Histogram, Gauge

# Classification metrics
classification_total = Counter(
    'tax_classification_total',
    'Total classifications',
    ['outcome', 'income_type']
)

classification_duration = Histogram(
    'tax_classification_duration_seconds',
    'Classification processing time',
    buckets=[0.1, 0.3, 0.5, 1.0, 5.0]
)

confidence_score_histogram = Histogram(
    'confidence_score',
    'Distribution of confidence scores',
    buckets=[0.6, 0.7, 0.8, 0.9, 0.95, 1.0]
)

# Queue metrics
review_queue_size = Gauge(
    'compliance_review_queue_size',
    'Items pending human review'
)

# Audit metrics
audit_writes_total = Counter(
    'audit_writes_total',
    'Total audit log entries'
)

audit_write_errors = Counter(
    'audit_write_errors_total',
    'Failed audit writes'
)
```

### Grafana Dashboards

```
Dashboard: Tax Compliance Agent Health
├─ Classification Success Rate (target: 99.5%)
├─ Latency P99 (target: 500ms)
├─ Model Accuracy (target: 94.7%)
├─ Confidence Score Distribution
├─ Review Queue Backlog
├─ Error Rate (target: < 0.5%)
├─ Feature Drift Indicator
└─ Audit Log Write Latency
```

### Alerting Rules

```yaml
# prometheus-rules.yaml

groups:
  - name: tax_compliance_agent
    rules:
      - alert: ClassificationSuccessRateLow
        expr: rate(tax_classification_total{outcome="success"}[5m]) < 0.995
        for: 5m
        annotations:
          summary: "Classification success rate below 99.5%"
      
      - alert: ClassificationLatencyHigh
        expr: histogram_quantile(0.99, tax_classification_duration_seconds) > 1.0
        for: 10m
        annotations:
          summary: "P99 latency above 1 second"
      
      - alert: ReviewQueueBacklog
        expr: compliance_review_queue_size > 500
        for: 1h
        annotations:
          summary: "Review queue backlog exceeds 500 items"
      
      - alert: AuditWriteFailure
        expr: rate(audit_write_errors_total[5m]) > 0
        for: 1m
        annotations:
          summary: "Audit write failures detected (CRITICAL)"
```

---

## 🔧 TROUBLESHOOTING

### Common Issues

#### Issue 1: Classification Latency > 500ms

```bash
# Diagnosis
1. Check model inference time:
   SELECT 
     action_type,
     (new_value->'model_execution_time_ms')::int AS inference_ms,
     COUNT(*) as count
   FROM audit.audit_logs
   WHERE timestamp_utc > NOW() - INTERVAL 1 HOUR
   GROUP BY action_type, inference_ms
   ORDER BY inference_ms DESC;

2. Check Redis availability:
   redis-cli PING → Should return PONG

3. Check ML model replicas:
   kubectl get pods -l app=tax-classifier

# Resolution
- Scale classification service: kubectl scale deployment tax-classifier --replicas=250
- Check model cache: Is model loaded in Redis?
- Check database connection pool saturation
```

#### Issue 2: Low Confidence Classifications

```bash
# Diagnosis
1. Check confidence distribution:
   SELECT 
     confidence_score,
     COUNT(*) as count
   FROM compliance.tax_classifications
   WHERE created_at > NOW() - INTERVAL 24 HOURS
   GROUP BY ROUND(confidence_score, 2)
   ORDER BY confidence_score;

2. Check recent model version:
   SELECT model_version, COUNT(*) as count
   FROM compliance.tax_classifications
   WHERE created_at > NOW() - INTERVAL 24 HOURS
   GROUP BY model_version;

# Resolution
- Retrain model (may have data drift):
  ./scripts/retrain-models.sh
- Review recent rule changes (may be conflicting)
```

#### Issue 3: Audit Log Write Failures

```bash
# Diagnosis
1. Check database disk space:
   df -h /var/lib/postgresql

2. Check audit table size:
   SELECT pg_size_pretty(pg_total_relation_size('audit.audit_logs'));

3. Check replication lag:
   SELECT slot_name, restart_lsn FROM pg_replication_slots;

# Resolution
- Trigger compression/archival:
  SELECT compress_chunk(chunk) FROM show_chunks('audit.audit_logs');

- Archive old logs to S3:
  ./scripts/archive-audit-logs.sh --before-date="2024-02-25"
```

---

## 📞 SUPPORT & ESCALATION

| Issue | Severity | Contact | SLA |
|-------|----------|---------|-----|
| Classification success < 95% | CRITICAL | ML Engineering + Ops | 1 hour |
| Audit write failure | CRITICAL | Security + Database Team | 15 min |
| Confidence < 0.60 | HIGH | Compliance Team | 24 hours |
| Review queue backlog > 500 | HIGH | Compliance Manager | 4 hours |
| Latency P99 > 1 sec | MEDIUM | Platform Engineering | 8 hours |
| Low model accuracy | MEDIUM | ML Engineering | 24 hours |

---

**END OF IMPLEMENTATION GUIDE**

*For deployment support, contact: tax-compliance-team@ecoskiller.com*
