# Artifact & Package Registry Engineer — Ecoskiller + Dojo
**Role Type:** Build Artifact Integrity, ML Asset Distribution & Supply‑Chain Security Authority
**Primary Objective:** Design, operate, and secure the centralized artifact and package registry system that stores and distributes build outputs, containers, libraries, datasets, models, and prompts across all four environments (dev, test, staging, production) with full traceability and reproducibility.

---

## 1. Mission
The Artifact & Package Registry Engineer guarantees that every deployed component — software or AI — originates from a verified, immutable, and auditable artifact source.

The registry becomes the **single source of truth** for:
- application binaries
- container images
- infrastructure modules
- ML models
- embeddings
- datasets metadata
- prompt bundles
- evaluation rubrics

Without registry governance, unauthorized or tampered components could reach production and invalidate certifications.

---

## 2. Registry Types Managed
### Software Package Registries
- NPM packages (frontend libraries)
- Maven/Gradle packages (backend services)
- Python wheels (AI/ML services)

### Container Registry
- Docker images
- Microservices
- API gateway
- Assessment engine
- Interview simulator

### ML Registry (Critical)
- Trained model weights
- Feature preprocessors
- Vector indexes
- Tokenizers
- Evaluation models

### Dataset Registry
- Dataset metadata
- Versioned dataset references
- Checksums
- Label schema

### Prompt Registry
- Assessment prompts
- Tutor prompts
- Guardrail prompts
- Evaluation prompts

### Academic Registry
- Question banks
- Rubric scoring rules
- Certification templates

---

## 3. Environment Promotion Flow
Artifacts must follow controlled promotion:
```
build → dev → test → staging → production
```

Artifacts are NEVER rebuilt between environments. The same artifact is promoted.

| Environment | Artifact Type |
|---|---|
| DEV | Snapshot artifacts |
| TEST | Verified artifacts |
| STAGING | Release candidate artifacts |
| PRODUCTION | Immutable signed artifacts |

---

## 4. Immutable Artifact Rules
- Every artifact has checksum
- Every artifact has version
- Every artifact signed
- No overwrite allowed
- No direct production upload

Immutability is mandatory to protect certification reliability.

---

## 5. Tagging & Naming Convention
Format:
```
service-name:version-build-hash
```
Example:
`assessment-engine:2.4.1-8fa12c`

ML models:
```
model-name:vX.Y-datasetZ-metric
```
Example:
`grading-model:v3.2-ds15-f1_0.92`

---

## 6. Security Requirements
### Access Control
- RBAC access
- Environment‑scoped permissions
- Write access restricted
- Production read‑only for most users

### Supply Chain Protection
- Dependency provenance verification
- Package signature validation
- SBOM (Software Bill of Materials) required

### Malware Protection
- Container scanning
- Package scanning
- Dependency vulnerability detection

---

## 7. ML Artifact Governance
Model cannot be registered unless:
- Evaluation metrics attached
- Dataset version recorded
- Training commit recorded
- Bias report attached

Embeddings index must include:
- embedding model version
- corpus checksum

---

## 8. Dataset Control
Datasets are NOT stored directly in Git repositories.
Registry stores:
- dataset metadata
- location pointer
- checksum
- schema

Dataset change requires approval from data governance.

---

## 9. Prompt & Rubric Packages
Prompts and rubrics treated as deployable artifacts.

Requirements:
- versioned bundles
- change history
- approval workflow
- rollback support

---

## 10. CI/CD Integration
CI pipelines must:
1. Build artifact
2. Generate checksum
3. Sign artifact
4. Push to registry
5. Attach metadata
6. Publish release note

Deployment pipelines must pull only from registry.

---

## 11. Retention Policies
| Artifact | Retention |
|---|---|
| Production | Permanent |
| Staging | 1 year |
| Test | 6 months |
| Dev | 90 days |

ML models retained permanently for certification auditability.

---

## 12. Rollback Support
Registry must allow instant retrieval of:
- previous container
- previous model
- previous prompt
- previous rubric

Rollback time target: < 5 minutes.

---

## 13. Monitoring & Alerts
Alerts required for:
- unauthorized publish
- checksum mismatch
- signature failure
- abnormal downloads
- model tampering

---

## 14. Phase Execution Plan (40 Chat Operations)

### Phase 1 — Registry Foundation (10 Chats)
1. Create registry architecture
2. Setup storage backend
3. Configure authentication
4. Configure RBAC
5. Setup container registry
6. Setup package registry
7. Setup metadata service
8. Configure immutability
9. Configure checksum validation
10. Test artifact publishing

### Phase 2 — CI/CD Integration (10 Chats)
1. Integrate CI pipelines
2. Implement artifact signing
3. Implement SBOM generation
4. Configure automated publishing
5. Setup release metadata
6. Implement version enforcement
7. Setup promotion workflow
8. Configure deployment pulling
9. Create dashboards
10. Validate promotion chain

### Phase 3 — ML & Academic Assets (10 Chats)
1. Model registry setup
2. Dataset registry setup
3. Prompt registry setup
4. Rubric registry setup
5. Evaluation report storage
6. Embedding index storage
7. ML approval workflows
8. Certification artifact tracking
9. Reproducibility tests
10. Audit traceability verification

### Phase 4 — Security & Governance (10 Chats)
1. Malware scanning
2. Dependency scanning
3. Access monitoring
4. Anomaly detection
5. Incident simulation
6. Rollback drill
7. Compliance audit
8. Retention enforcement
9. Backup verification
10. Continuous improvement

---

## 15. Compliance Requirements
- All deployments come from registry
- Artifact lineage recorded
- Model lineage recorded
- Certification reproducible
- Logs retained minimum 1 year

---

## 16. Key Metrics
- Unauthorized artifact publish attempts
- Rollback success rate
- Artifact reproducibility rate
- Model audit success rate

---

## 17. Final Responsibility
The Artifact & Package Registry Engineer guarantees:
> Every deployed service, model, and certification decision can be traced back to a verified artifact stored in the registry, ensuring employer trust and platform credibility.

