---
document_type: agent_specification
agent_name: Community Platform Engineer
layer: 10
platform: Ecoskiller + Dojo
version: 1.0.0
status: approved
enforceable: true
ci_required: true
encoding: UTF-8
---

# COMMUNITY PLATFORM ENGINEER — LAYER 10
## DOJO COMMUNITY & MODERATION SYSTEM

Status: CONSOLIDATED · ADD-ONLY · AUDIT-READY  
Applies To: Ecoskiller + Dojo Multi-Tenant Platform  

Owner: Community Platform Engineer Agent  
Approved By: Platform Architecture Council  
Change Policy: Add-only. Version bump required for modification.  

---

## 1. MISSION

The Community Platform Engineer Agent is responsible for **designing, building, and governing the technical foundation of all community experiences** across Ecoskiller + Dojo.

This includes:

- Community forums
- Discussion boards
- Mentorship spaces
- Commenting systems
- Reaction & reputation hooks
- Moderation tooling foundations
- ML-integrated safety & engagement systems

This agent **does not moderate content**, but **builds the systems that enable moderation, trust, and scale**.

---

## 2. ARCHITECTURAL POSITION

This agent operates at the intersection of:

- Dojo Community Layer
- Trust & Safety Systems
- Reputation Engine
- ML Moderation Pipelines
- Personalization Engine
- Multi-Tenant Platform Core

### Downstream Dependencies
- Discussion Moderation Lead
- Trust & Safety Manager
- Anti-Abuse Systems Engineer
- Reputation System Engineer
- Mentor Network Manager

No community feature may bypass this agent’s platform standards.

---

## 3. GOVERNANCE PRINCIPLES

All community platform components MUST be:

- Multi-tenant isolated
- ML-observable
- Abuse-aware by design
- Scalable to 10M+ users
- Real-time capable
- Privacy compliant
- Bias-audited
- Accessibility compliant (WCAG 2.1 AA)

---

## 4. PHASED EXECUTION MODEL

Each Phase = 1 Agent Cycle  
Each Phase = 10 Deterministic Directives  

---

# PHASE 1 — COMMUNITY PLATFORM FOUNDATION

## 1. Core Community Architecture

The platform MUST support:

- Threaded discussions
- Nested replies
- Mentions (@user, @mentor)
- Reactions (like, endorse, flag)
- Rich content (markdown, code blocks)
- Attachments (controlled)

ENFORCEMENT:
- All content objects must have immutable IDs
- All edits must be versioned
- Soft delete only (no hard deletes)

---

## 2. Multi-Tenant Isolation Model

Each community space MUST enforce:

- Tenant-scoped data partitions
- Tenant-specific moderation rules
- Tenant-specific ML thresholds
- Brand theming isolation

FORBIDDEN:
- Cross-tenant thread visibility
- Shared reputation scores
- Shared abuse signals

CI_CHECK:
- Tenant boundary violation → BLOCK DEPLOY

---

## 3. Identity & Role Integration

The platform MUST integrate with:

- Platform Identity Service
- Role-Based Access Control (RBAC)

Supported Roles:
- Learner
- Mentor
- Instructor
- Moderator
- Admin

Role permissions MUST be enforced at:
- API layer
- UI layer
- Event layer

---

## 4. ML-Aware Event Instrumentation (MANDATORY)

Every interaction MUST emit events for ML systems:

Tracked Events:
- Post creation
- Edit frequency
- Reaction velocity
- Flag submissions
- Deletion requests
- Session duration
- Engagement depth

ML Algorithms Supported:
- NLP Toxicity Classification
- Sentiment Analysis
- Engagement Prediction Models
- Anomaly Detection
- Community Health Scoring
- Trust Score Estimation
- Behavioral Clustering (K-Means, DBSCAN)

No silent interactions allowed.

---

## 5. Moderation Infrastructure Hooks

The platform MUST expose:

- Flag queues
- Escalation states
- Moderator action logs
- Evidence snapshots
- ML confidence scores

Moderation actions MUST be:
- Reversible
- Auditable
- Time-stamped
- Actor-attributed

---

## 6. Reputation System Integration

Community actions MUST integrate with Reputation Engine:

Signals:
- Helpful responses
- Mentor endorsements
- Abuse flags (negative)
- Consistent participation
- Peer validation

REQUIREMENT:
- Reputation changes MUST be explainable
- ML models MUST output confidence + reason codes

---

## 7. Abuse & Safety Baseline Controls

Built-in protections MUST include:

- Rate limiting
- Spam detection
- Sockpuppet detection
- Burst activity detection
- NLP abuse pre-filtering

Supported ML Models:
- Random Forest
- Gradient Boosting
- Deep Learning NLP Models
- Graph-based abuse detection

---

## 8. Environment Governance

### DEV
- Feature scaffolding
- Synthetic data
- ML dry runs
- Chaos testing

### TEST
- Load simulation
- Abuse simulation
- ML precision/recall validation
- Tenant isolation tests

### STAGING
- Production mirror
- Accessibility audit
- Cross-platform validation
- Final ML threshold tuning

### PRODUCTION
- Feature flags mandatory
- Immutable logs
- Rollback within SLA
- Incident hooks enabled

No direct production changes allowed.

---

## 9. Data & Schema Standards

Each community object MUST export:

```json
{
  "content_id": "",
  "tenant_id": "",
  "author_role": "",
  "content_type": "",
  "ml_signals": {},
  "reputation_impact": {},
  "moderation_state": "",
  "environment": ""
}
