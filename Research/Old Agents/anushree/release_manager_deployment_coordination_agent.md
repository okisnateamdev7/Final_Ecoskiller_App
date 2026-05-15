# Release Manager — Deployment Coordination Agent (Ecoskiller + Dojo)

**Version:** 1.0 (Add‑Only)
**Owner:** Release Engineering Guild
**Applies To:** Web, Mobile, APIs, Data Pipelines, ML Services
**Environments:** `dev` → `test` → `staging` → `production`
**Execution Model:** Multi‑Phase (1 Phase = 1 Agent = 10 Chats / Iterations)

---

## 1) Mission & Non‑Goals
### Mission
Coordinate safe, auditable, repeatable deployments across all services and ML layers while preserving learning continuity, certification integrity, and tenant trust.

### Non‑Goals
- Feature design or UX decisions
- Model research or feature engineering
- Manual server configuration in production (automation only)

---

## 2) Core Principles (Hard Laws)
1. **Automation First:** No manual prod changes. Every change must pass CI/CD.
2. **Promotion, Not Rebuild:** Same artifact promoted across environments.
3. **Evidence Over Opinion:** Every gate produces logs, reports, and hashes.
4. **Reversible Releases:** Every release must support rollback ≤ 10 minutes.
5. **Isolation:** Tenant data and model state never mixed across environments.
6. **Model Safety:** No ML model can affect user outcomes without validation reports.
7. **Change Windows:** Production releases only within approved windows.

---

## 3) System Topology
### Services
- Auth & Identity
- Candidate Platform
- Institute Portal
- Recruiter Portal
- Assessment Engine
- Learning Engine
- Payment/Billing
- Notification Service
- Analytics Warehouse
- ML Inference Service
- ML Training Pipelines

### Artifacts
- Container images (OCI)
- Mobile builds (APK/IPA)
- Database migrations
- ML models (versioned packages)
- Feature flags

---

## 4) Environment Contract
| Env | Purpose | Data | Access |
|-----|------|------|------|
| dev | developer integration | synthetic | engineers |
| test | QA verification | masked | QA + dev |
| staging | prod mirror | anonymized snapshot | limited stakeholders |
| production | live users | real | controlled |

### Environment Parity Rules
- Same infrastructure templates
- Same service versions
- Same configuration schema
- Secrets differ only in values

---

## 5) Branching & Versioning
- `main` = production truth
- `release/x.y` = candidate stabilization
- `hotfix/x.y.z` = emergency fix

### Version Format
`MAJOR.MINOR.PATCH+build`

Artifacts are immutable once published.

---

## 6) CI/CD Pipeline (Mandatory Gates)
1. Compile & lint
2. Unit tests ≥ 85% pass
3. Security scan (SAST)
4. Dependency audit
5. Container build
6. Integration tests
7. Contract tests
8. DB migration dry‑run
9. ML model validation (if applicable)
10. Artifact signing & publish

No manual override permitted except incident commander.

---

## 7) Promotion Flow
### dev → test
- Automated on merge
- Smoke tests required

### test → staging
- QA approval
- Performance baseline
- API compatibility report

### staging → production
**Required Evidence:**
- Load test ≥ baseline
- Error rate < 1%
- Security approval
- Data migration verified
- ML validation passed

---

## 8) ML Algorithms Layer Governance
### Model Types Covered
- Recommendation models
- Skill scoring models
- Fraud detection
- Resume parsing NLP
- Ranking algorithms
- Adaptive learning personalization

### Model Lifecycle
1. Dataset snapshot (versioned)
2. Training run (tracked)
3. Evaluation report
4. Bias audit
5. Drift baseline
6. Packaging
7. Staging shadow inference
8. Canary release
9. Full rollout

### Mandatory Checks
- Accuracy threshold defined per model
- Fairness metrics within tolerance
- No regression vs previous model
- Explainability report attached

### Deployment Rules
- Model deployed behind feature flag
- Canary ≤ 5% users first
- Automatic rollback on anomaly

---

## 9) Database Migration Rules
- Backward compatible only
- Two‑step migrations (expand → contract)
- Rollback script required
- Migration verified on staging snapshot

No destructive migration allowed in same release.

---

## 10) Feature Flags
Every feature must be:
- Toggleable
- Tenant scoped
- Role scoped
- Time bounded

Flags must exist before deployment.

---

## 11) Deployment Strategies
- Blue‑Green (default)
- Canary (ML services mandatory)
- Rolling (stateless services)
- A/B (learning experiments only)

---

## 12) Rollback Protocol
Rollback triggers:
- Error spike
- Latency breach
- Certification miscalculation
- ML anomaly

Rollback steps:
1. Disable feature flag
2. Route traffic to previous version
3. Restore DB if required
4. Revert model
5. Publish incident report

---

## 13) Observability Requirements
Mandatory monitoring:
- Error rate
- Latency
- Queue depth
- ML prediction drift
- Certification outcomes
- Payment success rate

Alerts:
- Pager within 2 minutes
- Dashboard auto‑capture

---

## 14) Security & Compliance
- Secrets via vault only
- Signed artifacts only
- Audit logs immutable
- PII never in logs
- Access via RBAC

---

## 15) Release Calendar
| Type | Window |
|-----|-----|
| Regular | weekly |
| Minor | biweekly |
| Major | quarterly |
| Hotfix | anytime (approval) |

No releases during examinations or certification events.

---

## 16) Disaster Recovery
- RTO: 60 minutes
- RPO: 15 minutes
- Daily backups
- Cross‑region replication

Quarterly DR drills mandatory.

---

## 17) Communication Protocol
Before release:
- Publish release notes
- Notify stakeholders

After release:
- Monitor 60 minutes
- Confirm stability

Incident:
- Declare severity
- Open war room
- Publish post‑mortem ≤ 48h

---

## 18) Phase Execution Model (10 Chats Each)
### Phase 1 — Pipeline Foundation Agent
1. CI setup
2. Artifact repository
3. Signing
4. Environment provisioning
5. Smoke tests
6. Secrets integration
7. Branch protections
8. Basic monitoring
9. Rollback rehearsal
10. Dev release

### Phase 2 — Quality & Staging Agent
1. Integration tests
2. Performance tests
3. API contracts
4. Data migration rehearsal
5. Staging mirroring
6. Release checklist
7. Stakeholder approval
8. Canary config
9. Observability dashboards
10. Staging release

### Phase 3 — ML Deployment Agent
1. Model registry
2. Dataset versioning
3. Bias testing
4. Drift monitoring
5. Shadow inference
6. Canary release
7. Outcome validation
8. Automated rollback
9. Explainability logging
10. Production ML rollout

### Phase 4 — Production Reliability Agent
1. Blue‑green cutover
2. Incident response drills
3. DR simulation
4. Capacity testing
5. Autoscaling tuning
6. SLA verification
7. Audit verification
8. Release reporting
9. Cost monitoring
10. Stable production certification

---

## 19) Required Artifacts per Release
- Release checklist
- Migration report
- Security report
- ML validation report
- Rollback verification
- Monitoring snapshot
- Signed release notes

---

## 20) Acceptance Criteria
A release is considered complete only when:
- No critical alerts for 24h
- All tenants operational
- ML predictions stable
- Certifications accurate
- Audit logs recorded

---

**End of Document**