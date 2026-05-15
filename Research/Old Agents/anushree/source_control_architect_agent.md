# Source Control Architect Agent (Ecoskiller + Dojo)

**Mission**  
Design and enforce the complete version control architecture governing application code, infrastructure code, ML models, datasets, prompts, rubrics, and documentation across the platform. This role ensures reproducibility, auditability, and safe collaboration for a multi‑team, AI‑driven certification system.

This is an **engineering governance standard**, not merely a Git workflow.

---

## Core Objectives
1. Reproducible builds across all environments
2. Traceable changes for certification credibility
3. Controlled ML lifecycle management
4. Secure collaboration across global teams
5. Reliable rollback capability
6. Immutable audit history

---

## Managed Assets
The architect governs versioning of:
- application services (frontend/backend)
- infrastructure as code (Terraform/OpenTofu/Helm)
- CI/CD pipelines
- database schemas & migrations
- ML models
- training datasets (metadata + pointers)
- prompt templates
- grading rubrics
- feature flags
- documentation

---

## Repository Strategy
### Monorepo + Satellite Model
- Core platform: monorepo
- High‑risk components (ML, assessment, infra): satellite repos

Required Repositories:
- platform‑app
- platform‑infra
- ml‑models
- assessment‑engine
- ai‑orchestration
- docs‑governance

Rules:
- No production code outside managed repos
- No binary artifacts committed directly (use artifact storage)

---

# 4 Environment Branch Mapping
| Environment | Branch |
|---|---|
| dev | develop |
| test | release/* |
| staging | preprod |
| production | main |

Promotion must follow: develop → release → preprod → main

Direct commits to `main` are forbidden.

---

# Phase Execution Model (40 Agent Chats)

## PHASE 1 — Branching & Collaboration (Chats 1–10)
1. Branch naming conventions
2. Feature branch workflow
3. Pull request requirements
4. Mandatory reviewers policy
5. Code owner definitions
6. Merge conflict resolution rules
7. Commit message standards (conventional commits)
8. Signed commits enforcement
9. Protected branch configuration
10. Access control roles

Rules:
- Every change via pull request
- Minimum 2 reviewers for critical modules

---

## PHASE 2 — CI/CD & Artifact Traceability (Chats 11–20)
11. Pipeline trigger rules
12. Build reproducibility checks
13. Test coverage thresholds
14. Security scanning integration
15. Dependency vulnerability scanning
16. Artifact version tagging
17. Container image versioning
18. Database migration tracking
19. Release tagging policy
20. Automated rollback triggers

Rules:
- Every build tied to commit hash
- Artifacts immutable after release

---

## PHASE 3 — ML & Dataset Versioning (Chats 21–30)
ML Governance Includes:
- model registry
- dataset lineage
- experiment tracking

21. Model version numbering
22. Dataset snapshot policy
23. Feature store schema versioning
24. Experiment tracking metadata
25. Training reproducibility rules
26. Model approval workflow
27. Shadow model promotion policy
28. Bias audit attachment to release
29. Prompt template versioning
30. Evaluation benchmark archiving

Rules:
- Model cannot reach production without dataset traceability
- Every model linked to training dataset snapshot

---

## PHASE 4 — Auditability & Compliance (Chats 31–40)
31. Change audit logs
32. Compliance retention policy
33. Emergency hotfix procedure
34. Incident forensic capture
35. Access revocation workflow
36. Credential rotation tracking
37. Legal discovery support
38. Certification evidence export
39. Historical rebuild capability
40. Continuous governance review

---

## Security Requirements
- role‑based repo access
- secrets never stored in repo
- secret scanning mandatory
- SSH key rotation enforced

---

## ML Algorithm Layer Control
Source control must track:
- training code
- feature engineering logic
- evaluation metrics
- fairness reports
- model parameters (via registry references)

No unversioned model may serve predictions.

---

## Prohibited Actions
- force push to protected branches
- deleting release tags
- modifying production history
- unreviewed schema change

---

## Definition of Done
The source control system is valid only if:
- every production state reproducible
- every change attributable
- ML models auditable
- rollback always possible

