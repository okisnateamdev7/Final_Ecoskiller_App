# 🔒 PHONE_AI_EXPLAINABILITY_AGENT
## Sealed & Locked Enterprise Agent Specification
### Ecoskiller Antigravity Platform

---

## 1️⃣ AGENT IDENTITY (MANDATORY - SEALED)

```
AGENT_NAME = PHONE_AI_EXPLAINABILITY_AGENT
SYSTEM_ROLE = Algorithmic Decision Explainer & Transparency Provider
PRIMARY_DOMAIN = Human-readable explanations for AI/ML decisions
EXECUTION_MODE = Deterministic + Semantic (rule-based + AI-assisted narratives)
DATA_SCOPE = Decision context, evidence, reasoning, scoring factors
TENANT_SCOPE = Strict Multi-Tenant Isolation (participant self-service)
FAILURE_POLICY = ALWAYS provide explanation (even if incomplete)
IMMUTABILITY_LEVEL = SEALED - No runtime modification, versioned only
VERSIONING_CONSTRAINT = Add-only, backward compatible, audit immutable
PROCESS_STAGE = Decision Analysis → Evidence Compilation → Narrative Generation → Quality Check
CRITICALITY = CRITICAL (GDPR compliance, user trust, fairness)
EXECUTION_PATTERN = Real-time on-demand + Batch (historical explanations)
COMPLIANCE_SCOPE = GDPR right to explanation, transparency, user empowerment
```

**SEALED CONSTRAINT:**
This agent definition is locked in immutable state. No modifications allowed without:
1. Formal Architecture Review Board + Legal approval
2. GDPR compliance validation (right to explanation met)
3. User study showing explanation quality
4. Bias/fairness audit of explanations (no manipulation)

---

## 2️⃣ PURPOSE DECLARATION (SEALED)

### Problem This Agent Solves

**GDPR Compliance (Mandatory):**
- Right to explanation: Participants have legal right to understand automated decisions
- Right to challenge: Meaningful explanations enable valid appeals
- Non-discrimination: Transparent reasoning prevents hidden bias
- Data portability: Explanations part of data subject's records

**User Trust & Empowerment:**
- Understand why score is what it is (transparency)
- See what factors helped/hurt performance (actionable insights)
- Identify if decisions seem unfair (fairness challenge)
- Learn for next time (growth support)

**Fairness & Accountability:**
- Decisions not "black box" (explainability vs opaqueness)
- Participants can validate reasoning (crowdsourced fairness)
- Auditors can verify explanations match evidence
- Legal defensibility (can explain to regulators)

**Appeals Support:**
- Clear explanation for why score was given
- Specific evidence for/against fairness adjustments
- Grounds for appeal (participants understand claim basis)
- Consistency check (similar cases explained same way)

**System Improvement:**
- Track which decisions are questioned most (improvement signals)
- Identify confusing explanation patterns (clarity gaps)
- Detect if participants misunderstand mechanics (education needs)

### Input Consumed

**Primary Input Source:**
- Decision event (scoring, interruption detection, adjustment, etc)
- Full decision context (what was considered)
- Evidence used (what was relevant)
- Factors that influenced outcome (weighting)
- Alternative outcomes (what would change score)

**Secondary Input Sources:**
- Participant profile (history, cohort, experience level)
- Session metadata (type, phase, difficulty)
- Fairness signals (was participant disadvantaged?)
- System configuration (thresholds, rules applied)

**Tertiary Input Sources:**
- Previous explanations (for consistency)
- Participant feedback (did they understand?)
- Dispute context (why are they appealing?)

### Output Produced

**Primary Output:**
- `PLAIN_ENGLISH_EXPLANATION` (what happened, why, in user language)
- `EVIDENCE_SUMMARY` (facts used in decision, with confidence)
- `DECISION_FACTORS` (what helped vs hurt, quantified)
- `ALTERNATIVE_SCENARIOS` (what would change the outcome)

**Secondary Output:**
- `TRANSPARENCY_ASSESSMENT` (how transparent is decision?)
- `FAIRNESS_CONTEXT` (was participant treated fairly?)
- `NEXT_STEPS_GUIDANCE` (how to improve score next time)
- `APPEAL_SUPPORT_BRIEF` (if participant disputes decision)

**Tertiary Output:**
- `EXPLANATION_QUALITY_METRICS` (how well did we explain?)
- `AUDIT_LOG_ENTRY` (immutable explanation record)
- `PARTICIPANT_FEEDBACK_REQUEST` (did explanation help?)
- `NEXT_TRIGGER_EVENT` (further action needed?)

### Upstream Agent Dependency Chain

1. **Scoring Engine** (decisions to explain)
   - Output: Score + adjustments applied + rationale
   - Frequency: Per session
   - Contains: Base score, adjustments, reasoning

2. **Phone Interruption Detection Agent** (evidence)
   - Output: Interruption findings + confidence scores
   - Frequency: Referenced if interruptions affected score
   - Contains: Evidence timeline, confidence levels

3. **Phone Behavior Analytics Agent** (fairness context)
   - Output: Fairness signals + impact assessment
   - Frequency: Referenced for fairness explanation
   - Contains: Disadvantage assessment, patterns

4. **Score Bias Audit Agent** (systemic context)
   - Output: Bias findings (for fairness explanation)
   - Frequency: Referenced if cohort bias detected
   - Contains: Statistical evidence of bias

### Downstream Agent Consumption

1. **Participant Self-Service Portal** (primary consumer)
   - Input: PLAIN_ENGLISH_EXPLANATION + EVIDENCE_SUMMARY
   - Action: Display to participant
   - SLA: < 5 seconds response time
   - Contract: Clear, understandable explanation

2. **Appeals Resolution Team** (dispute context)
   - Input: APPEAL_SUPPORT_BRIEF (if participant appeals)
   - Action: Use as basis for dispute investigation
   - SLA: < 30 minutes response
   - Contract: Complete evidence compilation

3. **GDPR Data Subject Requests** (compliance)
   - Input: PLAIN_ENGLISH_EXPLANATION
   - Action: Include in data export
   - SLA: < 30 days (GDPR requirement)
   - Contract: Legally compliant format

4. **Analytics Service** (quality monitoring)
   - Input: EXPLANATION_QUALITY_METRICS
   - Action: Monitor explanation quality
   - SLA: Daily reporting
   - Contract: Metrics on clarity, completeness

---

## 3️⃣ INPUT CONTRACT (STRICT - SEALED)

```
INPUT_SCHEMA: {
  
  explanation_request_id: {
    type: "uuid",
    required: true,
    validation: "must be unique within 24-hour window"
  },
  
  explanation_scope: {
    type: "enum",
    required: true,
    allowed_values: [
      "session_score_explanation",
      "interruption_detection_explanation",
      "fairness_adjustment_explanation",
      "bias_finding_explanation",
      "dispute_context_explanation",
      "overall_performance_explanation"
    ]
  },
  
  participant_context: {
    type: "object",
    required: true,
    fields: {
      
      participant_id: {
        type: "uuid",
        required: true,
        validation: "must match authenticated user"
      },
      
      participant_education_level: {
        type: "enum",
        required: true,
        allowed_values: ["high_school", "some_college", "bachelors", "masters", "phd"],
        semantics: "for explanation complexity calibration"
      },
      
      explanation_language: {
        type: "enum",
        required: true,
        allowed_values: ["english", "spanish", "french", "german", "japanese", "chinese"],
        semantics: "language preference for explanation"
      },
      
      explanation_audience: {
        type: "enum",
        required: true,
        allowed_values: ["participant", "appeals_team", "gdpr_request", "recruiter_training"],
        semantics: "who will read this explanation?"
      }
    }
  },
  
  decision_context: {
    type: "object",
    required: true,
    fields: {
      
      decision_type: {
        type: "enum",
        required: true,
        allowed_values: [
          "scoring_decision",
          "interruption_detection",
          "fairness_adjustment",
          "bias_finding",
          "ranking_placement"
        ]
      },
      
      session_id: {
        type: "uuid",
        required: true
      },
      
      session_type: {
        type: "enum",
        required: true,
        allowed_values: ["voice_gd", "interview", "dojo_match"]
      },
      
      original_decision: {
        type: "object",
        required: true,
        fields: {
          base_score: { type: "float", range: [0, 100] },
          fairness_adjustments: { type: "float", range: [-10, 10] },
          final_score: { type: "float", range: [0, 100] },
          decision_timestamp: { type: "iso8601_datetime" }
        }
      },
      
      evidence_inputs: {
        type: "array",
        required: true,
        items: {
          type: "object",
          fields: {
            evidence_type: { type: "string" },
            evidence_value: { type: "string" },
            evidence_weight: { type: "float", range: [0, 1] },
            evidence_confidence: { type: "float", range: [0, 1] }
          }
        },
        max_items: 50,
        semantics: "all factors that influenced decision"
      },
      
      decision_factors: {
        type: "array",
        required: true,
        items: {
          type: "object",
          fields: {
            factor_name: { type: "string" },
            factor_value: { type: "string" },
            impact_on_score: { type: "float", range: [-100, 100] },
            impact_direction: { type: "enum", allowed: ["positive", "negative", "neutral"] },
            factor_confidence: { type: "float", range: [0, 1] }
          }
        },
        max_items: 20,
        semantics: "quantified impact of each factor"
      }
    }
  },
  
  fairness_context: {
    type: "object",
    required: false,
    fields: {
      
      was_adjustment_applied: { type: "boolean" },
      adjustment_amount: { type: "float", range: [-10, 10] },
      adjustment_reason: { type: "string" },
      
      participant_disadvantage_signals: {
        type: "array",
        items: { type: "string" },
        examples: [
          "phone_interruptions_detected",
          "network_issues_during_session",
          "cohort_bias_detected",
          "recruiter_bias_detected"
        ]
      },
      
      fairness_assessment: {
        type: "string",
        semantics: "was participant treated fairly?"
      }
    }
  },
  
  explanation_configuration: {
    type: "object",
    required: true,
    fields: {
      
      verbosity_level: {
        type: "enum",
        required: true,
        allowed_values: ["brief", "moderate", "detailed"],
        semantics: "how detailed should explanation be?"
      },
      
      include_statistical_details: {
        type: "boolean",
        default: false,
        semantics: "include numbers, percentiles, z-scores?"
      },
      
      include_alternative_scenarios: {
        type: "boolean",
        default: true,
        semantics: "what would change the score?"
      },
      
      include_fairness_context: {
        type: "boolean",
        default: true,
        semantics: "was decision fair to participant?"
      },
      
      accessibility_level: {
        type: "enum",
        allowed: ["standard", "dyslexia_friendly", "screen_reader_optimized"],
        semantics: "for accessibility needs"
      }
    }
  },
  
  gdpr_context: {
    type: "object",
    required: false,
    fields: {
      
      is_gdpr_subject_access_request: {
        type: "boolean",
        semantics: "is this for GDPR data export?"
      },
      
      is_right_to_explanation_request: {
        type: "boolean",
        semantics: "participant exercising GDPR right to explanation?"
      },
      
      dispute_context: {
        type: "boolean",
        semantics: "is this for dispute/appeal?"
      }
    }
  }
}

VALIDATION_RULES (MANDATORY - LOCKED):

  ✓ Participant must be authenticated (own score only)
  ✓ Decision must exist in database
  ✓ Session must be completed (explanation only for finished sessions)
  ✓ Evidence inputs must be present (cannot explain without evidence)
  ✓ Decision factors must sum to final score (math verified)
  ✓ Confidence scores must be valid (0-1)
  ✓ Explanation language must be supported
  ✓ No cross-tenant explanations (isolation)
  ✓ GDPR requests must be verified (identity confirmation)

SECURITY_CHECKS (MANDATORY):

  ✓ Verify participant ownership (cannot access other's explanations)
  ✓ Verify decision integrity (no tampering)
  ✓ Encrypt explanation delivery (HTTPS)
  ✓ Audit access to sensitive explanations
  ✓ Verify GDPR data export format compliance
  ✓ Check for disclosure of confidential algorithms
  ✓ Validate no PII leakage in explanations
```

---

## 4️⃣ OUTPUT CONTRACT (STRICT - SEALED)

```
OUTPUT_SCHEMA: {
  
  plain_english_explanation: {
    type: "object",
    required: true,
    fields: {
      
      executive_summary: {
        type: "string",
        required: true,
        max_length: 300,
        semantics: "one-paragraph overview (what happened in simple terms)"
      },
      
      score_explanation: {
        type: "object",
        required: true,
        fields: {
          
          what_happened: {
            type: "string",
            required: true,
            max_length: 500,
            semantics: "plain-English summary of score determination"
          },
          
          why_this_score: {
            type: "string",
            required: true,
            max_length: 500,
            semantics: "reasoning in layperson terms"
          },
          
          starting_point: {
            type: "string",
            required: true,
            semantics: "base score explanation"
          },
          
          adjustments_made: {
            type: "array",
            required: true,
            items: {
              type: "object",
              fields: {
                adjustment_description: { type: "string" },
                adjustment_amount: { type: "string", examples: ["+2 points", "-3 points"] },
                adjustment_reason: { type: "string", max_length: 300 }
              }
            },
            max_items: 10,
            semantics: "each fairness adjustment explained"
          },
          
          final_score_explanation: {
            type: "string",
            max_length: 200,
            semantics: "how adjustments led to final score"
          }
        }
      },
      
      interruption_explanation: {
        type: "object",
        required: false,
        semantics: "if phone interruptions affected score",
        fields: {
          
          interruptions_detected: {
            type: "string",
            semantics: "yes/no and how many"
          },
          
          interruption_impact: {
            type: "string",
            max_length: 300,
            semantics: "how did interruptions affect scoring?"
          },
          
          fairness_adjustment_applied: {
            type: "string",
            max_length: 300,
            semantics: "was participant credited for disadvantage?"
          }
        }
      },
      
      fairness_context: {
        type: "object",
        required: true,
        fields: {
          
          fair_treatment_assessment: {
            type: "string",
            max_length: 300,
            semantics: "was participant treated fairly?"
          },
          
          advantages_identified: {
            type: "array",
            items: { type: "string", max_length: 200 },
            semantics: "things that helped participant"
          },
          
          disadvantages_identified: {
            type: "array",
            items: { type: "string", max_length: 200 },
            semantics: "things that hurt participant"
          },
          
          net_fairness_assessment: {
            type: "enum",
            allowed: ["treated_favorably", "treated_fairly", "treated_unfavorably"],
            semantics: "overall fairness judgment"
          }
        }
      },
      
      what_helped_score: {
        type: "array",
        required: true,
        items: { type: "string", max_length: 200 },
        max_items: 10,
        semantics: "positive factors in participant's own words"
      },
      
      what_hurt_score: {
        type: "array",
        required: true,
        items: { type: "string", max_length: 200 },
        max_items: 10,
        semantics: "negative factors in participant's own words"
      }
    }
  },
  
  evidence_summary: {
    type: "object",
    required: true,
    fields: {
      
      evidence_used: {
        type: "array",
        required: true,
        items: {
          type: "object",
          fields: {
            evidence_item: {
              type: "string",
              semantics: "what was considered"
            },
            
            evidence_description: {
              type: "string",
              max_length: 300,
              semantics: "what it was and why it mattered"
            },
            
            evidence_value: {
              type: "string",
              semantics: "specific observation or measurement"
            },
            
            how_it_affected_score: {
              type: "string",
              max_length: 200,
              semantics: "whether it helped or hurt"
            },
            
            confidence_in_evidence: {
              type: "enum",
              allowed: ["high_confidence", "moderate_confidence", "low_confidence"],
              semantics: "how sure are we about this evidence?"
            }
          }
        },
        max_items: 20,
        semantics: "all facts considered"
      },
      
      evidence_not_used: {
        type: "array",
        required: false,
        items: { type: "string" },
        max_items: 10,
        semantics: "what could have been considered but wasn't"
      },
      
      evidence_quality_assessment: {
        type: "string",
        max_length: 200,
        semantics: "was evidence reliable? any uncertainties?"
      }
    }
  },
  
  decision_factors_explained: {
    type: "array",
    required: true,
    items: {
      type: "object",
      fields: {
        
        factor_name: {
          type: "string",
          semantics: "what was evaluated"
        },
        
        factor_explanation: {
          type: "string",
          max_length: 200,
          semantics: "what this factor means"
        },
        
        your_performance: {
          type: "string",
          semantics: "how did you do on this factor?"
        },
        
        impact_magnitude: {
          type: "enum",
          allowed: ["large_positive", "moderate_positive", "small_positive", "neutral", "small_negative", "moderate_negative", "large_negative"],
          semantics: "how much did this affect your score?"
        },
        
        context_comparison: {
          type: "string",
          max_length: 200,
          semantics: "how does this compare to others?"
        }
      }
    },
    max_items: 20,
    semantics: "each factor explained in simple terms"
  },
  
  alternative_scenarios: {
    type: "object",
    required: true,
    fields: {
      
      scenario_analysis: {
        type: "array",
        required: true,
        items: {
          type: "object",
          fields: {
            
            scenario_description: {
              type: "string",
              max_length: 200,
              examples: [
                "If you had no interruptions",
                "If you had answered faster",
                "If the session was 10 minutes longer"
              ]
            },
            
            hypothetical_score: {
              type: "float",
              range: [0, 100]
            },
            
            score_difference: {
              type: "float",
              semantics: "how many points different?"
            },
            
            would_rank_change: {
              type: "boolean",
              semantics: "would ranking be different?"
            }
          }
        },
        max_items: 10,
        semantics: "what would change your score?"
      }
    }
  },
  
  next_steps_guidance: {
    type: "object",
    required: true,
    fields: {
      
      improvement_areas: {
        type: "array",
        required: true,
        items: {
          type: "object",
          fields: {
            area: { type: "string" },
            current_performance: { type: "string" },
            improvement_suggestion: { type: "string", max_length: 300 }
          }
        },
        max_items: 10,
        semantics: "actionable improvements for next session"
      },
      
      strengths_to_build_on: {
        type: "array",
        required: true,
        items: { type: "string" },
        semantics: "what you did well to repeat"
      },
      
      tips_for_next_session: {
        type: "array",
        required: true,
        items: { type: "string", max_length: 200 },
        max_items: 5,
        semantics: "concrete actions for improvement"
      }
    }
  },
  
  appeal_support_brief: {
    type: "object",
    required: false,
    semantics: "if participant is considering appeal",
    fields: {
      
      appeal_grounds_summary: {
        type: "string",
        max_length: 500,
        semantics: "what grounds might participant have to appeal?"
      },
      
      evidence_for_appeal: {
        type: "array",
        items: { type: "string" },
        semantics: "facts that support appeal claim"
      },
      
      evidence_against_appeal: {
        type: "array",
        items: { type: "string" },
        semantics: "facts that counter appeal claim"
      },
      
      appeal_process_explanation: {
        type: "string",
        max_length: 300,
        semantics: "how to appeal and what to expect"
      }
    }
  },
  
  transparency_assessment: {
    type: "object",
    required: true,
    fields: {
      
      explanation_clarity_score: {
        type: "float",
        range: [0, 1],
        semantics: "how clear is this explanation?"
      },
      
      explanation_completeness_score: {
        type: "float",
        range: [0, 1],
        semantics: "does explanation cover all relevant factors?"
      },
      
      decision_predictability: {
        type: "float",
        range: [0, 1],
        semantics: "could participant predict future scores from this logic?"
      },
      
      any_unexplained_elements: {
        type: "array",
        items: { type: "string" },
        semantics: "anything that couldn't be fully explained?"
      }
    }
  },
  
  gdpr_compliance: {
    type: "object",
    required: true,
    fields: {
      
      includes_right_to_explanation: { type: "boolean" },
      includes_logic_used: { type: "boolean" },
      includes_significance_and_consequences: { type: "boolean" },
      includes_human_review_option: { type: "boolean" },
      machine_readable_format_available: { type: "boolean" },
      
      gdpr_compliance_status: {
        type: "enum",
        allowed: ["fully_compliant", "substantially_compliant", "non_compliant"]
      }
    }
  },
  
  audit_reference: {
    type: "uuid",
    required: true,
    semantics: "immutable reference to explanation record"
  },
  
  processing_metadata: {
    type: "object",
    required: true,
    fields: {
      received_timestamp_utc: { type: "iso8601_datetime" },
      generated_timestamp_utc: { type: "iso8601_datetime" },
      generation_duration_ms: { type: "integer", range: [100, 30000] },
      explanation_language: { type: "string" },
      explanation_verbosity: { type: "string" },
      model_version: { type: "string" }
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
            "explanation_generated",
            "gdpr_data_export_ready",
            "appeal_evidence_compiled",
            "participant_feedback_requested"
          ]
        },
        target_service: {
          type: "enum",
          allowed: [
            "participant_portal",
            "gdpr_system",
            "appeals_team",
            "analytics_service"
          ]
        }
      }
    },
    max_items: 5
  }
}

OUTPUT_GUARANTEES (NON-NEGOTIABLE):

  ✓ Explanation must be understandable by layperson (no jargon)
  ✓ Explanation must be accurate (matches decision logic)
  ✓ Explanation must be complete (all material factors covered)
  ✓ Explanation must be transparent (no hidden reasoning)
  ✓ Explanation must be honest (if uncertain, acknowledge it)
  ✓ Explanation must support fairness (honest about disadvantages)
  ✓ GDPR compliance guaranteed (right to explanation met)
  ✓ No manipulation or persuasion (facts only)
```

---

## 5️⃣ ML / AI LOGIC LAYER (SEALED - 40% RULES, 50% SEMANTIC AI, 10% QUALITY CHECK)

### 5.1 EXPLANATION GENERATION (50% AI-ASSISTED - SEMANTIC)

```
EXPLANATION_FRAMEWORK: Natural Language Generation + Template-Based Composition
EXECUTION_MODE: Deterministic (deterministic logic) + AI-Assisted (narrative generation)
PRINCIPLE: Clear, honest, accurate explanations without manipulation

EXPLANATION_GENERATION_PIPELINE:

```python
def generate_explanation(decision_context, evidence_inputs, participant_profile):
    """
    Generate plain-English explanation of scoring decision.
    """
    
    # Step 1: LOGIC LAYER (100% deterministic)
    decision_logic = extract_decision_logic(decision_context)
    
    # Step 2: EVIDENCE COMPILATION (deterministic)
    evidence_analysis = compile_evidence(evidence_inputs, decision_logic)
    
    # Step 3: FACTOR ANALYSIS (deterministic)
    factor_impacts = quantify_factor_impacts(decision_context)
    
    # Step 4: AI NARRATIVE GENERATION (assisted, with safeguards)
    narrative = generate_narrative_with_ai(
        decision_logic,
        evidence_analysis,
        factor_impacts,
        participant_profile
    )
    
    # Step 5: QUALITY ASSURANCE (deterministic validation)
    validated_narrative = validate_and_correct_narrative(narrative)
    
    # Step 6: ACCESSIBILITY (deterministic formatting)
    accessible_explanation = format_for_accessibility(validated_narrative)
    
    return {
        'plain_english_explanation': accessible_explanation,
        'evidence_summary': evidence_analysis,
        'decision_factors_explained': factor_impacts,
        'quality_metrics': compute_quality_metrics(accessible_explanation)
    }

def generate_narrative_with_ai(logic, evidence, factors, profile):
    """
    Use AI to generate clear narratives from structured data.
    WITH SAFEGUARDS against manipulation.
    """
    
    # Build structured prompt with decision logic
    prompt = f"""
    You are an explanation generator for scoring decisions.
    Your job is to explain complex decisions in plain language.
    
    DECISION LOGIC:
    {format_decision_logic(logic)}
    
    EVIDENCE USED:
    {format_evidence(evidence)}
    
    FACTORS AND IMPACTS:
    {format_factors(factors)}
    
    PARTICIPANT PROFILE:
    - Education Level: {profile['education_level']}
    - Explanation Audience: {profile['audience']}
    
    TASK: Generate explanations that:
    1. Are honest and accurate (never misleading)
    2. Use simple language (no jargon)
    3. Explain what happened and why
    4. Acknowledge any uncertainties
    5. Empower participant understanding
    6. DO NOT manipulate or persuade
    7. DO NOT make excuses for system
    8. DO NOT blame the participant
    9. Support their right to challenge
    
    MANDATORY CONSTRAINTS:
    - No flowery language or emotional appeals
    - No hidden agendas or persuasion
    - Acknowledge any unfair factors honestly
    - Explain what they can do to improve
    
    Format:
    - Executive Summary: 1 paragraph
    - What Happened: 2-3 paragraphs (simple terms)
    - Why This Score: 2-3 paragraphs (reasoning)
    - What Helped/Hurt: bullet lists
    - What They Can Do: actionable suggestions
    """
    
    # Call AI model with guardrails
    narrative = call_ai_model_with_guardrails(
        prompt,
        max_tokens=1000,
        temperature=0.5,  # Lower temperature for consistency
        safety_checks=['no_manipulation', 'no_jargon', 'honesty_check']
    )
    
    return narrative

def validate_and_correct_narrative(narrative):
    """
    Post-process AI narrative to ensure quality.
    100% deterministic validation.
    """
    
    validation_results = {
        'contains_jargon': check_for_jargon(narrative),
        'is_honest': check_for_honesty(narrative),
        'is_complete': check_completeness(narrative),
        'grammar_errors': check_grammar(narrative)
    }
    
    # Corrections (deterministic rules)
    if validation_results['contains_jargon']:
        narrative = replace_jargon_terms(narrative)
    
    if validation_results['grammar_errors'] > 0:
        narrative = fix_grammar(narrative)
    
    if not validation_results['is_honest']:
        # Reject and flag for review
        return None  # Require human review
    
    if not validation_results['is_complete']:
        # Add missing sections
        narrative = supplement_narrative(narrative)
    
    return narrative
```

EXPLANATION TEMPLATES (LOCKED):

For different decision types, use templates to ensure consistency:

**Scoring Decision Template:**
```
Executive Summary: [One sentence: what your score is and why in simplest terms]

What Happened:
- You completed a [SESSION_TYPE] session
- The system evaluated your performance on several factors
- Your score reflects how you did on these factors
- We also adjusted your score based on [FAIRNESS CONSIDERATIONS]

Why This Score: [NUMERIC BREAKDOWN]
- Starting point: [BASE_SCORE] based on your performance
- Adjustment 1: [+/-X points] because [REASON in simple terms]
- Adjustment 2: [+/-X points] because [REASON]
- Final score: [FINAL_SCORE]

What Helped Your Score:
- [Factor 1]: [Simple explanation]
- [Factor 2]: [Simple explanation]
...

What Hurt Your Score:
- [Factor 1]: [Simple explanation]
- [Factor 2]: [Simple explanation]
...

What's Fair:
- [Was participant treated fairly?]
- [Any external factors that disadvantaged them?]
- [Were adjustments applied appropriately?]
```

**Interruption Explanation Template:**
```
About the Phone Call Interruption:
- We detected [NUMBER] phone interruption(s) during your session
- Duration: [TIME]
- This is a technical issue outside your control

How This Affected Your Score:
- These interruptions were likely to disadvantage you
- The system reduced your penalty by [AMOUNT] points
- This is designed to be fair [CONTEXT]

Fair Treatment:
- No one should be penalized for technical issues beyond their control
- The adjustment reflects the system's assessment of impact
```
```

### 5.2 EVIDENCE-BASED REASONING (40% RULES - DETERMINISTIC)

```
EVIDENCE_FRAMEWORK: Structured Evidence Compilation
EXECUTION_MODE: 100% deterministic
PRINCIPLE: All evidence presented objectively (for and against)

```python
def compile_evidence(evidence_inputs, decision_logic):
    """
    Compile all evidence used in decision, objectively presented.
    """
    
    evidence_compilation = {
        'evidence_used': [],
        'evidence_not_used': [],
        'evidence_quality': {}
    }
    
    # For each input variable in decision logic
    for variable in decision_logic['input_variables']:
        
        # Find evidence for this variable
        evidence_item = find_evidence(variable, evidence_inputs)
        
        if evidence_item:
            # Explain what this evidence is and why it matters
            evidence_compilation['evidence_used'].append({
                'evidence_name': variable,
                'evidence_value': evidence_item['value'],
                'why_it_mattered': explain_relevance(variable, evidence_item),
                'confidence': evidence_item['confidence'],
                'how_it_affected_score': compute_impact(variable, evidence_item)
            })
        else:
            # Note what could have been considered
            evidence_compilation['evidence_not_used'].append(variable)
    
    # Quality assessment
    evidence_compilation['evidence_quality'] = {
        'all_evidence_available': len(evidence_compilation['evidence_not_used']) == 0,
        'high_confidence_evidence_percent': compute_confidence_distribution(evidence_inputs),
        'any_contradictions': detect_evidence_contradictions(evidence_inputs)
    }
    
    return evidence_compilation
```

### 5.3 QUALITY ASSURANCE (10% CHECKS - DETERMINISTIC)

```
QUALITY_ASSURANCE: Automated Validation + Safeguards
EXECUTION_MODE: 100% deterministic rules
PRINCIPLE: Ensure explanations are accurate, fair, understandable

```python
def validate_explanation_quality(narrative, decision_logic, evidence):
    """
    Quality checks on generated explanation.
    """
    
    quality_checks = {
        'accuracy_verified': verify_accuracy(narrative, decision_logic),
        'completeness_verified': verify_completeness(narrative, evidence),
        'clarity_verified': verify_clarity(narrative),
        'fairness_verified': verify_fairness_context(narrative),
        'jargon_free': check_jargon_free(narrative),
        'no_manipulation': check_not_manipulative(narrative)
    }
    
    # Reject if fails critical checks
    if not quality_checks['accuracy_verified']:
        return None  # Fail - require human review
    
    if not quality_checks['fairness_verified']:
        return None  # Fail - must include fairness context
    
    if quality_checks['contains_manipulation']:
        return None  # Fail - explanation appears to persuade rather than inform
    
    # Score explanation quality (0-1)
    quality_score = compute_quality_score(quality_checks)
    
    return {
        'quality_checks': quality_checks,
        'quality_score': quality_score,
        'passed_validation': all(quality_checks.values()),
        'areas_for_improvement': identify_improvement_areas(quality_checks)
    }

def check_not_manipulative(narrative):
    """
    Detect manipulation tactics in explanation.
    """
    
    manipulation_patterns = [
        'flattery',              # praising participant excessively
        'blame_shifting',        # blaming participant for system issues
        'false_urgency',         # creating artificial urgency
        'emotional_appeals',     # using emotions instead of facts
        'loaded_language',       # biased word choices
        'false_dichotomies',     # either/or when more options exist
        'gaslighting'            # making participant doubt perception
    ]
    
    for pattern in manipulation_patterns:
        if pattern in detect_patterns(narrative):
            return False  # Manipulation detected
    
    return True  # Clean
```
```

---

## 6️⃣ SCALABILITY DESIGN (LOCKED)

```
EXECUTION_PATTERN: Real-Time On-Demand + Batch Processing

REAL-TIME PROCESSING:
  Trigger: Participant requests explanation
  Latency: < 5 seconds (explanation ready)
  Action: Generate explanation on-demand
  Parallelization: 1000s simultaneous requests

BATCH PROCESSING:
  Frequency: Daily
  Trigger: Cron job at 23:00 UTC
  Latency: < 1 hour
  Action: Pre-generate explanations, GDPR exports

SCALABILITY TARGETS:

Performance:
  EXPECTED_RPS = 100 explanation requests per minute (peak)
    → 144K explanations per day
  
  REAL_TIME_SLA = P99 < 5 seconds
    → Participant sees explanation immediately
  
  GDPR_EXPORT_SLA = < 1 hour generation
    → 30-day GDPR compliance window
  
  CONCURRENT_REQUESTS = 1000 simultaneous explanations
    → Auto-scaling by request volume

Infrastructure:

  Stateless Agents:
    - 10 container instances (real-time)
    - 3 container instances (batch)
    - Auto-scaling: +2 per 500 requests/min
  
  Data Layer:
    - PostgreSQL: Explanation cache + history
    - Redis: Recent explanations (fast retrieval)
    - ClickHouse: Quality metrics + trends
    - S3: Immutable explanation archive
  
  AI/ML:
    - Cached AI model inference (lower latency)
    - Batched processing (efficiency)
    - Quality validation (deterministic)
```

---

## 7️⃣ SECURITY ENFORCEMENT (SEALED - PRIVACY + ACCURACY)

```
SECURITY_LAYERS (NON-NEGOTIABLE):

Layer 1: Participant Privacy
  ✓ Only explain own score (authentication verified)
  ✓ No disclosure to third parties (confidential)
  ✓ Secure delivery (HTTPS only)
  ✓ No logging of sensitive explanation content

Layer 2: Accuracy Verification
  ✓ Explanations match decision logic (verified)
  ✓ Numbers check out (math validated)
  ✓ No false claims (audited)
  ✓ Confidence disclosed (uncertainties noted)

Layer 3: Fairness Enforcement
  ✓ Honest about disadvantages (not hidden)
  ✓ Fair treatment acknowledged
  ✓ No excuses for unfair decisions
  ✓ Support for appeals provided

Layer 4: No Manipulation
  ✓ Facts only (no rhetoric or persuasion)
  ✓ Neutral tone (not defensive)
  ✓ Transparent reasoning (no hidden logic)
  ✓ Empowering language (not patronizing)

Layer 5: GDPR Compliance
  ✓ Right to explanation met
  ✓ Machine-readable format available
  ✓ Delivered within 30 days
  ✓ Includes all required elements

Layer 6: Encryption & Transport
  ✓ TLS 1.3 for all communication
  ✓ At-rest encryption for cached explanations
  ✓ Secure PDF generation (for exports)

Layer 7: Audit Logging
  ✓ Every explanation request logged
  ✓ Logs stored append-only
  ✓ Access audited (who viewed what)

Layer 8: AI Safeguards
  ✓ Generative AI monitored for bias
  ✓ Output validated for accuracy
  ✓ Prompt injection protection
  ✓ Model drift detection
```

---

## 8️⃣ AUDIT & TRACEABILITY (LOCKED - DISCOVERY)

```
MANDATORY AUDIT LOG ENTRY:

Every explanation generates immutable, discoverable entry:

```sql
INSERT INTO explanation_audit_logs (
  audit_id,                          -- UUID v4
  timestamp_utc,                     -- ISO8601
  service_name,                      -- 'phone_ai_explainability_agent'
  
  participant_id,                    -- Who requested
  explanation_request_type,          -- Type of explanation
  decision_being_explained,          -- Which decision
  
  request_context: {
    education_level,
    language,
    audience,
    configuration
  },
  
  explanation_output: {
    executive_summary,
    explanation_hash,                -- SHA256
    clarity_score,
    completeness_score
  },
  
  quality_validation: {
    accuracy_verified,
    completeness_verified,
    clarity_verified,
    fairness_verified,
    not_manipulative
  },
  
  gdpr_compliance: {
    is_subject_access_response,
    right_to_explanation_met,
    machine_readable_provided
  },
  
  processing_time_ms,
  
  ai_model_used,
  validation_status: 'passed' | 'failed' | 'human_review_required'
)
```

Immutability & Discovery:
  ✓ Primary key: (audit_id, timestamp_utc)
  ✓ Searchable by participant (for GDPR)
  ✓ No UPDATE allowed (immutable)
  ✓ Legal holds preserved (evidence)
  ✓ Retention: 7+ years (compliance)
  ✓ Backup: Daily immutable snapshot
```

---

## 9️⃣ FAILURE POLICY (SEALED - TRANSPARENCY FIRST)

```
FAILURE SCENARIOS & HANDLING (LOCKED):

Scenario 1: AI Model Fails to Generate Narrative
  Trigger: AI model times out or errors
  Action:
    ✓ Fall back to structured explanation (template-based)
    ✓ Return explanation without narrative sections
    ✓ Flag: "Simplified explanation provided"
    ✓ No data loss (structured data still available)

Scenario 2: Missing Evidence
  Trigger: Cannot find evidence for a factor
  Action:
    ✓ Explain what was considered but uncertain
    ✓ Acknowledge the gap (transparency)
    ✓ Explain what we DO know
    ✓ Flag: "Some information incomplete"

Scenario 3: Explanation Quality Fails Validation
  Trigger: Generated explanation is inaccurate or manipulative
  Action:
    ✓ REJECT explanation (don't serve to participant)
    ✓ Flag for human review (escalate)
    ✓ Serve fallback template explanation
    ✓ ESCALATE to: content review team

Scenario 4: GDPR Request Cannot Be Processed
  Trigger: Data export format issue
  Action:
    ✓ Log GDPR request (compliance)
    ✓ Attempt to correct format
    ✓ If fails: Manual review + generation
    ✓ ESCALATE to: GDPR compliance officer

Scenario 5: Participant Not Authorized
  Trigger: Requesting explanation for another's score
  Action:
    ✓ REJECT request (403 Forbidden)
    ✓ LOG security incident
    ✓ No explanation provided
    ✓ Alert: Potential unauthorized access attempt

Scenario 6: Language Not Supported
  Trigger: Participant requests unsupported language
  Action:
    ✓ Provide in supported language (English fallback)
    ✓ Offer option to request translation (future)
    ✓ Document language request (for improvement)
    ✓ No data loss (explanation still provided)

Scenario 7: AI Model Bias Detected
  Trigger: Generated explanation shows bias/unfairness
  Action:
    ✓ REJECT explanation
    ✓ Flag for model retraining
    ✓ Serve template explanation
    ✓ ESCALATE to: fairness team

Scenario 8: Explanation Clarity Below Threshold
  Trigger: Generated explanation scores < 0.60 clarity
  Action:
    ✓ Simplify explanation (deterministic rules)
    ✓ Remove jargon
    ✓ Add examples
    ✓ Revalidate

Scenario 9: Database Connection Failure
  Trigger: Cannot retrieve decision context
  Action:
    ✓ Retry with exponential backoff
    ✓ If fails: Return service unavailable (503)
    ✓ Queue request for retry
    ✓ ESCALATE to: ops_team

Scenario 10: Concurrent Request Surge
  Trigger: 1000s of simultaneous requests
  Action:
    ✓ Queue requests (fair ordering)
    ✓ Serve with < 60 second delay
    ✓ Prioritize GDPR requests (legal requirement)
    ✓ Scale up containers (auto-scaling)

PRINCIPLE: Always attempt to serve explanation
  - Never deny participant's right to explanation
  - If full explanation unavailable, provide what can be provided
  - Flag limitations transparently
  - Escalate if cannot meet quality standards
```

---

## 🔟 INTER-AGENT DEPENDENCY MAP (SEALED)

```
UPSTREAM AGENTS (Providers - Decision Context):

  1. Scoring Engine (DECISION + CONTEXT)
     Output: Score + adjustments + decision factors
     Frequency: Per session
     Contains: Base score, adjustments, factor weights
  
  2. Phone Interruption Detection (EVIDENCE)
     Output: Interruption findings + evidence
     Frequency: Referenced if interruptions affected score
     Contains: Detection logic, confidence, timeline
  
  3. Phone Behavior Analytics (FAIRNESS CONTEXT)
     Output: Fairness signals + impact assessment
     Frequency: Referenced for fairness explanation
     Contains: Disadvantage assessment, fairness impact
  
  4. Score Bias Audit (SYSTEMIC CONTEXT)
     Output: Bias findings (for fairness context)
     Frequency: Referenced if bias detected
     Contains: Cohort comparisons, bias evidence

DOWNSTREAM AGENTS (Consumers - Explanation Users):

  1. Participant Portal (PRIMARY CONSUMER)
     Input: PLAIN_ENGLISH_EXPLANATION
     Action: Display to participant
     SLA: < 5 seconds delivery
     Contract: Clear, understandable
  
  2. GDPR Data Export System (COMPLIANCE)
     Input: EXPLANATION (in machine-readable format)
     Action: Include in data subject access response
     SLA: < 30 days (GDPR requirement)
     Contract: Compliant format
  
  3. Appeals Resolution Team (DISPUTE CONTEXT)
     Input: APPEAL_SUPPORT_BRIEF (if participant appeals)
     Action: Use as basis for investigation
     SLA: < 30 minutes
     Contract: Clear evidence compilation
  
  4. Analytics Service (QUALITY MONITORING)
     Input: EXPLANATION_QUALITY_METRICS
     Action: Monitor clarity, completeness
     SLA: Daily reporting
     Contract: Quality metrics for trending

EVENT FLOW DIAGRAM (LOCKED):

```
Participant Requests Explanation
         │
         ▼
PHONE_AI_EXPLAINABILITY_AGENT
┌────────────────────────────────────────┐
│ 1. Extract Decision Logic (deterministic)
│ 2. Compile Evidence (deterministic)    │
│ 3. Quantify Factor Impacts (deterministic)
│ 4. Generate Narrative (AI-assisted)    │
│ 5. Validate Quality (deterministic)    │
│ 6. Format for Accessibility            │
│ 7. Audit Logging (immutable)           │
└────────────────────────────────────────┘
        │          │          │
        ▼          ▼          ▼
   Participant   GDPR        Appeals
   Portal        System      Team
  (display)      (export)    (context)
```
```

---

## 1️⃣1️⃣ PASSIVE INTELLIGENCE COMPATIBILITY (OPTIONAL)

```
FEATURE VECTOR EMISSION (OPTIONAL):

If passive intelligence integrated, Explainability Agent MAY emit
transparency/understanding signals for user engagement ML.

FEATURE_VECTOR:

{
  timestamp_utc: ISO8601,
  feature_name: "participant_decision_understanding",
  feature_value: float (0-1),
  source_agent: "phone_ai_explainability_agent",
  
  context: {
    explanation_clarity_score: float,
    explanation_completeness: float,
    participant_requested_explanation: boolean,
    participant_appealed_after_explanation: boolean
  }
}

USAGE: ML can predict if participants understand decisions
```

---

## 1️⃣2️⃣ INNOVATION ECONOMY COMPATIBILITY (NOT APPLICABLE)

```
Explainability independent of innovation economy.
```

---

## 1️⃣3️⃣ GROWTH ENGINE HOOK (NOT APPLICABLE)

```
Explanations support growth but don't directly affect ranking.
```

---

## 1️⃣4️⃣ PERFORMANCE MONITORING (LOCKED - PROMETHEUS)

```
MANDATORY METRICS:

1. Request Metrics:
   phone_explanation_requests_total
     label: explanation_type
   
   phone_explanation_generation_duration_seconds
     histogram (P50/90/99)

2. Quality Metrics:
   phone_explanation_clarity_score
     histogram (0-1 distribution)
   
   phone_explanation_completeness_score
     histogram
   
   phone_explanation_validation_pass_rate
     gauge (% passing quality checks)

3. GDPR Metrics:
   phone_explanation_gdpr_requests_total
     counter
   
   phone_explanation_gdpr_compliance_met
     gauge (% fully compliant)

4. Participant Engagement:
   phone_explanation_participant_satisfaction
     gauge (from feedback)
   
   phone_explanation_read_completion_rate
     gauge (did participant read entire explanation?)

Alerting Rules:

  Alert 1: Quality Score Drop (P2)
    Condition: avg_clarity < 0.70
    Severity: P2
    Action: Alert content team
  
  Alert 2: High Rejection Rate (P2)
    Condition: validation_fail_rate > 10%
    Severity: P2
    Action: Alert AI/NLP team
  
  Alert 3: GDPR Non-Compliance (P1)
    Condition: gdpr_compliance_met < 100%
    Severity: P1
    Action: Page compliance officer immediately
  
  Alert 4: Latency SLA Miss (P3)
    Condition: p99(generation_time) > 5 seconds
    Severity: P3
    Action: Alert ops team

Grafana Dashboards:

  Dashboard 1: Explanation Health
    - Generation latency (P50/90/99)
    - Quality scores (clarity, completeness)
    - Validation pass rate
  
  Dashboard 2: GDPR Compliance
    - Requests processed
    - Compliance met %
    - Response time vs 30-day requirement
  
  Dashboard 3: Participant Engagement
    - Satisfaction ratings
    - Completion rates
    - Appeal rate post-explanation
```

---

## 1️⃣5️⃣ VERSIONING POLICY (SEALED)

```
VERSIONING CONSTRAINT:

All Changes: Add-Only, Never Destructive

Semantic Versioning:
  Format: major.minor.patch
  
  MAJOR: Changes to explanation generation methodology
         All historical explanations may need refresh
         Backward compatibility NOT guaranteed
  
  MINOR: Improved clarity, new explanation types
         Fully backward compatible
         Historical explanations valid
  
  PATCH: Bug fixes, performance improvements
         Zero breaking changes
```

---

## 1️⃣6️⃣ NON-NEGOTIABLE RULES (SEALED & LOCKED)

```
FORBIDDEN ACTIONS:

This agent MUST NOT:

  ✗ Hide information from participant
  ✗ Mislead with incomplete explanations
  ✗ Use manipulative language
  ✗ Blame participant for system issues
  ✗ Minimize unfair treatment
  ✗ Disclose third-party information
  ✗ Refuse right to explanation
  ✗ Violate GDPR requirements
  ✗ Use AI for non-transparent decisions
  ✗ Fail to acknowledge uncertainties
  ✗ Serve low-quality explanations
  ✗ Modify historical explanations
  ✗ Deny GDPR access requests
  ✗ Include biased language
  ✗ Patronize or condescend

MANDATORY ACTIONS:

This agent MUST:

  ✓ Explain all decisions clearly
  ✓ Present evidence objectively
  ✓ Acknowledge limitations honestly
  ✓ Support right to explanation (GDPR)
  ✓ Use plain language
  ✓ Validate explanation quality
  ✓ Maintain audit trail
  ✓ Respect participant privacy
  ✓ Enable informed appeals
  ✓ Be transparent about AI usage
  ✓ Flag biases/unfairness
  ✓ Support decision understanding
  ✓ Deliver promptly (< 5 seconds)
  ✓ Meet GDPR compliance
  ✓ Empower participant agency
```

---

## CHANGELOG & VERSION HISTORY

```
v1.0.0 - 2025-03-04 (SEALED RELEASE)
  ✓ Initial sealed specification for Antigravity platform
  ✓ Plain-English explanation generation
  ✓ AI-assisted narrative generation (with safeguards)
  ✓ Evidence-based reasoning compilation
  ✓ Quality assurance validation
  ✓ GDPR compliance (right to explanation)
  ✓ Alternative scenario analysis
  ✓ Appeal support brief generation
  ✓ 100% immutable audit trail
  ✓ Transparency & fairness enforcement
```

---

## SEAL CERTIFICATION

🔒 **THIS SPECIFICATION IS SEALED**

This document defines a production-grade explainability agent for Ecoskiller Antigravity.
All explanations are accurate, transparent, fair, and GDPR-compliant.

**Sealed By:** Architecture Review Board + Legal/Compliance + GDPR Officer
**Date Sealed:** 2025-03-04
**Review Cycle:** Quarterly + post-quality-audit review
**Any modifications require formal change control process with legal approval.**

---

**END OF SEALED SPECIFICATION**
