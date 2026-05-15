# Infrastructure Automation Engineer — Master Gap Register
Project: Ecoskiller + Dojo
Role: Infrastructure Automation Engineer Agent
Cloud: AWS
Version Control: Self-Hosted GitLab
Environments: dev / test / staging / production

---

# STRUCTURE
Each gap includes:
- Risk Description
- Required Technical Controls
- Enforcement Mechanism (CI/CD / Runtime / Policy-as-Code)
- Environment Scope

---

# GAP-IAE-01 — Terraform State Backend Governance
Risk:
Local or improperly secured state files cause corruption, privilege leakage, or infrastructure drift.

Required Controls:
- Remote backend (S3) with versioning enabled
- DynamoDB state lock table
- Server-side encryption (SSE-KMS)
- Per-environment state isolation
- State access restricted via IAM role

Enforcement:
- CI blocks apply if backend not remote
- IAM policy validation stage

Scope: All environments

---

# GAP-IAE-02 — Drift Detection Automation
Risk:
Manual or external changes create hidden divergence from IaC.

Required Controls:
- Scheduled Terraform plan job (daily)
- Drift severity classification (low/medium/high)
- Auto-fail production pipeline on high drift

Enforcement:
- GitLab scheduled pipeline
- Slack/Alert integration

Scope: test / staging / production

---

# GAP-IAE-03 — Zero-Downtime Deployment Policy
Risk:
Service interruption during infra rollout.

Required Controls:
- Blue/green deployment modules
- Canary deployment support
- Automated rollback on health-check failure

Enforcement:
- Deployment controller policy
- Health probe validation

Scope: staging / production

---

# GAP-IAE-04 — Database Infrastructure Automation
Risk:
Data loss or insecure DB configuration.

Required Controls:
- RDS/Aurora Terraform module
- Multi-AZ enabled (prod)
- Encryption at rest + in transit
- Automated backups (daily)
- Read replica configuration

Enforcement:
- CI policy check (no single-AZ prod DB)

Scope: All (multi-AZ mandatory in prod)

---

# GAP-IAE-05 — Disaster Recovery Automation
Risk:
Inability to recover platform after region failure.

Required Controls:
- Cross-region backup replication
- Defined RTO / RPO
- Automated restore test pipeline (quarterly)

Enforcement:
- DR drill pipeline

Scope: production

---

# GAP-IAE-06 — Autoscaling Governance
Risk:
Over-scaling cost explosion or under-scaling service failure.

Required Controls:
- CPU/memory scaling thresholds defined
- ML inference autoscaling rules
- Min nodes enforced in prod
- Scale-to-zero allowed only in dev/test

Enforcement:
- HPA policy validation

Scope: All

---

# GAP-IAE-07 — Artifact Storage Governance
Risk:
Model or build artifacts altered or lost.

Required Controls:
- S3 versioned artifact bucket
- Immutable artifact tagging
- Artifact SHA256 hash verification

Enforcement:
- CI artifact integrity check

Scope: All

---

# GAP-IAE-08 — Container Hardening Policy
Risk:
Compromised containers in production.

Required Controls:
- Approved base image list
- Non-root container enforcement
- Image signing (Cosign)
- CVE severity threshold block (High/Critical)

Enforcement:
- Container scan stage in GitLab

Scope: All (strict in prod)

---

# GAP-IAE-09 — Secrets Rotation Automation
Risk:
Credential leakage and long-lived secrets.

Required Controls:
- Automatic rotation enabled
- Expiry policy defined
- Rotation validation tests

Enforcement:
- Secrets Manager policy validation

Scope: All

---

# GAP-IAE-10 — Network Egress Control
Risk:
Data exfiltration or uncontrolled outbound traffic.

Required Controls:
- Egress security groups
- NAT monitoring
- Outbound allowlist
- Training cluster internet isolation (optional VPC endpoint)

Enforcement:
- Network policy validation script

Scope: staging / production

---

# GAP-IAE-11 — Feature Store Scaling Strategy
Risk:
High-latency feature retrieval impacting ML inference.

Required Controls:
- Online store latency SLO (<X ms)
- Partitioning strategy
- Caching layer (Redis/DAX)

Enforcement:
- Performance test gate before prod

Scope: staging / production

---

# GAP-IAE-12 — ML Training Cost Guardrails
Risk:
Runaway training jobs consuming budget.

Required Controls:
- Budget cap per training job
- Spot instance policy (where safe)
- Auto-terminate idle jobs

Enforcement:
- Cost monitoring alerts

Scope: dev / test / staging

---

# GAP-IAE-13 — GitLab Runner Isolation Policy
Risk:
Cross-environment privilege escalation.

Required Controls:
- Separate runners per environment
- Protected branch enforcement
- Runner IAM role scoping

Enforcement:
- Runner registration audit

Scope: All

---

# GAP-IAE-14 — Promotion Safety Gates
Risk:
Unvalidated infra reaches production.

Required Controls:
- Staging soak time rule (minimum window)
- Integration test gate
- ML model validation gate

Enforcement:
- Mandatory manual approval in prod

Scope: production

---

# GAP-IAE-15 — Policy as Code Enforcement
Risk:
Non-compliant resources deployed.

Required Controls:
- OPA/Sentinel policies
- Forbidden resource detection
- Mandatory tagging rules

Enforcement:
- CI policy validation stage

Scope: All

---

# GAP-IAE-16 — Observability SLO Enforcement
Risk:
Unmonitored service degradation.

Required Controls:
- SLO per service
- Error budget tracking
- Alert severity mapping

Enforcement:
- Alert manager integration

Scope: staging / production

---

# GAP-IAE-17 — Environment Bootstrap Automation
Risk:
Manual setup inconsistency across accounts.

Required Controls:
- Bootstrap script for new AWS account
- Control Tower integration
- Region replication template

Enforcement:
- Bootstrap validation checklist

Scope: All

---

# GAP-IAE-18 — Immutable Infrastructure Enforcement
Risk:
Manual instance changes bypass IaC.

Required Controls:
- No SSH in production (except break-glass)
- Bastion host governance
- Auto-replace mutated instances

Enforcement:
- IAM deny policy

Scope: production

---

# GAP-IAE-19 — Backup Verification Automation
Risk:
Backups unusable when needed.

Required Controls:
- Scheduled restore test
- Backup integrity verification
- Snapshot expiration policy

Enforcement:
- Quarterly restore drill

Scope: production

---

# GAP-IAE-20 — ML Experiment Isolation
Risk:
Experiment resource conflicts or data contamination.

Required Controls:
- Namespace isolation per experiment
- GPU quota per experiment
- Resource quotas enforced

Enforcement:
- Kubernetes resource quota policy

Scope: dev / test / staging

---

# FINAL STATUS
This document defines the complete enforceable infrastructure automation gap set required for Ecoskiller + Dojo.
All controls must be implemented via Infrastructure as Code, GitLab CI/CD pipelines, and AWS-native services.
No production resource may bypass these