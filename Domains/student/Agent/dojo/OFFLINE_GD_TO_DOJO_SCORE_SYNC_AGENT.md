# 🔒 OFFLINE_GD_TO_DOJO_SCORE_SYNC_AGENT
## Sealed & Locked Enterprise Agent Specification
### Ecoskiller Antigravity Platform

---

## 1️⃣ AGENT IDENTITY (MANDATORY - SEALED)

```
AGENT_NAME = OFFLINE_GD_TO_DOJO_SCORE_SYNC_AGENT
SYSTEM_ROLE = Data Consistency & Cross-Session Score Synchronizer
PRIMARY_DOMAIN = Bidirectional score sync between Voice GD and Dojo Match sessions
EXECUTION_MODE = Deterministic + Stateful (maintains sync state machine)
DATA_SCOPE = Session scores, fairness adjustments, participant rankings
TENANT_SCOPE = Strict Multi-Tenant Isolation (row-level security enforced)
FAILURE_POLICY = HALT on data corruption, LOG all mismatches, ESCALATE conflicts
IMMUTABILITY_LEVEL = SEALED - No runtime modification, versioned only
VERSIONING_CONSTRAINT = Add-only, backward compatible, audit immutable
PROCESS_STAGE = Data Ingestion → Validation → Conflict Detection → Merge → Sync
CRITICALITY = CRITICAL (maintains platform scoring integrity)
EXECUTION_PATTERN = Batch (hourly/4-hourly sync) + Event-driven (completion triggers)
CONSISTENCY_MODEL = Eventual Consistency (with bounded divergence)
```

**SEALED CONSTRAINT:**
This agent definition is locked in immutable state. No modifications allowed without:
1. Formal Architecture Review Board approval
2. Data consistency proof (no score loss, no duplication)
3. Conflict resolution strategy review
4. Audit trail completeness verification

---

## 2️⃣ PURPOSE DECLARATION (SEALED)

### Problem This Agent Solves

**Cross-Session Fairness Consistency:**
- Participants engage in both Voice GD sessions AND Dojo matches
- Scoring fairness must be consistent across session types
- Phone interruptions in GD should inform Dojo fairness assessments
- Ranking/progression must not be artificially distorted by single session type

**Offline Resilience:**
- Network failures may prevent real-time sync during GD/Dojo sessions
- Scores may be finalized offline (especially on mobile)
- System must eventually sync without losing data or fairness
- Participants disconnected must still be ranked fairly post-reconnection

**Data Integrity Guarantee:**
- Prevent score duplication (same adjustment applied twice)
- Detect conflicts (both GD and Dojo updated same participant)
- Resolve conflicts deterministically (no manual intervention needed)
- Maintain audit trail of all sync decisions

**Ranking Consistency:**
- Participants ranked by combined GD+Dojo performance
- Scores must be normalized across session types (GD 0-100, Dojo 0-100)
- Interruption fairness adjustments must be consistently applied
- Percentile rankings must be recalculated after each sync

**Temporal Causality:**
- Ensure scores reflected in rankings in correct order
- Prevent "future" scores affecting past rankings
- Maintain session timestamps as source of truth
- Detect and quarantine out-of-order updates

### Input Consumed

**Primary Input Source:**
- Voice GD completion events (final scores, fairness adjustments)
- Dojo match completion events (final scores, skill assessments)
- Participant ranking snapshots (before/after scores)
- Sync state markers (last sync timestamp, version)

**Secondary Input Sources:**
- Session metadata (type, duration, participants, timestamp)
- Scoring adjustments (fairness penalties/credits applied)
- Phone interruption signals (from prior agents)
- Device sync status (online/offline at completion)

**Tertiary Input Sources:**
- Previous sync state (for detecting conflicts)
- Participant historical scores (for consistency validation)
- Platform configuration (fairness normalization factors)
- Appeal/dispute data (for conflict resolution context)

### Output Produced

**Primary Output:**
- `SYNC_RESULT` (success/partial/conflict)
- `SYNCED_PARTICIPANT_SCORES` (consistent cross-session scores)
- `UPDATED_RANKINGS` (recalculated with new data)
- `CONFLICT_RESOLUTION_LOG` (all decisions made)

**Secondary Output:**
- `FAIRNESS_CONSISTENCY_SIGNAL` (no anomalous score jumps)
- `RANKING_RECALCULATION_REPORT` (percentiles updated)
- `DATA_DIVERGENCE_DETECTION` (mismatches found/resolved)
- `SYNC_STATE_CHECKPOINT` (new baseline for next sync)

**Tertiary Output:**
- `ESCALATION_SIGNAL` (unresolvable conflicts requiring human review)
- `AUDIT_LOG_ENTRY` (immutable record of all sync decisions)
- `COMPENSATION_SIGNAL` (if errors detected, who needs adjustment)
- `NEXT_TRIGGER_EVENT` (cascading updates needed)

### Upstream Agent Dependency Chain

1. **Voice GD Orchestrator** (session completion)
   - Output: Session completion event with final scores
   - Frequency: Per session completion
   - Contains: Participant scores, fairness adjustments, phase metrics

2. **Dojo Match Engine** (match completion)
   - Output: Match completion event with final scores
   - Frequency: Per match completion
   - Contains: Skill assessments, match results, performance metrics

3. **Scoring Engine** (fairness adjustments)
   - Output: Finalized fairness adjustments applied
   - Frequency: Per session
   - Contains: Adjustment rationale, applied amounts

4. **Participant Ranking Service** (current rankings)
   - Output: Historical ranking snapshots
   - Frequency: Per ranking recalculation
   - Contains: Percentiles, comparative scores

### Downstream Agent Consumption

1. **Ranking Service** (primary consumer)
   - Input: `UPDATED_RANKINGS` (new percentiles)
   - Action: Updates public rankings
   - SLA: Must consume within 300 seconds
   - Contract: Deterministic ranking calculation

2. **Participant Profile Service** (score display)
   - Input: `SYNCED_PARTICIPANT_SCORES` (canonical scores)
   - Action: Updates displayed scores
   - SLA: P99 < 5 minutes propagation
   - Contract: Only canonical synced scores

3. **Admin Governance Service** (escalation)
   - Input: `ESCALATION_SIGNAL` (conflicts requiring review)
   - Action: Manual conflict resolution
   - SLA: Must consume within 3600 seconds
   - Contract: Full conflict evidence + recommendation

4. **Analytics Service** (auditing)
   - Input: `SYNC_STATE_CHECKPOINT` (for historical reconstruction)
   - Action: Maintains consistency audit trail
   - SLA: Eventually consistent (batched)
   - Contract: Complete sync state history

5. **Platform Health Dashboard** (operations)
   - Input: `FAIRNESS_CONSISTENCY_SIGNAL` (health metric)
   - Action: Real-time consistency monitoring
   - SLA: < 5 minutes latency
   - Contract: Boolean consistency status

---

## 3️⃣ INPUT CONTRACT (STRICT - SEALED)

```
INPUT_SCHEMA: {
  
  sync_request_id: {
    type: "uuid",
    required: true,
    validation: "must be unique within 24-hour window",
    domain_check: "idempotency key for deduplication"
  },
  
  sync_trigger_type: {
    type: "enum",
    required: true,
    allowed_values: [
      "scheduled_hourly",
      "gd_completion_event",
      "dojo_completion_event",
      "manual_reconciliation",
      "conflict_resolution",
      "offline_reconciliation"
    ],
    semantics: "what triggered this sync?"
  },
  
  sync_direction: {
    type: "enum",
    required: true,
    allowed_values: [
      "gd_to_dojo",
      "dojo_to_gd",
      "bidirectional",
      "conflict_resolution"
    ],
    semantics: "which direction should changes flow?"
  },
  
  participant_context: {
    type: "object",
    required: true,
    fields: {
      
      participant_id: {
        type: "uuid",
        required: true,
        validation: "must exist in participants table"
      },
      
      tenant_id: {
        type: "uuid",
        required: true,
        validation: "must match authenticated tenant"
      },
      
      sync_scope: {
        type: "enum",
        required: true,
        allowed_values: [
          "single_participant",
          "participant_cohort",
          "session_group",
          "full_platform"
        ],
        semantics: "how many participants affected?"
      },
      
      affected_participant_count: {
        type: "integer",
        required: true,
        range: [1, 1000000],
        validation: "matches actual count of affected participants"
      }
    }
  },
  
  gd_session_data: {
    type: "object",
    required: true,
    fields: {
      
      session_id: {
        type: "uuid",
        required: true
      },
      
      session_timestamp_utc: {
        type: "iso8601_datetime",
        required: true,
        validation: "must be within GD session window"
      },
      
      participant_scores: {
        type: "array",
        required: true,
        items: {
          type: "object",
          fields: {
            participant_id: { type: "uuid" },
            final_score: {
              type: "float",
              range: [0, 100],
              precision: 0.1,
              validation: "must match scoring engine calculation"
            },
            
            fairness_adjustments: {
              type: "object",
              fields: {
                interruption_penalty: {
                  type: "float",
                  range: [-10, 0],
                  semantics: "phone interruption fairness penalty"
                },
                
                recovery_credit: {
                  type: "float",
                  range: [0, 5],
                  semantics: "credit for good recovery behavior"
                },
                
                other_adjustments: {
                  type: "float",
                  range: [-10, 10],
                  semantics: "any other fairness adjustments"
                }
              }
            },
            
            base_score_before_adjustment: {
              type: "float",
              range: [0, 100],
              validation: "must equal final_score - (all adjustments)"
            }
          }
        },
        max_items: 10,
        validation: "all participants must be in session"
      }
    }
  },
  
  dojo_match_data: {
    type: "object",
    required: true,
    fields: {
      
      match_id: {
        type: "uuid",
        required: true
      },
      
      match_timestamp_utc: {
        type: "iso8601_datetime",
        required: true
      },
      
      participant_scores: {
        type: "array",
        required: true,
        items: {
          type: "object",
          fields: {
            participant_id: { type: "uuid" },
            
            final_score: {
              type: "float",
              range: [0, 100],
              precision: 0.1
            },
            
            skill_assessment: {
              type: "object",
              fields: {
                technical_score: { type: "float", range: [0, 100] },
                communication_score: { type: "float", range: [0, 100] },
                collaboration_score: { type: "float", range: [0, 100] }
              }
            },
            
            match_outcome: {
              type: "enum",
              allowed: ["win", "loss", "draw"]
            }
          }
        },
        max_items: 4,
        validation: "typically 2-4 participants per match"
      }
    }
  },
  
  previous_sync_state: {
    type: "object",
    required: true,
    fields: {
      
      last_sync_timestamp_utc: {
        type: "iso8601_datetime",
        required: true,
        semantics: "when was sync last successfully completed?"
      },
      
      last_sync_version: {
        type: "string",
        required: true,
        format: "semantic versioning",
        semantics: "which sync algorithm version was used?"
      },
      
      participant_scores_at_last_sync: {
        type: "object",
        required: true,
        fields: {
          participant_id: { type: "uuid" },
          gd_score: { type: "float" },
          dojo_score: { type: "float" },
          combined_score: { type: "float" },
          sync_hash: { type: "string" }
        },
        semantics: "baseline for detecting changes"
      },
      
      known_conflicts: {
        type: "array",
        required: false,
        items: {
          type: "object",
          fields: {
            participant_id: { type: "uuid" },
            conflict_type: {
              type: "enum",
              allowed: [
                "score_divergence",
                "timestamp_inversion",
                "adjustment_duplication",
                "orphaned_score"
              ]
            },
            conflict_timestamp_utc: { type: "iso8601_datetime" },
            resolution_status: {
              type: "enum",
              allowed: ["open", "pending_review", "resolved", "escalated"]
            }
          }
        },
        max_items: 100,
        semantics: "previously detected conflicts"
      }
    }
  },
  
  offline_context: {
    type: "object",
    required: false,
    fields: {
      
      offline_duration_seconds: {
        type: "integer",
        range: [0, 3600],
        semantics: "how long was system offline?"
      },
      
      offline_reason: {
        type: "enum",
        allowed: [
          "network_disconnection",
          "server_maintenance",
          "participant_offline",
          "data_sync_backlog",
          "unknown"
        ]
      },
      
      device_sync_status_mapping: {
        type: "object",
        fields: {
          participant_id: { type: "uuid" },
          was_online_at_completion: { type: "boolean" },
          client_local_score: { type: "float" },
          sync_acknowledged: { type: "boolean" }
        },
        semantics: "device sync status for each participant"
      },
      
      offline_events_queued: {
        type: "integer",
        range: [0, 100000],
        semantics: "how many events queued while offline?"
      }
    }
  },
  
  fairness_context: {
    type: "object",
    required: true,
    fields: {
      
      phone_interruption_signals: {
        type: "array",
        required: true,
        items: {
          type: "object",
          fields: {
            participant_id: { type: "uuid" },
            gd_session_id: { type: "uuid" },
            interruption_count: { type: "integer" },
            fairness_adjustment_applied: { type: "float" },
            cross_session_fairness_factor: {
              type: "float",
              range: [0.8, 1.2],
              semantics: "should Dojo score be adjusted for GD fairness?"
            }
          }
        },
        max_items: 1000,
        semantics: "interruption data affecting fairness"
      },
      
      fairness_normalization_factors: {
        type: "object",
        fields: {
          gd_to_dojo_conversion_factor: {
            type: "float",
            range: [0.5, 1.5],
            default: 1.0,
            semantics: "multiply GD adjustments by this when applying to Dojo"
          },
          
          dojo_to_gd_conversion_factor: {
            type: "float",
            range: [0.5, 1.5],
            default: 1.0,
            semantics: "multiply Dojo adjustments by this when applying to GD"
          },
          
          combined_score_weight_gd: {
            type: "float",
            range: [0, 1],
            default: 0.5,
            semantics: "weight for GD in combined ranking"
          },
          
          combined_score_weight_dojo: {
            type: "float",
            range: [0, 1],
            default: 0.5,
            semantics: "weight for Dojo in combined ranking"
          }
        }
      }
    }
  },
  
  conflict_resolution_policy: {
    type: "object",
    required: true,
    fields: {
      
      conflict_resolution_strategy: {
        type: "enum",
        required: true,
        allowed_values: [
          "gd_priority",
          "dojo_priority",
          "newer_timestamp_priority",
          "human_review",
          "conservative_merge"
        ],
        semantics: "how should conflicts be resolved?"
      },
      
      allow_score_adjustment_on_merge: {
        type: "boolean",
        required: true,
        semantics: "can scores be modified during merge (vs kept as-is)?"
      },
      
      max_score_divergence_tolerance: {
        type: "float",
        required: true,
        range: [0.1, 10.0],
        semantics: "max allowed difference before flagging conflict"
      },
      
      escalate_unresolvable_conflicts: {
        type: "boolean",
        required: true,
        semantics: "send to admin governance if conflict unresolvable?"
      }
    }
  },
  
  VALIDATION_SCOPE: {
    type: "array",
    required: false,
    items: { type: "string" },
    allowed_values: [
      "schema_validation",
      "temporal_validation",
      "score_consistency",
      "conflict_detection",
      "fairness_validation",
      "rank_recalculation",
      "offline_reconciliation"
    ],
    default: ["all validations"]
  }
}

VALIDATION_RULES (MANDATORY - LOCKED):

  ✓ ALL required fields must be present (no null tolerance)
  ✓ Sync direction must be deterministic (no ambiguity)
  ✓ Timestamps must be monotonic (no time travel)
  ✓ Scores must be within valid ranges (0-100)
  ✓ Fairness adjustments must be consistent across sessions
  ✓ Participant IDs must exist in database
  ✓ Session/match IDs must exist and match timestamps
  ✓ Tenant isolation verified on all data
  ✓ No duplicate adjustments detected
  ✓ Conversion factors must sum properly
  ✓ Conflict resolution strategy must be valid
  ✓ Previous sync state must exist (no orphaned sync)

SECURITY_CHECKS (MANDATORY):

  ✓ Verify tenant_id isolation (no cross-tenant sync)
  ✓ Verify data ownership (scores belong to correct participant)
  ✓ Verify timestamp ordering (no score from future)
  ✓ Verify fairness adjustments authorized (not modified)
  ✓ Check for score tampering (hash verification)
  ✓ Verify immutable fields unchanged (base_score, etc)
```

---

## 4️⃣ OUTPUT CONTRACT (STRICT - SEALED)

```
OUTPUT_SCHEMA: {
  
  sync_status: {
    type: "enum",
    required: true,
    allowed_values: ["SUCCESS", "PARTIAL_SUCCESS", "CONFLICT_DETECTED", "FAILED"],
    semantics: {
      SUCCESS: "all scores synced, no conflicts, rankings updated",
      PARTIAL_SUCCESS: "some scores synced, minor conflicts auto-resolved",
      CONFLICT_DETECTED: "significant conflicts requiring human review",
      FAILED: "sync failed, no changes applied, data unchanged"
    }
  },
  
  synced_participant_scores: {
    type: "array",
    required: true,
    items: {
      type: "object",
      fields: {
        
        participant_id: {
          type: "uuid",
          required: true
        },
        
        gd_score: {
          type: "float",
          required: true,
          range: [0, 100],
          precision: 0.1,
          semantics: "canonical GD score after sync"
        },
        
        dojo_score: {
          type: "float",
          required: true,
          range: [0, 100],
          precision: 0.1,
          semantics: "canonical Dojo score after sync"
        },
        
        combined_score: {
          type: "float",
          required: true,
          range: [0, 100],
          precision: 0.1,
          semantics: "weighted average for ranking"
        },
        
        gd_fairness_adjustments: {
          type: "float",
          required: true,
          range: [-10, 10],
          semantics: "total fairness adjustments in GD"
        },
        
        dojo_fairness_adjustments: {
          type: "float",
          required: true,
          range: [-10, 10],
          semantics: "total fairness adjustments in Dojo"
        },
        
        cross_session_fairness_adjustments: {
          type: "float",
          required: true,
          range: [-10, 10],
          semantics: "adjustments applied to sync disparities"
        },
        
        score_sync_history: {
          type: "array",
          required: true,
          items: {
            type: "object",
            fields: {
              timestamp_utc: { type: "iso8601_datetime" },
              session_type: { type: "enum", allowed: ["gd", "dojo"] },
              score_before: { type: "float" },
              score_after: { type: "float" },
              change_reason: { type: "string" }
            }
          },
          max_items: 100,
          semantics: "complete history of score changes"
        },
        
        score_change_from_sync: {
          type: "float",
          range: [-100, 100],
          semantics: "how much did this participant's score change in this sync?"
        },
        
        sync_confidence: {
          type: "float",
          range: [0, 1],
          semantics: "how confident are we in these synced scores?"
        }
      }
    },
    max_items: 1000000
  },
  
  updated_rankings: {
    type: "object",
    required: true,
    semantics: "recalculated rankings with new scores",
    
    fields: {
      
      ranking_timestamp_utc: {
        type: "iso8601_datetime",
        required: true
      },
      
      total_ranked_participants: {
        type: "integer",
        required: true,
        range: [1, 1000000]
      },
      
      ranking_changes: {
        type: "array",
        required: true,
        items: {
          type: "object",
          fields: {
            participant_id: { type: "uuid" },
            previous_rank: { type: "integer", range: [1, 1000000] },
            new_rank: { type: "integer", range: [1, 1000000] },
            rank_change: { type: "integer", range: [-1000000, 1000000] },
            previous_percentile: { type: "float", range: [0, 100] },
            new_percentile: { type: "float", range: [0, 100] },
            percentile_change: { type: "float", range: [-100, 100] }
          }
        },
        max_items: 1000000,
        semantics: "only include participants whose rank changed"
      },
      
      ranking_stability_metrics: {
        type: "object",
        fields: {
          percent_of_participants_with_rank_change: { type: "float", range: [0, 100] },
          average_rank_shift_magnitude: { type: "float", range: [0, 1000000] },
          max_rank_shift: { type: "integer" },
          min_rank_shift: { type: "integer" }
        }
      }
    }
  },
  
  conflict_resolution_log: {
    type: "array",
    required: true,
    items: {
      type: "object",
      fields: {
        
        participant_id: {
          type: "uuid",
          required: true
        },
        
        conflict_type: {
          type: "enum",
          required: true,
          allowed: [
            "score_divergence",
            "timestamp_inversion",
            "adjustment_duplication",
            "orphaned_score",
            "fairness_inconsistency"
          ]
        },
        
        conflict_severity: {
          type: "enum",
          required: true,
          allowed: ["low", "medium", "high", "critical"]
        },
        
        conflict_description: {
          type: "string",
          required: true,
          max_length: 500
        },
        
        gd_evidence: {
          type: "object",
          fields: {
            score: { type: "float" },
            timestamp: { type: "iso8601_datetime" },
            adjustment_amount: { type: "float" }
          }
        },
        
        dojo_evidence: {
          type: "object",
          fields: {
            score: { type: "float" },
            timestamp: { type: "iso8601_datetime" },
            adjustment_amount: { type: "float" }
          }
        },
        
        resolution_strategy_applied: {
          type: "enum",
          allowed: [
            "gd_priority",
            "dojo_priority",
            "newer_timestamp_priority",
            "conservative_merge",
            "deferred_to_human_review"
          ]
        },
        
        resolution_decision: {
          type: "object",
          fields: {
            canonical_score: { type: "float" },
            justification: { type: "string", max_length: 500 },
            confidence: { type: "float", range: [0, 1] }
          }
        },
        
        requires_human_review: {
          type: "boolean",
          semantics: "should governance team review this conflict?"
        }
      }
    },
    max_items: 100000
  },
  
  fairness_consistency_signal: {
    type: "object",
    required: true,
    semantics: "overall fairness health after sync",
    
    fields: {
      
      overall_consistency_score: {
        type: "float",
        required: true,
        range: [0, 1],
        semantics: "how consistent are fairness adjustments across sessions?"
      },
      
      fairness_anomalies_detected: {
        type: "array",
        required: true,
        items: {
          type: "object",
          fields: {
            participant_id: { type: "uuid" },
            anomaly_type: { type: "string" },
            severity: { type: "enum", allowed: ["low", "medium", "high"] },
            description: { type: "string" }
          }
        },
        max_items: 10000
      },
      
      score_divergence_analysis: {
        type: "object",
        fields: {
          
          avg_gd_dojo_score_difference: {
            type: "float",
            semantics: "average |GD - Dojo| across all participants"
          },
          
          max_individual_divergence: {
            type: "float",
            semantics: "largest |GD - Dojo| difference"
          },
          
          participants_with_high_divergence: {
            type: "integer",
            semantics: "count where |GD - Dojo| > threshold"
          }
        }
      },
      
      fairness_health_status: {
        type: "enum",
        allowed: ["healthy", "at_risk", "degraded", "critical"]
      }
    }
  },
  
  sync_state_checkpoint: {
    type: "object",
    required: true,
    semantics: "baseline state for next sync (immutable snapshot)",
    
    fields: {
      
      checkpoint_id: {
        type: "uuid",
        required: true,
        semantics: "unique ID for this checkpoint"
      },
      
      checkpoint_timestamp_utc: {
        type: "iso8601_datetime",
        required: true
      },
      
      data_hash: {
        type: "string",
        required: true,
        semantics: "SHA256 hash of all synced data"
      },
      
      participant_count_synced: {
        type: "integer",
        required: true
      },
      
      participant_scores_snapshot: {
        type: "array",
        required: true,
        items: {
          type: "object",
          fields: {
            participant_id: { type: "uuid" },
            gd_score: { type: "float" },
            dojo_score: { type: "float" },
            combined_score: { type: "float" },
            score_hash: { type: "string" }
          }
        },
        max_items: 1000000,
        semantics: "complete snapshot for next sync comparison"
      },
      
      ranking_snapshot: {
        type: "array",
        required: true,
        items: {
          type: "object",
          fields: {
            participant_id: { type: "uuid" },
            rank: { type: "integer" },
            percentile: { type: "float" }
          }
        },
        max_items: 1000000
      }
    }
  },
  
  data_divergence_detection: {
    type: "object",
    required: true,
    semantics: "mismatches found and resolved during sync",
    
    fields: {
      
      total_mismatches_detected: {
        type: "integer",
        required: true,
        range: [0, 1000000]
      },
      
      mismatches_by_type: {
        type: "object",
        fields: {
          score_value_mismatch: { type: "integer" },
          timestamp_inversion: { type: "integer" },
          adjustment_duplication: { type: "integer" },
          orphaned_record: { type: "integer" },
          hash_mismatch: { type: "integer" }
        }
      },
      
      mismatches_auto_resolved: {
        type: "integer",
        semantics: "count resolved without human intervention"
      },
      
      mismatches_requiring_review: {
        type: "integer",
        semantics: "count escalated to governance"
      },
      
      mismatch_details: {
        type: "array",
        required: true,
        items: {
          type: "object",
          fields: {
            mismatch_id: { type: "uuid" },
            participant_id: { type: "uuid" },
            mismatch_type: { type: "string" },
            expected_value: { type: "string" },
            actual_value: { type: "string" },
            resolution: { type: "string" }
          }
        },
        max_items: 100000
      }
    }
  },
  
  audit_reference: {
    type: "uuid",
    required: true,
    semantics: "immutable reference to sync audit log entry"
  },
  
  processing_metadata: {
    type: "object",
    required: true,
    fields: {
      
      received_timestamp_utc: { type: "iso8601_datetime" },
      processed_timestamp_utc: { type: "iso8601_datetime" },
      processing_duration_ms: { type: "integer", range: [100, 600000] },
      
      sync_direction_applied: {
        type: "enum",
        allowed: ["gd_to_dojo", "dojo_to_gd", "bidirectional"]
      },
      
      participants_affected: { type: "integer" },
      scores_synced: { type: "integer" },
      conflicts_detected: { type: "integer" },
      conflicts_resolved: { type: "integer" },
      
      model_version: {
        type: "string",
        format: "semantic versioning"
      },
      
      sync_computation_warnings: {
        type: "array",
        items: { type: "string" },
        max_items: 50
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
            "sync_completed_successfully",
            "ranking_update_required",
            "conflict_review_required",
            "compensation_payment_due",
            "audit_log_entry_created"
          ]
        },
        
        target_service: {
          type: "enum",
          allowed: [
            "ranking_service",
            "participant_profile_service",
            "admin_governance_service",
            "analytics_service",
            "compensation_engine"
          ]
        },
        
        priority: {
          type: "enum",
          allowed: ["critical", "high", "normal", "low"]
        },
        
        deadline_seconds: {
          type: "integer",
          range: [1, 3600]
        }
      }
    },
    max_items: 50
  }
}

OUTPUT_GUARANTEES (NON-NEGOTIABLE):

  ✓ Sync status must be deterministic (no ambiguity)
  ✓ All participant scores must be present in output
  ✓ All conflicts must be documented with evidence
  ✓ Rankings must be recalculated consistently
  ✓ Sync state checkpoint must be immutable baseline
  ✓ All divergences must be detected and logged
  ✓ Audit reference must be traceable
  ✓ Score changes must include history
  ✓ Fairness adjustments must be consistent across sessions
  ✓ No scores can be lost during sync
```

---

## 5️⃣ ML / AI LOGIC LAYER (SEALED - 100% DETERMINISTIC RULES - NO ML)

### 5.1 SYNCHRONIZATION ALGORITHM (DETERMINISTIC)

```
SYNC_FRAMEWORK: Deterministic State Machine + Conflict Resolution
EXECUTION_MODE: Pure rules-based (no ML, no probabilistic decisions)
PRINCIPLE: Eventual consistency with audit trail

CORE_SYNC_ALGORITHM:

```python
def perform_score_sync(gd_data, dojo_data, previous_state, policy):
    """
    Deterministic synchronization algorithm.
    No ML, no randomness, 100% reproducible.
    """
    
    # Step 1: VALIDATION (deterministic schema checks)
    validate_input_schema(gd_data)
    validate_input_schema(dojo_data)
    validate_sync_policy(policy)
    
    # Step 2: CONFLICT DETECTION
    conflicts = detect_conflicts(
        gd_data, 
        dojo_data, 
        previous_state
    )
    
    if conflicts and len(conflicts) > 0:
        # Step 3: CONFLICT RESOLUTION
        resolutions = resolve_conflicts(
            conflicts, 
            policy.conflict_resolution_strategy
        )
    else:
        resolutions = []
    
    # Step 4: MERGE (deterministic merge logic)
    synced_state = merge_scores(
        gd_data,
        dojo_data,
        resolutions,
        policy
    )
    
    # Step 5: FAIRNESS ADJUSTMENT (deterministic formula)
    adjusted_state = apply_fairness_adjustments(
        synced_state,
        policy.fairness_normalization_factors
    )
    
    # Step 6: RANKING RECALCULATION (deterministic sort)
    rankings = recalculate_rankings(
        adjusted_state
    )
    
    # Step 7: VALIDATION (checksum verification)
    validate_output_consistency(
        adjusted_state,
        rankings
    )
    
    # Step 8: AUDIT LOGGING (immutable record)
    audit_entry = create_audit_log(
        gd_data,
        dojo_data,
        conflicts,
        resolutions,
        adjusted_state
    )
    
    # Step 9: CHECKPOINT (immutable baseline)
    checkpoint = create_sync_checkpoint(
        adjusted_state,
        rankings,
        audit_entry
    )
    
    return {
        'sync_status': determine_status(conflicts, resolutions),
        'synced_scores': adjusted_state,
        'rankings': rankings,
        'conflicts_log': conflicts,
        'resolutions_log': resolutions,
        'audit_reference': audit_entry.id,
        'checkpoint': checkpoint
    }
```

CONFLICT DETECTION RULES (LOCKED):

```python
def detect_conflicts(gd_data, dojo_data, previous_state):
    """
    Detect all possible conflicts between GD and Dojo scores.
    """
    
    conflicts = []
    
    for participant_id in union(gd_data.keys(), dojo_data.keys()):
        
        # CONFLICT 1: Score Divergence
        gd_score = gd_data.get(participant_id, {}).get('final_score')
        dojo_score = dojo_data.get(participant_id, {}).get('final_score')
        
        if gd_score is not None and dojo_score is not None:
            divergence = abs(gd_score - dojo_score)
            if divergence > policy['max_score_divergence_tolerance']:
                conflicts.append({
                    'type': 'score_divergence',
                    'participant': participant_id,
                    'gd_score': gd_score,
                    'dojo_score': dojo_score,
                    'divergence': divergence,
                    'severity': classify_severity(divergence)
                })
        
        # CONFLICT 2: Timestamp Inversion
        gd_timestamp = gd_data.get(participant_id, {}).get('timestamp')
        dojo_timestamp = dojo_data.get(participant_id, {}).get('timestamp')
        
        if gd_timestamp and dojo_timestamp:
            if dojo_timestamp < gd_timestamp:
                # Dojo updated before GD, unusual order
                conflicts.append({
                    'type': 'timestamp_inversion',
                    'participant': participant_id,
                    'gd_timestamp': gd_timestamp,
                    'dojo_timestamp': dojo_timestamp,
                    'severity': 'medium'
                })
        
        # CONFLICT 3: Adjustment Duplication
        gd_adjustments = gd_data.get(participant_id, {}).get('fairness_adjustments', [])
        dojo_adjustments = dojo_data.get(participant_id, {}).get('fairness_adjustments', [])
        
        for adj in gd_adjustments:
            if adj in dojo_adjustments:
                # Same adjustment applied to both
                conflicts.append({
                    'type': 'adjustment_duplication',
                    'participant': participant_id,
                    'adjustment': adj,
                    'severity': 'high'
                })
        
        # CONFLICT 4: Orphaned Score
        if (participant_id in gd_data) and (participant_id not in previous_state.get('gd', {})):
            if (participant_id not in dojo_data):
                # GD score with no baseline and no Dojo to pair with
                conflicts.append({
                    'type': 'orphaned_score',
                    'participant': participant_id,
                    'session_type': 'gd',
                    'severity': 'low'
                })
        
        # CONFLICT 5: Fairness Inconsistency
        gd_adj_total = sum(gd_adjustments) if gd_adjustments else 0
        dojo_adj_total = sum(dojo_adjustments) if dojo_adjustments else 0
        
        if abs(gd_adj_total - dojo_adj_total) > 5.0:
            # Fairness adjustments significantly different
            conflicts.append({
                'type': 'fairness_inconsistency',
                'participant': participant_id,
                'gd_adjustment': gd_adj_total,
                'dojo_adjustment': dojo_adj_total,
                'severity': 'medium'
            })
    
    return conflicts
```

CONFLICT RESOLUTION RULES (LOCKED):

```python
def resolve_conflicts(conflicts, resolution_strategy):
    """
    Resolve detected conflicts using deterministic rules.
    """
    
    resolutions = []
    
    for conflict in conflicts:
        participant_id = conflict['participant']
        conflict_type = conflict['type']
        
        # RESOLUTION STRATEGY 1: GD Priority
        if resolution_strategy == 'gd_priority':
            if conflict_type == 'score_divergence':
                # Use GD score as canonical
                resolution = {
                    'participant': participant_id,
                    'conflict_type': conflict_type,
                    'canonical_value': conflict['gd_score'],
                    'rationale': 'GD priority: using GD score',
                    'confidence': 0.7
                }
            elif conflict_type == 'fairness_inconsistency':
                # Use GD fairness adjustment
                resolution = {
                    'participant': participant_id,
                    'conflict_type': conflict_type,
                    'canonical_adjustment': conflict['gd_adjustment'],
                    'rationale': 'GD priority: using GD adjustments',
                    'confidence': 0.6
                }
        
        # RESOLUTION STRATEGY 2: Newer Timestamp Priority
        elif resolution_strategy == 'newer_timestamp_priority':
            if conflict_type == 'timestamp_inversion':
                if conflict['dojo_timestamp'] > conflict['gd_timestamp']:
                    # Dojo is newer
                    canonical_source = 'dojo'
                else:
                    canonical_source = 'gd'
                
                resolution = {
                    'participant': participant_id,
                    'conflict_type': conflict_type,
                    'canonical_source': canonical_source,
                    'rationale': f'Using {canonical_source} (newer timestamp)',
                    'confidence': 0.9
                }
        
        # RESOLUTION STRATEGY 3: Conservative Merge
        elif resolution_strategy == 'conservative_merge':
            if conflict_type == 'score_divergence':
                # Use average of GD and Dojo
                canonical_value = (conflict['gd_score'] + conflict['dojo_score']) / 2
                resolution = {
                    'participant': participant_id,
                    'conflict_type': conflict_type,
                    'canonical_value': canonical_value,
                    'rationale': 'Conservative merge: using average',
                    'confidence': 0.5
                }
            elif conflict_type == 'fairness_inconsistency':
                # Take average adjustment
                canonical_adjustment = (conflict['gd_adjustment'] + conflict['dojo_adjustment']) / 2
                resolution = {
                    'participant': participant_id,
                    'conflict_type': conflict_type,
                    'canonical_adjustment': canonical_adjustment,
                    'rationale': 'Conservative merge: averaging adjustments',
                    'confidence': 0.5
                }
        
        # RESOLUTION STRATEGY 4: Escalate to Human Review
        elif resolution_strategy == 'human_review':
            resolution = {
                'participant': participant_id,
                'conflict_type': conflict_type,
                'resolution': 'DEFERRED_TO_HUMAN_REVIEW',
                'requires_escalation': True,
                'escalation_reason': f'{conflict_type} cannot be auto-resolved',
                'confidence': 0.0
            }
        
        resolutions.append(resolution)
    
    return resolutions
```

MERGE ALGORITHM (LOCKED):

```python
def merge_scores(gd_data, dojo_data, resolutions, policy):
    """
    Merge GD and Dojo scores deterministically.
    """
    
    merged_state = {}
    
    # Collect all participant IDs
    all_participants = set(gd_data.keys()) | set(dojo_data.keys())
    
    for participant_id in all_participants:
        
        gd_score = gd_data.get(participant_id, {}).get('final_score', None)
        dojo_score = dojo_data.get(participant_id, {}).get('final_score', None)
        
        # If both scores exist, merge them
        if gd_score is not None and dojo_score is not None:
            
            # Check if there's a resolution for this participant
            resolution = next(
                (r for r in resolutions if r['participant'] == participant_id),
                None
            )
            
            if resolution and resolution.get('canonical_value'):
                # Use resolved value
                canonical_score = resolution['canonical_value']
            else:
                # No conflict, or unresolved conflict
                # Apply fairness normalization and weighting
                gd_fairness = gd_data[participant_id].get('fairness_adjustments', {})
                dojo_fairness = dojo_data[participant_id].get('fairness_adjustments', {})
                
                gd_normalized = gd_score * policy['gd_to_dojo_conversion_factor']
                dojo_normalized = dojo_score * policy['dojo_to_gd_conversion_factor']
                
                canonical_score = (
                    gd_normalized * policy['combined_score_weight_gd'] +
                    dojo_normalized * policy['combined_score_weight_dojo']
                )
        
        # If only GD score exists
        elif gd_score is not None:
            canonical_score = gd_score
            dojo_score = None
        
        # If only Dojo score exists
        elif dojo_score is not None:
            dojo_score = dojo_score
            gd_score = None
        
        # Both are None (should not happen)
        else:
            continue
        
        # Store merged state
        merged_state[participant_id] = {
            'gd_score': gd_score,
            'dojo_score': dojo_score,
            'combined_score': canonical_score if (gd_score and dojo_score) else (gd_score or dojo_score),
            'gd_adjustments': gd_data.get(participant_id, {}).get('fairness_adjustments', 0),
            'dojo_adjustments': dojo_data.get(participant_id, {}).get('fairness_adjustments', 0)
        }
    
    return merged_state
```

FAIRNESS ADJUSTMENT LOGIC (LOCKED):

```python
def apply_fairness_adjustments(merged_state, normalization_factors):
    """
    Apply fairness adjustments consistently across sessions.
    """
    
    adjusted_state = {}
    
    for participant_id, scores in merged_state.items():
        
        # Calculate fairness adjustment factor
        fairness_factor = 1.0
        
        # If GD has interruption penalty, apply factor to Dojo
        gd_adj = scores.get('gd_adjustments', 0)
        if gd_adj < 0:  # Penalty
            fairness_factor *= (1 + (gd_adj / 100))  # Reduce Dojo by proportion
        
        # Apply fairness factor to Dojo score
        if scores['dojo_score']:
            dojo_adjusted = scores['dojo_score'] * fairness_factor
        else:
            dojo_adjusted = None
        
        # Recalculate combined score with adjustments
        if scores['gd_score'] and dojo_adjusted:
            combined = (
                scores['gd_score'] * normalization_factors['combined_score_weight_gd'] +
                dojo_adjusted * normalization_factors['combined_score_weight_dojo']
            )
        elif scores['gd_score']:
            combined = scores['gd_score']
        elif dojo_adjusted:
            combined = dojo_adjusted
        else:
            combined = None
        
        adjusted_state[participant_id] = {
            'gd_score': scores['gd_score'],
            'dojo_score': dojo_adjusted,
            'combined_score': combined,
            'cross_session_fairness_adjustments': gd_adj * normalization_factors['dojo_to_gd_conversion_factor']
        }
    
    return adjusted_state
```

RANKING RECALCULATION (LOCKED):

```python
def recalculate_rankings(state):
    """
    Deterministic ranking calculation.
    """
    
    # Sort by combined score (descending)
    ranked = sorted(
        state.items(),
        key=lambda x: x[1]['combined_score'] if x[1]['combined_score'] else -1,
        reverse=True
    )
    
    # Assign ranks with tie-breaking (stable sort)
    rankings = {}
    for rank, (participant_id, scores) in enumerate(ranked, 1):
        percentile = (1 - (rank - 1) / len(ranked)) * 100 if len(ranked) > 0 else 0
        
        rankings[participant_id] = {
            'rank': rank,
            'percentile': round(percentile, 1),
            'combined_score': scores['combined_score']
        }
    
    return rankings
```
```

### 5.2 OFFLINE RECONCILIATION (DETERMINISTIC)

```
OFFLINE_RECONCILIATION: Event Replay + State Reconstruction
PRINCIPLE: Idempotent event processing with deduplication

```python
def reconcile_offline_events(queued_events, last_sync_checkpoint):
    """
    Replay offline events to reconstruct state.
    """
    
    # Start from last known good checkpoint
    reconstructed_state = deepcopy(last_sync_checkpoint)
    
    # Replay events in order
    for event in sorted(queued_events, key=lambda e: e['timestamp']):
        
        # Check for duplicates
        if event['event_id'] in reconstructed_state.get('processed_event_ids', set()):
            continue  # Skip duplicate
        
        # Apply event
        participant_id = event['participant_id']
        
        if event['event_type'] == 'score_update':
            reconstructed_state['scores'][participant_id] = event['score']
        
        elif event['event_type'] == 'fairness_adjustment':
            current_adj = reconstructed_state['adjustments'].get(participant_id, 0)
            reconstructed_state['adjustments'][participant_id] = current_adj + event['adjustment_amount']
        
        # Track processed event
        reconstructed_state['processed_event_ids'].add(event['event_id'])
    
    return reconstructed_state
```
```

---

## 6️⃣ SCALABILITY DESIGN (LOCKED)

```
EXECUTION_PATTERN: Scheduled Batch + Event-Driven Triggers

BATCH PROCESSING (Scheduled):
  Frequency: Hourly (primary) + 4-hourly (full reconciliation)
  Trigger: Cron job at :00 each hour (UTC)
  Latency: < 30 minutes (95th percentile)
  Window: Last 1 hour's score updates
  Parallelization: By tenant (100s in parallel)

EVENT-DRIVEN TRIGGERS:
  GD Session Completion: Trigger sync immediately (< 10 seconds)
  Dojo Match Completion: Trigger sync immediately
  Offline Reconciliation: Trigger when device reconnects
  Manual Reconciliation: On-demand admin trigger

SCALABILITY TARGETS:

Performance:
  EXPECTED_THROUGHPUT = 100 sync operations per minute
    → 144K sync ops per day
    → 1.2M participant records synced per day
  
  BATCH_PROCESSING_SLA = Complete hourly sync < 30 minutes
    → 10K-50K participants per batch
    → Parallel processing across tenants
  
  EVENT_DRIVEN_LATENCY_TARGET = P99 < 10 seconds
    → Real-time sync on session completion
  
  CONCURRENT_SYNCS = 1,000 simultaneous sync operations
    → Per-tenant queuing (max 10 per tenant)

Infrastructure:

  Stateful Agents:
    - 10 container instances (batch processing)
    - 5 container instances (event-driven processing)
    - Auto-scaling: +2 instances per 500K daily participants
  
  Data Layer:
    - PostgreSQL: Score state + sync history (partitioned by tenant)
    - Redis: Sync lock management + deduplication cache
    - ClickHouse: Time-series audit logs
    - S3: Immutable sync checkpoints (7-year retention)

Caching Strategy (Redis):

  Cache 1: Sync Locks
    Key: lock:sync:{tenant_id}:{participant_id}
    Value: Lock token + TTL 5 minutes
    Purpose: Prevent concurrent syncs same participant
  
  Cache 2: Deduplication
    Key: dedup:event:{event_id}
    Value: Processing status
    TTL: 86400 seconds (24 hours)
    Purpose: Detect and skip duplicate events
  
  Cache 3: Checkpoint Validation
    Key: checkpoint:{checkpoint_id}
    Value: Serialized state + hash
    TTL: 604800 seconds (7 days)
    Purpose: Fast validation of sync points

Batch Aggregation Strategy:

  Hourly Batch (< 30 minutes):
    - Collect all scores from last hour
    - Detect conflicts
    - Perform sync
    - Recalculate rankings
    - Emit audit logs
    - Create immutable checkpoint
  
  4-Hourly Deep Reconciliation (< 60 minutes):
    - Full participant population reconciliation
    - Compare against previous checkpoint
    - Detect data loss/corruption
    - Validate fairness consistency
    - Generate reconciliation report

Idempotent Operations:

  ✓ All syncs keyed by (sync_request_id, timestamp)
  ✓ Duplicate sync requests produce identical output
  ✓ Ranking service can reprocess events safely
  ✓ Safe to retry failed syncs
  ✓ IDEMPOTENCY_KEY = hash(participant_id + session_type + session_id + timestamp)

Async Processing:

  ✓ Batch jobs enqueued asynchronously
  ✓ Results published to Kafka (events.sync_completed)
  ✓ Downstream consumers process at own pace
  ✓ No blocking waits (max 300-second lag tolerance)
```

---

## 7️⃣ SECURITY ENFORCEMENT (SEALED - ZERO-TRUST)

```
SECURITY_LAYERS (NON-NEGOTIABLE):

Layer 1: Tenant Isolation (Database + Application)
  ✓ All syncs scoped to authenticated tenant
  ✓ Row-level security enforced on all queries
  ✓ No cross-tenant score mixing
  ✓ Violation → CRITICAL alert + immediate investigation

Layer 2: Score Integrity (Hash Verification)
  ✓ All scores signed with HMAC-SHA256
  ✓ Hash verification before/after sync
  ✓ Detect tampering: hash mismatch → reject
  ✓ Audit all hash verification failures

Layer 3: Access Control (Role-Based)
  ✓ Sync engine: system-level (automated)
  ✓ Ranking service: read-only access to synced scores
  ✓ Admin governance: read-only + escalation authority
  ✓ Participants: cannot access raw sync logs

Layer 4: Timestamp Integrity (Monotonic Validation)
  ✓ Verify all timestamps monotonic within session
  ✓ Detect time travel: future timestamp → reject
  ✓ Cross-session temporal consistency
  ✓ Server timestamp is source of truth

Layer 5: Encryption & Transport
  ✓ TLS 1.3 for all network communication
  ✓ At-rest encryption: PostgreSQL column encryption
  ✓ Checkpoint encryption: S3 Server-Side Encryption (AES-256)
  ✓ Redis: AUTH token + encrypted persistence

Layer 6: Audit Logging (Immutable)
  ✓ Every sync logged with: who, what, when, result
  ✓ Logs stored in append-only table
  ✓ Cannot be deleted or modified
  ✓ Backup: Daily immutable snapshot to S3 (7+ years)

Layer 7: Rate Limiting & DDoS Protection
  ✓ Max 100 syncs per tenant per hour
  ✓ Max 10K sync requests per IP per hour
  ✓ Max 1 sync per participant per minute
  ✓ Violation: return 429, quarantine IP

Layer 8: Conflict Detection & Resolution
  ✓ All conflicts detected and logged
  ✓ No silent data loss (conflicts escalated)
  ✓ Resolution deterministic (not arbitrary)
  ✓ Human review for unresolvable conflicts
```

---

## 8️⃣ AUDIT & TRACEABILITY (LOCKED - IMMUTABLE)

```
MANDATORY AUDIT LOG ENTRY:

Every sync generates immutable audit entry:

```sql
INSERT INTO sync_audit_logs (
  audit_id,                          -- UUID v4
  timestamp_utc,                     -- ISO8601
  service_name,                      -- 'offline_gd_to_dojo_score_sync_agent'
  actor_id,                          -- UUID of triggering service
  sync_request_id,                   -- Correlation ID
  tenant_id,                         -- Tenant context
  
  sync_trigger_type,                 -- Type of sync
  sync_direction,                    -- Direction (GD→Dojo, etc)
  
  input_snapshot: {
    input_hash,                      -- SHA256
    gd_participants_count,
    dojo_participants_count,
    offline_events_replayed
  },
  
  output_snapshot: {
    output_hash,                     -- SHA256
    synced_participants_count,
    conflicts_detected_count,
    conflicts_resolved_count
  },
  
  sync_result: {
    sync_status,                     -- SUCCESS / PARTIAL / CONFLICT / FAILED
    scores_changed_count,
    rankings_changed_count,
    fairness_adjustments_applied
  },
  
  conflicts_log: [
    {
      participant_id,
      conflict_type,
      gd_evidence,
      dojo_evidence,
      resolution_applied
    }
  ],
  
  divergence_analysis: {
    avg_score_divergence,
    max_divergence,
    fairness_consistency_score
  },
  
  checkpoint_reference,              -- UUID of saved checkpoint
  
  processing_time_ms,
  data_integrity_checks: {
    hash_verification_passed,
    timestamp_ordering_valid,
    no_data_loss_detected
  },
  
  status: 'completed' | 'partial' | 'conflict_detected' | 'failed'
)
```

Immutability:
  ✓ Primary key: (audit_id, timestamp_utc)
  ✓ No UPDATE allowed (schema constraint)
  ✓ Only INSERT/SELECT (immutable append-only)
  ✓ Retention: Permanent
  ✓ Backup: Daily immutable snapshot to S3 Object Lock
```

---

## 9️⃣ FAILURE POLICY (SEALED - DATA INTEGRITY FIRST)

```
FAILURE SCENARIOS & HANDLING (LOCKED):

Scenario 1: Conflict Unresolvable
  Trigger: Conflict cannot be resolved by policy rules
  Action:
    ✓ HALT sync (don't proceed)
    ✓ LOG_CRITICAL: "unresolvable_conflict"
    ✓ Create detailed investigation brief
    ✓ ESCALATE to: admin_governance_service
    ✓ No data modified (conservative approach)
    ✓ Return CONFLICT_DETECTED status

Scenario 2: Score Divergence > Threshold
  Trigger: |GD - Dojo| > max tolerance
  Action:
    ✓ Log divergence with severity
    ✓ If divergence < critical threshold:
      - Attempt auto-resolution using merge strategy
      - Apply fairness normalization
      - Continue sync
    ✓ If divergence > critical threshold:
      - HALT sync
      - ESCALATE to governance
      - Require manual review

Scenario 3: Hash Verification Failure
  Trigger: Score hash doesn't match after computation
  Action:
    ✓ HALT immediately
    ✓ LOG_CRITICAL: "data_integrity_failure"
    ✓ ESCALATE to: security_team + governance
    ✓ Incident severity: P1 (possible tampering)
    ✓ Quarantine affected data
    ✓ No changes applied (rollback)

Scenario 4: Database Connection Failure
  Trigger: PostgreSQL connection timeout
  Action:
    ✓ Retry with exponential backoff (3 retries, max 30 seconds)
    ✓ If all retries fail:
      - Return FAILED status
      - ESCALATE to: ops_team
      - Queue sync for retry (max 5 retries)
    ✓ No partial updates (transactional safety)

Scenario 5: Offline Event Replay Failure
  Trigger: Cannot reconstruct state from offline events
  Action:
    ✓ Log reconstruction failure
    ✓ Fall back to last checkpoint
    ✓ Flag as DEGRADED (using last-known good state)
    ✓ Queue for manual reconciliation
    ✓ ESCALATE if > 10 failed replays

Scenario 6: Conflict Detection False Positive
  Trigger: Flagged as conflict but actually valid
  Action:
    ✓ Require human review for unflagging
    ✓ Cannot auto-disable conflict detection
    ✓ Escalate to governance for validation
    ✓ Document as false positive for improvement

Scenario 7: Concurrent Sync Attempts (Same Participant)
  Trigger: Multiple sync requests for same participant simultaneously
  Action:
    ✓ Acquire distributed lock (Redis)
    ✓ Queue second request (FIFO order)
    ✓ Process sequentially (no race conditions)
    ✓ Lock timeout: 5 minutes (release stale locks)

Scenario 8: Ranking Recalculation Error
  Trigger: Ranking calculation produces invalid percentiles
  Action:
    ✓ Validate ranking math (sum of percentiles = 100 * count)
    ✓ If invalid: log error, skip ranking update
    ✓ Use previous ranking as fallback
    ✓ ESCALATE to data team
    ✓ Do NOT emit invalid rankings

Scenario 9: Audit Log Insertion Failure
  Trigger: Cannot write to audit log
  Action:
    ✓ HALT sync completion (audit is non-negotiable)
    ✓ Return status: FAILED
    ✓ ESCALATE to: ops_team
    ✓ Trigger incident (audit failure = P2 at minimum)

Scenario 10: Out-of-Memory During Large Sync
  Trigger: Sync operation exceeds heap during batch processing
  Action:
    ✓ Subdivide cohort into smaller batches
    ✓ Process serially (slower but memory-safe)
    ✓ Log resource constraint
    ✓ Monitor for recurrence (possible scaling issue)

NO SILENT FAILURES (MANDATORY):

  ✗ Never silently skip conflict
  ✗ Never silently drop score update
  ✗ Never silently ignore divergence
  ✗ Never silently apply unreviewed escalation
  ✗ Always log with full context
  ✗ Always escalate when appropriate
  ✗ Always preserve data integrity
```

---

## 🔟 INTER-AGENT DEPENDENCY MAP (SEALED)

```
UPSTREAM AGENTS (Providers):

  1. Voice GD Orchestrator (COMPLETION EVENTS)
     Output: GD session completion with final scores
     Frequency: Per GD session completion
     Contains: Participant scores, fairness adjustments
     Protocol: Kafka topic events.gd_session_completed
  
  2. Dojo Match Engine (COMPLETION EVENTS)
     Output: Match completion with final scores
     Frequency: Per match completion
     Contains: Skill assessments, match results
     Protocol: Kafka topic events.dojo_match_completed
  
  3. Scoring Engine (ADJUSTMENT REFERENCE)
     Output: Applied fairness adjustments
     Frequency: Per scoring decision
     Contains: Adjustment rationale, amounts
  
  4. Participant Ranking Service (BASELINE)
     Output: Current ranking snapshots
     Frequency: Per ranking update
     Contains: Percentiles, scores

DOWNSTREAM AGENTS (Consumers):

  1. Ranking Service (PRIMARY CONSUMER)
     Input: UPDATED_RANKINGS
     Action: Updates public rankings
     SLA: Must consume within 300 seconds
     Contract: Deterministic rankings
  
  2. Participant Profile Service (SCORES)
     Input: SYNCED_PARTICIPANT_SCORES
     Action: Updates displayed scores
     SLA: P99 < 5 minutes propagation
     Contract: Canonical synced scores
  
  3. Admin Governance Service (ESCALATION)
     Input: ESCALATION_SIGNAL + conflict details
     Action: Manual conflict resolution
     SLA: Must consume within 3600 seconds
     Contract: Complete conflict evidence
  
  4. Analytics Service (AUDIT)
     Input: SYNC_STATE_CHECKPOINT
     Action: Maintains consistency audit
     SLA: Eventually consistent
     Contract: Complete state history
  
  5. Platform Health Dashboard (MONITORING)
     Input: FAIRNESS_CONSISTENCY_SIGNAL
     Action: Real-time health metric
     SLA: < 5 minutes latency
     Contract: Boolean consistency status

EVENT FLOW DIAGRAM (LOCKED):

```
GD Session Completion          Dojo Match Completion
         │                              │
         ▼                              ▼
  Kafka Topic                    Kafka Topic
  events.gd_completed            events.dojo_completed
         │                              │
         └──────────────┬───────────────┘
                        │
                        ▼
        OFFLINE_GD_TO_DOJO_SCORE_SYNC_AGENT
        ┌──────────────────────────────────┐
        │ 1. Conflict Detection            │
        │ 2. Conflict Resolution           │
        │ 3. Fairness Normalization        │
        │ 4. Ranking Recalculation         │
        │ 5. Audit Log Creation            │
        │ 6. Checkpoint Snapshot           │
        └──────────────────────────────────┘
                        │
        ┌───────────────┼───────────────┐
        │               │               │
        ▼               ▼               ▼
   Ranking Service  Participant      Analytics
   (update ranks)   Profile Service  Service
                    (update scores)  (audit logs)
```
```

---

## 1️⃣1️⃣ PASSIVE INTELLIGENCE COMPATIBILITY (OPTIONAL)

```
FEATURE VECTOR EMISSION (OPTIONAL):

If passive intelligence integrated, Sync Agent MAY emit
consistency signals for platform health ML.

FEATURE_VECTOR:

{
  timestamp_utc: ISO8601,
  feature_name: "cross_session_score_consistency",
  feature_value: float (0-1),
  source_agent: "offline_gd_to_dojo_score_sync_agent",
  
  context: {
    sync_conflicts_detected: integer,
    sync_conflicts_resolved: integer,
    avg_score_divergence: float,
    fairness_consistency_score: float
  }
}

USAGE: ML models can predict platform stability
```

---

## 1️⃣2️⃣ INNOVATION ECONOMY COMPATIBILITY (NOT APPLICABLE)

```
Sync agent does not touch innovation economy.
Score sync is independent from idea tracking.
```

---

## 1️⃣3️⃣ GROWTH ENGINE HOOK (NOT APPLICABLE)

```
Sync agent does not affect achievement/ranking directly.
Only ensures fairness consistency across session types.
```

---

## 1️⃣4️⃣ PERFORMANCE MONITORING (LOCKED - PROMETHEUS)

```
MANDATORY METRICS:

1. Sync Metrics:
   phone_sync_operations_total
     label: sync_status (success, partial, conflict, failed)
     label: trigger_type (scheduled, event_driven, manual)
   
   phone_sync_duration_seconds
     quantiles: [0.50, 0.90, 0.99]
     histogram by sync scope (single, cohort, platform)

2. Conflict Metrics:
   phone_sync_conflicts_detected_total
     label: conflict_type
   
   phone_sync_conflicts_resolved_total
     label: resolution_strategy
   
   phone_sync_conflicts_escalated_total
     (requiring human review)

3. Data Quality:
   phone_sync_score_divergence
     histogram (|GD - Dojo| distribution)
   
   phone_sync_fairness_consistency_score
     gauge (0-1)
   
   phone_sync_ranking_changes_percent
     gauge (% of participants with rank change)

4. Dependency Metrics:
   phone_sync_kafka_consumer_lag
     gauge (event processing lag)
   
   phone_sync_database_latency
     histogram (query response times)
   
   phone_sync_lock_wait_duration
     histogram (distributed lock contention)

Alerting Rules:

  Alert 1: Sync Latency SLA (P2)
    Condition: p99(sync_duration) > 300 seconds
  
  Alert 2: High Conflict Rate (P2)
    Condition: conflict_rate > 5%
  
  Alert 3: Data Divergence Spike (P2)
    Condition: avg_divergence > 15 points
  
  Alert 4: Fairness Inconsistency (P1)
    Condition: fairness_score < 0.70
  
  Alert 5: Lock Contention (P3)
    Condition: p99(lock_wait) > 10 seconds

Grafana Dashboards:

  Dashboard 1: Sync Health
    - Throughput (ops/minute)
    - Latency P50/90/99
    - Success/partial/conflict/failed rates
  
  Dashboard 2: Conflict Analysis
    - Conflict detection rate
    - Resolution success rate
    - Escalation count
  
  Dashboard 3: Data Quality
    - Score divergence distribution
    - Fairness consistency trend
    - Ranking stability
```

---

## 1️⃣5️⃣ VERSIONING POLICY (SEALED)

```
VERSIONING CONSTRAINT:

All Changes: Add-Only, Never Destructive

Semantic Versioning:
  Format: major.minor.patch
  Example: 1.2.0
  
  MAJOR: Breaking changes to sync algorithm
         All syncs must be re-validated
         Backward compatibility NOT guaranteed
  
  MINOR: New conflict types, new resolution strategies
         Fully backward compatible
         Existing syncs unaffected
  
  PATCH: Bug fixes, performance improvements
         Zero breaking changes
         Automatic deployment

Versioning Strategy:

  Algorithm Versioning (Separate):
    sync_algorithm_v1.2
    conflict_resolution_v1.0
    fairness_normalization_v2.1
  
  Checkpoint Versioning (Separate):
    Each checkpoint includes version tag
    Allows point-in-time reconstruction
  
  Agent Versioning (Overall):
    offline_gd_to_dojo_score_sync_agent_v1.2.0

Rollback Capability (REQUIRED):

  Keep last 4 versions active:
    v1.2.0 (current)
    v1.1.9
    v1.1.8
    v1.1.7
```

---

## 1️⃣6️⃣ NON-NEGOTIABLE RULES (SEALED & LOCKED)

```
FORBIDDEN ACTIONS:

This agent MUST NOT:

  ✗ Silently drop a score during sync
  ✗ Modify a score without audit trail
  ✗ Ignore a detected conflict
  ✗ Apply unreviewed conflict resolution
  ✗ Bypass fairness adjustment logic
  ✗ Auto-overwrite newer data with older
  ✗ Sync across tenants (isolation)
  ✗ Operate without atomic transactions
  ✗ Process requests without authentication
  ✗ Skip checkpoint creation
  ✗ Disable audit logging
  ✗ Return success when sync partial
  ✗ Allow concurrent same-participant syncs
  ✗ Re-apply duplicate adjustments
  ✗ Modify historical sync records
  ✗ Perform sync without validation
  ✗ Silently fail (always explicit status)

MANDATORY ACTIONS:

This agent MUST:

  ✓ Detect all conflicts deterministically
  ✓ Resolve conflicts per policy rules
  ✓ Apply fairness adjustments consistently
  ✓ Recalculate rankings after each sync
  ✓ Create immutable audit trails
  ✓ Generate checkpoints (baseline for next sync)
  ✓ Respect tenant isolation (never mix)
  ✓ Validate all inputs against schema
  ✓ Verify data integrity (hashes)
  ✓ Maintain temporal consistency
  ✓ Support offline reconciliation
  ✓ Scale horizontally (stateless-friendly)
  ✓ Monitor with observable metrics
  ✓ Encrypt data in transit + at rest
  ✓ Support rollback (4-version history)
  ✓ Document all changes (add-only)
  ✓ Support dispute resolution (audit access)
  ✓ Degrade gracefully (not crash)
  ✓ Escalate unresolvable conflicts
  ✓ Maintain fairness consistency
```

---

## CHANGELOG & VERSION HISTORY

```
v1.0.0 - 2025-03-04 (SEALED RELEASE)
  ✓ Initial sealed specification for Antigravity platform
  ✓ Deterministic sync algorithm (100% rules-based)
  ✓ Conflict detection + resolution
  ✓ Offline reconciliation support
  ✓ Fairness adjustment normalization
  ✓ Ranking recalculation
  ✓ 100% immutable audit trail
  ✓ Multi-tenant zero-trust architecture
  ✓ Event-driven + batch execution
  ✓ Compliance-ready documentation
```

---

## SEAL CERTIFICATION

🔒 **THIS SPECIFICATION IS SEALED**

This document defines a production-grade score synchronization agent for Ecoskiller Antigravity.
All sync logic is deterministic, auditable, and compliant with enterprise SaaS + data integrity requirements.

**Sealed By:** Architecture Review Board
**Date Sealed:** 2025-03-04
**Review Cycle:** Quarterly + post-conflict-detection review
**Any modifications require formal change control process with full documentation.**

---

**END OF SEALED SPECIFICATION**
