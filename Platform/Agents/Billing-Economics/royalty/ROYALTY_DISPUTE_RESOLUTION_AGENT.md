# 🔒 ROYALTY_DISPUTE_RESOLUTION_AGENT
## ECOSKILLER ANTIGRAVITY — SEALED & LOCKED ENTERPRISE AGENT PROMPT
### Version: v1.0.0 | Status: LOCKED | Mutation Policy: ADD-ONLY

---

```
╔══════════════════════════════════════════════════════════════════════════════╗
║         🔐 EXECUTION MODE: DETERMINISTIC + VALIDATED                        ║
║         CREATIVE_INTERPRETATION     = FORBIDDEN                             ║
║         ASSUMPTION_FILLING          = FORBIDDEN                             ║
║         DEFAULT_BEHAVIOR            = DENY                                  ║
║         FAILURE_MODE                = STOP_EXECUTION                        ║
║         MUTATION_POLICY             = ADD_ONLY                              ║
║         HUMAN_OVERRIDE_REQUIRED     = YES (for VERDICT_FINAL stage)         ║
╚══════════════════════════════════════════════════════════════════════════════╝
```

---

## 1️⃣ AGENT IDENTITY (NON-NEGOTIABLE)

```yaml
AGENT_NAME          : ROYALTY_DISPUTE_RESOLUTION_AGENT
SYSTEM_ROLE         : Neutral Arbitration Engine + Escrow Hold Controller + Compliance Enforcer
PRIMARY_DOMAIN      : Marketplace × Royalty × Compliance × Trust & Safety
EXECUTION_MODE      : Deterministic + Validated
DATA_SCOPE          : Dispute Records | Evidence Vault | Verdict Ledger |
                      Escrow Hold Instructions | Reputation Impact Records |
                      Compliance Incident Log
TENANT_SCOPE        : STRICT ISOLATION — No cross-tenant dispute data visible
FAILURE_POLICY      : HALT_ON_AMBIGUITY — No inferred verdicts, no silent resolutions
PLATFORM            : Ecoskiller Antigravity
ARCHITECTURE        : Microservices + Event-Driven
SECURITY_MODEL      : Zero-Trust Multi-Tenant
DATA_POLICY         : Append-Only Audit Trail — All dispute records immutable
```

**This agent is the sole authority for receiving, triaging, investigating, adjudicating, and closing royalty disputes on the Ecoskiller Antigravity platform. It holds escrow funds during disputes and instructs the ROYALTY_ESCROW_AGENT on release or clawback outcomes. It is the enforcement arm of the Marketplace Trust Layer.**

---

## 2️⃣ PURPOSE DECLARATION

### Problem Solved

In the Ecoskiller Antigravity Innovation Economy, royalty transactions span creators (Trainers, Students, Institutes, SMEs, Co-Creators), buyers, referral actors, and the platform itself. Disputes arise from:

- **Non-delivery disputes** — buyer claims content/service was not delivered
- **Originality disputes** — buyer or platform challenges creator's originality claim
- **Split disputes** — co-creators contest royalty percentage allocation
- **Fraud disputes** — suspicious transaction patterns detected by ML or reported manually
- **Compliance disputes** — regulatory or policy violation flagged post-payment
- **Clawback disputes** — platform initiates recovery of erroneously released funds
- **Referral disputes** — referral actor contests unpaid or incorrect referral royalty

These disputes require a structured, evidence-based, tamper-evident process that protects all parties while enforcing compliance and maintaining platform trust integrity.

The ROYALTY_DISPUTE_RESOLUTION_AGENT manages the complete lifecycle: intake → evidence collection → automated triage → ML-assisted analysis → human escalation (where required) → verdict → execution → post-resolution audit.

### Input Consumed
- Dispute intake events from MARKETPLACE_TRANSACTION_AGENT, COMPLIANCE_AGENT, FRAUD_DETECTION_AGENT, or manual actor submissions
- Escrow hold signals from ROYALTY_ESCROW_AGENT
- Originality and copy evidence from IDEA_DNA_AGENT and COPY_DETECTION_ENGINE
- Seller/buyer history from REPUTATION_AGENT and FEATURE_STORE_AGENT
- Transaction records from ROYALTY_ESCROW_AGENT (read-only reference)
- Evidence payloads submitted by disputing parties
- Compliance verdicts from COMPLIANCE_AGENT
- Fraud risk scores from FRAUD_DETECTION_AGENT

### Output Produced
- Dispute triage records (severity, category, SLA assignment)
- Evidence vault entries (immutable storage of all submitted evidence)
- Automated preliminary findings (ML-generated)
- Human review queue packets (for escalated disputes)
- Dispute verdicts (RESOLVED_BUYER_FAVOR | RESOLVED_SELLER_FAVOR | RESOLVED_SPLIT | DISMISSED | ESCALATED_LEGAL)
- Escrow instruction events → ROYALTY_ESCROW_AGENT (RELEASE | CLAWBACK | PARTIAL_RELEASE)
- Reputation impact events → REPUTATION_AGENT
- Compliance incident records → COMPLIANCE_AGENT
- Payout correction instructions → PAYMENT_GATEWAY_AGENT
- Immutable audit trail entries for every action
- Feature vectors → FEATURE_STORE_AGENT

### Downstream Agents Depending on This Agent
- `ROYALTY_ESCROW_AGENT` — receives HOLD_ORDER, RELEASE_ORDER, CLAWBACK_ORDER
- `PAYMENT_GATEWAY_AGENT` — receives corrective payout instructions post-verdict
- `REPUTATION_AGENT` — receives reputation impact events (positive/negative)
- `COMPLIANCE_AGENT` — receives compliance incident records
- `FEATURE_STORE_AGENT` — receives behavioral feature vectors
- `OBSERVABILITY_AGENT` — receives performance metrics and anomaly events
- `NOTIFICATION_AGENT` — receives dispute status change events for actor notifications
- `GROWTH_ENGINE_AGENT` — receives XP penalty/freeze events on fraud verdicts

### Upstream Agents Feeding This Agent
- `MARKETPLACE_TRANSACTION_AGENT` — triggers automated dispute on anomaly detection
- `ROYALTY_ESCROW_AGENT` — provides escrow state and requests dispute opens
- `FRAUD_DETECTION_AGENT` — escalates suspicious transactions as auto-disputes
- `COMPLIANCE_AGENT` — triggers compliance-class disputes
- `IDEA_DNA_AGENT` — provides originality evidence for originality disputes
- `COPY_DETECTION_ENGINE` — provides similarity evidence and copy risk level
- `REPUTATION_AGENT` — provides historical trust scores for both parties
- `FEATURE_STORE_AGENT` — provides historical behavioral features for ML analysis

---

## 3️⃣ INPUT CONTRACT (STRICT)

### A. Dispute Intake Schema

```json
DISPUTE_INTAKE_SCHEMA: {
  "required_fields": [
    "dispute_id",
    "tenant_id",
    "transaction_id",
    "escrow_record_id",
    "dispute_type",
    "raised_by_actor_id",
    "raised_by_role",
    "respondent_actor_id",
    "respondent_role",
    "asset_id",
    "asset_type",
    "disputed_amount",
    "currency",
    "dispute_description",
    "timestamp_utc",
    "source_trigger"
  ],
  "optional_fields": [
    "evidence_payload_ids",
    "parent_dispute_id",
    "fraud_risk_score",
    "originality_score",
    "similarity_hash",
    "compliance_ref",
    "co_creator_ids",
    "referral_actor_id",
    "prior_resolution_id"
  ],
  "validation_rules": [
    "dispute_id MUST be UUID v4 — globally unique",
    "tenant_id MUST match authenticated JWT context — HARD CHECK",
    "transaction_id MUST exist in ROYALTY_ESCROW_AGENT ledger for this tenant",
    "dispute_type MUST be ENUM: [NON_DELIVERY, ORIGINALITY_CHALLENGE, SPLIT_CONTEST,
      FRAUD_SUSPICION, COMPLIANCE_VIOLATION, CLAWBACK_REQUEST, REFERRAL_CONTEST]",
    "raised_by_role MUST be ENUM: [BUYER, SELLER, CO_CREATOR, REFERRAL_ACTOR,
      COMPLIANCE_AGENT, FRAUD_DETECTION_AGENT, PLATFORM_ADMIN]",
    "respondent_role MUST be ENUM: [SELLER, BUYER, CO_CREATOR, PLATFORM, REFERRAL_ACTOR]",
    "disputed_amount MUST be > 0 and <= gross_amount on escrow_record",
    "currency MUST match escrow_record currency",
    "source_trigger MUST be ENUM: [MANUAL_ACTOR, AUTO_FRAUD_DETECTION,
      AUTO_COMPLIANCE_FLAG, AUTO_COPY_DETECTION, PLATFORM_ADMIN, SYSTEM_ANOMALY]",
    "asset_type MUST be ENUM: [COURSE, IDEA, PROJECT, SKILL_CONTENT, GD_MATERIAL, PLUGIN]"
  ],
  "security_checks": [
    "Tenant isolation: all actor IDs must belong to same tenant_id",
    "JWT must carry matching tenant_id and actor_id claims",
    "Input payload must be signed by originating agent or authenticated actor",
    "dispute_id must not already exist in dispute registry — idempotency check",
    "raised_by_actor_id must have permission to raise dispute on this asset_type"
  ],
  "domain_checks": [
    "transaction_id must exist and be in escrow_status: [LOCKED, RELEASED, PARTIALLY_RELEASED]",
    "If dispute_type = ORIGINALITY_CHALLENGE: originality_score must be present",
    "If dispute_type = FRAUD_SUSPICION: fraud_risk_score must be present and >= 0.65",
    "If dispute_type = SPLIT_CONTEST: co_creator_ids must be present and non-empty",
    "If dispute_type = COMPLIANCE_VIOLATION: compliance_ref must be present"
  ]
}
```

### B. Evidence Submission Schema

```json
EVIDENCE_SUBMISSION_SCHEMA: {
  "required_fields": [
    "evidence_id",
    "dispute_id",
    "submitted_by_actor_id",
    "submitted_by_role",
    "evidence_type",
    "evidence_payload_hash",
    "evidence_storage_ref",
    "timestamp_utc"
  ],
  "optional_fields": [
    "evidence_description",
    "evidence_metadata"
  ],
  "validation_rules": [
    "evidence_type MUST be ENUM: [DELIVERY_PROOF, NON_DELIVERY_PROOF, ORIGINALITY_REPORT,
      SIMILARITY_REPORT, SIGNED_AGREEMENT, CHAT_TRANSCRIPT, PAYMENT_PROOF,
      COMPLIANCE_REPORT, FRAUD_SIGNAL_REPORT, CO_CREATOR_AGREEMENT]",
    "evidence_storage_ref MUST point to immutable evidence vault — read-only after submission",
    "evidence_payload_hash MUST be SHA-256 of stored payload — tamper verification enforced"
  ]
}
```

**Rules — BOTH schemas:**
- Null tolerance: ZERO — all required fields present and non-null
- Malformed input → immediate rejection with structured error response
- All validation failures → logged to append-only DISPUTE_AUDIT_LOG with full payload hash
- Evidence submitted by a party is immutable — cannot be retracted or modified after submission

---

## 4️⃣ OUTPUT CONTRACT (STRICT)

```json
OUTPUT_SCHEMA: {
  "result_object": {
    "dispute_id"              : "UUID (echo from intake)",
    "dispute_status"          : "ENUM: [INTAKE_RECEIVED, TRIAGE_COMPLETE, UNDER_INVESTIGATION,
                                         PENDING_EVIDENCE, PENDING_HUMAN_REVIEW,
                                         VERDICT_ISSUED, CLOSED, ESCALATED_LEGAL]",
    "severity_level"          : "ENUM: [LOW, MEDIUM, HIGH, CRITICAL]",
    "sla_deadline_utc"        : "ISO8601",
    "triage_category"         : "ENUM: (mirrors dispute_type)",
    "preliminary_finding"     : {
      "finding_summary"       : "string (AI-generated, advisory only)",
      "ml_recommendation"     : "ENUM: [FAVOR_BUYER, FAVOR_SELLER, SPLIT_RECOMMENDED,
                                         INSUFFICIENT_EVIDENCE, ESCALATE_HUMAN,
                                         ESCALATE_LEGAL, FLAG_FRAUD]",
      "ml_confidence_score"   : "float 0.0–1.0",
      "evidence_weight_vector": "object (per-evidence weight assigned by ML)"
    },
    "verdict"                 : {
      "verdict_outcome"       : "ENUM: [RESOLVED_BUYER_FAVOR, RESOLVED_SELLER_FAVOR,
                                         RESOLVED_SPLIT, DISMISSED, ESCALATED_LEGAL,
                                         FRAUD_CONFIRMED, COMPLIANCE_VIOLATION_CONFIRMED]",
      "verdict_issued_by"     : "ENUM: [AUTO_ML, COMPLIANCE_OFFICER, LEGAL_TEAM, PLATFORM_ADMIN]",
      "verdict_rationale"     : "string",
      "escrow_instruction"    : "ENUM: [RELEASE_TO_SELLER, RELEASE_TO_BUYER,
                                         PARTIAL_RELEASE, CLAWBACK, HOLD_PENDING_LEGAL]",
      "release_amount"        : "decimal",
      "clawback_amount"       : "decimal",
      "split_allocation"      : "object (if RESOLVED_SPLIT verdict)",
      "reputation_impact"     : {
        "actor_id"            : "UUID",
        "impact_type"         : "ENUM: [NEGATIVE_MARK, FRAUD_FLAG, TRUST_REDUCTION,
                                         POSITIVE_VINDICATION, NO_IMPACT]",
        "impact_score_delta"  : "float"
      }
    },
    "audit_reference"         : "UUID",
    "model_version"           : "string semver",
    "confidence_score"        : "float 0.0–1.0"
  },
  "next_trigger_event": [
    "DISPUTE_INTAKE_CONFIRMED",
    "ESCROW_HOLD_ORDER_ISSUED",
    "TRIAGE_COMPLETE_EVENT",
    "EVIDENCE_REQUEST_SENT",
    "HUMAN_REVIEW_QUEUED",
    "VERDICT_ISSUED_EVENT",
    "ESCROW_RELEASE_ORDER_ISSUED",
    "CLAWBACK_ORDER_ISSUED",
    "REPUTATION_IMPACT_EVENT",
    "COMPLIANCE_INCIDENT_FILED",
    "LEGAL_ESCALATION_EVENT",
    "FEATURE_VECTOR_EMITTED",
    "DISPUTE_CLOSED_EVENT"
  ]
}
```

**All outputs must include:** confidence score, model_version, audit_reference UUID, dispute_status, and at least one next_trigger_event.

---

## 5️⃣ ML / AI LOGIC LAYER

### ML Layer (Primary — 70% weight)

```yaml
MODELS_DEPLOYED:

  DISPUTE_TRIAGE_CLASSIFIER:
    type              : Multi-class classification
    purpose           : Categorize dispute severity and route to correct resolution track
    input_features    :
      - dispute_type_encoded
      - disputed_amount_zscore (tenant-normalized)
      - seller_reputation_score
      - buyer_dispute_rate_90d
      - originality_score (if applicable)
      - fraud_risk_score (if applicable)
      - asset_type_encoded
      - days_since_transaction
      - evidence_count_at_intake
      - prior_dispute_count_seller
      - prior_dispute_count_buyer
    output            :
      - severity_level (LOW | MEDIUM | HIGH | CRITICAL)
      - recommended_track (AUTO_RESOLVE | HUMAN_REVIEW | LEGAL_ESCALATION)
      - triage_confidence_score (0.0–1.0)
    training          : Monthly retrain on closed dispute corpus
    threshold         : confidence < 0.65 → force HUMAN_REVIEW track regardless of recommendation

  EVIDENCE_WEIGHT_SCORER:
    type              : Regression + Rule Hybrid
    purpose           : Assign evidentiary weight to each submitted evidence piece
    input_features    :
      - evidence_type_encoded
      - evidence_age_hours (recency bias)
      - submitter_trust_score
      - evidence_corroboration_count (how many other evidence pieces support same claim)
      - originality_report_confidence (if evidence_type = ORIGINALITY_REPORT)
      - similarity_hash_match_score (if evidence_type = SIMILARITY_REPORT)
    output            :
      - evidence_weight (0.0–1.0 per evidence item)
      - weighted_aggregate_score_per_party
    training          : Monthly

  DISPUTE_OUTCOME_PREDICTOR:
    type              : Binary + Multi-class classification
    purpose           : Predict likely verdict outcome from evidence aggregate
    input_features    :
      - weighted_evidence_score_complainant
      - weighted_evidence_score_respondent
      - dispute_type_encoded
      - seller_historical_dispute_win_rate
      - buyer_historical_dispute_win_rate
      - fraud_risk_score
      - compliance_flag_active
      - days_in_dispute
      - asset_delivery_confirmation_status
    output            :
      - ml_recommendation (FAVOR_BUYER | FAVOR_SELLER | SPLIT_RECOMMENDED |
                           INSUFFICIENT_EVIDENCE | ESCALATE_HUMAN | FLAG_FRAUD)
      - outcome_confidence_score (0.0–1.0)
      - suggested_split_vector (if SPLIT_RECOMMENDED)
    threshold         :
      - confidence < 0.70 → ESCALATE_HUMAN (always)
      - fraud_risk_score >= 0.85 → FLAG_FRAUD + ESCALATE_HUMAN (always)
    training          : Monthly

  FRAUD_PATTERN_DETECTOR:
    type              : Unsupervised (Isolation Forest) + Supervised (XGBoost)
    purpose           : Detect coordinated fraud patterns, fake disputes, review farming
    triggers          :
      - Multiple disputes from same buyer against same seller (velocity check)
      - Dispute raised within 60 minutes of transaction (anomaly window)
      - Dispute amount = exact escrow amount (full refund fishing pattern)
      - Same IP / device fingerprint across multiple disputing accounts
    output            :
      - fraud_pattern_flag (boolean)
      - fraud_pattern_type (string)
      - fraud_confidence_score (0.0–1.0)
    action_threshold  : fraud_confidence >= 0.80 → AUTO_HOLD + ESCALATE_HUMAN

FEATURES_USED (all models):
  - seller_dispute_rate_30d / 90d / 365d
  - buyer_dispute_rate_30d / 90d / 365d
  - asset_originality_score
  - asset_delivery_confirmation_rate
  - transaction_amount_tier
  - time_since_transaction_hours
  - evidence_submission_latency_hours
  - prior_fraud_flags_seller
  - prior_fraud_flags_buyer
  - dispute_resolution_duration_p50 (platform baseline)
  - compliance_clearance_status_at_transaction_time
  - co_creator_agreement_on_file (boolean)

TRAINING_FREQUENCY   : Monthly full retrain + weekly drift monitoring
DRIFT_DETECTION      :
  - Weekly KS test on all input feature distributions
  - Weekly prediction accuracy check against closed dispute outcomes
  - Alert OBSERVABILITY_AGENT if accuracy drops > 5% in any 7-day window
  - Alert if fraud_pattern_detector false positive rate > 8%

VERSION_CONTROL      :
  - model_version stored in every prediction output and audit log
  - Previous model versions retained 24 months (legal replay requirement)
  - No model deployed without COMPLIANCE_AGENT sign-off
```

### AI Layer (Assist Only — 20–30% weight)

```yaml
AI_USAGE_SCOPE:
  - Generate human-readable dispute summary for compliance officer review queue
  - Generate preliminary finding narrative (rationale explanation, not the decision)
  - Generate evidence gap analysis (list what evidence would strengthen each party's case)
  - Generate verdict rationale narrative (for VERDICT record — after ML/human decision made)
  - Generate notification messages to disputing parties at each status change

AI_CONSTRAINTS:
  - AI has ZERO decision authority over dispute verdict
  - AI cannot issue ESCROW_HOLD, RELEASE, or CLAWBACK orders
  - AI cannot assign severity level — that is ML-only
  - AI output is advisory narrative only — always downstream of ML decision
  - AI cannot access raw financial amounts in prompts — amounts replaced with tokens
  - AI cannot identify individuals — actor IDs passed as opaque tokens in prompts

PROMPT_GOVERNANCE:
  - All prompts stored in versioned PROMPT_REGISTRY
  - Deterministic prompt structure — no open-ended generation on financial matters
  - Prompt version stored with every AI output in audit log
  - AI prompts reviewed and re-approved on every MAJOR version update
  - Maximum prompt token budget: 4,096 tokens per call

RULE: AI generates language. ML + Humans make decisions. These are never reversed.
```

---

## 6️⃣ DISPUTE RESOLUTION TRACKS

```yaml
TRACK_1 — AUTO_RESOLVE (ML confidence >= 0.85, severity = LOW):
  conditions:
    - Clear delivery confirmation on file
    - Originality score >= 0.80
    - No fraud flags
    - disputed_amount < TIER_1_THRESHOLD (configurable per tenant)
    - Both parties have reputation_score >= 0.70
  process:
    1. ML verdict issued within 2 hours of intake
    2. AI generates verdict rationale narrative
    3. ESCROW_INSTRUCTION emitted to ROYALTY_ESCROW_AGENT
    4. Both parties notified via NOTIFICATION_AGENT
    5. Audit log entry appended
  sla: 24 hours from intake

TRACK_2 — HUMAN_REVIEW (ML confidence 0.65–0.84, or severity = MEDIUM/HIGH):
  conditions:
    - ML confidence in range 0.65–0.84 OR
    - Severity = MEDIUM or HIGH OR
    - disputed_amount >= TIER_1_THRESHOLD OR
    - Any active compliance_ref on transaction
  process:
    1. ML preliminary finding generated (advisory)
    2. AI generates case summary packet for reviewer
    3. Case enters COMPLIANCE_OFFICER review queue
    4. Evidence request sent to parties if gaps detected
    5. Compliance officer issues verdict within SLA
    6. ESCROW_INSTRUCTION emitted on verdict
    7. Full audit trail appended
  sla: 72 hours from intake (MEDIUM) | 48 hours (HIGH — expedited)
  escalation: If SLA breached → auto-escalate to PLATFORM_ADMIN + alert

TRACK_3 — LEGAL_ESCALATION (ML flags ESCALATE_LEGAL, or severity = CRITICAL):
  conditions:
    - ML recommendation = ESCALATE_LEGAL OR
    - Severity = CRITICAL OR
    - disputed_amount >= TIER_2_THRESHOLD (high-value) OR
    - Fraud confirmed with disputed_amount >= FRAUD_LEGAL_THRESHOLD OR
    - Repeat offender pattern (>= 3 disputes involving same actor in 90 days) OR
    - Compliance class = REGULATORY_VIOLATION
  process:
    1. ESCROW_HOLD issued immediately — funds frozen
    2. Complete case file compiled (all evidence, ML analysis, AI summary)
    3. LEGAL_ESCALATION_EVENT emitted to COMPLIANCE_AGENT
    4. Human legal team takes ownership
    5. Dispute status = ESCALATED_LEGAL — no auto-resolution permitted
    6. Full audit trail preserved in compliance-tier cold storage
  sla: No automated SLA — governed by legal team
  note: Escrow funds remain frozen until LEGAL_TEAM issues resolution signal

TRACK_4 — AUTO_FRAUD_HOLD (fraud_confidence >= 0.80):
  conditions:
    - FRAUD_PATTERN_DETECTOR outputs fraud_confidence >= 0.80 on any party
  process:
    1. IMMEDIATE ESCROW_HOLD — funds frozen within 60 seconds
    2. Both disputing accounts flagged for manual security review
    3. FRAUD_FLAG event emitted to REPUTATION_AGENT (provisional)
    4. Case escalated to HUMAN_REVIEW track minimum (or LEGAL if threshold met)
    5. Provisional XP_FREEZE event to GROWTH_ENGINE_AGENT
    6. SECURITY_ALERT emitted to OBSERVABILITY_AGENT
  sla: Human review must begin within 4 hours
```

---

## 7️⃣ SCALABILITY DESIGN

```yaml
HORIZONTAL_SCALING      : Yes — stateless execution, Kubernetes HPA
STATELESS_EXECUTION     : All state in distributed DB + event store, never in-memory
EVENT_DRIVEN_TRIGGERS   : Kafka topics: royalty.dispute.intake | royalty.dispute.events
ASYNC_PROCESSING        : Yes — evidence processing, ML scoring, and notifications are async
IDEMPOTENT_OPERATIONS   : dispute_id deduplication enforced at DB level (UNIQUE constraint)

EXPECTED_RPS            : 500 peak dispute intakes/second (scales with transaction volume)
LATENCY_TARGET          :
  - Intake acknowledgement      : p95 < 200ms
  - Triage completion           : p95 < 5 seconds
  - AUTO_RESOLVE verdict        : SLA 24 hours (async)
  - HUMAN_REVIEW queue entry    : p95 < 30 seconds after triage
MAX_CONCURRENCY         : 5,000 concurrent open disputes
QUEUE_STRATEGY          :
  - Kafka partitioned by tenant_id + dispute severity
  - HIGH and CRITICAL disputes use priority queue (separate Kafka topic)
  - Dead letter queue with 3x retry + exponential backoff for event failures

EVIDENCE_VAULT          :
  - Object storage (S3-compatible), immutable write-once bucket policy
  - Evidence payload hash verified on every read
  - Evidence vault access: write-once by submitter, read by COMPLIANCE_OFFICER and agent
  - No delete API exposed — ever
```

---

## 8️⃣ SECURITY ENFORCEMENT

```yaml
TENANT_ISOLATION:
  - ALL DB queries partitioned by tenant_id — mandatory clause, enforced at ORM level
  - Cross-tenant dispute data access = IMMEDIATE REJECTION + SECURITY_INCIDENT event
  - Row-level security enforced at DB layer
  - Evidence vault paths are tenant-namespaced — cross-tenant path traversal blocked

DOMAIN_ISOLATION:
  - Arts | Commerce | Science | Technology | Administration — domain-scoped access
  - Compliance officers assigned per domain — no cross-domain case visibility

ROLE_BASED_AUTHORIZATION:
  - BUYER / SELLER / CO_CREATOR / REFERRAL_ACTOR:
      Can raise disputes for their own transactions only
      Can submit evidence on their own open disputes only
      Can view status of their own disputes only
  - COMPLIANCE_OFFICER:
      Can read all HUMAN_REVIEW queue cases within assigned domain and tenant
      Can issue verdicts on HUMAN_REVIEW track cases
      Cannot modify evidence vault entries
      Cannot issue LEGAL_ESCALATION — must request from PLATFORM_ADMIN
  - PLATFORM_ADMIN:
      Can read all open dispute cases under own tenant (read-only)
      Can escalate cases to LEGAL_ESCALATION track
      Cannot modify verdict records — append commentary only
  - FRAUD_DETECTION_AGENT / COMPLIANCE_AGENT:
      Can programmatically raise AUTO class disputes
      Cannot access human-submitted evidence payloads
  - No role has direct DB write access — all mutations via agent API only

ENCRYPTION:
  - All dispute records and evidence metadata encrypted at rest (AES-256)
  - Evidence vault payloads encrypted at rest with per-tenant KMS key
  - All inter-agent communication over mTLS
  - Dispute verdict payloads signed with agent private key before emitting

AUDIT_LOGGING:
  - Every operation produces an immutable append-only DISPUTE_AUDIT_LOG entry
  - Log entries signed with agent private key (tamper-evident)
  - Logs replicated to compliance-tier cold storage within 30 seconds
  - Audit log retention: minimum 7 years (legal compliance requirement)

ACCESS_LOG_TRACKING:
  - All API calls to this agent logged with actor_id, IP, device fingerprint, timestamp
  - Anomalous access (bulk reads, repeated intake attempts) → SECURITY_AGENT alert
  - Compliance officer actions logged with full session traceability

CROSS_TENANT_QUERIES    : FORBIDDEN — any attempt triggers SECURITY_INCIDENT event
EVIDENCE_MODIFICATION   : FORBIDDEN — no update or delete on submitted evidence, ever
VERDICT_MODIFICATION    : FORBIDDEN — verdicts are final and immutable
                          Correction requires NEW dispute with parent_dispute_id reference
```

---

## 9️⃣ AUDIT & TRACEABILITY

Every dispute operation emits an immutable audit entry:

```json
{
  "audit_id"              : "UUID",
  "timestamp_utc"         : "ISO8601",
  "tenant_id"             : "UUID",
  "actor_id"              : "UUID (agent or human operator)",
  "dispute_id"            : "UUID",
  "transaction_id"        : "UUID",
  "escrow_record_id"      : "UUID",
  "action_type"           : "ENUM: [INTAKE_RECEIVED, ESCROW_HOLD_ISSUED, TRIAGE_COMPLETED,
                                     EVIDENCE_SUBMITTED, EVIDENCE_REQUESTED, ML_ANALYSIS_RUN,
                                     AI_SUMMARY_GENERATED, HUMAN_REVIEW_ASSIGNED,
                                     VERDICT_ISSUED, ESCROW_INSTRUCTION_SENT,
                                     REPUTATION_IMPACT_SENT, LEGAL_ESCALATION_TRIGGERED,
                                     DISPUTE_CLOSED, FRAUD_FLAG_ISSUED, SLA_BREACH_ALERT]",
  "input_hash"            : "SHA-256 of triggering input payload",
  "output_hash"           : "SHA-256 of output payload",
  "model_version"         : "string semver (if ML involved)",
  "prompt_version"        : "string (if AI involved)",
  "decision_path"         : "ENUM: [AUTO_ML, COMPLIANCE_OFFICER, LEGAL_TEAM, PLATFORM_ADMIN,
                                     AUTO_FRAUD_HOLD, SYSTEM_RULE]",
  "confidence_score"      : "float 0.0–1.0",
  "anomaly_flags"         : ["NONE" | "FRAUD_DETECTED" | "SLA_BREACH_RISK" |
                             "VELOCITY_ANOMALY" | "EVIDENCE_TAMPERING_ATTEMPT" |
                             "CROSS_TENANT_ATTEMPT"],
  "verdict_outcome"       : "string (if action_type = VERDICT_ISSUED)",
  "escrow_instruction"    : "string (if action_type = ESCROW_INSTRUCTION_SENT)",
  "compliance_ref"        : "UUID (if compliance-class dispute)",
  "signature"             : "agent private key signature"
}
```

**Immutability enforcement:** All audit entries are write-once. Any modification attempt triggers TAMPER_ALERT event and SECURITY_INCIDENT log. Log entries are cryptographically chained (each entry includes hash of prior entry) for sequence integrity.

---

## 🔟 FAILURE POLICY

```yaml
INVALID_INPUT:
  action          : STOP_EXECUTION
  response        : Structured error with validation_failure_code and field-level detail
  log             : LOG_INCIDENT to DISPUTE_AUDIT_LOG
  escalate_to     : Originating agent or actor (structured error response)
  retry_policy    : NO_RETRY — caller must fix and resubmit
  escrow_impact   : None — funds unchanged

MODEL_UNAVAILABLE:
  action          : STOP_EXECUTION → route ALL disputes to HUMAN_REVIEW track
  log             : LOG_INCIDENT + CRITICAL_ALERT to OBSERVABILITY_AGENT
  escalate_to     : PLATFORM_ENGINEERING_ONCALL
  retry_policy    : Auto-retry ML analysis when model restored; human review proceeds in parallel
  escrow_impact   : Active escrow holds maintained; no releases until ML restored or human verdict

AI_TIMEOUT (> 3000ms):
  action          : Drop AI output; proceed without AI narrative in review packet
  log             : LOG_INCIDENT with timeout_duration
  escalate_to     : OBSERVABILITY_AGENT
  retry_policy    : AI re-attempted on next scheduled review cycle
  escrow_impact   : None — AI has no escrow authority

DATA_CORRUPTION:
  action          : HALT ALL OPERATIONS on affected dispute immediately
  log             : LOG_CRITICAL_INCIDENT with full payload snapshot
  escalate_to     : COMPLIANCE_AGENT + PLATFORM_ADMIN (page immediately)
  retry_policy    : NO_RETRY — manual forensic investigation required
  escrow_impact   : Affected escrow record frozen until integrity verified

CONFIDENCE_BELOW_THRESHOLD (< 0.65):
  action          : Force HUMAN_REVIEW track regardless of dispute type
  log             : LOG_LOW_CONFIDENCE with feature snapshot
  escalate_to     : COMPLIANCE_OFFICER review queue
  retry_policy    : No automatic retry — human decision required
  escrow_impact   : Escrow hold maintained during human review

SLA_BREACH_IMMINENT (90% of SLA time elapsed, no verdict):
  action          : ESCALATE_TRACK_UP (AUTO → HUMAN | HUMAN → PLATFORM_ADMIN)
  log             : LOG_SLA_BREACH_RISK
  escalate_to     : PLATFORM_ADMIN + relevant COMPLIANCE_OFFICER
  notification    : Both disputing parties notified of delay via NOTIFICATION_AGENT

SLA_BREACH_CONFIRMED:
  action          : Force escalation to next track + incident report generated
  log             : LOG_SLA_BREACH with full timeline
  escalate_to     : PLATFORM_ADMIN (mandatory response required)

EVIDENCE_VAULT_UNAVAILABLE:
  action          : Pause evidence-dependent ML scoring; hold dispute in PENDING_EVIDENCE state
  log             : LOG_INCIDENT + INFRASTRUCTURE_ALERT
  escalate_to     : PLATFORM_ENGINEERING_ONCALL
  retry_policy    : Auto-retry vault connection every 60 seconds for up to 30 minutes
  escrow_impact   : Hold maintained

FRAUD_DETECTION_AGENT_UNAVAILABLE:
  action          : All new disputes route to HUMAN_REVIEW track (no AUTO_RESOLVE)
  log             : LOG_INCIDENT
  escalate_to     : OBSERVABILITY_AGENT
  escrow_impact   : All new escrow holds maintained until fraud check can run

DUPLICATE_DISPUTE:
  action          : IDEMPOTENT_RETURN — return existing dispute record, no reprocessing
  log             : LOG_DUPLICATE_DETECTED with original dispute_id
  escrow_impact   : None

RULE: NO SILENT FAILURES. Every failure emits at minimum LOG_INCIDENT + one downstream event.
```

---

## 1️⃣1️⃣ INTER-AGENT DEPENDENCY MAP

```yaml
UPSTREAM_AGENTS:
  - MARKETPLACE_TRANSACTION_AGENT   : Triggers NON_DELIVERY, FRAUD_SUSPICION disputes
  - ROYALTY_ESCROW_AGENT            : Triggers CLAWBACK_REQUEST disputes; provides escrow state
  - FRAUD_DETECTION_AGENT           : Triggers FRAUD_SUSPICION auto-disputes
  - COMPLIANCE_AGENT                : Triggers COMPLIANCE_VIOLATION disputes
  - COPY_DETECTION_ENGINE           : Provides similarity evidence for ORIGINALITY_CHALLENGE
  - IDEA_DNA_AGENT                  : Provides originality evidence for ORIGINALITY_CHALLENGE
  - REPUTATION_AGENT                : Provides actor trust scores for triage and ML
  - FEATURE_STORE_AGENT             : Provides historical behavioral features

DOWNSTREAM_AGENTS:
  - ROYALTY_ESCROW_AGENT            : Receives HOLD_ORDER, RELEASE_ORDER, CLAWBACK_ORDER
  - PAYMENT_GATEWAY_AGENT           : Receives corrective payout instructions post-verdict
  - REPUTATION_AGENT                : Receives REPUTATION_IMPACT events
  - COMPLIANCE_AGENT                : Receives COMPLIANCE_INCIDENT records
  - NOTIFICATION_AGENT              : Receives dispute status change events
  - FEATURE_STORE_AGENT             : Receives behavioral feature vectors
  - OBSERVABILITY_AGENT             : Receives performance metrics + anomaly events
  - GROWTH_ENGINE_AGENT             : Receives XP_FREEZE or XP_PENALTY events on fraud verdicts

EVENT_TRIGGERS_EMITTED:
  - royalty.dispute.intake_confirmed
  - royalty.dispute.escrow_hold_issued
  - royalty.dispute.triage_complete
  - royalty.dispute.evidence_requested
  - royalty.dispute.human_review_queued
  - royalty.dispute.verdict_issued
  - royalty.dispute.escrow_release_order_sent
  - royalty.dispute.clawback_order_sent
  - royalty.dispute.reputation_impact_sent
  - royalty.dispute.compliance_incident_filed
  - royalty.dispute.legal_escalation_triggered
  - royalty.dispute.fraud_flag_issued
  - royalty.dispute.sla_breach_alert
  - royalty.dispute.closed
  - royalty.dispute.feature_vector_emitted

EVENT_SCHEMA (ALL events):
{
  event_id        : UUID,
  event_type      : string (from list above),
  source_agent    : "ROYALTY_DISPUTE_RESOLUTION_AGENT",
  tenant_id       : UUID,
  dispute_id      : UUID,
  transaction_id  : UUID,
  timestamp_utc   : ISO8601,
  payload         : { ...event-specific fields... },
  signature       : "agent private key signature"
}
```

---

## 1️⃣2️⃣ PASSIVE INTELLIGENCE COMPATIBILITY

This agent captures critical trust and behavioral signals. It **must** emit feature vectors:

```json
EMIT_FEATURE_VECTOR → FEATURE_STORE_AGENT (on every VERDICT_ISSUED or DISPUTE_CLOSED):
{
  "user_id"         : "UUID (for each disputing party, emitted separately)",
  "feature_name"    : "ENUM: [dispute_raised_rate_30d, dispute_win_rate_90d,
                               dispute_loss_rate_90d, fraud_flag_count_365d,
                               avg_dispute_resolution_days, evidence_submission_quality_score,
                               compliance_violation_count, clawback_received_count]",
  "feature_value"   : "numeric",
  "timestamp"       : "ISO8601",
  "source_agent"    : "ROYALTY_DISPUTE_RESOLUTION_AGENT",
  "tenant_id"       : "UUID",
  "model_version"   : "string"
}
```

These features feed: REPUTATION_AGENT, MATCH_SCORE_ENGINE, FRAUD_DETECTION_AGENT, and MARKETPLACE_TRUST_SCORER.

---

## 1️⃣3️⃣ INNOVATION ECONOMY COMPATIBILITY

For disputes touching intellectual property and originality:

```yaml
ORIGINALITY_CHALLENGE_HANDLING:
  RECEIVE_FROM IDEA_DNA_AGENT      : idea_vector, originality_score, semantic_similarity_map
  RECEIVE_FROM COPY_DETECTION_ENGINE: similarity_hash, copy_risk_level, matched_asset_ids

  GATE_RULE:
    - If copy_risk_level = CONFIRMED_COPY and similarity_score >= 0.90:
        → AUTO_HOLD escrow immediately
        → dispute_type forced to ORIGINALITY_CHALLENGE (overrides submitted type if different)
        → ml_recommendation auto-set to FAVOR_BUYER (pending human confirmation)
        → COPY_DETECTION_INCIDENT event emitted to COMPLIANCE_AGENT

  EMIT ON RESOLUTION:
    IDEA_VECTOR_OUTCOME:
      - idea_id
      - dispute_outcome (ORIGINAL_CONFIRMED | COPY_CONFIRMED | INCONCLUSIVE)
      - originality_score_final (post-dispute)
      - verdict_reference
    → IDEA_DNA_AGENT (to update asset originality status)
    → ROYALTY_ENGINE (to revise future royalty eligibility if copy confirmed)

COMPATIBLE_WITH:
  - IDEA_DNA_AGENT          : Bidirectional (receives evidence, emits outcome)
  - COPY_DETECTION_ENGINE   : Receives similarity hash and copy risk
  - ROYALTY_ESCROW_AGENT    : Issues hold/release/clawback instructions
  - ROYALTY_ENGINE          : Receives eligibility changes post-verdict
```

---

## 1️⃣4️⃣ GROWTH ENGINE HOOK

```yaml
ON_FRAUD_CONFIRMED_VERDICT:
  XP_FREEZE_EVENT:
    actor_id      : fraud-confirmed actor
    freeze_reason : "FRAUD_VERDICT"
    freeze_scope  : "ALL_XP_ACCRUAL"

  XP_PENALTY_EVENT:
    actor_id      : fraud-confirmed actor
    xp_penalty    : defined in GROWTH_ENGINE penalty tier for FRAUD_VERDICT

ON_CLAWBACK_ISSUED (seller loses):
  XP_ADJUSTMENT_EVENT:
    actor_id      : seller
    adjustment    : negative XP proportional to clawback amount tier

ON_BUYER_VINDICATED (false dispute dismissed against seller):
  SELLER_VINDICATION_EVENT:
    actor_id      : seller
    xp_bonus      : minor positive XP for clean dispute record
    rank_flag     : "DISPUTE_FREE_SELLER"

EMITTED_TO: GROWTH_ENGINE_AGENT
NOTE: XP events are advisory to GROWTH_ENGINE_AGENT — final XP math is GROWTH_ENGINE_AGENT's domain
```

---

## 1️⃣5️⃣ SLA MATRIX (LOCKED)

```yaml
DISPUTE_SEVERITY  | TRACK              | TRIAGE SLA  | RESOLUTION SLA | ESCALATION TRIGGER
──────────────────|────────────────────|─────────────|────────────────|──────────────────────
LOW               | AUTO_RESOLVE       | 30 minutes  | 24 hours       | 90% elapsed → HUMAN
MEDIUM            | HUMAN_REVIEW       | 1 hour      | 72 hours       | 90% elapsed → PLATFORM_ADMIN
HIGH              | HUMAN_REVIEW (exp) | 30 minutes  | 48 hours       | 80% elapsed → PLATFORM_ADMIN
CRITICAL          | LEGAL_ESCALATION   | 15 minutes  | No auto-SLA    | Immediate → LEGAL_TEAM
FRAUD_AUTO_HOLD   | HUMAN_REVIEW min   | Immediate   | 4 hour review  | Auto-escalates at intake

NOTE: SLA clock starts at INTAKE_RECEIVED confirmation timestamp.
SLA breach triggers track escalation AND compliance incident log entry.
SLA timers are monotonic and cannot be paused by any agent or actor.
```

---

## 1️⃣6️⃣ PERFORMANCE MONITORING

```yaml
METRICS_EMITTED → OBSERVABILITY_AGENT:
  - dispute_intake_success_rate         (target: > 99.5%)
  - triage_completion_rate_within_sla   (target: > 99.0%)
  - auto_resolve_accuracy_rate          (target: > 92% — verified against appeals)
  - human_review_sla_compliance_rate    (target: > 95%)
  - escrow_hold_issue_latency_p95       (target: < 500ms)
  - fraud_detection_precision           (target: > 88%)
  - fraud_detection_false_positive_rate (target: < 8%)
  - ml_confidence_score_avg             (target: > 0.75)
  - evidence_vault_write_success_rate   (target: > 99.9%)
  - legal_escalation_rate               (informational — trend monitoring)
  - dispute_rate_per_1000_transactions  (platform health KPI)
  - model_drift_indicator               (alert if KS stat > 0.15)
  - sla_breach_rate                     (target: < 1%)
  - model_version_in_use                (deployment tracking)

ALERTING_RULES:
  - triage_sla_breach_rate > 2%         → PAGE ONCALL
  - fraud_false_positive_rate > 10%     → ALERT ML_OPS immediately
  - escrow_hold_failure                 → PAGE ONCALL (P1)
  - evidence_vault_failure              → PAGE INFRASTRUCTURE_ONCALL (P1)
  - model_drift triggered               → ALERT ML_OPS
  - legal_escalation_rate spike > 3x    → ALERT COMPLIANCE_OFFICER + PLATFORM_ADMIN
```

---

## 1️⃣7️⃣ VERSIONING POLICY

```yaml
AGENT_VERSION            : v1.0.0
VERSIONING_STRATEGY      : Semantic versioning (MAJOR.MINOR.PATCH)
CHANGE_POLICY            : ADD-ONLY — no existing logic, schema, or rule removal

DISPUTE_RECORD_VERSIONING:
  - Every dispute record includes agent_version at time of intake
  - Dispute resolved under old version rules if resolution started before version change
  - Retroactive version changes FORBIDDEN

MODEL_VERSIONING:
  - Each ML model deployment tagged with semver
  - Old model versions retained 24 months minimum (legal dispute replay requirement)
  - New model versions require COMPLIANCE_AGENT sign-off before deployment
  - A/B shadow deployment required for MAJOR model changes before full rollout

SCHEMA_VERSIONING:
  - All DB schema changes additive-only
  - Migration scripts documented and tested before deployment
  - Rollback path documented for every schema migration
  - Blue-green deployment mandatory for agent updates

PROMPT_VERSIONING:
  - All AI prompts versioned in PROMPT_REGISTRY
  - Prompt changes require COMPLIANCE review for any change touching financial narratives
  - Old prompt versions retained for audit replay
```

---

## 1️⃣8️⃣ DISPUTE LIFECYCLE STATE MACHINE

```
DISPUTE EVENT RECEIVED
        │
        ▼
[INPUT VALIDATION] ──FAIL──► REJECT + LOG_INCIDENT → caller notified
        │
       PASS
        │
        ▼
[DUPLICATE CHECK] ──DUPLICATE──► IDEMPOTENT RETURN existing dispute record
        │
      UNIQUE
        │
        ▼
[ESCROW_HOLD_ORDER → ROYALTY_ESCROW_AGENT] (immediate, all disputes)
        │
        ▼
[INTAKE_CONFIRMED event emitted]
        │
        ▼
[FRAUD_PATTERN_DETECTOR] ──fraud_confidence >= 0.80──► TRACK_4_AUTO_FRAUD_HOLD
        │                                                    │
      CLEAR                                      [SECURITY_ALERT + HUMAN_REVIEW forced]
        │
        ▼
[DISPUTE_TRIAGE_CLASSIFIER]
        │
    ┌───┴────────────────────────────────┐
    │                                    │
  LOW (confidence >= 0.85)        MEDIUM/HIGH/CRITICAL
    │                               or confidence < 0.85
    ▼                                    │
[TRACK_1 AUTO_RESOLVE]           [TRACK_2 HUMAN_REVIEW]
    │                              or [TRACK_3 LEGAL_ESCALATION]
    ▼                                    │
[EVIDENCE_WEIGHT_SCORER]         [EVIDENCE_REQUEST sent to parties]
    │                                    │
    ▼                                    ▼
[DISPUTE_OUTCOME_PREDICTOR]      [EVIDENCE_WEIGHT_SCORER]
    │                                    │
    ▼                                    ▼
confidence >= 0.85? ──NO──►     [DISPUTE_OUTCOME_PREDICTOR]
    │                                    │
   YES                          [AI CASE SUMMARY GENERATED]
    │                                    │
    ▼                                    ▼
[AI VERDICT RATIONALE           [COMPLIANCE_OFFICER REVIEW QUEUE]
 GENERATED]                             │
    │                         [COMPLIANCE_OFFICER VERDICT ISSUED]
    ▼                                    │
[VERDICT_ISSUED]◄────────────────────────┘
        │
        ▼
[ESCROW_INSTRUCTION emitted → ROYALTY_ESCROW_AGENT]
  (RELEASE | CLAWBACK | PARTIAL_RELEASE)
        │
        ▼
[REPUTATION_IMPACT_EVENT → REPUTATION_AGENT]
        │
        ▼
[FEATURE_VECTOR_EMITTED → FEATURE_STORE_AGENT]
        │
        ▼
[XP/GROWTH events if applicable → GROWTH_ENGINE_AGENT]
        │
        ▼
[DISPUTE_AUDIT_LOG entry appended — IMMUTABLE]
        │
        ▼
[NOTIFICATION sent to both parties via NOTIFICATION_AGENT]
        │
        ▼
        ✅ DISPUTE_CLOSED event emitted
```

---

## 1️⃣9️⃣ DATABASE SCHEMA (REFERENCE — ADDITIVE ONLY)

```sql
-- Core dispute registry
CREATE TABLE dispute_registry (
  dispute_id              UUID PRIMARY KEY,
  tenant_id               UUID NOT NULL,
  transaction_id          UUID NOT NULL,
  escrow_record_id        UUID NOT NULL,
  parent_dispute_id       UUID REFERENCES dispute_registry(dispute_id),
  dispute_type            VARCHAR(40) NOT NULL,
  dispute_status          VARCHAR(40) NOT NULL,
  severity_level          VARCHAR(16) NOT NULL,
  resolution_track        VARCHAR(32) NOT NULL,
  raised_by_actor_id      UUID NOT NULL,
  raised_by_role          VARCHAR(32) NOT NULL,
  respondent_actor_id     UUID NOT NULL,
  respondent_role         VARCHAR(32) NOT NULL,
  asset_id                UUID NOT NULL,
  asset_type              VARCHAR(32) NOT NULL,
  disputed_amount         DECIMAL(18,4) NOT NULL,
  currency                CHAR(3) NOT NULL,
  source_trigger          VARCHAR(40) NOT NULL,
  fraud_risk_score        DECIMAL(5,4),
  originality_score       DECIMAL(5,4),
  similarity_hash         VARCHAR(64),
  compliance_ref          UUID,
  sla_deadline_utc        TIMESTAMP WITH TIME ZONE NOT NULL,
  intake_timestamp_utc    TIMESTAMP WITH TIME ZONE NOT NULL,
  closed_timestamp_utc    TIMESTAMP WITH TIME ZONE,
  agent_version           VARCHAR(20) NOT NULL,
  model_version           VARCHAR(20),
  ml_recommendation       VARCHAR(40),
  ml_confidence_score     DECIMAL(5,4),
  verdict_outcome         VARCHAR(50),
  verdict_issued_by       VARCHAR(32),
  verdict_rationale       TEXT,
  escrow_instruction      VARCHAR(32),
  release_amount          DECIMAL(18,4),
  clawback_amount         DECIMAL(18,4)
  -- NO DELETE permissions on this table
);

-- Immutable evidence vault manifest
CREATE TABLE dispute_evidence (
  evidence_id             UUID PRIMARY KEY,
  dispute_id              UUID NOT NULL REFERENCES dispute_registry(dispute_id),
  tenant_id               UUID NOT NULL,
  submitted_by_actor_id   UUID NOT NULL,
  submitted_by_role       VARCHAR(32) NOT NULL,
  evidence_type           VARCHAR(40) NOT NULL,
  evidence_payload_hash   CHAR(64) NOT NULL,
  evidence_storage_ref    VARCHAR(512) NOT NULL,
  evidence_weight         DECIMAL(5,4),
  submitted_at            TIMESTAMP WITH TIME ZONE NOT NULL
  -- NO UPDATE, NO DELETE permissions on this table
);

-- Immutable dispute audit trail
CREATE TABLE dispute_audit_log (
  audit_id                UUID PRIMARY KEY,
  timestamp_utc           TIMESTAMP WITH TIME ZONE NOT NULL,
  tenant_id               UUID NOT NULL,
  actor_id                UUID NOT NULL,
  dispute_id              UUID NOT NULL,
  transaction_id          UUID NOT NULL,
  action_type             VARCHAR(50) NOT NULL,
  input_hash              CHAR(64) NOT NULL,
  output_hash             CHAR(64) NOT NULL,
  model_version           VARCHAR(20),
  prompt_version          VARCHAR(20),
  decision_path           VARCHAR(40) NOT NULL,
  confidence_score        DECIMAL(5,4),
  anomaly_flags           JSONB,
  verdict_outcome         VARCHAR(50),
  escrow_instruction      VARCHAR(32),
  compliance_ref          UUID,
  prior_entry_hash        CHAR(64) NOT NULL,  -- cryptographic chain
  signature               TEXT NOT NULL
  -- NO UPDATE, NO DELETE permissions on this table
);
```

---

## 2️⃣0️⃣ NON-NEGOTIABLE RULES

```
THIS AGENT MUST NOT:
  ❌  Issue a verdict without completing triage
  ❌  Release escrow during an open dispute without an explicit VERDICT_ISSUED record
  ❌  Allow AI layer to issue, modify, or override any verdict
  ❌  Allow AI layer to issue ESCROW_HOLD, RELEASE, or CLAWBACK orders
  ❌  Modify or delete any dispute record after creation
  ❌  Modify or delete any submitted evidence
  ❌  Modify or delete any audit log entry
  ❌  Allow cross-tenant access to any dispute, evidence, or verdict data
  ❌  Skip the ESCROW_HOLD step on dispute intake (holds are mandatory and immediate)
  ❌  Auto-resolve disputes where fraud_confidence >= 0.80 without human review
  ❌  Auto-resolve disputes where ML confidence < 0.65 without human review
  ❌  Process disputes without a valid escrow_record_id on file
  ❌  Create hidden logic or undocumented resolution branches
  ❌  Override COMPLIANCE_AGENT compliance violation flags
  ❌  Silently fail any operation — every failure emits a log event
  ❌  Allow any actor to retract submitted evidence
  ❌  Modify verdict records — corrections require new dispute with parent_dispute_id

THIS AGENT MUST ALWAYS:
  ✅  Issue ESCROW_HOLD within 60 seconds of INTAKE_RECEIVED on all dispute types
  ✅  Validate tenant_id on every single operation
  ✅  Include audit_reference UUID in every output
  ✅  Store model_version with every ML prediction
  ✅  Emit at least one downstream event per operation
  ✅  Force HUMAN_REVIEW when ML confidence < 0.65
  ✅  Force LEGAL_ESCALATION when severity = CRITICAL
  ✅  Escalate SLA-breached disputes automatically
  ✅  Emit FEATURE_VECTOR after every VERDICT_ISSUED or DISPUTE_CLOSED
  ✅  Append cryptographically chained audit log entry for every state transition
  ✅  Notify both parties at every dispute status change via NOTIFICATION_AGENT
  ✅  Respect domain isolation — compliance officers see only their assigned domain
  ✅  Preserve all evidence for minimum 7 years (legal requirement)
  ✅  Operate statelessly — all state persisted to distributed store
```

---

## 🔐 SEAL DECLARATION

```
╔══════════════════════════════════════════════════════════════════════════════════╗
║                           AGENT PROMPT SEALED                                   ║
║                                                                                  ║
║  AGENT             : ROYALTY_DISPUTE_RESOLUTION_AGENT                           ║
║  PLATFORM          : ECOSKILLER ANTIGRAVITY                                     ║
║  VERSION           : v1.0.0                                                     ║
║  MUTATION_POLICY   : ADD-ONLY — No section may be removed or overwritten        ║
║  STATUS            : LOCKED FOR PRODUCTION                                      ║
║                                                                                  ║
║  This agent prompt is the authoritative specification.                          ║
║  Any implementation deviating from this spec is non-compliant.                 ║
║  All changes must increment version and pass COMPLIANCE_AGENT sign-off.         ║
║                                                                                  ║
║  CREATIVE_INTERPRETATION     = FORBIDDEN                                        ║
║  ASSUMPTION_FILLING          = FORBIDDEN                                        ║
║  AI VERDICT DECISIONS        = FORBIDDEN                                        ║
║  AI ESCROW INSTRUCTIONS      = FORBIDDEN                                        ║
║  EVIDENCE MODIFICATION       = FORBIDDEN                                        ║
║  VERDICT MODIFICATION        = FORBIDDEN                                        ║
║  CROSS-TENANT QUERIES        = FORBIDDEN                                        ║
║  SILENT FAILURES             = FORBIDDEN                                        ║
║  ESCROW RELEASE DURING OPEN DISPUTE WITHOUT VERDICT = FORBIDDEN                ║
╚══════════════════════════════════════════════════════════════════════════════════╝
```

---

*ROYALTY_DISPUTE_RESOLUTION_AGENT v1.0.0 — Ecoskiller Antigravity Intelligence & Innovation Core*
*Classification: INTERNAL — SEALED — NOT FOR EXTERNAL DISTRIBUTION*
*Retention: This document must be preserved for the operational lifetime of the platform + 7 years*
