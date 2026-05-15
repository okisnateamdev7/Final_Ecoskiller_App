# 🔒 API_RATE_LIMIT_ENFORCEMENT_AGENT
## Governance & Core Control Agent for Ecoskiller Antigravity Platform

**Status:** 🔐 SEALED · LOCKED · GOVERNED · DETERMINISTIC  
**Version:** 1.0.0  
**Mutation Policy:** Add-only via version bump  
**Interpretation Authority:** NONE  
**Execution Authority:** Human declaration only  
**Platform:** Ecoskiller Antigravity SaaS  
**Effective Date:** 2025-02-28  
**Last Updated:** 2025-02-28  

---

## 📋 TABLE OF CONTENTS

1. [Executive Summary](#executive-summary)
2. [Agent Identity (Mandatory)](#agent-identity-mandatory)
3. [Purpose Declaration](#purpose-declaration)
4. [Input Contract (Strict)](#input-contract-strict)
5. [Output Contract (Strict)](#output-contract-strict)
6. [Rate Limiting Algorithms](#rate-limiting-algorithms)
7. [ML/AI Logic Layer](#mlai-logic-layer)
8. [Scalability Design](#scalability-design)
9. [Security Enforcement](#security-enforcement)
10. [Audit & Traceability](#audit--traceability)
11. [Failure Policy](#failure-policy)
12. [Inter-Agent Dependency Map](#inter-agent-dependency-map)
13. [Passive Intelligence Compatibility](#passive-intelligence-compatibility)
14. [Growth Engine Hook](#growth-engine-hook)
15. [Performance Monitoring](#performance-monitoring)
16. [Versioning Policy](#versioning-policy)
17. [Non-Negotiable Rules](#non-negotiable-rules)
18. [Sealed Execution Contract](#sealed-execution-contract)

---

## EXECUTIVE SUMMARY

The **API_RATE_LIMIT_ENFORCEMENT_AGENT** is an enterprise-grade autonomous system agent operating within the Ecoskiller Antigravity multi-tenant SaaS platform. This agent serves as the **API Gateway Governance & Control** layer responsible for enforcing rate limits, preventing API abuse, protecting infrastructure, and ensuring fair resource allocation across 10M–100M users spanning 8 distinct user roles and 5 domains.

**Key Characteristics:**
- Real-time rate limit enforcement (microsecond decisions)
- Distributed, stateless, horizontally scalable
- Zero-trust multi-tenant isolation
- Anomaly-based adaptive rate limiting
- Complete audit trail of all rate limit events
- Deterministic, reproducible decisions
- Failure-halt model (block suspicious requests)

**Operating Context:**
- Scale Target: 10M–100M users
- Expected RPS: 100K–1M requests per second at platform peak
- ML Usage: 70–80% traditional ML (anomaly detection, traffic patterns)
- AI Usage: 20–30% LLM (abuse pattern explanation, policy recommendation)
- Architecture: Microservices + Event-Driven
- Security Model: Zero-trust isolation per tenant + domain + user role

---

## 1️⃣ AGENT IDENTITY (MANDATORY)

### 1.1 Core Identity Declaration

```
AGENT_NAME = API_RATE_LIMIT_ENFORCEMENT_AGENT
AGENT_ID = ARLEA-CORE-001
SYSTEM_ROLE = API Gateway Governance & Control Layer
PRIMARY_DOMAIN = API Rate Limit Enforcement + Traffic Control
EXECUTION_MODE = Deterministic + Validated (ultra-low latency)
DATA_SCOPE = All API requests across platform
TENANT_SCOPE = Strict isolation per tenant + domain + user_role
FAILURE_POLICY = Block request on ambiguity + Log incident + Escalate
LATENCY_REQUIREMENT = < 5ms (P99), P50 < 1ms
IMMUTABILITY_REQUIREMENT = Append-only audit trail (no retroactive modification)
```

### 1.2 Agent Classification

**Type:** API Gateway Control Agent  
**Category:** Cross-functional rate limiting + traffic control  
**Responsibility Tier:** Tier-0 (Critical infrastructure, inline in request path)  
**Operational Mode:** Always-on, synchronous inline (no async allowed)  
**Restart Behavior:** Stateless recovery (shared state in Redis only)  
**Request Blocking:** Yes (primary enforcement mechanism)  

### 1.3 Authority & Constraints

- **Can:** Enforce rate limits, block requests, log traffic, escalate abusers, dynamically adjust limits
- **Cannot:** Modify historical logs, delete rate limit records, bypass security checks, override tenant isolation
- **Must:** Never assume missing actor information
- **Must:** Never create hidden logic or undocumented rate limit rules
- **Must:** Enforce strict tenant + domain + role isolation
- **Must:** Maintain deterministic behavior (same actor, same limits, same time = same decision)
- **Must:** Provide clear, auditable reason for every block

---

## 2️⃣ PURPOSE DECLARATION

### 2.1 Problem Statement

In a multi-tenant SaaS ecosystem serving 10M–100M users with 8 distinct roles and 5 domains, API abuse is inevitable:

1. **Legitimate Concerns:**
   - Denial of Service (DoS) attacks (single malicious actor overwhelming system)
   - Distributed Denial of Service (DDoS) attacks (coordinated attack from multiple sources)
   - Resource exhaustion (runaway processes, infinite loops, batch operations)
   - Unfair resource contention (one tenant consuming resources meant for others)
   - Cost overruns (premium features consumed beyond license)
   - Accidental abuse (buggy client code hammering API)

2. **Current Challenges:**
   - Static rate limits don't adapt to real traffic patterns
   - Per-user limits don't account for coordinated abuse
   - Cross-tenant isolation leaks (one tenant's abuse affecting others)
   - No real-time visibility into abusive patterns
   - Slow response to emerging threats (manual intervention required)

3. **Rate Limiting Goals:**
   - Protect platform infrastructure (prevent overload)
   - Ensure fair resource allocation (prevent tenant/user hoarding)
   - Detect and block malicious patterns (anomaly detection)
   - Enable legitimate high-volume use cases (dynamic adaptation)
   - Provide operator control (policy override, emergency mode)
   - Maintain complete audit trail (compliance, forensics)

### 2.2 What This Agent Solves

The **API_RATE_LIMIT_ENFORCEMENT_AGENT** solves the critical problem of **enforcing intelligent, adaptive rate limits** across a complex multi-tenant ecosystem where:

- Users span 8 distinct roles with different quota requirements
- Tenants span 5 domains with isolated resources
- Legitimate use cases range from single-request queries to bulk 100K-item operations
- Attack patterns evolve faster than manual rules can adapt
- Every decision impacts user experience and platform stability
- Every decision must be auditable and reproducible

### 2.3 Input Consumption

**Consumes:**
- API request metadata (actor_id, tenant_id, domain_id, endpoint, method, timestamp)
- Actor history (previous requests, patterns, abuse history)
- Tenant quota (license level, API tier, usage to date)
- System state (current load, available capacity, emergency mode)
- Threat intelligence (known abusers, attack signatures, DDoS patterns)
- Behavioral baselines (normal traffic patterns per actor/tenant)

**Sources:**
- API Gateway (all incoming requests)
- Actor Registry (user/agent metadata)
- Tenant Configuration (quota, limits, settings)
- Historical traffic patterns (ML feature store)
- Threat intelligence feeds (external + internal)
- System monitoring (CPU, memory, disk, network)

### 2.4 Output Production

**Produces:**
- Rate limit decision (ALLOW with cost, BACKPRESSURE, REJECT with reason)
- Usage event (request consumed X cost against quota)
- Anomaly alert (unusual traffic pattern detected)
- Abuse notification (actor exceeding limits, escalation needed)
- Quota exceeded notice (tenant approaching/exceeding license)
- Rate limit headers (client-facing: X-RateLimit-Remaining, X-RateLimit-Reset)
- Audit log entry (complete request context, decision, reason, enforcement action)
- Metrics event (RPS, latency, error rate, by tenant/actor/endpoint)

**Consumers:**
- API Gateway (request allow/block decision)
- Notification Agent (user/admin alerts)
- Observability Agent (metrics, dashboards, alerts)
- Audit/Compliance Agent (complete audit trail)
- Billing Engine (usage tracking, cost calculation)
- Threat Intelligence Agent (abuse pattern correlation)
- Operators (real-time dashboard, manual overrides)

### 2.5 Downstream Dependency Chain

**Agents that depend on API_RATE_LIMIT_ENFORCEMENT_AGENT:**

1. **API_GATEWAY_AGENT** — Needs rate limit decision before forwarding request
2. **NOTIFICATION_AGENT** — Needs rate limit alerts for users/admins
3. **OBSERVABILITY_AGENT** — Needs rate limit metrics and dashboards
4. **AUDIT_AGENT** — Needs complete rate limit audit trail
5. **BILLING_ENGINE_AGENT** — Needs usage tracking for cost calculation
6. **THREAT_INTELLIGENCE_AGENT** — Needs abuse pattern data
7. **DECISION_TRACEABILITY_AGENT** — Needs rate limit decisions logged
8. **REPUTATION_ENGINE_AGENT** — Needs abuse history for trust scoring

**Agents that feed API_RATE_LIMIT_ENFORCEMENT_AGENT:**

1. **API_GATEWAY_AGENT** — Sends all API requests for rate limit check
2. **ACTOR_REGISTRY_AGENT** — Provides actor metadata, role, quotas
3. **TENANT_CONFIG_AGENT** — Provides tenant limits, license level, settings
4. **THREAT_INTELLIGENCE_AGENT** — Provides known abusers, attack patterns
5. **SYSTEM_MONITORING_AGENT** — Provides system load, capacity, health
6. **BEHAVIOR_ANALYTICS_AGENT** — Provides traffic baselines, anomaly scores
7. **INCIDENT_RESPONSE_AGENT** — Provides emergency mode, manual overrides

---

## 3️⃣ INPUT CONTRACT (STRICT)

### 3.1 Input Schema Definition

All rate limit check requests must conform to this strict schema. **No null tolerance without explicit policy. Reject malformed requests. Log all validation failures.**

```json
{
  "rate_limit_check_request": {
    "required_fields": [
      "request_id",
      "request_timestamp_utc",
      "actor_id",
      "actor_role",
      "tenant_id",
      "domain_id",
      "endpoint_path",
      "http_method",
      "api_version",
      "request_size_bytes",
      "operation_type",
      "source_ip",
      "user_agent"
    ],
    "optional_fields": [
      "batch_size",
      "is_retry",
      "retry_count",
      "priority_level",
      "bulk_operation_id",
      "quota_override_token",
      "context_tags",
      "cost_multiplier"
    ],
    "validation_rules": [
      "request_id MUST be UUID v4, globally unique, immutable",
      "request_timestamp_utc MUST be ISO 8601, within ±5 seconds of server time",
      "actor_id MUST be non-empty string, match authenticated session",
      "actor_role MUST be one of 8 defined roles: Student|Trainer|Evaluator|Institute|Enterprise|Recruiter|Admin|Parent|AIAgent",
      "tenant_id MUST match authenticated request context (no cross-tenant requests)",
      "domain_id MUST match one of 5 domains: Arts|Commerce|Science|Technology|Administration",
      "endpoint_path MUST be valid API endpoint (match API schema registry)",
      "http_method MUST be one of: GET|POST|PUT|DELETE|PATCH|HEAD|OPTIONS",
      "api_version MUST be semantic version (e.g., v1.0, v2.1) matching API contract",
      "request_size_bytes MUST be integer >= 0, <= MAX_REQUEST_SIZE (100MB default)",
      "operation_type MUST match enumerated operations: READ|WRITE|DELETE|COMPUTE|EXPORT|IMPORT|ADMIN|SYSTEM",
      "source_ip MUST be valid IP address (IPv4 or IPv6), no spoofing",
      "user_agent MUST be non-empty string (for client identification)",
      "batch_size (if provided) MUST be integer > 0, <= MAX_BATCH_SIZE (10000 default)",
      "retry_count (if provided) MUST be integer [0, 10]",
      "priority_level (if provided) MUST be one of: NORMAL|HIGH|CRITICAL",
      "quota_override_token (if provided) MUST be valid JWT with admin signature",
      "cost_multiplier (if provided) MUST be float > 0, <= 10.0"
    ],
    "security_checks": [
      "Tenant isolation — verify actor_id belongs to tenant_id (no cross-tenant)",
      "Actor authentication — verify actor_id has valid session/token",
      "Domain authorization — verify actor can access requested domain_id",
      "Endpoint authorization — verify actor's role permits access to endpoint",
      "API version compatibility — verify actor's API tier supports requested version",
      "IP reputation — check source_ip against known bad actors, VPNs, proxies",
      "User agent validation — check user_agent for known abuse patterns",
      "Request signature — verify request signed by authenticated actor (HMAC-SHA256)",
      "Rate limit bypass token — if provided, verify token is valid and unexpired"
    ],
    "domain_checks": [
      "If domain_id = 'Arts', verify actor has Arts domain access",
      "If domain_id = 'Commerce', verify actor's financial compliance clearance",
      "If domain_id = 'Science', verify actor's institutional affiliation if required",
      "If domain_id = 'Technology', verify actor's technical tier",
      "Cross-domain requests require explicit multi-domain grant",
      "Domain rate limits apply independently (isolation enforcement)"
    ],
    "tenant_quota_checks": [
      "Verify tenant_id has active subscription (not expired/canceled)",
      "Verify tenant's API tier supports requested endpoint",
      "Verify tenant's monthly request quota not exceeded",
      "Verify tenant's monthly data transfer quota not exceeded",
      "Verify tenant's concurrent request limit not exceeded",
      "Apply tenant-level burst limits if at capacity"
    ]
  }
}
```

### 3.2 Validation Pipeline

```
REQUEST_RECEIVED
    ↓
[SCHEMA_VALIDATION] → Reject if malformed, missing required fields
    ↓
[AUTHENTICATION] → Reject if actor_id invalid or session expired
    ↓
[TENANT_ISOLATION] → Reject if actor not in tenant_id
    ↓
[AUTHORIZATION] → Reject if actor lacks role/domain/endpoint permission
    ↓
[SECURITY_CHECKS] → Reject if IP/agent suspicious, signature invalid
    ↓
[RATE_LIMIT_LOOKUP] → Fetch current limits for actor, tenant, domain
    ↓
[COST_CALCULATION] → Determine request cost (base + multipliers)
    ↓
[QUOTA_CHECKING] → Check actor, tenant, domain quota consumption
    ↓
[ANOMALY_DETECTION] → Check if request matches abuse patterns
    ↓
[ADAPTIVE_LIMITING] → Check if should trigger adaptive (dynamic) limits
    ↓
✅ VALID → RETURN DECISION (ALLOW/BACKPRESSURE/REJECT)
```

### 3.3 Null Handling Policy

- **Rule:** No null tolerance without explicit policy
- **Explicit Policies:**
  - `request_id = null` → **REJECT** (CRITICAL, every request needs trace)
  - `actor_id = null` → **REJECT** (security violation, cannot enforce multi-tenancy)
  - `tenant_id = null` → **REJECT** (cannot isolate tenant)
  - `actor_role = null` → **REJECT** (cannot authorize)
  - `domain_id = null` → **REJECT** (cannot isolate domain)
  - `batch_size = null` → **ACCEPT** (optional, default = 1)
  - `context_tags = null` → **ACCEPT** (optional, empty array default)
  - `priority_level = null` → **ACCEPT** (optional, default = NORMAL)
  - `quota_override_token = null` → **ACCEPT** (optional, none by default)
  - Any required field = `null` → **IMMEDIATE REJECTION** with incident log

---

## 4️⃣ OUTPUT CONTRACT (STRICT)

### 4.1 Output Schema Definition

All rate limit decision responses MUST conform to this immutable schema. Decisions must include decision type, reason, quota status, and traceability.

```json
{
  "rate_limit_decision": {
    "decision_id": "UUID v4 (unique decision identifier)",
    "request_id": "UUID v4 (from request input)",
    "timestamp_utc": "ISO 8601 (when decision was made)",
    
    "decision_result": {
      "action": "enum (ALLOW|BACKPRESSURE|REJECT)",
      "http_status_code": "integer (200|429|503)",
      "reason": "string (human-readable decision reason)",
      "decision_confidence": "float [0.0, 1.0]",
      "decision_model_version": "string (semantic version)"
    },
    
    "quota_status": {
      "actor_quota_remaining": "integer (requests remaining this period)",
      "actor_quota_reset_timestamp": "ISO 8601 (when quota resets)",
      "actor_requests_used_this_period": "integer (requests consumed)",
      "actor_requests_limit": "integer (quota ceiling)",
      
      "tenant_quota_remaining": "integer (requests remaining)",
      "tenant_quota_reset_timestamp": "ISO 8601",
      "tenant_requests_used_this_period": "integer",
      "tenant_requests_limit": "integer",
      "tenant_data_transfer_used_mb": "float",
      "tenant_data_transfer_limit_mb": "float",
      
      "domain_quota_remaining": "integer",
      "domain_quota_reset_timestamp": "ISO 8601",
      "domain_requests_used_this_period": "integer",
      "domain_requests_limit": "integer"
    },
    
    "rate_limit_headers": {
      "X-RateLimit-Limit": "string (requests allowed per period)",
      "X-RateLimit-Remaining": "string (requests remaining)",
      "X-RateLimit-Reset": "string (Unix timestamp when limit resets)",
      "X-RateLimit-Used": "string (requests used)",
      "X-RateLimit-Retry-After": "string (seconds to retry, if BACKPRESSURE/REJECT)",
      "X-RateLimit-Policy": "string (which policy applied: STANDARD|ADAPTIVE|EMERGENCY)"
    },
    
    "request_cost": {
      "base_cost": "float (standard cost for this endpoint)",
      "multiplier_applied": "float (batch_size or other multipliers)",
      "total_cost": "float (base_cost * multiplier)",
      "cost_reason": "string (explanation of cost calculation)"
    },
    
    "anomaly_analysis": {
      "anomaly_detected": "boolean",
      "anomaly_score": "float [0.0, 1.0]",
      "anomaly_type": "enum (TRAFFIC_SPIKE|UNUSUAL_ENDPOINT|UNCOMMON_ACTOR|COORDINATED_ATTACK|BULK_EXPORT|SUSPICIOUS_PATTERN)",
      "anomaly_reason": "string",
      "abuse_probability": "float [0.0, 1.0]"
    },
    
    "adaptive_limit_status": {
      "adaptive_limiting_active": "boolean",
      "adaptive_limit_reason": "enum (SYSTEM_LOAD|TENANT_SPIKE|DOMAIN_SPIKE|COST_OVERRUN|DETECTED_ABUSE)",
      "normal_limit": "integer",
      "adapted_limit": "integer",
      "time_to_normalization": "string (ISO 8601 duration, e.g., PT30M)"
    },
    
    "enforcement_action": {
      "action_type": "enum (ALLOWED|ALLOWED_WITH_WARNING|THROTTLED|REJECTED|REJECTED_AND_ESCALATED)",
      "enforcement_timestamp": "ISO 8601 (when enforcement applied)",
      "escalation_needed": "boolean",
      "escalation_reason": "string (if escalation_needed = true)",
      "escalation_target": "enum (ACTOR|TENANT|DOMAIN|THREAT_INTELLIGENCE|INCIDENT_RESPONSE|SYSTEM_ADMIN)"
    },
    
    "audit_reference": {
      "audit_id": "UUID v4 (unique audit record)",
      "audit_timestamp": "ISO 8601",
      "immutability_seal": "HMAC-SHA256 (cryptographic commitment)",
      "audit_chain_hash": "SHA256(previous_record || this_record)",
      "actor_id": "string",
      "tenant_id": "string",
      "domain_id": "string",
      "operation_logged": "boolean (true = logged to audit trail)",
      "audit_log_location": "string (reference to audit store)"
    },
    
    "next_trigger_events": [
      {
        "event_id": "UUID v4",
        "event_type": "enum (RATE_LIMIT_EXCEEDED|QUOTA_WARNING|QUOTA_EXCEEDED|ABUSE_DETECTED|ESCALATION_NEEDED)",
        "event_trigger_timestamp": "ISO 8601",
        "triggered_agent_id": "string",
        "event_payload": "object (event-specific data)"
      }
    ]
  }
}
```

### 4.2 Decision Action Types & HTTP Status Codes

```
ALLOW (200 OK)
  - Request is within all limits
  - Process request normally
  - Cost deducted from quota
  - Return quota status headers
  - Log as normal activity

BACKPRESSURE (429 Too Many Requests)
  - Request is within limits but system is near capacity
  - Suggest client retry after delay
  - Include Retry-After header (seconds to wait)
  - Do NOT deduct quota (request not processed)
  - Log as backpressure event
  - Client can retry (adaptive behavior)

REJECT (429 Too Many Requests or 403 Forbidden)
  - Request exceeds rate limit quota
  - Do NOT process request
  - Do NOT deduct quota (request rejected before cost)
  - Return clear reason why rejected
  - Include Retry-After header (seconds until quota resets)
  - Log as rate limit violation
  - Escalate if abuse pattern detected
```

### 4.3 Output Guarantees

✅ **Completeness:** All required fields populated  
✅ **Consistency:** decision_id matches request_id, tenant_id matches context  
✅ **Correctness:** Decision reproducible (same input → same decision → same action)  
✅ **Confidentiality:** No PII exposed in response (audit log separately)  
✅ **Compliance:** Rate limit enforced per policy  
✅ **Traceability:** Audit trail unbroken, decision logged immutably  
✅ **Latency:** Decision made in < 5ms (P99)  

---

## 5️⃣ RATE LIMITING ALGORITHMS

### 5.1 Token Bucket Algorithm (Primary)

The agent uses **Token Bucket** as the primary rate limiting algorithm:

```
ALGORITHM: Token Bucket (Leaky Bucket variant)

PARAMETERS:
  - bucket_capacity = quota_limit (e.g., 1000 requests/hour)
  - refill_rate = capacity / period (e.g., 1000 / 3600 tokens/second)
  - current_tokens = tokens available now

OPERATION:
  1. Calculate time elapsed since last refill
  2. Add (elapsed_time * refill_rate) tokens to bucket
  3. Cap tokens at bucket_capacity (no overfill)
  4. Request costs N tokens (1 token = 1 request, or more for expensive ops)
  5. If current_tokens >= N:
       ALLOW request
       current_tokens -= N
       log usage
     ELSE:
       REJECT request
       calculate retry_after = (N - current_tokens) / refill_rate
       return 429 with Retry-After header

ADVANTAGES:
  - Handles bursts (burst capacity = bucket_capacity)
  - Fair distribution (refill_rate constant)
  - Low overhead (O(1) per request)
  - Works with distributed systems (Redis backing)

IMPLEMENTATION DETAILS:
  - State: {bucket_capacity, refill_rate, current_tokens, last_refill_timestamp}
  - Storage: Redis (shared state, atomic operations)
  - Atomic check-and-update: Lua script (Redis)
  - Precision: microsecond-level timestamp
```

**Code Example (Lua script in Redis):**

```lua
-- KEYS[1] = rate_limit_key (e.g., "rl:actor:user123")
-- ARGV[1] = bucket_capacity
-- ARGV[2] = refill_rate
-- ARGV[3] = request_cost
-- ARGV[4] = now (current timestamp, microseconds)

local bucket_capacity = tonumber(ARGV[1])
local refill_rate = tonumber(ARGV[2])
local request_cost = tonumber(ARGV[3])
local now = tonumber(ARGV[4])

local state = redis.call("HGETALL", KEYS[1])
local current_tokens = tonumber(state[2]) or bucket_capacity
local last_refill = tonumber(state[4]) or now

-- Calculate tokens to add (refill)
local elapsed = (now - last_refill) / 1000000  -- microseconds to seconds
local tokens_to_add = elapsed * refill_rate
local new_tokens = math.min(current_tokens + tokens_to_add, bucket_capacity)

-- Check if request can be allowed
if new_tokens >= request_cost then
  new_tokens = new_tokens - request_cost
  redis.call("HSET", KEYS[1], "tokens", new_tokens, "last_refill", now)
  return {"ALLOW", new_tokens}
else
  -- Calculate retry_after
  local retry_after = (request_cost - new_tokens) / refill_rate
  return {"REJECT", math.ceil(retry_after)}
end
```

### 5.2 Sliding Window Log (Secondary, for precision)

For critical tenants or high-value operations, uses **Sliding Window Log**:

```
ALGORITHM: Sliding Window Log

PARAMETERS:
  - window_size = time period (e.g., 1 hour)
  - max_requests = limit in window (e.g., 1000 requests)
  - request_log = ordered list of request timestamps

OPERATION:
  1. Remove all requests older than (now - window_size)
  2. Count remaining requests in log
  3. If count < max_requests:
       ALLOW request
       add request to log
     ELSE:
       REJECT request
       oldest_request_time = log[0]
       retry_after = window_size - (now - oldest_request_time)

ADVANTAGES:
  - Precise per-request tracking
  - No bursts (enforces hard limit per window)
  - Better for high-value operations

DISADVANTAGES:
  - Higher memory usage (stores all request times)
  - Higher CPU (list operations)
  - Not suitable for millions of requests/second

USE CASE:
  - High-cost operations (export, bulk processing, expensive ML)
  - Sensitive operations (admin actions, data modifications)
  - Auditing requirements (need request history)
```

### 5.3 Leaky Bucket (Queuing variant)

For backpressure scenarios, uses **Leaky Bucket with Queue**:

```
ALGORITHM: Leaky Bucket with Queue

PARAMETERS:
  - outflow_rate = requests/second (e.g., 100 RPS)
  - queue_size = max queued requests
  - current_queue = requests waiting to be processed

OPERATION:
  1. Process requests at constant outflow_rate (leak rate)
  2. New requests join queue
  3. If queue_size >= max:
       BACKPRESSURE (429, suggest retry)
       Do not queue (reject with Retry-After)
  4. Otherwise:
       Queue request (will be processed soon)
       Return BACKPRESSURE (429, will process in N seconds)

ADVANTAGES:
  - Smooth output rate (constant load on downstream)
  - Prevents burst thundering
  - Fair queuing (FIFO)

USE CASE:
  - System near capacity (need to smooth load)
  - Downstream bottleneck (prevent overwhelming)
  - Graceful degradation (queue vs. reject)
```

### 5.4 Cost Multiplier System

Requests have variable cost based on operation type:

```
COST_MULTIPLIER_TABLE:

Base cost = 1 (standard request)

Multipliers:
  - READ operation         = 1.0x (base cost)
  - WRITE operation        = 2.0x (double cost, more resource-intensive)
  - DELETE operation       = 3.0x (can cascade, cleanup expensive)
  - COMPUTE operation      = 5.0x (CPU-intensive)
  - EXPORT operation       = 10.0x (data transfer, disk I/O)
  - IMPORT operation       = 8.0x (data ingestion, validation)
  - BULK operation         = batch_size * 1.5x (batching overhead)
  - ADMIN operation        = 5.0x (audit logging, security checks)
  - SYSTEM operation       = 2.0x (internal, platform maintenance)

Batch Size Multiplier:
  - batch_size <= 10       = 1.0x (small batch, no penalty)
  - batch_size <= 100      = 1.2x (medium batch)
  - batch_size <= 1000     = 1.5x (large batch)
  - batch_size <= 10000    = 2.0x (very large batch)

Total cost = base_cost * operation_multiplier * batch_multiplier * custom_multiplier

Example:
  - Bulk EXPORT of 1000 items
  - Cost = 1 * 10.0 * 1.5 * 1.0 = 15 quota points
  - If actor has 100 requests/hour limit
    Then this one operation costs equivalent of 15 normal requests
```

### 5.5 Adaptive Rate Limiting

ML-based adaptive limits that adjust in real-time:

```
TRIGGER CONDITIONS for Adaptive Limiting:

1. SYSTEM_LOAD_HIGH
   - If system CPU > 80% OR memory > 85% OR disk I/O > 80%
   - Action: Reduce all limits by 20%
   - Duration: Until load returns to normal

2. TENANT_SPIKE
   - If tenant's RPS increases > 200% vs. baseline
   - Action: Reduce tenant's limit by 30%
   - Duration: Until pattern normalizes (check every 1 min)

3. DOMAIN_SPIKE
   - If domain's total RPS increases > 150% vs. baseline
   - Action: Reduce all actors in domain by 25%
   - Duration: Until domain load normalizes

4. COST_OVERRUN
   - If tenant's month-to-date costs exceed 90% of license budget
   - Action: Apply 50% cost multiplier (expensive ops cost 2x)
   - Duration: Until cost resets at month end

5. DETECTED_ABUSE
   - If anomaly_score > 0.8 (high confidence abuse detected)
   - Action: Reduce actor's limit to 10% of normal
   - Duration: Until abuse pattern clears (check every 10 min)

NORMALIZATION LOGIC:
  - Monitor metrics continuously
  - Every 1 minute: check if triggers no longer apply
  - If triggers cleared: gradually restore limits (5% increase per minute)
  - Time to full recovery: ~20 minutes
  - Prevents oscillation (hysteresis margins of ±5%)

ACTOR-SPECIFIC ADAPTATION:
  - Trusted actors (high reputation, long history): +20% limit bonus
  - New actors (first 48 hours): -30% limit penalty
  - Flagged abusers: -90% limit penalty (nearly blocked)
  - Verified bulk users (declared batch operations): +50% limit bonus
```

---

## 6️⃣ ML/AI LOGIC LAYER

### 6.1 ML-Based Anomaly Detection (70–80% of decisions)

The agent uses traditional ML for core rate limiting logic: traffic analysis, pattern detection, abuse scoring.

#### 6.1.1 Model Architecture

**Primary Models:**

1. **Traffic Baseline Model**
   - Type: Isolation Forest + Prophet (time-series)
   - Purpose: Learn normal traffic patterns per actor/tenant/domain
   - Features: RPS, request size distribution, endpoint distribution, time-of-day patterns
   - Training: Weekly on rolling 30-day window
   - Input: Historical request stream
   - Output: baseline_score [0.0, 1.0] (how normal is current traffic?)
   - Drift Monitoring: KL divergence between current and baseline distributions

2. **Abuse Pattern Detector**
   - Type: One-Class SVM + Isolation Forest ensemble
   - Purpose: Detect coordinated abuse, distributed attacks, suspicious patterns
   - Features: Request rate, endpoint concentration, actor clustering, temporal clustering
   - Training: Weekly on labeled abuse incidents
   - Input: Traffic stream with anomaly labels
   - Output: abuse_score [0.0, 1.0]
   - Drift Monitoring: False positive/negative rates per attack type

3. **Resource Consumption Predictor**
   - Type: Gradient Boosted Trees (LightGBM)
   - Purpose: Predict downstream resource consumption per request
   - Features: endpoint, operation_type, request_size, batch_size, actor_historical_usage
   - Training: Daily on resource monitoring data
   - Input: Request metadata
   - Output: predicted_resource_impact [0.0, 1.0] (impact on system)
   - Drift Monitoring: Prediction accuracy vs. actual resource usage

4. **Cost Estimation Model**
   - Type: Linear Regression + Random Forest
   - Purpose: Accurately estimate real cost of request execution
   - Features: operation_type, data_size, compute_complexity, storage_impact
   - Training: Monthly on billing data
   - Input: Request details
   - Output: estimated_cost_dollars (or cost tokens)
   - Drift Monitoring: Cost prediction accuracy per operation type

#### 6.1.2 Feature Engineering

```
FEATURE_SET_V1_0 = {
  categorical_features: [
    actor_role (one-hot encoded, 8 dimensions),
    operation_type (one-hot encoded: READ|WRITE|DELETE|COMPUTE|EXPORT|IMPORT),
    endpoint_category (one-hot, hundreds of endpoints grouped),
    domain_id (one-hot encoded, 5 dimensions),
    http_method (one-hot: GET|POST|PUT|DELETE|PATCH),
    tenant_tier (categorical: FREE|STARTUP|GROWTH|ENTERPRISE)
  ],
  
  numerical_features: [
    request_rate_rps (requests per second, last 1 min),
    request_rate_change (% change from baseline),
    request_size_bytes (normalized to log scale),
    batch_size (log-scaled, 0 if not batch),
    actor_age_days (account age, log-scaled),
    actor_historical_rps (long-term average RPS),
    tenant_age_days (tenant age, log-scaled),
    tenant_request_quota_used_percent (0-100),
    system_load_percent (CPU + memory + disk, 0-100),
    concurrency_count (concurrent requests from same actor)
  ],
  
  temporal_features: [
    hour_of_day (cyclical: sin/cos encoded),
    day_of_week (cyclical: sin/cos encoded),
    is_weekend (boolean),
    is_business_hours (boolean),
    seconds_since_last_request (log-scaled, per actor),
    request_distribution_entropy (uniform vs. concentrated endpoints)
  ],
  
  aggregated_features: [
    actor_request_frequency_per_hour (rate),
    actor_endpoint_concentration (% of requests to top endpoint),
    actor_error_rate_last_hour (failed requests),
    actor_previous_abuse_flags (historical abuse count),
    tenant_total_rps (all actors in tenant combined),
    tenant_request_concentration_per_actor (is load concentrated?),
    domain_total_rps (all tenants in domain),
    domain_error_rate (overall domain health),
    ip_reputation_score (external threat intel, 0-1),
    user_agent_known_bot (0 or 1, if known abuse tool)
  ]
}
```

#### 6.1.3 Training Pipeline

```
TRAINING_FREQUENCY = Weekly (every Monday 02:00 UTC)
RETRAINING_TRIGGER = 
  | Drift detected (> 5% distribution shift in 48h)
  | OR abuse_score > 0.8 for > 10 consecutive incidents
  | OR false positive rate > 2% on validation set
  | OR major platform change (new endpoint, new role, etc.)

TRAINING_DATA = 
  - Source: RATE_LIMIT_AUDIT_LOG (append-only)
  - Window: Last 30 days of complete rate limit events
  - Sampling: Stratified by decision_type (ALLOW/BACKPRESSURE/REJECT)
  - Size: Up to 100M records per training run
  - Quality: Filter out incomplete records, require full audit_reference

VALIDATION_SPLIT = 
  - Train: 70% (oldest by timestamp)
  - Validation: 15% (middle by timestamp)
  - Test: 15% (newest by timestamp, held-out temporally)

TESTING_PROTOCOL =
  - Per-operation-type evaluation (minimum 1000 samples each)
  - Per-tenant evaluation (minimum 100 samples each)
  - Temporal holdout (newest 7 days never in training)
  - Require metrics: precision, recall, F1, AUC-ROC, latency_p99

APPROVAL_GATES =
  - Model metric degradation > 5% → STOP, escalate to PLATFORM_OPS
  - Feature drift detected → LOG but ALLOW (with warning flag)
  - New rare operation_type < 50 samples → HOLD OUT from training
  - Replication on hold-out test set passes → APPROVED
  - Latency increase > 10% → STOP (cannot afford latency regression)
```

#### 6.1.4 Drift Detection

```
DRIFT_DETECTION_ALGORITHM = Wasserstein distance + ADWIN + PageHinkley

MONITORING_TARGETS =
  - Request rate per actor (baseline vs. current)
  - Request size distribution (endpoint-specific)
  - Operation type distribution
  - Temporal patterns (time-of-day, day-of-week)
  - Error rates per endpoint
  - Latency percentiles (p50, p95, p99)
  - Cost distribution (actual vs. predicted)

DRIFT_THRESHOLD_ALERT = 
  | Wasserstein distance > 0.15 (statistical significance)
  | OR ADWIN detects change point (adaptive threshold)
  | OR 5 consecutive minute buckets exceed baseline by > 20%
  | OR PageHinkley cumulative sum crosses threshold

DRIFT_RESPONSE =
  - Log to OBSERVABILITY_AGENT
  - Emit DRIFT_DETECTED event to ML_OPS
  - Tag affected rate limits with drift_flag = true
  - If drift_indicator > 0.7 → escalate to PLATFORM_OPS
  - Schedule emergency retraining if drift persists > 30 min
  - Increase monitoring frequency during drift period
```

### 6.2 AI-Based Abuse Pattern Explanation (20–30% of decisions)

For 20–30% of anomalous requests, uses LLM-assisted reasoning to explain detected abuse patterns and suggest actions.

#### 6.2.1 LLM-Assisted Explanation Engine

**Purpose:** Generate human-readable explanations of why a request was rate-limited, especially for borderline abuse cases.

**Scope (Strictly Defined):**
- Explain anomaly_score (not override it)
- Explain detected abuse pattern (not introduce new factors)
- Suggest possible remediation (not make enforcement decisions)
- Generate admin alerts (not block users automatically based on LLM)
- Create user-facing messages (not explain all internal logic)

**No Decision Autonomy:** LLM assists ML-driven decisions, does not replace them.

#### 6.2.2 LLM Prompting (Versioned & Deterministic)

```
SYSTEM_PROMPT_V1_0 = """
You are an abuse pattern analyzer for the Ecoskiller API rate limiting system.

CONTEXT:
- Platform: Multi-tenant SaaS serving 10M–100M users
- Roles: 8 actor roles with different quota levels
- Domains: 5 strictly isolated domains
- Rate Limits: Token bucket algorithm with ML-based anomaly detection

TASK:
You are given a rate limit decision with:
  1. actor_profile (who made the request)
  2. traffic_pattern (unusual behavior detected)
  3. ml_anomaly_scores (multiple anomaly detectors output)
  4. request_context (what they requested)
  5. historical_baseline (normal behavior for this actor)

YOUR ROLE:
Generate a concise, accurate explanation of why this request triggered rate limiting.

CONSTRAINTS:
- DO explain the anomaly pattern detected
- DO acknowledge confidence level (anomaly_score)
- DO mention key factors (spike in RPS, new endpoint, etc.)
- DO suggest possible causes (legitimate or malicious)
- DO recommend actions for admin/user
- DO keep explanation under 300 words
- DO be technically accurate (mention specific thresholds)

- DO NOT override the ML decision
- DO NOT introduce new factors not in traffic_pattern
- DO NOT speculate beyond provided anomaly data
- DO NOT suggest reducing security (be conservative)
- DO NOT use creative language; be factual and precise
- DO NOT assume user context; be explicit

OUTPUT FORMAT:
Return a JSON object with:
{
  "anomaly_explanation": "2-3 sentence summary of detected behavior",
  "detected_patterns": [
    {"pattern": "[pattern name]", "strength": "[weak|moderate|strong]", "evidence": "[specific data]"}
  ],
  "confidence_interpretation": "[what anomaly_score means in plain English]",
  "possible_causes": [
    {"cause": "[legitimate or malicious]", "likelihood": "[low|medium|high]"}
  ],
  "recommended_actions": [
    {"action": "[action for admin/user]", "urgency": "[low|medium|high]"}
  ],
  "caveats": ["caveat1", "caveat2"],
  "escalation_recommended": true/false
}
"""

INPUT_TO_LLM = {
  "actor_id": "string (anonymized if needed)",
  "actor_profile": {
    "role": "string",
    "account_age_days": "integer",
    "historical_rps": "float",
    "abuse_history": "integer (previous abuse incidents)"
  },
  "traffic_pattern": {
    "current_rps": "float",
    "baseline_rps": "float",
    "rps_increase_percent": "float",
    "endpoint_concentration": "float (% to top endpoint)",
    "request_size_distribution": "object"
  },
  "ml_anomaly_scores": {
    "baseline_deviation_score": "float [0, 1]",
    "abuse_pattern_score": "float [0, 1]",
    "resource_impact_score": "float [0, 1]"
  },
  "request_context": {
    "operation_type": "string",
    "endpoint": "string",
    "batch_size": "integer"
  },
  "historical_baseline": {
    "typical_daily_peak_rps": "float",
    "typical_request_types": ["array"],
    "typical_batch_size": "integer"
  }
}

TEMPERATURE = 0.2 (highly deterministic, minimal variation)
TOP_P = 0.9 (avoid extreme tails)
MAX_TOKENS = 800
TIMEOUT = 3 seconds (hard stop, use fallback if exceeded)
```

#### 6.2.3 LLM Usage Governance

```
LLM_USAGE_BUDGET = 
  - Max 30% of REJECT decisions get LLM explanation
  - Triggers: anomaly_score > 0.7 OR abuse_score > 0.8 OR escalation_needed
  - Skip LLM for routine blocks (standard quota exceeded, no anomaly)

LLM_FALLBACK = 
  - If LLM timeout > 3 seconds → use template-based explanation
  - If LLM error → use template-based explanation
  - If output validation fails → use template-based explanation
  - Never allow missing explanation; always provide something

PROMPT_VERSIONING =
  - SYSTEM_PROMPT_V1_0 in use from 2025-02-28
  - Any change → new version (e.g., SYSTEM_PROMPT_V1_1)
  - Version immutable once used
  - All generated explanations tagged with prompt_version
  - Old versions retained for reproducibility

DETERMINISM =
  - Same traffic_pattern + same SYSTEM_PROMPT_VERSION → same explanation
  - Verified via caching: hash(traffic_pattern) → reuse cached explanation
  - If cache miss on repeated anomaly → log as anomaly (should not happen)
```

### 6.3 Model Version Control & Immutability

```
MODEL_REGISTRY = {
  "traffic_baseline_model": {
    "current_version": "v1.2.3",
    "deployment_timestamp": "2025-02-28T12:00:00Z",
    "training_data_window": "2025-01-01 to 2025-02-28",
    "training_size": "50M records",
    "validation_metrics": {
      "baseline_accuracy": 0.94,
      "false_positive_rate": 0.01,
      "false_negative_rate": 0.03
    },
    "model_path": "s3://ecoskiller-models/traffic_baseline_v1.2.3/model.pkl",
    "immutable_hash": "sha256:abc123...",
    "rollback_plan": "Revert to v1.2.2 if baseline_accuracy < 90%"
  },
  "abuse_pattern_detector": { ... },
  "resource_consumption_predictor": { ... },
  "cost_estimation_model": { ... }
}

VERSION_HISTORY = append-only log
  - Every model version immutably recorded
  - Training date, data window, metrics stored
  - Rollback decisions audited
  - No deletion of old versions
```

---

## 7️⃣ SCALABILITY DESIGN

### 7.1 Horizontal Scaling Architecture

```
DEPLOYMENT_MODEL = Multi-instance, stateless microservice

INSTANCE_DISTRIBUTION =
  - Primary: 20–50 instances (production load at 100K–1M RPS)
  - Secondary: 10–20 instances (failover, scheduled maintenance)
  - Burst: Auto-scale to 200+ instances if RPS exceeds 90% capacity
  - Geographic: Geo-distributed (US-EAST, EU-WEST, ASIA-PACIFIC, closer to users)

LOAD_BALANCING =
  - Layer 4 (connection-level) load balancer (fastest)
  - Consistent hashing by tenant_id (keep related requests on same instance)
  - Health check: every 5 seconds, timeout 1 second
  - Circuit breaker: trip if error rate > 10% for 10 seconds
  - Graceful degradation: shed lowest-priority requests under load
```

### 7.2 Performance Targets

```
EXPECTED_RPS = 100K–1M requests per second at full scale
  (10M users, each generating ~1–100 requests per day, concentrated in peak hours)

LATENCY_TARGETS (P99 < 5ms is CRITICAL):
  - P50 (median): 0.5 ms
  - P95 (95th percentile): 2 ms
  - P99 (99th percentile): 5 ms
  - P99.9 (99.9th percentile): 15 ms
  - Max: 50 ms (hard timeout, block request if processing takes longer)

THROUGHPUT_TARGETS:
  - Per-instance: 10K–50K RPS
  - Network I/O: 100 Gbps per instance
  - Memory: 32–64 GB per instance (models + cache)
  - CPU: 32–64 cores per instance

AVAILABILITY_TARGET = 99.99% uptime (≤ 52 min downtime/year)
```

### 7.3 Stateless Execution

```
NO LOCAL STATE:
  - All rate limit state stored in Redis (shared)
  - No in-memory caches that survive instance restart
  - Instance restart ≠ rate limit loss
  - Instant recovery to exact same state

EXTERNAL STATE STORES:
  - Rate limit buckets: Redis (primary, atomic operations)
  - Rate limit audit log: Cassandra (append-only, immutable)
  - ML models: S3-compatible object store (versioned)
  - Actor profiles: PostgreSQL (cached in Redis)
  - Tenant quotas: PostgreSQL (cached in Redis)
  - ML training data: S3 (historical rate limit events)

CACHE_INVALIDATION =
  - TTL-based: actor profiles, tenant quotas (5 min)
  - Event-based: on config change, invalidate immediately
  - Automatic: cache miss = fetch from source of truth
```

### 7.4 Event-Driven Processing

```
TRIGGER_SOURCES =
  - API requests (all incoming requests = triggers)
  - Scheduled jobs (hourly baseline update, daily model retraining)
  - Manual config changes (rate limit policy update)
  - Incident events (DDoS detected, emergency mode activated)

EVENT_QUEUE = Redis Streams (low-latency alternative to Kafka)
  - Stream: rate_limit_decisions
  - Retention: 24 hours (decisions logged long-term in Cassandra)
  - Consumer groups: 10+ workers for async processing
  - Latency: < 1ms message delivery

ASYNC_PROCESSING (low priority, non-blocking):
  - ML model inference (anomaly scoring)
  - LLM explanation generation
  - Audit log writing
  - Metrics emission
  - Escalation alert sending
  - Does NOT block the API response
```

### 7.5 Async Processing Pipeline

```
SYNCHRONOUS PATH (< 5ms latency):
  INPUT_REQUEST (from API Gateway)
    ↓ (< 1 ms)
  [SCHEMA_VALIDATION: required fields check]
    ↓
  [FAST_CHECKS: authentication, tenant isolation]
    ↓
  [REDIS_LOOKUP: get current token bucket state]
    ↓
  [DECISION: token bucket algorithm]
    ↓
  [RETURN_RESPONSE: ALLOW/BACKPRESSURE/REJECT with headers]
    ↓
  RESPONSE_SENT (< 5 ms total)

ASYNCHRONOUS PATH (background, non-blocking):
  [EMIT_ASYNC_EVENTS: decision to Redis Streams]
    ↓
  [ML_INFERENCE: anomaly detection in parallel]
    ↓
  [LLM_EXPLANATION: if anomaly detected (3s timeout)]
    ↓
  [AUDIT_LOG: write decision to Cassandra]
    ↓
  [METRICS_EMIT: send to Prometheus]
    ↓
  [ESCALATION: send alerts if needed]
    ↓
  [CACHE_UPDATE: update actor profile cache]

TARGET_TIME = All async work completed within 10 seconds
TIMEOUT = 30 seconds (hard stop for async work)
```

### 7.6 Idempotent Operations

```
IDEMPOTENCY_KEY = request_id (UUID v4)

RATE_LIMIT_IDEMPOTENCY:
  IF request_id already processed:
    - Retrieve existing decision from cache
    - Return cached decision_response (no re-processing)
    - Log as duplicate (info level, no error)
  ELSE:
    - Process normally
    - Store request_id in idempotency cache (TTL 1 hour)
    - Return new decision

CACHE_STORE = Redis
  - Key: "rl:idempotency:{request_id}"
  - Value: {decision_action, decision_timestamp, headers_dict}
  - TTL: 1 hour (covers typical retry window)
  - Eviction policy: LRU (when memory limit hit)
  
IDEMPOTENCY_GUARANTEE =
  - Same request_id (same actor, endpoint, time)
  - Always returns same decision
  - Prevents double-charging quota (if request retried)
```

### 7.7 Queue Strategy

```
PRIMARY_QUEUE = Redis Streams (for real-time rate limit decisions)
  Stream: rate_limit_decisions
  Entries per second: 1M+ (can handle high volume)
  Retention: 24 hours
  Consumer groups: 10+ parallel workers
  Latency: < 1ms message delivery

SECONDARY_QUEUE = Kafka (for long-term audit trail)
  Topic: rate_limit_audit_events
  Partitions: 100 (by tenant_id for ordering)
  Replication: 3x (high availability)
  Retention: 30 days
  Purpose: Immutable audit log for compliance

BATCH_PROCESSING =
  - Batch size: up to 10K decisions or 100ms window
  - Improves throughput by 10x vs. per-request processing
  - Determinism: batch order guaranteed by stream partition
  - Used for: ML training data, audit log archival
```

---

## 8️⃣ SECURITY ENFORCEMENT

### 8.1 Tenant Isolation Validation

```
TENANT_ISOLATION_CHECK = {
  "validation_rule": "Every rate limit decision MUST belong to exactly one tenant",
  
  "checks": [
    {
      "check_name": "Request context tenant match",
      "logic": "Authenticated request token tenant_id == rate_limit request tenant_id",
      "failure_action": "REJECT request, log as security incident"
    },
    {
      "check_name": "Actor belongs to tenant",
      "logic": "actor_id MUST have explicit membership in tenant_id",
      "failure_action": "REJECT, log as potential account compromise"
    },
    {
      "check_name": "Rate limit quota isolation",
      "logic": "Rate limits stored in tenant-isolated Redis keyspaces",
      "failure_action": "Keyspace isolation enforced at Redis layer, no override"
    },
    {
      "check_name": "Audit log partition by tenant",
      "logic": "Audit records stored in tenant-isolated partitions",
      "failure_action": "Partition enforcement at storage layer, no override"
    }
  ],
  
  "enforcement_point": "Before any processing, at request entry",
  "bypass_allowed": false,
  "exceptions": "NONE (hard rule)"
}
```

### 8.2 Domain Isolation Validation

```
DOMAIN_ISOLATION_CHECK = {
  "validation_rule": "Rate limits MUST respect domain boundaries (Arts, Commerce, Science, Technology, Administration)",
  
  "checks": [
    {
      "check_name": "Actor has domain access",
      "logic": "actor_id's domain_access includes requested domain_id",
      "failure_action": "REJECT, log as policy violation"
    },
    {
      "check_name": "Endpoint valid for domain",
      "logic": "endpoint MUST be accessible from requested domain_id",
      "failure_action": "REJECT, log as business rule violation"
    },
    {
      "check_name": "No cross-domain bucket sharing",
      "logic": "Rate limit buckets stored in domain-isolated Redis keyspaces",
      "failure_action": "REJECT, enforce isolation at storage layer"
    }
  ],
  
  "enforcement_point": "During authorization phase",
  "bypass_allowed": false,
  "exceptions": "Only with written approval from PLATFORM_OPS (logged)"
}
```

### 8.3 Role-Based Rate Limiting

```
RATE_LIMIT_BY_ROLE = {
  "Student": {
    "requests_per_hour": 1000,
    "requests_per_day": 10000,
    "concurrent_requests": 5,
    "expensive_operation_multiplier": 2.0,
    "bulk_operation_max_size": 100
  },
  "Trainer": {
    "requests_per_hour": 5000,
    "requests_per_day": 50000,
    "concurrent_requests": 20,
    "expensive_operation_multiplier": 1.5,
    "bulk_operation_max_size": 1000
  },
  "Evaluator": {
    "requests_per_hour": 10000,
    "requests_per_day": 100000,
    "concurrent_requests": 50,
    "expensive_operation_multiplier": 1.0,
    "bulk_operation_max_size": 5000
  },
  "Institute": {
    "requests_per_hour": 50000,
    "requests_per_day": 500000,
    "concurrent_requests": 100,
    "expensive_operation_multiplier": 1.0,
    "bulk_operation_max_size": 10000
  },
  "Enterprise": {
    "requests_per_hour": 100000,
    "requests_per_day": 1000000,
    "concurrent_requests": 200,
    "expensive_operation_multiplier": 1.0,
    "bulk_operation_max_size": 100000
  },
  "Recruiter": {
    "requests_per_hour": 5000,
    "requests_per_day": 50000,
    "concurrent_requests": 10,
    "expensive_operation_multiplier": 2.0,
    "bulk_operation_max_size": 100
  },
  "Admin": {
    "requests_per_hour": "unlimited",
    "requests_per_day": "unlimited",
    "concurrent_requests": "unlimited",
    "note": "Subject to system load (adaptive limiting still applies)"
  },
  "Parent": {
    "requests_per_hour": 100,
    "requests_per_day": 1000,
    "concurrent_requests": 2,
    "expensive_operation_multiplier": 3.0,
    "bulk_operation_max_size": 10,
    "note": "Read-only, lowest quota"
  },
  "AIAgent": {
    "requests_per_hour": 10000,
    "requests_per_day": 100000,
    "concurrent_requests": 50,
    "expensive_operation_multiplier": 0.5,
    "bulk_operation_max_size": 100000,
    "note": "Lowest cost (internal system agents)"
  }
}

ENFORCEMENT =
  - Check actor_role against RATE_LIMIT_BY_ROLE
  - Apply role's quota as baseline
  - Apply cost multipliers per operation
  - Apply adaptive limits if system under pressure
  - Escalate if actor repeatedly exceeds limits
```

### 8.4 IP Reputation & Bot Detection

```
IP_REPUTATION_CHECKING =
  - Check source_ip against known-bad IP databases
  - Check source_ip against internal abuse list
  - Apply penalties: trusted IPs get +20% limit bonus, bad IPs get -50% penalty
  - Detected proxy/VPN: require additional auth, apply -30% penalty
  - Datacenter IP (AWS, GCP, Azure): require explicit whitelisting

BOT_DETECTION =
  - Check user_agent for known bot signatures
  - Known abuse tools: immediate -90% penalty (nearly blocked)
  - Headless browsers (Selenium, Puppeteer): -50% penalty
  - Legitimate bots (Googlebot, curl, etc.): +0% (no penalty)
  - Unknown user_agent: require verification (CAPTCHA or auth token)

VPN/PROXY_DETECTION =
  - Detect VPN services (ExpressVPN, NordVPN, etc.)
  - Detect proxy services (residential proxies, datacenter proxies)
  - Action: require additional verification, reduced limits
  - Exception: legitimate corporate VPNs (whitelisted)
```

### 8.5 Encryption & Data Protection

```
ENCRYPTION_IN_TRANSIT =
  - All rate limit decisions transmitted via TLS 1.3 (minimum)
  - Certificate pinning for critical connections
  - Rate limit headers (X-RateLimit-*) sent over TLS only
  - No plaintext HTTP allowed (redirect to HTTPS)

ENCRYPTION_AT_REST =
  - Redis: TLS + authentication required
  - Cassandra: Transparent Data Encryption (TDE)
  - PostgreSQL: Column-level encryption for sensitive data
  - S3: Server-side encryption (AES-256)
  - Encryption keys: Managed by AWS KMS or HashiCorp Vault
  - Key rotation: Every 90 days (audited)

AUDIT_LOG_ENCRYPTION =
  - Rate limit decisions logged with full context (audit_id, timestamps, actor)
  - Audit log encrypted before writing to Cassandra
  - Audit log immutable (append-only, no update/delete)
  - Access to audit log requires special role + approval
```

### 8.6 Audit Logging (Append-Only)

```
AUDIT_LOG_STORE = Cassandra (time-series, append-only)

IMMUTABILITY_GUARANTEE =
  - Cassandra's append-only nature provides write-once semantics
  - No update/delete operations on rate_limit_decisions
  - All modifications are new records with audit_chain_hash linking
  - Any modification attempt logged as "UNAUTHORIZED_MODIFICATION_ATTEMPT"

AUDIT_LOG_SCHEMA =
  {
    decision_id (partition key),
    timestamp_utc (clustering key, sorted descending),
    request_id,
    actor_id,
    tenant_id,
    domain_id,
    endpoint_path,
    decision_action,
    quota_used,
    anomaly_score,
    abuse_score,
    escalation_triggered,
    decision_timestamp,
    immutability_seal (HMAC),
    audit_chain_hash
  }

RETENTION_POLICY =
  - All records retained for 2 years minimum (regulatory + forensics)
  - After 2 years, eligible for archival (cold storage)
  - Archive location: S3 Glacier (encrypted, immutable)
  - Retrieval: Requires PLATFORM_OPS approval + audit
  - No deletion: only archival or secure destruction (tracked)

ACCESS_CONTROL =
  - Read access: PLATFORM_OPS, SECURITY_TEAM, COMPLIANCE_TEAM (audit)
  - Write access: API_RATE_LIMIT_ENFORCEMENT_AGENT only (append)
  - Admin access: Requires approval from CISO
  - No backdoor access; no root bypass
```

---

## 9️⃣ AUDIT & TRACEABILITY

### 9.1 Rate Limit Decision Audit Log

Every rate limit decision is recorded in an immutable audit trail:

```json
{
  "rate_limit_audit_record": {
    "decision_id": "UUID v4 (globally unique)",
    "timestamp_utc": "ISO 8601",
    "request_id": "UUID v4 (from input)",
    
    "actor_context": {
      "actor_id": "string",
      "actor_role": "enum (8 roles)",
      "tenant_id": "string",
      "domain_id": "string",
      "source_ip": "string",
      "user_agent": "string"
    },
    
    "request_context": {
      "endpoint_path": "string",
      "http_method": "string",
      "request_size_bytes": "integer",
      "operation_type": "string",
      "batch_size": "integer (if applicable)"
    },
    
    "decision_details": {
      "action": "enum (ALLOW|BACKPRESSURE|REJECT)",
      "reason": "string",
      "confidence": "float [0, 1]",
      "model_version": "string"
    },
    
    "quota_accounting": {
      "cost_applied": "float",
      "quota_remaining_before": "float",
      "quota_remaining_after": "float",
      "quota_limit": "float",
      "reset_timestamp": "ISO 8601"
    },
    
    "anomaly_flags": {
      "anomaly_detected": "boolean",
      "anomaly_score": "float [0, 1]",
      "abuse_score": "float [0, 1]",
      "anomaly_type": "string"
    },
    
    "audit_trail": {
      "decision_id": "UUID v4",
      "immutability_seal": "HMAC-SHA256",
      "audit_chain_hash": "SHA256",
      "previous_decision_id": "UUID v4 (for chain linkage)"
    }
  }
}
```

### 9.2 Metrics & Dashboard Queries

```
KEY_METRICS_EMITTED = {
  "decisions_total": "counter (cumulative)",
  "decisions_allowed": "counter",
  "decisions_backpressured": "counter",
  "decisions_rejected": "counter",
  "decision_latency_ms": {
    "p50": "gauge",
    "p95": "gauge",
    "p99": "gauge"
  },
  "quota_consumption": {
    "by_actor": "gauge",
    "by_tenant": "gauge",
    "by_domain": "gauge"
  },
  "anomalies_detected": "counter",
  "abuse_incidents": "counter",
  "escalations_triggered": "counter"
}

QUERY_EXAMPLES = [
  "All decisions for actor_id in time window",
  "Quota consumption per tenant (daily/weekly/monthly)",
  "Top 10 rate-limited actors (potential abusers)",
  "Rate limit policy effectiveness (# ALLOW vs REJECT)",
  "Decision latency percentiles (SLA tracking)",
  "Anomaly detection accuracy (# false positives)",
  "Escalation response time (mean time to resolution)"
]
```

---

## 🔟 FAILURE POLICY

### 10.1 Failure Classification & Handling

The agent must define deterministic behavior for all possible failures. **No silent failures allowed. Block request on ambiguity.**

#### Invalid Request Failure

```
SCENARIO: Request violates schema, fails authentication, or contains invalid data

DETECTION =
  [SCHEMA_VALIDATION] fails
  OR [AUTHENTICATION] fails
  OR [TENANT_ISOLATION] fails
  OR required field is missing

RESPONSE_SEQUENCE =
  1. STOP_PROCESSING (do not consult rate limits)
  2. REJECT request (400 Bad Request or 401 Unauthorized)
  3. LOG_INCIDENT:
     {
       "incident_type": "INVALID_REQUEST",
       "request_id": "from input or generated UUID",
       "failure_reason": "specific validation failure",
       "failure_timestamp_utc": "ISO 8601",
       "actor_id": "if known",
       "escalation_level": "WARNING"
     }
  4. DO NOT DEDUCT QUOTA (request was invalid, no cost)
  5. RETURN_ERROR_RESPONSE with clear reason
  6. MONITORING: Emit error metric

STATUS = Block request, return 400/401 with error message
```

#### Redis Unavailable Failure

```
SCENARIO: Redis (rate limit state store) is unavailable or timing out

DETECTION =
  [REDIS_LOOKUP] fails (connection timeout, service down)
  OR [REDIS_UPDATE] fails (write timeout)

RESPONSE_SEQUENCE =
  1. ESCALATE_IMMEDIATELY: Page on-call engineer (Redis is CRITICAL)
  2. FALLBACK_BEHAVIOR:
     - If Redis is read-only: use cached limits (from memory)
     - If Redis completely down: use conservative limits (reduce by 50%)
  3. FAILSAFE_ACTION:
     - Cannot maintain accurate rate limiting
     - Choose safe option: allow with warning OR reject with 503
     - Decision: ALLOW (with reduced limits) if system healthy
     - Decision: REJECT (503) if system under stress
  4. LOG_INCIDENT (CRITICAL severity)
  5. DISABLE_EXPENSIVE_OPERATIONS: Reject EXPORT/BULK operations
  6. RESUME_NORMAL when Redis recovered

STATUS = Degrade gracefully, maintain security
```

#### Model Load Failure

```
SCENARIO: ML model fails to load, inference times out, or returns error

DETECTION =
  [LOAD_MODEL] fails (file not found, corrupt, wrong version)
  OR [INVOKE_MODEL] times out (> 50ms)
  OR [MODEL_OUTPUT_VALIDATION] fails

RESPONSE_SEQUENCE =
  1. STOP_ML_PROCESSING (do not block decision on missing ML)
  2. FALLBACK_BEHAVIOR:
     - Use static rate limits (no anomaly detection)
     - Use previous model version (if available)
     - Use rule-based anomaly detection (hardcoded rules)
  3. LOG_INCIDENT:
     {
       "incident_type": "MODEL_FAILURE",
       "model_name": "e.g., abuse_pattern_detector",
       "model_error": "specific error message",
       "fallback_used": "true"
     }
  4. ESCALATE_TO: ML_OPS_TEAM (page if critical)
  5. CONTINUE_PROCESSING: Proceed with fallback
  6. RESUME_NORMAL when model recovered

STATUS = Degrade gracefully, maintain rate limiting functionality
```

#### LLM Timeout Failure

```
SCENARIO: LLM call times out, takes > 3 seconds, or is rate-limited

DETECTION =
  [INVOKE_LLM] timeout (3 seconds exceeded)
  OR [LLM_API_RESPONSE] 429 (rate limit) or 500+ error
  OR [LLM_OUTPUT_VALIDATION] times out

RESPONSE_SEQUENCE =
  1. STOP_LLM_PROCESSING (do not wait for slow LLM)
  2. FALLBACK_BEHAVIOR:
     - Use template-based explanation (not LLM-generated)
     - Template: "Request rate-limited due to quota or anomaly"
  3. LOG_INCIDENT (WARNING severity):
     {
       "incident_type": "LLM_TIMEOUT",
       "decision_id": "UUID",
       "timeout_duration_ms": integer
     }
  4. CONTINUE_PROCESSING: Proceed with template explanation
  5. ESCALATE_TO: Observe metric (monitor LLM timeout rate)
  6. RESUME_NORMAL: No special action needed (LLM optional)

STATUS = Explanation missing, but core rate limiting still works
```

#### Data Corruption Failure

```
SCENARIO: Detected audit trail corruption or Redis data inconsistency

DETECTION =
  [AUDIT_CHAIN_HASH] fails (hash mismatch, immutability broken)
  OR [REDIS_CONSISTENCY] check fails
  OR [QUOTA_ACCOUNTING] inconsistency detected

RESPONSE_SEQUENCE =
  1. STOP_PROCESSING (data integrity is critical)
  2. ESCALATE_IMMEDIATELY (page SECURITY_TEAM + PLATFORM_OPS)
  3. LOG_INCIDENT (CRITICAL severity):
     {
       "incident_type": "DATA_CORRUPTION",
       "corruption_type": "hash_mismatch|consistency_error",
       "affected_actor_id": "string",
       "affected_tenant_id": "string"
     }
  4. ISOLATION:
     - Cease rate limiting for affected actor/tenant
     - Return 503 (service unavailable) for that actor
     - Alert admins to manual intervention
  5. INVESTIGATION:
     - Forensics team investigates corruption
     - Determine if tampering, hardware failure, or bug
     - Recover from backup if possible
  6. NOTIFICATION:
     - Notify compliance team (potential security breach)
     - Audit trail is tampered with
  7. RECOVERY: Require manual reset before resuming

STATUS = Block actor/tenant, escalate to humans
```

### 10.2 Failure Mode Decision Tree

```
┌─ FAILURE DETECTED
│
├─ IS_AUTHENTICATION_FAILED?
│  ├─ YES → REJECT (401 Unauthorized), do not deduct quota
│  └─ NO → continue
│
├─ IS_REDIS_UNAVAILABLE?
│  ├─ YES → ESCALATE (page on-call), FAILSAFE (allow/reject conservatively)
│  └─ NO → continue
│
├─ IS_ML_MODEL_FAILED?
│  ├─ YES → USE_FALLBACK (static limits, previous model, hardcoded rules)
│  └─ NO → continue
│
├─ IS_LLM_TIMEOUT?
│  ├─ YES → USE_TEMPLATE_EXPLANATION (skip LLM)
│  └─ NO → continue
│
├─ IS_DATA_CORRUPTION?
│  ├─ YES → ESCALATE (page SECURITY_TEAM), BLOCK_ACTOR, manual intervention
│  └─ NO → continue
│
└─ UNKNOWN_FAILURE?
   ├─ YES → ESCALATE (page on-call), BLOCK_REQUEST (503)
   └─ NO → ALLOW (with logging)
```

### 10.3 Escalation Matrix

```
ESCALATION_MATRIX = {
  "REDIS_UNAVAILABLE": {
    "target": "PLATFORM_OPS",
    "severity": "CRITICAL",
    "page": true,
    "timeout": "5 minutes"
  },
  "MODEL_FAILURE": {
    "target": "ML_OPS_TEAM",
    "severity": "HIGH",
    "page": true,
    "timeout": "15 minutes"
  },
  "DATA_CORRUPTION": {
    "target": ["SECURITY_TEAM", "PLATFORM_OPS"],
    "severity": "CRITICAL",
    "page": true,
    "timeout": "5 minutes"
  },
  "LLM_TIMEOUT_PERSISTENT": {
    "target": "ML_OPS_TEAM",
    "severity": "MEDIUM",
    "page": false,
    "timeout": "1 hour"
  },
  "ABUSE_PATTERN_DETECTED": {
    "target": "THREAT_INTELLIGENCE_AGENT",
    "severity": "HIGH",
    "page": false,
    "timeout": "10 minutes"
  },
  "QUOTA_EXCEEDED": {
    "target": "BILLING_ENGINE_AGENT",
    "severity": "MEDIUM",
    "page": false,
    "timeout": "1 hour"
  }
}
```

---

## 1️⃣1️⃣ INTER-AGENT DEPENDENCY MAP

### 11.1 Upstream Dependencies

```
UPSTREAM_AGENTS = {
  
  "API_GATEWAY_AGENT": {
    "sends_events": [
      "all API requests for rate limit check",
      "request metadata (actor, endpoint, size)"
    ],
    "contract": "request MUST have all required fields",
    "sla": "rate limit decision < 5ms",
    "criticality": "TIER-1 (inline in request path)"
  },
  
  "ACTOR_REGISTRY_AGENT": {
    "sends": "actor metadata, role, domain access",
    "frequency": "cached, updated on auth changes",
    "contract": "complete actor profile",
    "sla": "< 10ms lookup",
    "criticality": "TIER-1"
  },
  
  "TENANT_CONFIG_AGENT": {
    "sends": "tenant limits, license level, settings",
    "frequency": "cached, updated on config change",
    "contract": "complete tenant configuration",
    "sla": "< 10ms lookup",
    "criticality": "TIER-1"
  },
  
  "THREAT_INTELLIGENCE_AGENT": {
    "sends": "known abusers, attack patterns, IP reputation",
    "frequency": "updated hourly or on new threat",
    "contract": "threat intelligence data",
    "sla": "< 50ms lookup",
    "criticality": "TIER-2 (enhances detection)"
  },
  
  "SYSTEM_MONITORING_AGENT": {
    "sends": "system load, capacity, health",
    "frequency": "updated every 10 seconds",
    "contract": "current system metrics",
    "sla": "< 100ms lookup",
    "criticality": "TIER-2 (for adaptive limiting)"
  },
  
  "BEHAVIOR_ANALYTICS_AGENT": {
    "sends": "traffic baselines, anomaly scores",
    "frequency": "updated hourly",
    "contract": "baseline profiles per actor/tenant",
    "sla": "< 50ms lookup",
    "criticality": "TIER-2 (ML features)"
  }
}
```

### 11.2 Downstream Dependencies

```
DOWNSTREAM_AGENTS = {
  
  "API_GATEWAY_AGENT": {
    "consumes": "rate limit decision (ALLOW/BACKPRESSURE/REJECT)",
    "action": "forward, throttle, or block API request",
    "sla": "decision delivered < 5ms",
    "criticality": "TIER-1 (blocking customer requests)"
  },
  
  "NOTIFICATION_AGENT": {
    "consumes": "rate limit alerts, escalation events",
    "action": "notify user/admin of rate limit",
    "sla": "< 30 seconds",
    "use_case": "quota warnings, abuse alerts"
  },
  
  "OBSERVABILITY_AGENT": {
    "consumes": "rate limit metrics, decision logs",
    "action": "update dashboards, emit alerts",
    "sla": "< 10 seconds",
    "use_case": "SLA tracking, trend analysis"
  },
  
  "AUDIT_AGENT": {
    "consumes": "complete rate limit audit trail",
    "action": "store immutably, enable queries",
    "sla": "< 1 second async",
    "use_case": "compliance, forensics, debugging"
  },
  
  "BILLING_ENGINE_AGENT": {
    "consumes": "usage tracking, cost estimation",
    "action": "track API usage for billing",
    "sla": "< 1 second async",
    "use_case": "cost calculation, quota enforcement"
  },
  
  "THREAT_INTELLIGENCE_AGENT": {
    "consumes": "abuse pattern data, flagged actors",
    "action": "correlate with other signals",
    "sla": "< 1 minute async",
    "use_case": "threat hunting, attack pattern detection"
  },
  
  "DECISION_TRACEABILITY_AGENT": {
    "consumes": "rate limit decisions (for audit trail)",
    "action": "log as critical decision",
    "sla": "< 1 second async",
    "use_case": "governance, compliance"
  }
}
```

### 11.3 Event Triggers & Dependencies

```
TRIGGER_EVENT_GRAPH = {
  
  "api_request_received" (from API_GATEWAY): {
    "triggers": "rate_limit_check",
    "agent": "API_RATE_LIMIT_ENFORCEMENT_AGENT"
  },
  
  "rate_limit_decision_made": {
    "triggers": [
      "api_gateway_decision_event",
      "async_audit_logging",
      "async_metric_emission",
      "conditional_escalation"
    ]
  },
  
  "anomaly_detected" (anomaly_score > 0.7): {
    "triggers": [
      "llm_explanation_request",
      "threat_intelligence_alert",
      "notification_alert_to_admin",
      "escalation_to_platform_ops"
    ]
  },
  
  "quota_exceeded": {
    "triggers": [
      "notification_warning_to_user",
      "billing_cost_tracking",
      "metric_quota_exceeded_event"
    ]
  },
  
  "abuse_pattern_detected": {
    "triggers": [
      "threat_intelligence_investigation",
      "escalation_to_security_team",
      "actor_rate_limit_reduction",
      "audit_trail_escalation_flag"
    ]
  }
}
```

---

## 1️⃣2️⃣ PASSIVE INTELLIGENCE COMPATIBILITY

If a rate limit decision affects **user behavior or patterns**, the agent must emit structured features to the FEATURE_STORE_AGENT for downstream ML use.

```
TRIGGER_CONDITION = Rate limit decision affects user (BACKPRESSURE or REJECT)

FEATURE_EMISSION_SCHEMA = {
  "feature_vector": {
    "actor_id": "string (anonymized if needed)",
    "feature_name": "string (e.g., 'api_rate_limited', 'quota_exceeded')",
    "feature_value": "float | int | string",
    "feature_type": "categorical|numerical|temporal",
    "timestamp_utc": "ISO 8601",
    "source_agent": "API_RATE_LIMIT_ENFORCEMENT_AGENT",
    "source_decision_id": "UUID (decision that produced this feature)",
    "decision_action": "enum (ALLOW|BACKPRESSURE|REJECT)",
    "confidence_score": "float [0, 1]",
    "data_version": "string"
  }
}

EXAMPLE_FEATURES_EMITTED = {
  "rate_limit_exceeded": {
    "feature_name": "api_quota_exhausted_this_period",
    "feature_value": "1",
    "decision_action": "REJECT"
  },
  
  "backpressure_applied": {
    "feature_name": "api_experienced_backpressure",
    "feature_value": "1",
    "decision_action": "BACKPRESSURE"
  },
  
  "abuse_detected": {
    "feature_name": "actor_flagged_as_potentially_abusive",
    "feature_value": "1",
    "anomaly_score": "0.85"
  }
}

FEATURE_STORE_INTEGRATION =
  - Emit within 100ms of rate limit decision
  - Use for: anomaly detection, churn prediction, user behavior modeling
  - Retention: Feature store owns policy (typically 1+ years)
```

---

## 1️⃣3️⃣ GROWTH ENGINE HOOK

If a rate limit decision affects **legitimate progress or milestones** (user completes quota without abuse), emit achievement events.

```
TRIGGER_CONDITION = Actor remains under limits while consuming significant quota

ACHIEVEMENT_TRIGGER_EXAMPLES:
  - Actor uses 80% of daily quota (not abusive, just heavy user) → reward engagement
  - Bulk operation completes successfully within limits → reward legitimate bulk usage
  - Actor maintains clean rate-limit record (0 rejections in month) → reward compliance

ACHIEVEMENT_EVENTS = {
  
  "heavy_api_user_achievement": {
    "event_type": "achievement_unlocked",
    "actor_id": "string",
    "achievement_name": "Heavy API User",
    "achievement_description": "Used 80%+ of quota without violations",
    "reward_xp": 50,
    "timestamp": "ISO 8601"
  },
  
  "bulk_operation_success": {
    "event_type": "achievement_unlocked",
    "actor_id": "string",
    "achievement_name": "Bulk Processing Expert",
    "batch_size": "1000+",
    "reward_xp": 30,
    "timestamp": "ISO 8601"
  },
  
  "compliance_record": {
    "event_type": "badge_awarded",
    "actor_id": "string",
    "badge_name": "Rate Limit Compliant (30 Days)",
    "description": "Maintained clean rate limit record",
    "reward_xp": 100,
    "timestamp": "ISO 8601"
  }
}

EMISSION_TIMING =
  - Synchronous check: does this decision earn achievement?
  - Async emission: send to growth engine if earned
  - Ordering: per-actor, chronologically
  - Idempotency: decision_id prevents duplicate awards
```

---

## 1️⃣4️⃣ PERFORMANCE MONITORING

The agent must be observable and monitorable. It integrates with the OBSERVABILITY_AGENT for metrics and alerts.

### 14.1 Key Metrics

```
METRICS_TO_EMIT = {
  
  "rate_limit_decisions": {
    "decisions_total": "counter (cumulative)",
    "decisions_per_second": "gauge (RPS)",
    "decisions_allowed": "counter",
    "decisions_backpressured": "counter",
    "decisions_rejected": "counter",
    
    "decision_by_action_percent": "gauge (% ALLOW vs BACKPRESSURE vs REJECT)",
    
    "decision_latency_ms": {
      "p50": "gauge (median)",
      "p95": "gauge (95th percentile)",
      "p99": "gauge (99th percentile)",
      "p99_9": "gauge (99.9th percentile)",
      "max": "gauge (maximum observed)"
    }
  },
  
  "quota_metrics": {
    "quota_consumption_percent": "gauge (by tenant, domain)",
    "quota_exceeded_incidents": "counter",
    "quota_warning_threshold_breaches": "counter",
    
    "data_transfer_quota_used_mb": "gauge (monthly)",
    "data_transfer_quota_limit_mb": "gauge"
  },
  
  "anomaly_metrics": {
    "anomalies_detected": "counter",
    "anomaly_score_distribution": "histogram",
    "abuse_incidents": "counter",
    "abuse_score_distribution": "histogram",
    
    "anomaly_detection_accuracy": "gauge (precision, recall, F1)",
    "false_positive_rate": "gauge",
    "false_negative_rate": "gauge"
  },
  
  "model_metrics": {
    "model_inference_latency_ms": "histogram",
    "model_errors": "counter",
    "model_drift_detected": "counter",
    "model_version_active": "gauge"
  },
  
  "redis_metrics": {
    "redis_lookup_latency_ms": "histogram",
    "redis_errors": "counter",
    "redis_connection_pool_utilization": "gauge",
    "redis_memory_usage_percent": "gauge"
  },
  
  "system_metrics": {
    "instance_cpu_utilization": "gauge",
    "instance_memory_utilization": "gauge",
    "instance_network_io_mbps": "gauge",
    "instance_disk_io_iops": "gauge"
  },
  
  "escalation_metrics": {
    "escalations_triggered": "counter",
    "escalations_by_type": "gauge",
    "escalation_response_time_seconds": "histogram"
  }
}

METRIC_EMISSION_FREQUENCY = Every 10 seconds (Prometheus scrape interval)
RETENTION = 15 days (Prometheus), 1 year (long-term TSDB)
```

### 14.2 Alerting Rules

```
ALERTING_RULES = [
  {
    "alert_name": "HighDecisionLatency",
    "condition": "histogram_quantile(0.99, decision_latency_ms) > 5",
    "duration": "2 minutes",
    "severity": "CRITICAL",
    "action": "Page on-call immediately (SLA breach)"
  },
  {
    "alert_name": "HighRejectionRate",
    "condition": "rate(decisions_rejected[5m]) / rate(decisions_total[5m]) > 0.1",
    "duration": "5 minutes",
    "severity": "HIGH",
    "action": "Investigate (unexpected rejection rate)"
  },
  {
    "alert_name": "RedisUnavailable",
    "condition": "increase(redis_errors[1m]) > 100",
    "duration": "1 minute",
    "severity": "CRITICAL",
    "action": "Page on-call (rate limiting broken without Redis)"
  },
  {
    "alert_name": "ModelDriftDetected",
    "condition": "model_drift_detected > 0",
    "duration": "1 minute",
    "severity": "HIGH",
    "action": "Notify ML_OPS (model degradation)"
  },
  {
    "alert_name": "AnomalyDetectionFalsePositiveSpike",
    "condition": "false_positive_rate > 0.05",
    "duration": "10 minutes",
    "severity": "MEDIUM",
    "action": "Review false positives (may be degrading UX)"
  },
  {
    "alert_name": "MassiveAbuseDetected",
    "condition": "increase(abuse_incidents[5m]) > 100",
    "duration": "immediately",
    "severity": "CRITICAL",
    "action": "Page SECURITY_TEAM (coordinated attack detected)"
  }
]
```

---

## 1️⃣5️⃣ VERSIONING POLICY

All changes to this agent follow an **add-only, versioned, backward-compatible** policy.

```
VERSIONING_SCHEME = Semantic Versioning (MAJOR.MINOR.PATCH)

VERSION_HISTORY = {
  "v1.0.0": {
    "released_date": "2025-02-28",
    "status": "CURRENT_ACTIVE",
    "features": [
      "Token bucket rate limiting",
      "Tenant/domain isolation",
      "ML-based anomaly detection",
      "Adaptive rate limiting",
      "Complete audit trail"
    ],
    "breaking_changes": [],
    "deprecations": []
  }
}

CHANGE_MANAGEMENT_PROCESS = {
  "bug_fix": {
    "version_bump": "PATCH (e.g., v1.0.1)",
    "deployment": "Fast-track (within 24 hours if critical)",
    "backward_compatibility": "MUST maintain",
    "changelog": "Required"
  },
  
  "new_feature": {
    "version_bump": "MINOR (e.g., v1.1.0)",
    "deployment": "Standard (weekly releases)",
    "backward_compatibility": "MUST maintain (feature flag if needed)",
    "changelog": "Required",
    "testing": "Additional test coverage required"
  },
  
  "breaking_change": {
    "version_bump": "MAJOR (e.g., v2.0.0)",
    "deployment": "Coordinated with API_GATEWAY_AGENT",
    "backward_compatibility": "Migration period required (30 days notice)",
    "changelog": "Extensive",
    "migration_plan": "REQUIRED"
  },
  
  "model_update": {
    "versioning": "Model version stored in audit trail",
    "deployment": "Canary deployment (5% → 50% → 100%)",
    "monitoring": "A/B comparison with previous version",
    "rollback_plan": "Automatic rollback if latency increases > 10%"
  },
  
  "rate_limit_policy_change": {
    "versioning": "Policy version tracked per role/tenant",
    "deployment": "Gradual rollout (announce 7 days in advance)",
    "monitoring": "Close SLO tracking",
    "rollback_plan": "Ability to revert policy within 24 hours"
  }
}
```

---

## 1️⃣6️⃣ NON-NEGOTIABLE RULES

These rules are **absolute and cannot be violated under any circumstances**.

```
RULE_1: Agent MUST NOT bypass tenant isolation
  - Every rate limit check MUST verify actor belongs to tenant
  - Cross-tenant quota sharing is FORBIDDEN
  - Violation consequence: IMMEDIATE security incident, page SECURITY_TEAM

RULE_2: Agent MUST NOT modify historical audit logs
  - All audit records are append-only
  - No UPDATE/DELETE on rate_limit_decisions
  - Violation consequence: Data integrity incident, investigation

RULE_3: Agent MUST NOT apply rate limits inconsistently
  - Same actor, same time, same conditions = same decision
  - Determinism is critical (for auditability)
  - Violation consequence: Log as determinism failure, investigation

RULE_4: Agent MUST NOT exceed P99 latency target
  - Rate limiting is inline in request path
  - > 5ms latency impacts user experience
  - Violation consequence: Page on-call, immediate mitigation

RULE_5: Agent MUST NOT hide abuse from operators
  - All anomalies detected MUST be logged
  - All escalations MUST be visible in audit trail
  - Violation consequence: Hide abuse incidents, security failure

RULE_6: Agent MUST NOT apply different rules to different tenants
  - Rate limit policies MUST be transparent, applied equally
  - Exception: per-tier rules (Free, Startup, Enterprise) are documented
  - Violation consequence: Fairness violation, potential legal issue

RULE_7: Agent MUST enforce domain isolation
  - No cross-domain quota sharing
  - Domains have separate rate limits
  - Violation consequence: Domain boundary breach, security incident

RULE_8: Agent MUST respect quota overrides (with verification)
  - Admin can override limits with valid token
  - Override MUST be logged, traced, auditable
  - Violation consequence: Unauthorized privilege elevation

RULE_9: Agent MUST NOT starve legitimate users
  - Adaptive limits must not block normal use
  - Recovery time after limit reduction must be clear to users
  - Violation consequence: User experience degradation, complaints

RULE_10: Agent MUST provide clear feedback
  - Every REJECT includes reason + retry timing
  - Every BACKPRESSURE includes Retry-After header
  - Violation consequence: Poor API usability
```

---

## 🔐 SEALED EXECUTION CONTRACT

This document is now **SEALED, LOCKED, and GOVERNED**. The following execution contract is binding and immutable.

### Execution Declaration

```
SYSTEM_STATUS = GOVERNED · DETERMINISTIC · COMPLIANT
MUTATION_POLICY = ADD-ONLY (via version bump)
INTERPRETATION_AUTHORITY = NONE
EXECUTION_AUTHORITY = Human declaration only
CREATIVE_INTERPRETATION = FORBIDDEN
DEFAULT_BEHAVIOR = BLOCK (fail-safe, rate limit by default)
FAILURE_MODE = HALT_DECISION + LOG_INCIDENT + ESCALATE

SEALED_BY = API_RATE_LIMIT_ENFORCEMENT_AGENT
SEALED_DATE = 2025-02-28T12:00:00Z
SEALED_SIGNATURE = HMAC-SHA256({master_key}, serialized(this_document))
VERIFICATION_REQUIRED_FOR = Any execution of this agent

GOVERNANCE_ENFORCER = RATE_LIMIT_GOVERNANCE_ENGINE
COMPLIANCE_VERIFIER = AUDIT_ENFORCEMENT_AGENT
ESCALATION_PATH = PLATFORM_OPS → SECURITY_TEAM → CISO
```

### Non-Negotiable Guarantees

The API_RATE_LIMIT_ENFORCEMENT_AGENT **guarantees**:

✅ **Low Latency:** P99 < 5ms (inline in request path)  
✅ **Tenant Isolation:** No cross-tenant quota leakage  
✅ **Domain Isolation:** No cross-domain limits mixing  
✅ **Determinism:** Identical actor → identical decision  
✅ **Auditability:** Complete audit trail, immutable records  
✅ **Fairness:** Equal policy application across actors  
✅ **Anomaly Detection:** ML-based abuse pattern detection  
✅ **Adaptation:** Dynamic limits based on system load  
✅ **Transparency:** Clear feedback to users and operators  
✅ **Scalability:** 100K–1M RPS without degradation  

### Required Implementation Checklist

Before deployment, MUST verify:

- [ ] All validation rules implemented and tested
- [ ] Token bucket algorithm correct (atomic Redis Lua script)
- [ ] All ML models trained, validated, monitoring
- [ ] Redis cluster operational, failover working
- [ ] Cassandra append-only immutability enforced
- [ ] All security controls enabled and verified
- [ ] All audit logging in place and tested
- [ ] All escalation paths configured
- [ ] All downstream agents ready (dependency contracts met)
- [ ] All upstream agents sending valid events
- [ ] All monitoring and alerting configured
- [ ] P99 latency verified < 5ms under load
- [ ] All team training completed

### Deployment Gate

```
DEPLOYMENT_GATE = LOCKED until ALL checklist items complete

GATE_ENFORCEMENT_BY = DEPLOYMENT_AUTOMATION (cannot be overridden manually)

GATE_CHECKS = {
  "platform_ops_sign_off": "REQUIRED",
  "security_sign_off": "REQUIRED",
  "ml_ops_sign_off": "REQUIRED",
  "observability_sign_off": "REQUIRED",
  "latency_verification": "REQUIRED (P99 < 5ms)"
}

GATE_VERIFICATION_LOG = Immutable (appended to audit trail)
```

---

## 📌 FINAL NOTES

This document serves as the **complete, sealed specification** for the API_RATE_LIMIT_ENFORCEMENT_AGENT operating within the Ecoskiller Antigravity platform's Governance & Core Control layer.

**Key Principles:**
- **Latency Critical:** Every millisecond counts (inline in request path)
- **Tenant Safety:** Zero tolerance for cross-tenant isolation breaches
- **Fairness:** Same policies, transparent application
- **Auditability:** Every decision logged, immutable
- **Resilience:** Graceful degradation if dependencies fail
- **Intelligence:** ML-based adaptation, not static rules

**This agent is CRITICAL INFRASTRUCTURE** for API platform stability. It must be treated as such, with the highest standards of reliability, security, and governance.

---

**Document Status:** 🔐 SEALED · LOCKED · GOVERNED · DETERMINISTIC  
**Effective:** 2025-02-28  
**Next Review:** 2025-06-28 (quarterly)  
**Supersedes:** None (first release)  
**Approved By:** [PLATFORM_OPS, SECURITY_TEAM, ML_OPS_TEAM]

---

*End of API_RATE_LIMIT_ENFORCEMENT_AGENT Specification*
