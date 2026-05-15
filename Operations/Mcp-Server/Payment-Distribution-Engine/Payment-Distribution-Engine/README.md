# Payment Distribution Engine - MCP Server (Secure Java Implementation)

**Ecoskiller | CAT-18 — Payment Distribution & Multi-Party Payout Processing**  
MCP Server in Java | 30+ Tools | Priority: TIER 1 CRITICAL | Security: Enterprise-Grade

---

## Table of Contents

1. [Overview](#overview)
2. [Architecture](#architecture)
3. [Security Features](#security-features)
4. [Installation & Setup](#installation--setup)
5. [Building the Project](#building-the-project)
6. [Docker Deployment](#docker-deployment)
7. [Kubernetes Deployment](#kubernetes-deployment)
8. [Tools & APIs](#tools--apis)
9. [Configuration](#configuration)
10. [Monitoring & Logging](#monitoring--logging)
11. [Testing](#testing)
12. [Troubleshooting](#troubleshooting)

---

## Overview

The **Payment Distribution Engine** is a mission-critical MCP (Model Context Protocol) server that manages:

- **Multi-Party Payment Splits** - Calculate and distribute payments across candidates, coaches, mentors, franchises, government programs, and CSR initiatives
- **Settlement Orchestration** - Manage fund aggregation, escrow, and settlement across 40+ payment gateways
- **Compliance & Tax** - Automatic GST, TDS, and income tax calculations with regulatory reporting
- **Fraud Detection** - Real-time fraud scoring and AML/KYC compliance checks
- **Double-Entry Ledger** - Immutable audit logs in ClickHouse for 3-year regulatory retention
- **Multi-Currency FX** - Support 40+ currencies with real-time FX rate management

**Service Classification:**
- **Tier:** Tier 1 (Critical Revenue Path)
- **Language:** Java (JDK 11+)
- **Protocol:** MCP 2.0 (JSON-RPC 2.0 over WebSocket)
- **Response Time SLA:** < 500ms (split calculation), < 2s (settlement)
- **Availability:** 99.9% (< 43 min/month downtime)

---

## Architecture

### Component Diagram

```
┌─────────────────────────────────────────────────────────────┐
│           Claude / External MCP Clients                      │
│           (JSON-RPC 2.0 over WebSocket)                     │
└────────────────────┬────────────────────────────────────────┘
                     │
        ┌────────────▼────────────┐
        │  MCPPaymentServer       │
        │  (WebSocket Listener)   │
        └────────────┬────────────┘
                     │
        ┌────────────▼──────────────────────────────┐
        │  MCP Request Router & Security Validator  │
        │  - JWT Token Validation (Keycloak)       │
        │  - Rate Limiting (Token Bucket)          │
        │  - RBAC (Role-Based Access Control)      │
        │  - Audit Logging                         │
        └────┬──────────────────────────────────────┘
             │
     ┌───────┴───────┬────────────┬─────────────┬──────────────┐
     │               │            │             │              │
  ▼──────────┐  ▼─────────┐  ▼────────┐  ▼──────────┐  ▼─────────┐
  │Payment   │  │Settlement│  │Ledger │  │Fraud     │  │Compliance│
  │Split     │  │Orchestr. │  │Manager│  │Detection │  │Engine    │
  │Calculator│  │          │  │       │  │          │  │          │
  └──┬───────┘  └────┬─────┘  └───┬───┘  └────┬─────┘  └────┬─────┘
     │               │            │           │            │
     └───────────────┼────────────┼───────────┼────────────┘
                     │
        ┌────────────▼────────────┐
        │  Database Layer         │
        │  - PostgreSQL (Transact)│
        │  - ClickHouse (Audit)   │
        │  - Redis (Cache)        │
        └─────────────────────────┘
```

### Data Flow

1. **Client Request** → MCP Server via WebSocket
2. **Authentication** → Validate JWT Token from Keycloak
3. **Authorization** → Check RBAC roles (recruiter, finance_admin, platform_admin)
4. **Rate Limiting** → Token bucket algorithm (600 req/min per client)
5. **Encryption** → AES-256 for sensitive fields
6. **Processing** → Route to appropriate payment engine
7. **Persistence** → Write to PostgreSQL (transactional) + ClickHouse (audit)
8. **Response** → JSON-RPC 2.0 response to client

---

## Security Features

### 1. Authentication (JWT Tokens)

- **Keycloak Integration** - OAuth2/OIDC with Keycloak 24.0
- **Token Validation** - Verify signature, expiry, and claims
- **Short-Lived Tokens** - 5-minute expiry time (recommended)
- **Token Caching** - Cache validated tokens to reduce load

```java
// Example JWT payload
{
  "sub": "user@example.com",
  "tenant_id": "tenant_123",
  "roles": ["recruiter"],
  "exp": 1704067200,
  "iat": 1704066900
}
```

### 2. Authorization (RBAC)

- **Role-Based Access Control** - Fine-grained permission model
- **Tenant Isolation** - Data strictly scoped by tenant_id
- **Role Hierarchy:**
  - `recruiter` - Can initiate payments, view own settlements
  - `finance_admin` - Can run reconciliation, investigate discrepancies
  - `platform_admin` - Full access to all endpoints
  - `finance_readonly` - Read-only access to ledger and reports

### 3. Data Encryption

- **In Transit:** HTTPS/TLS 1.3 for all API communication
- **At Rest:** AES-256 encryption for sensitive fields
  - Bank account numbers
  - Aadhaar/PAN (PII)
  - Password hashes
  
```java
// Encryption example
private String encryptSensitiveField(String plaintext, SecretKey key) throws Exception {
    Cipher cipher = Cipher.getInstance("AES");
    cipher.init(Cipher.ENCRYPT_MODE, key);
    byte[] encrypted = cipher.doFinal(plaintext.getBytes(StandardCharsets.UTF_8));
    return Base64.getEncoder().encodeToString(encrypted);
}
```

### 4. Request Signing (HMAC-SHA256)

- **Request Signature** - Prevent tampering and replay attacks
- **Signature Header:** `X-Request-Signature: HMAC-SHA256(body, secret)`

### 5. Rate Limiting

- **Token Bucket Algorithm** - 600 requests/minute per client
- **Per-Client Tracking** - Separate limit for each client_id
- **Graceful Degradation** - 429 status when limit exceeded

### 6. Audit Logging

- **Immutable Audit Trail** - All actions logged to ClickHouse
- **What's Logged:**
  - API calls (endpoint, parameters, response)
  - Security events (auth failures, unauthorized access)
  - Data modifications (who changed what, when)
  - Payment operations (splits, settlements, reconciliations)
- **Retention:** 3 years (regulatory requirement)
- **Asynchronous Processing** - Non-blocking audit queue

### 7. Fraud Detection

- **Real-Time Scoring** - Calculate risk score for each transaction
- **Rules Engine:**
  - Duplicate detection (same recipient within 60 seconds)
  - Velocity monitoring (max 10 payouts/hour per recipient)
  - Amount anomaly (Z-score > 3)
  - KYC/PEP screening (for amounts > ₹50k)
  - Geo-velocity (block high-risk countries)
- **Scoring:** 0-100 scale (< 30 = proceed, 30-70 = review, > 70 = block)

### 8. Compliance

- **Tax Withholding:**
  - GST: 5% on assessment services
  - TDS: 10% on contractor payments > ₹50k
  - Income Tax: 30% on high-value payments > ₹5 lakh
- **AML/KYC:** PAN/Aadhaar verification, OFAC screening
- **SAR Filing:** Automated Suspicious Activity Reports
- **DPDPA 2023:** Personal data protection and consent management

### 9. Container Security

- **Non-Root User** - Runs as `appuser` (UID 1000)
- **Read-Only Filesystem** - Except for `/app/logs` and `/tmp`
- **Dropped Capabilities** - No CAP_SYS_ADMIN or other dangerous caps
- **Security Context:**
  ```yaml
  securityContext:
    runAsNonRoot: true
    runAsUser: 1000
    allowPrivilegeEscalation: false
    readOnlyRootFilesystem: true
  ```

### 10. Network Security

- **Network Policies** - Pod-level ingress/egress rules
- **Service Account RBAC** - Kubernetes role-based access
- **Secrets Management** - Encrypted Kubernetes secrets

---

## Installation & Setup

### Prerequisites

- Java 11+ (OpenJDK or Oracle JDK)
- Maven 3.8+
- PostgreSQL 15+
- ClickHouse 23.0+
- Redis 7+
- Docker & Docker Compose (optional)
- Kubernetes cluster (for k3s deployment)

### Clone Repository

```bash
git clone https://github.com/ecoskiller/payment-distribution-engine-mcp.git
cd payment-distribution-engine-mcp
```

### Create Environment File

```bash
# .env file
DATABASE_URL=jdbc:postgresql://localhost:5432/ecoskiller
DATABASE_USER=payment_user
DATABASE_PASSWORD=secure_password_here

CLICKHOUSE_HOST=localhost
CLICKHOUSE_PORT=8123
CLICKHOUSE_USER=default
CLICKHOUSE_PASSWORD=secure_password_here

RAZORPAY_API_KEY=rzp_test_XXXXXXXXXXXX
RAZORPAY_API_SECRET=test_secret_XXXXXXXXXXXX

WISE_API_TOKEN=test_XXXXXXXXXXXX
XE_COM_API_KEY=test_XXXXXXXXXXXX

KEYCLOAK_URL=https://keycloak.example.com
KEYCLOAK_REALM=ecoskiller
KEYCLOAK_CLIENT_ID=payment-engine
KEYCLOAK_CLIENT_SECRET=secure_secret

LOG_LEVEL=INFO
MCP_PORT=9001
```

### Initialize Databases

#### PostgreSQL

```bash
# Create database
createdb ecoskiller

# Create tables
psql -d ecoskiller < database/schema.sql

# Create user
psql -d ecoskiller -c "CREATE USER payment_user WITH PASSWORD 'secure_password';"
psql -d ecoskiller -c "GRANT ALL PRIVILEGES ON DATABASE ecoskiller TO payment_user;"
```

#### ClickHouse

```bash
# Create database
clickhouse-client --query "CREATE DATABASE IF NOT EXISTS audit;"

# Create audit tables
clickhouse-client -d audit < database/clickhouse-schema.sql

# Create user
clickhouse-client --query "CREATE USER audit_user IDENTIFIED BY 'secure_password';"
```

---

## Building the Project

### Build with Maven

```bash
# Clean build
mvn clean package

# Build with tests
mvn clean package -DskipTests=false

# Build with security scanning
mvn clean package org.owasp:dependency-check-maven:check

# View dependency tree
mvn dependency:tree
```

### Output

```
target/
├── payment-distribution-engine-mcp-1.0.0-SECURE.jar  (regular JAR)
├── payment-distribution-engine-mcp-1.0.0-SECURE-uber.jar (with dependencies)
└── ...
```

### Running Locally

```bash
# Set environment variables
export DATABASE_URL=jdbc:postgresql://localhost:5432/ecoskiller
export DATABASE_USER=payment_user
export DATABASE_PASSWORD=secure_password
export CLICKHOUSE_HOST=localhost

# Run the server
java -jar target/payment-distribution-engine-mcp-1.0.0-SECURE-uber.jar 9001

# Output:
# Payment Distribution Engine MCP Server listening on port 9001
```

---

## Docker Deployment

### Build Docker Image

```bash
# Build image
docker build -t ecoskiller/payment-distribution-engine-mcp:1.0.0-secure .

# Verify image
docker images | grep payment-distribution
```

### Run Container

```bash
docker run -d \
  --name payment-engine \
  --network billing-network \
  -p 9001:9001 \
  -e DATABASE_URL=jdbc:postgresql://postgres:5432/ecoskiller \
  -e DATABASE_USER=payment_user \
  -e DATABASE_PASSWORD=secure_password \
  -e CLICKHOUSE_HOST=clickhouse \
  -e LOG_LEVEL=INFO \
  ecoskiller/payment-distribution-engine-mcp:1.0.0-secure

# Check logs
docker logs -f payment-engine

# Health check
curl http://localhost:9001/actuator/health
```

### Docker Compose

```bash
docker-compose up -d

# Verify services
docker-compose ps

# View logs
docker-compose logs -f payment-engine
```

---

## Kubernetes Deployment

### Deploy to k3s

```bash
# Create namespace
kubectl create namespace billing

# Apply ConfigMaps and Secrets
kubectl apply -f kubernetes-deployment.yaml

# Verify deployment
kubectl get pods -n billing
kubectl get svc -n billing

# Check logs
kubectl logs -n billing deployment/payment-distribution-engine -f

# Port forward for testing
kubectl port-forward -n billing svc/payment-engine-service 9001:9001
```

### Monitoring

```bash
# Check pod status
kubectl describe pod -n billing <pod-name>

# View metrics
kubectl top pods -n billing

# Check HPA status
kubectl get hpa -n billing payment-engine-hpa

# View events
kubectl get events -n billing
```

---

## Tools & APIs

### 30+ Available Tools

#### Payment Processing (3 tools)
- `calculate_payment_split` - Calculate splits across stakeholders
- `validate_split_config` - Validate split configuration rules
- `get_split_status` - Get split status and details

#### Settlement (3 tools)
- `initiate_settlement` - Trigger settlement for accumulated funds
- `get_settlement_status` - Get settlement tracking info
- `retry_failed_settlement` - Retry with exponential backoff

#### Ledger & Accounting (3 tools)
- `create_ledger_entry` - Create double-entry transaction
- `query_ledger` - Query ledger with filters
- `verify_ledger_balance` - Verify balance integrity

#### Reconciliation (3 tools)
- `run_reconciliation` - Manually trigger reconciliation
- `get_reconciliation_status` - Get status and results
- `resolve_discrepancy` - Resolve identified discrepancies

#### Fraud Detection (3 tools)
- `check_fraud_score` - Calculate risk score
- `flag_suspicious_transaction` - Flag transaction
- `get_fraud_status` - Get fraud detection status

#### Compliance (4 tools)
- `calculate_tax_withholding` - Calculate GST/TDS/Income Tax
- `verify_kyc` - Verify KYC/AML compliance
- `file_suspicious_activity_report` - File SAR
- `generate_tax_report` - Generate tax report

#### Multi-Currency (3 tools)
- `get_fx_rate` - Get FX rate for currency pair
- `lock_fx_rate` - Lock rate for 24 hours
- `convert_currency` - Convert between currencies

#### Disputes (3 tools)
- `create_dispute` - Create payment dispute
- `resolve_dispute` - Resolve dispute
- `get_dispute_status` - Get status

#### Admin Tools (4 tools)
- `update_split_rules` - Update split percentages
- `update_gateway_credentials` - Update gateway config
- `generate_audit_report` - Generate audit report
- `export_ledger` - Export ledger for audit

### Example Request

```bash
curl -X POST \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <JWT_TOKEN>" \
  -d '{
    "jsonrpc": "2.0",
    "method": "tools/call",
    "params": {
      "name": "calculate_payment_split",
      "arguments": {
        "tenant_id": "tenant_123",
        "transaction_id": "txn_456",
        "gross_amount": 10000,
        "assessment_type": "coding_dojo"
      }
    },
    "id": 1
  }' \
  ws://localhost:9001
```

---

## Configuration

### Environment Variables

```bash
# Server
MCP_PORT=9001                                    # Server port
LOG_LEVEL=INFO                                   # Log level (DEBUG, INFO, WARN, ERROR)

# Database
DATABASE_URL=jdbc:postgresql://localhost:5432/ecoskiller
DATABASE_USER=payment_user
DATABASE_PASSWORD=secure_password

# ClickHouse
CLICKHOUSE_HOST=localhost
CLICKHOUSE_PORT=8123
CLICKHOUSE_USER=default
CLICKHOUSE_PASSWORD=secure_password

# Payment Gateways
RAZORPAY_API_KEY=rzp_test_XXXX
RAZORPAY_API_SECRET=test_secret_XXXX
WISE_API_TOKEN=test_XXXX
XE_COM_API_KEY=test_XXXX

# Keycloak
KEYCLOAK_URL=https://keycloak.example.com
KEYCLOAK_REALM=ecoskiller
KEYCLOAK_PUBLIC_KEY=<public-key-pem>

# Security
ENCRYPTION_KEY=<32-byte-hex-key>
HMAC_SECRET=<32-byte-hex-key>
TOKEN_EXPIRY_MINUTES=5

# Rate Limiting
RATE_LIMIT_PER_MINUTE=600
MAX_CONCURRENT_REQUESTS=1000

# Java
JAVA_OPTS="-Xmx512m -Xms256m -XX:+UseG1GC"
```

---

## Monitoring & Logging

### Prometheus Metrics

```yaml
# Key metrics exposed
payment_split_duration_ms          # Split calculation latency
settlement_initiation_duration_ms  # Settlement initiation latency
settlement_failure_rate            # Failed settlements %
reconciliation_match_rate          # Daily match rate (target 99.8%)
fraud_flags_raised_total          # Suspicious transactions flagged
tax_withheld_total                # Tax withholding amount
kafka_consumer_lag                # Event processing lag
```

### Log Levels

```
DEBUG  - Detailed diagnostic info (development only)
INFO   - General operational info
WARN   - Warning messages (recoverable issues)
ERROR  - Error messages (needs attention)
```

### Centralized Logging

```bash
# Send logs to ELK Stack
java -jar app.jar \
  -Dlog4j.configuration=log4j2-elk.xml

# Or to Datadog
java -jar app.jar \
  -Dlog4j.configuration=log4j2-datadog.xml
```

---

## Testing

### Unit Tests

```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=PaymentSplitCalculatorTest

# Run with coverage
mvn test jacoco:report

# View coverage report
open target/site/jacoco/index.html
```

### Integration Tests

```bash
# Start test database
docker-compose -f docker-compose.test.yml up -d

# Run integration tests
mvn verify

# Cleanup
docker-compose -f docker-compose.test.yml down
```

### Load Testing

```bash
# Using Apache JMeter
jmeter -n -t test-plan.jmx \
  -Jhost=localhost \
  -Jport=9001 \
  -Jusers=100 \
  -Jrampup=10

# Using k6
k6 run load-test.js
```

### Security Testing

```bash
# Dependency vulnerability scan
mvn org.owasp:dependency-check-maven:check

# SAST - SonarQube
mvn sonar:sonar

# Container image scan
trivy image ecoskiller/payment-distribution-engine-mcp:1.0.0-secure
```

---

## Troubleshooting

### Common Issues

#### 1. Database Connection Timeout

```
Error: Failed to acquire database connection within timeout
```

**Solution:**
```bash
# Check PostgreSQL is running
psql -h localhost -U payment_user -d ecoskiller -c "SELECT 1;"

# Check connection pool settings
export DATABASE_POOL_SIZE=20
```

#### 2. JWT Token Validation Fails

```
Error: Invalid or expired authentication token
```

**Solution:**
```bash
# Verify Keycloak public key
curl https://keycloak.example.com/auth/realms/ecoskiller | jq .public_key

# Check token expiry
echo "TOKEN" | jq '.' (decode base64)
```

#### 3. Rate Limiting Exceeded

```
Error: Too many requests
```

**Solution:**
```bash
# Increase rate limit
export RATE_LIMIT_PER_MINUTE=1000

# Or implement client-side backoff
# with exponential retry (1s, 5s, 30s)
```

#### 4. Out of Memory (OOM)

```
Error: java.lang.OutOfMemoryError: Java heap space
```

**Solution:**
```bash
# Increase heap size
export JAVA_OPTS="-Xmx1024m -Xms512m -XX:+UseG1GC"
```

#### 5. ClickHouse Connection Issues

```
Error: Failed to connect to ClickHouse at localhost:8123
```

**Solution:**
```bash
# Check ClickHouse status
clickhouse-client --query "SELECT 1;"

# Verify network connectivity
nc -zv localhost 8123
```

### Debug Mode

```bash
# Enable debug logging
export LOG_LEVEL=DEBUG

# Enable Java debugging
export JAVA_OPTS="-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005"

# Connect with IDE debugger to localhost:5005
```

---

## Performance Tuning

### JVM Optimization

```bash
# Production settings
export JAVA_OPTS="
  -Xmx2048m
  -Xms1024m
  -XX:+UseG1GC
  -XX:MaxGCPauseMillis=200
  -XX:+ParallelRefProcEnabled
  -XX:+UnlockDiagnosticVMOptions
  -XX:G1NewCollectionHeuristicPercent=35
  -XX:G1ReservePercent=15
"
```

### Database Optimization

```sql
-- Create indexes
CREATE INDEX idx_splits_tenant_id ON payment_splits(tenant_id);
CREATE INDEX idx_settlements_status ON settlements(status);
CREATE INDEX idx_ledger_entries_tenant ON ledger_entries(tenant_id);

-- Vacuum
VACUUM ANALYZE;
```

### Connection Pooling

```bash
export DATABASE_POOL_SIZE=30
export DATABASE_IDLE_TIMEOUT=900
export DATABASE_MAX_LIFETIME=1800
```

---

## Support & Contact

**Payment Team**  
- Email: payment-team@ecoskiller.com
- Slack: #payment-engine
- On-Call: PagerDuty rotation

**Documentation**  
- [Architecture Decision Records](docs/adr/)
- [API Documentation](docs/api/)
- [Security Guidelines](docs/security/)
- [Operational Runbooks](docs/runbooks/)

---

## License

© Ecoskiller 2024-2026. All rights reserved.

---

## Version History

| Version | Date | Changes |
|---------|------|---------|
| 1.0.0 | 2026-03-21 | Initial secure Java implementation |

---

**Last Updated:** March 21, 2026  
**Status:** Production Ready (Secure)  
**Next Review:** Q2 2026
