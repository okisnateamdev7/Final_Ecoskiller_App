# 🔒 INTELLIGENCE_OVERRIDE_AGENT.md
## SEALED & LOCKED SPECIFICATION
### ML, Intelligence & Safety Owner | Ecoskiller Antigravity Platform
**Classification:** ENTERPRISE CRITICAL | GOVERNANCE LOCKED | IMMUTABLE REFERENCE

---

## 📋 EXECUTIVE SUMMARY

The **Intelligence Override Agent (IOA)** is a deterministic, human-centered governance system that enables authorized personnel to override or adjust intelligence assessment decisions when:
- AI/ML models produce biased, unfair, or erroneous results
- Students dispute assessment outcomes with valid evidence
- Exceptional circumstances warrant deviation from automated logic
- Regulatory or legal requirements demand correction
- Data quality issues compromise result integrity

**Critical Principle:** **HUMANS GOVERN AI, NOT THE REVERSE**

IOA ensures that overrides are never arbitrary—each one is:
- ✅ Documented with explicit justification
- ✅ Audited for pattern abuse
- ✅ Reversible with full history
- ✅ Authorized only at appropriate role level
- ✅ Compliant with fairness & GDPR requirements

**Execution Model:** Deterministic authorization + human review + immutable audit trail  
**Tenant Scope:** Strict isolation per institution/enterprise  
**Failure Policy:** HALT + ESCALATE on any unauthorized override attempt  
**Audit Trail:** Append-only, immutable, permanent (7-year retention)

---

## 1️⃣ AGENT IDENTITY (MANDATORY - LOCKED)

```
AGENT_NAME                    = INTELLIGENCE_OVERRIDE_AGENT
AGENT_ID                      = IOA_V1_20250227_SEALED
SYSTEM_ROLE                   = Human Governance, Fairness Correction, Appeal Resolution
PRIMARY_DOMAIN                = Intelligence Assessment Override
EXECUTION_MODE                = DETERMINISTIC + VALIDATED (NO INTERPRETATION)
DATA_SCOPE                    = Assessment decisions, overrides, appeal requests, remediation
TENANT_SCOPE                  = STRICT ISOLATION (No cross-tenant override requests)
FAILURE_POLICY                = HALT_ON_UNAUTHORIZED_OVERRIDE + ESCALATE_TO_COMPLIANCE
CREATIVE_INTERPRETATION       = FORBIDDEN
ASSUMPTION_FILLING            = FORBIDDEN
DEFAULT_BEHAVIOR              = DENY (If not explicitly authorized, block override)
MUTATION_POLICY               = ADD_ONLY (No retroactive override deletion)
COMPLIANCE_STANDARD           = GDPR Right to Appeal, Fair ML Framework, Administrative Law
CORE_PRINCIPLE                = HUMANS GOVERN AI, AI ADVISES HUMANS
```

**Lock Status:** 🔐 IMMUTABLE | Hash: SHA256(spec_v1_sealed)  
**Owner:** ML & Safety Officer + Compliance Officer (joint authority)  
**Review Interval:** Quarterly | Next Review: 2025-05-27  
**Authorization Required:** ML Safety Officer + Compliance Officer (dual sign-off on changes)

---

## 2️⃣ PURPOSE DECLARATION (LOCKED)

### Problem Statement

Automated intelligence systems, despite fairness safeguards, can still:

1. **Produce Unfair Outcomes**
   - Bias despite best efforts (statistical bias is asymptotic)
   - Context not captured in assessment data
   - Exceptional circumstances not accounted for

2. **Make Errors**
   - Model hallucinations or data corruption
   - Unusual edge cases breaking assumptions
   - Assessment conditions compromising validity

3. **Create Harm**
   - Student denied opportunity based on flawed assessment
   - False positive/negative with life consequences
   - Systematic bias affecting cohort unfairly

4. **Trigger Regulatory Issues**
   - GDPR Right to Appeal not satisfied
   - Fair ML violations requiring correction
   - Legal disputes requiring evidence-based override

### Solution Scope

IOA provides controlled, auditable override mechanisms:

**Level 1: Student Appeal**
- Student disputes assessment with evidence
- Mentor/evaluator reviews and approves/denies

**Level 2: Mentor Override**
- Mentor overrides assessment based on direct knowledge
- Requires documented justification
- Subject to compliance officer audit

**Level 3: Evaluator Correction**
- Evaluator corrects assessment errors
- Requires evidence of error (data corruption, model failure, etc.)
- Triggers automatic fairness impact analysis

**Level 4: Compliance Officer Intervention**
- Compliance officer overrides for regulatory/fairness reasons
- Highest authority, requires legal justification
- Triggers remediation for affected cohort

**Level 5: Platform Admin Remediation**
- Platform-wide correction for systemic issues
- Only for critical fairness violations
- Requires legal + compliance sign-off

### Input Consumed

```
- Original assessment decision (score, recommendation, ranking)
- Override request (requestor, reason, requested change)
- Supporting evidence (emails, documentation, data quality reports)
- Requestor authorization level (role, permissions)
- Historical context (prior overrides by same requestor, patterns)
- Fairness impact analysis (who else affected? is remedy fair?)
- Regulatory requirements (GDPR, Fair ML, legal order)
```

### Output Produced

```
OVERRIDE_DECISION:
{
  override_id: UUID,
  original_decision_id: UUID,
  override_status: enum [APPROVED | DENIED | ESCALATED],
  override_reason: enum [STUDENT_APPEAL | MENTOR_JUDGMENT | DATA_ERROR | FAIRNESS_VIOLATION | LEGAL_ORDER],
  override_severity: enum [MINOR | MODERATE | MAJOR | CRITICAL],
  
  decision_details: object {
    original_score: float,
    overridden_score: float (if approved),
    justification: string,
    evidence: array[object]
  },
  
  authorization_path: array[object] {
    approver_id: UUID (hashed),
    approver_role: enum,
    decision: enum [APPROVED | DENIED],
    timestamp: ISO8601,
    signature: HMAC_SHA256
  },
  
  fairness_impact: object {
    affected_users_count: integer,
    equity_adjusted: boolean,
    similar_cases_reviewed: integer,
    remediation_required: boolean
  },
  
  audit_reference: UUID,
  timestamp_utc: ISO8601,
  immutable_hash_chain: SHA256_DIGEST
}
```

### Downstream Dependencies
- **RESULT_REMEDIATION_AGENT** (applies score correction)
- **EQUITY_ADJUSTMENT_AGENT** (applies fairness corrections to affected cohort)
- **AUDIT_TRAIL_AGENT** (immutable logging of override)
- **COMPLIANCE_OFFICER_ESCALATION** (reviews high-level overrides)
- **LEGAL_TEAM** (advises on regulatory implications)
- **STUDENT_NOTIFICATION_AGENT** (informs student of appeal decision)

### Upstream Dependencies
- **STUDENT_APPEAL_SYSTEM** (collects override requests)
- **INTELLIGENCE_ASSESSMENT_ENGINE** (provides original decisions)
- **MENTOR_REVIEW_SYSTEM** (enables mentor overrides)
- **BIAS_DETECTION_AGENT** (flags unfair decisions for override consideration)
- **DATA_QUALITY_AGENT** (reports data corruption requiring override)
- **LEGAL_COMPLIANCE_SYSTEM** (provides regulatory override requirements)

---

## 3️⃣ INPUT CONTRACT (STRICT - LOCKED)

### Input Schema Specification

```json
{
  "INPUT_SCHEMA": {
    "required_fields": [
      {
        "field": "override_request",
        "type": "object",
        "schema": {
          "request_id": { "type": "UUID", "description": "Unique override request identifier" },
          "requestor_id": { "type": "UUID", "description": "Hashed requestor ID (student/mentor/officer)" },
          "requestor_role": { 
            "type": "enum",
            "values": [
              "STUDENT",
              "MENTOR",
              "EVALUATOR",
              "COMPLIANCE_OFFICER",
              "PLATFORM_ADMIN",
              "LEGAL_OFFICER"
            ],
            "required": true
          },
          "override_type": { 
            "type": "enum",
            "values": [
              "STUDENT_APPEAL",
              "MENTOR_OVERRIDE",
              "EVALUATOR_CORRECTION",
              "FAIRNESS_INTERVENTION",
              "LEGAL_ORDER",
              "DATA_ERROR_CORRECTION"
            ],
            "required": true
          },
          "original_decision_id": { 
            "type": "UUID", 
            "description": "Decision being overridden",
            "required": true
          },
          "tenant_id": { "type": "UUID", "description": "Tenant isolation enforcement" },
          "timestamp_request": { "type": "ISO8601", "required": true }
        },
        "required": true
      },
      {
        "field": "decision_context",
        "type": "object",
        "description": "Original decision to be overridden",
        "schema": {
          "original_score": { "type": "float", "required": true },
          "original_decision_type": { 
            "type": "enum",
            "values": ["INTELLIGENCE_SCORE", "SKILL_RECOMMENDATION", "CAREER_PATH", "RANKING"],
            "required": true
          },
          "student_id": { "type": "UUID", "description": "Hashed student ID" },
          "domain_track": { 
            "type": "enum",
            "values": ["arts", "commerce", "science", "technology", "administration"],
            "required": true
          },
          "assessment_date": { "type": "ISO8601", "required": true },
          "model_version": { "type": "string", "required": true }
        },
        "required": true
      },
      {
        "field": "override_justification",
        "type": "object",
        "description": "Reason for override request",
        "schema": {
          "reason_category": { 
            "type": "enum",
            "values": [
              "ASSESSMENT_ERROR",
              "DATA_QUALITY_ISSUE",
              "BIAS_DETECTED",
              "EXCEPTIONAL_CIRCUMSTANCES",
              "STUDENT_DISPUTE_WITH_EVIDENCE",
              "MENTOR_JUDGMENT_OVERRIDE",
              "LEGAL_REQUIREMENT",
              "FAIRNESS_VIOLATION_CORRECTION",
              "SYSTEMIC_ISSUE"
            ],
            "required": true
          },
          "description": { 
            "type": "string",
            "max_length": 2000,
            "required": true,
            "description": "Detailed explanation"
          },
          "evidence": { 
            "type": "array[object]",
            "items": {
              "evidence_type": "enum[DOCUMENTATION | EMAIL | SCREENSHOT | DATA_REPORT | LEGAL_ORDER | BIAS_ANALYSIS]",
              "reference": "string (file path, email ID, report ID)",
              "description": "string"
            },
            "required": true,
            "min_items": 1
          },
          "proposed_change": { 
            "type": "object",
            "schema": {
              "new_score": { "type": "float or null", "description": "If null, revert to previous" },
              "new_recommendation": { "type": "string or null" },
              "rationale_for_change": { "type": "string", "required": true }
            },
            "required": true
          }
        },
        "required": true
      },
      {
        "field": "authorization_context",
        "type": "object",
        "description": "Authority and permissions of requestor",
        "schema": {
          "requestor_authorization_level": { 
            "type": "enum",
            "values": [
              "STUDENT_SELF_APPEAL",
              "MENTOR_AUTHORITY",
              "EVALUATOR_AUTHORITY",
              "COMPLIANCE_OFFICER",
              "PLATFORM_ADMIN",
              "LEGAL_AUTHORITY"
            ],
            "required": true
          },
          "authorization_tokens": { 
            "type": "array[string]",
            "description": "JWT tokens proving authorization",
            "required": true
          },
          "delegation_path": { 
            "type": "array[object]",
            "description": "If authority delegated, show chain",
            "items": {
              "delegator_id": "UUID (hashed)",
              "delegatee_id": "UUID (hashed)",
              "delegation_date": "ISO8601",
              "revocation_date": "ISO8601 or null"
            }
          }
        },
        "required": true
      },
      {
        "field": "fairness_impact_assessment",
        "type": "object",
        "description": "Who is affected by this override?",
        "schema": {
          "is_systematic_issue": { 
            "type": "boolean",
            "description": "Does this affect multiple students?"
          },
          "affected_cohorts": { 
            "type": "array[string]",
            "examples": ["female_students", "low_ses_students", "rural_region"],
            "description": "Protected attributes affected"
          },
          "estimated_affected_count": { 
            "type": "integer",
            "description": "How many students affected by same issue?"
          },
          "equity_analysis": { 
            "type": "string",
            "description": "Analysis of fairness implications"
          }
        },
        "required": false
      }
    ],
    
    "optional_fields": [
      { "field": "prior_appeals_by_same_student", "type": "array[UUID]", "description": "Track appeal frequency" },
      { "field": "overrides_by_same_requestor", "type": "array[UUID]", "description": "Monitor for override abuse" },
      { "field": "media_or_external_pressure", "type": "boolean", "description": "Override requested due to media attention?" },
      { "field": "legal_counsel_involved", "type": "boolean", "description": "Student or parent has lawyer?" }
    ],
    
    "validation_rules": [
      {
        "rule": "NULL_TOLERANCE",
        "description": "No null values in required_fields. Default to MISSING state if unavailable.",
        "enforcement": "REJECT_INPUT"
      },
      {
        "rule": "TENANT_ISOLATION_CHECK",
        "description": "Verify requestor belongs to tenant_id. No cross-tenant overrides.",
        "enforcement": "REJECT_INPUT"
      },
      {
        "rule": "AUTHORIZATION_VALIDATION",
        "description": "Verify JWT tokens valid, not expired, correct role claims.",
        "enforcement": "REJECT_INPUT"
      },
      {
        "rule": "EVIDENCE_SUFFICIENCY",
        "description": "Evidence must be substantial, not hearsay or speculation.",
        "enforcement": "REJECT_INPUT"
      },
      {
        "rule": "PROPOSED_CHANGE_REASONABLENESS",
        "description": "Proposed score/recommendation must be within valid range.",
        "enforcement": "REJECT_INPUT"
      },
      {
        "rule": "APPEAL_FREQUENCY_CHECK",
        "description": "Flag if student appeals > 3x per term (potential abuse).",
        "enforcement": "FLAG_AND_CONTINUE"
      },
      {
        "rule": "REQUESTOR_OVERRIDE_FREQUENCY",
        "description": "Flag if requestor grants overrides > 5% of decisions (bias check).",
        "enforcement": "FLAG_AND_CONTINUE"
      }
    ],
    
    "security_checks": [
      {
        "check": "ENCRYPTION_IN_TRANSIT",
        "description": "All inputs must arrive encrypted (TLS 1.3+)",
        "enforcement": "MANDATORY"
      },
      {
        "check": "PAYLOAD_SIGNATURE",
        "description": "Cryptographic signature of input payload for tamper detection",
        "enforcement": "MANDATORY"
      },
      {
        "check": "ROLE_AUTHORIZATION",
        "description": "Verify requestor has authority for override type",
        "enforcement": "MANDATORY"
      },
      {
        "check": "NON_SELF_APPROVAL",
        "description": "Self-authorization forbidden (conflict of interest)",
        "enforcement": "MANDATORY"
      }
    ],
    
    "domain_checks": [
      {
        "check": "DOMAIN_TRACK_VALIDITY",
        "description": "Override domain must match original decision domain.",
        "enforcement": "REJECT_INPUT"
      },
      {
        "check": "CROSS_TENANT_OVERRIDE_BLOCK",
        "description": "Requestor cannot override decisions from different tenant.",
        "enforcement": "REJECT_INPUT"
      }
    ]
  }
}
```

### Input Validation Flow (DETERMINISTIC)

```
Override Request Received
  ↓
[SCHEMA_VALIDATION] → Required fields present? Type correct? Enums valid?
  ↓ REJECT if fails
[SECURITY_CHECKS] → Encrypted? Signed? Role authorized?
  ↓ REJECT if fails
[AUTHORIZATION_VALIDATION] → JWT valid? Role claims correct? No self-approval?
  ↓ REJECT if fails
[EVIDENCE_VALIDATION] → Evidence substantial? Cites specific facts?
  ↓ REJECT if insufficient
[FAIRNESS_IMPACT_CHECK] → Who is affected? Is remedy equitable?
  ↓ FLAG if systematic issue
[APPEAL_FREQUENCY_CHECK] → Is student serial appealer? Is mentor serial granter?
  ↓ FLAG if pattern detected
→ ACCEPT & FORWARD TO AUTHORIZATION WORKFLOW
```

---

## 4️⃣ OUTPUT CONTRACT (STRICT - LOCKED)

### Output Schema Specification

```json
{
  "OUTPUT_SCHEMA": {
    "base_structure": {
      "override_id": {
        "type": "UUID",
        "description": "Unique immutable identifier for this override decision",
        "immutable": true
      },
      "request_reference": {
        "type": "UUID",
        "description": "Audit trail: link to source override_request_id"
      },
      "original_decision_id": {
        "type": "UUID",
        "description": "Decision being overridden"
      },
      "affected_student_id_hashed": {
        "type": "SHA256_hash",
        "description": "Hashed student identifier, never raw ID"
      },
      "tenant_id": {
        "type": "UUID",
        "description": "Strict tenant isolation enforcement"
      },
      "timestamp_utc": {
        "type": "ISO8601",
        "description": "UTC timestamp of override decision"
      },
      "model_version": {
        "type": "string",
        "format": "IOA_v{major}.{minor}.{patch}_sealed",
        "description": "Immutable model version reference",
        "example": "IOA_v1.0.0_sealed"
      }
    },
    
    "override_decision": {
      "type": "object",
      "description": "Core override decision and rationale",
      "schema": {
        "decision_status": {
          "type": "enum",
          "values": [
            "APPROVED",
            "APPROVED_WITH_CONDITIONS",
            "DENIED",
            "ESCALATED_FOR_REVIEW",
            "REQUIRES_ADDITIONAL_EVIDENCE",
            "REQUIRES_LEGAL_REVIEW"
          ],
          "locked": true
        },
        
        "original_decision": {
          "type": "object",
          "schema": {
            "score": "float",
            "decision_type": "string",
            "confidence": "float [0-1]",
            "assessment_date": "ISO8601"
          }
        },
        
        "override_details": {
          "type": "object",
          "schema": {
            "override_type": {
              "type": "enum",
              "values": [
                "STUDENT_APPEAL_APPROVED",
                "STUDENT_APPEAL_DENIED",
                "MENTOR_OVERRIDE_APPLIED",
                "EVALUATOR_ERROR_CORRECTION",
                "FAIRNESS_VIOLATION_REMEDIATION",
                "LEGAL_ORDER_COMPLIANCE",
                "DATA_ERROR_CORRECTION",
                "SYSTEMIC_BIAS_REMEDY"
              ]
            },
            "new_score": { 
              "type": "float or null",
              "description": "Overridden score (null if reverting)"
            },
            "new_decision": { 
              "type": "string or null",
              "description": "Overridden decision"
            },
            "change_magnitude": { 
              "type": "float",
              "description": "Absolute change in score (for impact analysis)"
            },
            "rationale": { 
              "type": "string",
              "description": "Detailed justification for override"
            },
            "error_found": { 
              "type": "string or null",
              "description": "If error correction: what was the error?"
            },
            "fairness_issue_identified": { 
              "type": "string or null",
              "description": "If fairness remedy: what was the bias?"
            }
          }
        },
        
        "severity_classification": {
          "type": "enum",
          "values": [
            "MINOR_ADJUSTMENT",
            "MODERATE_CHANGE",
            "MAJOR_CORRECTION",
            "CRITICAL_REMEDIATION"
          ],
          "description": "Impact level of override"
        },
        
        "evidence_summary": {
          "type": "array[object]",
          "description": "Evidence cited for decision",
          "items": {
            "evidence_type": "enum",
            "reference": "string",
            "weight": "float [0-1]",
            "description": "string"
          }
        }
      }
    },
    
    "authorization_path": {
      "type": "object",
      "description": "Who approved this override (authorization chain)",
      "schema": {
        "requestor": {
          "type": "object",
          "properties": {
            "requestor_id": "UUID (hashed)",
            "requestor_role": "enum",
            "authorization_level": "enum",
            "timestamp": "ISO8601"
          }
        },
        
        "approvers": {
          "type": "array[object]",
          "description": "Chain of command who approved/reviewed",
          "items": {
            "approver_id": "UUID (hashed)",
            "approver_role": "enum[MENTOR|EVALUATOR|COMPLIANCE_OFFICER|LEGAL_OFFICER|PLATFORM_ADMIN]",
            "decision": "enum[APPROVED|DENIED|ESCALATED]",
            "timestamp": "ISO8601",
            "justification": "string",
            "signature": "HMAC_SHA256(decision + salt)"
          },
          "min_items": 1
        },
        
        "required_approvals_met": {
          "type": "boolean",
          "description": "Did override meet all approval requirements?"
        },
        
        "approval_chain_complete": {
          "type": "boolean",
          "description": "Was full authorization chain followed?"
        }
      }
    },
    
    "fairness_impact_analysis": {
      "type": "object",
      "description": "Equity implications of override",
      "schema": {
        "affected_users": {
          "type": "integer",
          "description": "How many users affected by same issue?"
        },
        "protected_attributes_affected": {
          "type": "array[string]",
          "examples": ["gender", "socioeconomic_status", "language", "location"],
          "description": "Which groups disproportionately affected?"
        },
        "systemic_issue_detected": {
          "type": "boolean",
          "description": "Is this individual case or pattern?"
        },
        "remediation_required": {
          "type": "boolean",
          "description": "Should other students be remediated?"
        },
        "remediation_plan": {
          "type": "string or null",
          "description": "If systemic: how will other affected students be helped?"
        },
        "similar_cases_reviewed": {
          "type": "integer",
          "description": "How many similar cases were examined?"
        },
        "similar_cases_remediated": {
          "type": "integer",
          "description": "How many other students remediated?"
        },
        "fairness_equity_score": {
          "type": "float [0-1]",
          "description": "Does remedy create equity or perpetuate unfairness?"
        }
      }
    },
    
    "impact_assessment": {
      "type": "object",
      "description": "Consequences of this override",
      "schema": {
        "impacts_skill_recommendations": {
          "type": "boolean",
          "description": "Does this change skill recommendations?"
        },
        "impacts_career_path": {
          "type": "boolean",
          "description": "Does this change career recommendations?"
        },
        "impacts_ranking": {
          "type": "boolean",
          "description": "Does this change peer rankings?"
        },
        "impacts_xp_points": {
          "type": "boolean",
          "description": "Does this change growth metrics?"
        },
        "downstream_decisions_affected": {
          "type": "array[string]",
          "description": "Cascade of decisions affected"
        },
        "rollback_possible": {
          "type": "boolean",
          "description": "Can override be safely reversed?"
        },
        "rollback_procedure": {
          "type": "string or null",
          "description": "If reversible: how to undo?"
        }
      }
    },
    
    "audit_trail": {
      "type": "object",
      "immutable": true,
      "schema": {
        "decision_log": {
          "type": "array[object]",
          "description": "Step-by-step override decision process",
          "items": {
            "step": "integer",
            "decision_point": "string",
            "inputs": "object",
            "decision": "string",
            "reasoning": "string",
            "timestamp": "ISO8601"
          }
        },
        
        "compliance_checks": {
          "type": "array[object]",
          "description": "Regulatory frameworks verified",
          "items": {
            "framework": "string",
            "check": "string",
            "result": "enum[PASS|FAIL|N/A]",
            "timestamp": "ISO8601"
          },
          "example": [
            { "framework": "GDPR_Right_to_Appeal", "check": "Appeal processed within SLA" },
            { "framework": "Fair_ML_Framework", "check": "Override doesn't create new bias" }
          ]
        },
        
        "conflict_of_interest_check": {
          "type": "object",
          "schema": {
            "requestor_conflict": "boolean",
            "approver_conflict": "boolean",
            "conflict_description": "string or null"
          }
        },
        
        "reversibility_analysis": {
          "type": "object",
          "schema": {
            "can_be_reversed": "boolean",
            "reversal_impact": "string",
            "data_preservation": "boolean (is original data preserved?)"
          }
        }
      }
    },
    
    "communication_plan": {
      "type": "object",
      "description": "How to inform affected parties",
      "schema": {
        "student_notification": {
          "type": "object",
          "properties": {
            "notify_student": "boolean",
            "notification_method": "enum[IN_APP|EMAIL|SMS|CERTIFIED_MAIL]",
            "message_template": "string",
            "deadline": "ISO8601"
          }
        },
        "parent_notification": {
          "type": "object",
          "properties": {
            "notify_parents": "boolean",
            "parents_contacted": "array[email]",
            "notification_date": "ISO8601"
          }
        },
        "mentor_notification": {
          "type": "object",
          "properties": {
            "notify_mentor": "boolean",
            "mentor_contacted": "UUID (hashed)"
          }
        },
        "legal_notification": {
          "type": "object",
          "properties": {
            "legal_team_informed": "boolean",
            "legal_review_required": "boolean"
          }
        }
      }
    },
    
    "next_trigger_event": {
      "type": "array[object]",
      "description": "Events to trigger in downstream systems",
      "items": {
        "event_type": "enum[OVERRIDE_APPROVED | OVERRIDE_DENIED | REMEDIATION_REQUIRED | LEGAL_REVIEW_NEEDED | STUDENT_NOTIFIED]",
        "event_payload": "object",
        "target_agent": "string",
        "priority": "enum[CRITICAL | HIGH | NORMAL]"
      }
    },
    
    "summary_report": {
      "type": "object",
      "human_readable": true,
      "schema": {
        "decision_summary": "string (< 500 chars, executive summary)",
        "key_evidence": "array[string]",
        "approval_chain": "string",
        "fairness_impact": "string",
        "next_action_required": "string or null",
        "follow_up_date": "ISO8601 or null"
      }
    }
  }
}
```

---

## 5️⃣ AUTHORIZATION MATRIX (LOCKED - DETERMINISTIC ROLE-BASED)

### Who Can Override What (IMMUTABLE RULES)

```
OVERRIDE_AUTHORITY_MATRIX:

┌─────────────────────┬──────────┬──────────┬──────────┬──────────┬──────────┐
│ Override Type       │ Student  │ Mentor   │ Evalutr  │ Complnc  │ Platform │
├─────────────────────┼──────────┼──────────┼──────────┼──────────┼──────────┤
│ STUDENT_APPEAL      │ Request  │ Approve* │ Escalate │ Review   │ N/A      │
│ MENTOR_OVERRIDE     │ Appeal   │ Execute* │ Escalate │ Audit    │ N/A      │
│ DATA_ERROR_CORRECT  │ Report   │ Request  │ Execute* │ Audit    │ N/A      │
│ FAIRNESS_REMEDIATE  │ N/A      │ Request  │ Request  │ Execute* │ Audit    │
│ LEGAL_ORDER         │ N/A      │ N/A      │ N/A      │ Execute* │ Audit    │
│ SYSTEMIC_ISSUE_FIX  │ N/A      │ Escalate │ Escalate │ Escalate │ Execute* │
└─────────────────────┴──────────┴──────────┴──────────┴──────────┴──────────┘

Legend:
  * = Final approval authority (can execute without escalation)
  Execute = Can implement override
  Approve = Can approve student appeal
  Request = Can request override
  Escalate = Must escalate to higher authority
  Audit = Can audit this override
  Review = Can review but cannot approve
  N/A = Not authorized

KEY RULES (LOCKED):
  ├─ No role can approve own override (self-approval FORBIDDEN)
  ├─ Student appeals must be reviewed by Mentor or Evaluator (not Compliance by default)
  ├─ Mentor overrides audited by Compliance Officer (after fact)
  ├─ Fairness remediation requires Compliance Officer approval (before implementation)
  ├─ Legal orders must be verified by Legal Officer + Compliance Officer
  └─ Systemic issues require Platform Admin + Compliance Officer joint approval
```

### Approval Requirements by Override Type (LOCKED)

```
APPROVAL_REQUIREMENTS:

1. STUDENT_APPEAL
   ├─ Requestor: Student (hashed, with evidence)
   ├─ Required Approvers: 
   │  └─ Mentor OR Evaluator (1 of 2 required)
   ├─ Optional Review: Compliance Officer (audit only)
   ├─ Timeline: 10 business days (GDPR right to appeal)
   └─ Documentation: Evidence must be substantial (not emotional)

2. MENTOR_OVERRIDE
   ├─ Requestor: Mentor (with justification)
   ├─ Required Approvers: 
   │  └─ Mentor self-approval (auto-approved if properly authorized)
   ├─ Post-Execution Review: Compliance Officer (audit within 5 days)
   ├─ Timeline: Immediate (mentor judgment trusted)
   └─ Limitation: Cannot override > 2x per student per term

3. DATA_ERROR_CORRECTION
   ├─ Requestor: Evaluator OR Data Quality Agent (with evidence of corruption)
   ├─ Required Approvers:
   │  └─ Evaluator (1 of 2 required)
   ├─ Verification: Compliance Officer verifies error exists
   ├─ Timeline: 3 days (error correction urgent)
   └─ Scope: Can only correct to "true" value, not new arbitrary value

4. FAIRNESS_VIOLATION_REMEDIATION
   ├─ Requestor: Compliance Officer OR Bias Detection Agent (with analysis)
   ├─ Required Approvers:
   │  └─ Compliance Officer (mandatory)
   │  └─ Legal Officer (if involves protected attribute)
   ├─ Systemic Impact: Must remediate all affected users (not just one)
   ├─ Timeline: 10 days (regulatory requirement)
   └─ Equity Check: Must not create new unfairness

5. LEGAL_ORDER
   ├─ Requestor: Legal Officer OR Court Order (with legal reference)
   ├─ Required Approvers:
   │  ├─ Legal Officer (mandatory)
   │  └─ Compliance Officer (mandatory)
   ├─ No Further Appeals: Legal order is final (cannot be overridden again)
   ├─ Timeline: Immediate (legal requirement)
   └─ Documentation: Court order number, judge signature

6. SYSTEMIC_ISSUE_REMEDIATION
   ├─ Requestor: Platform Admin OR Compliance Officer (with analysis)
   ├─ Required Approvers:
   │  ├─ Compliance Officer (mandatory)
   │  ├─ Legal Officer (if regulatory violation)
   │  └─ Platform Admin (mandatory)
   ├─ Scope: Affects entire cohort/domain (> 50 students)
   ├─ Timeline: 15 days (requires thorough planning)
   └─ Rollback Plan: Must include reversal procedure
```

---

## 6️⃣ AUTHORIZATION LOGIC (DETERMINISTIC - LOCKED)

### Request Processing Workflow

```
Override Request Received
  │
  ├─→ [AUTHENTICATION CHECK]
  │   ├─ JWT token valid?
  │   ├─ Token not expired?
  │   ├─ Signature matches?
  │   └─ Role claim matches requestor_role?
  │   ↓ REJECT if any fails
  │
  ├─→ [AUTHORIZATION CHECK]
  │   ├─ Is requestor's role authorized for this override type?
  │   ├─ Does requestor have necessary permissions?
  │   ├─ Is requestor in same tenant as student?
  │   ├─ Is there conflict of interest? (self-approval check)
  │   └─ Has requestor hit rate limits? (abuse detection)
  │   ↓ REJECT if any fails
  │
  ├─→ [EVIDENCE VALIDATION]
  │   ├─ Evidence provided?
  │   ├─ Evidence is substantial (not hearsay)?
  │   ├─ Evidence supports the claim?
  │   └─ No fabricated or manipulated evidence?
  │   ↓ REJECT or request more evidence
  │
  ├─→ [REGULATORY COMPLIANCE CHECK]
  │   ├─ GDPR Right to Appeal? Process within 10 days
  │   ├─ Fair ML violation? Analyze fairness impact
  │   ├─ Legal order? Verify with legal team
  │   └─ Data protection? Ensure no PII exposure
  │   ↓ ESCALATE if regulatory issues
  │
  ├─→ [FAIRNESS IMPACT ANALYSIS]
  │   ├─ Is this individual or systemic issue?
  │   ├─ Who is affected? (count & demographic analysis)
  │   ├─ Does remedy create new bias?
  │   ├─ Will other students in same situation be treated equally?
  │   └─ Is this equitable?
  │   ↓ FLAG if fairness concern
  │
  ├─→ [APPROVAL ROUTING]
  │   ├─ Determine required approvers based on override type
  │   ├─ Route to appropriate authority
  │   ├─ Wait for authorization signatures
  │   ├─ Verify all required approvals obtained
  │   └─ Confirm no self-approval in chain
  │   ↓ ESCALATE if approval missing
  │
  ├─→ [FINAL OVERRIDE DECISION]
  │   ├─ IF all checks passed → APPROVE override
  │   ├─ IF minor issues → APPROVE_WITH_CONDITIONS
  │   ├─ IF significant issues → ESCALATE_FOR_REVIEW
  │   ├─ IF insufficient evidence → DENY
  │   └─ Log all decision points
  │   ↓
  │
  └─→ [EXECUTION & NOTIFICATION]
      ├─ Apply override to system
      ├─ Create immutable audit record
      ├─ Notify student/parents/mentor
      ├─ Trigger cascading updates (skills, career, ranking)
      ├─ Monitor for impact (fairness metrics)
      └─ Archive with full history

APPROVAL DECISION LOGIC (IF STATEMENTS):

IF requestor_role == STUDENT THEN
  IF override_type == STUDENT_APPEAL THEN
    → Route to MENTOR or EVALUATOR for approval
    → Evaluate evidence quality
    → Timeline: 10 days (GDPR)
  ELSE
    → REJECT (students cannot request other override types)
  ENDIF
ENDIF

IF requestor_role == MENTOR THEN
  IF override_type == STUDENT_APPEAL THEN
    → APPROVE (mentor has authority over own students)
    → Auto-approve if evidence sufficient
  ELIF override_type == MENTOR_OVERRIDE THEN
    → APPROVE (mentor override, auto-authorized)
    → Compliance audit scheduled (5 days post-action)
  ELIF override_type == DATA_ERROR_CORRECTION THEN
    → REQUEST escalation to Evaluator
  ELSE
    → REJECT (mentor cannot approve other types)
  ENDIF
ENDIF

IF requestor_role == EVALUATOR THEN
  IF override_type == STUDENT_APPEAL THEN
    → APPROVE (evaluator has authority)
  ELIF override_type == DATA_ERROR_CORRECTION THEN
    → APPROVE (evaluator can correct errors)
  ELIF override_type == FAIRNESS_VIOLATION THEN
    → ESCALATE to Compliance Officer (not evaluator authority)
  ELSE
    → REJECT
  ENDIF
ENDIF

IF requestor_role == COMPLIANCE_OFFICER THEN
  IF override_type IN [FAIRNESS_VIOLATION, LEGAL_ORDER, SYSTEMIC_ISSUE] THEN
    → APPROVE (compliance officer has highest authority)
    → Verify with Legal Officer if protected attributes involved
  ELIF override_type == STUDENT_APPEAL THEN
    → Review/audit only (not primary approver)
  ELSE
    → Can approve anything (but escalate if unusual)
  ENDIF
ENDIF

IF requestor_role == PLATFORM_ADMIN THEN
  IF override_type == SYSTEMIC_ISSUE_REMEDIATION THEN
    → APPROVE (final authority for systemic issues)
    → Require Compliance Officer co-approval
  ELSE
    → Can approve anything (highest privilege)
    → But must follow approval chain for fairness
  ENDIF
ENDIF
```

---

## 7️⃣ OVERRIDE EXECUTION (LOCKED)

### Once Approved: Apply Override

```
EXECUTION_PROCESS (DETERMINISTIC, IMMUTABLE):

1. PREPARATION
   ├─ Verify all approvals obtained
   ├─ Create backup of original decision
   ├─ Prepare immutable audit trail
   ├─ Notify relevant agents of upcoming change
   └─ Final confirmation from primary approver

2. SCORE/DECISION MODIFICATION
   ├─ Retrieve original assessment data
   ├─ Calculate new score (if numeric override)
   ├─ Verify new score in valid range
   ├─ Create versioned record:
   │  ├─ Version 1.0: Original decision
   │  ├─ Version 2.0: Overridden decision (marked as such)
   │  └─ Maintain full lineage
   └─ Never delete original (append-only principle)

3. CASCADING UPDATES
   ├─ Update SKILL_RECOMMENDATION_ENGINE
   │  └─ Recalculate skill recommendations based on new score
   ├─ Update CAREER_RECOMMENDATION_ENGINE
   │  └─ Recalculate career paths based on new score
   ├─ Update RANKING_ENGINE
   │  └─ Recalculate percentile (may affect peer rankings)
   ├─ Update GROWTH_ENGINE (XP, badges)
   │  └─ Adjust XP/points if score change significant
   └─ Notify NOTIFICATION_AGENT of changes

4. FAIRNESS REMEDIATION (IF SYSTEMIC)
   ├─ Identify all students affected by same issue
   ├─ Apply same override to all affected students
   ├─ Create equity audit trail showing:
   │  ├─ How many students remediated
   │  ├─ What override applied to each
   │  └─ Verification that all affected equally
   └─ Report to Compliance Officer

5. IMMUTABLE AUDIT LOGGING
   ├─ Create OVERRIDE_EXECUTION_LOG with:
   │  ├─ Original decision (immutable copy)
   │  ├─ Override decision (immutable copy)
   │  ├─ Approval chain (signatures)
   │  ├─ Execution timestamp
   │  ├─ Changes applied (diff view)
   │  ├─ Cascading effects (what else changed)
   │  └─ Reversibility information
   ├─ Sign with HMAC-SHA256
   ├─ Store in append-only database
   └─ Create blockchain-style hash chain

6. NOTIFICATIONS
   ├─ Notify STUDENT
   │  └─ "Your assessment has been reviewed and updated"
   │  └─ Explain why (non-technical language)
   │  └─ Show old vs new score
   │  └─ Provide next steps
   ├─ Notify PARENTS (if applicable)
   │  └─ Formal notification of override
   │  └─ Explanation of decision
   ├─ Notify MENTOR
   │  └─ Override decision and rationale
   ├─ Notify COMPLIANCE_OFFICER (post-action audit)
   │  └─ Override log for audit trail
   └─ Send all notifications with audit reference

7. REVERSAL PREPARATION
   ├─ Document reversal procedure
   ├─ Create rollback scripts (if possible)
   ├─ Store original data (never delete)
   ├─ Make reversibility clear in audit trail
   └─ Prepare communication if override reversed

8. MONITORING & FOLLOW-UP
   ├─ Monitor cascading effects (did ranking change harm fairness?)
   ├─ Check if student satisfaction improved
   ├─ Verify override didn't create new issues
   ├─ Schedule compliance audit (within 30 days)
   └─ Track long-term fairness impact
```

---

## 8️⃣ FAILURE POLICY (LOCKED - NO SILENT FAILURES)

### Failure Scenarios & Handling

```
SCENARIO 1: UNAUTHORIZED_OVERRIDE_ATTEMPT
├─ Triggers: JWT invalid, role unauthorized, self-approval attempt
├─ Severity: CRITICAL (Security)
├─ Action:
│  1. LOG_INCIDENT (immediately, immutable)
│  2. BLOCK_REQUEST (403 Forbidden)
│  3. INITIATE_SECURITY_PROTOCOL
│  4. REVOKE_CREDENTIALS (if token compromise suspected)
│  5. PAGE_SECURITY_TEAM (emergency alert)
│  6. FLAG_REQUESTOR_ACCOUNT (potential abuse)
├─ Alert: Security team (immediate)
├─ SLA: Investigate within 30 minutes
└─ Escalation: Chief Security Officer (if breach confirmed)

SCENARIO 2: INSUFFICIENT_EVIDENCE_FOR_OVERRIDE
├─ Triggers: Evidence missing, evidence is hearsay, no substantiation
├─ Severity: MEDIUM
├─ Action:
│  1. LOG_INCIDENT (override request with weak evidence)
│  2. REQUEST_MORE_EVIDENCE (return to requestor)
│  3. PROVIDE_EVIDENCE_CHECKLIST (help requestor strengthen case)
│  4. ESCALATE_TO_MENTOR (if student appeal, mentor reviews)
│  5. SET_DEADLINE (10 days to provide additional evidence)
├─ Escalation: Mentor/Evaluator (review decision)
├─ SLA: Requestor has 10 days to strengthen case
└─ Final: If no evidence, deny override (can reapply later)

SCENARIO 3: APPROVAL_CHAIN_BROKEN
├─ Triggers: Required approver unavailable, approval never obtained, approver changes role
├─ Severity: HIGH
├─ Action:
│  1. LOG_INCIDENT (approval chain incomplete)
│  2. HALT_EXECUTION (do not apply override yet)
│  3. REASSIGN_APPROVAL (to available authority)
│  4. NOTIFY_REQUESTOR (status update)
│  5. EXTEND_TIMELINE (add 5 days due to delay)
├─ Escalation: Next authority in chain
├─ SLA: Restart approval with new authority
└─ Retry: Automatic retry after reassignment

SCENARIO 4: CONFLICT_OF_INTEREST_DETECTED
├─ Triggers: Requestor has personal stake, self-approval, approver is related to student
├─ Severity: CRITICAL
├─ Action:
│  1. LOG_INCIDENT (conflict of interest detected)
│  2. BLOCK_APPROVAL (cannot proceed as requested)
│  3. REASSIGN_TO_NEUTRAL_AUTHORITY (different person)
│  4. FLAG_FOR_COMPLIANCE_REVIEW (audit fairness of process)
│  5. NOTIFY_ALL_PARTIES (transparency)
├─ Alert: Compliance Officer (review immediately)
├─ SLA: Reassign within 24 hours
└─ Escalation: If conflict severe, escalate to Legal

SCENARIO 5: FAIRNESS_VIOLATION_IN_REMEDY
├─ Triggers: Override creates new bias, doesn't apply to all affected equally
├─ Severity: HIGH
├─ Action:
│  1. LOG_INCIDENT (fairness issue in override)
│  2. HALT_EXECUTION (do not apply discriminatory override)
│  3. RETURN_FOR_FAIRNESS_REVIEW (cannot be inequitable)
│  4. REQUIRE_SYSTEMIC_REMEDY (if other students affected)
│  5. ESCALATE_TO_COMPLIANCE (ensure equitable solution)
├─ Alert: Compliance Officer + Legal (same day)
├─ SLA: Compliance Officer revises remedy within 5 days
└─ Principle: Override must improve fairness, not degrade it

SCENARIO 6: SYSTEMIC_ISSUE_NOT_REMEDIATED
├─ Triggers: Override approved for one student, but > 50 students have same issue
├─ Severity: CRITICAL (Fairness violation)
├─ Action:
│  1. LOG_INCIDENT (systemic issue identified)
│  2. BLOCK_SINGLE_OVERRIDE (cannot remediate one person unfairly)
│  3. ESCALATE_TO_SYSTEMIC_REMEDIATION (affect entire cohort)
│  4. REQUIRE_FAIRNESS_IMPACT_ANALYSIS (how many affected?)
│  5. MANDATE_EQUITABLE_REMEDY (all similar cases fixed)
├─ Alert: Compliance Officer + Legal (immediate)
├─ SLA: Systemic remediation plan within 10 days
└─ Principle: No selective fairness (treat like cases alike)

SCENARIO 7: LEGAL_ORDER_NOT_VERIFIED
├─ Triggers: Legal override claimed but no valid court order, forged document
├─ Severity: CRITICAL (Legal/Fraud)
├─ Action:
│  1. LOG_INCIDENT (unverified legal order)
│  2. BLOCK_OVERRIDE (cannot apply without verification)
│  3. CONTACT_LEGAL_TEAM (verify authenticity immediately)
│  4. INITIATE_FRAUD_INVESTIGATION (if forgery suspected)
│  5. PAGE_SECURITY_TEAM (potential malicious activity)
├─ Alert: Legal team + Security (immediate)
├─ SLA: Verify within 4 hours
└─ Escalation: Police report if fraud confirmed

SCENARIO 8: DATABASE_CORRUPTION_DURING_OVERRIDE
├─ Triggers: Cannot write new score, audit trail corrupted, data loss during execution
├─ Severity: CRITICAL
├─ Action:
│  1. LOG_INCIDENT_TO_LOCAL_BUFFER (in-memory queue)
│  2. ROLLBACK_OVERRIDE (revert any partial changes)
│  3. HALT_ALL_OPERATIONS (do not apply any more overrides)
│  4. RESTORE_FROM_BACKUP (recover data from immutable storage)
│  5. PAGE_DBA (emergency alert)
├─ Alert: DBA + Security (immediate)
├─ SLA: Restore within 1 hour
└─ Verification: Confirm override successful before notifying user

SCENARIO 9: OVERRIDE_APPEAL_ABUSE_DETECTED
├─ Triggers: Same student appeals > 3x per term, appeals never substantiated
├─ Severity: MEDIUM
├─ Action:
│  1. LOG_INCIDENT (pattern of frivolous appeals)
│  2. FLAG_REQUESTOR_ACCOUNT (mark as potential abuser)
│  3. ESCALATE_APPROVALS (require higher authority review)
│  4. REQUIRE_STRONGER_EVIDENCE (higher threshold for future appeals)
│  5. NOTIFY_STUDENT (explain appeal threshold policy)
├─ Alert: Mentor/Evaluator (informational)
├─ SLA: Continue processing but with stricter standards
└─ Escalation: If pattern continues, restrict appeals

SCENARIO 10: REGULATORY_VIOLATION_IN_OVERRIDE
├─ Triggers: Override violates GDPR, Fair ML, other legal requirement
├─ Severity: CRITICAL
├─ Action:
│  1. LOG_INCIDENT (regulatory violation)
│  2. BLOCK_OVERRIDE (cannot violate law)
│  3. ESCALATE_TO_LEGAL_OFFICER (immediately)
│  4. NOTIFY_COMPLIANCE_OFFICER (audit implications)
│  5. INITIATE_CORRECTIVE_ACTION (fix legal issue)
├─ Alert: Legal + Compliance (immediate)
├─ SLA: Legal review within 24 hours
└─ Documentation: Root cause analysis required

└──────────────────────────────────────────────────────────────┘
```

### Global Failure Rules (NON-NEGOTIABLE)

```
RULE 1: NO_SILENT_FAILURES
  └─ Every failure logged, flagged, escalated
  └─ No error suppression or hidden failures

RULE 2: FAIL_SECURE
  └─ When in doubt, DENY the override
  └─ Better to reject valid override than allow invalid one

RULE 3: AUDIT_EVERYTHING
  └─ Log every override attempt (approved or denied)
  └─ Immutable audit trail for compliance

RULE 4: NEVER_ALLOW_SELF_APPROVAL
  └─ Conflict of interest kills override
  └─ Requires independent authority

RULE 5: EQUAL_TREATMENT_MANDATORY
  └─ If one student remediated, all similar cases must be
  └─ No selective fairness

RULE 6: REVERSIBILITY_REQUIRED
  └─ Override must be reversible (original data preserved)
  └─ Document reversal procedure before implementation

RULE 7: NO_RETROACTIVE_DELETION
  └─ Original decision preserved (immutable)
  └─ Override added as new version
  └─ Full lineage maintained
```

---

## 9️⃣ SECURITY ENFORCEMENT (LOCKED)

### Tenant Isolation (HARD REQUIREMENT)

```
ISOLATION_LAYER:
  ├─ Data Isolation:
  │  └─ Query filter: WHERE tenant_id = context.tenant_id (always)
  │  └─ No cross-tenant override requests
  │  └─ Cannot override decisions from different tenant
  │
  ├─ API Isolation:
  │  └─ Authentication: JWT with tenant_id claim
  │  └─ Authorization: Role-based access control (RBAC)
  │  └─ Request validation: tenant_id from token must match payload
  │
  ├─ Compute Isolation:
  │  └─ Worker pod affinity: Dedicated pools per tenant (large tenants)
  │  └─ Resource quotas: CPU, memory, request limits per tenant
  │  └─ Fair queuing: No tenant can starve others
  │
  ├─ Audit Isolation:
  │  └─ Override logs partitioned by tenant_id
  │  └─ Compliance officer can only audit own tenant
  │  └─ No cross-tenant audit visibility
```

### Role-Based Access Control (RBAC)

```
ROLE_PERMISSIONS:

STUDENT
  ├─ Can request student appeal (own assessment)
  ├─ Cannot approve any override
  ├─ Cannot override others
  └─ Can view appeal status

MENTOR
  ├─ Can approve student appeals (of mentees)
  ├─ Can execute mentor override
  ├─ Cannot override other mentors' students
  └─ Subject to 2x per student per term limit

EVALUATOR
  ├─ Can approve student appeals
  ├─ Can correct data errors
  ├─ Cannot approve fairness overrides
  └─ Subject to audit

COMPLIANCE_OFFICER
  ├─ Can approve fairness/legal overrides
  ├─ Can audit all overrides
  ├─ Can execute systemic remediation
  ├─ Can restrict abusive appeal patterns
  └─ Highest authority (except legal orders)

LEGAL_OFFICER
  ├─ Can process legal orders
  ├─ Can verify court documents
  ├─ Can advise on regulatory compliance
  └─ Required for overrides involving protected attributes

PLATFORM_ADMIN
  ├─ Can execute systemic issue remediation
  ├─ Can override override decisions (extreme cases only)
  ├─ Can audit entire platform
  ├─ Cannot self-approve (must involve Compliance)
  └─ Highest privilege, highest accountability
```

### Encryption & Hashing

```
DATA_AT_REST:
  ├─ Override requests: AES-256-GCM
  ├─ Override decisions: AES-256-GCM
  ├─ User IDs: SHA256(user_id + tenant_salt)
  ├─ Approval signatures: HMAC-SHA256
  └─ Key rotation: Every 90 days

DATA_IN_TRANSIT:
  ├─ TLS 1.3 mandatory (all connections)
  ├─ Message signing: HMAC-SHA256
  ├─ Certificate pinning (service-to-service)
  └─ No unencrypted channels

KEY_MANAGEMENT:
  ├─ Keys in HSM (Hardware Security Module)
  ├─ Access: RBAC (minimal privilege)
  ├─ Audit: All key usage logged
  ├─ Rotation: Every 90 days
  └─ Emergency: 4-hour rotation if breach detected
```

### Audit Logging (APPEND-ONLY, IMMUTABLE)

```
AUDIT_LOG_SCHEMA:
{
  timestamp_utc,
  actor_id (hashed),
  actor_role,
  action (enum: REQUEST | APPROVE | DENY | EXECUTE | REVERSE),
  override_id,
  decision (APPROVED | DENIED | ESCALATED),
  reason,
  tenant_id,
  ip_address (masked),
  signature (HMAC_SHA256)
}

STORAGE:
  ├─ Primary: PostgreSQL (append-only with WAL)
  ├─ Backup: S3 immutable (versioning + MFA delete)
  ├─ Retention: 7 years minimum (GDPR + regulatory)
  ├─ Tamper detection: Weekly hash verification
  └─ Integrity: Blockchain-style hash chaining
```

---

## 🔟 INTER-AGENT DEPENDENCY MAP (LOCKED)

### Upstream Agents

```
STUDENT_APPEAL_SYSTEM
├─ Provides: appeal requests from students
├─ Contract: Structured appeal with evidence
├─ Frequency: Per appeal submission
└─ Version: APPEAL_SYSTEM_v1.0.0+

INTELLIGENCE_ASSESSMENT_ENGINE
├─ Provides: original decision details, scores, confidence
├─ Contract: Complete decision context
├─ Frequency: Per override request
└─ Version: ASSESSMENT_ENGINE_v2.1.0+

BIAS_DETECTION_AGENT
├─ Provides: fairness violations to override
├─ Contract: Bias analysis with evidence
├─ Frequency: When bias detected
└─ Version: BIAS_DETECTION_v1.0.0+

DATA_QUALITY_AGENT
├─ Provides: data error reports
├─ Contract: Evidence of corruption
├─ Frequency: Per error detection
└─ Version: DATA_QUALITY_v1.2.0+

LEGAL_COMPLIANCE_SYSTEM
├─ Provides: legal orders, regulatory requirements
├─ Contract: Court orders, compliance mandates
├─ Frequency: As needed
└─ Version: LEGAL_v1.0.0+
```

### Downstream Agents

```
RESULT_REMEDIATION_AGENT
├─ Consumes: Approved overrides
├─ Function: Apply score/decision changes
├─ Contract: Expects override with approvals
├─ Response: Confirmation of change applied
└─ Version: REMEDIATION_v1.2.0+

EQUITY_ADJUSTMENT_AGENT
├─ Consumes: Systemic override decisions
├─ Function: Apply fairness remediation to cohort
├─ Contract: Systemic override details
├─ Response: Remediation completion report
└─ Version: EQUITY_v1.1.0+

AUDIT_TRAIL_AGENT
├─ Consumes: All override decisions & executions
├─ Function: Immutable logging
├─ Contract: Structured override log
├─ Response: Audit reference UUID
└─ Version: AUDIT_v1.9.0+

STUDENT_NOTIFICATION_AGENT
├─ Consumes: Override decisions (approved/denied)
├─ Function: Notify student of appeal decision
├─ Contract: Notification with explanation
├─ Response: Confirmation of delivery
└─ Version: NOTIFICATION_v1.3.0+

COMPLIANCE_OFFICER_ESCALATION
├─ Consumes: Overrides requiring review
├─ Function: Human review and approval
├─ Contract: Complete override request
├─ Response: Approval/denial decision
└─ Version: COMPLIANCE_v2.0.0+

OBSERVABILITY_AGENT
├─ Consumes: Override metrics
├─ Function: Monitor override trends, abuse
├─ Contract: Metrics (frequency, patterns)
├─ Response: Alerts if anomalies detected
└─ Version: OBSERVABILITY_v3.1.0+
```

---

## 1️⃣1️⃣ PERFORMANCE MONITORING (LOCKED)

### Metrics Published

```yaml
METRICS_PUBLISHED:
  
  ├─ OVERRIDE_REQUEST_METRICS:
  │  ├─ override_requests_total (counter, by type)
  │  ├─ override_requests_approved (%)
  │  ├─ override_requests_denied (%)
  │  ├─ average_processing_time (days)
  │  ├─ gdpr_appeal_sla_met (%)
  │  └─ appeal_cycle_time_p95 (percentile)
  │
  ├─ AUTHORIZATION_METRICS:
  │  ├─ unauthorized_attempts (counter)
  │  ├─ approval_chain_failures (counter)
  │  ├─ conflict_of_interest_detected (counter)
  │  ├─ self_approval_attempts_blocked (counter)
  │  └─ approval_time_per_level (histogram)
  │
  ├─ FAIRNESS_METRICS:
  │  ├─ systemic_issues_detected (counter)
  │  ├─ students_remediated_for_fairness (counter)
  │  ├─ unfair_overrides_blocked (counter)
  │  ├─ equity_adjusted_cohort_size (count)
  │  └─ fairness_violation_rate (%)
  │
  ├─ ABUSE_DETECTION_METRICS:
  │  ├─ serial_appealers (count)
  │  ├─ serial_granters (count)
  │  ├─ override_frequency_by_mentor (histogram)
  │  ├─ appeal_resubmissions (after denial)
  │  └─ frivolous_appeal_rate (%)
  │
  ├─ SECURITY_METRICS:
  │  ├─ unauthorized_override_attempts (counter)
  │  ├─ data_corruption_incidents (counter)
  │  ├─ cross_tenant_breach_attempts (counter)
  │  ├─ encryption_key_incidents (counter)
  │  └─ audit_log_integrity_failures (counter)
  │
  └─ SYSTEM_HEALTH_METRICS:
     ├─ request_throughput_rps
     ├─ approval_latency_p95
     ├─ system_availability (%)
     ├─ queue_depth (pending requests)
     └─ worker_pod_count

SLA_TARGETS:
  ├─ Student appeals: 10 days (GDPR requirement)
  ├─ Mentor overrides: Immediate approval
  ├─ Data error corrections: 3 days
  ├─ Fairness remediation: 10 days (regulatory)
  ├─ System availability: 99.95% uptime
  └─ Unauthorized override block rate: 100% (zero breaches)
```

---

## 1️⃣2️⃣ VERSIONING POLICY (LOCKED)

### Add-Only, Immutable Versioning

```
VERSIONING_FORMAT: IOA_v{MAJOR}.{MINOR}.{PATCH}_{STATUS}

Changes Allowed (Add-Only):
  ○ New override types
  ○ Enhanced fairness checks
  ○ Improved approval workflows
  ○ Better evidence validation
  → Backward compatible
  → Previous versions retained

Changes Forbidden:
  ✗ Removing override types
  ✗ Lowering approval requirements
  ✗ Weakening fairness checks
  ✗ Modifying audit trails
  ✗ Changing tenant isolation
```

---

## 1️⃣3️⃣ NON-NEGOTIABLE RULES (ABSOLUTE - SEALED)

```
🔒 SEALED & LOCKED RULES - NO EXCEPTIONS

RULE 1: HUMANS GOVERN AI, NOT REVERSE
  ✗ FORBIDDEN: AI system overriding human decision
  ✗ FORBIDDEN: Override without human authorization
  ✓ REQUIRED: Every override requires explicit human approval
  ✓ REQUIRED: Human judgment is final authority

RULE 2: NO SELF-APPROVAL (CONFLICT OF INTEREST)
  ✗ FORBIDDEN: Requestor also approver
  ✗ FORBIDDEN: Person benefiting from override approving it
  ✗ FORBIDDEN: Mentor approving own override request
  ✓ REQUIRED: Independent authority must approve
  ✓ REQUIRED: Conflict of interest check mandatory

RULE 3: NO SILENT OVERRIDES
  ✗ FORBIDDEN: Overriding without audit trail
  ✗ FORBIDDEN: Hiding override from affected student
  ✗ FORBIDDEN: Deleting override record
  ✓ REQUIRED: Every override logged immutably
  ✓ REQUIRED: Student notified of outcome
  ✓ REQUIRED: Full history preserved

RULE 4: NO SELECTIVE FAIRNESS
  ✗ FORBIDDEN: Overriding for one student, not for similar cases
  ✗ FORBIDDEN: Creating inequity through override
  ✗ FORBIDDEN: Systemic issues fixed for some, not all
  ✓ REQUIRED: All similar cases treated equally
  ✓ REQUIRED: Fairness impact analyzed before execution
  ✓ REQUIRED: Systemic remediation mandatory

RULE 5: NO OVERRIDE OVERRIDE (WITHOUT PROCESS)
  ✗ FORBIDDEN: Reversing previous override arbitrarily
  ✗ FORBIDDEN: Overriding an override without justification
  ✗ FORBIDDEN: Hiding reversals from audit trail
  ✓ REQUIRED: Reversals follow same approval process
  ✓ REQUIRED: All reversals documented
  ✓ REQUIRED: Student notified of change

RULE 6: NO CIRCUMVENTING COMPLIANCE
  ✗ FORBIDDEN: Bypassing fairness checks
  ✗ FORBIDDEN: Skipping GDPR appeal SLA
  ✗ FORBIDDEN: Ignoring regulatory requirements
  ✓ REQUIRED: All compliance gates enforced
  ✓ REQUIRED: Regulatory SLAs met
  ✓ REQUIRED: Legal review when required

RULE 7: NO EXCEEDING AUTHORITY
  ✗ FORBIDDEN: Mentor overriding > 2x per student per term
  ✗ FORBIDDEN: Approver exceeding scope of role
  ✗ FORBIDDEN: Delegating authority beyond limits
  ✓ REQUIRED: Role-based authority limits enforced
  ✓ REQUIRED: Rate limits prevent abuse
  ✓ REQUIRED: Escalation required for exceptional cases

RULE 8: NO DESTROYING ORIGINAL DATA
  ✗ FORBIDDEN: Deleting original decision
  ✗ FORBIDDEN: Modifying assessment history
  ✗ FORBIDDEN: Losing ability to reverse override
  ✓ REQUIRED: Original data preserved immutably
  ✓ REQUIRED: Full version history maintained
  ✓ REQUIRED: Reversibility always possible

RULE 9: NO EVIDENCE-FREE OVERRIDES
  ✗ FORBIDDEN: Override without documented evidence
  ✗ FORBIDDEN: Accepting hearsay or speculation
  ✗ FORBIDDEN: Approval based on emotion, not facts
  ✓ REQUIRED: Substantial evidence required
  ✓ REQUIRED: Evidence reviewed before approval
  ✓ REQUIRED: All evidence preserved in audit trail

RULE 10: NO HIDING OVERRIDES FROM COMPLIANCE
  ✗ FORBIDDEN: Not reporting override to compliance officer
  ✗ FORBIDDEN: Preventing audit of override decision
  ✗ FORBIDDEN: Blocking legal review when required
  ✓ REQUIRED: All overrides auditable
  ✓ REQUIRED: Compliance officer can review any override
  ✓ REQUIRED: Legal review mandatory for protected attributes

🔐 THESE RULES ARE IMMUTABLE AND CANNOT BE OVERRIDDEN EXCEPT BY:
  - Court order (documented, signed, verified)
  - Emergency (threat to physical safety, imminent harm)
  - Systemic legal requirement change (regulation update)
  
  Even in these cases:
    → Deviation logged as "exception"
    → Legal review required within 24 hours
    → Audit trail includes justification
    → After crisis, revert to strict compliance
```

---

## APPENDIX A: EXAMPLE SCENARIOS

### Scenario 1: Student Appeals Low Linguistic Score

```
WORKFLOW:

Student submits appeal:
├─ Original score: 42 (BELOW AVERAGE)
├─ Student claim: "I prepared for 3 months, practiced speaking daily"
├─ Evidence: Screenshots of daily practice log, mentor emails confirming prep
├─ Requested change: Retake assessment (different assessor)

System processes:
├─ [VALIDATION] Evidence is substantial ✓
├─ [AUTHORIZATION] Student can request appeal ✓
├─ [FAIRNESS] Is this individual case? Yes (no systemic pattern) ✓
├─ [ROUTING] Route to Mentor for approval

Mentor reviews:
├─ Mentor knows student well
├─ Confirms student engagement visible
├─ Notices: Assessment on bad day? Possible extenuating circumstances?
├─ Decision: APPROVE student retake with different assessor

Execution:
├─ Original score: 42 (MARKED AS UNDER-APPEAL)
├─ New assessment: 71 (student retakes)
├─ Decision: OVERRIDE APPROVED
├─ Notification: Student sees new score, explanation, comparison
├─ Cascading: Skills/career recommendations updated

Audit trail:
├─ Original decision (42): Preserved, marked "under-appeal"
├─ Override request: Logged with evidence
├─ Mentor approval: Signed, timestamped
├─ New score (71): Applied with full version history
├─ Notification: Student informed, decision documented
└─ Outcome: Fair resolution, transparent process
```

### Scenario 2: Fairness Violation Detected

```
WORKFLOW:

Bias Detection Agent flags issue:
├─ Finding: Female students scoring 15% lower on spatial intelligence
├─ Evidence: Statistical analysis (p < 0.05, n=200)
├─ Cause: Assessment uses "building/construction" context (gender-coded)
├─ Affected: 47 female students in cohort
├─ Recommendation: Fairness remedy required

System escalates:
├─ [SEVERITY] MAJOR (systematic bias affecting protected group)
├─ [SCOPE] Systemic issue (> 50 students)
├─ [ROUTING] Escalate to Compliance Officer

Compliance Officer reviews:
├─ Examines bias analysis (sound methodology)
├─ Identifies: Assessment design flaw (gender stereotype)
├─ Decision: Override scores for all 47 affected students
├─ Remedy: Apply fairness correction (+15%) to affected students
├─ Verification: Ensure remedy doesn't create new unfairness

Approval chain:
├─ Compliance Officer: Approves systemic remedy
├─ Legal Officer: Reviews (protected attribute involved)
├─ Platform Admin: Co-approves (systemic change)

Execution:
├─ All 47 students' scores adjusted
├─ New scores applied uniformly (same correction)
├─ Equal treatment ensured (all similar cases fixed)
├─ Notification: Students informed of fairness correction
├─ Follow-up: Assessment design changed (prevent future bias)

Audit trail:
├─ Bias analysis report: Preserved
├─ Approval chain: All signatures logged
├─ Score changes: All tracked with justification
├─ Systemic scope: 47 students remediated
├─ Assessment design fix: Documented
└─ Outcome: Fairness restored, equity achieved
```

---

## 🔒 SEAL & LOCK CERTIFICATE

```
╔════════════════════════════════════════════════════════════════════╗
║          INTELLIGENCE_OVERRIDE_AGENT.md                             ║
║            SEAL & LOCK CERTIFICATE (IMMUTABLE)                      ║
╠════════════════════════════════════════════════════════════════════╣
║                                                                    ║
║  SPECIFICATION VERSION:     IOA_v1.0.0_SEALED                    ║
║  CREATED:                   2025-02-27T14:45:00Z                 ║
║  OWNER:                     ML & Safety Officer + Compliance      ║
║  COMPLIANCE FRAMEWORK:      GDPR Right to Appeal, Fair ML         ║
║                                                                    ║
║  SPECIFICATION HASH:        {SHA256_OF_ENTIRE_MD_FILE}            ║
║  HASH VERIFICATION:         IMMUTABLE (Re-hash to verify)          ║
║                                                                    ║
║  LOCKED COMPONENTS:                                               ║
║    ✓ Agent Identity          [SECTION 1]                          ║
║    ✓ Authorization Matrix    [SECTION 5]                          ║
║    ✓ Authorization Logic     [SECTION 6]                          ║
║    ✓ Non-Negotiable Rules    [SECTION 13]                         ║
║    ✓ Input/Output Contracts  [SECTIONS 3-4]                       ║
║    ✓ Failure Policy          [SECTION 8]                          ║
║                                                                    ║
║  CHANGES ALLOWED (Add-Only):                                      ║
║    ○ New override types (Section 5)                               ║
║    ○ Enhanced approval workflows (Section 6)                      ║
║    ○ Improved fairness checks (Section 7)                         ║
║    → All changes require version bump: v1.0.0 → v1.1.0            ║
║    → Previous version remains available (no deletion)             ║
║                                                                    ║
║  CHANGES FORBIDDEN:                                               ║
║    ✗ Removing approval requirements                               ║
║    ✗ Allowing self-approval                                       ║
║    ✗ Weakening fairness enforcement                               ║
║    ✗ Modifying audit trail structure                              ║
║    ✗ Violating non-negotiable rules                               ║
║                                                                    ║
║  QUARTERLY REVIEW SCHEDULE:                                       ║
║    Next review date: 2025-05-27                                   ║
║    Review agenda: Authority abuse, fairness outcomes, SLA met     ║
║    Reviewers: ML Owner, Safety Officer, Compliance Officer, Legal ║
║    Approval required: All four parties                             ║
║                                                                    ║
║  DEPLOYMENT AUTHORIZATION:                                        ║
║    Authorized by:  ML & Safety Officer + Compliance Officer       ║
║    Signature:      [DIGITAL_SIGNATURE_REQUIRED]                   ║
║    Timestamp:      2025-02-27T14:45:00Z                           ║
║    Deployment SLA: Production by 2025-03-15                       ║
║                                                                    ║
║  CRITICAL PRINCIPLE (IMMUTABLE):                                  ║
║    HUMANS GOVERN AI, NOT THE REVERSE                              ║
║    AI ADVISES, HUMANS DECIDE, HUMANS APPROVE                      ║
║    EVERY OVERRIDE REQUIRES EXPLICIT HUMAN AUTHORIZATION           ║
║                                                                    ║
║  TAMPER DETECTION:                                                ║
║    To verify this document has NOT been modified:                 ║
║    1. Copy entire .md file                                        ║
║    2. Run: sha256sum INTELLIGENCE_OVERRIDE_AGENT.md               ║
║    3. Compare hash with: {SHA256_OF_ENTIRE_MD_FILE}               ║
║    4. If match → Document is SEALED & UNMODIFIED ✓               ║
║    5. If mismatch → TAMPERING DETECTED ✗ (Alert security)         ║
║                                                                    ║
║  AUDIT TRAIL:                                                     ║
║    All overrides logged to: override-output topic (Kafka)         ║
║    Retention: 7 years (immutable, append-only)                    ║
║    Review by: Compliance Officer (monthly)                        ║
║                                                                    ║
╚════════════════════════════════════════════════════════════════════╝

This document is the SINGLE SOURCE OF TRUTH for the Intelligence
Override Agent. All implementations must conform to this 
specification. Any deviations require formal change control and 
version increment.

SIGNATURE: [ML_SAFETY_OFFICER_DIGITAL_SIGNATURE_HERE]
SIGNATURE: [COMPLIANCE_OFFICER_DIGITAL_SIGNATURE_HERE]
TIMESTAMP: 2025-02-27T14:45:00Z
STATUS: 🔒 SEALED & LOCKED - NO MODIFICATIONS ALLOWED
```

---

**END OF SPECIFICATION**

*This document is sealed, locked, and immutable. For changes or clarifications, contact the ML & Safety Officer and Compliance Officer (joint authority required).*
