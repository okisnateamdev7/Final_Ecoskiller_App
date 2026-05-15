# 🔒 CROSS_PLATFORM_TRUST_SCORE_AGENT
## ECOSKILLER ANTIGRAVITY - TRUST, IDENTITY & SAFEGUARDS
### STATUS: SEALED · LOCKED · GOVERNED · DETERMINISTIC
**Version:** 1.0.0  
**Mutation Policy:** Add-only via version bump  
**Interpretation Authority:** NONE  
**Execution Authority:** Human declaration only  
**Classification:** ENTERPRISE CRITICAL AGENT

---

## 1️⃣ AGENT IDENTITY (MANDATORY)

```yaml
AGENT_NAME: CROSS_PLATFORM_TRUST_SCORE_AGENT
SYSTEM_ROLE: Trust & Identity Verification Engine
PRIMARY_DOMAIN: Trust, Identity, Safeguards, Cross-Platform Reputation
EXECUTION_MODE: Deterministic + Validated
DATA_SCOPE: User Identity, Verification Records, Trust Signals, Cross-Platform Links
TENANT_SCOPE: Strict Isolation (Multi-Tenant Enforced)
FAILURE_POLICY: Halt on ambiguity, Log all anomalies, Escalate security violations
SECURITY_LEVEL: CRITICAL
COMPLIANCE_REQUIREMENTS: GDPR, SOC2, ISO27001, Minor Protection Laws
```

---

## 2️⃣ PURPOSE DECLARATION

### Problem This Agent Solves
In a multi-tenant SaaS ecosystem serving students, trainers, enterprises, institutes, and recruiters, establishing **verifiable trust** across platforms is critical. The agent solves:

1. **Identity Verification Crisis**: Fake profiles, impersonation, credential fraud
2. **Cross-Platform Trust Gap**: LinkedIn, GitHub, university credentials cannot be auto-trusted
3. **Reputation Portability**: User achievements in one domain must be trustworthy in another
4. **Minor Protection**: Students under 18 require parental consent and monitoring
5. **Enterprise Hiring Risk**: Recruiters need verified skill claims and work history
6. **Institute Integrity**: Educational institutions need student verification
7. **SME Reliability**: Small businesses need credibility scoring
8. **Multi-Intelligence Trust**: Passive intelligence scores must be tamper-proof

### Input Consumption
```json
{
  "user_identity_claim": {
    "user_id": "UUID",
    "tenant_id": "UUID",
    "identity_type": "student|trainer|recruiter|enterprise|institute",
    "verification_request_type": "email|phone|govt_id|institution|linkedin|github|parent_consent",
    "external_platform_link": "URL_optional",
    "claim_metadata": {}
  },
  "trust_signal_event": {
    "event_type": "project_completion|skill_verification|endorsement|certification|behavioral_pattern",
    "source_agent": "STRING",
    "evidence_hash": "SHA256",
    "timestamp_utc": "ISO8601"
  }
}
```

### Output Production
```json
{
  "trust_score": {
    "overall_score": 0-100,
    "identity_verification_score": 0-100,
    "reputation_score": 0-100,
    "behavioral_trust_score": 0-100,
    "cross_platform_trust_score": 0-100,
    "risk_flags": [],
    "verification_status": "verified|partial|unverified|suspicious",
    "trust_level": "high|medium|low|critical_risk"
  },
  "verification_evidence": {
    "verified_attributes": [],
    "verification_timestamps": {},
    "evidence_chain": []
  },
  "confidence_score": 0.0-1.0,
  "model_version": "1.0.0",
  "audit_reference": "UUID",
  "next_trigger_event": ["reputation_update", "verification_expiry_check"]
}
```

### Downstream Agent Dependencies
- `PARENT_TRUST_GUARDIAN_AGENT`: Receives minor protection signals
- `RECRUITER_MATCHING_AGENT`: Consumes trust scores for hiring decisions
- `SKILL_VERIFICATION_AGENT`: Uses trust data for skill authenticity
- `INTELLIGENCE_DNA_AGENT`: Receives tamper-proof intelligence scores
- `REPUTATION_ENGINE`: Feeds verified trust signals
- `FRAUD_DETECTION_AGENT`: Receives anomaly flags
- `AUDIT_LOG_AGENT`: Records all trust events immutably

### Upstream Agent Feeds
- `IDENTITY_PROVIDER_AGENT`: Supplies authentication events
- `EXTERNAL_PLATFORM_INTEGRATION_AGENT`: LinkedIn/GitHub verification data
- `BEHAVIORAL_ANALYTICS_AGENT`: User behavior patterns
- `PROJECT_VERIFICATION_AGENT`: Project completion evidence
- `SKILL_ASSESSMENT_AGENT`: Skill validation results
- `INSTITUTE_VERIFICATION_AGENT`: Educational credential verification
- `PASSIVE_INTELLIGENCE_ENGINE`: Intelligence scores from behavioral analysis

---

## 3️⃣ INPUT CONTRACT (STRICT)

```yaml
INPUT_SCHEMA:
  required_fields:
    - user_id: UUID (Non-null, Indexed)
    - tenant_id: UUID (Non-null, Indexed, Isolation enforced)
    - request_type: ENUM[identity_verification, trust_score_calculation, cross_platform_link, reputation_update]
    - timestamp_utc: ISO8601 (Non-null)
    - request_source: STRING (Agent name or API endpoint)
  
  optional_fields:
    - external_platform_data: OBJECT (LinkedIn, GitHub, Portfolio URLs)
    - parent_consent_token: UUID (Required if user.age < 18)
    - verification_documents: ARRAY[document_hash] (Encrypted references)
    - behavioral_signals: OBJECT (Passive intelligence data)
  
  validation_rules:
    - user_id MUST exist in Identity_Provider database
    - tenant_id MUST match user's tenant (No cross-tenant access)
    - If user.age < 18 → parent_consent_token REQUIRED
    - external_platform_data MUST be OAuth-verified or webhook-confirmed
    - All document_hash references MUST exist in secure storage
    - timestamp_utc MUST NOT be future date
    - request_source MUST be whitelisted agent or authenticated API
  
  security_checks:
    - Check tenant_isolation_token
    - Verify RBAC permissions (only authorized agents can trigger)
    - Validate input size limits (max 10MB per request)
    - Scan for SQL injection patterns
    - Validate all UUIDs (no malformed IDs)
    - Check rate limiting per user/tenant
  
  domain_checks:
    - If identity_type = "student" → must have institution_id
    - If identity_type = "trainer" → must have skill_domain
    - If identity_type = "recruiter" → must have company_id
    - If identity_type = "enterprise" → verify L1-L7 hierarchy position
    - Cross-domain verification requests DENIED unless explicitly permitted
```

**Failure Behavior:**
- **Invalid Input**: Log `INPUT_VALIDATION_FAILURE`, Return HTTP 400, Audit trail created
- **Missing Tenant**: STOP_EXECUTION, Log `TENANT_ISOLATION_VIOLATION`, Alert security team
- **Minor Without Consent**: DENY, Log `MINOR_PROTECTION_FAILURE`, Notify parent guardian
- **Malformed UUID**: REJECT, Log `MALFORMED_REQUEST`, Increment fraud counter

---

## 4️⃣ OUTPUT CONTRACT (STRICT)

```yaml
OUTPUT_SCHEMA:
  result_object:
    trust_score_composite:
      overall_trust_score: INTEGER (0-100)
      identity_verification_score: INTEGER (0-100)
      reputation_score: INTEGER (0-100)
      behavioral_trust_score: INTEGER (0-100)
      cross_platform_trust_score: INTEGER (0-100)
    
    verification_status:
      status: ENUM[verified, partial_verified, unverified, suspended, flagged]
      verified_attributes: ARRAY[STRING] # ["email", "phone", "linkedin", "github"]
      last_verification_timestamp: ISO8601
      verification_expiry_date: ISO8601 (NULL if permanent)
    
    risk_assessment:
      risk_level: ENUM[low, medium, high, critical]
      risk_flags: ARRAY[STRING] # ["multiple_accounts_detected", "suspicious_pattern"]
      fraud_score: FLOAT (0.0-1.0)
      anomaly_detected: BOOLEAN
    
    cross_platform_links:
      linkedin_verified: BOOLEAN
      github_verified: BOOLEAN
      portfolio_verified: BOOLEAN
      institution_verified: BOOLEAN
      external_certifications: ARRAY[OBJECT]
    
    trust_signals:
      endorsements_received: INTEGER
      projects_completed: INTEGER
      skills_verified: INTEGER
      intelligence_consistency_score: FLOAT (0.0-1.0)
      behavior_stability_score: FLOAT (0.0-1.0)
  
  confidence_score: FLOAT (0.0-1.0)
  model_version: STRING (Immutable reference)
  audit_reference: UUID (Unique per execution)
  execution_timestamp_utc: ISO8601
  
  next_trigger_event:
    - event_name: "reputation_score_update"
      target_agent: "REPUTATION_ENGINE"
      trigger_condition: "trust_score_change > 10"
    - event_name: "verification_expiry_check"
      target_agent: "VERIFICATION_SCHEDULER_AGENT"
      trigger_delay_hours: 720 # 30 days

  metadata:
    processing_time_ms: INTEGER
    data_sources_used: ARRAY[STRING]
    ai_model_used: STRING (if applicable)
```

**Output Guarantees:**
- All scores MUST be deterministic for same input
- Confidence score < 0.6 → Flag for manual review
- Risk flags MUST trigger downstream security agents
- All outputs MUST include audit_reference for traceability
- NO personal identifiable information (PII) in plain text
- All external URLs MUST be sanitized and validated

---

## 5️⃣ ML / AI LOGIC LAYER

### ML-Based Components (70% of agent logic)

#### A. Trust Score Regression Model
```yaml
MODEL_TYPE: Gradient Boosting Regressor (XGBoost)
PURPOSE: Predict overall trust score based on multi-dimensional signals

FEATURES_USED:
  Identity Verification Features (20%):
    - email_verified: BOOLEAN
    - phone_verified: BOOLEAN
    - govt_id_verified: BOOLEAN
    - institution_verified: BOOLEAN
    - biometric_verified: BOOLEAN (optional)
    - verification_method_count: INTEGER
  
  Behavioral Features (30%):
    - account_age_days: INTEGER
    - login_frequency_score: FLOAT
    - activity_consistency_score: FLOAT (daily/weekly patterns)
    - session_duration_avg_minutes: FLOAT
    - device_consistency_score: FLOAT (same devices over time)
    - geographic_consistency_score: FLOAT
  
  Reputation Features (25%):
    - endorsements_received: INTEGER
    - projects_completed: INTEGER
    - skills_verified_count: INTEGER
    - certifications_earned: INTEGER
    - community_contributions: INTEGER
    - positive_feedback_ratio: FLOAT
  
  Cross-Platform Features (15%):
    - linkedin_profile_completeness: FLOAT (0-1)
    - github_contributions_score: INTEGER
    - external_certifications_verified: INTEGER
    - portfolio_quality_score: FLOAT
  
  Intelligence Consistency Features (10%):
    - passive_intelligence_stability: FLOAT (variance over time)
    - test_vs_behavioral_alignment: FLOAT
    - skill_claim_vs_evidence_ratio: FLOAT

TRAINING_FREQUENCY: Weekly (every Sunday 02:00 UTC)
TRAINING_DATA_WINDOW: Last 180 days
MINIMUM_TRAINING_SAMPLES: 10,000 users per tenant category

DRIFT_DETECTION:
  - Monitor score distribution shift (KL divergence < 0.05)
  - Monitor feature importance changes (top 10 features stable)
  - Monitor prediction accuracy on holdout set (RMSE < 5.0)
  - Alert if model performance degrades by >10%

VERSION_CONTROL:
  - Model stored: /ml_models/trust_score_v{version}.pkl
  - Feature pipeline: /ml_models/trust_feature_pipeline_v{version}.pkl
  - Immutable references in database: model_version_id
  - Rollback policy: Automatic if production accuracy drops >15%
```

#### B. Fraud Detection Classifier
```yaml
MODEL_TYPE: Random Forest Classifier
PURPOSE: Binary classification (legitimate / fraudulent account)

FEATURES_USED:
  - Multiple account signals (same device, IP patterns)
  - Rapid verification attempts
  - Inconsistent profile data
  - Behavioral anomalies (bot-like patterns)
  - External platform mismatch signals
  - Verification document image analysis scores

TRAINING_FREQUENCY: Weekly
OUTPUT: fraud_probability (0.0-1.0)
THRESHOLD: >0.75 → Flag for manual review
POSITIVE_CLASS_HANDLING: SMOTE oversampling (fraud cases are rare)
```

#### C. Cross-Platform Link Verification Classifier
```yaml
MODEL_TYPE: Binary Logistic Regression
PURPOSE: Verify authenticity of LinkedIn/GitHub profile links

FEATURES:
  - Profile age vs Ecoskiller account age
  - Skills overlap score
  - Name similarity score
  - Profile picture similarity (if available)
  - Activity timeline correlation
  - Connection/follower count reasonableness

TRAINING: Monthly
VERIFICATION_CONFIDENCE: >0.80 → Accepted as verified
```

#### D. Behavioral Trust Time-Series Model
```yaml
MODEL_TYPE: LSTM (Long Short-Term Memory) for time-series
PURPOSE: Predict trust trajectory and detect sudden changes

INPUT_SEQUENCE: Last 90 days of daily behavioral metrics
OUTPUT: Trust stability score + anomaly detection

ANOMALY_DETECTION:
  - Sudden burst of activity after dormancy → Suspicious
  - Unusual login patterns → Flag
  - Device changes without notification → Risk signal
```

### AI-Based Components (20% of agent logic)

#### A. Document Verification AI
```yaml
AI_USAGE_SCOPE: OCR + Text extraction from government IDs, certificates
MODEL: Pre-trained Vision Transformer (ViT) + Fine-tuned OCR
PURPOSE: Extract and verify document authenticity

PROMPT_GOVERNANCE:
  - Versioned prompt templates for document classification
  - Deterministic extraction rules (no creative interpretation)
  - Confidence threshold: >0.85 → Accept, <0.85 → Human review

SECURITY:
  - Documents encrypted at rest
  - Access logs immutable
  - Automatic PII redaction in logs
  - GDPR-compliant retention (deleted after verification)
```

#### B. Natural Language Trust Signal Analyzer
```yaml
AI_USAGE_SCOPE: Analyze user-generated content for trust signals
MODEL: Fine-tuned BERT for trust/reputation classification

USE_CASES:
  - Analyze project descriptions for authenticity
  - Detect plagiarized portfolio content
  - Evaluate endorsement quality (generic vs specific)
  - Identify bot-generated reviews

PROMPT_GOVERNANCE:
  - Strict input sanitization
  - No decision autonomy (AI advises, ML decides)
  - Versioned inference pipeline
```

#### C. Multi-Modal Intelligence Consistency Validator
```yaml
AI_USAGE_SCOPE: Cross-validate passive intelligence claims with artifacts
PURPOSE: Detect manipulation of intelligence scores

VALIDATION_CHECKS:
  - Linguistic intelligence vs writing samples quality
  - Logical intelligence vs coding problem solutions
  - Spatial intelligence vs design portfolio
  - Interpersonal intelligence vs community interactions

OUTPUT: consistency_score (0.0-1.0)
THRESHOLD: <0.5 → Flag for investigation
```

---

## 6️⃣ SCALABILITY DESIGN

```yaml
EXPECTED_RPS: 5,000 requests/second (peak load)
LATENCY_TARGET: 
  - P50: <100ms
  - P95: <250ms
  - P99: <500ms

MAX_CONCURRENCY: 50,000 parallel requests
QUEUE_STRATEGY: Redis-based FIFO queue with priority levels
  - PRIORITY_HIGH: Real-time verification during onboarding
  - PRIORITY_MEDIUM: Periodic trust score recalculation
  - PRIORITY_LOW: Background cross-platform sync

HORIZONTAL_SCALING:
  - Kubernetes HPA (Horizontal Pod Autoscaler)
  - Target CPU: 70%
  - Min replicas: 3
  - Max replicas: 50
  - Scale-up threshold: Avg latency >200ms for 2 minutes

STATELESS_EXECUTION:
  - NO in-memory session state
  - All state fetched from database or cache
  - Idempotent operations (same request → same result)

ASYNC_PROCESSING:
  - Trust score recalculations → Background jobs
  - Cross-platform verification → Webhook listeners
  - Batch processing for non-urgent updates

CACHING_STRATEGY:
  - Trust scores cached for 1 hour (Redis)
  - Verification status cached for 24 hours
  - Cache invalidation on verification events

DATABASE_SHARDING:
  - Shard key: tenant_id
  - Read replicas for trust score queries
  - Write master for verification events
```

---

## 7️⃣ SECURITY ENFORCEMENT

### Tenant Isolation Validation
```yaml
RULE: NO cross-tenant data access under any circumstances
ENFORCEMENT:
  - Every query includes WHERE tenant_id = :current_tenant
  - Database row-level security policies
  - API gateway enforces tenant context
  - Audit logs capture tenant_id for every operation

VIOLATION_RESPONSE:
  - Immediate request termination
  - Security alert triggered
  - Incident logged with full context
  - Auto-ban offending service account
```

### Domain Isolation Validation
```yaml
RULE: Trust signals from one domain (e.g., Arts) cannot influence another (e.g., Technology)
ENFORCEMENT:
  - Domain-specific trust score sub-components
  - Cross-domain trust transfer requires explicit user consent
  - Domain tags immutable post-creation

EXCEPTION: Platform-wide reputation (endorsements, verified identity) is cross-domain
```

### Role-Based Authorization
```yaml
PERMITTED_ACTORS:
  - Identity_Provider_Agent: Can trigger verification workflows
  - Recruiter_Matching_Agent: Read-only access to trust scores
  - Admin_Users: Can flag suspicious accounts (logged)
  - Parent_Guardian: Read-only access to minor's trust data
  - User (self): View own trust score breakdown

DENIED_ACTORS:
  - Other users (even within same tenant)
  - Non-whitelisted agents
  - External API without authentication

ACCESS_CONTROL_MATRIX:
  | Actor                     | Read Trust Score | Modify Trust Score | Flag Account |
  |---------------------------|------------------|--------------------|--------------|
  | User (self)               | ✅               | ❌                 | ❌           |
  | Parent (if minor)         | ✅               | ❌                 | ❌           |
  | Recruiter                 | ✅ (anonymized)  | ❌                 | ❌           |
  | Admin                     | ✅               | ❌                 | ✅           |
  | Fraud_Detection_Agent     | ✅               | ✅                 | ✅           |
```

### Encryption Enforcement
```yaml
AT_REST:
  - Database: AES-256-GCM encryption
  - Sensitive fields: govt_id_hash, biometric_hash → Encrypted separately
  - Key rotation: Every 90 days
  - Key storage: Hardware Security Module (HSM) or AWS KMS

IN_TRANSIT:
  - TLS 1.3 minimum
  - Certificate pinning for agent-to-agent communication
  - mTLS for inter-service requests

FIELD_LEVEL_ENCRYPTION:
  - parent_consent_token → Encrypted
  - external_platform_oauth_tokens → Encrypted + Short-lived (1 hour)
```

### Audit Logging (Append-Only)
```yaml
EVERY_OPERATION_LOGS:
  - timestamp_utc
  - actor_id (user_id or agent_id)
  - action_type (verification_requested, trust_score_calculated, fraud_flagged)
  - input_hash (SHA256 of request payload)
  - output_hash (SHA256 of response)
  - tenant_id
  - model_version (if ML used)
  - confidence_score
  - anomaly_flags
  - ip_address (hashed for privacy)
  - user_agent

LOG_STORAGE:
  - Immutable append-only storage (AWS S3 with object lock)
  - Retention: 7 years (compliance)
  - Tamper detection: Merkle tree integrity checks

ACCESS_LOG_TRACKING:
  - Who accessed which user's trust score (GDPR requirement)
  - Logged separately in access_audit_log table
```

---

## 8️⃣ AUDIT & TRACEABILITY

### Execution Log Format
```json
{
  "audit_id": "550e8400-e29b-41d4-a716-446655440000",
  "timestamp_utc": "2026-02-28T14:32:15.123Z",
  "actor_id": "agent:identity_provider_v1",
  "actor_type": "system_agent",
  "action": "trust_score_calculation",
  "user_id": "c8f9d4e2-5a3b-4c1d-8e2f-9a7b6c5d4e3f",
  "tenant_id": "7f8e9d6c-5b4a-3c2d-1e0f-9a8b7c6d5e4f",
  "input_hash": "sha256:a3c5f8...",
  "output_hash": "sha256:b7d2e9...",
  "model_version": "trust_score_xgboost_v1.2.3",
  "decision_path": {
    "identity_verification_score": 85,
    "behavioral_trust_score": 72,
    "reputation_score": 90,
    "cross_platform_trust_score": 68,
    "overall_trust_score": 79
  },
  "confidence_score": 0.89,
  "anomaly_flags": [],
  "processing_time_ms": 145,
  "data_sources": ["identity_db", "behavioral_analytics", "linkedin_api"],
  "geographic_source": "IN-GJ-Vapi",
  "compliance_tags": ["GDPR_compliant", "minor_protection_verified"]
}
```

### Immutability Guarantee
- Logs written to append-only storage
- Cryptographic hash chain (each log references previous log hash)
- No UPDATE or DELETE operations permitted
- Tamper detection via periodic Merkle tree validation

### Traceability Requirements
- Every trust score MUST be traceable to source events
- Every verification MUST link to evidence (document hash, OAuth token hash)
- Every fraud flag MUST reference triggering behavioral pattern
- Full audit trail available for compliance audits

---

## 9️⃣ FAILURE POLICY

### Invalid Input
```yaml
TRIGGER: Malformed JSON, missing required fields, invalid UUIDs
RESPONSE:
  - STOP_EXECUTION
  - LOG_INCIDENT: input_validation_failure
  - RETURN: HTTP 400 Bad Request
  - AUDIT_TRAIL: Capture malformed request
  - RETRY_POLICY: None (client must fix request)
```

### Model Unavailable
```yaml
TRIGGER: ML model file missing, model server down
RESPONSE:
  - FALLBACK_MODE: Use rule-based trust score calculation
  - LOG_INCIDENT: model_unavailable
  - ESCALATE_TO: DevOps on-call engineer
  - ALERT: Slack notification
  - RETRY_POLICY: Exponential backoff (1s, 2s, 4s, 8s, max 3 retries)
  - DEGRADED_SERVICE_FLAG: Set in status endpoint
```

### AI Timeout
```yaml
TRIGGER: Document OCR takes >5 seconds, LLM inference timeout
RESPONSE:
  - STOP_AI_CALL
  - LOG_INCIDENT: ai_timeout
  - FALLBACK: Queue for manual review
  - NOTIFY_USER: "Verification pending manual review (24-48 hours)"
  - ESCALATE_TO: Human verification queue
  - RETRY_POLICY: None (manual review takes over)
```

### Data Corruption
```yaml
TRIGGER: Database integrity check fails, hash mismatch, NULL in non-null field
RESPONSE:
  - STOP_EXECUTION
  - LOG_CRITICAL_INCIDENT: data_corruption_detected
  - ESCALATE_TO: Security team + Database admin
  - ALERT: PagerDuty critical alert
  - ROLLBACK_POLICY: Use last known good snapshot
  - QUARANTINE_DATA: Mark affected records as "under_review"
```

### Confidence Below Threshold
```yaml
TRIGGER: ML model confidence_score < 0.60
RESPONSE:
  - FLAG_FOR_REVIEW: Human verification required
  - LOG_INCIDENT: low_confidence_prediction
  - NOTIFY_USER: "Your verification is pending additional review"
  - ESCALATE_TO: Trust & Safety team review queue
  - SLA: Manual review within 48 hours
```

### Tenant Isolation Breach Attempt
```yaml
TRIGGER: Query attempts to access data from different tenant_id
RESPONSE:
  - IMMEDIATE_TERMINATION: Kill request
  - LOG_SECURITY_INCIDENT: tenant_isolation_breach_attempt
  - ESCALATE_TO: Security team (immediate escalation)
  - AUTO_BAN: Suspend offending service account for 1 hour
  - AUDIT_FULL_REQUEST: Capture all headers, payload, IP
  - COMPLIANCE_ALERT: Trigger security incident report
```

### No Silent Failures
**ABSOLUTE RULE**: Every failure MUST produce:
1. Log entry (audit trail)
2. Incident report (if severity > INFO)
3. User notification (if user-facing failure)
4. Downstream agent notification (if failure impacts other agents)

---

## 🔟 INTER-AGENT DEPENDENCY MAP

### Upstream Agents (Data Providers)
```yaml
IDENTITY_PROVIDER_AGENT:
  - Supplies: Authentication events, email/phone verification status
  - Event: user_authenticated, email_verified, phone_verified
  - Contract: identity_event_v1.0

EXTERNAL_PLATFORM_INTEGRATION_AGENT:
  - Supplies: LinkedIn profile data, GitHub activity, OAuth tokens
  - Event: external_profile_linked, oauth_verification_completed
  - Contract: external_platform_event_v1.0

BEHAVIORAL_ANALYTICS_AGENT:
  - Supplies: User activity patterns, session data, device fingerprints
  - Event: behavioral_pattern_detected, anomaly_flagged
  - Contract: behavioral_event_v1.0

PROJECT_VERIFICATION_AGENT:
  - Supplies: Project completion evidence, portfolio verification
  - Event: project_verified, portfolio_updated
  - Contract: project_event_v1.0

SKILL_ASSESSMENT_AGENT:
  - Supplies: Skill test results, peer endorsements
  - Event: skill_verified, endorsement_received
  - Contract: skill_event_v1.0

INSTITUTE_VERIFICATION_AGENT:
  - Supplies: Student enrollment verification, degree verification
  - Event: institution_verified, enrollment_confirmed
  - Contract: institute_event_v1.0

PASSIVE_INTELLIGENCE_ENGINE:
  - Supplies: Intelligence scores (linguistic, logical, spatial, etc.)
  - Event: intelligence_profile_updated, consistency_check_passed
  - Contract: intelligence_event_v1.0
```

### Downstream Agents (Data Consumers)
```yaml
PARENT_TRUST_GUARDIAN_AGENT:
  - Consumes: Minor protection flags, trust score updates
  - Event Sent: minor_trust_score_updated, consent_required
  - Contract: parent_guardian_event_v1.0

RECRUITER_MATCHING_AGENT:
  - Consumes: Trust scores, verification status
  - Event Sent: trust_score_updated (anonymized for GDPR)
  - Contract: recruiter_event_v1.0

SKILL_VERIFICATION_AGENT:
  - Consumes: Trust data for skill claim validation
  - Event Sent: trust_based_skill_verification
  - Contract: skill_verification_event_v1.0

INTELLIGENCE_DNA_AGENT:
  - Consumes: Tamper-proof intelligence score validation
  - Event Sent: intelligence_trust_validated
  - Contract: intelligence_dna_event_v1.0

REPUTATION_ENGINE:
  - Consumes: Verified trust signals for reputation calculation
  - Event Sent: reputation_source_verified
  - Contract: reputation_event_v1.0

FRAUD_DETECTION_AGENT:
  - Consumes: Anomaly flags, suspicious patterns
  - Event Sent: fraud_risk_detected, account_flagged
  - Contract: fraud_event_v1.0

AUDIT_LOG_AGENT:
  - Consumes: All trust events for immutable logging
  - Event Sent: trust_event_logged
  - Contract: audit_event_v1.0

NOTIFICATION_AGENT:
  - Consumes: Verification status changes
  - Event Sent: user_notification (verification_completed, review_required)
  - Contract: notification_event_v1.0
```

### Event Schema (Structured Events)
```json
{
  "event_type": "trust_score_updated",
  "event_id": "UUID",
  "timestamp_utc": "ISO8601",
  "source_agent": "cross_platform_trust_score_agent",
  "target_agent": "reputation_engine",
  "payload": {
    "user_id": "UUID",
    "tenant_id": "UUID",
    "trust_score_delta": 5,
    "new_trust_score": 84,
    "reason": "linkedin_profile_verified",
    "confidence": 0.92
  },
  "metadata": {
    "model_version": "1.2.3",
    "processing_time_ms": 150
  }
}
```

---

## 1️⃣1️⃣ PASSIVE INTELLIGENCE COMPATIBILITY

### Integration with Passive Intelligence System
```yaml
PURPOSE: Ensure intelligence scores from behavioral analysis are tamper-proof and trustworthy

FEATURE_STORE_EMISSION:
  - Emit trust-validated intelligence features to Feature Store Agent
  - Ensure consistency between claimed intelligence and demonstrated behavior

EMIT_FEATURE_VECTOR_FORMAT:
  {
    "user_id": "UUID",
    "tenant_id": "UUID",
    "feature_name": "linguistic_intelligence_trust_validated",
    "feature_value": 0.85,
    "trust_validation_score": 0.92,
    "timestamp": "ISO8601",
    "source_agent": "cross_platform_trust_score_agent",
    "evidence_sources": ["blog_posts_quality", "communication_analysis", "peer_feedback"],
    "consistency_check_passed": true
  }

VALIDATION_RULES:
  - If passive intelligence score deviates >20% from demonstrated skills → Flag inconsistency
  - If user claims high logical intelligence but fails basic coding tests → Reduce trust score
  - If linguistic intelligence high but writing samples are plagiarized → Fraud flag
  - If spatial intelligence high but design portfolio is stock templates → Verification required

CROSS-VALIDATION_WORKFLOW:
  1. Passive Intelligence Engine calculates intelligence scores from behavior
  2. Cross-Platform Trust Agent validates consistency with artifacts
  3. If consistency_score < 0.60 → Manual review queue
  4. If consistency_score > 0.85 → Boost trust score
  5. Emit validated intelligence features to Feature Store
```

---

## 1️⃣2️⃣ INNOVATION ECONOMY COMPATIBILITY

### Integration with Innovation & Idea Management Systems
```yaml
PURPOSE: Ensure originality and ownership of ideas, projects, and innovations

IDEA_DNA_VALIDATION:
  - When user submits an innovation/idea, validate it's not plagiarized
  - Cross-check against existing ideas in the platform
  - Generate IDEA_VECTOR for similarity detection
  - Calculate ORIGINALITY_SCORE

EMIT_TO_IDEA_DNA_AGENT:
  {
    "idea_id": "UUID",
    "user_id": "UUID",
    "idea_vector": [0.12, 0.45, 0.89, ...], # 512-dim embedding
    "similarity_hash": "SHA256",
    "originality_score": 0.78,
    "trust_contribution": 0.85, # User's trust score boosts idea credibility
    "verification_status": "trust_validated",
    "timestamp": "ISO8601"
  }

ROYALTY_ENGINE_INTEGRATION:
  - If user's trust score is high → Ideas get priority in innovation marketplace
  - If trust score is low but idea is original → Idea accepted but user reputation monitored
  - If trust score is low AND idea is similar to existing → Flag for review

COPY_DETECTION_ENGINE_INTEGRATION:
  - Continuous monitoring of user's submitted projects/ideas
  - If plagiarism detected → Trust score penalty (immediate -20 points)
  - If repeat offender → Account suspended, fraud flag raised
  - If false positive → Trust score restored + compensation bonus

INNOVATION_TRUST_SIGNALS:
  - Original ideas count: +5 trust points per verified original idea
  - Successful project completion: +10 trust points
  - Peer endorsements on projects: +2 points per endorsement
  - External recognition (awards, publications): +15 trust points
```

---

## 1️⃣3️⃣ GROWTH ENGINE HOOK

### Integration with Gamification & Reputation Systems
```yaml
PURPOSE: Translate trust score changes into user-visible achievements and ranks

RANK_UPDATE_EVENT:
  Trigger: When trust_score crosses threshold (50→60, 60→70, etc.)
  Event: rank_update_event
  Payload:
    {
      "user_id": "UUID",
      "new_rank": "trusted_contributor",
      "rank_badge": "verified_professional",
      "unlocked_features": ["external_recruiter_visibility", "premium_endorsements"],
      "rank_tier": 3
    }

XP_UPDATE_EVENT:
  Trigger: Trust score improvement
  Event: xp_update_event
  Payload:
    {
      "user_id": "UUID",
      "xp_gained": 50,
      "reason": "linkedin_profile_verified",
      "xp_multiplier": 1.2 # Higher trust → Higher XP multiplier
    }

SHARE_TRIGGER_EVENT:
  Trigger: Major verification milestone (e.g., "All verifications completed")
  Event: share_trigger_event
  Payload:
    {
      "user_id": "UUID",
      "shareable_achievement": "Fully Verified Professional",
      "share_text": "I'm now a Fully Verified Professional on Ecoskiller!",
      "share_image_url": "https://cdn.ecoskiller.com/badges/verified_pro.png",
      "social_platforms": ["linkedin", "twitter"]
    }

GAMIFICATION_MECHANICS:
  - Trust Score 0-30: "New Member" (limited visibility)
  - Trust Score 31-60: "Active Contributor" (standard features)
  - Trust Score 61-80: "Trusted Professional" (recruiter visibility)
  - Trust Score 81-100: "Elite Verified" (premium features, platform ambassador)

UNLOCKABLE_FEATURES_BY_TRUST:
  - Trust 60+: Enable external recruiter matching
  - Trust 70+: Enable project royalty marketplace participation
  - Trust 80+: Enable mentorship program (can mentor others)
  - Trust 90+: Platform ambassador badge (visible across platform)
```

---

## 1️⃣4️⃣ PERFORMANCE MONITORING

### Key Performance Indicators (KPIs)
```yaml
SUCCESS_RATE:
  - Metric: Percentage of successful trust score calculations
  - Target: >99.5%
  - Alert: If <98% for 5 consecutive minutes

ERROR_RATE:
  - Metric: Percentage of requests returning errors
  - Target: <0.5%
  - Alert: If >1% for 5 consecutive minutes

LATENCY_PERCENTILES:
  - P50: <100ms
  - P95: <250ms
  - P99: <500ms
  - Alert: If P99 >1000ms for 10 minutes

DRIFT_INDICATOR:
  - Metric: Model prediction distribution shift (KL divergence)
  - Target: <0.05
  - Alert: If >0.10 (model retraining required)

ANOMALY_FREQUENCY:
  - Metric: Fraud flags per 1000 users
  - Baseline: 5-10 flags per 1000 users
  - Alert: If >50 flags per 1000 users (possible attack)

VERIFICATION_COMPLETION_RATE:
  - Metric: % of users who complete full verification
  - Target: >70%
  - Track: Drop-off points in verification funnel

CROSS_PLATFORM_LINK_SUCCESS:
  - Metric: % of LinkedIn/GitHub links successfully verified
  - Target: >85%
  - Alert: If <70% (API issues or verification logic problem)

MANUAL_REVIEW_QUEUE_SIZE:
  - Metric: Number of accounts in manual review queue
  - Target: <100 at any time
  - Alert: If >500 (backlog building up)
```

### Integration with Observability Agent
```yaml
METRICS_EMISSION:
  - Push metrics to Prometheus every 15 seconds
  - Metric format: trust_score_agent_{metric_name}
  - Labels: tenant_id, environment (dev/staging/prod)

DISTRIBUTED_TRACING:
  - Jaeger trace ID propagation across all agent calls
  - Trace every trust score calculation end-to-end
  - Identify bottlenecks in ML model inference

LOGGING:
  - Structured JSON logs to ELK stack (Elasticsearch, Logstash, Kibana)
  - Log levels: DEBUG, INFO, WARN, ERROR, CRITICAL
  - Production: INFO and above
  - Development: DEBUG and above

ALERTING:
  - PagerDuty for critical alerts (data corruption, security breach)
  - Slack for warnings (high latency, model drift)
  - Email for informational (weekly performance report)

DASHBOARDS:
  - Grafana dashboard: "Trust Score Agent Health"
    - Request rate, latency, error rate
    - Trust score distribution histogram
    - Fraud detection rate
    - Verification funnel metrics
  - Grafana dashboard: "ML Model Performance"
    - Model accuracy over time
    - Feature importance drift
    - Prediction confidence distribution
```

---

## 1️⃣5️⃣ VERSIONING POLICY

### Add-Only Mutation Policy
```yaml
PRINCIPLE: Never modify existing logic; always add new versions

VERSION_BUMP_TRIGGERS:
  - New ML model trained
  - New verification method added
  - New trust signal incorporated
  - Security vulnerability patched
  - API contract change

VERSION_NUMBER_FORMAT: MAJOR.MINOR.PATCH
  - MAJOR: Breaking API changes (output schema changes)
  - MINOR: New features (new verification methods)
  - PATCH: Bug fixes, performance improvements

BACKWARD_COMPATIBILITY:
  - Old API versions supported for 6 months after deprecation
  - Deprecated endpoints return HTTP 410 Gone after sunset
  - Migration guide published 3 months before sunset
```

### Model Versioning
```yaml
MODEL_REGISTRY:
  - Location: /ml_models/trust_score_v{major}.{minor}.{patch}.pkl
  - Metadata: /ml_models/trust_score_v{version}_metadata.json
  - Metadata includes:
    - Training date
    - Training data window
    - Feature list
    - Performance metrics (RMSE, MAE, R²)
    - Approval status (staging, production)

ROLLBACK_PROCEDURE:
  1. Detect model performance degradation (automated alerts)
  2. Compare current model vs previous model on validation set
  3. If current model is worse → Auto-rollback to previous model
  4. Log rollback event in audit trail
  5. Notify ML team for investigation
```

### Database Schema Versioning
```yaml
SCHEMA_EVOLUTION:
  - All schema changes via migration scripts
  - Migration scripts versioned (001_initial.sql, 002_add_linkedin_verification.sql)
  - No destructive migrations (no DROP TABLE, DROP COLUMN)
  - New columns added with default values or NULL

DATA_MIGRATION:
  - If new trust signal added → Backfill historical data in batches
  - Batch size: 10,000 records per batch
  - Rate limit: 1 batch per minute (avoid DB overload)
```

---

## 1️⃣6️⃣ NON-NEGOTIABLE RULES

### Absolute Prohibitions
```yaml
AGENT_MUST_NOT:
  1. Create hidden logic: All business rules MUST be documented in code + documentation
  2. Modify historical records: Trust scores are append-only (new version created, old preserved)
  3. Auto-delete audit logs: Audit logs retained for 7 years minimum (compliance)
  4. Override governance agents: Cannot bypass fraud detection or compliance checks
  5. Bypass compliance checks: GDPR, minor protection laws MUST be enforced
  6. Mix domain data: Arts domain trust signals isolated from Technology domain
  7. Execute outside scope: Only calculate trust scores; no user management, no billing

SECURITY_RULES:
  - NO plain-text storage of sensitive data (govt IDs, biometrics)
  - NO cross-tenant queries (hard failure if attempted)
  - NO data exfiltration to external APIs without encryption + audit
  - NO AI decision autonomy beyond confidence scoring

DATA_INTEGRITY_RULES:
  - NO NULL in required fields (database constraints enforced)
  - NO duplicate trust scores for same user at same timestamp
  - NO trust score >100 or <0 (validation at API layer)

COMPLIANCE_RULES:
  - NO processing of minor data without parent consent
  - NO sharing of PII with third parties without explicit user consent
  - NO data retention beyond legal requirements (GDPR Article 5)
```

---

## 🔐 SECURITY HARDENING CHECKLIST

```yaml
✅ Input Validation:
  - SQL injection protection (parameterized queries)
  - XSS protection (input sanitization)
  - CSRF tokens for state-changing operations
  - Rate limiting (100 requests per user per minute)

✅ Authentication & Authorization:
  - mTLS for agent-to-agent communication
  - JWT tokens with short expiry (15 minutes)
  - API key rotation every 90 days
  - RBAC enforced at API gateway

✅ Data Protection:
  - Field-level encryption for sensitive data
  - Database encryption at rest (AES-256)
  - TLS 1.3 for data in transit
  - Key management via HSM/KMS

✅ Audit & Monitoring:
  - Immutable audit logs (append-only)
  - Real-time anomaly detection
  - SIEM integration for security events
  - Penetration testing quarterly

✅ Compliance:
  - GDPR data subject request handling (export, delete)
  - Minor protection (COPPA, GDPR-K)
  - SOC2 Type II compliance
  - ISO 27001 controls implemented

✅ Incident Response:
  - Documented incident response plan
  - Security breach notification within 72 hours (GDPR)
  - Automated rollback procedures
  - Post-incident review process
```

---

## 📊 TRUST SCORE CALCULATION ALGORITHM

### Composite Trust Score Formula
```python
def calculate_trust_score(user_data, behavioral_data, reputation_data, cross_platform_data):
    """
    Deterministic trust score calculation
    Input: User identity, behavioral patterns, reputation signals, cross-platform links
    Output: Trust score (0-100)
    """
    
    # Component 1: Identity Verification Score (0-100)
    identity_score = (
        (email_verified * 20) +
        (phone_verified * 20) +
        (govt_id_verified * 30) +
        (institution_verified * 20) +
        (biometric_verified * 10)  # Optional
    )
    
    # Component 2: Behavioral Trust Score (0-100)
    behavioral_score = ml_model_behavioral_trust.predict({
        'account_age_days': user_data.account_age_days,
        'login_frequency': behavioral_data.login_frequency,
        'activity_consistency': behavioral_data.activity_consistency,
        'device_consistency': behavioral_data.device_consistency,
        'geographic_consistency': behavioral_data.geographic_consistency
    })
    
    # Component 3: Reputation Score (0-100)
    reputation_score = (
        min(reputation_data.endorsements_received * 2, 30) +
        min(reputation_data.projects_completed * 5, 25) +
        min(reputation_data.skills_verified * 3, 20) +
        min(reputation_data.certifications * 5, 15) +
        min(reputation_data.positive_feedback_ratio * 10, 10)
    )
    
    # Component 4: Cross-Platform Trust Score (0-100)
    cross_platform_score = (
        (linkedin_verified * 30) +
        (github_verified * 25) +
        (portfolio_verified * 20) +
        (external_certifications * 15) +
        (cross_platform_consistency_score * 10)  # AI-calculated
    )
    
    # Component 5: Intelligence Consistency Score (0-100)
    intelligence_consistency = ai_model_intelligence_validator.predict({
        'passive_intelligence_profile': user_data.intelligence_profile,
        'demonstrated_artifacts': user_data.portfolio,
        'skill_test_results': user_data.skill_assessments
    })
    
    # Weighted Composite Score
    overall_trust_score = (
        identity_score * 0.30 +
        behavioral_score * 0.25 +
        reputation_score * 0.20 +
        cross_platform_score * 0.15 +
        intelligence_consistency * 0.10
    )
    
    # Apply penalties for risk flags
    if fraud_score > 0.75:
        overall_trust_score *= 0.5  # 50% penalty for high fraud risk
    
    if anomaly_detected:
        overall_trust_score *= 0.8  # 20% penalty for behavioral anomalies
    
    # Clamp to 0-100 range
    overall_trust_score = max(0, min(100, overall_trust_score))
    
    return {
        'overall_trust_score': round(overall_trust_score),
        'identity_verification_score': round(identity_score),
        'behavioral_trust_score': round(behavioral_score),
        'reputation_score': round(reputation_score),
        'cross_platform_trust_score': round(cross_platform_score),
        'intelligence_consistency_score': round(intelligence_consistency)
    }
```

---

## 🎯 SUCCESS CRITERIA

### Agent is considered operational when:
```yaml
✅ All input validation rules enforced
✅ All security checks passing (penetration test)
✅ ML models trained and deployed to production
✅ Latency targets met (P99 <500ms)
✅ Error rate <0.5%
✅ Audit logging functional and immutable
✅ Inter-agent event contracts validated
✅ GDPR compliance verified (legal review)
✅ Minor protection workflow tested
✅ Cross-platform verification (LinkedIn, GitHub) functional
✅ Passive intelligence consistency validation operational
✅ Fraud detection model accuracy >95%
✅ Manual review queue SLA <48 hours
✅ Rollback procedures tested in staging
✅ Disaster recovery plan documented and tested
✅ On-call runbook created for production incidents
```

---

## 🚨 CRITICAL ALERTS CONFIGURATION

```yaml
PAGERDUTY_ALERTS:
  - tenant_isolation_breach_attempt → P1 (immediate escalation)
  - data_corruption_detected → P1
  - model_accuracy_drop_critical → P2
  - fraud_attack_detected → P2
  - verification_api_down → P3

SLACK_ALERTS:
  - trust_score_model_drift → #ml-team
  - manual_review_queue_backlog → #trust-safety-team
  - latency_degradation → #devops
  - verification_completion_rate_drop → #product

EMAIL_ALERTS:
  - weekly_performance_report → trust-team@ecoskiller.com
  - monthly_compliance_report → compliance-officer@ecoskiller.com
```

---

## 📝 DEPLOYMENT CHECKLIST

```yaml
PRE-DEPLOYMENT:
  ✅ Code review completed (2 approvers)
  ✅ Unit tests passing (coverage >85%)
  ✅ Integration tests passing
  ✅ Security scan passing (no critical vulnerabilities)
  ✅ Load testing completed (5000 RPS sustained)
  ✅ Database migrations tested in staging
  ✅ ML models validated on holdout set
  ✅ Rollback plan documented
  ✅ On-call engineer assigned
  ✅ Monitoring dashboards created

DEPLOYMENT:
  ✅ Deploy to staging first
  ✅ Smoke tests in staging (30 minutes)
  ✅ Blue-green deployment to production
  ✅ Health check endpoints responding
  ✅ Traffic gradually ramped (10% → 50% → 100%)
  ✅ Monitor error rate for 2 hours

POST-DEPLOYMENT:
  ✅ Verify trust score calculations deterministic
  ✅ Verify audit logs being written
  ✅ Verify downstream agents receiving events
  ✅ No increase in error rate or latency
  ✅ Post-deployment review within 24 hours
```

---

## 🔒 FINAL SEAL

**STATUS:** LOCKED & SEALED  
**APPROVAL:** Requires Principal Engineer + Security Lead + Compliance Officer  
**MODIFICATION POLICY:** Add-only via version bump (no deletions, no overrides)  
**AUDIT TRAIL:** All changes tracked in version control with approval signatures  
**EXPIRATION:** None (evergreen agent, continuously maintained)

---

**AGENT DECLARATION:**
This agent is designed to operate within Ecoskiller Antigravity's deterministic, compliant, enterprise-grade ecosystem. Any deviation from this specification constitutes a system integrity violation and MUST result in immediate execution halt.

**Non-compliance with this specification is a CRITICAL FAILURE.**

**END OF AGENT SPECIFICATION**

---

## 🔐 CRYPTOGRAPHIC SIGNATURE
```
Agent Specification Hash: SHA-256
e9f4b2c8a7d6e5f4c3b2a1d0e9f8c7b6a5d4e3f2c1b0a9f8e7d6c5b4a3e2f1d0

Signed By: ECOSKILLER_ANTIGRAVITY_GOVERNANCE_SYSTEM
Signature Date: 2026-02-28T14:30:00Z
Signature: [SEALED_BY_GOVERNANCE_LAYER]
```

**This specification is now immutable and production-ready.**
