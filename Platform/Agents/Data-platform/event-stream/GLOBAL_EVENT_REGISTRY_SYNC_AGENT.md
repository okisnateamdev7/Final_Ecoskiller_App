# GLOBAL_EVENT_REGISTRY_SYNC_AGENT
## ENTERPRISE OPTIMIZATION + TRUST INFRASTRUCTURE
### ECOSKILLER — ANTIGRAVITY MODULE
**Status:** `SEALED · LOCKED · GOVERNED · DETERMINISTIC`  
**Version:** `v1.0`  
**Mutation Policy:** `ADD-ONLY via version bump`  
**Interpretation Authority:** `NONE`  
**Execution Authority:** `Human declaration only`  
**Default Behavior:** `DENY`  
**Failure Mode:** `STOP → REPORT → NO PARTIAL OUTPUT`  
**Companion Agents:** `PHONE_API_CONTRACT_REGISTRY_AGENT v1.0 · PHONE_EVENT_SCHEMA_VALIDATION_AGENT v1.0`

---

## SECTION 0 — ANTIGRAVITY DECLARATION

ANTIGRAVITY is the architectural law inside Ecoskiller that eliminates invisible systemic gravity. For a platform of ~75–80 services spanning Job Portal, Skill Development, Dojo, Society Franchise, Innovation Engine, Intelligence Engine, Royalty & Licensing, and AI/Data Infrastructure — the single heaviest source of silent systemic gravity is **event schema divergence**: the silent desynchronisation of what producers declare, what consumers expect, what registries contain, and what each environment believes is the truth.

The GLOBAL_EVENT_REGISTRY_SYNC_AGENT is the **master synchronisation organ** of ANTIGRAVITY. It does not produce business events. It does not validate individual message payloads. It does not govern a single domain. It **owns, versions, synchronises, and enforces the global registry of all event schemas across every domain, every service, every namespace, and every environment** in the Ecoskiller platform.

This agent is the **single source of truth** from which all domain-specific schema validators (including PHONE_EVENT_SCHEMA_VALIDATION_AGENT) derive their active schemas. No schema exists in production unless it has been registered, versioned, approved, and synchronised by this agent.

This agent exists because:

- Ecoskiller's Contract-First Execution Law (SECTION F, Master Execution Prompt) declares the **Event Schema Registry** as a mandatory pre-code contract. This agent is that contract made operational.
- The Contract Gate `event_schema_ready` (Lane A, SECTION E) is a hard dependency for `db_ready`, `search_ready`, and all downstream lanes. This agent produces that gate signal.
- With ~75–80 services across 7+ Kubernetes namespaces, 4 environments, and 15+ domain verticals, schema drift between dev → staging → production is not a risk — it is a certainty without a synchronisation authority.
- Apicurio Registry (Apache 2.0, self-hosted) is already confirmed in the Ecoskiller infrastructure stack (v8 Audit Report: `✓ KEEP`). This agent is the governance layer that operates above it.
- The absence of a global sync authority means every domain schema agent operates in isolation, creating divergence between the registry, Kafka topics, and deployed consumer code — invisible until production failure.

**Absence of this agent → `event_schema_ready` gate NEVER emits → ALL downstream lanes BLOCKED → STOP EXECUTION.**

---

## SECTION 1 — AGENT IDENTITY

```
AGENT_NAME                       : GLOBAL_EVENT_REGISTRY_SYNC_AGENT
AGENT_CLASS                      : Trust Infrastructure · Global Schema Governance · Environment Sync
DOMAIN_MODULE                    : ENTERPRISE_OPTIMIZATION
SUB_MODULE                       : TRUST_INFRASTRUCTURE
ANTIGRAVITY_LAYER                : GLOBAL_EVENT_SURFACE
COMPANION_AGENTS                 : PHONE_API_CONTRACT_REGISTRY_AGENT v1.0
                                   PHONE_EVENT_SCHEMA_VALIDATION_AGENT v1.0
SERVICE_NAMESPACE                : trust-infra
KUBERNETES_NAMESPACE             : ecoskiller-trust
DEPLOY_TARGET                    : k3s / GCP Kubernetes (ALL environments)
LANGUAGE_RUNTIME                 : Spring Boot (Java 21)
SCHEMA_FORMAT_CANONICAL          : AsyncAPI 2.6
SCHEMA_FORMAT_RUNTIME_VALIDATION : JSON Schema Draft 2020-12
SCHEMA_REGISTRY_ENGINE           : Apicurio Registry (Apache 2.0) — self-hosted
STORAGE_PRIMARY                  : PostgreSQL (global_event_registry schema)
STORAGE_STATE                    : Redis (version index · environment sync state · compatibility cache)
STORAGE_ARCHIVE                  : MinIO (schema artifact bundles · environment snapshots)
EVENT_BUS                        : Apache Kafka 3.7.0
SYNC_TARGETS                     : DEV · TEST · STAGING · PRODUCTION (all 4 environments)
AUDIT_STORE                      : ClickHouse (global_schema_sync_audit table)
OBSERVABILITY                    : Prometheus · Loki · Grafana · OpenTelemetry
SECURITY_LAYER                   : Keycloak (JWT) · OPA (policy-as-code) · Vault · ModSecurity (WAF)
GATE_PRODUCED                    : event_schema_ready
```

---

## SECTION 2 — AGENT SCOPE AND AUTHORITY

### 2.1 Full Domain Coverage (ALL 15 Platform Domains)

This agent governs event schemas across every domain in the Ecoskiller platform. No domain is exempt. No event escapes the registry.

| Domain ID | Domain Name | Namespace | Est. Event Types |
|---|---|---|---|
| `DOM-01` | Identity & Auth | `core` | 12 |
| `DOM-02` | User & Profile | `core` | 14 |
| `DOM-03` | Job & Recruitment | `core` | 18 |
| `DOM-04` | Application Pipeline | `core` | 10 |
| `DOM-05` | Interview & Scheduling | `realtime` | 12 |
| `DOM-06` | Voice GD Orchestrator | `realtime` | 16 |
| `DOM-07` | Dojo Match Engine | `realtime` | 20 |
| `DOM-08` | Billing & Subscription | `billing` | 16 |
| `DOM-09` | Notification & Delivery | `core` | 10 |
| `DOM-10` | Intelligence Engine | `analytics` | 14 |
| `DOM-11` | Innovation & Royalty | `analytics` | 22 |
| `DOM-12` | Society & Franchise | `society` | 18 |
| `DOM-13` | Admin & Governance | `admin` | 12 |
| `DOM-14` | Analytics & Reporting | `analytics` | 8 |
| `DOM-15` | Phone & Trust (PACA)  | `trust-infra` | 25 |

**Total Governed Event Types: ~227 across all domains**  
**Total Kafka Topics Under Registry Governance: ~60+**

### 2.2 What This Agent Does (LOCKED)

```
DOES     : Maintain the master global event schema registry (single source of truth)
DOES     : Register, version, approve, deprecate, and revoke event schemas
DOES     : Synchronise schema state across all 4 environments (DEV/TEST/STAGING/PROD)
DOES     : Push schema updates to domain-specific validation agents (including PESCA)
DOES     : Validate cross-domain event compatibility (producer ↔ consumer contracts)
DOES     : Detect and alert on schema drift between environments
DOES     : Produce the event_schema_ready gate signal for the Contract Gate System
DOES     : Maintain a complete event catalog with producer/consumer wiring map
DOES     : Enforce the rule: no Kafka topic may be created without a registered schema
DOES     : Manage Apicurio Registry as the runtime enforcement backend
DOES     : Archive schema bundles to MinIO for disaster recovery
DOES     : Run compatibility checks in CI/CD pipelines before any deployment
DOES     : Maintain backward/forward compatibility matrices per event type
DOES     : Emit schema lifecycle events to Kafka for audit and governance consumers
```

### 2.3 What This Agent Does NOT Do (HARD BOUNDARY)

```
DOES NOT : Validate individual Kafka message payloads at runtime
           (that is the jurisdiction of domain schema validation agents)
DOES NOT : Process business events or contain domain logic
DOES NOT : Directly produce or consume domain Kafka events
DOES NOT : Manage Kafka broker configuration (topic creation, replication, ACLs)
           (it declares intent — the Kafka Admin service executes)
DOES NOT : Store or buffer event message data
DOES NOT : Make retry or replay decisions on failed events
DOES NOT : Override human approval requirements for schema activation
DOES NOT : Auto-resolve schema conflicts between domains
```

---

## SECTION 3 — GLOBAL EVENT CATALOG (ALL DOMAINS)

### 3.1 Complete Event Type Master List

#### DOM-01 — Identity & Auth

| Event Type | Producer | Tenant Scoped | PII Fields |
|---|---|---|---|
| `auth.user.registered` | Auth Service | YES | `email_hash` |
| `auth.user.login.success` | Auth Service | YES | `device_fingerprint` |
| `auth.user.login.failed` | Auth Service | YES | `ip_address` |
| `auth.user.logout` | Auth Service | YES | — |
| `auth.password.reset.requested` | Auth Service | YES | `email_hash` |
| `auth.password.reset.completed` | Auth Service | YES | — |
| `auth.mfa.enabled` | Auth Service | YES | — |
| `auth.mfa.disabled` | Auth Service | YES | — |
| `auth.mfa.challenge.failed` | Auth Service | YES | `ip_address` |
| `auth.session.revoked` | Auth Service | YES | — |
| `auth.oauth.linked` | Auth Service | YES | — |
| `auth.account.locked` | Auth Service | YES | — |

#### DOM-02 — User & Profile

| Event Type | Producer | Tenant Scoped | PII Fields |
|---|---|---|---|
| `user.created` | User Service | YES | `email_hash`, `phone_hash` |
| `user.profile.updated` | User Service | YES | — |
| `user.skill.added` | User Service | YES | — |
| `user.skill.removed` | User Service | YES | — |
| `user.skill.verified` | User Service | YES | — |
| `user.resume.uploaded` | User Service | YES | — |
| `user.avatar.updated` | User Service | YES | — |
| `user.account.deactivated` | User Service | YES | — |
| `user.account.reactivated` | User Service | YES | — |
| `user.role.assigned` | User Service | YES | — |
| `user.role.revoked` | User Service | YES | — |
| `user.tenant.bound` | User Service | YES | — |
| `user.onboarding.completed` | User Service | YES | — |
| `user.deletion.requested` | User Service | YES | `email_hash`, `phone_hash` |

#### DOM-03 — Job & Recruitment

| Event Type | Producer | Tenant Scoped |
|---|---|---|
| `job.created` | Job Service | YES |
| `job.updated` | Job Service | YES |
| `job.published` | Job Service | YES |
| `job.unpublished` | Job Service | YES |
| `job.expired` | Job Service | YES |
| `job.closed` | Job Service | YES |
| `job.deleted` | Job Service | YES |
| `job.moderation.flagged` | Admin Governance Service | YES |
| `job.moderation.cleared` | Admin Governance Service | YES |
| `recruiter.created` | Recruiter Service | YES |
| `recruiter.approved` | Admin Governance Service | YES |
| `recruiter.suspended` | Admin Governance Service | YES |
| `recruiter.job.limit.reached` | Recruiter Service | YES |
| `company.profile.created` | Recruiter Service | YES |
| `company.profile.updated` | Recruiter Service | YES |
| `company.domain.verified` | Auth Service | YES |
| `job.search.index.updated` | Job Service | YES |
| `job.recommendation.triggered` | Intelligence Prediction Engine | YES |

#### DOM-04 — Application Pipeline

| Event Type | Producer |
|---|---|
| `application.submitted` | Application Service |
| `application.under_review` | Application Service |
| `application.shortlisted` | Application Service |
| `application.rejected` | Application Service |
| `application.hired` | Application Service |
| `application.withdrawn` | Application Service |
| `application.stage.changed` | Application Service |
| `application.offer.issued` | Application Service |
| `application.offer.accepted` | Application Service |
| `application.offer.declined` | Application Service |

#### DOM-05 — Interview & Scheduling

| Event Type | Producer |
|---|---|
| `interview.scheduled` | Interview Service |
| `interview.slot.locked` | Interview Service |
| `interview.slot.released` | Interview Service |
| `interview.reminder.sent` | Notification Service |
| `interview.started` | Interview Service |
| `interview.completed` | Interview Service |
| `interview.no_show.candidate` | Interview Service |
| `interview.no_show.recruiter` | Interview Service |
| `interview.cancelled` | Interview Service |
| `interview.token.generated` | Interview Service |
| `interview.token.expired` | Interview Service |
| `interview.feedback.submitted` | Interview Service |

#### DOM-06 — Voice GD Orchestrator

| Event Type | Producer |
|---|---|
| `gd.batch.created` | Voice GD Orchestrator |
| `gd.room.created` | Voice GD Orchestrator |
| `gd.participant.joined` | Voice GD Orchestrator |
| `gd.participant.late_blocked` | Voice GD Orchestrator |
| `gd.speaking.token.granted` | Voice GD Orchestrator |
| `gd.speaking.token.expired` | Voice GD Orchestrator |
| `gd.turn.skipped` | Voice GD Orchestrator |
| `gd.interrupt.attempted` | Voice GD Orchestrator |
| `gd.silence.detected` | Voice GD Orchestrator |
| `gd.participant.dropped` | Voice GD Orchestrator |
| `gd.participant.exited_early` | Voice GD Orchestrator |
| `gd.open_round.started` | Voice GD Orchestrator |
| `gd.session.completed` | Voice GD Orchestrator |
| `gd.score.computed` | Scoring Engine |
| `gd.score.published` | Scoring Engine |
| `gd.completed` | Voice GD Orchestrator |

#### DOM-07 — Dojo Match Engine

| Event Type | Producer |
|---|---|
| `dojo.match.created` | Dojo Match Engine |
| `dojo.match.scenario.assigned` | Dojo Match Engine |
| `dojo.match.roles.bound` | Dojo Match Engine |
| `dojo.match.started` | Dojo Match Engine |
| `dojo.match.timer.tick` | Dojo Match Engine |
| `dojo.match.state.changed` | Dojo Match Engine |
| `dojo.match.completed` | Dojo Match Engine |
| `dojo.match.abandoned` | Dojo Match Engine |
| `dojo.score.peer.submitted` | Scoring Engine |
| `dojo.score.mentor.submitted` | Scoring Engine |
| `dojo.score.merged` | Scoring Engine |
| `dojo.score.variance.flagged` | Scoring Engine |
| `dojo.score.published` | Scoring Engine |
| `dojo.match.scored` | Scoring Engine |
| `belt.eligibility.checked` | Certification & Belt Engine |
| `belt.eligible` | Certification & Belt Engine |
| `belt.promoted` | Certification & Belt Engine |
| `belt.certificate.generated` | Certification & Belt Engine |
| `dojo.replay.created` | Dojo Match Engine |
| `dojo.tournament.completed` | Dojo Match Engine |

#### DOM-08 — Billing & Subscription

| Event Type | Producer |
|---|---|
| `billing.subscription.created` | Billing Service |
| `billing.subscription.upgraded` | Billing Service |
| `billing.subscription.downgraded` | Billing Service |
| `billing.subscription.cancelled` | Billing Service |
| `billing.subscription.expired` | Billing Service |
| `billing.invoice.generated` | Billing Service |
| `billing.invoice.paid` | Billing Service |
| `billing.invoice.failed` | Billing Service |
| `billing.invoice.overdue` | Billing Service |
| `billing.refund.issued` | Billing Service |
| `billing.feature.gated` | Billing Service |
| `billing.feature.unlocked` | Billing Service |
| `billing.usage.metered` | Billing Service |
| `billing.gst.computed` | Billing Service |
| `billing.payment.initiated` | Billing Service |
| `invoice.generated` | Billing Service |

#### DOM-09 — Notification & Delivery

| Event Type | Producer |
|---|---|
| `notification.created` | Notification Service |
| `notification.email.sent` | Notification Service |
| `notification.email.failed` | Notification Service |
| `notification.sms.sent` | Notification Service |
| `notification.sms.failed` | Notification Service |
| `notification.push.sent` | Notification Service |
| `notification.push.failed` | Notification Service |
| `notification.template.rendered` | Notification Service |
| `notification.preference.updated` | Notification Service |
| `notification.optout.recorded` | Notification Service |

#### DOM-10 — Intelligence Engine

| Event Type | Producer |
|---|---|
| `intelligence.profile.created` | Intelligence Profile Service |
| `intelligence.score.updated` | Passive Intelligence Engine |
| `intelligence.test.started` | Dojo Intelligence Testing Service |
| `intelligence.test.completed` | Dojo Intelligence Testing Service |
| `intelligence.test.scored` | Dojo Intelligence Testing Service |
| `intelligence.prediction.generated` | Intelligence Prediction Engine |
| `intelligence.career.trajectory.updated` | Intelligence Prediction Engine |
| `intelligence.dna.recalculated` | Intelligence Profile Service |
| `intelligence.signal.captured` | Passive Intelligence Engine |
| `intelligence.evolution.milestone` | Intelligence Evolution Timeline Service |
| `intelligence.anomaly.detected` | Intelligence Profile Service |
| `intelligence.ranking.updated` | Intelligence Prediction Engine |
| `intelligence.benchmark.exceeded` | Intelligence Prediction Engine |
| `intelligence.growth.stagnation.flagged` | Passive Intelligence Engine |

#### DOM-11 — Innovation & Royalty

| Event Type | Producer |
|---|---|
| `idea.submitted` | Idea Registry Service |
| `idea.versioned` | Idea Registry Service |
| `idea.visibility.changed` | Idea Registry Service |
| `idea.dna.fingerprinted` | Idea DNA Fingerprint Engine |
| `idea.similarity.checked` | Idea Similarity & Anti-Theft Engine |
| `idea.similarity.flagged` | Idea Similarity & Anti-Theft Engine |
| `idea.innovation.scored` | Innovation Scoring Engine |
| `idea.marketplace.listed` | Idea Marketplace Service |
| `idea.marketplace.bid` | Idea Marketplace Service |
| `idea.license.created` | Licensing Contract Service |
| `idea.license.activated` | Licensing Contract Service |
| `idea.license.terminated` | Licensing Contract Service |
| `royalty.calculated` | Royalty Accounting Engine |
| `royalty.wallet.credited` | Royalty Wallet Service |
| `royalty.payout.released` | Royalty Wallet Service |
| `royalty.revenue.ingested` | Revenue Ingestion Gateway |
| `royalty.audit.flagged` | Royalty Audit & Compliance Service |
| `royalty.anomaly.detected` | Fraud Detection Engine |
| `innovation.reputation.updated` | Innovation Reputation Service |
| `parent.consent.captured` | Innovation Trust Governance Service |
| `ownership.transfer.initiated` | Innovation Trust Governance Service |
| `ownership.transfer.completed` | Innovation Trust Governance Service |

#### DOM-12 — Society & Franchise

| Event Type | Producer |
|---|---|
| `society.created` | Society Service |
| `franchise.agreement.signed` | Franchise Service |
| `franchise.territory.locked` | Franchise Service |
| `coordinator.onboarded` | Coordinator Service |
| `coach.registered` | Coach Service |
| `batch.started` | Workshop Service |
| `batch.completed` | Workshop Service |
| `enrollment.created` | Enrollment Service |
| `attendance.marked` | Attendance Service |
| `certificate.issued` | Certificate Service |
| `tournament.completed` | Tournament Service |
| `commission.calculated` | Commission Engine Service |
| `payout.processed` | Payout Service |
| `scheme.approved` | Society Service |
| `csr.milestone.completed` | Society Service |
| `society.dispute.raised` | Dispute Service |
| `society.compliance.audited` | Audit Service |
| `society.product.sold` | Product Inventory Service |

#### DOM-13 — Admin & Governance

| Event Type | Producer |
|---|---|
| `admin.moderation.flagged` | Admin Governance Service |
| `admin.moderation.resolved` | Admin Governance Service |
| `admin.dispute.opened` | Admin Governance Service |
| `admin.dispute.resolved` | Admin Governance Service |
| `admin.mentor.misconduct.reported` | Admin Governance Service |
| `admin.mentor.misconduct.resolved` | Admin Governance Service |
| `admin.user.banned` | Admin Governance Service |
| `admin.user.ban.lifted` | Admin Governance Service |
| `admin.platform.config.changed` | Admin Governance Service |
| `admin.feature.toggle.changed` | Unleash |
| `admin.audit.review.completed` | Admin Governance Service |
| `admin.compliance.check.triggered` | Admin Governance Service |

#### DOM-14 — Analytics & Reporting

| Event Type | Producer |
|---|---|
| `analytics.funnel.step.completed` | Analytics Service |
| `analytics.dashboard.generated` | Analytics Service |
| `analytics.anomaly.detected` | Analytics Service |
| `analytics.report.scheduled` | Airflow |
| `analytics.report.completed` | Airflow |
| `analytics.snapshot.created` | Analytics Service |
| `analytics.metric.threshold.breached` | Analytics Service |
| `analytics.clickhouse.ingestion.failed` | Analytics Service |

#### DOM-15 — Phone & Trust (PACA — governed by companion agents)

*(25 event types declared in PHONE_EVENT_SCHEMA_VALIDATION_AGENT v1.0 — this agent synchronises those schemas to Apicurio Registry and all environments but does not re-declare them here.)*

---

## SECTION 4 — REGISTRY DATA MODEL

### 4.1 Core Tables (PostgreSQL — global_event_registry schema)

```sql
-- GLOBAL DOMAIN REGISTRY
CREATE TABLE event_domains (
  id                UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  domain_id         TEXT NOT NULL UNIQUE,        -- e.g. DOM-01
  domain_name       TEXT NOT NULL,
  k8s_namespace     TEXT NOT NULL,
  owning_team       TEXT NOT NULL,
  is_active         BOOLEAN DEFAULT TRUE,
  created_at        TIMESTAMPTZ DEFAULT now()
);

-- GLOBAL EVENT TYPE MASTER REGISTRY
CREATE TABLE global_event_types (
  id                    UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  domain_id             TEXT NOT NULL REFERENCES event_domains(domain_id),
  event_type            TEXT NOT NULL UNIQUE,
  description           TEXT NOT NULL,
  owning_service        TEXT NOT NULL,
  kafka_topic           TEXT NOT NULL,
  is_active             BOOLEAN DEFAULT TRUE,
  is_deprecated         BOOLEAN DEFAULT FALSE,
  is_pii_bearing        BOOLEAN DEFAULT FALSE,
  pii_fields            TEXT[] DEFAULT '{}',
  requires_tenant_id    BOOLEAN DEFAULT TRUE,
  consumer_services     TEXT[] DEFAULT '{}',
  created_at            TIMESTAMPTZ DEFAULT now(),
  updated_at            TIMESTAMPTZ DEFAULT now()
);

-- SCHEMA VERSION MASTER TABLE
CREATE TABLE global_schema_versions (
  id                        UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  event_type_id             UUID NOT NULL REFERENCES global_event_types(id),
  schema_version            TEXT NOT NULL,           -- Semantic MAJOR.MINOR.PATCH
  asyncapi_fragment         JSONB NOT NULL,
  json_schema               JSONB NOT NULL,
  apicurio_artifact_id      TEXT,                    -- Apicurio Registry artifact reference
  apicurio_global_id        BIGINT,                  -- Apicurio global ID for version
  state                     TEXT NOT NULL DEFAULT 'DRAFT',
  -- STATES: DRAFT · VALIDATED · ACTIVE · DEPRECATED · REVOKED
  is_backward_compatible    BOOLEAN NOT NULL,
  breaking_change_summary   TEXT,
  approved_by               UUID,
  second_approver           UUID,                    -- Required for PII event types
  approved_at               TIMESTAMPTZ,
  activated_at              TIMESTAMPTZ,
  deprecated_at             TIMESTAMPTZ,
  revoked_at                TIMESTAMPTZ,
  revocation_reason         TEXT,
  migration_notes           TEXT,
  created_at                TIMESTAMPTZ DEFAULT now(),
  UNIQUE(event_type_id, schema_version)
);

-- ENVIRONMENT SYNC STATE TABLE
CREATE TABLE environment_sync_state (
  id                    UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  environment           TEXT NOT NULL CHECK (environment IN ('DEV','TEST','STAGING','PRODUCTION')),
  domain_id             TEXT NOT NULL REFERENCES event_domains(domain_id),
  event_type            TEXT NOT NULL,
  registered_version    TEXT NOT NULL,
  apicurio_sync_status  TEXT NOT NULL CHECK (apicurio_sync_status IN ('SYNCED','PENDING','FAILED','DRIFT_DETECTED')),
  kafka_topic_exists    BOOLEAN DEFAULT FALSE,
  last_sync_at          TIMESTAMPTZ,
  last_checked_at       TIMESTAMPTZ DEFAULT now(),
  drift_detected_at     TIMESTAMPTZ,
  drift_detail          JSONB DEFAULT '{}',
  UNIQUE(environment, event_type)
);

-- PRODUCER ↔ CONSUMER WIRING MAP
CREATE TABLE event_wiring_map (
  id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  event_type          TEXT NOT NULL,
  producer_service    TEXT NOT NULL,
  consumer_service    TEXT NOT NULL,
  consumer_namespace  TEXT NOT NULL,
  consumer_group_id   TEXT NOT NULL,
  registered_at       TIMESTAMPTZ DEFAULT now(),
  is_active           BOOLEAN DEFAULT TRUE,
  UNIQUE(event_type, consumer_service)
);

-- CROSS-DOMAIN COMPATIBILITY MATRIX
CREATE TABLE cross_domain_compatibility (
  id                      UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  source_event_type       TEXT NOT NULL,
  target_event_type       TEXT NOT NULL,
  relationship_type       TEXT NOT NULL CHECK (relationship_type IN (
                            'TRIGGERS',
                            'PRECEDES',
                            'CORRELATES',
                            'REPLACES',
                            'ENRICHES'
                          )),
  contract_locked         BOOLEAN DEFAULT FALSE,
  description             TEXT,
  registered_at           TIMESTAMPTZ DEFAULT now()
);

-- KAFKA TOPIC REGISTRY (Intent Declaration)
CREATE TABLE kafka_topic_registry (
  id                    UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  topic_name            TEXT NOT NULL UNIQUE,
  domain_id             TEXT NOT NULL REFERENCES event_domains(domain_id),
  partitions            INTEGER NOT NULL,
  replication_factor    INTEGER NOT NULL DEFAULT 3,
  retention_ms          BIGINT NOT NULL,
  cleanup_policy        TEXT NOT NULL DEFAULT 'delete',
  max_message_bytes     INTEGER NOT NULL DEFAULT 1048576,
  is_provisioned        BOOLEAN DEFAULT FALSE,
  provisioned_at        TIMESTAMPTZ,
  schema_registered     BOOLEAN DEFAULT FALSE,
  -- RULE: schema_registered must be TRUE before is_provisioned may be TRUE
  created_at            TIMESTAMPTZ DEFAULT now()
);

-- ENVIRONMENT SYNC AUDIT LOG (append-only)
CREATE TABLE global_sync_audit_log (
  id                UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  sync_type         TEXT NOT NULL CHECK (sync_type IN (
                      'SCHEMA_PUSH',
                      'SCHEMA_PULL',
                      'DRIFT_DETECTED',
                      'DRIFT_RESOLVED',
                      'TOPIC_REGISTERED',
                      'VERSION_ACTIVATED',
                      'VERSION_DEPRECATED',
                      'VERSION_REVOKED',
                      'GATE_EMITTED',
                      'WIRING_REGISTERED',
                      'COMPATIBILITY_CHECK'
                    )),
  environment       TEXT,
  domain_id         TEXT,
  event_type        TEXT,
  schema_version    TEXT,
  actor_service     TEXT,
  actor_user_id     UUID,
  result            TEXT NOT NULL CHECK (result IN ('SUCCESS','FAILURE','WARNING')),
  detail            JSONB DEFAULT '{}',
  synced_at         TIMESTAMPTZ DEFAULT now()
) PARTITION BY RANGE (synced_at);

-- GATE SIGNAL REGISTRY
CREATE TABLE gate_signals (
  id                UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  gate_name         TEXT NOT NULL,                   -- e.g. event_schema_ready
  environment       TEXT NOT NULL,
  gate_value        BOOLEAN NOT NULL,
  conditions_met    JSONB NOT NULL,
  conditions_failed JSONB DEFAULT '[]',
  emitted_at        TIMESTAMPTZ DEFAULT now(),
  emitted_by        TEXT NOT NULL DEFAULT 'GLOBAL_EVENT_REGISTRY_SYNC_AGENT'
);
```

### 4.2 Redis State Keys (Deterministic Namespace)

```
schema:global:version:{event_type}                → Active version string per event type
schema:global:domain:active:{domain_id}           → Set of active event types in domain
schema:global:env:sync_state:{env}:{event_type}   → SYNCED|PENDING|FAILED|DRIFT_DETECTED
schema:global:wiring:{event_type}                 → JSON list of consumer services
schema:global:gate:{gate_name}:{env}              → TRUE|FALSE gate state
schema:global:drift:detected                      → Set of event types with detected drift
schema:global:apicurio:health                     → TTL=30s health status of Apicurio
schema:global:topic:registered:{topic_name}       → Schema registration flag for topic
schema:global:compat:{event_type}:{v1}:{v2}       → Compatibility result cache · TTL=3600s
schema:global:sync:lock:{env}                     → Distributed lock for environment sync
```

---

## SECTION 5 — APICURIO REGISTRY INTEGRATION

### 5.1 Apicurio Registry as Runtime Backend

Apicurio Registry is the declared schema registry engine for Ecoskiller (confirmed in Infrastructure v8 Audit: `✓ KEEP`, Apache 2.0). This agent is the **exclusive management authority** for Apicurio. No other service or agent may write to Apicurio directly.

```
APICURIO_DEPLOYMENT   : Self-hosted on k3s · ecoskiller-trust namespace
APICURIO_IMAGE        : apicurio/apicurio-registry (Apache 2.0)
APICURIO_STORAGE      : PostgreSQL backend (apicurio schema)
APICURIO_API_VERSION  : v2
APICURIO_CONTENT_TYPE : ASYNCAPI (canonical) + JSON (runtime validation)
APICURIO_GROUPS       : One group per domain (dom-01 through dom-15)
APICURIO_RULES        : COMPATIBILITY rule enforced per artifact (BACKWARD by default)
                        VALIDITY rule enforced per artifact (FULL — no invalid schemas)
```

### 5.2 Apicurio Write Rules (LOCKED)

```
RULE-AP-01  : Only GLOBAL_EVENT_REGISTRY_SYNC_AGENT may write artifacts to Apicurio.
              No other service, agent, or CI pipeline may POST/PUT to Apicurio directly.
              All schema changes route through this agent's API → Apicurio.

RULE-AP-02  : Each event type maps to exactly one Apicurio artifact:
              Group ID    = domain_id (e.g., dom-06)
              Artifact ID = event_type string (e.g., gd.session.completed)
              Version     = schema_version (e.g., 1.0.0)

RULE-AP-03  : Apicurio COMPATIBILITY rule is set to BACKWARD for all artifacts.
              Any schema push that violates BACKWARD compatibility is REJECTED
              by Apicurio's own rule engine before this agent persists the version.
              BREAKING changes require explicit override flag + dual human approval.

RULE-AP-04  : Apicurio VALIDITY rule is set to FULL for all artifacts.
              Invalid AsyncAPI or JSON Schema content is rejected at upload.

RULE-AP-05  : Apicurio global IDs are stored in global_schema_versions.apicurio_global_id
              for version reference integrity.

RULE-AP-06  : Apicurio is deployed per-environment with environment-specific data.
              DEV Apicurio ≠ STAGING Apicurio ≠ PRODUCTION Apicurio.
              This agent synchronises schemas across all four instances.

RULE-AP-07  : Apicurio instances are READ-accessible to domain schema validation agents
              (e.g., PHONE_EVENT_SCHEMA_VALIDATION_AGENT) via read-only service tokens.
              They pull compiled schemas from Apicurio on startup and cache in Redis.
```

---

## SECTION 6 — ENVIRONMENT SYNCHRONISATION ENGINE

This is the core capability that distinguishes this agent from a simple schema registry. It actively detects drift, enforces parity, and synchronises schema state across all 4 environments on every change.

### 6.1 Sync Flow (Deterministic)

```
TRIGGER: Schema version activated in PRODUCTION
           ↓
[01] Mark version as ACTIVE in global_schema_versions
           ↓
[02] Acquire distributed sync lock: schema:global:sync:lock:{env} (Redis, TTL=120s)
           ↓
[03] Push schema artifact to Apicurio PRODUCTION instance
       → Await confirmation (timeout = 10s)
       → On failure: HALT sync · emit sync.failed event · alert
           ↓
[04] Update Kafka topic registry: schema_registered = TRUE
       → Emit topic provisioning intent to Kafka Admin intent topic
           ↓
[05] Update environment_sync_state: PRODUCTION → SYNCED
           ↓
[06] Write audit record to global_sync_audit_log
           ↓
[07] Push schema to DEV Apicurio instance
[08] Push schema to TEST Apicurio instance
[09] Push schema to STAGING Apicurio instance
       → Each environment follows steps 03-06 independently
       → Each environment failure is isolated — does not block others
           ↓
[10] Re-evaluate gate: event_schema_ready conditions for each environment
           ↓
[11] Emit gate signal update: schema:global:gate:event_schema_ready:{env}
           ↓
[12] Release all sync locks
           ↓
[13] Emit global_schema_sync.completed event to Kafka topic: schema.lifecycle.events
```

### 6.2 Drift Detection Engine

Drift occurs when the schema version in any environment's Apicurio instance diverges from the canonical version in this agent's PostgreSQL registry. Drift detection runs on a schedule and on-demand.

```
DRIFT DETECTION SCHEDULE   : Every 5 minutes (Kubernetes CronJob)
DRIFT DETECTION TRIGGER    : On demand via API · On any schema mutation

DRIFT DETECTION ALGORITHM:
  FOR EACH active event_type IN global_event_types:
    FOR EACH environment IN [DEV, TEST, STAGING, PRODUCTION]:
      apicurio_version = GET apicurio.{env}/apis/registry/v2/groups/{domain_id}/artifacts/{event_type}/versions/latest
      registry_version = SELECT schema_version FROM global_schema_versions
                         WHERE event_type = ? AND state = 'ACTIVE'

      IF apicurio_version ≠ registry_version:
        MARK environment_sync_state.apicurio_sync_status = 'DRIFT_DETECTED'
        SET schema:global:drift:detected.add(event_type)
        EMIT drift.detected event
        ALERT Platform Admin (severity based on environment):
          PRODUCTION drift → CRITICAL
          STAGING drift    → HIGH
          TEST/DEV drift   → MEDIUM
        
        IF drift in PRODUCTION:
          INITIATE automatic resync within 60 seconds
          IF resync fails: PAGE on-call + HALT gate_signal for affected domain
```

### 6.3 Environment Promotion Rules (LOCKED)

Schema versions must follow a promotion path. Skipping environments is FORBIDDEN.

```
PROMOTION PATH (MANDATORY):
  DRAFT → VALIDATED → DEV-ACTIVE → TEST-ACTIVE → STAGING-ACTIVE → PRODUCTION-ACTIVE

RULE-EP-01  : A schema version may not be promoted to STAGING until it has been
              ACTIVE in TEST for minimum 24 hours with zero validation failures

RULE-EP-02  : A schema version may not be promoted to PRODUCTION until it has been
              ACTIVE in STAGING for minimum 72 hours with zero validation failures

RULE-EP-03  : Production promotion requires explicit human approval
              (Platform Admin role, recorded in global_schema_versions.approved_by)

RULE-EP-04  : BREAKING CHANGE (MAJOR version bump) promotion to PRODUCTION requires:
              (a) Dual approval (Platform Admin + Domain Lead)
              (b) Minimum 7 days in STAGING
              (c) Documented migration plan in schema_version.migration_notes
              (d) All consumer services confirmed compatible (wiring_map check)

RULE-EP-05  : Emergency hotfix promotion (skipping staging) requires:
              (a) Compliance Officer override approval
              (b) Incident record linked in schema_version.migration_notes
              (c) Full staging sync must complete within 4 hours post-promotion
              (d) Automatic post-incident review scheduled (Airflow workflow)

RULE-EP-06  : Schema version in DEPRECATED state may not be promoted to new environments.
              Only ACTIVE versions may be promoted.
```

---

## SECTION 7 — CONTRACT GATE SYSTEM INTEGRATION

### 7.1 Gate: `event_schema_ready`

This is the most critical gate signal produced by this agent. It is declared in SECTION E of the Ecoskiller Master Execution Prompt as a prerequisite for `db_ready`, `search_ready`, and all subsequent lanes.

```
GATE_NAME     : event_schema_ready
PRODUCER      : GLOBAL_EVENT_REGISTRY_SYNC_AGENT
ENVIRONMENT   : Produced independently per environment
GATE_STORAGE  : Redis key: schema:global:gate:event_schema_ready:{env}
GATE_TOPIC    : contract.gates (Kafka) — consumed by CI/CD orchestrator and all lane executors
```

### 7.2 Gate Evaluation Conditions (ALL must be TRUE)

```
CONDITION-G01  : Apicurio Registry instance reachable and healthy in this environment
CONDITION-G02  : ALL domains (DOM-01 through DOM-15) have at least one ACTIVE schema version
                 registered in Apicurio for this environment
CONDITION-G03  : environment_sync_state shows zero DRIFT_DETECTED entries for PRODUCTION
                 (STAGING/TEST/DEV: zero DRIFT_DETECTED for more than 15 min)
CONDITION-G04  : kafka_topic_registry shows all declared topics have schema_registered = TRUE
CONDITION-G05  : event_wiring_map has at least one consumer registered for every event type
                 classified as requiring_consumer = TRUE
CONDITION-G06  : No schema versions in REVOKED state are currently the latest version
                 for any ACTIVE event type
CONDITION-G07  : global_sync_audit_log shows no SCHEMA_PUSH FAILURE in last 30 minutes
CONDITION-G08  : All companion agents (PHONE_API_CONTRACT_REGISTRY_AGENT,
                 PHONE_EVENT_SCHEMA_VALIDATION_AGENT) have reported their own
                 domain-level ready signals to this agent
CONDITION-G09  : gate_signals table has no open FAILURE records for any domain in
                 the last 60 minutes
CONDITION-G10  : This agent's own health endpoint returns HEALTHY status
```

```
GATE EVALUATION FORMULA:
  event_schema_ready = (G01 AND G02 AND G03 AND G04 AND G05 AND G06 AND G07 AND G08 AND G09 AND G10)

  TRUE  → Emit gate signal to Kafka topic: contract.gates
          Set Redis: schema:global:gate:event_schema_ready:{env} = TRUE
          Write gate_signals record: gate_value = TRUE

  FALSE → Block all dependent lanes
          Emit WHICH conditions failed to contract.gates topic
          Alert Platform Admin with full condition evaluation report
          Set Redis: schema:global:gate:event_schema_ready:{env} = FALSE
```

### 7.3 Gate Dependency Chain (From Master Execution Prompt)

```
Lane A produces → event_schema_ready (THIS AGENT)
                       ↓
Lane B requires event_schema_ready → produces → db_ready, search_ready
                       ↓
Lane C requires db_ready → produces → api_contract_ready
Lane D requires db_ready → produces → governance_ready
                       ↓
Lane E requires api_contract_ready + rbac_ready → produces → ui_ready
Lane F requires search_ready + api_contract_ready → produces → ai_ready
Lane G produces → deployment_ready
```

**If this agent fails → ENTIRE CONTRACT GATE CHAIN STOPS.**

---

## SECTION 8 — KAFKA TOPIC GOVERNANCE

### 8.1 Complete Topic Registry Declaration

This agent declares the intent for all Kafka topics. The Kafka Admin service provisions them only after `schema_registered = TRUE` is confirmed.

**RULE: No Kafka topic may be provisioned without a registered schema. Violation → STOP TOPIC CREATION.**

#### Core Platform Topics

| Topic | Domain | Partitions | Retention | Owner |
|---|---|---|---|---|
| `auth.events` | DOM-01 | 6 | 7 days | Auth Service |
| `user.events` | DOM-02 | 6 | 7 days | User Service |
| `job.events` | DOM-03 | 12 | 14 days | Job Service |
| `application.events` | DOM-04 | 12 | 14 days | Application Service |
| `interview.events` | DOM-05 | 6 | 14 days | Interview Service |
| `gd.events` | DOM-06 | 12 | 30 days | Voice GD Orchestrator |
| `dojo.events` | DOM-07 | 12 | 30 days | Dojo Match Engine |
| `billing.events` | DOM-08 | 6 | 90 days | Billing Service |
| `notification.events` | DOM-09 | 6 | 7 days | Notification Service |
| `intelligence.events` | DOM-10 | 6 | 30 days | Intelligence Engine |
| `innovation.events` | DOM-11 | 6 | 90 days | Innovation Registry |
| `society.events` | DOM-12 | 12 | 30 days | Society Service |
| `admin.events` | DOM-13 | 3 | 90 days | Admin Governance |
| `analytics.events` | DOM-14 | 3 | 14 days | Analytics Service |
| `phone.events` | DOM-15 | 6 | 7 days | PACA |

#### Trust Infrastructure & Governance Topics

| Topic | Purpose | Partitions | Retention |
|---|---|---|---|
| `contract.gates` | Lane gate signals for CI/CD orchestrator | 1 | 30 days |
| `schema.lifecycle.events` | Schema registration/activation/deprecation events | 3 | 365 days |
| `phone.events.dlq` | Phone event dead letter queue | 3 | 30 days |
| `phone.events.quarantine` | Phone event quarantine (PII violations) | 3 | 90 days |
| `phone.schema.audit` | Phone schema validation decisions | 3 | 30 days |
| `phone.schema.mutations` | Phone schema version changes | 1 | 365 days |
| `fraud.signals.phone` | Fraud detection → PACA inbound signals | 3 | 7 days |
| `global.sync.audit` | Schema sync audit events from this agent | 3 | 90 days |

### 8.2 Topic Naming Convention (LOCKED)

```
PATTERN          : {domain}.{sub_context}  OR  {domain}.{sub_context}.{qualifier}
EXAMPLES:
  Correct        : gd.events · billing.events · phone.schema.audit
  Correct        : phone.events.dlq · phone.events.quarantine
  Incorrect      : GD-Events · billingEvents · phone_schema_audit
  
RULE-TN-01  : All lowercase · dot-separated · no hyphens · no underscores
RULE-TN-02  : Maximum 3 segments · third segment = qualifier only (dlq, quarantine, audit)
RULE-TN-03  : Topic name registered in kafka_topic_registry BEFORE provisioning
RULE-TN-04  : Topic name change requires new topic creation + migration plan
              (renaming topics is FORBIDDEN — immutable once provisioned)
```

---

## SECTION 9 — PRODUCER ↔ CONSUMER WIRING MAP

### 9.1 Wiring Governance Rules (LOCKED)

```
RULE-W-01  : Every event type must declare its producer service and at least one consumer
             in event_wiring_map before schema activation is permitted

RULE-W-02  : Consumer services must declare their consumer group ID.
             Two consumers on the same topic MUST use different consumer group IDs.

RULE-W-03  : Wiring changes (adding/removing consumers) trigger a wiring_map.updated
             event on schema.lifecycle.events topic

RULE-W-04  : Removing a consumer from the wiring map requires:
             (a) Confirmation that the consumer service has been decommissioned, OR
             (b) Confirmation that the consumer has migrated to an alternative event source
             Absence of confirmation → REJECT wiring removal

RULE-W-05  : Cross-domain consumption is permitted but must be explicitly declared.
             Undeclared cross-domain consumption = unauthorized — Kafka ACL enforcement blocks it.

RULE-W-06  : Wiring map is the authority for Kafka ACL generation.
             Only declared producers and consumers receive Kafka ACL entries.
```

### 9.2 Critical Cross-Domain Wiring (SELECTED)

| Event Type | Producer | Consumers |
|---|---|---|
| `gd.completed` | Voice GD Orchestrator | Analytics Service · Scoring Engine · Notification Service |
| `match.scored` | Scoring Engine | Analytics Service · Belt Engine · Intelligence Engine |
| `belt.eligible` | Belt Engine | Certification Engine · Notification Service · User Service |
| `invoice.generated` | Billing Service | Notification Service · Analytics Service · Admin Governance |
| `intelligence.prediction.generated` | Intelligence Prediction Engine | User Service · Analytics Service · Job Service |
| `idea.similarity.flagged` | Idea Similarity Engine | Admin Governance · Innovation Reputation Service |
| `royalty.anomaly.detected` | Fraud Detection Engine | Royalty Audit Service · Admin Governance · Compliance |
| `society.created` | Society Service | Analytics Service · Commission Engine · Admin Governance |
| `commission.calculated` | Commission Engine | Payout Service · Analytics Service · Billing Service |
| `auth.account.locked` | Auth Service | Notification Service · Admin Governance · Intelligence Engine |
| `user.deletion.requested` | User Service | ALL services that hold user PII (fan-out consumer group) |

---

## SECTION 10 — API CONTRACT (OpenAPI 3.1)

```yaml
openapi: 3.1.0
info:
  title: GLOBAL_EVENT_REGISTRY_SYNC_AGENT
  version: "1.0"
  description: |
    Master global event schema registry, synchronisation, and gate enforcement
    for all ~227 event types across 15 domains in Ecoskiller.
    ENTERPRISE OPTIMIZATION + TRUST INFRASTRUCTURE — ANTIGRAVITY MODULE.

servers:
  - url: https://api.ecoskiller.com/trust/schema-registry
    description: Production (internal only — not exposed externally)
  - url: https://api.ecoskiller.staging/trust/schema-registry
    description: Staging

security:
  - bearerAuth: []

paths:

  /catalog:
    get:
      summary: Get complete global event catalog
      operationId: getEventCatalog
      x-rbac-roles: [PLATFORM_ADMIN, COMPLIANCE_OFFICER, CI_CD_VALIDATOR, OPS_INTERNAL]
      parameters:
        - name: domain_id
          in: query
          schema: { type: string }
        - name: is_active
          in: query
          schema: { type: boolean, default: true }
        - name: is_pii_bearing
          in: query
          schema: { type: boolean }
      responses:
        "200":
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/EventCatalogResponse'

  /domains:
    get:
      summary: List all registered domains
      operationId: listDomains
      x-rbac-roles: [PLATFORM_ADMIN, CI_CD_VALIDATOR, OPS_INTERNAL]
      responses:
        "200":
          content:
            application/json:
              schema:
                type: array
                items:
                  $ref: '#/components/schemas/DomainRecord'

  /schemas/{domain_id}/{event_type}:
    post:
      summary: Register a new event type and initial schema version
      operationId: registerEventSchema
      x-rbac-roles: [PLATFORM_ADMIN]
      requestBody:
        required: true
        content:
          application/json:
            schema:
              $ref: '#/components/schemas/RegisterEventSchemaRequest'
      responses:
        "201": { description: Event type and schema version registered in DRAFT state }
        "409": { description: Event type already registered }
        "422": { description: Schema failed Apicurio VALIDITY rule }

  /schemas/{domain_id}/{event_type}/versions/{version}/promote:
    post:
      summary: Promote schema version to next environment
      operationId: promoteSchemaVersion
      x-rbac-roles: [PLATFORM_ADMIN]
      requestBody:
        required: true
        content:
          application/json:
            schema:
              type: object
              required: [target_environment]
              properties:
                target_environment:
                  type: string
                  enum: [DEV, TEST, STAGING, PRODUCTION]
                approval_note: { type: string }
                second_approver_id:
                  type: string
                  format: uuid
                  description: Required for PII event types and BREAKING changes
      responses:
        "200": { description: Schema promoted · sync initiated }
        "400": { description: Promotion path violated (must follow DEV→TEST→STAGING→PROD) }
        "403": { description: Dual approval required }
        "412": { description: Minimum dwell time not met in prior environment }

  /sync/environments:
    get:
      summary: Get full environment synchronisation status
      operationId: getEnvSyncStatus
      x-rbac-roles: [PLATFORM_ADMIN, OPS_INTERNAL, CI_CD_VALIDATOR]
      responses:
        "200":
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/EnvironmentSyncStatusResponse'

  /sync/drift:
    get:
      summary: List all detected schema drifts
      operationId: listDrifts
      x-rbac-roles: [PLATFORM_ADMIN, OPS_INTERNAL]
      parameters:
        - name: environment
          in: query
          schema: { type: string, enum: [DEV, TEST, STAGING, PRODUCTION] }
        - name: resolved
          in: query
          schema: { type: boolean, default: false }
      responses:
        "200":
          content:
            application/json:
              schema:
                type: array
                items:
                  $ref: '#/components/schemas/DriftRecord'

  /sync/drift/{event_type}/resolve:
    post:
      summary: Trigger manual drift resolution for an event type
      operationId: resolveDrift
      x-rbac-roles: [PLATFORM_ADMIN]
      responses:
        "200": { description: Drift resolution sync initiated }

  /gates/{gate_name}/{environment}:
    get:
      summary: Get gate signal state for an environment
      operationId: getGateState
      x-rbac-roles: [PLATFORM_ADMIN, CI_CD_VALIDATOR, OPS_INTERNAL]
      responses:
        "200":
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/GateStateResponse'

  /gates/evaluate/{environment}:
    post:
      summary: Force re-evaluation of all gate conditions for an environment
      operationId: evaluateGates
      x-rbac-roles: [PLATFORM_ADMIN, CI_CD_VALIDATOR]
      responses:
        "200":
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/GateEvaluationResponse'

  /wiring:
    get:
      summary: Get producer-consumer wiring map
      operationId: getWiringMap
      x-rbac-roles: [PLATFORM_ADMIN, OPS_INTERNAL, CI_CD_VALIDATOR]
      parameters:
        - name: event_type
          in: query
          schema: { type: string }
        - name: consumer_service
          in: query
          schema: { type: string }
      responses:
        "200":
          content:
            application/json:
              schema:
                type: array
                items:
                  $ref: '#/components/schemas/WiringRecord'

  /wiring/{event_type}/consumers:
    post:
      summary: Register a new consumer for an event type
      operationId: registerConsumer
      x-rbac-roles: [PLATFORM_ADMIN]
      requestBody:
        required: true
        content:
          application/json:
            schema:
              $ref: '#/components/schemas/RegisterConsumerRequest'
      responses:
        "201": { description: Consumer registered }
        "409": { description: Consumer already registered for this event type }

  /compatibility/{event_type}/{from_version}/{to_version}:
    get:
      summary: Check compatibility between schema versions
      operationId: checkSchemaCompatibility
      x-rbac-roles: [PLATFORM_ADMIN, CI_CD_VALIDATOR, DEV_INTERNAL]
      responses:
        "200":
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/CompatibilityResult'

  /topics:
    get:
      summary: Get full Kafka topic registry
      operationId: getTopicRegistry
      x-rbac-roles: [PLATFORM_ADMIN, OPS_INTERNAL]
      responses:
        "200":
          content:
            application/json:
              schema:
                type: array
                items:
                  $ref: '#/components/schemas/TopicRecord'

  /health:
    get:
      summary: Agent and registry health
      operationId: healthCheck
      x-rbac-roles: [OPS_INTERNAL, PLATFORM_ADMIN]
      responses:
        "200":
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/AgentHealthResponse'

components:
  securitySchemes:
    bearerAuth:
      type: http
      scheme: bearer
      bearerFormat: JWT

  schemas:
    RegisterEventSchemaRequest:
      type: object
      required: [event_type, description, owning_service, kafka_topic,
                 schema_version, asyncapi_fragment, json_schema, is_backward_compatible]
      properties:
        event_type:            { type: string, pattern: "^[a-z][a-z0-9]*(\\.[a-z][a-z0-9]*)+$" }
        description:           { type: string, minLength: 10 }
        owning_service:        { type: string }
        kafka_topic:           { type: string }
        is_pii_bearing:        { type: boolean, default: false }
        pii_fields:            { type: array, items: { type: string } }
        requires_tenant_id:    { type: boolean, default: true }
        schema_version:        { type: string, pattern: "^\\d+\\.\\d+\\.\\d+$" }
        asyncapi_fragment:     { type: object }
        json_schema:           { type: object }
        is_backward_compatible:{ type: boolean }
        breaking_change_summary:{ type: string }

    GateStateResponse:
      type: object
      properties:
        gate_name:           { type: string }
        environment:         { type: string }
        gate_value:          { type: boolean }
        conditions_met:      { type: array, items: { type: string } }
        conditions_failed:   { type: array, items: { type: string } }
        last_evaluated_at:   { type: string, format: date-time }
        blocking_lanes:      { type: array, items: { type: string } }

    GateEvaluationResponse:
      type: object
      properties:
        environment:          { type: string }
        event_schema_ready:   { type: boolean }
        condition_results:    { type: object, additionalProperties: { type: boolean } }
        failed_conditions:    { type: array, items: { type: string } }
        evaluated_at:         { type: string, format: date-time }

    AgentHealthResponse:
      type: object
      properties:
        status:                     { type: string, enum: [HEALTHY, DEGRADED, CRITICAL] }
        apicurio_connected:         { type: boolean }
        postgres_connected:         { type: boolean }
        redis_connected:            { type: boolean }
        kafka_connected:            { type: boolean }
        total_event_types:          { type: integer }
        total_active_schemas:       { type: integer }
        drift_detected_count:       { type: integer }
        gate_event_schema_ready:    { type: object, additionalProperties: { type: boolean } }
        last_sync_at:               { type: string, format: date-time }
        sync_errors_last_hour:      { type: integer }
```

---

## SECTION 11 — EVENT SCHEMA (AsyncAPI 2.6) — AGENT'S OWN EVENTS

```yaml
asyncapi: 2.6.0
info:
  title: GLOBAL_EVENT_REGISTRY_SYNC_AGENT Lifecycle Events
  version: "1.0"

channels:

  schema.lifecycle.events:
    description: All schema registry lifecycle changes across all domains
    publish:
      message:
        oneOf:
          - $ref: '#/components/messages/SchemaVersionRegistered'
          - $ref: '#/components/messages/SchemaVersionActivated'
          - $ref: '#/components/messages/SchemaVersionDeprecated'
          - $ref: '#/components/messages/SchemaVersionRevoked'
          - $ref: '#/components/messages/SchemaDriftDetected'
          - $ref: '#/components/messages/SchemaDriftResolved'
          - $ref: '#/components/messages/WiringMapUpdated'
          - $ref: '#/components/messages/TopicSchemaRegistered'

  contract.gates:
    description: Gate signal events for Contract Gate System (all lanes)
    publish:
      message:
        $ref: '#/components/messages/GateSignalEmitted'

  global.sync.audit:
    description: Sync operation audit records
    publish:
      message:
        $ref: '#/components/messages/SyncAuditRecord'

components:
  messages:

    SchemaVersionActivated:
      payload:
        type: object
        required: [event_type, domain_id, schema_version, environment,
                   activated_at, approved_by, is_breaking_change]
        properties:
          event_type:          { type: string }
          domain_id:           { type: string }
          schema_version:      { type: string }
          environment:         { type: string }
          activated_at:        { type: string, format: date-time }
          approved_by:         { type: string, format: uuid }
          second_approver:     { type: ["string","null"], format: uuid }
          is_breaking_change:  { type: boolean }
          prior_version:       { type: ["string","null"] }

    SchemaDriftDetected:
      payload:
        type: object
        required: [event_type, domain_id, environment, expected_version,
                   actual_version, detected_at, severity]
        properties:
          event_type:        { type: string }
          domain_id:         { type: string }
          environment:       { type: string }
          expected_version:  { type: string }
          actual_version:    { type: ["string","null"] }
          detected_at:       { type: string, format: date-time }
          severity:          { type: string, enum: [LOW, MEDIUM, HIGH, CRITICAL] }
          auto_resync:       { type: boolean }

    GateSignalEmitted:
      payload:
        type: object
        required: [gate_name, environment, gate_value, emitted_at]
        properties:
          gate_name:           { type: string }
          environment:         { type: string }
          gate_value:          { type: boolean }
          conditions_met:      { type: array, items: { type: string } }
          conditions_failed:   { type: array, items: { type: string } }
          emitted_at:          { type: string, format: date-time }
          blocking_lanes:      { type: array, items: { type: string } }
```

---

## SECTION 12 — CI/CD INTEGRATION (LOCKED)

### 12.1 Mandatory Pipeline Gate: Schema Registry Check

This gate runs BEFORE any service deployment in ALL environments. It is non-optional.

```yaml
schema-registry-gate:
  stage: contract-validation         # Runs first — before build
  script:
    - |
      echo "Checking global event schema registry..."

      response=$(curl -s -f \
        "${SCHEMA_REGISTRY_URL}/gates/event_schema_ready/${CI_ENVIRONMENT_NAME}" \
        -H "Authorization: Bearer ${CI_SERVICE_TOKEN}")

      gate_value=$(echo $response | jq -r '.gate_value')

      if [ "$gate_value" != "true" ]; then
        echo "SCHEMA REGISTRY GATE FAILED for environment: ${CI_ENVIRONMENT_NAME}"
        echo "Failed conditions:"
        echo $response | jq -r '.conditions_failed[]'
        echo "Blocking lanes:"
        echo $response | jq -r '.blocking_lanes[]'
        exit 1
      fi

      echo "SCHEMA REGISTRY GATE PASSED — event_schema_ready = TRUE"
  rules:
    - if: '$CI_COMMIT_BRANCH == "production"'
      variables:
        CI_ENVIRONMENT_NAME: "PRODUCTION"
    - if: '$CI_COMMIT_BRANCH == "staging"'
      variables:
        CI_ENVIRONMENT_NAME: "STAGING"
    - if: '$CI_COMMIT_BRANCH == "test"'
      variables:
        CI_ENVIRONMENT_NAME: "TEST"
    - if: '$CI_COMMIT_BRANCH == "dev"'
      variables:
        CI_ENVIRONMENT_NAME: "DEV"

schema-wiring-check:
  stage: contract-validation
  script:
    - |
      # Verify all events declared by this service have registered consumers
      for event_type in $(cat ./schema-declarations/produces.txt); do
        consumers=$(curl -s "${SCHEMA_REGISTRY_URL}/wiring?event_type=${event_type}" \
          -H "Authorization: Bearer ${CI_SERVICE_TOKEN}" | jq '.| length')
        if [ "$consumers" -eq "0" ]; then
          echo "WIRING GATE FAILED: No consumer registered for ${event_type}"
          exit 1
        fi
      done
      echo "WIRING GATE PASSED"
```

### 12.2 Schema Compatibility Check in Pull Request Pipeline

```yaml
schema-compatibility-check:
  stage: pre-merge
  script:
    - |
      # Check all modified schema files for compatibility
      for schema_file in $(git diff --name-only origin/main | grep "schemas/"); do
        event_type=$(jq -r '.event_type' $schema_file)
        new_version=$(jq -r '.schema_version' $schema_file)
        
        # Get current active version
        current_version=$(curl -s \
          "${SCHEMA_REGISTRY_URL}/schemas/$(dirname $schema_file | cut -d/ -f2)/${event_type}" \
          -H "Authorization: Bearer ${CI_SERVICE_TOKEN}" | jq -r '.active_version')
        
        if [ "$current_version" != "null" ] && [ "$current_version" != "" ]; then
          compat=$(curl -s \
            "${SCHEMA_REGISTRY_URL}/compatibility/${event_type}/${current_version}/${new_version}" \
            -H "Authorization: Bearer ${CI_SERVICE_TOKEN}" | jq -r '.compatibility_type')
          
          if [ "$compat" == "BREAKING" ]; then
            echo "BREAKING CHANGE DETECTED: ${event_type} ${current_version} → ${new_version}"
            echo "Dual approval and 7-day staging dwell required."
            exit 1
          fi
          echo "COMPATIBLE: ${event_type} ${compat}"
        fi
      done
```

---

## SECTION 13 — OBSERVABILITY (NON-OPTIONAL)

### 13.1 Prometheus Metrics (All Required)

```
# Registry totals
global_schema_event_types_total{domain_id, state}
global_schema_versions_total{domain_id, state}
global_schema_active_versions_total{domain_id}
global_schema_deprecated_versions_total{domain_id}
global_schema_revoked_versions_total{domain_id}

# Sync health
global_schema_env_sync_state{environment, domain_id}           → Gauge: 1=SYNCED, 0=DRIFT/FAILED
global_schema_drift_detected_total{environment, domain_id, event_type}
global_schema_drift_active_count{environment}                  → Gauge
global_schema_sync_latency_ms{environment, domain_id}         → Histogram
global_schema_sync_failures_total{environment, domain_id}

# Gate signals
global_schema_gate_state{gate_name, environment}              → Gauge: 1=TRUE, 0=FALSE
global_schema_gate_evaluation_total{gate_name, environment, result}
global_schema_gate_condition_failures_total{gate_name, environment, condition_id}

# Apicurio health
global_schema_apicurio_health{environment}                    → Gauge: 1=UP, 0=DOWN
global_schema_apicurio_sync_latency_ms{environment}           → Histogram

# Kafka topics
global_schema_topic_registered_total{domain_id}
global_schema_topic_schema_missing_count                      → Gauge (critical if > 0)

# Wiring
global_schema_wiring_consumers_total{event_type}
global_schema_wiring_uncovered_events_count                   → Gauge (events with no consumers)
```

### 13.2 Alerting Rules

```
ALERT GlobalSchemaGateFailed
  expr: global_schema_gate_state{gate_name="event_schema_ready", environment="PRODUCTION"} == 0
  severity: CRITICAL
  action: Page on-call · ALL deployment lanes BLOCKED · incident declared

ALERT SchemaDriftProduction
  expr: global_schema_drift_active_count{environment="PRODUCTION"} > 0
  for: 5m
  severity: CRITICAL
  action: Page Platform Admin · Auto-resync initiated · Alert Domain Lead

ALERT SchemaDriftStaging
  expr: global_schema_drift_active_count{environment="STAGING"} > 0
  for: 15m
  severity: HIGH
  action: Alert Platform Admin · Auto-resync initiated

ALERT ApicurioDown
  expr: global_schema_apicurio_health{environment="PRODUCTION"} == 0
  severity: CRITICAL
  action: Page on-call · Gate set to FALSE · Incident declared

ALERT TopicWithoutSchema
  expr: global_schema_topic_schema_missing_count > 0
  severity: HIGH
  action: Alert Platform Admin · Block topic provisioning

ALERT UncoveredEventTypes
  expr: global_schema_wiring_uncovered_events_count > 0
  for: 30m
  severity: HIGH
  action: Alert Domain Lead · Wiring map must be updated

ALERT SchemaSyncFailureSpike
  expr: rate(global_schema_sync_failures_total[5m]) > 3
  severity: HIGH
  action: Alert Platform Admin · Investigate Apicurio connectivity

ALERT BreakingChangeInPromotion
  expr: increase(global_schema_gate_condition_failures_total{condition_id="breaking_change_approval"}[10m]) > 0
  severity: HIGH
  action: Alert Platform Admin + Domain Lead · Dual approval required
```

### 13.3 Grafana Dashboards (Required)

```
DASHBOARD-01  : Global Event Registry Overview
  - Active event types per domain
  - Schema version distribution (ACTIVE/DEPRECATED/REVOKED)
  - Event_schema_ready gate state per environment (4 gauges)

DASHBOARD-02  : Environment Synchronisation Health
  - Drift heatmap (domain × environment)
  - Sync latency histograms per environment
  - Apicurio health status per environment

DASHBOARD-03  : Gate Signal History
  - event_schema_ready timeline per environment
  - Condition failure frequency chart
  - Gate downtime analysis

DASHBOARD-04  : Kafka Topic Registry
  - Topic schema registration rate
  - Topics pending schema registration
  - Consumer coverage by event type

DASHBOARD-05  : Schema Evolution Velocity
  - Schema registrations per week per domain
  - Breaking changes per quarter
  - Version promotion time (DEV→PROD cycle time)
```

---

## SECTION 14 — SECURITY ENFORCEMENT

```
SEC-GE-01  : Only PLATFORM_ADMIN role may write to schema registry (register, activate,
             deprecate, revoke). DEV_INTERNAL may use /validate for testing only.

SEC-GE-02  : PII-bearing event type schemas (is_pii_bearing = TRUE) require dual
             approval (Platform Admin + Compliance Officer) for ACTIVE promotion.
             Single approval is REJECTED.

SEC-GE-03  : Apicurio Registry write access is exclusive to this agent via a
             dedicated Vault-managed service account token. Token TTL = 1 hour.
             No human or other service has direct Apicurio write credentials.

SEC-GE-04  : All domain-specific validation agents (including PHONE_EVENT_SCHEMA_
             VALIDATION_AGENT) receive READ-ONLY Apicurio tokens. Write is forbidden.

SEC-GE-05  : Schema artifacts pushed to Apicurio are HMAC-signed by this agent.
             Consumers that detect unsigned artifacts must reject and alert.

SEC-GE-06  : All API endpoints sit behind Kong API Gateway with ModSecurity WAF.
             Rate limit: 200 requests/min for read endpoints.
             Rate limit: 10 requests/min for write endpoints (schema mutation).

SEC-GE-07  : The global_sync_audit_log and gate_signals tables are append-only.
             No UPDATE or DELETE permitted. PostgreSQL trigger enforces this.

SEC-GE-08  : Environment sync operations use Vault-managed per-environment
             Apicurio credentials. DEV credentials cannot access PRODUCTION Apicurio.
             Credential isolation is enforced by Vault policy namespaces.

SEC-GE-09  : Schema bundle archives pushed to MinIO are encrypted at rest
             using Vault-managed AES-256 keys. MinIO server-side encryption required.

SEC-GE-10  : Any attempt to register an event type that duplicates an existing name
             across any domain is REJECTED. Event type names are globally unique.
             This prevents shadow topic registration attacks.
```

---

## SECTION 15 — FAILURE HANDLING (DETERMINISTIC)

| Failure Scenario | Agent Action | Recovery Path |
|---|---|---|
| Apicurio PRODUCTION unreachable | Set gate `event_schema_ready` = FALSE · emit CRITICAL alert · halt all schema pushes | Page on-call · k3s pod restart · manual restore from MinIO archive if data lost |
| Apicurio DEV/TEST/STAGING unreachable | Log DRIFT_DETECTED for affected environment · continue PROD operations · emit HIGH alert | Auto-retry every 60s · escalate after 10 min |
| PostgreSQL unreachable | HALT all registry reads/writes · return 503 · emit CRITICAL alert · gate = FALSE | DB HA failover via k3s persistent volume |
| Redis unreachable | Fallback to PostgreSQL for all state reads · disable caching · emit HIGH alert | Auto-reconnect · re-warm cache on restore |
| Kafka unreachable | Buffer lifecycle events in-memory (max 500, 10 min) · log sync operations to DB only | Auto-flush on Kafka restore · escalate after 5 min |
| Drift detected in PRODUCTION | Auto-initiate resync within 60s · if resync fails → page on-call · gate remains TRUE during resync attempt (grace=120s) | Manual resolution via /sync/drift/{event_type}/resolve |
| Schema push fails PRODUCTION Apicurio | ROLLBACK schema version to VALIDATED state · emit sync.failed event · alert | Human must investigate and re-trigger promotion |
| Gate condition G08 fails (companion agent down) | Attempt to reach companion agent for 60s · if still down: emit DEGRADED gate (not FALSE) for non-phone domains · emit FALSE gate for phone domain | Restart companion agent pod |
| MinIO archive write fails | Log failure · continue operations · alert · schedule retry | Retry every 300s · escalate after 3 failures |
| Breaking change detected in production promotion attempt | HARD REJECT promotion · emit BREAKING_CHANGE_BLOCKED alert · require dual approval before retry | Human approval + 7-day staging dwell |

---

## SECTION 16 — DEPLOYMENT SPECIFICATION

### 16.1 Kubernetes Manifest Requirements

```yaml
namespace: ecoskiller-trust

Deployment:
  name: global-event-registry-sync-agent
  replicas: 2 (minimum) · 4 (maximum HPA — registry ops are moderate CPU)
  resources:
    requests: { cpu: 500m, memory: 1Gi }
    limits:   { cpu: 2000m, memory: 2Gi }
  hpa:
    minReplicas: 2
    maxReplicas: 4
    targetCPUUtilizationPercentage: 70

CronJob (Drift Detector):
  name: schema-drift-detector
  schedule: "*/5 * * * *"           # Every 5 minutes
  image: global-event-registry-sync-agent (same image, drift-detect mode)
  concurrencyPolicy: Forbid          # No parallel drift detections

CronJob (Gate Evaluator):
  name: schema-gate-evaluator
  schedule: "*/2 * * * *"           # Every 2 minutes
  image: same
  concurrencyPolicy: Forbid

  env:
    - name: APICURIO_URL_PROD
      valueFrom: { secretKeyRef: { name: apicurio-prod-secret, key: url } }
    - name: APICURIO_URL_STAGING
      valueFrom: { secretKeyRef: { name: apicurio-staging-secret, key: url } }
    - name: APICURIO_URL_TEST
      valueFrom: { secretKeyRef: { name: apicurio-test-secret, key: url } }
    - name: APICURIO_URL_DEV
      valueFrom: { secretKeyRef: { name: apicurio-dev-secret, key: url } }
    - name: POSTGRES_URL
      valueFrom: { secretKeyRef: { name: postgres-secret, key: url } }
    - name: REDIS_URL
      valueFrom: { secretKeyRef: { name: redis-secret, key: url } }
    - name: KAFKA_BOOTSTRAP_SERVERS
      valueFrom: { configMapKeyRef: { name: kafka-config, key: bootstrap_servers } }
    - name: VAULT_ADDR
      valueFrom: { configMapKeyRef: { name: vault-config, key: addr } }
    - name: MINIO_ENDPOINT
      valueFrom: { secretKeyRef: { name: minio-secret, key: endpoint } }
    - name: DRIFT_RESYNC_GRACE_SECONDS
      value: "120"
    - name: PROMOTION_DWELL_TEST_HOURS
      value: "24"
    - name: PROMOTION_DWELL_STAGING_HOURS
      value: "72"
    - name: BREAKING_CHANGE_DWELL_STAGING_HOURS
      value: "168"
    - name: GATE_TOPIC
      value: "contract.gates"
    - name: LIFECYCLE_TOPIC
      value: "schema.lifecycle.events"
```

---

## SECTION 17 — SERVICE DEPENDENCIES (CONTRACT GATES)

### 17.1 Upstream Dependencies (This Agent Requires)

| Service | Dependency | Gate Requirement |
|---|---|---|
| Apicurio Registry (all 4 envs) | Schema artifact storage and enforcement | Apicurio MUST be healthy in PRODUCTION |
| PostgreSQL | Master registry persistence | `db_ready` MUST be ACTIVE |
| Redis | State cache · sync locks · gate signals | Redis MUST be healthy |
| Kafka | Lifecycle event publication · gate signal publication | Kafka MUST be reachable |
| MinIO | Schema bundle archive | Available at startup (non-blocking if degraded) |
| Keycloak | JWT validation for API access | `identity_ready` MUST be ACTIVE |
| OPA | Policy enforcement on schema mutations | `policy_engine_ready` MUST be ACTIVE |
| Vault | Apicurio credentials · HMAC keys · MinIO keys | MUST be reachable |
| PHONE_API_CONTRACT_REGISTRY_AGENT | Companion domain ready signal | phone_trust_ready consumed |
| PHONE_EVENT_SCHEMA_VALIDATION_AGENT | Companion domain ready signal | phone_schema_ready consumed |

### 17.2 Gate Signal Produced

```
GATE PRODUCED      : event_schema_ready
PRODUCED PER       : All 4 environments independently
CONSUMED BY        : Lane B (db_ready gate executor)
                     Lane B (search_ready gate executor)
                     CI/CD orchestrator (Forgejo Actions / GitLab CI)
                     All microservice deployment pipelines
```

---

## SECTION 18 — CONTRACT GATE CHECKLIST

All items must be TRUE before agent may emit `event_schema_ready = TRUE` for any environment.

```
[ ] global_event_registry schema migrated (Flyway)
[ ] event_domains table seeded with all 15 domains (DOM-01 through DOM-15)
[ ] global_event_types table seeded with all ~227 event types
[ ] kafka_topic_registry table seeded with all ~60+ declared topics
[ ] event_wiring_map table seeded with all declared producer-consumer pairs
[ ] At least one ACTIVE schema version registered for every event type in every domain
[ ] Apicurio Registry PRODUCTION instance reachable and authenticated
[ ] Apicurio Registry STAGING instance reachable and authenticated
[ ] Apicurio Registry TEST instance reachable and authenticated
[ ] Apicurio Registry DEV instance reachable and authenticated
[ ] All ~227 event type schemas pushed to all 4 Apicurio instances
[ ] Apicurio BACKWARD compatibility rule set for all artifact groups
[ ] Apicurio FULL validity rule set for all artifact groups
[ ] Redis connected · all gate keys initialised
[ ] Kafka producer connected · contract.gates topic created
[ ] Kafka producer connected · schema.lifecycle.events topic created
[ ] Kafka producer connected · global.sync.audit topic created
[ ] Kafka consumer connected · (no domain events consumed — this agent is read-only on domain topics)
[ ] MinIO bucket created: ecoskiller-schema-archive
[ ] MinIO encryption enabled with Vault-managed key
[ ] OPA policy bundle loaded · schema_mutation policies active
[ ] Vault credentials loaded for all 4 Apicurio instances
[ ] Vault HMAC key loaded for schema artifact signing
[ ] Drift detection CronJob deployed and scheduled
[ ] Gate evaluation CronJob deployed and scheduled
[ ] PHONE_API_CONTRACT_REGISTRY_AGENT health confirmed (phone_trust_ready = TRUE)
[ ] PHONE_EVENT_SCHEMA_VALIDATION_AGENT health confirmed (phone_schema_ready = TRUE)
[ ] All 15 domains show zero DRIFT_DETECTED in environment_sync_state
[ ] All 10 gate conditions evaluate TRUE for PRODUCTION
[ ] All Prometheus alert rules deployed to Alertmanager
[ ] All 5 Grafana dashboards deployed
[ ] CI/CD schema-registry-gate step confirmed in all pipeline templates
```

**All 32 checks must pass → `event_schema_ready = TRUE` emitted to `contract.gates` topic.**  
**Any failure → STOP EXECUTION → REPORT EVENT-SCHEMA-NOT-READY → ALL LANES BLOCKED.**

---

## SECTION 19 — MUTATION POLICY

```
ALLOWED        : Registering new event types for existing or new domains (human approval)
ALLOWED        : Adding MINOR schema versions (optional fields · backward compatible)
ALLOWED        : Registering new Kafka topics with schemas (human approval + version bump)
ALLOWED        : Adding new consumer entries to wiring map (human approval)
ALLOWED        : Adding new domains (DOM-16+) with full domain registration (agent version bump)
ALLOWED        : Adjusting drift detection schedule (ConfigMap change, no version bump)
ALLOWED        : Adjusting promotion dwell times (ConfigMap change, approval required)

FORBIDDEN      : Removing event types from the registry without full sunset + migration plan
FORBIDDEN      : Changing global event type names post-activation (immutable)
FORBIDDEN      : Changing Kafka topic names post-provisioning (immutable)
FORBIDDEN      : Granting Apicurio write access to any service other than this agent
FORBIDDEN      : Promoting schemas directly from DEV to PRODUCTION (path must be followed)
FORBIDDEN      : Auto-activating schemas in PRODUCTION without human approval
FORBIDDEN      : Skipping the wiring map registration for any event type
FORBIDDEN      : Disabling the gate evaluation CronJob in production
FORBIDDEN      : Disabling the drift detection CronJob in production
FORBIDDEN      : Setting `event_schema_ready` = TRUE manually via Redis write
               without a full gate condition evaluation passing
FORBIDDEN      : Adding AI/ML judgment to schema compatibility decisions
               (This agent is deterministic — rules and Apicurio rules only)
```

---

## SECTION 20 — RELATIONSHIP TO COMPANION AGENTS

```
GLOBAL_EVENT_REGISTRY_SYNC_AGENT (THIS AGENT)
  │
  │ Registers DOM-15 schemas to Apicurio
  │ Pushes phone event schemas across all environments
  │ Receives phone_trust_ready signal (gate condition G08a)
  │ Receives phone_schema_ready signal (gate condition G08b)
  ▼
PHONE_API_CONTRACT_REGISTRY_AGENT (v1.0)
  │ Governs phone API contracts + OTP + trust bindings
  │ Produces: phone.events topic
  │ Produces gate: phone_trust_ready
  │ Consumes schemas FROM Apicurio (read-only, synced by this agent)
  ▼
PHONE_EVENT_SCHEMA_VALIDATION_AGENT (v1.0)
  │ Validates every message on phone.events topic
  │ Derives active schemas from Apicurio (read-only, synced by this agent)
  │ Produces gate: phone_schema_ready
  │ Produces: phone.schema.audit · phone.events.dlq · phone.events.quarantine
  ▼
[All downstream phone event consumers]
  Fraud Detection Engine · Analytics Service · Admin Governance
  Parent Dashboard Service · Notification Service · Immutable Archive
```

**The authority hierarchy is unambiguous:**  
`GLOBAL_EVENT_REGISTRY_SYNC_AGENT → (pushes schemas to) Apicurio → (read by) Domain Validation Agents → (validated events consumed by) Domain Consumers`

---

## SECTION 21 — VERSION HISTORY

| Version | Status | Change |
|---|---|---|
| v1.0 | SEALED · ACTIVE | Initial declaration — all 21 sections locked · 15 domains · ~227 event types · ~60+ Kafka topics · 32-point gate checklist · 10-condition gate evaluation |

---

**DOCUMENT END**  
**GLOBAL_EVENT_REGISTRY_SYNC_AGENT v1.0**  
**ECOSKILLER — ENTERPRISE OPTIMIZATION + TRUST INFRASTRUCTURE — ANTIGRAVITY**  
`SEALED · LOCKED · GOVERNED · ADD-ONLY`
