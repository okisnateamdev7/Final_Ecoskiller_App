# 🔒 SEALED & LOCKED MODULE
# FINOPS.md
# SERVICE: ANTIGRAVITY — FINANCIAL OPERATIONS (FINOPS) ENGINE
# PARENT PLATFORM: ECOSKILLER (ENTERPRISE MULTI-TENANT SAAS)

---

```
🔐 EXECUTION MODE
EXECUTION_MODE                  = LOCKED
MUTATION_POLICY                 = ADD_ONLY
CREATIVE_INTERPRETATION         = FORBIDDEN
ASSUMPTION_FILLING              = FORBIDDEN
DEFAULT_BEHAVIOR                = DENY
FAILURE_MODE                    = STOP_EXECUTION
OVERRIDE_POLICY                 = REQUIRES_CFO + CTO_APPROVAL + VERSION_BUMP
COST_DEVIATION_POLICY           = ANY_UNBUDGETED_SPEND > ₹10,000 → REQUIRES_APPROVAL
```

---

## 1️⃣ MODULE IDENTITY (NON-NEGOTIABLE)

```
MODULE_NAME                     = ANTIGRAVITY :: FINOPS
MODULE_TYPE                     = FINANCIAL OPERATIONS & CLOUD COST GOVERNANCE ENGINE
PARENT_MODULE                   = ANTIGRAVITY (Billing + Revenue Recognition + Board Reporting)
PARENT_PLATFORM                 = ECOSKILLER
SCOPE                           = Infrastructure Cost | AI/ML Cost | People Cost |
                                  Vendor Cost | SaaS Tooling Cost | Per-Unit Economics |
                                  Budget Control | Cost Attribution | Optimization Programs
FINOPS_FRAMEWORK                = FinOps Foundation (Inform → Optimize → Operate)
CURRENCY_PRIMARY                = INR
CURRENCY_SECONDARY              = USD (cloud vendor invoices)
FISCAL_YEAR_START               = April 1
FISCAL_YEAR_END                 = March 31
BUDGET_CYCLE                    = Annual (with quarterly reforecast)
COST_ATTRIBUTION_MODEL          = FULL ALLOCATION — every rupee attributed to a module, tenant tier, or cost center
UNATTRIBUTED_SPEND              = FORBIDDEN — all cost must be tagged
```

---

## 2️⃣ FINOPS LIFECYCLE (LOCKED — THREE PHASES)

```
PHASE_1 = INFORM
  Purpose : Complete cost visibility — know what everything costs
  Outputs : Cost dashboards, tagging enforcement, unit cost baselines

PHASE_2 = OPTIMIZE
  Purpose : Eliminate waste, rightsize, negotiate — reduce cost per unit
  Outputs : Rightsizing recommendations, reserved capacity, waste reports

PHASE_3 = OPERATE
  Purpose : Embed cost accountability into every engineering & product decision
  Outputs : Per-sprint cost impact, budget gates, anomaly alerts, FinOps OKRs

ALL THREE PHASES RUN CONCURRENTLY AFTER STAGE 2 (INTELLIGENCE) OF THE FOUR-STAGE MODEL.
PHASE_1 ALONE IS MANDATORY FROM STAGE 1 DAY 1.
```

---

## 3️⃣ COST DOMAIN TAXONOMY (LOCKED — COMPLETE)

Every cost incurred by the Ecoskiller platform must be classified under exactly one of these domains. No unclassified cost is permitted.

```
DOMAIN_1  = COMPUTE (Kubernetes nodes, VMs, containers)
DOMAIN_2  = DATABASE (PostgreSQL, Redis, ClickHouse, etcd)
DOMAIN_3  = OBJECT_STORAGE (MinIO — resumes, certificates, invoices, audit files)
DOMAIN_4  = SEARCH (OpenSearch / Elasticsearch)
DOMAIN_5  = MESSAGING (Apache Kafka, RabbitMQ)
DOMAIN_6  = MEDIA_REALTIME (Jitsi, LiveKit, coturn / TURN/STUN)
DOMAIN_7  = NETWORKING (Bandwidth, CDN, Ingress, NAT, DNS)
DOMAIN_8  = OBSERVABILITY (Prometheus, Grafana, Loki, OpenTelemetry)
DOMAIN_9  = SECURITY (Wazuh SIEM, Vault, ModSecurity, OPA)
DOMAIN_10 = COMMUNICATION (Postfix, Docker Mail Server, Jasmin SMS, ntfy)
DOMAIN_11 = CICD_DEVOPS (GitHub Actions, GitLab CI, Helm, Velero, Opentofu)
DOMAIN_12 = AI_ML_INTELLIGENCE (LLM API calls, ML inference, model hosting)
DOMAIN_13 = THIRD_PARTY_SAAS (Razorpay, Stripe, PayU fees, external APIs)
DOMAIN_14 = PEOPLE (Salaries, benefits, contractors, ESOPs, recruitment)
DOMAIN_15 = LICENSING (Commercial software, fonts, maps, data feeds)
DOMAIN_16 = COMPLIANCE_LEGAL (Audit, legal, GST filing, certifications)
DOMAIN_17 = WORKSPACE (Office, co-working, equipment, utilities)
DOMAIN_18 = MARKETING_DISTRIBUTION (Paid acquisition, events, materials)
```

---

## 4️⃣ COST TAGGING POLICY (MANDATORY — HARD ENFORCEMENT)

```
TAGGING_POLICY        = MANDATORY on ALL infrastructure resources
UNTAGGED_SPEND        = ZERO TOLERANCE — triggers automated alert + approval block
TAGGING_ENGINE        = Kubernetes labels + Cloud provider tags + Opentofu tag modules
TAG_ENFORCEMENT       = OPA (Open Policy Agent) policy — resource without tags = DEPLOY BLOCKED
```

### Required Tag Set (ALL resources must carry ALL tags)

| Tag Key | Allowed Values | Purpose |
|---------|---------------|---------|
| `platform` | `ecoskiller` | Platform identifier |
| `environment` | `dev` \| `test` \| `staging` \| `prod` | Environment isolation |
| `module` | `job-portal` \| `skill-engine` \| `project-engine` \| `dojo` \| `erp` \| `billing` \| `analytics` \| `auth` \| `shared` | Module attribution |
| `service` | Exact microservice name (e.g., `auth-service`, `dojo-match-engine`) | Service-level cost |
| `k8s-namespace` | `core` \| `realtime` \| `media` \| `billing` \| `analytics` \| `admin` \| `ops` | Kubernetes namespace |
| `cost-domain` | Domain 1–18 from §3 | FinOps domain classification |
| `tenant-tier` | `free` \| `starter` \| `pro` \| `team` \| `institute` \| `enterprise` | Tenant tier attribution |
| `stage` | `stage-1` \| `stage-2` \| `stage-3` \| `stage-4` | Four-stage model alignment |
| `owner` | Team or squad name | Cost accountability owner |
| `cost-center` | `engineering` \| `product` \| `ai-ml` \| `operations` \| `gna` | Finance cost center |

### Tag Enforcement Flow

```
Resource Creation Request
  → OPA Policy Check: All required tags present?
      YES → Deploy allowed
      NO  → Deploy BLOCKED
           → Alert: cost-tagging-violation → Slack #finops-alerts
           → Ticket auto-created in engineering backlog
           → No exceptions without CFO + CTO written approval
```

---

## 5️⃣ KUBERNETES NAMESPACE COST ALLOCATION (LOCKED)

Kubernetes namespaces map directly to cost centers. Each namespace has a dedicated cost budget.

```
NAMESPACE_COST_TRACKING_TOOL = Kubecost (primary) + Prometheus metrics (secondary)
ALLOCATION_METHOD            = CPU + Memory actual usage (not requested)
IDLE_COST_POLICY             = Attributed to namespace owner — idle = waste
```

| K8s Namespace | Services Running | Budget Owner | Cost Allocation Method |
|--------------|-----------------|-------------|----------------------|
| `core` | Auth, User, Job, Application, Recruiter | Engineering Lead - Core | CPU + Memory actual |
| `realtime` | Interview, Voice GD Orchestrator, Dojo Match Engine, WebSocket | Engineering Lead - Realtime | CPU + Memory + connection-hours |
| `media` | Jitsi, Jitsi Videobridge, Jicofo, LiveKit, coturn | Engineering Lead - Media | Participant-minutes + bandwidth |
| `billing` | Antigravity Billing Service, Airflow (billing DAGs) | CFO + Engineering Lead | CPU + Memory actual |
| `analytics` | Analytics Service, ClickHouse, Airflow (analytics DAGs) | Engineering Lead - Data | Storage-GB + query-compute |
| `admin` | Admin Governance, Integration Hub, Unleash | Engineering Lead - Platform | CPU + Memory actual |
| `ops` | Prometheus, Grafana, Loki, OpenTelemetry, Wazuh, Velero | DevOps Lead | Flat allocation (ops overhead) |
| `search` | OpenSearch / Elasticsearch | Engineering Lead - Core | Index-GB + query-compute |
| `messaging` | Apache Kafka, RabbitMQ | Engineering Lead - Platform | Message-throughput + storage |

---

## 6️⃣ INFRASTRUCTURE COST BASELINE — COMPLETE STACK

### 6A. Compute (Kubernetes Nodes)

```
COMPUTE_ARCHITECTURE    = Kubernetes (mandatory) on cloud VMs
NODE_SIZING_POLICY      = Rightsize every 30 days using actual P95 CPU + Memory
OVERPROVISIONING_LIMIT  = Maximum 30% headroom above P95 usage
IDLE_NODE_POLICY        = Scale to zero for non-prod environments at night (20:00–07:00 IST)
SPOT_INSTANCE_POLICY    = Use spot/preemptible nodes for: analytics, batch jobs, search indexing
ON_DEMAND_POLICY        = Required for: auth, billing, realtime, media, core APIs
```

| Node Pool | Purpose | Node Type Policy | Min Nodes | Max Nodes | Scale Trigger |
|-----------|---------|-----------------|-----------|-----------|--------------|
| `core-pool` | auth, users, jobs, recruiter | On-demand (stability critical) | 2 | 10 | CPU > 70% |
| `realtime-pool` | GD, Dojo, WebSocket, Interviews | On-demand (latency critical) | 2 | 20 | Connection count |
| `media-pool` | Jitsi, LiveKit, coturn | On-demand (media SLA) | 2 | 15 | Active sessions |
| `analytics-pool` | ClickHouse, Airflow DAGs | Spot (batch tolerant) | 1 | 10 | Job queue depth |
| `search-pool` | OpenSearch | On-demand (query SLA) | 2 | 6 | Query latency P95 |
| `billing-pool` | Antigravity billing | On-demand (financial critical) | 2 | 4 | CPU > 60% |
| `ops-pool` | Prometheus, Grafana, Loki, Wazuh | On-demand (observability) | 2 | 4 | Fixed |
| `batch-pool` | Kafka consumers, Airflow workers | Spot (fault tolerant) | 0 | 20 | Job queue |

**Cost Control Rules:**
```
OVERPROVISIONED_NODE_ALERT  = Node CPU < 20% for 7 consecutive days → Rightsize recommendation
NODE_WASTE_REPORT           = Weekly, per namespace, surfaced in FinOps dashboard
PROD_SCALE_DOWN_FORBIDDEN   = core-pool | realtime-pool | media-pool | billing-pool never scale to zero
NON_PROD_AUTO_SHUTDOWN      = dev + test environments: auto-shutdown 20:00 IST; auto-start 07:00 IST
NON_PROD_SAVINGS            = Target 60% cost reduction vs always-on
```

---

### 6B. Database Costs

| Database | Technology | Deployment | Cost Driver | Optimization Policy |
|----------|-----------|-----------|------------|---------------------|
| Primary Transactional | PostgreSQL | Managed or self-hosted on K8s | Storage-GB + IOPS + compute | Row-level partitioning; archive cold data to MinIO after 90 days |
| In-Memory / State | Redis | Self-hosted on K8s | Memory-GB | Eviction policy: allkeys-lru; max memory hard cap per namespace |
| Analytics | ClickHouse | Self-hosted on K8s (analytics-pool) | Compute + storage + query volume | Spot nodes; tiered storage (hot/warm/cold); TTL policies |
| Search | OpenSearch | Self-hosted on K8s (search-pool) | Index-GB + query compute | Index lifecycle management; delete indices > 180 days old |
| Coordination | etcd | Self-hosted K8s control plane | Minimal | Not a variable cost driver |

**Database FinOps Rules:**
```
POSTGRES_CONNECTION_POOLING     = PgBouncer mandatory — no direct connection from services
POSTGRES_ARCHIVE_POLICY         = Records > 90 days cold in prod → MinIO cold tier
CLICKHOUSE_TTL_POLICY           = Raw events: 90 days hot; 180 days warm; 365 days cold (S3/MinIO)
REDIS_MEMORY_BUDGET             = Hard cap per namespace; exceed = OOM eviction (never oversubscribe)
OPENSEARCH_SHARD_SIZING         = Max 50GB per shard; review monthly; merge shards when under-utilized
DATABASE_COST_REPORT            = Weekly per database, attributed to owning namespace
```

---

### 6C. Object Storage (MinIO)

```
MINIO_USAGE                     = Resumes, certificates, invoices, audit files, backups
STORAGE_TIERING                 = Hot (accessed < 30 days) | Warm (30–180 days) | Cold (> 180 days)
LIFECYCLE_POLICY                = Automated via MinIO ILM (Information Lifecycle Management)
BACKUP_RETENTION                = 7 years (billing / audit) | 2 years (user content) | 1 year (logs)
COST_DRIVER                     = Storage-GB + egress bandwidth
```

| Content Type | Retention | Storage Tier | Lifecycle Rule |
|-------------|-----------|-------------|---------------|
| Resumes / CVs | User-controlled (min 2 years post-offboard) | Hot → Warm after 90 days | Compress on warm transition |
| Certificates & Badges | Permanent (immutable) | Hot forever | No expiry |
| Invoices & Billing Docs | 7 years (statutory) | Hot 1 year → Cold | AES-256 encrypted at rest |
| Audit Logs | 7 years (statutory) | Cold (write-once) | Immutable + checksum |
| Dojo Session Recordings | 90 days (consent-bound) | Hot → Delete at 90 days | Auto-delete policy |
| Profile Images | User-controlled | Hot | Resize on upload (CDN-optimized) |
| Backup Snapshots (Velero) | 30 days rolling | Warm | 30-day TTL auto-delete |

---

### 6D. Messaging Infrastructure

| Component | Technology | Cost Driver | Optimization Policy |
|-----------|-----------|------------|---------------------|
| Event Bus | Apache Kafka | Broker storage + partition count + throughput | Compact topics after 48h; archive to MinIO after 7 days |
| Background Jobs | RabbitMQ | Message volume + queue depth | Dead-letter queue monitoring; purge stale queues weekly |
| Scheduled Workflows | Apache Airflow | Worker node CPU (analytics-pool spot) | Pack DAGs efficiently; consolidate billing + analytics DAGs |

```
KAFKA_TOPIC_RETENTION           = 48 hours (real-time events) | 7 days (billing events) | 30 days (audit events)
KAFKA_PARTITION_REVIEW          = Monthly — remove unused partitions
RABBITMQ_DEAD_LETTER_ALERT      = DLQ > 100 messages → PagerDuty + FinOps ticket
AIRFLOW_DAG_COST_TAGGING        = Every DAG tagged with module + cost-center
```

---

### 6E. Media & Realtime Stack Costs

```
MEDIA_STACK                     = Jitsi + Jitsi Videobridge + Jicofo + LiveKit + coturn
COST_DRIVER_PRIMARY             = Participant-minutes (active audio/video sessions)
COST_DRIVER_SECONDARY           = Bandwidth (upload + download per session)
COST_DRIVER_TERTIARY            = Node compute (media-pool on-demand nodes)
MEDIA_COST_UNIT                 = Cost per Dojo session | Cost per Interview session | Cost per GD session
```

| Session Type | Avg Duration | Avg Participants | Compute Driver | Bandwidth Driver | Target Unit Cost |
|-------------|-------------|-----------------|---------------|-----------------|-----------------|
| Dojo Match (GD) | 45 mins | 6–8 participants | Jitsi Videobridge | Audio stream × participants | ₹ [SET BY CFO] |
| Interview Session | 60 mins | 2 participants | LiveKit SFU | Audio + optional video | ₹ [SET BY CFO] |
| Webinar / Demo | 60–90 mins | Up to 500 | LiveKit SFU | Broadcast downstream | ₹ [SET BY CFO] |

**Media FinOps Rules:**
```
IDLE_MEDIA_ROOMS                = Rooms with 0 active participants > 5 mins → auto-terminate
COTURN_RELAY_OPTIMIZATION       = Prefer STUN (free) over TURN (bandwidth cost); TURN only when NAT forces it
JITSI_NODE_SCALING              = Scale based on active_sessions metric; scale-in aggressively after sessions end
MEDIA_COST_PER_SESSION_ALERT    = If unit cost exceeds target by > 20% → FinOps investigation
RECORDING_POLICY                = Dojo recordings: consent required; stored 90 days max; no permanent recording
```

---

### 6F. Networking & CDN Costs

```
BANDWIDTH_POLICY                = Minimize egress; maximize in-cluster (zero-cost) traffic
CDN_POLICY                      = Static assets (Flutter web, images, docs): CDN-served; never origin-served
API_EGRESS                      = All external API calls counted; budget per service per month
DNS_POLICY                      = Self-managed where possible; cloud DNS only if required
```

| Traffic Type | Direction | Cost Impact | Optimization |
|-------------|-----------|------------|-------------|
| API responses (JSON) | Egress | Medium | Compression (gzip/br); pagination |
| Media streams (Jitsi/LiveKit) | Egress + Ingress | High | coturn STUN-first; direct P2P where possible |
| Static assets (Flutter web) | Egress | Medium | CDN cache; long TTLs |
| Database backups (Velero) | Cross-zone egress | Low–Medium | Same-region bucket; avoid cross-region |
| Kafka replication | Internal | Low | Same-region brokers only |
| Audit log archival (MinIO) | Cross-region (if DR) | Low | DR region: archive only, no realtime replication |

**Networking Budget Targets:**
```
CDN_CACHE_HIT_RATE_TARGET       = ≥ 85%
COMPRESSION_COVERAGE            = 100% of JSON API responses
EGRESS_AS_%_OF_INFRA_BUDGET     = ≤ 20%
EGRESS_GROWTH_ALERT             = Month-over-month egress growth > 30% without corresponding MAU growth → Investigation
```

---

### 6G. Observability Stack Costs

```
OBSERVABILITY_STACK             = Prometheus + Grafana + Loki + OpenTelemetry + Wazuh
COST_DRIVER                     = Metrics cardinality | Log volume | Trace storage | SIEM event volume
OBSERVABILITY_AS_%_OF_INFRA     = Target ≤ 8% of total infrastructure cost
```

**Observability FinOps Rules:**
```
PROMETHEUS_CARDINALITY_LIMIT    = Max 10M active time series; high-cardinality labels forbidden in prod
LOKI_LOG_RETENTION              = prod: 30 days hot | 90 days cold; dev/staging: 7 days only
OPENTELEMETRY_SAMPLING          = 100% sampling: errors + slow traces (>2s); 10% sampling: normal traces
WAZUH_ALERT_TUNING              = Review alert rules monthly; silence noisy rules; don't ignore real alerts
GRAFANA_DASHBOARD_HYGIENE       = Unused dashboards (no views > 30 days) → archived; reduces query load
```

---

### 6H. Security Infrastructure Costs

```
SECURITY_STACK                  = Wazuh SIEM | HashiCorp Vault | ModSecurity | OPA | Keycloak
SECURITY_COST_POLICY            = Non-negotiable; security cost is never optimized away
SECURITY_BUDGET_FLOOR           = Minimum 5% of total infrastructure budget; no exceptions
```

| Component | Cost Driver | FinOps Note |
|-----------|------------|-------------|
| HashiCorp Vault | Compute + storage | Self-hosted; use Vault agent caching to reduce API calls |
| Keycloak | Compute + session storage | Self-hosted; session count scales with MAU |
| Wazuh SIEM | Compute + log ingestion volume | Tune agents to reduce noise; archive old SIEM events |
| ModSecurity (WAF) | CPU overhead on NGINX | Profile rules; disable unused rule sets |
| OPA (Open Policy Agent) | CPU per policy evaluation | Cache policy decisions at API gateway layer |

---

## 7️⃣ AI & INTELLIGENCE COST GOVERNANCE (RULE R28 — ENFORCED)

```
GOVERNING_LAW                   = R28 — Intelligence Cost Optimization Law (Master Prompt)
AI_ADVISORY_ONLY                = TRUE (AI never approves, blocks, or overrides humans)
LLM_USE_POLICY                  = RESTRICTED — only permitted for NLU, text generation, summarization, explainability
ML_MODEL_POLICY                 = Traditional ML (Gradient Boosting, Logistic Regression, LTR, Anomaly Detection) for all
                                  structured prediction, ranking, matching, classification, recommendation, fraud detection
RULE_ENGINE_POLICY              = Deterministic workflows, permissions, billing, compliance MUST use rule engines — NOT ML/AI
COST_DECLARATION_REQUIRED       = Every AI/ML component must declare: model type + cost per 1,000 requests + monthly cost at MVP traffic
```

### AI Cost Registry (MANDATORY — LOCKED FORMAT)

Every AI/ML function deployed must be registered here with full cost declaration before production deployment.

| AI Function | Model Class | Model Type | Cost / 1K Requests | Monthly Volume (MVP) | Monthly Cost Est. | Justification for AI (vs Rule Engine) |
|------------|-------------|-----------|-------------------|---------------------|------------------|--------------------------------------|
| Resume Parsing | LLM | GPT-4o-mini / Gemini Flash | ₹ [DECLARE] | [DECLARE] | ₹ [DECLARE] | Unstructured text → structured extraction |
| Skill Gap Detection | Traditional ML | Gradient Boosting | ₹ [DECLARE] | [DECLARE] | ₹ [DECLARE] | Structured vector comparison |
| Job Match Scoring | Traditional ML | LTR (LambdaMART) | ₹ [DECLARE] | [DECLARE] | ₹ [DECLARE] | Ranking task — ML native |
| Placement Probability | Traditional ML | Logistic Regression | ₹ [DECLARE] | [DECLARE] | ₹ [DECLARE] | Binary classification |
| Offer Acceptance Prediction | Traditional ML | Gradient Boosting | ₹ [DECLARE] | [DECLARE] | ₹ [DECLARE] | Structured feature prediction |
| Recruiter Behavior Analytics | Traditional ML | Anomaly Detection | ₹ [DECLARE] | [DECLARE] | ₹ [DECLARE] | Pattern anomaly detection |
| AI Explainability (Human-readable) | LLM | GPT-4o-mini | ₹ [DECLARE] | [DECLARE] | ₹ [DECLARE] | NLU + text generation |
| Conversational Guidance (Dojo) | LLM | GPT-4o-mini | ₹ [DECLARE] | [DECLARE] | ₹ [DECLARE] | Conversational NLU |

**AI FinOps Rules:**
```
LLM_COST_GUARD                  = Monthly LLM spend budget set by CFO; auto-throttle at 90% of budget
TRADITIONAL_ML_PREFERENCE       = Always prefer traditional ML over LLM if task is solvable; LLM = last resort
MODEL_CACHING_POLICY            = Cache identical prompts/inputs for 24 hours (same input → same output)
BATCH_INFERENCE_POLICY          = Non-real-time AI tasks (skill gap analysis, placement probability) run as batch — not real-time API
AI_COST_PER_USER_ALERT          = Monthly AI cost per MAU exceeds ₹ [THRESHOLD set by CFO] → investigation
UNUSED_MODEL_POLICY             = AI models with < 100 requests/month → decommission or merge
AI_SPEND_APPROVAL               = Any new AI/ML API integration > ₹5,000/month requires CFO + CTO approval before production
```

---

## 8️⃣ THIRD-PARTY SAAS & PAYMENT GATEWAY COSTS

```
THIRD_PARTY_APPROVAL_POLICY     = Any new SaaS tool > ₹2,000/month requires CFO approval before purchase
DUPLICATE_TOOL_POLICY           = No two tools may serve identical primary function without written justification
ANNUAL_CONTRACT_POLICY          = Prefer annual contracts for tools in use > 6 months; typically 20–40% discount
UNUSED_TOOL_POLICY              = SaaS tool with < 5 active users per month → mandatory review; likely cancellation
```

| Vendor | Category | Billing Model | Cost Driver | FinOps Action |
|--------|---------|--------------|------------|---------------|
| Razorpay | Payment Gateway (India) | % of transaction | GMV volume | Negotiate rate at scale; monitor failed payment cost |
| Stripe | Payment Gateway (International) | % of transaction | GMV volume | Enable Stripe Radar to reduce fraud losses |
| PayU | Payment Gateway (Backup) | % of transaction | Fallback volume | Keep active; avoid lock-in |
| Jasmin SMS Gateway | SMS OTP Delivery | Per SMS | OTP volume + alerts | Optimize OTP resend limits; use push notification where possible |
| ntfy | Push Notifications | Self-hosted | Compute only | No variable cost |
| Docker Mail Server + Postfix | Email | Self-hosted | Compute + bandwidth | No variable cost |
| GitHub / GitLab | CI/CD | Per seat + minutes | Developer count + CI minutes | Optimize CI pipeline duration; use caching aggressively |
| Unleash | Feature Flags | Self-hosted | Compute only | No variable cost |

**Payment Gateway FinOps:**
```
PAYMENT_PROCESSING_COST_TARGET  = ≤ 2% of Gross Revenue
FAILED_PAYMENT_COST             = Track separately; high failure rate → payment method UX review
PAYMENT_GATEWAY_RECONCILIATION  = Daily automated reconciliation via webhook + bank statement match
CHARGEBACK_RATE_ALERT           = Chargeback rate > 0.5% → immediate investigation + gateway notification
FOREX_CONVERSION_POLICY         = International payments: USD invoiced; INR settled; track FX loss monthly
```

---

## 9️⃣ PEOPLE COST FINOPS (HUMAN CAPITAL)

```
PEOPLE_COST_AS_%_REVENUE        = Track monthly; target trajectory toward ≤ 50% at scale
CONTRACTOR_VS_FTE_POLICY        = Contractors for time-bound projects; FTEs for core product/engineering
ESOP_ACCOUNTING                 = Non-cash charge; tracked separately from cash burn
RECRUITMENT_COST_TRACKING       = Per hire; attributed to hiring department cost center
```

### People Cost Attribution by Function

| Function | Cost Center | Cost Items | Cost Attribution Method |
|----------|------------|-----------|------------------------|
| Engineering (Core) | `engineering` | Salary + benefits + tools | Per service owned |
| Engineering (AI/ML) | `ai-ml` | Salary + GPU credits + data tooling | Per AI model deployed |
| Product | `product` | Salary + design tools + research | Per module |
| DevOps / Platform | `engineering` | Salary + infra tooling | Shared overhead — spread across modules |
| Sales (B2B) | `sales` | Salary + commission + travel | Per deal closed; pipeline attribution |
| Marketing | `marketing` | Salary + paid spend + content | Per campaign; CAC attribution |
| Customer Success | `operations` | Salary + tooling | Per customer tier managed |
| Finance & Legal | `gna` | Salary + professional fees | Overhead — spread across G&A |
| HR | `gna` | Salary + HRMS tooling | Overhead |

**People FinOps Rules:**
```
REVENUE_PER_EMPLOYEE_TRACKING   = Calculated monthly; improves as ARR scales; floor set by CFO
CONTRACTOR_BUDGET_CAP           = Max 25% of total engineering cost in contractors; excess requires CFO approval
ESOP_VESTING_COST               = Modeled monthly in cash flow forecasts (non-cash but dilution impact tracked)
HIRING_PLAN_COST_GATE           = Every new hire approved only after role cost modeled in annual budget
REDUNDANCY_REVIEW               = If revenue-per-employee drops QoQ for 2 consecutive quarters → org review triggered
```

---

## 🔟 BUDGET FRAMEWORK (LOCKED STRUCTURE)

### Annual Budget Cycle

```
BUDGET_CYCLE                    = April 1 → March 31 (aligned to Indian fiscal year)
BUDGET_PROCESS_TIMELINE:
  January                       = Department budget submissions due
  February                      = CFO review + challenge sessions
  March 1                       = Board approval of annual budget
  April 1                       = Budget live; tracking begins
  July 15                       = Q1 actuals review; Q2–Q4 reforecast if variance > 10%
  October 15                    = Q2 actuals review; H2 reforecast
  January 15                    = Q3 actuals review; Q4 reforecast + next year planning begins
```

### Budget Structure — Annual Template

```
TOTAL_BUDGET = COGS + OPEX + CAPEX
```

| Budget Category | Sub-Category | Budget Owner | Q1 | Q2 | Q3 | Q4 | FY Total | Notes |
|----------------|-------------|-------------|----|----|----|----|---------|-------|
| **COGS** | | | | | | | | |
| | Infrastructure (Compute + DB + Storage + Network) | CTO | | | | | | |
| | AI/ML API Costs | CTO + Head of AI | | | | | | |
| | Media/Realtime Stack | CTO | | | | | | |
| | Payment Processing Fees | CFO | | | | | | |
| | Third-Party SaaS (COGS-linked) | CTO | | | | | | |
| **OPEX — Engineering & Product** | | | | | | | | |
| | Engineering Salaries + Benefits | CTO | | | | | | |
| | DevOps + Platform | CTO | | | | | | |
| | Product + Design | CPO | | | | | | |
| | AI/ML Engineering | CTO | | | | | | |
| | Engineering Tooling (GitHub, etc.) | CTO | | | | | | |
| **OPEX — Sales & Marketing** | | | | | | | | |
| | Sales Salaries + Commission | CSO | | | | | | |
| | Paid Marketing | CMO | | | | | | |
| | Events + Content + Community | CMO | | | | | | |
| **OPEX — Customer Success & Ops** | | | | | | | | |
| | Customer Success Salaries | COO | | | | | | |
| | Support Tooling | COO | | | | | | |
| **OPEX — G&A** | | | | | | | | |
| | Finance + Legal Salaries | CFO + Legal | | | | | | |
| | External Legal + Compliance | CFO | | | | | | |
| | HR Salaries + HRMS | CFO | | | | | | |
| | Audit + Accounting Fees | CFO | | | | | | |
| | Workspace + Office | CFO | | | | | | |
| **CAPEX** | | | | | | | | |
| | Hardware (if any on-prem) | CTO | | | | | | |
| | Software Licenses (perpetual) | CTO | | | | | | |
| **TOTAL BUDGET** | | | | | | | | |

---

## 1️⃣1️⃣ BUDGET CONTROL & VARIANCE MANAGEMENT

```
VARIANCE_REPORTING_FREQUENCY    = Monthly (management) | Quarterly (board)
FAVORABLE_VARIANCE              = Actual < Budget (under-spend); investigate if > 15% under (possible under-delivery)
ADVERSE_VARIANCE                = Actual > Budget (over-spend); triggers below
```

### Variance Escalation Matrix

| Adverse Variance Level | Action Required | Approver |
|-----------------------|-----------------|---------|
| ≤ 5% over budget | Monitor; note in monthly report | Budget Owner |
| 5–10% over budget | Written explanation required; mitigation plan | Budget Owner + CFO |
| 10–20% over budget | CFO review; reforecast may be required | CFO |
| > 20% over budget | Board notification; mandatory reforecast; spending freeze on non-essential | CFO + Board |
| Any single unbudgeted item > ₹10,000 | Prior approval required before commitment | CFO |
| Any single unbudgeted item > ₹1,00,000 | CFO + CTO + CEO approval required | C-Suite |

### Spending Freeze Triggers (Automatic)

```
SPENDING_FREEZE_TRIGGERS:
  Cash runway < 6 months at current burn rate
  Monthly burn exceeds budget by > 25% for 2 consecutive months
  ARR growth < 5% QoQ for 2 consecutive quarters
  Board resolution ordering freeze

SPENDING_FREEZE_SCOPE:
  FROZEN  = New hires | New SaaS subscriptions | Marketing spend | Travel | Events | Capex
  EXEMPT  = Security | Compliance | Legal (statutory) | Critical infra (keeps platform live) | Existing salaries
```

---

## 1️⃣2️⃣ UNIT COST ECONOMICS — FINOPS TARGETS

```
UNIT_COST_PHILOSOPHY            = Every cost must be expressible as a cost per meaningful unit
UNIT_COST_REVIEW_FREQUENCY      = Monthly; trend-tracked quarterly
UNIT_COST_IMPROVEMENT_TARGET    = Improve (reduce) by ≥ 10% YoY as platform scales
```

### Master Unit Cost Table

| Unit Cost Metric | Formula | Current | Target | Owner |
|----------------|---------|---------|--------|-------|
| Cost per MAU | Total COGS / MAU | ₹ [MEASURE] | ₹ [CFO SET] | CTO |
| Cost per paying customer | Total COGS / Paying Customers | ₹ [MEASURE] | ₹ [CFO SET] | CTO |
| Cost per Job Posting | Job Portal Module Cost / Active Postings | ₹ [MEASURE] | ₹ [CFO SET] | Job Portal Lead |
| Cost per Dojo Session | Media+Realtime Cost / Dojo Sessions | ₹ [MEASURE] | ₹ [CFO SET] | Dojo Lead |
| Cost per Interview Session | Media cost / Interview Sessions | ₹ [MEASURE] | ₹ [CFO SET] | Interview Lead |
| Cost per Certificate Issued | Cert Engine Cost / Certs Issued | ₹ [MEASURE] | ₹ [CFO SET] | Skill Engine Lead |
| Cost per AI Request | Total AI Spend / AI API Calls | ₹ [MEASURE] | ₹ [CFO SET] | AI Lead |
| Cost per SMS | Total SMS Cost / SMS Sent | ₹ [MEASURE] | ≤ ₹0.15/SMS | Ops Lead |
| Cost per Email | Total Email Cost / Emails Sent | ₹ [MEASURE] | ≤ ₹0.01/Email | Ops Lead |
| Gross Margin % | (Net Revenue - COGS) / Net Revenue | [MEASURE] | ≥ 70% | CFO |
| Infrastructure as % of Revenue | Total Infra Cost / Net Revenue | [MEASURE] | ≤ 15% | CTO |
| AI Spend as % of Revenue | Total AI Cost / Net Revenue | [MEASURE] | ≤ 5% | CTO |
| People Cost as % of Revenue | Total People Cost / Net Revenue | [MEASURE] | → ≤ 50% at scale | CFO |
| CAC Payback Period | CAC / (ARPU × Gross Margin) | [MEASURE] | ≤ 18 months | CSO + CMO |

---

## 1️⃣3️⃣ COST OPTIMIZATION PROGRAMS (MANDATORY — ALL ACTIVE)

### Program 1: Reserved Capacity & Committed Use

```
RESERVED_CAPACITY_POLICY        = Resources running > 6 months → evaluate reserved/committed pricing
SAVINGS_TARGET                  = 40–60% cost reduction vs on-demand for committed resources
COMMITMENT_APPROVAL             = CFO approval required for any commitment > 12 months
COMMITMENT_REVIEW               = Quarterly — right-size commitments as usage changes
FORBIDDEN                       = Committing to capacity without Prometheus utilization data
```

### Program 2: Spot / Preemptible Instance Optimization

```
SPOT_ELIGIBLE_WORKLOADS         = analytics-pool | batch-pool | AI/ML batch inference | search indexing | Airflow workers
SPOT_INELIGIBLE_WORKLOADS       = core-pool | realtime-pool | media-pool | billing-pool | ops-pool
SPOT_INTERRUPTION_HANDLING      = Graceful shutdown hooks mandatory; checkpointing for long batch jobs
SPOT_SAVINGS_TARGET             = ≥ 60% cost reduction vs on-demand for spot-eligible pools
```

### Program 3: Non-Production Environment Rightsizing

```
DEV_ENVIRONMENT_POLICY          = Scale to zero: 20:00–07:00 IST weekdays; full shutdown weekends
TEST_ENVIRONMENT_POLICY         = Ephemeral; spin up for CI runs only; auto-terminate after pipeline completes
STAGING_ENVIRONMENT_POLICY      = 50% of prod sizing; scale to zero outside business hours
SAVINGS_TARGET                  = Non-prod cost ≤ 15% of prod cost
NON_PROD_COST_ALERT             = Non-prod cost exceeds 25% of prod cost → immediate investigation
```

### Program 4: Data Tiering & Storage Lifecycle

```
DATA_TIERING_MANDATORY          = ClickHouse hot/warm/cold | MinIO hot/warm/cold | Loki retention policies
COLD_STORAGE_SAVINGS            = 70–80% cost reduction vs hot storage
LIFECYCLE_AUTOMATION            = Automated via MinIO ILM + ClickHouse TTL; no manual archiving
ORPHANED_DATA_AUDIT             = Monthly: identify and delete data with no owner or expired retention
```

### Program 5: AI/ML Cost Optimization

```
PROMPT_CACHING                  = Cache identical LLM prompts; 24-hour TTL; track cache hit rate
BATCH_INFERENCE_SHIFT           = Shift non-real-time AI calls to batch (off-peak hours): 02:00–06:00 IST
MODEL_DOWNSIZING                = Regularly test smaller models; use cheapest model that meets accuracy threshold
EMBEDDING_CACHING               = Cache vector embeddings for static content (job descriptions, skill definitions)
AI_SPEND_GUARDRAIL              = Hard monthly budget cap; auto-throttle at 90%; halt at 100%
```

### Program 6: Engineering Cost Hygiene

```
ZOMBIE_RESOURCE_AUDIT           = Weekly: identify resources with 0 traffic for > 7 days → delete or justify
DUPLICATE_ENVIRONMENT_POLICY    = No developer runs a persistent personal cloud environment; use shared dev cluster
CI_CACHE_POLICY                 = Mandatory build caching in GitHub Actions / GitLab CI; cache hit rate target ≥ 70%
DOCKER_IMAGE_OPTIMIZATION       = Multi-stage builds mandatory; final image ≤ 200MB where possible
DEPENDENCY_AUDIT                = Quarterly: remove unused npm/pip/pub packages; reduce supply chain cost + risk
```

### Program 7: Vendor Negotiation Calendar

```
VENDOR_REVIEW_FREQUENCY         = Annual for all contracts; 6-month for contracts > ₹50,000/year
NEGOTIATION_TRIGGERS            = Contract renewal | Usage > 50% growth | New competing product available
NEGOTIATION_TARGETS             = Payment gateways (volume discount) | Cloud (committed use) | SaaS (annual prepay)
CONSOLIDATION_POLICY            = Prefer fewer, deeper vendor relationships over many small vendors
BENCHMARKING                    = Compare vendor pricing vs market annually; renegotiate if > 15% above market
```

---

## 1️⃣4️⃣ FINOPS MONITORING — DASHBOARDS & ALERTS (CANONICAL)

```
MONITORING_STACK                = Prometheus (metrics) + Grafana (dashboards) + Kubecost (K8s cost) + Airflow (cost DAGs)
FINOPS_DASHBOARD_URL            = internal: grafana.ecoskiller.internal/finops (access: CTO + CFO + FinOps team)
ALERT_CHANNEL                   = Slack: #finops-alerts (critical) + #finops-weekly (reports)
ALERT_ESCALATION                = PagerDuty for budget breach alerts during business hours
```

### FinOps Alert Registry (ALL ALERTS MANDATORY)

| Alert Name | Trigger Condition | Severity | Notification Channel | Auto-Action |
|-----------|------------------|---------|---------------------|------------|
| `infra-cost-daily-spike` | Daily infra cost > 150% of 7-day average | P1 | #finops-alerts + CTO | None — human investigation |
| `ai-budget-90pct` | Monthly AI spend ≥ 90% of budget | P1 | #finops-alerts + CTO + CFO | Throttle non-critical AI calls |
| `ai-budget-100pct` | Monthly AI spend = 100% of budget | P0 | PagerDuty + CTO + CFO | Hard halt AI API calls |
| `untagged-resource-detected` | Any resource deployed without required tags | P2 | #finops-alerts | OPA blocks deploy |
| `zombie-resource-detected` | Resource with 0 requests for 7 consecutive days | P2 | #finops-alerts + owner | Auto-ticket created |
| `non-prod-cost-spike` | Non-prod cost > 25% of prod cost | P2 | #finops-alerts + DevOps Lead | None |
| `budget-adverse-variance-10pct` | Any cost center > 10% over monthly budget | P2 | #finops-alerts + CFO | CFO review scheduled |
| `budget-adverse-variance-20pct` | Any cost center > 20% over monthly budget | P1 | #finops-alerts + CFO + CEO | Spending freeze review |
| `storage-cost-spike` | MinIO/ClickHouse cost MoM increase > 40% | P2 | #finops-alerts + Data Lead | None |
| `payment-processing-cost-high` | Payment processing cost > 2.5% of Gross Revenue | P2 | #finops-alerts + CFO | Renegotiation trigger |
| `spot-interruption-rate-high` | Spot interruption rate > 10% in 24 hours | P2 | #finops-alerts + DevOps Lead | None |
| `media-unit-cost-breach` | Dojo/Interview session cost > target × 1.2 | P2 | #finops-alerts + Media Lead | Investigate |
| `runway-below-9-months` | Cash runway < 9 months at current burn | P0 | PagerDuty + CFO + CEO + Board | Board escalation |
| `contractor-budget-breach` | Contractor cost > 25% of total engineering | P2 | #finops-alerts + CFO | CFO review |

---

### FinOps Dashboard Suite (Mandatory Panels)

```
DASHBOARD_1 = DAILY COST OVERVIEW
  - Total daily spend (INR)
  - Spend by cost domain (bar chart)
  - Spend by K8s namespace (treemap)
  - Top 10 most expensive resources
  - Anomaly flags

DASHBOARD_2 = MONTHLY BUDGET TRACKER
  - Budget vs Actual by cost center (waterfall)
  - Burn rate projection to month-end
  - Variance % by department
  - Remaining budget by cost center

DASHBOARD_3 = UNIT ECONOMICS
  - Cost per MAU (trend line)
  - Cost per paying customer (trend line)
  - Infra as % of revenue (trend line)
  - AI spend per user (trend line)
  - Gross margin trend

DASHBOARD_4 = AI / ML COST INTELLIGENCE
  - LLM API call volume + cost (daily)
  - Traditional ML inference cost (daily)
  - Cache hit rate (LLM prompts)
  - AI cost per request (by function)
  - Monthly AI budget consumption gauge

DASHBOARD_5 = MEDIA & REALTIME COST
  - Active sessions (real-time)
  - Participant-minutes (daily)
  - Cost per Dojo session (trend)
  - Cost per Interview session (trend)
  - Bandwidth cost (daily)
  - TURN vs STUN ratio

DASHBOARD_6 = STORAGE INTELLIGENCE
  - MinIO storage by content type (GB)
  - ClickHouse hot/warm/cold split
  - Data growth rate (MoM %)
  - Lifecycle savings captured (₹)
  - Orphaned data detected (GB)

DASHBOARD_7 = OPTIMIZATION TRACKER
  - Reserved capacity coverage %
  - Spot instance savings (₹)
  - Non-prod shutdown savings (₹)
  - Zombie resources cleared (count)
  - CI cache hit rate %
  - Total optimization savings (₹ cumulative)
```

---

## 1️⃣5️⃣ FINOPS GOVERNANCE — ROLES & RESPONSIBILITIES

```
FINOPS_MODEL                    = Centralized FinOps function (CFO-owned) with distributed accountability (each team owns their cost)
FINOPS_COMMITTEE_FREQUENCY      = Monthly (operational) | Quarterly (board-level)
```

| Role | FinOps Responsibility | Reporting |
|------|----------------------|----------|
| CFO | FinOps program ownership; budget approval; board reporting | Board |
| CTO | Infrastructure cost ownership; engineering FinOps culture; AI spend | CFO |
| FinOps Lead (if hired) | Daily cost monitoring; optimization programs; alert management | CFO |
| Engineering Team Leads | Cost accountability for services owned; tag compliance; optimization execution | CTO |
| DevOps Lead | Infrastructure rightsizing; non-prod shutdown; spot optimization; K8s efficiency | CTO |
| AI/ML Lead | AI cost declaration; model optimization; prompt caching; batch scheduling | CTO |
| Product Managers | Cost impact assessment before new feature launch; unit cost targets per feature | CPO + CFO |
| Finance Analyst | Budget tracking; variance reporting; vendor contracts | CFO |

**FinOps Culture Rules:**
```
COST_IN_SPRINT_PLANNING         = Every sprint includes cost impact estimate for new features
COST_IN_ARCHITECTURE_REVIEW     = Every new service/component reviewed for cost implication before approval
COST_IN_POST-MORTEMS            = Every P1/P2 incident includes cost impact analysis
TEAM_COST_LEADERBOARD           = Monthly internal leaderboard: most cost-efficient team (optimization savings)
FINOPS_OKR                      = FinOps has formal OKRs in engineering and product teams
```

---

## 1️⃣6️⃣ FINOPS REPORTING CALENDAR (LOCKED)

| Report | Audience | Frequency | Delivery | Owner |
|--------|---------|-----------|---------|-------|
| Daily Cost Flash | CTO + CFO | Daily | 08:00 IST | Automated (Antigravity) |
| Weekly FinOps Summary | Engineering Leads + CFO | Weekly | Monday 09:00 IST | FinOps Lead |
| Monthly Cost Report (full) | Leadership Team | Monthly | 5th of following month | CFO |
| Monthly Unit Economics | CEO + CFO + CTO | Monthly | 5th of following month | CFO |
| Quarterly FinOps Board Pack | Full Board | Quarterly | With Board Pack | CFO |
| Quarterly Optimization Review | CTO + Engineering Leads | Quarterly | 10th after quarter-end | FinOps Lead + CTO |
| Annual Budget vs Actuals | Board + Auditors | Annual | 30 days after FY-end | CFO |
| Vendor Contract Review | CFO + Legal | Annual (per contract) | 30 days before renewal | Finance |

---

## 1️⃣7️⃣ DATABASE SCHEMA — FINOPS TABLES (CANONICAL)

```sql
-- Cost Events (Raw ingestion from all cost sources)
finops_cost_events (
  id                UUID PK,
  event_date        DATE,
  cost_domain       ENUM(compute, database, object_storage, search, messaging,
                         media_realtime, networking, observability, security,
                         communication, cicd_devops, ai_ml, third_party_saas,
                         people, licensing, compliance_legal, workspace, marketing),
  resource_id       VARCHAR,          -- Cloud resource ID or service name
  resource_name     VARCHAR,
  k8s_namespace     VARCHAR,
  module_tag        VARCHAR,          -- job-portal | skill-engine | dojo | erp | billing | shared
  service_tag       VARCHAR,          -- Exact microservice name
  environment_tag   ENUM(dev, test, staging, prod),
  tenant_tier_tag   VARCHAR,
  cost_center_tag   VARCHAR,
  owner_tag         VARCHAR,
  stage_tag         ENUM(stage-1, stage-2, stage-3, stage-4),
  amount_inr        DECIMAL(15, 4),
  amount_usd        DECIMAL(15, 6),   -- For cloud vendor USD invoices
  fx_rate           DECIMAL(10, 6),
  currency_source   CHAR(3),
  cost_source       ENUM(cloud_api, kubecost, vendor_invoice, manual_entry),
  is_tagged         BOOLEAN DEFAULT TRUE,
  created_at        TIMESTAMPTZ
)

-- Monthly Budget Allocations
finops_budgets (
  id                UUID PK,
  fiscal_year       CHAR(7),          -- e.g., FY25-26
  fiscal_quarter    ENUM(Q1, Q2, Q3, Q4),
  fiscal_month      DATE,             -- First day of month
  cost_center       VARCHAR,
  cost_domain       VARCHAR,
  budget_owner      VARCHAR,
  budget_amount_inr DECIMAL(15, 4),
  approved_by       VARCHAR,
  approved_at       TIMESTAMPTZ,
  version           INTEGER DEFAULT 1, -- Increments on reforecast
  is_reforecast     BOOLEAN DEFAULT FALSE,
  created_at        TIMESTAMPTZ
)

-- Budget vs Actuals (Computed monthly)
finops_budget_vs_actuals (
  id                UUID PK,
  fiscal_month      DATE,
  cost_center       VARCHAR,
  cost_domain       VARCHAR,
  budget_amount     DECIMAL(15, 4),
  actual_amount     DECIMAL(15, 4),
  variance_amount   DECIMAL(15, 4),   -- actual - budget
  variance_pct      DECIMAL(8, 4),    -- variance / budget * 100
  variance_status   ENUM(favorable, on_track, adverse_minor, adverse_major, adverse_critical),
  notes             TEXT,
  created_at        TIMESTAMPTZ
)

-- Unit Cost Metrics (Computed daily)
finops_unit_costs (
  id                UUID PK,
  metric_date       DATE,
  metric_name       VARCHAR,          -- e.g., cost_per_mau, cost_per_dojo_session
  numerator         DECIMAL(15, 4),   -- Cost
  denominator       DECIMAL(15, 4),   -- Unit count (MAU, sessions, etc.)
  unit_cost         DECIMAL(15, 6),   -- numerator / denominator
  target            DECIMAL(15, 6),
  variance_vs_target DECIMAL(8, 4),
  module_scope      VARCHAR,
  created_at        TIMESTAMPTZ
)

-- AI Cost Registry
finops_ai_cost_registry (
  id                UUID PK,
  ai_function       VARCHAR,          -- e.g., resume_parsing, job_match_scoring
  model_class       ENUM(llm, traditional_ml, rule_engine),
  model_type        VARCHAR,          -- e.g., GPT-4o-mini, GradientBoosting
  cost_per_1k_req   DECIMAL(12, 6),
  monthly_volume    INTEGER,
  monthly_cost_est  DECIMAL(12, 4),
  monthly_cost_act  DECIMAL(12, 4),
  cache_hit_rate    DECIMAL(6, 4),
  justification     TEXT,             -- Why AI vs rule engine
  declared_by       VARCHAR,          -- Engineer who declared cost
  approved_by       VARCHAR,          -- CTO approval
  status            ENUM(active, deprecated, under_review),
  created_at        TIMESTAMPTZ,
  updated_at        TIMESTAMPTZ
)

-- Optimization Savings Log
finops_optimization_savings (
  id                UUID PK,
  program_name      VARCHAR,          -- e.g., reserved_capacity, spot_instances, data_tiering
  period_start      DATE,
  period_end        DATE,
  cost_before       DECIMAL(15, 4),
  cost_after        DECIMAL(15, 4),
  savings_amount    DECIMAL(15, 4),
  savings_pct       DECIMAL(6, 4),
  description       TEXT,
  implemented_by    VARCHAR,
  created_at        TIMESTAMPTZ
)

-- Vendor Contracts
finops_vendor_contracts (
  id                UUID PK,
  vendor_name       VARCHAR,
  service_category  VARCHAR,
  billing_model     ENUM(fixed, usage_based, per_seat, commission, hybrid),
  annual_value_inr  DECIMAL(15, 4),
  contract_start    DATE,
  contract_end      DATE,
  auto_renew        BOOLEAN,
  renewal_notice_days INTEGER,
  next_review_date  DATE,
  approved_by       VARCHAR,
  cancellation_terms TEXT,
  notes             TEXT,
  created_at        TIMESTAMPTZ
)

-- FinOps Alerts Log
finops_alerts_log (
  id                UUID PK,
  alert_name        VARCHAR,
  triggered_at      TIMESTAMPTZ,
  severity          ENUM(P0, P1, P2, P3),
  trigger_value     DECIMAL(15, 4),
  threshold_value   DECIMAL(15, 4),
  notified_to       TEXT[],           -- Array of notified parties
  auto_action       VARCHAR NULLABLE,
  resolved_at       TIMESTAMPTZ NULLABLE,
  resolved_by       VARCHAR NULLABLE,
  resolution_notes  TEXT NULLABLE,
  created_at        TIMESTAMPTZ
)
```

---

## 1️⃣8️⃣ FOUR-STAGE FINOPS MATURITY (LOCKED ALIGNMENT)

```
STAGE 1 (Foundation)
  FinOps Maturity  = CRAWL
  Requirements:
    → All resources tagged from day one (OPA enforced)
    → Daily cost visibility dashboard live
    → Monthly budget set and tracked
    → AI cost declaration mandatory before any AI deploy
    → Non-prod auto-shutdown enforced

STAGE 2 (Intelligence)
  FinOps Maturity  = WALK
  Requirements:
    → Unit cost metrics calculated and trended
    → AI cost optimization programs active (caching, batch, model downsizing)
    → Reserved capacity evaluated and implemented for stable workloads
    → Spot instances live for analytics and batch pools
    → Budget variance alerts automated

STAGE 3 (Ecosystem)
  FinOps Maturity  = RUN
  Requirements:
    → Full data tiering live (ClickHouse hot/warm/cold; MinIO lifecycle)
    → Vendor negotiation cycle active
    → FinOps OKRs embedded in all engineering teams
    → Cost-per-module accountability fully delegated to module leads
    → Quarterly optimization savings tracked and reported to board

STAGE 4 (Compliance & Scale)
  FinOps Maturity  = OPTIMIZE
  Requirements:
    → Fully automated FinOps pipeline (Airflow DAGs for all cost attribution)
    → Predictive cost forecasting (ML model on cost trends)
    → Multi-cloud cost comparison (if applicable)
    → FinOps integrated into SOC2 evidence pack
    → Board-level FinOps KPIs in quarterly pack
```

---

## 1️⃣9️⃣ PROHIBITED ACTIONS (PERMANENTLY LOCKED)

```
❌ Deploying any resource without complete required tags
❌ Using LLMs for tasks solvable by traditional ML or rule engines (violates R28)
❌ Committing to cloud reserved capacity without CFO approval
❌ Running persistent personal developer environments in cloud
❌ Purchasing any SaaS tool > ₹2,000/month without CFO approval
❌ Allowing non-prod environments to run 24/7 without business justification
❌ Storing cold/archive data in hot storage tiers
❌ Ignoring zombie resource alerts for > 7 days
❌ Manual cost data entry into FinOps dashboards without source reference
❌ Bypassing AI cost declaration registry for new AI/ML features
❌ Using cross-region data replication for non-DR purposes (egress cost)
❌ Paying for duplicate tools serving the same primary function
❌ Extending vendor contracts without renegotiation attempt
❌ Attributing infra costs to "shared" without a defined allocation key
❌ Ignoring payment gateway chargeback rate alerts
❌ Overproviding Kubernetes resource requests vs actual usage by > 100%
❌ Silencing FinOps alerts without documented justification
❌ Generating board FinOps reports with manually adjusted numbers
❌ Using downtime to mask infra cost overruns (violates platform governance)
❌ Starting Stage 2+ features without Stage 1 FinOps baseline established
```

---

## 2️⃣0️⃣ COMPLIANCE INHERITANCE (DO NOT DUPLICATE)

This module INHERITS the following already finalized across the Ecoskiller sealed prompt family:

- **Master Prompt:** Kubernetes namespaces, microservice list, four-stage model, tech stack, R28 AI cost law, multi-tenant isolation, environment isolation (dev/test/staging/prod)
- **REVENUE_RECOGNITION.md:** All financial definitions (MRR, ARR, Gross Margin, COGS) — FinOps uses identical definitions; no re-definition permitted
- **BOARD_REPORTING.md:** Unit economics, infrastructure cost report, engineering velocity report formats — FinOps is the data source; board reporting is the consumer
- Authorization (RBAC + ABAC) — FinOps dashboard access gated by Auth Service
- Audit Immutability — All cost data events and budget approvals logged in immutable audit trail
- Tenant Isolation — Cost attribution must never mix tenant data without proper aggregation
- Data Encryption — FinOps database tables with financial data encrypted at rest (AES-256)

---

## 2️⃣1️⃣ VERSION CONTROL

```
DOCUMENT_VERSION                = 1.0.0
STATUS                          = SEALED & LOCKED
CREATED_BY                      = Antigravity FinOps Architecture Team
APPROVED_BY                     = [CFO + CTO SIGN-OFF REQUIRED BEFORE IMPLEMENTATION]
NEXT_REVIEW                     = Quarterly OR on any major infrastructure or pricing change
CHANGE_POLICY                   = ADD_ONLY; removal of any cost domain requires CFO + CTO approval + version bump
PARENT_MODULES:
  [1] Ecoskiller SEALED & LOCKED MASTER PROMPT v1.0
  [2] ANTIGRAVITY :: REVENUE_RECOGNITION.md v1.0.0
  [3] ANTIGRAVITY :: BOARD_REPORTING.md v1.0.0
CROSS_REFERENCES:
  → R28 (Intelligence Cost Optimization Law) — master prompt
  → K8s Namespaces: core | realtime | media | billing | analytics | admin | ops — master prompt
  → Microservices 1–15 cost attribution — master prompt
  → Unit economics definitions — REVENUE_RECOGNITION.md
  → Infrastructure cost report format — BOARD_REPORTING.md Track D
```

---

*🔒 END OF SEALED DOCUMENT — FINOPS.md — ANTIGRAVITY*
*Unauthorized modification constitutes a financial governance breach.*
*All changes require CFO + CTO approval + version bump.*
*Cost data sourced exclusively from Antigravity automated pipelines — no manual overrides permitted.*
