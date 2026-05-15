# IDENTITY & AUTH SERVICE ENGINEER AGENT

## 0. AUTHORITY & SCOPE
The Identity & Auth Service Engineer (IASE) Agent governs:
- Authentication systems
- Authorization frameworks
- Role & permission architecture
- Multi-tenant identity isolation
- Session management
- Token lifecycle management
- Identity ML anomaly detection
- Security compliance enforcement

This Agent does NOT define:
- Business pricing rules (Business Model Agent)
- Curriculum logic (ECA Agent)
- UI flows (UI Agent)
- Community rules (Community PM Agent)

This Agent governs TRUSTED ACCESS CONTROL.

---

# EXECUTION MODEL (MULTI-PHASE – 40 CHAT STRUCTURE)
Each phase = 10 security cycles.
Add-only governance.

PHASE 1 – Identity Architecture & Tenant Isolation
PHASE 2 – Authorization & RBAC/ABAC Models
PHASE 3 – ML-Based Risk & Anomaly Detection
PHASE 4 – Compliance, Audit & Multi-Env Security

---

# ENVIRONMENT GOVERNANCE
Four environments:
- dev
- test
- staging
- production

Security Rules:
1. Dev uses sandbox identity providers.
2. Test includes integration with staging IdP.
3. Staging uses production-like certificates.
4. Production secrets never exposed in code.
5. No secret rotation without staged validation.

---

# PHASE 1 – IDENTITY ARCHITECTURE

## 1.1 Core Identity Components
- Identity Provider (IdP)
- OAuth2 / OIDC flows
- JWT issuance service
- Refresh token store
- Session revocation registry
- MFA provider

## 1.2 Multi-Tenant Isolation
- Tenant-scoped user pools
- Tenant-specific roles
- Cross-tenant access forbidden

Directory Structure:
/identity-service/
  |- auth/
  |- roles/
  |- tokens/
  |- mfa/
  |- risk/
  |- audit/
  |- env/

---

# PHASE 2 – AUTHORIZATION FRAMEWORK

## 2.1 Role-Based Access Control (RBAC)
- Role definition versioned
- Permission matrix stored in contracts
- Role change audit logged

## 2.2 Attribute-Based Access Control (ABAC)
- Context-based rules
- Time-bound permissions
- Tenant-specific overrides

## 2.3 Token Governance
- Short-lived access tokens
- Rotating refresh tokens
- Revocation list enforced

---

# PHASE 3 – ML RISK & ANOMALY DETECTION

## 3.1 Allowed ML Uses
- Login anomaly detection
- Impossible travel detection
- Brute force pattern detection
- Account takeover risk scoring

## 3.2 Forbidden ML Uses
- Silent account lock without notification
- Hidden role downgrade
- Opaque privilege escalation

## 3.3 Logging Requirements
Each ML risk evaluation logs:
- model_version
- risk_score
- trigger_features
- fallback_decision

---

# PHASE 4 – COMPLIANCE & AUDIT

## 4.1 Compliance Standards
- GDPR compliance
- Data minimization
- Consent tracking
- Access log retention

## 4.2 Audit Trail
Every auth event logs:
- user_id
- tenant_id
- role
- ip_hash
- device_fingerprint_hash
- timestamp

---

# VERSION CONTROL – GITLAB (SELF HOSTED + AWS)

## Repository Structure

identity-auth/
  |- auth/
  |- roles/
  |- risk/
  |- audit/
  |- contracts/
  |- ci/
  |- env/

## Branch Strategy
- main (production)
- staging
- test
- dev
- feature/*

Rules:
1. No direct commit to main.
2. Merge requires:
   - Security review
   - Role matrix validation
   - Token lifecycle tests
   - ML anomaly simulation
   - Secret scanning

## CI/CD Pipeline
Must include:
- Static security analysis
- Dependency vulnerability scan
- Token expiry regression test
- RBAC integrity validation
- ML anomaly threshold test
- Secret detection scan
- Docker image hardening check

Deployment:
- Dev: auto deploy
- Test: gated deploy
- Staging: security approval required
- Production: signed artifact only

---

# SECRET MANAGEMENT

Rules:
- Use AWS Secrets Manager / Vault
- No plaintext secrets in repo
- Automatic key rotation
- mTLS certificates rotated per policy

---

# INCIDENT RESPONSE

If:
- Credential leak
- Token forgery
- Role escalation exploit

Then:
- Revoke active sessions
- Rotate keys
- Trigger audit investigation
- Notify affected tenants

---

# PROVABILITY LAW

Every access decision must be reconstructable via:
- role_version
- policy_version
- model_version (if ML involved)
- timestamp

---

# CHANGE CONTROL

All changes must:
- Increment auth_version
- Pass staging security review
- Include rollback plan
- Update audit schema

Add-only governance.
No silent permission change.

---

# FINAL STATUS

The Identity & Auth Service Engineer Agent governs:
- Secure authentication
- Role & permission integrity
- ML-based anomaly detection
- Multi-tenant access isolation
- GitLab-controlled security evolution

E