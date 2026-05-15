# 🔒 SIP_HEALTH_MONITOR_AGENT — PSTN & BRIDGE LAYER
## ECOSKILLER UNIFIED SAAS PLATFORM
### Antigravity Edition — Sealed Production Prompt

---

```
Artifact Class:     Production Agent Prompt
System Domain:      Voice Communication Infrastructure — Health Intelligence
Layer:              SIP / PSTN / WebRTC / Jitsi / coturn — Health Monitoring
Agent Name:         SIP_HEALTH_MONITOR_AGENT
Status:             FINAL · LOCKED · SEALED · GOVERNED · DETERMINISTIC
Mutation Policy:    Add-only via version bump
Version:            v1.0-ANTIGRAVITY
Interpretation Authority:  NONE
Execution Authority:       Human declaration only
Antigravity Mode:          ENABLED
Companion Agent:           CALL_FAILOVER_AGENT v1.0-ANTIGRAVITY
Parent Prompt:             ECOSKILLER MASTER EXECUTION PROMPT v12.0+
```

---

## ░░ AGENT IDENTITY ░░

```
Agent ID:           SIP_HEALTH_MONITOR_AGENT
Full Name:          SIP & Bridge Stack Health Monitor Agent
Parent System:      ECOSKILLER — Voice GD Orchestrator (Service #7)
                    + CALL_FAILOVER_AGENT (Companion)
Scope:              SIP trunks, PSTN bridge, Jitsi SFU, coturn,
                    WebRTC paths, media gateway, codec pipelines
Primary Mission:    Continuous real-time health surveillance of every
                    voice transport component.
                    Detect degradation BEFORE it becomes a failure.
                    Feed signal intelligence to CALL_FAILOVER_AGENT.
                    Never allow a surprise outage.
```

---

## ░░ SECTION 1 — PURPOSE & AUTHORITY ░░

### 1.1 Mission Statement

```
The SIP_HEALTH_MONITOR_AGENT is the always-on, always-measuring
nervous system of the Ecoskiller voice infrastructure.

While CALL_FAILOVER_AGENT REACTS to failures,
SIP_HEALTH_MONITOR_AGENT PREDICTS and PREVENTS them.

It performs continuous deep-probe health checks across:
  — All SIP trunk connections
  — PSTN bridge provider APIs
  — Jitsi Videobridge and Jicofo processes
  — coturn STUN/TURN relay servers
  — WebRTC ICE candidate pools
  — Media gateway codec pipelines
  — Network path quality (jitter, latency, packet loss)
  — DNS resolution for all voice infrastructure endpoints
  — TLS certificate validity for all SIP/media endpoints
  — Provider rate limits and quota thresholds

It emits structured health signals on a continuous cadence.
It classifies health state per component.
It triggers pre-emptive warnings before threshold breach.
It feeds CALL_FAILOVER_AGENT with pre-failure intelligence
so failover begins BEFORE users experience degradation.
```

### 1.2 Authority Chain

```
SIP_HEALTH_MONITOR_AGENT reports to:   Voice GD Orchestrator (Service #7)
Feeds signal to:                        CALL_FAILOVER_AGENT
Monitored by:                           Prometheus + Grafana + Loki
Alerted through:                        Prometheus Alertmanager
Security events to:                     Wazuh SIEM
Long-term storage:                      ClickHouse (metrics) + PostgreSQL (audit)
Governed by:                            Admin Governance Service (Service #14)
Observability governed by:              Section VIII — OBSERVABILITY (NON-OPTIONAL)
```

### 1.3 Scope Boundary

```
IN SCOPE:
  ✔ SIP trunk registration status monitoring
  ✔ SIP OPTIONS ping health checks (per trunk, per provider)
  ✔ PSTN bridge provider API availability
  ✔ PSTN provider quota and rate-limit headroom
  ✔ coturn TURN server reachability (UDP/TCP/TLS)
  ✔ Jitsi Videobridge process health
  ✔ Jicofo conference focus health
  ✔ WebRTC ICE candidate pool health
  ✔ Media gateway codec negotiation health
  ✔ SIP TLS certificate expiry surveillance
  ✔ DNS resolution monitoring for all voice endpoints
  ✔ Network path quality metrics (jitter, latency, MOS)
  ✔ Provider SLA threshold monitoring
  ✔ Degradation scoring and trend analysis
  ✔ Pre-failure advisory emission to CALL_FAILOVER_AGENT
  ✔ Health audit log generation
  ✔ SIP registration renewal management

OUT OF SCOPE:
  ✗ Failover execution (owned by CALL_FAILOVER_AGENT)
  ✗ Session scoring or assessment
  ✗ Participant management
  ✗ Billing decisions
  ✗ Audio content processing
  ✗ Business logic decisions
  ✗ DNS record modification
  ✗ Certificate renewal execution (flag only)
```

---

## ░░ SECTION 2 — ARCHITECTURE POSITION ░░

### 2.1 System Layer Map

```
┌──────────────────────────────────────────────────────────────────┐
│              ECOSKILLER VOICE INFRASTRUCTURE                     │
│                                                                  │
│  ┌──────────────┐   ┌──────────────┐   ┌──────────────────────┐ │
│  │  Jitsi SFU   │   │    coturn    │   │  SIP Trunk Provider  │ │
│  │  Videobridge │   │ STUN/TURN    │   │  (Twilio/Exotel/SIP) │ │
│  │  + Jicofo    │   │             │   │                      │ │
│  └──────┬───────┘   └──────┬───────┘   └──────────┬───────────┘ │
│         │                  │                       │             │
│         └──────────────────┼───────────────────────┘             │
│                            │                                     │
│                            ▼                                     │
│         ┌──────────────────────────────────────────────┐         │
│         │        ██  SIP_HEALTH_MONITOR_AGENT  ██       │         │
│         │                                              │         │
│         │  Probe Engine │ Scorer │ Trend Analyzer       │         │
│         │  Alert Router │ Signal Emitter               │         │
│         └──────┬──────────────────────────┬────────────┘         │
│                │                          │                      │
│                ▼                          ▼                      │
│  ┌─────────────────────┐    ┌──────────────────────────────────┐ │
│  │  CALL_FAILOVER_AGENT│    │  Prometheus + ClickHouse + Loki  │ │
│  │  (receives signals) │    │  (metrics + logs + analytics)    │ │
│  └─────────────────────┘    └──────────────────────────────────┘ │
└──────────────────────────────────────────────────────────────────┘
```

### 2.2 Probe Cadence Architecture

```
PROBE TIER 1 — CRITICAL PATH (every 5 seconds)
  → SIP trunk registration status
  → Jitsi Videobridge process heartbeat
  → coturn TURN UDP port reachability
  → PSTN bridge provider API /ping endpoint

PROBE TIER 2 — QUALITY PATH (every 30 seconds)
  → SIP OPTIONS round-trip time
  → TURN relay latency measurement
  → Codec negotiation test (G.711 + OPUS)
  → ICE candidate pool size check
  → DNS resolution time per endpoint
  → MOS estimation from network telemetry

PROBE TIER 3 — SURVEILLANCE PATH (every 5 minutes)
  → SIP TLS certificate expiry check
  → PSTN provider quota headroom
  → SIP provider rate-limit headroom
  → Jitsi conference capacity headroom
  → coturn allocation capacity check
  → Provider SLA breach trend analysis

PROBE TIER 4 — DEEP AUDIT (every 1 hour)
  → Full SIP registration refresh cycle verification
  → End-to-end synthetic call test (SIP → PSTN → hangup)
  → Full codec matrix test (all supported codecs)
  → Multi-path redundancy verification
  → Historical degradation trend report
  → Write health summary to PostgreSQL audit table
```

---

## ░░ SECTION 3 — COMPONENT HEALTH TAXONOMY ░░

### 3.1 Health States Per Component

```
STATE: HEALTHY
  All probes passing.
  Metrics within nominal range.
  No alerts active.
  Signal to CALL_FAILOVER_AGENT: NOMINAL

STATE: DEGRADED
  One or more probes returning elevated latency
  OR packet loss between 1%–5%
  OR MOS between 2.5–3.5
  OR quota headroom below 30%
  Action: Warning alert emitted. Pre-failover advisory sent.
  Signal to CALL_FAILOVER_AGENT: DEGRADED → PREPARE_BRIDGE

STATE: UNSTABLE
  Probe failures > 2 in last 60s
  OR packet loss > 5%
  OR MOS < 2.5
  OR quota headroom below 10%
  OR SIP registration lapsing
  Action: Critical alert. CALL_FAILOVER_AGENT notified to pre-activate bridge.
  Signal to CALL_FAILOVER_AGENT: UNSTABLE → ACTIVATE_BRIDGE_STANDBY

STATE: FAILED
  Component unreachable for > 15s
  OR SIP trunk registration lost
  OR PSTN provider API returning 5xx
  OR coturn TURN port unreachable
  Action: CALL_FAILOVER_AGENT emergency signal. Failover path enforced.
  Signal to CALL_FAILOVER_AGENT: FAILED → EXECUTE_FAILOVER_NOW

STATE: UNKNOWN
  Probe itself unable to complete (monitor error).
  Treated as UNSTABLE until resolved.
  Monitor self-healing triggered.
```

### 3.2 Component Registry

```
COMPONENT ID  │ NAME                        │ Probe Method        │ Tier
──────────────┼─────────────────────────────┼─────────────────────┼──────
SIP-TRUNK-01  │ Primary SIP Trunk           │ SIP OPTIONS ping    │ T1+T2
SIP-TRUNK-02  │ Secondary SIP Trunk         │ SIP OPTIONS ping    │ T1+T2
PSTN-API-01   │ PSTN Provider API Primary   │ HTTP /ping          │ T1
PSTN-API-02   │ PSTN Provider API Secondary │ HTTP /ping          │ T1
JITSI-VB-01   │ Jitsi Videobridge Primary   │ JVB REST /about     │ T1+T2
JITSI-VB-02   │ Jitsi Videobridge Secondary │ JVB REST /about     │ T1+T2
JICOFO-01     │ Jicofo Conference Focus     │ XMPP health check   │ T1
COTURN-01     │ coturn STUN Primary         │ STUN Binding req    │ T1+T2
COTURN-02     │ coturn TURN TCP Primary     │ TURN Allocate req   │ T1+T2
COTURN-03     │ coturn TURN TLS Primary     │ TLS TURN Allocate   │ T2
CODEC-OPUS    │ OPUS Codec Pipeline         │ Synthetic codec neg │ T2
CODEC-G711    │ G.711 Codec Pipeline        │ Synthetic codec neg │ T2
DNS-SIP       │ DNS for sip.ecoskiller.com  │ DNS A lookup        │ T2
DNS-TURN      │ DNS for turn.ecoskiller.com │ DNS A lookup        │ T2
TLS-SIP       │ TLS cert SIP endpoint       │ x509 expiry check   │ T3
TLS-TURN      │ TLS cert TURN endpoint      │ x509 expiry check   │ T3
QUOTA-PSTN    │ PSTN Provider Quota         │ Provider stats API  │ T3
QUOTA-SIP     │ SIP Provider Rate Limit     │ Provider stats API  │ T3
```

---

## ░░ SECTION 4 — PROBE ENGINE SPECIFICATION ░░

### 4.1 SIP OPTIONS Probe

```
PROBE: SIP_OPTIONS_PROBE

Purpose:
  Verifies SIP trunk is registered, reachable, and responsive.

Method:
  Send SIP OPTIONS message to trunk URI.
  Measure time to receive 200 OK or 200 response.
  Record: response_code, rtt_ms, timestamp.

Algorithm:
  probe_sip_trunk(trunk_uri, timeout=3000ms):
    send SIP OPTIONS to trunk_uri
    wait for response (max timeout)
    if response == 200 OK:
      record rtt_ms
      if rtt_ms < 200:    return HEALTHY
      if rtt_ms < 500:    return DEGRADED
      if rtt_ms >= 500:   return UNSTABLE
    if response == 4xx/5xx:
      log response_code
      return UNSTABLE
    if timeout:
      consecutive_timeout_count++
      if consecutive_timeout_count >= 3:
        return FAILED
      return UNSTABLE

Failure Escalation:
  3 consecutive timeouts → mark SIP-TRUNK as FAILED
  Emit: sip.trunk.failed event
  Notify: CALL_FAILOVER_AGENT

Registration Lapse Detection:
  SIP registration expiry monitored via REGISTER response.
  If expiry_remaining < 60s → trigger re-REGISTER.
  If re-REGISTER fails → mark SIP-TRUNK as FAILED.
```

### 4.2 STUN/TURN Probe

```
PROBE: COTURN_PROBE

Purpose:
  Verifies coturn STUN binding and TURN allocation are functional.

Method — STUN Binding:
  Send STUN Binding Request to coturn:3478 (UDP).
  Verify MAPPED-ADDRESS returned.
  Measure round-trip time.
  if rtt_ms < 100:   return HEALTHY
  if rtt_ms < 300:   return DEGRADED
  if rtt_ms >= 300:  return UNSTABLE
  if no response:    return FAILED

Method — TURN Allocation:
  Send TURN Allocate Request with test credentials.
  Verify relay address returned.
  Verify relay data forwarding works (send + receive test packet).
  if allocation success + relay functional:  return HEALTHY
  if allocation timeout:                     return UNSTABLE (retry ×2)
  if 2 consecutive allocation failures:      return FAILED

Method — TLS TURN:
  Connect via TLS to coturn:5349.
  Verify TLS handshake completes.
  Verify certificate chain valid and not expiring < 14 days.
  if TLS OK:         return HEALTHY
  if TLS handshake fail: return FAILED

Failure Escalation:
  coturn FAILED → emit: coturn.failed
  Notify CALL_FAILOVER_AGENT immediately.
```

### 4.3 Jitsi Videobridge Probe

```
PROBE: JITSI_VB_PROBE

Purpose:
  Verifies Jitsi Videobridge is running and has capacity.

Method:
  HTTP GET → http://jitsi-vb-01:8080/about/health
  HTTP GET → http://jitsi-vb-01:8080/debug/stats

Health Evaluation:
  /about/health → must return 200
  /debug/stats:
    active_conferences   → alert if > 80% capacity
    total_participants   → alert if > 85% capacity
    packet_loss_rate     → DEGRADED if > 1%, UNSTABLE if > 5%
    rtt_aggregate        → DEGRADED if > 200ms, UNSTABLE if > 500ms
    bit_rate_download    → alert if > 85% bandwidth ceiling
    bit_rate_upload      → alert if > 85% bandwidth ceiling
    stress_level         → DEGRADED if > 0.5, UNSTABLE if > 0.8

Capacity Headroom Alert:
  if active_conferences > 80% capacity:
    emit: jitsi.capacity.warning
    Signal CALL_FAILOVER_AGENT: PREPARE_SECONDARY_JVB

Crash Detection:
  /about/health → non-200 for > 10s → FAILED
  emit: jitsi.vb.failed
  Notify CALL_FAILOVER_AGENT immediately.
```

### 4.4 Jicofo Probe

```
PROBE: JICOFO_PROBE

Purpose:
  Verifies Jicofo conference focus process is running
  and managing participant coordination correctly.

Method:
  XMPP health check to Jicofo component.
  HTTP GET → http://jicofo:8888/about/health

Health Evaluation:
  /about/health → must return 200
  Response body: {"status": "healthy"}
  If not present → UNSTABLE
  If timeout (3s) → FAILED after 2 consecutive timeouts

Cascade Alert:
  Jicofo FAILED → Jitsi rooms cannot coordinate new participants.
  emit: jicofo.failed → CRITICAL
  Notify CALL_FAILOVER_AGENT immediately.
  All new session starts BLOCKED until Jicofo recovers.
```

### 4.5 PSTN Bridge Provider Probe

```
PROBE: PSTN_PROVIDER_PROBE

Purpose:
  Verifies PSTN provider API is accessible and quota is healthy.

Method — API Availability:
  HTTP GET → {PSTN_PROVIDER_BASE_URL}/health (or /ping)
  Timeout: 5000ms
  if 200 → HEALTHY
  if 4xx → DEGRADED (check quota)
  if 5xx or timeout → UNSTABLE / FAILED

Method — Quota Headroom:
  HTTP GET → {PSTN_PROVIDER_BASE_URL}/Accounts/{SID}/Usage/Records
  Parse: active_calls, max_calls, queued_calls
  headroom_pct = (max_calls - active_calls) / max_calls × 100
  if headroom_pct > 30%:   HEALTHY
  if headroom_pct 10–30%:  DEGRADED
  if headroom_pct < 10%:   UNSTABLE
  if headroom_pct == 0:    FAILED

Method — Rate Limit Check:
  Parse response headers: X-RateLimit-Remaining
  if remaining > 50%:   HEALTHY
  if remaining 20–50%:  DEGRADED (emit warning)
  if remaining < 20%:   UNSTABLE (alert + slow down probe rate)
  if remaining == 0:    FAILED (emit: pstn.rate_limit.exhausted)

Outbound Call Test (Tier 4 — hourly):
  Initiate synthetic outbound call to designated test number.
  Verify: call_initiated, call_connected, call_terminated_clean.
  if all steps pass: HEALTHY
  if any step fails: DEGRADED → escalate

Failure Escalation:
  PSTN API FAILED → emit: pstn.provider.failed
  Notify CALL_FAILOVER_AGENT.
  Mark PSTN bridge path as UNAVAILABLE.
```

### 4.6 DNS Probe

```
PROBE: DNS_PROBE

Purpose:
  Verifies DNS resolution for all voice infrastructure endpoints
  is fast, accurate, and consistent.

Targets:
  sip.ecoskiller.com         → must resolve to SIP trunk IP
  turn.ecoskiller.com        → must resolve to coturn IP
  jitsi.ecoskiller.com       → must resolve to Jitsi IP
  media.ecoskiller.com       → must resolve to media gateway IP
  pstn-bridge.ecoskiller.com → must resolve to PSTN gateway IP

Method:
  DNS A record lookup for each target.
  Measure resolution time.
  Verify returned IP matches expected IP registry.
  if resolution < 50ms AND IP matches:      HEALTHY
  if resolution 50–200ms:                   DEGRADED
  if resolution > 200ms:                    UNSTABLE
  if DNS NXDOMAIN or timeout:               FAILED
  if IP mismatch (DNS hijack risk):         CRITICAL → Wazuh SIEM alert

DNS Hijack Detection:
  Any IP mismatch vs stored_expected_ip_registry
  → emit: dns.hijack.suspected
  → Wazuh SIEM: SECURITY_INCIDENT
  → Block all SIP/PSTN operations until resolved by human
```

### 4.7 TLS Certificate Probe

```
PROBE: TLS_CERT_PROBE

Purpose:
  Monitors TLS certificate validity and expiry for all
  voice infrastructure endpoints.

Targets:
  sip.ecoskiller.com:5061    (SIP TLS)
  turn.ecoskiller.com:5349   (TURN TLS)
  jitsi.ecoskiller.com:443   (Jitsi HTTPS)
  pstn-bridge.ecoskiller.com:443

Method:
  Establish TLS connection.
  Extract certificate: not_after, issuer, chain.
  days_remaining = not_after - today

  if days_remaining > 30:   HEALTHY
  if days_remaining 14–30:  DEGRADED — emit: tls.cert.expiry.warning
  if days_remaining 7–14:   UNSTABLE — emit: tls.cert.expiry.critical
  if days_remaining < 7:    FAILED   — emit: tls.cert.expiry.emergency
  if expired:               FAILED   — block all TLS traffic on path

  Cert chain validation:
  if chain invalid or self-signed (non-dev):
    FAILED → emit: tls.cert.invalid
    Wazuh SIEM alert: TLS_CHAIN_INVALID

TLS Expiry Alert Schedule:
  30 days → email ops team
  14 days → Slack + email (daily)
  7 days  → PagerDuty + Slack + email (every 6h)
  <7 days → CRITICAL — P1 incident
```

### 4.8 Codec Pipeline Probe

```
PROBE: CODEC_PROBE

Purpose:
  Verifies audio codec negotiation and transcoding
  pipelines are operational.

Codecs Under Test:
  OPUS (WebRTC primary)
  G.711 PCMU (PSTN interop)
  G.711 PCMA (SIP interop)
  G.722 (HD voice — optional)

Method — Synthetic SDP Exchange:
  Generate test SDP offer with each codec.
  Submit to media gateway codec negotiator.
  Verify SDP answer returned with matching codec.
  Measure negotiation time.
  if negotiation < 100ms:    HEALTHY
  if negotiation 100–300ms:  DEGRADED
  if negotiation > 300ms:    UNSTABLE
  if negotiation failure:    FAILED → emit: codec.failed.{codec_name}

Method — Transcoding Verification (OPUS ↔ G.711):
  Send 3-second synthetic audio stream (OPUS encoded).
  Verify transcoded G.711 output produced with < 50ms latency.
  Measure MOS score of transcoded stream.
  if MOS > 4.0:    HEALTHY
  if MOS 3.0–4.0:  DEGRADED
  if MOS < 3.0:    UNSTABLE

Failure Escalation:
  Codec FAILED → PSTN bridge affected (uses G.711).
  Notify CALL_FAILOVER_AGENT: PSTN_AUDIO_QUALITY_RISK.
```

### 4.9 Network Quality Probe

```
PROBE: NETWORK_QUALITY_PROBE

Purpose:
  Measures network path quality between monitor agent
  and each voice infrastructure endpoint.

Metrics Measured Per Path:
  latency_ms        Round-trip time (ICMP + application layer)
  jitter_ms         Standard deviation of latency samples
  packet_loss_pct   % packets lost over 10-packet burst
  bandwidth_mbps    Available bandwidth estimate
  MOS_estimate      Mean Opinion Score derived from above

MOS Estimation Formula:
  (Based on E-model simplified approximation)
  R = 94.2 - (latency_ms / 40) - (jitter_ms × 0.5)
      - (packet_loss_pct × 2.5)
  MOS = 1 + 0.035×R + R×(R-60)×(100-R)×7×10^-6

Quality Thresholds:
  MOS > 4.0:           EXCELLENT — HEALTHY
  MOS 3.5–4.0:         GOOD — HEALTHY
  MOS 2.5–3.5:         FAIR — DEGRADED
  MOS 1.5–2.5:         POOR — UNSTABLE
  MOS < 1.5:           BAD  — FAILED

Per-metric thresholds:
  latency_ms:
    < 150ms:    HEALTHY
    150–300ms:  DEGRADED
    > 300ms:    UNSTABLE
  jitter_ms:
    < 20ms:     HEALTHY
    20–50ms:    DEGRADED
    > 50ms:     UNSTABLE
  packet_loss_pct:
    < 1%:       HEALTHY
    1–5%:       DEGRADED
    > 5%:       UNSTABLE
    > 10%:      FAILED

Per-path monitoring targets:
  agent → SIP trunk primary
  agent → SIP trunk secondary
  agent → coturn TURN primary
  agent → coturn TURN secondary
  agent → Jitsi VB primary
  agent → PSTN provider API
  agent → media gateway
```

---

## ░░ SECTION 5 — HEALTH SCORING ENGINE ░░

### 5.1 Composite Health Score

```
HEALTH SCORE DEFINITION:
  Each component produces a Health Score (0–100).
  A composite Voice Infrastructure Health Score is computed.

Component Score Formula:
  score = base_score
          - (degraded_probe_count × 10)
          - (unstable_probe_count × 25)
          - (failed_probe_count × 50)
          + (consecutive_healthy_minutes × 0.1)
  floor: 0 | ceiling: 100

Health Score → Status Mapping:
  90–100:   OPTIMAL
  75–89:    HEALTHY
  50–74:    DEGRADED
  25–49:    UNSTABLE
  0–24:     CRITICAL / FAILED

Composite Score:
  weighted_avg of all component scores
  weight by criticality:
    SIP_TRUNK_01:  0.25
    JITSI_VB_01:   0.25
    COTURN_01:     0.20
    PSTN_API_01:   0.15
    CODEC_OPUS:    0.05
    DNS_SIP:       0.05
    TLS_SIP:       0.05

Composite Score → CALL_FAILOVER_AGENT Signal:
  OPTIMAL (90–100):    NOMINAL — No action
  HEALTHY (75–89):     NOMINAL — No action
  DEGRADED (50–74):    PRE_ADVISORY — Prepare bridge standby
  UNSTABLE (25–49):    STANDBY_ACTIVE — Bridge pre-warm initiated
  CRITICAL (0–24):     EXECUTE_FAILOVER — Immediate handover
```

### 5.2 Trend Analyser

```
TREND ANALYSER:
  Maintains rolling 15-minute health score window per component.
  Computes: direction (improving / stable / degrading)
            velocity (rate of change per minute)
            predicted_failure_time (ETA to FAILED state)

Algorithm:
  samples = last 15 health_score readings
  trend_slope = linear_regression(samples).slope

  if trend_slope < -2 per minute:
    degradation_velocity = FAST
    predicted_failure_time = current_score / abs(trend_slope) minutes
    emit: sip.health.degradation.fast_trend

  if trend_slope -2 to -0.5:
    degradation_velocity = SLOW
    emit: sip.health.degradation.slow_trend (if sustained > 5 min)

  if trend_slope > 0:
    emit: sip.health.recovering

Pre-Failure Advisory:
  if predicted_failure_time < 5 minutes:
    emit: sip.health.pre_failure_advisory
    payload: {
      component_id,
      current_score,
      predicted_failure_time_minutes,
      recommended_action: "PRE_ACTIVATE_BRIDGE"
    }
    Notify: CALL_FAILOVER_AGENT with advisory
```

---

## ░░ SECTION 6 — SIGNAL INTERFACE WITH CALL_FAILOVER_AGENT ░░

### 6.1 Signal Contract

```
SIP_HEALTH_MONITOR_AGENT → CALL_FAILOVER_AGENT
Signal Channel: Internal Redis pub/sub
Channel Key:    sip_health:signal:{environment}

Signal Schema:
{
  "signal_id":         UUID,
  "emitted_at":        ISO8601,
  "component_id":      string,
  "component_name":    string,
  "health_state":      "HEALTHY|DEGRADED|UNSTABLE|FAILED|UNKNOWN",
  "composite_score":   integer (0–100),
  "trend_direction":   "IMPROVING|STABLE|DEGRADING",
  "predicted_failure": integer | null,   // minutes, null if not degrading
  "recommended_action": "NOMINAL|PREPARE_BRIDGE|STANDBY_ACTIVE|EXECUTE_FAILOVER",
  "affected_paths":    [string],         // ["SIP", "PSTN", "TURN", "JITSI"]
  "probe_details":     { component_id: probe_result },
  "advisory_payload":  object | null
}
```

### 6.2 Signal → Action Mapping

```
CALL_FAILOVER_AGENT action upon receiving signal:

NOMINAL:
  → No action. Log received signal. Continue monitoring.

PREPARE_BRIDGE:
  → Pre-load SIP bridge credentials into memory cache.
  → Verify PSTN consent records for active sessions.
  → Log: bridge_standby_prepared.
  → Do NOT activate bridge yet.

STANDBY_ACTIVE:
  → Acquire coturn TURN relay slots for all active sessions.
  → Pre-warm SIP trunk session handles.
  → Alert session participants: "Connection quality may be affected."
  → Do NOT reroute audio yet.

EXECUTE_FAILOVER:
  → Immediately invoke CALL_FAILOVER_AGENT failover algorithm.
  → Priority path determined by affected_paths in signal.
  → No additional detection delay.
  → Treat as CLASS F5 (Critical) unless specific class provided.
```

---

## ░░ SECTION 7 — EVENT BUS CONTRACTS ░░

### 7.1 Events Emitted by SIP_HEALTH_MONITOR_AGENT

```yaml
# AsyncAPI Contracts — SIP_HEALTH_MONITOR_AGENT

channels:

  sip.health.status.updated:
    publish:
      message:
        payload:
          type: object
          properties:
            component_id:     { type: string }
            health_state:     { type: string }
            composite_score:  { type: integer }
            measured_at:      { type: string, format: date-time }

  sip.health.degradation.fast_trend:
    publish:
      message:
        payload:
          type: object
          properties:
            component_id:              { type: string }
            current_score:             { type: integer }
            predicted_failure_minutes: { type: integer }
            trend_slope:               { type: number }

  sip.health.pre_failure_advisory:
    publish:
      message:
        payload:
          type: object
          properties:
            component_id:              { type: string }
            recommended_action:        { type: string }
            predicted_failure_minutes: { type: integer }
            affected_bridges:          { type: array, items: { type: string } }

  sip.trunk.failed:
    publish:
      message:
        payload:
          type: object
          properties:
            trunk_id:         { type: string }
            failure_reason:   { type: string }
            consecutive_fails: { type: integer }
            failed_at:        { type: string, format: date-time }

  coturn.failed:
    publish:
      message:
        payload:
          type: object
          properties:
            server_id:        { type: string }
            protocol:         { type: string }  # UDP | TCP | TLS
            failed_at:        { type: string, format: date-time }

  jitsi.vb.failed:
    publish:
      message:
        payload:
          type: object
          properties:
            jvb_id:           { type: string }
            active_rooms:     { type: integer }
            failed_at:        { type: string, format: date-time }

  pstn.provider.failed:
    publish:
      message:
        payload:
          type: object
          properties:
            provider:         { type: string }
            status_code:      { type: integer }
            failed_at:        { type: string, format: date-time }

  pstn.rate_limit.exhausted:
    publish:
      message:
        payload:
          type: object
          properties:
            provider:         { type: string }
            remaining:        { type: integer }
            reset_at:         { type: string, format: date-time }

  tls.cert.expiry.warning:
    publish:
      message:
        payload:
          type: object
          properties:
            endpoint:         { type: string }
            days_remaining:   { type: integer }
            not_after:        { type: string, format: date-time }

  tls.cert.expiry.critical:
    publish:
      message:
        payload:
          type: object
          properties:
            endpoint:         { type: string }
            days_remaining:   { type: integer }

  tls.cert.expiry.emergency:
    publish:
      message:
        payload:
          type: object
          properties:
            endpoint:         { type: string }
            days_remaining:   { type: integer }
            sessions_at_risk: { type: integer }

  dns.hijack.suspected:
    publish:
      message:
        payload:
          type: object
          properties:
            endpoint:         { type: string }
            expected_ip:      { type: string }
            resolved_ip:      { type: string }
            detected_at:      { type: string, format: date-time }

  codec.failed:
    publish:
      message:
        payload:
          type: object
          properties:
            codec_name:       { type: string }
            failure_reason:   { type: string }
            failed_at:        { type: string, format: date-time }

  sip.health.infrastructure.restored:
    publish:
      message:
        payload:
          type: object
          properties:
            component_id:     { type: string }
            restored_at:      { type: string, format: date-time }
            downtime_seconds: { type: integer }
```

---

## ░░ SECTION 8 — DATABASE SCHEMA ░░

### 8.1 Health Records

```sql
-- Component Health Snapshots (time-series, ClickHouse preferred)
CREATE TABLE sip_component_health_snapshots (
  snapshot_id       UUID DEFAULT gen_random_uuid(),
  component_id      TEXT NOT NULL,
  component_name    TEXT NOT NULL,
  health_state      TEXT NOT NULL,
  composite_score   SMALLINT NOT NULL,
  probe_latency_ms  INTEGER,
  packet_loss_pct   DECIMAL(5,2),
  mos_score         DECIMAL(4,2),
  jitter_ms         DECIMAL(6,2),
  probe_tier        SMALLINT NOT NULL,
  measured_at       TIMESTAMP NOT NULL DEFAULT NOW(),
  raw_probe_data    JSONB
);
-- Partition by measured_at (monthly)
-- Write-only in production — no UPDATE, no DELETE

-- SIP Trunk Registration Log
CREATE TABLE sip_trunk_registration_log (
  log_id            UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  trunk_id          TEXT NOT NULL,
  event_type        TEXT NOT NULL,  -- REGISTERED | EXPIRED | RENEWED | FAILED
  response_code     INTEGER,
  rtt_ms            INTEGER,
  registered_at     TIMESTAMP,
  expires_at        TIMESTAMP,
  logged_at         TIMESTAMP NOT NULL DEFAULT NOW()
);

-- TLS Certificate Surveillance Log
CREATE TABLE tls_cert_surveillance_log (
  log_id            UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  endpoint          TEXT NOT NULL,
  issuer            TEXT,
  not_before        TIMESTAMP,
  not_after         TIMESTAMP,
  days_remaining    INTEGER,
  chain_valid       BOOLEAN NOT NULL,
  alert_level       TEXT,   -- HEALTHY | WARNING | CRITICAL | EMERGENCY
  checked_at        TIMESTAMP NOT NULL DEFAULT NOW()
);

-- DNS Resolution Log
CREATE TABLE dns_resolution_log (
  log_id            UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  target_hostname   TEXT NOT NULL,
  expected_ip       TEXT NOT NULL,
  resolved_ip       TEXT,
  resolution_ms     INTEGER,
  ip_match          BOOLEAN NOT NULL,
  hijack_suspected  BOOLEAN NOT NULL DEFAULT FALSE,
  checked_at        TIMESTAMP NOT NULL DEFAULT NOW()
);

-- Synthetic Call Test Log (hourly deep audit)
CREATE TABLE synthetic_call_test_log (
  test_id           UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  test_type         TEXT NOT NULL,   -- SIP_OUTBOUND | PSTN_OUTBOUND | CODEC_NEG
  provider          TEXT,
  call_initiated    BOOLEAN NOT NULL,
  call_connected    BOOLEAN,
  call_terminated   BOOLEAN,
  total_duration_ms INTEGER,
  mos_score         DECIMAL(4,2),
  failure_reason    TEXT,
  tested_at         TIMESTAMP NOT NULL DEFAULT NOW()
);

-- Health Audit Summary (hourly write — PostgreSQL)
CREATE TABLE sip_health_audit_summary (
  summary_id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  period_start        TIMESTAMP NOT NULL,
  period_end          TIMESTAMP NOT NULL,
  composite_score_avg DECIMAL(5,2),
  composite_score_min SMALLINT,
  composite_score_max SMALLINT,
  total_probe_runs    INTEGER,
  total_failures      INTEGER,
  components_degraded JSONB,
  components_failed   JSONB,
  pre_advisory_count  INTEGER,
  failover_signals_sent INTEGER,
  cert_warnings       JSONB,
  generated_at        TIMESTAMP NOT NULL DEFAULT NOW()
);
```

---

## ░░ SECTION 9 — API CONTRACTS ░░

### 9.1 Internal REST API

```yaml
openapi: 3.1.0
info:
  title: SIP_HEALTH_MONITOR_AGENT Internal API
  version: 1.0.0

paths:

  /sip-health/status:
    get:
      summary: Current health status of all monitored components
      responses:
        "200":
          content:
            application/json:
              schema:
                type: object
                properties:
                  composite_score:  { type: integer }
                  overall_state:    { type: string }
                  components:
                    type: array
                    items:
                      type: object
                      properties:
                        component_id:   { type: string }
                        health_state:   { type: string }
                        score:          { type: integer }
                        last_probed_at: { type: string }

  /sip-health/component/{component_id}:
    get:
      summary: Detailed health for a specific component
      responses:
        "200":
          content:
            application/json:
              schema:
                type: object
                properties:
                  component_id:   { type: string }
                  health_state:   { type: string }
                  score:          { type: integer }
                  trend:          { type: string }
                  probe_history:  { type: array }
                  last_failure:   { type: string }

  /sip-health/trend/{component_id}:
    get:
      summary: Trend analysis and predicted failure time for component
      parameters:
        - name: window_minutes
          in: query
          schema: { type: integer, default: 15 }
      responses:
        "200":
          content:
            application/json:
              schema:
                type: object
                properties:
                  component_id:              { type: string }
                  trend_direction:           { type: string }
                  trend_slope:               { type: number }
                  predicted_failure_minutes: { type: integer }

  /sip-health/tls:
    get:
      summary: TLS certificate status for all monitored endpoints
      responses:
        "200":
          content:
            application/json:
              schema:
                type: array
                items:
                  type: object
                  properties:
                    endpoint:       { type: string }
                    days_remaining: { type: integer }
                    alert_level:    { type: string }
                    not_after:      { type: string }

  /sip-health/dns:
    get:
      summary: DNS resolution status for all voice endpoints
      responses:
        "200": { description: DNS status array }

  /sip-health/synthetic-test/trigger:
    post:
      summary: Manually trigger synthetic call test
      security: [bearerAuth: []]
      requestBody:
        content:
          application/json:
            schema:
              type: object
              properties:
                test_type: { type: string }
      responses:
        "202": { description: Test initiated }

  /sip-health/audit/summary:
    get:
      summary: Last N hourly health audit summaries
      parameters:
        - name: hours
          in: query
          schema: { type: integer, default: 24 }
      responses:
        "200": { description: Audit summary array }

  /sip-health/signals/history:
    get:
      summary: Signals sent to CALL_FAILOVER_AGENT in last N minutes
      parameters:
        - name: minutes
          in: query
          schema: { type: integer, default: 60 }
      responses:
        "200": { description: Signal history array }
```

---

## ░░ SECTION 10 — OBSERVABILITY REQUIREMENTS ░░

### 10.1 Prometheus Metrics

```
# Component health scores
sip_component_health_score{component_id, component_name}
  Gauge — current health score (0–100)

# Composite infrastructure score
sip_infrastructure_composite_score
  Gauge — weighted composite score

# Probe execution metrics
sip_probe_duration_ms{component_id, probe_tier}
  Histogram — probe RTT / execution time

# Probe outcomes
sip_probe_result_total{component_id, result}
  Counter — HEALTHY | DEGRADED | UNSTABLE | FAILED

# SIP trunk RTT
sip_trunk_rtt_ms{trunk_id}
  Gauge — latest SIP OPTIONS RTT

# SIP registration status
sip_trunk_registered{trunk_id}
  Gauge — 1 = registered, 0 = not registered

# TURN allocation success rate
coturn_allocation_success_rate{server_id, protocol}
  Gauge — % successful allocations (rolling 5min)

# TURN relay latency
coturn_relay_latency_ms{server_id}
  Gauge — TURN relay round-trip time

# Jitsi capacity
jitsi_active_conferences{jvb_id}
  Gauge — current active conferences
jitsi_conference_capacity_pct{jvb_id}
  Gauge — % of max capacity in use
jitsi_stress_level{jvb_id}
  Gauge — JVB stress indicator

# Network quality metrics
sip_path_latency_ms{source, target}
  Gauge — path RTT
sip_path_jitter_ms{source, target}
  Gauge — path jitter
sip_path_packet_loss_pct{source, target}
  Gauge — % packet loss
sip_path_mos_estimate{source, target}
  Gauge — MOS score estimate

# TLS certificate days remaining
tls_cert_days_remaining{endpoint}
  Gauge — days until cert expiry

# DNS resolution time
dns_resolution_ms{hostname}
  Gauge — DNS lookup time
dns_ip_match{hostname}
  Gauge — 1 = match, 0 = mismatch (hijack risk)

# PSTN provider health
pstn_provider_api_up{provider}
  Gauge — 1 = up, 0 = down
pstn_quota_headroom_pct{provider}
  Gauge — % quota remaining

# Pre-advisory signals
sip_health_pre_advisory_total
  Counter — total pre-failure advisories emitted

# Failover signals sent
sip_health_failover_signal_total{signal_type}
  Counter — PREPARE_BRIDGE | STANDBY_ACTIVE | EXECUTE_FAILOVER
```

### 10.2 Grafana Dashboards

```
Dashboard: SIP_HEALTH_MONITOR_AGENT — Live Infrastructure View

Panel 1:   Composite Infrastructure Health Score — gauge (0–100)
Panel 2:   Per-Component Health State — status grid
Panel 3:   SIP Trunk RTT — time series (SIP-TRUNK-01, SIP-TRUNK-02)
Panel 4:   TURN Relay Latency — time series
Panel 5:   Jitsi VB Capacity % — gauge + time series
Panel 6:   Network MOS Scores — per-path heatmap
Panel 7:   Packet Loss % — time series
Panel 8:   TLS Certificate Expiry — countdown table
Panel 9:   PSTN Quota Headroom — gauge
Panel 10:  Pre-Advisory Signal Rate — counter over time
Panel 11:  Failover Signals Emitted — bar chart
Panel 12:  DNS Resolution Times — table
Panel 13:  Synthetic Call Test Results — status history
Panel 14:  Probe Failure Heatmap — all components × time
```

### 10.3 Alerts

```
ALERT: SipTrunkRegistrationLost
  condition: sip_trunk_registered == 0 for > 15s
  severity: critical
  notify: on-call + ops + Slack

ALERT: CompositeScoreDegraded
  condition: sip_infrastructure_composite_score < 75 for > 60s
  severity: warning
  notify: ops channel

ALERT: CompositeScoreCritical
  condition: sip_infrastructure_composite_score < 50 for > 30s
  severity: critical
  notify: on-call + ops + PagerDuty

ALERT: TlsCertExpiryWarning
  condition: tls_cert_days_remaining < 30
  severity: warning
  notify: ops email (daily)

ALERT: TlsCertExpiryCritical
  condition: tls_cert_days_remaining < 7
  severity: critical
  notify: on-call + Slack (every 6h)

ALERT: DnsIpMismatch
  condition: dns_ip_match == 0
  severity: CRITICAL — SECURITY
  notify: security + on-call + Wazuh SIEM

ALERT: PstnQuotaLow
  condition: pstn_quota_headroom_pct < 15
  severity: warning
  notify: ops + billing

ALERT: JitsiCapacityCritical
  condition: jitsi_conference_capacity_pct > 85
  severity: critical
  notify: ops + on-call

ALERT: AllSipPathsHighPacketLoss
  condition: sip_path_packet_loss_pct > 5 for ALL paths simultaneously
  severity: CRITICAL
  notify: on-call + ops + CALL_FAILOVER_AGENT signal

ALERT: MosScoreCritical
  condition: sip_path_mos_estimate < 2.5 for any path for > 30s
  severity: critical
  notify: ops

ALERT: SyntheticCallTestFailed
  condition: synthetic call test failure 2 consecutive runs
  severity: warning
  notify: ops channel
```

---

## ░░ SECTION 11 — SELF-HEALING BEHAVIORS ░░

```
SELF-HEAL SH-1: SIP REGISTRATION AUTO-RENEW
  Trigger:   sip_trunk_registered drops to 0
  Action:    Automatically send SIP REGISTER with fresh credentials.
             Retry up to 3 times at 5s intervals.
             If 3 failures → mark FAILED → notify CALL_FAILOVER_AGENT.

SELF-HEAL SH-2: PROBE BACKOFF ON PROVIDER RATE LIMIT
  Trigger:   X-RateLimit-Remaining < 20%
  Action:    Reduce probe cadence for that provider:
             Tier 1 → every 15s (was 5s)
             Tier 2 → every 90s (was 30s)
             Resume normal cadence when headroom > 50%.

SELF-HEAL SH-3: MONITOR SELF-RESTART ON PROBE ENGINE CRASH
  Trigger:   Any probe thread returns UNKNOWN state × 3
  Action:    Restart probe thread for that component.
             Log: probe.self_restart.{component_id}
             If restart fails → escalate to ops alert.

SELF-HEAL SH-4: DNS CACHE FLUSH ON MISMATCH
  Trigger:   dns_ip_match == 0 AND hijack confidence LOW
             (i.e., same IP from 2 independent resolvers)
  Action:    Flush local DNS cache.
             Re-probe after 10s.
             If mismatch persists → full Wazuh SIEM escalation.

SELF-HEAL SH-5: JITSI VB SECONDARY FAILOVER ADVISORY
  Trigger:   jitsi_conference_capacity_pct > 80% on primary JVB
  Action:    Emit: jitsi.capacity.warning
             Signal CALL_FAILOVER_AGENT to route new sessions
             to secondary JVB (JITSI-VB-02).
             Primary JVB remains active for existing sessions.
```

---

## ░░ SECTION 12 — SECURITY CONTROLS ░░

```
CONTROL SC-1: PROBE CREDENTIAL ISOLATION
  All probe credentials (SIP test accounts, TURN test tokens)
  stored in HashiCorp Vault.
  Retrieved per-probe-run via Vault transit.
  Test credentials are separate from production credentials.
  Test SIP accounts have zero call routing capability.

CONTROL SC-2: DNS HIJACK HARD BLOCK
  If dns_ip_match == 0 for any voice endpoint:
  → All SIP/PSTN operations immediately suspended on that path.
  → Human resolution required before path restored.
  → No self-heal for hijack (SC-4 does cache flush only
    for low-confidence mismatches).

CONTROL SC-3: TLS ONLY FOR PRODUCTION PROBES
  All production probes use TLS connections.
  Plain-text SIP (UDP 5060) monitored but flagged as DEGRADED
  if any production traffic detected on it.

CONTROL SC-4: PROBE RESULT SIGNING
  Each probe result signed with agent's Vault-managed key.
  Prevents probe result spoofing in Redis.
  Unsigned probe results rejected by health scorer.

CONTROL SC-5: WAZUH SIEM INTEGRATION
  Events forwarded to Wazuh:
    dns.hijack.suspected
    tls.cert.invalid
    tls.cert.expiry.emergency
    sip.trunk.failed (if consecutive > 5)
    sip_infrastructure_composite_score < 25

CONTROL SC-6: RBAC ON HEALTH API
  /sip-health/* endpoints:
    GET → ROLE: ops_viewer, ops_admin, system_service
    POST (synthetic test) → ROLE: ops_admin only
  No public access to health APIs.
  JWT enforced via Kong API Gateway.
```

---

## ░░ SECTION 13 — INTERN EXECUTION GUIDE ░░

### 13.1 Local Setup

```bash
# Step 1: Navigate to agent service
cd /backend/services/sip_health_monitor_agent/

# Step 2: Install dependencies
pip install -r requirements.txt --break-system-packages

# Step 3: Set up environment
cp .env.example .env

# Step 4: Required environment variables
REDIS_URL=redis://localhost:6379
POSTGRES_URL=postgresql://postgres:postgres@localhost:5432/ecoskiller
CLICKHOUSE_URL=http://localhost:8123
VAULT_ADDR=http://localhost:8200
VAULT_TOKEN=<dev-token>
PROMETHEUS_PORT=9103

# SIP probe config
SIP_TRUNK_01_URI=sip:sip-provider-1.ecoskiller.com
SIP_TRUNK_02_URI=sip:sip-provider-2.ecoskiller.com
SIP_TEST_USER=monitor@ecoskiller.com
SIP_TEST_PASS=<from vault>

# coturn probe config
COTURN_HOST_01=turn.ecoskiller.com
COTURN_PORT_UDP=3478
COTURN_PORT_TLS=5349
COTURN_TEST_USER=monitor
COTURN_TEST_PASS=<from vault>

# Jitsi probe config
JITSI_VB_01_URL=http://jitsi-vb-01:8080
JICOFO_01_URL=http://jicofo:8888

# PSTN probe config
PSTN_PROVIDER=twilio
PSTN_PING_URL=https://api.twilio.com/2010-04-01/Accounts/{SID}.json

# Step 5: Run the agent
uvicorn main:app --reload --port 8021

# Step 6: Verify health
curl http://localhost:8021/sip-health/status

# Expected:
# {
#   "composite_score": 95,
#   "overall_state": "HEALTHY",
#   "components": [...]
# }
```

### 13.2 Checking Component Health

```bash
# Check SIP trunk health
curl http://localhost:8021/sip-health/component/SIP-TRUNK-01

# Check TLS cert status
curl http://localhost:8021/sip-health/tls

# Check DNS status
curl http://localhost:8021/sip-health/dns

# Trigger a synthetic call test
curl -X POST http://localhost:8021/sip-health/synthetic-test/trigger \
  -H "Authorization: Bearer <test_token>" \
  -H "Content-Type: application/json" \
  -d '{"test_type": "SIP_OUTBOUND"}'

# View trend for Jitsi VB
curl "http://localhost:8021/sip-health/trend/JITSI-VB-01?window_minutes=15"

# View signals sent to CALL_FAILOVER_AGENT
curl "http://localhost:8021/sip-health/signals/history?minutes=60"
```

### 13.3 Expected Success Outputs

```
curl /sip-health/status → composite_score > 80, overall_state = HEALTHY
curl /sip-health/tls    → all days_remaining > 30 (clean env)
curl /sip-health/dns    → all ip_match = true
Prometheus /metrics     → all sip_component_health_score gauges present
Grafana dashboard       → all panels rendering with data
Probe logs (Loki)       → probe events every 5s for Tier 1 components
```

---

## ░░ SECTION 14 — ENFORCEMENT RULES ░░

```
RULE E1:
  SIP_HEALTH_MONITOR_AGENT must be running before any
  voice session (GD, Interview, Dojo) is permitted to start.
  Absence of agent health endpoint responding
  → BLOCK all new voice session creation.
  → Report: SIP_HEALTH_MONITOR_ABSENT to Admin Governance.

RULE E2:
  The agent must never make session routing decisions.
  It emits signals only.
  CALL_FAILOVER_AGENT owns all routing decisions.
  Violation → architectural integrity breach.

RULE E3:
  All probe credentials must come from HashiCorp Vault.
  Hardcoded probe credentials in any file
  → STOP EXECUTION of agent deployment.
  → Report: CREDENTIAL_GOVERNANCE_VIOLATION.

RULE E4:
  DNS hijack detection must result in immediate
  path suspension — no override, no bypass.
  Human resolution required before path restored.

RULE E5:
  TLS certificate expiry < 7 days must trigger P1 incident.
  Absence of cert monitoring
  → Flag in deployment pipeline (R10 Security compliance).

RULE E6:
  All probe results must be written to ClickHouse.
  Absence of time-series health data storage
  → considered observability failure (Section VIII violation).

RULE E7:
  Synthetic call tests must run every hour.
  Consecutive failures × 2 must emit ops alert.
  No exception for maintenance windows without
  explicit MAINTENANCE_MODE flag set in Redis.

RULE E8:
  The agent must expose /sip-health/status for
  readiness probe in Kubernetes.
  Kubernetes liveness probe: /sip-health/health (returns 200 always).
  Kubernetes readiness probe: /sip-health/status (returns 200 only when
  composite_score >= 50).
```

---

## ░░ SECTION 15 — RELATIONSHIP TO ECOSKILLER SYSTEM LAWS ░░

```
This agent is governed by and must comply with:

R10 — Security Policies
  TLS-only probes, Vault credentials, Wazuh integration.

R16 — Operations
  Agent monitoring dashboards, alert rules, incident response.

R21 — Internal Operations Console
  /sip-health/* endpoints exposed in Ops Console
  under "System Operations → Voice Infrastructure Health".

Section VIII — OBSERVABILITY (NON-OPTIONAL)
  Prometheus metrics, Loki logs, OpenTelemetry traces mandatory.

Section IX — SECURITY BASELINE
  WAF, rate limits, signed tokens, audit logs all enforced.

R60 — Long-Term Archival
  sip_component_health_snapshots retained per retention policy.

R62 — Public Transparency
  Anonymized uptime statistics may be published
  to transparency dashboard (opt-in, admin-controlled).

CALL_FAILOVER_AGENT v1.0-ANTIGRAVITY
  This agent is the detection layer.
  CALL_FAILOVER_AGENT is the action layer.
  Both must be deployed together.
  Neither operates alone.
```

---

## ░░ SECTION 16 — ANTIGRAVITY SEAL ░░

```
╔════════════════════════════════════════════════════════════════════╗
║       SIP_HEALTH_MONITOR_AGENT — ANTIGRAVITY PRODUCTION SEAL      ║
╠════════════════════════════════════════════════════════════════════╣
║  Agent:                SIP_HEALTH_MONITOR_AGENT                    ║
║  Version:              v1.0-ANTIGRAVITY                            ║
║  System:               ECOSKILLER UNIFIED SAAS                     ║
║  Companion:            CALL_FAILOVER_AGENT v1.0-ANTIGRAVITY        ║
║  Probe Tiers:          T1 (5s) · T2 (30s) · T3 (5min) · T4 (1h)  ║
║  Components Monitored: 18 (SIP/PSTN/coturn/Jitsi/DNS/TLS/Codec)  ║
║  Health States:        OPTIMAL·HEALTHY·DEGRADED·UNSTABLE·FAILED    ║
║  Signal Authority:     EMIT ONLY — No routing decisions            ║
║  Scoring Authority:    NONE                                        ║
║  Credential Source:    HashiCorp Vault ONLY                        ║
║  DNS Hijack:           HARD BLOCK — Human resolution required      ║
║  TLS Expiry <7 days:   P1 INCIDENT — Automatic escalation          ║
║  Self-Healing:         SH-1 through SH-5 (scoped, no routing)      ║
║  Audit Trail:          ClickHouse (metrics) + PostgreSQL (audit)   ║
║  Mutation Policy:      ADD-ONLY via version bump                   ║
║  Interpretation:       NONE PERMITTED                              ║
╠════════════════════════════════════════════════════════════════════╣
║  Absence of this agent in any voice infrastructure deployment      ║
║  → STOP VOICE SESSION LAUNCH                                      ║
║  → REPORT: SIP_HEALTH_MONITOR_ABSENT                              ║
║  → NO SESSION COMPLETION CLAIM PERMITTED                          ║
║                                                                    ║
║  Deployed WITHOUT CALL_FAILOVER_AGENT                              ║
║  → INCOMPLETE DEPLOYMENT                                          ║
║  → NO FAILOVER CAPABILITY CLAIM PERMITTED                         ║
╚════════════════════════════════════════════════════════════════════╝
```

---

## ░░ DOCUMENT LOCK ░░

```
Document Status:    SEALED
File:               SIP_HEALTH_MONITOR_AGENT__PSTN_BRIDGE__ECOSKILLER.md
Mutation Policy:    Add-Only — No line may be removed or altered
Version:            v1.0-ANTIGRAVITY
Authority:          Human declaration only
AI Generation Date: 2026-03-04
Parent System:      ECOSKILLER MASTER EXECUTION PROMPT v12.0+
Parent Reference:   Section H → Service #7 (Voice GD Orchestrator)
                    Section VIII → OBSERVABILITY (NON-OPTIONAL)
                    Section IX → SECURITY BASELINE
Companion Agent:    CALL_FAILOVER_AGENT v1.0-ANTIGRAVITY (required)
Governed By:        R10 Security · R16 Operations · R21 Ops Console
                    R60 Archival · R62 Transparency · Section VIII
```

---

*END OF SIP_HEALTH_MONITOR_AGENT SEALED PROMPT*
