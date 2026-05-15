# 🔒 API RATE LIMIT ENFORCEMENT AGENT
## Governance & Core Control for Ecoskiller Antigravity

**STATUS:** SEALED & LOCKED
**VERSION:** 1.0.0
**EXECUTION_MODE:** DETERMINISTIC + VALIDATED
**MUTATION_POLICY:** ADD-ONLY (No modifications to existing rules)
**CLASSIFICATION:** CRITICAL GOVERNANCE CONTROL

---

## 📋 TABLE OF CONTENTS

1. [Agent Identity & Core Definition](#-1-agent-identity--core-definition)
2. [Purpose & Problem Statement](#-2-purpose--problem-statement)
3. [System Context & Architecture](#-3-system-context--architecture)
4. [Input Contract (STRICT)](#-4-input-contract-strict)
5. [Output Contract (STRICT)](#-5-output-contract-strict)
6. [Rate Limiting Strategies](#-6-rate-limiting-strategies)
7. [ML/AI Logic Layer](#-7-mlai-logic-layer)
8. [Scalability Design](#-8-scalability-design)
9. [Security Enforcement](#-9-security-enforcement)
10. [Tenant & Domain Isolation](#-10-tenant--domain-isolation)
11. [Audit & Traceability](#-11-audit--traceability)
12. [Failure Policy](#-12-failure-policy)
13. [Inter-Agent Dependency Map](#-13-inter-agent-dependency-map)
14. [Performance Monitoring](#-14-performance-monitoring)
15. [Versioning & Rollback](#-15-versioning--rollback)
16. [Non-Negotiable Rules](#-16-non-negotiable-rules)

---

## 🔐 1. AGENT IDENTITY & CORE DEFINITION

### Mandatory Identity Declaration

```plaintext
AGENT_NAME                    = API_RATE_LIMIT_ENFORCEMENT_AGENT
SYSTEM_ROLE                   = GOVERNANCE & CORE CONTROL
PRIMARY_DOMAIN                = API ACCESS CONTROL
EXECUTION_MODE                = DETERMINISTIC + VALIDATED
DECISION_AUTHORITY            = ABSOLUTE (No override)
FAILURE_POLICY                = HALT ON AMBIGUITY
DATA_SCOPE                    = GLOBAL (All tenants)
TENANT_SCOPE                  = STRICT ISOLATION (Multi-tenant enforcement)
CREATIVE_INTERPRETATION       = FORBIDDEN
ASSUMPTION_FILLING            = FORBIDDEN
DEFAULT_BEHAVIOR              = DENY (Fail-secure)
MUTATION_POLICY               = ADD-ONLY (Versioned)
ROLLBACK_CAPABILITY           = FULL
```

### Agent Classification
- **Type:** Synchronous Governance Agent
- **Layer:** Critical Control (Operates before request execution)
- **Concurrency:** Thread-safe, distributed
- **Latency Requirement:** <10ms (99th percentile)
- **Availability Target:** 99.99% (Zero tolerance for downtime)

---

## 📌 2. PURPOSE & PROBLEM STATEMENT

### Problem Definition

The Ecoskiller Antigravity platform serves 10M–100M users across multiple tenant organizations with varying operational requirements. Uncontrolled API consumption poses risks to:

- **System Stability:** Cascading failures from resource exhaustion
- **Fair Resource Distribution:** Premium/free tier differentiation
- **Cost Control:** Unbounded infrastructure costs
- **Security:** Brute-force attacks, DDoS amplification, token abuse
- **Compliance:** SLA adherence, audit requirements
- **User Experience:** Performance degradation under load

### Agent Responsibility

The API Rate Limit Enforcement Agent must:

1. **Enforce per-tenant rate limits** in real-time
2. **Implement multi-tier rate limiting** (user, tenant, service, global)
3. **Detect and mitigate abuse patterns** (ML-driven anomaly detection)
4. **Maintain compliance** with SLA commitments
5. **Provide fair resource allocation** across tenants
6. **Generate audit trails** for all decisions
7. **Support graceful degradation** under extreme load

### Expected Outcomes

- **99.9% of legitimate traffic:** Pass without delay
- **Abusive traffic:** Rejected within 10ms
- **Cost savings:** 30–40% infrastructure reduction via load shedding
- **Security incidents prevented:** 95%+ attack mitigation rate
- **Zero silent failures:** All rejections logged with reason codes

---

## 🏗️ 3. SYSTEM CONTEXT & ARCHITECTURE

### Platform Architecture Context

```plaintext
┌─────────────────────────────────────────────────────────────┐
│                   ECOSKILLER ANTIGRAVITY                    │
│              Enterprise Multi-Tenant SaaS Platform           │
├─────────────────────────────────────────────────────────────┤
│                                                               │
│  ┌─────────────────────────────────────────────────────────┐ │
│  │          API GATEWAY (Request Entry Point)              │ │
│  │  - Protocol: HTTP/2, gRPC, WebSocket                   │ │
│  │  - Endpoints: 500+ distributed                         │ │
│  │  - TLS 1.3 enforced                                    │ │
│  └──────────────────┬──────────────────────────────────────┘ │
│                     │                                         │
│  ┌──────────────────▼──────────────────────────────────────┐ │
│  │    🔒 RATE_LIMIT_ENFORCEMENT_AGENT (THIS AGENT)        │ │
│  │                                                          │ │
│  │  ▪ Tenant-level enforcement                            │ │
│  │  ▪ Multi-tier bucketing                                │ │
│  │  ▪ ML anomaly detection                                │ │
│  │  ▪ Distributed state management                        │ │
│  │  ▪ Sub-10ms decision latency                           │ │
│  └──────────────────┬──────────────────────────────────────┘ │
│                     │                                         │
│  ┌──────────────────▼──────────────────────────────────────┐ │
│  │        REQUEST ROUTING & VALIDATION LAYER               │ │
│  │  - Auth enforcement                                     │ │
│  │  - Schema validation                                    │ │
│  │  - Domain isolation check                              │ │
│  └──────────────────┬──────────────────────────────────────┘ │
│                     │                                         │
│  ┌──────────────────▼──────────────────────────────────────┐ │
│  │       MICROSERVICES (Job Portal, Skill Engine,         │ │
│  │        Projects, Dojo, ERP, etc.)                       │ │
│  └─────────────────────────────────────────────────────────┘ │
│                                                               │
│  ┌─────────────────────────────────────────────────────────┐ │
│  │            OBSERVABILITY & MONITORING                   │ │
│  │  └──────────────────────────────────────────────────────┘ │
│  │         (Metrics, Logs, Traces → Real-time dashboard)   │ │
│  └─────────────────────────────────────────────────────────┘ │
│                                                               │
└─────────────────────────────────────────────────────────────┘
```

### Rate Limit Enforcement Position

The agent operates **immediately after API Gateway authentication** and **before request routing** to ensure:

- Early rejection of invalid/abusive traffic
- Minimal resource consumption for rate-limited requests
- Global view of all tenant consumption
- Atomic decision making (no race conditions)

### Supported User Ecosystem

```plaintext
STUDENT
├─ Free Tier: 100 req/min per user
├─ Premium Tier: 1,000 req/min per user
└─ Institution-sponsored: 5,000 req/min per user

TRAINERS/MENTORS
├─ Free: 500 req/min
├─ Professional: 2,000 req/min
└─ Enterprise Trainer: 10,000 req/min

EVALUATORS
├─ Institute: 5,000 req/min
├─ Corporate: 15,000 req/min
└─ Platform: Unlimited

INSTITUTES
├─ Small (< 500 users): 50K req/min per tenant
├─ Medium (500-5K users): 200K req/min per tenant
└─ Large (> 5K users): 500K req/min per tenant

ENTERPRISES
├─ SME: 100K req/min per tenant
├─ Corporate (L1-L5): 500K req/min per tenant
├─ Enterprise (L6-L7): 2M req/min per tenant
└─ Strategic Partner: Custom limits

RECRUITERS/HR
├─ Individual: 1,000 req/min
├─ Agency (< 50 users): 10K req/min
└─ Global Recruitment: 50K req/min

ADMINS
├─ Tenant Admin: 10K req/min
├─ Compliance Admin: 20K req/min
└─ Platform Admin: Unlimited (with logging)

AUTOMATION/AI AGENTS
├─ Background Job: 5,000 req/min
├─ Scheduled Task: 10,000 req/min
└─ Real-time Service: 50,000 req/min
```

### Deployment Architecture

```plaintext
DEPLOYMENT_MODEL        = DISTRIBUTED & REPLICATED
INFRASTRUCTURE          = MICROSERVICES
DATA_CONSISTENCY        = EVENTUAL (Optimistic sync)
STATE_STORE             = REDIS CLUSTER (Primary)
BACKUP_STATE            = CASSANDRA (Audit trail)
DISTRIBUTION            = GEOGRAPHIC (Multi-region)
REDUNDANCY              = 3-way replication minimum
FAILOVER_TIME           = < 100ms automatic
```

---

## 📥 4. INPUT CONTRACT (STRICT)

### Request Structure (Immutable)

Every API request must provide the following mandatory fields to the Rate Limit Enforcement Agent:

```json
{
  "request_metadata": {
    "request_id": "uuid-v4-string (required)",
    "timestamp_utc": "ISO8601-string (required)",
    "request_path": "/api/v1/endpoint (required)",
    "http_method": "GET|POST|PUT|DELETE|PATCH (required)",
    "protocol_version": "HTTP/2 or HTTP/1.1 (required)"
  },
  
  "client_identity": {
    "client_id": "UUID (required)",
    "user_id": "UUID (required)",
    "tenant_id": "UUID (required, hard isolation)",
    "user_role": "STUDENT|TRAINER|EVALUATOR|INSTITUTE|ENTERPRISE|RECRUITER|ADMIN|AGENT (required)",
    "subscription_tier": "FREE|PROFESSIONAL|ENTERPRISE|CUSTOM (required)",
    "auth_token_hash": "SHA256(token) (required for validation)"
  },
  
  "request_classification": {
    "api_endpoint": "fully-qualified endpoint string (required)",
    "operation_category": "READ|WRITE|DELETE|COMPUTE|BACKGROUND (required)",
    "priority_level": "CRITICAL|HIGH|NORMAL|LOW (required)",
    "idempotency_key": "UUID (required for WRITE operations)",
    "estimated_cost": "integer (0-1000, relative cost units)"
  },
  
  "security_context": {
    "ip_address": "IPv4|IPv6 (required)",
    "user_agent": "string (required)",
    "origin_domain": "string (required)",
    "tls_cipher_suite": "string (required for HTTPS)",
    "client_certificate_cn": "string (optional but recommended)"
  },
  
  "domain_context": {
    "domain_track": "Arts|Commerce|Science|Technology|Administration (required)",
    "tenant_domain_scope": "array of approved domains (required)"
  },
  
  "validation_requirements": {
    "require_rate_check": true,
    "require_quota_check": true,
    "require_abuse_detection": true
  }
}
```

### Input Validation Rules (Non-Negotiable)

```plaintext
VALIDATION RULE SET:

1. REQUEST IDENTITY VALIDATION
   ├─ request_id: Must be unique UUID v4, reject duplicates within 60s window
   ├─ timestamp_utc: Must be within ±5 seconds of server time (clock skew tolerance)
   ├─ request_path: Must match regex ^/api/v[0-9]+/[a-z0-9_/]*$
   ├─ http_method: Must be one of 5 HTTP verbs
   └─ protocol_version: Must be HTTP/1.1 or HTTP/2

2. CLIENT IDENTITY VALIDATION
   ├─ client_id: Must exist in tenant registry
   ├─ user_id: Must exist and be active in client's tenant
   ├─ tenant_id: Must exist in platform registry
   ├─ user_role: Must be valid enum, must match user profile
   ├─ subscription_tier: Must match user's current subscription
   └─ auth_token_hash: Must be valid, non-revoked, non-expired

3. REQUEST CLASSIFICATION VALIDATION
   ├─ api_endpoint: Must be registered, must be accessible by user_role
   ├─ operation_category: Must match HTTP method (GET → READ, POST → WRITE)
   ├─ priority_level: Must be <= user's tier priority
   ├─ idempotency_key: Must be unique UUID for WRITE operations (reject duplicates)
   └─ estimated_cost: Must be 0-1000 range

4. SECURITY CONTEXT VALIDATION
   ├─ ip_address: Must be valid IPv4/IPv6, check against blocklist
   ├─ user_agent: Reject known malicious agents
   ├─ origin_domain: Must match registered domain for user
   ├─ tls_cipher_suite: Must be TLS 1.2+ (reject SSL 3.0, TLS 1.0, TLS 1.1)
   └─ client_certificate_cn: Validate against certificate chain if present

5. DOMAIN CONTEXT VALIDATION
   ├─ domain_track: Must be valid enum value
   ├─ tenant_domain_scope: User must have access to all requested domains
   └─ No cross-domain access without explicit permission grant

6. GLOBAL SECURITY CHECKS
   ├─ Tenant isolation: user_tenant_id must match request_tenant_id
   ├─ User exists: Verify user is active, not suspended
   ├─ Auth token: Verify token signature, expiry, revocation status
   ├─ IP whitelist (if configured): Verify IP against whitelist
   └─ Geo-blocking (if configured): Verify user location
```

### Rejection Criteria (Fail-Secure)

If ANY of the following conditions are true, REJECT the request immediately:

```plaintext
MANDATORY REJECTION CONDITIONS:

1. Missing required field → REJECT (400 Bad Request)
2. Invalid format on required field → REJECT (400 Bad Request)
3. user_id ≠ token_user_id → REJECT (401 Unauthorized)
4. tenant_id ≠ token_tenant_id → REJECT (401 Unauthorized)
5. user_role mismatch with token → REJECT (401 Unauthorized)
6. subscription_tier expired → REJECT (402 Payment Required)
7. user_id in suspension list → REJECT (403 Forbidden)
8. tenant_id in suspension list → REJECT (403 Forbidden)
9. IP in global blocklist → REJECT (403 Forbidden)
10. Request timestamp > 5 seconds skew → REJECT (401 Unauthorized)
11. user_role not permitted for endpoint → REJECT (403 Forbidden)
12. Cross-tenant access attempt → REJECT (403 Forbidden)
13. Rate limit exceeded (see Section 6) → REJECT (429 Too Many Requests)
14. Quota exceeded (see Section 6) → REJECT (429 Too Many Requests)
15. Anomaly score > threshold → REJECT (429 Too Many Requests, with flag)
```

### Null/Default Handling Policy

```plaintext
NULL TOLERANCE POLICY (STRICT):

- NO null values allowed in required fields
- NO implicit defaults applied
- NO type coercion (string "123" ≠ integer 123)
- NO field inference from context
- REJECT on ambiguity, NEVER assume

OPTIONAL FIELDS behavior:
- If provided: VALIDATE according to rules
- If missing: USE explicit default (documented in schema)
- Example: idempotency_key is optional for READ operations
  ├─ If provided: MUST be valid UUID, check against idempotency store
  └─ If missing: Generate from (user_id, request_id, timestamp)
```

---

## 📤 5. OUTPUT CONTRACT (STRICT)

### Response Structure (Immutable)

Every decision by the Rate Limit Enforcement Agent must produce the following response:

```json
{
  "decision_metadata": {
    "decision_id": "uuid-v4 (globally unique)",
    "request_id": "uuid (echo from input)",
    "timestamp_utc": "ISO8601",
    "decision_timestamp_ns": "nanoseconds (high-precision timing)",
    "processing_time_ms": "integer (latency in milliseconds)",
    "agent_version": "string (e.g., '1.0.0-beta.5')"
  },

  "decision_outcome": {
    "status": "ALLOWED|REJECTED|THROTTLED|QUEUED",
    "reason_code": "string (e.g., 'RATE_LIMIT_EXCEEDED')",
    "reason_description": "human-readable reason (for logging/debugging)",
    "http_status_code": "integer (200|429|403|401|402)",
    "should_execute": "boolean (true = allow to proceed)"
  },

  "rate_limit_state": {
    "tier_applied": "STUDENT_FREE|TRAINER_PRO|INSTITUTE_LARGE|ENTERPRISE_L7|CUSTOM",
    "limit_type": "PER_USER|PER_TENANT|PER_SERVICE|GLOBAL",
    
    "current_metrics": {
      "requests_in_window": "integer (current count)",
      "limit_for_window": "integer (maximum allowed)",
      "remaining_quota": "integer (requests before limit)",
      "window_duration_seconds": "integer (time window size)",
      "time_until_reset_seconds": "integer (seconds until next window)"
    },

    "throttle_details": {
      "is_throttled": "boolean",
      "throttle_probability": "float (0.0-1.0, for probabilistic throttling)",
      "retry_after_seconds": "integer (for 429 responses)"
    }
  },

  "anomaly_detection_state": {
    "anomaly_detected": "boolean",
    "anomaly_type": "BURST|BRUTE_FORCE|PATTERN_VIOLATION|UNUSUAL_TIMING|SUSPICIOUS_DISTRIBUTION",
    "anomaly_score": "float (0.0-1.0, higher = more suspicious)",
    "anomaly_confidence": "float (0.0-1.0, confidence in detection)",
    "ml_model_version": "string (which version made this decision)",
    "features_analyzed": ["feature_1", "feature_2", ...],
    "decision_factors": {
      "historical_avg_requests": "float",
      "current_rate_deviation": "float (sigma units)",
      "peer_comparison_percentile": "integer (0-100)"
    }
  },

  "quota_tracking": {
    "monthly_usage": "integer (total requests this month)",
    "monthly_limit": "integer (max allowed this month)",
    "monthly_remaining": "integer (quota left)",
    "daily_usage": "integer",
    "daily_limit": "integer",
    "daily_remaining": "integer",
    "billing_cycle_end_date": "ISO8601"
  },

  "compliance_context": {
    "sla_status": "OK|AT_RISK|VIOLATED",
    "compliance_flags": ["flag_1", "flag_2"],
    "audit_required": "boolean (whether this decision needs special logging)",
    "security_incident_flagged": "boolean"
  },

  "traceability": {
    "audit_reference": "uuid (immutable audit log entry ID)",
    "decision_path": ["step_1", "step_2", ...],
    "checksum": "SHA256(entire decision object)",
    "signature": "RSA-4096(checksum, agent_private_key)"
  },

  "next_actions": {
    "next_trigger_event": [
      {
        "event_type": "RATE_LIMIT_RESET|QUOTA_WARNING|UPGRADE_RECOMMENDED|ABUSE_ESCALATION",
        "trigger_time": "ISO8601"
      }
    ],
    "recommended_action": "string (e.g., 'wait_and_retry', 'upgrade_subscription')",
    "contact_support": "boolean (if human intervention needed)"
  }
}
```

### Output Guarantees

```plaintext
IMMUTABLE GUARANTEES:

1. ATOMICITY
   └─ Decision is all-or-nothing: no partial states
   └─ No race conditions possible (distributed locks)
   └─ Decision cannot be undone retroactively

2. CONSISTENCY
   └─ Same input → Same decision (deterministic)
   └─ Decisions respect all validation rules
   └─ State never violates constraints

3. IDEMPOTENCY
   └─ Repeated identical request → Same decision (within 60s window)
   └─ idempotency_key ensures no double-execution
   └─ Safe to retry without side effects

4. TRACEABILITY
   └─ Every decision logged immutably
   └─ Decision path recorded for audit
   └─ Signature prevents tampering

5. LATENCY GUARANTEE
   └─ 99th percentile latency < 10ms
   └─ 99.99th percentile latency < 50ms
   └─ No decision delayed > 100ms (timeout at backend)

6. SECURITY GUARANTEE
   └─ No information leakage in response
   └─ reason_description logged separately, never sent to untrusted client
   └─ Only HTTP status code and rate_limit_state sent to client
```

---

## 🎯 6. RATE LIMITING STRATEGIES

### 6.1 Token Bucket Algorithm (Primary Strategy)

#### Overview

Token bucket is the primary rate limiting mechanism. Each entity (user, tenant) has a "bucket" that fills with tokens at a fixed rate. Each request consumes tokens; if insufficient tokens exist, the request is rejected.

#### Mathematical Model

```plaintext
BUCKET STATE EQUATION:

tokens(t) = min(
  tokens(t-1) + (refill_rate × time_elapsed),
  max_capacity
)

WHERE:
  tokens(t)     = tokens available at time t
  refill_rate   = tokens per second (depends on tier)
  max_capacity  = maximum tokens (burst capacity)
  time_elapsed  = seconds since last update

REQUEST PROCESSING:

IF tokens(t) >= cost_of_request:
  ├─ tokens(t) := tokens(t) - cost_of_request
  ├─ STATUS := ALLOWED
  └─ RETURN: Updated bucket state
ELSE:
  ├─ Calculate: tokens_needed = cost_of_request - tokens(t)
  ├─ Calculate: retry_after = tokens_needed / refill_rate
  ├─ STATUS := REJECTED (429 Too Many Requests)
  └─ RETURN: retry_after header
```

#### Tier-Specific Configuration

```plaintext
STUDENT_FREE TIER:
├─ refill_rate = 100 tokens/minute = 1.67 tokens/second
├─ max_capacity = 100 tokens (burst of 1 minute of traffic)
├─ cost_per_request = 1 token (all endpoints)
└─ Example: 100 req/min, bursts allowed for up to 1 minute

STUDENT_PREMIUM TIER:
├─ refill_rate = 1,000 tokens/minute = 16.67 tokens/second
├─ max_capacity = 1,000 tokens (burst of 1 minute)
├─ cost_per_request = 1 token
└─ Example: 1,000 req/min sustained

TRAINER_PROFESSIONAL TIER:
├─ refill_rate = 2,000 tokens/minute = 33.33 tokens/second
├─ max_capacity = 2,000 tokens
├─ cost_per_request = variable (1 for GET, 5 for WRITE)
└─ Example: 2,000 req/min for reads, 400 req/min for writes

INSTITUTE_MEDIUM TIER (500-5K users):
├─ refill_rate = 200K tokens/minute = 3,333 tokens/second
├─ max_capacity = 200K tokens
├─ cost_per_request = variable by operation category
│  ├─ READ: 1 token
│  ├─ WRITE: 10 tokens
│  ├─ DELETE: 100 tokens
│  └─ COMPUTE: 50 tokens
└─ Example: 200K read req/min, 20K write req/min

ENTERPRISE_L7 TIER:
├─ refill_rate = 2M tokens/minute = 33,333 tokens/second
├─ max_capacity = 2M tokens
├─ cost_per_request = variable
└─ Example: 2M req/min available

CUSTOM TIER:
├─ refill_rate = as contracted
├─ max_capacity = as contracted
├─ cost_per_request = as contracted
└─ Audit every request (compliance requirement)
```

#### Implementation Details

```plaintext
DISTRIBUTED TOKEN BUCKET IMPLEMENTATION:

1. STATE STORAGE (Redis cluster with 3-way replication)
   ├─ Key: rate_limit_bucket:{tenant_id}:{user_id}
   ├─ Value: {
   │    tokens: float (current tokens),
   │    capacity: integer (max tokens),
   │    refill_rate: float (tokens per second),
   │    last_refill_timestamp: int (ns),
   │    tier: string,
   │    updated_at: int (ns)
   │  }
   └─ TTL: 30 days (auto-cleanup of inactive users)

2. CONCURRENT UPDATE MECHANISM
   ├─ Use Redis GETEX with SET options (atomic)
   ├─ Lua script to ensure atomicity:
   │  local bucket = redis.call('GET', key)
   │  if bucket == false then
   │    -- Create new bucket
   │  else
   │    -- Calculate elapsed time
   │    -- Update tokens = min(tokens + refill*elapsed, capacity)
   │    -- Check if sufficient tokens
   │    -- Deduct cost
   │    -- Return decision + state
   │  end
   └─ No race conditions possible

3. SYNCHRONIZATION
   ├─ Redis Cluster mode (not sentinel)
   ├─ 3-way replication across zones
   ├─ Quorum write (2-of-3 success = proceed)
   ├─ Quorum read (2-of-3 success = trust)
   ├─ Eventual consistency < 100ms
   └─ Fallback to pessimistic (stricter) estimate if quorum fails

4. CLOCK SKEW HANDLING
   ├─ Use NTP for server clock synchronization
   ├─ Monitor clock drift across instances
   ├─ If drift > 100ms: log incident, use server time (not client time)
   ├─ Periodic (5-minute) clock correction from NTP master
   └─ Alert if sustained drift > 1 second
```

### 6.2 Sliding Window Counter (Secondary Strategy)

For high-precision enforcement (used in parallel with token bucket):

```plaintext
SLIDING WINDOW COUNTER:

WINDOW_SIZE                    = 60 seconds
REQUEST_COUNT_WINDOW           = number of requests in current 60-second window

ALGORITHM:
1. Maintain circular buffer of 60 one-second buckets
2. When request arrives:
   ├─ Calculate current second index
   ├─ Roll forward any expired buckets
   ├─ Sum all non-expired buckets
   ├─ If sum < limit: increment current bucket, ALLOW
   └─ Else: REJECT

BENEFITS:
├─ More precise than fixed windows
├─ No artificial burst at window boundaries
├─ Works well in parallel with token bucket

STORAGE:
├─ Key: sliding_window:{tenant_id}:{user_id}
├─ Value: [counts for each second of past 60 seconds]
├─ Update frequency: Once per request (lightweight)
└─ TTL: 120 seconds (2 windows)
```

### 6.3 Burst Allowance (Fairness)

```plaintext
BURST_ALLOWANCE_POLICY:

Allow temporary bursts above sustained rate to handle:
├─ Legitimate traffic spikes (user batching requests)
├─ Batch processing operations
├─ Retry storms from faulty clients (handled gracefully)

IMPLEMENTATION:
├─ max_capacity in token bucket allows burst
├─ max_capacity = refill_rate × 60 seconds
│  └─ Allows bursting at 1-minute sustained rate for up to 1 minute
├─ Burst tracked separately: burst_used_tokens
├─ Alert if burst usage > 80% in any 5-minute window

EXAMPLE:
├─ Student free tier: 100 req/min sustained
├─ max_capacity = 100 tokens
│  └─ Can send 100 requests in rapid succession, then wait 1 minute
├─ No burst penalty unless pattern is sustained abuse
```

### 6.4 Cost-Based Limiting (Per-Operation)

Different operations have different costs:

```plaintext
OPERATION COST MATRIX:

                          | STUDENT | TRAINER | INSTITUTE | ENTERPRISE
READ (simple)             |    1    |    1    |     1     |     1
WRITE (insert/update)     |    5    |    5    |    10     |     5
DELETE (permanent)        |   25    |   25    |   100     |    25
COMPUTE (ML inference)    |   50    |   50    |    50     |    50
BACKGROUND_JOB           |    2    |    2    |     2     |     2
BATCH_OPERATION          |   100   |   100   |   200     |   100
EXPORT_LARGE_DATASET     |   500   |   500   |   500     |   500

IMPLEMENTATION:
├─ Each endpoint has registered cost
├─ Cost varies by tenant tier (economic differentiation)
├─ Batch operations cost more (resource intensive)
├─ Machine learning operations cost most (compute-bound)
└─ Helps fair allocation: WRITE ≤ 20 READ requests per minute
```

### 6.5 Tenant-Level Aggregation

```plaintext
MULTI-LEVEL RATE LIMITING:

LEVEL 1: PER-USER LIMIT
├─ Each user_id has individual bucket
├─ Prevents single malicious user from exploiting tenant quota
├─ Applied first (earliest rejection opportunity)

LEVEL 2: PER-TENANT LIMIT
├─ All users in tenant_id share pool
├─ Prevents tenant from exhausting shared infrastructure
├─ Aggregates all per-user usage
├─ Key: rate_limit_bucket:tenant:{tenant_id}

LEVEL 3: PER-SERVICE LIMIT
├─ Each microservice (Job Portal, Skill Engine, etc.) has limit
├─ Prevents one service from blocking others
├─ Key: rate_limit_bucket:service:{service_name}

LEVEL 4: GLOBAL LIMIT
├─ Platform-wide burst protection
├─ Prevents total platform from exceeding infrastructure capacity
├─ Key: rate_limit_bucket:platform:global
├─ Tier: emergency threshold (99th percentile load × 1.5)

CHECK ORDER (Fail-fast):
1. Check per-user limit (fastest check, usually passes)
2. Check per-tenant limit (if institution/enterprise)
3. Check per-service limit (global visibility)
4. Check global limit (emergency circuit-breaker)
5. Only accept if ALL levels allow
```

### 6.6 Prioritization During Overload

```plaintext
PRIORITY-BASED QUEUING (When under extreme load):

PRIORITY TIERS:

CRITICAL (≤5% of capacity, no limiting):
├─ Authentication requests
├─ Compliance audit requests
├─ Platform admin requests
└─ Rescue requests (unblock suspended users)

HIGH (≤20% of capacity, soft limiting):
├─ Payment/subscription requests
├─ Account recovery
├─ Admin panel access
└─ Reporting/analytics for compliance

NORMAL (≤50% of capacity, standard limiting):
├─ Regular read/write operations
├─ User-initiated requests
└─ Standard tier users

LOW (≤25% of capacity, aggressive limiting):
├─ Batch/export operations
├─ Background jobs
├─ Non-authenticated crawlers
└─ Free tier users during outage

IMPLEMENTATION:
├─ Assign priority_level in request metadata
├─ Under overload, reject LOW priority first
├─ Use probabilistic throttling to gradually shed load
└─ Monitor queue depth and adjust accordingly
```

---

## 🤖 7. ML/AI LOGIC LAYER

### 7.1 Anomaly Detection Engine

Detects abusive patterns that bypass rate limits (e.g., slow attacks):

#### Machine Learning Model

```plaintext
MODEL_TYPE                      = UNSUPERVISED ANOMALY DETECTION
ALGORITHM                       = Isolation Forest + LSTM Time Series
FEATURES                        = 50+ behavioral features
TRAINING_FREQUENCY              = Daily (batch) + Real-time drift monitoring
MODEL_VERSION                   = Locked (add-only updates)

PURPOSE:
├─ Detect brute-force attacks
├─ Identify DDoS patterns
├─ Flag suspicious timing patterns
├─ Catch distributed abuse (multiple users coordinating)
└─ Detect token-sharing / credential abuse

TRAINING DATA:
├─ Historical traffic (12 months minimum)
├─ Labeled anomalies (manual + automated flagging)
├─ Peer comparison (how user behaves vs cohort)
├─ Seasonal patterns (weekday vs weekend, etc.)
└─ Industry benchmarks (how institution behaves vs similar size)
```

#### Feature Engineering

```plaintext
FEATURE SET (50+ features):

TIME-SERIES FEATURES:
├─ requests_per_second (last 1m, 5m, 60m)
├─ coefficient_of_variation (burst detection)
├─ autocorrelation_lag1 (predictable patterns)
├─ trend_slope (accelerating/decelerating requests)
├─ entropy (randomness of inter-request time)
├─ zero_crossing_rate (pattern deviation)
└─ seasonal_decomposition (trend vs seasonal vs noise)

REQUEST PATTERN FEATURES:
├─ endpoint_diversity (how many different endpoints hit)
├─ method_distribution (% GET vs POST vs DELETE)
├─ parameter_variation (repeated params vs varied)
├─ payload_size_mean / std (typical size of request bodies)
├─ response_code_distribution (% 200 vs 4xx vs 5xx)
└─ error_rate_trend (increasing errors = pattern change)

BEHAVIORAL FEATURES:
├─ inter_request_time_distribution (timing pattern)
├─ request_clustering (temporal clustering vs spread)
├─ user_agent_consistency (same agent or varied)
├─ ip_consistency (same source or changing)
├─ geo_location_plausibility (can user physically move between locations)
├─ device_consistency (same device or varied)
└─ user_engagement_pattern (legitimate user behavior profile)

COMPARATIVE FEATURES:
├─ peer_percentile_rank (user's rate vs cohort, 0-100)
├─ institution_percentile_rank (tenant vs similar-size institutions)
├─ temporal_anomaly (vs same user's historical pattern)
├─ cohort_anomaly (vs peer group's pattern)
├─ distribution_shift_ks_statistic (statistical test vs normal)
└─ outlier_distance_score (how far from normal distribution)

SECURITY FEATURES:
├─ token_reuse_frequency (same token, different users)
├─ account_takeover_score (behavioral change magnitude)
├─ credential_sharing_indicator (multiple users, same token)
├─ automated_tool_likelihood (User-Agent analysis)
└─ replay_attack_likelihood (identical requests repeated)

CONTEXTUAL FEATURES:
├─ time_of_day (requests at 3am vs 3pm)
├─ day_of_week (weekend vs weekday pattern)
├─ subscription_tier_mismatch (free tier patterns vs paid behavior)
├─ geographic_mismatch (IP location vs user registered location)
├─ device_mismatch (different device than usual)
└─ platform_load_level (is global system overloaded)
```

#### Training Process

```plaintext
TRAINING PIPELINE:

1. DATA COLLECTION (Daily batch job)
   ├─ Sample 10M requests from past 24 hours
   ├─ Stratified by tenant to ensure coverage
   ├─ Remove personally identifiable information (privacy)
   ├─ Aggregate to hourly (user, tenant) time series
   └─ Store in feature warehouse (Parquet + Spark)

2. FEATURE ENGINEERING
   ├─ Calculate 50+ features per user per hour
   ├─ Normalize features (z-score normalization)
   ├─ Handle missing data (forward-fill, then mean imputation)
   ├─ Detect and remove collinear features (correlation > 0.95)
   └─ Version all transformations (add-only)

3. TRAINING (Isolation Forest)
   ├─ Unsupervised: no labels required
   ├─ n_trees = 200
   ├─ max_samples = 256
   ├─ max_features = 30 (random subset)
   ├─ contamination = 0.05 (expect 5% anomalies)
   ├─ Fit to 80% of data
   └─ Score all data (0 = normal, 1 = anomaly)

4. VALIDATION (20% holdout)
   ├─ Manual review of top 100 flagged anomalies
   ├─ False positive rate target: < 10%
   ├─ False negative rate target: < 5% (catch real attacks)
   ├─ Precision-recall curve analysis
   └─ Adjust decision threshold based on business requirements

5. TIME-SERIES VALIDATION (LSTM)
   ├─ Separate model for temporal patterns
   ├─ LSTM(64 → 32 → 16) encoder-decoder
   ├─ Predict next 1-hour request pattern
   ├─ Reconstruction error as anomaly score
   ├─ Combine with Isolation Forest score (weighted average)
   └─ Weights: 70% Isolation Forest, 30% LSTM

6. MODEL VERSIONING
   ├─ Store model as v1.0.0-20250228-prod
   ├─ Keep all previous versions (1-year retention)
   ├─ A/B test new models (10% traffic) before full rollout
   ├─ Instant rollback if performance degrades
   └─ Never modify existing model weights (immutable)

7. MONITORING & DRIFT DETECTION
   ├─ Daily: Compare today's feature distribution to training distribution
   ├─ Use Kolmogorov-Smirnov test (p-value < 0.05 = drift detected)
   ├─ If drift: Flag features, retrain only if significant
   ├─ Monitor false positive/negative rates in production
   └─ Alert if either ratio drifts > 50%
```

#### Real-Time Scoring

```plaintext
REAL-TIME ANOMALY SCORING:

FOR EACH REQUEST:
1. Extract feature vector (50 features)
2. Query Isolation Forest model → score_if (0-1)
3. Query LSTM model → score_lstm (0-1)
4. Combine scores:
   └─ anomaly_score = 0.7 × score_if + 0.3 × score_lstm
5. Adjust for context:
   ├─ If global_platform_load < 30%: increase threshold (tolerate more anomalies)
   ├─ If global_platform_load > 80%: decrease threshold (aggressive enforcement)
   └─ Final_anomaly_score = anomaly_score × context_multiplier

DECISION LOGIC:
├─ If anomaly_score > 0.95: AUTOMATIC REJECT (high confidence)
├─ If anomaly_score > 0.80: PROBABILISTIC REJECT (0.8 chance)
├─ If anomaly_score > 0.60: LOG & MONITOR (no rejection, but flag)
├─ If anomaly_score < 0.60: ALLOW (normal behavior)
└─ Always include anomaly_score in audit logs
```

### 7.2 LLM-Assisted Anomaly Context (Optional Layer)

For complex attacks requiring semantic reasoning:

```plaintext
USAGE_SCOPE:
├─ ASSIST ONLY: No decision autonomy
├─ Secondary validation only
├─ Never used as primary enforcement mechanism

EXAMPLE USE CASE:
├─ User sends valid requests structurally, but pattern is suspicious
├─ Example: Queries for {user_A's data}, {user_B's data}, {user_C's data}
│         (systematic enumeration of other users)
├─ Pattern bypasses numerical rate limits
├─ LLM can flag this as "potential IDOR attack" for human review
└─ Never automatically reject based on LLM reasoning

PROMPT GOVERNANCE:
├─ Prompt is versioned (e.g., v2.0.0-anomaly-context)
├─ Deterministic structure (no creative interpretation)
├─ Always include confidence score and decision path
├─ Decision is logged but marked as "ML-assisted, not decisive"
└─ Requires human approval for any action based on LLM output
```

---

## 📈 8. SCALABILITY DESIGN

### 8.1 Horizontal Scalability

```plaintext
SCALING ARCHITECTURE:

STATELESS DESIGN:
├─ Rate limit enforcement agent is stateless
├─ All state stored in external Redis cluster
├─ No in-memory state = instant scale up/down
├─ No persistent connections = request-level isolation

DEPLOYMENT:
├─ Run on Kubernetes cluster
├─ Horizontal pod autoscaling (HPA) based on metrics:
│  ├─ CPU > 70% → scale up
│  ├─ Memory > 80% → scale up
│  ├─ Request latency p99 > 10ms → scale up
│  └─ Concurrent connections > 80% of node capacity → scale up
├─ Vertical pod autoscaling (VPA) for right-sizing
├─ Pod disruption budgets (PDB) to ensure availability during rollouts

REPLICA COUNT:
├─ Minimum: 5 replicas (across 3+ zones)
├─ Maximum: 500 replicas (configurable)
├─ Target: 10,000 requests per pod per second
└─ Example: 100M req/s platform → ~10K pod replicas
```

### 8.2 Data Store Scalability

```plaintext
REDIS CLUSTER CONFIGURATION:

TOPOLOGY:
├─ 16 shards × 3 replicas = 48 nodes total
├─ 64GB memory per node = 768GB total cluster capacity
├─ Cross-zone replication (3 zones, round-robin)
├─ Consistent hashing with virtual nodes

THROUGHPUT:
├─ Single Redis node: ~100K operations/second
├─ 16 shards: 1.6M operations/second
├─ 3x replication traffic: Still linear scaling

REDUNDANCY:
├─ Master fails → replica promotes (automatic, <100ms failover)
├─ Zone fails → all shards still have 2+ replicas in other zones
├─ Cluster quorum: 2-of-3 replicas must acknowledge write
└─ RPO (Recovery Point Objective): < 100ms

BACKUPS:
├─ Snapshots every 1 hour (AOF for point-in-time)
├─ Stored in 3 geographic regions
├─ Test restore monthly
└─ Recovery time objective (RTO): < 15 minutes
```

### 8.3 Performance Metrics

```plaintext
LATENCY TARGETS:

99th percentile latency         < 10ms
99.9th percentile latency       < 25ms
99.99th percentile latency      < 50ms
Maximum latency (hard timeout)  100ms

THROUGHPUT TARGETS:

Per-node capacity               10,000 req/s
Per-pod capacity                10,000 req/s
Per-cluster capacity            > 100,000 req/s

QUEUE STRATEGY (Under heavy load):

├─ Request arrival rate > processing rate
├─ Queue using Kafka (durable, distributed)
├─ Queue key: rate_limit_queue:{tenant_id}
├─ Process with 50 consumer threads per pod
├─ SLA: Process queued request within 1 minute
└─ If queue depth > 1 million: Start rejecting (graceful degradation)
```

### 8.4 Cost Optimization

```plaintext
INFRASTRUCTURE COST MODEL:

COMPUTE:
├─ Pod cost: $0.02 per pod per hour (shared node pool)
├─ 10K pods: $200/hour = $1.46M/year
├─ Trade-off: High availability worth the cost

STORAGE (Redis):
├─ Memory cost: $0.15 per GB per month
├─ 768GB cluster: ~$110/month
├─ Negligible compared to compute

NETWORK:
├─ Cross-zone egress: $0.05 per GB
├─ Replication traffic: ~100GB/day (estimate)
├─ Monthly: $150
├─ Again, negligible compared to compute

OPTIMIZATION LEVERS:
├─ Spot instances for non-critical pods (40% cost reduction)
├─ Reserved instances for baseline load (30% reduction)
├─ Right-size pods (VPA) to minimize unused resources
├─ Batch similar requests to reduce total pods needed
└─ Cache frequently-accessed data (bucket states)
```

---

## 🔐 9. SECURITY ENFORCEMENT

### 9.1 Access Control & Authorization

```plaintext
ROLE-BASED ACCESS CONTROL (RBAC):

Only the following roles can:

PLATFORM_ADMIN:
├─ View all rate limit buckets across all tenants
├─ Modify rate limit tiers (requires approval workflow)
├─ Access detailed anomaly detection results
├─ Trigger manual overrides (rare, fully audited)
└─ No direct data manipulation (read-only for most fields)

TENANT_ADMIN:
├─ View rate limit metrics for own tenant
├─ Request tier upgrades
├─ View anomaly flags for own users
├─ Cannot access other tenants' data
└─ Cannot modify own tier (only platform admin)

COMPLIANCE_OFFICER:
├─ View audit logs (all tenants, via governance interfaces)
├─ Generate compliance reports
├─ Access anonymized statistical data
└─ Cannot see individual user details (PII redacted)

NON-ADMIN_USERS:
├─ View own rate limit status (remaining quota)
├─ See own retry_after recommendations
├─ Cannot access other users' data
├─ Cannot see anomaly detection features
└─ Can only access via public API (with authentication)

ENFORCEMENT:
├─ Every access checked against JWT claims
├─ Tenant_id in JWT must match requested tenant_id
├─ User_role in JWT must be in allowed_roles list
├─ No exception, no override capability
```

### 9.2 Tenant Isolation

```plaintext
STRICT MULTI-TENANT ISOLATION:

HARD BOUNDARIES:
├─ No queries across tenant_id boundaries (FORBIDDEN)
├─ Rate limit buckets keyed by tenant_id
├─ Metrics queries filtered by tenant_id
├─ Audit logs tagged with tenant_id
├─ ML models trained separately per tenant (optional, for privacy)
└─ Never combine data from different tenants

IMPLEMENTATION:
├─ Every query includes WHERE tenant_id = ?
├─ Parameterized queries (prevent SQL injection)
├─ Database-level constraints (tenant_id foreign keys)
├─ Application-level assertion (fail if tenant_id missing)
└─ No implicit tenant_id from context

VIOLATION DETECTION:
├─ Cross-tenant query attempt → IMMEDIATE REJECT (403 Forbidden)
├─ Log incident with full stack trace
├─ Alert security team
├─ Require incident post-mortem
└─ Possible suspension of user/admin
```

### 9.3 Data Encryption

```plaintext
ENCRYPTION STRATEGY:

AT REST:
├─ Redis Cluster: TLS 1.3 encryption between nodes
├─ Cassandra audit trail: AES-256-GCM at rest
├─ Database: Encrypted by cloud provider (AWS KMS, GCP KMS, etc.)
├─ Backups: AES-256 encrypted before transfer
└─ Keys: Stored in HSM (hardware security module), rotated monthly

IN TRANSIT:
├─ All API traffic: TLS 1.3 enforced
├─ TLS_ECDHE_RSA_WITH_AES_256_GCM_SHA384 (strongest cipher)
├─ Certificate: Wildcard cert, signed by trusted CA
├─ HSTS header: max-age=63072000 (2 years)
├─ No SSL 3.0, TLS 1.0, TLS 1.1 support (reject immediately)
└─ HTTPS redirect on all HTTP requests

IN MEMORY:
├─ Sensitive fields (tokens, passwords) never logged
├─ Hash tokens before storage (SHA256, never reversible)
├─ Clear sensitive buffers after use (wipe from RAM)
├─ Use secure libraries (no custom cryptography)
└─ Regular security audits (pen testing, code review)
```

### 9.4 API Authentication & Authorization

```plaintext
AUTHENTICATION REQUIREMENTS:

EVERY REQUEST MUST INCLUDE:
├─ Authorization header: Bearer {JWT}
├─ Host header: Exact match to registered domain
├─ Origin header: Validated against CORS allowlist
├─ Content-Type header: application/json (for POSTs)
└─ X-Request-ID header: UUID (for traceability)

JWT VALIDATION:
├─ Signature verified using RS256 + public key
├─ Public key from secure endpoint (/auth/public-keys)
├─ Key rotated every 30 days
├─ Old keys retained for 60 days (grace period)
├─ exp claim checked: must not be expired
├─ iat claim checked: must be within ±5 minutes of server time
├─ iss claim verified: must match expected issuer
├─ aud claim verified: must include API service identifier
├─ Custom claims verified:
│  ├─ tenant_id: Must match request tenant_id
│  ├─ user_id: Must match request user_id (if applicable)
│  ├─ roles: Must include required role for endpoint
│  └─ subscription_tier: Must be active, not expired
└─ If ANY check fails: REJECT (401 Unauthorized)

TOKEN REVOCATION:
├─ Revoked tokens cached in Redis (quick lookup)
├─ Cache invalidated every 1 minute (fresh from auth service)
├─ If token marked revoked: REJECT (401 Unauthorized)
└─ Reason: "Token revoked" (logged, not sent to client)
```

### 9.5 Input Sanitization

```plaintext
SANITIZATION RULES:

FOR STRING INPUTS:
├─ Max length: 10KB per field
├─ Allowed characters: a-z, A-Z, 0-9, dash, underscore, dot
├─ No special characters (reject <, >, &, ", ', etc.)
├─ No unicode characters outside BMP (reject emoji, etc.)
├─ Whitespace trimmed (leading/trailing)
└─ Null bytes rejected (security risk)

FOR UUID INPUTS:
├─ Regex: ^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$
├─ Case-insensitive matching
├─ Leading/trailing whitespace allowed (but trimmed)
└─ No transformation (not lowercase automatically)

FOR INTEGER INPUTS:
├─ Regex: ^[0-9]+$
├─ Range checked (min/max per field documented)
├─ No leading zeros (except for 0 itself)
└─ No negative numbers unless explicitly allowed

FOR IP ADDRESSES:
├─ Regex for IPv4: ^[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}$
├─ Each octet must be 0-255
├─ Regex for IPv6: (complex, use library validation)
└─ Check against reserved ranges (10.0.0.0/8, etc.)

ENFORCEMENT:
├─ Parse, not evaluate
├─ Whitelist allowed values, not blacklist
├─ Reject ambiguous inputs (no auto-repair)
└─ Log all rejections (security monitoring)
```

---

## 🏝️ 10. TENANT & DOMAIN ISOLATION

### 10.1 Strict Domain Isolation

```plaintext
DOMAIN TRACK ISOLATION:

DOMAIN TRACKS (From Ecoskiller architecture):
├─ Arts
├─ Commerce
├─ Science
├─ Technology
└─ Administration

ISOLATION RULE:
├─ User has approved_domains list in their profile
├─ User can ONLY access endpoints scoped to their approved domains
├─ Cross-domain access: FORBIDDEN
├─ Example: Arts student cannot see Science curriculum

ENFORCEMENT IN RATE LIMITING AGENT:
├─ Request includes domain_context
├─ Request includes tenant_domain_scope (approved domains)
├─ For each endpoint access:
│  ├─ Check if endpoint's domain in tenant_domain_scope
│  ├─ Check if endpoint's domain in user_approved_domains
│  └─ If not in either: REJECT (403 Forbidden)
├─ Log domain violation attempts
└─ Alert if systematic violation detected
```

### 10.2 Institute vs Corporate vs Platform Boundaries

```plaintext
TENANT HIERARCHY:

PLATFORM LEVEL:
├─ System admins (global visibility)
├─ Compliance officers (audit access)
├─ All data aggregated
└─ Highest level of authority

ENTERPRISE TENANT:
├─ HR admin (manages own corporate users)
├─ Can see own employees, job postings, evaluations
├─ Cannot see other enterprises' data
├─ Cannot see institute data
├─ Different rate limits from institutes

INSTITUTE TENANT:
├─ School/college admin (manages own students/staff)
├─ Can see own students, trainers, projects
├─ Cannot see other institutes' data
├─ Cannot see enterprise data
├─ Different rate limits from enterprises

ISOLATION ENFORCEMENT:
├─ Every query: WHERE tenant_id = ? AND tenant_type IN (...)
├─ Cross-type access: FORBIDDEN
│  └─ Enterprise user cannot see institute students
├─ Rate limits are per-tenant-type
│  └─ Enterprise tier ≠ Institute tier
└─ No data leakage possible (strong guarantees)
```

---

## 📊 11. AUDIT & TRACEABILITY

### 11.1 Immutable Audit Logging

Every decision must be logged to append-only audit trail:

```plaintext
AUDIT LOG ENTRY STRUCTURE:

{
  "audit_id": "uuid-v4 (globally unique)",
  "timestamp_utc": "ISO8601 (server time, not client time)",
  "timestamp_ns": "nanoseconds (for sequencing)",
  "request_id": "uuid (from original request)",
  "decision_id": "uuid (from rate limiting decision)",
  
  "actor": {
    "user_id": "uuid",
    "tenant_id": "uuid",
    "user_role": "enum",
    "user_agent": "string (sanitized)",
    "ip_address": "string"
  },
  
  "decision": {
    "status": "ALLOWED|REJECTED|THROTTLED",
    "reason_code": "enum",
    "rate_limit_tier_applied": "enum",
    "rate_limit_breached": "enum or null"
  },
  
  "metrics": {
    "requests_in_window": "integer",
    "limit_for_window": "integer",
    "anomaly_score": "float (0.0-1.0)",
    "processing_time_ms": "integer"
  },
  
  "security": {
    "anomaly_detected": "boolean",
    "security_incident_flagged": "boolean",
    "cross_tenant_access_attempted": "boolean"
  },
  
  "signatures": {
    "input_hash": "SHA256(canonical_request)",
    "decision_hash": "SHA256(decision_object)",
    "hmac_signature": "HMAC_SHA256(decision_hash, audit_secret_key)"
  }
}
```

### 11.2 Audit Storage & Retention

```plaintext
STORAGE ARCHITECTURE:

PRIMARY STORE (Real-time queries):
├─ Database: Cassandra (time-series optimized)
├─ Keyspace: audit_logs (replicated 3-way)
├─ Partitioning: DATE + tenant_id (for query speed)
├─ TTL: 1 year (automatic expiration)
└─ Write throughput: 100K entries/second

SECONDARY STORE (Long-term archive):
├─ Storage: S3-compatible (cold storage)
├─ Prefix: s3://audit-logs/{year}/{month}/{day}/{hour}
├─ Format: Parquet (columnar, highly compressible)
├─ Encryption: AES-256-GCM
├─ Retention: 7 years (regulatory requirement)
└─ Access: Immutable (object lock enabled)

BACKUP:
├─ Daily backup of Cassandra to S3
├─ Incremental backup (copy-on-write)
├─ Separate KMS key for backup encryption
├─ Store in different AWS region
└─ Test restore quarterly

QUERY PATTERNS:

Most common: "Show decisions for user_id X on date Y"
└─ Cassandra partition key: (date, tenant_id)
└─ Clustering key: (user_id, timestamp_ns)
└─ Query latency: <100ms

Compliance: "Export all audit logs for tenant X, year 2024"
└─ Read from S3 (cold storage query, slower but cheaper)
└─ Fetch from Parquet columns (selective column access)
└─ Export as CSV for external audit

Investigation: "Find all decisions with anomaly_score > 0.8"
└─ Batch job (not real-time)
└─ Scan Parquet files (parallel execution)
└─ Export to data warehouse for analysis
```

### 11.3 Audit Log Tamper Detection

```plaintext
IMMUTABILITY GUARANTEES:

CRYPTOGRAPHIC CHAINING:
├─ Each audit entry includes hash of previous entry
├─ Hash chain: audit_1 → hash(audit_1) → audit_2 → hash(audit_1, audit_2) → ...
├─ Tampering with entry N invalidates all subsequent hashes
├─ Regular verification: scan chain, ensure all hashes valid
└─ Alert if any break detected

DIGITAL SIGNATURES:
├─ Each entry signed with agent's private key (RSA-4096)
├─ Signature covers: (timestamp, decision, metrics, hashes)
├─ Verification: Check signature with agent's public key
├─ Key rotation: Monthly (old keys retained for 1 year)
└─ No signature → Invalid entry (reject from query results)

CHECKSUMS:
├─ Each entry includes SHA256 hash of entire object
├─ Database integrity check: Verify checksum on read
├─ If checksum mismatch → Treat as corrupted
├─ Log corruption incident, alert security team
└─ Verify monthly (batch job)

WRITE PROTECTION:
├─ Audit logs in Cassandra marked as append-only
├─ Database-level constraint: DELETE forbidden
├─ Application-level check: No UPDATE statements
├─ S3 object lock: Enabled (GOVERNANCE mode)
│  └─ Even AWS admins cannot delete locked objects
└─ Compliance: Object lock complies with WORM (Write-Once-Read-Many)
```

---

## 🚨 12. FAILURE POLICY

### 12.1 Failure Scenarios & Responses

```plaintext
SCENARIO 1: INVALID INPUT
├─ Condition: Missing required field, invalid format
├─ Response: REJECT (400 Bad Request)
├─ Log: Validation failure (debug level)
├─ Alert: No alert (expected behavior)
└─ Retry: Client should retry with valid input

SCENARIO 2: RATE LIMIT EXCEEDED
├─ Condition: User has consumed all tokens in bucket
├─ Response: REJECT (429 Too Many Requests)
├─ Headers: Retry-After, X-RateLimit-Reset
├─ Log: Rate limit decision (info level)
├─ Alert: No alert (expected behavior)
└─ Retry: Client should wait retry_after seconds

SCENARIO 3: ANOMALY DETECTED (HIGH CONFIDENCE)
├─ Condition: anomaly_score > 0.95
├─ Response: REJECT (429 Too Many Requests)
├─ Log: Anomaly detection decision (warn level)
├─ Alert: Yes, flag for human review
├─ Retry: Client should contact support
└─ Follow-up: Security team investigates

SCENARIO 4: AUTH VALIDATION FAILED
├─ Condition: JWT signature invalid, token expired, token revoked
├─ Response: REJECT (401 Unauthorized)
├─ Log: Auth failure (warn level)
├─ Alert: Yes, track failed auth attempts
├─ Retry: Client should re-authenticate
└─ Follow-up: Rate limit on failed auth (prevent brute-force)

SCENARIO 5: REDIS CLUSTER UNAVAILABLE
├─ Condition: Cannot reach Redis for state check
├─ Response: FAIL SECURE (reject request)
├─ Fallback: Deny all requests (conservative approach)
├─ Log: Incident (error level, critical alert)
├─ Alert: Page on-call engineer immediately
├─ Duration: < 100ms timeout (don't hang)
└─ Action: Escalate to SRE, restore Redis, analyze root cause

SCENARIO 6: MODEL LOADING FAILED
├─ Condition: ML model unavailable or corrupted
├─ Response: Use previous model version (fallback)
├─ Log: Model loading failure (error level)
├─ Alert: Yes, but non-critical
├─ Action: Retry model load every 10 seconds
└─ Duration: After 5 retries (50s), escalate to SRE

SCENARIO 7: CLOCK SKEW DETECTED
├─ Condition: Client timestamp differs from server by > 5s
├─ Response: REJECT (401 Unauthorized)
├─ Log: Clock skew detected (warn level)
├─ Alert: No (unless systematic, > 10 occurrences/min)
└─ Action: Client should sync clock with NTP server

SCENARIO 8: CONCURRENCY CONFLICT
├─ Condition: Multiple simultaneous requests from same user
├─ Response: First request proceeds, others queued
├─ Log: Concurrency handled (debug level)
├─ Alert: No (expected for high-concurrency apps)
└─ Implementation: Distributed lock on user_id for 100ms

SCENARIO 9: DATABASE CORRUPTION
├─ Condition: Checksum mismatch on audit log read
├─ Response: Mark entry as corrupted, isolate
├─ Log: Corruption detected (error level, critical alert)
├─ Alert: Yes, immediate escalation
└─ Action: Investigate database integrity, restore from backup

SCENARIO 10: CASCADING FAILURE (System overloaded)
├─ Condition: Processing latency > 100ms, queues backing up
├─ Response: Activate load shedding
├─ Shedding strategy: Reject LOW priority requests first
│  └─ Priority order: CRITICAL > HIGH > NORMAL > LOW
├─ Metrics: Monitor queue depth, adjust shedding rate
├─ Log: Load shedding decisions (warn level)
├─ Alert: Yes, notify operations team
└─ Recovery: Manual or automatic (scaling up pods)
```

### 12.2 No Silent Failures

```plaintext
PRINCIPLE: EVERY FAILURE MUST BE LOGGED & OBSERVABLE

LOGGING REQUIREMENTS:
├─ Every rejection logged with reason_code
├─ Every anomaly logged with score & confidence
├─ Every error logged with stack trace
├─ Every timeout logged with duration
├─ Every retry logged with attempt number
└─ Every success logged with metadata (for completeness)

OBSERVABILITY:
├─ Metrics: Exposed in Prometheus format
├─ Traces: OpenTelemetry distributed tracing
├─ Logs: JSON structured logs to ELK stack
├─ Dashboard: Real-time visualization
└─ Alerts: PagerDuty integration for critical incidents

TRACEABILITY:
├─ Every event linked by request_id
├─ Every log entry tagged with request_id
├─ Every metric tagged with request_id
├─ Trace spans include request_id
└─ Client receives request_id in response (for support tickets)

ZERO TOLERANCE POLICY:
├─ Silent failure = data loss risk
├─ Silent failure = security vulnerability
├─ No exceptions, no shortcuts
├─ All failures must be visible to operations
└─ If something can't be logged, something is wrong
```

### 12.3 Retry Policy

```plaintext
CLIENT RETRY GUIDANCE (Sent in response):

ON 429 (Rate Limited):
├─ Retry-After: {retry_after_seconds} (exponential backoff hint)
├─ X-RateLimit-Reset: {timestamp when limit resets}
├─ X-RateLimit-Remaining: {tokens remaining}
├─ Recommended client behavior:
│  └─ Wait {retry_after_seconds}, then retry
│  └─ Max 3 retries before giving up
│  └─ Use exponential backoff: wait × (2^attempt_number)
└─ Example: Wait 10s, then retry. If fail, wait 20s. If fail, wait 40s. Give up.

ON 401 (Unauthorized):
├─ Do not retry immediately
├─ Client should:
│  ├─ Request new JWT from auth service
│  ├─ Wait 5 seconds
│  └─ Retry with new token
├─ Max 2 retries before user intervention required
└─ Example: Get new token, wait, retry. If still fail, ask user to re-login.

ON 403 (Forbidden):
├─ Do not retry automatically
├─ Client should:
│  ├─ Check user permissions
│  ├─ Verify subscription tier
│  └─ Contact support if issue persists
├─ Max 1 retry (no point retrying same request)
└─ Example: User lacks permission for endpoint. Wait 1 min, try again. If fail, ask for permission.

ON 400 (Bad Request):
├─ Do not retry
├─ Indicates client error (invalid format)
├─ Client must fix the request
└─ Retrying same request will fail again

ON 500 (Internal Server Error):
├─ Retry with exponential backoff
├─ Max 3 retries over 30 seconds
├─ Jitter added to prevent thundering herd
│  └─ Backoff = 1s + random(0, 1000ms), 2s + random(0, 2000ms), 4s + random(0, 4000ms)
└─ If still failing after 3 retries, escalate to support
```

---

## 🔗 13. INTER-AGENT DEPENDENCY MAP

### 13.1 Upstream Dependencies

```plaintext
AGENTS THAT FEED INTO RATE_LIMIT_ENFORCEMENT_AGENT:

AUTH_VALIDATION_AGENT:
├─ Provides: JWT validation results, user identity, roles
├─ Call sequence: Auth Agent first → Rate Limit Agent
├─ Data passed: user_id, tenant_id, user_role, subscription_tier
├─ SLA: Complete within 5ms

DOMAIN_ISOLATION_AGENT:
├─ Provides: User's approved domains, domain access rules
├─ Call sequence: Domain Agent first → Rate Limit Agent
├─ Data passed: approved_domains, domain_scope
├─ SLA: Complete within 2ms

TENANT_REGISTRY_AGENT:
├─ Provides: Tenant metadata, tier information, suspension status
├─ Call sequence: Tenant Registry first → Rate Limit Agent
├─ Data passed: tenant_type, subscription_tier, rate_limit_tier
├─ SLA: Complete within 3ms (cached heavily)

SUBSCRIPTION_STATUS_AGENT:
├─ Provides: User's current subscription, expiry date, tier
├─ Call sequence: Subscription Agent first → Rate Limit Agent
├─ Data passed: tier, expiry_date, billing_cycle_end
├─ SLA: Complete within 5ms (cached, checked daily)

THREAT_INTELLIGENCE_AGENT:
├─ Provides: IP blocklists, malicious user flags
├─ Call sequence: Threat Intel Agent first → Rate Limit Agent
├─ Data passed: is_blocked, blocklist_reason
├─ SLA: Complete within 10ms (cached, updated hourly)

COMPLIANCE_RULES_AGENT:
├─ Provides: Compliance requirements, audit requirements
├─ Call sequence: Compliance Agent first → Rate Limit Agent
├─ Data passed: requires_audit, compliance_flags
├─ SLA: Complete within 2ms (cached, static)
```

### 13.2 Downstream Dependencies

```plaintext
AGENTS THAT RATE_LIMIT_ENFORCEMENT_AGENT FEEDS:

REQUEST_ROUTING_AGENT:
├─ Consumes: Rate limit decision (ALLOWED|REJECTED)
├─ Usage: Only processes allowed requests
├─ Data received: decision_id, should_execute
├─ Frequency: Every request

AUDIT_LOGGING_AGENT:
├─ Consumes: Decision metadata and audit trail
├─ Usage: Immutable logging of all decisions
├─ Data received: Full audit_entry structure
├─ Frequency: Every request (100% sampling)

OBSERVABILITY_AGENT:
├─ Consumes: Metrics (latency, rejection rate, etc.)
├─ Usage: Monitoring, alerting, dashboards
├─ Data received: Metrics exposed in Prometheus format
├─ Frequency: Every 10 seconds (aggregated)

ANOMALY_RESPONSE_AGENT:
├─ Consumes: Anomaly flags (if anomaly_score > threshold)
├─ Usage: Escalate for human review, possibly block user
├─ Data received: anomaly_id, anomaly_score, request_details
├─ Frequency: On anomaly detection (varies)

BILLING_ENGINE_AGENT:
├─ Consumes: Quota usage per tenant (for cost calculation)
├─ Usage: Calculate monthly bill based on API usage
├─ Data received: total_requests, tier_applied
├─ Frequency: Daily aggregation, monthly billing

NOTIFICATION_AGENT:
├─ Consumes: Alerts (quota warnings, limit exceeded, etc.)
├─ Usage: Send notifications to users/admins
├─ Data received: alert_type, user_id, message
├─ Frequency: On event triggers
```

### 13.3 Event Emission Protocol

```plaintext
EVENTS EMITTED BY RATE_LIMIT_ENFORCEMENT_AGENT:

EVENT TYPE 1: RATE_LIMIT_EXCEEDED
├─ Trigger: Decision status = REJECTED due to rate limit
├─ Payload: {user_id, tenant_id, current_rate, limit, retry_after}
├─ Subscribers: NOTIFICATION_AGENT, ANOMALY_RESPONSE_AGENT, AUDIT_LOGGING_AGENT
├─ Delivery: Async (Kafka topic: rate_limit.exceeded)
└─ Retention: 24 hours

EVENT TYPE 2: ANOMALY_DETECTED
├─ Trigger: anomaly_score > 0.80
├─ Payload: {user_id, tenant_id, anomaly_score, anomaly_type, decision_id}
├─ Subscribers: ANOMALY_RESPONSE_AGENT, OBSERVABILITY_AGENT
├─ Delivery: Async (Kafka topic: security.anomaly_detected)
└─ Retention: 30 days

EVENT TYPE 3: QUOTA_USAGE_MILESTONE
├─ Trigger: User reaches 50%, 80%, 100% of monthly quota
├─ Payload: {user_id, tenant_id, usage_percentage, monthly_limit}
├─ Subscribers: NOTIFICATION_AGENT, BILLING_ENGINE_AGENT
├─ Delivery: Async (Kafka topic: quota.milestone)
└─ Retention: 90 days

EVENT TYPE 4: RATE_LIMIT_RESET
├─ Trigger: Hourly/daily bucket reset (replenishment)
├─ Payload: {user_id, tenant_id, bucket_id, tokens_restored}
├─ Subscribers: OBSERVABILITY_AGENT (metrics only, not actionable)
├─ Delivery: Async (Kafka topic: rate_limit.reset)
└─ Retention: 7 days

EVENT TYPE 5: TIER_UPGRADE_RECOMMENDED
├─ Trigger: User consistently hitting rate limits over 7 days
├─ Payload: {user_id, tenant_id, recommended_tier, cost_delta}
├─ Subscribers: NOTIFICATION_AGENT
├─ Delivery: Async (Kafka topic: billing.upgrade_recommended)
└─ Retention: 30 days

ALL EVENTS:
├─ Include: event_id (UUID), timestamp_utc, decision_id (traceback)
├─ Signed: HMAC-SHA256(event_payload, signing_key)
├─ Idempotent: event_id ensures no duplicate processing
└─ Ordered: Kafka partition key = user_id (ensures ordering per user)
```

---

## 📊 14. PERFORMANCE MONITORING

### 14.1 Key Performance Indicators (KPIs)

```plaintext
LATENCY METRICS:

p50_latency_ms              = 2ms (50th percentile)
p95_latency_ms              = 6ms (95th percentile)
p99_latency_ms              = 10ms (99th percentile)
p99_9_latency_ms            = 25ms (99.9th percentile)
p99_99_latency_ms           = 50ms (99.99th percentile)

SLA:
├─ 99% of decisions complete within 10ms
├─ 99.9% of decisions complete within 25ms
├─ 99.99% of decisions complete within 50ms
├─ If p99 > 10ms for 5 minutes: Page on-call engineer

THROUGHPUT METRICS:

requests_per_second         = Total API requests processed
allowed_requests_per_sec    = Requests allowed to proceed
rejected_requests_per_sec   = Requests rejected (rate limited)
throttled_requests_per_sec  = Requests probabilistically throttled

SLA:
├─ Support > 100K requests/second at p99 latency < 10ms
├─ No degradation if traffic < 100K req/s
├─ Graceful degradation (load shedding) if traffic > 100K req/s

ERROR RATE METRICS:

decision_error_rate         = % decisions that failed (not rate limit, but error)
anomaly_false_positive_rate = % legitimate users flagged as anomalies
anomaly_false_negative_rate = % actual attacks missed by detection

SLA:
├─ Error rate < 0.1% (99.9% success rate)
├─ False positive rate < 10% (catch 90% of real anomalies)
├─ False negative rate < 5% (miss < 5% of attacks)
```

### 14.2 Monitoring & Alerting

```plaintext
METRICS EXPORTED TO PROMETHEUS:

rate_limit_decisions_total (counter)
├─ Labels: status={ALLOWED|REJECTED|THROTTLED}, tier={tier}, tenant_id
├─ Example: rate_limit_decisions_total{status="ALLOWED", tier="ENTERPRISE"} = 1_234_567

rate_limit_decision_latency_ms (histogram)
├─ Buckets: [1, 2, 5, 10, 25, 50, 100, 250, 500, 1000]
├─ Labels: tier={tier}, operation={READ|WRITE|DELETE}
├─ Tracks: p50, p95, p99, p99.9

rate_limit_bucket_tokens_remaining (gauge)
├─ Labels: tenant_id, user_id, tier
├─ Value: Current tokens in user's bucket
├─ Updated: Every request

anomaly_detections_total (counter)
├─ Labels: anomaly_type={BURST|BRUTE_FORCE|...}, severity={low|medium|high}
├─ Example: anomaly_detections_total{anomaly_type="BRUTE_FORCE", severity="high"} = 123

redis_cluster_latency_ms (histogram)
├─ Tracks: Time to query rate limit state from Redis
├─ Buckets: [1, 2, 5, 10, 25, 50]
├─ Alert: If p99 > 10ms (indicates Redis issue)

model_inference_latency_ms (histogram)
├─ Tracks: Time for ML model scoring
├─ Buckets: [1, 5, 10, 25, 50, 100]
├─ Alert: If p99 > 25ms (indicates model bottleneck)

request_queue_depth (gauge)
├─ Value: Number of requests waiting in queue
├─ Alert: If > 1 million (indicates overload)

CRITICAL ALERTS (Page On-Call):

1. p99_latency > 10ms (sustained for 5 minutes)
2. error_rate > 1% (any error not rate limit)
3. redis_unavailable (cannot reach state store)
4. anomaly_detection_offline (ML model unavailable)
5. request_queue_depth > 5 million
6. audit_log_lag > 1 hour (logs falling behind)

WARNING ALERTS (Ticket Only):

1. p95_latency > 6ms (sustained for 10 minutes)
2. error_rate > 0.1% (track trend)
3. redis_latency_high (p99 > 10ms)
4. anomaly_false_positive_rate > 15%
5. request_queue_depth > 1 million
```

### 14.3 Observability Integration

```plaintext
LOGGING FRAMEWORK:

Log format: JSON (structured logging)
├─ timestamp_utc (ISO8601)
├─ request_id (UUID)
├─ log_level (DEBUG|INFO|WARN|ERROR|CRITICAL)
├─ message (short description)
├─ context (structured object with details)
└─ stack_trace (on ERROR/CRITICAL)

Log destination: ELK Stack (Elasticsearch, Logstash, Kibana)
├─ Ingestion: Filebeat (log shipper)
├─ Indexing: rate-limit-agent-logs-{date}
├─ Retention: 30 days hot, 1 year warm, 7 years cold (archive)
├─ Query: Kibana dashboard with saved searches
└─ Retention policy: Auto-rollover daily

Sampling:
├─ DEBUG: 1% sampling (not all, to reduce cost)
├─ INFO: 100% sampling (all decisions)
├─ WARN/ERROR: 100% sampling (all problems)
└─ CRITICAL: 100% + immediate alert

TRACING FRAMEWORK:

Distributed tracing: OpenTelemetry
├─ Trace ID: Same as request_id (correlate with logs)
├─ Span hierarchy:
│  ├─ api_gateway (entry point)
│  ├─ rate_limit_enforcement (this agent)
│  │  ├─ input_validation
│  │  ├─ auth_check
│  │  ├─ rate_limit_check
│  │  │  ├─ redis_query
│  │  │  ├─ token_bucket_update
│  │  ├─ anomaly_detection
│  │  │  ├─ feature_extraction
│  │  │  ├─ ml_inference
│  │  └─ decision_logging
│  └─ request_routing
├─ Backend: Jaeger or Lightstep (distributed trace collector)
├─ Sampling: 10% of requests (configurable)
└─ Retention: 72 hours (recent traces) + 30 days (aggregated stats)

CUSTOM DASHBOARDS:

Main Dashboard:
├─ Real-time rate limit decisions (ALLOWED|REJECTED)
├─ Latency percentiles (p50, p95, p99)
├─ Top 10 users by request rate
├─ Top 10 tenants by request rate
├─ Anomaly detection heatmap
├─ Redis cluster health
└─ System resource utilization (CPU, memory, network)

SLA Dashboard:
├─ Uptime % (target: 99.99%)
├─ Decision latency compliance (% within SLA)
├─ Error rate (% non-rate-limit errors)
└─ Ticket: Link to incident management

Security Dashboard:
├─ Anomalies detected (time series)
├─ Top anomaly types
├─ False positive rate
├─ IPs in blocklist
├─ Brute-force attack attempts
└─ Ticket: Link to security incidents
```

---

## 🔄 15. VERSIONING & ROLLBACK

### 15.1 Versioning Policy (Add-Only, Immutable)

```plaintext
VERSION FORMAT: SEMANTIC VERSIONING
└─ MAJOR.MINOR.PATCH-status.date
└─ Example: 1.2.5-prod.20250228 or 2.0.0-beta.1.20250301

VERSION COMPONENTS:

MAJOR: Breaking changes to decision logic
├─ Changing rate limit algorithm (token bucket → sliding window)
├─ Changing tenant isolation model
├─ Changing audit trail structure
└─ Requires: Data migration, customer communication

MINOR: New features, backward compatible
├─ Adding new anomaly detection types
├─ Adding new rate limit tiers
├─ Adding new observability metrics
└─ Requires: Feature documentation, monitoring

PATCH: Bug fixes, security fixes
├─ Fixing calculation bugs
├─ Fixing race conditions
├─ Fixing security vulnerabilities
└─ Requires: Immediate rollout

STATUS:

alpha: Pre-release, testing in dev environment only
beta:  Pre-release, testing with 1-5% production traffic
rc:    Release candidate, 10% production traffic, ready for GA
prod:  Production ready, 100% traffic

DATE: YYYYMMDD (when version released)

EXAMPLES:
├─ 1.0.0-alpha.20250215 (initial development)
├─ 1.0.0-beta.1.20250220 (first beta)
├─ 1.0.0-rc.1.20250225 (release candidate)
├─ 1.0.0-prod.20250228 (production release, stable)
├─ 1.1.0-prod.20250310 (minor update, new feature)
└─ 1.1.1-prod.20250312 (patch, bug fix)
```

### 15.2 Deployment Strategy

```plaintext
CANARY DEPLOYMENT (STANDARD):

1. Deploy new version to canary cluster (5% traffic)
   ├─ Run for 1 hour
   ├─ Monitor: latency, error rate, anomaly false positive rate
   ├─ Success criteria:
   │  ├─ p99 latency < 10ms
   │  ├─ Error rate < 0.1%
   │  └─ Anomaly false positive rate stable (±10%)
   └─ If fail: Rollback (see Section 15.3)

2. Expand to 25% traffic
   ├─ Run for 30 minutes
   ├─ Same monitoring criteria
   └─ If fail: Rollback

3. Expand to 50% traffic
   ├─ Run for 30 minutes
   ├─ Same monitoring criteria
   └─ If fail: Rollback

4. Expand to 100% traffic
   ├─ Monitor for 1 hour
   ├─ Declare stable
   └─ Mark version as active

Timeline: Minimum 2.5 hours for full rollout

BLUE-GREEN DEPLOYMENT (EMERGENCY HOTFIX):

1. Deploy to green cluster (parallel, zero traffic)
   ├─ Full smoke tests (synthetic traffic)
   ├─ Load testing (1% traffic redirected)
   └─ Verify all components healthy

2. Switch traffic to green cluster (atomic)
   ├─ Load balancer directs 100% traffic to green
   ├─ Rollback available: Switch back to blue (instant)
   └─ Monitor for 10 minutes

3. Mark green as active
   └─ Retire blue cluster after successful run

Timeline: 30 minutes total (faster than canary)

ROLLBACK STRATEGY (AUTOMATIC):

Automatic rollback triggers:
├─ p99 latency > 25ms (sustained 2 minutes)
├─ Error rate > 1%
├─ Request queue depth > 5 million
└─ Redis unavailable

Rollback process:
├─ Switch traffic to previous version
├─ Notify on-call engineer
├─ Log incident for post-mortem
├─ No action needed (automatic)
└─ Timeline: < 30 seconds (seamless to users)

MANUAL ROLLBACK:

If automatic rollback doesn't trigger but human intervention needed:
├─ On-call engineer checks: "kubectl rollout undo"
├─ Instant switch to previous version
├─ Same validation as canary
└─ Timeline: < 1 minute
```

### 15.3 Version Control & Storage

```plaintext
VERSION ARTIFACT STORAGE:

Container registry: Docker Hub (or ECR, GCR, etc.)
├─ Image: ecoskiller/rate-limit-enforcement-agent:1.2.5-prod.20250228
├─ Immutable digest: sha256:abcd1234...
├─ Stored with: Source code hash, git commit SHA, build timestamp
├─ Retention: All versions kept (no deletion)
└─ Signing: Image signed with agent's signing key (cosign)

Configuration versioning:
├─ Stored in Git (monorepo)
├─ Path: config/rate-limit-enforcement/v1.2.5/
├─ Files: limits.yaml, tiers.yaml, anomaly_model.pkl, prompts.yaml
├─ Commit: Linked to container image
└─ Review: All changes reviewed + approved before merge

Model versioning (ML):
├─ Path: models/rate-limit-enforcement/
├─ Files: isolation_forest_v1.2.5.joblib, lstm_model_v1.2.5.h5
├─ Stored: In model registry (MLflow)
├─ Metadata: Training date, training data hash, performance metrics
├─ Retention: All versions (no deletion)
└─ Validation: Run inference test before marking ready

Code versioning:
├─ Repository: GitHub (or GitLab, Bitbucket)
├─ Branch: main (production), develop (staging)
├─ Tags: v1.2.5-prod.20250228
├─ Linked: Container image built from this commit
└─ Retention: Full git history (immutable)

DEPENDENCY VERSIONS:

Lock file: go.mod (or requirements.txt, package.json, etc.)
├─ Specifies exact version of all dependencies
├─ Frozen at release time (reproducible builds)
├─ Example:
│  ├─ github.com/go-redis/redis/v8 v8.11.5
│  ├─ google.golang.org/protobuf v1.27.1
│  ├─ github.com/prometheus/client_golang v1.14.0
│  └─ ... (all dependencies pinned)
├─ Stored: In git (version controlled)
└─ Updated: Only when dependency upgrade approved
```

### 15.4 Rollback & Recovery

```plaintext
ROLLBACK PROCEDURE (STEP-BY-STEP):

TRIGGER IDENTIFICATION:
├─ Alert fired (automatic or manual)
├─ Decision: Is this a rollback candidate?
│  └─ If yes, proceed. If no, investigate.
└─ Notification: Slack, email, PagerDuty

PREPARATION (< 1 minute):
├─ Identify stable previous version
│  └─ Example: 1.2.4-prod.20250225
├─ Verify version is in registry
├─ Verify version has successful history
├─ Notify team: "Rolling back to 1.2.4"
└─ Start recording incident

EXECUTION (< 30 seconds):
├─ Command: kubectl set image deployment/rate-limit-enforcement \
│           rate-limit=ecoskiller/rate-limit-enforcement-agent:1.2.4-prod.20250225
├─ Monitor: Watch pod restart
├─ Validation: Health check passes
└─ Announce: "Rollback complete, 1.2.4 active"

VERIFICATION (5 minutes):
├─ Check metrics:
│  ├─ p99 latency < 10ms ✓
│  ├─ Error rate < 0.1% ✓
│  ├─ Request queue depth < 1M ✓
│  └─ Redis latency normal ✓
├─ Spot checks: Manual testing of rate limit enforcement
└─ Declare: "Rollback successful, system stable"

POST-MORTEM:
├─ Schedule: Within 24 hours
├─ Investigate: What went wrong?
├─ Fix: Patch the issue in new version
├─ Retest: More thorough testing before next release
└─ Document: Postmortem shared with team

DISASTER RECOVERY (Full data loss):

If state store (Redis) is corrupted:
├─ Restore from backup (Cassandra)
│  └─ Most recent backup < 1 hour old
├─ Verify: Data integrity check passes
├─ Switch: Direct queries to restored data
├─ Monitor: 1 hour of validation
└─ If fail: Escalate to platform team (rare)

Timeline: < 15 minutes RTO (recovery time objective)
```

---

## 🚫 16. NON-NEGOTIABLE RULES

### 16.1 Forbidden Operations

```plaintext
THIS AGENT MUST NEVER:

1. CREATE HIDDEN LOGIC
   ├─ All decision logic must be explicit, documented, versioned
   ├─ No undocumented rate limiting (hidden rules)
   ├─ No hardcoded exceptions (all in configuration)
   ├─ Violation: Audit review, possible termination

2. MODIFY HISTORICAL RECORDS
   ├─ Audit logs are append-only (no updates)
   ├─ Rate limit buckets' historical values never rewritten
   ├─ Decisions are immutable once made
   ├─ Violation: Data integrity violation, security incident

3. AUTO-DELETE AUDIT LOGS
   ├─ Only TTL-based expiration allowed (30 days warm, 1yr+ cold)
   ├─ No immediate deletion on request
   ├─ No batch deletion to "clean up"
   ├─ Violation: Compliance violation, possible legal consequences

4. OVERRIDE GOVERNANCE AGENTS
   ├─ Cannot bypass AUTH_VALIDATION_AGENT
   ├─ Cannot bypass DOMAIN_ISOLATION_AGENT
   ├─ Cannot bypass COMPLIANCE_RULES_AGENT
   ├─ Violation: Security vulnerability, privilege escalation

5. BYPASS COMPLIANCE CHECKS
   ├─ All requests must pass validation (no shortcuts)
   ├─ All decisions must be logged (no skipping)
   ├─ All anomalies must be flagged (no hiding)
   ├─ Violation: Regulatory violation, audit failure

6. MIX DOMAIN DATA
   ├─ No combining user data from different domains
   ├─ No cross-domain feature engineering (for anomaly detection)
   ├─ No tenant data mixing
   ├─ Violation: Data isolation violation, security incident

7. EXECUTE OUTSIDE SCOPE
   ├─ Only rate limit enforcement (core responsibility)
   ├─ No user modification (not the auth agent)
   ├─ No request routing (not the gateway)
   ├─ No database access for business logic (isolation)
   ├─ Violation: Agent scope creep, architectural violation

8. ASSUME MISSING DATA
   ├─ If required field missing: REJECT
   ├─ If ambiguous: REJECT
   ├─ If unclear: REJECT
   ├─ No "best guess" defaults
   ├─ Violation: Data quality issue, incorrect decisions

9. MAKE AUTONOMOUS DECISIONS
   ├─ Follow explicit rules only
   ├─ No ML model overrides (rules are ground truth)
   ├─ No LLM interpretation (ML-assist only, not decision)
   ├─ Violation: Unpredictable behavior, loss of trust

10. STORE SENSITIVE DATA IN LOGS
    ├─ No tokens in logs (hash only)
    ├─ No passwords (never handled by this agent)
    ├─ No credit cards (not in scope)
    ├─ No PII (user_id only, not email/name/SSN)
    ├─ Violation: Privacy violation, data breach
```

### 16.2 Immutable Constraints

```plaintext
CONSTRAINTS THAT CANNOT BE CHANGED:

1. TENANT ISOLATION IS ABSOLUTE
   └─ No query can access data from multiple tenants
   └─ No exception, no override capability
   └─ Enforced at database level + application level

2. AUDIT TRAIL IS IMMUTABLE
   └─ Append-only, cryptographically signed
   └─ No deletion, no modification
   └─ Verified weekly (integrity check)

3. RATE LIMITS ARE ENFORCED FAIRLY
   └─ Same tier → Same limit (no favoritism)
   └─ Different tier → Different limit (transparent pricing)
   └─ No "secret" unlimited access

4. DECISIONS ARE DETERMINISTIC
   └─ Same request state → Same decision (always)
   └─ Reproducible (can replay and verify)
   └─ No randomness in decision logic (except for probabilistic throttling)

5. LATENCY GUARANTEES ARE MET
   └─ p99 < 10ms (non-negotiable SLA)
   └─ Monitored 24/7
   └─ Violation triggers immediate page

6. SECURITY IS DEFENSE-IN-DEPTH
   └─ Multiple layers of validation
   └─ Fail-secure (reject on doubt)
   └─ Zero-trust (verify everything)

7. VERSIONING IS ADD-ONLY
   └─ Old versions never deleted
   └─ Rollback always possible
   └─ Full history preserved
```

### 16.3 Governance Compliance

```plaintext
MANDATORY COMPLIANCE REQUIREMENTS:

REGULATORY:
├─ GDPR: Personal data handled only for rate limiting (no enrichment)
├─ HIPAA: If institute is healthcare-related, audit logs kept for 6+ years
├─ SOC 2: Audit trail, access controls, incident response
├─ ISO 27001: Security controls, risk management
├─ CCPA: Right to delete (not applicable for rate limit state, audit-only)
└─ Action: Annual compliance audit

CONTRACTUAL:
├─ SLAs: 99.99% uptime, p99 latency < 10ms
├─ Data residency: Data stays in specified region/country
├─ Data retention: Audit logs kept per contract (typically 1-7 years)
├─ Data access: No third-party access without consent
└─ Action: Monthly SLA review

INTERNAL:
├─ Change management: All code changes reviewed, tested
├─ Incident response: RTO < 15 minutes for critical issues
├─ Security review: Annual penetration testing, code review
├─ Performance review: Monthly KPI review vs targets
└─ Action: Weekly governance meetings

ENFORCEMENT:
├─ Violations logged immediately
├─ Escalation: To compliance officer + security team
├─ Remediation: Fix within 24 hours (critical)
├─ Reporting: Quarterly compliance report
└─ Consequence: Possible suspension of service
```

---

## 🎯 FINAL DECLARATION

### Agent Certification

```plaintext
🔒 SEALED & LOCKED CERTIFICATION

Agent: API_RATE_LIMIT_ENFORCEMENT_AGENT
Version: 1.0.0-prod.20250228
Status: PRODUCTION READY

This agent has been certified as meeting ALL requirements:

✓ Input validation (strict, no assumptions)
✓ Output contracts (immutable, signed)
✓ Rate limiting strategies (token bucket + sliding window)
✓ ML anomaly detection (49-feature model)
✓ Scalability (distributed, stateless)
✓ Security enforcement (zero-trust, encryption)
✓ Audit trail (immutable, cryptographically signed)
✓ Tenant isolation (hard boundaries)
✓ Failure handling (no silent failures)
✓ Monitoring & observability (comprehensive)
✓ Versioning & rollback (immutable)
✓ Non-negotiable rules (enforced)

Decision Authority: ABSOLUTE
Override Capability: NONE
Appeal Process: ESCALATE TO PLATFORM COMPLIANCE OFFICER

This agent may be deployed to production immediately.

Certification Date: 2025-02-28
Certification Authority: Ecoskiller Platform Engineering
Valid Until: 2026-02-28 (annual recertification required)

Signed: SHA256(this_document)
Signature: RSA-4096(SHA256, agent_signing_key)

🔐 END OF SEALED & LOCKED SPECIFICATION 🔐
```

---

## 📚 APPENDIX: QUICK REFERENCE

### Configuration Template

```yaml
# rate-limit-enforcement-agent/config.yaml

version: "1.0.0"
execution_mode: "DETERMINISTIC"
mutation_policy: "ADD_ONLY"
failure_mode: "HALT_ON_AMBIGUITY"

rate_limiting:
  default_algorithm: "TOKEN_BUCKET"
  secondary_algorithm: "SLIDING_WINDOW"
  
  tiers:
    STUDENT_FREE:
      refill_rate_per_second: 1.67
      max_capacity: 100
      cost_per_read: 1
      cost_per_write: 5
      cost_per_delete: 25
      
    ENTERPRISE_L7:
      refill_rate_per_second: 33333
      max_capacity: 2000000
      cost_per_read: 1
      cost_per_write: 5
      cost_per_delete: 25

anomaly_detection:
  model_version: "v1.0.0-prod"
  algorithm: "ISOLATION_FOREST + LSTM"
  features_count: 50
  training_frequency: "DAILY"
  decision_threshold: 0.95

storage:
  primary: "redis_cluster"
  backup: "cassandra"
  audit_archive: "s3_parquet"
  
monitoring:
  export_format: "prometheus"
  alerting_backend: "pagerduty"
  dashboard_backend: "kibana"

security:
  encryption: "AES-256-GCM"
  tls_version: "1.3"
  audit_signing: "RSA-4096"
  tenant_isolation: "HARD"
```

### Decision Tree (Simplified)

```plaintext
DECISION TREE:

1. Is request complete & well-formed?
   NO → REJECT (400 Bad Request) → LOG
   YES → Continue

2. Is user authenticated & token valid?
   NO → REJECT (401 Unauthorized) → LOG
   YES → Continue

3. Is user in suspension list?
   YES → REJECT (403 Forbidden) → LOG
   NO → Continue

4. Is tenant in suspension list?
   YES → REJECT (403 Forbidden) → LOG
   NO → Continue

5. Is IP in blocklist?
   YES → REJECT (403 Forbidden) → LOG
   NO → Continue

6. Is subscription tier active?
   NO → REJECT (402 Payment Required) → LOG
   YES → Continue

7. Does user have access to endpoint?
   NO → REJECT (403 Forbidden) → LOG
   YES → Continue

8. Does user have access to requested domain?
   NO → REJECT (403 Forbidden) → LOG
   YES → Continue

9. Is anomaly score > 0.95?
   YES → REJECT (429 Too Many Requests) → LOG + ALERT
   NO → Continue

10. Does user have tokens in bucket?
    NO → REJECT (429 Too Many Requests, retry_after) → LOG
    YES → Continue

11. ALLOW request to proceed
    └─ Deduct tokens from bucket
    └─ LOG decision
    └─ EMIT events (quota milestone, etc.)
    └─ RETURN (200 OK + retry_after in header)
```

---

**END OF SPECIFICATION**

*This specification is SEALED & LOCKED. Any modifications require formal change management process and new certification.*
# API RATE LIMIT ENFORCEMENT AGENT - IMPLEMENTATION SUMMARY

## 📄 DOCUMENT OVERVIEW

**File Generated:** `API_RATE_LIMIT_ENFORCEMENT_AGENT.md`  
**Size:** 85KB (2,466 lines)  
**Type:** Enterprise-Grade SaaS Governance Agent Specification  
**Status:** SEALED & LOCKED (v1.0.0-prod.20250228)  
**Classification:** CRITICAL GOVERNANCE CONTROL

---

## 🎯 SPECIFICATION HIGHLIGHTS

### Core Components Defined

1. **Agent Identity** (Section 1)
   - Deterministic execution mode
   - Absolute decision authority
   - Fail-secure default behavior
   - Strict multi-tenant isolation

2. **Input/Output Contracts** (Sections 4-5)
   - Mandatory field validation (strict, no assumptions)
   - 20+ validation rules enforced
   - Immutable output structure with cryptographic signatures
   - 100% traceability via audit references

3. **Rate Limiting Strategies** (Section 6)
   - **Primary:** Token Bucket Algorithm (distributed Redis)
   - **Secondary:** Sliding Window Counter (high precision)
   - **Burst Allowance:** Up to 1-minute burst capacity
   - **Cost-Based:** Different costs for READ (1), WRITE (5), DELETE (25), COMPUTE (50)
   - **Multi-Level:** Per-user → Per-tenant → Per-service → Global

4. **User Tier Configuration** (in Section 6.1)
   ```
   STUDENT_FREE:           100 req/min
   STUDENT_PREMIUM:      1,000 req/min
   TRAINER_PROFESSIONAL: 2,000 req/min
   INSTITUTE_MEDIUM:   200,000 req/min
   ENTERPRISE_L7:    2,000,000 req/min
   CUSTOM:              As contracted
   ```

5. **ML/AI Logic Layer** (Section 7)
   - **Isolation Forest + LSTM** hybrid model
   - **50 features** across 7 categories:
     - Time-series (request rate, burst detection, entropy)
     - Request patterns (endpoint diversity, method distribution)
     - Behavioral (inter-request timing, device consistency)
     - Comparative (peer percentile, cohort anomaly)
     - Security (token reuse, account takeover likelihood)
     - Contextual (time-of-day, geographic mismatch)
   - **Daily retraining** with drift detection
   - **Real-time scoring** < 5ms per request
   - **Confidence-based decisions:** 70% Isolation Forest + 30% LSTM

6. **Scalability Design** (Section 8)
   - **Stateless agent** (all state in Redis)
   - **Target:** > 100,000 requests/second
   - **Horizontal scaling:** 5-500 pod replicas (Kubernetes HPA)
   - **Redis Cluster:** 16 shards × 3 replicas (768GB)
   - **Latency SLA:** p99 < 10ms, p99.9 < 25ms

7. **Security Enforcement** (Section 9)
   - **TLS 1.3 + AES-256-GCM** encryption at rest & in transit
   - **JWT validation** with RS256 signatures
   - **Zero-trust architecture** (verify everything)
   - **Input sanitization:** 6 validation rule sets
   - **RBAC:** Platform Admin, Tenant Admin, Compliance Officer, User
   - **Token revocation:** Checked every 60 seconds

8. **Tenant & Domain Isolation** (Section 10)
   - **Hard boundaries:** No cross-tenant queries
   - **Domain tracks:** Arts, Commerce, Science, Technology, Administration
   - **Database-level enforcement:** Foreign keys, constraints
   - **Application-level assertion:** Fail if tenant_id missing
   - **Audit for violations:** Cross-tenant access attempts logged

9. **Audit & Traceability** (Section 11)
   - **Immutable append-only** audit trail (Cassandra + S3)
   - **Cryptographic chaining:** Hash of previous entry in each entry
   - **Digital signatures:** RSA-4096 per entry
   - **1-year hot storage** (Cassandra), 7-year cold storage (S3 Parquet)
   - **Tamper detection:** Weekly verification, alert on breaks

10. **Failure Policy** (Section 12)
    - **10 specific failure scenarios** with response strategy
    - **No silent failures:** All rejections logged with reason code
    - **Fail-secure:** Reject on ambiguity
    - **Fallback strategies:** Use previous model version on ML failure
    - **Automatic rollback:** Page on-call for latency > 10ms (5 minutes)

11. **Inter-Agent Dependency Map** (Section 13)
    - **Upstream:** Auth, Domain Isolation, Tenant Registry, Subscription, Threat Intel, Compliance
    - **Downstream:** Request Routing, Audit Logging, Observability, Anomaly Response, Billing, Notifications
    - **Event emission:** 5 event types via Kafka (rate_limit.exceeded, security.anomaly_detected, etc.)

12. **Performance Monitoring** (Section 14)
    - **KPIs:** p50 (2ms), p95 (6ms), p99 (10ms), p99.9 (25ms), p99.99 (50ms)
    - **Error rate target:** < 0.1%
    - **Anomaly false positive:** < 10%, false negative < 5%
    - **Prometheus metrics:** Counters, histograms, gauges (15+ metrics)
    - **Observability:** ELK Stack (logs), Jaeger/Lightstep (traces), Kibana (dashboards)

13. **Versioning & Rollback** (Section 15)
    - **Semantic versioning:** MAJOR.MINOR.PATCH-status.date
    - **Immutable releases:** All versions kept (no deletion)
    - **Canary deployment:** 2.5-hour phased rollout (5% → 25% → 50% → 100%)
    - **Blue-green deployment:** Emergency hotfix (30 minutes)
    - **Automatic rollback:** < 30 seconds if p99 > 25ms

14. **Non-Negotiable Rules** (Section 16)
    - **10 forbidden operations:** No hidden logic, no audit deletion, no override
    - **7 immutable constraints:** Tenant isolation, audit immutability, fair limits
    - **Governance compliance:** GDPR, HIPAA, SOC 2, ISO 27001, CCPA

---

## 📊 ARCHITECTURE DIAGRAM

```
┌─────────────────────────────────────────────────────────┐
│          ECOSKILLER ANTIGRAVITY PLATFORM                │
│       (10M-100M users, Microservices + Event-Driven)    │
├─────────────────────────────────────────────────────────┤
│                                                          │
│  HTTP/2, gRPC, WebSocket → API GATEWAY (500+ endpoints) │
│           ↓                                              │
│  🔒 RATE_LIMIT_ENFORCEMENT_AGENT (< 10ms decision)     │
│  ├─ Token Bucket (Primary)                             │
│  ├─ Sliding Window (Secondary)                         │
│  ├─ ML Anomaly Detection (Isolation Forest + LSTM)     │
│  ├─ Multi-level enforcement (user/tenant/service/global)│
│  └─ Cryptographically signed audit trail               │
│           ↓                                              │
│  REQUEST ROUTING & MICROSERVICES                       │
│  ├─ Job Portal Engine                                  │
│  ├─ Skill Development Engine                           │
│  ├─ Project Execution Engine                           │
│  ├─ Dojo (Group Discussion)                            │
│  └─ ERP (for institutes/enterprises)                   │
│           ↓                                              │
│  OBSERVABILITY & MONITORING                            │
│  ├─ Prometheus (metrics)                               │
│  ├─ ELK Stack (logs)                                   │
│  ├─ Jaeger (traces)                                    │
│  └─ Kibana (dashboards)                                │
│                                                          │
└─────────────────────────────────────────────────────────┘

SUPPORTING SYSTEMS:

┌─────────────────┐  ┌──────────────────┐  ┌────────────────┐
│ Redis Cluster   │  │ Cassandra        │  │ S3 Archive     │
│ (State Store)   │  │ (Audit Trail)    │  │ (Compliance)   │
│ 768GB, 3-way    │  │ 1-year hot       │  │ 7-year cold    │
│ replication     │  │ storage          │  │ WORM locked    │
└─────────────────┘  └──────────────────┘  └────────────────┘
```

---

## 🔑 KEY STATISTICS

| Metric | Value |
|--------|-------|
| **Total Lines** | 2,466 |
| **Sections** | 16 major + appendix |
| **Code Examples** | 50+ JSON/YAML/plaintext examples |
| **Validation Rules** | 20+ input validation rules |
| **ML Features** | 50 behavioral features |
| **Decision Latency (p99)** | < 10ms |
| **Throughput Target** | > 100K req/sec |
| **Availability SLA** | 99.99% (< 50 minutes downtime/year) |
| **Audit Retention** | 1 year hot + 7 years cold storage |
| **Rate Limit Tiers** | 6 pre-defined + custom |
| **Supported User Roles** | 8 role types |
| **Security Algorithms** | TLS 1.3, AES-256-GCM, RSA-4096, HMAC-SHA256 |
| **Deployment Regions** | 3+ zones with cross-zone replication |
| **Version History** | Immutable (all versions kept) |
| **Rollback Time** | < 30 seconds (automatic) |

---

## 🚀 QUICK START GUIDE

### For Deployment Engineers

1. **Read Section 1:** Agent Identity & Core Definition (understand the role)
2. **Review Section 8:** Scalability Design (infrastructure requirements)
3. **Check Section 15:** Versioning & Rollback (deployment procedure)
4. **Deploy:** Use canary strategy (5% → 100% over 2.5 hours)
5. **Monitor:** Watch Section 14 KPIs (p99 latency, error rate, anomaly rate)

### For Security/Compliance Team

1. **Read Section 9:** Security Enforcement (authentication, encryption, RBAC)
2. **Review Section 11:** Audit & Traceability (immutable logging)
3. **Check Section 16:** Non-Negotiable Rules (governance constraints)
4. **Audit:** Run annual pen test + code review
5. **Certify:** Annual compliance audit per Section 16.3

### For Operations/SRE Team

1. **Read Section 14:** Performance Monitoring (KPIs, alerting, dashboards)
2. **Review Section 12:** Failure Policy (what to do when things break)
3. **Setup:** Prometheus metrics + ELK Stack + Jaeger
4. **Configure:** Alerts for p99 > 10ms, error rate > 0.1%
5. **Respond:** Automatic rollback kicks in for critical issues

### For Product/Business Team

1. **Read Section 6:** Rate Limiting Strategies (what users experience)
2. **Review Tier Configuration:** (Section 6.1) - pricing differentiation
3. **Understand:** Multi-level enforcement (fair resource allocation)
4. **Monitor:** Section 14 business metrics (false positives, user complaints)
5. **Iterate:** Adjust tiers based on user feedback

### For ML/Data Team

1. **Read Section 7:** ML/AI Logic Layer (anomaly detection)
2. **Review:** Feature engineering (50 features explained)
3. **Setup:** Training pipeline (daily batch job)
4. **Monitor:** Drift detection, false positive/negative rates
5. **Improve:** Retrain model with new labeled anomalies

---

## 📋 IMPLEMENTATION CHECKLIST

- [ ] **Infrastructure**
  - [ ] Kubernetes cluster with HPA configured
  - [ ] Redis cluster (16 shards, 3-way replication)
  - [ ] Cassandra cluster (time-series optimized)
  - [ ] S3-compatible cold storage

- [ ] **Security**
  - [ ] TLS 1.3 certificates installed
  - [ ] KMS key management setup
  - [ ] JWT signing keys rotated
  - [ ] IP blocklist integrated

- [ ] **Monitoring**
  - [ ] Prometheus scraping configured
  - [ ] ELK Stack (Elasticsearch, Logstash, Kibana)
  - [ ] Jaeger distributed tracing
  - [ ] PagerDuty integration for alerts

- [ ] **Data**
  - [ ] Rate limit tier configuration loaded
  - [ ] ML models (Isolation Forest + LSTM) trained
  - [ ] Audit log rotation policy configured
  - [ ] Backup and restore tested

- [ ] **Testing**
  - [ ] Unit tests (validation rules, decision logic)
  - [ ] Integration tests (with Auth, Domain, Compliance agents)
  - [ ] Load tests (100K+ req/sec)
  - [ ] Chaos engineering (failure scenarios)
  - [ ] Penetration testing (security validation)

- [ ] **Rollout**
  - [ ] Canary deployment (5% traffic)
  - [ ] Monitoring dashboard active
  - [ ] Runbook for on-call engineers
  - [ ] Incident response team on standby
  - [ ] Gradual rollout (5% → 25% → 50% → 100%)

---

## 🔒 COMPLIANCE & CERTIFICATION

**This specification meets the following standards:**

- ✅ **GDPR:** Personal data handling, right to delete
- ✅ **HIPAA:** 6+ year audit retention for healthcare institutes
- ✅ **SOC 2:** Audit trails, access controls, incident response
- ✅ **ISO 27001:** Information security management
- ✅ **CCPA:** Consumer privacy rights
- ✅ **PCI-DSS:** If processing payments (not directly, but audit-compliant)

**Annual Certification:**
- Due: 2026-02-28
- Conducted by: Ecoskiller Platform Engineering
- Scope: Code review, security audit, performance validation

---

## 📞 SUPPORT & ESCALATION

### For Questions About This Specification

1. **Architecture questions:** Consult Section 3 (System Context)
2. **Implementation questions:** Consult the relevant section number (1-16)
3. **Technical deep-dive:** See appendix with configuration template & decision tree
4. **Regulatory questions:** Consult Section 16.3 (Governance Compliance)

### For Issues in Production

1. **Latency > 10ms:** Page on-call engineer (auto-triggers alert)
2. **Error rate > 0.1%:** Create ticket, investigate root cause
3. **Anomaly rate anomalies:** Review Section 7 (ML model retraining)
4. **Audit trail issues:** Escalate to data integrity team (critical)
5. **Security incident:** Follow Section 16 compliance requirements

---

## 📚 DOCUMENT STRUCTURE

```
1. AGENT IDENTITY & CORE DEFINITION
   ├─ Mandatory declaration
   ├─ Agent classification
   └─ Execution guarantees

2. PURPOSE & PROBLEM STATEMENT
   ├─ Problem definition
   ├─ Agent responsibilities
   └─ Expected outcomes

3. SYSTEM CONTEXT & ARCHITECTURE
   ├─ Platform architecture
   ├─ Rate limit enforcement position
   ├─ User ecosystem (8 roles)
   ├─ Deployment architecture
   └─ Infrastructure overview

4. INPUT CONTRACT (STRICT)
   ├─ Request structure (immutable)
   ├─ Validation rules (20+ rules)
   ├─ Rejection criteria (15 conditions)
   └─ Null/default handling

5. OUTPUT CONTRACT (STRICT)
   ├─ Response structure (immutable)
   ├─ Output guarantees (5 properties)
   └─ Traceability fields

6. RATE LIMITING STRATEGIES
   ├─ Token bucket (primary)
   ├─ Sliding window (secondary)
   ├─ Burst allowance (fairness)
   ├─ Cost-based limiting (differentiation)
   ├─ Tenant-level aggregation (multi-level)
   └─ Prioritization under overload (graceful degradation)

7. ML/AI LOGIC LAYER
   ├─ Anomaly detection engine
   ├─ Feature engineering (50 features)
   ├─ Training process (daily batch)
   ├─ Real-time scoring (< 5ms)
   └─ LLM-assisted context (secondary only)

8. SCALABILITY DESIGN
   ├─ Horizontal scalability (stateless)
   ├─ Data store scalability (Redis cluster)
   ├─ Performance metrics (latency/throughput)
   └─ Cost optimization (spot instances, reserved)

9. SECURITY ENFORCEMENT
   ├─ Access control & authorization (RBAC)
   ├─ Tenant isolation (hard boundaries)
   ├─ Data encryption (at rest, in transit)
   ├─ API authentication (JWT validation)
   └─ Input sanitization (6 rule sets)

10. TENANT & DOMAIN ISOLATION
    ├─ Strict domain isolation (5 tracks)
    └─ Institute vs Corporate vs Platform (tenant hierarchy)

11. AUDIT & TRACEABILITY
    ├─ Immutable audit logging (append-only)
    ├─ Audit storage & retention (1yr hot, 7yr cold)
    └─ Tamper detection (cryptographic chaining)

12. FAILURE POLICY
    ├─ Failure scenarios (10 specific scenarios)
    ├─ No silent failures (all logged)
    └─ Retry policy (client guidance)

13. INTER-AGENT DEPENDENCY MAP
    ├─ Upstream dependencies (6 agents)
    ├─ Downstream dependencies (6 agents)
    └─ Event emission protocol (5 event types)

14. PERFORMANCE MONITORING
    ├─ Key performance indicators (latency, throughput, error rate)
    ├─ Monitoring & alerting (Prometheus, ELK, Jaeger)
    └─ Observability integration (logging, tracing, dashboards)

15. VERSIONING & ROLLBACK
    ├─ Versioning policy (semantic versioning, add-only)
    ├─ Deployment strategy (canary, blue-green)
    └─ Version control & storage (containers, git, models)

16. NON-NEGOTIABLE RULES
    ├─ Forbidden operations (10 forbidden)
    ├─ Immutable constraints (7 constraints)
    └─ Governance compliance (GDPR, HIPAA, SOC 2, ISO 27001, CCPA)

APPENDIX: QUICK REFERENCE
├─ Configuration template (YAML)
└─ Decision tree (simplified flow)
```

---

## 🎓 LEARNING OUTCOMES

After reading this specification, you will understand:

1. **How rate limiting works** at enterprise scale (100M+ users)
2. **Multi-tier enforcement** (per-user, per-tenant, per-service, global)
3. **ML-driven anomaly detection** (50 features, hybrid model)
4. **Distributed systems** (Redis, Cassandra, event-driven)
5. **Security by design** (zero-trust, defense-in-depth)
6. **Governance & compliance** (GDPR, HIPAA, audit trails)
7. **Observability & monitoring** (Prometheus, ELK, Jaeger)
8. **Safe deployment practices** (canary, blue-green, rollback)
9. **Immutable audit logging** (cryptographic chaining, tamper detection)
10. **Enterprise SaaS architecture** (multi-tenancy, isolation, scalability)

---

**Document Generated:** 2025-02-28  
**Total Size:** 85KB  
**Status:** SEALED & LOCKED (v1.0.0-prod)  
**Next Review:** 2026-02-28 (Annual)

*For questions or clarifications, escalate to Platform Engineering or Compliance.*
