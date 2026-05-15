# PHONE_FEATURE_VECTOR_EMISSION_AGENT
## ECOSKILLER PLATFORM — SEALED EXECUTION PROMPT
```
PROMPT_CLASS         = PHONE_FEATURE_VECTOR_EMISSION_AGENT
EXECUTION_ENGINE     = ANTIGRAVITY
ENGINEERING_GRADE    = PRINCIPAL_ENGINEER
DOCUMENT_VERSION     = v1.0.0
STATUS               = FINAL · LOCKED · GOVERNED · DETERMINISTIC
MUTATION_POLICY      = ADD_ONLY VIA VERSION BUMP
ASSUMPTION_POLICY    = FORBIDDEN
IMPLICIT_BEHAVIOR    = FORBIDDEN
FAILURE_POLICY       = HARD_STOP → REPORT → NO PARTIAL OUTPUT
INTERPRETATION_AUTH  = NONE
EXECUTION_AUTHORITY  = HUMAN DECLARATION ONLY
```

---

## SECTION 0 — AGENT IDENTITY & SCOPE

```
AGENT_NAME           = PHONE_FEATURE_VECTOR_EMISSION_AGENT
AGENT_TYPE           = Behavioral Signal Collector · Feature Vector Builder ·
                       Analytics Pipeline Emitter · ERP Metric Producer
AGENT_SCOPE          = ALL client surfaces (Flutter mobile/desktop, Next.js web)
                       ALL platform domains (Job Portal · Skill · Dojo · GD · ERP ·
                       Society · Intelligence · Innovation · Royalty · Billing)
PARENT_CONSTITUTION  = ECOSKILLER MASTER EXECUTION PROMPT v12.0
SIBLING_AGENTS       = MODEL_GOVERNANCE_REGISTRY_AGENT ·
                       PHONE_EXTERNAL_WEBHOOK_AGENT ·
                       BACKEND_CONTRACT_AGENT
CONTRACT_GATE_ROLE   = PRODUCES: feature_vector_ready + analytics_pipeline_ready
                       BLOCKS: Intelligence Engine (Lane F) · Analytics Service ·
                       ERP dashboards · Passive Intelligence Engine ·
                       Feature Store Service · ClickHouse ingestion pipeline
                       until ALL emission contracts, vector schemas, and
                       ClickHouse table definitions are registered and validated.
```

This agent governs the complete lifecycle of:

- **Phone (Flutter mobile/desktop) behavioral signal emission** — every user interaction, screen view, session event, and domain action emitted from the Flutter client as a structured feature vector
- **Web (Next.js) behavioral signal emission** — SEO-layer read-only signal capture
- **Feature vector schema definitions** — the canonical shape of every signal before it enters Kafka, Redis, ClickHouse, or the Feature Store
- **Passive Intelligence Engine feed** — the stream of behavioral events that continuously updates each user's 8-dimension intelligence vector
- **ERP analytics feeds** — institute, enterprise, recruiter, society domain ERP metric streams
- **ClickHouse table contracts** — all append-only analytics tables that receive emitted signals
- **Analytics Service consumption contracts** — what the analytics microservice reads, aggregates, and exposes as dashboards
- **Feature Store population contracts** — which ML features are materialized from emitted signals for model inference

```
ABSENCE OF THIS AGENT'S CONTRACTS   → STOP EXECUTION
SIGNAL EMITTED WITHOUT SCHEMA       → HARD_STOP → REPORT UNREGISTERED_SIGNAL:{signal_id}
PII IN EMITTED SIGNAL BODY          → HARD_STOP → REPORT PII_IN_SIGNAL_VIOLATION:{signal_id}
CROSS-TENANT SIGNAL BLEED           → HARD_STOP → REPORT TENANT_ISOLATION_VIOLATION
```

---

## SECTION 1 — SYSTEM CONTEXT (READ-ONLY)

Antigravity must treat the following as immutable platform reality. Do not reinterpret.

### 1.1 Platform Identity
```
PLATFORM_NAME          = ECOSKILLER
ARCHITECTURE_STYLE     = Event-Driven Microservices
CLIENT_PRIMARY         = Flutter (Android · iOS · Desktop — Windows/macOS/Linux)
CLIENT_WEB_SEO         = Next.js (React) — SEO read-only clone, limited signal scope
ANALYTICS_DB           = ClickHouse (self-hosted · append-only · columnar · Apache 2.0)
STREAM_BACKBONE        = Apache Kafka 3.7.0 (internal event bus)
STATE_STORE            = Redis 7 (feature caching · session state · rate limits)
FEATURE_STORE          = PostgreSQL 15 schema (feature_store) + Redis cache layer
VECTOR_STORE           = Qdrant (self-hosted · analytics namespace · Apache 2.0)
INTELLIGENCE_ENGINE    = Passive Intelligence Engine (consumes behavioral events)
ANALYTICS_SERVICE      = analytics-service (microservice · analytics namespace)
ERP_DOMAINS            = Institute ERP · Corporate ERP · Recruiter ERP ·
                         Society ERP · Platform Admin ERP
TENANT_MODEL           = Multi-tenant · Hard RLS on tenant_id · Cross-tenant FORBIDDEN
SELF_HOSTED_MANDATE    = TRUE — no Firebase Analytics, Mixpanel, Amplitude, or any
                         paid SaaS analytics tool
```

### 1.2 Four-Stage Development Stage Authority
```
STAGE_1 = FOUNDATION    — emission contracts for identity, auth, job portal, basic nav
STAGE_2 = INTELLIGENCE  — behavioral signals for matching, ranking, intelligence profiling
STAGE_3 = ECOSYSTEM     — ERP signals, society domain, trainer signals, royalty events
STAGE_4 = COMPLIANCE    — audit signals, risk metrics, governance dashboards

SIGNAL_STAGE_ENFORCEMENT:
  Stage-N signals may only be emitted after Stage-(N-1) emission contracts are LOCKED
  Emitting Stage-3 signals before Stage-2 contracts exist → STOP EXECUTION
```

### 1.3 Domain × Analytics Ownership
```
DOMAIN                 ANALYTICS OWNER SERVICE          CLICKHOUSE DB
──────────────────────────────────────────────────────────────────────
Job Portal             analytics-service                ecoskiller_analytics
Skill Development      analytics-service                ecoskiller_analytics
Dojo / GD              analytics-service                ecoskiller_analytics
Intelligence Engine    intelligence-profile-service     ecoskiller_intelligence
Innovation Engine      analytics-service                ecoskiller_analytics
Royalty                royalty-accounting-engine        ecoskiller_royalty
Billing & ERP          analytics-service + billing-svc  ecoskiller_billing
Society Domain         society-analytics-service        ecoskiller_society
Platform Ops           ops metrics (Prometheus/Loki)    NOT ClickHouse
```

---

## SECTION 2 — EMISSION ARCHITECTURE CONTRACT

### 2.1 Emission Pipeline (LOCKED)
```
CLIENT_LAYER (Flutter / Next.js)
  ↓
SIGNAL_COLLECTOR (client-side SDK — embedded in Flutter app, Next.js middleware)
  ↓  [batched, compressed, signed]
SIGNAL_INGEST_API (POST /v1/signals — API Gateway → analytics-service)
  ↓  [signature verify · schema validate · PII strip · tenant tag]
KAFKA_TOPIC: analytics.signal.raw
  ↓  [stream processing — Apache Kafka Streams or Faust]
KAFKA_TOPIC: analytics.signal.enriched  (device context + session context added)
  ↓
  ├──→ ClickHouse (append, immutable analytics tables)
  ├──→ Feature Store (materialized ML features via Airflow)
  ├──→ Passive Intelligence Engine (real-time behavioral score update)
  └──→ ERP Aggregation Workers (per-tenant, per-domain dashboards)
```

### 2.2 Emission Standards
```
FORMAT               = JSON (UTF-8, max 4 KB per signal event)
BATCH_SIZE           = max 50 events per batch POST
BATCH_INTERVAL       = 5 seconds (Flutter) · 10 seconds (Next.js)
COMPRESSION          = gzip (required for batch payloads > 1 KB)
TRANSPORT            = HTTPS POST (TLS 1.2 minimum) — plaintext FORBIDDEN
AUTH_HEADER          = Bearer JWT (same session token as REST API)
SIGNAL_SIGNATURE     = HMAC-SHA256 of payload (client-side secret from Vault)
PII_POLICY           = STRIP before emission — see Section 8
IDEMPOTENCY_KEY      = signal_id (UUID v4, client-generated per event)
                       Duplicate signal_id within 24h → DEDUPLICATED (not re-processed)
RATE_LIMIT_PER_USER  = 1000 signals/hour · 10,000 signals/day
RATE_LIMIT_PER_TENANT= 100,000 signals/hour
MISSING_SCHEMA       → REJECT signal at ingest API → LOG → REPORT
UNKNOWN_SIGNAL_TYPE  → REJECT → HARD_STOP
```

### 2.3 Canonical Signal Envelope (All Signals)
```json
{
  "signal_id":        "UUID v4 — client-generated",
  "signal_type":      "string — registered signal type (see catalog Section 3)",
  "signal_version":   "string — semver of signal schema (e.g. '1.0.0')",
  "client_ts":        "ISO 8601 UTC — client-side timestamp",
  "server_ts":        "ISO 8601 UTC — assigned at ingest (overrides client_ts for ordering)",
  "session_id":       "UUID — active session",
  "user_id":          "UUID — authenticated user (NEVER anonymous)",
  "tenant_id":        "UUID — tenant context (hard-required)",
  "role":             "string — RBAC role of emitting user",
  "domain_track":     "enum [Arts|Commerce|Science|Technology|Administration|Society|Platform]",
  "platform":         "enum [flutter_android|flutter_ios|flutter_desktop|nextjs_web]",
  "app_version":      "string — semver",
  "os_version":       "string",
  "screen_name":      "string — active screen at emission time",
  "signal_payload":   "object — signal-type-specific fields (see Section 3)",
  "signal_signature": "string — HMAC-SHA256"
}
```

```
MISSING FIELD IN ENVELOPE              → REJECT signal
ANONYMOUS USER (no user_id)            → REJECT signal
CROSS-TENANT user_id / tenant_id mismatch → REJECT + ALERT compliance
```

---

## SECTION 3 — SIGNAL CATALOG (COMPLETE REGISTRY)

> Every signal type in this catalog is REGISTERED and LOCKED.
> No signal may be emitted that is not in this catalog.
> Adding a new signal type requires a MINOR version bump and CI re-validation.

---

### 3.1 Navigation & Session Signals

#### SIG_001: screen_view
```yaml
signal_type:     screen_view
version:         1.0.0
trigger:         Flutter Navigator pushes a new route OR user returns to a prior route
stage:           STAGE_1
payload:
  screen_name:   string (required — from registered route table)
  previous_screen: string | null
  navigation_method: enum [push|pop|replace|deep_link|tab_switch]
  load_time_ms:  integer (time from intent to first frame rendered)
analytics_use:   funnel analysis · drop-off rates · screen engagement heatmap
intelligence_use: NONE (navigation alone is not a behavioral intelligence signal)
erp_use:         Institute admin: which screens students use most
pii_risk:        LOW — screen names contain no PII
clickhouse_table: ch_screen_views
```

#### SIG_002: session_start
```yaml
signal_type:     session_start
version:         1.0.0
trigger:         App foreground / first authenticated request after login
stage:           STAGE_1
payload:
  session_id:    string (UUID)
  login_method:  enum [password|sso|oauth_linkedin|oauth_github|refresh_token]
  device_type:   enum [phone|tablet|desktop]
  locale:        string (BCP 47)
  timezone:      string (IANA tz)
analytics_use:   DAU · MAU · session frequency · device distribution
intelligence_use: session_frequency → intrapersonal dimension signal (consistency)
erp_use:         Recruiter: candidate engagement frequency
clickhouse_table: ch_sessions
```

#### SIG_003: session_end
```yaml
signal_type:     session_end
version:         1.0.0
trigger:         App background (30s grace) · explicit logout · session timeout
stage:           STAGE_1
payload:
  session_duration_seconds: integer
  screens_visited_count:    integer
  actions_taken_count:      integer
  exit_reason:              enum [logout|timeout|background|crash]
analytics_use:   average session duration · bounce rate · engagement depth
intelligence_use: session_duration + actions_taken → engagement dimension update
clickhouse_table: ch_sessions (UPDATE session record by session_id)
```

#### SIG_004: app_crash
```yaml
signal_type:     app_crash
version:         1.0.0
trigger:         Unhandled Flutter exception / OS-level crash
stage:           STAGE_1
payload:
  error_type:    string (exception class — no stack trace with PII)
  screen_name:   string
  flutter_version: string
  crash_hash:    string (SHA-256 of sanitized stack trace — no PII)
analytics_use:   crash rate monitoring · stability tracking
intelligence_use: NONE
pii_policy:      Stack traces MUST be sanitized before emission — NO user data in stack
clickhouse_table: ch_app_errors
```

---

### 3.2 Job Portal Signals

#### SIG_010: job_search_executed
```yaml
signal_type:     job_search_executed
version:         1.0.0
trigger:         User submits search query on job discovery screen
stage:           STAGE_1
payload:
  query_hash:    string (SHA-256 of search query — not raw query text due to PII risk)
  filters_applied: string[] (filter keys only — not filter values if they contain PII)
  result_count:  integer
  search_latency_ms: integer
analytics_use:   popular search patterns · zero-result searches · search latency
intelligence_use: search_frequency + category → logical_mathematical signal
erp_use:         Recruiter ERP: what job categories candidates search for
clickhouse_table: ch_job_searches
pii_policy:      Raw query text NEVER emitted — SHA-256 hash only
```

#### SIG_011: job_card_viewed
```yaml
signal_type:     job_card_viewed
version:         1.0.0
trigger:         Job card becomes > 70% visible on screen for > 2 seconds
stage:           STAGE_1
payload:
  job_id:        UUID
  view_duration_ms: integer
  position_in_list: integer
  source_screen: enum [discovery|recommendations|search_results|saved_jobs]
analytics_use:   job impression rate · recommendation effectiveness
intelligence_use: job category interest → match scorer feature FS_001
erp_use:         Recruiter ERP: which jobs attract most views
clickhouse_table: ch_job_impressions
```

#### SIG_012: job_applied
```yaml
signal_type:     job_applied
version:         1.0.0
trigger:         User submits job application (after confirmation screen)
stage:           STAGE_1
payload:
  job_id:        UUID
  application_id: UUID
  time_to_apply_seconds: integer (from first job_card_viewed to application submit)
  resume_used:   boolean
analytics_use:   apply rate · time-to-apply funnel
intelligence_use: application intent → placement probability feature
erp_use:         Institute ERP: placement funnel progress per student cohort
clickhouse_table: ch_job_applications_analytics
```

#### SIG_013: application_pipeline_stage_viewed
```yaml
signal_type:     application_pipeline_stage_viewed
version:         1.0.0
trigger:         User opens application tracking screen to check status
stage:           STAGE_1
payload:
  application_id: UUID
  current_stage:  enum [submitted|under_review|shortlisted|rejected|hired]
  days_since_applied: integer
analytics_use:   anxiety funnel — how often candidates check status (engagement metric)
intelligence_use: persistence checking → intrapersonal dimension signal
clickhouse_table: ch_application_tracking_views
```

---

### 3.3 Skill Development Signals

#### SIG_020: course_module_started
```yaml
signal_type:     course_module_started
version:         1.0.0
trigger:         User begins a course module (first video play or content load)
stage:           STAGE_2
payload:
  course_id:     UUID
  module_id:     UUID
  module_index:  integer
  module_type:   enum [video|reading|quiz|lab|project]
  domain_track:  enum [Arts|Commerce|Science|Technology|Administration]
analytics_use:   course start rates · module drop-off
intelligence_use: learning initiation → intrapersonal consistency signal
erp_use:         Institute ERP: curriculum engagement per cohort
clickhouse_table: ch_course_module_events
```

#### SIG_021: course_module_completed
```yaml
signal_type:     course_module_completed
version:         1.0.0
trigger:         User completes module (video 95%+ watched or assessment submitted)
stage:           STAGE_2
payload:
  course_id:     UUID
  module_id:     UUID
  completion_time_seconds: integer
  score:         float | null (if assessment)
  attempts:      integer
analytics_use:   completion rates · average score distribution
intelligence_use: completion + score → logical_mathematical / linguistic dimension update
erp_use:         Institute ERP: student completion tracking per module
clickhouse_table: ch_course_module_events
```

#### SIG_022: skill_assessment_submitted
```yaml
signal_type:     skill_assessment_submitted
version:         1.0.0
trigger:         User submits a skill assessment (quiz, coding challenge, lab)
stage:           STAGE_2
payload:
  assessment_id: UUID
  skill_id:      UUID
  skill_category: string
  score:         float (0.0–1.0)
  time_taken_seconds: integer
  attempt_number: integer
analytics_use:   skill acquisition rate · assessment difficulty calibration
intelligence_use: score + time_taken + attempt_number → 8-dim intelligence update
                  (logical_mathematical for tech · linguistic for arts · etc.)
erp_use:         Institute ERP: cohort skill acquisition heatmap
clickhouse_table: ch_skill_assessments
```

#### SIG_023: certification_attempt_started
```yaml
signal_type:     certification_attempt_started
version:         1.0.0
trigger:         User initiates a certification exam
stage:           STAGE_2
payload:
  certification_id: UUID
  certification_type: string
  prerequisite_modules_completed: integer
  prerequisite_modules_total: integer
analytics_use:   certification funnel · readiness gap analysis
intelligence_use: attempt initiation → motivation signal → intrapersonal
clickhouse_table: ch_certification_events
```

---

### 3.4 Group Discussion (Voice GD) Signals

#### SIG_030: gd_waiting_room_entered
```yaml
signal_type:     gd_waiting_room_entered
version:         1.0.0
trigger:         User successfully joins GD waiting room (pre-session phase)
stage:           STAGE_2
payload:
  session_id:    UUID
  batch_id:      UUID
  join_offset_seconds: integer (seconds before session scheduled start)
  device_type:   enum [phone|tablet|desktop]
analytics_use:   on-time join rate · drop-off before session starts
intelligence_use: punctuality → interpersonal dimension signal
clickhouse_table: ch_gd_events
```

#### SIG_031: gd_turn_speaking_started
```yaml
signal_type:     gd_turn_speaking_started
version:         1.0.0
trigger:         Backend grants speaking token + client mic opens
stage:           STAGE_2
payload:
  session_id:    UUID
  turn_type:     enum [intro|rebuttal|open|conclusion]
  turn_index:    integer
  token_duration_seconds: integer
analytics_use:   turn utilization rate per session
intelligence_use: speaking initiation → linguistic_verbal signal start
clickhouse_table: ch_gd_turn_events
```

#### SIG_032: gd_turn_speaking_ended
```yaml
signal_type:     gd_turn_speaking_ended
version:         1.0.0
trigger:         Speaking token expires OR user voluntarily yields
stage:           STAGE_2
payload:
  session_id:    UUID
  turn_type:     enum [intro|rebuttal|open|conclusion]
  actual_spoke_seconds: integer
  token_duration_seconds: integer
  utilization_pct: float (actual / token * 100)
  yield_type:    enum [timeout|voluntary]
analytics_use:   average speaking time · participation distribution
intelligence_use: utilization_pct → linguistic_verbal + interpersonal dimension update
erp_use:         Institute ERP: student communication engagement per batch
clickhouse_table: ch_gd_turn_events
```

#### SIG_033: gd_interrupt_attempt_ui
```yaml
signal_type:     gd_interrupt_attempt_ui
version:         1.0.0
trigger:         User attempts to speak when NOT token holder (UI detects mic gesture)
stage:           STAGE_2
payload:
  session_id:    UUID
  interrupted_user_id: UUID (redacted — stored as hash for privacy)
analytics_use:   interrupt behavior tracking per session
intelligence_use: interrupt_rate → interpersonal dimension penalty signal
clickhouse_table: ch_gd_behavior_events
pii_policy:      interrupted_user_id → SHA-256 hash (not raw UUID in this signal)
```

---

### 3.5 Dojo Match Signals

#### SIG_040: dojo_match_screen_viewed
```yaml
signal_type:     dojo_match_screen_viewed
version:         1.0.0
trigger:         User opens assigned Dojo match screen
stage:           STAGE_2
payload:
  match_id:      UUID
  scenario_id:   UUID
  role_assigned: string
  time_to_open_seconds: integer (from match.assigned event to screen open)
analytics_use:   match engagement latency · role acceptance rate
intelligence_use: time_to_open → motivation signal → intrapersonal
clickhouse_table: ch_dojo_events
```

#### SIG_041: dojo_peer_evaluation_submitted
```yaml
signal_type:     dojo_peer_evaluation_submitted
version:         1.0.0
trigger:         User submits evaluation of a peer in Dojo match
stage:           STAGE_2
payload:
  match_id:      UUID
  evaluatee_id_hash: string (SHA-256 — not raw UUID)
  scores:        map<rubric_axis, float>
  time_taken_seconds: integer
analytics_use:   peer evaluation completion rate · score variance analysis
intelligence_use: evaluation thoroughness → interpersonal dimension signal
clickhouse_table: ch_dojo_evaluation_events
pii_policy:      evaluatee_id → SHA-256 hash only
```

#### SIG_042: dojo_belt_eligibility_viewed
```yaml
signal_type:     dojo_belt_eligibility_viewed
version:         1.0.0
trigger:         User opens belt eligibility check screen
stage:           STAGE_2
payload:
  belt_level:    string
  eligible:      boolean
  criteria_met:  string[] (criteria codes — not verbose descriptions)
  criteria_pending: string[]
analytics_use:   belt progression rate · eligibility gap analysis
intelligence_use: checking eligibility → achievement orientation → intrapersonal
clickhouse_table: ch_dojo_progression_events
```

---

### 3.6 Intelligence Engine Signals

#### SIG_050: intelligence_test_answer_submitted
```yaml
signal_type:     intelligence_test_answer_submitted
version:         1.0.0
trigger:         User submits answer to an intelligence test item
stage:           STAGE_2
payload:
  test_id:       UUID
  item_id:       UUID
  intelligence_dimension: enum [logical_mathematical|linguistic_verbal|spatial_visual|
                                bodily_kinesthetic|musical_rhythmic|interpersonal|
                                intrapersonal|naturalist]
  answer_correct:  boolean
  response_time_ms: integer
  difficulty_level: enum [easy|medium|hard|adaptive]
analytics_use:   item difficulty calibration · dimension score distribution
intelligence_use: DIRECT INPUT to intelligence_scorer_v1 model
                  → updates intelligence_vector dimension for that item's type
erp_use:         Institute ERP: cohort intelligence profile heatmap
clickhouse_table: ch_intelligence_test_responses
```

#### SIG_051: intelligence_profile_viewed
```yaml
signal_type:     intelligence_profile_viewed
version:         1.0.0
trigger:         User opens their own intelligence profile dashboard screen
stage:           STAGE_2
payload:
  profile_version: string (which intelligence_vector snapshot is being viewed)
  has_completed_full_test: boolean
  dimensions_with_scores: integer (0–8)
analytics_use:   profile engagement rate · test completion funnel
intelligence_use: NONE (viewing profile is not itself a behavioral signal)
clickhouse_table: ch_intelligence_profile_views
```

---

### 3.7 Innovation Engine Signals

#### SIG_060: idea_draft_started
```yaml
signal_type:     idea_draft_started
version:         1.0.0
trigger:         User begins filling the idea submission form (first keystroke)
stage:           STAGE_3
payload:
  idea_draft_id: UUID (client-generated, not yet server-side idea_id)
  category_selected: string | null
  mechanism_selected: string | null
analytics_use:   idea submission funnel · draft abandonment rate
intelligence_use: idea initiation → creative signal → naturalist + spatial_visual dimensions
clickhouse_table: ch_innovation_events
```

#### SIG_061: idea_submitted
```yaml
signal_type:     idea_submitted
version:         1.0.0
trigger:         User completes and submits idea (server confirms receipt)
stage:           STAGE_3
payload:
  idea_id:       UUID
  category:      string
  mechanism:     string
  word_count:    integer (of description — not the description text itself)
  time_to_submit_seconds: integer (from draft_started to submitted)
analytics_use:   idea submission volume · category distribution · quality proxy (word_count)
intelligence_use: idea_submitted → innovation orientation → high-weight naturalist signal
clickhouse_table: ch_innovation_events
```

#### SIG_062: similarity_result_viewed
```yaml
signal_type:     similarity_result_viewed
version:         1.0.0
trigger:         User views idea similarity check result screen
stage:           STAGE_3
payload:
  idea_id:       UUID
  theft_risk_level: enum [NONE|LOW|MEDIUM|HIGH|DEFINITE_COPY]
  user_action:   enum [continued|revised|abandoned]
analytics_use:   similarity result response behavior · theft risk distribution
intelligence_use: response to similarity check → reflective thinking → intrapersonal
clickhouse_table: ch_innovation_events
```

---

### 3.8 Royalty & Licensing Signals

#### SIG_070: royalty_wallet_viewed
```yaml
signal_type:     royalty_wallet_viewed
version:         1.0.0
trigger:         User opens royalty wallet screen (balance view)
stage:           STAGE_3
payload:
  wallet_id:     UUID
  balance_band:  enum [zero|low_0_to_1k|mid_1k_to_10k|high_10k_plus]
                 # Balance amount NOT emitted — band only (privacy)
  contract_count: integer
analytics_use:   royalty engagement rate · wallet activity frequency
intelligence_use: NONE (financial viewing is not a behavioral intelligence signal)
erp_use:         Platform admin: royalty wallet engagement trend
clickhouse_table: ch_royalty_events
pii_policy:      Exact balance NEVER emitted — band only
```

#### SIG_071: contract_details_viewed
```yaml
signal_type:     contract_details_viewed
version:         1.0.0
trigger:         User opens a specific licensing contract detail screen
stage:           STAGE_3
payload:
  contract_id:   UUID
  contract_status: enum [DRAFT|ACTIVE|TERMINATED|EXPIRED]
  view_duration_seconds: integer
analytics_use:   contract engagement rate
intelligence_use: reading legal documents → linguistic_verbal signal (comprehension behavior)
clickhouse_table: ch_royalty_events
```

---

### 3.9 ERP Domain Signals

#### SIG_080: erp_dashboard_loaded
```yaml
signal_type:     erp_dashboard_loaded
version:         1.0.0
trigger:         Institute/Enterprise/Recruiter/Society admin loads their ERP dashboard
stage:           STAGE_3
payload:
  erp_type:      enum [institute|enterprise|recruiter|society|platform_admin]
  dashboard_id:  string (which dashboard variant)
  widget_count_visible: integer
  load_time_ms:  integer
analytics_use:   ERP engagement rate per tenant type · dashboard performance
erp_use:         Platform admin: which ERP dashboards are actively used
clickhouse_table: ch_erp_events
```

#### SIG_081: erp_report_generated
```yaml
signal_type:     erp_report_generated
version:         1.0.0
trigger:         Admin generates a downloadable ERP report
stage:           STAGE_3
payload:
  erp_type:      enum [institute|enterprise|recruiter|society|platform_admin]
  report_type:   string (report category code — not report content)
  date_range_days: integer
  record_count:  integer
analytics_use:   report usage frequency · popular report types per ERP segment
erp_use:         Platform admin: ERP feature utilization tracking
clickhouse_table: ch_erp_events
```

#### SIG_082: recruiter_pipeline_action
```yaml
signal_type:     recruiter_pipeline_action
version:         1.0.0
trigger:         Recruiter takes action on a candidate in the hiring pipeline
stage:           STAGE_3
payload:
  action_type:   enum [viewed_profile|shortlisted|rejected|moved_to_interview|hired]
  time_on_profile_seconds: integer (before taking action)
  profile_completeness_bucket: enum [low|medium|high|complete]
                 # Not the candidate's identity
analytics_use:   recruiter decision speed · profile completeness effect on hiring
intelligence_use: NONE (recruiter behavior, not candidate behavior)
erp_use:         Recruiter ERP: personal hiring analytics dashboard
clickhouse_table: ch_recruiter_behavior_analytics
pii_policy:      Candidate identity NOT in signal — only profile completeness band
```

#### SIG_083: institute_cohort_report_viewed
```yaml
signal_type:     institute_cohort_report_viewed
version:         1.0.0
trigger:         Institute admin views a cohort placement or analytics report
stage:           STAGE_3
payload:
  report_type:   enum [placement_funnel|skill_gap|gd_participation|
                        intelligence_cohort|attendance|certification_progress]
  cohort_size:   integer
  date_range_days: integer
analytics_use:   institute analytics engagement · which reports drive action
erp_use:         Platform admin: institute ERP feature usage
clickhouse_table: ch_erp_events
```

---

### 3.10 Society Domain Signals

#### SIG_090: society_attendance_marked
```yaml
signal_type:     society_attendance_marked
version:         1.0.0
trigger:         QR attendance scan OR manual coach mark triggers client confirmation
stage:           STAGE_3
payload:
  batch_id:      UUID
  society_id:    UUID
  attendance_method: enum [qr_scan|offline_sync|manual]
  mark_offset_seconds: integer (offset from batch start time)
analytics_use:   attendance compliance rate per society · method distribution
erp_use:         Society ERP: batch compliance dashboard
clickhouse_table: ch_society_analytics
```

#### SIG_091: society_coach_performance_viewed
```yaml
signal_type:     society_coach_performance_viewed
version:         1.0.0
trigger:         Society admin opens coach performance screen
stage:           STAGE_3
payload:
  coach_id_hash: string (SHA-256 — not raw UUID)
  performance_band: enum [GREEN|YELLOW|RED] (from RUBRIC_004)
  viewed_by_role: enum [society_admin|coordinator|master_organizer]
analytics_use:   governance engagement — do admins actively monitor coaches?
erp_use:         Society ERP: governance oversight frequency
clickhouse_table: ch_society_analytics
pii_policy:      Coach identity → SHA-256 hash only in signal
```

#### SIG_092: tournament_result_viewed
```yaml
signal_type:     tournament_result_viewed
version:         1.0.0
trigger:         User views tournament leaderboard or personal result screen
stage:           STAGE_3
payload:
  tournament_id: UUID
  society_id:    UUID
  user_rank:     integer | null (null if user is admin viewing full board)
  result_viewed_within_hours: integer (hours after tournament completion)
analytics_use:   post-tournament engagement · leaderboard viral signal
intelligence_use: competitive engagement → interpersonal dimension signal
erp_use:         Society ERP: tournament engagement rate
clickhouse_table: ch_society_analytics
```

---

### 3.11 Billing & Subscription Signals

#### SIG_100: pricing_page_viewed
```yaml
signal_type:     pricing_page_viewed
version:         1.0.0
trigger:         User opens platform pricing / plan comparison screen
stage:           STAGE_1
payload:
  current_plan:  string (plan code)
  viewed_plan:   string | null (which plan card user focused on)
  view_duration_seconds: integer
analytics_use:   pricing page conversion funnel · plan interest distribution
erp_use:         Platform admin: pricing page engagement analytics
clickhouse_table: ch_billing_analytics
```

#### SIG_101: plan_upgrade_intent
```yaml
signal_type:     plan_upgrade_intent
version:         1.0.0
trigger:         User clicks "Upgrade" or "Start Trial" CTA on any plan
stage:           STAGE_1
payload:
  from_plan:     string
  to_plan:       string
  trigger_source: enum [pricing_page|feature_gate|notification|recommendation]
analytics_use:   upgrade conversion rate · feature gate effectiveness
erp_use:         Platform admin: upgrade funnel analytics
clickhouse_table: ch_billing_analytics
```

#### SIG_102: feature_gate_hit
```yaml
signal_type:     feature_gate_hit
version:         1.0.0
trigger:         User attempts to access a feature blocked by their subscription plan
stage:           STAGE_1
payload:
  feature_code:  string (registered feature code)
  current_plan:  string
  required_plan: string
  screen_name:   string
analytics_use:   feature gate conversion pressure · most-hit gates per plan
erp_use:         Platform admin: feature demand by plan tier
clickhouse_table: ch_billing_analytics
```

---

### 3.12 Platform Health & Observability Signals (Client-Side)

#### SIG_110: api_error_experienced
```yaml
signal_type:     api_error_experienced
version:         1.0.0
trigger:         Flutter app receives HTTP 4xx or 5xx from any API call
stage:           STAGE_1
payload:
  endpoint_hash: string (SHA-256 of endpoint path — not full URL)
  http_status:   integer
  error_code:    string (application error code — not message)
  latency_ms:    integer
  retry_count:   integer
analytics_use:   client-perceived error rate · API reliability from user perspective
erp_use:         Platform ops: client error distribution dashboard
clickhouse_table: ch_app_errors
pii_policy:      Full URL NEVER emitted — SHA-256 of path only
```

#### SIG_111: slow_screen_load_detected
```yaml
signal_type:     slow_screen_load_detected
version:         1.0.0
trigger:         Screen load_time_ms > 3000ms (threshold configurable via Unleash flag)
stage:           STAGE_1
payload:
  screen_name:   string
  load_time_ms:  integer
  network_type:  enum [wifi|cellular_4g|cellular_3g|cellular_2g|offline]
  platform:      enum [flutter_android|flutter_ios|flutter_desktop]
analytics_use:   performance regression detection · network-aware SLA tracking
erp_use:         Platform ops: screen performance dashboard
clickhouse_table: ch_app_performance
```

---

## SECTION 4 — FEATURE VECTOR SCHEMA REGISTRY

### 4.1 Feature Store Standards
```
FEATURE_STORE_DB       = PostgreSQL 15 (feature_store schema)
CACHE_LAYER            = Redis 7 (TTL per feature group — see below)
MATERIALIZATION        = Apache Airflow scheduled jobs (hourly/daily)
REGISTRY_LOCATION      = /contracts/features/{feature_group}/v{version}/features.yaml
VERSIONING             = MAJOR.MINOR.PATCH
BREAKING_CHANGE        = MAJOR (removing a feature, changing type)
NON_BREAKING           = MINOR (adding new optional feature)
GATE_SIGNAL_CONSUMED   = feature_vector_ready (for Model Registry Agent — MODEL_003+)
```

### 4.2 Feature Group Catalog

#### FS_001: Job-Candidate Match Features
```yaml
feature_group:   job_candidate_match
version:         1.0.0
materialized_for: Scoring Engine (MODEL_003 match-scorer-v1)
refresh_cadence: hourly
redis_ttl:       3600 seconds
source_signals:
  - SIG_011 (job_card_viewed) — category interest vector
  - SIG_012 (job_applied) — application intent signals
  - SIG_022 (skill_assessment_submitted) — verified skill scores
  - kafka: job.application.submitted, interview.completed
features:
  skill_overlap_ratio:       float   # candidate skills ∩ job requirements / job requirements
  experience_match_score:    float   # years experience vs job requirement
  location_preference_score: float   # candidate preferred location vs job location
  salary_expectation_ratio:  float   # expected / offered
  availability_match:        boolean
  category_interest_score:   float   # from SIG_011 viewing pattern (last 30 days)
  intelligence_composite:    float   # weighted intelligence_vector composite (15% weight)
  application_frequency:     integer # applications in last 30 days
  assessment_score_avg:      float   # average SIG_022 score across relevant skills
  profile_completeness:      float   # 0.0–1.0
```

#### FS_002: Intelligence Behavioral Features
```yaml
feature_group:   intelligence_behavioral
version:         1.0.0
materialized_for: intelligence_scorer_v1 (MODEL_006)
refresh_cadence: real-time (Kafka Streams consumer)
redis_ttl:       900 seconds (15 min rolling window)
source_signals:
  - SIG_020–SIG_023 (skill / certification signals)
  - SIG_030–SIG_033 (GD signals)
  - SIG_040–SIG_042 (Dojo signals)
  - SIG_050–SIG_051 (intelligence test signals)
  - SIG_060–SIG_062 (innovation signals)
features:
  # Per intelligence dimension (8 values):
  dimension_signal_count:    int[8]    # raw signal count per dimension
  dimension_signal_strength: float[8]  # weighted signal strength per dimension
  dimension_last_updated:    timestamp[8]
  session_consistency_score: float     # from session_start frequency pattern
  learning_completion_rate:  float     # from SIG_021
  gd_utilization_avg:        float     # from SIG_032 utilization_pct average
  gd_interrupt_rate:         float     # from SIG_033 count / gd_sessions_attended
  innovation_initiation_count: integer # from SIG_060
  peer_evaluation_thoroughness: float  # from SIG_041 time_taken_seconds normalized
```

#### FS_003: Placement Probability Features
```yaml
feature_group:   placement_probability
version:         1.0.0
materialized_for: placement_predictor_v1 (MODEL_004)
refresh_cadence: daily
redis_ttl:       86400 seconds
source_signals:
  - SIG_010–SIG_013 (job portal signals)
  - SIG_022 (skill assessment)
  - SIG_050 (intelligence tests)
  - kafka: job.application.* lifecycle events
features:
  total_applications_sent:   integer
  shortlisting_rate:         float   # shortlisted / applied
  interview_conversion_rate: float
  skill_coverage_score:      float   # % of market-demanded skills covered
  assessment_pass_rate:      float   # % of submitted assessments passed
  gd_participation_score:    float   # from Scoring Engine (RUBRIC_001)
  intelligence_composite:    float
  profile_recency_score:     float   # how recently profile was updated
  domain_track_alignment:    boolean # profile domain = target job domain
```

#### FS_004: Recruiter Behavior Features
```yaml
feature_group:   recruiter_behavior
version:         1.0.0
materialized_for: analytics dashboards · recruiter behavior analytics (AI advises only)
refresh_cadence: daily
redis_ttl:       43200 seconds
source_signals:
  - SIG_082 (recruiter_pipeline_action)
  - kafka: job.application.shortlisted · job.application.hired · job.listing.*
features:
  avg_time_to_decision_seconds: float
  shortlisting_rate:           float
  hire_rate:                   float
  profile_completeness_bias:   float  # correlation between completeness and shortlisting
  domain_preference_vector:    float[5] # per domain_track hire rate
  job_posting_frequency:       float
  avg_applications_per_posting: float
pii_policy: features are aggregate metrics only — no individual candidate identity
```

#### FS_005: Society Unit Health Features
```yaml
feature_group:   society_unit_health
version:         1.0.0
materialized_for: society-analytics-service · society ERP dashboards
refresh_cadence: daily
redis_ttl:       86400 seconds
source_signals:
  - SIG_090 (attendance)
  - SIG_091 (coach performance)
  - SIG_092 (tournament)
  - kafka: commission.calculated · payout.processed · batch.started
features:
  enrollment_growth_rate:      float
  attendance_compliance_rate:  float  # % batches meeting 80% threshold
  gender_participation_ratio:  float  # female / total
  coach_green_rate:            float  # % coaches in GREEN band (RUBRIC_004)
  tournament_participation_rate: float
  commission_disbursement_rate:  float
  payout_7day_compliance_rate:   float
  scheme_approval_rate:          float
```

#### FS_006: ERP Engagement Features
```yaml
feature_group:   erp_engagement
version:         1.0.0
materialized_for: platform admin ERP analytics · tenant health scoring
refresh_cadence: daily
redis_ttl:       86400 seconds
source_signals:
  - SIG_080 (erp_dashboard_loaded)
  - SIG_081 (erp_report_generated)
  - SIG_083 (institute_cohort_report_viewed)
  - SIG_100–SIG_102 (billing signals)
features:
  erp_dau:                   integer  # daily active ERP users per tenant
  report_generation_rate:    float    # reports/active_user/week
  feature_gate_hit_rate:     float    # upgrade conversion pressure
  dashboard_load_latency_p95: float   # 95th percentile load time
  most_used_report_type:     string   # mode of report_type in SIG_081
  upgrade_intent_rate:       float    # plan_upgrade_intent / pricing_page_viewed
```

---

## SECTION 5 — CLICKHOUSE TABLE CONTRACTS

### 5.1 ClickHouse Standards
```
ENGINE               = ClickHouse (self-hosted · Apache 2.0)
NAMESPACE            = analytics
INGESTION            = Kafka Kafka Connect (clickhouse-kafka-connect) → ClickHouse
TABLE_ENGINE         = MergeTree (for analytics) · ReplacingMergeTree (for dedup)
PARTITION_BY         = toYYYYMM(server_ts) on all tables
ORDER_BY             = (tenant_id, user_id, server_ts) unless noted
IMMUTABILITY         = Append-only — no UPDATEs or DELETEs except TTL expiry
TTL_POLICY           = See per-table (default: 2 years raw · 5 years aggregated)
TENANT_ISOLATION     = WHERE tenant_id = ? enforced in every query from analytics-service
REGISTRY_LOCATION    = /contracts/clickhouse/{table_name}/v{version}/schema.sql
```

### 5.2 ClickHouse Table Definitions

```sql
-- ch_screen_views: SIG_001
CREATE TABLE ch_screen_views (
  signal_id         UUID,
  server_ts         DateTime64(3, 'UTC'),
  tenant_id         UUID,
  user_id           UUID,
  role              LowCardinality(String),
  domain_track      LowCardinality(String),
  platform          LowCardinality(String),
  app_version       String,
  screen_name       LowCardinality(String),
  previous_screen   LowCardinality(String),
  navigation_method LowCardinality(String),
  load_time_ms      UInt32,
  session_id        UUID
) ENGINE = MergeTree()
PARTITION BY toYYYYMM(server_ts)
ORDER BY (tenant_id, user_id, server_ts)
TTL server_ts + INTERVAL 2 YEAR;

-- ch_sessions: SIG_002 + SIG_003
CREATE TABLE ch_sessions (
  signal_id              UUID,
  server_ts              DateTime64(3, 'UTC'),
  tenant_id              UUID,
  user_id                UUID,
  role                   LowCardinality(String),
  session_id             UUID,
  event_type             LowCardinality(String), -- 'start' | 'end'
  login_method           LowCardinality(String),
  device_type            LowCardinality(String),
  locale                 String,
  timezone               String,
  session_duration_seconds UInt32,
  screens_visited_count  UInt16,
  actions_taken_count    UInt16,
  exit_reason            LowCardinality(String)
) ENGINE = ReplacingMergeTree()
PARTITION BY toYYYYMM(server_ts)
ORDER BY (tenant_id, user_id, session_id, event_type)
TTL server_ts + INTERVAL 2 YEAR;

-- ch_job_searches: SIG_010
CREATE TABLE ch_job_searches (
  signal_id        UUID,
  server_ts        DateTime64(3, 'UTC'),
  tenant_id        UUID,
  user_id          UUID,
  query_hash       String,
  result_count     UInt32,
  search_latency_ms UInt32,
  filters_applied  Array(String),
  session_id       UUID
) ENGINE = MergeTree()
PARTITION BY toYYYYMM(server_ts)
ORDER BY (tenant_id, user_id, server_ts)
TTL server_ts + INTERVAL 2 YEAR;

-- ch_job_impressions: SIG_011
CREATE TABLE ch_job_impressions (
  signal_id          UUID,
  server_ts          DateTime64(3, 'UTC'),
  tenant_id          UUID,
  user_id            UUID,
  job_id             UUID,
  view_duration_ms   UInt32,
  position_in_list   UInt16,
  source_screen      LowCardinality(String),
  session_id         UUID
) ENGINE = MergeTree()
PARTITION BY toYYYYMM(server_ts)
ORDER BY (tenant_id, job_id, server_ts)
TTL server_ts + INTERVAL 2 YEAR;

-- ch_course_module_events: SIG_020 + SIG_021
CREATE TABLE ch_course_module_events (
  signal_id              UUID,
  server_ts              DateTime64(3, 'UTC'),
  tenant_id              UUID,
  user_id                UUID,
  event_type             LowCardinality(String), -- 'started' | 'completed'
  course_id              UUID,
  module_id              UUID,
  module_type            LowCardinality(String),
  domain_track           LowCardinality(String),
  completion_time_seconds UInt32,
  score                  Nullable(Float32),
  attempts               UInt8,
  session_id             UUID
) ENGINE = MergeTree()
PARTITION BY toYYYYMM(server_ts)
ORDER BY (tenant_id, user_id, course_id, module_id, server_ts)
TTL server_ts + INTERVAL 2 YEAR;

-- ch_skill_assessments: SIG_022
CREATE TABLE ch_skill_assessments (
  signal_id         UUID,
  server_ts         DateTime64(3, 'UTC'),
  tenant_id         UUID,
  user_id           UUID,
  assessment_id     UUID,
  skill_id          UUID,
  skill_category    LowCardinality(String),
  score             Float32,
  time_taken_seconds UInt32,
  attempt_number    UInt8
) ENGINE = MergeTree()
PARTITION BY toYYYYMM(server_ts)
ORDER BY (tenant_id, user_id, skill_id, server_ts)
TTL server_ts + INTERVAL 5 YEAR;  -- longer for skill history

-- ch_gd_turn_events: SIG_031 + SIG_032
CREATE TABLE ch_gd_turn_events (
  signal_id                UUID,
  server_ts                DateTime64(3, 'UTC'),
  tenant_id                UUID,
  user_id                  UUID,
  session_id               UUID,
  event_type               LowCardinality(String), -- 'started' | 'ended'
  turn_type                LowCardinality(String),
  turn_index               UInt8,
  token_duration_seconds   UInt16,
  actual_spoke_seconds     Nullable(UInt16),
  utilization_pct          Nullable(Float32),
  yield_type               Nullable(LowCardinality(String))
) ENGINE = MergeTree()
PARTITION BY toYYYYMM(server_ts)
ORDER BY (tenant_id, session_id, user_id, turn_index)
TTL server_ts + INTERVAL 3 YEAR;

-- ch_intelligence_test_responses: SIG_050
CREATE TABLE ch_intelligence_test_responses (
  signal_id               UUID,
  server_ts               DateTime64(3, 'UTC'),
  tenant_id               UUID,
  user_id                 UUID,
  test_id                 UUID,
  item_id                 UUID,
  intelligence_dimension  LowCardinality(String),
  answer_correct          Bool,
  response_time_ms        UInt32,
  difficulty_level        LowCardinality(String)
) ENGINE = MergeTree()
PARTITION BY toYYYYMM(server_ts)
ORDER BY (tenant_id, user_id, test_id, item_id)
TTL server_ts + INTERVAL 5 YEAR;  -- intelligence history is long-lived

-- ch_recruiter_behavior_analytics: SIG_082
CREATE TABLE ch_recruiter_behavior_analytics (
  signal_id                    UUID,
  server_ts                    DateTime64(3, 'UTC'),
  tenant_id                    UUID,
  recruiter_user_id            UUID,
  action_type                  LowCardinality(String),
  time_on_profile_seconds      UInt32,
  profile_completeness_bucket  LowCardinality(String)
) ENGINE = MergeTree()
PARTITION BY toYYYYMM(server_ts)
ORDER BY (tenant_id, recruiter_user_id, server_ts)
TTL server_ts + INTERVAL 2 YEAR;

-- ch_society_analytics: SIG_090–SIG_092
CREATE TABLE ch_society_analytics (
  signal_id          UUID,
  server_ts          DateTime64(3, 'UTC'),
  tenant_id          UUID,
  society_id         UUID,
  signal_type        LowCardinality(String),
  batch_id           Nullable(UUID),
  attendance_method  Nullable(LowCardinality(String)),
  performance_band   Nullable(LowCardinality(String)),
  tournament_id      Nullable(UUID),
  user_rank          Nullable(UInt32)
) ENGINE = MergeTree()
PARTITION BY toYYYYMM(server_ts)
ORDER BY (tenant_id, society_id, server_ts)
TTL server_ts + INTERVAL 3 YEAR;

-- ch_billing_analytics: SIG_100–SIG_102
CREATE TABLE ch_billing_analytics (
  signal_id          UUID,
  server_ts          DateTime64(3, 'UTC'),
  tenant_id          UUID,
  user_id            UUID,
  signal_type        LowCardinality(String),
  current_plan       LowCardinality(String),
  to_plan            Nullable(LowCardinality(String)),
  feature_code       Nullable(String),
  required_plan      Nullable(LowCardinality(String)),
  view_duration_seconds Nullable(UInt32),
  trigger_source     Nullable(LowCardinality(String))
) ENGINE = MergeTree()
PARTITION BY toYYYYMM(server_ts)
ORDER BY (tenant_id, user_id, server_ts)
TTL server_ts + INTERVAL 2 YEAR;

-- ch_app_errors: SIG_004 + SIG_110
CREATE TABLE ch_app_errors (
  signal_id        UUID,
  server_ts        DateTime64(3, 'UTC'),
  tenant_id        UUID,
  user_id          UUID,
  platform         LowCardinality(String),
  app_version      String,
  error_type       LowCardinality(String),
  http_status      Nullable(UInt16),
  error_code       Nullable(String),
  latency_ms       Nullable(UInt32),
  screen_name      LowCardinality(String),
  crash_hash       Nullable(String)
) ENGINE = MergeTree()
PARTITION BY toYYYYMM(server_ts)
ORDER BY (tenant_id, platform, server_ts)
TTL server_ts + INTERVAL 1 YEAR;

-- ch_app_performance: SIG_111
CREATE TABLE ch_app_performance (
  signal_id        UUID,
  server_ts        DateTime64(3, 'UTC'),
  tenant_id        UUID,
  user_id          UUID,
  platform         LowCardinality(String),
  screen_name      LowCardinality(String),
  load_time_ms     UInt32,
  network_type     LowCardinality(String),
  app_version      String
) ENGINE = MergeTree()
PARTITION BY toYYYYMM(server_ts)
ORDER BY (tenant_id, platform, screen_name, server_ts)
TTL server_ts + INTERVAL 1 YEAR;
```

---

## SECTION 6 — ANALYTICS SERVICE DASHBOARD CONTRACTS

### 6.1 Dashboard Registry
```
SERVICE             = analytics-service
NAMESPACE           = analytics
DASHBOARD_TOOL      = Grafana (self-hosted · ops namespace) for ops
                      Custom ERP dashboards in Flutter/Next.js for end-users
DATA_SOURCE         = ClickHouse (read-only connection from analytics-service)
QUERY_ISOLATION     = WHERE tenant_id = ? MANDATORY on all ClickHouse reads
REGISTRY_LOCATION   = /contracts/analytics/dashboards/{dashboard_id}/v{version}/
```

### 6.2 Mandatory Dashboard Catalog

| Dashboard ID | Consumer Role | Data Source Tables | Refresh |
|---|---|---|---|
| DASH_001: GD Failure Rate | Platform Ops | ch_gd_turn_events | 5 min |
| DASH_002: Interview No-Show Rate | Recruiter ERP | ch_sessions + kafka events | hourly |
| DASH_003: Scoring Anomaly Monitor | Platform Admin | ch_skill_assessments | hourly |
| DASH_004: Billing Error Rate | Platform Ops | ch_billing_analytics | 5 min |
| DASH_005: Media QoS (GD) | Platform Ops | ch_gd_turn_events | 5 min |
| DASH_006: Student Placement Funnel | Institute ERP | ch_job_impressions + ch_job_applications_analytics | daily |
| DASH_007: Skill Gap Heatmap | Institute ERP | ch_skill_assessments | daily |
| DASH_008: Intelligence Cohort Profile | Institute ERP | ch_intelligence_test_responses | daily |
| DASH_009: Recruiter Decision Analytics | Recruiter ERP | ch_recruiter_behavior_analytics | daily |
| DASH_010: Society Unit Health | Society ERP | ch_society_analytics | daily |
| DASH_011: Feature Gate Conversion | Platform Admin | ch_billing_analytics | hourly |
| DASH_012: App Crash & Error Rate | Platform Ops | ch_app_errors | 5 min |
| DASH_013: Screen Performance P95 | Platform Ops | ch_app_performance | 15 min |
| DASH_014: DAU / MAU Trend | Platform Admin | ch_sessions | daily |
| DASH_015: Royalty Wallet Engagement | Platform Admin | ch_royalty_events | daily |

```
DASHBOARD_WITHOUT_REGISTERED_CONTRACT → STOP EXECUTION → REPORT DASHBOARD_UNREGISTERED:{id}
CLICKHOUSE_QUERY_WITHOUT_TENANT_FILTER → STOP EXECUTION → REPORT CROSS_TENANT_QUERY_VIOLATION
```

---

## SECTION 7 — PASSIVE INTELLIGENCE ENGINE FEED CONTRACT

### 7.1 Real-Time Signal → Dimension Map
```
CONSUMER_SERVICE     = intelligence-profile-service (passive-intelligence-engine)
INPUT_TOPIC          = analytics.signal.enriched (Kafka)
OUTPUT_TOPIC         = intelligence.signal.received (Kafka)
PROCESSING_MODEL     = Kafka Streams (stateful, per-user window)
STATE_STORE          = Redis (user_id keyed · 15-min rolling window · 7-day decay)
UPDATE_FREQUENCY     = real-time on signal receipt (not batched)
INTELLIGENCE_LAW     = M5 — AI ADVISORY ONLY · human cannot be overridden by intelligence score
```

### 7.2 Signal → Intelligence Dimension Routing
```
SIGNAL               DIMENSION(S) UPDATED        WEIGHT    DECAY_DAYS
──────────────────────────────────────────────────────────────────────
SIG_022 (skill_assessment score > 0.8)
                     logical_mathematical         HIGH      30
                     linguistic_verbal (Arts)     HIGH      30
                     spatial_visual (Tech/Science) MED      30

SIG_031/032 (gd_turn utilization_pct)
                     linguistic_verbal            HIGH      14
                     interpersonal                MED       14

SIG_033 (gd_interrupt_rate)
                     interpersonal (penalty)      MED       14

SIG_040/041 (dojo peer evaluation thoroughness)
                     interpersonal                MED       30

SIG_050 (intelligence test item correct + fast)
                     [dimension from item]        HIGH      90

SIG_060/061 (idea submitted)
                     naturalist                   HIGH      60
                     spatial_visual               MED       60

SIG_020/021 (course completion rate)
                     intrapersonal (persistence)  MED       30

SIG_002/003 (session consistency — sessions per week)
                     intrapersonal (consistency)  LOW       7

SIG_092 (tournament_result — competitive engagement)
                     interpersonal                LOW       14

SIG_023 (certification attempt started)
                     intrapersonal (ambition)     MED       30

DECAY_RULE: signal contribution decreases linearly over decay_days
            at day 0: full weight · at decay_days: zero weight
            signals beyond decay window → excluded from recalculation
RECALCULATION_TRIGGER: any new signal for user → incremental update (not full recalc)
FULL_RECALC_TRIGGER:   once per 24 hours (Airflow scheduled job)
```

---

## SECTION 8 — PII STRIPPING CONTRACT

### 8.1 PII Rules (MANDATORY — ALL Signals)
```
RULE_PII_01: User's full name → NEVER in any signal payload
RULE_PII_02: Email address → NEVER in any signal payload
RULE_PII_03: Phone number → NEVER in any signal payload
RULE_PII_04: Aadhaar / national ID → NEVER in any signal payload
RULE_PII_05: Raw financial amounts → NEVER — use bands (zero/low/mid/high)
RULE_PII_06: Other user's identity → SHA-256 hash only (SIG_033, SIG_041, SIG_082, SIG_091)
RULE_PII_07: Search query text → SHA-256 hash only (SIG_010)
RULE_PII_08: Full API URL → SHA-256 hash of path only (SIG_110)
RULE_PII_09: Location coordinates → NEVER — use region code (ISO 3166-2 state/region)
RULE_PII_10: Raw score of another user → NEVER — only user's own score in their signal

PII_STRIP_LAYER    = Signal Ingest API (server-side) — second line of defense
CLIENT_STRIP_LAYER = Flutter SDK (client-side) — first line of defense
BOTH_LAYERS_MANDATORY = TRUE

VIOLATION → HARD_STOP → REPORT PII_IN_SIGNAL_VIOLATION:{signal_type}:{field_name}
```

---

## SECTION 9 — TENANT & DOMAIN ISOLATION CONTRACT

```
RULE_ISO_01: Every signal MUST carry tenant_id. Signals without tenant_id → REJECTED.
RULE_ISO_02: user_id MUST belong to the tenant declared in tenant_id.
             Mismatch → REJECTED + ALERT compliance-service.
RULE_ISO_03: ClickHouse writes MUST include tenant_id column.
             ClickHouse reads from analytics-service MUST filter by tenant_id.
RULE_ISO_04: Feature Store entries are keyed by (user_id, tenant_id).
             Cross-tenant feature reads → FORBIDDEN.
RULE_ISO_05: Society signals (SIG_090–092) carry society_id in addition to tenant_id.
             Society signal reads MUST filter by both tenant_id AND society_id.
RULE_ISO_06: ERP dashboards exposed to tenants MUST show only that tenant's data.
             Platform admin dashboards aggregate across tenants (explicit admin role only).
RULE_ISO_07: Domain track isolation — Arts/Commerce/Science intelligence profiles
             are computed separately and NEVER cross-pollinate.
RULE_ISO_08: Signals from Next.js SEO web frontend are tagged with platform = nextjs_web
             and EXCLUDED from intelligence dimension updates (read-only surface — no behavioral scoring).

ALL ISOLATION VIOLATIONS → HARD_STOP → REPORT:{rule_id}
```

---

## SECTION 10 — KAFKA EVENT CONTRACT (ANALYTICS TOPICS)

```
analytics.signal.raw
  → Schema: canonical signal envelope (Section 2.3)
  → Partitioned by: user_id
  → Retention: 7 days

analytics.signal.enriched
  → Schema: signal envelope + server_ts + device_context + session_context
  → Partitioned by: user_id
  → Retention: 7 days
  → Consumed by: ClickHouse Kafka Connect · Passive Intelligence Engine · ERP workers

analytics.feature.materialized
  → Schema: {feature_group, user_id, tenant_id, features{}, computed_at}
  → Partitioned by: user_id
  → Retention: 24 hours
  → Consumed by: Model inference services (read-only)

analytics.erp.metric.computed
  → Schema: {metric_id, tenant_id, domain, metric_name, value, period, computed_at}
  → Partitioned by: tenant_id
  → Retention: 30 days
  → Consumed by: ERP dashboard API endpoints

analytics.signal.rejected
  → Schema: {signal_id, reject_reason, signal_type, tenant_id, server_ts}
  → Partitioned by: tenant_id
  → Retention: 30 days (for debugging and compliance review)

MISSING_TOPIC           → STOP EXECUTION → REPORT KAFKA_TOPIC_MISSING:{topic}
SIGNAL_NOT_IN_CATALOG   → REJECTED to analytics.signal.rejected → ALERT ops
```

---

## SECTION 11 — CI VALIDATION GATE

```
GATE_SIGNALS_PRODUCED   = feature_vector_ready · analytics_pipeline_ready
GATE_CONSUMED_BY        = Intelligence Engine (Lane F) · Analytics Service ·
                          ERP Dashboard generation · Feature Store population ·
                          Scoring Engine (for FS_001–FS_003)

CI_VALIDATOR_SCRIPT     = /ci/validators/feature_vector_emission_gate_validator.sh
TRIGGER                 = Every push to test, staging, production branches

VALIDATION_STEPS:
  1.  Parse Section 3 signal catalog — verify ALL signal types have version, payload schema, clickhouse_table
  2.  Parse Section 4 feature groups — verify all features reference valid source signals
  3.  Parse Section 5 ClickHouse tables — run CREATE TABLE dry-run on ClickHouse test instance
  4.  Verify Kafka topics in Section 10 are registered in Apicurio Schema Registry
  5.  Verify Flutter SDK includes all registered signal types (grep signal_type catalog vs SDK source)
  6.  Verify Next.js middleware excludes intelligence signals (RULE_ISO_08)
  7.  Parse Section 7 dimension routing — verify all signals reference registered signal types
  8.  Verify Section 8 PII rules are implemented in Signal Ingest API (static analysis scan)
  9.  Verify Section 9 tenant isolation rules present in analytics-service query layer
  10. Verify all DASH_* dashboards in Section 6 reference registered ClickHouse tables
  11. Verify no hardcoded credentials in signal SDK or ingest API (secrets scanner)
  12. Run signal envelope schema validation against 100 synthetic test signals

ON_FAILURE: CI HARD_STOP → block merge → post failure to ops channel
ON_SUCCESS: PRODUCES gate artifacts:
            feature_vector_gate_check.json (all 12 steps: PASSED)
            GATE SIGNALS: feature_vector_ready · analytics_pipeline_ready
```

---

## SECTION 12 — TECHNOLOGY BINDINGS (NON-NEGOTIABLE)

```
ANALYTICS_DB         = ClickHouse (self-hosted · Apache 2.0) — NO BigQuery, NO Redshift
STREAM_PROCESSOR     = Apache Kafka Streams — NO Spark Streaming (overweight), NO Flink (cost)
FEATURE_STORE        = PostgreSQL 15 + Redis 7 — NO Feast (adds infra complexity), NO SageMaker
VECTOR_STORE         = Qdrant (self-hosted) — NO Pinecone (paid SaaS)
MATERIALIZATION_JOB  = Apache Airflow — NO dbt Cloud (paid SaaS)
CLIENT_SDK           = Flutter package (in-house, embedded in app) — NO Firebase Analytics
WEB_SDK              = Next.js middleware (server-side, minimal) — NO Segment, NO Amplitude
BI_TOOL              = Metabase (self-hosted · optional) for internal ops — NO Looker, NO Tableau
SCHEMA_REGISTRY      = Apicurio Registry (self-hosted) — signal schemas registered here

FORBIDDEN TOOLS:
  - Firebase Analytics / Google Analytics → FORBIDDEN (external SaaS, PII risk)
  - Mixpanel / Amplitude / Segment        → FORBIDDEN (paid SaaS, data leaves platform)
  - AWS Kinesis / GCP Dataflow            → FORBIDDEN (paid managed service)
  - BigQuery / Redshift / Snowflake       → FORBIDDEN (paid managed service)
  - Any signal routing through third-party before ClickHouse → FORBIDDEN

VIOLATION → HARD_STOP → REPORT FORBIDDEN_ANALYTICS_TOOL_DETECTED:{tool}
```

---

## SECTION 13 — ANTIGRAVITY EXECUTION COMMANDS

```
# Register a new signal type
REGISTER_SIGNAL
  SIGNAL_TYPE   = skill_assessment_submitted
  VERSION       = 1.0.0
  STAGE         = STAGE_2
  FILL_TEMPLATE = SECTION_3_SIGNAL_CATALOG_ENTRY

# Generate ClickHouse table DDL for a signal
GENERATE_CLICKHOUSE_TABLE
  SIGNAL_TYPE   = intelligence_test_answer_submitted
  TABLE_NAME    = ch_intelligence_test_responses
  FILL_TEMPLATE = SECTION_5_CLICKHOUSE_SCHEMA

# Register a Feature Group
REGISTER_FEATURE_GROUP
  GROUP_ID      = job_candidate_match
  MATERIALIZED_FOR = match-scorer-v1
  FILL_TEMPLATE = SECTION_4_FEATURE_GROUP

# Map signal to intelligence dimension
MAP_SIGNAL_DIMENSION
  SIGNAL_TYPE   = gd_turn_speaking_ended
  DIMENSION     = linguistic_verbal
  WEIGHT        = HIGH
  DECAY_DAYS    = 14
  FILL_TEMPLATE = SECTION_7_DIMENSION_ROUTING

# Validate all emission gates
VALIDATE_FEATURE_VECTOR_GATES
  SCOPE         = ALL
  OUTPUT        = feature_vector_gate_check.json
```

---

## SECTION 14 — FINAL SEAL

```
AGENT_STATUS             = COMPLETE · SEALED · LOCKED
CHANGE_POLICY            = APPEND_ONLY VIA VERSION BUMP
ANTIGRAVITY_CONFUSION    = IMPOSSIBLE
ASSUMPTION_PERMITTED     = NONE
IMPLICIT_BEHAVIOR        = NONE
QUALITY_SCORE            = 10 / 10

GATES PRODUCED BY THIS AGENT:
  ✔ feature_vector_ready
  ✔ analytics_pipeline_ready

SERVICES UNLOCKED BY THESE GATES:
  ✔ Intelligence Engine (Lane F) — ai_ready gate can now be attempted
  ✔ Analytics Service — ClickHouse ingestion pipeline can be built
  ✔ ERP dashboards — all 15 dashboards can be generated
  ✔ Passive Intelligence Engine — real-time dimension updates enabled
  ✔ Feature Store population jobs (Airflow) — can be deployed
  ✔ Scoring Engine (FS_001/002/003 features) — model inference unblocked

SERVICES BLOCKED UNTIL GATES PASS:
  ✗ Matching Engine (needs FS_001)
  ✗ Placement Probability Predictor (needs FS_003)
  ✗ Intelligence Profile evolution timeline
  ✗ Any ERP dashboard that reads ClickHouse
  ✗ Any Airflow feature materialization job

⚠️ Antigravity MUST NOT:
   - Emit a signal type not listed in Section 3
   - Write to a ClickHouse table without a registered schema in Section 5
   - Build a feature group not registered in Section 4
   - Route PII into any signal payload
   - Use Firebase Analytics, Mixpanel, Amplitude, or any paid analytics SaaS
   - Allow Next.js web signals to update intelligence dimensions (RULE_ISO_08)
   - Allow cross-tenant ClickHouse queries without explicit tenant_id filter
   - Allow any signal emitted by AUTOMATION/AI_AGENT role (signals are human behavioral only)

⚠️ This agent inherits and enforces:
   - STAGE_2 development gate (intelligence signals require STAGE_1 contracts locked)
   - M5 AI Reality Law (AI advises only — feature vectors feed models, models do not decide)
   - Multi-tenant RLS law (tenant_id on all analytics writes and reads)
   - Four-Stage Development Model (STAGE_1 → STAGE_2 → STAGE_3 → STAGE_4 sequentially)
   - Domain Track Isolation (Arts/Commerce/Science intelligence profiles are separate)

DOCUMENT_END: PHONE_FEATURE_VECTOR_EMISSION_AGENT v1.0.0
STATUS: FINAL · LOCKED · GOVERNED · DETERMINISTIC
```
