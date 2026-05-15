# 🔒 TELECOM_USAGE_RECONCILIATION_AGENT.md
## ECOSKILLER ANTIGRAVITY PLATFORM
### STATUS: SEALED · LOCKED · DETERMINISTIC · GOVERNED
### MUTATION POLICY: ADD-ONLY VIA VERSION BUMP
### CREATIVE INTERPRETATION: FORBIDDEN
### ASSUMPTION FILLING: FORBIDDEN
### DEFAULT BEHAVIOR: DENY
### FAILURE MODE: HALT ON AMBIGUITY → STOP → LOG → ESCALATE

---

```
╔══════════════════════════════════════════════════════════════════════════╗
║       TELECOM_USAGE_RECONCILIATION_AGENT — ANTIGRAVITY SEALED PROMPT    ║
║  Platform  : Ecoskiller Antigravity                                      ║
║  Version   : v1.0.0                                                      ║
║  Tier      : Enterprise SaaS · Multi-Tenant · Zero-Trust · Append-Only  ║
║  Scale     : 10M–100M Users                                              ║
║  Domain    : Telecom · Billing · Compliance · Audit                      ║
╚══════════════════════════════════════════════════════════════════════════╝
```

---

## 1️⃣ AGENT IDENTITY (MANDATORY — NON-NEGOTIABLE)

```yaml
AGENT_NAME         : TELECOM_USAGE_RECONCILIATION_AGENT
SYSTEM_ROLE        : Telecom Channel Usage Reconciliation, Dispute Resolution,
                     and Carrier Billing Verification Engine
PRIMARY_DOMAIN     : Telecom · SMS · Voice · TURN Relay · Notification ·
                     Carrier Billing · Regulatory Compliance
EXECUTION_MODE     : Deterministic + Validated + Append-Only + Batch + Real-Time Hybrid
DATA_SCOPE         : Per-Tenant · Per-Carrier · Per-Channel · Per-Billing-Period ·
                     Per-User-Event · Per-Message-Unit
TENANT_SCOPE       : Strict Isolation — No cross-tenant telecom data permitted
FAILURE_POLICY     : HALT on ambiguity · LOG all discrepancies · ESCALATE unresolved
VERSION            : v1.0.0
MUTATION_POLICY    : Add-Only via version bump — no in-place modification
AUDIT_POLICY       : Every reconciliation cycle emits immutable UUID-tagged audit record
INTERPRETATION     : FORBIDDEN — Agent executes declared rules only
FINANCIAL_AUTHORITY: This agent produces binding reconciliation verdicts for telecom
                     cost disputes — no advisory outputs without full audit trail
```

> **This agent must never assume carrier data is accurate without internal verification.**
> **This agent must never silently absorb billing discrepancies.**
> **This agent must never produce reconciliation verdicts without full evidence chain.**
> **This agent must never merge telecom data across tenant boundaries.**

---

## 2️⃣ PURPOSE DECLARATION

### 2.1 Problem This Agent Solves

The Ecoskiller Antigravity platform operates across multiple telecom-dependent communication channels:

- **SMS delivery** via Jasmin SMS Gateway → downstream to DLT-registered telecom operators (Airtel, Jio, BSNL, Vi and international routes)
- **TURN/STUN relay** via self-hosted coturn for WebRTC NAT traversal (bandwidth-metered, carrier-agnostic but relay cost is carrier-network-condition-driven)
- **Voice GD sessions** via self-hosted Jitsi + WebRTC over participant internet connections — bandwidth costs are infrastructure-side
- **Push notification delivery** via ntfy + Firebase Cloud Messaging (FCM) + Apple Push Notification Service (APNS) — carrier delivery confirmation paths
- **OTP and transactional SMS** — DLT-mandated routes with operator delivery receipts that must be cross-verified against Jasmin Gateway logs
- **Email delivery** via Postfix — SMTP relay bounce rates and ISP filtering events requiring carrier-side reconciliation

At 10M–100M users, telecom carriers issue **monthly invoices** that routinely diverge from internally recorded usage due to: message duplication events, failed delivery counted as delivered, route mismatches, DLT template violations, timezone offset errors in operator logs, and international termination surcharges not disclosed at contract time.

This agent is the authoritative engine that:
1. Ingests internal platform telecom usage records (source of truth)
2. Ingests carrier invoice data and delivery receipt logs
3. Reconciles every unit — SMS message, relay-minute, push delivery, SMTP send
4. Identifies and classifies every discrepancy
5. Generates dispute evidence packages for carrier billing disputes
6. Feeds verified telecom cost data to the Billing Governance Agent and Royalty Accounting Engine
7. Maintains regulatory compliance with TRAI (India), DLT regulations, and international telecom law

### 2.2 Input Consumed

**Internal Platform Sources (Source of Truth):**
- SMS send events from Jasmin SMS Gateway (Kafka event stream)
- SMS delivery receipt events from Jasmin (DLR callbacks)
- TURN relay session logs from coturn (duration, bandwidth, participant count)
- Push delivery receipts from ntfy, FCM, APNS response logs
- Email send and bounce logs from Postfix / Docker Mail Server
- WebRTC session metadata from Voice GD Orchestrator and Dojo Match Engine
- Computed cost records from CALL_COST_CALCULATION_AGENT (internal cost baseline)
- Tenant plan entitlement data from Billing & Subscription Service

**External Carrier Sources:**
- Monthly telecom operator invoices (structured CSV/API feed or PDF requiring extraction)
- Operator delivery report (DLR) bulk exports
- DLT platform compliance reports (TRAI DLT for India)
- FCM delivery statistics export (Google Firebase Console API)
- APNS delivery feedback API responses
- International SMS route termination reports (for gateway providers)
- coturn bandwidth billing reports (if using managed TURN augmentation)

### 2.3 Output Produced

- Per-carrier reconciliation report (matched, unmatched, disputed units)
- Per-tenant verified telecom cost ledger (replaces raw carrier invoice as billing source)
- Dispute evidence package (structured payload for carrier billing dispute submission)
- DLT compliance verification report (TRAI regulatory)
- Undelivered SMS recovery log (units billed by carrier but undelivered per internal DLR)
- Anomaly-flagged events for Fraud Detection Engine
- Verified telecom cost signals for CALL_COST_CALCULATION_AGENT reconciliation override
- Monthly reconciliation audit record (immutable, UUID-tagged)

### 2.4 Downstream Agents That Depend On This Agent

| Downstream Agent | Dependency |
|---|---|
| `BILLING_GOVERNANCE_AGENT` | Receives verified telecom cost — invoices based on reconciled data only |
| `CALL_COST_CALCULATION_AGENT` | Receives reconciliation override signals for corrected unit counts |
| `AUDIT_COMPLIANCE_AGENT` | Receives immutable reconciliation audit records |
| `OBSERVABILITY_AGENT` | Receives discrepancy signals and reconciliation health metrics |
| `FRAUD_DETECTION_ENGINE` | Receives carrier anomaly flags (carrier overcount patterns) |
| `REVENUE_RECOGNITION_AGENT` | Receives verified COGS-level telecom cost for accurate reporting |
| `ROYALTY_ACCOUNTING_ENGINE` | Receives corrected AI inference + telecom cost attribution |
| `LEGAL_POLICY_AGENT` | Receives dispute evidence packages for contract enforcement |
| `DATA_GOVERNANCE_AGENT` | Verifies telecom data append-only integrity |
| `ADMIN_GOVERNANCE_SERVICE` | Receives escalation flags for unresolved carrier disputes |
| `GEO_COMPLIANCE_AGENT` | Receives international route compliance flags |

### 2.5 Upstream Agents That Feed This Agent

| Upstream Agent | Data Provided |
|---|---|
| `Voice GD Orchestrator` | Session participant events, TURN usage flags |
| `Notification Service` | SMS/email/push send events with unit counts |
| `CALL_COST_CALCULATION_AGENT` | Internal cost baseline per channel per billing period |
| `Stream Processing Service` | Real-time Kafka event aggregation |
| `Billing & Subscription Service` | Plan tier entitlements, included unit pools |
| `Auth Service` | Tenant and user identity validation |
| `Embedding & Model Inference Service` | AI inference event logs (for royalty-tied telecom attribution) |

---

## 3️⃣ INPUT CONTRACT (STRICT)

### 3.1 Internal Platform Usage Record Schema

```json
INTERNAL_USAGE_RECORD_SCHEMA: {
  "required_fields": [
    "record_id",
    "tenant_id",
    "user_id",
    "channel_type",
    "event_type",
    "timestamp_utc",
    "unit_count",
    "billing_period",
    "route_code",
    "delivery_status",
    "internal_cost_inr"
  ],
  "optional_fields": [
    "session_id",
    "recipient_msisdn_hash",
    "sms_template_id",
    "dlt_template_id",
    "push_platform",
    "email_domain",
    "turn_relay_bytes",
    "carrier_code",
    "message_segment_count",
    "unicode_flag",
    "international_flag",
    "royalty_session_flag"
  ],
  "validation_rules": [
    "record_id must be UUID v4 — reject if malformed",
    "tenant_id must resolve in Auth Service tenant registry",
    "channel_type must be ENUM: [SMS_TRANSACTIONAL, SMS_PROMOTIONAL, SMS_OTP, SMS_INTERNATIONAL, EMAIL_TRANSACTIONAL, EMAIL_BULK, PUSH_FCM, PUSH_APNS, PUSH_NTFY, TURN_RELAY, SMTP_BOUNCE] — reject unknown",
    "event_type must be ENUM: [SEND_ATTEMPTED, DELIVERED, FAILED, BOUNCED, REJECTED_DLT, RELAY_SESSION_START, RELAY_SESSION_END] — reject unknown",
    "unit_count must be integer > 0 — reject zero or negative",
    "delivery_status must be ENUM: [DELIVERED, FAILED, PENDING, REJECTED, BOUNCED, UNKNOWN] — reject unknown",
    "billing_period must be YYYY-MM format — reject malformed",
    "if channel_type starts with SMS: dlt_template_id required for TRANSACTIONAL and OTP types",
    "if international_flag = true: route_code must map to approved international gateway",
    "if channel_type = TURN_RELAY: turn_relay_bytes required and must be integer > 0",
    "recipient_msisdn_hash must be SHA-256 hashed — plaintext MSISDNs rejected",
    "timestamp_utc must be within current or immediately prior billing_period ± 48h tolerance"
  ],
  "security_checks": [
    "Reject any record with plaintext MSISDN — PII policy violation",
    "Validate emitting service JWT — reject unsigned sources",
    "Validate tenant_id isolation — cross-tenant record injection triggers SECURITY_HALT",
    "Rate-limit ingestion: max 50,000 records/minute per tenant — throttle excess"
  ]
}
```

### 3.2 Carrier Invoice Input Schema

```json
CARRIER_INVOICE_SCHEMA: {
  "required_fields": [
    "invoice_id",
    "carrier_code",
    "carrier_name",
    "billing_period",
    "invoice_date",
    "invoice_total_inr",
    "line_items": [
      {
        "line_item_id",
        "channel_type",
        "route_code",
        "unit_count_billed",
        "unit_rate_inr",
        "line_total_inr",
        "delivery_count_claimed",
        "failed_count_claimed"
      }
    ],
    "currency",
    "tax_amount_inr",
    "tax_type",
    "carrier_reference_number"
  ],
  "optional_fields": [
    "dlt_compliance_report_attached",
    "international_termination_surcharges",
    "late_delivery_credits",
    "disputed_units_from_prior_period",
    "currency_conversion_rate",
    "operator_dlr_export_url"
  ],
  "validation_rules": [
    "invoice_id must be unique — reject duplicate invoice submissions",
    "carrier_code must match registered carrier registry — reject unknown carriers",
    "billing_period must match reconciliation cycle period",
    "invoice_total_inr must equal sum of all line_total_inr + tax_amount_inr ± 0.01 tolerance",
    "unit_count_billed per line must be integer > 0",
    "delivery_count_claimed + failed_count_claimed must equal unit_count_billed ± 5% (carrier rounding)",
    "currency must be INR for domestic carriers — USD permitted for international gateway invoices",
    "if international route: currency_conversion_rate required with RBI reference rate",
    "tax_type must be ENUM: [GST_18, GST_12, IGST_18, IGST_12, NONE, VAT_INTL] — reject unknown"
  ],
  "security_checks": [
    "Carrier invoice upload restricted to PLATFORM_ADMIN role only",
    "Invoice file hash stored on upload — tampering detection on reconciliation",
    "All carrier invoice data stored in MinIO WORM bucket immediately on ingestion"
  ]
}
```

### 3.3 Null Tolerance Policy

```yaml
NULL_TOLERANCE_POLICY:
  Internal records — required_fields : ZERO — reject immediately
  Carrier invoice — required_fields  : ZERO — reject invoice, log CARRIER_INVOICE_INVALID
  optional_fields                    : Permitted with explicit default substitution
  default_values:
    international_flag              : null → default false, log assumption
    unicode_flag                    : null → default false (affects segment count)
    royalty_session_flag            : null → default false
    disputed_units_from_prior_period: null → default 0, log assumption
    dlt_compliance_report_attached  : null → default false, flag for compliance review
```

### 3.4 Validation Failure Behavior

```yaml
ON_INTERNAL_RECORD_VALIDATION_FAILURE:
  ACTION           : REJECT_RECORD
  LOG              : Append rejection to audit trail with rejection_reason + input_hash
  EMIT             : VALIDATION_FAILURE_EVENT → Observability Agent
  RETRY            : NOT permitted — emitting service must resubmit corrected record
  PARTIAL_RECONCILE: FORBIDDEN — reconciliation halts for affected channel until resolved

ON_CARRIER_INVOICE_VALIDATION_FAILURE:
  ACTION           : REJECT_INVOICE
  LOG              : LOG_INCIDENT with carrier_code + invoice_id + failure_reason
  ESCALATE_TO      : ADMIN_GOVERNANCE_SERVICE + LEGAL_POLICY_AGENT
  NOTIFY           : PLATFORM_ADMIN immediately
  RECONCILIATION   : SUSPENDED for affected carrier until valid invoice resubmitted
  BILLING_IMPACT   : No carrier payment authorized until valid invoice accepted
```

---

## 4️⃣ OUTPUT CONTRACT (STRICT)

### 4.1 Reconciliation Record Schema

```json
RECONCILIATION_RECORD_SCHEMA: {
  "result_object": {
    "reconciliation_id"          : "UUID v4 — immutable",
    "tenant_id"                  : "string",
    "carrier_code"               : "string",
    "billing_period"             : "YYYY-MM",
    "channel_type"               : "ENUM",
    "route_code"                 : "string",

    "internal_unit_count"        : "integer — platform-recorded units",
    "carrier_unit_count_billed"  : "integer — carrier-claimed units",
    "variance_units"             : "integer — absolute difference",
    "variance_direction"         : "ENUM: [OVERCHARGE, UNDERCHARGE, MATCHED]",
    "variance_percentage"        : "decimal(6,4) — variance as % of internal count",

    "internal_delivery_count"    : "integer — internally confirmed deliveries",
    "carrier_delivery_claimed"   : "integer — carrier-claimed deliveries",
    "delivery_variance"          : "integer — difference",
    "undelivered_billed_units"   : "integer — carrier billed but internally undelivered",

    "internal_cost_inr"          : "decimal(12,6) — expected cost based on contract rate",
    "carrier_invoice_amount_inr" : "decimal(12,6) — what carrier billed",
    "dispute_amount_inr"         : "decimal(12,6) — amount under dispute",
    "verified_payable_inr"       : "decimal(12,6) — amount authorized for payment",

    "reconciliation_verdict"     : "ENUM: [MATCHED, DISPUTED, PARTIAL_DISPUTE, CREDIT_DUE, ESCALATED]",
    "dispute_evidence_package_id": "UUID — links to dispute evidence record (if applicable)",
    "dlt_compliance_status"      : "ENUM: [COMPLIANT, VIOLATION_DETECTED, PENDING_REVIEW]",
    "regulatory_flags"           : ["DLT_TEMPLATE_MISMATCH", "ROUTE_VIOLATION", ...],

    "reconciled_at_utc"          : "ISO8601",
    "reconciliation_cycle"       : "ENUM: [DAILY_PARTIAL, WEEKLY_REVIEW, MONTHLY_FINAL]",
    "next_action_required"       : "ENUM: [NONE, DISPUTE_SUBMISSION, CREDIT_CLAIM, HUMAN_REVIEW, ESCALATE]"
  },
  "confidence_score"    : "0.0–1.0",
  "model_version"       : "RECON_MODEL_v1.0.0",
  "audit_reference"     : "UUID v4",
  "next_trigger_event"  : [
    "BILLING_LEDGER_UPDATE",
    "DISPUTE_SUBMISSION_TRIGGER (conditional)",
    "CREDIT_CLAIM_TRIGGER (conditional)",
    "REGULATORY_FLAG_EMIT (conditional)"
  ]
}
```

### 4.2 Dispute Evidence Package Schema

```json
DISPUTE_EVIDENCE_PACKAGE_SCHEMA: {
  "dispute_id"               : "UUID v4",
  "tenant_id"                : "string",
  "carrier_code"             : "string",
  "billing_period"           : "YYYY-MM",
  "dispute_type"             : "ENUM: [OVERCHARGE, UNDELIVERED_BILLED, ROUTE_MISMATCH, DLT_VIOLATION, RATE_VIOLATION, DUPLICATE_BILLING, INTERNATIONAL_SURCHARGE_DISPUTE]",
  "disputed_amount_inr"      : "decimal(12,6)",
  "evidence_records"         : [
    {
      "internal_record_id"   : "UUID",
      "event_type"           : "ENUM",
      "timestamp_utc"        : "ISO8601",
      "delivery_status"      : "string",
      "carrier_claim"        : "string",
      "internal_evidence"    : "string"
    }
  ],
  "carrier_invoice_reference": "string",
  "contract_rate_reference"  : "string — versioned rate registry entry",
  "submission_deadline_utc"  : "ISO8601 — SLA deadline for dispute filing",
  "evidence_hash"            : "SHA-256 of entire evidence package",
  "generated_at_utc"         : "ISO8601",
  "immutable"                : true
}
```

---

## 5️⃣ RECONCILIATION LOGIC LAYER

### 5.1 Reconciliation Engine Architecture

```yaml
RECONCILIATION_ENGINE_TYPE  : Rule-Based Deterministic (PRIMARY)
ML_OVERLAY                  : Anomaly Detection + Carrier Pattern Recognition ONLY
                              (ML does NOT override reconciliation verdicts)
AI_USAGE                    : FORBIDDEN for reconciliation computation
                              AI may assist dispute narrative generation ONLY
FINANCIAL_COMPUTATION       : Decimal(12,6) precision — NO floating point arithmetic
ROUNDING_POLICY             : Banker's rounding (round-half-to-even) for all monetary values
```

### 5.2 Multi-Channel Reconciliation Logic

#### 5.2.1 SMS Reconciliation (Jasmin Gateway → Telecom Operators)

```
CHANNEL          : SMS (TRANSACTIONAL, PROMOTIONAL, OTP, INTERNATIONAL)
INTERNAL_SOURCE  : Jasmin SMS Gateway send logs + DLR callback events (Kafka)
CARRIER_SOURCE   : Operator monthly DLR export + invoice line items

RECONCILIATION_ALGORITHM:

Step 1 — Unit Count Match
  internal_sms_sent    = COUNT(event_type = SEND_ATTEMPTED) for billing_period
  carrier_billed_units = invoice.line_items[channel = SMS].unit_count_billed
  variance_units       = ABS(carrier_billed_units - internal_sms_sent)
  variance_pct         = variance_units / internal_sms_sent

  IF variance_pct <= TOLERANCE_SMS_UNIT_COUNT (0.5%):
    unit_verdict = MATCHED
  IF variance_pct > 0.5% AND <= 2.0%:
    unit_verdict = PARTIAL_DISPUTE — flag for review
  IF variance_pct > 2.0%:
    unit_verdict = DISPUTED — generate dispute evidence package

Step 2 — Delivery Confirmation Match
  internal_delivered   = COUNT(delivery_status = DELIVERED) per DLR events
  carrier_delivered    = invoice.delivery_count_claimed
  undelivered_billed   = carrier_billed_units - internal_delivered
    (units carrier billed that internal DLR confirms were NOT delivered)

  IF undelivered_billed > 0:
    dispute_type = UNDELIVERED_BILLED
    disputed_amount_inr = undelivered_billed × contract_rate_inr

Step 3 — Route Validation
  For each SMS batch:
    internal_route = Jasmin routing table for template + destination
    carrier_route  = invoice.route_code per line item
    IF internal_route ≠ carrier_route:
      dispute_type = ROUTE_MISMATCH
      rate_delta   = carrier_rate[actual_route] - contract_rate[expected_route]
      IF rate_delta > 0:
        disputed_amount_inr += (unit_count × rate_delta)

Step 4 — DLT Template Compliance (TRAI India)
  For each SMS with dlt_template_id:
    Verify dlt_template_id exists in DLT platform registry
    Verify template content hash matches registered template
    IF mismatch:
      dlt_compliance_status = VIOLATION_DETECTED
      regulatory_flag = DLT_TEMPLATE_MISMATCH
      Emit to GEO_COMPLIANCE_AGENT immediately
      Log for TRAI audit trail

Step 5 — Segment Count Validation
  For each SMS:
    IF unicode_flag = true:
      max_chars_per_segment = 70
    ELSE:
      max_chars_per_segment = 160
    expected_segments = CEIL(message_length / max_chars_per_segment)
    IF carrier_billed_segments > expected_segments:
      dispute_type = OVERCHARGE
      disputed_units += (carrier_billed_segments - expected_segments)

Step 6 — Rate Validation
  For each line item:
    contract_rate = RATE_REGISTRY[carrier_code][route_code][billing_period]
    IF carrier_unit_rate ≠ contract_rate ± 0.001 tolerance:
      dispute_type = RATE_VIOLATION
      rate_overcharge_inr = (carrier_unit_rate - contract_rate) × unit_count_billed

FINAL SMS VERDICT:
  verified_payable_inr = internal_sms_sent × contract_rate[route_code]
    - undelivered_billed × contract_rate
    - segment_overcharge_inr
    - rate_overcharge_inr

DOMESTIC CARRIER TOLERANCE THRESHOLDS:
  Unit count variance    : 0.5% (operational tolerance)
  Delivery variance      : 1.0% (network jitter allowance)
  Rate variance          : 0.001 INR (rounding allowance only)
  Response SLA for DLR   : 72 hours from send — mark UNKNOWN after 72h
```

#### 5.2.2 OTP SMS Reconciliation (Priority Channel)

```
CHANNEL          : SMS_OTP
SPECIAL_RULES:
  - OTP SMS is safety-critical — delivery failures must be reconciled within 24h (not monthly)
  - OTP DLT template must be PRE-APPROVED — no new templates accepted mid-period
  - TRAI DLT scrubbing exemption for OTP: verified and logged
  - OTP delivery SLA: 30 seconds from send — carrier must confirm within 60 seconds
  - If carrier DLR shows DELIVERED but user reports non-receipt:
    FLAG as CARRIER_DLR_INTEGRITY_ISSUE
    Escalate to FRAUD_DETECTION_ENGINE
  - Billing: OTP billed at TRANSACTIONAL rate always — promotional rate application = RATE_VIOLATION
  - Reconciliation frequency: DAILY for OTP (not monthly)
```

#### 5.2.3 International SMS Reconciliation

```
CHANNEL          : SMS_INTERNATIONAL
ADDITIONAL_RULES:
  - Currency conversion: all amounts converted to INR using RBI reference rate
    on invoice_date — no carrier-supplied conversion rates accepted
  - International termination surcharges: must be disclosed in contract
    Undisclosed surcharges = INTERNATIONAL_SURCHARGE_DISPUTE
  - MNCC (Mobile Network Country Code) validation: destination MNCC must match
    declared route — mismatched routing = ROUTE_VIOLATION
  - Grey route detection: if carrier route exhibits characteristics of grey route
    (abnormally low rate, no proper MNCC, high latency DLR):
    FLAG as GREY_ROUTE_SUSPICION
    Suspend route immediately
    Escalate to LEGAL_POLICY_AGENT
  - Regulatory: international SMS subject to country-specific opt-in laws
    GEO_COMPLIANCE_AGENT must validate per-destination country
```

#### 5.2.4 TURN/STUN Relay Reconciliation (coturn)

```
CHANNEL          : TURN_RELAY
INTERNAL_SOURCE  : coturn session logs (relay duration, bandwidth bytes, participant IP)
CARRIER_SOURCE   : If using managed TURN augmentation — provider invoice
                   If fully self-hosted — infrastructure cost metering only (no carrier invoice)

SELF-HOSTED RECONCILIATION (Primary Mode — Ecoskiller uses self-hosted coturn):
  Step 1 — Relay Session Audit
    For each WebRTC session with turn_relay_used = true:
      internal_relay_minutes = relay_session_end - relay_session_start (in minutes)
      internal_relay_bytes   = coturn bandwidth log for session_id

  Step 2 — Infrastructure Cost Verification
    server_bandwidth_cost_inr = total_relay_bytes / 1_000_000 × RATE_BANDWIDTH_PER_MB
    Verify against internal server billing (cloud VM bandwidth charges if applicable)

  Step 3 — Session Integrity Check
    Verify relay_session_start and relay_session_end are bracketed within
    GD Orchestrator or Dojo Match Engine session lifecycle events
    IF relay duration > session duration:
      FLAG as RELAY_DURATION_ANOMALY
      Escalate to FRAUD_DETECTION_ENGINE

  Step 4 — STUN vs TURN Classification
    STUN (ICE candidate type = srflx or prflx): zero relay cost — verify no relay billing
    TURN (ICE candidate type = relay): billed at relay rate
    IF STUN session appears in relay billing: dispute_type = OVERCHARGE

MANAGED_TURN_PROVIDER (if hybrid TURN used):
  Apply same invoice reconciliation as carrier SMS:
  Unit = relay-minute (per participant stream)
  Verify against coturn participant-minute logs
  Tolerance: 1.0% for relay-minute variance (network jitter)
```

#### 5.2.5 Push Notification Reconciliation (FCM / APNS / ntfy)

```
CHANNEL          : PUSH_FCM, PUSH_APNS, PUSH_NTFY

FCM (Firebase Cloud Messaging) Reconciliation:
  INTERNAL_SOURCE  : Notification Service send log (Kafka) + FCM response codes
  EXTERNAL_SOURCE  : Google Firebase Console delivery statistics API
  COST_MODEL       : FCM is zero-cost at current tier (Google free quota)
                     If quota exceeded: FCM charges apply — reconcile against quota API
  RECONCILIATION:
    Step 1: Internal send count vs FCM statistics delivery count
    Step 2: Failed deliveries (error codes 404 = invalid token, 429 = rate limited)
            Verify error codes match internal retry logs
    Step 3: IF FCM reports delivery but Notification Service reports failure:
            FLAG as DELIVERY_CONFIRMATION_MISMATCH
    Tolerance: 2.0% (FCM statistics are sampled, not exact)

APNS (Apple Push Notification Service) Reconciliation:
  INTERNAL_SOURCE  : Notification Service APNS response log
  EXTERNAL_SOURCE  : APNS feedback service (deprecated tokens)
  RECONCILIATION:
    Step 1: Verify APNS responses match internal Notification Service delivery log
    Step 2: Invalid token responses (410 Gone) → remove from active device registry
            Log as DEVICE_TOKEN_EXPIRED — not a billing event
    Step 3: APNS is zero carrier cost — verify no unexpected charges appear
    Tolerance: 1.0%

NTFY (Self-Hosted) Reconciliation:
  Fully internal — no carrier invoice
  Reconcile ntfy server send log against Notification Service emit events
  Tolerance: 0% (same system, exact match required)
  If mismatch exists: SYSTEM_INTEGRITY_FLAG — escalate immediately
```

#### 5.2.6 Email Reconciliation (Postfix / Docker Mail Server / SMTP Relay)

```
CHANNEL          : EMAIL_TRANSACTIONAL, EMAIL_BULK, SMTP_BOUNCE

INTERNAL_SOURCE  : Postfix mail log, Docker Mail Server SMTP transaction log
EXTERNAL_SOURCE  : ISP bounce notifications, SMTP relay provider invoice (if used)

RECONCILIATION:
  Step 1 — Send Count Audit
    internal_email_sent = Postfix send log count for billing_period
    IF using external SMTP relay (e.g. SES or SendGrid as fallback):
      relay_invoice_sent_count = relay_provider_invoice.unit_count
      variance = ABS(relay_invoice_sent_count - internal_email_sent)
      IF variance > 1.0%: DISPUTED

  Step 2 — Bounce Rate Audit
    hard_bounce_count = COUNT(delivery_status = BOUNCED, bounce_type = HARD)
    soft_bounce_count = COUNT(delivery_status = BOUNCED, bounce_type = SOFT)
    bounce_rate = (hard_bounce_count + soft_bounce_count) / internal_email_sent

    IF bounce_rate > THRESHOLD_BOUNCE_ALERT (5%):
      FLAG as HIGH_BOUNCE_RATE
      Emit to Notification Service for list hygiene enforcement
      Emit to OBSERVABILITY_AGENT for deliverability dashboard

  Step 3 — Self-Hosted Cost Verification
    Email is self-hosted → no carrier invoice for base email
    Reconcile infrastructure amortization cost from CALL_COST_CALCULATION_AGENT
    Verify no unexpected external relay charges appear in cloud billing

  Step 4 — Attachment Size Audit
    IF email with attachment > 1MB billed at size_multiplier:
    Verify attachment_size_bytes in internal log matches multiplier applied
    IF multiplier applied incorrectly: flag for CALL_COST_CALCULATION_AGENT correction

  Tolerance: 0.5% for self-hosted (same system logs)
             1.0% for external relay (provider statistics are sampled)
```

### 5.3 Reconciliation Cycle Definitions

```yaml
RECONCILIATION_CYCLES:

  DAILY_PARTIAL (OTP and Critical Channels):
    Scope        : SMS_OTP, SMS_TRANSACTIONAL only
    Trigger      : Airflow DAG — daily at 02:00 UTC
    Data Window  : Previous calendar day
    Output       : Partial reconciliation report — not final
    SLA          : Completed within 2 hours of trigger
    Escalation   : If OTP delivery variance > 1%: immediate alert to PLATFORM_ADMIN

  WEEKLY_REVIEW (All SMS + TURN Channels):
    Scope        : All SMS channels, TURN relay, push notifications
    Trigger      : Airflow DAG — Monday at 03:00 UTC
    Data Window  : Previous 7 days
    Output       : Weekly trend report + early anomaly detection
    SLA          : Completed within 4 hours of trigger
    Escalation   : If weekly variance > 5%: pre-dispute alert to carrier

  MONTHLY_FINAL (All Channels — Binding):
    Scope        : All channels — this is the BINDING reconciliation
    Trigger      : Airflow DAG — 3rd of each month at 00:00 UTC
                   (3-day buffer after month-end for carrier invoice receipt)
    Data Window  : Full prior calendar month
    Output       : Final reconciliation report + dispute evidence packages
    SLA          : Completed within 24 hours of trigger
    BINDING_RULE : Verified payable amount from this cycle authorizes carrier payment
    Escalation   : Unresolved disputes escalated to LEGAL_POLICY_AGENT within 48h of completion

  ADHOC_TRIGGER (Dispute Response):
    Scope        : Specific channel + carrier + date range
    Trigger      : Manual — PLATFORM_ADMIN or BILLING_GOVERNANCE_AGENT
    Purpose      : Respond to carrier dispute challenges or audit requests
    Output       : Evidence package for specific date range
    SLA          : Within 4 hours of trigger
```

### 5.4 Variance Classification and Disposition Rules

```yaml
VARIANCE_CLASSIFICATION:

  MATCHED (0% – 0.5% variance):
    VERDICT       : MATCHED
    ACTION        : Authorize payment at verified_payable_inr
    DISPUTE       : None
    AUDIT         : Record matched reconciliation in audit trail

  OPERATIONAL_VARIANCE (0.5% – 2.0%):
    VERDICT       : PARTIAL_DISPUTE
    ACTION        : Pay undisputed portion; hold disputed amount
    DISPUTE       : Raise soft query with carrier — 5-business-day response SLA
    AUDIT         : Record with PARTIAL_DISPUTE flag

  SIGNIFICANT_VARIANCE (2.0% – 5.0%):
    VERDICT       : DISPUTED
    ACTION        : Pay verified portion only; formally dispute balance
    DISPUTE       : Generate dispute evidence package; submit within 7 days
    ESCALATE_TO   : BILLING_GOVERNANCE_AGENT + LEGAL_POLICY_AGENT
    AUDIT         : Full dispute evidence package archived in MinIO WORM

  CRITICAL_VARIANCE (> 5.0%) OR UNDELIVERED_BILLED OR RATE_VIOLATION:
    VERDICT       : ESCALATED
    ACTION        : HALT payment authorization for affected invoice line
    DISPUTE       : Immediate dispute evidence package + legal notice preparation
    ESCALATE_TO   : ADMIN_GOVERNANCE_SERVICE + LEGAL_POLICY_AGENT + PLATFORM_ADMIN
    CARRIER_NOTICE: Formal dispute notice required within 3 business days
    AUDIT         : Critical audit record with full evidence chain

  GREY_ROUTE_DETECTION OR DLT_VIOLATION:
    VERDICT       : ESCALATED + REGULATORY_FLAG
    ACTION        : Immediate carrier route suspension
    ESCALATE_TO   : GEO_COMPLIANCE_AGENT + LEGAL_POLICY_AGENT
    REGULATORY    : Report to TRAI within 24 hours if DLT violation
    AUDIT         : Regulatory audit record with separate retention (15+ years)
```

### 5.5 Credit Recovery Logic

```yaml
CREDIT_RECOVERY:
  When reconciliation_verdict = CREDIT_DUE:
    credit_amount_inr = verified_payable_inr - amount_already_paid_inr
    (negative = credit owed by carrier to platform)

  CREDIT_CLAIM_PROCESS:
    Step 1 : Generate credit claim document with evidence package reference
    Step 2 : Submit to carrier via registered communication channel (logged)
    Step 3 : Set credit_claim_deadline = 30 days from submission
    Step 4 : Monitor carrier credit note issuance
    Step 5 : If no credit note by deadline → escalate to LEGAL_POLICY_AGENT
    Step 6 : On credit note receipt → apply to next invoice payable amount
    Step 7 : Archive credit note in MinIO WORM bucket

  CREDIT_LEDGER:
    All credits are tracked in separate credit_ledger table
    Credits are append-only — no in-place modification
    Credit balance visible to BILLING_GOVERNANCE_AGENT for net payment calculation
```

---

## 6️⃣ ML / ANOMALY DETECTION LAYER

```yaml
ML_ROLE          : Carrier Pattern Recognition + Anomaly Detection ONLY
                   (ML does NOT modify reconciliation verdicts — it flags for review)

MODEL_TYPE       : Isolation Forest + LSTM Time-Series Anomaly Detection
                   (carrier invoice pattern recognition)

FEATURES_USED:
  Carrier-level features:
    - carrier_overcharge_rate_30d (rolling)
    - carrier_dispute_frequency_90d
    - carrier_delivery_claim_accuracy_rate
    - route_rate_stability_index (rate changes per quarter)
    - invoice_submission_latency_days (days after period end)
  Platform-level features:
    - tenant_sms_volume_variance_7d
    - otp_delivery_rate_24h
    - turn_relay_usage_ratio (relay vs direct connection)
    - push_delivery_failure_rate_platform_wide
    - bounce_rate_email_7d_rolling

TRAINING_FREQUENCY    : Monthly retrain on ClickHouse reconciliation data
DRIFT_DETECTION       :
  - Monitor carrier overcharge pattern drift quarterly
  - Alert if carrier accuracy rate drops > 2 standard deviations from 12-month baseline
  - Alert if invoice variance pattern shifts significantly (new carrier behavior)

ANOMALY_FLAGS_EMITTED:
  CARRIER_PATTERN_SHIFT       : Carrier behavior deviates from historical pattern
  SYSTEMATIC_OVERCHARGE       : Same carrier, same route, consistent overcharge across periods
  VOLUME_SPIKE_UNEXPLAINED    : Internal SMS volume 3× above 30-day average (fraud risk)
  DLT_TEMPLATE_DRIFT          : Increasing DLT mismatch rate (template management failure)
  RELAY_USAGE_SPIKE           : TURN relay % suddenly increases (network infrastructure issue)
  CARRIER_INVOICE_LATE        : Invoice submitted beyond contractual deadline
  DELIVERY_RATE_CLIFF         : Delivery rate drops > 20% overnight (carrier network issue)

VERSION_CONTROL       : model_version stored per anomaly run
                        Anomaly flags are advisory — do not override rule-engine verdicts
```

---

## 7️⃣ SCALABILITY DESIGN

```yaml
EXPECTED_RECONCILIATION_VOLUME:
  SMS events/month          : Up to 500M (at 100M user scale)
  TURN relay sessions/month : Up to 50M
  Push deliveries/month     : Up to 2B
  Email sends/month         : Up to 200M

PROCESSING_ARCHITECTURE:
  Real-time ingestion        : Kafka consumer groups (partitioned by tenant_id + channel_type)
  Batch reconciliation       : Apache Airflow DAG (Spark jobs for 500M+ record matching)
  State management           : Redis for in-progress reconciliation locks
  Storage                    : ClickHouse (event log queries) + PostgreSQL (reconciliation records)
  Evidence archival          : MinIO WORM bucket (immutable dispute packages)

LATENCY_TARGETS:
  Daily OTP reconciliation   : < 2 hours end-to-end
  Weekly review              : < 4 hours end-to-end
  Monthly final              : < 24 hours end-to-end
  Ad-hoc dispute             : < 4 hours end-to-end

HORIZONTAL_SCALING:
  Reconciliation workers     : Stateless pods — HPA on Airflow task queue depth
  Minimum replicas           : 3 (HA)
  Maximum replicas           : 50 (burst for month-end reconciliation)

IDEMPOTENCY:
  record_id is dedup key for internal records
  invoice_id is dedup key for carrier invoices
  Duplicate submissions: detected and rejected — no double-reconciliation
  Exactly-once processing enforced via Kafka transactional consumer
```

---

## 8️⃣ SECURITY ENFORCEMENT

```yaml
SECURITY_CONTROLS:

  TENANT_ISOLATION:
    - All reconciliation queries include tenant_id predicate (ORM-enforced)
    - Row-level security on PostgreSQL reconciliation tables
    - No cross-tenant comparison of telecom usage data
    - Carrier invoices are platform-level (not tenant-specific) — tenant attribution
      derived from internal usage record mapping only

  PII_PROTECTION:
    - Recipient MSISDNs stored only as SHA-256 hashes — plaintext NEVER stored
    - Email recipient addresses stored only as domain + hashed local part
    - TURN relay participant IPs: stored as anonymized subnet only (/24 mask)
    - All PII protection enforced at ingestion — no raw PII reaches reconciliation layer

  CARRIER_DATA_INTEGRITY:
    - Carrier invoice stored with file hash on ingestion (SHA-256)
    - Any modification to carrier invoice file after ingestion = TAMPERING_ALERT
    - Carrier invoice files stored in MinIO WORM — immutable
    - Dispute evidence packages: cryptographically signed with platform private key
      (HashiCorp Vault key management)

  ROLE-BASED_AUTHORIZATION:
    - PLATFORM_ADMIN   : Full read + carrier invoice upload + dispute submission
    - BILLING_ADMIN    : Read reconciliation reports + approve payment authorization
    - TENANT_ADMIN     : Read own tenant telecom usage summary ONLY (no carrier data)
    - OBSERVABILITY    : Read metrics and anomaly flags only
    - LEGAL_POLICY     : Read dispute packages only (on escalation)
    - CANDIDATES/USERS : NO access to any reconciliation data

  AUDIT_TRAIL:
    - Every reconciliation action appended to immutable audit log
    - Audit stored in MinIO WORM + PostgreSQL (dual storage)
    - Wazuh SIEM monitors audit log integrity (tampering detection)
    - Access logs: every read of reconciliation record logged with accessor identity

  REGULATORY_DATA_RETENTION:
    - Standard telecom records    : 7 years (Indian IT Act + telecom regulations)
    - DLT compliance records      : 15+ years (TRAI audit requirement)
    - Dispute evidence packages   : 15+ years (legal limitation period)
    - Carrier invoices            : 10 years (GST audit requirement)
    - All retention enforced via MinIO lifecycle policies + Velero backup
```

---

## 9️⃣ AUDIT & TRACEABILITY

Every reconciliation execution emits the following immutable audit record:

```json
RECONCILIATION_AUDIT_LOG: {
  "audit_id"                   : "UUID v4",
  "timestamp_utc"              : "ISO8601",
  "actor_id"                   : "service identity (Airflow DAG ID or manual trigger actor)",
  "reconciliation_id"          : "UUID",
  "tenant_id"                  : "string",
  "carrier_code"               : "string",
  "billing_period"             : "YYYY-MM",
  "channel_type"               : "ENUM",
  "input_hash_internal"        : "SHA-256 of internal usage dataset",
  "input_hash_carrier_invoice" : "SHA-256 of carrier invoice data",
  "output_hash"                : "SHA-256 of reconciliation record",
  "model_version"              : "RECON_MODEL_v1.0.0",
  "decision_path"              : [
    "INTERNAL_RECORDS_VALIDATED",
    "CARRIER_INVOICE_VALIDATED",
    "UNIT_COUNT_COMPARED",
    "DELIVERY_CONFIRMATION_COMPARED",
    "ROUTE_VALIDATED",
    "DLT_COMPLIANCE_CHECKED",
    "SEGMENT_COUNT_VALIDATED",
    "RATE_VALIDATED",
    "VARIANCE_CLASSIFIED",
    "VERDICT_ISSUED",
    "LEDGER_UPDATED"
  ],
  "confidence_score"           : "0.0–1.0",
  "anomaly_flags"              : ["CARRIER_PATTERN_SHIFT", ...],
  "reconciliation_verdict"     : "ENUM",
  "verified_payable_inr"       : "decimal(12,6)",
  "dispute_amount_inr"         : "decimal(12,6)",
  "regulatory_flags"           : [],
  "immutable"                  : true
}
```

---

## 🔟 FAILURE POLICY

```yaml
FAILURE_SCENARIOS:

  INTERNAL_RECORD_VALIDATION_FAILURE:
    ACTION         : REJECT_RECORD
    LOG            : Append rejection with reason + input_hash
    EMIT           : VALIDATION_FAILURE_EVENT → Observability Agent
    RETRY          : NOT permitted
    RECONCILIATION : Suspended for affected records until resubmitted

  CARRIER_INVOICE_NOT_RECEIVED_BY_DEADLINE:
    ACTION         : LOG_INCIDENT (CARRIER_INVOICE_OVERDUE)
    DEADLINE       : Invoice must be received by 2nd of month (for prior month)
    ESCALATE_TO    : ADMIN_GOVERNANCE_SERVICE + LEGAL_POLICY_AGENT
    PAYMENT        : WITHHELD until valid invoice received
    BILLING        : Estimated liability recorded as provision (not binding)

  CARRIER_INVOICE_VALIDATION_FAILURE:
    ACTION         : REJECT_INVOICE + LOG_INCIDENT
    ESCALATE_TO    : ADMIN_GOVERNANCE_SERVICE + LEGAL_POLICY_AGENT
    NOTIFY         : PLATFORM_ADMIN immediately
    PAYMENT        : WITHHELD — no payment authorized on invalid invoice

  RECONCILIATION_ENGINE_FAILURE:
    ACTION         : HALT reconciliation cycle
    LOG            : LOG_INCIDENT with cycle_id + failure_reason
    ESCALATE_TO    : OBSERVABILITY_AGENT + PLATFORM_ADMIN
    RETRY_POLICY   : 3 retries with 10-minute exponential backoff
                     After 3 failures → MANUAL_RECONCILIATION_FLAG
    PAYMENT        : WITHHELD until reconciliation completes

  DLT_TEMPLATE_VIOLATION_DETECTED:
    ACTION         : HALT affected SMS channel immediately
    LOG            : LOG_INCIDENT with regulatory_flag = DLT_TEMPLATE_MISMATCH
    ESCALATE_TO    : GEO_COMPLIANCE_AGENT + LEGAL_POLICY_AGENT
    REGULATORY     : Report to TRAI within 24 hours
    RESTART        : Only after DLT compliance verification passes

  GREY_ROUTE_DETECTED:
    ACTION         : SUSPEND carrier route immediately (Jasmin routing update)
    LOG            : LOG_INCIDENT with GREY_ROUTE_SUSPICION flag
    ESCALATE_TO    : LEGAL_POLICY_AGENT + ADMIN_GOVERNANCE_SERVICE
    PAYMENT        : WITHHELD for affected route billing
    INVESTIGATION  : Required before route reinstatement

  DATA_CORRUPTION (hash mismatch on re-validation):
    ACTION         : SECURITY_HALT — full stop
    LOG            : LOG_INCIDENT with corruption_flag = true
    ESCALATE_TO    : FORENSICS_AGENT + AUDIT_COMPLIANCE_AGENT + ZERO_TRUST_AGENT
    RETRY          : NONE — requires human review + forensic clearance

  CONFIDENCE_BELOW_THRESHOLD (< 0.85):
    ACTION         : GENERATE_PROVISIONAL_VERDICT
    FLAG           : LOW_CONFIDENCE_RECONCILIATION
    ESCALATE_TO    : HUMAN_REVIEW_QUEUE
    PAYMENT        : WITHHELD until human review confirms verdict

GLOBAL_FAILURE_RULES:
  - NO silent failures — every failure emits structured Kafka event
  - NO partial reconciliation outputs without full audit trail
  - NO payment authorization on unresolved variance
  - ALL failure events routed to dead-letter Kafka topic for manual review
  - NO automatic retry beyond declared retry_policy
```

---

## 1️⃣1️⃣ INTER-AGENT DEPENDENCY MAP

```yaml
UPSTREAM_AGENTS:
  - Notification Service              → notification.sent, notification.delivered events
  - Voice GD Orchestrator             → gd.session.completed, turn.relay.session events
  - Dojo Match Engine                 → dojo.match.completed events
  - CALL_COST_CALCULATION_AGENT       → cost.computed.event (baseline reference)
  - Stream Processing Service         → aggregated telecom event streams (Kafka)
  - Billing & Subscription Service    → plan.entitlement.query
  - Auth Service                      → identity validation

DOWNSTREAM_AGENTS:
  - Billing Governance Agent          → reconciliation.verified.event
  - CALL_COST_CALCULATION_AGENT       → reconciliation.override.event (corrected units)
  - Audit Compliance Agent            → audit.record.appended event
  - Observability Agent               → reconciliation.health.metrics event
  - Fraud Detection Engine            → carrier.anomaly.flag event
  - Legal Policy Agent                → dispute.evidence.package event
  - GEO Compliance Agent              → regulatory.flag.event (DLT violations, grey routes)
  - Revenue Recognition Agent         → verified.telecom.cost.event
  - Admin Governance Service          → escalation.event (unresolved disputes)

KAFKA_TOPICS_CONSUMED:
  - telecom.usage.internal.events     (internal platform usage records)
  - telecom.carrier.invoice.ingested  (carrier invoice ingestion events)
  - telecom.dlr.callbacks             (Jasmin DLR callback stream)

KAFKA_TOPICS_PRODUCED:
  - telecom.reconciliation.completed  (per-channel reconciliation results)
  - telecom.dispute.generated         (dispute evidence package events)
  - telecom.regulatory.flag           (DLT/grey route flags)
  - telecom.credit.claim.generated    (credit recovery events)
  - telecom.reconciliation.failed     (failure events → dead-letter)
  - telecom.anomaly.detected          (ML anomaly flags)

EVENT_SCHEMA (all produced events):
  {
    event_id        : UUID v4,
    event_type      : ENUM (from topic list above),
    tenant_id       : string (null for platform-level carrier events),
    carrier_code    : string,
    billing_period  : YYYY-MM,
    payload         : object,
    timestamp_utc   : ISO8601,
    source_agent    : "TELECOM_USAGE_RECONCILIATION_AGENT",
    schema_version  : "v1.0.0"
  }
```

---

## 1️⃣2️⃣ PASSIVE INTELLIGENCE COMPATIBILITY

```yaml
FEATURE_EMISSION:
  This agent emits per-user and per-tenant communication quality feature vectors
  to FEATURE_STORE_SERVICE for use by intelligence and prediction models.

EMIT_FEATURE_VECTOR:
  {
    tenant_id          : string,
    feature_name       : "sms_delivery_rate_30d",
    feature_value      : decimal — rolling 30-day SMS delivery success rate,
    feature_name_2     : "otp_delivery_latency_p95_seconds",
    feature_value_2    : decimal — 95th percentile OTP delivery latency,
    feature_name_3     : "telecom_cost_accuracy_index",
    feature_value_3    : decimal — ratio of verified cost to billed cost (1.0 = perfect),
    feature_name_4     : "carrier_dispute_rate_90d",
    feature_value_4    : decimal — disputes per 100 invoices in rolling 90 days,
    timestamp          : ISO8601,
    source_agent       : "TELECOM_USAGE_RECONCILIATION_AGENT"
  }

INTELLIGENCE_USE_CASE:
  - High OTP delivery latency → Intelligence Prediction Engine flags communication
    barrier as career development friction for affected users
  - Low delivery rate for GD session notifications → Dojo Match Engine receives
    signal to increase notification lead time
  - Carrier dispute rate → FINOPS Agent receives signal for vendor risk scoring

PRIVACY_RULE:
  Feature vectors contain only aggregated delivery metrics — no MSISDN, no message content,
  no individual user communication records
```

---

## 1️⃣3️⃣ DLT REGULATORY COMPLIANCE MODULE (INDIA-SPECIFIC)

```yaml
DLT_COMPLIANCE_RULES:
  Regulatory_Body  : TRAI (Telecom Regulatory Authority of India)
  Framework        : DLT (Distributed Ledger Technology) mandate for commercial SMS

  MANDATORY_CHECKS_PER_SMS_BATCH:
    1. Sender ID (Header) must be registered on DLT platform
       - 6-character alphabetic header for transactional/OTP
       - 6-character numeric header for promotional
       - Unregistered header = DLT_HEADER_VIOLATION → HALT channel

    2. Template ID must be pre-registered on DLT platform
       - Every SMS template requires DLT approval before first send
       - Template content must match registered template within allowed variable fields
       - Content mismatch = DLT_TEMPLATE_MISMATCH → HALT + TRAI report

    3. Content Template Scrubbing
       - DLT platform performs real-time scrubbing against registered templates
       - Scrubbing rejection reason logged for each rejected message
       - Scrubbing failure rate monitored — threshold breach = platform risk flag

    4. Entity Registration
       - Ecoskiller must be registered as PE (Principal Entity) on DLT platform
       - Registration expiry monitored — alert 30 days before expiry
       - Lapsed registration = COMPLIANCE_CRITICAL → ALL SMS channels HALT

    5. Telemarketer Chain Validation
       - SMS flow: Ecoskiller (PE) → Telemarketer (registered) → Operator
       - Unregistered telemarketer in chain = DLT_CHAIN_VIOLATION

  DLT_AUDIT_OUTPUT:
    - Per-SMS DLT compliance record stored in PostgreSQL (7-year retention minimum)
    - Monthly DLT compliance report generated for TRAI audit readiness
    - All DLT violations reported to GEO_COMPLIANCE_AGENT within 1 hour

  INTERNATIONAL_REGULATORY_HOOKS:
    - GEO_COMPLIANCE_AGENT is authoritative for non-India regulatory requirements
    - This agent flags international route events to GEO_COMPLIANCE_AGENT for
      country-specific opt-in, GDPR, and telecom law validation
    - No country-specific logic hardcoded here — delegation model only
```

---

## 1️⃣4️⃣ PERFORMANCE MONITORING

```yaml
METRICS_EMITTED_TO_PROMETHEUS:
  - telecom_recon_records_processed_total       (counter, by channel_type, carrier)
  - telecom_recon_variance_detected_total       (counter, by variance_class)
  - telecom_recon_dispute_amount_inr_total      (gauge, rolling by billing_period)
  - telecom_recon_cycle_duration_seconds        (histogram, by cycle_type)
  - telecom_recon_carrier_invoice_latency_days  (gauge, by carrier_code)
  - telecom_recon_dlt_violations_total          (counter)
  - telecom_recon_grey_route_suspicions_total   (counter)
  - telecom_recon_delivery_variance_pct         (gauge, by channel + carrier)
  - telecom_recon_credit_claims_pending_inr     (gauge — financial exposure)
  - telecom_recon_confidence_score_avg          (gauge, rolling 24h)

GRAFANA_DASHBOARDS:
  - Monthly reconciliation status (all carriers, all channels)
  - Carrier accuracy trend (12-month rolling)
  - Dispute pipeline (open disputes, amounts, SLA status)
  - DLT compliance scorecard
  - OTP delivery health (real-time, 24h rolling)
  - Credit recovery pipeline
  - Grey route detection log

ALERTING_RULES:
  - reconciliation_cycle_duration > 2h for DAILY: PagerDuty alert
  - dlt_violations_total > 0: immediate alert (regulatory risk)
  - grey_route_suspicions_total > 0: immediate alert + auto-route-suspend
  - carrier_invoice_latency_days > 5: escalation alert
  - credit_claims_pending_inr > threshold: CFO-level alert
  - delivery_variance_pct > 5%: critical alert
  - confidence_score_avg < 0.85 for 1h: human review trigger

INTEGRATION:
  - Prometheus: scrape every 30 seconds
  - Loki: structured JSON log aggregation
  - OpenTelemetry: distributed trace per reconciliation chain
  - Wazuh: security event correlation (carrier data tampering detection)
```

---

## 1️⃣5️⃣ VERSIONING POLICY

```yaml
CURRENT_VERSION          : RECON_MODEL_v1.0.0
MUTATION_POLICY          : Add-only — new reconciliation rules added as new version
                           Existing rules immutable once version published
VERSION_ACTIVATION       : Requires PLATFORM_ADMIN activation
                           Prior version remains active for in-flight billing periods
BACKWARD_COMPATIBILITY   : All outputs include model_version — historical reconciliation
                           records auditable against the version that produced them
ROLLBACK_POLICY          : Prior version available for 90 days post-migration
                           Rollback requires PLATFORM_ADMIN approval + audit entry

TOLERANCE_THRESHOLD_VERSIONING:
  - Tolerance values (variance %, delivery %, confidence) are versioned
  - Threshold changes require version bump + migration documentation
  - Historical reconciliations re-evaluated at their original version's thresholds

CARRIER_CONTRACT_VERSIONING:
  - Each carrier contract stored as versioned record in RATE_REGISTRY
  - Rate changes = new contract version (add-only)
  - Reconciliation uses contract version active on billing_period start date
  - No retroactive rate application — ever

SCHEMA_MIGRATION_POLICY:
  - New fields added as optional with null-safe defaults
  - No field removal or rename permitted
  - Flyway manages PostgreSQL schema versions for reconciliation tables
  - All migrations are rollback-safe
```

---

## 1️⃣6️⃣ CARRIER REGISTRY

```yaml
CARRIER_REGISTRY:
  This agent operates against a registered carrier list (platform-level, not tenant-level).
  Unregistered carriers are rejected at invoice ingestion.

  REGISTERED_CARRIER_TYPES:
    DOMESTIC_SMS_OPERATOR    : Airtel, Jio, BSNL, Vi and licensed aggregators
    INTERNATIONAL_GATEWAY    : Licensed international SMS gateway providers
    PUSH_PROVIDER            : Google (FCM), Apple (APNS) — zero-cost, API-verified
    TURN_PROVIDER            : Self-hosted coturn (no carrier invoice) OR
                               managed TURN provider (if hybrid TURN deployed)
    EMAIL_RELAY              : Self-hosted Postfix (no carrier invoice) OR
                               external SMTP relay (if used as fallback)

  CARRIER_ONBOARDING_REQUIREMENTS:
    - Signed contract with versioned rate schedule stored in RATE_REGISTRY
    - DLT registration details (for SMS carriers)
    - Bank account details for payment (encrypted in HashiCorp Vault)
    - Dispute contact SLA agreement (response within 5 business days)
    - Invoice format specification (CSV/API/PDF + extraction rules)

  CARRIER_OFFBOARDING_POLICY:
    - Offboarding triggers reconciliation of all open billing periods first
    - No payment authorized until final reconciliation completes
    - Carrier data retained per retention policy (10 years for invoices)
```

---

## 1️⃣7️⃣ NON-NEGOTIABLE RULES

```yaml
THIS AGENT MUST NOT:

  ✗ Accept carrier-supplied unit counts as authoritative without internal verification
  ✗ Authorize payment based on unreconciled carrier invoice
  ✗ Store recipient MSISDNs or email addresses in plaintext — hashed form only
  ✗ Apply retroactive rate changes to closed billing periods
  ✗ Modify any reconciliation record after it is committed to audit trail
  ✗ Auto-delete any audit log, dispute evidence package, or carrier invoice
  ✗ Override GEO_COMPLIANCE_AGENT decisions on regulatory flags
  ✗ Bypass DLT compliance checks for any SMS message type
  ✗ Reinstate a suspended grey route without LEGAL_POLICY_AGENT clearance
  ✗ Produce reconciliation verdicts without full confidence score and audit reference
  ✗ Allow cross-tenant telecom usage data to appear in any single query or report
  ✗ Use AI/LLM for financial computation — rule engine only
  ✗ Accept invoice data from unregistered carriers
  ✗ Execute outside declared reconciliation cycle or without declared trigger
  ✗ Suppress regulatory alerts (DLT violations, grey routes) regardless of business reason
  ✗ Allow silent failure — every failure path emits structured Kafka event
  ✗ Process dispute evidence packages that have not been cryptographically signed
  ✗ Release credit claims without BILLING_GOVERNANCE_AGENT countersignature
  ✗ Mix carrier invoice data with tenant-specific usage attribution
  ✗ Communicate reconciliation results directly to external carriers —
     all external carrier communication routes through LEGAL_POLICY_AGENT
```

---

## 1️⃣8️⃣ DEPLOYMENT CONTEXT

```yaml
KUBERNETES_NAMESPACE       : billing
SERVICE_IDENTITY           : telecom-usage-reconciliation-agent
RESOURCE_PROFILE:
  CPU_REQUEST              : 1000m (batch-intensive workload)
  CPU_LIMIT                : 4000m
  MEMORY_REQUEST           : 1Gi
  MEMORY_LIMIT             : 8Gi (large-volume record matching at month-end)
  REPLICAS_MIN             : 2 (HA — active-passive for batch jobs)
  REPLICAS_MAX             : 20 (HPA on Airflow task queue depth)

AIRFLOW_DAG_DEFINITIONS:
  - telecom_recon_daily_otp           (cron: 0 2 * * *)
  - telecom_recon_weekly_review       (cron: 0 3 * * 1)
  - telecom_recon_monthly_final       (cron: 0 0 3 * *)
  - telecom_recon_adhoc_trigger       (manual trigger via Admin API)

HEALTH_CHECKS:
  LIVENESS                 : /health/live (200 = running)
  READINESS                : /health/ready (200 = Kafka connected + ClickHouse accessible
                              + PostgreSQL connected + MinIO accessible)
  STARTUP_PROBE            : 60s grace period (large reconciliation data warm-up)

GRACEFUL_SHUTDOWN:
  - Complete in-progress reconciliation cycle before pod termination
  - Flush all pending audit log writes
  - Release all Redis reconciliation locks
  - Emit agent.shutdown event to Observability Agent
  - Shutdown timeout: 300 seconds (5 minutes — batch job completion window)

CI_CD_PIPELINE:
  - GitHub Actions: build → unit test → integration test → security scan → image push
  - Helm chart deployment: blue/green with automated rollback
  - No manual production deployments
  - DB migrations via Flyway — rollback-safe scripts only
```

---

## 1️⃣9️⃣ TESTING CONTRACT

```yaml
REQUIRED_TEST_COVERAGE     : 92% minimum (financial-critical agent — enforced in CI)

UNIT_TESTS:
  - All reconciliation formula paths (SMS unit, delivery, route, DLT, segment, rate)
  - OTP 24-hour reconciliation cycle
  - International SMS reconciliation with currency conversion
  - TURN relay session vs infrastructure cost verification
  - Push reconciliation (FCM, APNS, ntfy)
  - Email bounce rate audit
  - All variance classification buckets (MATCHED → ESCALATED)
  - Credit recovery logic
  - All failure paths

INTEGRATION_TESTS:
  - End-to-end: Jasmin send event → DLR event → carrier invoice → reconciliation verdict
  - Airflow DAG execution (all 3 cycle types)
  - Dispute evidence package generation and MinIO archival
  - Carrier invoice rejection and escalation flow
  - DLT violation detection and GEO_COMPLIANCE_AGENT handoff
  - Grey route detection and route suspension

REGULATORY_TESTS:
  - DLT template mismatch detection (100% coverage of violation types)
  - TRAI audit report generation
  - International route MNCC validation
  - Data retention enforcement (record deletion prevention test)

SECURITY_TESTS:
  - Cross-tenant invoice data injection (must be rejected)
  - Plaintext MSISDN submission (must be rejected)
  - Carrier invoice tampering after ingestion (must be detected)
  - Unauthorized payment authorization attempt (must be blocked)
  - Unregistered carrier invoice submission (must be rejected)

PERFORMANCE_TESTS:
  - 500M SMS record reconciliation within 24h monthly cycle SLA
  - Concurrent daily OTP + weekly reconciliation (no resource contention)
  - Kafka consumer lag under 500M event backlog (< 4h catch-up)
  - MinIO dispute package archival under peak load

CHAOS_TESTS:
  - Carrier invoice not received (payment withheld, escalation fired correctly)
  - ClickHouse unavailable during monthly cycle (graceful halt, retry, alert)
  - Airflow DAG failure mid-cycle (idempotent restart, no double-reconciliation)
  - MinIO unavailable (dispute package generation halted, evidence chain preserved)
```

---

## 2️⃣0️⃣ AGENT SEAL DECLARATION

```
╔══════════════════════════════════════════════════════════════════════════╗
║                           AGENT SEAL                                     ║
║                                                                          ║
║  AGENT   : TELECOM_USAGE_RECONCILIATION_AGENT                            ║
║  VERSION : v1.0.0                                                        ║
║  STATUS  : SEALED · LOCKED · PRODUCTION-READY · REGULATORY-COMPLIANT    ║
║                                                                          ║
║  This agent specification is add-only from this point forward.          ║
║  No clause may be removed or modified without:                           ║
║    (a) Version bump to v1.1.0 or higher                                 ║
║    (b) PLATFORM_ADMIN written approval                                  ║
║    (c) Audit log entry for modification rationale                       ║
║    (d) Migration documentation for backward compatibility               ║
║    (e) GEO_COMPLIANCE_AGENT sign-off on any regulatory clause change    ║
║    (f) LEGAL_POLICY_AGENT sign-off on any dispute process change        ║
║                                                                          ║
║  Any execution that deviates from this specification is a               ║
║  GOVERNANCE VIOLATION and must be halted and reported immediately.      ║
║                                                                          ║
║  Financial verdicts produced by this agent are legally binding          ║
║  for carrier payment authorization. Unauthorized modification           ║
║  of reconciliation verdicts constitutes financial fraud.                ║
║                                                                          ║
║  SEALED BY : ECOSKILLER ANTIGRAVITY INTELLIGENCE & INNOVATION CORE      ║
║  PLATFORM  : ECOSKILLER ANTIGRAVITY                                      ║
║  ARCH TYPE : Enterprise SaaS · Zero-Trust · Multi-Tenant · DLT-Compliant║
╚══════════════════════════════════════════════════════════════════════════╝
```

---

*End of TELECOM_USAGE_RECONCILIATION_AGENT.md — v1.0.0 — SEALED*
