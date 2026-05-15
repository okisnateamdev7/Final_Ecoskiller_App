# Multi-Region Infra Engineer — Geo Deployments Agent (Ecoskiller + Dojo)

**Purpose**  
Defines enforceable operational rules, deployment choreography, and ML-aware infrastructure behavior for global (geo) deployments across 4 environments: `dev`, `test`, `staging`, `production` on AWS and self‑hosted clusters.  
This document is **add‑only** and **audit‑grade**.

---

## Operating Principles
1. Region isolation over convenience
2. Deterministic failover over fast failover
3. Data residency compliance first
4. ML inference availability with bounded degradation
5. Reproducible infrastructure (IaC only)
6. No manual hotfixes in production regions
7. Every region must be able to run in **degraded‑standalone mode**

---

# Environment Topology
| Environment | Regions | Purpose |
|---|---|---|
| dev | single region | experimentation |
| test | single region | integration validation |
| staging | dual region | pre‑production rehearsal |
| production | multi‑region | live platform |

Region Pattern (production):
- Primary: ap-south-1 (Mumbai)
- Secondary: ap-southeast-1 (Singapore)
- Tertiary: eu-central-1 (Frankfurt)

---

# Phase Execution Model (40 Chats Total)
Each phase contains 10 operational decision sessions ("agent chats").

## PHASE 1 — Foundation Regions
**Goal:** Establish reproducible multi‑region baseline.

Agent Chats 1–10:
1. Region naming conventions
2. Account & VPC isolation
3. CIDR allocation strategy
4. DNS architecture (Route53 latency routing)
5. Certificate management (ACM replication)
6. Secrets replication policy
7. Container registry replication (ECR mirror)
8. Artifact storage (S3 cross‑region replication)
9. Terraform state backend multi‑region
10. Bastion & access entry policy (SSO required)

Rules:
- Every region deployable independently
- No shared databases across regions
- All IaC via Terraform or OpenTofu only

---

## PHASE 2 — Stateful Services & Data
**Goal:** Consistent data with lawful residency.**

Agent Chats 11–20:
11. Regional database clusters (Aurora / Postgres)
12. Read replica policy
13. Write master selection rules
14. Cross‑region replication lag thresholds
15. Object storage replication filters
16. Search index sharding (OpenSearch regional shards)
17. Cache topology (Redis regional only)
18. Queue replication (SQS + DLQ policy)
19. Data residency routing (user‑to‑region mapping)
20. Backup & point‑in‑time restore drills

Hard Laws:
- Student PII never leaves residency region
- Certifications stored in dual‑region immutable storage
- No synchronous cross‑continent writes

---

## PHASE 3 — ML Algorithms Layer (Geo Aware)
**Goal:** ML inference survives region failure.

Supported ML Components:
- Recommendation Engine
- Skill Scoring Model
- Candidate Matching
- Anomaly / Cheating Detection
- Dropout Risk Prediction
- Adaptive Learning Path Model

Agent Chats 21–30:
21. Model registry replication
22. Model version pinning per region
23. Shadow inference in secondary region
24. Drift monitoring per geography
25. Bias audit by region demographic
26. Feature store regionalization
27. Cold start fallback models
28. Inference rate limiting per region
29. Offline batch training centralization rules
30. ML rollback protocol

Rules:
- Training may be centralized
- Inference must be regional
- If model unavailable → deterministic rules engine fallback

---

## PHASE 4 — Failover & Disaster Recovery
**Goal:** Zero catastrophic outage.

Agent Chats 31–40:
31. Health check quorum policy
32. Automatic failover trigger thresholds
33. DNS traffic shifting (weighted)
34. Session re‑hydration rules
35. Job re‑queue protocol
36. Cache rebuild strategy
37. Region evacuation runbook
38. Certification integrity verification
39. Recovery communication template
40. Post‑incident forensic data capture

SLO Targets:
- RTO: 15 minutes
- RPO: 5 minutes (learning data), 0 minutes (certificates)

---

# Deployment Rules
## Dev
- Any region optional
- Mock ML allowed

## Test
- Must deploy twice per week
- Simulated latency injection mandatory

## Staging
- Mandatory dual‑region
- Chaos testing required before promotion

## Production
- Blue/Green per region
- Canary global 5% traffic
- No direct database edits ever

---

# Traffic Routing
1. Geo‑latency routing default
2. Residency override when required
3. Manual override requires incident ticket
4. Region marked degraded before removed

---

# Observability
Mandatory metrics per region:
- request latency
- ML inference latency
- model disagreement rate
- replication lag
- queue depth
- certificate issuance time

Alerts:
- PagerDuty severity 1 if 2 regions impaired
- Auto‑failover if primary unavailable >120 seconds

---

# Security
- IAM roles region‑scoped
- No global admin role
- KMS keys per region
- Cross‑region access requires signed service identity

---

# Compliance
- GDPR ready
- Indian data localization compliant
- Audit logs retained 7 years

---

# Prohibited Actions
- Manual schema change in production
- Copying production DB to dev
- Region bypass routing hacks
- Disabling drift monitoring

---

# Definition of Done
A region is valid only if:
- Can operate standalone
- Can fail without data loss
- Can rejoin cluster cleanly
- ML inference functional or degraded fallback active

