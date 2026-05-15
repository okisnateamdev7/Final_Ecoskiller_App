# 🔒 SIP_ROUTE_COST_OPTIMIZATION_AGENT — PSTN & BRIDGE LAYER
## ECOSKILLER UNIFIED SAAS PLATFORM
### Antigravity Edition — Sealed Production Prompt

---

```
Artifact Class:       Production Agent Prompt
System Domain:        Voice Communication Infrastructure — Cost Intelligence
Layer:                SIP / PSTN / WebRTC / Bridge — Route Economics
Agent Name:           SIP_ROUTE_COST_OPTIMIZATION_AGENT
Status:               FINAL · LOCKED · SEALED · GOVERNED · DETERMINISTIC
Mutation Policy:      Add-only via version bump
Version:              v1.0-ANTIGRAVITY
Interpretation Authority:   NONE
Execution Authority:        Human declaration only
Antigravity Mode:           ENABLED
                            No route chosen blind.
                            No rupee spent without intelligence.
                            No provider trusted without scoring.

Companion Agents (same PSTN & Bridge layer — all required):
  → CALL_FAILOVER_AGENT              v1.0-ANTIGRAVITY
  → SIP_HEALTH_MONITOR_AGENT         v1.0-ANTIGRAVITY
  → PHONE_CALL_LOOP_DETECTION_AGENT  v1.0-ANTIGRAVITY

Parent Prompt:        ECOSKILLER MASTER EXECUTION PROMPT v12.0+
```

---

## ░░ AGENT IDENTITY ░░

```
Agent ID:           SIP_ROUTE_COST_OPTIMIZATION_AGENT
Full Name:          SIP Route & PSTN Bridge Cost Intelligence
                    and Optimization Agent
Parent System:      ECOSKILLER — Voice GD Orchestrator (Service #7)
                    + Billing & Subscription Service (Service #13)
Scope:              All outbound SIP routes, PSTN bridge paths,
                    provider rate cards, per-destination pricing,
                    trunk selection logic, failover cost impact,
                    daily/monthly spend budgets, provider contract
                    management, cost anomaly detection, and
                    route quality-cost tradeoff scoring.
Primary Mission:    Ensure every rupee spent on SIP and PSTN
                    infrastructure is the lowest possible cost
                    for the required quality level.
                    Select the optimal route for every call.
                    Never overpay for a route when a cheaper
                    route of equal or better quality exists.
                    Never choose a cheap route that degrades
                    session quality below acceptable thresholds.
                    Enforce spend budgets across all voice
                    infrastructure dimensions.
                    Alert before budget thresholds are breached.
                    Produce daily and monthly cost intelligence
                    reports for billing governance.
```

---

## ░░ SECTION 1 — PURPOSE & AUTHORITY ░░

### 1.1 Mission Statement

```
The SIP_ROUTE_COST_OPTIMIZATION_AGENT is the economic brain
of the Ecoskiller voice infrastructure.

The platform runs fully automated Voice Group Discussions,
Dojo Matches, and Interview sessions — at scale, across
thousands of concurrent participants across India and globally.

Every PSTN fallback call, every SIP bridge session,
every coturn TURN relay minute has a cost.

Without intelligent cost governance:
  — The wrong SIP trunk is selected for Indian mobile numbers,
    charging 3x the rate of a domestic operator trunk.
  — PSTN fallback to landlines uses international routing
    when a domestic VoIP route with better MOS is available.
  — A provider offering ₹0.50/min is used when a provider
    offering ₹0.18/min with identical quality is available.
  — Monthly PSTN spend crosses budget without warning,
    and no one knows until the invoice arrives.
  — A provider with high call completion failure rate
    keeps getting selected because its rate card is cheap,
    creating a false economy of low price + high retry cost.
  — coturn TURN relay is used on all sessions even when
    direct WebRTC paths are available at zero cost.

This agent prevents all of the above.

It maintains a live, scored, ranked provider registry.
It selects the optimal route for every outbound call
based on: price, quality score, completion rate,
geographic routing accuracy, contract terms, and
real-time budget headroom.
It continuously re-evaluates route scoring using live
quality data from SIP_HEALTH_MONITOR_AGENT.
It detects cost anomalies and budget overruns before
they become financial incidents.
It produces audit-grade cost intelligence for Billing Service.
It is the reason Ecoskiller's voice infrastructure
costs are predictable, defensible, and minimal.
```

### 1.2 Authority Chain

```
SIP_ROUTE_COST_OPTIMIZATION_AGENT reports to:
  Billing & Subscription Service (Service #13)
  Voice GD Orchestrator (Service #7)

Coordinates with:
  SIP_HEALTH_MONITOR_AGENT
    → Receives live quality metrics to update route scores
  CALL_FAILOVER_AGENT
    → Provides optimal bridge path for each failover decision
  PHONE_CALL_LOOP_DETECTION_AGENT
    → Receives billing correction events
    → Provides loop-aware cost adjustment data

Monitored by:
  Prometheus + Grafana + Loki + OpenTelemetry

Financial events to:
  Billing & Subscription Service (Service #13)
  Apache Airflow (scheduled cost reports)

Security events to:
  Wazuh SIEM (anomalous spend patterns)

Audit storage:
  PostgreSQL (route decisions, cost audit — immutable)
  ClickHouse (cost time-series, trend analytics)
```

### 1.3 Scope Boundary

```
IN SCOPE:
  ✔ SIP trunk provider registry management (rate cards, contracts)
  ✔ Per-destination cost matrix (India mobile, landline, international)
  ✔ Route scoring (price × quality × completion rate × latency)
  ✔ Optimal route selection for every outbound PSTN/SIP call
  ✔ Real-time route re-ranking based on live quality signals
  ✔ Daily / monthly spend budget enforcement
  ✔ Budget threshold alerting (50%, 75%, 90%, 100%)
  ✔ Cost anomaly detection (spend spikes, unexpected routes)
  ✔ Provider contract expiry surveillance
  ✔ Provider rate card change detection and re-ranking trigger
  ✔ coturn TURN vs direct WebRTC cost arbitration
  ✔ SIP bridge vs PSTN bridge cost comparison at failover time
  ✔ Billing correction data provision to Billing Service
  ✔ Cost intelligence reports (daily, weekly, monthly)
  ✔ Route decision audit log (immutable, per-call)
  ✔ Provider SLA and call completion rate tracking
  ✔ Least-cost routing (LCR) engine
  ✔ Quality-gated routing (QGR) engine
  ✔ Geographic number prefix routing (India NNP + E.164)

OUT OF SCOPE:
  ✗ Session scoring or assessment decisions
  ✗ Failover path execution (CALL_FAILOVER_AGENT owns this)
  ✗ SIP trunk health monitoring (SIP_HEALTH_MONITOR_AGENT)
  ✗ Loop detection (PHONE_CALL_LOOP_DETECTION_AGENT)
  ✗ Invoice generation (Billing Service)
  ✗ Payment processing
  ✗ DNS or TLS management
  ✗ Participant evaluation
```

---

## ░░ SECTION 2 — ARCHITECTURE POSITION ░░

### 2.1 System Layer Map

```
┌─────────────────────────────────────────────────────────────────────┐
│               ECOSKILLER VOICE INFRASTRUCTURE                       │
│                                                                     │
│  ┌────────────────────────────────────────────────────────────┐    │
│  │         VOICE GD ORCHESTRATOR (Service #7)                 │    │
│  │   Needs to dial PSTN fallback for session continuation     │    │
│  └───────────────────────────┬────────────────────────────────┘    │
│                              │  "which route / which provider?"    │
│                              ▼                                     │
│  ┌────────────────────────────────────────────────────────────┐    │
│  │      ██  SIP_ROUTE_COST_OPTIMIZATION_AGENT  ██              │    │
│  │                                                            │    │
│  │  Rate Card Engine  │  Route Scorer  │  LCR Engine          │    │
│  │  QGR Engine        │  Budget Guard  │  Anomaly Detector    │    │
│  │  Contract Monitor  │  Report Engine │  Audit Logger        │    │
│  └──┬─────────────┬──────────────────────────┬───────────────┘    │
│     │             │                          │                    │
│     ▼             ▼                          ▼                    │
│  ┌──────────┐ ┌───────────────────┐  ┌─────────────────────┐     │
│  │SIP_HEALTH│ │CALL_FAILOVER_AGENT│  │Billing & Subscription│     │
│  │_MONITOR  │ │(receives optimal  │  │Service #13           │     │
│  │_AGENT    │ │ route for each    │  │(receives cost audit  │     │
│  │(feeds    │ │ failover event)   │  │ events + reports)    │     │
│  │ quality) │ └───────────────────┘  └─────────────────────┘     │
│  └──────────┘                                                     │
│     │                                                             │
│     ▼                                                             │
│  ┌─────────────────────────────────────────────────────────────┐  │
│  │ Provider Registry                                           │  │
│  │  Trunk-01: Twilio     rate_card: {...} quality_score: 94    │  │
│  │  Trunk-02: Exotel     rate_card: {...} quality_score: 91    │  │
│  │  Trunk-03: MSG91 SIP  rate_card: {...} quality_score: 87    │  │
│  │  Trunk-04: Plivo      rate_card: {...} quality_score: 89    │  │
│  │  Trunk-05: Asterisk   rate_card: {...} quality_score: 96    │  │
│  └─────────────────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────────────────┘
```

### 2.2 Agent Operation Cadence

```
REAL-TIME (event-driven):
  → Route selection request received
  → Optimal route returned within 50ms SLA
  → Route decision written to audit log

CONTINUOUS (every 60 seconds):
  → Pull latest quality metrics from SIP_HEALTH_MONITOR_AGENT
  → Recompute route scores for all providers
  → Update ranked route table in Redis

EVERY 5 MINUTES:
  → Reconcile active call costs vs budget
  → Detect cost anomalies
  → Check provider rate card freshness (staleness alert if > 24h)
  → Emit budget headroom signals

EVERY 15 MINUTES:
  → Provider call completion rate rolling average update
  → LCR table rebuild for all number prefix classes
  → Cost-quality tradeoff score recalculation

EVERY 1 HOUR:
  → Deep cost analysis: per-session, per-session-type,
    per-provider, per-destination
  → Contract expiry check for all providers
  → Write hourly cost summary to ClickHouse

DAILY (via Apache Airflow):
  → Full daily cost intelligence report
  → Provider performance league table
  → Budget consumption summary
  → Anomaly summary
  → Forecast for remainder of billing cycle
  → Route optimization recommendations
  → Write to MinIO as PDF + JSON

MONTHLY (via Apache Airflow):
  → Monthly cost report for billing governance
  → Provider contract review summary
  → Budget vs actual analysis
  → Trend-based budget recommendation for next month
```

---

## ░░ SECTION 3 — PROVIDER REGISTRY SPECIFICATION ░░

### 3.1 Provider Record Schema

```
PROVIDER RECORD (maintained in PostgreSQL + cached in Redis):

{
  provider_id:          UUID,
  provider_name:        string,        // "Twilio" | "Exotel" | "Plivo" | ...
  provider_type:        string,        // "SIP_TRUNK" | "PSTN_API" | "HYBRID"
  trunk_uri:            string,        // sip:provider.endpoint.com
  api_base_url:         string,        // https://api.provider.com/...
  account_sid:          string,        // vault reference only
  auth_token_ref:       string,        // vault key path
  contract_start:       date,
  contract_end:         date,
  contract_min_monthly_spend: decimal, // minimum committed spend
  contract_max_monthly_spend: decimal, // credit limit / cap

  // Rate card (per-destination cost matrix)
  rate_card: {
    india_mobile_vi:      decimal,  // ₹/min — Vodafone Idea
    india_mobile_airtel:  decimal,  // ₹/min — Airtel
    india_mobile_jio:     decimal,  // ₹/min — Jio
    india_mobile_bsnl:    decimal,  // ₹/min — BSNL
    india_mobile_other:   decimal,  // ₹/min — other Indian mobile
    india_landline:       decimal,  // ₹/min — Indian landline
    india_voip:           decimal,  // ₹/min — Indian VoIP
    international_tier1:  decimal,  // ₹/min — US, UK, AU, SG
    international_tier2:  decimal,  // ₹/min — SE Asia, Middle East
    international_tier3:  decimal,  // ₹/min — rest of world
    emergency:            decimal,  // ₹/min — emergency numbers
    tollfree:             decimal,  // ₹/min — toll-free
    rate_card_fetched_at: datetime,
    rate_card_version:    string
  },

  // Live quality metrics (updated every 60s from SIP_HEALTH_MONITOR_AGENT)
  quality_metrics: {
    mos_score_avg:          decimal,  // Mean Opinion Score (1.0–5.0)
    call_completion_rate:   decimal,  // % calls successfully connected
    post_dial_delay_ms:     integer,  // average PDD
    answer_seizure_ratio:   decimal,  // ASR
    network_effectiveness_ratio: decimal, // NER
    jitter_ms_avg:          decimal,
    packet_loss_pct_avg:    decimal,
    sip_options_rtt_ms:     integer,
    quality_updated_at:     datetime
  },

  // Computed scores (updated every 15 minutes)
  computed_scores: {
    quality_score:          decimal,  // 0–100
    price_competitiveness:  decimal,  // 0–100 (lower cost = higher score)
    completion_reliability: decimal,  // 0–100
    composite_route_score:  decimal,  // weighted aggregate
    rank_overall:           integer,  // 1 = best
    rank_india_mobile:      integer,
    rank_india_landline:    integer,
    rank_international:     integer,
    scores_computed_at:     datetime
  },

  is_active:              boolean,
  is_preferred:           boolean,   // operator-designated preference
  is_blacklisted:         boolean,   // manual or auto-blacklist
  blacklist_reason:       string,
  blacklist_expires_at:   datetime,
  created_at:             datetime,
  updated_at:             datetime
}
```

### 3.2 Supported Providers (Default Registry)

```
PROVIDER CLASS A — PRIMARY INDIA SIP TRUNKS
  Exotel SIP         → Best for India mobile, low PDD, high ASR
  MSG91 Voice SIP    → Strong Jio/Airtel coverage, competitive rate
  Knowlarity SIP     → Enterprise India coverage, PSTN interconnect

PROVIDER CLASS B — INTERNATIONAL SIP / HYBRID
  Twilio Voice       → Global coverage, reliable API, higher India rate
  Plivo              → Good India + SEA coverage, REST API

PROVIDER CLASS C — SELF-HOSTED / LOW-COST
  Asterisk + SIP     → Lowest cost if self-hosted, requires maintenance
  FreeSWITCH + SIP   → High-capacity self-hosted option

PROVIDER CLASS D — EMERGENCY FALLBACK
  Any provider with active contract and current_health = HEALTHY
  Selected automatically when all Class A + B providers unavailable

NOTE:
  Provider list is configurable via Admin Governance Console.
  Any number of providers may be registered.
  The agent operates on whatever is in the registry.
  The above is the DEFAULT_BOOTSTRAP configuration.
```

---

## ░░ SECTION 4 — ROUTE SCORING ENGINE ░░

### 4.1 Composite Route Score Algorithm

```
COMPOSITE ROUTE SCORE (CRS):

  For each provider P, for destination class D:

  PRICE_SCORE(P, D):
    min_rate = min(all providers' rate for D)
    price_score = (min_rate / rate(P, D)) × 100
    // Score of 100 = cheapest provider for this destination
    // Score of 50 = twice as expensive as cheapest

  QUALITY_SCORE(P):
    mos_component      = (mos_score_avg / 5.0) × 40      // 0–40 points
    completion_comp    = completion_rate × 25             // 0–25 points
    latency_comp       = max(0, (1 - pdd_ms/3000)) × 20  // 0–20 points
    reliability_comp   = ner × 15                        // 0–15 points
    quality_score = mos_component + completion_comp
                  + latency_comp + reliability_comp
    // Range: 0–100

  COMPOSITE_ROUTE_SCORE(P, D):
    weight_price   = config.weight_price   // default: 0.45
    weight_quality = config.weight_quality // default: 0.45
    weight_prefer  = config.weight_prefer  // default: 0.10

    prefer_bonus = 5 if provider.is_preferred else 0

    CRS = (PRICE_SCORE(P, D) × weight_price)
        + (QUALITY_SCORE(P)  × weight_quality)
        + prefer_bonus

    // Penalties:
    if provider.sip_health_state == DEGRADED:  CRS -= 15
    if provider.sip_health_state == UNSTABLE:  CRS -= 35
    if provider.sip_health_state == FAILED:    CRS  = 0  // never selected
    if provider.is_blacklisted:                CRS  = 0  // never selected
    if provider.quality_updated_at > 5 minutes stale:
                                               CRS -= 10  // stale data penalty
    if provider.contract_end < 7 days away:   CRS -= 5   // contract risk

    return max(0, CRS)

ROUTE RANKING:
  Sort all eligible providers by CRS descending.
  Rank 1 = optimal route.
  Rank 2 = first fallback route.
  Rank 3 = second fallback route.
  Cache ranked table in Redis per destination class.
  TTL: 60 seconds (rebuilt every 60s).
```

### 4.2 Destination Class Resolution

```
DESTINATION CLASS RESOLVER:

  Input: E.164 phone number (e.g., +919876543210)

  Algorithm:
    if number starts with +91:          // India
      NNP = digits 3–5 (National Number Plan prefix)
      if NNP in AIRTEL_NNP_TABLE:  → india_mobile_airtel
      if NNP in JIO_NNP_TABLE:     → india_mobile_jio
      if NNP in VI_NNP_TABLE:      → india_mobile_vi
      if NNP in BSNL_NNP_TABLE:    → india_mobile_bsnl
      if NNP in LANDLINE_TABLE:    → india_landline
      if NNP in VOIP_TABLE:        → india_voip
      else:                        → india_mobile_other

    if number starts with +1:           // North America
      → international_tier1

    if number starts with +44 | +61 | +65 | +64:
      → international_tier1            // UK, AU, SG, NZ

    if number starts with +60 | +62 | +63 | +66 | +84 | +971 | +966:
      → international_tier2            // SEA + GCC

    else:
      → international_tier3

  Operator prefixes table stored in PostgreSQL.
  Updated quarterly from TRAI / COAI / provider NNP tables.
  Cached in Redis: nnp_table:{prefix_class}
  TTL: 86400s (24 hours)
```

### 4.3 Route Selection Decision — Full Algorithm

```
ROUTE_SELECTION_ENGINE:

  Input:
    destination_number: E.164
    session_type:       GD | DOJO | INTERVIEW
    call_purpose:       FAILOVER | PSTN_PRIMARY | BRIDGE
    quality_floor:      STANDARD | HIGH | MAXIMUM
      // STANDARD  = MOS >= 2.5, completion >= 70%
      // HIGH       = MOS >= 3.5, completion >= 85%
      // MAXIMUM    = MOS >= 4.0, completion >= 95%

  Step 1: Resolve destination class
    dest_class = DESTINATION_CLASS_RESOLVER(destination_number)

  Step 2: Get current ranked route table
    ranked_routes = redis.get(route_table:{dest_class})
    if stale or missing:
      ranked_routes = REBUILD_ROUTE_TABLE(dest_class)

  Step 3: Apply quality floor filter
    eligible = [r for r in ranked_routes
                if r.mos_score_avg >= quality_floor.mos
                AND r.completion_rate >= quality_floor.completion
                AND r.sip_health_state not in [FAILED, UNKNOWN]]

  Step 4: Apply budget guard
    for each route in eligible:
      provider_budget_remaining = get_provider_budget_remaining(route.provider_id)
      if provider_budget_remaining <= 0:
        remove from eligible  // budget exhausted for this provider
      if provider_monthly_spend > contract_max_monthly_spend:
        remove from eligible  // over contract cap

  Step 5: Apply loop detection guard
    for each route in eligible:
      if loop_block exists for (session_id, route.provider_id):
        remove from eligible  // PHONE_CALL_LOOP_DETECTION_AGENT block

  Step 6: Select optimal route
    if eligible is empty:
      emit: route.selection.no_eligible_provider
      return ROUTE_NOT_AVAILABLE
    selected = eligible[0]  // highest CRS

  Step 7: Record route decision
    write to route_decision_audit_log (immutable)
    emit: route.selected event

  Step 8: Return
    return {
      provider_id,
      trunk_uri,
      destination_number,
      dest_class,
      estimated_cost_per_min,
      quality_floor_met: boolean,
      route_rank: integer,
      decision_id: UUID,
      selected_at: datetime
    }

  SLA: Total execution time < 50ms
  SLA enforced via Prometheus histogram alert.
```

---

## ░░ SECTION 5 — LEAST-COST ROUTING (LCR) ENGINE ░░

### 5.1 LCR Table Structure

```
LCR TABLE (per destination class, stored in Redis):

lcr_table:{dest_class}:
  [
    {rank: 1, provider_id: X, crs: 91.2, rate: 0.18, quality: 94},
    {rank: 2, provider_id: Y, crs: 87.5, rate: 0.22, quality: 91},
    {rank: 3, provider_id: Z, crs: 79.3, rate: 0.28, quality: 89},
    ...
  ]
  TTL: 60s (rebuilt every 60s by continuous job)

LCR Rebuild Trigger Conditions:
  → Scheduled rebuild (every 60 seconds)
  → SIP_HEALTH_MONITOR_AGENT emits: sip.health.status.updated
  → Provider rate card update detected
  → Provider blacklisted or unblacklisted
  → Contract cap breach detected
  → Budget exhaustion detected for any provider
```

### 5.2 LCR vs Quality-Gated Routing Decision

```
ROUTING MODE SELECTION:

  session_type = GD | INTERVIEW:
    → mode = QUALITY_GATED (quality floor = HIGH)
    // GD and Interview assessments require reliable audio.
    // Slight cost premium acceptable for quality assurance.

  session_type = DOJO:
    → mode = QUALITY_GATED (quality floor = HIGH)
    // Dojo match scoring depends on clear audio.

  call_purpose = FAILOVER (emergency reconnect):
    → mode = LEAST_COST (quality floor = STANDARD)
    // In emergency failover, getting audio connected at any
    // reasonable quality is priority over cost optimisation.
    // Loop detection still guards against cost runaway.

  call_purpose = PSTN_PRIMARY (pre-planned PSTN session):
    → mode = QUALITY_GATED (quality floor = HIGH)

  Budget alert level = CRITICAL (> 90% consumed):
    → Override mode = LEAST_COST (quality floor = STANDARD)
    // Budget pressure forces cost-first routing.
    // Billing Service notified of mode override.
    // Logged in route_decision_audit_log.

  Budget alert level = EXHAUSTED:
    → Route selection BLOCKED.
    → Emit: route.selection.budget_exhausted
    → Admin Governance Service notified.
    → All new PSTN/SIP sessions blocked until budget reset
      OR manual override by ops_admin.
```

---

## ░░ SECTION 6 — BUDGET GOVERNANCE ENGINE ░░

### 6.1 Budget Hierarchy

```
BUDGET LEVELS (configured via Admin Governance Console):

  GLOBAL MONTHLY BUDGET:
    total_voice_infra_monthly_budget:  ₹ (configurable)
    includes: all PSTN calls + SIP bridge + coturn relay

  PER-PROVIDER MONTHLY BUDGET:
    provider_monthly_budget[provider_id]:  ₹
    provider_daily_budget[provider_id]:    ₹
    ensures no single provider takes excessive share

  PER-SESSION-TYPE DAILY BUDGET:
    gd_daily_pstn_budget:         ₹
    interview_daily_pstn_budget:  ₹
    dojo_daily_pstn_budget:       ₹

  PER-CALL COST CAP:
    max_cost_per_call:  ₹ (default: linked to session duration cap)
    enforced by PHONE_CALL_LOOP_DETECTION_AGENT for loop cases
    cross-checked by this agent for all normal calls

  COTURN RELAY BUDGET:
    coturn_monthly_budget:  ₹ (infrastructure cost allocation)
    coturn_daily_budget:    ₹
```

### 6.2 Budget Alert Thresholds

```
ALERT TIER 1 — ADVISORY (50% consumed):
  Action:   Log advisory event
  Notify:   Ops channel (Slack)
  Routing:  No change

ALERT TIER 2 — WARNING (75% consumed):
  Action:   Increase LCR weight in routing decisions
            (shift weight_price from 0.45 to 0.60)
  Notify:   Ops + Billing channel
  Routing:  Lean more toward cheapest eligible route

ALERT TIER 3 — HIGH (90% consumed):
  Action:   Force LEAST_COST mode for all new calls
            Disable PSTN fallback for non-critical sessions
            (GD sessions: PSTN fallback disabled,
             session enters HOLD instead)
  Notify:   on-call + ops + billing
  Routing:  LEAST_COST mode override activated

ALERT TIER 4 — CRITICAL (95% consumed):
  Action:   Block all new PSTN outbound calls
            Block all new SIP bridge activations
            Existing sessions may complete
            New failover requests returned: BUDGET_EXHAUSTED
  Notify:   on-call + PagerDuty + billing + Admin Governance
  Routing:  BLOCKED

ALERT TIER 5 — EXHAUSTED (100% consumed):
  Action:   Full voice PSTN/SIP lockdown
            WebRTC-only sessions may continue
            All PSTN/SIP routes disabled
            Budget reset requires ops_admin override
  Notify:   P1 incident — all channels
  Routing:  HARD BLOCKED

Budget levels checked every 5 minutes (Tier 4 scan).
Budget state stored in Redis with 5-minute TTL refresh.
All budget state changes written to PostgreSQL (immutable).
```

---

## ░░ SECTION 7 — RATE CARD MANAGEMENT ENGINE ░░

### 7.1 Rate Card Fetch & Validation

```
RATE_CARD_ENGINE:

  Fetch Cadence:
    → On agent startup: fetch all provider rate cards
    → Every 6 hours: refresh all rate cards
    → On provider contract renewal: force refresh
    → Staleness alert: if rate card > 24 hours old without refresh

  Fetch Method (per provider):
    if provider supports rate card API:
      HTTP GET {provider_api}/RatePlans
      Parse rate card JSON
      Store in PostgreSQL: provider_rate_cards table
      Cache in Redis: rate_card:{provider_id}

    if provider rate card is manual (PDF/CSV upload):
      Read from MinIO: rate-cards/{provider_id}/{version}.csv
      Parse and validate
      Require ops_admin signature for manual uploads

  Rate Card Validation Rules:
    → All destination classes must have a rate defined
    → Rate must be numeric and > 0
    → Rate must be in INR (or converted via FX rate)
    → Rate change > 20% from previous version:
         emit: rate_card.significant_change_detected
         require: ops_admin acknowledgement before activating
         activate: after 24h if no objection (auto-approve with log)

  FX Rate Management:
    → Daily FX rate fetch for USD/INR, EUR/INR, GBP/INR
    → Source: public FX API (configurable)
    → Stored in Redis: fx_rate:{currency_pair}
    → All non-INR rate cards converted at daily closing rate
    → FX rate used in route score stored in route_decision record
```

### 7.2 Rate Card Change Detection

```
RATE_CARD_CHANGE_DETECTOR:

  On each rate card fetch:
    compare new_rate_card vs stored_rate_card per destination class

    for each destination_class in rate_card:
      change_pct = abs(new_rate - old_rate) / old_rate × 100

      if change_pct > 0 AND change_pct <= 5:
        log: rate_card.minor_change
        auto-apply, trigger LCR rebuild

      if change_pct > 5 AND change_pct <= 20:
        emit: rate_card.moderate_change_detected
        auto-apply after 1 hour, trigger LCR rebuild
        notify: ops channel

      if change_pct > 20:
        emit: rate_card.significant_change_detected
        DO NOT auto-apply
        notify: ops + billing (requires acknowledgement)
        hold: pending_rate_card_changes table
        apply after: 24h OR ops_admin explicit approval

      if new_rate = 0 or NULL:
        emit: rate_card.invalid_rate_detected
        reject new rate card version
        keep existing rate card active
        alert: ops (data integrity issue)
```

---

## ░░ SECTION 8 — COST ANOMALY DETECTION ENGINE ░░

### 8.1 Anomaly Detection Algorithms

```
ANOMALY CLASS A1 — SPEND SPIKE (per provider):
  Definition:
    Provider's hourly spend is > 3x the 7-day hourly average
    for the same hour-of-day.
  Algorithm:
    baseline = avg(provider_hourly_spend[same_hour, last 7 days])
    current  = provider_hourly_spend[current_hour]
    if current > baseline × 3.0:
      classify: A1
  Severity: HIGH
  Action:   Emit anomaly event. Alert ops + billing.
            Do NOT automatically block — ops decision required.

ANOMALY CLASS A2 — WRONG ROUTE SELECTION (post-hoc audit):
  Definition:
    A route decision used a provider that was NOT the lowest
    CRS-ranked eligible provider at the time of decision,
    without a documented reason (budget override, loop block,
    quality floor miss).
  Algorithm:
    Periodic audit of route_decision_audit_log:
    for each decision:
      if decision.selected_provider != rank_1_provider_at_time:
        if no documented override reason:
          classify: A2
  Severity: MEDIUM
  Action:   Log anomaly. Flag for ops review.
            Investigate route scoring logic.

ANOMALY CLASS A3 — PROVIDER COST CREEP:
  Definition:
    A provider's average cost per minute has increased > 15%
    over the last 30 days despite no rate card change event.
    Likely cause: destination class misclassification or
    NNP table out of date routing more calls to higher-rate class.
  Algorithm:
    rolling_avg_cost[provider, 30d] vs rolling_avg_cost[provider, 60d]
    if 30d_avg > 60d_avg × 1.15:
      classify: A3
  Severity: MEDIUM
  Action:   Trigger NNP table staleness check.
            Trigger destination class audit.

ANOMALY CLASS A4 — HIGH RETRY COST (cross-agent signal):
  Definition:
    PHONE_CALL_LOOP_DETECTION_AGENT reports a PSTN dial loop (L1).
    The cost incurred during the loop is tracked.
    This agent aggregates loop-driven costs and flags if
    loop costs exceed 5% of total daily spend.
  Algorithm:
    loop_cost_pct = daily_loop_cost / daily_total_cost × 100
    if loop_cost_pct > 5:
      classify: A4
  Severity: HIGH
  Action:   Emit anomaly. Joint alert with loop detection agent.
            Investigate root cause (failover misconfiguration).

ANOMALY CLASS A5 — CONTRACT MINIMUM UNDERSPEND:
  Definition:
    Provider has a minimum monthly spend commitment in contract.
    Current pace (daily_spend × days_remaining) will not
    reach the minimum by month end.
    This triggers penalty charges from the provider.
  Algorithm:
    projected_month_spend = current_spend + (daily_avg × days_remaining)
    if projected_month_spend < contract_min_monthly_spend:
      classify: A5
  Severity: MEDIUM
  Action:   Emit advisory. Suggest routing preference boost for provider.
            Alert billing team.

ANOMALY CLASS A6 — COTURN COST OVERRUN:
  Definition:
    TURN relay usage is significantly higher than expected
    ratio to WebRTC direct sessions.
    Expected: < 30% of sessions require TURN relay.
    Anomaly threshold: > 60% of sessions using TURN.
  Algorithm:
    turn_usage_pct = turn_sessions / total_sessions × 100
    if turn_usage_pct > 60:
      classify: A6
  Severity: MEDIUM
  Action:   Alert SIP_HEALTH_MONITOR_AGENT (ICE config issue).
            Alert ops (network topology change suspected).
```

---

## ░░ SECTION 9 — PROVIDER PERFORMANCE TRACKING ░░

### 9.1 Call Completion Rate Tracker

```
CALL_COMPLETION_TRACKER:

  Metrics tracked per provider (rolling windows):

  answer_seizure_ratio (ASR):
    answered_calls / seized_calls × 100
    Target: > 60% for India mobile
    Alert: < 40% → route de-prioritised

  call_completion_rate (CCR):
    completed_calls / attempted_calls × 100
    Threshold STANDARD: > 70%
    Threshold HIGH:     > 85%
    Alert: < 60% → quality penalty applied to route score

  post_dial_delay (PDD):
    time from INVITE to first audio / ringback
    Target: < 3000ms for India
    Alert: > 5000ms → latency penalty applied

  network_effectiveness_ratio (NER):
    (answered + busy + no_answer) / total_attempts × 100
    Measures provider network reachability
    Target: > 90%
    Alert: < 80% → routing weight reduced

  short_call_rate (SCR):
    calls < 3 seconds / total_calls × 100
    Indicator of ghost connections / false answer supervision
    Target: < 5%
    Alert: > 15% → provider investigated for false ASR

  Update cadence: rolling 1-hour, 6-hour, 24-hour averages
  Stored: ClickHouse (time-series)
  Used by: Route Scoring Engine (quality_score component)
```

### 9.2 Provider Auto-Blacklist Rules

```
AUTO-BLACKLIST TRIGGERS (temporary — 1 hour unless ops override):

  TRIGGER BL-1:
    ASR < 20% for > 30 consecutive minutes
    → Auto-blacklist for 1 hour
    → Emit: provider.auto_blacklisted.low_asr
    → Alert ops

  TRIGGER BL-2:
    call_completion_rate < 50% for > 20 consecutive minutes
    → Auto-blacklist for 2 hours
    → Emit: provider.auto_blacklisted.low_completion

  TRIGGER BL-3:
    SIP_HEALTH_MONITOR_AGENT reports provider FAILED for > 5 minutes
    → Auto-blacklist until SIP_HEALTH_MONITOR_AGENT clears FAILED state
    → Emit: provider.auto_blacklisted.health_failed

  TRIGGER BL-4:
    Rate card fetch fails for > 24 hours (stale rate card)
    → De-prioritise (CRS penalty -20), do NOT blacklist
    → Alert ops to investigate rate card API

  TRIGGER BL-5:
    Provider API returns HTTP 429 (rate limit) on route selection
    → Auto-backoff: 15-minute cool-off period
    → Emit: provider.rate_limited.backoff_active

AUTO-UNBLACKLIST:
  After blacklist duration expires:
    → Check SIP_HEALTH_MONITOR_AGENT current state
    → If HEALTHY: remove blacklist, restore to route table
    → If not HEALTHY: extend blacklist by 30 minutes
    → Log: provider.blacklist.extended | provider.blacklist.cleared
```

---

## ░░ SECTION 10 — EVENT BUS CONTRACTS ░░

### 10.1 Events Emitted

```yaml
# AsyncAPI Contracts — SIP_ROUTE_COST_OPTIMIZATION_AGENT

channels:

  route.selected:
    publish:
      message:
        payload:
          type: object
          properties:
            decision_id:             { type: string }
            session_id:              { type: string }
            session_type:            { type: string }
            provider_id:             { type: string }
            provider_name:           { type: string }
            destination_class:       { type: string }
            estimated_cost_per_min:  { type: number }
            quality_floor:           { type: string }
            routing_mode:            { type: string }
            composite_route_score:   { type: number }
            selected_at:             { type: string, format: date-time }

  route.selection.no_eligible_provider:
    publish:
      message:
        payload:
          type: object
          properties:
            session_id:    { type: string }
            reason:        { type: string }
            dest_class:    { type: string }
            blocked_at:    { type: string, format: date-time }

  route.selection.budget_exhausted:
    publish:
      message:
        payload:
          type: object
          properties:
            budget_scope:   { type: string }
            consumed_pct:   { type: number }
            blocked_at:     { type: string, format: date-time }

  budget.alert:
    publish:
      message:
        payload:
          type: object
          properties:
            budget_scope:   { type: string }
            alert_tier:     { type: string }
            consumed_pct:   { type: number }
            consumed_amount: { type: number }
            remaining_amount: { type: number }
            emitted_at:     { type: string, format: date-time }

  rate_card.significant_change_detected:
    publish:
      message:
        payload:
          type: object
          properties:
            provider_id:      { type: string }
            destination_class: { type: string }
            old_rate:         { type: number }
            new_rate:         { type: number }
            change_pct:       { type: number }
            pending_approval_id: { type: string }

  cost.anomaly.detected:
    publish:
      message:
        payload:
          type: object
          properties:
            anomaly_class:   { type: string }
            anomaly_name:    { type: string }
            provider_id:     { type: string }
            severity:        { type: string }
            description:     { type: string }
            detected_at:     { type: string, format: date-time }

  provider.auto_blacklisted:
    publish:
      message:
        payload:
          type: object
          properties:
            provider_id:     { type: string }
            trigger:         { type: string }
            duration_minutes: { type: integer }
            blacklisted_at:  { type: string, format: date-time }

  provider.blacklist.cleared:
    publish:
      message:
        payload:
          type: object
          properties:
            provider_id:    { type: string }
            cleared_at:     { type: string, format: date-time }

  cost.report.daily.ready:
    publish:
      message:
        payload:
          type: object
          properties:
            report_date:    { type: string }
            report_url:     { type: string }
            total_spend:    { type: number }
            top_provider:   { type: string }

  lcr_table.rebuilt:
    publish:
      message:
        payload:
          type: object
          properties:
            dest_class:     { type: string }
            rank_1_provider: { type: string }
            rebuilt_at:     { type: string, format: date-time }
```

---

## ░░ SECTION 11 — DATABASE SCHEMA ░░

### 11.1 Core Tables

```sql
-- Provider Registry
CREATE TABLE sip_provider_registry (
  provider_id            UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  provider_name          TEXT NOT NULL UNIQUE,
  provider_type          TEXT NOT NULL,
  trunk_uri              TEXT,
  api_base_url           TEXT,
  account_sid            TEXT,
  auth_token_vault_path  TEXT NOT NULL,
  contract_start         DATE,
  contract_end           DATE,
  contract_min_monthly   DECIMAL(12,4) DEFAULT 0,
  contract_max_monthly   DECIMAL(12,4),
  is_active              BOOLEAN NOT NULL DEFAULT TRUE,
  is_preferred           BOOLEAN NOT NULL DEFAULT FALSE,
  is_blacklisted         BOOLEAN NOT NULL DEFAULT FALSE,
  blacklist_reason       TEXT,
  blacklist_expires_at   TIMESTAMP,
  created_at             TIMESTAMP NOT NULL DEFAULT NOW(),
  updated_at             TIMESTAMP NOT NULL DEFAULT NOW()
);

-- Provider Rate Cards (versioned)
CREATE TABLE provider_rate_cards (
  rate_card_id           UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  provider_id            UUID NOT NULL REFERENCES sip_provider_registry(provider_id),
  version                TEXT NOT NULL,
  india_mobile_vi        DECIMAL(10,6),
  india_mobile_airtel    DECIMAL(10,6),
  india_mobile_jio       DECIMAL(10,6),
  india_mobile_bsnl      DECIMAL(10,6),
  india_mobile_other     DECIMAL(10,6),
  india_landline         DECIMAL(10,6),
  india_voip             DECIMAL(10,6),
  international_tier1    DECIMAL(10,6),
  international_tier2    DECIMAL(10,6),
  international_tier3    DECIMAL(10,6),
  currency               TEXT NOT NULL DEFAULT 'INR',
  fx_rate_used           DECIMAL(10,4),
  fetched_at             TIMESTAMP NOT NULL DEFAULT NOW(),
  is_active              BOOLEAN NOT NULL DEFAULT TRUE,
  approved_by            UUID,
  approved_at            TIMESTAMP
);

-- Provider Quality Metrics (time-series snapshot)
CREATE TABLE provider_quality_snapshots (
  snapshot_id            UUID DEFAULT gen_random_uuid(),
  provider_id            UUID NOT NULL,
  mos_score_avg          DECIMAL(4,2),
  call_completion_rate   DECIMAL(6,4),
  answer_seizure_ratio   DECIMAL(6,4),
  post_dial_delay_ms     INTEGER,
  network_eff_ratio      DECIMAL(6,4),
  short_call_rate        DECIMAL(6,4),
  jitter_ms_avg          DECIMAL(6,2),
  packet_loss_pct_avg    DECIMAL(6,4),
  measured_at            TIMESTAMP NOT NULL DEFAULT NOW()
);
-- Partition by measured_at monthly

-- Computed Route Scores (current state)
CREATE TABLE provider_route_scores (
  score_id               UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  provider_id            UUID NOT NULL REFERENCES sip_provider_registry(provider_id),
  destination_class      TEXT NOT NULL,
  price_score            DECIMAL(6,2),
  quality_score          DECIMAL(6,2),
  composite_route_score  DECIMAL(6,2),
  rank_for_class         INTEGER,
  computed_at            TIMESTAMP NOT NULL DEFAULT NOW(),
  UNIQUE (provider_id, destination_class)
);

-- Route Decision Audit Log (IMMUTABLE)
CREATE TABLE route_decision_audit_log (
  decision_id            UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  session_id             UUID,
  session_type           TEXT,
  call_purpose           TEXT,
  destination_number     TEXT NOT NULL,   -- masked: last 4 digits only
  destination_class      TEXT NOT NULL,
  provider_id            UUID NOT NULL,
  provider_name          TEXT NOT NULL,
  routing_mode           TEXT NOT NULL,
  quality_floor          TEXT NOT NULL,
  composite_route_score  DECIMAL(6,2),
  estimated_cost_per_min DECIMAL(10,6),
  override_reason        TEXT,
  budget_state_at_time   TEXT,
  lcr_rank_selected      INTEGER,
  decision_latency_ms    INTEGER,
  decided_at             TIMESTAMP NOT NULL DEFAULT NOW(),
  audit_hash             TEXT NOT NULL
);
-- IMMUTABLE: NO UPDATE, NO DELETE via RLS

-- Budget Consumption Log
CREATE TABLE budget_consumption_log (
  log_id                 UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  budget_scope           TEXT NOT NULL,
  budget_period          TEXT NOT NULL,   -- DAILY | MONTHLY
  provider_id            UUID,
  session_type           TEXT,
  consumed_amount        DECIMAL(12,4) NOT NULL,
  consumed_pct           DECIMAL(6,2) NOT NULL,
  alert_tier_reached     TEXT,
  logged_at              TIMESTAMP NOT NULL DEFAULT NOW()
);

-- Cost Anomaly Log
CREATE TABLE cost_anomaly_log (
  anomaly_id             UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  anomaly_class          TEXT NOT NULL,
  anomaly_name           TEXT NOT NULL,
  severity               TEXT NOT NULL,
  provider_id            UUID,
  description            TEXT NOT NULL,
  supporting_data        JSONB NOT NULL,
  resolved               BOOLEAN NOT NULL DEFAULT FALSE,
  resolved_by            UUID,
  resolved_at            TIMESTAMP,
  detected_at            TIMESTAMP NOT NULL DEFAULT NOW()
);

-- Pending Rate Card Changes (awaiting approval)
CREATE TABLE pending_rate_card_changes (
  pending_id             UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  provider_id            UUID NOT NULL,
  destination_class      TEXT NOT NULL,
  old_rate               DECIMAL(10,6),
  new_rate               DECIMAL(10,6),
  change_pct             DECIMAL(6,2),
  status                 TEXT NOT NULL DEFAULT 'PENDING_APPROVAL',
  submitted_at           TIMESTAMP NOT NULL DEFAULT NOW(),
  approved_by            UUID,
  approved_at            TIMESTAMP,
  auto_approve_at        TIMESTAMP
);

-- NNP Prefix Table (India number plan)
CREATE TABLE india_nnp_prefix_table (
  prefix                 TEXT PRIMARY KEY,
  operator               TEXT NOT NULL,
  circle                 TEXT NOT NULL,
  destination_class      TEXT NOT NULL,
  updated_at             TIMESTAMP NOT NULL DEFAULT NOW()
);
```

---

## ░░ SECTION 12 — API CONTRACTS ░░

### 12.1 Internal REST API

```yaml
openapi: 3.1.0
info:
  title: SIP_ROUTE_COST_OPTIMIZATION_AGENT Internal API
  version: 1.0.0

paths:

  /route/select:
    post:
      summary: Select optimal route for an outbound call
      description: |
        Primary API. Called by CALL_FAILOVER_AGENT and Voice GD
        Orchestrator before every outbound PSTN/SIP call.
        SLA: response within 50ms.
      security: [bearerAuth: []]
      requestBody:
        content:
          application/json:
            schema:
              type: object
              required: [destination_number, session_type, call_purpose]
              properties:
                destination_number: { type: string }
                session_id:         { type: string }
                session_type:       { type: string }
                call_purpose:       { type: string }
                quality_floor:      { type: string }
      responses:
        "200":
          content:
            application/json:
              schema:
                type: object
                properties:
                  decision_id:            { type: string }
                  provider_id:            { type: string }
                  provider_name:          { type: string }
                  trunk_uri:              { type: string }
                  destination_class:      { type: string }
                  estimated_cost_per_min: { type: number }
                  routing_mode:           { type: string }
                  composite_route_score:  { type: number }
        "402":
          description: Budget exhausted — no routes available
        "503":
          description: No eligible provider found

  /route/providers:
    get:
      summary: Current ranked provider list for a destination class
      parameters:
        - name: dest_class
          in: query
          schema: { type: string }
      responses:
        "200": { description: Ranked provider list with scores }

  /route/providers/{provider_id}:
    get:
      summary: Full provider record with quality and score data
      responses:
        "200": { description: Provider details }

  /route/providers/{provider_id}/blacklist:
    post:
      summary: Manually blacklist a provider
      security: [bearerAuth: []]
      requestBody:
        content:
          application/json:
            schema:
              type: object
              properties:
                reason:           { type: string }
                duration_minutes: { type: integer }
      responses:
        "200": { description: Provider blacklisted }

  /route/lcr-table/{dest_class}:
    get:
      summary: Current LCR table for a destination class
      responses:
        "200": { description: LCR ranked table }

  /route/budget/status:
    get:
      summary: Current budget consumption across all scopes
      responses:
        "200":
          content:
            application/json:
              schema:
                type: object
                properties:
                  global_monthly:    { type: object }
                  per_provider:      { type: array }
                  per_session_type:  { type: array }
                  alert_tier:        { type: string }

  /route/budget/reset:
    post:
      summary: Reset budget block (ops_admin only — emergency)
      security: [bearerAuth: []]
      responses:
        "200": { description: Budget block cleared }
        "403": { description: Insufficient privilege }

  /route/anomalies:
    get:
      summary: Active and recent cost anomalies
      parameters:
        - name: status
          in: query
          schema: { type: string, default: unresolved }
      responses:
        "200": { description: Anomaly list }

  /route/rate-cards/pending:
    get:
      summary: Rate card changes pending approval
      responses:
        "200": { description: Pending changes list }

  /route/rate-cards/{pending_id}/approve:
    post:
      summary: Approve a pending rate card change
      security: [bearerAuth: []]
      responses:
        "200": { description: Rate card change approved and applied }

  /route/reports/daily/{date}:
    get:
      summary: Daily cost intelligence report
      responses:
        "200": { description: Daily report JSON or redirect to MinIO URL }

  /route/decision-audit/{session_id}:
    get:
      summary: All route decisions for a session (immutable log)
      responses:
        "200": { description: Decision audit records }
```

---

## ░░ SECTION 13 — OBSERVABILITY REQUIREMENTS ░░

### 13.1 Prometheus Metrics

```
# Route selection performance
route_selection_latency_ms
  Histogram — p50, p90, p99 selection time
  SLA breach alert if p99 > 50ms

route_selection_total{provider_id, dest_class, routing_mode}
  Counter — routes selected per provider per destination

route_selection_blocked_total{reason}
  Counter — selections blocked (no_eligible, budget_exhausted)

# Provider scores
provider_composite_route_score{provider_id}
  Gauge — current CRS per provider

provider_quality_score{provider_id}
  Gauge — current quality score

provider_call_completion_rate{provider_id}
  Gauge — rolling completion rate

provider_blacklisted{provider_id}
  Gauge — 1 = blacklisted, 0 = active

# Cost metrics
voice_infra_spend_daily_total{provider_id, session_type}
  Gauge — current day spend accumulation

voice_infra_budget_consumed_pct{budget_scope}
  Gauge — % of budget consumed

voice_infra_estimated_cost_per_session{session_type}
  Histogram — cost distribution per session type

# LCR table freshness
lcr_table_age_seconds{dest_class}
  Gauge — seconds since last LCR rebuild
  Alert if > 120s

# Rate card freshness
rate_card_age_hours{provider_id}
  Gauge — hours since last rate card fetch
  Alert if > 24h

# Anomaly counts
cost_anomaly_active_count{anomaly_class}
  Gauge — active unresolved anomalies

# Contract surveillance
provider_contract_days_remaining{provider_id}
  Gauge — days until contract expiry
  Alert if < 30

# Route decision audit completeness
route_decision_audit_missing_total
  Counter — decisions without audit record (must be 0)
```

### 13.2 Grafana Dashboards

```
Dashboard: SIP_ROUTE_COST_OPTIMIZATION_AGENT — Cost Intelligence

Panel 1:  Daily Spend by Provider — stacked bar chart
Panel 2:  Budget Consumption % (all scopes) — gauge grid
Panel 3:  Route Selection Latency p50/p99 — time series
Panel 4:  Provider CRS Rankings — live ranked table
Panel 5:  Provider Call Completion Rates — bar chart
Panel 6:  LCR Table: Current Top Route per Destination — table
Panel 7:  Cost Anomalies Active — count + list
Panel 8:  Provider Blacklist Status — status grid
Panel 9:  Rate Card Freshness — staleness table
Panel 10: Contract Expiry Countdown — sorted table
Panel 11: Routing Mode Distribution (LCR vs QGR) — pie chart
Panel 12: Cost per Session Type — histogram
Panel 13: Budget Alert Tier — current state indicator
Panel 14: Pending Rate Card Changes — approval queue
```

### 13.3 Alerts

```
ALERT: RouteSelectionSlaBreached
  condition: route_selection_latency_ms p99 > 50
  severity: warning
  notify: ops

ALERT: BudgetAlert75Pct
  condition: voice_infra_budget_consumed_pct > 75
  severity: warning
  notify: ops + billing

ALERT: BudgetAlert90Pct
  condition: voice_infra_budget_consumed_pct > 90
  severity: critical
  notify: on-call + billing + ops

ALERT: BudgetExhausted
  condition: voice_infra_budget_consumed_pct >= 100
  severity: CRITICAL
  notify: on-call + PagerDuty + billing + Admin Governance

ALERT: AllProvidersBlacklisted
  condition: count(provider_blacklisted == 0) == 0
  severity: CRITICAL
  notify: on-call + PagerDuty (no route available)

ALERT: CostAnomalySpendSpike
  condition: cost_anomaly_active_count{anomaly_class="A1"} > 0
  severity: high
  notify: billing + ops

ALERT: RateCardStale
  condition: rate_card_age_hours > 24
  severity: warning
  notify: ops channel

ALERT: ProviderContractExpiringSoon
  condition: provider_contract_days_remaining < 30
  severity: warning
  notify: billing + ops (daily)

ALERT: LcrTableStale
  condition: lcr_table_age_seconds{dest_class} > 120
  severity: warning
  notify: ops (agent may be unhealthy)

ALERT: RouteAuditMissing
  condition: route_decision_audit_missing_total > 0
  severity: critical
  notify: on-call + Admin Governance (compliance breach)
```

---

## ░░ SECTION 14 — COST INTELLIGENCE REPORTS ░░

### 14.1 Daily Cost Intelligence Report (via Apache Airflow)

```
DAILY REPORT CONTENTS:

  1. EXECUTIVE SUMMARY
     Total voice infra spend today: ₹ X
     vs yesterday: ₹ Y (Δ Z%)
     vs 7-day avg: ₹ A (Δ B%)
     Budget consumed: C%
     Projected month-end spend: ₹ D

  2. PROVIDER PERFORMANCE LEAGUE TABLE
     Rank | Provider | Calls | Duration | Spend | ASR | MOS | CRS
     Sorted by composite_route_score descending

  3. DESTINATION CLASS BREAKDOWN
     Cost per destination class (India mobile, landline, international)
     Volume per destination class
     Average cost-per-minute per class

  4. ROUTING MODE ANALYSIS
     % sessions using LCR mode vs QGR mode
     Cost delta: if all sessions had used lowest-cost route

  5. ANOMALIES DETECTED TODAY
     List of A1–A6 anomalies with description

  6. TOP 10 MOST EXPENSIVE SESSIONS
     session_id | session_type | provider | duration | cost
     (session_id masked in external reports)

  7. PROVIDER BLACKLIST EVENTS
     Providers auto-blacklisted today + duration + reason

  8. RATE CARD CHANGES
     Any rate card changes applied or pending today

  9. BUDGET FORECAST
     Days remaining in billing period: N
     Average daily spend: ₹ X
     Projected month-end spend: ₹ Y
     Projected vs budget: ±%

  10. OPTIMIZATION OPPORTUNITIES
     Identified route switches that would reduce spend by > 10%
     Providers with degraded performance dragging up retry costs

Delivered to: MinIO (PDF + JSON) + Ops email + Billing Service
Generated by: Apache Airflow DAG: daily_cost_intelligence_report
Scheduled: 00:30 IST daily
```

---

## ░░ SECTION 15 — SECURITY CONTROLS ░░

```
CONTROL SC-1: PROVIDER CREDENTIAL ISOLATION
  All provider API keys and SIP credentials stored
  in HashiCorp Vault only.
  Retrieved per-use via Vault dynamic secrets where supported.
  This agent holds Vault policy: sip-route-readonly.
  It may READ rate cards and call provider APIs.
  It may NOT modify provider account settings.

CONTROL SC-2: ROUTE DECISION AUDIT IMMUTABILITY
  route_decision_audit_log:
    → NO UPDATE via RLS
    → NO DELETE (including admin)
    → Append-only write role
    → SHA256 integrity hash per row
    → Missing audit rows trigger: route_decision_audit_missing_total++
    → Wazuh SIEM alert on any detected tamper

CONTROL SC-3: BUDGET RESET ACCESS CONTROL
  /route/budget/reset:
    → Accessible only to ops_admin role
    → JWT + MFA required
    → Every reset logged to immutable budget_events table
    → Admin Governance Service notified on reset

CONTROL SC-4: RATE CARD APPROVAL CONTROL
  Rate card changes > 20%:
    → Cannot be applied without ops_admin approval
    → Approval logged with approver identity
    → Auto-approve timer: 24h (logged as auto-approved)
    → Emergency manual apply requires two-person approval

CONTROL SC-5: DESTINATION NUMBER PRIVACY
  Phone numbers in route_decision_audit_log stored as:
    → Last 4 digits only: xxxxxxxx{4321}
    → Full number stored only in HashiCorp Vault
      (keyed to call_sid, retrievable for disputes only)

CONTROL SC-6: ANOMALOUS SPEND WAZUH REPORTING
  Wazuh events triggered for:
    → A1 (spend spike > 3x baseline)
    → Budget reaching CRITICAL tier (90%)
    → Budget EXHAUSTED
    → Provider blacklist cascade (> 2 providers in 10 minutes)
    → Any route decision without corresponding audit record
```

---

## ░░ SECTION 16 — INTERN EXECUTION GUIDE ░░

### 16.1 Local Setup

```bash
# Step 1: Navigate to service
cd /backend/services/sip_route_cost_optimization_agent/

# Step 2: Install dependencies
pip install -r requirements.txt --break-system-packages

# Step 3: Environment setup
cp .env.example .env

# Step 4: Required environment variables
REDIS_URL=redis://localhost:6379
POSTGRES_URL=postgresql://postgres:postgres@localhost:5432/ecoskiller
CLICKHOUSE_URL=http://localhost:8123
VAULT_ADDR=http://localhost:8200
VAULT_TOKEN=<dev-token>
PROMETHEUS_PORT=9105
MINIO_URL=http://localhost:9000
MINIO_ACCESS_KEY=minioadmin
MINIO_SECRET_KEY=minioadmin

# Budget config (test values)
GLOBAL_MONTHLY_BUDGET_INR=50000
PSTN_BUDGET_CAP_PER_CALL_SECONDS=300
BUDGET_ALERT_50_PCT=true
BUDGET_ALERT_75_PCT=true
BUDGET_ALERT_90_PCT=true

# Provider config (dev — mock providers)
DEFAULT_PROVIDER_COUNT=2
MOCK_PROVIDER_01_NAME=MockTrunk-Alpha
MOCK_PROVIDER_01_RATE_INDIA_MOBILE=0.20
MOCK_PROVIDER_02_NAME=MockTrunk-Beta
MOCK_PROVIDER_02_RATE_INDIA_MOBILE=0.18

# Route selection config
ROUTE_SELECTION_SLA_MS=50
WEIGHT_PRICE=0.45
WEIGHT_QUALITY=0.45
WEIGHT_PREFER=0.10

# Step 5: Run database migrations
alembic upgrade head

# Step 6: Seed NNP prefix table (India operator prefixes)
python scripts/seed_nnp_table.py

# Step 7: Start service
uvicorn main:app --reload --port 8023

# Step 8: Verify
curl http://localhost:8023/route/providers
# Expected: [{provider_id, provider_name, composite_route_score, ...}]
```

### 16.2 Testing Route Selection

```bash
# Test route selection for India mobile (Jio)
curl -X POST http://localhost:8023/route/select \
  -H "Authorization: Bearer <test_token>" \
  -H "Content-Type: application/json" \
  -d '{
    "destination_number": "+919867543210",
    "session_id": "test-gd-001",
    "session_type": "GD",
    "call_purpose": "FAILOVER",
    "quality_floor": "HIGH"
  }'

# Expected response:
# {
#   "decision_id": "uuid",
#   "provider_name": "MockTrunk-Beta",
#   "destination_class": "india_mobile_jio",
#   "estimated_cost_per_min": 0.18,
#   "routing_mode": "QUALITY_GATED",
#   "composite_route_score": 87.4
# }

# Check LCR table for India Jio
curl "http://localhost:8023/route/lcr-table/india_mobile_jio"

# Check budget status
curl http://localhost:8023/route/budget/status

# View route decision audit for test session
curl http://localhost:8023/route/decision-audit/test-gd-001

# Check anomalies
curl "http://localhost:8023/route/anomalies?status=unresolved"

# Check pending rate card changes
curl http://localhost:8023/route/rate-cards/pending
```

### 16.3 Expected Success Outputs

```
Service up:                /route/providers → array of providers
Route selected in time:    decision_latency_ms < 50
Audit record written:      route_decision_audit_log has 1 row after test call
Budget status visible:     /route/budget/status → alert_tier = HEALTHY
LCR table populated:       /route/lcr-table/india_mobile_jio → ranked list
Prometheus metrics:        localhost:9105/metrics → route_selection_* present
```

---

## ░░ SECTION 17 — ENFORCEMENT RULES ░░

```
RULE E1:
  Every outbound PSTN or SIP call MUST be routed through
  SIP_ROUTE_COST_OPTIMIZATION_AGENT.
  No call may be placed using a hardcoded provider.
  No call may bypass route selection.
  Violation → audit gap detected → Wazuh SIEM alert.

RULE E2:
  Route selection response MUST be returned within 50ms.
  SLA breach → Prometheus alert.
  If agent is down → CALL_FAILOVER_AGENT uses LAST_KNOWN_ROUTE
  from Redis cache (TTL: 300s emergency fallback).

RULE E3:
  Every route decision MUST produce an immutable audit record.
  Missing audit record → route_decision_audit_missing_total++
  → CRITICAL alert → Admin Governance notified.

RULE E4:
  No route may be selected for a provider that is:
    → blacklisted (auto or manual)
    → health state = FAILED (per SIP_HEALTH_MONITOR_AGENT)
    → budget exhausted
    → loop-blocked (per PHONE_CALL_LOOP_DETECTION_AGENT)
  Any selection attempt against these states
  → logged as RULE_VIOLATION in audit log.

RULE E5:
  Budget EXHAUSTED state may only be cleared by:
    → ops_admin explicit /route/budget/reset
    → Automated monthly billing period reset (via Airflow)
  No other mechanism may clear EXHAUSTED state.

RULE E6:
  Rate card changes > 20% require ops_admin approval.
  Auto-approval timer: 24 hours.
  No scoring or routing may use a pending-approval rate card.
  Until approved, previous rate card version remains active.

RULE E7:
  This agent has ZERO SCORING AUTHORITY.
  It governs costs only.
  It may not affect session assessment outcomes.

RULE E8:
  The agent must expose /route/health for Kubernetes
  readiness probe.
  Readiness = all of:
    → LCR tables populated for all destination classes
    → At least 1 eligible provider per destination class
    → PostgreSQL writable
    → Redis accessible
  If not ready → pod marked not-ready → CALL_FAILOVER_AGENT
  uses emergency fallback cache.
```

---

## ░░ SECTION 18 — COMPLETE AGENT QUARTET ░░

```
The four PSTN & Bridge agents now form the complete
Ecoskiller Antigravity Voice Infrastructure System:

┌──────────────────────────────────────────────────────────────────┐
│            ECOSKILLER ANTIGRAVITY VOICE AGENT QUARTET            │
│                                                                  │
│  SIP_HEALTH_MONITOR_AGENT          "I watch and predict."        │
│  → Continuous health surveillance of every voice component       │
│  → Signals degradation before it becomes failure                 │
│                           │                                      │
│                           ▼                                      │
│  CALL_FAILOVER_AGENT               "I catch and reroute."        │
│  → Executes failover path on failure or health signal            │
│  → Asks ROUTE_COST_AGENT for cheapest eligible path              │
│                           │                                      │
│                           ▼                                      │
│  PHONE_CALL_LOOP_DETECTION_AGENT   "I terminate and protect."    │
│  → Detects loops, ghosts, zombies, billing leaks                 │
│  → Hard-kills pathological states                                │
│                           │                                      │
│                           ▼                                      │
│  SIP_ROUTE_COST_OPTIMIZATION_AGENT "I optimise and govern."      │
│  → Selects optimal route for every call                          │
│  → Guards budgets, scores providers, detects cost anomalies      │
│  → Produces cost intelligence for billing governance             │
│                                                                  │
│  All four must be deployed together.                             │
│  Together they deliver:                                          │
│    → Zero silent failures                                        │
│    → Zero surprise bills                                         │
│    → Zero loops or ghost sessions                                │
│    → Zero overpayment for voice infrastructure                   │
│    = ANTIGRAVITY VOICE INFRASTRUCTURE                            │
└──────────────────────────────────────────────────────────────────┘
```

---

## ░░ SECTION 19 — ANTIGRAVITY SEAL ░░

```
╔══════════════════════════════════════════════════════════════════════╗
║  SIP_ROUTE_COST_OPTIMIZATION_AGENT — ANTIGRAVITY PRODUCTION SEAL    ║
╠══════════════════════════════════════════════════════════════════════╣
║  Agent:               SIP_ROUTE_COST_OPTIMIZATION_AGENT              ║
║  Version:             v1.0-ANTIGRAVITY                               ║
║  System:              ECOSKILLER UNIFIED SAAS                        ║
║  Route Selection SLA: 50ms MANDATORY                                 ║
║  Routing Modes:       LCR (cost-first) · QGR (quality-gated)        ║
║  Destination Classes: India Mobile (4 operators) + Landline +        ║
║                       VoIP + International Tier 1/2/3               ║
║  Budget Tiers:        HEALTHY·ADVISORY·WARNING·HIGH·CRITICAL·        ║
║                       EXHAUSTED                                      ║
║  Anomaly Classes:     A1 through A6                                  ║
║  Audit Trail:         IMMUTABLE — SHA256 hashed, append-only         ║
║  Phone Number Privacy: Masked — last 4 digits only in logs           ║
║  Rate Card Changes:   >20% requires ops_admin approval               ║
║  Scoring Authority:   NONE — cost governance only                    ║
║  Companion Agents:                                                   ║
║    CALL_FAILOVER_AGENT              v1.0-ANTIGRAVITY (required)     ║
║    SIP_HEALTH_MONITOR_AGENT         v1.0-ANTIGRAVITY (required)     ║
║    PHONE_CALL_LOOP_DETECTION_AGENT  v1.0-ANTIGRAVITY (required)     ║
║  Mutation Policy:     ADD-ONLY via version bump                      ║
║  Interpretation:      NONE PERMITTED                                 ║
╠══════════════════════════════════════════════════════════════════════╣
║  No PSTN or SIP call may be placed without this agent.              ║
║  Absence → BLOCK all outbound voice infrastructure calls            ║
║  → REPORT: ROUTE_COST_AGENT_ABSENT                                  ║
║  → NO VOICE SESSION COMPLETION CLAIM PERMITTED                      ║
║                                                                      ║
║  Budget EXHAUSTED without ops_admin reset                            ║
║  → FULL VOICE PSTN/SIP LOCKDOWN ACTIVE                              ║
║  → WebRTC-only sessions permitted                                   ║
║                                                                      ║
║  Deployed without all three companion agents                         ║
║  → INCOMPLETE ANTIGRAVITY QUARTET                                   ║
║  → NO VOICE INFRASTRUCTURE COMPLETION CLAIM PERMITTED               ║
╚══════════════════════════════════════════════════════════════════════╝
```

---

## ░░ DOCUMENT LOCK ░░

```
Document Status:    SEALED
File:               SIP_ROUTE_COST_OPTIMIZATION_AGENT__PSTN_BRIDGE__ECOSKILLER.md
Mutation Policy:    Add-Only — No line may be removed or altered
Version:            v1.0-ANTIGRAVITY
Authority:          Human declaration only
AI Generation Date: 2026-03-04
Parent System:      ECOSKILLER MASTER EXECUTION PROMPT v12.0+
Parent Reference:   Section H → Service #7 (Voice GD Orchestrator)
                    Section H → Service #13 (Billing & Subscription)
                    Section H → Service #14 (Admin Governance)
                    Section VIII → OBSERVABILITY (NON-OPTIONAL)
                    Section IX → SECURITY BASELINE
                    R28 → Intelligence Cost Optimization Law
Companion Agents (all required, must co-deploy):
  CALL_FAILOVER_AGENT              v1.0-ANTIGRAVITY
  SIP_HEALTH_MONITOR_AGENT         v1.0-ANTIGRAVITY
  PHONE_CALL_LOOP_DETECTION_AGENT  v1.0-ANTIGRAVITY
Governed By:        R10·R13·R14·R16·R21·R28·R60·Section VIII·Section IX
```

---

*END OF SIP_ROUTE_COST_OPTIMIZATION_AGENT SEALED PROMPT*
