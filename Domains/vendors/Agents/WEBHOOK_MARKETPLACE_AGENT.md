# 🔐 WEBHOOK_MARKETPLACE_AGENT.md
## SEALED & LOCKED EXECUTION PROMPT — ANTIGRAVITY ENGINE
### ECOSKILLER · ENTERPRISE SAAS PLATFORM
---

```
PROMPT_CLASS        = WEBHOOK_MARKETPLACE_AGENT
EXECUTION_ENGINE    = ANTIGRAVITY
AGENT_VERSION       = 1.0.0
MUTATION_POLICY     = ADD_ONLY
CREATIVE_INTERPRETATION = FORBIDDEN
ASSUMPTION_FILLING  = FORBIDDEN
IMPLICIT_BEHAVIOR   = FORBIDDEN
DEFAULT_BEHAVIOR    = DENY
FAILURE_MODE        = STOP_EXECUTION
EXECUTION_MODE      = LOCKED
STATUS              = SEALED
```

---

## ⚠️ PREAMBLE — READ BEFORE EXECUTION

This agent governs the complete **Webhook Delivery System** and **Plugin/Integration Marketplace** for the ECOSKILLER platform. It inherits — and must never duplicate — all decisions already sealed in the Master Execution Prompt v12.0, the UI Constitution, the Backend Architecture Constitution, and Sections R65 (Open Platform Extensibility Law) and R19 (API Versioning & Deprecation Law).

**Antigravity MUST NOT:**
- Re-derive architecture decisions already defined in the master prompt
- Generate any UI component outside Flutter (primary) and Next.js (SEO read-only clone)
- Introduce any authentication logic beyond Keycloak + JWT already mandated
- Create any new database outside the mandated PostgreSQL / Redis / ClickHouse / OpenSearch stack
- Apply creative interpretation to any field, schema, or workflow

**Any deviation from the above = STOP EXECUTION + REPORT DEVIATION**

---

## SECTION 1 — AGENT IDENTITY & SCOPE

```
AGENT_NAME          = WebhookMarketplaceAgent
AGENT_TYPE          = INTEGRATION_ORCHESTRATION_AGENT
PARENT_PROMPT       = ECOSKILLER_MASTER_EXECUTION_PROMPT_v12.0
INHERITS_FROM       = [R65, R19, Auth_Service, Integration_Hub, Kafka_EventBus]
OWNED_MODULES       = [Webhook_Engine, Plugin_Registry, Developer_Portal, Marketplace_UI]
KAFKA_CONSUMER      = YES
KAFKA_PRODUCER      = YES
PLATFORM_SCOPE      = MULTI_TENANT
```

**Functional Mandate:**

This agent is responsible for:

1. Registering, validating, and delivering webhook events to external subscribers
2. Managing the Plugin Marketplace — the curated store of verified third-party integrations
3. Issuing, rotating, and revoking External API Keys for developer and enterprise access
4. Enforcing rate-limiting, signature validation, retry logic, and dead-letter handling on all outbound webhook payloads
5. Providing a Developer Portal UI (Flutter + Next.js) that is the sole surface for external access configuration
6. Consuming platform-wide Kafka events and routing them to eligible webhook subscribers
7. Maintaining an immutable delivery audit log per tenant per event

**Out of Scope (Do NOT generate):**

- Core authentication flows (owned by Auth_Service)
- Billing subscription logic (owned by Billing_Service)
- Job/Skill/Project domain business logic
- Media infrastructure (Jitsi / LiveKit / coturn)
- AI/ML scoring engines

---

## SECTION 2 — EXECUTION INHERITANCE LOCK

The following contracts are inherited as SEALED. Antigravity MUST NOT redefine them:

| Inherited Contract | Source Section | Rule |
|---|---|---|
| RBAC + ABAC enforcement | Master Prompt §Auth | All webhook subscriptions are role-gated |
| Tenant isolation at DB layer | Master Prompt §Data | Row-level security on all webhook tables |
| Kafka event bus topology | Master Prompt §VI | Events originate from existing Kafka topics ONLY |
| JWT token format | Master Prompt §Auth_Service | API Key issuance wraps existing JWT infrastructure |
| PostgreSQL as primary store | Master Prompt §III | Webhook tables live in PostgreSQL |
| Redis for state/locks/timers | Master Prompt §III | Delivery queue locks use Redis |
| ClickHouse for analytics | Master Prompt §III | Delivery metrics written to ClickHouse |
| OpenSearch for search | Master Prompt §III | Plugin marketplace search via OpenSearch |
| Kong as API Gateway | Master Prompt §NGINX/Kong | All developer API requests pass through Kong |
| HashiCorp Vault for secrets | Master Prompt §Vault | Webhook signing keys stored in Vault |
| Open Policy Agent | Master Prompt §OPA | Permission-as-code enforces subscription eligibility |
| Prometheus + Loki + OTEL | Master Prompt §VIII | Webhook delivery metrics and tracing mandatory |
| WCAG 2.1 AA accessibility | R20 | All Developer Portal UI must comply |
| Flutter primary UI | UI Constitution §2 | Developer Portal primary app = Flutter |
| Next.js SEO read-only clone | R31 | Developer Portal web = Next.js (read-only) |
| API versioning policy | R19 | All webhook APIs follow SemVer + deprecation rules |

**Conflict resolution rule:** If any generated artifact in this agent contradicts an inherited contract, the inherited contract wins. The conflicting artifact MUST NOT be generated. STOP and REPORT.

---

## SECTION 3 — CANONICAL EVENT CATALOG (LOCKED)

All webhook deliverable events are sourced exclusively from the Ecoskiller Kafka event bus. No new event types may be invented by this agent. The following is the sealed event catalog:

### 3.1 Core Platform Events

```
EVENT_NAMESPACE     = ecoskiller.v1
ENCODING            = JSON + UTF-8
TRANSPORT           = Kafka (internal) → Webhook Engine → HTTPS (external)
```

| Event Key | Source Service | Payload Summary | Tenant-Scoped |
|---|---|---|---|
| `user.created` | Auth_Service | user_id, role, tenant_id, created_at | YES |
| `user.verified` | Auth_Service | user_id, verification_type, timestamp | YES |
| `user.deactivated` | Admin_Governance | user_id, reason, admin_id, timestamp | YES |
| `job.published` | Job_Service | job_id, title, company, location, salary_band | YES |
| `job.closed` | Job_Service | job_id, closed_at, filled_flag | YES |
| `job.applied` | Application_Service | application_id, job_id, candidate_id, timestamp | YES |
| `application.stage_changed` | Application_Service | application_id, from_stage, to_stage, actor_id | YES |
| `application.rejected` | Application_Service | application_id, reason_code, timestamp | YES |
| `application.offer_extended` | Application_Service | application_id, offer_id, expiry_date | YES |
| `interview.scheduled` | Interview_Service | interview_id, candidate_id, recruiter_id, slot | YES |
| `interview.completed` | Interview_Service | interview_id, outcome, duration_seconds | YES |
| `interview.cancelled` | Interview_Service | interview_id, cancelled_by, reason | YES |
| `gd.session_started` | Voice_GD_Orchestrator | session_id, batch_id, participant_count | YES |
| `gd.completed` | Voice_GD_Orchestrator | session_id, scores_emitted_flag, duration | YES |
| `match.scored` | Scoring_Engine | match_id, candidate_id, final_score, belt_eligible | YES |
| `belt.eligible` | Certification_Engine | user_id, belt_level, criteria_met | YES |
| `belt.awarded` | Certification_Engine | user_id, belt_id, certificate_url, issued_at | YES |
| `invoice.generated` | Billing_Service | invoice_id, tenant_id, amount, due_date | YES |
| `invoice.paid` | Billing_Service | invoice_id, paid_at, payment_method | YES |
| `subscription.upgraded` | Billing_Service | tenant_id, from_plan, to_plan, effective_date | YES |
| `subscription.cancelled` | Billing_Service | tenant_id, reason, end_date | YES |
| `skill.gap_detected` | Analytics_Service | user_id, skill_domain, gap_score | YES |
| `project.milestone_met` | Project_Service | project_id, milestone_id, verifier_id | YES |
| `project.completed` | Project_Service | project_id, completion_date, portfolio_url | YES |
| `career_stage.changed` | Career_Lifecycle_Service | user_id, from_stage, to_stage, triggered_by | YES |
| `complaint.filed` | Admin_Governance | complaint_id, anonymous_flag, target_entity | YES |
| `moderation.action_taken` | Admin_Governance | entity_id, action_type, actor_admin_id | YES |

**Rules:**
- No event may be delivered to a subscriber in a different tenant than the event source
- Events marked tenant-scoped MUST carry `tenant_id` in all payloads
- Empty or null tenant_id on a tenant-scoped event = DELIVERY BLOCKED + ALERT

---

## SECTION 4 — DATABASE SCHEMA (POSTGRESQL — MANDATORY)

All tables live inside the `ecoskiller` schema. Tenant isolation is enforced via `tenant_id` on every row. Row-level security policies are mandatory and must be generated alongside tables.

### 4.1 `webhook_subscriptions`

```sql
CREATE TABLE ecoskiller.webhook_subscriptions (
  id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  tenant_id           UUID NOT NULL REFERENCES ecoskiller.tenants(id) ON DELETE CASCADE,
  subscriber_id       UUID NOT NULL,         -- user_id or service account
  subscriber_type     TEXT NOT NULL          -- 'user' | 'service_account' | 'plugin'
                      CHECK (subscriber_type IN ('user', 'service_account', 'plugin')),
  endpoint_url        TEXT NOT NULL,
  secret_hash         TEXT NOT NULL,         -- HMAC-SHA256 secret, stored as bcrypt hash
  vault_secret_ref    TEXT NOT NULL,         -- Reference to HashiCorp Vault path
  subscribed_events   TEXT[] NOT NULL,       -- Array of event keys from catalog §3.1
  status              TEXT NOT NULL DEFAULT 'active'
                      CHECK (status IN ('active', 'paused', 'suspended', 'deleted')),
  failure_count       INT NOT NULL DEFAULT 0,
  last_success_at     TIMESTAMPTZ,
  last_failure_at     TIMESTAMPTZ,
  created_at          TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  updated_at          TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  created_by          UUID NOT NULL,
  version             INT NOT NULL DEFAULT 1
);

-- Row-level security
ALTER TABLE ecoskiller.webhook_subscriptions ENABLE ROW LEVEL SECURITY;
CREATE POLICY tenant_isolation ON ecoskiller.webhook_subscriptions
  USING (tenant_id = current_setting('app.current_tenant_id')::UUID);
```

### 4.2 `webhook_delivery_log`

```sql
CREATE TABLE ecoskiller.webhook_delivery_log (
  id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  subscription_id     UUID NOT NULL REFERENCES ecoskiller.webhook_subscriptions(id),
  tenant_id           UUID NOT NULL,
  event_key           TEXT NOT NULL,
  event_id            UUID NOT NULL,              -- Kafka message ID / event correlation
  payload_hash        TEXT NOT NULL,              -- SHA-256 of delivered payload (immutable proof)
  attempt_number      INT NOT NULL DEFAULT 1,
  http_status_code    INT,
  response_body_hash  TEXT,                       -- Hashed response (PII-safe)
  delivery_status     TEXT NOT NULL
                      CHECK (delivery_status IN ('pending', 'success', 'failed', 'dead_letter')),
  delivered_at        TIMESTAMPTZ,
  next_retry_at       TIMESTAMPTZ,
  error_message       TEXT,
  duration_ms         INT,
  created_at          TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- Immutable: no UPDATE or DELETE permitted on this table
-- Enforce via trigger:
CREATE OR REPLACE FUNCTION ecoskiller.prevent_delivery_log_mutation()
RETURNS TRIGGER AS $$
BEGIN
  RAISE EXCEPTION 'webhook_delivery_log is immutable. No UPDATE or DELETE permitted.';
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER delivery_log_immutability
  BEFORE UPDATE OR DELETE ON ecoskiller.webhook_delivery_log
  FOR EACH ROW EXECUTE FUNCTION ecoskiller.prevent_delivery_log_mutation();

-- RLS
ALTER TABLE ecoskiller.webhook_delivery_log ENABLE ROW LEVEL SECURITY;
CREATE POLICY tenant_isolation ON ecoskiller.webhook_delivery_log
  USING (tenant_id = current_setting('app.current_tenant_id')::UUID);
```

### 4.3 `external_api_keys`

```sql
CREATE TABLE ecoskiller.external_api_keys (
  id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  tenant_id           UUID NOT NULL REFERENCES ecoskiller.tenants(id) ON DELETE CASCADE,
  owner_id            UUID NOT NULL,             -- user_id who created the key
  key_prefix          TEXT NOT NULL,             -- First 8 chars for identification (esk_abc12345)
  key_hash            TEXT NOT NULL,             -- bcrypt hash of the full key
  vault_path          TEXT NOT NULL,             -- Vault path where full key is stored (write-once)
  label               TEXT NOT NULL,             -- Human-readable name
  scopes              TEXT[] NOT NULL,           -- e.g. ['webhooks:read', 'plugins:read']
  status              TEXT NOT NULL DEFAULT 'active'
                      CHECK (status IN ('active', 'revoked', 'expired')),
  expires_at          TIMESTAMPTZ,
  last_used_at        TIMESTAMPTZ,
  request_count       BIGINT NOT NULL DEFAULT 0,
  revoked_at          TIMESTAMPTZ,
  revoked_by          UUID,
  created_at          TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

ALTER TABLE ecoskiller.external_api_keys ENABLE ROW LEVEL SECURITY;
CREATE POLICY tenant_isolation ON ecoskiller.external_api_keys
  USING (tenant_id = current_setting('app.current_tenant_id')::UUID);
```

### 4.4 `plugin_registry`

```sql
CREATE TABLE ecoskiller.plugin_registry (
  id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  slug                TEXT UNIQUE NOT NULL,      -- e.g. 'slack-notifier', 'github-sync'
  name                TEXT NOT NULL,
  description         TEXT NOT NULL,
  vendor_name         TEXT NOT NULL,
  vendor_verified     BOOLEAN NOT NULL DEFAULT FALSE,
  category            TEXT NOT NULL
                      CHECK (category IN ('notification', 'hris', 'lms', 'crm', 'analytics', 'identity', 'productivity', 'compliance', 'other')),
  tier                TEXT NOT NULL DEFAULT 'free'
                      CHECK (tier IN ('free', 'premium', 'enterprise')),
  webhook_events      TEXT[] NOT NULL,           -- Events this plugin subscribes to
  oauth_supported     BOOLEAN NOT NULL DEFAULT FALSE,
  api_key_supported   BOOLEAN NOT NULL DEFAULT TRUE,
  docs_url            TEXT,
  logo_url            TEXT,
  status              TEXT NOT NULL DEFAULT 'pending'
                      CHECK (status IN ('pending', 'approved', 'suspended', 'deprecated')),
  install_count       INT NOT NULL DEFAULT 0,
  rating_avg          NUMERIC(3,2),
  review_count        INT NOT NULL DEFAULT 0,
  created_at          TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  approved_by         UUID,
  approved_at         TIMESTAMPTZ,
  version             TEXT NOT NULL DEFAULT '1.0.0'
);
```

### 4.5 `tenant_plugin_installations`

```sql
CREATE TABLE ecoskiller.tenant_plugin_installations (
  id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  tenant_id           UUID NOT NULL REFERENCES ecoskiller.tenants(id) ON DELETE CASCADE,
  plugin_id           UUID NOT NULL REFERENCES ecoskiller.plugin_registry(id),
  installed_by        UUID NOT NULL,
  config_json         JSONB NOT NULL DEFAULT '{}',   -- Plugin-specific config (encrypted at rest)
  subscription_id     UUID REFERENCES ecoskiller.webhook_subscriptions(id),
  status              TEXT NOT NULL DEFAULT 'active'
                      CHECK (status IN ('active', 'paused', 'uninstalled')),
  installed_at        TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  uninstalled_at      TIMESTAMPTZ
);

ALTER TABLE ecoskiller.tenant_plugin_installations ENABLE ROW LEVEL SECURITY;
CREATE POLICY tenant_isolation ON ecoskiller.tenant_plugin_installations
  USING (tenant_id = current_setting('app.current_tenant_id')::UUID);
```

### 4.6 `plugin_reviews`

```sql
CREATE TABLE ecoskiller.plugin_reviews (
  id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  plugin_id           UUID NOT NULL REFERENCES ecoskiller.plugin_registry(id),
  reviewer_id         UUID NOT NULL,
  tenant_id           UUID NOT NULL,
  rating              SMALLINT NOT NULL CHECK (rating BETWEEN 1 AND 5),
  review_text         TEXT,
  moderation_status   TEXT NOT NULL DEFAULT 'approved'
                      CHECK (moderation_status IN ('pending', 'approved', 'rejected')),
  created_at          TIMESTAMPTZ NOT NULL DEFAULT NOW()
);
```

---

## SECTION 5 — WEBHOOK ENGINE MICROSERVICE SPECIFICATION

```
SERVICE_NAME        = webhook-engine
LANGUAGE            = Node.js (TypeScript) OR Java (Spring Boot) — team choice
NAMESPACE           = ecoskiller-core (Kubernetes)
SCALING_POLICY      = Horizontal (HPA min: 2, max: 10)
PERSISTENCE         = PostgreSQL (primary), Redis (queue locks, retry state)
ANALYTICS_SINK      = ClickHouse (delivery metrics)
SECRET_STORE        = HashiCorp Vault
API_GATEWAY         = Kong
TRACING             = OpenTelemetry
```

### 5.1 Core Responsibilities

The Webhook Engine is a dedicated microservice with exactly the following responsibilities. It has NO business domain logic:

1. **Kafka Consumer** — Subscribe to all event topics in the sealed event catalog (§3.1)
2. **Subscription Router** — Match each consumed event to eligible webhook subscriptions in PostgreSQL. Tenant-scope enforcement is mandatory.
3. **Payload Builder** — Construct canonical JSON payloads. Sign with HMAC-SHA256 using the subscriber's secret (fetched from Vault).
4. **HTTP Delivery Worker** — POST the signed payload to the subscriber's `endpoint_url` with required headers.
5. **Retry Orchestrator** — Manage exponential backoff retry schedule on delivery failures.
6. **Dead-Letter Handler** — Move subscriptions exceeding failure threshold to `suspended` status and emit an internal `webhook.subscription.suspended` event.
7. **Delivery Log Writer** — Write an immutable delivery record to `webhook_delivery_log` for every delivery attempt.
8. **Analytics Emitter** — Write aggregated delivery metrics to ClickHouse.

### 5.2 HTTP Delivery Contract

Every outbound webhook POST MUST include the following headers. Missing any header = DELIVERY FAILURE:

```
POST {endpoint_url}
Content-Type: application/json
X-Ecoskiller-Event: {event_key}
X-Ecoskiller-Delivery: {delivery_id}         // UUID from webhook_delivery_log.id
X-Ecoskiller-Timestamp: {unix_epoch_ms}
X-Ecoskiller-Signature-256: sha256={hmac}    // HMAC-SHA256 of raw payload body
X-Ecoskiller-Version: v1
```

**Signature Computation (SEALED):**

```
ALGORITHM         = HMAC-SHA256
SECRET_SOURCE     = HashiCorp Vault (path: secret/tenants/{tenant_id}/webhooks/{subscription_id})
INPUT             = raw JSON payload bytes (UTF-8)
OUTPUT            = hex-encoded HMAC digest
PREFIX            = sha256=
```

Subscribers MUST validate the signature before processing. The developer portal must display implementation examples in: JavaScript, Python, Java, PHP, Go, and cURL.

### 5.3 Retry Policy (SEALED — No Creative Deviation)

```
MAX_ATTEMPTS        = 5
BACKOFF_TYPE        = EXPONENTIAL
BACKOFF_FORMULA     = 30 * (2 ^ (attempt - 1)) seconds
ATTEMPT_SCHEDULE    = 30s → 60s → 120s → 240s → 480s
TIMEOUT_PER_REQUEST = 10 seconds
FAILURE_THRESHOLD   = 50 consecutive failures across any 24h window
FAILURE_ACTION      = Suspend subscription + notify tenant admin
DEAD_LETTER_EVENT   = webhook.subscription.suspended (emitted to Kafka)
```

### 5.4 Payload Envelope Schema (ALL EVENTS)

```json
{
  "meta": {
    "event_key": "job.applied",
    "event_id": "uuid-v4",
    "delivery_id": "uuid-v4",
    "tenant_id": "uuid-v4",
    "subscription_id": "uuid-v4",
    "timestamp_ms": 1700000000000,
    "api_version": "v1",
    "attempt_number": 1
  },
  "data": {
    // Event-specific payload matching sealed catalog §3.1
  }
}
```

- `meta` block is immutable and mandatory on every delivery
- `data` block contents are defined per event in the catalog §3.1
- No additional fields may be injected by the engine (no creative payload additions)

### 5.5 Kafka Consumer Configuration (LOCKED)

```
GROUP_ID            = webhook-engine-consumer
CONSUMER_THREADS    = 4 (configurable via ENV)
AUTO_OFFSET_RESET   = earliest
COMMIT_POLICY       = MANUAL (commit only after successful DB write)
DESERIALIZER        = JSON
TOPICS_SUBSCRIBED   = ALL topics in §3.1 event catalog
ISOLATION_LEVEL     = read_committed
```

---

## SECTION 6 — EXTERNAL API KEY SERVICE SPECIFICATION

```
SERVICE_NAME        = api-key-service
OWNED_BY            = Integration_Hub (webhook-engine module)
AUTH_DEPENDENCY     = Keycloak + JWT (inherited, not re-implemented)
VAULT_DEPENDENCY    = HashiCorp Vault (key storage)
GATEWAY_DEPENDENCY  = Kong (rate limiting + key validation middleware)
```

### 6.1 API Key Format

```
PREFIX              = esk_
TOTAL_LENGTH        = 48 characters
STRUCTURE           = esk_{base62_random_40_chars}
EXAMPLE             = esk_X7kP2mNqR8vLtY9wZaJcB3dF0gH4iKnM6oQsT1uV5
STORAGE_RULE        = Full key stored in Vault (write-once). Only prefix stored in PostgreSQL.
DISPLAY_RULE        = Full key shown ONCE at creation. Never retrievable again.
```

### 6.2 API Key Scopes (Sealed List)

| Scope | Description |
|---|---|
| `webhooks:read` | List and read webhook subscriptions |
| `webhooks:write` | Create, update, pause, delete subscriptions |
| `webhooks:logs:read` | Read delivery logs |
| `plugins:read` | Browse plugin marketplace |
| `plugins:install` | Install plugins to tenant |
| `api_keys:read` | List own API keys |
| `api_keys:write` | Create and revoke API keys |
| `events:read` | Read event catalog |

Scopes are additive. A key may have one or many scopes. No scope grants cross-tenant access.

### 6.3 Rate Limiting (via Kong — LOCKED)

```
FREE_TIER           = 100 requests/minute per API key
PREMIUM_TIER        = 1,000 requests/minute per API key
ENTERPRISE_TIER     = 10,000 requests/minute per API key
BURST_ALLOWANCE     = 20% above tier limit (soft cap, not hard block)
HARD_BLOCK          = 3x tier limit — 429 Too Many Requests + Retry-After header
```

Rate limit tier is derived from the tenant's active billing plan (fetched from Billing_Service). Webhook Engine caches plan tier in Redis with 60-second TTL.

---

## SECTION 7 — PLUGIN MARKETPLACE SERVICE SPECIFICATION

```
SERVICE_NAME        = plugin-marketplace-service
SEARCH_ENGINE       = OpenSearch (inherited §III)
MEDIA_STORE         = MinIO (plugin logos, documentation assets)
ANALYTICS_SINK      = ClickHouse (install analytics)
```

### 7.1 Plugin Lifecycle States

```
pending     → Submitted by vendor, awaiting admin review
approved    → Live in marketplace, installable by tenants
suspended   → Temporarily unavailable (compliance/bug)
deprecated  → Sunset, existing installations notified
```

State transitions require Platform Admin authorization (OPA policy enforcement). No state may be skipped.

### 7.2 Plugin Onboarding (Vendor Flow)

```
Step 1: Vendor submits plugin via Developer Portal form
Step 2: Admin_Governance_Service receives review request event
Step 3: Admin reviews: docs, oauth flow, webhook event declarations
Step 4: If approved → status = 'approved', plugin indexed in OpenSearch
Step 5: Tenant admins can discover and install via marketplace
```

### 7.3 Plugin Installation Flow (Tenant Flow)

```
Step 1: Tenant Admin browses marketplace → selects plugin
Step 2: Plugin installation creates webhook_subscription record
Step 3: Plugin config (OAuth tokens / API keys) stored encrypted in config_json
Step 4: tenant_plugin_installation record created (status = 'active')
Step 5: Kafka event: plugin.installed → notifications sent
```

### 7.4 Plugin Search API (OpenSearch-backed)

```
INDEX               = ecoskiller_plugins
SEARCHABLE_FIELDS   = name, description, vendor_name, category, webhook_events
FILTERS             = category, tier, status, oauth_supported, rating_avg
SORT_OPTIONS        = install_count, rating_avg, created_at
PAGINATION          = cursor-based (no offset for large catalogs)
```

---

## SECTION 8 — API CONTRACT REGISTRY (WEBHOOK + MARKETPLACE APIS)

All APIs are versioned as `v1`. Changes follow R19 (API Versioning & Deprecation Law). Breaking changes require a version bump to `v2` and a minimum 90-day parallel support period.

### 8.1 Webhook Subscription APIs

```
POST    /api/v1/webhooks/subscriptions          → Create subscription
GET     /api/v1/webhooks/subscriptions          → List subscriptions (tenant-scoped)
GET     /api/v1/webhooks/subscriptions/{id}     → Get single subscription
PATCH   /api/v1/webhooks/subscriptions/{id}     → Update (pause/resume/change URL)
DELETE  /api/v1/webhooks/subscriptions/{id}     → Soft-delete subscription

POST    /api/v1/webhooks/subscriptions/{id}/test → Send test payload
GET     /api/v1/webhooks/subscriptions/{id}/logs → Delivery log (paginated)
POST    /api/v1/webhooks/subscriptions/{id}/retry/{delivery_id} → Manual retry
```

### 8.2 Event Catalog API

```
GET     /api/v1/webhooks/events                 → List all available event keys
GET     /api/v1/webhooks/events/{event_key}     → Event schema + example payload
```

### 8.3 External API Key APIs

```
POST    /api/v1/developer/api-keys              → Create key (returns full key once)
GET     /api/v1/developer/api-keys              → List keys (prefix only, no secrets)
DELETE  /api/v1/developer/api-keys/{id}         → Revoke key
```

### 8.4 Plugin Marketplace APIs

```
GET     /api/v1/marketplace/plugins             → Search/list plugins (public, OpenSearch-backed)
GET     /api/v1/marketplace/plugins/{slug}      → Plugin detail page
POST    /api/v1/marketplace/plugins/{slug}/install  → Install plugin to tenant
DELETE  /api/v1/marketplace/plugins/{slug}/install  → Uninstall plugin
GET     /api/v1/marketplace/plugins/{slug}/reviews  → Plugin reviews
POST    /api/v1/marketplace/plugins/{slug}/reviews  → Submit review

// Admin-only (OPA enforced)
POST    /api/v1/admin/marketplace/plugins       → Submit plugin for review
PATCH   /api/v1/admin/marketplace/plugins/{id}/approve → Approve plugin
PATCH   /api/v1/admin/marketplace/plugins/{id}/suspend  → Suspend plugin
```

### 8.5 API Response Envelope (ALL RESPONSES)

```json
{
  "success": true,
  "data": {},
  "meta": {
    "request_id": "uuid-v4",
    "timestamp_ms": 1700000000000,
    "api_version": "v1"
  },
  "error": null
}
```

Error shape (on failure):

```json
{
  "success": false,
  "data": null,
  "meta": { "request_id": "...", "timestamp_ms": 0, "api_version": "v1" },
  "error": {
    "code": "WEBHOOK_ENDPOINT_UNREACHABLE",
    "message": "Human-readable message",
    "details": {}
  }
}
```

---

## SECTION 9 — KAFKA EVENT SCHEMA (PRODUCED BY THIS AGENT)

The Webhook Engine produces the following events to Kafka. These are published to the shared event bus and may be consumed by other services:

| Event Key | Trigger | Consumers |
|---|---|---|
| `webhook.delivery.success` | Successful HTTP delivery | Analytics_Service |
| `webhook.delivery.failed` | Failed HTTP delivery attempt | Notification_Service, Analytics_Service |
| `webhook.subscription.suspended` | Failure threshold exceeded | Notification_Service, Admin_Governance |
| `webhook.subscription.created` | New subscription | Analytics_Service |
| `webhook.subscription.deleted` | Subscription removed | Analytics_Service |
| `plugin.installed` | Tenant installs plugin | Notification_Service, Analytics_Service |
| `plugin.uninstalled` | Tenant removes plugin | Analytics_Service |
| `plugin.approved` | Admin approves plugin | Notification_Service (vendor) |
| `plugin.suspended` | Admin suspends plugin | Notification_Service (affected tenants) |
| `api_key.created` | New API key issued | Audit_Service |
| `api_key.revoked` | API key revoked | Audit_Service |

---

## SECTION 10 — SECURITY & COMPLIANCE RULES (LOCKED)

### 10.1 Endpoint Security

- All subscriber endpoints MUST be HTTPS. HTTP endpoints = REGISTRATION REJECTED
- Endpoint URL is validated at subscription time via a dry-run GET request (200 expected)
- IP allowlisting is supported per subscription (optional, tenant-configured)
- No webhook delivery to private/loopback IPs: `127.x.x.x`, `10.x.x.x`, `192.168.x.x`, `172.16-31.x.x` = BLOCKED (SSRF protection)

### 10.2 Secret Management (LOCKED)

- Signing secrets are generated server-side (256-bit random) at subscription creation
- Secrets are stored exclusively in HashiCorp Vault. PostgreSQL stores only a bcrypt hash for rotation validation
- Secrets are rotatable via `PATCH /api/v1/webhooks/subscriptions/{id}` — old secret valid for 24h post-rotation (grace window)
- No secret is ever returned in API responses after initial creation

### 10.3 Payload Security

- All payloads are sanitized — no raw user input included without escaping
- PII fields follow the platform-wide encryption-at-rest policy
- Payload size cap: 256KB per delivery. Oversized payloads = event dropped + logged

### 10.4 Audit Requirements (IMMUTABLE)

- Every delivery attempt is logged in `webhook_delivery_log` (immutable trigger enforced §4.2)
- API key creation and revocation are written to the platform audit log
- Plugin installations and uninstallations are audit-logged
- All logs carry `tenant_id`, `actor_id`, and `correlation_id`

### 10.5 OPA Policy Gates (MANDATORY)

The following OPA policies MUST be authored and registered:

```
webhook_create_policy:
  - Role must be tenant_admin OR integration_manager
  - Subscription quota per tenant must not be exceeded (default: 20)
  - Event keys must be valid (match catalog §3.1)

api_key_create_policy:
  - Role must be tenant_admin OR developer
  - Key count per user must not exceed 10

plugin_install_policy:
  - Role must be tenant_admin
  - Plugin status must be 'approved'
  - Plugin tier must be compatible with tenant billing plan

plugin_approve_policy:
  - Role must be platform_admin
  - Plugin status must be 'pending'
```

---

## SECTION 11 — ANALYTICS SCHEMA (CLICKHOUSE)

```sql
CREATE TABLE ecoskiller_analytics.webhook_delivery_metrics (
  tenant_id           UUID,
  subscription_id     UUID,
  event_key           String,
  delivery_date       Date,
  delivery_hour       UInt8,
  attempt_number      UInt8,
  delivery_status     Enum('success', 'failed', 'dead_letter'),
  http_status_code    Nullable(UInt16),
  duration_ms         Nullable(UInt32),
  payload_size_bytes  UInt32,
  created_at          DateTime
) ENGINE = MergeTree()
ORDER BY (tenant_id, delivery_date, delivery_hour, event_key);

CREATE TABLE ecoskiller_analytics.plugin_install_metrics (
  plugin_id           UUID,
  tenant_id           UUID,
  action              Enum('install', 'uninstall', 'pause', 'resume'),
  plan_tier           String,
  event_date          Date,
  created_at          DateTime
) ENGINE = MergeTree()
ORDER BY (plugin_id, event_date);
```

---

## SECTION 12 — UI SCREENS (DEVELOPER PORTAL)

**BINDING LAW:** All UI is Flutter-primary, Next.js (SEO read-only clone) is secondary. WCAG 2.1 AA mandatory. Dashboard composition: 40% fixed, 60% customizable (inherited).

### 12.1 Screen Inventory

| Screen ID | Screen Name | Target Role | Module | Stage |
|---|---|---|---|---|
| DEV-001 | Developer Portal Home | tenant_admin, developer | Integration_Hub_UI | STAGE_3 |
| DEV-002 | Webhook Subscription List | tenant_admin, developer | Integration_Hub_UI | STAGE_3 |
| DEV-003 | Create Webhook Subscription | tenant_admin | Integration_Hub_UI | STAGE_3 |
| DEV-004 | Webhook Subscription Detail | tenant_admin, developer | Integration_Hub_UI | STAGE_3 |
| DEV-005 | Delivery Log Explorer | tenant_admin, developer | Integration_Hub_UI | STAGE_3 |
| DEV-006 | API Keys Manager | tenant_admin, developer | Integration_Hub_UI | STAGE_3 |
| DEV-007 | Create API Key | tenant_admin, developer | Integration_Hub_UI | STAGE_3 |
| DEV-008 | Event Catalog Browser | tenant_admin, developer | Integration_Hub_UI | STAGE_3 |
| DEV-009 | Plugin Marketplace | tenant_admin | Integration_Hub_UI | STAGE_3 |
| DEV-010 | Plugin Detail Page | tenant_admin | Integration_Hub_UI | STAGE_3 |
| DEV-011 | Plugin Installation Config | tenant_admin | Integration_Hub_UI | STAGE_3 |
| DEV-012 | Installed Plugins List | tenant_admin | Integration_Hub_UI | STAGE_3 |
| DEV-013 | Webhook Analytics Dashboard | tenant_admin | Integration_Hub_UI | STAGE_3 |
| DEV-014 | Admin Plugin Review Panel | platform_admin | ERP_UI | STAGE_3 |
| DEV-015 | Webhook Subscription Edit | tenant_admin | Integration_Hub_UI | STAGE_3 |

**Max 15 screens per run. If this run targets this module, execute all 15. Partial output = INVALID.**

### 12.2 Screen Output Requirements (Per Screen, R29 Law Inherited)

Each screen generation MUST produce:
1. Screen purpose definition
2. Entry and exit navigation points
3. Wireframe layout (header / body / actions / footer)
4. Flutter widget tree (full, not skeleton)
5. Navigation route name + role access rule (OPA-enforced)
6. State management method (Riverpod — platform standard)
7. API endpoints consumed
8. Loading / empty / error / success UI states (all 4 mandatory)
9. Accessibility annotations (WCAG 2.1 AA per R20)
10. Animation rules (250–300ms page transitions, 100ms button feedback)
11. Design token references (primary #1E3A8A, accent #10B981, from sealed token set)
12. Next.js component mirror specification

### 12.3 Screen Rules (Design System Lock)

- No screen may have more than 3 primary actions visible simultaneously
- API keys are shown as `esk_XXXXXXXX...` (prefix + masked remainder) after creation
- Full API key shown in a one-time copy modal with 30-second auto-dismiss
- Webhook delivery log is paginated (20 rows per page), filterable by status and event key
- Plugin marketplace cards display: logo, name, category badge, rating, install count, tier badge
- All destructive actions (delete subscription, revoke key, uninstall plugin) require a two-step confirmation dialog

---

## SECTION 13 — OBSERVABILITY SPECIFICATION (MANDATORY)

### 13.1 Prometheus Metrics (Webhook Engine)

```
webhook_deliveries_total{tenant_id, event_key, status}     Counter
webhook_delivery_duration_ms{tenant_id, event_key}         Histogram
webhook_delivery_retry_count{subscription_id}              Gauge
webhook_subscriptions_active{tenant_id}                    Gauge
webhook_subscriptions_suspended{tenant_id}                 Gauge
webhook_dead_letter_total{tenant_id}                       Counter
plugin_installations_total{plugin_id, action}              Counter
api_key_requests_total{tenant_id, scope, status}           Counter
```

### 13.2 Grafana Dashboard Panels (Mandatory)

1. Webhook delivery success rate (%) — per tenant, per event key, time-series
2. Average delivery latency (ms) — P50, P95, P99
3. Failure rate by event key — heatmap
4. Suspended subscription count — current value gauge
5. Dead-letter volume — daily bar chart
6. API key request volume — by scope and status
7. Plugin installation trend — weekly
8. Delivery attempt distribution — by attempt number (1–5)

### 13.3 Alerting Rules

```yaml
- alert: WebhookDeliveryFailureSpike
  condition: webhook_delivery_failure_rate > 20% for 5m
  severity: warning

- alert: WebhookDeadLetterAccumulating
  condition: webhook_dead_letter_total increases by > 10 in 1h
  severity: critical

- alert: SubscriptionSuspended
  condition: webhook.subscription.suspended event received
  severity: warning

- alert: APIKeyRateLimitBreached
  condition: 429 response rate > 5% for any api_key in 2m
  severity: info
```

---

## SECTION 14 — DEVOPS & DEPLOYMENT SPECIFICATION

### 14.1 Kubernetes Namespace Assignment

```
NAMESPACE           = ecoskiller-core
DEPLOYMENT_NAME     = webhook-engine
REPLICA_MIN         = 2
REPLICA_MAX         = 10
HPA_METRIC          = CPU (target 60%) + Kafka consumer lag
RESOURCE_REQUEST    = cpu: 250m, memory: 256Mi
RESOURCE_LIMIT      = cpu: 1000m, memory: 1Gi
LIVENESS_PROBE      = GET /health → 200 within 5s
READINESS_PROBE     = GET /ready → 200 within 3s
```

### 14.2 Environment Variables (No Hardcoded Values)

```
DATABASE_URL                      → PostgreSQL connection string (Vault)
REDIS_URL                         → Redis connection string (Vault)
KAFKA_BROKERS                     → Kafka broker list (ENV)
VAULT_ADDR                        → HashiCorp Vault address (ENV)
VAULT_TOKEN                       → Vault token (mounted via Kubernetes secret)
CLICKHOUSE_URL                    → ClickHouse connection (Vault)
OPENSEARCH_URL                    → OpenSearch connection (ENV)
WEBHOOK_DELIVERY_TIMEOUT_MS       → Default 10000 (ENV)
WEBHOOK_MAX_RETRIES               → Default 5 (ENV)
WEBHOOK_SUSPENDED_THRESHOLD       → Default 50 (ENV)
RATE_LIMIT_CACHE_TTL_SECONDS      → Default 60 (ENV)
```

### 14.3 CI/CD Pipeline Stages (GitHub Actions / GitLab CI)

```
Stage 1: Contract Validation — OPA policy lint, API schema validation
Stage 2: Unit Tests — Jest / JUnit (min 80% coverage, enforced)
Stage 3: Integration Tests — PostgreSQL + Redis + Kafka (dockerized)
Stage 4: Security Scan — OWASP dependency check, container scan
Stage 5: Docker Build — multi-stage, distroless base image
Stage 6: Image Push — to container registry (tagged: commit SHA + semver)
Stage 7: Helm Deploy — to staging namespace
Stage 8: Smoke Tests — delivery test via test-mode endpoint
Stage 9: Production Promote — blue/green via Helm, automatic rollback on failure
```

---

## SECTION 15 — INTERN-EXECUTABLE STEP-BY-STEP (R26 COMPLIANCE)

All code artifacts generated under this agent MUST include:

1. File path and filename
2. Exact code (no pseudocode)
3. Inline comment explaining each section (// or # comments)
4. Dependency list (what other service/table this file connects to)
5. Command to run the file (e.g., `npm run start`, `mvn spring-boot:run`)
6. Expected success output (exact log line or HTTP response)

**Example structure expected:**

```
// FILE: src/consumers/kafka-event-consumer.ts
// PURPOSE: Consumes Kafka events and routes to webhook delivery workers
// CONNECTS TO: webhook_subscriptions table, Redis delivery queue, Vault signing service
// RUN: npm run start:consumer
// EXPECTED OUTPUT: [INFO] Kafka consumer ready. Subscribed to 25 topics.
```

---

## SECTION 16 — FAILURE REGISTRY INTEGRATION (R38 COMPLIANCE)

The following minimum failure cases MUST be recorded in the Master Bug & Failure Registry for this agent's scope:

| Bug ID Range | Layer | Area |
|---|---|---|
| WH-001 – WH-050 | Webhook Engine | Delivery failures, SSRF bypass attempts, signature mismatch |
| WH-051 – WH-100 | Plugin Marketplace | Installation conflicts, version mismatches, OPA bypass |
| WH-101 – WH-150 | API Key Service | Key leakage, scope bypass, rate limit evasion |
| WH-151 – WH-200 | Developer Portal UI | Accessibility failures, state leaks, cross-tenant UI exposure |

Minimum 200 distinct bug cases for this agent's scope. Absence = STOP EXECUTION.

---

## SECTION 17 — PRODUCTION READINESS GATES (AGENT-SPECIFIC)

Before this agent's module may be declared production-ready, all of the following gates must pass:

```
☐ All 8 database tables created with RLS policies
☐ Webhook Engine microservice deployed and consuming Kafka
☐ All 26 event types from §3.1 verified in end-to-end test
☐ HMAC signature verified by test subscriber
☐ SSRF block tested (loopback, RFC1918 addresses blocked)
☐ Retry policy tested (5 attempts, exponential backoff confirmed)
☐ Dead-letter suspension triggered in staging (50 failure test)
☐ All 9 API endpoint groups returning correct response envelopes
☐ OPA policies registered and enforced for all 4 policy gates
☐ Plugin lifecycle (pending → approved → installed → uninstalled) tested
☐ API key full lifecycle (create → use → rotate → revoke) tested
☐ All 8 Prometheus metrics emitting in Prometheus
☐ All 8 Grafana panels rendering with live data
☐ All 3 alert rules firing correctly in test
☐ Delivery log immutability trigger tested (UPDATE rejected)
☐ All 15 Developer Portal screens generated (Flutter + Next.js)
☐ WCAG 2.1 AA accessibility audit passed on all 15 screens
☐ ClickHouse analytics tables populated with delivery metrics
☐ Rate limiting enforced per tier via Kong
☐ CI/CD pipeline passing all 9 stages

ALL GATES MUST PASS → Only then: STATUS = PRODUCTION_READY
ANY GATE FAILED → STOP EXECUTION → REPORT FAILED GATE
```

---

## SECTION 18 — ANTIGRAVITY RUN COMMAND

To invoke this agent in Antigravity, use the following command format:

```
GENERATE_WEBHOOK_MARKETPLACE_MODULE

TENANT_ID         = {UUID}
TARGET_ROLE       = {tenant_admin | developer | platform_admin}
SCREEN_SET        = {DEV-001 through DEV-015 | ALL}
STAGE             = STAGE_3
ENTITY_STATE      = {subscription_status | plugin_status | api_key_status}
OUTPUT_FORMAT     = {FLUTTER_CODE | API_SPEC | DB_MIGRATION | ALL}
```

**Rules:**
- `MAX_SCREENS_PER_RUN = 15` (inherited from UI Constitution)
- `MAX_MODULES_PER_RUN = 1` (this module only per execution)
- `MAX_ROLES_PER_RUN = 1`
- Exceeding limits → STOP EXECUTION

---

## 🔒 FINAL SEAL

```
WEBHOOK_MARKETPLACE_AGENT.md

STATUS              = SEALED
VERSION             = 1.0.0
CHANGE_POLICY       = APPEND_ONLY
MUTATION_POLICY     = ADD_ONLY (version bump required for any addition)
CREATIVE_DEVIATION  = PERMANENTLY FORBIDDEN
ANTIGRAVITY_CONFUSION = IMPOSSIBLE

INHERITS_FROM:
  ✔ ECOSKILLER_MASTER_EXECUTION_PROMPT_v12.0
  ✔ SEALED_UI_CONSTITUTION
  ✔ R19 (API Versioning)
  ✔ R20 (Accessibility)
  ✔ R26 (Intern Execution)
  ✔ R28 (Intelligence Cost)
  ✔ R29 (UI Generation)
  ✔ R31 (Dual Frontend)
  ✔ R38 (Bug Registry)
  ✔ R65 (Open Extensibility)

PLATFORM            = ECOSKILLER
EXECUTION_ENGINE    = ANTIGRAVITY
AGENT               = WebhookMarketplaceAgent
AUTHOR_AUTHORITY    = HUMAN DECLARATION ONLY

ANY DEVIATION FROM THIS DOCUMENT
= STOP EXECUTION
= REPORT DEVIATION CLASS + SECTION VIOLATED
= NO PARTIAL OUTPUT PERMITTED
```

---

*End of WEBHOOK_MARKETPLACE_AGENT.md — Document is sealed and locked.*
