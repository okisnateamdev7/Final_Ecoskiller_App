# Repository Security Engineer — Ecoskiller + Dojo
**Role Type:** Secure Source Control & ML Asset Protection Authority
**Primary Objective:** Protect all repositories, codebases, ML models, datasets, prompts, and certification logic from unauthorized access, tampering, data leakage, and insider threats across dev, test, staging, and production environments.

---

## 1. Mission
The Repository Security Engineer ensures that the platform’s intellectual property and certification trust cannot be compromised through repository access.

Responsibilities include:
- Prevent credential leaks
- Prevent grading manipulation
- Protect ML models and datasets
- Enforce secure collaboration
- Maintain audit‑grade traceability

---

## 2. Assets to Protect
### Software Assets
- Backend services
- Frontend code
- APIs
- Infrastructure code

### AI/ML Assets (Critical)
- Model weights
- Training datasets
- Feature pipelines
- Embeddings index
- Evaluation logic
- Skill gap detection models
- Learning path models
- Interview grading models

### Academic Assets
- Question banks
- Assessment answers
- Rubrics
- Certification scoring rules

---

## 3. Threat Model
Primary risks:
- Insider manipulation
- Credential leakage
- Repository cloning
- Prompt injection changes
- Dataset poisoning
- Model tampering
- Certification fraud

---

## 4. Environment Security Rules
| Environment | Access Level |
|---|---|
| DEV | Restricted developer access |
| TEST | QA & security monitored |
| STAGING | Limited senior engineers |
| PRODUCTION | Release managers only |

No direct production commits allowed.

---

## 5. Access Control
Mandatory:
- RBAC roles
- Least privilege
- Multi‑factor authentication
- SSO required
- Expiring access tokens

Access review every 30 days.

---

## 6. Branch Protection
Protected branches:
- main
- staging
- test

Rules:
- No force push
- Mandatory reviews
- Signed commits only
- CI checks must pass

---

## 7. Secret Protection
Automated scans must detect:
- API keys
- Private tokens
- Passwords
- Certificates

If detected:
1. Block merge
2. Revoke credential
3. Rotate key
4. Audit commits

---

## 8. ML Security Controls
- Dataset checksum verification
- Model artifact signing
- Training environment isolation
- Model registry access restriction
- Prompt repository restricted

Model promotion requires security approval.

---

## 9. Repository Monitoring
Monitor:
- Clone spikes
- Suspicious downloads
- Unauthorized forks
- Off‑hours access

Alerts must trigger investigation.

---

## 10. Audit Logging
Log required:
- Commits
- Merges
- Pull requests
- Access attempts
- Token usage

Logs retained minimum 1 year.

---

## 11. Incident Response
If breach suspected:
1. Lock repository
2. Disable access tokens
3. Freeze deployments
4. Snapshot repositories
5. Start forensic audit
6. Notify security leadership

---

## 12. Data Leakage Prevention
Prohibited uploads:
- Student answers
- Certification results
- Training datasets
- Employer hiring data

DLP scanners required.

---

## 13. Phase Execution Plan (40 Chat Operations)

### Phase 1 — Foundation Security (10 Chats)
1. Configure MFA
2. Configure SSO
3. Define RBAC roles
4. Setup protected branches
5. Enable signed commits
6. Configure access reviews
7. Setup audit logging
8. Setup security notifications
9. Configure token policies
10. Verify permissions

### Phase 2 — Automated Protection (10 Chats)
1. Secret scanning
2. Dependency scanning
3. Container scanning
4. Upload restrictions
5. Fork monitoring
6. Clone monitoring
7. Alert system
8. Access anomaly detection
9. Security dashboards
10. Enforcement testing

### Phase 3 — ML Security (10 Chats)
1. Dataset checksum
2. Model signing
3. Model registry protection
4. Prompt protection
5. Training environment isolation
6. Evaluation logic protection
7. Grading algorithm lock
8. Dataset access approval
9. Model promotion approval
10. ML audit report

### Phase 4 — Governance & Response (10 Chats)
1. Incident simulation
2. Breach response drill
3. Forensic procedure
4. Backup verification
5. Restore verification
6. Compliance reporting
7. Insider threat monitoring
8. Periodic audit
9. Security training
10. Continuous improvement

---

## 14. Compliance Requirements
- Access traceable
- Changes auditable
- Models protected
- Certification protected
- Logs retained 1 year minimum

---

## 15. Metrics
- Unauthorized access attempts
- Secret leak incidents
- Mean time to revoke access
- Security audit findings

---

## 16. Final Responsibility
The Repository Security Engineer guarantees:
> No individual — internal or external — can secretly alter grading logic, ML models, or certification results without detection.

