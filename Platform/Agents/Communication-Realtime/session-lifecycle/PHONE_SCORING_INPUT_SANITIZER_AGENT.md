# 🔒 PHONE_SCORING_INPUT_SANITIZER_AGENT
## Sealed & Locked Enterprise Agent Specification
### Ecoskiller Antigravity Platform

---

## 1️⃣ AGENT IDENTITY (MANDATORY - SEALED)

```
AGENT_NAME = PHONE_SCORING_INPUT_SANITIZER_AGENT
SYSTEM_ROLE = Data Quality Gatekeeper & Input Validation Enforcer
PRIMARY_DOMAIN = Scoring inputs from phone interruption detection
EXECUTION_MODE = Deterministic + Stateless (ZERO interpretation)
DATA_SCOPE = Session integrity signals, interruption events, scoring request payloads
TENANT_SCOPE = Strict Multi-Tenant Isolation (row-level security enforced)
FAILURE_POLICY = REJECT on any ambiguity, LOG all rejections, ESCALATE to Admin
IMMUTABILITY_LEVEL = SEALED - No runtime modification, versioned only
VERSIONING_CONSTRAINT = Add-only, backward compatible, audit immutable
PROCESS_STAGE = Input → Validation → Sanitization → Output → Scoring Engine
CRITICALITY = CRITICAL (gatekeeper before ML scoring decisions)
```

**SEALED CONSTRAINT:**
This agent definition is locked in immutable state. No modifications allowed without:
1. Formal Architecture Review Board approval
2. Change request with migration documentation
3. Full backward compatibility proof
4. Audit trail entry with all stakeholders signed

---

## 2️⃣ PURPOSE DECLARATION (SEALED)

### Problem This Agent Solves

**Data Quality Crisis Prevention:**
- Prevent malformed/corrupted scoring inputs from reaching Scoring Engine
- Detect injection attacks, data type violations, schema mismatches
- Block cascading failures that could silently skew fairness metrics
- Maintain data integrity across multi-tenant environment

**Fairness Safeguard:**
- Ensure phone interruption signals are legitimate (not spoofed)
- Validate that penalty/credit calculations use clean inputs
- Prevent participants from manipulating their own scoring context
- Enforce consistent scoring rules across all participants

**Compliance & Auditability:**
- Generate immutable record of all input validations
- Enable dispute resolution (prove input was/wasn't valid)
- Support compliance audits (demonstrate data quality controls)
- Detect anomalies that suggest tampering or data corruption

### Input Consumed

**Primary Input Source:**
- `INTERRUPTION_DETECTED` events from Phone Interruption Detection Agent
- `SESSION_INTEGRITY_SIGNAL` with scoring adjustment recommendations
- Raw metric data for validation/re-verification
- Session metadata for context-based validation

**Secondary Input Sources:**
- Session context from Voice GD Orchestrator (phase, duration, role)
- Interview metadata from Interview Service (candidate, interviewer, score topic)
- Dojo match context from Dojo Match Engine (role, scenario, round)
- Baseline metrics for cross-validation

### Output Produced

**Primary Output:**
- `SANITIZED_SCORING_INPUT` (clean, validated payload)
- `DATA_QUALITY_REPORT` (validation results + any corrections applied)
- `AUDIT_LOG_ENTRY` (immutable record of validation decision)
- `SANITIZATION_STATUS` (PASS, PASS_WITH_WARNINGS, REJECT)

**Secondary Output:**
- `ANOMALY_FLAG` (suspicious patterns detected)
- `CORRECTION_APPLIED_EVENT` (if data was auto-corrected)
- `ESCALATION_SIGNAL` (for admin governance review)
- `DOWNSTREAM_TRIGGER_EVENT` (for Scoring Engine consumption)

### Upstream Agent Dependency Chain

1. **Phone Interruption Detection Agent** (primary provider)
   - Output: `interruption_detected` events + evidence
   - Frequency: Variable (per interruption)
   - Contains: Confidence scores, severity classification, signal timeline

2. **Voice GD Orchestrator** (validation context)
   - Output: Session metadata, participant roles, phase timing
   - Frequency: Per session
   - Contains: Baseline metrics, session state machine data

3. **Interview Service** (validation context)
   - Output: Interview session metadata, roles, scheduled duration
   - Frequency: Per session
   - Contains: Candidate ID, interviewer ID, interview type

4. **Dojo Match Engine** (validation context)
   - Output: Match metadata, role assignment, scenario details
   - Frequency: Per match
   - Contains: Participant pairs, match state, scoring rules

### Downstream Agent Consumption

1. **Scoring Engine** (primary consumer)
   - Input: `SANITIZED_SCORING_INPUT` (validated + cleaned)
   - Action: Applies fairness penalties/credits based on interruptions
   - SLA: Must accept within 120 seconds of sanitization
   - Contract: Only accepts inputs from Sanitizer Agent

2. **Data Quality Monitoring Service** (observability)
   - Input: `DATA_QUALITY_REPORT` events
   - Action: Aggregates for dashboard + anomaly detection
   - SLA: Eventually consistent (batched hourly)
   - Contract: Structured reports with metrics

3. **Admin Governance Service** (escalation)
   - Input: `ANOMALY_FLAG` + `ESCALATION_SIGNAL` (HIGH severity only)
   - Action: Flags for manual review + dispute investigation
   - SLA: Must consume within 600 seconds
   - Contract: Full evidence + recommendations

4. **Audit Service** (compliance)
   - Input: `AUDIT_LOG_ENTRY` references (immutable storage)
   - Action: Stores for 7+ year retention
   - SLA: Synchronous (at most 100ms latency)
   - Contract: Complete validation chain traceable

---

## 3️⃣ INPUT CONTRACT (STRICT - SEALED)

```
INPUT_SCHEMA: {
  
  scoring_request_id: {
    type: "uuid",
    required: true,
    validation: [
      "must be valid UUID v4 format",
      "must be unique within 24-hour window",
      "cannot contain null bytes or unicode edge cases"
    ],
    domain_check: "idempotency key for deduplication",
    security_check: "no PII exposure in logs"
  },
  
  session_id: {
    type: "uuid",
    required: true,
    validation: [
      "must exist in sessions table",
      "must match calling service tenant_id",
      "must not be cancelled/expired"
    ],
    domain_check: "strict tenant isolation enforced",
    security_check: "verify caller owns this session_id"
  },
  
  participant_id: {
    type: "uuid",
    required: true,
    validation: [
      "must be active session participant",
      "must match authenticated user context",
      "cannot be spoofed by another participant"
    ],
    domain_check: "row-level security check",
    security_check: "verify authentication token matches participant_id"
  },
  
  interruption_event: {
    type: "object",
    required: true,
    fields: {
      
      event_id: {
        type: "uuid",
        required: true,
        validation: "must match event_id from Interruption Detection Agent"
      },
      
      confidence_score: {
        type: "float",
        required: true,
        range: [0.40, 1.0],
        precision: 0.01,
        validation: [
          "must be exactly 2 decimal places",
          "must be >= 0.40 (low confidence rejected)",
          "must be <= 1.0 (no overflow)",
          "must be deterministic (reproducible)"
        ],
        domain_check: "matches Detection Agent output",
        failure_action: "REJECT with confidence_out_of_range error"
      },
      
      severity_level: {
        type: "enum",
        required: true,
        allowed_values: ["CRITICAL", "HIGH", "MEDIUM", "LOW"],
        validation: [
          "must match exactly (case-sensitive)",
          "must be consistent with confidence_score",
          "confidence >= 0.85 requires CRITICAL or HIGH"
        ],
        failure_action: "REJECT with severity_confidence_mismatch error"
      },
      
      interruption_type: {
        type: "enum",
        required: true,
        allowed_values: [
          "incoming_call_detected",
          "call_hold_pattern",
          "network_redirect",
          "device_context_switch",
          "app_backgrounding",
          "mic_hardware_disconnect"
        ],
        validation: "must be valid classification",
        domain_check: "must be deterministic (not speculative)"
      },
      
      duration_ms: {
        type: "integer",
        required: true,
        range: [100, 600000],
        validation: [
          "must be positive integer",
          "must be >= 100ms (minimum interruption detection window)",
          "must be <= 10 minutes",
          "must match session elapsed time constraints"
        ],
        failure_action: "REJECT with invalid_interruption_duration error"
      },
      
      signal_timeline: {
        type: "array",
        required: true,
        items: {
          type: "object",
          fields: {
            offset_ms: {
              type: "integer",
              range: [0, 600000],
              required: true,
              validation: "must be monotonically increasing"
            },
            
            event_name: {
              type: "string",
              required: true,
              max_length: 100,
              allowed_patterns: [
                "network_disconnect",
                "jitter_spike",
                "packet_loss_surge",
                "rtt_volatility",
                "bandwidth_collapse",
                "codec_switch",
                "ice_restart",
                "reconnection"
              ],
              validation: "no SQL injection, no shell metacharacters"
            },
            
            metric_value: {
              type: "float",
              required: true,
              validation: "must be finite number (no NaN, Inf)"
            },
            
            baseline_value: {
              type: "float",
              required: true,
              validation: "must be finite number"
            },
            
            deviation_percent: {
              type: "float",
              required: true,
              range: [-100, 1000],
              validation: "must represent realistic deviation"
            }
          }
        },
        validation_rules: [
          "minimum 1 item, maximum 100 items",
          "offsets must be in ascending order",
          "no duplicate offsets within 100ms",
          "total timeline duration must match session_elapsed_seconds"
        ]
      },
      
      evidence_summary: {
        type: "object",
        required: true,
        fields: {
          
          primary_signal: {
            type: "string",
            required: true,
            max_length: 200,
            validation: [
              "must not contain PII",
              "must not contain SQL/script injection",
              "must be deterministic description from ML model"
            ]
          },
          
          supporting_signals: {
            type: "array",
            required: true,
            items: { type: "string", max_length: 200 },
            max_items: 5,
            validation: "each item must be valid signal name"
          },
          
          confidence_breakdown: {
            type: "object",
            required: true,
            fields: {
              network_discontinuity_weight: {
                type: "float",
                range: [0, 1],
                required: true
              },
              metric_anomaly_weight: {
                type: "float",
                range: [0, 1],
                required: true
              },
              device_state_weight: {
                type: "float",
                range: [0, 1],
                required: true
              },
              temporal_clustering_weight: {
                type: "float",
                range: [0, 1],
                required: true
              },
              pattern_matching_weight: {
                type: "float",
                range: [0, 1],
                required: true
              }
            },
            validation: "all weights must sum to 1.0 (±0.01 tolerance)"
          }
        }
      },
      
      model_version: {
        type: "string",
        required: true,
        format: "semantic versioning (major.minor.patch)",
        validation: [
          "must match known deployed model version",
          "must be in whitelist: [v2.0.0, v2.0.1, v2.1.0]"
        ],
        failure_action: "REJECT with unsupported_model_version error"
      },
      
      audit_reference: {
        type: "uuid",
        required: true,
        validation: "must exist in audit_logs table"
      },
      
      timestamp_utc: {
        type: "iso8601_datetime",
        required: true,
        validation: [
          "must be valid ISO8601 format",
          "must be within session window",
          "must not be in future (max 60 seconds ahead of server time)",
          "must be monotonically increasing per session"
        ]
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
        precision: 0.01,
        validation: [
          "must be valid probability score",
          "must be deterministic from metrics",
          "must be consistent with interruption_count"
        ]
      },
      
      interruption_count: {
        type: "integer",
        required: true,
        range: [0, 100],
        validation: [
          "must match count of interruption events in session",
          "must be >= 0",
          "must be <= session_duration_seconds / 30 (sanity check)"
        ]
      },
      
      total_interruption_duration_seconds: {
        type: "integer",
        required: true,
        range: [0, 3600],
        validation: [
          "must sum to <= session_duration_seconds",
          "must be consistent with individual event durations"
        ]
      },
      
      network_reliability_metric: {
        type: "float",
        required: true,
        range: [0, 1],
        validation: "independent from interruption score"
      },
      
      signal_anomaly_density: {
        type: "float",
        required: true,
        range: [0, 1],
        validation: "frequency of metric anomalies must be reasonable"
      },
      
      flags: {
        type: "array",
        required: true,
        items: {
          type: "string",
          max_length: 100,
          allowed_values: [
            "multiple_interruptions_single_participant",
            "interruption_during_critical_phase",
            "device_switch_mid_session",
            "network_type_change_detected",
            "pattern_matches_known_exploit",
            "baseline_not_established",
            "insufficient_metrics_for_validation"
          ]
        },
        validation: "no custom strings (enum-only)"
      }
    }
  },
  
  session_context: {
    type: "object",
    required: true,
    fields: {
      
      session_type: {
        type: "enum",
        required: true,
        allowed_values: ["voice_gd", "interview", "dojo_match"],
        validation: "must match session metadata in database"
      },
      
      session_phase: {
        type: "enum",
        required: true,
        allowed_values: [
          "intro",
          "discussion",
          "rebuttal",
          "conclusion",
          "open_discussion",
          "interview_qa",
          "match_round"
        ],
        validation: "must be valid phase for session_type"
      },
      
      elapsed_seconds: {
        type: "integer",
        required: true,
        range: [0, 3600],
        validation: [
          "must be >= 0",
          "must be <= expected_duration_seconds",
          "must match server-side session timer (±5 seconds tolerance)"
        ]
      },
      
      expected_duration_seconds: {
        type: "integer",
        required: true,
        range: [60, 3600],
        validation: "must match session_type baseline"
      },
      
      participant_role: {
        type: "enum",
        required: true,
        allowed_values: ["candidate", "interviewer", "mentor", "peer"],
        validation: [
          "must match authenticated role",
          "candidate cannot report on other candidate"
        ]
      },
      
      participant_count: {
        type: "integer",
        required: true,
        range: [2, 10],
        validation: "must match actual session participants"
      },
      
      speaking_turn_active: {
        type: "boolean",
        required: true,
        validation: "must match state machine state at interruption_timestamp"
      },
      
      speaking_turn_duration_remaining_seconds: {
        type: "integer",
        required: true,
        range: [0, 300],
        validation: "must be non-negative"
      },
      
      tenant_id: {
        type: "uuid",
        required: true,
        validation: "must match authenticated tenant_id (cross-validation)"
      }
    }
  },
  
  scoring_adjustment_request: {
    type: "object",
    required: true,
    fields: {
      
      adjustment_type: {
        type: "enum",
        required: true,
        allowed_values: [
          "apply_penalty",
          "apply_credit",
          "mark_disputed",
          "flag_for_review",
          "no_adjustment"
        ],
        validation: "must be deterministic from interruption evidence"
      },
      
      impact_score: {
        type: "float",
        required: true,
        range: [-1.0, 1.0],
        precision: 0.01,
        validation: [
          "negative = penalty, positive = credit",
          "magnitude must match interruption severity"
        ]
      },
      
      penalty_basis: {
        type: "enum",
        required: true,
        allowed_values: [
          "speaking_turn_disrupted",
          "context_switch_during_critical_phase",
          "fairness_disadvantage_applied",
          "participant_disadvantaged_relative_to_peers",
          "external_factor_outside_control"
        ],
        validation: "must be justified by evidence"
      },
      
      fairness_impact_narrative: {
        type: "string",
        required: true,
        max_length: 500,
        validation: [
          "must be non-empty",
          "must not contain PII",
          "must not contain encoded payloads",
          "generated by AI or documented in rule"
        ]
      },
      
      supporting_evidence_ids: {
        type: "array",
        required: true,
        items: { type: "uuid" },
        min_items: 1,
        max_items: 10,
        validation: "each ID must reference valid audit_log entry"
      },
      
      override_flag: {
        type: "boolean",
        required: true,
        validation: [
          "if true, requires Admin Governance approval",
          "cannot be set by automated process"
        ]
      }
    }
  },
  
  VALIDATION_SCOPE: {
    type: "array",
    required: false,
    items: { type: "string" },
    allowed_values: [
      "schema_validation",
      "type_validation",
      "range_validation",
      "enum_validation",
      "cross_field_validation",
      "temporal_validation",
      "tenant_isolation",
      "role_authorization",
      "data_consistency",
      "anomaly_detection"
    ],
    default: ["all validations"]
  }
}

VALIDATION_RULES (MANDATORY - LOCKED):

  ✓ ALL required fields must be present (no null tolerance)
  ✓ Enum values must match exactly (case-sensitive, no abbreviations)
  ✓ Numeric ranges enforced strictly (no overflow, underflow)
  ✓ Precision requirements enforced (0.01 decimals, no rounding)
  ✓ Cross-field consistency validated (e.g., confidence ↔ severity)
  ✓ Temporal ordering validated (monotonic increase, no time travel)
  ✓ Tenant isolation verified (no cross-tenant data leakage)
  ✓ Schema must be version-compatible (backward compatibility check)
  ✓ No unicode edge cases (ASCII printable only for string fields)
  ✓ No null bytes, escape sequences, injection patterns
  ✓ All UUIDs validated (format + existence in DB)
  ✓ All timestamps validated (within session window)
  ✓ All references validated (IDs must exist)

SECURITY_CHECKS (MANDATORY - ZERO TOLERANCE):

  ✓ SQL Injection Detection: Scan string fields for SQL metacharacters
  ✓ Script Injection Detection: Scan for JavaScript, Python, Bash patterns
  ✓ PII Leakage Detection: Scan for email addresses, phone numbers, SSNs
  ✓ Encoding Attack Detection: Check for base64, URL encoding, hex encoding
  ✓ Path Traversal Detection: Scan for ../, ..\, path separators
  ✓ Command Injection Detection: Scan for shell metacharacters (;|&`$)
  ✓ XXE Detection: Scan for XML/entity patterns
  ✓ Deserialization Attacks: Reject complex nested objects
  ✓ Type Confusion: Enforce strict type matching
  ✓ Privilege Escalation: Verify role boundaries not crossed
  
  Failure action: REJECT immediately with security_violation_detected
```

---

## 4️⃣ OUTPUT CONTRACT (STRICT - SEALED)

```
OUTPUT_SCHEMA: {
  
  result_object: {
    type: "object",
    
    sanitization_status: {
      type: "enum",
      required: true,
      allowed_values: [
        "PASS",
        "PASS_WITH_WARNINGS",
        "PASS_WITH_CORRECTIONS",
        "REJECT"
      ],
      semantics: {
        PASS: "input clean, no issues detected, ready for scoring",
        PASS_WITH_WARNINGS: "input valid but contains non-critical anomalies (log only)",
        PASS_WITH_CORRECTIONS: "input had minor issues auto-corrected, output reflects corrections",
        REJECT: "input invalid or unsafe, must not proceed to scoring"
      }
    },
    
    sanitized_scoring_input: {
      type: "object",
      required: true,
      semantics: "cleaned, validated, idempotent copy of input for Scoring Engine",
      
      fields: {
        
        scoring_request_id: {
          type: "uuid",
          required: true,
          semantics: "unchanged from input (idempotency key)"
        },
        
        session_id: {
          type: "uuid",
          required: true,
          semantics: "validated to exist in sessions table"
        },
        
        participant_id: {
          type: "uuid",
          required: true,
          semantics: "verified ownership via authentication"
        },
        
        interruption_event: {
          type: "object",
          required: true,
          semantics: "validated structure with optional normalization",
          
          fields: {
            
            event_id: { type: "uuid" },
            confidence_score: {
              type: "float",
              range: [0.40, 1.0],
              precision: 0.01,
              semantics: "normalized to exactly 2 decimal places"
            },
            
            severity_level: {
              type: "enum",
              allowed: ["CRITICAL", "HIGH", "MEDIUM", "LOW"],
              semantics: "validated against confidence_score consistency"
            },
            
            interruption_type: { type: "enum" },
            
            duration_ms: {
              type: "integer",
              semantics: "validated within session bounds"
            },
            
            signal_timeline: {
              type: "array",
              semantics: "validated for chronological order, sanitized event names"
            },
            
            evidence_summary: {
              type: "object",
              semantics: "text fields sanitized (no injection), weights re-validated"
            },
            
            model_version: {
              type: "string",
              semantics: "verified against deployed model whitelist"
            },
            
            audit_reference: {
              type: "uuid",
              semantics: "verified to exist in audit_logs"
            },
            
            timestamp_utc: {
              type: "iso8601_datetime",
              semantics: "validated and normalized to UTC"
            }
          }
        },
        
        session_integrity_signal: {
          type: "object",
          required: true,
          semantics: "validated for internal consistency",
          
          fields: {
            integrity_score: { type: "float", range: [0, 1] },
            interruption_count: { type: "integer", range: [0, 100] },
            total_interruption_duration_seconds: { type: "integer", range: [0, 3600] },
            network_reliability_metric: { type: "float", range: [0, 1] },
            signal_anomaly_density: { type: "float", range: [0, 1] },
            flags: { type: "array", items: { type: "string" } }
          }
        },
        
        session_context: {
          type: "object",
          required: true,
          semantics: "cross-checked against session metadata table"
        },
        
        scoring_adjustment_request: {
          type: "object",
          required: true,
          semantics: "validated as deterministic from evidence"
        }
      }
    },
    
    data_quality_report: {
      type: "object",
      required: true,
      
      fields: {
        
        validation_rules_passed: {
          type: "integer",
          required: true,
          semantics: "count of validation rules that passed"
        },
        
        validation_rules_failed: {
          type: "integer",
          required: true,
          semantics: "count of validation rules that failed (causes REJECT if > 0)"
        },
        
        validation_rules_triggered: {
          type: "array",
          required: true,
          items: {
            type: "object",
            fields: {
              rule_name: { type: "string" },
              field_path: { type: "string" },
              result: {
                type: "enum",
                allowed: ["PASS", "FAIL", "WARNING", "CORRECTION_APPLIED"]
              },
              message: { type: "string", max_length: 500 },
              evidence: { type: "string", max_length: 1000 }
            }
          },
          max_items: 100
        },
        
        corrections_applied: {
          type: "array",
          required: true,
          items: {
            type: "object",
            fields: {
              field_path: { type: "string" },
              original_value: { type: "string" },
              corrected_value: { type: "string" },
              correction_type: {
                type: "enum",
                allowed: [
                  "format_normalization",
                  "precision_rounding",
                  "whitespace_trimming",
                  "timestamp_adjustment",
                  "enum_case_normalization"
                ]
              },
              justification: { type: "string", max_length: 500 }
            }
          },
          max_items: 50,
          semantics: "auto-corrections applied are logged for auditability"
        },
        
        warnings_detected: {
          type: "array",
          required: true,
          items: {
            type: "object",
            fields: {
              warning_type: { type: "string" },
              field_path: { type: "string" },
              message: { type: "string" },
              severity: {
                type: "enum",
                allowed: ["LOW", "MEDIUM", "HIGH"]
              },
              recommendation: { type: "string" }
            }
          },
          max_items: 50,
          semantics: "non-critical issues that don't block processing"
        },
        
        security_checks_passed: {
          type: "array",
          required: true,
          items: { type: "string" },
          examples: [
            "sql_injection_detection",
            "script_injection_detection",
            "pii_leakage_detection",
            "path_traversal_detection"
          ]
        },
        
        security_violations_detected: {
          type: "array",
          required: true,
          items: {
            type: "object",
            fields: {
              violation_type: { type: "string" },
              field: { type: "string" },
              pattern_matched: { type: "string" },
              severity: {
                type: "enum",
                allowed: ["CRITICAL", "HIGH"]
              }
            }
          },
          semantics: "any security violation causes REJECT status"
        },
        
        consistency_checks: {
          type: "object",
          required: true,
          fields: {
            confidence_severity_match: {
              type: "boolean",
              semantics: "confidence_score consistent with severity_level?"
            },
            
            interruption_count_matches_events: {
              type: "boolean",
              semantics: "count field matches actual event count?"
            },
            
            duration_within_session_bounds: {
              type: "boolean",
              semantics: "total interruption duration <= session duration?"
            },
            
            weight_sum_normalized: {
              type: "boolean",
              semantics: "confidence weights sum to 1.0 (±0.01)?"
            },
            
            timestamp_monotonic: {
              type: "boolean",
              semantics: "timeline timestamps in ascending order?"
            },
            
            tenant_isolation_verified: {
              type: "boolean",
              semantics: "session tenant matches request tenant?"
            },
            
            role_authorization_verified: {
              type: "boolean",
              semantics: "participant authorized to submit this request?"
            },
            
            model_version_whitelisted: {
              type: "boolean",
              semantics: "model_version in deployed versions list?"
            }
          }
        }
      }
    },
    
    anomaly_detection_report: {
      type: "object",
      required: true,
      
      fields: {
        
        anomalies_detected: {
          type: "boolean",
          required: true,
          semantics: "any suspicious patterns found?"
        },
        
        anomaly_flags: {
          type: "array",
          required: true,
          items: {
            type: "object",
            fields: {
              flag_type: {
                type: "enum",
                allowed: [
                  "confidence_uncertainty",
                  "severity_confidence_mismatch",
                  "unusual_interruption_pattern",
                  "repeated_participant",
                  "bulk_submission_timing",
                  "baseline_not_computed",
                  "insufficient_supporting_signals",
                  "statistical_outlier",
                  "temporal_anomaly",
                  "evidence_contradiction"
                ]
              },
              
              severity: {
                type: "enum",
                allowed: ["LOW", "MEDIUM", "HIGH"]
              },
              
              description: { type: "string", max_length: 500 },
              
              impact_on_scoring: {
                type: "enum",
                allowed: [
                  "no_impact",
                  "scoring_may_be_inaccurate",
                  "requires_manual_review",
                  "should_reject"
                ]
              },
              
              recommendation: { type: "string", max_length: 500 }
            }
          },
          max_items: 20
        },
        
        statistical_outlier_score: {
          type: "float",
          required: true,
          range: [0, 1],
          semantics: "how unusual is this input relative to historical baselines?"
        },
        
        similar_cases_in_history: {
          type: "integer",
          required: true,
          range: [0, 1000],
          semantics: "how many similar cases have been seen before?"
        }
      }
    },
    
    audit_reference: {
      type: "uuid",
      required: true,
      semantics: "immutable reference to sanitization audit log entry"
    },
    
    processing_metadata: {
      type: "object",
      required: true,
      fields: {
        
        received_timestamp_utc: {
          type: "iso8601_datetime",
          required: true
        },
        
        processed_timestamp_utc: {
          type: "iso8601_datetime",
          required: true
        },
        
        processing_duration_ms: {
          type: "integer",
          required: true,
          range: [10, 5000],
          semantics: "time from input receipt to output generation"
        },
        
        validation_rules_evaluated: {
          type: "integer",
          required: true
        },
        
        security_checks_performed: {
          type: "integer",
          required: true
        },
        
        anomaly_detection_model_version: {
          type: "string",
          required: true,
          format: "semantic versioning"
        },
        
        computation_warnings: {
          type: "array",
          required: true,
          items: { type: "string" },
          max_items: 20
        }
      }
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
              "sanitized_input_ready",
              "manual_review_required",
              "security_incident_escalation",
              "data_quality_issue_detected",
              "statistical_anomaly_flagged"
            ]
          },
          
          target_service: {
            type: "enum",
            allowed: [
              "scoring_engine",
              "admin_governance_service",
              "security_team",
              "data_quality_monitoring",
              "analytics_service"
            ]
          },
          
          payload_reference: {
            type: "uuid",
            semantics: "reference to event stored in message bus"
          },
          
          priority: {
            type: "enum",
            allowed: ["critical", "high", "normal", "low"]
          },
          
          deadline_seconds: {
            type: "integer",
            range: [1, 3600],
            semantics: "max time before action must be taken"
          },
          
          depends_on: {
            type: "array",
            items: { type: "uuid" },
            semantics: "other events that must complete first (DAG ordering)"
          }
        }
      }
    }
  },
  
  sanitization_status: {
    type: "enum",
    required: true,
    allowed: ["PASS", "PASS_WITH_WARNINGS", "PASS_WITH_CORRECTIONS", "REJECT"],
    semantics: "overall determination of input validity"
  },
  
  confidence_score: {
    type: "float",
    required: true,
    range: [0, 1],
    precision: 0.01,
    semantics: "confidence that sanitization decision is correct",
    rule: "confidence < 0.60 → escalate to manual review"
  },
  
  model_version: {
    type: "string",
    required: true,
    format: "semantic versioning (major.minor.patch)",
    semantics: "exact version of sanitization rules/ML models used"
  },
  
  audit_reference: {
    type: "uuid",
    required: true,
    semantics: "immutable audit log entry ID for full traceability"
  }
}

OUTPUT_GUARANTEES (NON-NEGOTIABLE):

  ✓ All outputs must include sanitization_status (deterministic)
  ✓ All outputs must include confidence_score (0-1, 0.01 precision)
  ✓ All outputs must include model_version (immutable reference)
  ✓ All outputs must include audit_reference (to audit log)
  ✓ All outputs must include next_trigger_events (for event-driven flow)
  ✓ Rejected inputs must not have sanitized_scoring_input populated
  ✓ All corrections must be documented with justification
  ✓ All anomalies must be flagged (none silently ignored)
  ✓ All security violations must be logged to security team
  ✓ All outputs serializable to JSON-LD with @context
  ✓ Output structure backward compatible with v1.x consumers
```

---

## 5️⃣ ML / AI LOGIC LAYER (SEALED - 70% HEURISTIC RULES, 20% ML ANOMALY DETECTION, 10% SEMANTIC)

### 5.1 VALIDATION ENGINE (70% DETERMINISTIC RULES - LOCKED)

```
VALIDATION_FRAMEWORK: Multi-Layer Rule Engine
EXECUTION_MODE: Deterministic (no probabilistic decision-making)
FRAMEWORK: Custom rule engine (not ML) + pattern matching

LAYER 1: SCHEMA VALIDATION (STRICTEST)

```python
def validate_schema(input_json):
    """
    Strictest validation: must match schema exactly.
    No inference, no defaults, no corrections at this layer.
    """
    
    # Check all required fields present
    required_fields = [
        'scoring_request_id',
        'session_id',
        'participant_id',
        'interruption_event',
        'session_integrity_signal',
        'session_context',
        'scoring_adjustment_request'
    ]
    
    for field in required_fields:
        if field not in input_json:
            return REJECT("missing_required_field", field)
    
    # Check no extra fields (strict schema)
    allowed_fields = set(required_fields + ['VALIDATION_SCOPE'])
    extra_fields = set(input_json.keys()) - allowed_fields
    if extra_fields:
        return REJECT("unexpected_fields", extra_fields)
    
    # Validate each field structure
    for field_name, field_schema in FIELD_SCHEMAS.items():
        result = validate_field(input_json[field_name], field_schema)
        if result.status == FAIL:
            return result
    
    return PASS("schema_validation_passed")
```

LAYER 2: TYPE VALIDATION (STRICT ENFORCEMENT)

```python
def validate_types(input_json):
    """
    Enforce exact type matching. No type coercion.
    """
    
    type_checks = [
        ('scoring_request_id', uuid),
        ('session_id', uuid),
        ('participant_id', uuid),
        ('interruption_event.event_id', uuid),
        ('interruption_event.confidence_score', float),
        ('interruption_event.severity_level', str),
        ('session_integrity_signal.integrity_score', float),
        ('session_context.elapsed_seconds', int),
        ...
    ]
    
    for field_path, expected_type in type_checks:
        value = get_nested_field(input_json, field_path)
        actual_type = type(value)
        
        if actual_type != expected_type:
            return REJECT(
                "type_mismatch",
                f"{field_path}: expected {expected_type}, got {actual_type}"
            )
    
    return PASS("type_validation_passed")
```

LAYER 3: RANGE VALIDATION (NUMERIC BOUNDS)

```python
def validate_ranges(input_json):
    """
    Enforce numeric ranges strictly. No fuzzy matching.
    """
    
    range_checks = [
        ('interruption_event.confidence_score', 0.40, 1.0),
        ('interruption_event.duration_ms', 100, 600000),
        ('session_context.elapsed_seconds', 0, 3600),
        ('session_integrity_signal.interruption_count', 0, 100),
        ('session_integrity_signal.integrity_score', 0, 1),
        ...
    ]
    
    for field_path, min_val, max_val in range_checks:
        value = get_nested_field(input_json, field_path)
        
        if not (min_val <= value <= max_val):
            return FAIL(
                "range_violation",
                f"{field_path}: {value} not in [{min_val}, {max_val}]"
            )
    
    return PASS("range_validation_passed")
```

LAYER 4: ENUM VALIDATION (EXACT MATCH)

```python
def validate_enums(input_json):
    """
    Enforce enum values exactly. Case-sensitive. No abbreviations.
    """
    
    enum_checks = [
        ('interruption_event.severity_level', 
         ["CRITICAL", "HIGH", "MEDIUM", "LOW"]),
        
        ('interruption_event.interruption_type',
         ["incoming_call_detected", "call_hold_pattern", "network_redirect", ...]),
        
        ('session_context.session_type',
         ["voice_gd", "interview", "dojo_match"]),
        
        ('scoring_adjustment_request.adjustment_type',
         ["apply_penalty", "apply_credit", "mark_disputed", ...]),
        
        ...
    ]
    
    for field_path, allowed_values in enum_checks:
        value = get_nested_field(input_json, field_path)
        
        if value not in allowed_values:
            return FAIL(
                "enum_violation",
                f"{field_path}: '{value}' not in {allowed_values}"
            )
    
    return PASS("enum_validation_passed")
```

LAYER 5: CROSS-FIELD VALIDATION (CONSISTENCY CHECKS)

```python
def validate_consistency(input_json):
    """
    Check relationships between fields. Deterministic rules only.
    """
    
    checks = [
        {
            "name": "confidence_severity_match",
            "rule": lambda i: (
                i['interruption_event']['confidence_score'] >= 0.85 and
                i['interruption_event']['severity_level'] in ["CRITICAL", "HIGH"]
            ) or (
                i['interruption_event']['confidence_score'] < 0.75 and
                i['interruption_event']['severity_level'] in ["MEDIUM", "LOW"]
            ),
            "error": "confidence score and severity level inconsistent"
        },
        
        {
            "name": "interruption_count_matches_timeline",
            "rule": lambda i: (
                len(i['interruption_event']['signal_timeline']) > 0
            ),
            "error": "signal timeline empty"
        },
        
        {
            "name": "duration_within_session",
            "rule": lambda i: (
                i['session_integrity_signal']['total_interruption_duration_seconds'] <= 
                i['session_context']['elapsed_seconds']
            ),
            "error": "total interruption duration exceeds session duration"
        },
        
        {
            "name": "weight_sum_normalized",
            "rule": lambda i: (
                abs(sum([
                    i['interruption_event']['evidence_summary']['confidence_breakdown']['network_discontinuity_weight'],
                    i['interruption_event']['evidence_summary']['confidence_breakdown']['metric_anomaly_weight'],
                    i['interruption_event']['evidence_summary']['confidence_breakdown']['device_state_weight'],
                    i['interruption_event']['evidence_summary']['confidence_breakdown']['temporal_clustering_weight'],
                    i['interruption_event']['evidence_summary']['confidence_breakdown']['pattern_matching_weight']
                ]) - 1.0) < 0.01
            ),
            "error": "confidence weights don't sum to 1.0"
        },
        
        {
            "name": "role_authorization",
            "rule": lambda i: (
                authenticate_role(
                    i['participant_id'],
                    i['session_context']['participant_role']
                )
            ),
            "error": "participant not authorized for this role"
        }
    ]
    
    for check in checks:
        if not check['rule'](input_json):
            return FAIL("consistency_violation", check['error'])
    
    return PASS("consistency_validation_passed")
```

LAYER 6: TEMPORAL VALIDATION (ORDERING, TIMESTAMPS)

```python
def validate_temporal(input_json):
    """
    Verify timestamps and ordering. No time travel. Monotonic increase.
    """
    
    # Check timestamp within session window
    event_timestamp = datetime.fromisoformat(
        input_json['interruption_event']['timestamp_utc']
    )
    session_start = get_session_start_time(input_json['session_id'])
    session_end = session_start + timedelta(
        seconds=input_json['session_context']['expected_duration_seconds']
    )
    
    if not (session_start <= event_timestamp <= session_end + timedelta(seconds=5)):
        return FAIL(
            "temporal_violation",
            f"timestamp {event_timestamp} outside session window"
        )
    
    # Check no future timestamp (max 60 seconds ahead)
    if event_timestamp > datetime.utcnow() + timedelta(seconds=60):
        return FAIL("temporal_violation", "timestamp in future (> 60 seconds)")
    
    # Check timeline is monotonic
    timeline = input_json['interruption_event']['signal_timeline']
    for i in range(1, len(timeline)):
        if timeline[i]['offset_ms'] <= timeline[i-1]['offset_ms']:
            return FAIL(
                "temporal_violation",
                f"signal timeline not monotonic at index {i}"
            )
    
    return PASS("temporal_validation_passed")
```

LAYER 7: DATABASE REFERENCE VALIDATION (EXISTENCE CHECKS)

```python
def validate_references(input_json):
    """
    Verify all UUID references exist in database.
    No orphaned references allowed.
    """
    
    # Check session exists and is active
    session = db.query(Session).filter(
        Session.id == input_json['session_id'],
        Session.tenant_id == input_json['session_context']['tenant_id'],
        Session.status == 'active'
    ).first()
    
    if not session:
        return FAIL("reference_violation", "session not found or inactive")
    
    # Check participant is in session
    participant = db.query(SessionParticipant).filter(
        SessionParticipant.session_id == input_json['session_id'],
        SessionParticipant.participant_id == input_json['participant_id'],
        SessionParticipant.role == input_json['session_context']['participant_role']
    ).first()
    
    if not participant:
        return FAIL("reference_violation", "participant not in session")
    
    # Check model version is whitelisted
    whitelisted_versions = ['v2.0.0', 'v2.0.1', 'v2.1.0']
    if input_json['interruption_event']['model_version'] not in whitelisted_versions:
        return FAIL(
            "reference_violation",
            f"model version not whitelisted: {input_json['interruption_event']['model_version']}"
        )
    
    # Check audit reference exists
    audit_entry = db.query(AuditLog).filter(
        AuditLog.id == input_json['interruption_event']['audit_reference']
    ).first()
    
    if not audit_entry:
        return FAIL("reference_violation", "audit reference not found")
    
    return PASS("reference_validation_passed")
```

VALIDATION RULE EXECUTION ORDER (LOCKED):

  1. Schema validation (fastest, catches structure errors)
  2. Type validation (fast, catches type mismatches)
  3. Range validation (fast, catches numeric violations)
  4. Enum validation (fast, catches classification errors)
  5. Cross-field validation (medium, catches consistency errors)
  6. Temporal validation (medium, checks ordering)
  7. Database reference validation (slowest, hits DB)
  8. Security checks (parallelizable, threat detection)
  9. Anomaly detection (ML-based, slower but optional)

If ANY layer returns FAIL → REJECT entire input (no further processing)
```

### 5.2 SECURITY SCANNING ENGINE (100% HEURISTIC - LOCKED)

```
SECURITY_LAYER: Pattern-Matching & Regex-Based Detection
METHOD: Deterministic signature matching (no ML)
EXECUTION: Synchronous (must complete < 200ms)

THREAT DETECTION SIGNATURES:

Threat 1: SQL Injection
  Patterns:
    - '; DROP TABLE
    - ' OR '1'='1
    - UNION SELECT
    - exec(
    - xp_cmdshell
  
  Scan Target: All string fields
  Action: REJECT with security_violation_detected
  
  Implementation:
  ```python
  sql_injection_patterns = [
      r"(';\s*DROP\s+TABLE)",
      r"('\s+OR\s+'1'='1)",
      r"(UNION\s+SELECT)",
      r"(exec\s*\()",
      r"(xp_cmdshell)",
      r"(script>)",
      r"(javascript:)",
  ]
  
  for field_value in all_string_fields(input_json):
      for pattern in sql_injection_patterns:
          if re.search(pattern, field_value, re.IGNORECASE):
              return REJECT("sql_injection_detected", field_value)
  ```

Threat 2: Script Injection (XSS, JavaScript, Python)
  Patterns:
    - <script>
    - onclick=
    - eval(
    - exec(
    - __import__
  
  Scan Target: text fields (narrative, evidence descriptions)
  Action: REJECT immediately
  
  Implementation:
  ```python
  script_injection_patterns = [
      r"(<script[^>]*>)",
      r"(on\w+\s*=)",
      r"(eval\s*\()",
      r"(exec\s*\()",
      r"(__import__)",
      r"(subprocess\.)",
      r"(os\.system)",
  ]
  ```

Threat 3: PII Leakage Detection
  Patterns:
    - Email addresses: \b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Z|a-z]{2,}\b
    - Phone numbers: \b\d{3}[-.]?\d{3}[-.]?\d{4}\b
    - SSN: \b\d{3}-\d{2}-\d{4}\b
    - Credit cards: \b\d{4}[\s-]?\d{4}[\s-]?\d{4}[\s-]?\d{4}\b
  
  Scan Target: All text fields (not UUIDs)
  Action: LOG_WARNING (not reject, but flag for review)
  
  Implementation:
  ```python
  pii_patterns = {
      'email': r"\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Z|a-z]{2,}\b",
      'phone': r"\b\d{3}[-.]?\d{3}[-.]?\d{4}\b",
      'ssn': r"\b\d{3}-\d{2}-\d{4}\b",
  }
  
  for field_value in all_text_fields(input_json):
      for pii_type, pattern in pii_patterns.items():
          if re.search(pattern, field_value):
              log_warning(f"PII_DETECTED: {pii_type} in field", field_name)
  ```

Threat 4: Path Traversal
  Patterns:
    - ../
    - ..\
    - ....
  
  Scan Target: All string fields
  Action: REJECT

Threat 5: Command Injection
  Patterns:
    - ; rm -rf
    - | cat
    - ` backtick commands
    - && command
    - || command
  
  Scan Target: Text fields
  Action: REJECT

Threat 6: XXE (XML External Entity)
  Patterns:
    - <!DOCTYPE
    - <!ENTITY
    - SYSTEM
  
  Scan Target: Complex text fields
  Action: REJECT

SECURITY CHECK EXECUTION (MANDATORY):

```python
def perform_security_checks(input_json):
    """
    Execute all security checks synchronously.
    Any violation → REJECT immediately.
    """
    
    security_checks = [
        ('sql_injection', check_sql_injection),
        ('script_injection', check_script_injection),
        ('path_traversal', check_path_traversal),
        ('command_injection', check_command_injection),
        ('xxe', check_xxe),
        ('pii_leakage', check_pii_leakage),
        ('unicode_attacks', check_unicode_attacks),
        ('encoding_attacks', check_encoding_attacks),
    ]
    
    violations = []
    
    for check_name, check_func in security_checks:
        result = check_func(input_json)
        
        if result.status == VIOLATION:
            violations.append({
                'type': check_name,
                'field': result.field,
                'pattern': result.pattern_matched,
                'severity': 'CRITICAL'
            })
    
    if violations:
        return REJECT(
            "security_violation_detected",
            violations
        )
    
    return PASS("all_security_checks_passed")
```
```

### 5.3 ANOMALY DETECTION ENGINE (20% ML - OPTIONAL LAYER)

```
ANOMALY_DETECTION: Statistical + Pattern-Based
MODEL_TYPE: Isolation Forest (for univariate anomalies) + DBSCAN (multivariate clustering)
TRAINING_FREQUENCY: Weekly on clean historical data
DRIFT_DETECTION: Monitor detection sensitivity over time

FEATURE ENGINEERING (ANOMALY DETECTION):

Features extracted:
  1. confidence_score (float)
  2. interruption_count (integer)
  3. interruption_duration_seconds (integer)
  4. session_phase_criticality (derived)
  5. session_type_encoded (integer)
  6. signal_timeline_length (integer)
  7. time_since_session_start (integer)
  8. participant_historical_interruption_rate (float)
  9. session_type_historical_interruption_rate (float)
  10. score_entropy (how uncertain are the component scores?)

ANOMALY SCORING:

```python
def compute_anomaly_score(input_json, historical_statistics):
    """
    Compute anomalousness of this input using Isolation Forest.
    Result: probability that this is an outlier (0-1).
    """
    
    features = extract_anomaly_features(input_json)
    
    # Normalize features using historical mean/std
    for i, feature_value in enumerate(features):
        historical_mean = historical_statistics['mean'][i]
        historical_std = historical_statistics['std'][i]
        
        if historical_std > 0:
            features[i] = (feature_value - historical_mean) / historical_std
        else:
            features[i] = 0
    
    # Run Isolation Forest model
    anomaly_score = isolation_forest_model.predict_proba([features])[0][1]
    
    # Adjust for known benign outliers
    if is_benign_outlier(input_json, anomaly_score):
        anomaly_score *= 0.5
    
    return round(anomaly_score, 2)

def is_benign_outlier(input_json, anomaly_score):
    """
    Check if this outlier is expected/benign.
    Examples:
      - First session for participant (high interruptions expected)
      - Very long session (more time for interruptions)
      - Scheduled breaks (intentional disconnections)
    """
    
    benign_conditions = [
        is_participant_first_session(input_json),
        session_duration_very_long(input_json),
        session_in_maintenance_window(input_json),
    ]
    
    return any(benign_conditions)
```

ANOMALY THRESHOLDS (LOCKED):

  - anomaly_score 0.0-0.30: NO ANOMALY (normal behavior)
  - anomaly_score 0.30-0.60: LOW ANOMALY (note, continue processing)
  - anomaly_score 0.60-0.80: MEDIUM ANOMALY (flag for review, pass to scoring)
  - anomaly_score 0.80-1.00: HIGH ANOMALY (escalate to admin, flag with confidence penalty)

If anomaly_score > 0.80:
  ✓ Log detailed evidence
  ✓ Add flag to output
  ✓ Reduce confidence_score by 0.15 (penalize uncertain detections)
  ✓ Escalate to Admin Governance Service
```

### 5.4 DATA CORRECTION ENGINE (OPTIONAL - LIMITED SCOPE)

```
CORRECTION_POLICY: Strict, Limited to Format Normalization
PRINCIPLE: Only correct when deterministic and safe
RULE: All corrections logged with justification

ALLOWED CORRECTIONS (LOCKED):

Correction 1: Timestamp Normalization
  Rule: Convert to UTC ISO8601
  Example: "2025-03-04T10:30:00+05:30" → "2025-03-04T05:00:00Z"
  Condition: Only if timezone provided
  Logging: Mandatory

Correction 2: Enum Case Normalization
  Rule: Correct case only if single exact match found
  Example: "critical" → "CRITICAL"
  Condition: Only if unambiguous
  Validation: Must be in allowed list

Correction 3: UUID Formatting
  Rule: Normalize to lowercase canonical UUID
  Example: "550e8400-e29b-41d4-a716-446655440000" (unchanged, already canonical)
  Condition: Only if valid UUID format
  Validation: Must match pattern

Correction 4: Numeric Precision
  Rule: Round to specified precision (e.g., 0.01 for floats)
  Example: 0.8549 → 0.85 (confidence score)
  Condition: Only if rounding doesn't change result significantly
  Validation: Max rounding error < 1%

Correction 5: Whitespace Normalization
  Rule: Trim leading/trailing whitespace from text
  Example: "  critical phase  " → "critical phase"
  Condition: Only for non-critical fields
  Validation: Content preserved

FORBIDDEN CORRECTIONS (ABSOLUTE):
  ✗ Cannot insert missing values (use defaults or reject)
  ✗ Cannot change numeric magnitudes
  ✗ Cannot change enum values beyond case normalization
  ✗ Cannot reorder array elements
  ✗ Cannot combine multiple fields
  ✗ Cannot infer values from context

CORRECTION LOGGING (MANDATORY):

```python
def log_correction(field_path, original, corrected, reason):
    """
    Log every correction with full details.
    """
    correction_record = {
        'timestamp': datetime.utcnow(),
        'field_path': field_path,
        'original_value': str(original),
        'corrected_value': str(corrected),
        'correction_type': categorize_correction(reason),
        'justification': reason,
        'actor': 'phone_scoring_input_sanitizer_agent',
        'audit_reference': generate_uuid()
    }
    
    db.insert(CorrectionLog, correction_record)
```
```

---

## 6️⃣ SCALABILITY DESIGN (LOCKED)

```
HORIZONTAL SCALING CONSTRAINTS:

Performance Targets (SEALED):
  EXPECTED_RPS = 30,000 RPS
    Justification: 50K interruption detection RPS × 60% go to scoring
  
  LATENCY_TARGET = P99 < 300ms
    - Input validation: < 100ms (local rules)
    - Security scanning: < 80ms (regex patterns)
    - Reference validation: < 100ms (DB lookups, cached)
    - Output construction: < 20ms
  
  MAX_CONCURRENCY = 50,000 concurrent validation requests
    - Auto-scaling: 100 container instances (300 RPS each)
    - Burst handling: Redis queue with sliding window rate limiting
  
  QUEUE_STRATEGY = Apache Kafka (consumer group model)
    - Topic: events.scoring_input_sanitization
    - Partitions: 50 (by session_id hash for ordering)
    - Replication factor: 3
    - Retention: 24 hours (for replay/audit)

Stateless Execution (MANDATORY):
  ✓ No session state stored in agent memory
  ✓ All state lives in Redis (cache for participant history)
  ✓ Queries to PostgreSQL only for validation (cached via Redis)
  ✓ Each request independently processable
  ✓ Container restart = zero data loss

Event-Driven Triggers:
  Inbound: Kafka topic events.interruption_detected
  Outbound: Kafka topic events.sanitization_completed
  Processing: Stream consumer (Kafka Streams or custom)

Async Processing (REQUIRED):
  ✓ Input → Queue (fast)
  ✓ Validation → Kafka (async)
  ✓ No blocking waits for downstream consumers
  ✓ Timeout: 10 seconds max per validation

Idempotent Operations (REQUIRED):
  ✓ Input includes scoring_request_id = UUID (deduplication key)
  ✓ Duplicate submissions produce identical output
  ✓ Scoring engine can safely reprocess events
  ✓ IDEMPOTENCY_KEY = hash(scoring_request_id)

Caching Strategy (Redis):
  Cache Layer 1: Reference Data
    Key: ref:session:{session_id}
    Value: {session_type, status, tenant_id, participants}
    TTL: 86400 seconds (session lifetime + 24h)
    Hit rate target: 80% (reduces DB queries)
  
  Cache Layer 2: Anomaly Model Data
    Key: anomaly:participant:{participant_id}
    Value: {historical_interruption_rate, first_session_flag}
    TTL: 604800 seconds (7 days)
    Hit rate target: 95%
  
  Cache Layer 3: Validation Ruleset
    Key: rules:v2.0.0
    Value: {all validation rules, serialized}
    TTL: 3600 seconds (refresh hourly)
    Hit rate target: 99.9%

Observability Integration:
  ✓ Prometheus metrics (latency histogram, error counts)
  ✓ Loki log aggregation (structured JSON logs)
  ✓ OpenTelemetry tracing (request flow)
```

---

## 7️⃣ SECURITY ENFORCEMENT (SEALED - ZERO-TRUST)

```
SECURITY LAYERS (NON-NEGOTIABLE):

Layer 1: Tenant Isolation (Database + Application)
  ✓ All queries filtered by WHERE tenant_id = authenticated_tenant_id
  ✓ Row-level security (RLS) enforced at PostgreSQL level
  ✓ No cross-tenant queries permitted (architecture enforced)
  ✓ Violation logging → CRITICAL alert → immediate escalation

Layer 2: Domain Isolation (Scoring Input Only)
  ✓ Agent only processes scoring-related inputs
  ✓ Rejects inputs with unrelated schema
  ✓ No cross-domain data leakage

Layer 3: Role-Based Authorization (RBAC)
  ✓ Only authenticated participants can submit scoring inputs
  ✓ Candidates submit own session metrics
  ✓ Recruiters submit metrics for sessions they own
  ✓ Role verified against JWT claims
  
  Roles:
    CANDIDATE: submit own scoring context
    RECRUITER: submit scoring context for own sessions
    MENTOR: submit scoring context for own dojo matches
    ADMIN: override decisions (requires audit trail)

Layer 4: Encryption Enforcement
  ✓ TLS 1.3 for all network communication (in-flight)
  ✓ At-rest encryption: PostgreSQL column encryption for sensitive fields
  ✓ Redis: AUTH token required + encrypted disk
  ✓ Kafka: SASL/SCRAM authentication

Layer 5: Audit Logging (Append-Only, Immutable)
  ✓ Every validation attempt logged to immutable audit log
  ✓ Log entry includes: timestamp, actor_id, input_hash, output, decision
  ✓ Logs stored in PostgreSQL with sequence numbers
  ✓ Cannot be modified or deleted (schema constraint)
  ✓ Backup: daily immutable snapshot to S3 with Object Lock (7+ year retention)

Layer 6: Rate Limiting (Per User, Per IP, Per Session)
  ✓ Max 100 validation requests per participant per hour
  ✓ Max 10,000 validation requests per IP per hour
  ✓ Max 1,000 validation requests per session during session lifetime
  ✓ Violation: return 429 Too Many Requests, log to security team

Layer 7: Input Validation & Injection Prevention (Comprehensive)
  ✓ All inputs validated against strict schema (see Section 3)
  ✓ SQL injection scanning (pattern matching)
  ✓ Script injection scanning (XSS, JavaScript, Python)
  ✓ Path traversal detection
  ✓ Command injection detection
  ✓ XXE detection
  ✓ Unicode attack detection
  ✓ Encoding attack detection

Layer 8: API Gateway Security (Kong)
  ✓ Request signing: X-Signature header (HMAC-SHA256)
  ✓ Token validation: JWT with 15-minute expiry
  ✓ Origin verification: whitelist internal services only
  ✓ DDoS protection: rate limit by IP (10,000 req/min)

Layer 9: Output Sanitization (Defense in Depth)
  ✓ All outputs serialized to JSON (no dangerous types)
  ✓ String values escaped (no embedded null bytes)
  ✓ No circular references (acyclic DAG only)
  ✓ Size limit: max 10MB per output
```

---

## 8️⃣ AUDIT & TRACEABILITY (LOCKED - IMMUTABLE)

```
MANDATORY AUDIT LOG ENTRY:

Every execution generates an immutable audit entry:

```sql
INSERT INTO sanitization_audit_logs (
  audit_id,                          -- UUID v4
  timestamp_utc,                     -- ISO8601
  service_name,                      -- 'phone_scoring_input_sanitizer_agent'
  actor_id,                          -- UUID of calling service (Interruption Agent)
  request_id,                        -- scoring_request_id (correlation key)
  session_id,                        -- UUID
  participant_id,                    -- UUID
  tenant_id,                         -- UUID for isolation
  
  input_hash,                        -- SHA256(serialized_input)
  input_metadata: {
    schema_version,
    field_count,
    text_field_lengths
  },
  
  output_hash,                       -- SHA256(sanitized_output)
  output_metadata: {
    sanitization_status,
    validation_rules_passed,
    validation_rules_failed
  },
  
  sanitization_status,               -- PASS | PASS_WITH_WARNINGS | PASS_WITH_CORRECTIONS | REJECT
  
  validation_results: {
    schema_check: PASS,
    type_check: PASS,
    range_check: PASS,
    enum_check: PASS,
    cross_field_check: PASS,
    temporal_check: PASS,
    reference_check: PASS,
    security_check: PASS,
    anomaly_check: LOW_ANOMALY,
    corrections_applied: 0
  },
  
  corrections_log: [
    {
      field: 'field_path',
      original: 'value',
      corrected: 'value',
      type: 'correction_type',
      justification: 'reason'
    }
  ],
  
  anomaly_detection: {
    anomaly_score: 0.45,
    is_outlier: false,
    benign_outlier: false,
    flags: []
  },
  
  security_scan_results: {
    sql_injection: CLEAR,
    script_injection: CLEAR,
    pii_leakage: CLEAR,
    path_traversal: CLEAR,
    violations_count: 0
  },
  
  processing_time_ms,
  
  decision_reason,                   -- human-readable explanation
  
  status: 'completed' | 'failed' | 'timeout'
)
```

Immutability Enforcement:
  ✓ audit_id + timestamp form primary key
  ✓ No UPDATE permitted (database schema enforces)
  ✓ Only INSERT and SELECT allowed
  ✓ Retention: permanent (no deletion policy)
  ✓ Backup: daily immutable snapshot to S3 Object Lock (7-year minimum retention)

Audit Access Control:
  ✓ Admins: full audit log access
  ✓ Recruiters: only own scoring audit entries
  ✓ Participants: can request their own session audit
  ✓ All queries: logged and monitored for suspicious patterns

Query Audit (Meta-Auditing):
  ✓ Every query against audit log is logged
  ✓ Who accessed what, when, why
  ✓ Stored in immutable audit_access_log
  ✓ Prevents audit tampering/coverups
```

---

## 9️⃣ FAILURE POLICY (SEALED - NO SILENT FAILURES)

```
FAILURE SCENARIOS & HANDLING (DETERMINISTIC):

Scenario 1: Validation Failure (Schema / Type / Range)
  Trigger: Input fails any validation rule
  Action:
    ✓ REJECT immediately
    ✓ LOG_FAILURE to audit_logs with specific violation
    ✓ Return 400 Bad Request with detailed error
    ✓ Do NOT proceed to Scoring Engine
    ✓ Escalate to: Calling service (provide correction guidance)

Scenario 2: Security Violation Detected
  Trigger: SQL injection, XSS, or command injection detected
  Action:
    ✓ REJECT immediately
    ✓ LOG_CRITICAL: "security_violation_detected"
    ✓ Return 400 Bad Request (don't expose pattern matched)
    ✓ ESCALATE to: security_team + admin_governance
    ✓ Alert severity: P1 (potential attack)

Scenario 3: Database Connection Failure
  Trigger: PostgreSQL or Redis connection fails
  Action:
    ✓ Retry with exponential backoff (3 retries, max 5 seconds)
    ✓ If all retries fail:
      - HALT execution
      - Return 503 Service Unavailable
      - ESCALATE to: ops_team
    ✓ No silent failure (caller knows state is unknown)

Scenario 4: Reference Validation Failure
  Trigger: session_id, participant_id, or audit_reference not found
  Action:
    ✓ REJECT input
    ✓ LOG_FAILURE: "reference_not_found"
    ✓ Return 400 with specific missing reference
    ✓ Do NOT proceed (stale/invalid data)

Scenario 5: Tenant Isolation Violation
  Trigger: session_tenant_id ≠ request_tenant_id
  Action:
    ✓ REJECT immediately
    ✓ Return 403 Forbidden
    ✓ LOG_CRITICAL: "tenant_isolation_violation"
    ✓ ESCALATE to: security_team (P1 incident)
    ✓ Trigger: Security review + access audit

Scenario 6: Anomaly Score Computation Failure
  Trigger: ML model inference fails or times out
  Action:
    ✓ CONTINUE execution (anomaly detection is optional)
    ✓ Skip anomaly scoring step
    ✓ Output: anomaly_report with computation_failed = true
    ✓ LOG_WARNING: "anomaly_detection_skipped"
    ✓ No escalation (non-critical path)

Scenario 7: Kafka Event Publishing Failure
  Trigger: Cannot publish sanitization event to Kafka
  Action:
    ✓ RETRY: 5 attempts with exponential backoff (max 10 seconds)
    ✓ If all retries fail:
      - STORE event in Redis queue for async replay
      - LOG_CRITICAL: "kafka_publish_failed"
      - ESCALATE to: messaging_team
    ✓ Response to caller: 202 Accepted (event queued)

Scenario 8: Cache Miss (Reference Lookup)
  Trigger: Reference data not found in Redis cache
  Action:
    ✓ Query PostgreSQL (cache miss is normal)
    ✓ Refresh cache with DB result
    ✓ Continue processing
    ✓ Log cache miss for observability (aggregate metrics)

Scenario 9: Rate Limit Exceeded
  Trigger: > 100 requests per participant per hour
  Action:
    ✓ STOP processing
    ✓ Return 429 Too Many Requests
    ✓ Include Retry-After header (guidance)
    ✓ LOG_WARNING: "rate_limit_exceeded"
    ✓ No escalation (expected behavior)

Scenario 10: Timeout During Validation
  Trigger: Validation exceeds 10-second timeout
  Action:
    ✓ HALT execution
    ✓ Return 504 Gateway Timeout
    ✓ LOG_CRITICAL: "validation_timeout"
    ✓ ESCALATE to: ops_team (performance issue)
    ✓ Possible causes: DB overload, network latency

ALL FAILURES MUST:
  ✓ Be logged with full context (input, error, actor, timestamp)
  ✓ Include audit reference for traceability
  ✓ Never silently fail (always inform caller)
  ✓ Generate observable metrics (Prometheus counters)
  ✓ Trigger escalation rules when appropriate (PagerDuty)
```

---

## 🔟 INTER-AGENT DEPENDENCY MAP (SEALED)

```
UPSTREAM AGENTS (Metric Providers):

  1. Phone Interruption Detection Agent (PRIMARY)
     Output: interruption_detected events
     Frequency: Variable (per interruption detected)
     Contains: Confidence scores, severity, evidence, audit reference
     SLA: P99 latency < 300ms to our agent
     Protocol: Kafka topic: events.interruption_detected
  
  2. Session Context Providers (VALIDATION ONLY)
     Output: Session metadata (for cross-validation)
     Sources:
       - Voice GD Orchestrator: gd.session_metadata
       - Interview Service: interview.session_metadata
       - Dojo Match Engine: match.session_metadata
     Frequency: Once per session
     Usage: Validate input against actual session state
     SLA: Cache-based (low latency)

DOWNSTREAM AGENTS (Consumers):

  1. Scoring Engine (PRIMARY CONSUMER - CRITICAL)
     Input: SANITIZED_SCORING_INPUT (only if status = PASS*)
     Action: Applies fairness penalties/credits
     SLA: Must consume within 120 seconds
     Contract: Only accepts inputs from Sanitizer Agent
     Rejection Behavior: If receives malformed input, escalates to Governance
  
  2. Data Quality Monitoring Service (OBSERVABILITY)
     Input: DATA_QUALITY_REPORT events
     Action: Aggregates metrics, detects degradation
     SLA: Eventually consistent (batched hourly)
     Contract: Structured quality metrics
  
  3. Admin Governance Service (ESCALATION)
     Input: ANOMALY_FLAG (anomaly_score > 0.80) + ESCALATION_SIGNAL
     Action: Flags for manual review
     SLA: Must consume within 600 seconds
     Contract: Full evidence + recommendations
  
  4. Audit Service (COMPLIANCE)
     Input: audit_reference UUID
     Action: Stores immutable audit logs
     SLA: < 500ms response time
     Contract: Full audit context for appeals/disputes
  
  5. Analytics Service (BUSINESS INTELLIGENCE)
     Input: Sanitization quality metrics (batched)
     Action: Reporting, anomaly trending
     SLA: Eventually consistent (daily batch)

EVENT FLOW DIAGRAM (LOCKED):

```
┌──────────────────────────────────────────────────────────────┐
│              UPSTREAM METRIC PRODUCER                        │
├──────────────────────────────────────────────────────────────┤
│                                                              │
│  Phone Interruption Detection Agent                          │
│        ↓                                                     │
│  Kafka Topic: events.interruption_detected                   │
│                                                              │
└──────────────────────────────────────────────────────────────┘
                           │
                           │ Kafka Consumer Group
                           ↓
┌──────────────────────────────────────────────────────────────┐
│   PHONE_SCORING_INPUT_SANITIZER_AGENT                       │
│  ┌──────────────────────────────────────────────────────┐   │
│  │ 1. Schema Validation                                 │   │
│  │ 2. Type Validation                                   │   │
│  │ 3. Range & Enum Validation                           │   │
│  │ 4. Cross-Field Consistency Checks                    │   │
│  │ 5. Database Reference Validation                     │   │
│  │ 6. Security Scanning (Injection, PII detection)      │   │
│  │ 7. Anomaly Detection (optional ML layer)             │   │
│  │ 8. Data Correction (limited scope)                   │   │
│  │ 9. Output Construction & Serialization               │   │
│  │ 10. Audit Log Entry Creation (immutable)             │   │
│  │ 11. Event Emission to Kafka (async)                  │   │
│  └──────────────────────────────────────────────────────┘   │
└──────────────────────────────────────────────────────────────┘
        │                          │                      │
        │ (if PASS/PASS_*)         │ (if anomaly_score   │ (all events)
        │                          │  > 0.80)            │
        ↓                          ↓                      ↓
 ┌─────────────────┐   ┌──────────────────┐  ┌──────────────────┐
 │ Scoring Engine  │   │ Admin Governance │  │ Analytics Service│
 │                 │   │ Service          │  │                  │
 │ Applies scoring │   │ Manual Review    │  │ Quality Metrics  │
 │ adjustments     │   │ Escalation       │  │ & Trending       │
 └─────────────────┘   └──────────────────┘  └──────────────────┘
        │                      │                        │
        └──────────┬───────────┴────────────────────────┘
                   │
                   ↓
        ┌──────────────────────┐
        │ Audit Service        │
        │ (immutable logs)      │
        └──────────────────────┘
```

INTER-SERVICE CONTRACTS (SEALED):

  Contract 1: Interruption Agent → Sanitizer Agent
    Event: events.interruption_detected
    Schema: See Input Contract (Section 3)
    Retry: Kafka guaranteed delivery (3 partitions × 3 replicas)
    Timeout: Agent must return result within 10 seconds
  
  Contract 2: Sanitizer Agent → Scoring Engine
    Event: events.sanitization_completed (status = PASS*)
    Payload: SANITIZED_SCORING_INPUT
    Guarantee: At-least-once delivery via Kafka
    SLA: Scoring must consume within 120 seconds
  
  Contract 3: Sanitizer Agent → Admin Governance Service
    Event: anomaly_requires_manual_review
    Payload: Complete anomaly_report + evidence
    Triggers: Only on anomaly_score > 0.80
    SLA: Admin must be notified within 600 seconds
  
  Contract 4: Sanitizer Agent → Audit Service
    Event: sanitization_audit_entry_created
    Payload: audit_reference UUID (link to immutable log)
    Guarantee: Synchronous (audit must be stored before response)
    SLA: < 500ms
```

---

## 1️⃣1️⃣ PASSIVE INTELLIGENCE COMPATIBILITY (OPTIONAL)

```
FEATURE VECTOR EMISSION (IF INTEGRATED):

If passive intelligence layer exists, Sanitizer Agent MAY emit
quality signals about input data for ML training.

FEATURE_STRUCTURE (OPTIONAL):

EMIT_WHEN: Sanitization completes successfully

{
  user_id: UUID,
  feature_name: "input_validation_quality_score",
  feature_value: float (0-1),
  timestamp: ISO8601,
  source_agent: "phone_scoring_input_sanitizer_agent",
  model_version: semantic_version,
  
  context: {
    validation_status: enum,
    correction_count: integer,
    anomaly_score: float,
    security_violations_detected: integer,
    processing_latency_ms: integer
  }
}

INTEGRATION RULES:
  ✓ Only emit if validation passed or passed_with_corrections
  ✓ Do NOT emit on REJECT (no feature signal for failures)
  ✓ Features represent DATA QUALITY, not decision quality
  ✓ Used for trend analysis, not individual predictions

PASSIVE INTELLIGENCE USAGE:
  - High-quality inputs → suggests reliable participant behavior
  - Repeated corrections → suggests participant pattern
  - Frequent anomalies → suggests environmental factors
```

---

## 1️⃣2️⃣ INNOVATION ECONOMY COMPATIBILITY (NOT APPLICABLE)

```
This agent does not touch the innovation economy layer.
Scoring input sanitization is not related to ideas/innovation.

No integration required with:
  - IDEA_VECTOR
  - ORIGINALITY_SCORE
  - IDEA_DNA_AGENT
  - ROYALTY_ENGINE
```

---

## 1️⃣3️⃣ GROWTH ENGINE HOOK (NOT APPLICABLE)

```
This agent does not affect ranking/achievement systems.
Its role is data quality enforcement, not scoring outcome.

Growth Engine Independence:
  - Sanitization passes/fails input validation
  - Scoring Engine makes actual fairness decisions
  - Growth Engine tracks achievements independently
  
No XP or ranking changes triggered by sanitization alone.
```

---

## 1️⃣4️⃣ PERFORMANCE MONITORING (LOCKED - PROMETHEUS)

```
MANDATORY METRICS (SEALED):

Prometheus Metrics Exposed:

1. Validation Metrics:
   phone_scoring_input_sanitization_total
     label: status (PASS, PASS_WITH_WARNINGS, PASS_WITH_CORRECTIONS, REJECT)
     label: validation_layer (schema, type, range, enum, cross_field, temporal, reference, security, anomaly)
     label: model_version
   
   phone_scoring_input_rejection_reasons_total
     label: reason (missing_field, type_mismatch, range_violation, security_violation, etc)
   
   phone_scoring_input_corrections_applied_total
     label: correction_type (format_normalization, precision_rounding, etc)

2. Performance Metrics:
   phone_scoring_input_sanitization_latency_seconds
     quantiles: [0.50, 0.90, 0.99]
   
   phone_scoring_input_validation_layer_duration_seconds
     histogram by layer (schema, type, range, security)
   
   phone_scoring_input_reference_lookup_duration_seconds
     histogram (DB queries + cache hits)
   
   phone_scoring_input_anomaly_detection_duration_seconds
     histogram (ML inference time)

3. Quality Metrics:
   phone_scoring_input_anomaly_score_distribution
     histogram (0-1 range)
   
   phone_scoring_input_false_rejection_rate_estimated
     gauge (based on appeals/reversals)
   
   phone_scoring_input_data_corruption_detected_total
     counter (integrity check failures)

4. Security Metrics:
   phone_scoring_input_security_violations_total
     label: violation_type (sql_injection, xss, pii_leakage, etc)
   
   phone_scoring_input_suspicious_activity_attempts_total
     counter
   
   phone_scoring_input_rate_limit_violations_total
     counter

5. Dependency Metrics:
   phone_scoring_input_kafka_consumer_lag
     gauge (event processing lag)
   
   phone_scoring_input_cache_hit_rate
     gauge (Redis cache performance)
   
   phone_scoring_input_database_connection_errors_total
     counter

Alerting Rules (PagerDuty Integration):

  Alert 1: Validation Latency SLA Breach
    Condition: p99(sanitization_latency) > 300ms
    Severity: P3
    Action: Alert ops team
  
  Alert 2: Security Violation Detected
    Condition: security_violations_total > 0 (any spike)
    Severity: P1
    Action: Page security team immediately
  
  Alert 3: High Rejection Rate
    Condition: rejection_rate > 5% (sustained)
    Severity: P2
    Action: Alert data quality team (possible input schema change)
  
  Alert 4: Anomaly Detection Timeout
    Condition: anomaly_detection_latency_p99 > 5000ms
    Severity: P3
    Action: Alert ML team (model performance issue)
  
  Alert 5: Database Connection Failures
    Condition: db_connection_errors_total > 10 (per 5 minutes)
    Severity: P2
    Action: Page database team
  
  Alert 6: Kafka Consumer Lag > 60 seconds
    Condition: kafka_consumer_lag > 60000ms
    Severity: P2
    Action: Page messaging team

Grafana Dashboards (LOCKED):

  Dashboard 1: Agent Health
    - Throughput (RPS)
    - Latency P50/P90/P99
    - Error rate (rejections)
    - Validation failure breakdown
    - Processing duration by layer
  
  Dashboard 2: Data Quality
    - Correction frequency
    - Anomaly score distribution
    - Data corruption detection
    - Top failure reasons
  
  Dashboard 3: Security Status
    - Security violations timeline
    - Violation types breakdown
    - Rate limit events
    - Suspicious activity trends
  
  Dashboard 4: Performance & Scaling
    - Cache hit rate
    - DB query latency
    - Kafka consumer lag
    - Container resource usage
```

---

## 1️⃣5️⃣ VERSIONING POLICY (SEALED - IMMUTABLE)

```
VERSIONING CONSTRAINT (NON-NEGOTIABLE):

All Changes: Add-Only, Never Destructive

Semantic Versioning Scheme:
  Format: major.minor.patch
  Example: 1.2.3
  
  MAJOR: Breaking changes to input/output schema
         Scoring Engine integration required
         Backward compatibility NOT guaranteed
  
  MINOR: New validation rules, new optional fields
         Fully backward compatible
         Existing validations unchanged
  
  PATCH: Bug fixes, performance improvements
         Zero breaking changes
         Automatic deployment (no approval needed)

Change Process (LOCKED):

  1. PROPOSAL PHASE
     - Draft change proposal with impact analysis
     - Security review (new validation rules safe?)
     - Get approval from: Tech Lead + Compliance
  
  2. IMPLEMENTATION PHASE
     - Create feature branch
     - Implement validation rules
     - Add comprehensive unit tests
     - Add integration tests against test Scoring Engine
  
  3. VALIDATION PHASE
     - Run full test suite (coverage ≥ 95%)
     - Performance benchmark (latency targets verified)
     - Security scan (new validation rules don't enable bypass)
     - Compatibility test (Scoring Engine acceptance)
  
  4. DEPLOYMENT PHASE
     - Deploy to staging (blue/green)
     - Run smoke tests
     - Monitor for 24 hours
     - If green: deploy canary (5% → 50% → 100%)
     - If red: rollback immediately
  
  5. DOCUMENTATION PHASE
     - Update this agent spec (CHANGELOG section)
     - Update validation rule documentation
     - Train support team on new rules
     - Add migration guide if breaking change

Backward Compatibility Rules (MANDATORY):

  ✓ Old input versions must still be accepted
  ✓ Old output versions must still be producible
  ✓ New validation rules: optional (with opt-in)
  ✓ New enum values: safe (backward compatible)
  ✓ New optional fields: safe (with defaults)
  ✓ Field deletions: forbidden (deprecate instead)
  ✓ Type changes: forbidden (create new field)
  ✓ Range changes: allowed (if expanding, not shrinking)

Version Tracking (IMMUTABLE):

  Every output includes:
    - sanitizer_version (this agent spec version)
    - validation_ruleset_version
    - anomaly_model_version
  
  All versions stored in audit log for reproducibility.

Rollback Capability (REQUIRED):

  Keep last 4 versions deployable at all times:
    v1.2.3 (current)
    v1.2.2
    v1.2.1
    v1.2.0
  
  If critical bug in v1.2.3:
    1. Rollback to v1.2.2 (< 5 minutes)
    2. Investigate root cause
    3. Fix in v1.2.4
    4. Deploy v1.2.4 (canary)
```

---

## 1️⃣6️⃣ NON-NEGOTIABLE RULES (SEALED & LOCKED)

```
FORBIDDEN ACTIONS (ABSOLUTE CONSTRAINTS):

This agent MUST NOT:

  ✗ Create hidden validation rules not documented
  ✗ Modify historical audit records (append-only only)
  ✗ Auto-delete audit logs (permanent retention only)
  ✗ Override Admin Governance Service decisions
  ✗ Bypass tenant isolation checks
  ✗ Execute outside declared scope (scoring inputs only)
  ✗ Make fairness decisions (pass input to Scoring Engine only)
  ✗ Persist state in agent memory (stateless only)
  ✗ Use AI for validation (rules only)
  ✗ Accept malformed inputs and "fix" without permission
  ✗ Correct confidence scores or metrics (only format normalization)
  ✗ Emit interruption detection events (not its role)
  ✗ Access participant data outside session context
  ✗ Store PII outside encrypted channels
  ✗ Operate without active monitoring dashboards
  ✗ Process requests from unauthenticated callers
  ✗ Return generic errors (must specify failure reason)
  ✗ Silently fail (all failures logged + escalated)
  ✗ Ignore security violations
  ✗ Disable validation for "performance"

MANDATORY ACTIONS (ENFORCEMENT):

This agent MUST:

  ✓ Validate all inputs against strict schema
  ✓ Log every validation decision with context
  ✓ Emit structured events to Kafka
  ✓ Respect tenant isolation boundaries
  ✓ Return explicit error codes on failure
  ✓ Generate immutable audit trails
  ✓ Store all versions for reproducibility
  ✓ Perform security scanning on all inputs
  ✓ Escalate security violations immediately
  ✓ Support full rollback capability
  ✓ Maintain latency targets (P99 < 300ms)
  ✓ Scale horizontally (stateless)
  ✓ Integrate with observability stack
  ✓ Encrypt all data in transit + at rest
  ✓ Document all changes (add-only versioning)
  ✓ Pass security audits annually
  ✓ Support dispute resolution (full audit access)
  ✓ Respond to compliance requests
  ✓ Publish performance metrics
  ✓ Reject invalid inputs (never guess or infer)

GOVERNANCE OVERSIGHT (LOCKED):

This agent is subject to:
  ✓ Phone Interruption Detection Agent (upstream validator)
  ✓ Scoring Engine (downstream consumer)
  ✓ Admin Governance Service (escalation authority)
  ✓ Compliance & Legal (regulatory requirements)
  ✓ Security Team (threat modeling)
  ✓ Data Science (validation quality SLA)
  ✓ Platform Engineering (deployment gates)
  ✓ Finance/Legal (contractual obligations)

No single engineer can override these constraints.
Changes require multi-stakeholder approval.
```

---

## CHANGELOG & VERSION HISTORY

```
v1.0.0 - 2025-03-04 (SEALED RELEASE)
  ✓ Initial sealed specification for Antigravity platform
  ✓ Comprehensive input validation framework (7 layers)
  ✓ Security scanning engine (SQL injection, XSS, PII detection)
  ✓ Deterministic correction engine (limited scope)
  ✓ Anomaly detection layer (optional ML)
  ✓ 100% immutable audit trail
  ✓ Multi-tenant zero-trust architecture
  ✓ Kafka-driven event streaming
  ✓ Prometheus-based observability
  ✓ Compliance-ready documentation
```

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
