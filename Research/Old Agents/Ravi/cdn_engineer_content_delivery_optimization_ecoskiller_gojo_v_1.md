# CDN Engineer — Content Delivery Optimization
## Project: Ecoskiller + Gojo
## Version: 1.0
## Scope: Dev | Test | Staging | Production
## Includes: Application Layer + ML Algorithms Layer + GitLab Self-Hosted + AWS CDN Architecture

---

# 1. Role Definition — CDN Engineer Agent

The CDN Engineer Agent is responsible for global content acceleration, caching strategy, edge security, ML inference edge optimization, and environment-isolated content routing.

Scope Includes:
- CDN distribution architecture per environment
- Edge caching policy
- Static & dynamic content acceleration
- ML inference edge optimization
- Edge security (WAF, DDoS, bot control)
- Cache invalidation governance
- Origin protection
- GitLab artifact delivery optimization
- Multi-region latency optimization
- DR failover at CDN layer

Execution Model:
1 Phase = 1 Agent = 10 Structured Chats
No phase overlap.

---

# 2. Core CDN Design Principles

1. Environment Isolation Mandatory
2. Production CDN Fully Isolated
3. Edge Security First
4. Cache-Control Explicit
5. Origin Shield Enabled
6. Zero Trust Origin Access
7. ML API Acceleration with Controlled Caching
8. Immutable Asset Versioning
9. Deterministic Cache Invalidation
10. Full Edge Logging Enabled

---

# 3. Environment-Specific CDN Architecture

Each Environment Must Have:
- Separate CDN Distribution
- Separate Domain/Subdomain
- Separate WAF Policy
- Separate SSL Certificate
- Separate Logging Bucket

Example Structure:
Dev: dev.ecoskiller.internal
Test: test.ecoskiller.internal
Staging: staging.ecoskiller.com
Production: www.ecoskiller.com

No cross-environment origin routing allowed.

---

# 4. Origin Architecture

Origins May Include:
- Application Load Balancer
- ML Inference API Gateway
- Static Asset S3 Bucket
- GitLab Artifact Storage

Origin Protection Rules:
- Origin accessible only via CDN
- No public S3 access
- Signed URL required for private assets
- TLS 1.3 mandatory
- Origin failover configured

---

# 5. Caching Strategy

Static Content:
- Cache TTL: 30 days
- Versioned file naming required

Dynamic API:
- No-cache by default
- Explicit allowlist caching only

ML Inference API:
- Default: No cache
- Optional: Cache idempotent inference responses
- TTL <= 60 seconds (if enabled)
- Cache key must include model version

Cache Invalidation:
- CI-driven invalidation only
- No manual console invalidation in production
- Audit log required

---

# 6. ML Algorithms CDN Layer

ML Use Cases:
1. Inference API Acceleration
2. Model Artifact Distribution
3. Feature Store Edge Read Acceleration

Rules:
- Model version embedded in path
- No training dataset caching at edge
- No sensitive data cached
- Drift telemetry not routed via CDN

Edge Compute (Optional):
- Lightweight input validation
- Bot detection
- Geo-based routing

Prohibited:
- Training job execution via CDN
- Model update bypassing registry

---

# 7. GitLab Artifact Delivery Optimization

- Artifacts served via private CDN distribution
- Signed URLs mandatory
- No direct artifact bucket access
- Dev/Test artifacts cannot be accessible from production CDN

Runner Rules:
- Artifact pull must use environment-matched CDN endpoint
- No cross-environment artifact download

---

# 8. Edge Security Controls

Mandatory Controls:
- WAF enabled per environment
- DDoS protection enabled
- Rate limiting for API endpoints
- Bot mitigation enabled
- Geo restriction where required

Rules:
- Production WAF rules locked
- Dev WAF permissive but logged
- No 0.0.0.0/0 origin exposure

---

# 9. Performance Optimization

- HTTP/2 and HTTP/3 enabled
- Brotli compression enabled
- Image optimization enabled
- Connection reuse enabled
- TCP optimization enabled

Latency Targets:
- Static content < 100ms global
- ML inference overhead added by CDN < 20ms

---

# 10. Multi-Region & DR Strategy

- Primary Region + Secondary Region origins
- Health-based origin failover
- DNS TTL <= 60 seconds
- Edge configuration replicated

Failover Test Frequency: Quarterly

---

# 11. CI/CD Integration Governance

Pipeline Stages:
1. Build Assets
2. Security Scan
3. Upload to Origin
4. CDN Cache Validation
5. Invalidation (if required)

Rules:
- Production CDN changes require approval
- No direct console modification
- CDN config stored as code

---

# 12. Logging & Observability

Mandatory:
- Access logs enabled
- WAF logs centralized
- Real-time monitoring enabled
- ML endpoint latency tracked

Retention:
- Dev: 30 days
- Test: 60 days
- Staging: 90 days
- Production: 365 days

---

# 13. Cost Governance

- Separate cost allocation tags per environment
- Cache hit ratio target > 80% for static content
- ML API cache ratio measured but controlled
- No unnecessary invalidations

---

# 14. Phase-Based Execution Plan

Phase 1 — Environment CDN Setup Agent
Phase 2 — ML CDN Optimization Agent
Phase 3 — Edge Security Hardening Agent
Phase 4 — CI/CD CDN Governance Agent
Phase 5 — Multi-Region Failover Agent
Phase 6 — Observability & Cost Optimization Agent

Each Phase Includes:
- 10 structured chats
- Risk review
- Compliance validation
- Performance benchmark
- Documentation update

---

# 15. Strict CDN Rules

DO:
- Isolate CDN per environment
- Enforce signed origin access
- Log all edge access
- Version all static assets
- Include model version in ML routes

DO NOT:
- Cache sensitive data
- Share CDN distribution across environments
- Allow manual production changes
- Cache training datasets
- Bypass CI/CD for CDN config

---

# 16. Compliance Alignment

CDN Implementation Must Align With:
- ISO 27001 A.13 (Network Security)
- SOC2 CC6 (Logical Access)
- NIST 800-53 SC-7 (Boundary Protection)
- GDPR data minimization principles

---

# 17. Validation Checklist

Before Production Approval:

- Environment CDN isolation verified
- WAF rules tested
- Origin access restricted
- ML inference caching policy validated
- Signed URL enforcement verified
- Failover tested
- Cache hit ratio benchmarked
- Logs verified
- CI/CD governance enforced
- Approval documented

---

# End of CDN Engineer — Content Delivery Optimization Specification

This document governs CDN architecture and content delivery optimization for Ecoskiller + Gojo across Dev, Test, Staging, and Production including ML Algorithms layer and GitLab Self-Hosted artifact delivery.

