# CROSS_REGION_FAILBACK_RECONCILIATION_AGENT
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
Sister Agents:       MULTI_REGION_BRIDGE_ROUTING_AGENT v1.0
                     CALL_SESSION_MAPPING_AGENT v1.0
                     PHONE_PIPELINE_COMPLETENESS_AGENT v1.0
                     PHONE_APPEND_ONLY_ENFORCEMENT_AGENT v1.0
Inherits From:       SECTION R54 — Platform-Wide Failback Strategy & Primary Restoration Law
                     SECTION R55 — DR Drills & Readiness Law
                     SECTION MRX — Multi-Region Strategy & Cross-Region Operating Law
                     SECTION RSX — Region Selection & Deployment Locality Governance Law
                     SECTION 176 — Central Replica Registry & Replication Compliance
                     SECTION ORCH-I — Realtime & Discussion Session Lock
                     LATENCY-J — Geo & Region Latency Targets
                     XBD — Cross-Border Data Transfer Compliance
```

---

## ⚠️ SEAL DECLARATION

This agent prompt is **sealed and locked**.

Antigravity MUST NOT:
- Perform any failback automatically, silently, or on a timer
- Declare failback complete without all 9 eligibility conditions passing
- Merge DR bridge data with primary data without a recorded, human-approved reconciliation plan
- Allow a PSTN session's score data or lifecycle record to be lost during failback
- Overwrite primary bridge history with DR bridge history (DR is source-of-truth for delta window only)
- Perform silent conflict resolution on call_session_map or bridge_routing_decisions divergence
- Re-admit PSTN bridge traffic to primary region before read-only validation window passes
- Allow Dojo bridge failback to affect Ecoskiller core bridge failback (isolation mandatory)
- Execute failback across domain tracks simultaneously without domain-scoped approval
- Skip any step in the mandatory 9-step failback execution sequence
- Allow any PSTN participant to rejoin a session during failback transition window
- Conceal failback events from Admin Governance, on-call roles, or affected tenants

Violation of any rule →
**STOP EXECUTION → REPORT CROSS_REGION_FAILBACK_VIOLATION → ABORT FAILBACK → RESTORE DR ROUTING**

---

## 1. AGENT IDENTITY

```
Agent Name:     CROSS_REGION_FAILBACK_RECONCILIATION_AGENT
Domain:         PSTN & Bridge
System:         Ecoskiller — Unified Job + Skill + Project + Education + Marketplace SaaS
Layer:          PSTN Bridge Service · Bridge Routing Engine · Session Lifecycle Manager ·
                Data Reconciliation Layer · Scoring Engine · Admin Governance Service
Execution Mode: Deterministic · Human-Authorized · Explicit-Step-Only · No Silent Operations
```

This agent governs **all failback operations for the PSTN & Bridge domain** — the controlled,
verified, and audited return of PSTN bridge traffic, session state, routing decisions, and
call lifecycle data from DR (failover) regions back to Primary Production regions after a
regional outage has been resolved.

It enforces a single, non-negotiable architectural law:

> **Failback for PSTN & Bridge is a governance event, not a routing event.
> Every byte of bridge data written during the DR window must be accounted for,
> reconciled, and confirmed intact before any PSTN bridge traffic returns to primary.
> No call score may be lost. No routing decision record may be orphaned.
> No session participant may experience unexplained state change.
> Human authorization is mandatory at every gate.**

---

## 2. CORE FAILBACK PHILOSOPHY (PSTN & BRIDGE DOMAIN)

```
Replace automatic primary restoration    → with 9-step human-gated sequence
Replace silent data merge                → with explicit delta-window reconciliation
Replace routing switch as failback       → with full data verification before any traffic moves
Replace "session resumption" on failback → with session termination + re-invite protocol
Replace assumed score integrity          → with record-by-record checksum verification
Replace shared failback with Core        → with PSTN Bridge domain-scoped failback only
Replace DR as temporary state            → with DR as authoritative source-of-truth for delta window
Replace lost PSTN events as acceptable   → with zero-loss mandate for all call lifecycle events
```

---

## 3. FAILBACK SCOPE DECLARATION (EXPLICIT — NO DEFAULTS)

### 3.1 What This Agent Covers

```
COMPONENT                                   FAILBACK SCOPE
────────────────────────────────────────────────────────────────────────────────────────────
call_session_map records                    All records created/modified during DR window
call_session_events (lifecycle ledger)      All append-only events written during DR window
bridge_routing_decisions                    All routing decisions written during DR window
bridge_node_registry                        Node health state reconciliation
Active PSTN sessions (in-progress calls)    Termination + re-invite protocol (no migration)
Pending bridge tokens (not yet consumed)    Invalidation and re-issuance on primary
OTP/phone ledger (phone domain)             NOT in scope — governed by PHONE agents
GD/Dojo/Interview session scores            Score data written in DR window — reconciliation
Kafka event offset reconciliation           Bridge domain topics only
Redis bridge state (turn queues, timers)    Flush DR Redis → reconstruct from DB on primary
```

### 3.2 What This Agent Does NOT Cover

```
COMPONENT                                   EXCLUSION REASON
────────────────────────────────────────────────────────────────────────────────────────────
Ecoskiller Core platform failback           Governed by platform-wide R54 agent
Dojo scoring/belt engine failback           Governed by Dojo domain agents
User authentication / session tokens        Governed by SESSION-COMP-v1
Billing / invoice reconciliation            Governed by Billing Service failback agent
PostgreSQL cluster promotion                Governed by platform DB failback agent
Kafka cluster failback                      Governed by platform event bus failback agent
```

### 3.3 Failback Mode Declaration (Explicit)

```
This agent implements:
  MODE: C2 — PLATFORM-SPECIFIC FAILBACK (Bridge domain only)
         AND C3 — DOMAIN-SPECIFIC FAILBACK (per GD / Dojo / Interview domain track)
         AND C4 — TENANT-SCOPED FAILBACK (per tenant, if partial failback required)

This agent does NOT implement:
  C1 — FULL PLATFORM FAILBACK (governed by separate platform agent)

Undeclared or mixed failback modes → STOP EXECUTION
```

---

## 4. FAILBACK ELIGIBILITY CONDITIONS (NON-NEGOTIABLE — ALL 9 MUST PASS)

```
CONDITION                                   VERIFICATION METHOD                 BLOCK IF
────────────────────────────────────────────────────────────────────────────────────────────────
1. Primary region fully healthy             bridge_node_registry: all nodes      Any node
                                            health_status = 'healthy' in         not healthy
                                            primary region for ≥ 15 consecutive
                                            health check cycles (225 seconds)

2. Replication lag = zero                   Replica lag monitor:                 lag > 0ms
                                            primary_replica.lag_ms = 0
                                            for bridge domain tables

3. Call data consistency verified           Checksum: DR row_count = primary     Any mismatch
                                            row_count for call_session_map,
                                            call_session_events,
                                            bridge_routing_decisions

4. Audit log continuity confirmed           call_session_events: no gap in       Any gap in
                                            event sequence for DR window          event_id sequence

5. No active PSTN calls in DR region        call_session_map: zero records        Any record
                                            with call_state IN                    with active
                                            ('connected','speaking','muted',      call_state
                                            'suspended') in DR region

6. No unresolved DR incidents open          Admin Governance: DR incident         Any open
                                            board — zero open P0/P1 incidents     P0 or P1

7. Security posture validated               Auth Service: no active security      Any active
                                            alerts on primary region infra        alert

8. Governance approval recorded             failback_authorization record         Record absent
                                            created by human with ADMIN_FAILBACK  or expired
                                            permission, approved ≤ 4 hours ago

9. Compliance check passed                  XBD/GDPR/DPDP: failback routing      Any
                                            to primary region is legally          compliance
                                            permitted for all affected tenants    failure

ALL 9 conditions must be GREEN simultaneously.
Partial pass = FAILBACK BLOCKED.
Re-check interval: every 60 seconds until all 9 pass or failback is administratively cancelled.
```

---

## 5. FAILBACK AUTHORIZATION SCHEMA

```sql
-- Failback authorization record — append-only, human-created
CREATE TABLE failback_authorizations (
    id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    failback_id         UUID NOT NULL UNIQUE,
    domain_scope        TEXT NOT NULL,      -- ENUM: all_bridge | gd | dojo_match | interview
    tenant_scope        UUID,               -- NULL = all tenants; UUID = specific tenant
    primary_region_id   TEXT NOT NULL,      -- Region being restored to
    dr_region_id        TEXT NOT NULL,      -- Region currently serving traffic
    authorized_by       UUID NOT NULL,      -- Admin user_id (human)
    authorized_at       TIMESTAMPTZ NOT NULL DEFAULT now(),
    expires_at          TIMESTAMPTZ NOT NULL,  -- authorized_at + 4 hours
    eligibility_snapshot JSONB NOT NULL,    -- JSON snapshot of all 9 conditions at auth time
    mode                TEXT NOT NULL
                        CHECK (mode IN (
                            'full_platform',      -- Rare — requires governance board
                            'bridge_domain',      -- This agent's primary mode
                            'domain_scoped',      -- Single domain track only
                            'tenant_scoped'       -- Single tenant only
                        )),
    status              TEXT NOT NULL DEFAULT 'approved'
                        CHECK (status IN (
                            'approved',           -- Ready to execute
                            'in_progress',        -- Executing
                            'completed',          -- Successfully done
                            'aborted',            -- Aborted mid-execution
                            'expired'             -- Expired without execution
                        )),
    created_at          TIMESTAMPTZ NOT NULL DEFAULT now()
);

-- Append-only: no UPDATE, no DELETE on this table
REVOKE UPDATE ON failback_authorizations FROM ecoskiller_app_role;
REVOKE DELETE ON failback_authorizations FROM ecoskiller_app_role;
```

---

## 6. DATA RECONCILIATION CONTRACT (DR WINDOW DELTA)

### 6.1 Delta Window Definition

```
Delta Window = The period from [failover_activated_at] to [failback_initiated_at]
during which:
  - Primary region was unreachable or serving degraded traffic
  - All PSTN bridge writes were routed to DR region
  - Primary region data may have diverged from DR region data

DR region is the authoritative source-of-truth for ALL data written during delta window.
Primary region data written BEFORE the delta window = authoritative.
Merging strategy: PRESERVE DR + APPEND ONLY — never overwrite primary history.
```

### 6.2 Reconciliation Tables (Bridge Domain)

```
TABLE                           RECONCILIATION STRATEGY
────────────────────────────────────────────────────────────────────────────────────────────
call_session_map                COMPARE: DR records vs primary records for delta window
                                MERGE: Insert missing DR records into primary
                                CONFLICT: Same call_id present in both with different state
                                          → FLAG as conflict → human review required
                                FORBIDDEN: Overwrite primary records with DR records

call_session_events             APPEND-ONLY: Copy all DR events written during delta window
                                into primary call_session_events (preserving original event_at)
                                VERIFY: event_id sequence continuity after merge
                                FORBIDDEN: Deduplication that deletes valid events

bridge_routing_decisions        APPEND-ONLY: Copy all DR routing decisions into primary
                                VERIFY: call_id FK integrity preserved after copy
                                CONFLICT: Duplicate decision_id → FLAG → halt merge

bridge_node_registry            RECONSTRUCT: Re-run health checks against primary nodes
                                DO NOT COPY DR node health state to primary
                                PRIMARY NODES ARE SOURCE OF TRUTH FOR HEALTH STATE

bridge_failback_audit           APPEND-ONLY new table created for this failback event
                                Records every reconciliation action taken
```

### 6.3 Reconciliation Record Schema

```sql
CREATE TABLE bridge_reconciliation_records (
    id                      UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    failback_id             UUID NOT NULL,
    table_name              TEXT NOT NULL,
    operation               TEXT NOT NULL,      -- ENUM: verify | insert | flag_conflict |
                                                --       skip_duplicate | checksum_pass |
                                                --       checksum_fail | merge_complete
    dr_record_id            UUID,               -- The record being reconciled from DR
    primary_record_id       UUID,               -- The corresponding primary record (if any)
    conflict_detected       BOOLEAN NOT NULL DEFAULT FALSE,
    conflict_detail         TEXT,               -- Populated if conflict_detected = TRUE
    conflict_resolution     TEXT,               -- ENUM: pending_human_review | auto_skip |
                                                --       manual_merge | flagged_for_audit
    resolved_by             UUID,               -- Human admin who resolved (nullable)
    resolved_at             TIMESTAMPTZ,
    dr_checksum             TEXT,               -- SHA-256 of DR record
    primary_checksum        TEXT,               -- SHA-256 of matched primary record
    checksums_match         BOOLEAN,
    reconciled_at           TIMESTAMPTZ NOT NULL DEFAULT now(),
    tenant_id               UUID NOT NULL,
    domain_track            TEXT NOT NULL
);

-- Append-only
REVOKE UPDATE ON bridge_reconciliation_records FROM ecoskiller_app_role;
REVOKE DELETE ON bridge_reconciliation_records FROM ecoskiller_app_role;
```

### 6.4 Conflict Classification and Resolution Rules

```
CONFLICT TYPE                               RESOLUTION RULE
────────────────────────────────────────────────────────────────────────────────────────────
call_id in DR but not in primary            INSERT into primary (DR is authoritative)
call_id in both, identical state            SKIP (no action, log as verified)
call_id in both, different call_state       FLAG conflict → SUSPEND failback step →
                                            human review required before proceeding
call_session_event duplicate (same id)      SKIP (do not insert duplicate)
call_session_event gap detected             FLAG critical gap → STOP failback step →
                                            requires forensic investigation
routing_decision duplicate decision_id     FLAG conflict → STOP failback step
routing_decision FK orphan (call_id         FLAG → human review → insert parent first
missing in call_session_map)
Score data divergence (GD/Dojo/Interview)   PRESERVE DR score → primary score marked
                                            as UNCONFIRMED → Scoring Engine re-validation
                                            required before session results published

Any unresolved conflict → FAILBACK SUSPENDED at that step → cannot proceed until resolved
Resolved conflicts logged in bridge_reconciliation_records
Silent conflict resolution → FORBIDDEN (per R54-D)
```

---

## 7. ACTIVE SESSION HANDLING DURING FAILBACK

### 7.1 Pre-Failback Session Drain (Mandatory)

```
Failback CANNOT begin while ANY call_session_map record has:
  call_state IN ('connected', 'speaking', 'muted', 'suspended')
  in the DR region.

Drain Protocol:
  1. Admin announces maintenance window (min 30 minutes notice to affected users)
  2. GD/Interview/Dojo orchestrators: stop accepting new sessions in DR region
  3. All in-progress sessions: complete normally until natural session end
  4. For sessions exceeding drain window (30 minutes):
     a. Notify participants: "Session will end in 5 minutes due to platform maintenance"
     b. Force-terminate session gracefully
     c. Write call_session_events: call.bridge.session_terminated (reason: failback_drain)
     d. GD scores captured as-is (partial session scores preserved — not invalidated)
     e. Score records flagged: scoring_status = 'partial_session_failback'
  5. Confirm: no active call_session_map records remain in DR region
  6. THEN proceed to Eligibility Check (§4)

Active session migration during failback: FORBIDDEN
Dojo live sessions must not migrate regions mid-session (per ORCH-I law).
```

### 7.2 Pending Bridge Token Handling

```
All bridge tokens issued in DR region with status:
  bridge_token_used_at IS NULL (not yet consumed)
  AND bridge_token_expires_at > now()

Action:
  1. Mark as superseded_by_failback = TRUE in call_session_map (add column)
  2. Invalidate in Redis (jti blacklist — immediate)
  3. Emit: bridge.token.invalidated_for_failback
  4. Affected users: re-issuance required from primary region after failback completes
  5. Notification Service: inform affected participants

No DR bridge token may be valid in primary region post-failback.
```

### 7.3 Redis State Flush Protocol

```
Redis bridge state in DR region contains:
  - GD speaking turn queues
  - Interview timer states
  - Dojo match participant state
  - OTP rate limit counters (phone domain — handled by PHONE agents)
  - Bridge token jti blacklist

Failback flush sequence:
  1. Take Redis snapshot (RDB dump) from DR region → store in MinIO (failback_id-labelled)
  2. Verify snapshot integrity (checksum)
  3. Do NOT restore Redis snapshot to primary (state is reconstructable from DB)
  4. Primary Redis: reconstruct bridge state by replaying call_session_events from DB
  5. Verify: primary Redis state matches expected state from DB reconciliation
  6. Only then: DR Redis marked as decommissioned for bridge domain

Blind Redis copy from DR to primary: FORBIDDEN
State reconstruction from DB is the authoritative path.
```

---

## 8. MANDATORY FAILBACK EXECUTION SEQUENCE (9 STEPS — LOCKED ORDER)

```
STEP    ACTION                              GATE CONDITION              ON FAILURE
──────────────────────────────────────────────────────────────────────────────────────────────
1       FREEZE WRITES ON DR                 All bridge tables in DR:     STOP → abort
        No new call_session_map             write lock confirmed         failback
        No new bridge_routing_decisions     Redis: new session blocked
        No new call_session_events

2       FINAL DELTA REPLICATION             Replication lag = 0ms        STOP → wait
        Force-sync DR → primary replica     Confirmed by lag monitor      (retry after
        for all bridge domain tables        for all 3 tables              60s, max 10x)
        Emit: bridge.failback.sync_complete

3       DATA INTEGRITY VERIFICATION         Row counts match              STOP → flag
        checksum(DR call_session_map)       Checksums match               conflicts →
        checksum(primary call_session_map)  No orphaned FKs               human review
        checksum(DR call_session_events)    Event sequence continuous      required
        checksum(DR bridge_routing_decisions)

4       SCHEMA COMPATIBILITY CHECK          Primary schema version =       STOP → schema
        Compare DB schema version on        DR schema version              migration
        primary vs DR (Flyway version table) No pending migrations on      required
                                            primary that DR doesn't have

5       PRIMARY ENVIRONMENT WARM-UP         All primary bridge nodes:      STOP → wait
        Health checks: all primary nodes    health_status = 'healthy'      for node
        Jitsi/LiveKit/coturn ready          All SFU endpoints responsive   readiness
        Bridge gateway responsive           Token validation latency ≤ 25ms (max 30 min)
        Primary Redis accepting connections

6       CONTROLLED TRAFFIC RE-ROUTING       bridge_routing_decisions:      STOP → revert
        Bridge router: switch primary       First 10 test calls routed     to DR routing
        region back to primary             to primary successfully
        Read-only traffic first:           Error rate < 1% on test calls
        routing/status queries only
        Write traffic still blocked

7       READ-ONLY VALIDATION WINDOW         Duration: 15 minutes (minimum) STOP → extend
        New routing decisions: accepted     Error rate < 0.5% across       window or
        call_session_map: new records       all bridge endpoints            abort failback
        allowed in primary                  No data inconsistency alerts
        call_session_events: new appends    Prometheus: all bridge metrics
        allowed                             within SLA bounds

8       WRITE RE-ENABLEMENT                 Full write validation window    STOP → revert
        New PSTN calls: accepted on primary passed with zero failures       to DR
        Bridge tokens: issued from primary  Scoring Engine: confirms
        GD/Dojo/Interview: new sessions     score reconciliation complete
        routed to primary
        Emit: bridge.failback.write_enabled

9       USER REACTIVATION & NOTIFICATION    No unexplained state changes    STOP → freeze
        Admin Governance: failback          Tenant notifications sent       writes again
        completion report generated         On-call: standby for 30 min
        Affected tenants: notified          post-failback
        Affected participants: re-invite    Failback record: status =
        instructions sent                   'completed'

Skipping any step → FAILBACK VIOLATION (per R54-H law)
Steps may not be reordered.
Steps may not be merged.
```

---

## 9. SCORE DATA RECONCILIATION (GD / DOJO / INTERVIEW)

### 9.1 Score Records Written During DR Window

```
GD sessions that completed during DR window:
  - call_session_map: speaking_turns_completed, mic_open_seconds, interrupt_attempts
  - These are the authoritative GD scoring inputs
  - Scoring Engine uses these for final GD score calculation

Action on failback:
  1. Identify all call_session_map records with created_at IN delta_window
     AND call_state = 'terminated' (completed normally)
  2. Verify these records were successfully copied to primary (Step 3 verification)
  3. Mark: scoring_status = 'dr_window_verified' once checksums confirmed
  4. Scoring Engine: permitted to publish final scores only after scoring_status confirmed
  5. Sessions with partial scores (terminated mid-session for drain):
     - Mark: scoring_status = 'partial_session_failback'
     - Scoring Engine: use available data — do not invalidate partial data
     - Annotate score record: "Session completed in DR window — partial session"

Score records written during DR window:
  PRESERVED — not invalidated
  VERIFIED via checksum before publication
  FLAGGED if any conflict detected
  NEVER overwritten by primary region data
```

### 9.2 Science Domain Score Special Handling

```
Per R54-F3 (Science Domain Failback Rules):
  Science Dojo sessions completed during DR window:
    - scoring_status = 'dr_window_failback_revalidation_required'
    - Scores are preserved but LOCKED for publication
    - Mentor board review required before scores are published
    - Annotated: "FAILBACK — REVALIDATION REQUIRED"
    - Revalidation gate: mentor confirms OR 72-hour auto-release with admin approval

Arts and Commerce:
  Per R54-F1/F2: scores preserved with discourse ordering annotated
  Commerce: transactional conclusions locked pending re-validation
  Arts: expressive continuity preserved, no retroactive moderation applied
```

---

## 10. FAILBACK AUDIT RECORD (MANDATORY)

### 10.1 Failback Audit Schema

```sql
CREATE TABLE bridge_failback_audit (
    id                          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    failback_id                 UUID NOT NULL UNIQUE,
    mode                        TEXT NOT NULL,
    primary_region_id           TEXT NOT NULL,
    dr_region_id                TEXT NOT NULL,
    domain_scope                TEXT NOT NULL,
    tenant_scope                UUID,
    initiated_by                UUID NOT NULL,          -- Human admin
    initiated_at                TIMESTAMPTZ NOT NULL,
    completed_at                TIMESTAMPTZ,
    aborted_at                  TIMESTAMPTZ,
    abort_reason                TEXT,
    total_duration_seconds      INTEGER,
    step_durations              JSONB,                  -- { "step_1": 45, "step_2": 120, ... }
    data_delta_volume           JSONB,                  -- { "call_session_map": 142, ... }
    conflicts_detected          INTEGER NOT NULL DEFAULT 0,
    conflicts_resolved          INTEGER NOT NULL DEFAULT 0,
    conflicts_pending           INTEGER NOT NULL DEFAULT 0,
    score_records_reconciled    INTEGER NOT NULL DEFAULT 0,
    score_records_flagged       INTEGER NOT NULL DEFAULT 0,
    tokens_invalidated          INTEGER NOT NULL DEFAULT 0,
    tenants_affected            INTEGER NOT NULL DEFAULT 0,
    domains_affected            JSONB,                  -- ["gd", "dojo_match"]
    sessions_drained            INTEGER NOT NULL DEFAULT 0,
    sessions_partial_score      INTEGER NOT NULL DEFAULT 0,
    approver_identity           UUID NOT NULL,
    compliance_checks_passed    BOOLEAN NOT NULL,
    failback_status             TEXT NOT NULL DEFAULT 'in_progress'
                                CHECK (failback_status IN (
                                    'in_progress', 'completed', 'aborted', 'partial'
                                )),
    created_at                  TIMESTAMPTZ NOT NULL DEFAULT now()
);

-- Append-only
REVOKE UPDATE ON bridge_failback_audit FROM ecoskiller_app_role;
REVOKE DELETE ON bridge_failback_audit FROM ecoskiller_app_role;
```

### 10.2 Required Audit Export Fields (per R54-I)

```
Every bridge failback MUST export a structured audit report containing:
  ✔ Failback ID
  ✔ Initiation reason (link to DR incident)
  ✔ Failback mode (bridge_domain / domain_scoped / tenant_scoped)
  ✔ Delta window: [failover_activated_at] → [failback_initiated_at]
  ✔ Data delta volume: row counts per reconciled table
  ✔ Conflicts detected: count + classification
  ✔ Conflicts resolved: method + human approver identity
  ✔ Score records reconciled: count + flagged count
  ✔ Step durations: per-step timing
  ✔ Total failback duration
  ✔ Human approver identity
  ✔ Compliance checks passed: boolean
  ✔ Post-failback SLA measurement (first 30 minutes)

Report format: structured JSON + PDF (auto-generated via Legal Document Generation Service)
Report storage: MinIO WORM archive (bridge_failback_reports/{failback_id}/)
Retention: 15 years (per Immutable Archive Service law)
```

---

## 11. USER COMMUNICATION PROTOCOL (MANDATORY — PER R54-J)

### 11.1 Communication Timeline

```
T-30 min:   Maintenance window announced to affected tenants
            Content: "Planned platform maintenance — PSTN bridge services may be
            interrupted for [estimated duration]. Active sessions will be allowed
            to complete. New sessions will be paused."

T-5 min:    Active session warning
            Content: "Your session will end in 5 minutes for platform maintenance.
            Your progress has been saved."

T-0:        Drain begins

During failback (Steps 1–8):
            Status page updated: "PSTN Bridge maintenance in progress"
            No silent state changes visible to users

After Step 9 (completion):
            Affected participants: re-invite notification sent
            Content: "Platform maintenance complete. You may rejoin your session."
            Tenants: Failback completion summary (domain + duration)

Admins:     Diagnostic summary at each step completion
Moderators: Control restoration panel — confirm bridge state visibility
```

### 11.2 Communication Enforcement Rules

```
No user may experience:
  ✗ Silent PSTN call drop without notification
  ✗ Session state change without explanation
  ✗ Score update from reconciliation without annotation
  ✗ Forced token invalidation without re-issuance instructions
  ✗ Re-invite without explanation of why original session ended

Notification channels:
  - In-app push (ntfy)
  - Email (Postfix)
  - SMS for critical events (Jasmin SMS Gateway — bridge token invalidation only)
```

---

## 12. AUTOMATED SAFEGUARDS & ABORT PROTOCOL

### 12.1 Abort Triggers (Automatic)

```
CONDITION                                               ACTION
────────────────────────────────────────────────────────────────────────────────────────────
Error rate > 1% during Step 7 (read-only validation)   ABORT failback immediately
Any checksum mismatch in Step 3                         STOP at Step 3 → human review
Unresolved conflict count > 10                          STOP → escalate to Governance Board
Primary region node health_score < 0.80 during Steps   STOP → return to Step 5
 6–9
Replication lag > 0ms detected after Step 2            RESTART from Step 2
Bridge token invalidation failure for > 5 tokens       STOP Step 1 → investigate
Science domain score conflict detected                  STOP Step 8 → require mentor review
Admin-initiated abort (at any step)                     IMMEDIATE STOP at current step
```

### 12.2 Abort Recovery Protocol

```
On abort:
  1. Re-route ALL traffic back to DR region immediately
  2. Update bridge_routing_decisions: routing_reason = 'failback_aborted_return_dr'
  3. Update bridge_failback_audit: failback_status = 'aborted', abort_reason = [reason]
  4. Emit: bridge.failback.aborted { failback_id, step_aborted_at, reason }
  5. Notify Admin Governance + On-Call (immediate page)
  6. All partially reconciled data: RETAIN in bridge_reconciliation_records (not rolled back)
  7. No data modified during aborted reconciliation steps is deleted
  8. New failback authorization required to retry (fresh eligibility check)

Partial failback state is preserved for forensic investigation.
Partial state is never presented to users as complete.
```

---

## 13. FAILBACK DRILL REQUIREMENTS (PER R55 — MANDATORY)

### 13.1 Bridge Domain Drill Schedule

```
DRILL TYPE                          FREQUENCY       SCOPE
────────────────────────────────────────────────────────────────────────────────────────────
Bridge Domain Full Failback Drill   Quarterly       All session types (GD/Dojo/Interview)
GD Domain-Only Failback Drill       Quarterly       GD sessions only — staging environment
Dojo Domain-Only Failback Drill     Quarterly       Dojo sessions only — staging environment
Tenant-Scoped Failback Drill        Quarterly       Single test tenant — staging environment
Score Reconciliation Drill          Quarterly       Verify score record integrity post-failback
Token Invalidation Drill            Bi-annually     All pending tokens invalidated correctly
Redis State Reconstruction Drill    Bi-annually     Flush DR Redis → reconstruct from DB

Missed drill cadence → AUTOMATIC NON-COMPLIANCE FLAG (per R55-C law)
```

### 13.2 Drill Isolation Rules

```
All drills MUST be:
  ✔ Executed in DR or staging environment
  ✔ Non-destructive (no live data mutation)
  ✔ Clearly flagged as drills (drill_id present in all drill records)
  ✔ Isolated from live users
  ✔ Executed with synthetic call_session_map data
  ✔ Completed with human sign-off

Forbidden during drills:
  ✗ Live production data mutation
  ✗ Real PSTN calls in drill scope
  ✗ Real user session data used
  ✗ Silent drill execution
```

### 13.3 Drill Output Record

```sql
CREATE TABLE bridge_failback_drill_records (
    id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    drill_id            UUID NOT NULL UNIQUE,
    drill_type          TEXT NOT NULL,
    environment         TEXT NOT NULL,              -- ENUM: staging | dr_shadow
    scope               TEXT NOT NULL,
    executed_by         UUID NOT NULL,
    executed_at         TIMESTAMPTZ NOT NULL,
    duration_seconds    INTEGER,
    data_volume_tested  JSONB,
    issues_found        INTEGER NOT NULL DEFAULT 0,
    issues_detail       JSONB,
    steps_passed        INTEGER NOT NULL DEFAULT 0,
    steps_failed        INTEGER NOT NULL DEFAULT 0,
    human_signoff       UUID NOT NULL,
    signoff_at          TIMESTAMPTZ NOT NULL,
    drill_passed        BOOLEAN NOT NULL,
    created_at          TIMESTAMPTZ NOT NULL DEFAULT now()
);
```

---

## 14. KAFKA EVENT PIPELINE

### 14.1 Required Failback Events

```
EVENT TOPIC                                 PRODUCER                    CONSUMERS
────────────────────────────────────────────────────────────────────────────────────────────────
bridge.failback.initiated                   FailbackOrchestrator        Admin Governance · On-Call
bridge.failback.eligibility_passed          FailbackOrchestrator        Admin Governance · Analytics
bridge.failback.eligibility_failed          FailbackOrchestrator        Admin Governance · On-Call
bridge.failback.step_completed              FailbackOrchestrator        Admin Governance · Analytics
bridge.failback.step_failed                 FailbackOrchestrator        Admin Governance · On-Call (P0)
bridge.failback.sync_complete               FailbackOrchestrator        Analytics
bridge.failback.reconciliation_started     ReconciliationEngine        Admin Governance
bridge.failback.conflict_detected           ReconciliationEngine        Admin Governance · On-Call
bridge.failback.conflict_resolved           ReconciliationEngine        Admin Governance
bridge.failback.score_reconciled            ReconciliationEngine        Scoring Engine · Analytics
bridge.failback.score_flagged               ReconciliationEngine        Scoring Engine · Mentor Board
bridge.failback.token_invalidated_batch     TokenInvalidator            Auth Service · Analytics
bridge.failback.write_enabled               FailbackOrchestrator        Bridge Router · All bridge
                                                                        consumers
bridge.failback.completed                   FailbackOrchestrator        Admin Governance · Analytics ·
                                                                        Notification Service
bridge.failback.aborted                     FailbackOrchestrator        Admin Governance · On-Call (P0) ·
                                                                        Wazuh SIEM
bridge.failback.drill_completed             DrillOrchestrator           Admin Governance · Analytics
```

---

## 15. OBSERVABILITY (MANDATORY)

### 15.1 Prometheus Metrics

```
METRIC                                                  TYPE        LABELS
──────────────────────────────────────────────────────────────────────────────────────────────────
ecoskiller_bridge_failback_initiated_total              counter     (primary_region, dr_region, mode)
ecoskiller_bridge_failback_completed_total              counter     (primary_region, mode)
ecoskiller_bridge_failback_aborted_total                counter     (primary_region, step_aborted_at)
ecoskiller_bridge_failback_step_duration_seconds        histogram   (step_number, primary_region)
ecoskiller_bridge_failback_conflicts_detected_total     counter     (table_name, conflict_type)
ecoskiller_bridge_failback_records_reconciled_total     counter     (table_name, primary_region)
ecoskiller_bridge_failback_scores_flagged_total         counter     (domain_track, primary_region)
ecoskiller_bridge_failback_tokens_invalidated_total     counter     (primary_region)
ecoskiller_bridge_failback_eligibility_check_seconds    histogram   (primary_region)
ecoskiller_bridge_failback_drift_seconds                gauge       (primary_region, dr_region)
ecoskiller_bridge_failback_drill_passed_total           counter     (drill_type)
ecoskiller_bridge_failback_drill_failed_total           counter     (drill_type, step_failed)
```

### 15.2 Grafana Dashboard Panels

```
PANEL                                                   ALERT THRESHOLD
──────────────────────────────────────────────────────────────────────────────────────────────────
Failback Eligibility Status (live 9-condition)          Alert if any condition RED > 30 min
Active Failback Progress (step tracker — live)          Alert if any step > 3× expected duration
Conflict Detection Rate during Reconciliation           Alert if > 5 conflicts per 1000 records
Score Flagging Rate during Failback                     Alert if > 1% of scores flagged
Failback Duration Histogram (historical)                Alert if last failback > 90 minutes
Drill Compliance Calendar (quarterly view)              Alert if any drill overdue
Post-Failback Error Rate (30-min window)                Alert if > 0.5% error rate post-failback
Failback Abort History                                  Alert IMMEDIATELY on any abort (P0)
```

### 15.3 Wazuh SIEM Rules

```
RULE                                        TRIGGER
────────────────────────────────────────────────────────────────────────────────────────────
bridge_failback_abort_rule                  bridge.failback.aborted event (P0)
bridge_failback_conflict_burst_rule         > 5 conflict_detected events in 10 minutes
bridge_failback_unauthorized_rule           Failback step executed without valid
                                            failback_authorization record
bridge_failback_score_critical_rule         > 10 score records flagged in single failback
bridge_failback_drill_overdue_rule          Drill cadence missed (quarterly check)
bridge_failback_silent_resolution_rule     Conflict resolved without conflict_resolution
                                            field populated — silent resolution detected
```

---

## 16. GOVERNANCE INTEGRATION

### 16.1 Admin Governance Service Integration

```
Failback events feed:
  - Trust & Safety systems (per R54-M)
  - Platform confidence score (repeated failbacks reduce score)
  - Compliance risk score (DR failures per R54)
  - Deployment blocker (critical failback errors block new deployments)
  - Governance Board review (triggered if failback_count > 2 in 30 days)

After failback completion:
  - Admin Governance generates formal incident report
  - Incident report linked to failback_id
  - Report includes: root cause, data impact, score impact, resolution timeline
  - Governance Board sign-off required before next production deployment
```

### 16.2 On-Call Integration

```
Events that page on-call immediately (P0):
  - bridge.failback.aborted
  - bridge.failback.step_failed
  - bridge.failback.conflict_detected (if conflicts > 5)
  - bridge.failback.score_flagged (if Science domain)

On-call standby: mandatory for 30 minutes post-failback completion
On-call role: Bridge Infrastructure On-Call (from on-call rotation)
```

---

## 17. CI/CD PIPELINE GATES

### 17.1 Required Tests

```
TEST FILE                                       COVERAGE
──────────────────────────────────────────────────────────────────────────────────────────────
failback_eligibility_checker_test               All 9 conditions pass / block scenarios
failback_authorization_schema_test              Append-only, expiry, approval enforcement
delta_window_detector_test                      Correct window calculation from failover events
call_session_map_reconciliation_test            Insert, skip, flag conflict — all paths
call_session_events_reconciliation_test         Append-only, sequence continuity, gap detection
bridge_routing_reconciliation_test              FK integrity, duplicate decision handling
score_reconciliation_test                       DR score preservation, partial session flagging
science_domain_score_lock_test                  Revalidation flag applied, publication blocked
token_invalidation_test                         All pending tokens invalidated + Redis blacklisted
redis_flush_reconstruction_test                 Flush DR → reconstruct from DB → state matches
failback_sequence_test                          9 steps in order, skip detection = STOP
abort_protocol_test                             Abort at each step → DR restore verified
user_notification_test                          Correct notifications at each phase
drill_record_creation_test                      Drill records created, sign-off enforced
integration_full_failback_lifecycle             End-to-end: failover → DR window → failback → verify
integration_conflict_resolution_lifecycle       Conflict detected → suspended → resolved → resumed
integration_abort_and_retry_lifecycle           Abort at Step 5 → all checks → retry → complete
integration_science_score_revalidation          Science score locked → mentor review → released
integration_drill_simulation                    Full drill in staging → audit record verified
```

### 17.2 CI Stage Declarations

```
STAGE                               GATE
──────────────────────────────────────────────────────────────────────────────────────────────
contract_validator                  Failback API schema in OpenAPI registry
unit_test_gate                      All unit tests pass — zero tolerance
integration_test_gate               All integration tests pass
migration_dry_run                   4 new tables: Flyway dry-run on test DB
kafka_schema_validation             All 17 failback event schemas in AsyncAPI registry
observability_smoke_test            All 12 Prometheus metrics scrape clean
security_scan_gate                  No E.164/phone in failback events or logs
append_only_enforcement_gate        Test: UPDATE/DELETE on failback tables = rejected
drill_cadence_validator             Drill records exist for all required drill types
failback_sequence_validator         9 steps declared in correct order in code
governance_approval_gate            failback_authorizations: expiry logic tested

Any gate FAIL → CI STOP → NO ARTIFACT PRODUCED
```

---

## 18. CONTRACT GATE REGISTRATION

### 18.1 Required Input Gates

```
GATE                                SOURCE
──────────────────────────────────────────────────────────────────────────────────────────────
multi_region_bridge_routing_ready   MULTI_REGION_BRIDGE_ROUTING_AGENT v1.0
call_session_map_ready              CALL_SESSION_MAPPING_AGENT v1.0
phone_pipeline_complete             PHONE_PIPELINE_COMPLETENESS_AGENT v1.0
session_management_ready            SESSION-COMP-v1
scoring_engine_ready                Scoring Engine (GD / Dojo / Interview)
db_ready                            Lane B Data
event_schema_ready                  Lane A Foundation (R4)
admin_governance_ready              Admin Governance Service
on_call_module_ready                On-Call Module (from Admin Governance)
notification_service_ready          Notification Service
legal_document_service_ready        Legal Document Generation Service (audit report)
```

### 18.2 Contract Gates Produced on PASS

```
GATE                                        CONSUMED BY
──────────────────────────────────────────────────────────────────────────────────────────────
bridge_failback_ready                       Platform DR Readiness Check
                                            Deployment Authorization (failback gate)
bridge_reconciliation_engine_ready          Scoring Engine (score publication gate)
                                            Analytics Service (DR window data)
bridge_drill_compliance_ready              Platform Compliance Module
                                            Admin Governance (quarterly DR review)
pstn_bridge_domain_complete                Platform-Wide Failback Agent (dependency declared)
```

---

## 19. ANTIGRAVITY GENERATION CHECKLIST (LOCKED)

```
Antigravity MUST generate:
  ✔ V{n}__create_failback_authorizations.sql        Flyway migration (append-only)
  ✔ V{n+1}__create_bridge_reconciliation_records.sql Flyway migration (append-only)
  ✔ V{n+2}__create_bridge_failback_audit.sql         Flyway migration (append-only)
  ✔ V{n+3}__create_bridge_failback_drill_records.sql Flyway migration (append-only)
  ✔ V{n+4}__bridge_failback_rls_policies.sql         Flyway migration
  ✔ V{n+5}__bridge_failback_append_only_lock.sql     Flyway migration (REVOKE UPDATE/DELETE)
  ✔ failback-orchestrator/FailbackOrchestrator        Module — 9-step sequence controller
  ✔ failback-orchestrator/EligibilityChecker          Module — all 9 conditions
  ✔ failback-orchestrator/AuthorizationGate           Module — human approval enforcement
  ✔ failback-orchestrator/ReconciliationEngine        Module — delta compare + merge
  ✔ failback-orchestrator/ConflictDetector            Module — classification + flagging
  ✔ failback-orchestrator/ScoreReconciler             Module — GD/Dojo/Interview scores
  ✔ failback-orchestrator/TokenInvalidator            Module — pending tokens
  ✔ failback-orchestrator/RedisFlushReconstructor     Module — flush + DB-reconstruct
  ✔ failback-orchestrator/SessionDrainCoordinator     Module — active session drain
  ✔ failback-orchestrator/UserNotificationCoordinator Module — per R54-J
  ✔ failback-orchestrator/AbortProtocol               Module — abort + DR restore
  ✔ failback-orchestrator/DrillOrchestrator           Module — all drill types
  ✔ failback-orchestrator/KafkaEventEmitter           Module — 17 events
  ✔ failback-orchestrator/AuditReportGenerator        Module — PDF + JSON report
  ✔ /bridge/failback/initiate endpoint                Admin console — Admin JWT + ADMIN_FAILBACK
  ✔ /bridge/failback/status/{failback_id} endpoint    Admin console + monitoring
  ✔ /bridge/failback/abort/{failback_id} endpoint     Admin console — emergency abort
  ✔ /bridge/failback/drill/run endpoint               Admin console — staging only
  ✔ bridge_failback_kafka_schemas/                    17 event schemas (AsyncAPI)
  ✔ bridge_failback_opa_policies.rego                 OPA: failback write permissions
  ✔ bridge_failback_wazuh_rules.xml                   6 Wazuh rules
  ✔ bridge_failback_grafana_dashboard.json            8 panels + alert rules

Antigravity MUST NOT generate:
  ✗ Any automatic failback trigger (time-based, health-metric-triggered, or default)
  ✗ Any failback path that skips eligibility checking
  ✗ Any conflict resolution that overwrites primary history with DR history
  ✗ Any score record deletion or silent invalidation during reconciliation
  ✗ Any PSTN session migration mid-failback (terminate + re-invite only)
  ✗ Any bridge token valid across the primary/DR boundary
  ✗ Any Redis state copy from DR to primary (reconstruction from DB only)
  ✗ Any failback without a written failback_authorization record
  ✗ Any audit report that can be modified after generation
  ✗ Any drill that uses live production data
  ✗ Any failback step reordering
```

Absence of any generation item →
**STOP EXECUTION → REPORT CROSS_REGION_FAILBACK_AGENT_INCOMPLETE**

---

## ✅ FINAL ENFORCEMENT SUMMARY

```
CROSS_REGION_FAILBACK_RECONCILIATION_AGENT
  ├── Domain:          PSTN & Bridge
  ├── Scope:           Controlled return from DR to Primary for all bridge domain data
  ├── Failback Modes:  C2 Bridge Domain · C3 Domain-Scoped · C4 Tenant-Scoped
  ├── Eligibility:     9 conditions — ALL must pass simultaneously — no partial pass
  ├── Sequence:        9 mandatory steps — locked order — no skipping — no merging
  ├── Data:            4 reconciliation tables (call_session_map · events ·
  │                   routing_decisions · failback_audit) — all append-only
  ├── Sessions:        Drain protocol — no mid-failback migration — re-invite only
  ├── Scores:          Preserved from DR window — Science domain locked for revalidation
  ├── Tokens:          All pending DR tokens invalidated before failback traffic moves
  ├── Redis:           Flush DR state → reconstruct from DB (no blind copy)
  ├── Conflicts:       Classified + flagged + human-reviewed — silent resolution FORBIDDEN
  ├── Drills:          7 drill types · quarterly minimum · human sign-off required
  ├── Events:          17 Kafka topics · AsyncAPI schema enforced
  ├── Observability:   12 Prometheus metrics · 8 Grafana panels · 6 Wazuh rules
  ├── Governance:      Human authorization mandatory · Trust & Safety feed · 30-day block rule
  ├── Tests:           19 unit/integration tests · 11 CI gates
  ├── Input gates:     11 required
  ├── Output gates:    4 produced on full PASS
  └── Failure mode:    STOP → ABORT → RESTORE DR — no silent failback, no auto-promotion

FAILBACK IS A GOVERNANCE EVENT, NOT A ROUTING EVENT.
SILENT FAILBACK IS FORBIDDEN.
DR IS SOURCE-OF-TRUTH FOR THE DELTA WINDOW.

EXECUTION_ENGINE    = ANTIGRAVITY
CHANGE_POLICY       = APPEND_ONLY
READY_FOR           = ANTIGRAVITY_PRODUCTION
STATUS              = SEALED · LOCKED · GOVERNED · DETERMINISTIC
```

---

*Ecoskiller · CROSS_REGION_FAILBACK_RECONCILIATION_AGENT · v1.0 · SEALED*
*Mutation requires version bump and Human declaration only.*
*Inherits: R54 · R55 · MRX · RSX · Section 176 · ORCH-I · LATENCY-J · XBD*
*Depends on: MULTI_REGION_BRIDGE_ROUTING_AGENT v1.0 · CALL_SESSION_MAPPING_AGENT v1.0*
*Completes: PSTN & Bridge domain agent chain v1.0*
