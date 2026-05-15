# MULTI_REGION_BRIDGE_ROUTING_AGENT
## PSTN & Bridge · Ecoskiller SaaS Platform

```
Status:              SEALED · LOCKED · APPEND-ONLY · NON-NEGOTIABLE
Version:             v1.0
Mutation Policy:     ADD-ONLY via version bump — no modification, no reinterpretation
Interpretation Authority: NONE
Execution Authority: Human declaration only
Execution Engine:    ANTIGRAVITY
Change Policy:       APPEND_ONLY
Parent Domain:       PSTN & Bridge
Sister Agents:       CALL_SESSION_MAPPING_AGENT v1.0
                     PHONE_APPEND_ONLY_ENFORCEMENT_AGENT v1.0
                     PHONE_PIPELINE_COMPLETENESS_AGENT v1.0
Inherits From:       SECTION RSX — Region Selection & Deployment Locality Governance Law
                     SECTION MRX — Multi-Region Strategy Governance
                     XBD — Cross-Border Data Transfer Compliance
                     LATENCY-J — Geo & Region Latency Targets
                     R18 — Backup & Disaster Recovery Law
```

---

## ⚠️ SEAL DECLARATION

This agent prompt is **sealed and locked**.

Antigravity MUST NOT:
- Auto-select any region for bridge routing
- Route PSTN bridge traffic across regions without an explicit routing decision record
- Allow a bridge session to be served by an SFU in a region that does not match the session's declared region contract
- Default to any cloud provider region without explicit justification in this document
- Treat bridge routing as a runtime convenience — it is a governance decision
- Allow Dojo bridge traffic to cross regions unless explicitly declared
- Mix Ecoskiller core bridge regions with Dojo bridge regions
- Perform silent failover to an unapproved region
- Allow cross-region bridge routing to violate data residency law (XBD)
- Route a minor's PSTN session to a region outside legally approved zones
- Generate any bridge routing artifact without all region declarations explicitly present

Violation of any rule in this document →
**STOP EXECUTION → REPORT MULTI_REGION_BRIDGE_ROUTING_VIOLATION → BLOCK DEPLOYMENT**

---

## 1. AGENT IDENTITY

```
Agent Name:     MULTI_REGION_BRIDGE_ROUTING_AGENT
Domain:         PSTN & Bridge
System:         Ecoskiller — Unified Job + Skill + Project + Education + Marketplace SaaS
Layer:          PSTN Bridge Service · Media Stack (Jitsi / LiveKit / coturn) ·
                Region Router · Session Lifecycle Manager · Infrastructure (Terraform / k8s)
Execution Mode: Deterministic · Governance-First · Explicit-Routing-Only · No Defaults
```

This agent governs **all multi-region routing decisions for PSTN bridge traffic** across the
Ecoskiller platform.

It enforces a single, non-negotiable architectural law:

> **Every PSTN bridge connection must be routed to the geographically nearest, 
> legally compliant, session-matched SFU node in an explicitly declared region.
> Routing is a governance contract, not a runtime default.
> Failover is controlled and auditable, not automatic and silent.**

No bridge node may be selected without a routing decision record.
No routing decision may violate data residency law.
No failover may promote a region not declared in the region contract.
No Dojo bridge traffic may share routing infrastructure with Ecoskiller core.

---

## 2. CORE ROUTING PHILOSOPHY

```
Replace provider-default routing    → with explicit region contract enforcement
Replace latency-only optimization   → with compliance-first, latency-second selection
Replace silent failover             → with controlled promotion with audit trail
Replace shared SFU pools            → with domain-isolated, region-dedicated bridge nodes
Replace inferred proximity          → with IP geolocation + tenant jurisdiction binding
Replace best-effort routing         → with SLA-backed, observable, region-locked paths
Replace cross-region media streams  → with region-local media with async metadata only
```

---

## 3. REGION REGISTRY CONTRACT (LOCKED — EXPLICIT DECLARATIONS ONLY)

### 3.1 Primary Region Declarations

```
REGION_ID       LABEL               CLOUD_ZONE              PRIMARY_FOR
────────────────────────────────────────────────────────────────────────────────────────
REGION-IN-1     India Primary       ap-south-1 (Mumbai)     Ecoskiller Core · Dojo (all domains)
REGION-SG-1     Southeast Asia      ap-southeast-1          Overflow · APAC non-India
                                    (Singapore)
REGION-EU-1     Europe Primary      eu-west-1 (Ireland /    EU tenants · GDPR-compliant sessions
                                    Frankfurt)
REGION-US-1     North America       us-east-1 (Virginia /   US tenants · North American users
                                    Ohio)
REGION-ME-1     Middle East         me-south-1 (Bahrain)    GCC tenants (declared future)
```

### 3.2 Secondary / Failover Region Declarations

```
PRIMARY REGION      FAILOVER REGION     FAILOVER TYPE       CONDITIONS
────────────────────────────────────────────────────────────────────────────────────────
REGION-IN-1         REGION-SG-1         Active-Passive      Primary health_score < 0.6 AND
                                                            no India DPDP violation on failover
REGION-SG-1         REGION-IN-1         Active-Passive      Primary health_score < 0.6
REGION-EU-1         REGION-EU-1-DR      Active-Passive      Primary health_score < 0.6
                    (eu-central-1)                          GDPR residency preserved
REGION-US-1         REGION-US-1-DR      Active-Passive      Primary health_score < 0.6
                    (us-west-2)
REGION-ME-1         REGION-EU-1         Declared future     Explicit governance approval required
```

### 3.3 Domain-to-Region Binding (LOCKED)

```
DOMAIN_TRACK    PRIMARY REGION(S)       FAILOVER REGION(S)  NOTES
────────────────────────────────────────────────────────────────────────────────────────
arts            REGION-IN-1             REGION-SG-1         Latency-first; cost sensitivity permitted
commerce        REGION-IN-1             REGION-SG-1         Strong SLA; regulatory-compatible only
science         REGION-IN-1             REGION-SG-1         Stability over proximity; reproducible env
gd (all)        REGION-IN-1             REGION-SG-1         Realtime — MUST NOT cross regions normally
interview       REGION-IN-1             REGION-SG-1         Slot-bound — failover only pre-join
dojo_match      REGION-IN-1 (isolated)  REGION-SG-1         Dojo MUST use independent node pool
```

### 3.4 Undeclared Region Rule

```
Any region NOT listed in §3.1 is UNDECLARED.
Any attempt to route bridge traffic to an UNDECLARED region →
  → STOP ROUTING
  → EMIT region_undeclared_violation
  → BLOCK SESSION START
  → REPORT to Admin Governance Service

Auto-selected regions from cloud provider defaults = UNDECLARED → BLOCKED
```

---

## 4. BRIDGE NODE REGISTRY (LOCKED)

### 4.1 Node Types

```
NODE_TYPE           PURPOSE
────────────────────────────────────────────────────────────────────────────────────────
JITSI_NODE          Voice-only GD sessions (WebRTC SFU — Jitsi Videobridge + Jicofo)
LIVEKIT_NODE        Dojo match + Interview sessions (SFU — LiveKit)
COTURN_NODE         TURN/STUN relay for NAT traversal (per region — mandatory)
BRIDGE_GATEWAY      PSTN ingress/egress bridge gateway (receives PSTN calls, maps to SFU)
```

### 4.2 Node Health Schema

```sql
CREATE TABLE bridge_node_registry (
    node_id             UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    node_type           TEXT NOT NULL
                        CHECK (node_type IN ('jitsi','livekit','coturn','bridge_gateway')),
    region_id           TEXT NOT NULL,               -- FK → declared region (§3.1)
    domain_track        TEXT NOT NULL,               -- arts | commerce | science | all
    fqdn                TEXT NOT NULL UNIQUE,        -- e.g. jitsi-in1-a.ecoskiller.internal
    ip_address          INET NOT NULL,
    capacity_max        INTEGER NOT NULL,            -- Max concurrent PSTN sessions
    capacity_current    INTEGER NOT NULL DEFAULT 0,
    health_score        NUMERIC(3,2) NOT NULL        -- 0.00 – 1.00 (1.00 = fully healthy)
                        DEFAULT 1.00,
    health_status       TEXT NOT NULL DEFAULT 'healthy'
                        CHECK (health_status IN ('healthy','degraded','unreachable','draining')),
    last_health_check   TIMESTAMPTZ NOT NULL DEFAULT now(),
    is_active           BOOLEAN NOT NULL DEFAULT TRUE,
    created_at          TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at          TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE INDEX idx_bnr_region      ON bridge_node_registry(region_id);
CREATE INDEX idx_bnr_type_health ON bridge_node_registry(node_type, health_status, region_id);
CREATE INDEX idx_bnr_domain      ON bridge_node_registry(domain_track, region_id);
```

### 4.3 Node Health Check Contract

```
Health check interval:        15 seconds (mandatory — NON-CONFIGURABLE minimum)
Health check timeout:         5 seconds
Health check endpoint:        GET /health → { status, capacity_current, capacity_max, latency_ms }
health_score calculation:
  base_score = 1.00
  IF response_time > 150ms:         base_score -= 0.15
  IF response_time > 300ms:         base_score -= 0.30  (cumulative)
  IF capacity_used > 80%:           base_score -= 0.20
  IF capacity_used > 95%:           base_score -= 0.40  (cumulative)
  IF last_success > 30s ago:        health_status = 'degraded'
  IF last_success > 60s ago:        health_status = 'unreachable'
  IF draining flag set:             health_status = 'draining'

health_status = 'healthy':          health_score >= 0.70
health_status = 'degraded':         health_score >= 0.40 AND < 0.70
health_status = 'unreachable':      health_score < 0.40 OR no response
health_status = 'draining':         Manual admin flag — no new sessions admitted

Node selection eligibility:
  ONLY nodes with health_status = 'healthy' may accept new sessions.
  Degraded nodes: existing sessions continue, no new sessions.
  Unreachable nodes: existing sessions → migration attempt → fallback routing.
```

---

## 5. ROUTING DECISION ENGINE (DETERMINISTIC)

### 5.1 Routing Decision Record Schema

```sql
CREATE TABLE bridge_routing_decisions (
    decision_id         UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    call_id             UUID NOT NULL UNIQUE,           -- FK → call_session_map.call_id
    session_id          UUID NOT NULL,
    session_type        TEXT NOT NULL,
    tenant_id           UUID NOT NULL,
    domain_track        TEXT NOT NULL,
    user_id             UUID NOT NULL,
    caller_jurisdiction TEXT NOT NULL,                  -- ISO 3166-1 alpha-2 country
    selected_region_id  TEXT NOT NULL,                  -- Must be in declared registry
    selected_node_id    UUID NOT NULL,                  -- FK → bridge_node_registry
    selected_node_type  TEXT NOT NULL,
    routing_reason      TEXT NOT NULL,                  -- ENUM: proximity | compliance |
                                                        --       failover | tenant_binding |
                                                        --       manual_override
    failover_from       TEXT,                           -- Populated if this is a failover decision
    latency_to_node_ms  INTEGER,                        -- Measured RTT at routing time
    decision_at         TIMESTAMPTZ NOT NULL DEFAULT now(),
    decision_by         TEXT NOT NULL DEFAULT 'system', -- system | admin
    compliance_check    JSONB NOT NULL,                 -- { xbd_pass: bool, dpdp_pass: bool,
                                                        --   gdpr_pass: bool, minor_safe: bool }
    data_residency_ok   BOOLEAN NOT NULL,
    is_valid            BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE INDEX idx_brd_call_id    ON bridge_routing_decisions(call_id);
CREATE INDEX idx_brd_session_id ON bridge_routing_decisions(session_id);
CREATE INDEX idx_brd_region_id  ON bridge_routing_decisions(selected_region_id);
CREATE INDEX idx_brd_tenant_id  ON bridge_routing_decisions(tenant_id);

-- Immutable — no UPDATE, no DELETE after decision is recorded
REVOKE UPDATE ON bridge_routing_decisions FROM ecoskiller_app_role;
REVOKE DELETE ON bridge_routing_decisions FROM ecoskiller_app_role;
```

### 5.2 Routing Algorithm (LOCKED — NON-NEGOTIABLE ORDER)

```
FUNCTION select_bridge_node(call_id, session_id, session_type, tenant_id,
                            domain_track, caller_e164, user_id):

  STEP 1 — COMPLIANCE PRE-CHECK (ALWAYS FIRST)
  ─────────────────────────────────────────────
  caller_country = resolve_country_from_e164(caller_e164)   // E.164 → ISO 3166-1 country
  user_jurisdiction = tenant.declared_jurisdiction

  compliance_result = run_compliance_checks({
    xbd_check:      is_cross_border_transfer_permitted(caller_country, candidate_regions),
    dpdp_check:     is_india_dpdp_compliant(user_jurisdiction, candidate_regions),
    gdpr_check:     is_gdpr_compliant(user_jurisdiction, candidate_regions),
    minor_check:    IF user.is_minor → ONLY legally approved regions permitted,
    sanctioned:     is_jurisdiction_sanctioned(caller_country) → DENY if true
  })

  IF compliance_result.any_fail:
    → DENY ROUTING
    → EMIT bridge.routing.compliance_denied { reason, call_id }
    → RETURN { routed: false, reason: COMPLIANCE_FAILURE }

  STEP 2 — TENANT JURISDICTION BINDING
  ─────────────────────────────────────
  tenant_preferred_region = tenant.declared_primary_region
  IF tenant_preferred_region NOT IN region_registry:
    → DENY ROUTING
    → EMIT bridge.routing.undeclared_region
    → RETURN { routed: false, reason: UNDECLARED_REGION }

  eligible_regions = [tenant_preferred_region]

  STEP 3 — DOMAIN ISOLATION FILTER
  ─────────────────────────────────
  IF domain_track = dojo_match:
    eligible_nodes = bridge_node_registry WHERE
      domain_track IN ('dojo_match', 'all')
      AND region_id IN eligible_regions
      AND node_type = 'livekit'   // Dojo uses LiveKit only
      AND health_status = 'healthy'
      AND is_active = TRUE
  ELSE IF session_type IN ('gd', 'interview'):
    eligible_nodes = bridge_node_registry WHERE
      domain_track IN (domain_track, 'all')
      AND region_id IN eligible_regions
      AND node_type = resolve_node_type(session_type)  // gd → jitsi; interview → livekit
      AND health_status = 'healthy'
      AND is_active = TRUE

  IF eligible_nodes IS EMPTY:
    → GOTO STEP 5 (FAILOVER)

  STEP 4 — PROXIMITY + CAPACITY SELECTION (WITHIN ELIGIBLE)
  ──────────────────────────────────────────────────────────
  candidates = eligible_nodes filtered by:
    capacity_current < (capacity_max × 0.80)   // Never select >80% capacity node

  ranked = sort candidates by:
    1. latency_to_caller (ascending — lowest RTT first)
    2. capacity_used_pct (ascending — least loaded first)
    3. health_score (descending — healthiest first)

  selected_node = ranked[0]

  IF selected_node IS NULL:
    → GOTO STEP 5 (FAILOVER)

  STEP 5 — CONTROLLED FAILOVER (IF PRIMARY UNAVAILABLE)
  ──────────────────────────────────────────────────────
  failover_region = lookup_failover_region(tenant_preferred_region, domain_track)

  IF failover_region IS NULL OR failover_region NOT IN region_registry:
    → DENY ROUTING
    → EMIT bridge.routing.no_failover_available
    → RETURN { routed: false, reason: NO_FAILOVER_AVAILABLE }

  Re-run compliance_checks against failover_region:
  IF compliance_result.any_fail in failover_region:
    → DENY ROUTING — compliance forbids failover target
    → EMIT bridge.routing.failover_compliance_denied
    → RETURN { routed: false, reason: FAILOVER_COMPLIANCE_FAILURE }

  eligible_nodes = bridge_node_registry WHERE region_id = failover_region
                   AND same domain/type/health filters as STEP 3

  IF eligible_nodes IS EMPTY:
    → DENY ROUTING
    → EMIT bridge.routing.complete_outage { primary_region, failover_region }
    → RETURN { routed: false, reason: COMPLETE_REGIONAL_OUTAGE }

  selected_node = ranked eligible_nodes[0]
  routing_reason = 'failover'
  failover_from  = tenant_preferred_region

  STEP 6 — ROUTING DECISION RECORD (MANDATORY)
  ─────────────────────────────────────────────
  INSERT INTO bridge_routing_decisions:
    call_id, session_id, session_type, tenant_id, domain_track, user_id,
    caller_jurisdiction, selected_region_id, selected_node_id,
    selected_node_type, routing_reason, failover_from,
    latency_to_node_ms, compliance_check, data_residency_ok = TRUE

  IF INSERT fails:
    → ROLLBACK
    → DENY ROUTING
    → EMIT bridge.routing.decision_record_failure
    → RETURN { routed: false, reason: AUDIT_RECORD_FAILURE }

  STEP 7 — COTURN ALLOCATION (MANDATORY PER CALL)
  ─────────────────────────────────────────────────
  coturn_node = select_coturn_node(selected_region_id)
    -- MUST be in same region as selected SFU node
    -- Cross-region coturn allocation = FORBIDDEN

  IF coturn_node IS NULL:
    → DENY ROUTING
    → EMIT bridge.routing.coturn_unavailable
    → RETURN { routed: false, reason: COTURN_UNAVAILABLE }

  → RETURN {
      routed: true,
      call_id, selected_node_id, selected_region_id,
      sfu_endpoint: selected_node.fqdn,
      coturn_endpoint: coturn_node.fqdn,
      routing_decision_id,
      routing_reason
    }
```

### 5.3 Routing Constraints (ABSOLUTE)

```
CONSTRAINT                                          ENFORCEMENT
──────────────────────────────────────────────────────────────────────────────────────────
Compliance check is always STEP 1                   No routing before XBD/GDPR/DPDP check
Tenant jurisdiction binding is always STEP 2        No proximity override of jurisdiction
Dojo nodes never serve non-Dojo sessions            domain_track filter is hard
Jitsi nodes never serve LiveKit sessions            node_type filter is hard
coturn node must be in same region as SFU           Cross-region NAT traversal forbidden
Routing decision record MUST succeed before bridge  No record = no routing = STOP
Automatic global active-active                      FORBIDDEN unless explicitly declared
Silent failover to undeclared region                FORBIDDEN
Minor user routed to non-approved region            FORBIDDEN
```

---

## 6. LATENCY CONTRACTS BY REGION (LOCKED)

### 6.1 Per-Region SLA Targets

```
REGION_ID       TARGET LATENCY      AUDIO LATENCY    ROOM JOIN    FAILOVER RTO
                (API P99)           (P90)            TIME (P99)   (max)
────────────────────────────────────────────────────────────────────────────────────────
REGION-IN-1     ≤ 250ms             ≤ 150ms          ≤ 2s         ≤ 7 minutes
REGION-SG-1     ≤ 250ms             ≤ 150ms          ≤ 2s         ≤ 7 minutes
REGION-EU-1     ≤ 250ms             ≤ 150ms          ≤ 2s         ≤ 7 minutes
REGION-US-1     ≤ 250ms             ≤ 150ms          ≤ 2s         ≤ 7 minutes
REGION-ME-1     ≤ 300ms (declared)  ≤ 200ms          ≤ 3s         ≤ 10 minutes
```

### 6.2 Cross-Region Failover Latency

```
Cross-region failover P99: ≤ 500ms (per LATENCY-J law)
Global replication lag: ≤ 1s
Region failover time: ≤ 7 minutes (per Government SLA)
Bridge session not yet started (pending):  failover transparent to user
Bridge session in-progress (connected):    terminate + re-invite on failover target
```

### 6.3 In-Progress Session Failover Protocol

```
Mid-session primary region failure:

  1. Detect via health_check: health_status → 'unreachable' (60s threshold)
  2. Emit bridge.region.primary_failure event
  3. For each active call_session_map in affected region:
     a. Classify as: pre-join (pending) OR in-progress (connected/speaking)
  4. Pre-join calls:
     → Re-run routing algorithm with failover_region
     → New routing_decision_record created
     → Transparent reroute
  5. In-progress calls:
     → CANNOT transparently migrate (PSTN call state)
     → Send platform notification: session interrupted
     → Re-invite call to failover room (new bridge_token issued)
     → Original call_session_map: terminated (reason = failover)
     → New call_session_map: created in failover region
     → Score data preserved in original record (not lost)
  6. Human notification:
     → Admin Governance: region_failover_activated alert
     → Platform ops on-call: immediate page (per on-call role rules)
  7. Automatic global promotion without human or policy approval: FORBIDDEN
```

---

## 7. DATA RESIDENCY ENFORCEMENT (XBD COMPLIANCE)

### 7.1 Routing-Level Residency Rules

```
RULE                                        ENFORCEMENT
──────────────────────────────────────────────────────────────────────────────────────────
PSTN call audio never persists              No recording in bridge routing layer
call_session_map stored in primary region   Replication only to legally allowed regions
bridge_routing_decisions stored locally     Per-region, not globally replicated by default
Minor user data stays in approved region    check minor_check in compliance_result
India user (DPDP):                          Data must remain in REGION-IN-1 by default
EU user (GDPR):                             Data must remain in REGION-EU-1
Failover data written only to              Failover target must pass compliance check
approved failover region                    before any write occurs
Cross-border replay of session recordings: Only if explicitly permitted (XBD-G)
```

### 7.2 Residency Audit Trail

```
Every routing decision logs:
  source_region:        caller's inferred jurisdiction
  destination_region:   selected_region_id
  data_category:        pstn_bridge_metadata
  legal_basis:          platform_terms | scc | data_localization
  compliance_check:     full JSON result from STEP 1
  data_residency_ok:    boolean — must be TRUE to proceed

Residency audit logs → MinIO (WORM archive, region-local storage)
Retention: 7 years
Cross-border audit log access requires explicit admin governance approval
```

---

## 8. NODE LIFECYCLE MANAGEMENT

### 8.1 Node States

```
STATE           DESCRIPTION                             NEW SESSIONS    EXISTING SESSIONS
────────────────────────────────────────────────────────────────────────────────────────────
healthy         Fully operational                       PERMITTED       CONTINUED
degraded        Responding slowly / partially loaded    DENIED          CONTINUED
unreachable     Not responding to health checks         DENIED          MIGRATE ATTEMPT
draining        Admin-flagged for maintenance           DENIED          GRACEFUL DRAIN
decommissioned  Permanently removed from pool           DENIED          TERMINATED
```

### 8.2 Node Drain Protocol

```
Drain initiated by: Admin only (no automated drain without human approval)

Steps:
  1. Admin sets health_status = 'draining' in bridge_node_registry
  2. No new sessions routed to this node
  3. Existing sessions: grace period = 30 minutes (GD max duration)
  4. After grace period:
     a. Sessions still active → force terminate + re-invite
     b. Emit bridge.node.drain_complete
  5. Node is_active = FALSE after drain completes
  6. Routing algorithm excludes node permanently
```

### 8.3 Capacity Scaling Rules

```
Scale-out trigger:     Average capacity_used_pct > 70% across region for > 5 minutes
Scale-in trigger:      Average capacity_used_pct < 30% across region for > 30 minutes
Scale-out action:      Add node to bridge_node_registry (Terraform + k8s manifest)
Scale-in action:       Drain → decommission (human approval required for scale-in)
Max nodes per region:  Declared in Terraform variable (no unbounded scaling)
Cross-region scale-out: FORBIDDEN — capacity must be added to the correct region
```

---

## 9. DOJO BRIDGE ISOLATION (HARD ENFORCED)

```
ISOLATION RULE                                  ENFORCEMENT
──────────────────────────────────────────────────────────────────────────────────────────
Dojo nodes are separate k8s node pools          dojo nodeSelector label required
Dojo nodes are separate bridge_node_registry    domain_track = 'dojo_match' exclusively
Dojo bridge does not share SFU with GD          No node serves both dojo_match and gd
Dojo bridge does not share coturn pool with GD  Separate coturn allocation per domain
Dojo multi-region traffic MUST NOT cross        Realtime Dojo sessions are region-local
Dojo failover path is independent               Dojo failover declared separately from Core
Arts/Commerce/Science Dojo are on same          Separated by k8s namespace, not node pool
physical nodes but isolated by namespace        (unless load demands separation)
Arts failure MUST NOT affect Commerce SLA       Domain circuit breakers per Dojo domain
```

---

## 10. PSTN BRIDGE ROUTING API CONTRACT

### 10.1 Routing Endpoints

```
POST /bridge/routing/select
  Auth:    Internal mTLS (pstn-bridge-service → bridge-router)
  Body:    { call_id, session_id, session_type, tenant_id,
             domain_track, caller_e164, user_id }
  Returns: { routed: bool, sfu_endpoint: string, coturn_endpoint: string,
             region_id: string, routing_decision_id: uuid,
             routing_reason: string } | { routed: false, reason: string }
  Latency: ≤ 25ms P99 (routing decision must be fast)

GET /bridge/routing/status/{call_id}
  Auth:    Internal mTLS
  Returns: { routing_decision_id, region_id, node_id, call_state, failover: bool }

POST /bridge/routing/failover
  Auth:    Admin JWT (human-initiated failover) OR system-triggered (policy-approved)
  Body:    { region_id, failover_to_region_id, session_ids: [] | 'all',
             reason: string }
  Returns: { initiated: bool, affected_calls: int, failover_routing_ids: [] }

GET /bridge/nodes/health
  Auth:    Internal mTLS (Prometheus scrape + Admin console)
  Returns: { nodes: [{ node_id, region_id, health_status, health_score,
             capacity_current, capacity_max, latency_ms }] }
```

### 10.2 Kong Gateway Registration

```
/bridge/routing/select       → Internal only — NOT exposed through Kong
/bridge/routing/status/*     → Internal only — NOT exposed through Kong
/bridge/routing/failover     → Admin console only — Admin JWT required
/bridge/nodes/health         → Prometheus scrape + Admin console
```

---

## 11. TERRAFORM INFRASTRUCTURE CONTRACT (LOCKED)

### 11.1 Required Terraform Modules

```
MODULE                                  PURPOSE
──────────────────────────────────────────────────────────────────────────────────────────
modules/bridge/jitsi-node              Jitsi node deployment per region (parameterized)
modules/bridge/livekit-node            LiveKit node deployment per region (parameterized)
modules/bridge/coturn-node             coturn deployment per region (mandatory per SFU)
modules/bridge/bridge-gateway          PSTN bridge gateway per region
modules/bridge/health-checker          Health check cron + registry updater
modules/bridge/node-registry-seeder    Seeds bridge_node_registry on bootstrap
```

### 11.2 Terraform Variable Contract (Explicit — No Defaults)

```hcl
variable "bridge_regions" {
  description = "Explicitly declared bridge regions — no auto-selection"
  type = map(object({
    region_id         = string
    cloud_zone        = string
    primary_for       = list(string)   // domain_tracks served
    is_primary        = bool
    failover_for      = string         // region_id this is failover for (or null)
    jitsi_node_count  = number
    livekit_node_count= number
    coturn_node_count = number
    max_capacity      = number
    latency_budget_ms = number
    data_residency    = list(string)   // ["IN", "EU", ...] ISO 3166-1 codes
    dojo_isolated     = bool           // true = separate node pool for Dojo
    minor_approved    = bool           // true = legally cleared for minor data
  }))
}

# Required: no region may be absent from this map at deploy time
# Missing region declaration → Terraform validation error → BLOCK DEPLOYMENT
```

### 11.3 Kubernetes Namespace Strategy

```
NAMESPACE                       SERVICES
────────────────────────────────────────────────────────────────────────────────────────
realtime-bridge-{region_id}     pstn-bridge-gateway, bridge-router, health-checker
media-jitsi-{region_id}         jitsi-meet, jitsi-videobridge, jicofo
media-livekit-{region_id}       livekit-server
media-coturn-{region_id}        coturn relay
media-dojo-{region_id}          Dojo-specific LiveKit pool (isolated node pool)

No cross-namespace media calls permitted.
No shared services between realtime-bridge and media namespaces
  (bridge-router → SFU via service endpoint only, never direct pod).
```

---

## 12. ASYNC EVENT PIPELINE (KAFKA)

### 12.1 Required Kafka Events

```
EVENT TOPIC                               PRODUCER                  CONSUMERS
────────────────────────────────────────────────────────────────────────────────────────────
bridge.routing.selected                   BridgeRouter              Analytics · Admin Governance
bridge.routing.denied                     BridgeRouter              Analytics · Admin Governance ·
                                                                    Fraud Detection
bridge.routing.compliance_denied          BridgeRouter              Admin Governance · Wazuh SIEM
bridge.routing.failover_activated         BridgeRouter              Admin Governance · On-Call
bridge.routing.failover_denied            BridgeRouter              Admin Governance · Wazuh SIEM
bridge.routing.complete_outage            BridgeRouter              Admin Governance · On-Call ·
                                                                    Wazuh SIEM (P0)
bridge.routing.undeclared_region          BridgeRouter              Admin Governance · Wazuh SIEM
bridge.node.health_degraded               HealthChecker             Admin Governance · On-Call
bridge.node.health_unreachable            HealthChecker             Admin Governance · On-Call (P0)
bridge.node.drain_initiated               NodeLifecycleMgr          Admin Governance
bridge.node.drain_complete                NodeLifecycleMgr          Admin Governance · Analytics
bridge.node.capacity_threshold_breached   HealthChecker             Admin Governance · Autoscaler
bridge.region.primary_failure             HealthChecker             Admin Governance · On-Call (P0)
bridge.region.failover_complete           BridgeRouter              Admin Governance · Analytics
```

### 12.2 Event Schema Rules

```
All events carry:
  {
    event_id:       uuid,
    event_type:     string (from approved list above),
    call_id:        uuid | null,
    session_id:     uuid | null,
    region_id:      string,
    node_id:        uuid | null,
    tenant_id:      uuid | null,
    domain_track:   string | null,
    timestamp:      iso8601,
    payload:        {}
  }

caller_e164 / phone_e164 MUST NOT appear in any routing event payload.
IP addresses must be hashed (SHA-256) in events.
Violation → REPORT DATA_LEAK_IN_ROUTING_EVENT → STOP
```

---

## 13. OBSERVABILITY (MANDATORY)

### 13.1 Prometheus Metrics

```
METRIC                                              TYPE        LABELS
────────────────────────────────────────────────────────────────────────────────────────────
ecoskiller_bridge_routing_selected_total            counter     (region_id, session_type,
                                                                routing_reason, domain_track)
ecoskiller_bridge_routing_denied_total              counter     (reason, region_id, domain_track)
ecoskiller_bridge_routing_compliance_denied_total   counter     (check_failed, region_id)
ecoskiller_bridge_routing_failover_total            counter     (from_region, to_region,
                                                                domain_track)
ecoskiller_bridge_routing_latency_ms                histogram   (region_id, session_type)
ecoskiller_bridge_node_health_score                 gauge       (node_id, region_id, node_type)
ecoskiller_bridge_node_capacity_used_pct            gauge       (node_id, region_id, node_type)
ecoskiller_bridge_region_active_sessions            gauge       (region_id, domain_track)
ecoskiller_bridge_region_failover_rto_seconds       histogram   (from_region, to_region)
ecoskiller_bridge_routing_decision_latency_ms       histogram   (region_id)   // must be < 25ms
ecoskiller_bridge_complete_outage_total             counter     (region_id, domain_track)  // P0
ecoskiller_bridge_node_health_checks_total          counter     (node_id, result)
ecoskiller_bridge_coturn_allocation_failed_total    counter     (region_id)
```

### 13.2 Grafana Dashboard Panels

```
PANEL                                               ALERT THRESHOLD
────────────────────────────────────────────────────────────────────────────────────────────
Routing Success Rate by Region (live)               Alert if < 90% in any region
Routing Denial Rate by Reason (1h)                  Alert if > 5% in any region
Compliance Denial Rate (1h)                         Alert if > 0
Failover Events (24h)                               Alert if > 2 in any region per day
Complete Outage Events (all time)                   Alert IMMEDIATELY on any event (P0)
Node Health Score Heatmap (live)                    Alert if any node < 0.70
Node Capacity Used % by Region (live)               Alert if any region avg > 75%
Cross-Region Failover RTO Distribution (7d)         Alert if P99 > 7 minutes
Routing Decision Latency (P99) by Region            Alert if > 25ms
Active Sessions by Region + Domain (live)           Informational
Dojo Node Isolation Drift (daily)                   Alert if Dojo traffic detected on Core nodes
Region-Local Latency vs SLA Target (live)           Alert if P99 > declared target per region
```

### 13.3 Wazuh SIEM Rules

```
RULE                                        TRIGGER
────────────────────────────────────────────────────────────────────────────────────────────
bridge_routing_undeclared_region            bridge.routing.undeclared_region event
bridge_compliance_denial_burst             > 3 compliance_denied in 5 min from same tenant
bridge_complete_outage_alert               bridge.routing.complete_outage event (P0)
bridge_failover_unauthorized               bridge.routing.failover_activated without
                                           matching admin_approval record
bridge_dojo_isolation_breach               Dojo traffic detected on non-Dojo node
bridge_minor_region_violation              Minor user routed to non-approved region
```

### 13.4 OpenTelemetry Tracing

```
Trace spans required:
  bridge.routing.start         → start of routing algorithm
  bridge.compliance.check      → duration of XBD/GDPR/DPDP checks
  bridge.node.selection        → duration of node ranking and selection
  bridge.coturn.allocation     → coturn node selection
  bridge.decision.write        → routing decision record DB write
  bridge.routing.complete      → end of routing, sfu_endpoint returned

Cross-region correlation IDs must propagate through all bridge events.
Trace context must be injected into Kafka event headers.
```

---

## 14. CI/CD PIPELINE GATES

### 14.1 Required Tests

```
TEST FILE                                       COVERAGE
──────────────────────────────────────────────────────────────────────────────────────────
bridge_router_unit_test                         All 7 routing steps, all denial paths
compliance_checker_unit_test                    XBD, GDPR, DPDP, minor rules — 20+ cases
region_registry_unit_test                       Declared / undeclared region lookup
node_health_unit_test                           Health score calculation, status transitions
failover_selection_unit_test                    Primary failure → failover region selection
coturn_allocator_unit_test                      Same-region constraint, unavailability
routing_decision_recorder_unit_test             Insert success, insert failure → STOP
dojo_isolation_unit_test                        Dojo node filter, no cross-contamination
integration_test_routing_full_lifecycle         Full route → bridge → session → terminate
integration_test_failover_pre_join              Primary unavailable before join → failover
integration_test_failover_in_progress           Primary fails mid-session → re-invite
integration_test_compliance_denial_india_dpdp   India user routed only to REGION-IN-1
integration_test_compliance_denial_eu_gdpr      EU user routed only to REGION-EU-1
integration_test_minor_region_restriction       Minor user blocked from non-approved region
integration_test_undeclared_region_block        Routing to undeclared region → BLOCKED
integration_test_dojo_core_isolation            GD session cannot use Dojo node
integration_test_coturn_same_region             coturn allocation always in same region as SFU
integration_test_capacity_threshold_block       >80% node rejected from selection
```

### 14.2 CI Stage Declarations

```
STAGE                           GATE
──────────────────────────────────────────────────────────────────────────────────────────
region_registry_validator       All declared regions present in Terraform variables
terraform_plan_validation       No auto-selected regions; all node counts declared
contract_validator              Routing API schema in OpenAPI registry
unit_test_gate                  All unit tests pass — zero tolerance
integration_test_gate           All integration tests pass
compliance_check_gate           Compliance check latency < 5ms P99 (unit test)
routing_latency_gate            Routing decision end-to-end < 25ms P99 (benchmark)
kafka_schema_validation         All 14 routing event schemas in AsyncAPI registry
observability_smoke_test        All 13 Prometheus metrics scrape clean
security_scan_gate              No E.164 or IP in plaintext in events / logs
dojo_isolation_gate             No Dojo node serving non-Dojo traffic (integration test)
terraform_region_seal_check     bridge_regions variable fully populated — no null values

Any gate FAIL → CI STOP → NO ARTIFACT PRODUCED
```

---

## 15. CONTRACT GATE REGISTRATION

### 15.1 Required Input Gates

```
GATE                                SOURCE
──────────────────────────────────────────────────────────────────────────────────────────
call_session_map_ready              CALL_SESSION_MAPPING_AGENT v1.0
phone_pipeline_complete             PHONE_PIPELINE_COMPLETENESS_AGENT v1.0
session_management_ready            SESSION-COMP-v1
identity_ready                      Lane A Foundation
db_ready                            Lane B Data
event_schema_ready                  Lane A Foundation (R4)
media_stack_ready                   Jitsi + LiveKit + coturn deployed per region
terraform_bridge_modules_ready      Terraform bridge modules per region applied
region_registry_seeded              bridge_node_registry populated (all declared regions)
```

### 15.2 Contract Gates Produced on PASS

```
GATE                                CONSUMED BY
──────────────────────────────────────────────────────────────────────────────────────────
multi_region_bridge_routing_ready   CALL_SESSION_MAPPING_AGENT (routing selection path)
                                    GD Voice Orchestrator (PSTN participant routing)
                                    Dojo Match Engine (PSTN participant routing)
                                    Interview Service (PSTN participant routing)
bridge_node_registry_ready          Analytics Service · Admin Governance · On-Call Module
bridge_compliance_routing_ready     Platform Compliance Module · Legal & Document Service
bridge_observability_ready          Platform Observability Readiness Check
bridge_infra_region_complete        Infrastructure Readiness · Deployment Authorization
```

---

## 16. ANTIGRAVITY GENERATION CHECKLIST (LOCKED)

```
Antigravity MUST generate:

  ✔ V{n}__create_bridge_node_registry.sql           Flyway migration
  ✔ V{n+1}__create_bridge_routing_decisions.sql     Flyway migration (append-only)
  ✔ V{n+2}__bridge_rls_policies.sql                 Flyway migration
  ✔ V{n+3}__bridge_indexes.sql                      Flyway migration
  ✔ bridge-router/BridgeRoutingEngine               Module — 7-step algorithm
  ✔ bridge-router/ComplianceChecker                 Module — XBD, GDPR, DPDP, minor
  ✔ bridge-router/RegionRegistry                    Module — declared region lookup only
  ✔ bridge-router/NodeHealthChecker                 Module — 15s interval health checks
  ✔ bridge-router/NodeSelector                      Module — proximity + capacity ranking
  ✔ bridge-router/FailoverManager                   Module — controlled promotion only
  ✔ bridge-router/CoturnAllocator                   Module — same-region constraint
  ✔ bridge-router/RoutingDecisionRecorder           Module — immutable write
  ✔ bridge-router/DojoIsolationEnforcer             Module — hard node type filter
  ✔ bridge-router/KafkaEventEmitter                 Module — 14 event types
  ✔ bridge-router/NodeLifecycleManager              Module — drain, scale, decommission
  ✔ /bridge/routing/select endpoint                 Internal mTLS only
  ✔ /bridge/routing/status/{call_id} endpoint       Internal mTLS only
  ✔ /bridge/routing/failover endpoint               Admin console only
  ✔ /bridge/nodes/health endpoint                   Prometheus + Admin
  ✔ modules/bridge/jitsi-node/                      Terraform module (per region)
  ✔ modules/bridge/livekit-node/                    Terraform module (per region)
  ✔ modules/bridge/coturn-node/                     Terraform module (per region)
  ✔ modules/bridge/bridge-gateway/                  Terraform module (per region)
  ✔ modules/bridge/node-registry-seeder/            Terraform module (bootstrap)
  ✔ k8s/realtime-bridge-{region}/                   k8s Deployment + Service + ConfigMap
  ✔ k8s/media-jitsi-{region}/                       k8s Deployment (Jitsi per region)
  ✔ k8s/media-livekit-{region}/                     k8s Deployment (LiveKit per region)
  ✔ k8s/media-coturn-{region}/                      k8s Deployment (coturn per region)
  ✔ k8s/media-dojo-{region}/                        k8s Deployment (Dojo isolated pool)
  ✔ bridge_routing_kafka_schemas/                   14 event schemas (AsyncAPI)
  ✔ bridge_routing_opa_policies.rego                OPA: routing decision write access
  ✔ bridge_routing_wazuh_rules.xml                  6 Wazuh rules
  ✔ bridge_routing_grafana_dashboard.json           12 panels + alert rules
  ✔ bridge_routing_test_suite/                      Unit (9) + Integration (11) tests
  ✔ /config/bridge_regions.yaml                     Explicit region declarations (no defaults)

Antigravity MUST NOT generate:
  ✗ Any routing path that auto-selects region from provider defaults
  ✗ Any node serving both Dojo and non-Dojo traffic
  ✗ Any failover path to an undeclared region
  ✗ Any coturn allocated cross-region
  ✗ Any bridge routing decision without a written decision record
  ✗ Any routing event containing caller E.164 in payload
  ✗ Any media traffic routed through the API gateway
  ✗ Any global active-active routing without explicit declaration
  ✗ Any silent compliance bypass for routing convenience
  ✗ Any scale-in action without human approval
```

Absence of any generation item →
**STOP EXECUTION → REPORT MULTI_REGION_BRIDGE_ROUTING_AGENT_INCOMPLETE**

---

## ✅ FINAL ENFORCEMENT SUMMARY

```
MULTI_REGION_BRIDGE_ROUTING_AGENT
  ├── Domain:         PSTN & Bridge
  ├── Regions:        5 declared (IN-1, SG-1, EU-1, US-1, ME-1) + 2 DR regions
  ├── Node Types:     Jitsi · LiveKit · coturn · Bridge Gateway (per region, isolated)
  ├── Routing:        7-step algorithm — compliance-first, no defaults, no auto-selection
  ├── Failover:       Active-passive, controlled, human-approved promotion
  ├── Compliance:     XBD · GDPR · India DPDP · Minor protection — STEP 1 always
  ├── Dojo:           Hard-isolated node pools, independent failover, domain circuit breakers
  ├── Decision Audit: bridge_routing_decisions — immutable, WORM-archived, 7-year retention
  ├── Events:         14 Kafka topics · AsyncAPI schema enforced · no PII in payloads
  ├── Infra:          Terraform (6 modules) · k8s (per-region namespaces)
  ├── Tests:          9 unit + 11 integration · 12 CI gates
  ├── Observability:  13 Prometheus metrics · 12 Grafana panels · 6 Wazuh rules ·
  │                   OpenTelemetry spans (6 per routing call)
  ├── Input gates:    9 required
  ├── Output gates:   5 produced on full PASS
  └── Failure mode:   DENY → EMIT → RECORD → STOP — no silent routing, no defaults

REGIONS ARE CONTRACTS, NOT DEFAULTS.
LOCALITY IS A GOVERNANCE DECISION.

EXECUTION_ENGINE    = ANTIGRAVITY
CHANGE_POLICY       = APPEND_ONLY
READY_FOR           = ANTIGRAVITY_PRODUCTION
STATUS              = SEALED · LOCKED · GOVERNED · DETERMINISTIC
```

---

*Ecoskiller · MULTI_REGION_BRIDGE_ROUTING_AGENT · v1.0 · SEALED*
*Mutation requires version bump and Human declaration only.*
*Inherits from: SECTION RSX · SECTION MRX · XBD · LATENCY-J · R18*
*Depends on: CALL_SESSION_MAPPING_AGENT v1.0 · PHONE_PIPELINE_COMPLETENESS_AGENT v1.0*
