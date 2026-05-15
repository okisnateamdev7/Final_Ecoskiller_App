# 🔐 STORAGE_CONNECT_AGENT.md
## ECOSKILLER PLATFORM — STORAGE & CONNECTIVITY AGENT CONSTITUTION
### Execution Target: Google Antigravity Tool
### Status: LOCKED · SEALED · DETERMINISTIC · ANTIGRAVITY-SAFE

---

```
AGENT_ID              = STORAGE_CONNECT_AGENT
AGENT_CLASS           = INFRASTRUCTURE_CONSTITUTION
EXECUTION_ENGINE      = ANTIGRAVITY
MUTATION_POLICY       = ADD_ONLY
CREATIVE_INTERPRETATION = FORBIDDEN
ASSUMPTION_FILLING    = FORBIDDEN
DEFAULT_BEHAVIOR      = DENY
FAILURE_MODE          = STOP_EXECUTION
DUPLICATION           = FORBIDDEN
CONFLICT              = DENY
SCOPE                 = STORAGE · DATA_PERSISTENCE · CONNECTIVITY · CACHING · SEARCH · MEDIA
COMPLIANCE_MODE       = ENABLED
ANTIGRAVITY_COMPAT    = TRUE
VERSION               = 1.0.0
```

---

## ⚠️ ABSOLUTE ENFORCEMENT PREAMBLE

This agent constitutes the **complete, locked, and sealed storage and connectivity layer** for the ECOSKILLER multi-tenant enterprise SaaS platform (including the DOJO engine).

Antigravity MUST NOT:
- Reinterpret any storage boundary
- Merge tenant data stores
- Auto-select storage engines beyond this document
- Apply creative schema decisions
- Skip any section

Any undefined storage scenario → **STOP EXECUTION → REPORT STORAGE GAP → REQUEST HUMAN DECISION**

---

## SECTION S0 — STORAGE ARCHITECTURE PHILOSOPHY (IMMUTABLE)

```
STORAGE_PARADIGM     = POLYGLOT_PERSISTENCE
TENANT_ISOLATION     = HARD (schema-level minimum, cluster-level preferred)
DOMAIN_ISOLATION     = HARD (arts | commerce | science | cross)
DATA_SOVEREIGNTY     = ENFORCED_PER_JURISDICTION
DEFAULT_POSTURE      = DENY_ALL_ACCESS
ENCRYPTION_AT_REST   = MANDATORY
ENCRYPTION_IN_TRANSIT = MANDATORY (TLS 1.2+ minimum)
IMMUTABILITY_LAYER   = AUDIT_LOGS + COMPLIANCE_RECORDS
BACKUP_POLICY        = AUTOMATED_SCHEDULED
RECOVERY_POLICY      = DEFINED_RPO_RTO_PER_TIER
```

No storage component may be shared across tenants unless explicitly declared.
No storage decision is implicit. Every engine choice must map to a declared data class.

---

## SECTION S1 — STORAGE ENGINE REGISTRY (LOCKED)

The following are the **only approved storage engines** for this platform.

### S1.A — PRIMARY RELATIONAL DATABASE

```
ENGINE              = PostgreSQL (v15+)
PURPOSE             = Transactional data, entity state, financial records, audit trails,
                      user profiles, job postings, applications, skills, projects,
                      certifications, billing, ERP records, compliance records
DEPLOYMENT_MODEL    = Multi-tenant with row-level security (RLS) enforced
ISOLATION_LEVEL     = Schema-per-tenant (minimum) | Cluster-per-tenant (enterprise tier)
ENCRYPTION_AT_REST  = AES-256 (managed key, vault-rotated)
REPLICATION         = Synchronous primary → replica (minimum 1 read replica per region)
BACKUP_FREQUENCY    = Daily full + Hourly WAL streaming
RPO                 = ≤ 1 hour
RTO                 = ≤ 4 hours
CONNECTION_POOL     = PgBouncer (transaction mode, tenant-scoped limits)
MIGRATIONS          = Flyway or Liquibase (versioned, backward-compatible only)
INDEX_POLICY        = Explicit, reviewed per query profile
QUERY_TIMEOUT       = 30s (standard) | 5s (auth paths)
```

**Hard Rules:**
- No cross-tenant queries without explicit join guard
- No unindexed queries on user-facing paths
- No raw SQL from frontend or AI systems
- Sensitive columns (`password_hash`, `pii_fields`, `financial_data`) must use column-level encryption
- Destructive migrations require dual approval + rollback script

---

### S1.B — CACHE LAYER

```
ENGINE              = Redis (v7+) | Redis Cluster (production)
PURPOSE             = Session tokens, rate limit counters, feature flags, 
                      real-time match state (Dojo), OTP codes, ephemeral UI state,
                      leaderboard rankings, pub/sub for live notifications
DEPLOYMENT_MODEL    = Tenant-scoped keyspace prefixes (mandatory)
ISOLATION_LEVEL     = Keyspace prefix isolation: {tenant_id}:{domain}:{key}
ENCRYPTION_AT_REST  = Enabled (managed encryption)
ENCRYPTION_IN_TRANSIT = TLS mandatory
TTL_POLICY          = ALL keys must have explicit TTL
SESSION_TTL         = 24h (standard) | 1h (privileged roles)
OTP_TTL             = 5 minutes (hard expiry, no extension)
RATE_LIMIT_TTL      = Sliding window per endpoint definition
PERSISTENCE_MODE    = RDB + AOF (critical data) | No persistence (ephemeral only)
EVICTION_POLICY     = allkeys-lru (standard cache) | noeviction (session store)
```

**Hard Rules:**
- No PII stored in Redis
- No cross-tenant key access (prefix enforcement mandatory)
- No hardcoded keys in application code
- OTP and session stores must be on isolated Redis logical databases
- Dojo real-time state must be in separate Redis instance from user session store

---

### S1.C — SEARCH ENGINE

```
ENGINE              = Elasticsearch (v8+)
PURPOSE             = Job search, skill search, user discovery, project search,
                      full-text content indexing, audit log querying,
                      SEO-serving read models (Next.js layer)
DEPLOYMENT_MODEL    = Tenant-isolated indices: ecoskiller_{tenant_id}_{domain}_{entity}
ISOLATION_LEVEL     = Index-per-tenant (minimum) | Cluster-per-domain (enterprise)
ENCRYPTION_AT_REST  = Enabled
ENCRYPTION_IN_TRANSIT = TLS
REPLICATION         = Minimum 1 replica shard per index
INDEX_GOVERNANCE    = Schema mapping locked per index; no dynamic mapping in production
ALIAS_STRATEGY      = Aliases for zero-downtime re-indexing
RETENTION           = Hot: 90 days | Warm: 1 year | Cold: archived (ILM enforced)
SEO_SCOPE           = Read-only React (Next.js) layer only; Flutter apps excluded from indexing
```

**Hard Rules:**
- No cross-tenant index queries
- No raw user input passed to Elasticsearch without sanitization
- Index lifecycle management (ILM) policies mandatory
- No dynamic field mapping enabled in production
- Flutter app routes must NOT be indexed; only `/` React routes are indexable

---

### S1.D — OBJECT / BLOB STORAGE

```
ENGINE              = S3-compatible (AWS S3 | MinIO for on-prem)
PURPOSE             = Uploaded resumes, profile pictures, project files, 
                      certificates, training materials, Dojo session recordings,
                      exported reports, audit evidence attachments, media assets
DEPLOYMENT_MODEL    = Bucket-per-tenant OR prefix-per-tenant (minimum)
ISOLATION_LEVEL     = Strict IAM policy per tenant bucket/prefix
ENCRYPTION_AT_REST  = SSE-S3 or SSE-KMS (tenant-specific keys preferred)
ENCRYPTION_IN_TRANSIT = HTTPS only (no HTTP)
ACCESS_PATTERN      = Presigned URLs only (no public bucket exposure)
PRESIGNED_URL_TTL   = 15 minutes (standard) | 5 minutes (sensitive documents)
VIRUS_SCANNING      = Mandatory on every upload (ClamAV or equivalent)
FILE_TYPE_ALLOWLIST = PDF, DOCX, PNG, JPG, MP4, WEBM, ZIP (declared per use case)
MAX_FILE_SIZE       = Declared per upload type (e.g., 10MB resume, 500MB video)
RETENTION_POLICY    = Lifecycle rules per content class (aligned with legal retention)
BACKUP              = Cross-region replication enabled
```

**Hard Rules:**
- No public read access on any bucket
- All URLs generated server-side via presigned mechanism
- Virus scan must complete before file is accessible to the application
- Dojo session recordings stored in isolated, access-controlled prefix
- Minor user uploads (parental consent required) stored in flagged prefix with enhanced access controls

---

### S1.E — MESSAGE BROKER / EVENT STREAMING

```
ENGINE              = Apache Kafka (v3+) | Confluent Platform (managed option)
PURPOSE             = Event-driven microservice communication, async job dispatch,
                      analytics ingestion, notification delivery, 
                      gamification event pipeline, audit event streaming,
                      Dojo match lifecycle events, AI job queue
DEPLOYMENT_MODEL    = Topic-per-domain isolation: {domain}.{service}.{event_type}
ISOLATION_LEVEL     = Topic ACL per tenant + domain (mandatory)
ENCRYPTION_IN_TRANSIT = TLS
ENCRYPTION_AT_REST  = Enabled
REPLICATION_FACTOR  = 3 (minimum production)
MIN_IN_SYNC_REPLICAS = 2
RETENTION_POLICY    = 7 days (default) | 30 days (compliance events) | 1 year (audit stream)
DLQ_STRATEGY        = Dead Letter Queue per critical topic (mandatory)
CONSUMER_GROUPS     = Tenant-scoped consumer group IDs
SCHEMA_REGISTRY     = Confluent Schema Registry or Apicurio (mandatory; all events versioned)
```

**Hard Rules:**
- No cross-tenant topic sharing without explicit routing guard
- Event schema changes must be backward-compatible or produce new topic version
- Dead Letter Queues must be monitored with alerting
- No direct database writes from Kafka consumers without idempotency guard
- Auth events, payment events, and audit events are HIGHEST PRIORITY (separate partitions)

---

### S1.F — DOCUMENT / NOSQL STORE (CONDITIONAL USE)

```
ENGINE              = MongoDB (v6+) | optional per module
PURPOSE             = Semi-structured data: AI model outputs, conversation transcripts,
                      dynamic ERP configurations, flexible portfolio data,
                      content management (blog, scenarios, rubrics)
DEPLOYMENT_MODEL    = Collection-per-tenant or tenant_id field on all documents
ISOLATION_LEVEL     = Mandatory tenant_id filter on ALL queries (enforced at ORM layer)
ENCRYPTION_AT_REST  = Encrypted storage engine
ENCRYPTION_IN_TRANSIT = TLS
BACKUP              = Daily automated backup
ALLOWED_MODULES     = Dojo scenario content | AI output logs | Portfolio items | CMS data
FORBIDDEN_MODULES   = Financial data | Auth data | Audit trails | Compliance records
```

**Hard Rules:**
- Financial, auth, and compliance data MUST NOT use MongoDB
- tenant_id must be indexed and mandatory on all collections
- No unvalidated document writes from external input
- Schema validation (JSON Schema) enforced at collection level

---

### S1.G — TIME-SERIES / OBSERVABILITY STORE

```
ENGINE              = Prometheus (metrics) + InfluxDB or VictoriaMetrics (long-term)
PURPOSE             = Platform health metrics, performance counters, SLO tracking,
                      rate limit counters, AI token burn metrics, cost dashboards
DEPLOYMENT_MODEL    = Global (single cluster, tenant-tagged metrics)
TENANT_TAGGING      = Mandatory label: tenant_id + domain on all metrics
RETENTION           = 90 days hot (Prometheus) | 2 years cold (long-term store)
ALERTING            = AlertManager → PagerDuty / OpsGenie integration
DASHBOARDS          = Grafana (role-based access; public status page = read-only subset)
```

**Hard Rules:**
- No PII in metric labels or metric names
- Public Grafana dashboard (`status.ecoskiller.com`) exposes ONLY aggregated uptime and response time
- AI inference cost metrics must be tracked per-tenant and per-model
- Government SLA metrics tracked separately with guaranteed availability

---

### S1.H — AUDIT & IMMUTABLE LOG STORE

```
ENGINE              = Append-only PostgreSQL table (primary) + 
                      WORM-compatible object store (archival)
PURPOSE             = All system audit trails: user actions, admin actions, 
                      permission changes, financial transactions, 
                      API access events, data subject rights requests,
                      security events, deployment logs
ISOLATION_LEVEL     = Tenant-isolated schema; domain-tagged records
IMMUTABILITY        = INSERT-only (no UPDATE, no DELETE permitted)
HASH_CHAINING       = SHA-256 hash chain per audit record (tamper detection)
MERKLE_VERIFICATION = Enabled for compliance-critical audit streams
ENCRYPTION_AT_REST  = AES-256 mandatory
RETENTION           = 7–10 years (minimum, per jurisdiction)
ARCHIVAL            = WORM object storage after 90 days active window
LEGAL_HOLD          = Override flag blocks destruction; requires approval to release
```

**Hard Rules:**
- No UPDATE or DELETE on audit tables under any circumstance
- Legal hold flag must be checked before any retention-expiry deletion
- Audit records must include: actor_id, tenant_id, domain, action, resource_id, timestamp, result, correlation_id
- Tamper detection must run on scheduled basis and alert on mismatch

---

### S1.I — SECRETS MANAGEMENT

```
ENGINE              = HashiCorp Vault | AWS Secrets Manager | GCP Secret Manager
PURPOSE             = Database credentials, API keys, JWT signing keys, 
                      encryption keys, OAuth secrets, third-party vendor credentials
ROTATION_POLICY     = Automatic rotation (database creds: 30 days | API keys: 90 days)
ACCESS_CONTROL      = Role-based, service-identity bound (no human access to prod secrets)
AUDIT               = Every secret read/write logged to audit store
ENVIRONMENT_ISOLATION = Separate vault namespaces for dev | staging | prod
```

**Hard Rules:**
- No hardcoded secrets in code, config files, or environment variables checked into source control
- No shared secrets across environments
- No secrets accessible from frontend or client-side code
- Emergency break-glass access requires dual approval and full audit trail

---

## SECTION S2 — DATA CLASSIFICATION & STORAGE ROUTING MATRIX (LOCKED)

Every data class must route to its designated storage engine.
Routing deviations require explicit governance approval.

```
DATA CLASS                        STORAGE ENGINE          ENCRYPTION    TENANT_ISOLATION
─────────────────────────────────────────────────────────────────────────────────────────
User Identity & Auth              PostgreSQL              AES-256       Schema-level
User Profile & PII                PostgreSQL              Column-level  Schema-level
Job Postings & Applications       PostgreSQL              AES-256       Schema-level
Skill Records & Certifications    PostgreSQL              AES-256       Schema-level
Financial & Billing Records       PostgreSQL              Column-level  Schema-level
ERP Records                       PostgreSQL              AES-256       Schema-level
Audit Trails & Compliance         PostgreSQL (append-only) AES-256+Hash Schema-level
AI Match Scores & Rankings        PostgreSQL (results)    AES-256       Schema-level
Session Tokens                    Redis (isolated DB)     TLS+keyspace  Keyspace prefix
Rate Limit State                  Redis                   TLS+keyspace  Keyspace prefix
OTP Codes                         Redis (isolated DB)     TLS+keyspace  Keyspace prefix
Dojo Real-Time Match State        Redis (dedicated)       TLS+keyspace  Keyspace prefix
Leaderboard / Rankings (live)     Redis                   TLS+keyspace  Keyspace prefix
Job & Skill Search Index          Elasticsearch           TLS+index     Index per tenant
User Discovery Index              Elasticsearch           TLS+index     Index per tenant
Full-text Content Index           Elasticsearch           TLS+index     Index per tenant
Audit Log Search                  Elasticsearch (read)    TLS+index     Index per tenant
Uploaded Files (all types)        S3-compatible           SSE-KMS       Bucket/prefix
Dojo Session Recordings           S3-compatible (isolated) SSE-KMS      Isolated bucket
Certificates & Evidence           S3-compatible           SSE-KMS       Bucket/prefix
Exported Reports                  S3-compatible           SSE-KMS       Bucket/prefix
Platform Events / Async Jobs      Kafka                   TLS+ACL       Topic per domain
Gamification Events               Kafka                   TLS+ACL       Topic per domain
Analytics Ingestion Stream        Kafka                   TLS+ACL       Topic per domain
AI Output Logs                    MongoDB (if applicable) TLS+field     tenant_id field
Dojo Scenario Content             MongoDB (CMS)           TLS+field     tenant_id field
Portfolio Items (flexible)        MongoDB (optional)      TLS+field     tenant_id field
Performance Metrics               Prometheus + Grafana    TLS           Labeled by tenant
Cost & SLO Metrics                Prometheus + long-term  TLS           Labeled by tenant
Secrets & Credentials             Vault / Secrets Manager Vault-native  Namespace per env
```

---

## SECTION S3 — TENANT ISOLATION ENFORCEMENT (HARD LOCK)

```
ISOLATION_POLICY    = HARD_ISOLATION
SHARED_STORAGE      = FORBIDDEN (unless explicitly listed)
CROSS_TENANT_QUERY  = FORBIDDEN
CROSS_TENANT_READ   = FORBIDDEN
CROSS_TENANT_WRITE  = FORBIDDEN
```

### S3.A — PostgreSQL Tenant Isolation

Every table MUST include:
```sql
tenant_id UUID NOT NULL,
domain_track VARCHAR(20) NOT NULL CHECK (domain_track IN ('arts','commerce','science','cross','system'))
```

Row-Level Security (RLS) policy MUST be enabled on all tenant-scoped tables:
```sql
-- Example RLS policy (must be present on every user-facing table)
CREATE POLICY tenant_isolation ON {table_name}
  USING (tenant_id = current_setting('app.tenant_id')::UUID);

ALTER TABLE {table_name} ENABLE ROW LEVEL SECURITY;
ALTER TABLE {table_name} FORCE ROW LEVEL SECURITY;
```

Absence of RLS on a tenant-scoped table → **STOP EXECUTION → REPORT ISOLATION FAILURE**

### S3.B — Redis Tenant Isolation

All Redis keys must follow structure:
```
{tenant_id}:{domain}:{feature}:{entity_id}:{key_name}
```

Example:
```
a1b2c3d4:science:dojo:match_789:state
a1b2c3d4:commerce:session:user_456:token
```

Cross-tenant key access = **CRITICAL SECURITY VIOLATION**

### S3.C — Elasticsearch Tenant Isolation

Index naming convention (mandatory):
```
ecoskiller_{tenant_id}_{domain}_{entity_type}
```

Examples:
```
ecoskiller_a1b2c3d4_science_jobs
ecoskiller_a1b2c3d4_commerce_users
ecoskiller_platform_global_skills
```

Query-time tenant filter is MANDATORY on every Elasticsearch query:
```json
{
  "query": {
    "bool": {
      "filter": [
        { "term": { "tenant_id": "{tenant_id}" } },
        { "term": { "domain": "{domain}" } }
      ]
    }
  }
}
```

### S3.D — Kafka Tenant Isolation

Topic naming convention (mandatory):
```
{domain}.{service_name}.{event_type}.{version}
```

Examples:
```
science.dojo-service.match_completed.v1
commerce.job-service.application_submitted.v1
arts.portfolio-service.item_published.v1
platform.auth-service.user_created.v1
```

Consumer group naming:
```
{tenant_id}.{service_name}.{consumer_purpose}
```

### S3.E — S3 Tenant Isolation

Bucket naming and prefix structure:
```
Bucket: ecoskiller-{env}-{tenant_id}
  OR
Bucket: ecoskiller-{env}-shared
  Prefix: {tenant_id}/{domain}/{content_type}/{year}/{month}/
```

IAM policy must restrict: each service role may only access its declared prefix.

---

## SECTION S4 — DOMAIN ISOLATION RULES (HARD LOCK)

```
DOMAIN_TRACKS       = arts | commerce | science | cross | system
CROSS_DOMAIN_ACCESS = FORBIDDEN unless explicitly declared as cross-domain resource
DOMAIN_LEAKAGE      = SECURITY FAILURE → STOP + ALERT
```

Storage-level domain isolation must be enforced through:

1. **tag/column**: `domain_track` column on every entity table
2. **index**: `domain` field on every Elasticsearch document
3. **prefix**: `{domain}` segment in Redis key structure
4. **topic**: `{domain}` prefix in Kafka topic names
5. **path**: `{domain}` prefix in S3 object paths

Cross-domain data flow must declare:
```
SOURCE_DOMAIN      = arts | commerce | science | system
TARGET_DOMAIN      = arts | commerce | science | system
AUTHORITY          = PLATFORM_ADMIN_APPROVED
AUDIT_REQUIRED     = TRUE
```

---

## SECTION S5 — ENCRYPTION STANDARDS (MANDATORY)

### S5.A — Encryption at Rest

```
STANDARD               = AES-256-GCM (minimum)
KEY_MANAGEMENT         = KMS (cloud-managed or Vault)
KEY_ROTATION           = Automatic, ≤ 365 days (data keys) | ≤ 90 days (auth keys)
COLUMN_ENCRYPTION      = Mandatory for: passwords (bcrypt), PII fields, financial data, 
                          card/payment tokens, national IDs, health data (if applicable)
BACKUP_ENCRYPTION      = All backups encrypted with same standard
ENCRYPTION_AUDIT       = Key usage logged to immutable audit store
```

### S5.B — Encryption in Transit

```
PROTOCOL              = TLS 1.2 minimum | TLS 1.3 preferred
CERTIFICATE_AUTHORITY = Platform-managed CA for internal services | 
                        Public CA (Let's Encrypt or commercial) for public endpoints
CERTIFICATE_ROTATION  = Automatic (cert-manager or equivalent)
MUTUAL_TLS (mTLS)     = Required for ALL service-to-service communication
PLAINTEXT_TRAFFIC     = FORBIDDEN (no HTTP, no unencrypted database connections)
HSTS                  = Enforced on all web endpoints
```

### S5.C — Key Governance

Every encryption key must have:
- Unique key identifier
- Creation timestamp
- Expiry date
- Owner service
- Purpose declaration
- Rotation schedule
- Audit log entries for all operations

---

## SECTION S6 — BACKUP & DISASTER RECOVERY (MANDATORY)

### S6.A — Backup Policy

```
POSTGRESQL_BACKUP    = Daily full backup + WAL-streaming continuous backup
REDIS_BACKUP         = RDB snapshot every 6 hours (critical data only; ephemeral excluded)
ELASTICSEARCH_BACKUP = Daily snapshot to S3 (repository plugin)
S3_BACKUP            = Cross-region replication (same-day)
MONGODB_BACKUP       = Daily automated backup
KAFKA_BACKUP         = Topic replication factor ≥ 3; separate geo-redundant cluster for compliance topics
```

### S6.B — Recovery Objectives (Per Data Tier)

```
TIER        DESCRIPTION                  RPO          RTO
────────────────────────────────────────────────────────────
CRITICAL    Auth, Financial, Audit       ≤ 15 min     ≤ 1 hour
HIGH        User profiles, Job data      ≤ 1 hour     ≤ 4 hours
STANDARD    Search indices, Analytics    ≤ 4 hours    ≤ 8 hours
ARCHIVE     Cold compliance data         ≤ 24 hours   ≤ 48 hours
```

### S6.C — Restore Drills

- Restore drill frequency: **Quarterly minimum**
- Drill must validate: data integrity, tenant isolation preservation, decryption success
- Failed restore drill → **Incident escalation → Production freeze until resolved**
- Restore results must be logged to audit store

### S6.D — Multi-Region Strategy

```
PRIMARY_REGION        = Declared per deployment (India: Mumbai/Hyderabad)
SECONDARY_REGION      = Geographically separated (India: Chennai/Pune or alternate)
FAILOVER_TRIGGER      = Automated (health check failure ≥ 3 consecutive) OR manual
FAILOVER_SCOPE        = Full failover | Domain-isolated failover | Service-specific failover
DATA_RESIDENCY        = Student data must not leave declared jurisdiction (India DPDP compliance)
GOVERNMENT_DATA       = Must remain within India (sovereign data compliance)
```

---

## SECTION S7 — CONNECTION MANAGEMENT & POOLING (LOCKED)

### S7.A — PostgreSQL Connection Pool

```
POOLER              = PgBouncer (mandatory)
POOL_MODE           = Transaction (standard) | Session (long-running ops)
MAX_POOL_SIZE       = Calculated: (available_connections / active_services) × safety_factor
TENANT_POOL_LIMIT   = Each tenant has defined max connections (no noisy-neighbor)
TIMEOUT             = Connection acquisition timeout: 5 seconds (hard)
OVERFLOW_BEHAVIOR   = Queue with timeout; reject beyond queue limit
MONITORING          = Pool utilization metrics exposed to Prometheus
```

### S7.B — Redis Connection Pool

```
CLIENT              = ioredis (Node.js) | redis-py (Python) | Lettuce (Java)
POOL_SIZE           = Configured per service (not shared across services)
TIMEOUT             = 2 seconds (read/write operations)
RETRY_POLICY        = Exponential backoff, max 3 retries, jitter applied
CLUSTER_MODE        = Enabled for production (Redis Cluster)
```

### S7.C — Elasticsearch Client

```
CLIENT              = Official Elasticsearch client per language
POOL_SIZE           = Configured per service
TIMEOUT             = 10 seconds (search) | 30 seconds (indexing batch)
RETRY_ON_TIMEOUT    = true (max 2 retries)
SNIFFING            = Disabled (use static node list behind load balancer)
```

---

## SECTION S8 — DATA LIFECYCLE GOVERNANCE (LOCKED)

Every persisted entity must declare a lifecycle policy:

```
entity_name
storage_engine
retention_period
retention_basis (law | policy | contract | user_consent)
start_event (creation | last_access | account_deletion | contract_end)
deletion_method (hard_delete | soft_delete | anonymization)
legal_hold_eligible (true | false)
archival_tier (hot | warm | cold | glacier)
```

### S8.A — Soft Delete Policy

```
SOFT_DELETE_TABLES   = Entities with business or legal retention obligations
SOFT_DELETE_FIELDS   = deleted_at TIMESTAMPTZ | deletion_reason TEXT | deletion_actor UUID
HARD_DELETE_TRIGGER  = After retention period expiry AND legal hold = false
ANONYMIZATION_PATH   = Identity fields nullified; pseudonymous ID retained for analytics
```

### S8.B — Legal Hold Override

```
LEGAL_HOLD_FLAG      = Stored on entity record and in dedicated legal_holds table
HOLD_TRIGGER_ACTORS  = Platform compliance officer | Legal authority | Automated flag (investigation)
DESTRUCTION_BLOCK    = Any entity under legal hold CANNOT be deleted or anonymized
HOLD_RELEASE         = Requires dual approval (legal + platform compliance)
AUDIT_TRAIL          = All hold creation, modification, and release events logged immutably
```

### S8.C — Right to Erasure (GDPR / DPDP compliance)

```
ERASURE_WORKFLOW     = RightRequest → RightValidation → ExecutionPlan → Execution → Verification → AuditLog
EXECUTION_WINDOW     = ≤ 15 days from validated request
ANONYMIZATION_TARGET = All direct identifiers (name, email, phone, institution_id, device_id, IP)
AUDIT_RETENTION      = Anonymized audit record retained (no personal identity, event log preserved)
CERTIFICATION_HOLD   = Certification records anonymized; audit trail of certification event retained
LEGAL_HOLD_BLOCK     = Erasure blocked if legal hold exists; user notified with reason
VERIFICATION_STEP    = Automated scan post-erasure to confirm no residual PII
```

---

## SECTION S9 — DATA ACCESS CONTROL LAYER (MANDATORY)

### S9.A — Database Access Rules

```
PRINCIPLE           = LEAST PRIVILEGE (mandatory, no exceptions)
SERVICE_ACCOUNTS    = One database role per microservice
ROLE_PERMISSIONS    = SELECT only for read services | SELECT+INSERT for write services |
                      SELECT+INSERT+UPDATE for state-transition services |
                      No DROP, TRUNCATE, or ALTER granted to application roles
ADMIN_ACCESS        = Only DBA role (not application role) for schema changes
HUMAN_ACCESS        = Read-only + time-limited + fully audited (no standing prod access)
PRODUCTION_ACCESS   = No developer has standing read/write access to production data
```

### S9.B — Storage Access Patterns

```
API_GATEWAY         = All external read/write flows go through API gateway
DIRECT_DB_ACCESS    = Forbidden from frontend, mobile clients, and AI systems
SERVICE_MESH        = Internal service-to-service calls via mTLS service mesh
FIELD_LEVEL_ACCESS  = Enforced at ORM/query layer (not just RLS)
PII_FIELDS          = Masked by default in all API responses unless explicitly permitted
```

---

## SECTION S10 — SEARCH (ELASTICSEARCH) GOVERNANCE (LOCKED)

### S10.A — Index Design Rules

```
INDEX_PER_ENTITY    = One index per entity type per tenant-domain combination
MAPPING_LOCK        = All field mappings declared in advance; no dynamic mapping in prod
MAPPING_VERSION     = Mappings versioned (v1, v2, etc.); new version = new index + reindex
ALIAS_STRATEGY      = Write alias + Read alias for zero-downtime reindex operations
SHARD_SIZING        = ≤ 50GB per shard (enforced via ILM)
REPLICA_COUNT       = Minimum 1 replica in production
```

### S10.B — SEO Index Rules

```
SEO_SCOPE           = Next.js (React) layer ONLY
INDEXABLE_ROUTES    = /jobs | /jobs/[id] | /skills | /projects | /company/[id] |
                      /institute/[id] | /blog/[slug] (as declared in R7)
FLUTTER_ROUTES      = MUST NOT be indexed; noindex meta enforced
SITEMAP             = Auto-generated from Elasticsearch public index
SCHEMA_ORG_MARKUP   = Generated from structured index documents
ROBOTS_TXT          = Managed; Flutter deep links excluded
```

### S10.C — Search Security

```
QUERY_SANITIZATION  = All user input sanitized before Elasticsearch query construction
MAX_QUERY_SIZE      = 10KB per query request
WILDCARD_LIMIT      = Leading wildcard forbidden (performance + injection risk)
RESULT_FILTERING    = Tenant + domain filter mandatory on every query result
SENSITIVE_FIELDS    = Never stored in Elasticsearch (passwords, tokens, financial data)
```

---

## SECTION S11 — MEDIA & FILE STORAGE GOVERNANCE (LOCKED)

### S11.A — Upload Lifecycle

```
STEP_1   = Client requests presigned upload URL from server (authenticated, scope-validated)
STEP_2   = Server generates time-limited presigned URL (TTL: 15 minutes)
STEP_3   = Client uploads directly to S3 using presigned URL
STEP_4   = S3 triggers upload event → Lambda/service performs virus scan
STEP_5   = Scan result: PASS → mark object as accessible | FAIL → quarantine + alert
STEP_6   = File metadata record created in PostgreSQL (not accessible until scan passes)
STEP_7   = Application accesses file via presigned download URL (server-generated, short TTL)
```

### S11.B — File Classification & Retention

```
FILE CLASS              RETENTION   STORAGE TIER  TENANT ACCESS
─────────────────────────────────────────────────────────────────
Resume / CV             5 years     Standard      Owner + Recruiter
Profile Picture         Account life Standard      Public (masked URL)
Project Evidence        7 years     Standard      Owner + Mentor + Institution
Certificate             Permanent   Standard      Owner + Verifier (presigned)
Dojo Session Recording  2 years     Standard+Cold Owner + Mentor + Platform Admin
Export Reports          90 days     Standard      Requester only
Audit Attachments       10 years    Cold          Compliance role only
Training Materials      Contract+1y Standard      Enrolled users
```

### S11.C — Dojo Recording Governance

```
RECORDING_CONSENT    = Explicit consent captured before session (UI consent gate)
CONSENT_LOG          = Consent event stored in PostgreSQL (immutable)
ACCESS_CONTROL       = Match participants + assigned mentor + platform compliance
DOWNLOAD_RESTRICTION = Download disabled; streaming only via time-limited presigned URL
REPLAY_ACCESS_LOG    = Every replay access logged to audit store
TRANSCRIPT_STORAGE   = Separate from recording; stored in MongoDB | retention = 2 years
ANONYMIZATION_ON_ERASURE = Voice/face biometric identifiers stripped on right-to-erasure
```

---

## SECTION S12 — EVENT SCHEMA REGISTRY GOVERNANCE (LOCKED)

### S12.A — Schema Registry Rules

```
REGISTRY            = Confluent Schema Registry OR Apicurio Registry
FORMAT              = Apache Avro (preferred) | Protocol Buffers | JSON Schema
EVOLUTION_RULES     = BACKWARD_COMPATIBLE required for minor versions |
                      NEW_TOPIC required for breaking schema changes
REGISTRATION        = All schemas registered before first producer write
VALIDATION          = Consumer must validate incoming event against registered schema
```

### S12.B — Standard Event Envelope

Every event published to Kafka MUST include:
```json
{
  "event_id": "uuid-v4",
  "event_type": "string (declared in registry)",
  "event_version": "semver",
  "timestamp": "ISO-8601 UTC",
  "tenant_id": "uuid",
  "domain": "arts | commerce | science | cross | system",
  "actor_id": "uuid (user or service)",
  "actor_type": "user | service | system",
  "correlation_id": "uuid (request trace)",
  "payload": { /* event-specific typed payload */ },
  "metadata": {
    "source_service": "string",
    "environment": "dev | staging | prod"
  }
}
```

Missing required fields → **Event rejected by consumer; DLQ route triggered**

### S12.C — Domain-Specific Event Streams

```
TOPIC: platform.auth-service.user_created.v1
TOPIC: platform.auth-service.user_deleted.v1
TOPIC: platform.auth-service.mfa_enabled.v1
TOPIC: arts.dojo-service.match_started.v1
TOPIC: arts.dojo-service.match_completed.v1
TOPIC: commerce.dojo-service.match_started.v1
TOPIC: commerce.job-service.application_submitted.v1
TOPIC: commerce.job-service.offer_extended.v1
TOPIC: science.dojo-service.certification_awarded.v1
TOPIC: platform.billing-service.payment_completed.v1
TOPIC: platform.billing-service.subscription_renewed.v1
TOPIC: platform.compliance-service.audit_event.v1
TOPIC: platform.notification-service.notification_created.v1
TOPIC: platform.gamification-service.points_awarded.v1
TOPIC: platform.ai-service.match_scored.v1
```

---

## SECTION S13 — CONNECTIVITY GOVERNANCE (LOCKED)

### S13.A — Internal Service Connectivity

```
PROTOCOL          = gRPC (preferred for internal) | REST+JSON (secondary)
TRANSPORT         = mTLS (mandatory for all internal traffic)
SERVICE_MESH      = Istio or Linkerd (mandatory)
DISCOVERY         = Kubernetes Service + DNS (no hardcoded IPs)
LOAD_BALANCING    = L7 (application-aware) for gRPC | L4 for data stores
CIRCUIT_BREAKER   = Required on all external service calls (Resilience4j / Hystrix / Istio)
TIMEOUT           = Per-service defined; no unbounded calls
RETRY_POLICY      = Max 3 retries with exponential backoff + jitter
```

### S13.B — External API Connectivity

```
INGRESS             = API Gateway (Kong or AWS API GW or equivalent)
AUTHENTICATION      = JWT (RS256 or ES256) validated at gateway
RATE_LIMITING       = Enforced at gateway (per user + per tenant + per endpoint)
WAF                 = Web Application Firewall (OWASP ruleset)
DDoS_PROTECTION     = Cloud-level DDoS mitigation (Cloudflare or cloud-native)
TLS_TERMINATION     = At gateway (not at pod level for external traffic)
```

### S13.C — Third-Party Integration Connectivity

```
CONNECTION_TYPE     = OAuth 2.0 (preferred) | API Key (fallback, vault-stored)
CIRCUIT_BREAKER     = Mandatory on all third-party calls
TIMEOUT             = 10 seconds hard timeout on all third-party API calls
FALLBACK            = Graceful degradation defined per third-party dependency
VENDOR_REGISTRY     = Only approved vendors from R56 registry may be called
SECRET_STORAGE      = All third-party credentials in Vault (never in code or config)
RETRY_POLICY        = Max 2 retries (third-party); no aggressive retry on rate-limit (429)
```

### S13.D — Dojo Real-Time Connectivity

```
PROTOCOL            = WebRTC (peer media) + WebSocket (signaling + state sync)
SIGNALING_SERVER    = LiveKit (SFU model) | Mediasoup (alternative)
TRANSPORT           = WSS (WebSocket Secure; no ws://)
TOKEN_VALIDATION    = Room token validated server-side before join
DOMAIN_ISOLATION    = Arts | Commerce | Science rooms fully isolated at session level
RECORDING           = Server-side recording via SFU (not client-side)
RECONNECTION        = Auto-reconnect within 30-second window (state preserved)
SESSION_EXPIRY      = Match session TTL enforced server-side
RATE_LIMITING       = Message send rate limited per user per room (Redis-backed)
```

---

## SECTION S14 — OFFLINE & SYNC GOVERNANCE (LOCKED)

```
OFFLINE_DETECTION   = Real-time network monitoring in Flutter app
OFFLINE_CACHE       = Critical read data cached locally:
                      Dashboard state | Profile | Recent jobs | Learning progress | Applications
CACHE_FRESHNESS     = Timestamp displayed to user for cached data
OFFLINE_WRITE_QUEUE = All mutations queued locally (FIFO)
SYNC_ON_RECONNECT   = Automatic sync (FIFO order, deduplicated)
CONFLICT_RESOLUTION = Auto-merge (additive changes) | User choice modal (conflicting edits)
SENSITIVE_OFFLINE   = Payment, certification, and compliance actions BLOCKED offline
```

---

## SECTION S15 — STORAGE PERFORMANCE STANDARDS (LOCKED)

```
METRIC                    TARGET              MONITORING
─────────────────────────────────────────────────────────────────────
PostgreSQL query (P95)    < 100ms             Prometheus + PgAnalyze
PostgreSQL query (P99)    < 500ms             Prometheus + PgAnalyze
Redis read (P95)          < 5ms               Prometheus
Redis write (P95)         < 10ms              Prometheus
Elasticsearch search (P95) < 200ms            Prometheus
S3 presigned URL gen      < 500ms             Prometheus
S3 upload (to accessible) < 60s (incl. scan)  Prometheus
Kafka produce (P95)       < 50ms              Prometheus
Kafka consume lag         < 5 seconds         Prometheus + AlertManager
Connection pool wait (P99) < 5 seconds        Prometheus
```

SLA Breach → AlertManager triggers → PagerDuty → On-call engineer

---

## SECTION S16 — STORAGE SECURITY HARDENING (LOCKED)

### S16.A — SQL Injection Prevention

```
ORM_REQUIRED        = All queries through ORM with parameterized queries
RAW_SQL             = Allowed ONLY for migrations and admin scripts (not application paths)
QUERY_REVIEW        = Any raw SQL in application code requires security review
STORED_PROCEDURES   = Allowed; must use parameterized inputs
```

### S16.B — Data Exfiltration Prevention

```
BULK_EXPORT_LIMITS  = Maximum 10,000 records per export request
BULK_EXPORT_AUDIT   = Every bulk export logged to audit store with actor, scope, timestamp
API_OUTPUT_LIMIT    = Maximum page size enforced per endpoint
RESPONSE_FILTERING  = Only requested fields returned; no over-posting
RATE_LIMIT_EXPORTS  = Export endpoints have stricter rate limits
```

### S16.C — Storage Access Anomaly Detection

```
BASELINE_BEHAVIOR   = Normal access patterns established per role
ANOMALY_TRIGGERS    = Excessive read volume | Cross-tenant access attempt |
                      Off-hours admin access | Mass export | Schema discovery queries
RESPONSE            = Automatic throttle → Alert → Investigation trigger
```

---

## SECTION S17 — MIGRATION & ZERO-DOWNTIME UPGRADE GOVERNANCE (LOCKED)

### S17.A — Database Migration Rules

```
MIGRATION_TOOL      = Flyway or Liquibase (mandatory; no manual DDL in production)
VERSION_CONTROL     = All migrations committed to VCS before execution
MIGRATION_POLICY    = Additive-first (new columns, new tables); 
                      Destructive (column/table removal) requires two-release window
DUAL_WRITE_PERIOD   = Old and new schema paths must coexist during transition
ROLLBACK_REQUIRED   = Every migration must have reversible rollback script
ZERO_DOWNTIME       = Blue-Green or expand-contract pattern mandatory
REVIEW_GATE         = Migration script reviewed by DBA + security before production run
```

### S17.B — Index Migration Rules

```
ELASTICSEARCH_REINDEX = New index → Reindex API → Alias swap (zero-downtime)
REDIS_KEY_MIGRATION   = New key pattern introduced alongside old; TTL drains old pattern
NO_BREAKING_CHANGES   = Any breaking schema change requires new version (topic, index, table)
```

---

## SECTION S18 — COMPLIANCE INHERITANCE (ADD-ONLY)

This agent inherits and enforces all compliance mandates that have data storage implications, including but not limited to:

```
INHERITED_FROM                   STORAGE IMPLICATION
────────────────────────────────────────────────────────────────────────
Authorization (RBAC + ABAC)      DB: role-scoped row access; RLS policies
Password Security                DB: bcrypt hashed only; no plaintext
Authentication & MFA             Redis: session + OTP stores
Session Management               Redis: session TTL; no orphan sessions
Encryption at Rest               All engines: AES-256 mandatory
Tenant Isolation (hard)          All engines: schema/index/topic/prefix isolation
Domain Isolation (hard)          All engines: domain_track field + topic prefix
Audit Immutability               PostgreSQL: append-only audit tables; WORM archive
R18 – Backup & DR                All engines: backup policies + RPO/RTO defined
R22 – Zero-downtime migrations   PostgreSQL: dual-write + expand-contract
DATA-MIN-v1 – Data Minimization  All engines: only required fields stored/returned
RKC – Record Keeping             PostgreSQL: immutable records, class + owner declared
GDPR/DPDP Compliance             All engines: erasure workflows + data residency
Minor Data Protection            Enhanced controls on student/minor data across all engines
Security Audit Logging           PostgreSQL audit tables: every access, change, deletion
```

---

## SECTION S19 — ANTIGRAVITY GENERATION CONSTRAINTS (BINDING)

When Antigravity generates storage-related artifacts for ECOSKILLER:

**MUST generate:**
- PostgreSQL schema files with RLS policies per tenant table
- Redis keyspace convention enforcement utilities
- Elasticsearch index templates with mandatory field mappings
- S3 bucket policies and CORS configurations
- Kafka topic creation scripts with ACL definitions
- Secret management templates (no real values)
- Database migration scripts (Flyway/Liquibase)
- Backup schedule configurations
- Connection pool configurations per service
- Audit table DDL with append-only trigger enforcement

**MUST NOT:**
- Create shared tenant tables without explicit RLS
- Use dynamic Elasticsearch mapping in production configs
- Store secrets in application config files
- Generate code with raw SQL string concatenation
- Create S3 buckets with public read access
- Share Redis instances between tenants without namespace enforcement
- Generate hardcoded connection strings
- Create Kafka topics without schema registry registration
- Allow plaintext HTTP/WS connections to any storage service
- Generate MongoDB collections for financial or auth data

---

## SECTION S20 — INTERNSHIP EXECUTION MAPPING (R26 COMPLIANCE)

For every storage artifact generated, the following must be included:

```
ARTIFACT_NAME        = (e.g., "User table PostgreSQL migration")
FILE_PATH            = /db/migrations/V{version}__{description}.sql
CODE_CONTENT         = Exact SQL or configuration
SECTION_EXPLANATION  = What each block does (inline comments)
SERVICE_CONNECTIONS  = Which services read/write this table/index/topic
EXECUTION_COMMAND    = flyway migrate | kafka-topics.sh --create | etc.
SUCCESS_OUTPUT       = Expected output after successful execution
ROLLBACK_COMMAND     = How to reverse if needed
```

---

## SECTION S21 — COST-AWARE STORAGE DEFAULTS (R25 / R28 COMPLIANCE)

Startup-stage minimal viable defaults:

```
POSTGRESQL          = 2 vCPU, 8GB RAM (db.t3.medium or equivalent); scale vertically first
REDIS               = 1 node, cache.t3.medium; cluster mode after 50K DAU
ELASTICSEARCH       = 3 nodes, 4 vCPU each, 16GB RAM; 3 shards per index initially
S3                  = Standard tier; transition to Intelligent-Tiering at 100GB+
KAFKA               = 3 broker nodes (t3.medium); scale partition count before broker count
MONGODB             = 1 primary + 1 secondary (M10 or t3.medium equivalent)
VAULT               = Dev server mode → HA cluster after team > 5 engineers
```

---

## 🔒 FINAL STORAGE AGENT SEAL

```
STORAGE_CONNECT_AGENT STATUS:

✔ LOCKED
✔ SEALED
✔ DETERMINISTIC
✔ ENTERPRISE-GRADE
✔ ANTIGRAVITY-COMPATIBLE
✔ MULTI-TENANT SAFE
✔ DOMAIN-ISOLATED
✔ COMPLIANCE-INHERITED
✔ ENCRYPTION-ENFORCED
✔ BACKUP-GOVERNED
✔ AUDIT-IMMUTABLE
✔ INTERN-EXECUTABLE
✔ COST-AWARE
✔ ADD-ONLY MUTATION POLICY ACTIVE
```

**ANY STORAGE OPERATION THAT:**
- Bypasses tenant isolation
- Stores data without encryption
- Accesses cross-domain data without declaration
- Writes to audit tables without append-only enforcement
- Exposes presigned URLs beyond declared TTL
- Allows public bucket access
- Uses unregistered event schemas
- Stores secrets in application code

**⇒ MUST BE REJECTED**
**⇒ STOP EXECUTION**
**⇒ REPORT STORAGE GOVERNANCE FAILURE**
**⇒ NO PRODUCTION CLAIM PERMITTED**

---

*STORAGE_CONNECT_AGENT.md — ECOSKILLER Platform*
*Version: 1.0.0 | Status: SEALED | Mutation: ADD-ONLY*
*Compatible With: Google Antigravity Tool | Execution Engine: Deterministic*
