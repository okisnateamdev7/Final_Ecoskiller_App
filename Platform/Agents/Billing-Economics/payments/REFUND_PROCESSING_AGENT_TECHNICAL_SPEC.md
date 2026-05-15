# REFUND_PROCESSING_AGENT
## Technical Implementation Specification
### For Ecoskiller Enterprise SaaS Platform

---

## 1. SYSTEM OVERVIEW

**Classification:** Autonomous Financial Processor (Sealed & Locked)  
**Platform:** Ecoskiller Enterprise SaaS  
**Runtime:** Kubernetes microservice (Node.js or Spring Boot)  
**Namespace:** `billing`  
**Domain:** Subscription Billing & Refund Management

---

## 2. EXECUTION GUARANTEES (IMMUTABLE)

```
EXECUTION_MODE = LOCKED
MUTATION_POLICY = DENY_ALL
DEFAULT_BEHAVIOR = DENY
FAILURE_MODE = STOP_EXECUTION
SEMANTIC_RULES = SPECIFICATION_ONLY (no creative interpretation)
```

**No assumptions. No discretion. No human override.**

---

## 3. SERVICE DEPENDENCIES

### Inbound API Calls

| Service | Endpoint | Purpose | Timeout |
|---------|----------|---------|---------|
| User Service | GET /users/{user_id} | Validate user exists + tenant | 5s |
| Payment Gateway | POST /payments/refund | Process refund transaction | 30s |
| Notification Service | POST /notifications/send | Email receipt | 5s (async) |
| Admin Governance | GET /moderation/user/{user_id} | Check fraud flags | 3s |

### Event Stream Integration

| Topic | Direction | Purpose | Retention |
|-------|-----------|---------|-----------|
| `billing.refund.requested` | Inbound | Refund request trigger | 7 days |
| `billing.refund.completed` | Outbound | Success notification | 30 days |
| `billing.refund.rejected` | Outbound | Denial notification | 30 days |
| `billing.refund.error` | Outbound | System error alert | 90 days |

### Database Access

| Table | Access | Purpose |
|-------|--------|---------|
| `subscriptions` | READ | Validate subscription status |
| `billing_ledger` | READ | Lookup invoice details |
| `refund_ledger` | INSERT (append-only) | Record refund decision |
| `refund_policies` | READ (cached) | Lookup window + percentage |
| `moderation_flags` | READ | Check user fraud status |
| `payment_methods` | READ | Get payment processor info |

### Cache Layer (Redis)

```
refund_policies:{reason} → TTL 3600s (policy lookup cache)
rate_limit:{user_id}:{hour} → sliding window counter
```

---

## 4. REQUEST/RESPONSE SCHEMA

### Inbound: `billing.refund.requested`

```json
{
  "subscription_id": "uuid-v4",
  "invoice_id": "uuid-v4",
  "refund_reason": "network_failure | service_unavailable | billing_error | duplicate_charge | cancellation_request",
  "requested_amount": 199.99,
  "request_timestamp": "2026-03-04T15:30:00Z",
  "user_id": "uuid-v4"
}
```

**Validation Requirements:**
- All fields mandatory
- `requested_amount` > 0 and ≤ invoice.total
- `request_timestamp` in ISO 8601 format
- `refund_reason` is exact enum match

### Outbound: `billing.refund.completed`

```json
{
  "event_type": "refund.completed",
  "refund_id": "uuid-v4",
  "subscription_id": "uuid-v4",
  "user_id": "uuid-v4",
  "invoice_id": "uuid-v4",
  "amount": 199.99,
  "refund_reason": "network_failure",
  "status": "COMPLETED",
  "payment_method": "card",
  "transaction_id": "stripe_refund_id",
  "timestamp": "2026-03-04T15:31:22Z",
  "processing_duration_ms": 1250
}
```

### Outbound: `billing.refund.rejected`

```json
{
  "event_type": "refund.rejected",
  "refund_id": null,
  "subscription_id": "uuid-v4",
  "user_id": "uuid-v4",
  "invoice_id": "uuid-v4",
  "requested_amount": 199.99,
  "rejection_code": "OUTSIDE_WINDOW | ALREADY_REFUNDED | USER_FRAUD_FLAGGED | INVALID_SUBSCRIPTION",
  "rejection_detail": "Request timestamp 2026-03-15 exceeds 30-day window from invoice date 2026-01-01",
  "timestamp": "2026-03-04T15:31:22Z"
}
```

---

## 5. REFUND POLICY TABLE

**Table:** `refund_policies`

| refund_reason | max_window_days | refund_percentage | created_at | updated_at |
|---|---|---|---|---|
| network_failure | 30 | 100 | ... | ... |
| service_unavailable | 7 | 100 | ... | ... |
| billing_error | 14 | 100 | ... | ... |
| duplicate_charge | 60 | 100 | ... | ... |
| cancellation_request | 14 | prorated | ... | ... |

**Prorated Logic:** `(days_remaining / total_days_in_billing_period) × invoice_amount`

---

## 6. REFUND LEDGER SCHEMA

**Table:** `refund_ledger` (Append-Only, ACID)

```sql
CREATE TABLE refund_ledger (
  refund_id UUID PRIMARY KEY,
  subscription_id UUID NOT NULL,
  user_id UUID NOT NULL,
  tenant_id UUID NOT NULL,
  invoice_id UUID NOT NULL,
  refund_reason VARCHAR(50) NOT NULL,
  requested_amount DECIMAL(12,2) NOT NULL,
  calculated_amount DECIMAL(12,2) NOT NULL,
  status VARCHAR(20) NOT NULL, -- PENDING, COMPLETED, FAILED_INVALID_REQUEST, PENDING_RETRY
  payment_gateway_response JSONB,
  created_at TIMESTAMP NOT NULL DEFAULT NOW(),
  processed_at TIMESTAMP,
  CONSTRAINT fk_subscription FOREIGN KEY (subscription_id) REFERENCES subscriptions,
  CONSTRAINT fk_invoice FOREIGN KEY (invoice_id) REFERENCES billing_ledger,
  CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users
);

CREATE INDEX idx_refund_ledger_user_id ON refund_ledger(user_id);
CREATE INDEX idx_refund_ledger_invoice_id ON refund_ledger(invoice_id);
CREATE INDEX idx_refund_ledger_status ON refund_ledger(status);
```

**Row-Level Security (RLS):** Users can only see their own refunds (tenant_id isolation)

---

## 7. DETERMINISTIC PROCESSING ALGORITHM

### Phase 1: Input Validation (Atomic)

```javascript
async function validatePayload(request) {
  if (!request.subscription_id) throw INVALID_PAYLOAD("subscription_id missing");
  if (!request.invoice_id) throw INVALID_PAYLOAD("invoice_id missing");
  if (!request.refund_reason) throw INVALID_PAYLOAD("refund_reason missing");
  if (!Object.values(REFUND_REASONS).includes(request.refund_reason)) 
    throw INVALID_PAYLOAD("refund_reason not enumerated");
  if (request.requested_amount <= 0 || request.requested_amount > MAX_DECIMAL)
    throw INVALID_PAYLOAD("requested_amount invalid");
  if (!isISO8601(request.request_timestamp))
    throw INVALID_PAYLOAD("request_timestamp not ISO 8601");
  
  const subscription = await db.subscriptions.findById(request.subscription_id);
  if (!subscription) throw NOT_FOUND("subscription_id");
  if (subscription.tenant_id !== request.user_id.tenant_id)
    throw FORBIDDEN("tenant mismatch");
  
  const invoice = await db.billing_ledger.findById(request.invoice_id);
  if (!invoice) throw NOT_FOUND("invoice_id");
  if (invoice.status !== "PAID") throw FORBIDDEN("invoice not paid");
  if (invoice.amount < request.requested_amount)
    throw BAD_REQUEST("requested_amount > invoice.amount");
  
  return { subscription, invoice };
}
```

### Phase 2: Policy Compliance (Deterministic Window Check)

```javascript
async function checkPolicy(invoice, refundReason, requestTimestamp) {
  const policy = await getPolicy(refundReason); // cached in Redis
  
  const timeElapsed = requestTimestamp - invoice.created_at;
  const maxWindow = policy.max_window_days * 86400000; // ms
  
  if (timeElapsed > maxWindow) {
    return { allowed: false, code: "OUTSIDE_WINDOW", detail: `${timeElapsed}ms > ${maxWindow}ms` };
  }
  
  const existing = await db.refund_ledger.findByInvoiceId(invoice.id);
  if (existing.length > 0) {
    return { allowed: false, code: "ALREADY_REFUNDED", detail: `refund_id: ${existing[0].id}` };
  }
  
  const fraudFlag = await governanceService.checkFraud(userId);
  if (fraudFlag) {
    return { allowed: false, code: "USER_FRAUD_FLAGGED", detail: "User marked as fraudulent" };
  }
  
  return { allowed: true, policy };
}
```

### Phase 3: Amount Calculation (Deterministic)

```javascript
function calculateRefundAmount(invoice, reason, policy, requestedAmount) {
  if (reason === "cancellation_request") {
    const daysRemaining = (invoice.billing_period_end - Date.now()) / 86400000;
    const totalDays = (invoice.billing_period_end - invoice.billing_period_start) / 86400000;
    const prorated = (daysRemaining / totalDays) * invoice.amount;
    return Math.round(prorated * 100) / 100; // ROUND_HALF_UP
  }
  
  const policyAmount = (policy.refund_percentage / 100) * invoice.amount;
  const refundAmount = Math.min(requestedAmount, policyAmount);
  return Math.round(refundAmount * 100) / 100;
}
```

### Phase 4: Ledger Write (ACID Transaction)

```javascript
async function writeLedger(subscription, invoice, refundAmount, reason) {
  return await db.transaction(async (trx) => {
    const refund = await trx.refund_ledger.insert({
      refund_id: uuidv4(),
      subscription_id: subscription.id,
      user_id: subscription.user_id,
      tenant_id: subscription.tenant_id,
      invoice_id: invoice.id,
      refund_reason: reason,
      requested_amount: refundAmount, // from request
      calculated_amount: refundAmount, // after policy
      status: "PENDING",
      created_at: new Date()
    });
    return refund;
  });
}
```

### Phase 5: Payment Gateway Integration

```javascript
async function processPayment(refund, invoice, paymentMethod) {
  const gateway = getPaymentGateway(paymentMethod.processor);
  
  let response;
  try {
    response = await gateway.refund({
      refund_id: refund.id,
      original_transaction_id: invoice.transaction_id,
      amount: refund.calculated_amount,
      reason: refund.refund_reason,
      timeout: 30000,
      retries: 3
    });
  } catch (err) {
    if (err.statusCode >= 500) {
      await db.refund_ledger.update(refund.id, { status: "PENDING_RETRY" });
      scheduleRetry(refund.id, delayMs = 3600000); // 1 hour
      throw TEMPORARY_FAILURE(err.message);
    }
    await db.refund_ledger.update(refund.id, { status: "FAILED_INVALID_REQUEST" });
    throw PERMANENT_FAILURE(err.message);
  }
  
  if (response.status === 200) {
    await db.refund_ledger.update(refund.id, { 
      status: "COMPLETED", 
      payment_gateway_response: response,
      processed_at: new Date()
    });
    return { success: true };
  }
  
  throw PAYMENT_FAILURE(response.message);
}
```

### Phase 6: Event Emission

```javascript
async function emitEvent(refund, status) {
  const event = {
    event_type: status === "COMPLETED" ? "refund.completed" : "refund.rejected",
    refund_id: refund.id,
    subscription_id: refund.subscription_id,
    user_id: refund.user_id,
    invoice_id: refund.invoice_id,
    amount: refund.calculated_amount,
    timestamp: new Date().toISOString(),
    status
  };
  
  await kafka.publish("billing.refund.completed", event);
  
  // Non-blocking; refund already valid in ledger
}
```

---

## 8. ERROR HANDLING (Deterministic)

### Error Codes

| Code | HTTP | Recovery | Example |
|------|------|----------|---------|
| `INVALID_PAYLOAD` | 400 | None (log & reject) | Missing `subscription_id` |
| `SUBSCRIPTION_NOT_FOUND` | 404 | None (invalid request) | subscription_id doesn't exist |
| `OUTSIDE_WINDOW` | 403 | None (policy hard stop) | Request 45 days after invoice |
| `ALREADY_REFUNDED` | 409 | None (conflict) | Refund entry exists |
| `USER_FRAUD_FLAGGED` | 403 | Admin intervention only | Moderation flagged user |
| `PAYMENT_GATEWAY_TIMEOUT` | 503 | Retry in 1 hour | Gateway unresponsive |
| `DATABASE_ERROR` | 500 | Retry (exponential backoff) | Connection pool exhausted |

### Retry Strategy

**Idempotent Retries (via refund_id):**
- Payment gateway calls use `refund_id` as idempotency key
- Duplicate requests to same `refund_id` return cached result (no double-refund)

**Non-Retryable Errors:**
- Validation failures (400s)
- Policy denials (403s)
- Duplicate refunds (409s)

---

## 9. KUBERNETES DEPLOYMENT

### StatefulSet Definition

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: billing-refund-agent
  namespace: billing
spec:
  replicas: 3
  strategy:
    type: RollingUpdate
    rollingUpdate:
      maxUnavailable: 1
      maxSurge: 1
  selector:
    matchLabels:
      app: billing-refund-agent
  template:
    metadata:
      labels:
        app: billing-refund-agent
    spec:
      containers:
      - name: agent
        image: ecoskiller/billing-refund-agent:1.0.0
        imagePullPolicy: Always
        ports:
        - containerPort: 8080 (readiness/liveness)
        - containerPort: 9090 (metrics)
        env:
        - name: NODE_ENV
          value: "production"
        - name: DATABASE_URL
          valueFrom:
            secretKeyRef:
              name: billing-secrets
              key: database-url
        - name: KAFKA_BROKERS
          value: "kafka-0.kafka-headless.kafka.svc.cluster.local:9092"
        - name: REDIS_URL
          value: "redis://redis-master.cache.svc.cluster.local:6379"
        livenessProbe:
          httpGet:
            path: /health/live
            port: 8080
          initialDelaySeconds: 30
          periodSeconds: 10
          timeoutSeconds: 5
        readinessProbe:
          httpGet:
            path: /health/ready
            port: 8080
          initialDelaySeconds: 10
          periodSeconds: 5
          timeoutSeconds: 3
        resources:
          requests:
            cpu: 500m
            memory: 512Mi
          limits:
            cpu: 2000m
            memory: 1Gi
      affinity:
        podAntiAffinity:
          preferredDuringSchedulingIgnoredDuringExecution:
          - weight: 100
            podAffinityTerm:
              labelSelector:
                matchExpressions:
                - key: app
                  operator: In
                  values:
                  - billing-refund-agent
              topologyKey: kubernetes.io/hostname
```

### Horizontal Auto-Scaling

```yaml
apiVersion: autoscaling/v2
kind: HorizontalPodAutoscaler
metadata:
  name: billing-refund-agent-hpa
spec:
  scaleTargetRef:
    apiVersion: apps/v1
    kind: Deployment
    name: billing-refund-agent
  minReplicas: 3
  maxReplicas: 10
  metrics:
  - type: Resource
    resource:
      name: cpu
      target:
        type: Utilization
        averageUtilization: 70
  - type: Resource
    resource:
      name: memory
      target:
        type: Utilization
        averageUtilization: 75
```

---

## 10. MONITORING & ALERTING

### Prometheus Metrics

```
# Counter: Total refund requests processed
refund_requests_total{reason, status} 

# Histogram: Processing duration (ms)
refund_processing_duration_ms{reason, quantile}

# Gauge: Current rejection rate (%)
refund_rejection_rate{reason}

# Gauge: Payment gateway latency (ms)
payment_gateway_latency_ms{processor}

# Counter: Ledger write errors
refund_ledger_errors_total

# Gauge: Queue depth (pending messages in Kafka topic)
billing_refund_queue_depth
```

### Grafana Dashboard

**Key Metrics:**
- Refunds processed per minute (by reason)
- P50, P95, P99 latency
- Error rate (% of requests returning 4xx/5xx)
- Payment gateway success rate
- Ledger write latency

### Alert Rules (Prometheus)

```yaml
- alert: HighRefundRejectionRate
  expr: refund_rejection_rate > 0.05 # > 5%
  for: 5m
  annotations:
    summary: "Refund rejection rate exceeds 5%"

- alert: PaymentGatewayLatency
  expr: payment_gateway_latency_ms{quantile="0.99"} > 30000
  for: 5m
  annotations:
    summary: "Payment gateway P99 latency > 30s"

- alert: RefundLedgerErrors
  expr: rate(refund_ledger_errors_total[5m]) > 0.1
  for: 2m
  annotations:
    summary: "Refund ledger write errors > 0.1 req/s"

- alert: KafkaQueueDepth
  expr: billing_refund_queue_depth > 10000
  for: 10m
  annotations:
    summary: "Pending refund requests queued > 10,000"
```

---

## 11. SECURITY & COMPLIANCE

### Data Protection

- **At Rest:** PostgreSQL encrypted with KMS key (AWS, GCP, etc.)
- **In Transit:** TLS 1.3 for all external API calls
- **Secrets:** HashiCorp Vault (payment gateway credentials, API keys)
- **Row-Level Security:** PostgreSQL RLS enforces tenant isolation

### Rate Limiting

- **Per User:** 10 refund requests per hour (sliding window, Redis)
- **Per Tenant:** 1000 refund requests per day (sliding window)
- **Per IP:** 100 requests per minute (ingress controller)

### Audit Trail

- All refund events logged to Loki (structured JSON)
- Immutable append-only ledger in PostgreSQL
- Long-term archival to S3 (WORM: write-once-read-many)
- Compliance queries available for auditors

### Signature Verification

- All inbound Kafka events verified with HMAC-SHA256
- AWS Signature Version 4 for HTTP API calls
- Mutual TLS for payment gateway integration

---

## 12. TESTING STRATEGY

### Unit Tests (Jest)

```javascript
describe("validatePayload", () => {
  it("should reject missing subscription_id", () => {
    expect(() => validatePayload({})).toThrow(INVALID_PAYLOAD);
  });
  
  it("should reject invalid refund_reason", () => {
    expect(() => validatePayload({
      subscription_id: uuid,
      refund_reason: "invalid_reason"
    })).toThrow(INVALID_PAYLOAD);
  });
});

describe("calculateRefundAmount", () => {
  it("should calculate prorated cancellation correctly", () => {
    const amount = calculateRefundAmount(
      { amount: 100, billing_period_end: Date.now() + 7 * 86400000 },
      "cancellation_request",
      { refund_percentage: 100 },
      100
    );
    expect(amount).toBeCloseTo(50, 2); // 7/14 days
  });
});
```

### Integration Tests (Docker Compose)

- PostgreSQL + Redis + Kafka running locally
- Full refund flow end-to-end
- Payment gateway mock (responses: 200, 4xx, 5xx, timeout)
- Ledger verification post-refund

### Load Testing (k6)

```javascript
import http from 'k6/http';
import { check } from 'k6';

export const options = {
  stages: [
    { duration: '30s', target: 10 },   // 10 concurrent users
    { duration: '2m', target: 100 },   // 100 users
    { duration: '30s', target: 0 },    // ramp down
  ],
};

export default function () {
  const payload = {
    subscription_id: __VU, // unique per virtual user
    invoice_id: 'inv-123',
    refund_reason: 'network_failure',
    requested_amount: 99.99,
    request_timestamp: new Date().toISOString(),
    user_id: 'user-xyz'
  };
  
  const res = http.post('http://localhost:8080/refund', payload);
  check(res, { 'status is 200 or 4xx': (r) => r.status === 200 || r.status >= 400 });
}
```

---

## 13. DEPLOYMENT CHECKLIST

- [ ] All environment variables configured (DB, Kafka, Vault, payment gateway)
- [ ] PostgreSQL schema migrated (Flyway)
- [ ] Redis cache warmed with refund policies
- [ ] Kafka topics created (`billing.refund.{requested,completed,rejected,error}`)
- [ ] Certificate rotation scheduled (TLS, HMAC signing keys)
- [ ] Grafana dashboards deployed
- [ ] Prometheus scrape configs updated
- [ ] Alert manager notification channels tested
- [ ] Backup & disaster recovery tested
- [ ] Load test passed (>1000 req/s sustained)
- [ ] Security audit completed
- [ ] Canary deployment to staging (blue/green)
- [ ] Smoke tests passed in staging
- [ ] Canary deployment to production (5% traffic, 1h observation)
- [ ] Full production rollout

---

## 14. OPERATIONAL RUNBOOKS

### Scenario: High Refund Rejection Rate

1. Check Prometheus: `refund_rejection_rate` metric
2. Query logs: `kubectl logs -n billing -l app=billing-refund-agent | grep DENY`
3. Identify reason: OUTSIDE_WINDOW? FRAUD_FLAG? POLICY_CHANGE?
4. If policy change: Update `refund_policies` table, invalidate cache
5. If fraud flags false positive: Contact Admin Governance Service

### Scenario: Payment Gateway Timeout

1. Check payment gateway status page
2. If down: Agent auto-retries in 1 hour (PENDING_RETRY status)
3. Monitor `refund_ledger` for stuck PENDING refunds
4. Manual intervention: Update status to COMPLETED if confirmed processed externally

### Scenario: Database Connection Exhausted

1. Check PostgreSQL connection pool: `SELECT * FROM pg_stat_activity;`
2. If full: Scale up read replicas or adjust pool size
3. Restart agent pods (rolling restart, 1 at a time)
4. Verify health checks pass before rolling back

---

## 15. VERSION & CHANGE CONTROL

### Semantic Versioning

- **MAJOR (1.0.0 → 2.0.0):** Breaking policy changes (new refund_reason, window changes)
- **MINOR (1.0.0 → 1.1.0):** New features (new payment processor support)
- **PATCH (1.0.0 → 1.0.1):** Bug fixes

### Change Management

All changes go through:
1. Code review (2 approvals minimum)
2. Automated testing (unit + integration)
3. Staging deployment + smoke tests
4. Canary deployment to production (5% traffic, 1h)
5. Full rollout (if no errors)
6. Rollback plan documented (if needed)

---

**SEALED. NO MODIFICATIONS. NO CREATIVE INTERPRETATION.**

Version: 1.0.0 | Generated: 2026-03-04
