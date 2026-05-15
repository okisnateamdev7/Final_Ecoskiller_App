# Network Architect — Network Topology
## Project: Ecoskiller + Gojo
## Version: 1.0
## Scope: Dev | Test | Staging | Production
## Includes: Application Layer + ML Algorithms Layer + GitLab Self-Hosted + AWS

---

# 1. Role Definition — Network Architect Agent

The Network Architect Agent is responsible for designing, governing, validating, and securing the complete network topology across all four environments.

This includes:
- Application traffic architecture
- ML training & inference network segmentation
- GitLab self-hosted isolation architecture
- AWS VPC design
- Security zoning & zero trust model
- Inter-service communication rules
- CI/CD network governance
- Observability network layer
- Disaster recovery connectivity

Each Phase = 1 Agent = 10 Structured Chats
No phase overlap permitted.

---

# 2. Core Network Design Principles

1. Zero Trust Architecture (ZTA)
2. Environment Isolation (No cross-environment lateral traffic)
3. Least Privilege Network Access
4. Multi-AZ Redundancy
5. Infrastructure as Code enforced
6. ML Layer Network Segmentation
7. Encrypted Traffic Everywhere (TLS 1.3 minimum)
8. Immutable Infrastructure Pattern
9. Centralized Logging & Monitoring Network Flow
10. Deterministic Traffic Routing

---

# 3. Environment-Level Network Isolation

Each environment must have:
- Dedicated AWS Account (recommended)
- Separate VPC
- Separate CIDR Block
- Separate NAT Gateways
- Separate Load Balancers
- Separate EKS/ECS Clusters
- Separate ML compute clusters

Example CIDR Allocation:
- Dev: 10.10.0.0/16
- Test: 10.20.0.0/16
- Staging: 10.30.0.0/16
- Production: 10.40.0.0/16

Strict rule:
NO direct peering between Dev/Test and Production.

---

# 4. VPC Architecture Per Environment

Each VPC must contain:

## 4.1 Public Subnets
- Application Load Balancer
- Bastion Host (restricted IP)
- NAT Gateway

## 4.2 Private Application Subnets
- Microservices
- API Services
- Backend Services
- Auth Services

## 4.3 Private ML Subnets
- Model Training Nodes (GPU)
- Feature Store Services
- Model Registry
- Batch Processing Nodes

## 4.4 Data Subnets
- RDS / Aurora
- Redis
- Document DB
- Vector DB (for ML embeddings)

No public IP allowed in ML or Data subnets.

---

# 5. ML Algorithms Layer Network Architecture

The ML Layer must be segmented into:

1. Training Network Zone
2. Validation Network Zone
3. Inference Network Zone
4. Feature Store Zone
5. Model Registry Zone

Rules:
- Training nodes cannot directly access production databases
- Inference nodes only access approved model registry
- Feature store access via private endpoints only
- All datasets validated via checksum before ingestion
- No internet egress without NAT + Firewall inspection

Model Flow:
Training -> Validation -> Registry -> Staging Inference -> Production Inference

---

# 6. GitLab Self-Hosted Network Architecture

GitLab must run in isolated infrastructure.

## 6.1 Network Placement
- Dedicated VPC (Recommended)
- No direct production DB access
- Runner isolation per environment

## 6.2 Runner Segmentation
- Dev Runner
- Test Runner
- Staging Runner
- Production Runner (manual approval only)

Rules:
- Production runner cannot be shared
- Runners cannot access ML training subnet directly
- All pipeline traffic over HTTPS
- Container registry private-only access

---

# 7. Load Balancing Strategy

Each environment must include:
- Application Load Balancer (ALB)
- Internal Load Balancer for microservices
- API Gateway (optional external exposure)

Production:
- Multi-AZ ALB
- Health checks every 30 seconds
- Automatic target group failover

---

# 8. Security Groups & Network ACL Rules

Security Groups:
- Whitelist-only inbound rules
- No 0.0.0.0/0 except ALB 443
- Database accepts traffic only from App SG
- ML SG accepts traffic only from Orchestrator SG

NACL:
- Block unused ports
- Explicit deny for cross-environment CIDR

---

# 9. Service Mesh Layer (Mandatory for Production)

Implement Service Mesh (Istio / AWS App Mesh)

Capabilities:
- mTLS between services
- Traffic shaping
- Canary deployments
- Observability
- Circuit breaking

ML Inference traffic must use service mesh policies.

---

# 10. CI/CD Network Governance

All deployments must:
- Pass through GitLab runner
- Deploy via private endpoints
- No SSH-based manual deployment
- Infrastructure changes via Terraform only

Pipeline Stages Network Rules:
1. Build (Isolated)
2. Scan (Security Subnet)
3. Test (Test Subnet)
4. Deploy (Environment-specific Subnet)

---

# 11. Observability Network Layer

Centralized Logging:
- VPC Flow Logs enabled
- CloudTrail enabled
- ML pipeline logs centralized

Monitoring:
- Prometheus inside private subnet
- Grafana access restricted
- Alerting via secure webhook

---

# 12. Disaster Recovery & Multi-Region

Production must include:
- Cross-region VPC replication
- Read replica databases
- Backup ML model registry
- DNS failover via Route53

RTO: < 30 minutes
RPO: < 5 minutes

---

# 13. Phase-Based Execution Model

Phase 1 — Core VPC Architecture Agent
Phase 2 — ML Network Segmentation Agent
Phase 3 — GitLab & CI/CD Network Agent
Phase 4 — Security Hardening Agent
Phase 5 — Observability & Logging Agent
Phase 6 — Disaster Recovery Agent

Each Phase:
- 10 structured chats
- Design validation
- Risk assessment
- Approval gate
- Documentation update

---

# 14. Strict Network Rules

DO:
- Enforce environment isolation
- Enforce TLS everywhere
- Use IAM roles not static keys
- Log every ingress/egress
- Use private endpoints

DO NOT:
- Allow direct DB exposure
- Allow shared runners across environments
- Allow manual infra modification
- Allow ML training in production subnet
- Allow cross-environment peering without approval

---

# 15. Compliance Alignment

Network must align with:
- ISO 27001 Annex A.13 (Network Security)
- SOC2 CC6 & CC7
- GDPR data isolation
- OWASP Network segmentation best practices

---

# 16. Validation Checklist

Before Production Approval:

- VPC isolation validated
- No open ports exposed
- ML segmentation tested
- GitLab runner restrictions verified
- Load test completed
- Failover test executed
- Security scan passed

---

# End of Network Architecture Specification

This document governs the full network topology for Ecoskiller + Gojo including ML Algorithms Layer and GitLab Self-Hosted CI/CD integration across Dev, Test, Staging, and Production.

