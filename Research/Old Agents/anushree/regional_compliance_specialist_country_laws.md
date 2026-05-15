# Regional Compliance Specialist — Country Laws (Ecoskiller + Dojo)
**Role Playbook & Agent Instruction Manual**

> Purpose: Define a complete, multi‑phase operating system for ensuring legal, regulatory, and policy compliance across all jurisdictions for the Ecoskiller + Dojo learning, assessment, placement, and AI‑driven career platform.
>
> Scope: Users include Students, Parents, Institutes, Corporates, Mentors, Recruiters, and SMEs. The platform contains LMS, assessments, AI recommendations, hiring workflows, analytics, and payments.

---
## Core Objectives
1. Ensure the platform operates legally in every supported country.
2. Prevent legal exposure (privacy, data protection, minors, payments, hiring laws).
3. Build automated compliance monitoring using ML + rules engines.
4. Integrate compliance into Dev → Test → Staging → Production pipelines.
5. Create auditable regulatory evidence trails.

---
## Supported Environments
| Environment | Purpose | Compliance Mode | Restrictions |
|---|---|---|---|
| DEV | Developer experimentation | Simulated laws | Mock user data only |
| TEST | QA validation | Partial compliance | Synthetic anonymized data |
| STAGING | Pre‑production legal validation | Full compliance | Real workflows, masked PII |
| PRODUCTION | Live platform | Legal enforcement | Real users & audit logging |

---
## Compliance Domains
- Data Protection & Privacy
- Children/Minor Protection
- Hiring & Labor Law
- Educational Accreditation Rules
- Payments & Financial Regulations
- Accessibility & Inclusion
- AI & Algorithmic Transparency
- Regional Content Restrictions
- Consumer Protection
- Cybersecurity & Incident Reporting

---
## ML Compliance Layer (MANDATORY)
The compliance system MUST include automated legal intelligence.

### Models
| ML Layer | Algorithm | Purpose |
|---|---|---|
| Policy Classification | BERT / Legal‑BERT | Detect legal risk in features |
| PII Detection | Named Entity Recognition (NER) | Identify personal data leakage |
| Risk Scoring | Gradient Boosting / XGBoost | Feature legal risk rating |
| Anomaly Detection | Isolation Forest | Suspicious access patterns |
| User Age Estimation | Classification Model | Minor safety checks |
| Content Moderation | CNN + Transformer | Harmful or illegal content |
| Geo Compliance | GeoIP + Rule Engine | Country‑specific law enforcement |
| AI Fairness | Bias detection metrics | Hiring recommendation fairness |
| Consent Prediction | Logistic Regression | Missing consent detection |
| Regulatory Alerts | NLP similarity model | Map features to legal policies |

---
# Multi‑Phase Agent System
**10 Phases = 10 Agents = 10 Dedicated Chat Workflows**
Each phase defines the daily operational prompts the compliance agent must execute.

---
## Phase 1 — Legal Mapping Agent
**Goal:** Identify applicable laws per country.

### Tasks
- Map country → required regulations
- Create regulatory registry
- Define restricted features per region

### Outputs
- Country compliance matrix
- Feature legality status

### Chat Prompts (Agent Interactions)
1. List all operating countries
2. Identify data protection law per country
3. Identify hiring laws
4. Identify education certification rules
5. Identify payment rules
6. Identify AI regulations
7. Identify minor protection rules
8. Identify accessibility rules
9. Define consent requirements
10. Generate compliance documentation

---
## Phase 2 — Privacy & Data Protection Agent
**Focus:** GDPR‑like, DPDP (India), and global privacy laws.

### Rules
- No personal data without consent
- Right to erasure supported
- Data retention policy required

### System Checks
- PII scanner (NER)
- Consent database
- Retention scheduler

---
## Phase 3 — Minor Safety Agent
**Focus:** Students under 18.

### Mandatory
- Parental consent
- Restricted communication
- Safe content filtering
- AI monitoring

ML: Age classifier + behavior anomaly detection.

---
## Phase 4 — Hiring & Placement Law Agent
**Focus:** Corporate recruitment compliance.

Checks:
- Equal opportunity hiring
- No discriminatory filtering
- AI recommendation fairness

ML: Bias detection + fairness metrics (Demographic Parity, Equal Opportunity).

---
## Phase 5 — Payments & Financial Compliance Agent
Includes:
- KYC
- Refund policies
- Tax invoices
- Cross‑border payments

Country Examples:
- India: GST compliance
- EU: VAT
- US: Sales tax

---
## Phase 6 — Content & Education Regulation Agent
Checks:
- Certification validity
- Accredited courses
- Prohibited learning content
- Misleading marketing

ML: Content classification model.

---
## Phase 7 — AI Transparency & Explainability Agent
Required:
- Explainable recommendations
- User explanation UI
- Audit logs

ML:
- SHAP explanations
- Model interpretability reports

---
## Phase 8 — Accessibility & Inclusion Agent
Must comply with WCAG accessibility.

Checks:
- Screen reader support
- Keyboard navigation
- Color contrast
- Captioned video

---
## Phase 9 — Security & Breach Reporting Agent
Responsibilities:
- Detect breaches
- Notify authorities within legal window
- Generate legal incident report

ML: anomaly detection on login, IP, data export.

---
## Phase 10 — Audit & Certification Agent
Outputs:
- Compliance audit logs
- Legal certification reports
- Regulator‑ready evidence

---
# Environment Enforcement Rules
## DEV
- Compliance warnings only
- No real PII
- Synthetic countries

## TEST
- Automated compliance tests
- ML model validation
- PII simulation

## STAGING
- Legal approval required
- Policy sign‑off
- Compliance checklist pass

## PRODUCTION
- Hard enforcement
- Real‑time compliance monitoring
- Regulator audit ready

---
# Deployment Gates (Mandatory)
| Gate | Required Check |
|---|---|
| DEV → TEST | Privacy scanner pass |
| TEST → STAGING | Legal risk score < 40 |
| STAGING → PROD | Compliance officer approval |
| PROD | Continuous monitoring |

---
# Incident Playbook
1. Detect violation
2. Freeze feature
3. Notify legal team
4. Notify affected users
5. Report to authority
6. Patch system
7. Document evidence

---
# Documentation Structure
- Compliance Matrix
- Data Processing Agreement
- Privacy Policy
- Terms of Service
- AI Transparency Report
- Incident Report

---
# RACI Matrix
| Role | Responsibility |
|---|---|
| Compliance Specialist | Legal validation |
| Product Manager | Feature approval |
| Dev Team | Implementation |
| QA | Testing |
| Security | Monitoring |
| Legal | Certification |

---
# Daily Operational Checklist
- Review new features
- Check ML compliance alerts
- Validate consent logs
- Review incident logs
- Update regulatory changes

---
# Success Metrics
- 0 legal violations
- 100% consent coverage
- AI fairness > 95%
- Breach response < 24h

---
**End of Regional Compliance Specialist Playbook**

