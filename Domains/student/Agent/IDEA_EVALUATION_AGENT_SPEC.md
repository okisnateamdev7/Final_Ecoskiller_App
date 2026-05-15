# 🔒 IDEA_EVALUATION_AGENT — SEALED SPECIFICATION v1.0

**Agent Class:** ML/Intelligence/Safety Owner  
**Platform:** Ecoskiller Antigravity  
**Mutation Policy:** Add-only via version bump  
**Execution Mode:** Deterministic + Validated  
**Security Model:** Zero-trust multi-tenant isolation  
**Audit Policy:** Append-only immutable trail  
**Stack Compatibility:** 70% ML + 30% AI Semantic  

---

## 1️⃣ AGENT IDENTITY (MANDATORY)

```
AGENT_NAME = IDEA_EVALUATION_AGENT
SYSTEM_ROLE = Innovation Economy Intelligence & Safety Owner
PRIMARY_DOMAIN = Innovation_Economy
EXECUTION_MODE = Deterministic + Validated
DATA_SCOPE = Ideas, Innovations, Submissions, User Content
TENANT_SCOPE = Strict Multi-Tenant Isolation (NO cross-tenant access)
FAILURE_POLICY = Halt on ambiguity + Log + Escalate
COMPLIANCE_SCOPE = Copyright Detection, IP Violation Prevention, Content Safety
VERSION = 1.0.0
```

**Critical Context:**  
This agent operates within the Ecoskiller Innovation Economy where:
- Users submit ideas, innovations, projects, solutions
- Original ideas earn royalties when used/adopted
- Platform takes 5-10% royalty share
- Ideas must be evaluated for: originality, quality, safety, IP compliance
- Copy detection prevents plagiarism and fraud
- Idea DNA enables similarity tracking across 10M-100M users

---

## 2️⃣ PURPOSE DECLARATION

### What Problem This Agent Solves

**Primary Problems:**
1. **Originality Verification** - Determine if submitted idea is original or copied
2. **Quality Assessment** - Evaluate idea viability, clarity, completeness
3. **IP Compliance** - Detect copyright, trademark, patent violations
4. **Safety Screening** - Block harmful, illegal, inappropriate content
5. **Fraud Prevention** - Identify gaming, spam, plagiarism attempts
6. **Royalty Enablement** - Generate data needed for fair royalty distribution
7. **Ecosystem Trust** - Maintain innovation economy integrity at scale

### What Input It Consumes

**Primary Input:**
- User-submitted idea/innovation/project content
- Text descriptions, images, documents, links
- User profile & history data
- Submission metadata (timestamp, domain, category)

### What Output It Produces

**Primary Output:**
```json
{
  "evaluation_id": "UUID",
  "idea_submission_id": "UUID",
  "user_id": "UUID",
  "tenant_id": "UUID",
  "evaluation_timestamp": "ISO8601",
  
  "scores": {
    "originality_score": 0.0-1.0,
    "quality_score": 0.0-1.0,
    "feasibility_score": 0.0-1.0,
    "clarity_score": 0.0-1.0,
    "safety_score": 0.0-1.0,
    "overall_score": 0.0-1.0
  },
  
  "flags": {
    "is_original": boolean,
    "is_safe": boolean,
    "is_compliant": boolean,
    "requires_human_review": boolean,
    "is_spam": boolean,
    "is_plagiarized": boolean
  },
  
  "idea_dna": {
    "semantic_fingerprint": "hash_string",
    "concept_vector": [float array],
    "domain_tags": ["tag1", "tag2"],
    "similarity_hash": "hash_string"
  },
  
  "violations": [
    {
      "type": "copyright|trademark|safety|spam",
      "severity": "low|medium|high|critical",
      "description": "string",
      "detected_source": "optional_url_or_reference",
      "confidence": 0.0-1.0
    }
  ],
  
  "recommendations": {
    "action": "approve|reject|review|revise",
    "revision_suggestions": ["string"],
    "required_changes": ["string"]
  },
  
  "similar_ideas": [
    {
      "idea_id": "UUID",
      "similarity_score": 0.0-1.0,
      "submission_date": "ISO8601",
      "author_user_id": "UUID",
      "is_same_author": boolean
    }
  ],
  
  "ml_metadata": {
    "model_version": "string",
    "confidence_score": 0.0-1.0,
    "processing_time_ms": integer,
    "feature_importance": {"feature": score}
  },
  
  "audit_metadata": {
    "audit_id": "UUID",
    "evaluator_agent_version": "1.0.0",
    "data_lineage": ["source_ids"],
    "anomaly_flags": []
  }
}
```

### Downstream Agents That Depend On It

```
1. IDEA_DNA_AGENT - Consumes idea_dna for similarity indexing
2. ROYALTY_ENGINE - Uses originality_score for payment calculations
3. COPY_DETECTION_ENGINE - Uses semantic_fingerprint for duplicate detection
4. MODERATION_AGENT - Uses safety_score and violation flags
5. RECOMMENDATION_ENGINE - Uses quality_score for idea surfacing
6. INNOVATION_FEED_AGENT - Uses overall_score for feed ranking
7. USER_REPUTATION_AGENT - Uses evaluation results for reputation scoring
8. COMPLIANCE_AUDIT_AGENT - Uses violations log for regulatory compliance
```

### Upstream Agents That Feed It

```
1. CONTENT_EXTRACTION_AGENT - Provides cleaned, normalized idea content
2. USER_CONTEXT_AGENT - Provides user history, reputation, behavioral patterns
3. PASSIVE_INTELLIGENCE_AGENT - Provides user intelligence profile
4. DOMAIN_CLASSIFICATION_AGENT - Provides domain/category classification
5. FEATURE_STORE_AGENT - Provides pre-computed feature vectors
```

---

## 3️⃣ INPUT CONTRACT (STRICT)

```json
INPUT_SCHEMA: {
  "required_fields": [
    "idea_submission_id",
    "user_id", 
    "tenant_id",
    "domain",
    "idea_content_text",
    "submission_timestamp",
    "content_type"
  ],
  
  "optional_fields": [
    "idea_content_images",
    "idea_content_documents", 
    "idea_content_links",
    "category",
    "tags",
    "target_problem",
    "proposed_solution",
    "user_history_features"
  ],
  
  "validation_rules": [
    "idea_submission_id MUST be valid UUID",
    "user_id MUST exist in User table",
    "tenant_id MUST exist in Tenant table",
    "domain MUST be in ['Arts', 'Commerce', 'Science', 'Technology', 'Administration']",
    "idea_content_text MUST be 10-10000 characters",
    "idea_content_text MUST NOT be null or empty",
    "submission_timestamp MUST be within last 24 hours",
    "content_type MUST be in ['idea', 'innovation', 'project', 'solution']",
    "All image URLs MUST be from allowed storage domains",
    "All document URLs MUST be from allowed storage domains"
  ],
  
  "security_checks": [
    "VERIFY tenant_id matches user_id tenant ownership",
    "VERIFY user has permission 'idea:submit' in tenant",
    "VERIFY domain isolation (no cross-domain data leakage)",
    "VERIFY content does NOT contain PII of other users",
    "VERIFY submission rate limit not exceeded (10 per day)",
    "VERIFY user account is verified and not suspended"
  ],
  
  "domain_checks": [
    "VERIFY idea_submission_id not already evaluated",
    "VERIFY user_id not flagged as serial plagiarizer",
    "VERIFY tenant_id has active innovation economy enabled",
    "VERIFY domain matches tenant's allowed domains"
  ]
}
```

**Rejection Policy:**
- Invalid UUID format → REJECT with error code INPUT_INVALID_UUID
- Missing required field → REJECT with error code INPUT_MISSING_FIELD  
- Cross-tenant access attempt → REJECT with error code SECURITY_TENANT_VIOLATION
- Content too short (<10 chars) → REJECT with error code INPUT_CONTENT_TOO_SHORT
- Content too long (>10000 chars) → REJECT with error code INPUT_CONTENT_TOO_LONG
- Rate limit exceeded → REJECT with error code RATE_LIMIT_EXCEEDED
- Malformed data → REJECT with error code INPUT_MALFORMED

**All rejections MUST be logged to audit trail.**

---

## 4️⃣ OUTPUT CONTRACT (STRICT)

```json
OUTPUT_SCHEMA: {
  "result_object": {
    "type": "IdeaEvaluationResult",
    "required_fields": [
      "evaluation_id",
      "idea_submission_id",
      "scores",
      "flags",
      "idea_dna",
      "recommendations",
      "ml_metadata",
      "audit_metadata"
    ],
    "optional_fields": [
      "violations",
      "similar_ideas",
      "revision_suggestions"
    ]
  },
  
  "confidence_score": {
    "range": "0.0-1.0",
    "minimum_threshold": 0.70,
    "policy": "confidence < 0.70 → flag for human review"
  },
  
  "model_version": {
    "format": "semantic_version",
    "example": "1.0.0",
    "required": true
  },
  
  "audit_reference": {
    "type": "UUID",
    "immutable": true,
    "required": true
  },
  
  "next_trigger_events": [
    "IDEA_DNA_INDEX_UPDATE",
    "ROYALTY_ELIGIBILITY_CHECK", 
    "COPY_DETECTION_INDEX_UPDATE",
    "MODERATION_REVIEW_REQUIRED (if flagged)",
    "USER_REPUTATION_UPDATE",
    "INNOVATION_FEED_UPDATE (if approved)"
  ]
}
```

**Output Guarantees:**
- Every output MUST include all required fields
- evaluation_id MUST be globally unique
- confidence_score MUST reflect actual model confidence
- model_version MUST match deployed model version
- audit_reference MUST be immutable and logged
- All scores MUST be in range [0.0, 1.0]
- All timestamps MUST be UTC ISO8601
- All boolean flags MUST have explicit values (no null)

---

## 5️⃣ ML / AI LOGIC LAYER

### ML-Based Components (70-80%)

#### Component 1: Originality Detection Model

**MODEL_TYPE:** Binary Classification + Similarity Scoring

**ARCHITECTURE:**
- Base: Sentence-BERT embeddings (384-768 dim)
- Layer 1: Semantic similarity calculation (cosine)
- Layer 2: Historical idea comparison (FAISS index search)
- Layer 3: Binary classifier (Original vs Copied)
- Output: Originality score [0.0-1.0]

**FEATURES_USED:**
```
Primary Features:
- Semantic embedding vector (768 dim)
- Historical submission patterns (user-specific)
- Content length normalized
- Domain-specific vocabulary density
- Linguistic complexity score
- Submission timestamp patterns

Contextual Features:  
- User reputation score
- User submission frequency
- Similar ideas count in last 30 days
- User's past originality scores (avg)
- User's intelligence profile scores
- Domain expertise indicators
```

**TRAINING_FREQUENCY:** Weekly

**DRIFT_DETECTION:**
- Monitor similarity score distribution weekly
- Alert if mean originality score drops > 15%
- Alert if plagiarism detection rate increases > 20%
- Retrain if drift detected for 2 consecutive weeks

**THRESHOLD_POLICY:**
- originality_score >= 0.80 → ORIGINAL
- originality_score 0.60-0.79 → UNCERTAIN (human review)
- originality_score < 0.60 → LIKELY_COPIED (flag for review)

**VERSION_CONTROL:**
- Model stored in versioned model registry
- Each evaluation references exact model_version
- Rollback capability to previous 3 versions
- A/B testing capability for new versions

---

#### Component 2: Quality Assessment Model

**MODEL_TYPE:** Multi-class Regression

**ARCHITECTURE:**
- Feature extraction: TF-IDF + Domain ontology matching
- Quality dimensions: Clarity, Feasibility, Innovation, Completeness
- Ensemble model: Random Forest + Gradient Boosting
- Output: Quality score [0.0-1.0]

**FEATURES_USED:**
```
Content Features:
- Text clarity metrics (Flesch reading ease)
- Technical vocabulary appropriateness
- Completeness indicators (problem + solution + approach)
- Specificity vs vagueness ratio
- Evidence/reasoning quality

User Features:
- User domain expertise level
- User historical quality scores
- User intelligence profile alignment
- Educational background indicators
```

**TRAINING_FREQUENCY:** Monthly

**DRIFT_DETECTION:**
- Monitor quality score distribution
- Track correlation with human review scores
- Retrain if correlation drops below 0.75

---

#### Component 3: Safety Classification Model

**MODEL_TYPE:** Multi-label Classification

**ARCHITECTURE:**
- Text classifier for harmful content categories
- Categories: Violence, Hate, Sexual, Dangerous, Illegal, Spam
- Model: Fine-tuned BERT for content moderation
- Output: Safety score [0.0-1.0] + violation flags

**FEATURES_USED:**
```
Content Features:
- Text embeddings
- Keyword pattern matching
- Contextual language analysis
- Link/URL analysis (if present)

Behavioral Features:
- User flagging history
- Submission pattern anomalies
- Cross-reference with known violation patterns
```

**TRAINING_FREQUENCY:** Weekly

**THRESHOLD_POLICY:**
- safety_score >= 0.95 → SAFE
- safety_score 0.85-0.94 → REVIEW
- safety_score < 0.85 → UNSAFE (auto-reject)

---

### AI-Based Components (20-30%)

#### Component 4: Semantic Understanding (LLM)

**AI_USAGE_SCOPE:** Strictly limited to content comprehension only

**FUNCTION:**
- Extract key concepts from unstructured text
- Generate semantic fingerprint
- Identify domain-specific terminology
- Parse problem-solution structure

**PROMPT_GOVERNANCE:**
```
VERSION: 1.0.0
STRUCTURE: Deterministic template-based
CREATIVITY: DISABLED
RANDOMNESS: Temperature = 0.0

Template:
---
Extract the following from this idea submission:
1. Core Problem (1 sentence)
2. Proposed Solution (1 sentence)  
3. Key Concepts (list, max 10)
4. Domain Classification (single choice)
5. Innovation Type (incremental|disruptive|radical)

Content: {idea_content_text}

Output JSON only. No explanation.
---
```

**CONSTRAINTS:**
- NO decision autonomy beyond extraction
- NO subjective quality judgment  
- NO originality determination (handled by ML)
- Output MUST be structured JSON
- Timeout: 5 seconds max
- Fallback: If AI fails, use rule-based extraction

**MODEL:**
- Primary: Claude Sonnet 4.5 (API)
- Fallback: Local extraction rules
- Token limit: 1000 tokens max per idea

---

#### Component 5: Violation Detection (LLM-Assisted)

**AI_USAGE_SCOPE:** Identify potential IP/copyright violations

**FUNCTION:**
- Detect references to copyrighted works
- Identify trademark usage
- Flag potential patent similarity (basic)
- Detect plagiarized academic/professional content

**PROMPT_GOVERNANCE:**
```
VERSION: 1.0.0

Template:
---
Analyze if this content contains:
1. Direct quotes from copyrighted sources
2. Paraphrased copyrighted content
3. Trademark names without disclaimer
4. References to patented technologies
5. Plagiarized academic/technical content

Content: {idea_content_text}

Return JSON:
{
  "has_copyright_risk": boolean,
  "has_trademark_risk": boolean,
  "has_patent_risk": boolean,
  "detected_sources": [list],
  "confidence": 0.0-1.0
}
---
```

**HUMAN OVERRIDE REQUIRED:**
- All AI-detected violations MUST be reviewed by compliance team
- AI serves as first-pass filter only
- Final decision: Human moderator

---

## 6️⃣ SCALABILITY DESIGN

**Target Performance:**
```
EXPECTED_RPS = 500-1000 evaluations/second (peak)
LATENCY_TARGET = <2 seconds (p95)
MAX_CONCURRENCY = 10,000 concurrent evaluations
QUEUE_STRATEGY = Priority queue with tenant fairness
```

**Horizontal Scaling:**
- Stateless agent design (no session state)
- Worker pool architecture (Kubernetes pods)
- Auto-scaling: CPU > 70% → scale up
- Auto-scaling: CPU < 30% for 5min → scale down

**Event-Driven Triggers:**
```
Trigger: IDEA_SUBMISSION_CREATED
Source: Idea_Submission_Service
Queue: idea_evaluation_queue
Priority: STANDARD (user-facing)
```

**Async Processing:**
- User gets immediate "Evaluation in Progress" response
- Agent processes asynchronously
- User notified via EVALUATION_COMPLETED event
- Result cached for 7 days

**Idempotent Operations:**
- Same idea_submission_id evaluated twice → return cached result
- De-duplication based on (idea_submission_id + user_id)
- Idempotency key: SHA256(idea_submission_id)

**Database Optimization:**
- Read replicas for historical idea lookups
- FAISS vector index for similarity search (in-memory)
- Redis cache for recent evaluations (24hr TTL)
- Batch inserts for audit logs (every 1000 records)

---

## 7️⃣ SECURITY ENFORCEMENT

**Tenant Isolation Validation:**
```python
def validate_tenant_isolation(user_id, tenant_id, idea_submission_id):
    # Verify user belongs to tenant
    assert user.tenant_id == tenant_id
    
    # Verify idea submission belongs to tenant  
    assert idea_submission.tenant_id == tenant_id
    
    # Verify no cross-tenant data in input
    assert all_referenced_data.tenant_id == tenant_id
    
    # Violation → STOP + LOG + ALERT_SECURITY_TEAM
```

**Domain Isolation Validation:**
```python
def validate_domain_isolation(user_id, domain, idea_content):
    # Verify user has access to domain
    assert user.has_domain_access(domain)
    
    # Verify no cross-domain references in content
    assert no_cross_domain_leakage(idea_content)
    
    # Violation → STOP + LOG
```

**Role-Based Authorization:**
```
REQUIRED_PERMISSIONS:
- idea:submit (to trigger evaluation)
- innovation_economy:participate (to be eligible for royalties)

FORBIDDEN_PERMISSIONS (triggers security alert):
- idea:evaluate_override (only compliance team)
- idea:bulk_evaluate (only system admin)
```

**Encryption Enforcement:**
- All idea content encrypted at rest (AES-256)
- All API communication over TLS 1.3
- Sensitive fields (user_id, tenant_id) encrypted in logs

**Audit Logging (Append-Only):**
```json
{
  "log_id": "UUID",
  "timestamp_utc": "ISO8601",
  "actor_type": "AGENT",
  "actor_id": "IDEA_EVALUATION_AGENT",
  "action": "EVALUATE_IDEA",
  "idea_submission_id": "UUID",
  "user_id": "ENCRYPTED",
  "tenant_id": "ENCRYPTED",
  "input_hash": "SHA256(input)",
  "output_hash": "SHA256(output)",
  "model_version": "1.0.0",
  "decision_path": ["originality_check", "quality_check", "safety_check"],
  "confidence_score": 0.85,
  "anomaly_flags": [],
  "processing_time_ms": 1234
}
```

**Access Log Tracking:**
- Every data access logged with timestamp
- Unusual access patterns trigger alerts
- Cross-tenant access attempts → immediate alert + block

---

## 8️⃣ AUDIT & TRACEABILITY

**Immutable Audit Log Schema:**
```json
{
  "audit_id": "UUID (primary key)",
  "created_at": "TIMESTAMP (immutable)",
  "agent_name": "IDEA_EVALUATION_AGENT",
  "agent_version": "1.0.0",
  "event_type": "IDEA_EVALUATED",
  
  "input": {
    "idea_submission_id": "UUID",
    "user_id": "UUID",
    "tenant_id": "UUID",
    "content_hash": "SHA256",
    "input_size_bytes": integer
  },
  
  "output": {
    "evaluation_id": "UUID",
    "originality_score": float,
    "quality_score": float,
    "safety_score": float,
    "overall_score": float,
    "action": "approve|reject|review",
    "output_hash": "SHA256"
  },
  
  "ml_metadata": {
    "model_version": "1.0.0",
    "model_confidence": float,
    "feature_vector_hash": "SHA256",
    "similar_ideas_count": integer,
    "processing_time_ms": integer
  },
  
  "decision_path": [
    {"step": "input_validation", "result": "pass", "duration_ms": 50},
    {"step": "originality_check", "result": "pass", "score": 0.85, "duration_ms": 500},
    {"step": "quality_check", "result": "pass", "score": 0.78, "duration_ms": 300},
    {"step": "safety_check", "result": "pass", "score": 0.98, "duration_ms": 200}
  ],
  
  "anomaly_flags": [
    {
      "type": "UNUSUAL_SIMILARITY_PATTERN",
      "severity": "medium",
      "description": "5 similar ideas from same user in 24hrs",
      "confidence": 0.75
    }
  ],
  
  "data_lineage": [
    "USER_CONTEXT_AGENT:v1.2.0:user_123_features",
    "FEATURE_STORE_AGENT:v2.1.0:domain_embeddings",
    "HISTORICAL_IDEAS_INDEX:v3.0.0:faiss_index"
  ]
}
```

**Logs MUST be:**
- Append-only (no updates or deletes)
- Stored in separate audit database
- Replicated to 3 separate storage regions
- Backed up daily
- Retained for 7 years (regulatory compliance)
- Queryable for compliance audits

---

## 9️⃣ FAILURE POLICY

**Invalid Input Handling:**
```
DETECTION: Missing required field
ACTION: STOP_EXECUTION
RESPONSE: {
  "status": "error",
  "error_code": "INPUT_MISSING_FIELD",
  "message": "Required field 'idea_content_text' is missing",
  "timestamp": "ISO8601",
  "request_id": "UUID"
}
LOG: LOG_INCIDENT with severity=ERROR
ESCALATE: NO (user error)
RETRY: NO
```

**Model Unavailable:**
```
DETECTION: ML model service timeout or error
ACTION: STOP_EXECUTION (do not fall back to inferior logic)
RESPONSE: {
  "status": "error",
  "error_code": "MODEL_UNAVAILABLE",
  "message": "Evaluation service temporarily unavailable",
  "retry_after_seconds": 60
}
LOG: LOG_INCIDENT with severity=CRITICAL
ESCALATE: DevOps team (PagerDuty)
RETRY: User should retry after 60 seconds
```

**AI Timeout:**
```
DETECTION: LLM API call exceeds 5 seconds
ACTION: FALLBACK to rule-based extraction
RESPONSE: Continue with reduced AI features
LOG: LOG_INCIDENT with severity=WARNING
ESCALATE: If frequency > 10% → escalate to AI team
RETRY: NO (fallback successful)
```

**Data Corruption Detected:**
```
DETECTION: Output hash mismatch or invalid data structure
ACTION: STOP_EXECUTION + DISCARD_OUTPUT
RESPONSE: {
  "status": "error",
  "error_code": "DATA_CORRUPTION_DETECTED",
  "message": "Evaluation failed due to data integrity issue"
}
LOG: LOG_INCIDENT with severity=CRITICAL
ESCALATE: Security team + Data team (immediate)
RETRY: YES (after investigation)
```

**Confidence Below Threshold:**
```
DETECTION: confidence_score < 0.70
ACTION: FLAG_FOR_HUMAN_REVIEW
RESPONSE: {
  "status": "success",
  "evaluation_id": "UUID",
  "requires_human_review": true,
  "estimated_review_time_hours": 24
}
LOG: LOG_INCIDENT with severity=INFO
ESCALATE: Queue for human moderator
RETRY: NO (evaluation completed, awaiting review)
```

**Security Violation:**
```
DETECTION: Cross-tenant access attempt
ACTION: STOP_EXECUTION + BLOCK_USER + ALERT
RESPONSE: {
  "status": "error",
  "error_code": "SECURITY_VIOLATION",
  "message": "Access denied"
}
LOG: LOG_SECURITY_INCIDENT with severity=CRITICAL
ESCALATE: Security team (immediate) + Compliance officer
RETRY: NO (permanent block)
```

**No Silent Failures Policy:**
- Every failure MUST be logged
- Every error MUST have a response code
- User MUST receive clear error message or retry guidance
- Critical failures MUST trigger alerts
- Partial results MUST NOT be returned

---

## 🔟 INTER-AGENT DEPENDENCY MAP

### Upstream Agents (Input Providers)

```
AGENT: CONTENT_EXTRACTION_AGENT
PROVIDES: Clean, normalized idea text (HTML stripped, encoding fixed)
FALLBACK: Raw input if extraction fails
DEPENDENCY_TYPE: SOFT (agent can proceed with raw input)
```

```
AGENT: USER_CONTEXT_AGENT  
PROVIDES: User reputation, submission history, behavioral features
FALLBACK: Use partial features (historical data only)
DEPENDENCY_TYPE: SOFT (agent can proceed with reduced accuracy)
```

```
AGENT: PASSIVE_INTELLIGENCE_AGENT
PROVIDES: User intelligence profile (8 intelligence types)
FALLBACK: Skip intelligence-based features
DEPENDENCY_TYPE: SOFT (nice to have, not critical)
```

```
AGENT: DOMAIN_CLASSIFICATION_AGENT
PROVIDES: Verified domain classification
FALLBACK: Use user-declared domain
DEPENDENCY_TYPE: SOFT (can infer from content)
```

```
AGENT: FEATURE_STORE_AGENT
PROVIDES: Pre-computed embedding vectors, domain ontologies
FALLBACK: Compute features on-demand (slower)
DEPENDENCY_TYPE: MEDIUM (impacts performance, not accuracy)
```

### Downstream Agents (Output Consumers)

```
AGENT: IDEA_DNA_AGENT
CONSUMES: idea_dna.semantic_fingerprint, idea_dna.concept_vector
EVENT: IDEA_DNA_INDEX_UPDATE
TRIGGER: On every successful evaluation
CRITICAL: YES (innovation economy depends on this)
```

```
AGENT: ROYALTY_ENGINE
CONSUMES: scores.originality_score, flags.is_original, evaluation_id
EVENT: ROYALTY_ELIGIBILITY_CHECK
TRIGGER: When originality_score >= 0.80 AND flags.is_original == true
CRITICAL: YES (payment accuracy depends on this)
```

```
AGENT: COPY_DETECTION_ENGINE
CONSUMES: idea_dna.similarity_hash, similar_ideas[]
EVENT: COPY_DETECTION_INDEX_UPDATE
TRIGGER: On every evaluation
CRITICAL: YES (plagiarism prevention)
```

```
AGENT: MODERATION_AGENT
CONSUMES: flags.requires_human_review, violations[], scores.safety_score
EVENT: MODERATION_REVIEW_REQUIRED
TRIGGER: When requires_human_review == true OR safety_score < 0.85
CRITICAL: YES (safety compliance)
```

```
AGENT: RECOMMENDATION_ENGINE
CONSUMES: scores.quality_score, scores.overall_score, idea_dna
EVENT: INNOVATION_FEED_UPDATE
TRIGGER: When overall_score >= 0.70 AND flags.is_safe == true
CRITICAL: MEDIUM (affects discovery, not safety)
```

```
AGENT: USER_REPUTATION_AGENT
CONSUMES: scores.originality_score, scores.quality_score, flags
EVENT: USER_REPUTATION_UPDATE
TRIGGER: On every evaluation
CRITICAL: MEDIUM (impacts user standing)
```

```
AGENT: COMPLIANCE_AUDIT_AGENT
CONSUMES: Full audit_metadata, violations[], ml_metadata
EVENT: COMPLIANCE_AUDIT_LOG_UPDATE
TRIGGER: On every evaluation
CRITICAL: HIGH (regulatory requirement)
```

### Event Emission Structure

```json
EVENT_SCHEMA: {
  "event_type": "IDEA_EVALUATED",
  "event_id": "UUID",
  "timestamp_utc": "ISO8601",
  "source_agent": "IDEA_EVALUATION_AGENT",
  "source_version": "1.0.0",
  "payload": {
    "evaluation_id": "UUID",
    "idea_submission_id": "UUID",
    "user_id": "UUID",
    "tenant_id": "UUID",
    "originality_score": float,
    "quality_score": float,
    "safety_score": float,
    "overall_score": float,
    "action": "approve|reject|review",
    "idea_dna": {},
    "similar_ideas": [],
    "violations": []
  },
  "downstream_triggers": [
    "IDEA_DNA_INDEX_UPDATE",
    "ROYALTY_ELIGIBILITY_CHECK",
    "COPY_DETECTION_INDEX_UPDATE"
  ]
}
```

---

## 1️⃣1️⃣ PASSIVE INTELLIGENCE COMPATIBILITY

**Feature Emission to Feature Store:**

This agent emits structured behavioral features that contribute to passive intelligence measurement.

```python
EMIT_FEATURE_VECTOR:
{
  "feature_store_event": "USER_BEHAVIOR_FEATURE_CREATED",
  "source_agent": "IDEA_EVALUATION_AGENT",
  "user_id": "UUID",
  "timestamp_utc": "ISO8601",
  
  "features": {
    "innovation_submission_count_30d": integer,
    "avg_originality_score_lifetime": float,
    "avg_quality_score_lifetime": float,
    "plagiarism_flag_count_lifetime": integer,
    "domain_diversity_score": float, # number of unique domains
    "submission_consistency_score": float, # regularity of submissions
    "idea_complexity_avg": float, # based on text analysis
    "idea_completeness_avg": float, # problem+solution present?
    "domain_expertise_indicators": {
      "Technology": 0.85,
      "Science": 0.45,
      "Commerce": 0.20
    }
  },
  
  "intelligence_signals": {
    "linguistic_intelligence": 0.75, # from text quality
    "logical_intelligence": 0.68, # from problem-solution structure
    "spatial_intelligence": 0.30, # from visual content usage
    "interpersonal_intelligence": 0.60 # from collaborative idea patterns
  }
}
```

**Feature Store Agent Integration:**
- Features emitted as structured events
- Feature Store Agent aggregates across all agents
- Features used for user intelligence profiling
- Features feed into recommendation and personalization

---

## 1️⃣2️⃣ INNOVATION ECONOMY COMPATIBILITY

**Idea DNA Generation:**

Every idea receives a unique "DNA fingerprint" for:
- Similarity tracking across millions of ideas
- Royalty attribution when ideas are adopted
- Plagiarism detection and prevention
- Innovation genealogy tracking

```python
IDEA_DNA_STRUCTURE:
{
  "dna_id": "UUID",
  "idea_submission_id": "UUID",
  "generated_by": "IDEA_EVALUATION_AGENT:1.0.0",
  "generated_at": "ISO8601",
  
  "semantic_fingerprint": "SHA256 hash of normalized concept vector",
  
  "concept_vector": [768-dimensional embedding],
  # Sentence-BERT embedding of core concepts
  
  "domain_tags": ["AI", "Education", "Automation"],
  # Top 10 domain concepts extracted
  
  "similarity_hash": "Locality-sensitive hash for fast similarity search",
  
  "innovation_type": "incremental|disruptive|radical",
  
  "problem_signature": "hash of problem statement",
  "solution_signature": "hash of solution approach",
  
  "genealogy": {
    "inspired_by_ideas": ["idea_id1", "idea_id2"], # if similarity > 0.70
    "innovation_depth": integer # how many levels deep in idea evolution
  }
}
```

**IDEA_DNA_AGENT Integration:**
- DNA emitted immediately after evaluation
- IDEA_DNA_AGENT indexes in FAISS for similarity search
- Real-time queries: "Find ideas similar to this one"
- Genealogy tracking: "Which ideas inspired this innovation?"

**ROYALTY_ENGINE Integration:**
```python
ROYALTY_ELIGIBILITY_CRITERIA:
- originality_score >= 0.80
- quality_score >= 0.70  
- is_original == true
- is_safe == true
- is_compliant == true

IF ELIGIBLE:
  EMIT_EVENT: ROYALTY_ELIGIBILITY_CHECK
  PAYLOAD: {
    "idea_submission_id": UUID,
    "user_id": UUID,
    "originality_score": float,
    "idea_dna": {},
    "estimated_royalty_tier": "bronze|silver|gold|platinum"
  }
```

**COPY_DETECTION_ENGINE Integration:**
```python
COPY_DETECTION_WORKFLOW:
1. IDEA_EVALUATION_AGENT evaluates new idea
2. Generates similarity_hash
3. Queries COPY_DETECTION_ENGINE for similar hashes
4. If similarity > 0.90 AND different authors:
   - Flag as potential plagiarism
   - Trigger human review
   - Notify original author
5. COPY_DETECTION_ENGINE updates index with new hash
```

---

## 1️⃣3️⃣ GROWTH ENGINE HOOK

**Rank Update Events:**

```python
TRIGGER_RANK_UPDATE:
{
  "event": "USER_RANK_UPDATE_REQUIRED",
  "user_id": "UUID",
  "reason": "high_quality_idea_submission",
  "rank_impact": {
    "innovation_rank": +5 points,
    "domain_expertise_rank": +3 points,
    "overall_reputation_rank": +2 points
  },
  "triggered_by": {
    "evaluation_id": "UUID",
    "originality_score": 0.92,
    "quality_score": 0.88
  }
}
```

**XP Update Events:**

```python
TRIGGER_XP_UPDATE:
{
  "event": "USER_XP_EARNED",
  "user_id": "UUID",
  "xp_amount": 50,
  "xp_category": "innovation",
  "reason": "original_idea_approved",
  "milestone_achieved": false,
  "next_milestone_progress": "75%"
}
```

**Achievement/Badge Triggers:**

```python
TRIGGER_ACHIEVEMENT:
{
  "event": "USER_ACHIEVEMENT_UNLOCKED",
  "user_id": "UUID",
  "achievement_type": "INNOVATOR_BADGE_SILVER",
  "criteria_met": "10 original ideas with score >= 0.80",
  "share_worthy": true, # triggers social share prompt
  "reward": {
    "badge_icon": "url",
    "xp_bonus": 100,
    "platform_visibility_boost": true
  }
}
```

**Share Trigger Events:**

```python
TRIGGER_SOCIAL_SHARE:
{
  "event": "SHARE_OPPORTUNITY_CREATED",
  "user_id": "UUID",
  "share_type": "idea_evaluation_success",
  "share_content": {
    "template": "I just got a {originality_score}% originality score on my innovation idea!",
    "variables": {
      "originality_score": 92,
      "idea_title": "AI-powered personalized learning paths"
    },
    "share_image": "auto_generated_badge_image_url",
    "share_link": "ecoskiller.com/ideas/{idea_id}"
  },
  "incentive": "Share and earn 10 XP"
}
```

---

## 1️⃣4️⃣ PERFORMANCE MONITORING

**Metrics to Track:**

```yaml
SUCCESS_METRICS:
  - name: evaluation_success_rate
    type: percentage
    target: "> 98%"
    alert_threshold: "< 95%"
    
  - name: evaluations_per_second
    type: gauge
    target: "500-1000 RPS"
    alert_threshold: "< 100 RPS OR > 1500 RPS"
    
  - name: originality_detection_accuracy
    type: percentage
    target: "> 90%"
    measurement: "human review agreement rate"
    review_frequency: "weekly"
    
  - name: false_positive_plagiarism_rate
    type: percentage
    target: "< 5%"
    alert_threshold: "> 10%"
    
  - name: false_negative_plagiarism_rate
    type: percentage
    target: "< 2%"
    alert_threshold: "> 5%"

LATENCY_METRICS:
  - name: p50_evaluation_latency
    target: "< 1.0 seconds"
    alert_threshold: "> 2.0 seconds"
    
  - name: p95_evaluation_latency  
    target: "< 2.0 seconds"
    alert_threshold: "> 5.0 seconds"
    
  - name: p99_evaluation_latency
    target: "< 5.0 seconds"
    alert_threshold: "> 10.0 seconds"

ERROR_METRICS:
  - name: error_rate
    type: percentage
    target: "< 1%"
    alert_threshold: "> 5%"
    
  - name: timeout_rate
    type: percentage  
    target: "< 0.5%"
    alert_threshold: "> 2%"
    
  - name: model_failure_rate
    type: percentage
    target: "< 0.1%"
    alert_threshold: "> 1%"

DRIFT_METRICS:
  - name: originality_score_mean_drift
    type: percentage_change
    baseline: "weekly_moving_average"
    alert_threshold: "drift > 15% for 2 consecutive weeks"
    
  - name: quality_score_distribution_shift
    type: KL_divergence
    baseline: "previous_week_distribution"
    alert_threshold: "KL_divergence > 0.2"
    
  - name: safety_violation_rate_drift
    type: percentage_change
    baseline: "weekly_moving_average"
    alert_threshold: "increase > 20%"

ANOMALY_METRICS:
  - name: plagiarism_cluster_detection
    type: count
    description: "Same content submitted by multiple users"
    alert_threshold: "> 5 users submitting 90% similar content"
    
  - name: spam_burst_detection  
    type: count
    description: "Unusual spike in low-quality submissions"
    alert_threshold: "> 100 quality_score < 0.3 in 1 hour"
    
  - name: bot_submission_pattern
    type: boolean
    description: "Automated submission patterns detected"
    alert_threshold: "confidence > 0.85"
```

**OBSERVABILITY_AGENT Integration:**

```python
EMIT_METRICS_EVENT:
{
  "event": "AGENT_METRICS_UPDATE",
  "agent_name": "IDEA_EVALUATION_AGENT",
  "timestamp_utc": "ISO8601",
  "metrics": {
    "success_rate": 0.987,
    "error_rate": 0.013,
    "avg_latency_ms": 1234,
    "p95_latency_ms": 1850,
    "p99_latency_ms": 3200,
    "evaluations_last_hour": 45000,
    "model_confidence_avg": 0.83,
    "human_review_queue_size": 234
  },
  "anomalies_detected": [
    {
      "type": "LATENCY_SPIKE",
      "severity": "medium",
      "description": "p95 latency increased 40% in last 10 minutes",
      "possible_cause": "database read replica lag"
    }
  ]
}
```

**Dashboard Integration:**
- Real-time Grafana dashboard
- Prometheus metrics scraping
- Alerting via PagerDuty for critical thresholds
- Weekly performance reports to product team

---

## 1️⃣5️⃣ VERSIONING POLICY

**Version Scheme:** Semantic Versioning (MAJOR.MINOR.PATCH)

**MAJOR version change (X.0.0):**
- Breaking changes to input/output contracts
- Model architecture changes (e.g., switch from BERT to GPT)
- Fundamental algorithm changes
- Requires migration plan

**MINOR version change (1.X.0):**
- New features added (e.g., new safety category detection)
- New optional input fields
- Performance improvements
- Backward compatible changes

**PATCH version change (1.0.X):**
- Bug fixes
- Minor model retraining with same architecture
- Configuration updates
- No functional changes

**Version Change Process:**

```
1. PROPOSAL → Engineering team submits version bump proposal
2. REVIEW → Tech lead + ML lead review
3. TEST → Full regression testing on staging
4. MIGRATION_PLAN → Document any data/config migrations
5. DEPLOYMENT → Blue-green deployment (zero downtime)
6. ROLLBACK_PLAN → Documented rollback procedure
7. MONITORING → Enhanced monitoring for 48 hours post-deploy
```

**Version Storage:**
```
- Git tag: v1.0.0
- Model registry: models/idea_evaluation_agent/v1.0.0/
- Database: agent_versions table
- Audit log: Every evaluation references version used
```

**Backward Compatibility:**
- Last 3 MINOR versions supported simultaneously
- Deprecated versions: 90-day sunset period
- Critical security patches: backported to last 2 MAJOR versions

**Rollback Capability:**
- Instant rollback to previous MINOR version
- Data migration rollback scripts maintained
- Model version rollback via model registry
- Maximum rollback window: 30 days

---

## 1️⃣6️⃣ NON-NEGOTIABLE RULES

**Agent MUST NOT:**

```
❌ Create hidden logic not documented in this spec
❌ Modify historical evaluation records (append-only)
❌ Auto-delete audit logs (retention: 7 years minimum)
❌ Override governance agents (e.g., MODERATION_AGENT decisions)
❌ Bypass compliance checks (e.g., skip safety screening)
❌ Mix domain data (e.g., use Science domain patterns for Arts evaluation)
❌ Execute outside scope (e.g., auto-approve ideas without evaluation)
❌ Make subjective human judgment calls (e.g., "this idea will succeed")
❌ Access cross-tenant data for similarity comparison
❌ Cache user data beyond 24 hours without explicit consent
❌ Use unapproved AI models (only pre-approved Claude Sonnet 4.5)
❌ Expose model internals to users (ML opacity required)
❌ Train on user data without anonymization and consent
❌ Share evaluation results across tenants for benchmarking
❌ Implement features not defined in this specification
```

**Agent MUST:**

```
✅ Validate tenant isolation on every request
✅ Log every evaluation to immutable audit trail
✅ Emit structured events for downstream agents
✅ Return deterministic results for identical inputs
✅ Flag low-confidence evaluations for human review
✅ Reject malformed or incomplete inputs
✅ Honor user privacy and data protection rules
✅ Comply with GDPR, COPPA, DPDP regulations
✅ Use versioned models with explicit version tracking
✅ Provide explainability metadata (feature importance)
✅ Support disaster recovery and data restoration
✅ Maintain <2 second p95 latency SLA
✅ Achieve >98% uptime SLA
✅ Process evaluations in order (FIFO within tenant)
✅ Handle graceful degradation if upstream agents fail
```

---

## 1️⃣7️⃣ COMPLIANCE & REGULATORY REQUIREMENTS

**GDPR Compliance (European Users):**
```
- Right to explanation: Provide evaluation reasoning
- Right to erasure: Support user data deletion (preserve audit log)
- Right to rectification: Allow appeal of evaluation results
- Data minimization: Collect only necessary data
- Consent management: Track user consent for data processing
- Data portability: Export evaluation history in machine-readable format
```

**COPPA Compliance (Users < 13 years):**
```
- Parental consent required for innovation submissions
- No profiling of children without consent
- Limited data retention for minors (delete after 30 days)
- No targeted recommendations based on child's ideas
- Enhanced safety screening for child-submitted content
```

**DPDP Act (India):**
```
- Data fiduciary obligations met
- Purpose limitation enforced
- Data localization: Store Indian user data in India
- Grievance redressal officer appointed
- Audit trail for data processing activities
```

**Copyright & IP Compliance:**
```
- Detect copyrighted content references
- Detect trademark usage without permission
- Flag patent similarity (basic check)
- Provide DMCA takedown support
- Respect intellectual property rights
```

**Content Safety & Harmful Content:**
```
- Block violent, hateful, sexual content
- Block dangerous instructions (e.g., weapons, hacking)
- Block illegal activities promotion
- Block child exploitation content (zero tolerance)
- Report CSAM to NCMEC (US) and appropriate authorities
```

---

## 1️⃣8️⃣ DISASTER RECOVERY & BUSINESS CONTINUITY

**Recovery Time Objective (RTO):** < 4 hours

**Recovery Point Objective (RPO):** < 15 minutes

**Backup Strategy:**
```
- Real-time replication: Audit logs replicated to 3 regions
- Hourly backups: Evaluation results database
- Daily backups: ML models and configurations
- Weekly backups: Historical idea index (FAISS)
```

**Disaster Scenarios & Recovery:**

```
Scenario 1: Database Failure
- Detection: Health check fails
- Response: Failover to read replica (automatic)
- Recovery: Restore from latest backup
- Downtime: < 5 minutes

Scenario 2: ML Model Corruption
- Detection: Abnormal score distributions
- Response: Rollback to previous model version
- Recovery: Redeploy last known good model
- Downtime: < 30 minutes

Scenario 3: Complete Region Failure
- Detection: Multi-region health check
- Response: Traffic rerouted to backup region
- Recovery: Full region rebuild
- Downtime: < 4 hours
```

**Testing:**
- Disaster recovery drill: Quarterly
- Backup restore test: Monthly
- Failover test: Monthly

---

## 1️⃣9️⃣ HUMAN-IN-THE-LOOP REQUIREMENTS

**When Human Review Required:**

```
1. Low Confidence: confidence_score < 0.70
2. High Originality Uncertainty: originality_score 0.60-0.79
3. Safety Gray Area: safety_score 0.85-0.94
4. Copyright Violation Suspected: has_copyright_risk == true
5. User Appeals Evaluation: User disputes result
6. Anomaly Detected: Unusual submission patterns
7. High-Value Ideas: Potential platform-wide impact
8. Regulatory Compliance: Required for legal reasons
```

**Human Review SLA:**
```
- Priority 1 (Safety): < 4 hours
- Priority 2 (Appeals): < 24 hours  
- Priority 3 (Quality): < 72 hours
- Priority 4 (Routine): < 7 days
```

**Human Override Authority:**
```
- Moderators: Can override safety flags
- Compliance Officers: Can override IP violation flags
- Senior Reviewers: Can override quality assessments
- All overrides MUST be logged with justification
```

**Human-AI Collaboration:**
```
- AI provides initial evaluation + explanation
- Human reviews AI reasoning and evidence
- Human makes final decision (AI advises only)
- Human feedback used to retrain models
- Disagreements analyzed for model improvement
```

---

## 2️⃣0️⃣ TESTING & QUALITY ASSURANCE

**Unit Tests (100% Coverage Required):**
```
- Input validation logic
- Tenant isolation checks
- Score calculation formulas
- Threshold enforcement
- Event emission logic
- Error handling paths
```

**Integration Tests:**
```
- End-to-end evaluation workflow
- Upstream agent integration (FEATURE_STORE_AGENT)
- Downstream event triggering (IDEA_DNA_AGENT)
- Database read/write operations
- Model API calls and fallbacks
- Cache behavior verification
```

**ML Model Tests:**
```
- Accuracy on gold-standard test set (> 90%)
- Fairness across user demographics
- Robustness to adversarial inputs
- Drift detection validation
- Confidence calibration accuracy
```

**Load & Stress Tests:**
```
- Peak load: 1000 RPS sustained for 1 hour
- Burst traffic: 2000 RPS for 5 minutes
- Sustained load: 500 RPS for 24 hours
- Database connection pool stress
- Memory leak detection (24hr run)
```

**Security Tests:**
```
- Cross-tenant access attempts (should fail)
- SQL injection attempts (should fail)
- Malformed input fuzzing
- API authentication bypass attempts
- Rate limit enforcement verification
```

**Chaos Engineering:**
```
- Random service failures
- Network latency injection  
- Database read replica lag simulation
- Model API timeout simulation
- Verify graceful degradation
```

---

## 2️⃣1️⃣ DEPLOYMENT CHECKLIST

**Pre-Deployment:**
```
✅ All tests passing (unit, integration, load)
✅ Code review approved by 2+ engineers
✅ Security review completed
✅ Performance benchmarks met
✅ Documentation updated
✅ Version tag created (git)
✅ Rollback plan documented
✅ On-call engineer assigned
✅ Stakeholders notified (product, compliance)
```

**Deployment Process:**
```
1. Deploy to STAGING environment
2. Run smoke tests (15 minutes)
3. Deploy to PRODUCTION (blue-green, 10% traffic)
4. Monitor for 1 hour (metrics, errors, latency)
5. Increase traffic to 50%
6. Monitor for 2 hours
7. Increase traffic to 100%
8. Monitor for 24 hours (enhanced logging)
9. Declare deployment successful
```

**Post-Deployment:**
```
✅ Verify metrics dashboard (success rate, latency, errors)
✅ Check audit logs for anomalies
✅ Verify downstream agents receiving events correctly
✅ Monitor human review queue (should not spike)
✅ Check PagerDuty (no critical alerts)
✅ Verify model version in production matches expected
✅ Run post-deployment tests (smoke, canary)
```

**Rollback Triggers:**
```
❌ Error rate > 5%
❌ P95 latency > 5 seconds for 10 minutes
❌ Success rate < 95%
❌ Critical security vulnerability discovered
❌ Data corruption detected
❌ Downstream agents failing due to this agent
```

---

## 2️⃣2️⃣ OPERATIONAL RUNBOOKS

### Runbook 1: High Error Rate

```
SYMPTOM: Error rate > 5% for 10 minutes
IMPACT: Users cannot get idea evaluations

DIAGNOSIS:
1. Check Grafana dashboard for error types
2. Check application logs for stack traces
3. Check database connection health
4. Check ML model service health
5. Check upstream agent health (FEATURE_STORE_AGENT)

RESOLUTION:
If database issue:
  → Failover to read replica
  → Notify database team

If model service issue:
  → Check model API status page
  → Consider rollback to previous version
  
If upstream agent issue:
  → Enable fallback mode (reduced features)
  → Notify upstream agent owner

ESCALATION:
- 10 minutes: On-call engineer
- 30 minutes: Tech lead + Product manager
- 60 minutes: VP Engineering
```

### Runbook 2: Plagiarism Detection False Positives Spike

```
SYMPTOM: > 20% of ideas flagged as plagiarism (normal: 2-5%)
IMPACT: Legitimate ideas blocked, user complaints

DIAGNOSIS:
1. Check if new model version deployed recently
2. Analyze recent flagged ideas for common patterns
3. Check similarity threshold configuration
4. Check FAISS index health
5. Check if spam attack in progress

RESOLUTION:
If model issue:
  → Rollback to previous model version
  → Schedule model retraining review

If threshold misconfiguration:
  → Adjust similarity threshold (increase)
  → Monitor for 1 hour

If spam attack:
  → Enable stricter rate limits
  → Flag suspicious accounts
  → Notify security team

ESCALATION:
- 30 minutes: ML lead
- 2 hours: Product manager
- 4 hours: Compliance officer (if legal risk)
```

### Runbook 3: Evaluation Latency Degradation

```
SYMPTOM: P95 latency > 5 seconds
IMPACT: Slow user experience, timeout errors

DIAGNOSIS:
1. Check database query performance
2. Check FAISS similarity search latency
3. Check ML model inference time
4. Check LLM API latency
5. Check network latency to dependencies

RESOLUTION:
If database slow:
  → Identify slow queries (query analyzer)
  → Add database indexes if needed
  → Scale up database resources

If FAISS slow:
  → Check index size (rebuild if fragmented)
  → Increase FAISS workers
  → Consider sharding index

If model slow:
  → Check model server load
  → Scale up inference servers
  → Enable caching for common patterns

ESCALATION:
- 30 minutes: DevOps engineer
- 1 hour: Tech lead
- 2 hours: VP Engineering
```

---

## 2️⃣3️⃣ KEY PERFORMANCE INDICATORS (KPIs)

**Business KPIs:**
```
- Idea Evaluation Throughput: > 500K evaluations/day
- Evaluation Accuracy: > 90% agreement with human review
- User Trust Score: > 85% (via surveys)
- Innovation Economy GMV: Tracked separately (royalties paid)
- False Rejection Rate: < 5%
- Time to Evaluation: < 2 seconds (p95)
```

**Technical KPIs:**
```
- System Uptime: > 99.9% (SLA)
- Error Rate: < 1%
- Latency P50: < 1 second
- Latency P95: < 2 seconds  
- Latency P99: < 5 seconds
- Model Accuracy: > 90%
- Drift Detection: < 10% score shift per month
```

**Operational KPIs:**
```
- Human Review Queue Size: < 500 items
- Mean Time to Recovery (MTTR): < 1 hour
- Incident Count: < 2 per month (critical)
- Deployment Frequency: Weekly (continuous deployment)
- Rollback Success Rate: 100%
```

**Safety KPIs:**
```
- Harmful Content Blocked: 100% (zero tolerance)
- False Positive Safety Flags: < 2%
- COPPA Compliance: 100% (automated checks)
- Copyright Violation Detection: > 95%
- User Report Response Time: < 24 hours
```

---

## 2️⃣4️⃣ CONTINUOUS IMPROVEMENT PROCESS

**Weekly:**
```
- Review evaluation accuracy metrics
- Analyze human review disagreements
- Update ML model with new labeled data
- Review and adjust confidence thresholds
- Check for new spam/abuse patterns
```

**Monthly:**
```
- Full model retraining on updated dataset
- A/B test new model versions
- Review and update safety rules
- Analyze originality score distribution shifts
- Review operational runbooks for updates
```

**Quarterly:**
```
- Major model architecture review
- Compliance audit (GDPR, COPPA, DPDP)
- Security penetration testing
- Disaster recovery drill
- Stakeholder feedback sessions (product, users)
```

**Annually:**
```
- Full system architecture review
- Technology stack evaluation (consider upgrades)
- Competitive analysis (innovation economy features)
- Long-term roadmap planning
- Major version upgrade planning
```

---

## 🔒 FINAL SEAL & LOCK

**This specification is:**
```
✔ SEALED - No creative interpretation allowed
✔ LOCKED - Changes require version bump + review
✔ DETERMINISTIC - Same input → Same output
✔ COMPLIANT - GDPR + COPPA + DPDP + Copyright
✔ SECURE - Zero-trust + Multi-tenant isolation
✔ AUDITABLE - Immutable audit trail
✔ SCALABLE - 10M-100M users ready
✔ OBSERVABLE - Full monitoring + alerting
✔ RECOVERABLE - Disaster recovery tested
✔ EXPLAINABLE - ML decisions transparent
✔ ETHICAL - Human oversight + appeals process
```

**DEVIATION FROM THIS SPEC:**
```
→ STOP EXECUTION
→ LOG INCIDENT (severity: CRITICAL)
→ ALERT ENGINEERING MANAGER
→ REQUIRE SPECIFICATION AMENDMENT (versioned)
→ NO PRODUCTION DEPLOYMENT UNTIL RESOLVED
```

**SPECIFICATION OWNERSHIP:**
```
Author: AI Architecture Team
Reviewers: ML Lead, Security Lead, Compliance Officer
Approvers: VP Engineering, CTO
Version: 1.0.0
Last Updated: 2026-02-25
Next Review: 2026-05-25 (quarterly)
```

**CHANGES TO THIS SPECIFICATION:**
```
- Require pull request with full justification
- Require review by 3+ senior engineers
- Require security review if security-related
- Require compliance review if regulatory-related
- Require version bump (semantic versioning)
- Update CHANGELOG with detailed changes
- Notify all dependent teams
```

---

## 📋 APPENDIX A: GLOSSARY

**Innovation Economy:** System where users earn royalties for original ideas adopted by others

**Idea DNA:** Unique semantic fingerprint of an idea for similarity tracking

**Originality Score:** ML-computed score (0.0-1.0) indicating idea novelty

**Quality Score:** ML-computed score (0.0-1.0) indicating idea viability

**Safety Score:** ML-computed score (0.0-1.0) indicating content safety

**Plagiarism:** Copying someone else's idea without attribution

**Tenant Isolation:** Security principle preventing cross-tenant data access

**Domain Isolation:** Security principle preventing cross-domain data leakage

**Passive Intelligence:** Measuring user intelligence through behavior observation

**Semantic Fingerprint:** Hash-based representation of idea's core concepts

**Royalty Engine:** System calculating and distributing payments for idea adoption

**Copy Detection:** System identifying duplicate or similar content

**FAISS:** Facebook AI Similarity Search - vector similarity search library

**Confidence Score:** Model's self-assessment of prediction reliability

**Drift Detection:** Monitoring for ML model performance degradation over time

**Human-in-the-Loop:** Design requiring human oversight of AI decisions

---

## 📋 APPENDIX B: ERROR CODES

```
INPUT_INVALID_UUID - Malformed UUID provided
INPUT_MISSING_FIELD - Required field not provided
INPUT_CONTENT_TOO_SHORT - Content < 10 characters
INPUT_CONTENT_TOO_LONG - Content > 10,000 characters
INPUT_MALFORMED - Data structure invalid

SECURITY_TENANT_VIOLATION - Cross-tenant access attempt
SECURITY_DOMAIN_VIOLATION - Cross-domain access attempt  
SECURITY_PERMISSION_DENIED - User lacks required permission
SECURITY_RATE_LIMIT_EXCEEDED - Too many requests

MODEL_UNAVAILABLE - ML model service down
MODEL_TIMEOUT - ML model inference timeout
MODEL_CONFIDENCE_LOW - Model confidence < 0.70

AI_TIMEOUT - LLM API call timeout
AI_SERVICE_ERROR - LLM API returned error

DATA_CORRUPTION_DETECTED - Output data integrity check failed
DATA_LINEAGE_BROKEN - Upstream data source unavailable

EVALUATION_FAILED - Generic evaluation failure
EVALUATION_REQUIRES_REVIEW - Human review required
EVALUATION_REJECTED_UNSAFE - Content failed safety check
EVALUATION_REJECTED_COMPLIANCE - Copyright/IP violation detected

INTERNAL_ERROR - Unhandled exception
DEPENDENCY_UNAVAILABLE - Upstream agent unavailable
DATABASE_ERROR - Database operation failed
CACHE_ERROR - Cache operation failed
```

---

## 📋 APPENDIX C: SAMPLE EVALUATION FLOW

```
1. USER SUBMITS IDEA
   ↓
2. IDEA_SUBMISSION_CREATED EVENT
   ↓
3. IDEA_EVALUATION_AGENT TRIGGERED
   ↓
4. INPUT VALIDATION
   - Check UUID formats
   - Verify tenant isolation
   - Verify domain access
   - Check rate limits
   ↓
5. FEATURE EXTRACTION (parallel)
   - Content features (TF-IDF, embeddings)
   - User features (reputation, history)
   - Intelligence profile features
   - Domain features
   ↓
6. ML EVALUATION (parallel)
   - Originality Model → originality_score
   - Quality Model → quality_score
   - Safety Model → safety_score
   ↓
7. AI SEMANTIC ANALYSIS (parallel)
   - Extract concepts → idea_dna
   - Detect violations → violation_flags
   ↓
8. SIMILARITY SEARCH
   - Query FAISS index
   - Find similar ideas
   - Compute similarity scores
   ↓
9. DECISION LOGIC
   - Aggregate scores
   - Apply thresholds
   - Determine action (approve/reject/review)
   ↓
10. OUTPUT GENERATION
    - Package result
    - Generate audit log
    - Compute confidence score
    ↓
11. EVENT EMISSION (parallel)
    - IDEA_DNA_INDEX_UPDATE
    - ROYALTY_ELIGIBILITY_CHECK (if approved)
    - MODERATION_REVIEW_REQUIRED (if flagged)
    - USER_REPUTATION_UPDATE
    - COPY_DETECTION_INDEX_UPDATE
    ↓
12. RESPONSE TO USER
    - "Evaluation Complete"
    - Show scores (if approved)
    - Show revision suggestions (if review needed)
    ↓
13. AUDIT LOG PERSISTED (immutable)

Total Time: 1.2 seconds (typical)
```

---

**END OF SPECIFICATION**

**Version:** 1.0.0  
**Status:** SEALED & LOCKED  
**Authority:** MASTER_AGENT_CREATION_PROMPT v1.0  
**Platform:** Ecoskiller Antigravity  
**Compliance:** GDPR + COPPA + DPDP + Enterprise SaaS  

**This document is the single source of truth for IDEA_EVALUATION_AGENT implementation.**
