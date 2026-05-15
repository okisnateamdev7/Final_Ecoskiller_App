# Network Engineer — Routing & Switching Architecture
## Project: Ecoskiller + Gojo
## Version: 1.0
## Scope: Dev | Test | Staging | Production
## Includes: Application Layer + ML Algorithms Layer + GitLab Self-Hosted + AWS Hybrid Routing

---

# 1. Role Definition — Network Engineer Agent

The Network Engineer Agent is responsible for implementation, configuration, validation, and governance of routing and switching architecture across all environments.

Scope includes:
- VPC routing tables
- Transit Gateway routing
- Inter-subnet routing
- Hybrid connectivity (VPN / Direct Connect)
- ML data traffic routing
- GitLab runner routing isolation
- East-West traffic control
- North-South traffic inspection
- Switching segmentation (VLAN / Subnet policy)
- DR routing failover

Execution Model:
1 Phase = 1 Agent = 10 Structured Chats
No overlapping execution.

---

# 2. Core Routing Principles

1. Environment Isolation Mandatory
2. Deny-All Default Routing
3. Explicit Route Declaration Only
4. No Transitive Routing Between Environments
5. ML Training Traffic Segmented
6. Production Routes Immutable Without Change Control
7. All Route Changes via Infrastructure as Code
8. Centralized Inspection Routing Path
9. Deterministic Failover Design
10. Route Logging Enabled

---

# 3. Environment Routing Architecture

Each environment must have:
- Dedicated Route Tables
- Dedicated NAT Gateway
- Dedicated Internet Gateway
- Dedicated Private Route Tables
- Dedicated ML Route Tables

Example Structure (Per Environment):

Public Route Table:
- 0.0.0.0/0 -> Internet Gateway

Private App Route Table:
- 0.0.0.0/0 -> NAT Gateway
- ML CIDR -> ML Subnet

ML Route Table:
- Data Subnet CIDR -> Data Subnet
- Registry Endpoint -> Private Endpoint
- No direct Internet Route

Data Route Table:
- Accept traffic only from App & ML Subnets
- No outbound Internet

---

# 4. Transit Gateway Governance

If multi-account architecture:

- All VPCs attached to Transit Gateway
- Centralized Firewall VPC required
- Route propagation disabled by default
- Explicit route approval required
- No Dev/Test attachment to Production Route Table

Inspection Flow:
Ingress -> TGW -> Firewall VPC -> Target VPC

---

# 5. Switching & Segmentation Strategy

Within each VPC/Subnet structure:

Segmentation Layers:
1. Application Segment
2. ML Segment
3. Data Segment
4. CI/CD Segment
5. Observability Segment

Rules:
- No L2 bridging between segments
- VLAN/Subnet separation required (Hybrid On-Prem)
- Production switching changes require CAB approval
- MAC table flooding prevention enforced
- Broadcast traffic minimized

---

# 6. ML Algorithms Routing Layer

ML routing must enforce:

Training Cluster:
- Route only to Feature Store & Dataset Storage
- No Production DB access

Validation Cluster:
- Route to Staging Registry only

Inference Cluster (Production):
- Route to Production Registry
- Route to Data Subnet read-only
- No training subnet access

Model Artifact Flow:
Training -> Validation -> Registry -> Staging -> Production

All ML artifact traffic must:
- Use Private Endpoints
- Use TLS 1.3
- Be logged

---

# 7. GitLab Self-Hosted Routing Rules

GitLab Server:
- Separate VPC or isolated subnet
- No direct DB route to Production

Runners:
- Dev Runner -> Dev Route Table Only
- Test Runner -> Test Route Table Only
- Staging Runner -> Staging Route Table Only
- Production Runner -> Manual Trigger Only

Rules:
- Runners cannot access ML training subnet
- No unrestricted Internet egress
- Artifact storage private routing only

---

# 8. Hybrid Connectivity (If On-Prem Exists)

Connectivity Options:
- AWS Site-to-Site VPN
- AWS Direct Connect

Routing Rules:
- Static routes prohibited unless documented
- BGP preferred
- Route filtering mandatory
- No production DB exposure to on-prem
- ML datasets encrypted before transfer

---

# 9. Route Security Enforcement

Mandatory Controls:
- Route table drift detection via CI
- Firewall rule validation
- Network flow logs enabled
- IDS/IPS traffic inspection
- Explicit deny rules for cross-environment CIDR

Port Governance Matrix (Mandatory):

App -> DB : 5432 / 3306
App -> Redis : 6379
App -> ML Inference : 8443
ML Training -> Feature Store : 443
GitLab Runner -> Target Env : 443

All other ports blocked.

---

# 10. High Availability Routing

Production Requirements:
- Multi-AZ route tables
- Multi-NAT architecture
- Redundant VPN tunnels
- Automatic route failover
- Health-based route switching

RTO < 30 minutes
RPO < 5 minutes

---

# 11. CI/CD Route Governance

Pipeline stages must not bypass routing rules.

Stages:
1. Build (Isolated Subnet)
2. Scan (Security Segment)
3. Deploy (Env-Specific Route Only)

No manual SSH deployment allowed.

---

# 12. Observability Routing

Monitoring Traffic:
- Prometheus scraping restricted by SG
- Logs routed to centralized logging VPC
- ML telemetry routed to analytics subnet
- No direct internet log shipping

---

# 13. Disaster Recovery Routing

Cross-Region Strategy:
- Predefined secondary route tables
- Warm standby ML registry
- DNS failover via Route53
- Failover simulation every quarter

---

# 14. Phase-Based Execution Plan

Phase 1 — Environment Route Architecture Agent
Phase 2 — ML Routing Segmentation Agent
Phase 3 — GitLab & CI/CD Routing Agent
Phase 4 — Hybrid Connectivity Agent
Phase 5 — Security & Drift Detection Agent
Phase 6 — DR & Failover Agent

Each Phase:
- 10 structured chats
- Risk review
- Compliance validation
- Approval gate
- Documentation update

---

# 15. Strict Routing & Switching Rules

DO:
- Use explicit route definitions
- Log all route changes
- Enforce route drift detection
- Use BGP filtering
- Use private endpoints for ML traffic

DO NOT:
- Enable transitive routing between environments
- Allow ML training access to production DB
- Allow shared runners across route domains
- Allow manual route modification
- Allow open 0.0.0.0/0 in private route tables

---

# 16. Compliance Alignment

Routing & Switching must align with:
- ISO 27001 A.13
- SOC2 CC6
- NIST 800-53 SC-7
- GDPR data isolation requirements

---

# 17. Validation Checklist

Before Production Approval:

- Route isolation verified
- ML route restrictions validated
- Runner routing tested
- Firewall inspection validated
- Failover simulation executed
- Drift detection enabled
- Approval documented

---

# End of Network Engineer — Routing & Switching Specification

This document governs routing and switching architecture for Ecoskiller + Gojo across Dev, Test, Staging, and Production including ML Algorithms layer and GitLab Self-Hosted CI/CD routing enforcement.

