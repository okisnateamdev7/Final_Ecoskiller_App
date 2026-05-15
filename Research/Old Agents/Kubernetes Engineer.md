# Kubernetes Engineer Git Agent — GAP CLOSURE MASTER FILE (K-GAP-01 → K-GAP-30)

Status: LOCKED · ADD-ONLY · CLUSTER-EXECUTION-GRADE

This file consolidates **ALL IDENTIFIED KUBERNETES-SPECIFIC GAPS** for the Ecoskiller + Gojo platform.

Scope is strictly:
- Kubernetes engineering
- AWS EKS clusters
- GitOps-controlled cluster state
- ML training & inference runtime

No DevOps CI/CD, product, UI, or business logic is included.

Each gap contains:
- What is missing
- Why it is dangerous
- Exact implementation instructions
- Enforcement layer

---

## K-GAP-01 — GitOps Controller Lock
**Missing:** Explicit GitOps controller
**Risk:** Drift, manual kubectl usage
**Instruction:**
- Use ArgoCD per environment
- Auto-sync + self-heal enabled
- Manual sync forbidden in prod
**Enforce At:** Kubernetes, Git

---

## K-GAP-02 — Cluster Bootstrap (Day-0) Governance
**Missing:** Ordered bootstrap sequence
**Risk:** Broken admission & security
**Instruction:**
- Bootstrap repo
- Order: CRDs → controllers → policies → workloads
- Immutable base layer
**Enforce At:** GitOps

---

## K-GAP-03 — Node OS & AMI Lifecycle
**Missing:** Node OS governance
**Risk:** Unpatched nodes
**Instruction:**
- Use immutable AMIs (Bottlerocket or AL2)
- No SSH in prod
- Rolling node replacement
**Enforce At:** AWS EKS

---

## K-GAP-04 — Pod Identity (IRSA)
**Missing:** Pod-level AWS IAM
**Risk:** Node-role credential abuse
**Instruction:**
- Mandatory IRSA
- One IAM role per service account
**Enforce At:** AWS IAM, Kubernetes

---

## K-GAP-05 — Network Egress Control
**Missing:** Outbound traffic restrictions
**Risk:** Data exfiltration
**Instruction:**
- Default-deny egress NetworkPolicies
- NAT allowlists
**Enforce At:** Kubernetes Networking

---

## K-GAP-06 — Admission Control Hard-Fail Rules
**Missing:** Enforced deny policies
**Risk:** Unsafe pods admitted
**Instruction:**
- Gatekeeper deny rules:
  - no :latest
  - no privileged
  - limits required
**Enforce At:** Admission Control

---

## K-GAP-07 — Secret Consumption Pattern
**Missing:** Secure secret injection
**Risk:** Secrets leaked in manifests
**Instruction:**
- CSI Secrets Store
- No base64 secrets in Git
**Enforce At:** Kubernetes, Vault

---

## K-GAP-08 — StatefulSet Upgrade Safety
**Missing:** Upgrade playbooks
**Risk:** Data corruption
**Instruction:**
- Ordered rollout
- PreStop hooks
- Read-only drain
**Enforce At:** Kubernetes

---

## K-GAP-09 — ML Job Runtime Quotas
**Missing:** Job bounds
**Risk:** Runaway ML jobs
**Instruction:**
- Max runtime per Job
- Auto-kill on overrun
- Per-tenant quotas
**Enforce At:** Kubernetes

---

## K-GAP-10 — GPU Scheduling Isolation
**Missing:** GPU node protection
**Risk:** GPU starvation
**Instruction:**
- Dedicated GPU node groups
- Taints & tolerations
**Enforce At:** Kubernetes Scheduler

---

## K-GAP-11 — ML Model Rollback Semantics
**Missing:** Instant model rollback
**Risk:** Service-model mismatch
**Instruction:**
- Model version via ConfigMap
- Rollback without redeploy
**Enforce At:** ML Runtime

---

## K-GAP-12 — Multi-Tenant Namespace Hardening
**Missing:** Tenant isolation inside namespaces
**Risk:** Tenant bleed
**Instruction:**
- Per-tenant quotas
- Label-based network isolation
**Enforce At:** Kubernetes

---

## K-GAP-13 — CronJob Governance
**Missing:** Background job control
**Risk:** Overlapping execution
**Instruction:**
- ConcurrencyPolicy=Forbid
- Runtime limits
**Enforce At:** Kubernetes

---

## K-GAP-14 — Kubernetes Version Lifecycle
**Missing:** Upgrade strategy
**Risk:** Forced breaking upgrades
**Instruction:**
- Supported version window
- Staging rehearsal
- Rollback plan
**Enforce At:** Cluster Ops

---

## K-GAP-15 — Degraded-Mode Operation
**Missing:** Partial availability plan
**Risk:** Full outage
**Instruction:**
- Read-only modes
- PriorityClasses enforced
**Enforce At:** Kubernetes

---

## K-GAP-16 — Priority & Preemption Rules
**Missing:** Workload importance ordering
**Risk:** Core services evicted
**Instruction:**
- PriorityClasses
- Core > Realtime > ML > Batch
**Enforce At:** Scheduler

---

## K-GAP-17 — Backup Verification
**Missing:** Restore validation
**Risk:** Unusable backups
**Instruction:**
- Restore test namespace
- Quarterly restore jobs
**Enforce At:** Ops

---

## K-GAP-18 — Drift Resolution Authority
**Missing:** Drift winner rule
**Risk:** Manual cluster mutation
**Instruction:**
- Git always wins
- Auto-revert manual changes
**Enforce At:** GitOps

---

## K-GAP-19 — API Server Access Boundary
**Missing:** Control-plane exposure rules
**Risk:** Unauthorized access
**Instruction:**
- Private API endpoint
- VPN / bastion access only
**Enforce At:** AWS EKS

---

## K-GAP-20 — etcd Protection Awareness
**Missing:** State recovery clarity
**Risk:** Cluster state loss
**Instruction:**
- Snapshot awareness
- Restore runbook
**Enforce At:** Ops

---

## K-GAP-21 — CRD Lifecycle Governance
**Missing:** CRD version safety
**Risk:** Cluster breakage
**Instruction:**
- Version pinning
- Upgrade rehearsal
**Enforce At:** GitOps

---

## K-GAP-22 — Admission Webhook Availability
**Missing:** Failure handling
**Risk:** API freeze
**Instruction:**
- FailurePolicy defined
- Separate node pool
**Enforce At:** Kubernetes

---

## K-GAP-23 — kube-system Hardening
**Missing:** System namespace protection
**Risk:** Core compromise
**Instruction:**
- RBAC lockdown
- No unreviewed pods
**Enforce At:** Kubernetes

---

## K-GAP-24 — Image Trust Boundary
**Missing:** Registry trust
**Risk:** Supply-chain attack
**Instruction:**
- Registry allowlist
- Signed images only
**Enforce At:** Admission Control

---

## K-GAP-25 — Node Drain Semantics
**Missing:** Safe eviction process
**Risk:** Data loss
**Instruction:**
- Drain sequencing
- Pre-drain hooks
**Enforce At:** Ops

---

## K-GAP-26 — Cross-AZ Scheduling
**Missing:** AZ resilience
**Risk:** AZ outage
**Instruction:**
- Topology spread constraints
- Anti-affinity
**Enforce At:** Scheduler

---

## K-GAP-27 — Service Mesh Decision Lock
**Missing:** Mesh stance
**Risk:** Inconsistent expectations
**Instruction:**
- Explicit mesh/no-mesh decision
- Document rationale
**Enforce At:** Architecture

---

## K-GAP-28 — Feature Store Node Isolation
**Missing:** Performance isolation
**Risk:** Inference latency spikes
**Instruction:**
- Dedicated node pool
- Guaranteed QoS
**Enforce At:** Kubernetes

---

## K-GAP-29 — Large Model Artifact Handling
**Missing:** Startup safety
**Risk:** OOM / slow boots
**Instruction:**
- Init containers
- Read-only volumes
**Enforce At:** Kubernetes

---

## K-GAP-30 — Cluster Decommissioning
**Missing:** Safe teardown
**Risk:** Orphaned data
**Instruction:**
- Ordered teardown runbook
- Final backups enforced
**Enforce At:** Ops

---

## FINAL ENFORCEMENT RULE

No Kubernetes cluster may be declared **Production-Ready** until **ALL K-GAP-01 → K-GAP-30** are closed, verified, and tested.

---

END OF FILE

