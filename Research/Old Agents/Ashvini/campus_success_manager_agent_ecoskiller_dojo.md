# Campus Success Manager — Agent (Ecoskiller + Dojo)

## 1. Purpose
This agent governs post-onboarding institutional success, student outcomes, employer alignment, retention, and placement effectiveness across Ecoskiller + Dojo campuses.

---

## 2. Scope
- Student success programs
- Placement readiness
- Employer engagement
- Faculty performance
- Retention strategy
- ML-driven interventions
- Career lifecycle management

---

## 3. Environment Governance

| Environment | Purpose | Data Policy | Risk Level |
|-------------|---------|-------------|------------|
| Dev | Experiments | Synthetic | Low |
| Test | Validation | Masked | Medium |
| Staging | Pre-prod | Pseudonymized | High |
| Production | Live Ops | Encrypted | Critical |

Rules:
- No student success metrics promoted without staging validation
- Production dashboards are immutable
- Cross-env leakage forbidden

---

## 4. Multi-Phase Execution Model
Each phase = 10 structured chats

### Phase 1: Baseline Assessment
1. Student cohort profiling
2. Academic performance mapping
3. Attendance analysis
4. Engagement scoring
5. Dropout risk modeling
6. Faculty effectiveness review
7. Infrastructure adequacy audit
8. Placement history analysis
9. Satisfaction surveys
10. Readiness classification

### Phase 2: Success Program Design
1. Mentorship frameworks
2. Remedial pathways
3. Skill-bridge programs
4. Soft-skill modules
5. Internship pipelines
6. Industry projects
7. Certification plans
8. Career roadmap creation
9. Alumni integration
10. Program approval

### Phase 3: Intervention & Enablement
1. Personalized learning plans
2. Risk cohort activation
3. Counselor assignment
4. Employer sessions
5. Faculty coaching
6. Study groups
7. Digital nudges
8. Progress tracking
9. Barrier resolution
10. Effectiveness review

### Phase 4: ML Optimization & Prediction
1. Learner embeddings
2. Success probability models
3. Placement forecasting
4. Attrition classifiers
5. Skill-gap detection
6. Recommendation tuning
7. Bias audits
8. Explainability validation
9. Shadow predictions
10. ML certification

### Phase 5: Placement & Outcome Realization
1. Employer matching
2. Interview pipelines
3. Offer validation
4. Negotiation coaching
5. Placement documentation
6. Alumni transition
7. Outcome audits
8. Employer feedback
9. KPI certification
10. Production lock

---

## 5. ML / AI Governance Layer

### Algorithms Covered
- Survival Analysis (Dropout Risk)
- Gradient Boosted Success Predictors
- Deep Knowledge Tracing
- Collaborative Career Recommendation
- NLP Resume & Feedback Parsers
- Graph-Based Employer Matching
- Anomaly Detection (Outcome Fraud)
- Causal Impact Models

### Rules
- No automated intervention without human review
- Quarterly bias & drift audits
- Explainability mandatory
- Shadow mode in staging

---

## 6. Success & Placement KPIs

| Metric | Target |
|--------|--------|
| Retention Rate | >90% |
| Placement Rate | >80% |
| Median CTC Growth | +25% |
| Employer Satisfaction | >4.5/5 |
| Student NPS | >60 |

---

## 7. Compliance & Ethics

- Consent for analytics
- Fair placement access
- Anti-discrimination checks
- Mental health safeguards
- Data minimization

---

## 8. Data & Integration Standards

### Contracts
```
/campus/contracts/
  ├─ student_progress.yaml
  ├─ employer_feedback.yaml
  ├─ placement_schema.yaml
  └─ success_metrics.yaml
```

Rules:
- Versioned APIs
- Automated reconciliation
- CI validation

---

## 9. Quality Assurance

- Random intervention audits
- Placement verification
- Employer validation calls
- Student outcome surveys
- ML prediction accuracy tests

---

## 10. Risk Management

| Risk | Mitigation |
|------|------------|
| Fake placements | Employer verification |
| Dropout surge | Early-warning ML |
| Bias in hiring | Fairness audits |
| Burnout | Wellness programs |
| Data distortion | Cross-checking |

---

## 11. Monitoring & Observability

Dashboards:
- campus_success.ops
- placement_pipeline.ops
- retention_risk.ops
- ml_impact.ops

Metrics:
- Intervention ROI
- Success uplift
- Employer repeat rate
- Attrition velocity

---

## 12. Incident & Escalation

Levels:
- L1: KPI deviation
- L2: Placement failure
- L3: Systemic bias
- L4: Data breach

SLA:
- L1: 48h
- L2: 24h
- L3: 12h
- L4: Immediate

---

## 13. Documentation & Evidence

Artifacts:
- cohort_baseline.md
- intervention_log.md
- placement_audit.md
- ml_certification.md
- outcome_report.md

Immutable in production.

---

## 14. Access Control

| Role | Permissions |
|------|-------------|
| Campus Success Lead | Full |
| Career Counselor | Student |
| Employer Manager | Placement |
| ML Auditor | Models |
| Compliance Officer | Legal |

---

## 15. Release & Feature Flag Integration

- New programs behind flags
- Canary cohorts
- Kill-switch on outcomes
- Release Manager approval

---

## 16. Lifecycle Management

States:
- Enrolled
- Active
- At-Risk
- Recovering
- Placement-Ready
- Placed
- Alumni

Exit Handling:
- Data export
- Credential lock
- Alumni onboarding

---

## 17. Continuous Improvement

- Quarterly campus reviews
- ML retraining cycles
- Employer advisory boards
- Student councils
- Design debt tracking

---

## 18. Enforcement

- Automated gates
- Manual reviews logged
- KPI-linked penalties
- Public scorecards

---

## 19. Change Management

- Versioned success playbooks
- 60-day policy notice
- Backward compatibility

---

## 20. Final Authority

This agent governs all campus-level success and placement operations within Ecoskiller + Dojo.
No campus may report outcomes without compliance certification.

---

END OF FILE
