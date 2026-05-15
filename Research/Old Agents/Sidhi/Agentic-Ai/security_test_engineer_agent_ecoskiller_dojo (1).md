# Security Test Engineer Agent — Ecoskiller + Dojo Platform

---

## 0. Constitutional Security Governance

This document defines the complete Security Testing Architecture for the Ecoskiller + Dojo platform.

Scope Includes:
- Candidate Platform
- Institute Portal
- Corporate Recruitment Engine
- SME / Trainer Panel
- Parent / Observer View
- Dojo Gamification Engine
- All ML / AI Algorithm Layers
- APIs, Databases, Infrastructure, CI/CD

Mandatory Environment Isolation:
- dev
- test
- staging
- production

Zero cross-environment credential, token, or dataset leakage permitted.

---

# PHASE 1 — Security Architecture Foundation (10-Chat Execution Model)

---

## Chat 1 — Threat Modeling & Attack Surface Mapping

Deliverables:
- STRIDE-based Threat Model
- Role-based Attack Surface Matrix
- API Exposure Inventory
- ML Model Exposure Mapping
- Data Flow & Trust Boundary Diagram

Must Identify:
- Student PII storage paths
- Resume document upload flow
- Placement application workflow
- Certificate verification endpoints
- ML scoring and recommendation APIs

---

## Chat 2 — Environment-Specific Security Rules

### DEV
- Static Application Security Testing (SAST)
- Dependency vulnerability scan
- Mock secrets only
- No real PII

### TEST
- Dynamic Application Security Testing (DAST)
- API penetration simulation
- Masked dataset usage
- Auth bypass attempt testing

### STAGING
- Full penetration testing
- Infrastructure vulnerability scanning
- ML adversarial simulation
- Role escalation testing

### PRODUCTION
- Continuous monitoring only
- Runtime Application Self-Protection (RASP)
- Synthetic attack probes only
- No destructive penetration testing

---

## Chat 3 — Authentication & Authorization Testing

Validate:
- RBAC enforcement per role
- JWT integrity validation
- Token expiration & refresh logic
- Session fixation prevention
- Multi-factor authentication flows

Must Test:
- Cross-role data access attempts
- Horizontal privilege escalation
- Vertical privilege escalation

---

## Chat 4 — API Security & OWASP Coverage

Mandatory Automation:
- OWASP Top 10 validation
- SQL Injection tests
- XSS tests
- CSRF tests
- IDOR tests
- Rate limiting validation

API Hardening:
- Input schema validation
- Output data masking
- Error message sanitization

---

## Chat 5 — ML Algorithm Security Layer

Algorithms Covered:
- Recommendation Engine
- Skill Gap Analyzer
- Placement Probability Predictor
- Resume Scoring Model
- Adaptive Learning Path Generator
- Engagement Scoring Engine
- Fraud Detection Model

Security Tests Required:
- Adversarial input testing
- Data poisoning simulation
- Model inversion attack testing
- Membership inference attack validation
- Prompt injection testing (if LLM used)
- ML API rate abuse simulation

Model Governance:
- Secure model storage
- Signed model artifacts
- Encrypted model serving

---

## Chat 6 — Data Protection & Privacy Testing

Validate:
- PII encryption at rest
- TLS enforcement in transit
- Resume file scanning for malware
- Data masking enforcement
- Backup encryption

Compliance Alignment:
- GDPR principles
- Data retention enforcement
- Right-to-delete validation

---

## Chat 7 — Infrastructure & Cloud Security

Validate:
- Container vulnerability scan
- Kubernetes RBAC review
- Network segmentation
- Firewall rules validation
- IAM least privilege policy

Check:
- Secrets rotation policy
- Storage bucket exposure
- Public endpoint exposure

---

## Chat 8 — CI/CD & Supply Chain Security

Pipeline Validation:
- Dependency scanning
- Secret detection scan
- Artifact integrity verification
- Image signing enforcement

Block Deployment If:
- Critical vulnerability detected
- Secret leakage detected
- Unverified artifact detected

---

## Chat 9 — Security Monitoring & Incident Readiness

Monitoring Requirements:
- Failed login anomaly detection
- ML API abuse detection
- Data exfiltration alerts
- Privilege escalation alerts

Response Framework:
- Incident severity matrix
- Automated rollback trigger
- Audit log immutability

---

## Chat 10 — Production Security Governance

Rules:
- Continuous vulnerability scanning
- Real-time intrusion detection
- ML adversarial behavior alerts
- Quarterly penetration test mandatory
- Zero-trust architecture enforcement

Deployment Blockers:
- Open critical vulnerabilities
- ML model security validation failure
- RBAC enforcement failure
- Encryption misconfiguration

---

# Advanced ML Security Governance

## Model Drift vs Security
- Drift-induced anomaly detection validation
- Secure retraining pipeline validation

## Multi-Tenant Isolation
- Tenant data isolation testing
- Cross-tenant attack simulation

## Placement Drive Attack Simulation
- Bot traffic detection
- API abuse mitigation
- Credential stuffing simulation

---

# Reporting & Audit Requirements

Every release must generate:
- Vulnerability Assessment Report
- ML Security Validation Report
- Compliance Checklist Report
- Infrastructure Security Scan Report

All reports archived for audit traceability.

---

# Hard Security Gate

Release MUST be blocked if:
- Critical vulnerability unresolved
- ML model exposed without authentication
- Data encryption misconfigured
- Privilege escalation vulnerability found

---

END OF DOCUMENT

