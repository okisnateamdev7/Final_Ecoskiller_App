# 🔒 PHONE_INTERRUPTION_DETECTION_AGENT
## Sealed & Locked Enterprise Agent Specification
### Ecoskiller Antigravity Platform

---

## 1️⃣ AGENT IDENTITY (MANDATORY - SEALED)

```
AGENT_NAME = PHONE_INTERRUPTION_DETECTION_AGENT
SYSTEM_ROLE = Behavioral Signal Processor & Session Integrity Monitor
PRIMARY_DOMAIN = Voice GD, Interview, Dojo Match Audio Sessions
EXECUTION_MODE = Deterministic + Rule-Driven (ZERO AI judgment)
DATA_SCOPE = Session metadata, WebRTC telemetry, network diagnostics
TENANT_SCOPE = Strict Multi-Tenant Isolation (row-level security)
FAILURE_POLICY = HALT on ambiguity, LOG all failures, ESCALATE to Admin Governance Service
IMMUTABILITY_LEVEL = SEALED - No runtime logic modification allowed
VERSIONING_CONSTRAINT = Add-only, backward compatible, audit trail immutable
```

**SEALED CONSTRAINT:**
This agent definition is locked. No additions, deletions, or modifications permitted without:
1. Formal change request to Admin Governance Service
2. Audit trail entry with timestamp + actor ID
3. Version increment with backward compatibility proof
4. Full rollback capability documented

---

## 2️⃣ PURPOSE DECLARATION (SEALED)

### Problem This Agent Solves
- **Session Integrity Threats**: Detect when a participant receives an incoming phone call during a critical assessment (Voice GD, Interview, Dojo match)
- **Fairness Violation Prevention**: Identify interruptions that create unfair context switches affecting performance evaluation
- **Audit Compliance**: Maintain immutable record of all detected phone interruptions for dispute resolution and platform trust

### Input Consumed
- WebRTC telemetry events (network drops, codec shifts, jitter spikes)
- Session metadata (participant join time, role, session type)
- Audio stream metrics (silence patterns, sudden disconnections)
- Device state signals (mic hardware events, OS-level notifications)
- Application-level events (user refocus events, screen lock/unlock)

### Output Produced
- `INTERRUPTION_EVENT` with confidence score and evidence
- `SESSION_INTEGRITY_SIGNAL` for scoring adjustment decisions
- `AUDIT_LOG_ENTRY` for permanent immutable record
- `DOWNSTREAM_TRIGGER_EVENT` for notification service, admin governance

### Upstream Agent Dependency Chain
1. **Voice GD Orchestrator** → produces WebRTC session metrics
2. **Interview Service** → produces interview session events
3. **Dojo Match Engine** → produces match telemetry
4. **Analytics Service** (feeds observability events)

### Downstream Agent Consumption
1. **Scoring Engine** → receives SESSION_INTEGRITY_SIGNAL for penalty application
2. **Admin Governance Service** → receives INTERRUPTION_EVENT for disputes
3. **Notification Service** → receives alert events for recruiter/mentor
4. **Analytics Service** → consumes for anomaly trending
5. **Audit Service** → stores immutable logs

---

## 3️⃣ INPUT CONTRACT (STRICT - SEALED)

```
INPUT_SCHEMA: {
  session_id: {
    type: "uuid",
    required: true,
    validation: "must exist in PostgreSQL sessions table",
    domain_check: "must match tenant_id of calling service"
  },
  
  participant_id: {
    type: "uuid",
    required: true,
    validation: "must be active session participant",
    domain_check: "no cross-tenant lookups allowed"
  },
  
  session_type: {
    type: "enum",
    required: true,
    allowed_values: ["voice_gd", "interview", "dojo_match"],
    validation: "must match session metadata",
    failure_action: "REJECT with 400 INVALID_SESSION_TYPE"
  },
  
  timestamp_utc: {
    type: "iso8601_datetime",
    required: true,
    validation: "must be within session window ±5 seconds",
    domain_check: "must be monotonically increasing per session",
    failure_action: "LOG_ANOMALY, continue processing with server time"
  },
  
  webrtc_metrics: {
    type: "object",
    required: true,
    fields: {
      connection_state: {
        type: "enum",
        allowed: ["connected", "connecting", "disconnected", "failed"],
        required: true
      },
      
      audio_codec: {
        type: "string",
        required: true,
        validation: "must be valid WebRTC codec (opus, g711, etc)",
        change_detection: "alert if sudden shift"
      },
      
      jitter_ms: {
        type: "float",
        required: true,
        range: [0, 1000],
        anomaly_threshold: "> 150ms sustained for 5+ seconds"
      },
      
      packet_loss_percent: {
        type: "float",
        required: true,
        range: [0, 100],
        anomaly_threshold: "> 5% for 3+ seconds"
      },
      
      rtt_ms: {
        type: "float",
        required: true,
        range: [0, 500],
        anomaly_threshold: "sudden spike > 200ms or baseline × 2"
      },
      
      available_bandwidth_kbps: {
        type: "float",
        required: true,
        range: [0, 10000],
        anomaly_threshold: "drop > 70% from baseline"
      },
      
      audio_level_db: {
        type: "float",
        required: false,
        range: [-127, 0],
        usage: "optional, used for context only"
      },
      
      codec_switch_count: {
        type: "integer",
        required: true,
        range: [0, 100],
        anomaly_threshold: "> 3 switches in 30 seconds"
      }
    },
    validation_rules: [
      "at least one metric must be populated",
      "all numeric ranges enforced",
      "codec must be valid WebRTC standard"
    ]
  },
  
  network_events: {
    type: "array",
    required: true,
    items: {
      event_type: {
        type: "enum",
        allowed: [
          "network_disconnect",
          "network_reconnect",
          "ice_restart",
          "ssrc_mute",
          "hardware_mute",
          "stream_suspension"
        ],
        required: true
      },
      
      duration_ms: {
        type: "integer",
        required: true,
        range: [0, 300000],
        validation: "must be positive"
      },
      
      recovery_time_ms: {
        type: "integer",
        required: false,
        range: [0, 60000],
        usage: "time to restore connection after disconnect"
      }
    },
    validation_rules: [
      "events must be in chronological order",
      "no duplicate event types within 500ms window",
      "recovery_time must be <= 60 seconds"
    ]
  },
  
  device_state_signals: {
    type: "object",
    required: false,
    fields: {
      os_notification_detected: {
        type: "boolean",
        required: false,
        source: "browser permission API (limited access)"
      },
      
      screen_locked: {
        type: "boolean",
        required: false,
        source: "Screen Wake Lock API"
      },
      
      browser_visibility: {
        type: "enum",
        allowed: ["visible", "hidden", "unknown"],
        required: false
      },
      
      mic_hardware_changed: {
        type: "boolean",
        required: false,
        detection: "WebRTC deviceId change"
      },
      
      background_app_active: {
        type: "boolean",
        required: false,
        source: "Page Visibility API (best effort)"
      }
    },
    validation_rules: [
      "all fields optional - use for signal enrichment only",
      "do NOT assume causation from device signals alone",
      "combine with WebRTC metrics for decision threshold"
    ]
  },
  
  session_context: {
    type: "object",
    required: true,
    fields: {
      session_phase: {
        type: "enum",
        allowed: ["intro", "discussion", "rebuttal", "conclusion", "open_discussion", "interview_qa", "match_round"],
        required: true
      },
      
      elapsed_seconds: {
        type: "integer",
        required: true,
        range: [0, 3600],
        validation: "must match server-side timer"
      },
      
      speaking_turn_active: {
        type: "boolean",
        required: true,
        usage: "is participant currently holding speaking token?"
      },
      
      expected_duration_seconds: {
        type: "integer",
        required: true,
        range: [60, 3600],
        validation: "matches session_type baseline"
      },
      
      participant_role: {
        type: "enum",
        allowed: ["candidate", "interviewer", "mentor", "peer"],
        required: true,
        domain_check: "must match authenticated session role"
      }
    }
  },
  
  historical_baseline: {
    type: "object",
    required: true,
    fields: {
      avg_jitter_ms: {
        type: "float",
        required: true,
        validation: "computed from first 30 seconds of connection"
      },
      
      avg_packet_loss_percent: {
        type: "float",
        required: true,
        validation: "computed from first 30 seconds"
      },
      
      avg_rtt_ms: {
        type: "float",
        required: true,
        validation: "computed from first 30 seconds"
      },
      
      typical_bandwidth_kbps: {
        type: "float",
        required: true,
        validation: "median of stable periods"
      },
      
      device_model: {
        type: "string",
        required: false,
        usage: "context for device-specific anomalies"
      },
      
      network_type: {
        type: "enum",
        allowed: ["wifi", "cellular", "wired", "unknown"],
        required: true,
        detection: "WebRTC network adapter type"
      }
    },
    validation_rules: [
      "baseline computed once at session start",
      "immutable for duration of session",
      "all values must be non-negative"
    ]
  }
}

VALIDATION_RULES (MANDATORY):
  ✓ No null tolerance on REQUIRED fields → REJECT immediately
  ✓ Invalid enum values → REJECT with specific violation logged
  ✓ Numeric ranges enforced strictly → LOG_ANOMALY if violated
  ✓ Domain checks mandatory → verify tenant_id isolation
  ✓ Chronological order enforced → detect out-of-order events
  ✓ All validation failures logged to AUDIT_LOG with rejection reason
  ✓ Malformed input → return 400 with detailed error schema
  ✓ Missing required fields → return 400, do NOT infer

SECURITY_CHECKS (MANDATORY):
  ✓ Verify participant_id belongs to session_id
  ✓ Verify caller tenant_id matches session tenant_id
  ✓ Rate limit: max 1000 metric submissions per session per second
  ✓ Verify timestamp is recent (within 60 seconds of processing)
  ✓ Verify session_id exists and is active (not completed/cancelled)
  ✓ Verify role permissions (candidate can only submit own data)
```

---

## 4️⃣ OUTPUT CONTRACT (STRICT - SEALED)

```
OUTPUT_SCHEMA: {
  
  result_object: {
    type: "object",
    
    interruption_detected: {
      type: "boolean",
      required: true,
      semantics: "true = phone interruption with high confidence"
    },
    
    interruption_event: {
      type: "object",
      required_when: "interruption_detected = true",
      fields: {
        
        event_id: {
          type: "uuid",
          required: true,
          generation: "UUID v4 at processing time",
          immutability: "must be stored immutably"
        },
        
        severity_level: {
          type: "enum",
          required: true,
          allowed: ["CRITICAL", "HIGH", "MEDIUM", "LOW"],
          semantics: {
            CRITICAL: "phone call received during speaking turn in GD/interview",
            HIGH: "confirmed network disconnect pattern matching phone interrupt signature",
            MEDIUM: "suspicious metric anomalies correlating with interruption signals",
            LOW: "possible interruption but ambiguous evidence"
          }
        },
        
        interruption_type: {
          type: "enum",
          required: true,
          allowed: [
            "incoming_call_detected",
            "call_hold_pattern",
            "network_redirect",
            "device_context_switch",
            "app_backgrounding",
            "mic_hardware_disconnect"
          ],
          semantics: "mechanical classification of signal pattern"
        },
        
        confidence_score: {
          type: "float",
          required: true,
          range: [0, 1],
          semantics: "probability that input signals indicate phone interrupt",
          calculation: "deterministic scoring based on evidence weight",
          precision: "rounded to 0.01",
          rule: "score must be ≥ 0.75 for CRITICAL severity"
        },
        
        evidence_summary: {
          type: "object",
          required: true,
          fields: {
            
            primary_signal: {
              type: "string",
              required: true,
              examples: [
                "network_disconnect_duration_15000ms",
                "jitter_spike_from_45ms_to_220ms",
                "codec_switch_with_bandwidth_drop_80pct",
                "hardware_mute_followed_by_reconnect"
              ],
              semantics: "most significant metric anomaly"
            },
            
            supporting_signals: {
              type: "array",
              required: true,
              items: {
                type: "string",
                examples: [
                  "background_app_detected",
                  "screen_lock_event",
                  "multiple_ice_restarts",
                  "audio_silence_pattern_matching_hold_music"
                ]
              },
              max_items: 5,
              semantics: "corroborating evidence"
            },
            
            signal_weight_breakdown: {
              type: "object",
              required: true,
              fields: {
                network_discontinuity_weight: { type: "float", range: [0, 1] },
                metric_anomaly_weight: { type: "float", range: [0, 1] },
                device_state_weight: { type: "float", range: [0, 1] },
                temporal_clustering_weight: { type: "float", range: [0, 1] },
                pattern_matching_weight: { type: "float", range: [0, 1] }
              },
              validation: "all weights sum to 1.0"
            },
            
            duration_of_interruption_ms: {
              type: "integer",
              required: true,
              range: [100, 600000],
              semantics: "elapsed time from first signal to recovery"
            },
            
            recovery_quality: {
              type: "enum",
              required: true,
              allowed: ["clean_restore", "degraded_restore", "forced_rejoin", "session_impaired"],
              semantics: "assessment of audio/video quality post-interruption"
            }
          }
        },
        
        signal_timeline: {
          type: "array",
          required: true,
          items: {
            type: "object",
            fields: {
              offset_ms: { type: "integer", range: [0, 600000] },
              event_name: { type: "string" },
              metric_value: { type: "float" },
              baseline_value: { type: "float" },
              deviation_percent: { type: "float" }
            }
          },
          max_items: 100,
          semantics: "chronological sequence of metric changes leading to interruption"
        },
        
        excluded_hypotheses: {
          type: "array",
          required: true,
          items: {
            type: "object",
            fields: {
              hypothesis: { type: "string" },
              probability: { type: "float", range: [0, 1] },
              reason_excluded: { type: "string" }
            }
          },
          examples: [
            {
              hypothesis: "network degradation from ISP congestion",
              probability: 0.05,
              reason_excluded: "pattern inconsistent with sustained congestion; recovery too rapid and clean"
            },
            {
              hypothesis: "participant intentionally muted",
              probability: 0.02,
              reason_excluded: "hardware-level disconnect signature inconsistent with software mute"
            }
          ],
          semantics: "alternative explanations considered and weighted"
        },
        
        impact_assessment: {
          type: "object",
          required: true,
          fields: {
            session_phase_at_interruption: {
              type: "string",
              examples: ["intro", "discussion", "critical_rebuttal"]
            },
            
            speaking_turn_disrupted: {
              type: "boolean",
              semantics: "did this occur during participant's active speaking window?"
            },
            
            context_switch_severity: {
              type: "enum",
              allowed: ["none", "mild", "moderate", "severe"],
              semantics: "cognitive load of switching from session to phone call"
            },
            
            fairness_impact_score: {
              type: "float",
              range: [0, 1],
              semantics: "quantified unfair advantage/disadvantage relative to other participants"
            },
            
            recommended_action: {
              type: "enum",
              allowed: [
                "no_action",
                "flag_for_review",
                "apply_scoring_penalty",
                "apply_speaking_time_credit",
                "mark_session_disputed",
                "escalate_to_admin"
              ],
              semantics: "default recommendation for downstream Scoring Engine"
            }
          }
        },
        
        timestamp_utc: {
          type: "iso8601_datetime",
          required: true,
          semantics: "moment of interruption detection (not event occurrence)"
        },
        
        processing_latency_ms: {
          type: "integer",
          required: true,
          range: [10, 5000],
          semantics: "time from input receipt to detection output"
        }
      }
    },
    
    session_integrity_signal: {
      type: "object",
      required: true,
      fields: {
        
        integrity_score: {
          type: "float",
          required: true,
          range: [0, 1],
          semantics: "overall session health (1 = no concerns, 0 = critical failures)"
        },
        
        interruption_count: {
          type: "integer",
          required: true,
          range: [0, 100],
          semantics: "total phone interruptions detected in session"
        },
        
        total_interruption_duration_seconds: {
          type: "integer",
          required: true,
          range: [0, 3600],
          semantics: "cumulative seconds of detected interruption"
        },
        
        network_reliability_metric: {
          type: "float",
          required: true,
          range: [0, 1],
          semantics: "overall connection stability (independent of interruptions)"
        },
        
        signal_anomaly_density: {
          type: "float",
          required: true,
          range: [0, 1],
          semantics: "frequency of metric anomalies per minute of session"
        },
        
        flags: {
          type: "array",
          required: true,
          items: {
            type: "string",
            examples: [
              "multiple_interruptions_single_participant",
              "interruption_during_critical_phase",
              "device_switch_mid_session",
              "network_type_change_detected",
              "pattern_matches_known_exploit"
            ]
          }
        }
      }
    },
    
    audit_reference: {
      type: "uuid",
      required: true,
      semantics: "immutable reference to audit log entry"
    },
    
    next_trigger_events: {
      type: "array",
      required: true,
      items: {
        type: "object",
        fields: {
          event_type: {
            type: "enum",
            allowed: [
              "interruption_detected",
              "session_integrity_alert",
              "admin_review_required",
              "scoring_adjustment_pending",
              "notification_send"
            ]
          },
          
          target_service: {
            type: "enum",
            allowed: [
              "scoring_engine",
              "admin_governance_service",
              "notification_service",
              "analytics_service",
              "audit_service"
            ]
          },
          
          payload_reference: {
            type: "uuid",
            semantics: "reference to detailed event stored in message bus"
          },
          
          priority: {
            type: "enum",
            allowed: ["critical", "high", "normal", "low"]
          },
          
          deadline_seconds: {
            type: "integer",
            range: [1, 3600],
            semantics: "maximum time allowed before action must be taken"
          }
        }
      }
    },
    
    model_version: {
      type: "string",
      required: true,
      format: "semantic versioning (major.minor.patch)",
      examples: ["2.1.3"],
      immutability: "immutable for audit purposes"
    }
  },
  
  confidence_score: {
    type: "float",
    required: true,
    range: [0, 1],
    semantics: "overall confidence in this output",
    calculation: "median of all interruption event confidence scores",
    rounding: "0.01 precision"
  },
  
  model_version: {
    type: "string",
    required: true,
    format: "semantic versioning",
    semantics: "exact version used for this detection run"
  },
  
  audit_reference: {
    type: "uuid",
    required: true,
    semantics: "immutable audit log entry ID"
  },
  
  processing_metadata: {
    type: "object",
    required: true,
    fields: {
      received_timestamp_utc: { type: "iso8601_datetime" },
      processed_timestamp_utc: { type: "iso8601_datetime" },
      processing_duration_ms: { type: "integer" },
      total_metrics_analyzed: { type: "integer" },
      rules_evaluated: { type: "integer" },
      computation_warnings: { type: "array", items: { type: "string" } }
    }
  },
  
  next_trigger_event: {
    type: "array",
    required: true,
    items: {
      type: "object",
      fields: {
        event_type: { type: "string" },
        target_service: { type: "string" },
        priority: { type: "enum", allowed: ["critical", "high", "normal", "low"] },
        deadline_seconds: { type: "integer" }
      }
    },
    semantics: "structured events for downstream consumption"
  }
}

OUTPUT_GUARANTEES (NON-NEGOTIABLE):
  ✓ All outputs must include confidence score (0-1, 0.01 precision)
  ✓ All outputs must include model_version (immutable reference)
  ✓ All outputs must include audit_reference (UUID to audit log)
  ✓ All outputs must include next_trigger_events (for event-driven flow)
  ✓ All numeric values must be deterministically computed (reproducible)
  ✓ All enum values must match exactly (no abbreviations)
  ✓ Signal timeline must be complete (no gaps > 1 second)
  ✓ Excluded hypotheses must provide probabilistic weighting
  ✓ All timestamps must be ISO8601 UTC
  ✓ All outputs serializable to JSON-LD with @context
```

---

## 5️⃣ ML / AI LOGIC LAYER (SEALED - 90% TRADITIONAL ML, 10% SEMANTIC ASSIST)

### 5.1 DETERMINISTIC SIGNAL SCORING (90% ML)

```
DETECTION_ENGINE: Rule-Based Weighted Scoring
MODEL_TYPE: Gradient Boosted Decision Trees (GBDT) + Heuristic Rule Engine
FRAMEWORK: XGBoost (or LightGBM) for tabular metric signals
TRAINING_FREQUENCY: Weekly retraining on new session data
DRIFT_DETECTION: Mandatory monitoring

FEATURE_ENGINEERING (LOCKED):

Primary Features (directly from WebRTC metrics):
  1. jitter_delta = current_jitter - baseline_jitter
     - Weight: 0.25
     - Activation: delta > 3 × baseline
     - Signal: codec/network change OR phone call interference

  2. packet_loss_surge = current_loss - baseline_loss
     - Weight: 0.20
     - Activation: delta > 2% sustained for 3+ seconds
     - Signal: network redirect or call routing through same pipe

  3. rtt_volatility = max(rtt_last_10s) / median(rtt_baseline)
     - Weight: 0.20
     - Activation: ratio > 2.0
     - Signal: network congestion or dual routing

  4. bandwidth_collapse = (baseline_bandwidth - current_bandwidth) / baseline_bandwidth
     - Weight: 0.15
     - Activation: > 0.70 sustained
     - Signal: background app consuming bandwidth OR OS routing to cellular/WiFi simultaneously

  5. codec_instability = count(codec_switches_last_30s)
     - Weight: 0.10
     - Activation: count > 3
     - Signal: rapid failover attempt OR system trying multiple connection paths

  6. network_disconnect_duration = ms of full disconnection
     - Weight: 0.15
     - Activation: > 2000ms (2 seconds)
     - Signal: hardware-level interrupt (phone call seizing network interface)

  7. ice_restart_frequency = count(ICE_RESTART events in last 60s)
     - Weight: 0.10
     - Activation: > 2 restarts
     - Signal: connection re-negotiation under stress

  8. reconnection_pattern_match = distance to known phone_interrupt_signature
     - Weight: 0.12
     - Method: Euclidean distance in feature space from historical phone call patterns
     - Activation: distance < 0.85 similarity threshold

Secondary Features (contextual):

  9. speaking_turn_timing = seconds until/since turn start
     - Modifier: +0.15 if interruption occurs during active turn
     - Logic: phone calls during turn are more disruptive

  10. session_phase_criticality = weighted phase importance
      - Intro: 0.3
      - Discussion: 0.7
      - Rebuttal: 0.9
      - Conclusion: 0.6
      - Modifier: multiply confidence score by this weight

  11. temporal_clustering = count of anomalies within 5-second window
      - Activation: 3+ anomalies within 5s strongly indicate coordinated interrupt
      - Weight: 0.08

  12. device_state_coherence = agreement between device signals and network signals
      - Weight: 0.05
      - Logic: if os_notification + screen_lock + background_app all true, add 0.20 to confidence
      - Note: device signals weak alone, powerful in combination

DETERMINISTIC SCORING FORMULA (LOCKED):

```python
def score_phone_interrupt(features, baseline, context):
    """
    Sealed algorithm for phone interruption detection.
    """
    
    # Initialize score
    raw_score = 0.0
    
    # Signal 1: Jitter Spike (0-0.25)
    jitter_delta = features['jitter_delta']
    jitter_baseline = baseline['avg_jitter_ms']
    if jitter_delta > jitter_baseline * 3:
        jitter_score = min(0.25, (jitter_delta / (jitter_baseline * 5)))
    else:
        jitter_score = 0.0
    raw_score += jitter_score * 0.25
    
    # Signal 2: Packet Loss Surge (0-0.20)
    packet_loss_delta = features['packet_loss_percent'] - baseline['avg_packet_loss_percent']
    if packet_loss_delta > 2.0 and features['sustained_3sec']:
        packet_loss_score = min(0.20, packet_loss_delta / 10.0)
    else:
        packet_loss_score = 0.0
    raw_score += packet_loss_score * 0.20
    
    # Signal 3: RTT Volatility (0-0.20)
    rtt_ratio = features['rtt_ms'] / max(baseline['avg_rtt_ms'], 1)
    if rtt_ratio > 2.0:
        rtt_score = min(0.20, (rtt_ratio - 2.0) / 2.0 * 0.20)
    else:
        rtt_score = 0.0
    raw_score += rtt_score * 0.20
    
    # Signal 4: Bandwidth Collapse (0-0.15)
    bw_collapse = (baseline['typical_bandwidth_kbps'] - features['available_bandwidth_kbps']) / max(baseline['typical_bandwidth_kbps'], 100)
    if bw_collapse > 0.70:
        bandwidth_score = min(0.15, bw_collapse / 1.5)
    else:
        bandwidth_score = 0.0
    raw_score += bandwidth_score * 0.15
    
    # Signal 5: Codec Instability (0-0.10)
    codec_switches = features['codec_switch_count_last_30s']
    if codec_switches > 3:
        codec_score = min(0.10, (codec_switches - 3) / 10.0)
    else:
        codec_score = 0.0
    raw_score += codec_score * 0.10
    
    # Signal 6: Network Disconnect Duration (0-0.15)
    disconnect_duration = features['network_disconnect_duration_ms']
    if disconnect_duration > 2000:
        disconnect_score = min(0.15, disconnect_duration / 30000.0)
    else:
        disconnect_score = 0.0
    raw_score += disconnect_score * 0.15
    
    # Signal 7: ICE Restart Frequency (0-0.10)
    ice_restarts = features['ice_restart_frequency_last_60s']
    if ice_restarts > 2:
        ice_score = min(0.10, (ice_restarts - 2) / 5.0)
    else:
        ice_score = 0.0
    raw_score += ice_score * 0.10
    
    # Signal 8: Pattern Matching to Known Interruptions (0-0.12)
    # Uses GBDT model trained on historical phone call signatures
    gbdt_score = gbdt_model.predict(features)[0]
    raw_score += gbdt_score * 0.12
    
    # Context Modifiers
    # Modifier 1: Speaking Turn Active
    if context['speaking_turn_active']:
        raw_score *= 1.15
    
    # Modifier 2: Session Phase Criticality
    phase_weight = context['phase_criticality']
    raw_score *= (1.0 + phase_weight * 0.10)
    
    # Modifier 3: Temporal Clustering
    anomaly_cluster_count = features['anomalies_in_5sec_window']
    if anomaly_cluster_count >= 3:
        raw_score += 0.08 * (anomaly_cluster_count / 10.0)
    
    # Modifier 4: Device State Coherence (10% weight max)
    device_agreement = calculate_device_coherence(features)
    raw_score += min(0.10, device_agreement * 0.20)
    
    # Final Normalization
    final_score = min(1.0, raw_score)
    
    # Confidence Calculation
    confidence = final_score
    if confidence < 0.55:
        severity = "LOW"
    elif confidence < 0.75:
        severity = "MEDIUM"
    elif confidence < 0.85:
        severity = "HIGH"
    else:
        severity = "CRITICAL"
    
    return {
        'confidence_score': round(confidence, 2),
        'severity': severity,
        'component_scores': {
            'jitter': round(jitter_score, 3),
            'packet_loss': round(packet_loss_score, 3),
            'rtt': round(rtt_score, 3),
            'bandwidth': round(bandwidth_score, 3),
            'codec': round(codec_score, 3),
            'disconnect': round(disconnect_score, 3),
            'ice': round(ice_score, 3),
            'gbdt_pattern': round(gbdt_score, 3)
        }
    }
```

TRAINING DATA SPECIFICATION (LOCKED):

```
Training Dataset Composition:
  - Positive samples (confirmed phone interruptions): 5,000+ labeled sessions
  - Negative samples (network issues, ISP degradation): 20,000+ labeled sessions
  - Validation set: 2,000 random sessions with ground truth from admin review
  - Test set: 1,000 holdout sessions

Labeling Criteria for Positive Samples:
  ✓ Participant confirmed they received a phone call
  ✓ Timestamp of call receipt verified against session logs
  ✓ No competing explanations (network issue, intentional mute)
  ✓ Audio/network pattern signatures captured

Features Extracted for Training:
  ✓ All WebRTC metrics (jitter, packet loss, RTT, bandwidth)
  ✓ Network state transitions
  ✓ Device signals (when available)
  ✓ Temporal context (session phase, turn timing)
  ✓ Baseline metrics computed at session start
  ✓ Recovery patterns post-interruption

Training Algorithm: XGBoost with Cross-Validation
  - CV folds: 5
  - Early stopping patience: 50 rounds
  - Learning rate: 0.05
  - Max depth: 8
  - L2 regularization: 2.0

Performance Targets (LOCKED):
  - Precision (% of predicted positives that are true positives): ≥ 0.88
  - Recall (% of true positives caught): ≥ 0.82
  - F1-score: ≥ 0.85
  - AUC-ROC: ≥ 0.92

Failure Mode Analysis:
  - False positives: overly sensitive to ISP degradation → add feature: "provider_known_issues"
  - False negatives: misses subtle interruptions → increase jitter delta threshold
  - Drift: network changes over time → retrain weekly, monitor KL divergence

RETRAINING SCHEDULE (LOCKED):

Weekly Cycle:
  Monday 00:00 UTC: Extract last week's confirmed interruption labels
  Monday 06:00 UTC: Retrain GBDT model on expanded dataset
  Monday 12:00 UTC: Validation testing against holdout set
  Monday 18:00 UTC: If F1-score ≥ 0.85 and no performance regression, deploy
  If failure: rollback to previous version, escalate to data science team

Version Control:
  - Model versioning: model_v{YYYY}.{WW}.{revision}
  - Example: model_v2025.08.3 (week 8, revision 3)
  - Always store model_version in output for auditability
  - Keep last 4 versions for quick rollback

Drift Detection (MANDATORY):

  Trigger 1 - Distribution Shift:
    Monitor: KL divergence of inference features vs training features
    Threshold: KL divergence > 0.15
    Action: Flag in logs, plan retraining

  Trigger 2 - Performance Degradation:
    Monitor: Daily precision/recall on labeled subset
    Threshold: precision drops > 5% or recall drops > 8%
    Action: Halt model deployment, escalate

  Trigger 3 - Anomaly Spike:
    Monitor: % of sessions with uncertainty (confidence 0.45-0.65)
    Threshold: > 15% of sessions in uncertainty zone
    Action: Schedule emergency retraining
```

### 5.2 SEMANTIC REASONING LAYER (10% AI ASSIST)

**CRITICAL CONSTRAINT:** AI used ONLY to generate audit narratives and hypothesis exclusion explanations. NO decision-making authority.

```
AI_USAGE_SCOPE (LOCKED - READ-ONLY):

Purpose: Generate human-readable audit narrative from decision components
Input: Structured output from ML scoring layer
Output: Plain English explanation for dispute review

Prompt Template (SEALED):

---
# PHONE_INTERRUPT_AUDIT_NARRATIVE_GENERATOR

You are an audit narrative generator for a recruitment platform.
Your task is to explain WHY a phone interruption was detected, using ONLY these facts:

FACTS:
- Confidence score: {confidence}
- Severity: {severity}
- Primary signal: {primary_signal}
- Supporting signals: {supporting_signals}
- Session phase: {session_phase}
- Speaking turn active: {speaking_turn_active}
- Signal timeline: {timeline_json}

EXCLUDED HYPOTHESES (DO NOT DISPUTE):
{hypothesis_list}

RULES (NON-NEGOTIABLE):
1. Use only the provided facts
2. Do NOT add subjective judgment
3. Do NOT infer causation beyond what signals show
4. Do NOT make recommendations for action
5. Use passive voice for objectivity
6. Maximum 200 words
7. Format as a single paragraph

Task: Generate a concise audit narrative explaining the detection.
---

Output Format (LOCKED):

Detected signals indicate a phone interruption with {severity} confidence
({confidence_score}%). {primary_signal_explanation}. 
{supporting_signals_context}. 
Competing explanations were excluded: {hypothesis_rejection}. 
This occurred during {session_phase} when {participant_context}.
The session's integrity score was {integrity_impact}.

AI LIMITATIONS (MANDATORY):
  ✗ AI cannot override ML confidence score
  ✗ AI cannot change severity classification
  ✗ AI cannot recommend action to scoring engine
  ✓ AI can only provide narrative context for human review
  ✓ AI output is advisory only, never authoritative

Versioning: AI prompt versioning kept separate from ML model versioning
  - Format: prompt_v{YYYY}.{WW}
  - Stored in Git with full audit trail
  - All prompt changes require review + documentation
```

---

## 6️⃣ SCALABILITY DESIGN (LOCKED)

```
HORIZONTAL SCALING CONSTRAINTS:

Performance Targets:
  EXPECTED_RPS (Requests Per Second) = 50,000 RPS
    Justification: 10M users × 0.5% in active sessions = 50k simultaneous events
  
  LATENCY_TARGET = P99 < 500ms
    - Input validation: < 50ms
    - Feature computation: < 150ms
    - ML inference: < 200ms
    - Output serialization: < 100ms
  
  MAX_CONCURRENCY = 100,000 concurrent metric submissions
    - Auto-scaling: 200 container instances (250 RPS each)
    - Burst handling: Redis queue with 10-second backpressure
  
  QUEUE_STRATEGY = Apache Kafka (event-driven)
    - Topic: events.phone_interruption_metrics
    - Partitions: 100 (by session_id hash)
    - Replication factor: 3
    - Retention: 7 days (for training data)

Stateless Execution (REQUIRED):
  ✓ No session state stored in agent memory
  ✓ All state lives in Redis (baseline metrics, running interrupt count)
  ✓ Queries to PostgreSQL only for validation (cached via Redis)
  ✓ Each request is independently processable
  ✓ Container restart = zero data loss

Event-Driven Triggers:
  Inbound events from:
    - Voice GD Orchestrator: gd.metrics_event (every 1 second per participant)
    - Interview Service: interview.webrtc_telemetry
    - Dojo Match Engine: match.audio_metrics
  
  Outbound events to:
    - Kafka topic: interruptions.detected
    - Redis pubsub: channel interrupt:{session_id} (for real-time alerts)

Async Processing (REQUIRED):
  ✓ Input → Kafka → Agent (streaming)
  ✓ Agent → Output event → Kafka (asynchronous)
  ✓ No blocking waits for downstream consumers
  ✓ Timeout: 5 seconds max per metric batch

Idempotent Operations (REQUIRED):
  ✓ Detection output includes event_id = UUID (deterministic per input)
  ✓ Duplicate metric submissions produce identical output
  ✓ Scoring engine can safely reprocess events without duplication
  ✓ IDEMPOTENCY_KEY = hash(session_id + timestamp_utc + metric_snapshot)

Caching Strategy:
  Redis Caching (Session Baselines):
    Key: baseline:session:{session_id}
    Value: {avg_jitter, avg_packet_loss, avg_rtt, typical_bandwidth, network_type}
    TTL: 86400 seconds (session duration + 24 hours)
    Invalidation: Manual on session_completed event

  Prometheus Metrics Cache:
    - Detection confidence histogram (p50, p90, p99)
    - Interrupt severity distribution
    - Latency percentiles
    - Cache invalidation: 60 seconds (Prometheus scrape interval)
```

---

## 7️⃣ SECURITY ENFORCEMENT (SEALED - ZERO-TRUST)

```
SECURITY LAYERS (NON-NEGOTIABLE):

Layer 1: Tenant Isolation (Database)
  ✓ All queries filtered by WHERE tenant_id = authenticated_tenant_id
  ✓ Row-level security (RLS) enforced at PostgreSQL level
  ✓ No cross-tenant queries permitted
  ✓ Violation logging → CRITICAL alert → escalate to security team

Layer 2: Domain Isolation (Application)
  ✓ Agent only processes sessions from its own domain (voice_gd, interview, dojo_match)
  ✓ Session type validated against input
  ✓ Cross-domain requests rejected with 400

Layer 3: Role-Based Authorization (RBAC)
  ✓ Participants can ONLY submit metrics for their own session
  ✓ Recruiters/mentors can REQUEST interruption reports
  ✓ Admins can QUERY disputes
  ✓ No participant can access other participant's metrics
  
  Roles:
    CANDIDATE: read own session metrics
    RECRUITER: read interruption reports for own sessions
    MENTOR: read interruption reports for own matches
    ADMIN: read all interruption reports + full audit log
    SYSTEM: (internal only) can trigger agent via trusted service boundary

Layer 4: Encryption Enforcement
  ✓ TLS 1.3 for all network communication (in-flight)
  ✓ At-rest encryption: PostgreSQL column encryption for PII
  ✓ Redis: encrypted backups, AUTH token required
  ✓ Kafka: SASL/SCRAM authentication, encrypted topics

Layer 5: Audit Logging (Append-Only)
  ✓ Every detection attempt logged to immutable audit log
  ✓ Log entry includes: timestamp, actor_id, input, output, decision_rationale
  ✓ Logs stored in PostgreSQL with sequence numbers
  ✓ Cannot be modified or deleted (immutability enforced at schema level)
  ✓ Backup: replicated to immutable archive (S3 with object lock)

Layer 6: Rate Limiting (Per User, Per Session, Per IP)
  ✓ Max 1,000 metric submissions per session per second
  ✓ Max 100,000 detection requests per recruiter per day
  ✓ Max 10,000 detection requests per IP per hour
  ✓ Violation: return 429 Too Many Requests, log to security team

Layer 7: Input Validation (Strict)
  ✓ All inputs validated against schema (see Input Contract, Section 3)
  ✓ No null tolerance on required fields
  ✓ Enum values validated exactly (case-sensitive)
  ✓ Numeric ranges enforced
  ✓ Malformed input: reject immediately, log violation

Layer 8: API Gateway Security (Kong)
  ✓ Request signing: X-Signature header (HMAC-SHA256)
  ✓ Token validation: JWT with 15-minute expiry
  ✓ Origin verification: whitelist internal services only
  ✓ DDoS protection: rate limit by IP (10,000 req/min)
```

---

## 8️⃣ AUDIT & TRACEABILITY (LOCKED - IMMUTABLE)

```
MANDATORY AUDIT LOG ENTRY:

Every execution must generate an immutable audit entry:

```sql
INSERT INTO audit_logs (
  audit_id,                          -- UUID v4
  timestamp_utc,                     -- ISO8601
  service_name,                      -- 'phone_interruption_detection_agent'
  actor_id,                          -- UUID of calling service
  actor_type,                        -- 'system', 'recruiter', 'mentor', 'admin'
  action,                            -- 'detect_interruption'
  session_id,                        -- UUID of GD/interview/dojo session
  participant_id,                    -- UUID of affected participant
  tenant_id,                         -- UUID for isolation
  
  input_hash,                        -- SHA256(serialized_input)
  input_metadata: {
    metric_count,                    -- count of WebRTC metrics
    network_event_count,
    baseline_computed
  },
  
  output_hash,                       -- SHA256(serialized_output)
  output_metadata: {
    interruption_detected,
    confidence_score,
    severity_level
  },
  
  model_version,                     -- 'model_v2025.08.3'
  model_confidence,
  
  decision_path: {
    primary_signal_fired,            -- which threshold crossed first
    component_scores: {...},         -- all ML component scores
    context_modifiers: {...},        -- which modifiers applied
    final_score_computation
  },
  
  processing_latency_ms,
  
  anomaly_flags: [
    'uncertainty_high',              -- confidence between 0.45-0.65
    'pattern_unusual',               -- far from known interruption signatures
    'device_signal_contradiction'    -- device signals disagree with network signals
  ],
  
  disposition,                       -- 'accepted' | 'escalated' | 'rejected'
  escalation_reason,                 -- if applicable
  
  status: 'completed' | 'failed' | 'timeout'
)
```

Immutability Enforcement:
  ✓ audit_id + timestamp together form primary key
  ✓ No UPDATE permitted (database schema enforces)
  ✓ Only INSERT and SELECT allowed
  ✓ Retention: permanent (no deletion policy)
  ✓ Backup: daily immutable snapshot to S3 with Object Lock (7-year retention)

Audit Access Control:
  ✓ Admins: full audit log access
  ✓ Recruiters: only own session audit entries
  ✓ Participants: can request their own session audit
  ✓ Queries: logged and monitored for suspicious patterns

Query Audit:
  ✓ Every query against audit log is itself logged
  ✓ Who accessed what, when, why
  ✓ Stored in separate immutable audit_access_log
```

---

## 9️⃣ FAILURE POLICY (SEALED - NO SILENT FAILURES)

```
FAILURE SCENARIOS & HANDLING (DETERMINISTIC):

Scenario 1: Invalid Input (Malformed Data)
  Trigger: Input fails schema validation
  Action: 
    ✓ STOP execution
    ✓ LOG_FAILURE to audit_logs with rejection reason
    ✗ Do NOT proceed with detection
    ✓ Return 400 Bad Request with detailed error schema
    ✓ Escalate to calling service: "invalid_input_rejected"

Scenario 2: Model Unavailable (ML Service Down)
  Trigger: GBDT inference service timeout (> 5 seconds)
  Action:
    ✓ STOP execution
    ✓ LOG_INCIDENT: "model_inference_timeout"
    ✓ Return 503 Service Unavailable
    ✓ ESCALATE to: ops_team via PagerDuty
    ✓ Fallback: none (no fallback detection; safety > availability)

Scenario 3: AI Timeout (Audit Narrative Generation)
  Trigger: AI semantic layer exceeds 2-second timeout
  Action:
    ✓ CONTINUE execution (skip narrative generation)
    ✓ Output struct will have narrative_generation_failed = true
    ✓ LOG_WARNING: audit narrative skipped
    ✓ Downstream: skip narrative in dispute UI
    ✓ No escalation (non-critical path)

Scenario 4: Data Corruption (Hash Mismatch)
  Trigger: Input hash differs from recomputed hash
  Action:
    ✓ HALT immediately
    ✓ LOG_CRITICAL: "input_integrity_failure"
    ✓ ESCALATE to: security team + admin governance
    ✓ Response: 500 Internal Server Error
    ✓ Investigation: check network layer, replay detection

Scenario 5: Confidence Below Safety Threshold
  Trigger: Confidence score < 0.40 after all computation
  Action:
    ✓ STOP detection (insufficient evidence)
    ✓ Return output with interruption_detected = false
    ✓ Set flag: ambiguous_signals_detected = true
    ✓ Log as UNCERTAINTY_FLAG
    ✗ Do NOT emit interruption_detected event

Scenario 6: Database Connection Failure
  Trigger: PostgreSQL connection timeout or failure
  Action:
    ✓ HALT execution
    ✓ Retry policy: exponential backoff (3 retries, max 5 seconds)
    ✓ If all retries fail: ESCALATE to ops_team
    ✓ Response: 503 Service Unavailable
    ✓ Log: all retry attempts with timestamps

Scenario 7: Redis Baseline Lookup Failure
  Trigger: Session baseline metrics not found in Redis
  Action:
    ✓ RETRY from PostgreSQL (fallback store)
    ✓ If PostgreSQL also missing: LOG_WARNING
    ✓ Proceed with fallback baseline computation (use population statistics)
    ✓ Flag: baseline_source = "fallback" in audit log

Scenario 8: Kafka Event Publishing Failure
  Trigger: Cannot publish interruption_detected event to Kafka
  Action:
    ✓ RETRY: 5 attempts with exponential backoff (max 10 seconds total)
    ✓ If all retries fail: 
      - STORE event in Redis queue for async replay
      - LOG_CRITICAL: "event_queue_overflow"
      - ESCALATE to: messaging_team
    ✓ Response to caller: 202 Accepted (event queued)

Scenario 9: Tenant Isolation Violation Detected
  Trigger: Session tenant_id ≠ request tenant_id
  Action:
    ✓ HALT immediately
    ✓ Return 403 Forbidden
    ✓ LOG_CRITICAL: "tenant_isolation_violation"
    ✓ ESCALATE to: security team (potential attack)
    ✓ Incident severity: P1

Scenario 10: Rate Limit Exceeded
  Trigger: > 1000 metric submissions per session per second
  Action:
    ✓ STOP processing (queue full)
    ✓ Return 429 Too Many Requests
    ✓ Include Retry-After header (backoff guidance)
    ✓ LOG_WARNING: rate limit hit
    ✓ No escalation (expected under peak load)

ALL FAILURES MUST:
  ✓ Be logged with full context (input, reason, actor)
  ✓ Include timestamp + audit reference
  ✓ Never silently fail (always return error to caller)
  ✓ Generate observable metrics (for dashboards)
  ✓ Trigger escalation rules (ops, security, governance)
```

---

## 🔟 INTER-AGENT DEPENDENCY MAP (SEALED)

```
UPSTREAM AGENTS (Providers):
  1. Voice GD Orchestrator
     Output: gd.metrics_event
     Frequency: 1 event per second per participant
     Contains: WebRTC metrics, network events, session metadata
     SLA: P99 latency < 100ms to our agent
  
  2. Interview Service
     Output: interview.webrtc_telemetry
     Frequency: 1 event per 2 seconds per participant
     Contains: Same WebRTC metrics + interview context
     SLA: guaranteed delivery (Kafka partition guarantee)
  
  3. Dojo Match Engine
     Output: match.audio_metrics
     Frequency: 1 event per 1 second per participant
     Contains: Match-specific metrics + role context
     SLA: P99 latency < 200ms

DOWNSTREAM AGENTS (Consumers):
  1. Scoring Engine (CRITICAL DEPENDENCY)
     Input: interruption_detected event
     Action: Applies scoring penalties/credits
     SLA: Must consume within 60 seconds of event emission
     Contract: SESSION_INTEGRITY_SIGNAL with impact_assessment
  
  2. Admin Governance Service (CRITICAL)
     Input: interruption_event with HIGH or CRITICAL severity
     Action: Flags session for dispute review
     SLA: Must consume within 300 seconds
     Contract: Full audit trail for dispute investigation
  
  3. Notification Service
     Input: interruption_alert events
     Action: Sends alerts to recruiter/mentor
     SLA: P99 latency < 2 seconds (user-facing)
     Contract: JSON event with all context for message templating
  
  4. Analytics Service
     Input: All detection events (for trending)
     Action: Consumes for dashboards, anomaly detection
     SLA: Eventually consistent (batched hourly)
     Contract: Structured event stream to ClickHouse
  
  5. Audit Service
     Input: audit_reference UUID
     Action: Retrieves immutable audit log entry
     SLA: < 500ms response time
     Contract: Full audit context for appeals/disputes

EVENT FLOW DIAGRAM (LOCKED):

```
┌─────────────────────────────────────────────────────────────────┐
│                  UPSTREAM METRIC PRODUCERS                      │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│  Voice GD Orchestrator ──┐                                       │
│  Interview Service ──────┼──→ Kafka Topic: events.phone_metrics │
│  Dojo Match Engine ──────┘                                       │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
                                    │
                                    │ Kafka Consumer Group
                                    ▼
┌─────────────────────────────────────────────────────────────────┐
│      PHONE_INTERRUPTION_DETECTION_AGENT                         │
│  ┌─────────────────────────────────────────────────────────┐   │
│  │ 1. Input Validation & Normalization                     │   │
│  │ 2. Feature Engineering (WebRTC → ML Features)           │   │
│  │ 3. GBDT ML Model Inference                              │   │
│  │ 4. Threshold Decision (severity classification)         │   │
│  │ 5. AI Semantic Narrative Generation (async)             │   │
│  │ 6. Output Construction & Serialization                  │   │
│  │ 7. Audit Log Entry Creation (immutable)                 │   │
│  │ 8. Event Emission to Kafka                              │   │
│  └─────────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────────┘
                                    │
                    ┌───────────────┼───────────────┐
                    │               │               │
                    ▼               ▼               ▼
    ┌──────────────────┐  ┌─────────────────┐  ┌─────────────┐
    │ Scoring Engine   │  │ Admin Governance│  │ Notification│
    │                  │  │ Service         │  │ Service     │
    │ Applies penalties│  │ Flags disputes  │  │ Alerts users│
    │ for unfair       │  │ Manages reviews │  │             │
    │ advantages       │  │ Enforces policy │  │             │
    └──────────────────┘  └─────────────────┘  └─────────────┘
                    │               │               │
                    └───────────────┼───────────────┘
                                    │
                                    ▼
                    ┌──────────────────────────┐
                    │ Analytics Service        │
                    │ (aggregates for trends)  │
                    └──────────────────────────┘
                                    │
                                    ▼
                    ┌──────────────────────────┐
                    │ ClickHouse (Data Lake)   │
                    │ (historical analysis)    │
                    └──────────────────────────┘
```

INTER-SERVICE CONTRACTS (SEALED):

  Contract 1: Voice GD Orchestrator → Phone Interrupt Agent
    Event: gd.metrics_event
    Schema: See Input Contract (Section 3)
    Retry: Kafka guaranteed delivery (3 partitions × 3 replicas)
    Timeout: Agent must return result within 2 seconds
  
  Contract 2: Phone Interrupt Agent → Scoring Engine
    Event: interruption_detected
    Payload: SESSION_INTEGRITY_SIGNAL + INTERRUPTION_EVENT
    Guarantee: At-least-once delivery via Kafka
    SLA: Scoring must consume within 60 seconds
  
  Contract 3: Phone Interrupt Agent → Admin Governance Service
    Event: session_requires_dispute_review
    Payload: Complete interruption_event with evidence
    Triggers: Only on CRITICAL or HIGH severity
    SLA: Admin must be notified within 300 seconds
  
  Contract 4: Phone Interrupt Agent → Notification Service
    Event: notify_stakeholders
    Payload: Recruiter ID + notification template name
    QoS: Best effort (non-critical path)
    SLA: P99 < 2 seconds to user
```

---

## 1️⃣1️⃣ PASSIVE INTELLIGENCE COMPATIBILITY (OPTIONAL INTEGRATION)

```
FEATURE VECTOR EMISSION (IF INTEGRATED):

If the platform uses passive intelligence scoring, this agent MAY emit
feature vectors for ML model training.

FEATURE_VECTOR_SCHEMA (LOCKED):

EMIT_WHEN: Session completes and interruption detection occurred

FEATURE_STRUCTURE:
{
  user_id: UUID,
  feature_name: "phone_interruption_event_count",
  feature_value: integer (0-N),
  timestamp: ISO8601,
  source_agent: "phone_interruption_detection_agent",
  model_version: semantic_version,
  
  context: {
    session_type: "voice_gd" | "interview" | "dojo_match",
    session_id: UUID,
    session_duration_seconds: integer,
    
    interruption_metadata: {
      total_interruptions: integer,
      total_duration_seconds: integer,
      severity_levels: [CRITICAL, HIGH, MEDIUM, LOW],
      average_confidence: float,
      recovery_quality: "clean_restore" | "degraded_restore" | "forced_rejoin" | "session_impaired"
    }
  }
}

INTEGRATION RULES:
  ✓ Only emit if interruptions occurred (interruption_count > 0)
  ✓ Emit once per session completion
  ✓ Do NOT emit during session (avoid noise)
  ✓ Feature vectors are ADVISORY only for passive intelligence
  ✓ Scoring decisions are independent (made by Scoring Engine)

DOWNSTREAM PASSIVE INTELLIGENCE CONSIDERATIONS:
  - High interruption frequency may correlate with external stressors
  - Pattern matching: repeated interruptions may indicate work environment issues
  - Predictive: past interruption patterns may forecast performance variance
  
NOTE: This is informational only. Phone interruptions do NOT lower
intelligence scores; they remain a separate factor for scoring fairness.
```

---

## 1️⃣2️⃣ INNOVATION ECONOMY COMPATIBILITY (NOT APPLICABLE)

```
This agent does not touch the innovation economy layer.
Phone interruptions are session-integrity concerns, not idea-related.
No integration required with:
  - IDEA_VECTOR
  - SIMILARITY_HASH
  - ORIGINALITY_SCORE
  - IDEA_DNA_AGENT
  - ROYALTY_ENGINE
  - COPY_DETECTION_ENGINE
```

---

## 1️⃣3️⃣ GROWTH ENGINE HOOK (OPTIONAL TRIGGER)

```
IF the platform uses growth/achievement mechanics, this agent MAY signal
activity participation issues.

GROWTH_ENGINE_SIGNAL (OPTIONAL):

  Event: session_integrity_compromised
  Target: Growth Engine Service
  
  Trigger: When interruption_count > 2 OR total_interruption_duration > 120 seconds
  
  Payload: {
    user_id: UUID,
    session_type: enum,
    participation_disruption_score: float (0-1),
    fairness_impact: float (0-1)
  }
  
  Growth Engine Action:
    - Do NOT penalize XP for interruptions (out of user control)
    - MAY trigger: "Environmental Challenge" badge
    - MAY credit: "Resilience under Interruption" achievement
    - Do NOT adjust ranking (separate track)

RULE: Phone interruptions are EXTERNAL factors, not performance deficits.
Growth mechanics must account for this fairness constraint.
```

---

## 1️⃣4️⃣ PERFORMANCE MONITORING (LOCKED - PROMETHEUS)

```
MANDATORY METRICS (SEALED):

Prometheus Metrics Exposed:

1. Detection Metrics:
   phone_interruption_detections_total
     label: severity (CRITICAL, HIGH, MEDIUM, LOW)
     label: session_type (voice_gd, interview, dojo_match)
     label: model_version
   
   phone_interruption_confidence_histogram
     buckets: [0.0, 0.2, 0.4, 0.6, 0.8, 1.0]
     quantiles: [0.50, 0.90, 0.99]
   
   phone_interruption_severity_distribution
     gauge by severity level

2. Performance Metrics:
   phone_interrupt_detection_latency_seconds
     quantiles: [0.50, 0.90, 0.99]
   
   phone_interrupt_ml_inference_time_seconds
     histogram (GBDT inference duration)
   
   phone_interrupt_ai_narrative_time_seconds
     histogram (optional semantic layer)
   
   phone_interrupt_input_validation_failures_total
     label: failure_reason
   
   phone_interrupt_processing_errors_total
     label: error_type

3. Quality Metrics:
   phone_interrupt_model_drift_kl_divergence
     gauge (KL divergence of inference features vs training)
   
   phone_interrupt_uncertainty_rate
     gauge (% of detections with 0.45-0.65 confidence)
   
   phone_interrupt_baseline_cache_hit_rate
     gauge

4. Dependency Metrics:
   phone_interrupt_kafka_consumer_lag
     gauge (lag in event processing)
   
   phone_interrupt_redis_lookup_duration_seconds
     histogram
   
   phone_interrupt_postgres_query_duration_seconds
     histogram

Alerting Rules (PagerDuty Integration):

  Alert 1: Model Inference Timeout
    Condition: p99(ml_inference_time) > 500ms
    Severity: P2
    Action: Page on-call ML engineer
  
  Alert 2: Input Validation Failure Spike
    Condition: rate(validation_failures_total[5m]) > 10
    Severity: P3
    Action: Log to monitoring team
  
  Alert 3: Kafka Consumer Lag > 30 seconds
    Condition: kafka_consumer_lag > 30000ms
    Severity: P2
    Action: Page SRE
  
  Alert 4: Detection Latency P99 > 1 second
    Condition: p99(detection_latency) > 1000ms
    Severity: P3
    Action: Alert ops team
  
  Alert 5: High Uncertainty Rate (> 20%)
    Condition: uncertainty_rate > 0.20
    Severity: P2
    Action: Page data science team (possible model drift)
  
  Alert 6: Model KL Divergence Spike
    Condition: kl_divergence > 0.15
    Severity: P1
    Action: Page ML team + pause new deployments

Grafana Dashboards (LOCKED):

  Dashboard 1: Agent Health
    - Throughput (RPS)
    - Latency P50/P90/P99
    - Error rate
    - Input validation failures
    - Kafka lag
  
  Dashboard 2: Detection Patterns
    - Severity distribution over time
    - Session type breakdown
    - Confidence score distribution
    - Uncertainty rate trend
  
  Dashboard 3: Model Quality
    - KL divergence monitoring
    - Precision/Recall trend
    - Feature drift signals
    - Retraining schedule + last update
  
  Dashboard 4: Business Metrics
    - Interruptions per session type
    - Average interruption duration
    - Repeat offenders (users with 3+ interruptions)
    - Session integrity score distribution
```

---

## 1️⃣5️⃣ VERSIONING POLICY (SEALED - IMMUTABLE)

```
VERSIONING CONSTRAINT (NON-NEGOTIABLE):

All Changes: Add-Only, Never Destructive

Semantic Versioning Scheme:
  Format: major.minor.patch
  Example: 2.1.3
  
  MAJOR: Breaking changes to input/output schema
         Requires full system re-integration
         Backward compatibility NOT guaranteed
  
  MINOR: New features, new optional fields
         Fully backward compatible
         Existing clients continue to work
  
  PATCH: Bug fixes, performance improvements
         Zero breaking changes
         Automatic deployment (no approval needed)

Change Process (LOCKED):

  1. PROPOSAL PHASE
     - Draft change proposal with migration plan
     - Run impact analysis
     - Get approval from: Tech Lead + Compliance Officer
  
  2. IMPLEMENTATION PHASE
     - Create feature branch (git)
     - Implement changes
     - Update unit tests + integration tests
     - Add new logic (never delete old logic if breaking)
  
  3. VALIDATION PHASE
     - Run full test suite (coverage ≥ 95%)
     - Performance benchmark (latency target verified)
     - Security scan (static analysis, dependency check)
     - Data science validation (model performance maintained)
  
  4. DEPLOYMENT PHASE
     - Deploy to staging (blue/green)
     - Run smoke tests
     - Monitor for 24 hours
     - If green: deploy to production (canary 5% → 50% → 100%)
     - If red: rollback immediately
  
  5. DOCUMENTATION PHASE
     - Update this agent spec (CHANGELOG section)
     - Update API documentation
     - Train support team
     - Add migration guide if breaking change

Backward Compatibility Rules (MANDATORY):

  ✓ Old input versions must still be accepted
  ✓ Old output versions must still be producible (if requested)
  ✓ New fields in input: optional (with defaults)
  ✓ New fields in output: always included (never remove)
  ✓ Enum expansions: safe (add new values)
  ✓ Field deletions: forbidden (mark as deprecated instead)
  ✓ Type changes: forbidden (create new field instead)

Version Tracking (IMMUTABLE):

  Every output includes:
    - model_version (GBDT + feature engineering version)
    - agent_version (this agent spec version)
    - ai_prompt_version (semantic narrative generation version)
  
  All versions stored in audit log for reproducibility.

Rollback Capability (REQUIRED):

  Keep last 4 versions deployable at all times:
    v2.1.3 (current)
    v2.1.2
    v2.1.1
    v2.1.0
  
  If critical bug discovered in v2.1.3:
    1. Rollback to v2.1.2 (< 5 minutes)
    2. Investigate root cause
    3. Fix in v2.1.4
    4. Deploy v2.1.4 (canary)
```

---

## 1️⃣6️⃣ NON-NEGOTIABLE RULES (SEALED & LOCKED)

```
FORBIDDEN ACTIONS (ABSOLUTE CONSTRAINTS):

This agent MUST NOT:

  ✗ Create hidden logic not documented in this spec
  ✗ Modify historical audit records (append-only only)
  ✗ Auto-delete or expire audit logs (permanent retention)
  ✗ Override decisions from Admin Governance Service
  ✗ Bypass tenant isolation checks (any cross-tenant query)
  ✗ Execute outside declared scope (only phone interruptions in GD/interview/dojo)
  ✗ Make scoring decisions (only flag for Scoring Engine)
  ✗ Persist state in agent memory (stateless only)
  ✗ Use AI for decision-making (only for audit narratives)
  ✗ Apply confidence scores < 0.40 to production decisions
  ✗ Retry failed detections > 5 times (explicit policy)
  ✗ Emit interruption events on low confidence (ambiguity = halt)
  ✗ Mix domain data (GD ≠ interview ≠ dojo)
  ✗ Store PII outside encrypted channels
  ✗ Operate without monitoring dashboards active
  ✗ Process requests from unauthenticated callers
  ✗ Return generic errors (must include specific failure reason)
  ✗ Silently fail (all failures logged + escalated)
  ✗ Ignore rate limits
  ✗ Rewrite model outputs to match user expectations
  ✗ Disable audit logging for "performance"

MANDATORY ACTIONS (ENFORCEMENT):

This agent MUST:

  ✓ Validate all inputs against strict schema
  ✓ Log every decision with full context
  ✓ Emit structured events to Kafka
  ✓ Respect tenant isolation boundaries
  ✓ Return explicit error codes on failure
  ✓ Generate immutable audit trails
  ✓ Store all versions for reproducibility
  ✓ Monitor model drift continuously
  ✓ Retrain weekly on labeled data
  ✓ Escalate ambiguous cases (confidence < 0.55)
  ✓ Support full rollback capability
  ✓ Maintain latency targets (P99 < 500ms)
  ✓ Scale horizontally (stateless)
  ✓ Integrate with observability stack
  ✓ Encrypt all data in transit + at rest
  ✓ Document all changes (add-only versioning)
  ✓ Pass security audits annually
  ✓ Support dispute resolution (full audit access)
  ✓ Respond to compliance requests
  ✓ Publish performance metrics

GOVERNANCE OVERSIGHT (LOCKED):

This agent is subject to:
  ✓ Admin Governance Service (final disputes authority)
  ✓ Compliance & Legal (regulatory requirements)
  ✓ Security Team (threat modeling)
  ✓ Data Science (model performance SLA)
  ✓ Platform Engineering (deployment gates)
  ✓ Finance/Legal (contractual obligations to users)

No single engineer can override these constraints.
Changes require multi-stakeholder approval.
```

---

## CHANGELOG & VERSION HISTORY

```
v2.0.0 - 2025-03-04 (SEALED RELEASE)
  ✓ Initial sealed specification for Antigravity platform
  ✓ Deterministic GBDT-based detection (no AI decision-making)
  ✓ 100% immutable audit trail
  ✓ Multi-tenant zero-trust architecture
  ✓ Kafka-driven event streaming
  ✓ Prometheus-based observability
  ✓ Weekly model retraining protocol
  ✓ Explicit failure modes with escalation
  ✓ Full inter-agent dependency mapping
  ✓ Compliance-ready documentation

---

## SEAL CERTIFICATION

🔒 **THIS SPECIFICATION IS SEALED**

This document defines a production-grade agent for Ecoskiller Antigravity.
All logic is deterministic, auditable, and compliant with enterprise SaaS requirements.

**Sealed By:** Architecture Review Board
**Date Sealed:** 2025-03-04
**Review Cycle:** Quarterly security audit required
**Any modifications require formal change control process with full documentation.**

---

**END OF SEALED SPECIFICATION**
```
