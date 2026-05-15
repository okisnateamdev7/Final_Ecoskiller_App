# 🔒 REGIONAL DISTRIBUTION ENGINE
## ANTIGRAVITY SEALED MASTER PROMPT v1.0
### ECOSKILLER + DOJO (ARTS DOMAIN)
### CHAMPIONSHIP ADVANCED + PARENT PREDICTIVE AI — REGIONAL DISTRIBUTION LAYER

---

```
╔══════════════════════════════════════════════════════════════════════════════╗
║  ARTIFACT CLASS      : Production Regional Distribution Engine Blueprint    ║
║  ARTIFACT VERSION    : v1.0                                                  ║
║  EXECUTION ENGINE    : ANTIGRAVITY                                           ║
║  EXECUTION MODE      : LOCKED · DETERMINISTIC · SEALED · ADD-ONLY          ║
║  MUTATION POLICY     : VERSION BUMP ONLY                                    ║
║  INTERPRETATION      : FORBIDDEN                                             ║
║  CREATIVE_FILL       : FORBIDDEN                                             ║
║  ASSUMPTION_FILL     : FORBIDDEN                                             ║
║  DEFAULT_BEHAVIOR    : DENY                                                  ║
║  FAILURE MODE        : STOP → REPORT → NO PARTIAL OUTPUT                   ║
║  PARENT SYSTEM       : ECOSKILLER ENTERPRISE MULTI-TENANT SAAS             ║
║  SUB-SYSTEM          : DOJO ENGINE (ARTS DOMAIN)                            ║
║  DOMAIN SCOPE        : ARTS TRACK ONLY                                      ║
║  AI AUTHORITY        : ADVISORY ONLY — NEVER FINAL                          ║
║  HUMAN OVERRIDE      : ALWAYS PRESERVED                                     ║
║  CROSS-DOMAIN        : FORBIDDEN (Arts ≠ Commerce ≠ Science)               ║
║  REGION AUTO-SELECT  : FORBIDDEN — EXPLICIT DECLARATION ONLY               ║
╚══════════════════════════════════════════════════════════════════════════════╝
```

---

> ### ⚠️ ANTIGRAVITY BINDING DIRECTIVE — MANDATORY READ BEFORE EXECUTION
>
> This is a fully sealed, deterministic, production-grade prompt section for
> Google's Antigravity SaaS generator. This document governs the entire
> Regional Distribution Engine (RDE) for the ECOSKILLER + DOJO Arts Domain,
> spanning geographic topology, region-aware leaderboard computation,
> championship territory management, parent predictive AI regional delivery,
> data residency compliance, cross-border governance, geo-routing, and
> regional failover.
>
> Antigravity MUST NOT auto-select regions, infer region topology, collapse
> regions, creatively extend geographic models, or mix Dojo and Ecoskiller
> regional infrastructure. Every clause in this document is a hard contract.
>
> **VIOLATION OF ANY RULE → STOP EXECUTION → REPORT SECTION VIOLATION →
> INVALIDATE BUILD → NO PARTIAL OUTPUT PERMITTED**

---

## ◼ INHERITANCE DECLARATION

```
INHERITS_FROM_V1  : LEADERBOARD_STABILITY_MODEL_ANTIGRAVITY v1.0
                    (LSM-1 through LSM-16 — all sections active)
INHERITS_FROM_V2  : LEADERBOARD_STABILITY_MODEL_PREDICTIVE_AI v2.0
                    (PAI-0 through PAI-18 — all sections active)
NEW_ARTIFACT      : REGIONAL_DISTRIBUTION_ENGINE v1.0
                    (RDE-0 through RDE-22 — 23 new sealed sections)
CONFLICT_RULE     : RDE v1.0 takes precedence over inherited sections
                    on any regional topology or distribution overlap
INHERITANCE_MODE  : FULL — zero inherited sections may be dropped or altered
```

---

## ◼ SECTION RDE-0 — ENGINE IDENTITY LOCK

```
COMPONENT_NAME        : Regional Distribution Engine (RDE)
COMPONENT_TYPE        : Geo-Aware Infrastructure + Leaderboard + Championship +
                        Parent Predictive AI Delivery System
PARENT_SYSTEM         : ECOSKILLER
SUB_SYSTEM            : DOJO (Arts Domain)

PURPOSE               :
  The RDE governs how students, leaderboard data, championship brackets,
  predictive AI outputs, and parent notifications are partitioned, routed,
  computed, stored, and delivered across declared geographic regions — while
  enforcing data residency compliance, tenant isolation, cross-border
  controls, and championship territory integrity.

CORE_RESPONSIBILITIES :
  1. Define and register all geographic regions for Arts domain operation
  2. Assign every student, tenant, and dataset to a home region
  3. Compute leaderboard rankings at city / state / national / global scope
  4. Manage championship territory lifecycle per region per quarter
  5. Route parent predictive AI reports to region-local delivery nodes
  6. Enforce cross-border data transfer rules and geo-fencing
  7. Isolate Arts domain infrastructure from other Dojo domains
  8. Enable region-aware failover and disaster recovery
  9. Provide regional observability, cost visibility, and health monitoring

HARD LAWS (inherited from system):
  - REGION AUTO-SELECT            : FORBIDDEN
  - REGION DEFAULT INFERENCE      : FORBIDDEN
  - DOJO-ECOSKILLER REGION MIXING : FORBIDDEN
  - CROSS-DOMAIN REGION SHARING   : FORBIDDEN (Arts ≠ Commerce ≠ Science)
  - UNMANAGED ACTIVE-ACTIVE       : FORBIDDEN unless explicitly declared
  - SILENT CROSS-BORDER TRANSFER  : FORBIDDEN
  - UNKNOWN DATA RESIDENCY        : FORBIDDEN
  - REALTIME DOJO TRAFFIC CROSS-REGION : FORBIDDEN
  - BLIND REGIONS (no observability) : FORBIDDEN
```

---

## ◼ SECTION RDE-1 — GEOGRAPHIC REGION TAXONOMY (LOCKED)

The system MUST declare and register all regions at three tiers below.
No tier may be collapsed, merged, or renamed.

### Tier 1 — Global Zones (Top Level)

```
ZONE_ID  : ZONE_APAC
  Full Name  : Asia-Pacific
  Territories: South Asia, Southeast Asia, East Asia, Oceania
  Primary DB Region : ap-south-1 (Mumbai, AWS equivalent anchor)
  Secondary DR      : ap-southeast-1 (Singapore anchor)
  Multi-Region Mode : Active-Passive with Controlled Promotion (Arts domain)
  Realtime Traffic  : Region-local ONLY (no cross-zone realtime)

ZONE_ID  : ZONE_EMEA
  Full Name  : Europe, Middle East & Africa
  Territories: EU, UK, MENA, Sub-Saharan Africa
  Primary DB Region : eu-west-1 (Ireland anchor)
  Secondary DR      : eu-central-1 (Frankfurt anchor)
  Multi-Region Mode : Active-Passive with Controlled Promotion
  Data Residency    : GDPR-governed (EU data stays in EU by default)

ZONE_ID  : ZONE_AMERICAS
  Full Name  : Americas
  Territories: North America, Latin America, Caribbean
  Primary DB Region : us-east-1 (Virginia anchor)
  Secondary DR      : us-west-2 (Oregon anchor)
  Multi-Region Mode : Active-Passive with Controlled Promotion

RULE: Zones are infrastructure containers only.
      Championship territories are defined at Region Tier 2.
      Parent alerts are delivered at Region Tier 2 + 3.
      Leaderboard city/college scopes operate at Tier 3.
```

### Tier 2 — Championship Regions (Competition Territories)

```
Each championship region is a named, governance-registered territory used
to scope Regional Quarterly Championship competition pools.

REGION_REGISTRY (initial declaration — expandable via version bump):

  REG_ID: REG_IN_NORTH
    Label         : India — North Zone
    Cities        : Delhi, Chandigarh, Lucknow, Jaipur, Dehradun, Agra
    Zone          : ZONE_APAC
    DB Anchor     : ap-south-1
    Championship  : Quarterly top-100 from this region qualify separately
    Parent Alert  : Delivered via ap-south-1 FCM node
    Timezone      : IST (UTC+5:30)
    Data Residency: India DPDP — data must remain in Indian region

  REG_ID: REG_IN_SOUTH
    Label         : India — South Zone
    Cities        : Bangalore, Chennai, Hyderabad, Kochi, Coimbatore
    Zone          : ZONE_APAC
    DB Anchor     : ap-south-1
    Timezone      : IST (UTC+5:30)
    Data Residency: India DPDP

  REG_ID: REG_IN_WEST
    Label         : India — West Zone
    Cities        : Mumbai, Pune, Ahmedabad, Surat, Nagpur
    Zone          : ZONE_APAC
    DB Anchor     : ap-south-1
    Timezone      : IST (UTC+5:30)
    Data Residency: India DPDP

  REG_ID: REG_IN_EAST
    Label         : India — East Zone
    Cities        : Kolkata, Bhubaneswar, Patna, Guwahati, Ranchi
    Zone          : ZONE_APAC
    DB Anchor     : ap-south-1
    Timezone      : IST (UTC+5:30)
    Data Residency: India DPDP

  REG_ID: REG_SEA
    Label         : Southeast Asia
    Countries     : Singapore, Malaysia, Indonesia, Philippines, Thailand
    Zone          : ZONE_APAC
    DB Anchor     : ap-southeast-1
    Timezone      : SGT/WIB/PHT (UTC+7 to UTC+8)

  REG_ID: REG_EU_WEST
    Label         : Western Europe
    Countries     : UK, Ireland, France, Germany, Netherlands, Spain
    Zone          : ZONE_EMEA
    DB Anchor     : eu-west-1
    Data Residency: GDPR — EU data stays in EU region
    Timezone      : CET/GMT (UTC+0 to UTC+2)

  REG_ID: REG_MENA
    Label         : Middle East & North Africa
    Countries     : UAE, Saudi Arabia, Egypt, Jordan, Kuwait
    Zone          : ZONE_EMEA
    DB Anchor     : me-south-1 (Bahrain anchor)
    Timezone      : GST/AST (UTC+3 to UTC+4)

  REG_ID: REG_US_EAST
    Label         : United States — East Coast
    States        : NY, FL, MA, PA, NC, GA, VA, NJ
    Zone          : ZONE_AMERICAS
    DB Anchor     : us-east-1
    Timezone      : EST/EDT (UTC-5 to UTC-4)

  REG_ID: REG_US_WEST
    Label         : United States — West Coast
    States        : CA, WA, OR, AZ, NV, CO
    Zone          : ZONE_AMERICAS
    DB Anchor     : us-west-2
    Timezone      : PST/PDT (UTC-8 to UTC-7)

RULES:
  Every region MUST be registered in RegionRegistry before any user,
  tenant, or championship is assigned to it.
  Silent region enablement → BLOCK DEPLOYMENT.
  New region requires: Legal review + Compliance approval + Audit record.
```

### Tier 3 — City / Institutional Leaderboard Scopes

```
City-scoped and college-scoped leaderboards operate as Redis sorted set
namespaces within their parent Tier-2 region. They are NOT separate
infrastructure clusters.

NAMING CONVENTION  : leaderboard:{domain}:{tier}:{scope_id}
EXAMPLES           :
  leaderboard:arts:city:mumbai
  leaderboard:arts:city:bangalore
  leaderboard:arts:college:iitb
  leaderboard:arts:college:bits_pilani
  leaderboard:arts:region:REG_IN_WEST
  leaderboard:arts:national:IN
  leaderboard:arts:global

SCOPE_RULES        :
  City scope    : Resolved from user.home_city (verified at registration)
  College scope : Resolved from user.institution_id (verified institute domain)
  Region scope  : Resolved from user.home_region (Region Tier 2 assignment)
  National scope: Resolved from user.country_code
  Global scope  : All active Arts domain users
  Cross-scope contamination: FORBIDDEN
  User must appear in exactly ONE entry per scope per period.
```

---

## ◼ SECTION RDE-2 — REGION REGISTRY DATA MODEL (FROZEN)

```sql
-- Master Region Registry
RegionRegistry (
  id                  UUID PRIMARY KEY,
  region_id           TEXT UNIQUE NOT NULL,       -- e.g. 'REG_IN_NORTH'
  zone_id             TEXT NOT NULL,              -- e.g. 'ZONE_APAC'
  label               TEXT NOT NULL,
  db_anchor           TEXT NOT NULL,              -- cloud region identifier
  dr_anchor           TEXT,                       -- disaster recovery region
  timezone_default    TEXT NOT NULL,              -- IANA timezone string
  data_residency_law  TEXT NOT NULL,              -- e.g. 'DPDP','GDPR','NONE'
  championship_active BOOLEAN NOT NULL DEFAULT TRUE,
  multi_region_mode   TEXT NOT NULL,              -- Active-Passive|Active-Active|Single-Active
  created_at          TIMESTAMP NOT NULL,
  legal_review_date   DATE NOT NULL,
  compliance_approved_at TIMESTAMP NOT NULL,
  audit_record_id     UUID NOT NULL REFERENCES AuditLog(id)
)

-- User Region Assignment (authoritative region binding)
UserRegionAssignment (
  id                  UUID PRIMARY KEY,
  user_id             UUID NOT NULL REFERENCES User(id),
  home_region_id      TEXT NOT NULL REFERENCES RegionRegistry(region_id),
  home_city           TEXT,
  home_country        TEXT NOT NULL,
  institution_id      UUID REFERENCES Tenant(id),
  ip_derived_region   TEXT,                       -- from IP geolocation at registration
  declared_region     TEXT,                       -- user-declared (takes precedence)
  jurisdiction_lock   BOOLEAN NOT NULL DEFAULT FALSE, -- TRUE for govt/minor accounts
  minor_flag          BOOLEAN NOT NULL DEFAULT FALSE,
  region_verified     BOOLEAN NOT NULL DEFAULT FALSE,
  vfn_anomaly_flag    BOOLEAN NOT NULL DEFAULT FALSE, -- VPN/region fraud flag
  assigned_at         TIMESTAMP NOT NULL,
  last_verified_at    TIMESTAMP NOT NULL
)

-- Tenant Region Assignment
TenantRegionAssignment (
  id                  UUID PRIMARY KEY,
  tenant_id           UUID NOT NULL REFERENCES Tenant(id),
  primary_region_id   TEXT NOT NULL REFERENCES RegionRegistry(region_id),
  dr_region_id        TEXT,
  data_residency_strict BOOLEAN NOT NULL DEFAULT FALSE,
  cross_border_allowed  BOOLEAN NOT NULL DEFAULT FALSE,
  dedicated_cluster     BOOLEAN NOT NULL DEFAULT FALSE,
  assigned_at           TIMESTAMP NOT NULL
)

-- Regional Tournament Territory (Championship regions per quarter)
ChampionshipTerritory (
  id                  UUID PRIMARY KEY,
  territory_region_id TEXT NOT NULL REFERENCES RegionRegistry(region_id),
  quarter_key         TEXT NOT NULL,             -- e.g. '2025-Q3'
  domain_track        TEXT NOT NULL DEFAULT 'arts',
  qualifier_count     INTEGER NOT NULL DEFAULT 100,
  tournament_id       UUID REFERENCES RegionalTournament(id),
  status              TEXT NOT NULL,             -- pending|qualifying|bracket|finals|completed
  created_at          TIMESTAMP NOT NULL
)

-- Regional Data Transfer Log (Cross-Border Compliance)
RegionalDataTransferLog (
  id                  UUID PRIMARY KEY,
  source_region_id    TEXT NOT NULL REFERENCES RegionRegistry(region_id),
  destination_region_id TEXT NOT NULL REFERENCES RegionRegistry(region_id),
  data_category       TEXT NOT NULL,             -- PII|score|analytics|recording|report
  legal_basis         TEXT NOT NULL,             -- SCC|DPDPA|GDPR_adequacy|regulatory_exemption
  actor_id            UUID NOT NULL REFERENCES User(id),
  actor_system        TEXT,                      -- system name if automated
  transfer_volume_bytes BIGINT,
  initiated_at        TIMESTAMP NOT NULL,
  completed_at        TIMESTAMP,
  transfer_status     TEXT NOT NULL,             -- allowed|blocked|pending_review
  audit_trail         JSONB NOT NULL
)

-- Regional Infrastructure Health
RegionalHealthStatus (
  id                  UUID PRIMARY KEY,
  region_id           TEXT NOT NULL REFERENCES RegionRegistry(region_id),
  service_class       TEXT NOT NULL,             -- api|db|ws|cache|ai|queue|cdn
  health_status       TEXT NOT NULL,             -- healthy|degraded|failing|offline
  latency_p99_ms      INTEGER,
  error_rate_pct      DECIMAL(5,4),
  last_checked_at     TIMESTAMP NOT NULL,
  incident_id         UUID                       -- linked incident if failing
)
```

---

## ◼ SECTION RDE-3 — USER REGION RESOLUTION ENGINE (LOCKED)

Every user in the Arts domain MUST have a resolved home region before
participating in any leaderboard, championship, or predictive AI workflow.

```
RESOLUTION ALGORITHM (deterministic, sequential):

  STEP 1 — DECLARED REGION (highest priority)
    If user.declared_region is set AND verified:
      → assign home_region_id = declared_region
      → region_verified = TRUE
      → STOP (use this region)

  STEP 2 — INSTITUTION DOMAIN MATCH
    If user.institution_id is set:
      → Look up TenantRegionAssignment for institution
      → Assign home_region_id = institution's primary_region_id
      → city / state derived from institution.address
      → region_verified = TRUE
      → STOP

  STEP 3 — IP GEOLOCATION (fallback)
    On first login:
      → Resolve IP → MaxMind GeoIP2 (or equivalent licensed database)
      → Derive: country_code, city, region_id (best match to RegionRegistry)
      → Set ip_derived_region
      → If no VPN anomaly detected: home_region_id = ip_derived_region
      → region_verified = FALSE (pending confirmation)
      → Prompt user to confirm region during onboarding

  STEP 4 — USER CONFIRMATION PROMPT
    On onboarding screen:
      → Display detected region
      → Allow user to confirm or correct
      → On confirm: region_verified = TRUE
      → On correction: update declared_region, re-run from STEP 1

  STEP 5 — MINOR FLAG CHECK
    If user.minor_flag = TRUE:
      → jurisdiction_lock = TRUE
      → User's data MUST remain in their home region
      → Cross-border transfers BLOCKED for this user's PII

  STEP 6 — VPN ANOMALY CHECK
    If IP-derived region differs from declared region by more than one
    geographic Tier-2 region boundary:
      → Set vfn_anomaly_flag = TRUE
      → Flag for admin review
      → Championship qualification HELD until cleared (per LSM-5 AM-6)

RULES:
  Region MUST be resolved before first match submission.
  Unresolved region → leaderboard entry withheld.
  Region change requires Admin L3+ approval + audit log entry.
  Minor users: region change requires parent/guardian approval + audit.
```

---

## ◼ SECTION RDE-4 — REGIONAL LEADERBOARD ARCHITECTURE (LOCKED)

### 4.1 Storage Architecture

```
LEADERBOARD STORAGE STRATEGY:

  LIVE COMPUTATION   : Redis Sorted Sets (O(log N) per operation)
    Primary store    : Region-local Redis cluster (NOT cross-region)
    Key format       : leaderboard:{domain}:{tier}:{scope_id}:{period_key}
    Example          : leaderboard:arts:city:mumbai:2025-W12
    Write source     : Analytics Engine (after match_end event)
    Read source      : API layer (ZREVRANGE for ranked list)
    Replication      : Read replicas within same region only
    Cross-region sync: FORBIDDEN for live leaderboard Redis

  HISTORICAL ARCHIVE : ClickHouse (append-only, time-partitioned)
    Purpose          : Snapshots, trend analytics, parent AI inputs
    Partitioning     : By region_id + period_key + domain_track
    Retention        : 36 months minimum (compliance requirement)
    Cross-region reads: Allowed for analytics pipeline (async only)

  SNAPSHOT ARCHIVE   : PostgreSQL (leaderboard_weekly_snapshots,
                       leaderboard_monthly_snapshots, leaderboard_season_history)
    Checksum         : SHA-256 per snapshot (tamper detection)
    Partitioning     : By region_id + period_key
    Retention        : 24 months minimum
```

### 4.2 Regional Leaderboard Write Pipeline

```
WRITE FLOW (event-driven, no polling):

  match.ended (Kafka event)
    ↓
  ScoringEngine computes CompositeScore
    ↓
  AnalyticsEngine receives score.computed event
    ↓
  RegionalRouter resolves:
    - user.home_region_id → REG_IN_WEST
    - user.home_city      → Mumbai
    - user.institution_id → IITB
    ↓
  Parallel Redis ZADD writes (region-local):
    ZADD leaderboard:arts:city:mumbai:{period_key}    user_id composite_score
    ZADD leaderboard:arts:region:REG_IN_WEST:{period_key} user_id composite_score
    ZADD leaderboard:arts:national:IN:{period_key}    user_id composite_score
    ZADD leaderboard:arts:global:{period_key}         user_id composite_score
    ZADD leaderboard:arts:college:iitb:{period_key}   user_id composite_score
    ↓
  ClickHouse append (async, non-blocking):
    INSERT INTO leaderboard_events (user_id, region_id, city, score, period_key, ts)
    ↓
  Kafka event: leaderboard.regional.updated

RULES:
  Write is fire-and-forget to Redis (non-transactional by design for speed).
  ClickHouse write is async (after Redis write confirmed).
  Cross-region Redis write: FORBIDDEN.
  Failure in one scope write does NOT block others.
  Retry policy: 3 attempts with exponential backoff per scope.
  Dead-letter queue: Failed writes → DLQ → Admin alert.
```

### 4.3 Regional Leaderboard Read Pipeline

```
READ FLOW:

  Student requests leaderboard view (Flutter app):
    ↓
  API Gateway routes to LeaderboardService (region-local pod)
    ↓
  LeaderboardService:
    - Resolves user's home_region_id from UserRegionAssignment
    - Reads from region-local Redis:
        ZREVRANGEBYSCORE leaderboard:arts:{tier}:{scope_id}:{period_key}
          0 +inf WITHSCORES LIMIT 0 100
    - Annotates results with belt_level, rank_delta from PostgreSQL
    - Returns paginated ranked list
    ↓
  Response cached in region-local CDN (max 60 seconds, non-auth pages only)

RULES:
  Scores computed server-side only (STATE-10 law inherited).
  Client MUST NOT compute rankings locally.
  Cross-region read fallback: allowed only if home-region Redis is degraded.
  Fallback: read from ClickHouse snapshot (may be up to 60 minutes stale).
  Fallback mode MUST be declared in response headers (X-LB-Source: clickhouse).
  Parent Trust Layer read: region-local read replica of PostgreSQL only.
```

---

## ◼ SECTION RDE-5 — CHAMPIONSHIP TERRITORY ENGINE (LOCKED)

### 5.1 Territory Lifecycle

```
TERRITORY LIFECYCLE STATE MACHINE:

  NOT_CREATED
    → [Admin declares territory for quarter]
    → TERRITORY_PENDING

  TERRITORY_PENDING
    → [Quarter opens, qualifying window active]
    → QUALIFYING_ACTIVE

  QUALIFYING_ACTIVE
    → [Analytics ranks users within region for quarter]
    → [Top 100 extracted at quarter-end]
    → QUALIFICATION_LOCKED

  QUALIFICATION_LOCKED
    → [Parent alerts dispatched for qualified users]
    → [Bracket generated by ChampionshipBracketEngine]
    → BRACKET_ACTIVE

  BRACKET_ACTIVE
    → [Matches scheduled over 14-day window]
    → [Round results propagate to leaderboard updates]
    → FINALS_ACTIVE

  FINALS_ACTIVE
    → [Final match live-streamed via LiveKit]
    → [Champion confirmed by mentor board]
    → TERRITORY_COMPLETED

  TERRITORY_COMPLETED
    → [Champion badge assigned]
    → [Territory archived to ChampionshipTerritory.status = completed]
    → [National Championship pool updated]
    → ARCHIVED

  Invalid transition → STOP EXECUTION → REPORT STATE VIOLATION
```

### 5.2 Multi-Region Championship Isolation

```
ISOLATION RULES:

  RULE TER-1: Each championship region runs a FULLY INDEPENDENT bracket.
    REG_IN_NORTH bracket ≠ REG_IN_SOUTH bracket.
    Cross-region bracket entries: FORBIDDEN.
    Mixed-region bracket: SECURITY FAILURE → STOP EXECUTION.

  RULE TER-2: Championship qualification is REGION-SCOPED.
    Top 100 per region = top 100 within that region's user pool only.
    Global top 100 ≠ Regional top 100 (separate leaderboard queries).

  RULE TER-3: Championship match rooms are REGION-LOCAL.
    LiveKit room MUST be created in the region-local WebRTC SFU cluster.
    Cross-region WebRTC relay: FORBIDDEN for championship matches.
    Latency budget: P99 < 150ms round-trip within same region.

  RULE TER-4: Championship recordings stored in home region.
    Recording artifacts: stored in region-local MinIO cluster.
    Cross-region recording replication: only if BOTH residency laws permit.
    Recording transfer log: required in RegionalDataTransferLog.

  RULE TER-5: Championship results write to home-region DB first.
    Winner record persisted in region-local PostgreSQL primary.
    Replication to national/global layer: async, after local commit.

  RULE TER-6: National Championship pool is populated from regional winners.
    After TERRITORY_COMPLETED: winner propagated to national_champion_pool.
    National pool lives in Zone primary DB (e.g., ZONE_APAC → ap-south-1).
    National championship bracket spans zones for national final only.
    National match rooms: selected Zone primary SFU cluster.
```

### 5.3 Championship Schedule Per Region

```
CHAMPIONSHIP_SCHEDULE (per quarter, per region):

  Week 1–10  : QUALIFYING_ACTIVE
    Analytics Engine ranks users continuously.
    ChampionshipReadinessJob (from PAI-1 MODEL-2) runs daily.

  Week 10    : PRE_QUALIFICATION_ALERT_WINDOW
    ParentInsightFusion dispatches championship readiness alerts.
    Students with readiness_score ≥ 75: "Championship window opening" push.

  Week 13    : QUALIFICATION_LOCKED
    Airflow job: QualificationExtractionJob runs at quarter-end.
    Extracts top 100 per region from regional leaderboard.
    Eligibility gate checks (belt, match count, anomaly flags).
    Output: qualification_list locked in ChampionshipTerritory.

  Week 13    : PARENT QUALIFICATION ALERTS
    Immediate push + email to all parent accounts linked to qualified students.
    Alert type: CHAMPIONSHIP_QUALIFIED (from PAI-5 alert taxonomy).

  Week 13–15 : BRACKET_ACTIVE
    Bracket generated and published.
    Matches scheduled in 2-week window.
    Per-round parent alerts: FLUTTER PUSH ONLY.

  Week 15–16 : FINALS_ACTIVE
    Finals live-streamed via LiveKit (region-local SFU).
    Champion confirmed by mentor board (not auto-confirmed).

  Week 16    : TERRITORY_COMPLETED
    Champion badge assigned.
    Post-championship ParentInsightReport generated.
    National pool updated.
```

---

## ◼ SECTION RDE-6 — PARENT PREDICTIVE AI REGIONAL DELIVERY (LOCKED)

All parent predictive AI outputs (from LSM v2.0 PAI layer) MUST be delivered
via region-local infrastructure. This section seals the regional delivery
contract.

### 6.1 Regional Delivery Node Architecture

```
DELIVERY NODE PER REGION:

  Each championship region (Tier 2) MUST have:
    - Region-local FCM gateway proxy (push notification relay)
    - Region-local Postal SMTP relay (email delivery)
    - Region-local Notification Service pod (Kubernetes, same namespace as Dojo-Arts)
    - Region-local ParentInsightFusionJob runner (no cross-region job execution)
    - Region-local ParentPredictiveAlert queue (Kafka partition per region)

  Cross-region alert delivery: FORBIDDEN
    A parent in REG_IN_WEST MUST receive alerts from REG_IN_WEST delivery node.
    Routing via REG_IN_NORTH for convenience: FORBIDDEN.
    Exception: Regional node DOWN → failover to Zone DR node (controlled).

  Latency SLA per alert type:
    CRITICAL alerts (CHAMPIONSHIP_QUALIFIED, DROPOUT_RISK_RED): < 30 seconds
    OPERATIONAL alerts (RANK_RISING, BELT_MILESTONE)            : < 5 minutes
    WEEKLY_INSIGHT_READY (batch)                                 : < 60 minutes
```

### 6.2 Regional Alert Routing Logic

```
ALERT ROUTING ALGORITHM:

  STEP 1: Resolve parent's linked student home_region_id
    From UserRegionAssignment.home_region_id

  STEP 2: Route to region-local Notification Service pod
    Kafka message published to: alerts.{region_id}.parent
    Example: alerts.REG_IN_WEST.parent

  STEP 3: Region-local Notification Service pod consumes from its partition
    Delivery via:
      Push  → Region-local FCM proxy → FCM global servers → Parent device
      Email → Region-local Postal relay → Parent email inbox

  STEP 4: Delivery confirmation
    FCM delivery receipt stored in ParentPredictiveAlert.delivered_at
    Email bounce tracking: Postal webhook → ParentPredictiveAlert.delivery_status

  STEP 5: Failover (if region-local node down)
    Circuit breaker opens after 3 consecutive delivery failures
    Traffic fails over to Zone DR delivery node
    Failover event logged in RegionalHealthStatus + AuditLog
    Parent NOT notified about failover (transparent to user)

RULES:
  Minor users: region-local delivery MANDATORY (no cross-region fallback without
    compliance team approval + RegionalDataTransferLog entry).
  Government-linked users: jurisdiction_lock = TRUE → region-local only, NO failover
    outside jurisdiction without regulatory approval.
  Alert data NEVER routed through Ecoskiller core notification pipeline.
  Dojo Arts notification pipeline MUST remain isolated.
```

### 6.3 Regional ParentInsightReport Generation

```
REPORT GENERATION RULES:

  RULE PIR-1: ParentInsightFusionJob runs on region-local compute.
    Job assigned to region-partitioned Airflow worker pool.
    Worker pool assignment: arts-{region_id}-workers
    Example: arts-REG_IN_WEST-workers

  RULE PIR-2: Feature data for predictions read from region-local ClickHouse.
    No cross-region feature data pull during report generation.
    Exception: National/Global benchmark queries may read from Zone ClickHouse
      (async, non-blocking, degraded gracefully if unavailable).

  RULE PIR-3: Report stored in region-local PostgreSQL.
    ParentInsightReport.region_id = student.home_region_id
    Report replication to Zone DB: async, after local write confirmed.

  RULE PIR-4: LLM NL generation calls region-local AI inference endpoint.
    Region-local GPU cluster handles LLM NL calls.
    Cross-region AI inference: allowed as fallback only (if compliant with
      data residency law for that region).
    Government/minor data: region-local inference ONLY.

  RULE PIR-5: Report delivery window respects region timezone.
    Weekly report dispatched Sunday 22:00 LOCAL TIMEZONE (not UTC).
    Scheduled by RegionalCronManager using region.timezone_default.
    Example: REG_IN_WEST → Sunday 22:00 IST (16:30 UTC)
             REG_EU_WEST  → Sunday 22:00 CET (21:00 UTC)
```

---

## ◼ SECTION RDE-7 — CROSS-BORDER DATA GOVERNANCE (LOCKED)

Inherits and extends XBD-A through XBD-P from platform cross-border law.

### 7.1 Arts Domain Transfer Matrix

```
DATA TRANSFER MATRIX (Arts Domain — Dojo):

  DATA_CATEGORY          SOURCE_REGION        ALLOWED_DESTINATIONS
  ─────────────────────────────────────────────────────────────────
  Student PII            Home region          Home region ONLY
  Match scores           Home region          Home region + Zone (analytics)
  Leaderboard rankings   Home region          Global (read-only clone)
  Championship recordings Home region         Home region ONLY (unless permitted)
  Parent reports         Home region          Home region ONLY
  AI inference inputs    Home region          Home region + Zone fallback (if compliant)
  Belt certificates      Home region          Home region + Zone (for verification API)
  Mentor evaluations     Home region          Home region ONLY
  Audit logs             Home region          Zone DR (for backup, encrypted)

  MINOR USER PII         Home region          Home region ONLY (jurisdiction_lock)
  GOVT-LINKED DATA       Home region          Home region ONLY (jurisdiction_lock)

LEGAL BASIS REQUIRED FOR ANY TRANSFER OUTSIDE HOME REGION:
  - Standard Contractual Clauses (SCC) for EU data
  - India DPDP compliance for Indian student data
  - Documented regulatory exemption for others
  - All transfers logged in RegionalDataTransferLog

FORBIDDEN TRANSFERS:
  - Student PII to unapproved third-party processors
  - Championship recordings to non-compliant regions
  - Minor PII cross-border without guardian consent
  - AI inference inputs containing PII to non-compliant inference endpoints
```

### 7.2 Runtime Transfer Enforcement

```
ENFORCEMENT MECHANISM:

  Every inter-service call that carries user data MUST include:
    X-Data-Region: {source_region_id}
    X-Data-Category: {data_category}
    X-Transfer-Basis: {legal_basis}

  API Gateway validates headers on cross-region calls.
  If headers missing or basis invalid:
    → Request BLOCKED
    → Compliance incident raised
    → RegionalDataTransferLog.transfer_status = 'blocked'

  Runtime geo-routing enforcer:
    - Intercepts all outbound service calls
    - Validates destination region against transfer matrix
    - Blocks disallowed destinations
    - Logs all cross-region calls (allowed or blocked)

  Quarterly cross-border review:
    - DPO reviews RegionalDataTransferLog
    - Flag any patterns of unusual cross-border volume
    - Annual review mandatory (XBD-O compliance)
```

---

## ◼ SECTION RDE-8 — GEO-ROUTING ENGINE (LOCKED)

```
GEO-ROUTING RULES:

  RULE GR-1: STUDENT TRAFFIC AFFINITY
    All student API traffic routed to home-region API cluster.
    DNS resolution: GeoDNS (Cloudflare / Route53 latency routing) selects
      nearest healthy region endpoint.
    Dojo match room traffic: region-local WebSocket cluster.
    Live match video: region-local LiveKit SFU node.
    GeoDNS auto-routing MUST be constrained by RegionRegistry allowed zones.
    Auto-routing to blocked or sanctioned regions: FORBIDDEN.

  RULE GR-2: EDUCATOR/MENTOR AFFINITY
    Session stability over proximity.
    Mentor assigned to student's home region cluster.
    Cross-region mentor assignment: allowed for availability, but
      session MUST be hosted in student's home region SFU.

  RULE GR-3: ADMIN TRAFFIC
    Admin L1–L3: route to home-region admin API cluster.
    Admin L4–L7: route to Zone primary cluster (global visibility).
    Admin MUST NOT bypass regional isolation for data access.

  RULE GR-4: ANALYTICS TRAFFIC
    Batch analytics reads: allowed from Zone ClickHouse (cross-region async).
    Real-time analytics events: Kafka partition-local (no cross-region fan-out).
    ClickHouse cross-region replication: async, read-only replicas per Zone.

  RULE GR-5: CDN POLICY
    Static assets: Cloudflare global CDN (no PII).
    Leaderboard public pages (React SSG): CDN-cached, geo-distributed.
    Championship bracket public pages: CDN-cached per region.
    Dynamic authenticated data: NEVER CDN-cached.
    CDN signing keys: rotated every 60 days (per platform key rotation law).

  RULE GR-6: REALTIME TRAFFIC ISOLATION
    Dojo Arts WebRTC/WebSocket MUST NOT cross region boundaries.
    If student connects from different region than home region:
      → Detect via IP geolocation at session start
      → Route to home-region session host
      → If home-region degraded: failover to Zone DR SFU
      → Flag in RegionalHealthStatus
    Global Active-Active realtime sessions: FORBIDDEN (DOJO law).

  RULE GR-7: LOAD BALANCER GOVERNANCE
    Load balancers are the ONLY traffic ingress and distribution authority.
    Domain-agnostic traffic distribution: FORBIDDEN.
    Load balancers MUST be domain-aware (Arts ≠ Commerce ≠ Science).
    Dojo Arts load balancer: separate from Ecoskiller core load balancer.
    Layer-7 routing rules must include:
      - X-Domain-Track header enforcement
      - X-Tenant-ID isolation enforcement
      - Rate limiting per tenant per region
```

---

## ◼ SECTION RDE-9 — DOJO ARTS REGIONAL CLUSTER ARCHITECTURE (LOCKED)

```
PER CHAMPIONSHIP REGION — REQUIRED CLUSTER COMPONENTS:

  ┌─────────────────────────────────────────────────────────┐
  │  DOJO ARTS REGIONAL CLUSTER (e.g., REG_IN_WEST)        │
  ├─────────────────────────────────────────────────────────┤
  │  API Cluster          : dojo-arts-api-{region}          │
  │  WebSocket Cluster    : dojo-arts-ws-{region}           │
  │  LiveKit SFU Node     : dojo-arts-sfu-{region}          │
  │  PostgreSQL Primary   : dojo-arts-db-{region}           │
  │  Redis Cache          : dojo-arts-cache-{region}        │
  │  Kafka Partition Set  : dojo-arts-kafka-{region}        │
  │  AI Inference Worker  : dojo-arts-ai-{region}           │
  │  Notification Service : dojo-arts-notify-{region}       │
  │  MinIO Object Storage : dojo-arts-minio-{region}        │
  │  Airflow Worker Pool  : arts-{region}-workers           │
  │  ClickHouse Node      : dojo-arts-ch-{region}           │
  └─────────────────────────────────────────────────────────┘

ISOLATION RULES:
  Each component above is ARTS-DOMAIN-ONLY.
  No Commerce, Science, or Ecoskiller core workloads run in these clusters.
  No shared load balancers, NATs, firewalls, or gateways with other domains.
  Kubernetes namespace: dojo-arts-{region_id} (strictly isolated).
  Network policies: deny-all ingress except from declared Arts services.

MINIMUM VIABLE CLUSTER (MVP — pre-scale):
  For regions with < 1,000 active Arts users:
    Single-node PostgreSQL (no HA required at MVP)
    3-node Redis cluster
    Single LiveKit SFU node
    Single Kafka broker (3-partition Arts set)
    Single AI inference worker (CPU-based at MVP)
  Scale triggers:
    Active users > 1,000  → enable HA PostgreSQL
    Active users > 5,000  → add Redis cluster node
    Championship active   → mandatory LiveKit HA (2 nodes)
```

---

## ◼ SECTION RDE-10 — REGIONAL FAILOVER PROTOCOL (LOCKED)

```
FAILOVER CLASSIFICATION:

  LEVEL-1: Component Failure (single pod/service down)
    Kubernetes HPA/self-healing handles automatically.
    No human intervention required.
    Logged in RegionalHealthStatus.

  LEVEL-2: Regional Service Degradation (> 2 components failing)
    Circuit breakers open.
    Traffic fails over to Zone DR node for affected services.
    Admin L3+ notified via PagerDuty (or equivalent).
    Failover trigger: automated.
    Failover approval: not required for L2.
    Logged in RegionalHealthStatus + AuditLog.

  LEVEL-3: Full Regional Outage
    All traffic fails over to Zone DR region.
    Admin L5+ + human approval required for DNS promotion.
    Controlled promotion sequence:
      1. DB replica promoted to primary in DR region
      2. Kubernetes namespace activated in DR region
      3. DNS updated via GeoDNS (manual approval required)
      4. Parent notifications: "Service briefly disrupted, now restored"
         (delivered after recovery — do not alarm during outage)
    Championship in-flight:
      Active matches PAUSED (not terminated).
      LiveKit room held open for up to 15 minutes.
      After recovery: match resumes from last state.
      If unrecoverable: match declared void, rescheduled.
      Parent alert: "Championship match rescheduled" (push + email).

PROMOTION RULES (from MRX section — inherited):
  Automatic global promotion without human or policy approval: FORBIDDEN.
  Rollback conditions MUST be defined before promotion.
  Rollback requires Admin L4+ approval + impact simulation.
  Blind rollback: BLOCKED.

FAILOVER GOVERNANCE CHECKLIST (per region, quarterly test):
  [ ] DB replica promotion tested
  [ ] Kubernetes namespace activation tested
  [ ] DNS failover tested
  [ ] Parent alert delivery tested from DR node
  [ ] Redis snapshot-restore tested
  [ ] LiveKit SFU failover tested
  [ ] Championship bracket state preserved through failover
  [ ] RegionalDataTransferLog entries verified during DR
```

---

## ◼ SECTION RDE-11 — REGIONAL OBSERVABILITY (LOCKED)

```
PER-REGION MANDATORY METRICS (from platform observability law):

  INFRASTRUCTURE METRICS:
    dojo_arts_api_request_rate_by_region
    dojo_arts_api_error_rate_by_region
    dojo_arts_api_latency_p99_by_region
    dojo_arts_ws_connection_count_by_region
    dojo_arts_sfu_active_rooms_by_region
    dojo_arts_db_connection_pool_utilization_by_region
    dojo_arts_kafka_consumer_lag_by_region_partition
    dojo_arts_redis_memory_utilization_by_region
    dojo_arts_ai_worker_queue_depth_by_region
    dojo_arts_minio_storage_utilization_by_region

  LEADERBOARD METRICS:
    leaderboard_write_latency_ms_by_region
    leaderboard_read_latency_ms_by_region
    leaderboard_redis_zadd_ops_per_second_by_region
    leaderboard_snapshot_checksum_valid_by_region   (ALERT if FALSE)
    leaderboard_reset_completed_by_region            (ALERT if missed)
    cross_region_leaderboard_reads (should be 0 in normal operation)

  CHAMPIONSHIP METRICS:
    championship_territory_status_by_region
    championship_bracket_matches_completed_by_region
    championship_match_room_latency_p99_by_region   (ALERT if > 150ms)
    championship_recording_storage_by_region
    championship_qualification_extraction_completed  (ALERT if failed)

  PARENT AI DELIVERY METRICS:
    parent_alert_dispatch_rate_by_region
    parent_alert_delivery_success_rate_by_region     (ALERT if < 95%)
    parent_alert_delivery_latency_ms_by_region
    parent_insight_report_generation_time_by_region
    parent_insight_report_llm_call_latency_by_region
    cross_region_parent_alert_deliveries              (should be 0)

  CROSS-BORDER COMPLIANCE METRICS:
    cross_border_transfer_count_by_region_pair
    cross_border_transfer_blocked_count_by_region
    cross_border_transfer_without_legal_basis         (ALERT immediately)
    regional_data_transfer_log_write_failures         (ALERT immediately)

ALERT THRESHOLDS:
  API error rate > 1%           → WARN Admin L2
  API latency P99 > 500ms       → WARN Admin L2
  API error rate > 5%           → ALERT Admin L3 + PagerDuty
  Match room latency > 150ms    → ALERT Admin L3
  Cross-border blocked > 0      → ALERT Compliance + Admin L5
  Leaderboard snapshot invalid  → ALERT Admin L4 + STOP reset
  Parent delivery rate < 95%    → ALERT Admin L2

DASHBOARDS (mandatory per region):
  Regional Infrastructure Health (Grafana — region-scoped)
  Regional Leaderboard Health (Grafana — custom panel)
  Championship Territory Status (Admin Flutter panel)
  Parent AI Delivery Health (Admin Flutter panel)
  Cross-Border Transfer Audit (Compliance team — read-only Grafana)

BLIND REGIONS: FORBIDDEN (from platform observability law)
  Every region MUST emit domain-tagged telemetry.
  Region without metrics pipeline → DEPLOYMENT BLOCKED.
```

---

## ◼ SECTION RDE-12 — REGIONAL COST GOVERNANCE (LOCKED)

```
COST GOVERNANCE RULES (from platform cost law):

  Per-region cost ceiling MUST be declared before region activation.
  Cost ceiling breach → Alert Compliance + Admin L5 + scale review.
  Domain-aware billing: Arts domain cost tracked independently from
    Commerce, Science, and Ecoskiller core.

  COST BUDGET STRUCTURE (per region per month — MVP estimates):

    REGION: REG_IN_WEST (Mumbai — India West, ~5,000 active students MVP)

    Infrastructure:
      PostgreSQL HA (2-node)            : ~$80
      Redis cluster (3-node)            : ~$60
      Kubernetes cluster (3 worker nodes): ~$120
      LiveKit SFU node (standard)       : ~$40
      Kafka (3-broker)                  : ~$60
      ClickHouse node                   : ~$50
      MinIO (1TB storage)               : ~$25
      FCM/Postal delivery               : ~$10
      Monitoring (Prometheus/Grafana)   : ~$20
      CDN (Cloudflare)                  : ~$15
      Total Infra Estimate              : ~$480/month (MVP)

    AI Inference (region-local, Arts domain):
      Predictive ML batch jobs          : ~$120
      LLM NL generation (weekly reports): ~$50
      Total AI Estimate                 : ~$170/month (MVP)

    Total per region (MVP)              : ~$650/month

  SCALE FORECAST:
    T2 (100K active, 10 regions)        : ~$8,000–12,000/month
    T3 (1M active, 20+ regions)         : ~$60,000–100,000/month
    (forecasts subject to quarterly review — X140 capacity law)

  BUDGET BURN-RATE ALERTS:
    50% of monthly ceiling reached → WARN Admin L3
    70% of monthly ceiling reached → WARN Admin L4 + cost review
    90% of monthly ceiling reached → ALERT Admin L5 + block non-critical scale
    100% ceiling breach            → EMERGENCY Admin L7 response required

  COST OPTIMIZATION RULES:
    Multi-region fan-out without budget limits: FORBIDDEN.
    Over-provisioned regions without active users: ALERT Admin L3 monthly.
    Arts domain cost MUST NOT subsidize Commerce or Science (domain isolation).
```

---

## ◼ SECTION RDE-13 — REGIONAL CAPACITY PLANNING (LOCKED)

```
CAPACITY MODEL (per region, from X140 Capacity Law):

  T1 MVP (per region):
    Concurrent users         : 500–2,000
    Live championship rooms  : up to 50 simultaneous
    WebSocket connections     : up to 5,000
    AI inference calls/hour  : up to 1,000
    Leaderboard Redis ops/sec: up to 10,000 ZADD/ZRANGE

  T2 STABLE (per region):
    Concurrent users         : 10,000–50,000
    Live championship rooms  : up to 500 simultaneous
    WebSocket connections     : up to 100,000
    AI inference calls/hour  : up to 20,000
    Leaderboard Redis ops/sec: up to 200,000

  SCALING TRIGGERS (auto, governed):
    API CPU > 70%            → Add 2 API pods (HPA)
    WebSocket connections > 80% of cluster capacity → Add WS pod
    Redis memory > 70%       → Add Redis cluster node (manual approval)
    AI queue depth > 100     → Scale AI worker pool
    Championship bracket starts → Pre-scale LiveKit to 2 SFU nodes
    Arts traffic in region spikes → Commerce/Science unaffected (domain isolation)

  CAPACITY ISOLATION RULE:
    Arts regional capacity surge MUST NOT consume Commerce/Science resources.
    Separate cluster pools per domain (dojo-arts-pool, dojo-commerce-pool, etc.).

  DOMAIN IMBALANCE HANDLING:
    If Arts grows 3× faster than Commerce in a region:
      → Arts cluster pool scales independently
      → Commerce/Science pools unaffected
      → Domain imbalance spike logged in capacity planning dashboard
```

---

## ◼ SECTION RDE-14 — REGIONAL SHARD GOVERNANCE (LOCKED)

```
SHARD STRATEGY (from platform sharding law — 175.x series):

  PRIMARY SHARD KEY (PostgreSQL, ClickHouse):
    tenant_id + region_id + domain_track
    Example: {tenant_uuid}:{REG_IN_WEST}:{arts}

  LEADERBOARD SHARDS:
    Redis: Consistent hash ring per region (no cross-region keys)
    ClickHouse: Partitioned by (region_id, period_key, domain_track)

  SHARD REGISTRY:
    All shards registered in platform CentralShardRegistry.
    Unregistered shard → BLOCKED (175.2 compliance).
    Domain-prefixed shard keys: domain mismatch → SECURITY FAILURE.

  CROSS-SHARD TRANSACTION RULES:
    Cross-region shard transactions: FORBIDDEN in hot paths.
    Leaderboard write: single-shard (home region Redis only).
    National/Global leaderboard: separate shard from regional.
    Championship bracket: single-shard (home region PostgreSQL).
    Parent reports: single-shard (student home region PostgreSQL).

  HOTSPOT AVOIDANCE:
    Time-based partitioning in ClickHouse (avoid period_key hotspot).
    Redis key spreading: append period_key suffix to leaderboard keys.
    Large tenant isolation: dedicated shard for enterprise/govt tenants.
```

---

## ◼ SECTION RDE-15 — REGIONAL NOTIFICATION POLICY (LOCKED)

```
NOTIFICATION CLASSIFICATION per regional context:

  CRITICAL (override quiet hours, multi-channel mandatory):
    - Championship match start failure
    - Regional infrastructure down (admin-facing)
    - Security incident in student's region

  OPERATIONAL (region-local delivery, within 5 min):
    - Championship qualified (student + parent)
    - Championship round scheduled
    - Belt promoted
    - Match scheduled

  ASSESSMENT (region-local, within 30 min):
    - Mentor score submitted
    - Match result available
    - Belt criteria progress

  INFORMATIONAL (region-local, batched weekly):
    - Weekly insight report (parent)
    - Rank trend update
    - Peer benchmark available

QUIET HOURS POLICY (region-aware):
  Quiet hours = 22:00–07:00 LOCAL TIMEZONE (from region.timezone_default).
  CRITICAL: override quiet hours.
  OPERATIONAL: defer to first delivery window after quiet hours.
  INFORMATIONAL: always batched, never during quiet hours.

CONSENT REQUIREMENT:
  All notifications require user consent (per PN1 platform law).
  Consent verified before first delivery per channel.
  Minor users: guardian consent required.
  Withdrawal of consent: immediate halt of all non-CRITICAL alerts.

RATE LIMITS (inherited from PAI-5):
  Parent alerts: max 5 push per 7 days per linked student.
  Student alerts: max 10 push per day (platform-wide).
  Admin alerts: no rate limit (operational necessity).
```

---

## ◼ SECTION RDE-16 — REGIONAL DATA LIFECYCLE (LOCKED)

```
RETENTION POLICY (per data category, per region):

  LeaderboardEntry (live PostgreSQL)    : 24 months, then archive
  leaderboard Redis sorted sets        : TTL = period_key + 30 days
  ClickHouse leaderboard events        : 36 months (compliance minimum)
  LeaderboardSnapshot                  : 24 months
  ChampionshipTerritory records        : Permanent (archived)
  Championship recordings (MinIO)      : 36 months (or jurisdictional min)
  UserRegionAssignment                 : Lifetime of user account
  RegionalDataTransferLog              : 5 years (legal compliance)
  RegionalHealthStatus                 : 12 months rolling
  ParentPredictiveAlert history        : 12 months
  PredictiveAIAuditLog (regional)      : 24 months

DELETION WORKFLOW (GDPR/DPDP right to erasure):
  User deletion request:
    1. UserRegionAssignment: soft-delete (tombstoned)
    2. LeaderboardEntry: anonymised (user_id → anonymised_hash)
    3. Redis sorted sets: ZREM user_id from all regional keys
    4. ClickHouse: append anonymisation event (immutable log)
    5. ParentPredictiveAlert: hard delete
    6. ChampionshipBracket: user_id → ANONYMISED_PARTICIPANT
    7. Recordings: user voice tracks anonymised (not deleted — audit integrity)
    8. RegionalDataTransferLog: entries tombstoned, not deleted (legal requirement)
  Full deletion within: 30 days of request (GDPR standard).
  Deletion audit: required in AuditLog per step.

DATA BACKUP RULES:
  Daily backup per region (PostgreSQL).
  Backup stored in same region + Zone DR region only.
  Cross-region backup transfer: logged in RegionalDataTransferLog.
  Backup encryption: AES-256, keys in Hashicorp Vault (region-local key store).
  Restore drills: quarterly per region (from RDE-10 failover checklist).
```

---

## ◼ SECTION RDE-17 — REGIONAL API GATEWAY RULES (LOCKED)

```
API GATEWAY REGIONAL CONTRACT:

  GATEWAY: Kong OSS (region-local deployment per Dojo Arts cluster)
  ROUTING RULES:
    /api/arts/*         → dojo-arts-api-{region} (region-resolved at JWT decode)
    /leaderboard/arts/* → leaderboard-service-{region}
    /championship/*     → championship-service-{region}
    /parent/*           → parent-api-{region}
    /predictions/arts/* → predictive-ai-service-{region}

  REQUEST HEADERS (enforced by gateway on all inbound):
    X-Domain-Track   : must equal 'arts' for all Dojo Arts endpoints
    X-Tenant-ID      : must match authenticated user's tenant
    X-Region-ID      : resolved by gateway from JWT or GeoDNS context
    X-Request-ID     : unique per request (correlation ID)

  RATE LIMITS (per tenant per region):
    Standard students     : 100 req/min per user
    Mentor/Evaluator      : 200 req/min per user
    Admin L1–L3           : 500 req/min per user
    Admin L4–L7           : 1,000 req/min per user
    Batch jobs (system)   : 5,000 req/min per service account

  CROSS-DOMAIN REQUEST REJECTION:
    Any request with X-Domain-Track ≠ 'arts' routed to wrong Arts endpoint:
      → 403 Forbidden
      → Domain leak attempted → SECURITY ALERT

  REGIONAL FAILOVER ROUTING:
    If home-region gateway unreachable:
      → GeoDNS promotes Zone DR gateway
      → Requests routed to DR region
      → X-Region-Failover: true header appended to response
```

---

## ◼ SECTION RDE-18 — REGIONAL ENVIRONMENT ISOLATION (LOCKED)

```
ENVIRONMENT SEPARATION (per region):

  EACH REGION MUST MAINTAIN FOUR ISOLATED ENVIRONMENTS:
    DEV      → local developer only (no real user data, no real region)
    TEST     → CI automated testing (synthetic data, isolated namespace)
    STAGING  → pre-production in real region (synthetic data, full cluster)
    PROD     → live production

  RULES:
    Dev data MUST NOT contain real user PII from any region.
    Staging MUST use synthetic/anonymised datasets.
    Production data NEVER moved to dev or test (XBD-L compliance).
    Regional environment files: /config/environments/{region_id}/{env}.env
    Branch-to-environment mapping:
      dev branch     → DEV (local)
      test branch    → TEST (CI)
      staging branch → STAGING ({region_id}-staging namespace)
      production     → PROD ({region_id}-prod namespace)

  REGIONAL K8S NAMESPACES:
    dojo-arts-{region_id}-dev
    dojo-arts-{region_id}-test
    dojo-arts-{region_id}-staging
    dojo-arts-{region_id}-prod

  NAMESPACE ISOLATION:
    No cross-namespace pod communication without explicit NetworkPolicy.
    Domain-to-domain namespace traffic: denied by default.
    Ecoskiller core namespaces ≠ Dojo Arts namespaces.
```

---

## ◼ SECTION RDE-19 — REGIONAL AUDIT & COMPLIANCE EVIDENCE (LOCKED)

```
AUDIT EVIDENCE REQUIREMENTS (per region):

  LEADERBOARD INTEGRITY:
    Monthly: export LeaderboardSnapshot checksums + verification report
    Quarterly: Championship territory full audit (bracket + results + recordings)
    Annual: Hall of Fame entries verified against tournament records

  CROSS-BORDER COMPLIANCE:
    Monthly: RegionalDataTransferLog exported + reviewed by DPO
    Quarterly: Legal basis validation for all active transfer types
    Annual: Full cross-border compliance report per region

  CHAMPIONSHIP GOVERNANCE:
    Per championship: full bracket log + score audit + mentor confirmation records
    Per season: anomaly detection summary + manipulation flags + resolutions

  PARENT AI GOVERNANCE:
    Monthly: ModelBiasAuditRecord status per region
    Monthly: parent alert delivery success rate report per region
    Quarterly: AI confidence score distribution by region
      (detect regional model drift)
    Annual: Full predictive AI bias and fairness audit per region

  MINOR PROTECTION:
    Monthly: verify all minor users have jurisdiction_lock = TRUE
    Quarterly: cross-border transfer check for minor user data
    Annual: guardian consent validation sweep

EVIDENCE FORMAT:
  All audit evidence: JSONB + PDF export
  Stored in: MinIO (region-local) + Zone DR backup
  Retention: 5 years minimum
  Access: Compliance team + DPO + Admin L5+ only
  Tamper protection: SHA-256 checksum on each evidence package
```

---

## ◼ SECTION RDE-20 — ANTIGRAVITY GENERATION CONSTRAINTS (SEALED)

The following rules bind Antigravity's generation behavior specifically for
the Regional Distribution Engine. These supersede any Antigravity defaults.

```
ANTIGRAVITY MUST:

  ✔ Generate explicit region declarations for every RegionRegistry entry
  ✔ Generate per-region Kubernetes namespace manifests
      (dojo-arts-{region_id}-prod, staging, test, dev)
  ✔ Generate per-region Kafka partition configurations
      (arts.{region_id}.leaderboard, arts.{region_id}.championship,
       arts.{region_id}.parent_alerts, arts.{region_id}.ai_predictions)
  ✔ Generate region-local Redis sorted set key schemas
  ✔ Generate GeoDNS routing rules (per registered region)
  ✔ Generate regional environment variable files
      /config/environments/{region_id}/production.env
      /config/environments/{region_id}/staging.env
  ✔ Generate Kong API Gateway routing rules (per region)
  ✔ Generate Airflow worker pool assignments (per region)
  ✔ Generate region-local Prometheus alert rules
  ✔ Generate RegionalDataTransferLog write hooks on all cross-region calls
  ✔ Generate champion territory state machine implementation
  ✔ Generate regional failover playbooks (per LEVEL-1/2/3)
  ✔ Generate per-region cost ceiling configs

ANTIGRAVITY MUST NOT:

  ✘ Auto-select regions not declared in RegionRegistry
  ✘ Rely on cloud provider default region (provider defaults forbidden)
  ✘ Collapse multiple regions into one
  ✘ Mix Dojo Arts and Ecoskiller core in same Kubernetes namespace
  ✘ Mix Arts, Commerce, or Science regional infrastructure
  ✘ Infer user region from request IP without ResolutionEngine logic
  ✘ Enable Active-Active globally without explicit domain declaration
  ✘ Generate silent cross-region data flows without transfer log hooks
  ✘ Route realtime WebRTC/WebSocket across regions
  ✘ Generate multi-region leaderboard Redis keys (each region isolated)
  ✘ Deploy AI inference endpoints that send user PII cross-region
     without data residency validation
  ✘ Generate championship brackets that mix participants from different regions
  ✘ Skip environment isolation for any region (all 4 envs required per region)
```

---

## ◼ SECTION RDE-21 — MASTER ENFORCEMENT CHECKLIST

```
══════════════════════════════════════════════════════════════════════════
ANTIGRAVITY MUST VERIFY ALL GATES BEFORE DECLARING BUILD COMPLETE
══════════════════════════════════════════════════════════════════════════

INHERITED GATES (LSM v1.0 + v2.0 — all still required):
[ ] All LSM-1:16 gates from v1.0 passed
[ ] All PAI-0:18 gates from v2.0 passed

NEW GATES — REGIONAL DISTRIBUTION ENGINE v1.0:
[ ] RDE-0  : Engine identity declared; all hard laws acknowledged
[ ] RDE-1  : All 3 Geographic Zone tiers declared;
              all Tier-2 Championship Regions registered in RegionRegistry;
              Redis key schema defined for Tier-3 scopes
[ ] RDE-2  : All 5 data model entities created:
              RegionRegistry, UserRegionAssignment, TenantRegionAssignment,
              ChampionshipTerritory, RegionalDataTransferLog, RegionalHealthStatus
[ ] RDE-3  : User Region Resolution Engine implemented (6-step algorithm);
              VPN anomaly detection active; minor jurisdiction_lock enforced
[ ] RDE-4  : Regional leaderboard architecture declared:
              Redis sorted sets per region; ClickHouse archive; PostgreSQL snapshots;
              Write pipeline event-driven (no polling);
              Read pipeline server-side only (STATE-10 compliance)
[ ] RDE-5  : Championship Territory Engine implemented:
              7-state lifecycle machine; multi-region isolation TER-1:6;
              schedule per quarter declared; national pool propagation active
[ ] RDE-6  : Parent Predictive AI Regional Delivery implemented:
              Region-local delivery nodes per Tier-2 region;
              Alert routing algorithm (5 steps); regional report generation rules;
              Timezone-aware dispatch (PIR-5)
[ ] RDE-7  : Cross-border data governance:
              Transfer matrix implemented; runtime enforcement headers active;
              RegionalDataTransferLog written on every cross-region call;
              Quarterly DPO review process documented
[ ] RDE-8  : Geo-routing engine: GR-1:7 rules implemented;
              GeoDNS constrained to RegionRegistry zones;
              Realtime traffic isolation enforced; CDN policy locked
[ ] RDE-9  : Per-region Dojo Arts cluster architecture declared (all 11 components);
              Kubernetes namespace isolation enforced; domain isolation confirmed
[ ] RDE-10 : Regional failover protocol: L1/L2/L3 scenarios implemented;
              Championship match failover handling active;
              Quarterly failover drill checklist documented
[ ] RDE-11 : Regional observability: all mandatory metrics emitting;
              Alert thresholds set; all 5 mandatory dashboards defined;
              No blind regions
[ ] RDE-12 : Regional cost governance: per-region ceiling declared;
              burn-rate alerts at 50/70/90/100%;
              domain cost isolation (Arts ≠ Commerce ≠ Science)
[ ] RDE-13 : Regional capacity planning: T1/T2 models declared;
              scale triggers defined; domain isolation in capacity;
              Arts spike isolation from other domains confirmed
[ ] RDE-14 : Regional shard governance: shard key strategy declared;
              all shards registered; cross-shard rules enforced
[ ] RDE-15 : Regional notification policy: classification locked;
              quiet hours region-timezone-aware; consent requirements enforced
[ ] RDE-16 : Regional data lifecycle: retention policy per category declared;
              deletion workflow (GDPR/DPDP) implemented; backup rules enforced
[ ] RDE-17 : Regional API gateway: Kong rules declared;
              request headers enforced; rate limits configured;
              cross-domain rejection active
[ ] RDE-18 : Regional environment isolation: 4 envs per region declared;
              per-region K8s namespaces generated; prod data isolation confirmed
[ ] RDE-19 : Regional audit and compliance evidence: monthly/quarterly/annual
              schedules documented; evidence format and retention declared
[ ] RDE-20 : Antigravity generation constraints acknowledged;
              all MUST/MUST-NOT rules verified in generated artifacts
[ ] RDE-21 : This checklist complete
[ ] RDE-22 : Antigravity run command executed (below)

GLOBAL INVARIANTS (must always be true):
[ ] No region auto-selected or provider-defaulted
[ ] Dojo Arts regions isolated from Ecoskiller core and other domains
[ ] Arts domain cross-domain contamination: zero
[ ] Championship brackets: region-scoped, no cross-region participants
[ ] Parent alerts: region-local delivery only (no cross-region routing)
[ ] Minor user data: jurisdiction_lock = TRUE, no cross-border
[ ] All cross-region transfers logged in RegionalDataTransferLog
[ ] Unknown data residency forbidden in any region
[ ] Realtime WebRTC/WebSocket: region-local only
[ ] Leaderboard computation: server-side only, region-local Redis
[ ] All regions have observability (no blind regions)
[ ] Championship match failover: pause, not terminate

FAILURE ON ANY GATE → STOP EXECUTION → REPORT GATE_FAILURE → INVALIDATE BUILD
```

---

## ◼ SECTION RDE-22 — ANTIGRAVITY RUN COMMAND (SEALED EXECUTION BLOCK)

```
╔══════════════════════════════════════════════════════════════════════╗
║         ANTIGRAVITY EXECUTION BLOCK — DO NOT MODIFY                 ║
╠══════════════════════════════════════════════════════════════════════╣
║                                                                      ║
║  ANTIGRAVITY_RUN:                                                    ║
║    SPEC             = REGIONAL_DISTRIBUTION_ENGINE                   ║
║    VERSION          = v1.0                                           ║
║    INHERITS         = LSM_v1.0 + LSM_PPAI_v2.0 (all sections)       ║
║    EXECUTION_ENGINE = ANTIGRAVITY                                    ║
║    DOMAIN           = ARTS                                           ║
║    PARENT_SYSTEM    = ECOSKILLER                                     ║
║    SUB_SYSTEM       = DOJO                                           ║
║                                                                      ║
║  GENERATE (RDE v1.0 — NEW):                                          ║
║                                                                      ║
║  DATA MODELS:                                                        ║
║    - RegionRegistry migration + seed (all declared regions)          ║
║    - UserRegionAssignment migration                                  ║
║    - TenantRegionAssignment migration                                ║
║    - ChampionshipTerritory migration                                 ║
║    - RegionalDataTransferLog migration                               ║
║    - RegionalHealthStatus migration                                  ║
║                                                                      ║
║  SERVICES:                                                           ║
║    - UserRegionResolutionEngine (6-step algorithm)                   ║
║    - RegionalLeaderboardWritePipeline (event-driven ZADD)            ║
║    - RegionalLeaderboardReadService (server-side, Redis ZREVRANGE)   ║
║    - ChampionshipTerritoryEngine (7-state machine)                   ║
║    - QualificationExtractionJob (Airflow, quarter-end)               ║
║    - ChampionshipScheduleManager (per-region, per-quarter)           ║
║    - RegionalAlertRouter (5-step routing algorithm)                  ║
║    - RegionalParentInsightFusionJob (timezone-aware dispatch)        ║
║    - RegionalCronManager (timezone-aware cron per region)            ║
║    - RegionalDataTransferEnforcer (header validation + log hooks)    ║
║    - GeoRoutingEngine (GR-1:7 rules implementation)                  ║
║    - RegionalFailoverManager (L1/L2/L3 protocols)                   ║
║    - RegionalHealthMonitor (Prometheus metrics + alerts)             ║
║    - RegionalCostCeilingMonitor (burn-rate alerts)                   ║
║    - RegionalAuditEvidenceExporter (monthly/quarterly/annual)        ║
║    - RegionalEnvironmentBootstrapper (4 envs per region)             ║
║                                                                      ║
║  INFRASTRUCTURE:                                                     ║
║    - K8s namespace manifests: dojo-arts-{region_id}-{env}           ║
║      (for all registered regions × 4 environments)                  ║
║    - Redis sorted set key schema per region                          ║
║    - Kafka partition configs: arts.{region_id}.* topics              ║
║    - Kong gateway routing rules per region                           ║
║    - GeoDNS config (constrained to RegionRegistry zones)             ║
║    - Airflow worker pool configs per region                          ║
║    - Regional env files: /config/environments/{region_id}/{env}.env  ║
║    - Prometheus alert rules per region                               ║
║    - Grafana dashboard templates per region (5 dashboards)           ║
║    - MinIO bucket policies per region (domain-isolated)              ║
║    - LiveKit SFU deployment per region (championship-scale HA)       ║
║    - Network policies (deny-all default, explicit allow-list)        ║
║    - HPA configs (per service, per region)                           ║
║    - Cost ceiling configmap per region                               ║
║                                                                      ║
║  UI SCREENS:                                                         ║
║    - Leaderboard widget: tabs (Global / National / Regional /        ║
║        City / College) — Flutter, region-resolved                   ║
║    - Championship Territory Status panel (Admin Flutter)             ║
║    - Regional Health Dashboard panel (Admin Flutter)                 ║
║    - Regional Cross-Border Audit panel (Compliance Flutter)          ║
║    - Parent Alert Delivery Health panel (Admin Flutter)              ║
║    - React SSG: /leaderboard (regional context via GeoDNS)           ║
║    - React SSG: /championship (per-region page, static)              ║
║                                                                      ║
║  ENFORCE:                                                            ║
║    - All RDE gates (RDE-0 through RDE-22)                            ║
║    - All LSM gates (LSM-1:16)                                        ║
║    - All PAI gates (PAI-0:18)                                        ║
║    - Region isolation: Arts ≠ Ecoskiller ≠ other Dojo domains       ║
║    - Realtime traffic: region-local ONLY                             ║
║    - Champion brackets: region-scoped, no cross-region mixing        ║
║    - Parent delivery: region-local nodes ONLY                        ║
║    - Minor jurisdiction_lock: enforced on every data operation       ║
║    - All cross-border transfers: logged in RegionalDataTransferLog   ║
║    - Unknown data residency: BLOCK + ALERT                           ║
║    - Timezone-aware scheduling per region                            ║
║    - No blind regions (observability mandatory before activation)    ║
║    - Cost ceiling declared before region activated                   ║
║    - Legal review completed before region activated                  ║
║                                                                      ║
║  FORBIDDEN:                                                          ║
║    - Auto-selecting regions or using provider defaults               ║
║    - Collapsing geographic tiers                                     ║
║    - Mixing Dojo Arts with Ecoskiller or other domain infrastructure ║
║    - Cross-region realtime WebRTC or WebSocket routing               ║
║    - Unmanaged Active-Active multi-region                            ║
║    - Silent cross-border data transfers                              ║
║    - Championship brackets with cross-region participants            ║
║    - Parent alerts routed through wrong region                       ║
║    - Minor PII leaving jurisdiction without legal basis              ║
║    - Region activation without legal review + compliance approval    ║
║    - Environment creation without 4-env isolation                    ║
║    - Any creative interpretation of any RDE section                  ║
║                                                                      ║
║  FAILURE MODE = STOP → REPORT → NO PARTIAL OUTPUT                   ║
║                                                                      ║
╠══════════════════════════════════════════════════════════════════════╣
║  ANTIGRAVITY_SEAL: RDE-v1.0-ARTS-LOCKED ✅                          ║
╚══════════════════════════════════════════════════════════════════════╝
```

---

```
╔════════════════════════════════════════════════════════════════════════════╗
║  🔒 DOCUMENT SEAL                                                          ║
║  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━  ║
║  ARTIFACT     : REGIONAL_DISTRIBUTION_ENGINE_ANTIGRAVITY.md               ║
║  VERSION      : v1.0                                                       ║
║  INHERITS     : LSM v1.0 (fully) + LSM PPAI v2.0 (fully)                 ║
║  DOMAIN       : ARTS (Dojo SaaS)                                           ║
║  PLATFORM     : ECOSKILLER                                                 ║
║  ENGINE       : ANTIGRAVITY                                                ║
║  SECTIONS     : RDE-0:22 (23 new sealed sections)                         ║
║  GEO ZONES    : 3 Global Zones declared                                    ║
║  REGIONS      : 9 Championship Regions declared (expandable)              ║
║  DATA MODELS  : 6 new entities declared                                    ║
║  SERVICES     : 16 new services declared                                   ║
║  INFRA        : 17 infrastructure artifact types declared                  ║
║  STATUS       : SEALED · LOCKED · PRODUCTION-READY SPEC                  ║
║  MUTATION     : ADD-ONLY VIA VERSION BUMP                                 ║
║  SIGNED       : ECOSKILLER PLATFORM GOVERNANCE                            ║
║  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━  ║
╚════════════════════════════════════════════════════════════════════════════╝
```
