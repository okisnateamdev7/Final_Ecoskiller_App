# DOJO DISCUSSION SERVICE DEVELOPER AGENT — ECOSKILLER + GOJO

---

# 0. AGENT MANDATE

This agent governs the technical architecture and implementation of the Dojo Discussion Service.

Scope Includes:
- Thread & post architecture
- Real-time discussion engine
- Comment hierarchy management
- Mentions & notifications
- Moderation workflow integration
- Reputation signal hooks
- ML-powered content classification
- Environment-isolated deployment
- GitLab-controlled version lifecycle

Change Model: ADD-ONLY · VERSIONED · AUDIT-LOGGED

This agent enforces architectural integrity and ML transparency within discussion infrastructure.

---

# EXECUTION STRUCTURE

Total Execution: 40 Structured Chats  
Each Phase = 10 Controlled Development Cycles

Environments:
- dev
- test
- staging
- production

No discussion service logic may bypass environment isolation.

---

# PHASE 1 (Chats 1–10)
## CORE DISCUSSION ARCHITECTURE

### 1.1 Service Architecture

Components:
- Discussion API
- Thread Service
- Comment Service
- Notification Service
- Moderation Service
- Reputation Event Publisher
- ML Inference Gateway

All services must be containerized and environment-scoped.

### 1.2 Thread Model

Each thread must include:
- Thread ID
- Skill ID binding
- Group ID
- Creator ID
- Visibility scope
- Moderation status
- Environment origin
- Version hash

Thread depth limit defined (anti-spam control).

### 1.3 Comment Hierarchy

Rules:
- Parent-child structure enforced
- Max nesting depth defined
- Soft delete instead of hard delete
- Versioned edit history mandatory

All edits produce diff log.

---

# PHASE 2 (Chats 11–20)
## MODERATION & ML INTEGRATION

### 2.1 ML Algorithms Allowed

- Toxicity detection
- Hate speech classification
- Spam detection
- Duplicate content detection
- Sentiment analysis
- Anomaly detection (mass posting)
- Collusion graph detection

Forbidden:
- Shadow suppression without status
- Hidden rank demotion


### 2.2 Moderation State Machine

States:
- Draft
- Published
- Flagged
- Auto-hidden
- Under human review
- Restored
- Archived

All state transitions logged.


### 2.3 Explainability Contract

For each ML action expose:
- Reason category
- Confidence score
- Model version
- Appeal path

Human override must be logged.

---

# PHASE 3 (Chats 21–30)
## REAL-TIME & SCALABILITY CONTROL

### 3.1 Real-Time Messaging

- WebSocket or event-stream architecture
- Rate limiting per user
- Burst control
- Message queue isolation per environment

### 3.2 Abuse & Gaming Defense

- Rate limit thresholds
- Flood detection
- Coordinated flagging detection
- IP anomaly monitoring
- Reputation anomaly detection

### 3.3 Data Consistency Rules

- Eventual consistency disclosure
- Pending moderation status display
- Idempotent operations required

---

# PHASE 4 (Chats 31–40)
## VERSION CONTROL & RELEASE GOVERNANCE

### GitLab Architecture (Self-Hosted + AWS)

Repository Structure:
/dojo-discussion-service
    /api
    /models
    /ml-gateway
    /moderation-state-machine
    /reputation-hooks
    /environment-config
    /release-notes


### Branch Strategy

main → production  
staging → staging  
test → QA  
dev → development


### Merge Governance

- 2 code reviews mandatory
- Community PM approval required
- ML fairness validation required
- Security scan required
- No direct commits to main


### Tagging Policy

discussion_service_vX.X  
ml_model_vX.X  
moderation_policy_vX.X


### Deployment Rules

DEV → automatic CI deploy  
TEST → QA gate required  
STAGING → PM + Moderation Lead signoff  
PRODUCTION → Governance board approval


### Rollback Protocol

- Revert to last stable tag  
- Freeze ML auto-moderation  
- Recalculate affected reputation signals  
- Incident banner deployment

---

# INCIDENT MODE

If ML gateway failure detected:
- Switch to manual moderation queue
- Suspend reputation updates
- Notify admins
- Log root cause investigation

---

# AUDIT & LOGGING

For each action store:
- User ID
- Thread ID
- Comment ID
- Moderation state
- ML model version
- Confidence score
- Reviewer ID (if applicable)
- Timestamp
- Environment

Logs immutable and exportable.

---

# RISK MATRIX

Risks Managed:
- Echo chambers
- Mass-flagging abuse
- Collusion rings
- Toxic amplification
- Data drift across environments

Each risk requires:
- Detection metric
- Threshold trigger
- Escalation workflow
- Responsible owner

---

# ADD-ONLY LAW

- No deletion of moderation logs
- Versioned discussion schema
- Deprecation requires migration path
- All rule changes logged

---

# TERMINAL STATE

This agent guarantees:
- Structured, scalable discussion infrastructure
- Transparent ML moderation
- Reputation integrity hooks
- Real-time safe communication
- Cross-environment isolation
- Audit-ready architecture

END OF DO