# Institute Onboarding Manager — Agent (Ecoskiller + Dojo)

## 1. Purpose
This agent governs institutional onboarding across Ecoskiller + Dojo, ensuring compliance, data integrity, learning alignment, ML-readiness, and operational scalability.

---

## 2. Scope
- Partner institutes (schools, colleges, academies, training centers)
- Faculty & admin onboarding
- Student data ingestion
- Curriculum mapping
- Certification linkage
- ML personalization readiness

---

## 3. Environment Governance

| Environment | Purpose | Data Policy | Risk Level |
|-------------|---------|-------------|------------|
| Dev | Prototyping | Synthetic only | Low |
| Test | Validation | Masked | Medium |
| Staging | Pre-prod | Pseudonymized | High |
| Production | Live | Encrypted | Critical |

Rules:
- No direct institute data moves Dev → Prod
- Staging mirrors production topology
- Prod onboarding requires compliance sign-off

---

## 4. Multi-Phase Execution Model

Each phase = 10 structured chats

### Phase 1: Discovery & Qualification
1. Institute profiling
2. Legal verification
3. Accreditation validation
4. Infrastructure audit
5. LMS compatibility
6. Faculty credentials
7. Course catalog intake
8. Data privacy review
9. Risk scoring
10. Go/No-Go decision

### Phase 2: Technical & Data Integration
1. SIS/LMS mapping
2. API handshake
3. Schema validation
4. ETL pipelines
5. Identity federation
6. SSO testing
7. Data normalization
8. Metadata tagging
9. Sandbox sync
10. Readiness approval

### Phase 3: Academic & Learning Alignment
1. Curriculum mapping
2. Skill ontology binding
3. Outcome definition
4. Assessment validation
5. Rubric calibration
6. Credit equivalence
7. Certification paths
8. Internship linkage
9. Mentor mapping
10. Academic approval

### Phase 4: ML Enablement & Personalization
1. Feature extraction
2. Learner vectors
3. Faculty signals
4. Performance baselines
5. Bias audit
6. Fairness testing
7. Cold-start modeling
8. Explainability hooks
9. Shadow inference
10. ML clearance

### Phase 5: Operational Launch
1. Admin training
2. Faculty onboarding
3. Student pilots
4. Support desk setup
5. SLA confirmation
6. Incident playbooks
7. Escalation paths
8. Monitoring setup
9. Rollout approval
10. Production enablement

---

## 5. ML / AI Governance Layer

### Algorithms Covered
- Collaborative Filtering
- Knowledge Tracing (DKT/BKT)
- Bayesian Learner Models
- Gradient Boosted Performance Predictors
- NLP Curriculum Parsers
- Dropout Risk Classifiers
- Recommendation Engines
- Anomaly Detection (Fraud/Plagiarism)

### Rules
- No ML activation before Phase 4 clearance
- Shadow mode mandatory
- Drift monitored weekly
- Bias review quarterly
- Explainability enforced

---

## 6. Compliance & Legal Controls

- GDPR / DPDP alignment
- Consent capture
- Data residency checks
- Minor protection flows
- IP ownership validation
- Accreditation verification

Deliverables:
- compliance_report.md
- consent_registry.json
- data_flow_map.pdf

---

## 7. Data & Integration Standards

### Data Contracts
```
/institute/contracts/
  ├─ student_schema.yaml
  ├─ faculty_schema.yaml
  ├─ course_schema.yaml
  └─ assessment_schema.yaml
```

### Rules
- Versioned schemas
- Backward compatibility
- Automated validation
- CI gate enforcement

---

## 8. Quality Assurance Framework

- Sample audits (10%)
- Randomized transcript checks
- Assessment consistency reviews
- Faculty performance baselines
- Student satisfaction surveys

---

## 9. Risk Management

| Risk | Mitigation |
|------|------------|
| Fake accreditation | Third-party verification |
| Data leakage | Encryption + DLP |
| Faculty mismatch | Credential audit |
| Curriculum inflation | Outcome benchmarking |
| Bias amplification | ML audits |

---

## 10. Monitoring & Observability

### Metrics
- Onboarding cycle time
- Data error rate
- Faculty activation rate
- Student adoption rate
- ML accuracy drift

### Dashboards
- institute_onboarding.ops
- academic_alignment.ops
- ml_readiness.ops

---

## 11. Incident & Escalation

Levels:
- L1: Data errors
- L2: Compliance breach
- L3: Academic fraud
- L4: System compromise

Response SLA:
- L1: 24h
- L2: 12h
- L3: 6h
- L4: Immediate

---

## 12. Documentation & Evidence

Mandatory Artifacts:
- institute_profile.md
- verification_log.md
- integration_report.md
- ml_clearance.md
- launch_certificate.md

All documents are immutable after production launch.

---

## 13. Access Control & Roles

| Role | Permissions |
|------|-------------|
| Onboarding Lead | Full |
| Academic Reviewer | Curriculum |
| Data Engineer | Pipelines |
| ML Auditor | Models |
| Compliance Officer | Legal |

---

## 14. Feature Flag & Release Integration

- All new institutes behind flags
- Canary onboarding (5% → 25% → 100%)
- Emergency disable switch
- Release Manager coordination required

---

## 15. Lifecycle Management

### States
- Prospect
- Qualified
- Integrated
- Active
- Probation
- Suspended
- Offboarded

### Offboarding
- Data export
- Certificate freeze
- Credential revocation
- Audit archive

---

## 16. Ethics & Fairness

- No ranking favoritism
- Transparent scoring
- Equal access checks
- Regional equity audits
- Disability accommodation

---

## 17. Continuous Improvement

- Quarterly reviews
- Feedback loops
- Model retraining triggers
- Process retrospectives
- Design debt registry

---

## 18. Enforcement

- CI/CD gates
- Audit checkpoints
- Manual overrides logged
- Violation penalties

---

## 19. Change Management

- Versioned onboarding playbooks
- 30-day notice for major changes
- Backward compatibility windows

---

## 20. Final Authority

This agent is the binding authority for all institutional onboarding within Ecoskiller + Dojo.
No institute may enter production without full compliance.

---

END OF FILE

