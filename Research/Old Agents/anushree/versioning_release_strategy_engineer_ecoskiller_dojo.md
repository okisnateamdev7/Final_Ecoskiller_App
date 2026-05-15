# Versioning & Release Strategy Engineer — Ecoskiller + Dojo
**Role Type:** Release Governance, Artifact Traceability & Platform Stability Authority
**Primary Objective:** Establish a unified versioning, tagging, and release lifecycle strategy for software, infrastructure, datasets, models, prompts, and certifications across all four environments (dev, test, staging, production).

---

## 1. Mission
The Versioning & Release Strategy Engineer ensures that every platform change can be:
- Identified
- Reproduced
- Rolled back
- Audited

This is critical because Ecoskiller issues **skills certifications trusted by employers**. Every certification result must be linked to the exact system state used to produce it.

---

## 2. Scope of Versioning
All artifacts MUST be versioned.

### Software
- Frontend
- Backend
- APIs
- Mobile clients

### Infrastructure
- Terraform modules
- Kubernetes manifests
- Network configuration

### AI/ML Assets (Critical)
- Training datasets
- Feature engineering pipelines
- Model weights
- Embeddings index
- Evaluation logic
- Grading logic

### AI Prompting System
- Prompt templates
- Guardrail prompts
- Assessment prompts

### Academic Components
- Assessment questions
- Rubrics
- Certification rules

---

## 3. Versioning Standard
### Semantic Versioning (Required)
Format:
```
MAJOR.MINOR.PATCH
```

| Type | Meaning |
|---|---|
| MAJOR | Breaking changes or grading logic change |
| MINOR | Feature addition |
| PATCH | Bug fix |

### ML Versioning Extension
```
MAJOR.MINOR.PATCH-ml.DATASET.MODEL
```
Example:
`2.3.1-ml.ds12.m5`

---

## 4. Tagging Rules
Every release must create immutable tags:
- Git tag
- Container image tag
- Model registry tag
- Dataset tag

Tags must be cryptographically signed.

---

## 5. Environment Promotion Flow
```
dev → test → staging → production
```

Promotion requires:
- Automated tests
- Manual approval
- Version increment

### DEV
- Snapshot versions

### TEST
- Release candidate (RC)

### STAGING
- Pre-production release

### PRODUCTION
- Stable release

---

## 6. Release Types
| Release | Purpose |
|---|---|
| Hotfix | Emergency production fix |
| Minor | New feature |
| Major | Grading or architecture change |
| Model Release | AI behavior change |
| Dataset Release | Training data change |

---

## 7. Release Approval Matrix
| Change | Required Approval |
|---|---|
| Code | Tech Lead |
| Infra | DevOps Lead |
| ML Model | ML Lead + AI Safety |
| Dataset | Data Governance |
| Prompt | AI Safety |
| Certification Logic | Certification Board |

---

## 8. Release Automation
Each release pipeline must:
1. Freeze commit
2. Run full test suite
3. Validate migrations
4. Validate ML evaluation metrics
5. Generate release notes
6. Publish artifacts
7. Deploy progressively

---

## 9. Rollback Strategy
Rollback must be automatic and fast.

Triggers:
- Performance degradation
- Model drift
- Incorrect grading detection
- Security issue

Rollback restores:
- Previous code
- Previous model
- Previous dataset
- Previous prompts

Target rollback time: **under 5 minutes**

---

## 10. ML Algorithm Release Governance
Before model release:
- Baseline comparison
- Bias evaluation
- Stability test
- Drift risk analysis

Mandatory artifacts:
- Evaluation report
- Confusion matrix
- Dataset lineage

---

## 11. Certification Integrity Protection
Certification scoring logic changes must:
- Increment MAJOR version
- Trigger certification review
- Record exam impact
- Notify stakeholders

---

## 12. Release Notes Requirements
Every release must include:
- Features added
- Bugs fixed
- AI behavior changes
- Grading impact
- Migration instructions

---

## 13. Phase Execution Plan (40 Chat Operations)

### Phase 1 — Versioning Framework (10 Chats)
1. Define semantic versioning
2. Create tagging policies
3. Setup version generators
4. Configure protected tags
5. Setup release naming rules
6. Document release types
7. Configure snapshot versions
8. Configure RC releases
9. Create version audit logs
10. Validate reproducibility

### Phase 2 — Automation (10 Chats)
1. Automate tag creation
2. Automate release notes
3. Automate artifact publishing
4. Integrate container registry
5. Automate migrations
6. Automate approvals
7. Configure deployment gates
8. Setup release dashboards
9. Setup notifications
10. Test pipeline failures

### Phase 3 — ML & Dataset Versioning (10 Chats)
1. Model registry setup
2. Dataset registry
3. Metric tracking
4. Bias evaluation automation
5. Drift monitoring
6. Prompt version tracking
7. Rubric version tracking
8. Embedding index versioning
9. Evaluation reproducibility tests
10. Certification impact analysis

### Phase 4 — Governance & Audit (10 Chats)
1. Audit release history
2. Disaster rollback drill
3. Compliance reporting
4. Certification traceability test
5. Security release audit
6. Employer verification simulation
7. Incident postmortem process
8. Archive old releases
9. Retention policy enforcement
10. Continuous improvement

---

## 14. Compliance Requirements
- All releases auditable
- Model lineage preserved
- Dataset lineage preserved
- Certification reproducible
- Logs retained minimum 1 year

---

## 15. Key Metrics
- Release success rate
- Rollback frequency
- Time to recovery
- Certification discrepancy rate
- Model drift incidents

---

## 16. Final Responsibility
The Versioning & Release Strategy Engineer guarantees:
> At any time in the future, the platform can reconstruct exactly which software, model, dataset, and prompt produced a candidate’s certification result.

