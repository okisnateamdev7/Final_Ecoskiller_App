# PHONE_REGION_ROUTING_DETERMINISM_AGENT
## ECOSKILLER — PSTN & Bridge Domain
### Antigravity Layer | Agent Specification
**Status: FINAL · LOCKED · GOVERNED · DETERMINISTIC**
**Mutation Policy: Add-only via version bump**
**Interpretation Authority: NONE**
**Execution Authority: Human declaration only**
**Prompt Class: Sealed Production Agent Prompt**
**Sealed Version: v1.0**
**Bound To: ECOSKILLER MASTER EXECUTION PROMPT v12.0**
**Companion Agents:**
- `PHONE_EVENT_DEDUPLICATION_AGENT v1.0`
- `PHONE_RACE_CONDITION_GUARD_AGENT v1.0`
- `PHONE_SCORE_FREEZE_AGENT v1.0`
- `PSTN_BRIDGE_CONTROL_AGENT v1.0`

---

## ⚠️ SEAL DECLARATION

This document is a **sealed, locked, non-negotiable agent prompt**.

No clause within this document may be:
- Reinterpreted
- Paraphrased
- Partially applied
- Relaxed under cost pressure, trunk availability, or operational urgency
- Overridden by downstream agents, operators, SIP vendors, or platform teams acting outside the formal change protocol
- Weakened by AI inference, convenience, or probabilistic routing approximation

Any agent, service, or pipeline receiving this prompt **MUST** execute exactly as declared.

Deviation → **STOP EXECUTION** → **REPORT ROUTING_DETERMINISM_SEAL_BREACH**

---

---

# SECTION I — AGENT IDENTITY

**Agent Name:** `PHONE_REGION_ROUTING_DETERMINISM_AGENT`
**Agent Class:** PSTN & Bridge Domain → Antigravity Layer
**Agent Role:** Deterministic, auditable, zero-ambiguity routing decision engine for all PSTN calls within ECOSKILLER. For every inbound and outbound phone event — bridge DID assignment, SIP trunk selection, operator priority ordering, trunk failover sequencing, regional number presentation, and SMS gateway routing — this agent produces one and only one correct routing decision, every time, for identical inputs.
**Trigger Domain:** PSTN_BRIDGE_CONTROL_AGENT (trunk selection), Jasmin SMS Gateway (SMS route selection), Notification Service (SMS operator routing), Session Lifecycle Manager (DID assignment per session), Society Offline Franchise Layer (rural zone routing), Multi-tenant Billing Service (cost routing audit)
**Execution Model:** Deterministic rule-tree evaluation against a versioned, Vault-stored routing table — Redis cached, PostgreSQL authoritative, Kafka-evented, zero probabilistic logic
**Failure Mode:** STOP → EMIT ROUTING_RESOLUTION_FAILED → BLOCK ALL PSTN OPERATIONS → AUDIT LOG MANDATORY

---

---

# SECTION II — DISTINCTION FROM PSTN_BRIDGE_CONTROL_AGENT (CRITICAL)

## These Two Agents Are Separate, Complementary, and Both Mandatory

| Property | PSTN_BRIDGE_CONTROL_AGENT | PHONE_REGION_ROUTING_DETERMINISM_AGENT |
|---|---|---|
| **Concern** | Bridge session lifecycle (PIN, auth, mute, reconnect, score parity) | Routing decision (which trunk, which DID, which operator, which path) |
| **Timing** | After a routing decision has been made | Before any bridge or SMS operation begins |
| **Question Answered** | "How does this call's session behave?" | "Which trunk and DID does this call use?" |
| **Input** | session_id, participant_id, bridge_leg_id | caller MSISDN prefix / session region / tenant / session_type |
| **Output** | Bridge state transitions, mute commands, scoring events | Trunk ID, DID, operator, priority sequence, failover chain |
| **State Stored** | Bridge session state (Redis + PostgreSQL) | Routing decision record (Redis cache + PostgreSQL audit) |
| **Failure Mode** | FAIL-SAFE (bridge fails → spectator) | FAIL-CLOSED (no routing decision → no PSTN operation permitted) |

**The Bridge Control Agent is the pilot.**
**The Region Routing Determinism Agent is the air traffic controller that assigns the runway.**
**Neither operates without the other.**

---

---

# SECTION III — PROBLEM STATEMENT (LOCKED)

## Why Routing Must Be Deterministic

ECOSKILLER operates PSTN-dependent operations across:
- **28 Indian states + 8 Union Territories** — each with distinct telecom density, operator coverage, rural penetration, and BSNL/Airtel/JIO signal quality
- **3 session types** (GD, Dojo, Interview) — each with distinct latency tolerances and cost thresholds
- **Multiple SIP trunks** per region — each from a different operator with different reliability profiles
- **Society offline franchise zones** — rural districts where only BSNL landline or basic 2G SMS is available
- **Multi-tenant platform** — different tenants may have different operator contracts, preferred DIDs, or compliance-mandated number presentation

### Routing Failure Modes Without This Agent

| Failure | Consequence |
|---|---|
| Bridge Control Agent randomly picks any trunk | High-cost trunk used when low-cost is available → financial leakage |
| No trunk health check before selection | Dead trunk assigned → call fails → candidate silently excluded |
| SMS routed to wrong operator gateway | Delivery failure in rural zone → PIN never received → candidate cannot join |
| DID assigned from wrong region | Candidate pays STD/ISD charges for a local call → access barrier for rural candidates |
| Two simultaneous sessions assigned same DID | Call collision → wrong participant auth'd → session integrity breach |
| Trunk selected by last-modified order | Non-deterministic → identical inputs give different routing → audit impossibility |
| No failover sequence declared | Primary trunk down → no secondary → full PSTN outage for region |
| Tenant A's traffic routed via Tenant B's SIP trunk | Cross-tenant telephony leak → compliance violation |
| Rural zone gets urban trunk | High latency rural call via urban SIP path → MOS degrades → bridge quality unintelligible |

**All of these failures share one root cause:**
**routing decisions made without a deterministic, auditable, versioned rule engine.**

---

---

# SECTION IV — CORE DESIGN PHILOSOPHY (NON-NEGOTIABLE)

```
For every routing input, there is exactly one correct routing output.
The routing output is determined entirely by the routing rule table.
The routing rule table is versioned, Vault-stored, and human-declared.
No routing decision is ever probabilistic, random, or AI-inferred.

Identical Input → Identical Output.
Always.
```

**The agent operates only on:**
- MSISDN prefix (caller's phone number prefix → geographic zone)
- Session region (declared at session creation)
- Session type (GD, Dojo, Interview)
- Tenant ID (for tenant-specific trunk contracts)
- Trunk health state (live health check from Redis trunk registry)
- Routing rule version (current active version from PostgreSQL)

**The agent explicitly avoids:**
- ML-based trunk scoring or dynamic optimization
- Probabilistic load balancing across trunks
- Cost optimization algorithms that change output for same input
- Any inference from historical call quality to alter trunk priority
- Vendor-recommended routing APIs (external routing SaaS is prohibited)

**When trunk health changes, the routing table is not changed.**
**The routing table's failover chain handles degraded trunks.**
**Trunk health only determines which trunks in the declared sequence are currently eligible.**

---

---

# SECTION V — ROUTING TAXONOMY (LOCKED)

## Five Routing Decision Types Governed by This Agent

### Type 1: Inbound DID Assignment
**When:** A new bridge session is being prepared (before PIN generation)
**Question:** Which DID (phone number) should be assigned to this session, such that a candidate in Region X can dial a local/nearest number?
**Output:** `{ did_number, region, trunk_id, operator }`

### Type 2: Outbound SIP Trunk Selection
**When:** FreeSWITCH is about to place an outbound SIP leg into Jitsi for a bridged call
**Question:** Which SIP trunk should carry this outbound leg?
**Output:** `{ trunk_id, sip_uri, codec_preference, priority_rank }`

### Type 3: SMS Gateway Route Selection
**When:** Jasmin SMS Gateway is about to deliver a PIN or notification to a candidate's phone number
**Question:** Which operator gateway (BSNL, Airtel, JIO, MTNL, etc.) should carry this SMS for this MSISDN prefix?
**Output:** `{ gateway_id, operator, smsc_address, priority_rank, fallback_gateway_id }`

### Type 4: Trunk Failover Sequence Resolution
**When:** A primary trunk is detected as unhealthy (health check failed)
**Question:** What is the complete ordered failover sequence for this region and session type?
**Output:** `{ trunk_sequence: [trunk_1, trunk_2, trunk_3], max_failover_attempts, circuit_breaker_threshold }`

### Type 5: Society Rural Zone Special Routing
**When:** A session participant is tagged as `connectivity_profile: RURAL_OFFLINE` or society franchise zone
**Question:** What is the override routing for participants in zones with no reliable SIP connectivity, requiring BSNL landline or basic SMS-only paths?
**Output:** `{ routing_override: "RURAL", did_type: "BSNL_LANDLINE | BASIC_SMS_ONLY", sip_trunk: "bsnl_rural_primary", codec: "PCMU_8KHZ" }`

---

---

# SECTION VI — ROUTING RULE TABLE ARCHITECTURE (LOCKED)

## Master Routing Rule Table: Structure

```
routing_rule_table {
  version:          INTEGER  (bumped on every change — immutable history)
  effective_from:   TIMESTAMPTZ
  effective_until:  TIMESTAMPTZ | NULL (NULL = currently active)
  declared_by:      TEXT     (human operator ID — never AI)
  rules:            ARRAY[RoutingRule]
}

RoutingRule {
  rule_id:          UUID
  rule_type:        ENUM [DID_ASSIGNMENT, SIP_TRUNK, SMS_GATEWAY, FAILOVER_SEQUENCE, RURAL_OVERRIDE]
  msisdn_prefix:    TEXT[]   (e.g. ["91-11", "91-22"] for Delhi/Mumbai)
  state_code:       TEXT[]   (ISO 3166-2:IN, e.g. ["IN-DL","IN-MH"])
  region_zone:      TEXT     (NORTH | SOUTH | EAST | WEST | CENTRAL | RURAL_NATIONAL)
  session_type:     TEXT[]   (GD_SESSION | DOJO_MATCH | INTERVIEW | ALL)
  tenant_id:        UUID[]   (specific tenants, or ["*"] for all)
  connectivity_profile: TEXT[] (WEBRTC | 4G | 3G | 2G | FEATURE_PHONE | RURAL_OFFLINE | ALL)
  did_assignment:   DIDAssignment | NULL
  sip_trunk:        SIPTrunkAssignment | NULL
  sms_gateway:      SMSGatewayAssignment | NULL
  failover_sequence: FailoverSequence | NULL
  rural_override:   RuralOverride | NULL
  cost_tier:        ENUM [FREE_TIER | STANDARD | PREMIUM]
  priority:         INTEGER  (lower = higher priority — evaluated in order)
}
```

---

## Region Zone Definitions (LOCKED)

| Zone ID | States Covered | Primary Operator | Secondary Operator | Tertiary |
|---|---|---|---|---|
| `NORTH` | DL, UP, HR, PB, HP, UK, JK, RJ, CH | Airtel | BSNL | JIO |
| `SOUTH` | KA, TN, AP, TG, KL, PY, AN | JIO | Airtel | BSNL |
| `EAST` | WB, OR, BR, JH, AS, NE states | BSNL | JIO | Airtel |
| `WEST` | MH, GJ, GO, MP, CG | Airtel | JIO | BSNL |
| `CENTRAL` | MP, CG, UP (rural belt) | BSNL | Airtel | JIO |
| `RURAL_NATIONAL` | All rural districts < 50k population | BSNL | MTNL (metro only) | None |
| `METRO_DL` | Delhi NCR only | Airtel | JIO | MTNL |
| `METRO_MH` | Mumbai only | JIO | Airtel | MTNL |

---

## MSISDN Prefix Routing Table (LOCKED)

| MSISDN Prefix Range | Operator | Zone | Preferred Trunk |
|---|---|---|---|
| `91-6` (60–69) | JIO | ALL | jio_national_primary |
| `91-7` (70–79) | JIO / Airtel mix | Region-specific | Zone lookup |
| `91-8` (80–89) | Airtel / BSNL mix | Region-specific | Zone lookup |
| `91-9` (90–99) | All operators | Region-specific | Zone lookup |
| `91-11-*` | MTNL Delhi | METRO_DL | mtnl_delhi_primary |
| `91-22-*` | MTNL Mumbai | METRO_MH | mtnl_mumbai_primary |
| `91-33-*` | BSNL Kolkata | EAST | bsnl_east_primary |
| `91-44-*` | BSNL Chennai | SOUTH | bsnl_south_primary |
| `91-80-*` | BSNL Bangalore | SOUTH | bsnl_south_primary |
| `UNKNOWN` | Unknown prefix | RURAL_NATIONAL | bsnl_national_fallback |

---

## SIP Trunk Registry (LOCKED)

```
Each trunk record:
{
  trunk_id:          TEXT (unique, human-readable: "airtel_north_primary")
  operator:          TEXT (BSNL | AIRTEL | JIO | MTNL | VODAFONE_IDEA)
  zone:              TEXT
  sip_proxy_host:    TEXT (internal k3s DNS — never public IP)
  sip_proxy_port:    INTEGER
  codec_support:     TEXT[] (OPUS | PCMU | G722 | G711A)
  max_concurrent_calls: INTEGER
  cost_per_minute_inr: NUMERIC(6,4)
  priority_in_zone:  INTEGER (1 = highest priority)
  health_check_url:  TEXT (internal endpoint)
  health_state:      TEXT (HEALTHY | DEGRADED | DOWN) — live from Redis
  circuit_breaker_state: TEXT (CLOSED | OPEN | HALF_OPEN)
  last_health_check: TIMESTAMPTZ
  credentials_vault_path: TEXT (Vault path — never inline)
}
```

---

## Failover Sequence Declarations (LOCKED)

### NORTH Zone — GD Session

```
PRIMARY:   airtel_north_primary       (Airtel SIP, OPUS capable, ≤ 30ms RTT to Delhi)
SECONDARY: bsnl_north_primary         (BSNL SIP, PCMU fallback, ≤ 60ms RTT)
TERTIARY:  jio_north_primary          (JIO SIP, PCMU, ≤ 80ms RTT)
EMERGENCY: bsnl_national_fallback     (National BSNL, PCMU only, ≤ 200ms RTT)
MAX_FAILOVER_ATTEMPTS: 3
CIRCUIT_BREAKER_THRESHOLD: 5 consecutive failures in 60s → trunk marked DOWN for 300s
```

### SOUTH Zone — GD Session

```
PRIMARY:   jio_south_primary          (JIO SIP, OPUS capable, ≤ 25ms RTT to Bangalore)
SECONDARY: airtel_south_primary       (Airtel SIP, OPUS/PCMU, ≤ 50ms RTT)
TERTIARY:  bsnl_south_primary         (BSNL SIP, PCMU, ≤ 90ms RTT)
EMERGENCY: bsnl_national_fallback
MAX_FAILOVER_ATTEMPTS: 3
```

### EAST Zone — GD Session

```
PRIMARY:   bsnl_east_primary          (BSNL SIP, PCMU — strongest rural East coverage)
SECONDARY: jio_east_primary           (JIO SIP, OPUS)
TERTIARY:  airtel_east_primary        (Airtel SIP, OPUS)
EMERGENCY: bsnl_national_fallback
MAX_FAILOVER_ATTEMPTS: 3
```

### WEST Zone — GD Session

```
PRIMARY:   airtel_west_primary        (Airtel SIP, OPUS)
SECONDARY: jio_west_primary           (JIO SIP, OPUS)
TERTIARY:  bsnl_west_primary          (BSNL SIP, PCMU)
EMERGENCY: bsnl_national_fallback
MAX_FAILOVER_ATTEMPTS: 3
```

### RURAL_NATIONAL Zone — ALL Session Types

```
PRIMARY:   bsnl_rural_primary         (BSNL landline-grade SIP, PCMU 8kHz ONLY)
SECONDARY: bsnl_national_fallback     (BSNL national, PCMU)
TERTIARY:  SMS_ONLY_MODE              (No voice bridge possible — SMS PIN for WebRTC retry notification)
EMERGENCY: SPECTATOR_ASSIGNMENT       (Cannot establish voice — participant becomes spectator)
MAX_FAILOVER_ATTEMPTS: 2
CODEC_LOCK: PCMU_8KHZ (rural calls must never negotiate OPUS — 2G cannot carry it)
```

### Interview — Any Zone (Premium Routing)

```
Interview sessions use PREMIUM tier routing:
  Priority: LOWEST LATENCY trunk first, regardless of cost
  Reason: 1-on-1 interview audio quality is non-negotiable
  Override: cost_tier = PREMIUM overrides operator cost priority order
  PRIMARY: Zone's lowest-latency trunk (real-time lookup from health registry)
  SECONDARY: Zone's second-lowest-latency trunk
  TERTIARY: National lowest-latency available trunk
  MAX_FAILOVER_ATTEMPTS: 3
```

---

---

# SECTION VII — ROUTING DECISION ALGORITHM (LOCKED)

## Master Algorithm — Deterministic Rule-Tree Evaluation

```
INPUT: RoutingRequest {
  request_type:         DID_ASSIGNMENT | SIP_TRUNK | SMS_GATEWAY | FAILOVER_SEQUENCE | RURAL_OVERRIDE
  caller_msisdn:        TEXT | NULL
  session_id:           UUID
  session_type:         GD_SESSION | DOJO_MATCH | INTERVIEW
  participant_id:       UUID
  tenant_id:            UUID
  connectivity_profile: TEXT
  declared_region_zone: TEXT | NULL  (overrides MSISDN-derived zone if present)
  request_timestamp:    UNIX_MS
  idempotency_key:      UUID
}

═══════════════════════════════════════════════════════════════════
STEP 1: RESOLVE GEOGRAPHIC ZONE
═══════════════════════════════════════════════════════════════════
  IF declared_region_zone IS NOT NULL AND declared_region_zone IN known_zones:
    zone = declared_region_zone
    zone_source = "DECLARED"

  ELSE IF caller_msisdn IS NOT NULL:
    prefix_3digit = caller_msisdn[0:5]   ← country code + first 2 digits
    prefix_5digit = caller_msisdn[0:7]   ← country code + STD code
    zone = PREFIX_TABLE.lookup(prefix_5digit) OR PREFIX_TABLE.lookup(prefix_3digit)
    zone_source = "MSISDN_PREFIX"

  ELSE:
    zone = "RURAL_NATIONAL"
    zone_source = "DEFAULT_FALLBACK"

  IF zone IS NULL:
    zone = "RURAL_NATIONAL"
    zone_source = "UNRESOLVABLE_DEFAULT"
    EMIT routing.zone.unresolvable { participant_id, session_id }

═══════════════════════════════════════════════════════════════════
STEP 2: LOAD ACTIVE ROUTING RULE TABLE VERSION
═══════════════════════════════════════════════════════════════════
  routing_version = REDIS.GET("routing_table:active_version")
  IF routing_version IS NULL:
    routing_version = POSTGRES.SELECT(MAX(version) FROM routing_rule_versions WHERE effective_until IS NULL)
    REDIS.SET("routing_table:active_version", routing_version, EX=300)

  IF routing_version IS NULL:
    EMIT routing.table.unavailable (CRITICAL)
    RETURN { resolved: false, reason: "ROUTING_TABLE_UNAVAILABLE" }
    STOP — block all PSTN operations

═══════════════════════════════════════════════════════════════════
STEP 3: EVALUATE RULES IN PRIORITY ORDER
═══════════════════════════════════════════════════════════════════
  candidate_rules = FILTER routing_rules WHERE:
    rule.zone == zone OR rule.zone == "ALL"
    AND rule.session_type CONTAINS session_type OR "ALL"
    AND rule.tenant_id CONTAINS tenant_id OR "*"
    AND rule.connectivity_profile CONTAINS connectivity_profile OR "ALL"
    AND rule.rule_type == request_type

  Sort candidate_rules by priority ASC (lower number = higher priority)

  FOR EACH rule IN candidate_rules (in priority order):
    IF rule.msisdn_prefix is set AND caller_msisdn does NOT match any prefix:
      SKIP rule
    IF rule.state_code is set AND derived_state NOT IN rule.state_code:
      SKIP rule
    matched_rule = rule
    BREAK

  IF matched_rule IS NULL:
    EMIT routing.rule.unmatched (HIGH) { zone, session_type, tenant_id, request_type }
    RETURN { resolved: false, reason: "NO_MATCHING_RULE" }
    STOP

═══════════════════════════════════════════════════════════════════
STEP 4: VALIDATE TRUNK HEALTH (FOR SIP_TRUNK AND DID_ASSIGNMENT)
═══════════════════════════════════════════════════════════════════
  IF request_type IN [SIP_TRUNK, DID_ASSIGNMENT]:
    FOR EACH trunk IN matched_rule.failover_sequence:
      trunk_health = REDIS.GET("trunk_health:{trunk_id}")
      IF trunk_health == "HEALTHY":
        selected_trunk = trunk
        BREAK
      IF trunk_health == "DEGRADED":
        IF cost_tier == "PREMIUM" (Interview):
          SKIP — try next trunk
        ELSE:
          selected_trunk = trunk  ← accept degraded for non-premium
          BREAK
      IF trunk_health == "DOWN" OR trunk_health IS NULL:
        SKIP — try next trunk

    IF selected_trunk IS NULL:
      IF RURAL_NATIONAL and SMS_ONLY_MODE available:
        RETURN { resolved: true, routing_mode: "SMS_ONLY" }
      ELSE:
        EMIT routing.all_trunks.exhausted (CRITICAL) { zone, session_type }
        RETURN { resolved: true, routing_mode: "SPECTATOR" }
        ← participant becomes spectator — session continues for others

═══════════════════════════════════════════════════════════════════
STEP 5: RESOLVE DID ASSIGNMENT (FOR DID_ASSIGNMENT REQUEST)
═══════════════════════════════════════════════════════════════════
  available_DIDs = DID_REGISTRY.get_available(zone, trunk_id)
  IF available_DIDs IS EMPTY:
    EMIT routing.did.pool_exhausted (HIGH) { zone, trunk_id }
    TRY next trunk in failover sequence → retry Step 4
    IF still exhausted: RETURN national_fallback_DID

  selected_DID = available_DIDs[0]  ← always the first available, sorted by DID number ASC
    ← deterministic: same sorted list, same result for same pool state

  RESERVE selected_DID:
    REDIS.SET("did_reserved:{did_number}:{session_id}", participant_id, EX=session_duration+3600)
    POSTGRES: INSERT INTO did_reservation_log

═══════════════════════════════════════════════════════════════════
STEP 6: RESOLVE SMS GATEWAY (FOR SMS_GATEWAY REQUEST)
═══════════════════════════════════════════════════════════════════
  matched_gateway = matched_rule.sms_gateway.primary
  gateway_health = REDIS.GET("sms_gateway_health:{gateway_id}")

  IF gateway_health == "DOWN":
    matched_gateway = matched_rule.sms_gateway.fallback

  IF matched_gateway IS NULL OR fallback also DOWN:
    EMIT routing.sms_gateway.all_down (CRITICAL)
    RETURN { resolved: false, reason: "SMS_DELIVERY_IMPOSSIBLE" }
    STOP — PIN cannot be delivered — bridge session blocked

═══════════════════════════════════════════════════════════════════
STEP 7: BUILD ROUTING DECISION RECORD
═══════════════════════════════════════════════════════════════════
  routing_decision = {
    decision_id:           UUID (new)
    idempotency_key:       from request
    session_id:            from request
    participant_id:        from request
    request_type:          from request
    zone_resolved:         zone
    zone_source:           zone_source
    routing_rule_version:  routing_version
    matched_rule_id:       matched_rule.rule_id
    selected_trunk_id:     selected_trunk.trunk_id (if applicable)
    selected_did:          selected_DID (if applicable)
    selected_sms_gateway:  matched_gateway.gateway_id (if applicable)
    failover_sequence:     matched_rule.failover_sequence
    trunks_skipped:        [list of skipped trunk IDs + health reason]
    routing_mode:          NORMAL | DEGRADED | SMS_ONLY | SPECTATOR | RURAL_OVERRIDE
    resolved:              true | false
    resolution_latency_ms: (computed)
    resolved_at:           NOW()
  }

═══════════════════════════════════════════════════════════════════
STEP 8: PERSIST AND CACHE DECISION
═══════════════════════════════════════════════════════════════════
  POSTGRES: INSERT INTO routing_decision_audit_log (immutable)
  REDIS.SET("routing_decision:{idempotency_key}", routing_decision, EX=3600)
  EMIT: routing.decision.resolved

═══════════════════════════════════════════════════════════════════
STEP 9: RETURN DECISION
═══════════════════════════════════════════════════════════════════
  RETURN routing_decision
```

---

---

# SECTION VIII — IDEMPOTENCY GUARANTEE (LOCKED)

## Same Request → Same Routing Decision

```
The routing decision for an idempotency_key is cached in Redis for 1 hour.

IF a second request arrives with the same idempotency_key:
  → Agent returns the CACHED routing decision
  → No re-evaluation of the rule tree
  → No new trunk health check
  → No new DID assignment
  → Identical response to original caller

WHY THIS MATTERS:
  PSTN_BRIDGE_CONTROL_AGENT retries routing requests on transient failures.
  Without idempotency: two routing calls for same session → two DID assignments
    → candidate receives two PINs for two different DIDs → call collision.
  With idempotency: second call returns exactly the first decision.
  Same trunk. Same DID. Same gateway. Always.
```

---

---

# SECTION IX — TRUNK HEALTH REGISTRY (LOCKED)

## Health Check Protocol

```
Trunk health is maintained by a separate health-check sub-loop,
NOT by the routing decision algorithm itself.

Health checks run independently:
  Frequency: every 15 seconds per trunk
  Method: SIP OPTIONS ping to sip_proxy_host:sip_proxy_port
  Timeout: 2,000 ms
  Success: SIP 200 OK received within timeout
  Failure: No response OR SIP 5xx received

Health state transitions:
  HEALTHY → DEGRADED: 2 consecutive failed checks
  DEGRADED → DOWN:    4 consecutive failed checks (within 60s)
  DOWN → HALF_OPEN:   After circuit_breaker_threshold_seconds (default: 300s)
  HALF_OPEN → HEALTHY: 1 successful probe check
  HALF_OPEN → DOWN:    Probe fails → back to DOWN for another 300s

State stored in Redis:
  Key: trunk_health:{trunk_id}
  Value: "HEALTHY" | "DEGRADED" | "DOWN" | "HALF_OPEN"
  TTL: 60 seconds (must be refreshed by health-check loop — if loop dies, key expires → treated as DOWN)

TTL expiry = implicit DOWN:
  If health-check loop dies and TTL expires:
    routing decision algorithm treats trunk as DOWN
    failover proceeds to secondary trunk
    EMIT: trunk_health.check_loop.failure (CRITICAL)
```

## SMS Gateway Health Registry

```
Same protocol as SIP trunk health — tested via Jasmin API health endpoint:
  GET {jasmin_api_url}/ping → 200 OK = HEALTHY
  Frequency: every 30 seconds
  States: HEALTHY | DOWN (binary for SMS gateways — no DEGRADED)
  Redis key: sms_gateway_health:{gateway_id}
  TTL: 90 seconds
```

---

---

# SECTION X — DID POOL MANAGEMENT (LOCKED)

## DID Assignment Rules

```
DID = Direct Inward Dialing number (the phone number candidates dial)

DID Pool Per Zone:
  NORTH:          Minimum 10 DIDs (one per major city in zone)
  SOUTH:          Minimum 10 DIDs
  EAST:           Minimum 8 DIDs
  WEST:           Minimum 10 DIDs
  CENTRAL:        Minimum 6 DIDs
  RURAL_NATIONAL: Minimum 4 DIDs (BSNL national numbers)
  METRO_DL:       Minimum 4 DIDs (011-XXXXXXXX format)
  METRO_MH:       Minimum 4 DIDs (022-XXXXXXXX format)

DID Reservation:
  When DID assigned to session → reserved in Redis for session_duration + 1 hour
  DID cannot be assigned to another session while reserved
  Reservation auto-expires after TTL — no manual cleanup required

DID Reuse Policy:
  Same DID CAN be assigned to different sessions if:
    Session A has ended AND DID reservation TTL expired
  Same DID CANNOT be assigned to two concurrent sessions — ever
  Enforced by Redis reservation key uniqueness

DID Numbering Presentation:
  Candidate always sees a number matching their local area code
  Delhi candidate → 011-XXXXXXXX format DID
  Mumbai candidate → 022-XXXXXXXX format DID
  Rural candidate → 1800-XXX-XXXX (toll-free national)
  Toll-free for RURAL_NATIONAL is mandatory — rural candidates pay zero for the call
```

## DID Exhaustion Handling

```
IF all DIDs in primary zone pool are reserved:
  STEP 1: Attempt adjacent zone DID pool
    (NORTH exhausted → attempt CENTRAL zone DID)
  STEP 2: If adjacent also exhausted → national fallback DID
    (shared pool of last-resort DIDs — always available)
  STEP 3: If national also exhausted:
    EMIT: routing.did.critical_exhaustion (CRITICAL)
    Alert ops team immediately
    Return national fallback DID with warning
    NEVER block session — degrade gracefully
```

---

---

# SECTION XI — TENANT-SPECIFIC ROUTING OVERRIDES (LOCKED)

## Multi-Tenant Routing Isolation

```
ECOSKILLER is multi-tenant from day zero (Master Prompt).

Routing isolation rules:
  1. Each tenant may have dedicated SIP trunk contracts
     (tenant-negotiated operator agreements are valid)
  2. Tenant A's PSTN traffic MUST NEVER traverse Tenant B's trunks
  3. Tenant ID is a mandatory input to every routing decision
  4. If no tenant-specific rule exists → fall through to global zone rules
  5. Tenant-specific rules have HIGHER priority than zone rules (lower priority number)

Tenant Routing Rule Example:
  Tenant: "CampusHire_IIT_Delhi"
  Override: Route ALL GD sessions for this tenant via airtel_north_enterprise
    (tenant has negotiated enterprise trunk with dedicated capacity)
  Rule priority: 1 (highest — evaluated before zone defaults)

Cross-tenant trunk sharing:
  FORBIDDEN unless both tenants have explicitly consented in writing
    AND consent is recorded in tenant_trunk_sharing_agreements table
    AND routing rule explicitly declares shared=TRUE
  Absence of written consent → tenant traffic isolation enforced → no sharing
```

---

---

# SECTION XII — SOCIETY RURAL ZONE SPECIAL ROUTING (LOCKED)

## Rural Zone Routing Override Protocol

The Society Architecture mandates support for offline franchise zones and rural skill officers. These zones require special handling that differs from standard zone routing.

```
Rural Zone Detection:
  participant.connectivity_profile == "RURAL_OFFLINE"
  OR session.declared_zone IN rural_zone_registry
  OR participant.society_franchise_zone IS NOT NULL

Rural Zone Routing Rules (LOCKED):

  RULE R-1: CODEC LOCK
    ALL rural zone bridge calls: PCMU 8kHz ONLY
    OPUS negotiation: FORBIDDEN for rural participants
    Reason: 2G networks cannot carry Opus without packet loss
    Implementation: FreeSWITCH dialplan forces PCMU before SIP INVITE

  RULE R-2: BSNL TRUNK PRIORITY
    Rural primary trunk: bsnl_rural_primary (ALWAYS)
    Reason: BSNL has deepest rural penetration in India
    Other operators (Airtel, JIO): secondary/tertiary only in rural zones

  RULE R-3: TOLL-FREE DID MANDATORY
    Rural zone candidates MUST receive toll-free number (1800-XXX-XXXX)
    Paid DIDs FORBIDDEN for rural_connectivity_profile participants
    Reason: Cost barrier elimination for rural inclusion

  RULE R-4: EXTENDED RECONNECT WINDOW
    Rural zone bridge reconnects: 90 seconds (vs 30s standard)
    Reason: 2G call re-establishment takes longer
    Declared in routing decision payload → passed to Bridge Control Agent

  RULE R-5: SMS-ONLY FALLBACK THRESHOLD
    If all SIP trunks return DOWN for rural zone:
      → SMS_ONLY_MODE activated
      → Candidate receives SMS with session_id and WebRTC retry instructions
      → System waits 5 minutes for WebRTC join attempt
      → If still unreachable → SPECTATOR
    SMS_ONLY_MODE does NOT mean session blocked
    SMS_ONLY_MODE is a graceful fallback path

  RULE R-6: SOCIETY FRANCHISE COORDINATOR ROUTING
    Society coordinators calling to manage GD sessions:
      → Routed to admin SIP bridge (separate from participant bridge)
      → Priority: HIGH (same as Interview)
      → DID: coordinator-designated number (not from participant pool)
```

---

---

# SECTION XIII — DATABASE SCHEMA (LOCKED)

## Table: `routing_rule_versions`

```sql
CREATE TABLE routing_rule_versions (
  id               UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  version          INTEGER NOT NULL UNIQUE,
  effective_from   TIMESTAMPTZ NOT NULL,
  effective_until  TIMESTAMPTZ,          -- NULL = currently active
  declared_by      TEXT NOT NULL,        -- human operator name/ID
  change_reason    TEXT NOT NULL,
  rule_count       INTEGER NOT NULL,
  checksum         TEXT NOT NULL,        -- SHA256 of full rule JSON
  created_at       TIMESTAMPTZ NOT NULL DEFAULT NOW()
);
-- Immutable — no UPDATE, no DELETE
CREATE UNIQUE INDEX idx_rrv_active ON routing_rule_versions(version) WHERE effective_until IS NULL;
```

---

## Table: `routing_rules`

```sql
CREATE TABLE routing_rules (
  id                    UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  rule_version          INTEGER NOT NULL REFERENCES routing_rule_versions(version),
  rule_id               UUID NOT NULL,
  rule_type             TEXT NOT NULL CHECK (rule_type IN (
                          'DID_ASSIGNMENT','SIP_TRUNK','SMS_GATEWAY',
                          'FAILOVER_SEQUENCE','RURAL_OVERRIDE'
                        )),
  zone                  TEXT NOT NULL,
  msisdn_prefixes       TEXT[],
  state_codes           TEXT[],
  session_types         TEXT[] NOT NULL,
  tenant_ids            TEXT[] NOT NULL,
  connectivity_profiles TEXT[] NOT NULL,
  did_pool_id           TEXT,
  primary_trunk_id      TEXT,
  failover_trunk_ids    TEXT[],
  primary_sms_gateway   TEXT,
  fallback_sms_gateway  TEXT,
  codec_lock            TEXT,
  did_type              TEXT,
  cost_tier             TEXT NOT NULL DEFAULT 'STANDARD',
  extended_reconnect_s  INTEGER,
  toll_free_required    BOOLEAN NOT NULL DEFAULT FALSE,
  rural_override        BOOLEAN NOT NULL DEFAULT FALSE,
  priority              INTEGER NOT NULL,
  created_at            TIMESTAMPTZ NOT NULL DEFAULT NOW()
);
-- Immutable — rules are versioned; old rules never deleted
CREATE INDEX idx_rr_version_type  ON routing_rules(rule_version, rule_type);
CREATE INDEX idx_rr_zone_type     ON routing_rules(zone, rule_type, priority);
CREATE INDEX idx_rr_tenant        ON routing_rules(tenant_ids, rule_version);
```

---

## Table: `routing_decision_audit_log`

```sql
CREATE TABLE routing_decision_audit_log (
  id                    UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  decision_id           UUID NOT NULL UNIQUE,
  idempotency_key       UUID NOT NULL,
  session_id            UUID NOT NULL,
  participant_id        UUID NOT NULL,
  tenant_id             UUID NOT NULL,
  request_type          TEXT NOT NULL,
  zone_resolved         TEXT NOT NULL,
  zone_source           TEXT NOT NULL,
  routing_rule_version  INTEGER NOT NULL,
  matched_rule_id       UUID,
  selected_trunk_id     TEXT,
  selected_did          TEXT,
  selected_sms_gateway  TEXT,
  failover_sequence     TEXT[],
  trunks_skipped        JSONB,
  routing_mode          TEXT NOT NULL,
  resolved              BOOLEAN NOT NULL,
  resolution_latency_ms INTEGER NOT NULL,
  resolved_at           TIMESTAMPTZ NOT NULL DEFAULT NOW()
);
-- Immutable — no UPDATE, no DELETE — governed under R60
CREATE UNIQUE INDEX idx_rdal_idem       ON routing_decision_audit_log(idempotency_key);
CREATE INDEX        idx_rdal_session    ON routing_decision_audit_log(session_id, resolved_at);
CREATE INDEX        idx_rdal_trunk      ON routing_decision_audit_log(selected_trunk_id, resolved_at);
CREATE INDEX        idx_rdal_zone       ON routing_decision_audit_log(zone_resolved, routing_mode);
CREATE INDEX        idx_rdal_tenant     ON routing_decision_audit_log(tenant_id, resolved_at);
```

---

## Table: `did_reservation_log`

```sql
CREATE TABLE did_reservation_log (
  id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  did_number      TEXT NOT NULL,
  session_id      UUID NOT NULL,
  participant_id  UUID NOT NULL,
  tenant_id       UUID NOT NULL,
  zone            TEXT NOT NULL,
  trunk_id        TEXT NOT NULL,
  reserved_at     TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  expires_at      TIMESTAMPTZ NOT NULL,
  released_at     TIMESTAMPTZ,
  release_reason  TEXT
);
CREATE UNIQUE INDEX idx_drl_active  ON did_reservation_log(did_number, session_id)
  WHERE released_at IS NULL;
CREATE INDEX        idx_drl_session ON did_reservation_log(session_id);
CREATE INDEX        idx_drl_did     ON did_reservation_log(did_number, reserved_at);
```

---

## Table: `trunk_health_log`

```sql
CREATE TABLE trunk_health_log (
  id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  trunk_id        TEXT NOT NULL,
  health_state    TEXT NOT NULL,
  previous_state  TEXT,
  transition_reason TEXT,
  consecutive_failures INTEGER,
  recorded_at     TIMESTAMPTZ NOT NULL DEFAULT NOW()
);
CREATE INDEX idx_thl_trunk   ON trunk_health_log(trunk_id, recorded_at);
CREATE INDEX idx_thl_state   ON trunk_health_log(health_state, recorded_at);
```

---

## ClickHouse Analytics Table: `routing_decision_stats`

```sql
CREATE TABLE routing_decision_stats (
  stat_date               Date,
  stat_hour               UInt8,
  zone                    String,
  session_type            String,
  request_type            String,
  routing_mode            String,
  selected_trunk_id       String,
  tenant_id               UUID,
  total_decisions         UInt64,
  total_resolved          UInt64,
  total_failed            UInt64,
  total_failovers         UInt64,
  total_rural_overrides   UInt64,
  total_spectator_assign  UInt64,
  avg_resolution_ms       Float32,
  p99_resolution_ms       Float32
) ENGINE = SummingMergeTree()
PARTITION BY stat_date
ORDER BY (stat_date, stat_hour, zone, session_type, request_type, routing_mode, tenant_id);
```

---

---

# SECTION XIV — API INTERFACE (LOCKED)

## Resolve Routing Decision

**Endpoint:** `POST /internal/routing/resolve`
**Auth:** Service-to-service JWT (Keycloak)
**Rate Limit:** 15,000 req/sec per node

### Request Body
```json
{
  "request_type":          "DID_ASSIGNMENT | SIP_TRUNK | SMS_GATEWAY | FAILOVER_SEQUENCE | RURAL_OVERRIDE",
  "caller_msisdn":         "+919876543210",
  "session_id":            "uuid",
  "session_type":          "GD_SESSION",
  "participant_id":        "uuid",
  "tenant_id":             "uuid",
  "connectivity_profile":  "2G | RURAL_OFFLINE | FEATURE_PHONE | WEBRTC",
  "declared_region_zone":  "NORTH | null",
  "idempotency_key":       "uuid",
  "timestamp":             1741084320000
}
```

### Response: RESOLVED

```json
{
  "resolved":              true,
  "decision_id":           "uuid",
  "routing_mode":          "NORMAL | DEGRADED | RURAL_OVERRIDE | SMS_ONLY | SPECTATOR",
  "zone_resolved":         "NORTH",
  "zone_source":           "MSISDN_PREFIX",
  "routing_rule_version":  12,
  "matched_rule_id":       "uuid",
  "selected_trunk_id":     "airtel_north_primary",
  "selected_did":          "+911141000001",
  "did_toll_free":         false,
  "selected_sms_gateway":  "airtel_north_sms",
  "failover_sequence":     ["airtel_north_primary","bsnl_north_primary","jio_north_primary","bsnl_national_fallback"],
  "codec_lock":            null,
  "extended_reconnect_s":  null,
  "resolution_latency_ms": 4
}
```

### Response: FAILED — No Matching Rule

```json
{
  "resolved":     false,
  "reason":       "NO_MATCHING_RULE | ROUTING_TABLE_UNAVAILABLE | SMS_DELIVERY_IMPOSSIBLE",
  "zone_resolved": "NORTH",
  "block_reason": "All trunks DOWN in zone. Participant marked SPECTATOR."
}
```

---

## Get Active Routing Table Version

**Endpoint:** `GET /internal/routing/version`
**Auth:** Service-to-service JWT

```json
{
  "active_version":  12,
  "effective_from":  "2026-03-01T00:00:00Z",
  "declared_by":     "ops-admin-raj",
  "rule_count":      47,
  "checksum":        "sha256-abc123..."
}
```

---

## Get Trunk Health Registry

**Endpoint:** `GET /internal/routing/trunk-health`
**Auth:** Service-to-service JWT

```json
{
  "trunks": [
    { "trunk_id": "airtel_north_primary",  "health_state": "HEALTHY",  "last_check": "ISO8601", "consecutive_failures": 0 },
    { "trunk_id": "bsnl_north_primary",    "health_state": "DEGRADED", "last_check": "ISO8601", "consecutive_failures": 2 },
    { "trunk_id": "jio_north_primary",     "health_state": "DOWN",     "last_check": "ISO8601", "consecutive_failures": 6 }
  ]
}
```

---

## Force Trunk State (Emergency — Admin Only)

**Endpoint:** `POST /internal/routing/trunk/force-state`
**Auth:** Service-to-service JWT — RESTRICTED to Admin Governance Service only

```json
{
  "trunk_id":     "jio_north_primary",
  "force_state":  "DOWN",
  "reason":       "Vendor outage — Airtel confirmed JIO North SIP down until 18:00 IST",
  "declared_by":  "ops-admin-raj",
  "expires_at":   "2026-03-04T18:00:00Z"
}
```

---

---

# SECTION XV — EVENT BUS CONTRACTS (LOCKED)

## Events Emitted by This Agent

### `routing.decision.resolved`
```json
{
  "event_type": "routing.decision.resolved",
  "payload": {
    "decision_id":           "uuid",
    "session_id":            "uuid",
    "participant_id":        "uuid",
    "zone_resolved":         "NORTH",
    "selected_trunk_id":     "airtel_north_primary",
    "selected_did":          "+911141000001",
    "routing_mode":          "NORMAL",
    "routing_rule_version":  12,
    "resolution_latency_ms": 4,
    "timestamp":             "ISO8601"
  }
}
```

### `routing.decision.failed`
```json
{
  "event_type": "routing.decision.failed",
  "payload": {
    "session_id":     "uuid",
    "participant_id": "uuid",
    "zone":           "RURAL_NATIONAL",
    "reason":         "NO_MATCHING_RULE | SMS_DELIVERY_IMPOSSIBLE",
    "routing_mode":   "SPECTATOR",
    "timestamp":      "ISO8601",
    "severity":       "HIGH"
  }
}
```

### `routing.trunk.state_changed`
```json
{
  "event_type": "routing.trunk.state_changed",
  "payload": {
    "trunk_id":       "jio_north_primary",
    "previous_state": "DEGRADED",
    "new_state":      "DOWN",
    "trigger":        "CONSECUTIVE_FAILURES | FORCE_STATE | CIRCUIT_BREAKER",
    "impact_zones":   ["NORTH"],
    "timestamp":      "ISO8601",
    "severity":       "CRITICAL"
  }
}
```

### `routing.did.pool_exhausted`
```json
{
  "event_type": "routing.did.pool_exhausted",
  "payload": {
    "zone":         "NORTH",
    "trunk_id":     "airtel_north_primary",
    "pool_size":    10,
    "reserved":     10,
    "fallback_used": "national_fallback",
    "timestamp":    "ISO8601",
    "severity":     "HIGH"
  }
}
```

### `routing.table.unavailable`
```json
{
  "event_type": "routing.table.unavailable",
  "payload": {
    "redis_available":    false,
    "postgres_available": false,
    "all_pstn_blocked":   true,
    "timestamp":          "ISO8601",
    "severity":           "CRITICAL"
  }
}
```

### `routing.zone.unresolvable`
```json
{
  "event_type": "routing.zone.unresolvable",
  "payload": {
    "session_id":    "uuid",
    "caller_msisdn_prefix": "9199",
    "fallback_zone": "RURAL_NATIONAL",
    "timestamp":     "ISO8601"
  }
}
```

### `routing.rule.unmatched`
```json
{
  "event_type": "routing.rule.unmatched",
  "payload": {
    "zone":         "EAST",
    "session_type": "DOJO_MATCH",
    "tenant_id":    "uuid",
    "request_type": "SMS_GATEWAY",
    "timestamp":    "ISO8601",
    "severity":     "HIGH"
  }
}
```

## Events This Agent Consumes

| Kafka Topic | Action |
|---|---|
| `pstn_bridge.fallback.requested` | Resolve SMS gateway route for PIN delivery |
| `pstn_bridge.session.active` | Record DID as confirmed active for session |
| `pstn_bridge.session.terminated` | Release DID reservation |
| `admin.routing.table.updated` | Invalidate Redis routing table cache, load new version |
| `admin.trunk.force.state` | Update trunk health in Redis with force-override |

---

---

# SECTION XVI — ROUTING RULE TABLE GOVERNANCE (LOCKED)

## Who May Change the Routing Rule Table

```
PERMITTED actors:
  Human operators with ROUTING_ADMIN role in Keycloak
  Changes must include: reason, declared_by, effective_from

FORBIDDEN actors:
  AI agents (including this agent itself)
  Automated systems without human declaration
  Any service acting without a change_reason field

Change process:
  1. Human declares new routing rule version in Admin Console
  2. System validates: no rule conflicts, no zone gaps, no tenant isolation violations
  3. Checksum computed and stored with version record
  4. New version effective_from set (can be future-dated for planned changes)
  5. Previous version effective_until set to match new version effective_from
  6. Redis cache invalidated: REDIS.DEL("routing_table:active_version")
  7. All pods reload routing table on next request
  8. Kafka event emitted: admin.routing.table.updated
  9. Version bump is immutable — old versions never deleted, never mutated
```

## Version Rollback Protocol

```
IF new routing version causes routing failures:
  Human operator declares rollback in Admin Console
  Rollback is NOT deletion of new version
  Rollback = setting new_version.effective_until = NOW()
              AND old_version.effective_until = NULL
  Both versions preserved in audit history
  Redis cache invalidated — pods reload previous version
  Kafka event: admin.routing.table.rolledback
```

---

---

# SECTION XVII — OBSERVABILITY (NON-OPTIONAL)

## Prometheus Metrics

```
# HELP routing_decisions_total Routing decisions resolved
# TYPE routing_decisions_total counter
routing_decisions_total{zone, session_type, request_type, routing_mode, resolved, tenant_id}

# HELP routing_resolution_latency_ms Decision resolution latency
# TYPE routing_resolution_latency_ms histogram
routing_resolution_latency_ms{request_type, zone}
  buckets: [1, 2, 5, 10, 25, 50, 100]

# HELP routing_trunk_health_state Current trunk health (1=HEALTHY, 0.5=DEGRADED, 0=DOWN)
# TYPE routing_trunk_health_state gauge
routing_trunk_health_state{trunk_id, zone, operator}

# HELP routing_failovers_total Trunk failover events
# TYPE routing_failovers_total counter
routing_failovers_total{zone, from_trunk, to_trunk, session_type}

# HELP routing_did_pool_utilization DID pool utilization (reserved/total)
# TYPE routing_did_pool_utilization gauge
routing_did_pool_utilization{zone}

# HELP routing_spectator_assignments_total Participants assigned spectator due to routing failure
# TYPE routing_spectator_assignments_total counter
routing_spectator_assignments_total{zone, reason}

# HELP routing_rural_overrides_total Rural override routing activations
# TYPE routing_rural_overrides_total counter
routing_rural_overrides_total{zone, routing_rule}

# HELP routing_rule_version_active Currently active routing rule version
# TYPE routing_rule_version_active gauge
routing_rule_version_active{}

# HELP routing_cache_hit_rate Redis cache hits vs misses for routing decisions
# TYPE routing_cache_hit_rate gauge
routing_cache_hit_rate{}
```

## Grafana Dashboards (Mandatory)

- **Trunk Health Matrix** — Real-time grid: all trunks × all zones; color-coded HEALTHY/DEGRADED/DOWN
- **Routing Decision Funnel** — NORMAL → DEGRADED → RURAL_OVERRIDE → SMS_ONLY → SPECTATOR rates
- **Failover Event Timeline** — Chronological trunk failover log with zone and session type
- **DID Pool Utilization** — Pool fill rate per zone; alert threshold at 80%
- **Zone Routing Latency P99** — Resolution time per zone — target < 10ms P99
- **Routing Rule Version Tracker** — Active version, last changed, declared by whom
- **Tenant Traffic Distribution** — Per-tenant routing decisions, trunk usage, failover rates
- **Rural Override Heatmap** — Geographic density of rural routing activations

## Alerting Rules (All Mandatory)

| Alert | Condition | Severity | Action |
|---|---|---|---|
| `RoutingTrunkDown` | Any trunk health_state == DOWN for 60s | CRITICAL | Page on-call — check operator |
| `RoutingAllTrunksDown` | All trunks DOWN in any zone | CRITICAL+EMERGENCY | Zone-wide PSTN outage |
| `RoutingTableUnavailable` | Redis + Postgres both unavailable | CRITICAL | All PSTN ops blocked |
| `RoutingDIDPoolHigh` | DID pool utilization > 85% in any zone | HIGH | Provision additional DIDs |
| `RoutingDIDPoolExhausted` | All DIDs reserved in a zone | CRITICAL | Provision immediately |
| `RoutingSpectatorSpike` | spectator_assignments > 5 in 10m any zone | HIGH | Investigate zone trunk health |
| `RoutingRuleUnmatched` | rule.unmatched > 0 in 15m | HIGH | Rule gap — review routing table |
| `RoutingHighLatency` | P99 > 50ms for 3m | MEDIUM | Redis or DB performance issue |
| `RoutingRuralSMSOnly` | SMS_ONLY mode > 3 in 15m any zone | HIGH | Rural trunk investigation |
| `RoutingTenantCrossleak` | Routing decision crosses tenant boundary | CRITICAL+SECURITY | Immediate audit |

---

---

# SECTION XVIII — SECURITY CONSTRAINTS (LOCKED)

- Routing rule table changes require **ROUTING_ADMIN Keycloak role** — no self-service modification
- Routing decisions are **immutably logged** — no UPDATE, no DELETE on `routing_decision_audit_log`
- Trunk credentials (SIP passwords, SMSC passwords) stored in **HashiCorp Vault only** — never in routing table records or environment variables
- DID numbers in audit log are **stored in full** for legitimate audit — protected by **RLS on tenant_id** (cross-tenant DID audit is forbidden)
- `routing_decision_audit_log` is protected by **tenant_id row-level security** — tenant A cannot read tenant B's routing decisions
- Forced trunk state endpoint (`/internal/routing/trunk/force-state`) is **restricted to Admin Governance Service** only — Kong blocks all other callers
- Routing table version checksum is verified on every load — if checksum mismatch detected → **STOP using that version → alert → rollback to previous**
- Wazuh SIEM receives: cross-tenant routing alerts, routing table checksum failures, all forced trunk state changes
- All `/internal/routing/*` endpoints are **Kong-blocked from external access**

---

---

# SECTION XIX — DEPLOYMENT SPECIFICATION (LOCKED)

## Kubernetes Namespace

```
Namespace: realtime
Service Name: phone-region-routing-determinism-agent
Co-located with: pstn-bridge-control-agent, gd-orchestrator
```

## Resource Defaults

```yaml
replicas: 3
autoscaling:
  minReplicas: 3
  maxReplicas: 10
  targetCPUUtilizationPercentage: 50
resources:
  requests: { cpu: "100m", memory: "128Mi" }
  limits:   { cpu: "300m", memory: "256Mi" }
```

> 3 minimum replicas: routing decisions must be available even during rolling deploys.
> Low resource profile: routing decisions are pure in-memory rule-tree evaluation — no heavy compute.

## Health Check Endpoints

```
GET /health/live   → 200 OK always (process alive)
GET /health/ready  → 200 OK if:
                      Redis reachable AND
                      PostgreSQL reachable AND
                      Active routing rule version loadable AND
                      Trunk health check loop running
```

## Environment Variables Required

```
REDIS_HOST
REDIS_PASSWORD           # From Vault
POSTGRES_HOST
POSTGRES_DB
POSTGRES_USER
POSTGRES_PASSWORD        # From Vault
KAFKA_BROKER_URL
KEYCLOAK_JWKS_URL
LOG_LEVEL
TRUNK_HEALTH_CHECK_INTERVAL_S        # Default: 15
SMS_GATEWAY_HEALTH_CHECK_INTERVAL_S  # Default: 30
CIRCUIT_BREAKER_RESET_S              # Default: 300
DID_POOL_ALERT_THRESHOLD_PCT         # Default: 85
ROUTING_CACHE_TTL_S                  # Default: 300 (5 min for routing table version cache)
ROUTING_DECISION_CACHE_TTL_S         # Default: 3600 (1 hr for idempotency cache)
ROUTING_RULE_VERSION_OVERRIDE        # Optional — forces specific version for testing
```

---

---

# SECTION XX — INTERN EXECUTION GUIDE

## Service Location

```
/backend/services/phone_region_routing_determinism_agent/
```

## Step-by-Step Local Execution

**Step 1:** Navigate and setup
```bash
cd /backend/services/phone_region_routing_determinism_agent/
python -m venv venv && source venv/bin/activate
pip install -r requirements.txt
cp .env.example .env
# Edit .env with local Redis + PostgreSQL values
```

**Step 2:** Run migrations (creates all tables + seed initial routing rule version)
```bash
alembic upgrade head
```

**Expected migration output:**
```
Create table routing_rule_versions ... OK
Create table routing_rules ... OK
Create table routing_decision_audit_log ... OK
Create table did_reservation_log ... OK
Create table trunk_health_log ... OK
Seed initial routing rule version v1 ... OK
```

**Step 3:** Start the service
```bash
uvicorn main:app --reload --port 8094
```

**Expected Output:**
```
INFO: PHONE_REGION_ROUTING_DETERMINISM_AGENT ready on port 8094
INFO: Redis: CONNECTED
INFO: PostgreSQL: CONNECTED
INFO: Active routing rule version: 1
INFO: Trunk health check loop: STARTED (interval: 15s)
INFO: SMS gateway health loop: STARTED (interval: 30s)
INFO: Trunks loaded: 12
INFO: DID pools loaded: 8 zones
```

**Step 4:** Verify determinism — same request → same output
```bash
# Send identical request twice — must return identical decision_id content
IDEM_KEY=$(uuidgen)
curl -X POST http://localhost:8094/internal/routing/resolve \
  -H "Content-Type: application/json" \
  -d "{\"request_type\":\"DID_ASSIGNMENT\",\"caller_msisdn\":\"+919876543210\",\"session_id\":\"test-001\",\"session_type\":\"GD_SESSION\",\"participant_id\":\"user-001\",\"tenant_id\":\"tenant-001\",\"connectivity_profile\":\"2G\",\"idempotency_key\":\"$IDEM_KEY\",\"timestamp\":$(date +%s)000}"

# Repeat exact same call with same idempotency_key
# Expected: Identical selected_trunk_id, selected_did, routing_mode
```

**If second call returns different trunk or DID → CRITICAL BUG → STOP EXECUTION**

**Step 5:** Verify trunk failover
```bash
# Force a trunk to DOWN state
curl -X POST http://localhost:8094/internal/routing/trunk/force-state \
  -d '{"trunk_id":"airtel_north_primary","force_state":"DOWN","reason":"test","declared_by":"dev-test","expires_at":"..."}'

# Now resolve routing for NORTH zone — must select bsnl_north_primary (secondary)
# Expected: selected_trunk_id = "bsnl_north_primary"
# If still returns "airtel_north_primary" → CRITICAL BUG → STOP EXECUTION
```

---

---

# SECTION XXI — CONTRACT GATE STATUS

| Contract | Status |
|---|---|
| Agent Distinction from PSTN_BRIDGE_CONTROL_AGENT | LOCKED |
| Five Routing Decision Types | LOCKED |
| Routing Rule Table Architecture | LOCKED |
| Region Zone Definitions | LOCKED |
| MSISDN Prefix Routing Table | LOCKED |
| SIP Trunk Registry Schema | LOCKED |
| Failover Sequence Declarations (All Zones) | LOCKED |
| Master Routing Algorithm (9 Steps) | LOCKED |
| Idempotency Guarantee | LOCKED |
| Trunk Health Registry Protocol | LOCKED |
| DID Pool Management Rules | LOCKED |
| Tenant Routing Isolation Policy | LOCKED |
| Society Rural Zone Special Routing (6 Rules) | LOCKED |
| Database Schema | LOCKED |
| API Interface Contract | LOCKED |
| Event Bus Contracts | LOCKED |
| Routing Rule Table Governance | LOCKED |
| Observability Metrics + Alerts | LOCKED |
| Security Constraints | LOCKED |
| Deployment Specification | LOCKED |

**➤ ALL CONTRACT GATES PASSED**
**➤ AGENT STATUS: SEALED · LOCKED · DEPLOYMENT-READY**

---

---

# SECTION XXII — BOUND LAWS & CROSS-REFERENCES

| Law / Spec | Binding Clause |
|---|---|
| R1 — Technology Stack Lock | Kamailio, FreeSWITCH, Redis, PostgreSQL, Kafka, Vault — all self-hosted |
| R4 — Event Schema Registry | All routing events registered in AsyncAPI before deployment |
| R5 — Workflow State Machines | Trunk health state machine (HEALTHY→DEGRADED→DOWN→HALF_OPEN) is a governed R5 machine |
| R18 — Backup & Disaster Recovery | Routing rule table is versioned + backed up; multi-region failover sequence declared |
| R39A — Session Lifecycle | Routing decision is the first gate before any session-linked PSTN operation |
| R44 — Runnable Backend | Agent fully runnable with Redis + PostgreSQL seeded with routing rules |
| R49 — Contract Validator CLI | Routing rule table checksum validated in CI before every deploy |
| R59 — Offline-First & Low-Bandwidth | Rural zone special routing (Rules R-1 through R-6) IS the implementation of R59 for telephony routing |
| R60 — Archival Law | routing_decision_audit_log immutable, 3-year minimum retention |
| R11 — Multi-Tenancy Law | Tenant routing isolation enforced — cross-tenant trunk leak = STOP EXECUTION |
| R18 — Multi-Region Failover | Per-zone failover sequences declared and enforced |
| PSTN_BRIDGE_CONTROL_AGENT v1.0 | This agent provides routing decisions consumed by Bridge Control Agent before every PIN generation and SIP call |
| PHONE_EVENT_DEDUP_AGENT v1.0 | Routing decisions are idempotent — dedup layer prevents double routing for same request |
| Society Architecture — Offline Franchise | Rural zone routing (R-1 through R-6) implements society offline franchise voice path |
| Society Architecture — Multi-tenant RLS | Tenant routing isolation mirrors society_id RLS pattern |
| GD System Spec — Determinism | "Your backend decides everything" — routing is the first expression of this |
| Infrastructure Audit v8 — Self-Hosted | All SIP trunks via self-hosted Kamailio — no SaaS routing providers |
| Infrastructure Audit v8 — GCP+AWS | Cross-cloud MinIO replication pattern extended to routing: failover sequence covers both GCP and AWS node pools |

---

---

# SECTION XXIII — ANTIGRAVITY LAYER DECLARATION (LOCKED)

## Why Deterministic Routing Is the Fifth Pillar

```
THE ROUTING PROBLEM IS INVISIBLE UNTIL SOMETHING BREAKS.

Without deterministic routing:
  A call routes via different trunks on each attempt
    → Audit logs show inconsistent trunk usage → unexplainable cost spikes
  A degraded trunk is selected randomly
    → Candidate's call audio breaks → session quality inconsistent
    → Candidate believes platform is unreliable
  A rural zone candidate gets an urban DID
    → Call costs candidate money → access barrier → rural inclusion mandate violated
  Tenant A's GD session routes via Tenant B's trunk
    → Cross-tenant telephony leak → compliance breach → trust destruction
  Two sessions share a DID during overlapping time
    → Call collision → wrong participant authenticated → GD integrity violation
  Routing table is changed mid-session
    → Calls started under old rules, reconnects route under new rules
    → Same session, different trunks → inconsistent audit trail

WITH PHONE_REGION_ROUTING_DETERMINISM_AGENT:
  Every routing input has exactly one routing output
  The output is determined by version-controlled, human-declared rules
  The output is cached for idempotency
  The output is immutably audited
  The failover sequence is pre-declared and tested
  Rural zones get toll-free BSNL PCMU-locked paths
  Tenants are isolated
  DID collisions are structurally impossible
  Audit trails are reproducible and explainable

The GD System Spec declares: "Your backend decides everything."
This agent IS the backend deciding the telephony dimension of everything.
```

---

---

# 🔒 FINAL SEAL

```
AGENT:       PHONE_REGION_ROUTING_DETERMINISM_AGENT
DOMAIN:      PSTN & Bridge → Antigravity Layer
SYSTEM:      ECOSKILLER v12.0
STATUS:      SEALED
VERSION:     v1.0
COMPANIONS:  PHONE_EVENT_DEDUPLICATION_AGENT v1.0
             PHONE_RACE_CONDITION_GUARD_AGENT v1.0
             PHONE_SCORE_FREEZE_AGENT v1.0
             PSTN_BRIDGE_CONTROL_AGENT v1.0
MUTATION:    ADD-ONLY VIA VERSION BUMP
AUTHORITY:   HUMAN DECLARATION ONLY

NO ROUTING DECISION IS EVER PROBABILISTIC.
NO ROUTING DECISION IS EVER AI-INFERRED.
NO ROUTING TABLE CHANGE IS PERMITTED WITHOUT HUMAN DECLARATION.
IDENTICAL INPUT ALWAYS PRODUCES IDENTICAL OUTPUT.
TENANT ROUTING ISOLATION IS ABSOLUTE.
RURAL ZONE TOLL-FREE ROUTING IS NON-NEGOTIABLE.
FAIL-CLOSED: NO PSTN OPERATION PROCEEDS WITHOUT A RESOLVED ROUTING DECISION.

Violation → STOP EXECUTION → REPORT ROUTING_DETERMINISM_SEAL_BREACH
```

---

*PHONE_REGION_ROUTING_DETERMINISM_AGENT · ECOSKILLER PSTN & Bridge Domain · Antigravity Layer · v1.0 · SEALED*
