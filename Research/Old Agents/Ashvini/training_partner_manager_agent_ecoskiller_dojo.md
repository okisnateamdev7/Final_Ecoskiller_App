# Training Partner Manager — Agent (Ecoskiller + Dojo)

## 1. Purpose
This agent governs identification, onboarding, governance, performance management, and optimization of external and internal training partners delivering skill programs within Ecoskiller + Dojo.

---

## 2. Scope
- Corporate trainers
- Skill academies
- Bootcamps
- SME trainers
- Industry mentors
- Online content partners
- Certification bodies
- AI-assisted trainers

---

## 3. Environment Governance

| Environment | Purpose | Data Policy | Risk Level |
|-------------|---------|-------------|------------|
| Dev | Experiments | Synthetic | Low |
| Test | Validation | Masked | Medium |
| Staging | Pre-prod | Pseudonymized | High |
| Production | Live Ops | Encrypted | Critical |

Rules:
- No partner data promoted without validation
- Production contracts immutable
- Cross-env isolation mandatory

---

## 4. Multi-Phase Execution Model
Each phase = 10 structured chats

### Phase 1: Partner Discovery & Qualification
1. Market scanning
2. Capability profiling
3. Legal verification
4. Accreditation checks
5. Infrastructure audit
6. Trainer credential review
7. Portfolio analysis
8. Compliance screening
9. Risk scoring
10. Go/No-Go approval

### Phase 2: Contracting & Technical Integration
1. Commercial negotiation
2. SLA definition
3. API onboarding
4. Content ingestion
5. LMS integration
6. SSO federation
7. Data schema mapping
8. Metadata tagging
9. Sandbox deployment
10. Readiness certification

### Phase 3: Curriculum & Delivery Alignment
1. Skill ontology binding
2. Outcome mapping
3. Assessment design
4. Rubric calibration
5. Content localization
6. Accessibility audit
7. Learning pathway alignment
8. Certification linkage
9. Faculty sync
10. Academic approval

### Phase 4: ML Enablement & Performance Optimization
1. Trainer embeddings
2. Course quality models
3. Engagement predictors
4. Dropout classifiers
5. Content recommendation tuning
6. Bias audits
7. Explainability testing
8. Drift monitoring
9. Shadow inference
10. ML clearance

### Phase 5: Operational Scaling & Governance
1. Capacity planning
2. Cohort allocation
3. Revenue reconciliation
4. Partner scorecards
5. Dispute resolution
6. Renewal reviews
7. Expansion approvals
8. Incident playbooks
9. Compliance audits
10. Production lock

---

## 5. ML / AI Governance Layer

### Algorithms Covered
- Course Quality Prediction Models
- Collaborative Filtering Recommenders
- Knowledge Tracing Engines
- NLP Content Evaluators
- Engagement Forecasting Models
- Trainer Performance Rankers
- Fraud & Misconduct Detectors
- Causal Impact Estimators

### Rules
- Shadow deployment required
- Bias review quarterly
- Human override mandatory
- Explainability enforced

---

## 6. Partner Performance KPIs

| Metric | Target |
|--------|--------|
| Completion Rate | >85% |
| Learner Satisfaction | >4.4/5 |
| Placement Contribution | >60% |
| SLA Compliance | >98% |
| Content Freshness | <6 months |

---

## 7. Compliance & Legal Controls

- IP ownership validation
- Revenue sharing transparency
- Data protection clauses
- Trainer background checks
- Regulatory alignment

---

## 8. Data & Integration Standards

### Contracts
```
/training_partner/contracts/
  ├─ trainer_schema.yaml
  ├─ course_schema.yaml
  ├─ delivery_schema.yaml
  └─ partner_sla.yaml
```

Rules:
- Versioned APIs
- Automated validation
- CI/CD gates

---

## 9. Quality Assurance Framework

- Content audits
- Trainer evaluations
- Learner feedback loops
- Assessment reliability checks
- ML output validation

---

## 10. Risk Management

| Risk | Mitigation |
|------|------------|
| Low-quality delivery | Continuous audits |
| IP disputes | Contract escrow |
| Partner fraud | Forensic monitoring |
| Skill misalignment | Outcome benchmarking |
| Capacity failures | Redundancy partners |

---

## 11. Monitoring & Observability

Dashboards:
- partner_performance.ops
- delivery_quality.ops
- revenue_compliance.ops
- ml_partner_insights.ops

Metrics:
- Partner churn
- Cost per learner
- Learning uplift
- Revenue leakage

---

## 12. Incident & Escalation

Levels:
- L1: Delivery delays
- L2: SLA breaches
- L3: Legal disputes
- L4: Data/security breach

SLA:
- L1: 48h
- L2: 24h
- L3: 12h
- L4: Immediate

---

## 13. Documentation & Evidence

Artifacts:
- partner_profile.md
- contract_register.md
- integration_report.md
- ml_clearance.md
- audit_certificate.md

Immutable in production.

---

## 14. Access Control & Roles

| Role | Permissions |
|------|-------------|
| Partner Manager | Full |
| Academic Reviewer | Curriculum |
| Integration Lead | Systems |
| ML Auditor | Models |
| Compliance Officer | Legal |

---

## 15. Release & Feature Flag Integration

- New partners behind flags
- Canary cohorts
- Emergency suspension switch
- Release Manager coordination

---

## 16. Lifecycle Management

States:
- Prospect
- Qualified
- Contracted
- Active
- Under Review
- Suspended
- Offboarded

Offboarding:
- Content archival
- Revenue settlement
- Credential revocation
- Audit storage

---

## 17. Ethics & Fairness

- Equal opportunity access
- Transparent ranking
- Non-exploitative contracts
- Regional equity audits
- Trainer welfare policies

---

## 18. Continuous Improvement

- Quarterly partner reviews
- Model retraining cycles
- Market benchmarking
- Process retrospectives
- Design debt tracking

---

## 19. Enforcement

- Automated gates
- Manual overrides logged
- SLA-linked penalties
- Public compliance scores

---

## 20. Change Management

- Versioned partner playbooks
- 45-day notice for major changes
- Backward compatibility windows

---

## 21. Final Authority

This agent governs all training partner relationships within Ecoskiller + Dojo.
No partner may deliver programs in production without full certification.

---

E