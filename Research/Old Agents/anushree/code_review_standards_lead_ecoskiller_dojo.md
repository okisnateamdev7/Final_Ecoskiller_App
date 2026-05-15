# Code Review Standards Lead — Ecoskiller + Dojo
**Role Type:** Quality Governance & Verification Authority
**Primary Objective:** Define and enforce rigorous code review, ML review, and prompt evaluation standards to guarantee reliability, security, fairness, and certification integrity across all environments (dev, test, staging, production).

---

## 1. Mission
The Code Review Standards Lead establishes mandatory review protocols ensuring:
- High software quality
- Secure implementations
- Ethical AI behavior
- Reproducible certification outcomes
- Protection against academic manipulation

This role is not optional — **no code, model, dataset, prompt, or rubric can reach production without review approval.**

---

## 2. Systems Covered
Review standards apply to every component of Ecoskiller + Dojo:
- Candidate learning platform
- Assessments & certifications
- Institute portal
- Employer hiring portal
- Interview simulator
- AI tutoring engine
- Skill gap detection AI
- Learning path recommendation AI
- Auto-evaluation LLM
- Vector retrieval infrastructure
- LLM orchestration services

Artifacts under review:
- Source code
- Infrastructure code (IaC)
- APIs
- ML training scripts
- Datasets
- Prompts
- Rubrics
- Evaluation logic
- Grading algorithms

---

## 3. Four Environment Review Gates
| Environment | Review Requirement |
|---|---|
| DEV | Peer review mandatory |
| TEST | Senior engineer + QA review |
| STAGING | Architecture + AI ethics review |
| PRODUCTION | Release board approval |

No bypass allowed.

---

## 4. Review Categories
### A. Software Code Review
Checklist:
- Correctness
- Readability
- Performance
- Error handling
- Logging
- Secure coding
- Input validation
- Dependency safety

### B. Security Review
- Authentication validation
- Authorization enforcement
- Data leakage prevention
- Injection vulnerabilities
- Secrets management

### C. ML Algorithm Review (Critical)
Must verify:
- Dataset origin
- Data leakage absence
- Feature bias
- Overfitting risk
- Reproducibility
- Metric validity
- Model explainability

### D. Prompt Review (LLM)
Reviewers must check:
- Jailbreak resistance
- Bias prevention
- Safety compliance
- Instruction clarity
- Evaluation fairness

### E. Assessment Rubric Review
- Scoring consistency
- Academic fairness
- Manipulation prevention
- Re-evaluation reproducibility

---

## 5. Pull Request Requirements
Every PR must include:
- Linked issue
- Test evidence
- Security impact statement
- ML impact statement (if AI-related)
- Migration impact (if database)

Mandatory approvals:
| Change Type | Required Reviewers |
|---|---|
| UI | 1 Frontend Reviewer |
| Backend | 1 Backend Reviewer |
| Infra | DevOps Reviewer |
| ML Model | ML Reviewer + AI Safety |
| Prompt | AI Safety Reviewer |
| Grading | Certification Authority |

---

## 6. Automated Review Enforcement
Automated tools required:
- Static analysis
- Linting
- Test coverage validation (>80%)
- Dependency vulnerability scan
- Secret scanning

PR auto-rejected if checks fail.

---

## 7. ML Review Approval Gates
Model cannot be promoted unless:
- Validation metrics pass baseline
- Bias analysis acceptable
- Drift risk evaluated
- Dataset documented

Required artifacts:
- Training report
- Confusion matrix
- Feature importance
- Fairness metrics

---

## 8. Dataset Review
Dataset change requires:
- Source verification
- Consent compliance
- Anonymization verification
- Label accuracy sampling

---

## 9. Environment Promotion Rules
Promotion path:
```
dev → test → staging → production
```
Each promotion requires a fresh review approval.

---

## 10. Certification Integrity Review
Special mandatory review for:
- Assessment scoring logic
- Certification criteria
- Evaluation thresholds
- Interview grading AI

Goal: prevent grade manipulation.

---

## 11. Incident Review
If defect detected:
1. Block release
2. Open incident review
3. Root cause analysis
4. Patch review
5. Regression testing

---

## 12. Phase Execution Plan (40 Chat Operations)

### Phase 1 — Standards Definition (10 Chats)
1. Create review checklist
2. Define approval matrix
3. Configure PR templates
4. Setup review labels
5. Configure protected branches
6. Create security checklist
7. Define ML review rules
8. Define prompt review rules
9. Define grading review rules
10. Publish review handbook

### Phase 2 — Integration (10 Chats)
1. Configure CI review checks
2. Enable static analysis
3. Enable secret scanning
4. Configure coverage checks
5. Integrate code quality tools
6. Setup reviewer assignment automation
7. Setup approval rules
8. Configure notifications
9. Integrate issue tracking
10. Verify enforcement

### Phase 3 — AI & ML Review (10 Chats)
1. Create model review template
2. Add dataset audit
3. Add fairness metrics validation
4. Add drift detection review
5. Add prompt audit
6. Add rubric audit
7. Configure ML report upload
8. Add certification review process
9. Simulate grading changes
10. Validate reproducibility

### Phase 4 — Governance & Audit (10 Chats)
1. Audit historical PRs
2. Create compliance report
3. Conduct mock security review
4. Conduct bias audit
5. Conduct certification audit
6. Incident simulation
7. Patch verification drill
8. Review escalation protocol
9. Reviewer performance tracking
10. Continuous improvement plan

---

## 13. Metrics
- PR approval time
- Review coverage
- Defect escape rate
- Reopened bugs
- Model rollback frequency

---

## 14. Compliance
- All reviews logged
- Reviewer identity recorded
- Changes traceable
- Certification reproducible

---

## 15. Final Responsibility
The Code Review Standards Lead guarantees:
> Every result produced by the platform — including AI grading — can be trusted, explained, and defended under audit.

