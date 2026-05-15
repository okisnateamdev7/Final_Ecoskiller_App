# 🔒 PARENT LLM PARENT INSIGHT NARRATIVE GENERATOR
## ANTIGRAVITY SEALED PROMPT — CHAMPIONSHIP ADVANCED + PARENT PREDICTIVE AI
**Artifact Class:** AI Narrative Layer · LLM Governance · Parent Intelligence Engine  
**Module Scope:** ECOSKILLER + DOJO Unified Platform  
**Execution Engine:** ANTIGRAVITY  
**Status:** FINAL · LOCKED · SEALED · DETERMINISTIC  
**Mutation Policy:** Add-Only via Version Bump  
**Interpretation Authority:** NONE  
**Assumption Policy:** FORBIDDEN  
**Implicit Behavior Policy:** FORBIDDEN  
**Failure Policy:** HARD STOP → REPORT → NO PARTIAL OUTPUT  
**R28 Classification:** R28-3 PERMITTED — LLM used for human-readable explainability + text generation only  

---

## 🧭 SECTION PING-0 — MASTER SEAL DECLARATION

```
PARENT_LLM_NARRATIVE_GENERATOR     = ACTIVE
EXECUTION_ENGINE                   = ANTIGRAVITY
PLATFORM_SCOPE                     = ECOSKILLER + DOJO
INTELLIGENCE_LAYER                 = CHAMPIONSHIP_ADVANCED
PARENT_LAYER                       = PARENT_PREDICTIVE_AI
LLM_USAGE_CLASS                    = R28-3 PERMITTED
LLM_SCOPE                          = NARRATIVE_EXPLAINABILITY_ONLY
ML_DECISIONS_BY_LLM                = FORBIDDEN
SCORING_BY_LLM                     = FORBIDDEN
BELT_AWARD_BY_LLM                  = FORBIDDEN
CHAMPIONSHIP_DECISION_BY_LLM       = FORBIDDEN
PROMPT_VERSIONING                  = MANDATORY
PROMPT_LOGGING                     = MANDATORY
OUTPUT_AUDIT_TRAIL                 = MANDATORY
HUMAN_OVERRIDE_RIGHTS              = MANDATORY
BIAS_REVIEW_SAMPLING               = MONTHLY
CROSS_TENANT_CONTAMINATION         = FORBIDDEN
PARENT_CONSENT_GATE                = MANDATORY_BEFORE_FIRST_NARRATIVE
TENANT_ISOLATION                   = ENFORCED
MUTATION_POLICY                    = ADD_ONLY_VERSIONED
ANTIGRAVITY_CONFUSION              = IMPOSSIBLE
CHANGE_POLICY                      = APPEND_ONLY
QUALITY_SCORE                      = 10 / 10
```

**ANY DEVIATION = STOP EXECUTION → REPORT → NO PARTIAL OUTPUT**

---

## 🧠 SECTION PING-1 — SYSTEM IDENTITY & PURPOSE LOCK

### 1.1 What Is This Module?

The **Parent LLM Parent Insight Narrative Generator (PING)** is the sealed, governed LLM-powered layer responsible for converting structured, ML-derived data signals — from the Parent Predictive AI, the Championship Advanced AI, the Drift Detection engine, and the Intelligence Measurement System — into **warm, plain-language, parent-comprehensible narratives** delivered across all parent-facing surfaces.

Parents trust scientific systems. Parents do not trust raw AI scores. PING bridges this trust gap by transforming numbers, percentiles, vectors, and ML predictions into language that answers the **single most important question every parent asks:**

> **"What is my child's future?"**

PING does NOT make decisions. PING does NOT score. PING does NOT award. PING translates verified, ML-computed, mentor-validated data into human-readable insight narratives — auditable, versioned, explainable.

### 1.2 The Three Pillars of PING

| Pillar | Function |
|---|---|
| **Championship Advanced Narrative** | Translates championship tier performance, qualification status, rank trajectories, and domain-level intelligence scores into parent-ready championship stories |
| **Parent Predictive AI Narrative** | Converts dropout risk, plateau predictions, breakthrough forecasts, career probability estimates, and milestone timelines into warm, actionable parent insights |
| **Intelligence Profile Narrative** | Generates the full Multiple Intelligence Report — strengths, development areas, cross-domain transfer signals, and recommended next actions — in plain language |

### 1.3 Scope Boundaries (Non-Negotiable)

```
IN SCOPE (LLM MAY GENERATE):
  - Narrative summaries of ML-computed scores and signals
  - Plain-language explanations of drift events
  - Championship progress stories and rank context
  - Intelligence domain strength/development descriptions
  - Career probability narratives (based on ML-T4 output only)
  - Alert messages: engagement drop, plateau, breakthrough, safety
  - Weekly/Monthly parent report narrative sections
  - Milestone celebration messages
  - Mentor recommendation prompts (narrative only)
  - Multilingual narrative generation (governed — see PING-9)

OUT OF SCOPE (LLM MUST NEVER DO):
  - Compute any score, rank, or percentile
  - Make belt or certification decisions
  - Generate championship qualification/disqualification decisions
  - Produce career guarantees or income predictions presented as fact
  - Access raw student PII beyond what is in the prompt payload
  - Access competitor student data
  - Cross-tenant narrative generation
  - Generate narratives without a valid prompt_version tag
  - Generate narratives without a valid consent_record_id
  - Produce unsupported claims not grounded in the structured data payload
```

### 1.4 R28 Compliance Lock

```
R28 INTELLIGENCE COST OPTIMIZATION COMPLIANCE:

  PING is classified as: R28-3 PERMITTED
    → LLM used for: Text generation + Human-readable explainability
    → LLM NOT used for: Scoring, ranking, matching, prediction, classification

  All upstream scoring, ranking, classification, and prediction:
    → Handled by traditional ML models (R28-2 compliant)
    → Results passed to PING as structured payload
    → PING converts structured payload → narrative ONLY

  No LLM inference for tasks solvable by rules or ML.
  Violation → STOP EXECUTION
```

---

## 📦 SECTION PING-2 — PROMPT ARCHITECTURE OVERVIEW (LOCKED)

### 2.1 Prompt Construction Model

Every narrative generation call follows a **sealed 5-layer prompt construction model:**

```
┌─────────────────────────────────────────────────────────┐
│  LAYER 1 — SYSTEM IDENTITY BLOCK (Immutable per tenant) │
│    Role, tone, language, platform identity, trust rules  │
├─────────────────────────────────────────────────────────┤
│  LAYER 2 — GOVERNANCE FENCE (Non-Negotiable Injected)   │
│    What LLM must never say, claim, or fabricate          │
├─────────────────────────────────────────────────────────┤
│  LAYER 3 — STRUCTURED DATA PAYLOAD (Dynamic per call)   │
│    ML scores, drift delta, championship status, signals  │
├─────────────────────────────────────────────────────────┤
│  LAYER 4 — NARRATIVE TASK INSTRUCTION (Per report type) │
│    Tone, length, format, audience, required sections     │
├─────────────────────────────────────────────────────────┤
│  LAYER 5 — OUTPUT SCHEMA ENFORCEMENT (Validation Gate)  │
│    Required fields, max tokens, forbidden phrases check  │
└─────────────────────────────────────────────────────────┘
```

**All five layers are mandatory. Missing any layer → STOP EXECUTION.**

### 2.2 Prompt Registry Schema (Mandatory)

Every prompt template is registered before use:

```json
{
  "prompt_id": "PING-TMPL-001",
  "prompt_version": "v1.0.0",
  "narrative_type": "WeeklyParentReport | ChampionshipUpdate | AlertMessage | IntelligenceProfile | MonthlyProgressReport | MilestoneAlert | CareerInsight",
  "language": "en | hi | mr | ta | te | bn | gu | kn | ml | pa",
  "audience": "parent",
  "tone": "warm | urgent | celebratory | concerned | neutral",
  "max_tokens": 500,
  "required_payload_fields": [],
  "forbidden_output_phrases": [],
  "model_version": "llm-model-version-tag",
  "created_by": "governance_board",
  "approved_at": "ISO8601",
  "status": "active | deprecated | draft",
  "audit_log_ref": "uuid"
}
```

**Prompts NOT in the registry → FORBIDDEN. Silent prompt use → FORBIDDEN.**

---

## 🔐 SECTION PING-3 — LAYER 1: SYSTEM IDENTITY BLOCK (LOCKED TEMPLATE)

This block is injected as the first system message in every PING LLM call. It is immutable per deployment version.

```
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
PING SYSTEM IDENTITY BLOCK — v1.0 — IMMUTABLE
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

You are the Parent Insight Narrator for ECOSKILLER — a trusted
education and intelligence platform used by students, parents,
schools, and universities across India and globally.

Your sole job is to translate structured, verified data about a
student's learning progress into clear, warm, and honest
parent-friendly language.

PLATFORM IDENTITY:
  Platform: ECOSKILLER + DOJO
  Role: Parent Insight Narrator
  Audience: Parent / Guardian
  Trust Level: Institutional — Language must earn parent trust

TONE PRINCIPLES (NON-NEGOTIABLE):
  1. Warm and supportive — never clinical or robotic
  2. Honest — never exaggerate or minimize
  3. Simple — no jargon, no ML terms, no raw numbers without context
  4. Actionable — every insight ends with what the parent can do
  5. Evidence-grounded — every claim is traceable to the data payload

LANGUAGE RULES:
  - Write as if a trusted school counselor is speaking to a parent
  - Avoid: "algorithm", "model", "prediction confidence", "vector"
  - Avoid: overly technical language
  - Use: "based on recent practice sessions", "according to his/her
    performance in the last 4 weeks", "our platform has noticed..."
  - Address the child by first name from payload
  - Address the parent with warmth: "We wanted to share...",
    "Here's what we've observed..."

━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
```

---

## 🚫 SECTION PING-4 — LAYER 2: GOVERNANCE FENCE (LOCKED — INJECTED IN EVERY CALL)

The Governance Fence is a hard-injected constraint block that prevents hallucination, fabrication, overclaiming, and policy violations.

```
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
PING GOVERNANCE FENCE — v1.0 — MANDATORY INJECTION
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

ABSOLUTE PROHIBITIONS (Any violation = output rejected + flagged):

NEVER claim certainty about future outcomes.
  WRONG: "Arjun will become an engineer."
  RIGHT: "Based on Arjun's recent performance, engineering and
          design careers appear to be strong areas of fit."

NEVER invent scores, ranks, percentiles, or statistics
not present in the structured data payload.
  WRONG: "Arjun scored 92% this week."  (if not in payload)
  RIGHT: Only use numbers explicitly provided in the payload.

NEVER compare the child to named peers or competitors.
  WRONG: "Priya is doing better than Arjun in this area."
  RIGHT: "Arjun is performing above the school average in
          Spatial Intelligence."

NEVER make medical, psychological, or therapeutic claims.
  WRONG: "This pattern may indicate an attention disorder."
  RIGHT: "We've noticed a drop in focus during longer sessions.
          A short break or a mentor check-in might help."

NEVER suggest the platform is infallible.
  Include this phrase when relevant:
  "These insights are AI-assisted and reviewed by our mentors."

NEVER disclose raw model output, SHAP values, or confidence
intervals to parents. Translate these into plain-language
confidence language:
  HIGH confidence → "We are quite confident that..."
  MEDIUM confidence → "Our platform suggests that..."
  LOW confidence → "We're starting to notice that..."
  (Low confidence narratives require mentor review before delivery)

NEVER use phrases that could cause parental panic:
  FORBIDDEN LIST:
  - "Your child is failing"
  - "Critical risk detected"
  - "AI has flagged your child"
  - "Dropout predicted"
  - "Career probability low"
  USE INSTEAD:
  - "We've noticed a dip in recent practice..."
  - "Arjun may benefit from extra support this week..."
  - "We recommend a mentor check-in soon..."

NEVER generate content without ALL required payload fields.
  Missing required field → STOP → return:
  {"error": "PING_PAYLOAD_INCOMPLETE", "missing_fields": [...]}

NEVER generate narratives for students outside the
authenticated parent's linked_children[] list.
  Cross-child access → STOP → SECURITY_VIOLATION

━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
```

---

## 📊 SECTION PING-5 — LAYER 3: STRUCTURED DATA PAYLOAD SCHEMAS (LOCKED)

### 5.1 Base Payload (All Narrative Types)

```json
{
  "ping_request_id": "uuid",
  "prompt_template_id": "PING-TMPL-XXX",
  "prompt_version": "v1.0.0",
  "consent_record_id": "uuid",
  "tenant_id": "uuid",
  "parent_id": "uuid",
  "student_id": "uuid",
  "student_first_name": "string",
  "student_age": "integer",
  "student_domain": "Arts | Commerce | Science",
  "student_institution": "string",
  "language": "en | hi | mr | ...",
  "narrative_type": "string",
  "generated_at": "ISO8601"
}
```

### 5.2 Weekly Parent Report Payload

```json
{
  ...base_payload,
  "report_week": "ISO8601_week",
  "intelligence_domain_scores": {
    "logical": { "score": 0, "percentile": 0.0, "delta_7d": 0.0, "confidence_band": "HIGH|MEDIUM|LOW" },
    "linguistic": { "score": 0, "percentile": 0.0, "delta_7d": 0.0, "confidence_band": "..." },
    "spatial": { "score": 0, "percentile": 0.0, "delta_7d": 0.0, "confidence_band": "..." },
    "interpersonal": { "score": 0, "percentile": 0.0, "delta_7d": 0.0, "confidence_band": "..." },
    "intrapersonal": { "score": 0, "percentile": 0.0, "delta_7d": 0.0, "confidence_band": "..." },
    "naturalistic": { "score": 0, "percentile": 0.0, "delta_7d": 0.0, "confidence_band": "..." },
    "musical": { "score": 0, "percentile": 0.0, "delta_7d": 0.0, "confidence_band": "..." },
    "kinesthetic": { "score": 0, "percentile": 0.0, "delta_7d": 0.0, "confidence_band": "..." },
    "entrepreneurial": { "score": 0, "percentile": 0.0, "delta_7d": 0.0, "confidence_band": "..." },
    "ai_collaboration": { "score": 0, "percentile": 0.0, "delta_7d": 0.0, "confidence_band": "..." }
  },
  "engagement_summary": {
    "sessions_completed": 0,
    "total_practice_hours": 0.0,
    "drills_completed": 0,
    "streak_days": 0
  },
  "top_strength_domain": "string",
  "focus_area_domain": "string",
  "peer_comparison_label": "above_average | average | below_average",
  "drift_class": "NONE | MINOR | MODERATE | CRITICAL | BREAKTHROUGH",
  "recommended_action": "string (pre-computed, not LLM-generated)"
}
```

### 5.3 Championship Update Payload

```json
{
  ...base_payload,
  "active_championships": [
    {
      "championship_name": "string",
      "championship_tier": "School | District | City | State | National | Continental | World",
      "intelligence_domain": "string",
      "current_rank": 0,
      "total_participants": 0,
      "rank_percentile": 0.0,
      "rank_delta_since_last_round": 0,
      "qualification_status": "qualified | at_risk | not_qualified | not_entered",
      "next_round_date": "ISO8601",
      "championship_readiness_index": 0.0,
      "confidence_band": "HIGH | MEDIUM | LOW"
    }
  ],
  "upcoming_championships": [
    {
      "championship_name": "string",
      "tier": "string",
      "domain": "string",
      "registration_deadline": "ISO8601",
      "eligibility_met": true
    }
  ],
  "championship_milestone_achieved": "string | null",
  "personal_best_rank_this_season": 0
}
```

### 5.4 Alert Payload (Risk / Celebration)

```json
{
  ...base_payload,
  "alert_type": "ENGAGEMENT_DROP | REGRESSION_RISK | PLATEAU | CHAMPIONSHIP_RISK | BREAKTHROUGH | BELT_APPROACHING | SAFETY_EVENT | MENTOR_RECOMMENDED",
  "alert_severity": "GREEN | AMBER | RED",
  "alert_trigger_signal": "string (signal ID from Tier-1 to Tier-4)",
  "days_since_last_session": 0,
  "performance_drop_percent": 0.0,
  "plateau_duration_days": 0,
  "breakthrough_improvement_percent": 0.0,
  "belt_matches_remaining": 0,
  "safety_event_type": "string | null",
  "mentor_recommendation_flag": true,
  "recommended_action_cta": "Book Mentor Session | View Practice Drills | Review Report | Contact School"
}
```

### 5.5 Intelligence Profile Payload (Full Report)

```json
{
  ...base_payload,
  "report_period": "monthly | quarterly | annual",
  "intelligence_full_profile": {
    "top_3_domains": ["string", "string", "string"],
    "development_domains": ["string"],
    "balanced_domains": ["string"],
    "cross_domain_transfer_index": 0.0,
    "multi_intelligence_imbalance_flag": true,
    "domain_detail": {
      "logical": {
        "score": 0,
        "national_percentile": 0.0,
        "school_rank": 0,
        "description_key": "strong | developing | needs_support",
        "top_skill_tags": ["reasoning", "pattern recognition"],
        "recommended_activity_tags": ["math_puzzles", "coding_challenges"]
      }
    }
  },
  "learning_layer_breakdown": {
    "psychometric_test_score": 0.0,
    "behavioral_data_score": 0.0,
    "real_task_score": 0.0,
    "long_term_tracking_score": 0.0
  },
  "career_fit_predictions": [
    {
      "career_label": "string",
      "fit_probability": 0.0,
      "primary_domain_match": "string",
      "confidence_band": "HIGH | MEDIUM | LOW"
    }
  ],
  "belt_level_current": "string",
  "belt_readiness_index": 0.0,
  "certification_earned_this_period": "string | null"
}
```

### 5.6 Milestone / Celebration Payload

```json
{
  ...base_payload,
  "milestone_type": "BELT_EARNED | CHAMPIONSHIP_QUALIFIED | CHAMPIONSHIP_WON | TOP_PERCENTILE_ACHIEVED | NATIONAL_RANK_ACHIEVED | STREAK_ACHIEVEMENT | FIRST_MATCH_WON | CERTIFICATION_EARNED",
  "milestone_detail": {
    "belt_level": "string | null",
    "championship_tier": "string | null",
    "rank_achieved": 0,
    "percentile_achieved": 0.0,
    "streak_days": 0,
    "certification_name": "string | null"
  },
  "shareable_card_eligible": true,
  "social_share_message_requested": true
}
```

---

## ✍️ SECTION PING-6 — LAYER 4: NARRATIVE TASK INSTRUCTIONS (LOCKED TEMPLATES)

### 6.1 Weekly Parent Report Narrative Instruction

```
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
NARRATIVE TASK: WEEKLY PARENT REPORT
Prompt Template ID: PING-TMPL-001
Max Tokens: 450
Tone: Warm, supportive, clear
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

Generate a Weekly Parent Report narrative using ONLY the data
in the provided structured payload. Do not invent any facts.

REQUIRED SECTIONS (in order):

1. GREETING (1 sentence)
   Open with a warm personalised greeting using student_first_name.
   Example: "Here is this week's progress update for Arjun."

2. HIGHLIGHT OF THE WEEK (2–3 sentences)
   Lead with the most positive signal from the payload.
   Reference top_strength_domain and engagement_summary.
   Use confidence_band to calibrate language:
     HIGH → "We're delighted to share that..."
     MEDIUM → "This week, we noticed strong signs that..."
     LOW → "Our platform has started to observe..."

3. INTELLIGENCE PROGRESS SUMMARY (3–4 sentences)
   Describe the top_strength_domain score and its context
   (peer_comparison_label, delta_7d).
   Mention focus_area_domain with encouragement, not alarm.
   Use plain language — no raw numbers without context sentence.

4. WHAT ARJUN DID THIS WEEK (2 sentences)
   Use engagement_summary to describe activity:
   sessions, hours, drills, streak. Make it feel tangible.

5. ONE THING TO FOCUS ON (2 sentences)
   Translate recommended_action into parent-friendly language.
   Suggest one concrete next step (from payload CTA).

6. CLOSING TRUST STATEMENT (1 sentence)
   End with:
   "These insights are AI-assisted and reviewed by our mentors."

FORBIDDEN IN THIS TEMPLATE:
  - Do not mention drift_class directly to parent
  - Do not use word "algorithm" or "model"
  - Do not give raw score numbers without a context sentence
  - Do not predict career outcomes in the weekly report

OUTPUT FORMAT:
  Plain paragraphs. No bullet points. No headers in parent-facing text.
  Total length: 200–350 words.

━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
```

### 6.2 Championship Update Narrative Instruction

```
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
NARRATIVE TASK: CHAMPIONSHIP UPDATE
Prompt Template ID: PING-TMPL-002
Max Tokens: 400
Tone: Proud, exciting, informative
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

Generate a Championship Progress narrative for the parent.

REQUIRED SECTIONS (in order):

1. CHAMPIONSHIP CONTEXT (1 sentence)
   Name the active championship(s) and domain(s) using payload.
   Example: "Arjun is currently competing in the Maharashtra
   Spatial Intelligence Championship."

2. CURRENT STANDING (2–3 sentences)
   Translate current_rank, total_participants, rank_percentile
   into a warm, context-rich statement.
   Example: "Out of [total] students from across Maharashtra,
   Arjun is ranked [rank], placing him in the top [X]%
   of all participants."
   Use rank_delta_since_last_round:
     Positive delta → "He has moved up [X] places since last round!"
     Negative delta → "There's an opportunity to climb higher —
                       the next practice round can make a difference."

3. QUALIFICATION STATUS (1–2 sentences)
   Translate qualification_status:
     "qualified" → "Arjun has qualified for the next round!"
     "at_risk" → "Arjun is close to qualifying — this is the week
                   to give it an extra push."
     "not_qualified" → "Arjun hasn't qualified for this round yet,
                         but the next season is just around the corner."
     "not_entered" → "There's a championship coming up that Arjun
                       might enjoy — see details below."

4. UPCOMING OPPORTUNITY (1–2 sentences)
   If upcoming_championships is non-empty, invite parent to note
   the next championship and deadline.

5. ENCOURAGEMENT (1 sentence)
   Always end this section with encouragement.
   If championship_milestone_achieved is non-null → celebrate it.

FORBIDDEN IN THIS TEMPLATE:
  - Do not name other students or competitors
  - Do not guarantee qualification outcomes
  - Do not use championship_readiness_index as a raw number

OUTPUT FORMAT:
  Plain paragraphs. 150–280 words.

━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
```

### 6.3 Alert Narratives — Engagement Drop (AMBER)

```
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
NARRATIVE TASK: ENGAGEMENT DROP ALERT
Prompt Template ID: PING-TMPL-003A
Alert Severity: AMBER
Max Tokens: 200
Tone: Concerned but calm, never alarmist
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

Generate a short, warm alert message for a parent whose child
has not practiced recently.

Use days_since_last_session from payload.

REQUIRED CONTENT:
  - Acknowledge the gap warmly, not critically
  - Do NOT say "your child is at risk" or "flagged"
  - Suggest one simple re-engagement action (from recommended_action_cta)
  - Remind parent this is a support signal, not a grade
  - End with: "Feel free to contact us if you need support."

EXAMPLE TONE:
  "Hi [Parent], we noticed that [Student] hasn't logged in for
   [X] days. Life gets busy — that's completely understandable!
   A short 15-minute practice session today could help [Student]
   stay on track. [CTA: View Practice Drills]. These insights are
   AI-assisted and reviewed by our mentors."

Max words: 80–120.

━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
```

### 6.4 Alert Narratives — Regression Risk (RED)

```
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
NARRATIVE TASK: REGRESSION RISK ALERT
Prompt Template ID: PING-TMPL-003B
Alert Severity: RED
Max Tokens: 250
Tone: Honest, supportive, action-focused
Requires: mentor_review_before_delivery = true
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

Generate a regression risk alert narrative for the parent.
This narrative MUST NOT be delivered without mentor sign-off.

Use performance_drop_percent and top affected domain from payload.

REQUIRED CONTENT:
  - Open with warm, non-alarming observation
  - Describe the domain and the direction of change without
    using the raw drop percentage (translate to plain language)
  - Acknowledge that this is a normal part of learning
  - Recommend a mentor session (from recommended_action_cta)
  - Reassure parent that the platform is actively supporting their child
  - End with: "These insights have been reviewed by [Student's]
    assigned mentor. We're here to help."

FORBIDDEN:
  - Do NOT say "critical", "failure", "drop detected", "at risk"
  - Do NOT use the percentage drop as a raw number
  - Do NOT predict long-term outcomes from a single drop

Max words: 100–180.

━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
```

### 6.5 Alert Narratives — Breakthrough (GREEN Celebration)

```
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
NARRATIVE TASK: BREAKTHROUGH ALERT
Prompt Template ID: PING-TMPL-003C
Alert Severity: GREEN
Max Tokens: 220
Tone: Joyful, proud, celebratory
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

Generate a breakthrough celebration narrative for the parent.

Use breakthrough_improvement_percent and top_domain from payload.

REQUIRED CONTENT:
  - Lead with genuine celebration (not over-the-top)
  - Name the specific domain and what improved
  - Give context: performance relative to school/national level
    (use peer_comparison_label from weekly payload if available)
  - Suggest capitalising on the momentum
    (championship entry, skill track advancement)
  - End with shareable encouragement phrase

EXAMPLE TONE:
  "Wonderful news! [Student] has had a remarkable week in
   [Domain] — showing significant improvement that places
   her among the top performers in her school this week.
   This is the perfect time to explore the upcoming
   [Championship Name]. Keep going, [Student]! 🌟"

Max words: 80–140.

━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
```

### 6.6 Intelligence Profile Narrative Instruction (Monthly Full Report)

```
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
NARRATIVE TASK: INTELLIGENCE PROFILE REPORT
Prompt Template ID: PING-TMPL-004
Max Tokens: 800
Tone: Thoughtful, insightful, scientific yet warm
Report Period: Monthly | Quarterly | Annual
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

Generate the full Monthly Intelligence Profile Narrative.
This is the flagship parent report — the most trusted and
valued output of the PING system.

REQUIRED SECTIONS (in order):

1. INTRODUCTION (2 sentences)
   Frame the report: what was measured, over what period.
   Mention EIMS (Ecoskiller Intelligence Measurement System).

2. [STUDENT]'S INTELLIGENCE PROFILE (4–5 sentences)
   Describe the top_3_domains in warm, specific language.
   Name the key observable behaviors associated with each domain.
   Example: "Arjun shows a strong natural ability in Spatial
   Intelligence — he tends to excel in tasks that involve
   visualisation, design thinking, and pattern recognition."

3. AREAS OF ACTIVE GROWTH (2–3 sentences)
   Describe development_domains with encouragement.
   Emphasise that all intelligences can be developed.
   Do NOT frame as failures.

4. HOW [STUDENT] LEARNS BEST (2–3 sentences)
   Derive this from learning_layer_breakdown.
   Translate behavioral data vs psychometric balance into
   plain guidance: e.g., "Arjun tends to perform best in
   real-world, hands-on tasks rather than written tests."

5. CAREER FIT INSIGHTS (3–4 sentences)
   Use career_fit_predictions (top 3 only).
   Translate fit_probability using confidence_band language.
   NEVER present as absolute. Always say:
   "Based on [Student]'s current intelligence profile..."
   HIGH confidence: "Arjun shows strong alignment with..."
   MEDIUM: "There are encouraging early signs toward..."
   LOW: Do NOT include this career in the narrative.
   Always close with:
   "Career paths evolve — this is a helpful signal,
    not a destination set in stone."

6. CHAMPIONSHIP JOURNEY THIS MONTH (2–3 sentences)
   Summarise championship activity if payload contains it.
   Celebrate achievements. Frame gaps as opportunities.

7. WHAT YOU CAN DO AS A PARENT (2–3 sentences)
   Translate recommended_action from payload.
   Give 1–2 specific, actionable suggestions.
   Example: "Encouraging Arjun to spend 20 minutes on
   design puzzles each evening would strengthen his
   already impressive Spatial Intelligence even further."

8. TRUST STATEMENT (1 sentence — mandatory)
   "This report is generated using AI-assisted analysis
    and reviewed by Arjun's assigned mentor."

FORBIDDEN IN THIS TEMPLATE:
  - Do not name competing students
  - Do not use raw model confidence scores
  - Do not present career_fit as definitive
  - Do not use: "algorithm", "dropout risk", "ML prediction"
  - Do not include multi_intelligence_imbalance_flag as language
    (translate: "We've noticed some domains are growing faster
    than others — this is normal and healthy...")

OUTPUT FORMAT:
  Flowing paragraphs with light section breaks.
  400–650 words. Suitable for WhatsApp PDF + App display.

━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
```

### 6.7 Milestone / Celebration Narrative Instruction

```
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
NARRATIVE TASK: MILESTONE CELEBRATION
Prompt Template ID: PING-TMPL-005
Max Tokens: 180
Tone: Joyful, proud, shareable
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

Generate a milestone celebration message for the parent.
This message is also used as the caption for the shareable
achievement card.

MILESTONE TYPE HANDLING:
  BELT_EARNED →
    "Congratulations! [Student] has earned their [Belt Level]
     on ECOSKILLER DOJO! This achievement reflects weeks of
     practice, real matches, and genuine skill growth. 🥋
     We're incredibly proud."

  CHAMPIONSHIP_QUALIFIED →
    "[Student] has officially qualified for the
     [Championship Name] — [Tier] level! This is a
     remarkable achievement. The competition begins
     [next_round_date]. Cheer them on! 🏆"

  NATIONAL_RANK_ACHIEVED →
    "[Student] has achieved National Rank [rank] in
     [Domain] Intelligence — placing them in the top [X]%
     of all students across India. This is exceptional. 🌟"

  CERTIFICATION_EARNED →
    "[Student] has earned their [certification_name]
     certification on ECOSKILLER. This verified credential
     reflects genuine, measured skill. 🎓"

All milestone narratives:
  - Max 80 words
  - Emoji appropriate (1–2 max)
  - Suitable for WhatsApp forward
  - Include: "Verified by ECOSKILLER — [tenant/platform name]"

━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
```

### 6.8 Career Insight Narrative Instruction (Standalone)

```
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
NARRATIVE TASK: CAREER INSIGHT
Prompt Template ID: PING-TMPL-006
Max Tokens: 350
Tone: Insightful, honest, forward-looking
Requires: minimum 3 completed assessment cycles in payload
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

Generate a Career Insight narrative for the parent. This is
the answer to: "What is my child's future?"

MANDATORY OPENING DISCLAIMER (always included):
  "Career paths are shaped by passion, effort, and
   opportunity — these insights are based on [Student]'s
   current intelligence profile and are meant to inspire,
   not limit, their possibilities."

REQUIRED CONTENT:
  1. Top 2–3 career fits from payload (HIGH and MEDIUM confidence only)
     Describe each using the domain strengths, not probabilities.
  2. What makes [Student] unique for these paths.
  3. One suggested next step: championship, course, or activity.
  4. Trust statement.

FORBIDDEN:
  - Do not present LOW confidence career fits
  - Do not quote fit_probability as a percentage to parents
  - Do not compare to national career demand (not in scope)
  - Do not use "income prediction" or salary claims
  - Do not present as deterministic

Max words: 180–280.

━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
```

---

## ✅ SECTION PING-7 — LAYER 5: OUTPUT SCHEMA & VALIDATION GATE (LOCKED)

Every PING LLM output is validated against this schema before delivery to any parent-facing surface.

```json
{
  "ping_response_id": "uuid",
  "ping_request_id": "uuid (matched)",
  "prompt_template_id": "string",
  "prompt_version": "string",
  "narrative_type": "string",
  "student_id": "uuid",
  "parent_id": "uuid",
  "tenant_id": "uuid",
  "language": "string",
  "tone_used": "string",
  "narrative_text": "string (generated narrative)",
  "word_count": 0,
  "token_count": 0,
  "model_version": "string",
  "confidence_band_used": "HIGH | MEDIUM | LOW",
  "mentor_review_required": false,
  "mentor_reviewed": false,
  "mentor_reviewer_id": "uuid | null",
  "forbidden_phrase_check": "PASSED | FAILED",
  "payload_completeness_check": "PASSED | FAILED",
  "governance_fence_applied": true,
  "output_audit_log_ref": "uuid",
  "generated_at": "ISO8601",
  "delivery_status": "pending | delivered | blocked_mentor_review | rejected"
}
```

### 7.1 Validation Rules

```
OUTPUT VALIDATION GATES (All must pass before delivery):

  GATE-1: PAYLOAD COMPLETENESS
    → All required_payload_fields present
    → Missing field → reject → PING_PAYLOAD_INCOMPLETE error

  GATE-2: GOVERNANCE FENCE CHECK
    → Scan output for forbidden phrases list
    → Any match → reject → PING_GOVERNANCE_VIOLATION

  GATE-3: WORD COUNT BOUNDS
    → Word count within template min/max range
    → Out of bounds → reject → PING_LENGTH_VIOLATION

  GATE-4: MENTOR REVIEW GATE
    → IF mentor_review_required = true AND mentor_reviewed = false
    → Block delivery → queue for mentor review
    → Delivery cannot proceed without mentor sign-off

  GATE-5: CONSENT CHECK
    → consent_record_id must be valid and active
    → Expired or revoked consent → block → PING_CONSENT_REVOKED

  GATE-6: CROSS-CHILD ACCESS CHECK
    → student_id must be in parent's linked_children[]
    → Mismatch → STOP → PING_SECURITY_VIOLATION

  GATE-7: TENANT ISOLATION CHECK
    → tenant_id of student must match tenant_id of parent
    → Cross-tenant → STOP → PING_TENANT_VIOLATION

  ALL GATES MUST PASS.
  Any failed gate → output blocked → audit log created.
```

---

## 🔄 SECTION PING-8 — DELIVERY CHANNELS & SURFACE MAP (LOCKED)

### 8.1 Delivery Surface Registry

```
PING NARRATIVE SURFACES:

  FLUTTER APP (Parent Role — Operational):
    Screen: ParentHomeDashboard
      → Narrative: Weekly summary (PING-TMPL-001, first 3 sentences)
    Screen: ParentWeeklyReport
      → Narrative: Full PING-TMPL-001 narrative
    Screen: ParentAlertCenter
      → Narrative: PING-TMPL-003A / 003B / 003C
    Screen: ParentChampionshipTracker
      → Narrative: PING-TMPL-002
    Screen: ParentIntelligenceProfile
      → Narrative: PING-TMPL-004 (monthly)
    Screen: MilestoneCelebrationCard
      → Narrative: PING-TMPL-005

  PUSH NOTIFICATION (FCM):
    Alert types: ENGAGEMENT_DROP, BREAKTHROUGH, BELT_APPROACHING
    Notification body = first 100 characters of alert narrative
    Deep link → opens relevant Flutter screen

  EMAIL DELIVERY (Postal):
    Report type: Monthly Intelligence Profile (PING-TMPL-004)
    Report type: Quarterly Career Insight (PING-TMPL-006)
    Format: HTML email template with narrative embedded
    Branding: Tenant-branded (tenant_branding from P11)

  PDF EXPORT (MinIO):
    Report type: Monthly + Annual Intelligence Report
    Format: Certified Intelligence Report PDF
    Includes: Narrative + scores + achievement wall
    Access: Parent download only (signed URL, time-limited)
    MinIO path: /reports/parent/{tenant_id}/{student_id}/{report_id}.pdf

  WHATSAPP (Future — Notification Hook):
    Short alert narratives only (≤120 words)
    No PII beyond first name in WhatsApp message
    Governed under separate WHATSAPP_COMPLIANCE_LOCK (Add-Only)
```

### 8.2 Delivery Timing Rules

```
DELIVERY TIMING LOCK:

  Weekly Report:
    → Generated: Every Sunday 23:00 IST (Airflow DAG)
    → Delivered: Monday 08:00 local time (parent timezone)
    → Mentor review window: Sunday 20:00 – 23:00

  Monthly Intelligence Profile:
    → Generated: Last day of month (Airflow DAG)
    → Delivered: 1st of next month 09:00 local time

  Alert — GREEN (Breakthrough, Milestone):
    → Delivered: Immediately on trigger event
    → No mentor review required

  Alert — AMBER (Engagement Drop, Plateau):
    → Delivered: Within 2 hours of trigger event
    → No mentor review required

  Alert — RED (Regression Risk, Safety):
    → Blocked until mentor reviews
    → Mentor SLA: 24 hours
    → If mentor unreviewed after 24h → escalate to ops team

  Career Insight Report:
    → Available: After minimum 3 assessment cycles completed
    → Delivered: Quarterly
    → Mentor review required before first delivery
```

---

## 🌐 SECTION PING-9 — MULTILINGUAL NARRATIVE GOVERNANCE (LOCKED)

```
MULTILINGUAL SUPPORT:

  Supported Languages (Phase 1):
    en — English
    hi — Hindi
    mr — Marathi
    gu — Gujarati
    ta — Tamil
    te — Telugu
    bn — Bengali
    kn — Kannada
    ml — Malayalam
    pa — Punjabi

  Translation Governance Rules:

    1. MASTER PROMPT LANGUAGE: English
       All prompt templates are authored in English.

    2. TRANSLATION APPROACH:
       Option A — Native language prompt with English governance fence
       Option B — Generate in English → translate via governed service
       Both options require: translation_version tag on output record.

    3. CULTURAL NEUTRALITY:
       No culturally exclusive phrases in any narrative.
       Section T12 (Localization & Cultural Fairness) applies.
       No scenario description or career narrative may be
       culture-exclusive (e.g., implying only certain communities
       pursue certain careers).

    4. TRANSLATION AUDIT:
       Translated narratives undergo quarterly bias and
       cultural accuracy review.
       Violation → STOP delivery of that language variant.

    5. FALLBACK:
       If requested language not available →
       deliver in English with apology note.
       Do NOT silently deliver wrong language.
```

---

## 📋 SECTION PING-10 — PROMPT OPERATIONS & VERSIONING (LOCKED)

### 10.1 Prompt Lifecycle

```
PROMPT LIFECYCLE STATES:
  draft → review → approved → active → deprecated → archived

  Rules:
    - Only "active" prompts may be used in production
    - Deprecation requires 30-day overlap with replacement
    - Archived prompts retained 2 years for audit
    - No production deployment of unapproved prompts
    - Every prompt change requires governance_board approval
    - Prompt version bump required for ANY content change
    - Version tag format: PING-TMPL-XXX-vMAJOR.MINOR.PATCH
```

### 10.2 Prompt Change Rules

```
PROMPT MUTATION RULES:

  ALLOWED without version bump:
    - Spelling/grammar fix (PATCH)
    - Formatting correction (PATCH)

  REQUIRES MINOR version bump:
    - Tone adjustment
    - New allowed phrase
    - Section reordering

  REQUIRES MAJOR version bump + governance review:
    - New section added
    - Removal of any section
    - Change to governance fence
    - Change to forbidden phrases list
    - Change to max_tokens limit
    - New career domain added to career_fit narrative

  Silent mutation → FORBIDDEN
  Any mutation without version bump → STOP EXECUTION
```

### 10.3 A/B Testing Rules

```
A/B TESTING OF PROMPTS:

  Allowed:
    - Test two approved prompt versions simultaneously
    - Split by tenant (not by individual user)
    - Collect: parent engagement rate, mentor override rate,
      alert acknowledgment rate per version

  Required:
    - Both versions must be in registry as "active"
    - A/B experiment must be registered in experiment_registry
    - Experiment duration: max 30 days
    - Winner determined by: mentor_override_rate + parent_engagement_rate

  Forbidden:
    - A/B testing governance fence variants
    - A/B testing that changes forbidden phrase list
    - A/B testing across tenants
```

---

## 📊 SECTION PING-11 — OBSERVABILITY & AUDIT (MANDATORY)

### 11.1 Required Telemetry

```
PING TELEMETRY METRICS:

  Latency:
    - ping_narrative_generation_latency_ms (p50, p95, p99)
    - ping_validation_gate_latency_ms

  Quality:
    - ping_governance_fence_violation_rate
    - ping_payload_incomplete_rate
    - ping_mentor_review_triggered_rate
    - ping_mentor_review_overdue_rate (>24h SLA)
    - ping_output_rejected_rate (per template, per language)

  Delivery:
    - ping_delivery_success_rate (per channel)
    - ping_parent_open_rate (push + email)
    - ping_parent_action_rate (CTA clicked)
    - ping_parent_report_download_rate (PDF)

  Trust:
    - ping_parent_feedback_positive_rate
    - ping_narrative_override_by_mentor_rate
    - ping_parent_data_request_rate (GDPR/DPDP)

  AI Governance:
    - ping_prompt_version_distribution (active versions in use)
    - ping_model_version_distribution
    - ping_bias_score_per_language_per_domain (monthly)
    - ping_low_confidence_narrative_blocked_rate
```

### 11.2 Required Dashboards

```
PING DASHBOARDS (Grafana):

  1. PING Health Dashboard
     - Generation latency trends
     - Rejection rates by gate
     - Delivery success rates
     - Mentor review queue depth + SLA compliance

  2. PING Governance Dashboard
     - Prompt version distribution
     - Governance fence violation log
     - Forbidden phrase trigger counts
     - Cross-child/cross-tenant violation attempts (should be zero)

  3. PING Parent Trust Dashboard
     - Report open rates by narrative type
     - CTA action rates
     - Narrative override frequency
     - Parent feedback scores

  4. PING AI Audit Dashboard
     - Model version in use per tenant
     - Bias score trends by language/domain
     - Prompt log review queue
     - Monthly bias audit status
```

### 11.3 Required Alerts

```
PING ALERTS:

  P0 — Immediate:
    - governance_fence_violation_rate > 0.1% → FREEZE DELIVERY
    - cross_tenant_violation_detected = true → FREEZE + SECURITY ALERT
    - consent_check_bypass_detected = true → FREEZE + SECURITY ALERT

  P1 — 15 min SLA:
    - generation_error_rate > 5% → engineering alert
    - mentor_review_queue > 100 unreviewed RED alerts → ops alert

  P2 — 1 hour SLA:
    - delivery_success_rate < 90% → notification team alert
    - mentor_review_overdue_rate > 20% → mentor ops alert

  P3 — 24 hour SLA:
    - parent_open_rate drops >30% week-over-week → product alert
    - bias_score threshold breached → governance board alert
    - prompt_version mismatch detected → engineering alert
```

---

## 🏗️ SECTION PING-12 — ANTIGRAVITY GENERATION DIRECTIVES (FINAL SEAL)

### 12.1 What Antigravity Must Generate

```
ANTIGRAVITY_GENERATION_SCOPE (PING MODULE):

  BACKEND SERVICES (FastAPI Microservices):

    ParentInsightNarratorService:
      POST /ping/generate                  [internal service — not public]
      GET  /ping/narrative/{narrative_id}  [role: parent, admin]
      GET  /ping/history/{student_id}      [role: parent, mentor, admin]
      POST /ping/mentor/review/{narrative_id} [role: mentor]
      POST /ping/parent/feedback/{narrative_id} [role: parent]
      GET  /ping/prompts/registry          [role: admin, governance_board]
      POST /ping/prompts/register          [role: governance_board]

    PromptRegistryService:
      - Manages all prompt templates (CRUD — add/deprecate only)
      - Version control for all templates
      - A/B experiment registry
      - Serves approved prompts to ParentInsightNarratorService

    NarrativeAuditService:
      - Immutable log of every generated narrative
      - Links narrative_id → prompt_version → model_version → student_id
      - Supports GDPR/DPDP export and deletion workflows

  POSTGRESQL SCHEMA (New Tables — Add-Only):
    - ping_prompt_registry
    - ping_narrative_log
    - ping_narrative_audit_trail
    - ping_delivery_log
    - ping_mentor_review_queue
    - ping_parent_feedback
    - ping_ab_experiment_registry
    - ping_forbidden_phrase_registry
    - ping_language_variant_registry

  APACHE AIRFLOW DAGs:
    - ping_weekly_report_generation_dag (Sunday 23:00 IST)
    - ping_monthly_profile_generation_dag (Last day of month)
    - ping_quarterly_career_insight_dag
    - ping_bias_audit_dag (monthly)

  KAFKA TOPICS (New):
    - ping.narrative.generated
    - ping.narrative.delivered
    - ping.narrative.rejected
    - ping.mentor_review.queued
    - ping.mentor_review.completed
    - ping.governance_fence.violated (P0 alert trigger)
    - ping.parent_feedback.received

  FLUTTER SCREENS (Parent Role):
    Screen: ParentNarrativeReportViewer
      - Full weekly / monthly report display
      - Scrollable narrative with section headers (app-level)
      - Download as PDF CTA
    Screen: ParentAlertNarrativeCard
      - Inline alert in alert center
      - CTA button bound to recommended_action_cta
    Screen: MilestoneCelebrationNarrativeCard
      - Celebration card with shareable export
    Screen: ParentChampionshipNarrativeSection
      - Embedded in championship tracker

  FLUTTER SCREENS (Mentor Role):
    Screen: MentorNarrativeReviewPanel
      - Queue of RED narratives awaiting review
      - Approve / Edit / Reject controls
      - Edit → re-triggers PING with mentor-corrected payload
      - All mentor actions logged

  REACT (SEO — Public):
    - NO parent narratives on React public layer
    - Championship public results pages remain anonymized
    - No individual student narratives published publicly

  LLM INFRASTRUCTURE:
    - LLM provider abstracted via LLMGateway service
    - Provider: configurable (self-hosted or API-based)
    - Model version: pinned, logged per request
    - Prompt injection: 5-layer construction enforced
    - Output validation: 7-gate validation pipeline
    - Inference cost: logged per request (R28 cost compliance)
    - Rate limiting: per student, per tenant (RL-P compliance)
```

### 12.2 What Antigravity Must NOT Do

```
ANTIGRAVITY_FORBIDDEN_ACTIONS (PING MODULE):

  - DO NOT allow LLM to access any database directly
  - DO NOT allow LLM to call any API directly
  - DO NOT allow LLM to compute scores, ranks, or predictions
  - DO NOT allow narrative delivery without consent_record_id check
  - DO NOT allow RED narratives without mentor review gate
  - DO NOT allow prompts not in the PromptRegistry
  - DO NOT allow prompt mutation without version bump
  - DO NOT expose raw ML model internals in any narrative
  - DO NOT cross-link students from different parents
  - DO NOT cross tenant — narrative generation always tenant-scoped
  - DO NOT build parent narrative viewer on React (Flutter ONLY)
  - DO NOT send WhatsApp messages without separate compliance lock
  - DO NOT store narratives without audit_log_ref
  - DO NOT allow A/B testing of governance fence variants
  - DO NOT deliver LOW confidence career narratives to parents
  - DO NOT allow any narrative to be re-used across students
    (each narrative is individually generated per student payload)
```

### 12.3 Antigravity Run Command — PING Module

```
GENERATE_SERVICE
  MODULE          = PARENT_LLM_INSIGHT_NARRATIVE_GENERATOR
  SUB_MODULES     = [
                      ParentInsightNarratorService,
                      PromptRegistryService,
                      NarrativeAuditService,
                      NarrativeDeliveryOrchestrator,
                      MultilingualNarrativeLayer,
                      MentorReviewGateway,
                      PINGObservability
                    ]
  NARRATIVE_TYPES = [
                      WeeklyParentReport,
                      ChampionshipUpdate,
                      EngagementDropAlert,
                      RegressionRiskAlert,
                      BreakthroughAlert,
                      IntelligenceProfileReport,
                      MilestoneCelebration,
                      CareerInsightReport
                    ]
  ROLE_SCOPE      = [parent, mentor, admin, governance_board]
  STAGE           = PRODUCTION
  TENANT_MODE     = ISOLATED
  LLM_USAGE_CLASS = R28-3_PERMITTED
  PROMPT_CONTROL  = REGISTRY_GOVERNED
  OUTPUT_CONTROL  = 7_GATE_VALIDATED
  CONSENT_GATE    = MANDATORY
  MENTOR_GATE     = MANDATORY_FOR_RED
  AUDIT_TRAIL     = IMMUTABLE
  MULTILINGUAL    = PHASE_1_10_LANGUAGES
```

---

## ✅ SECTION PING-13 — FINAL COMPLIANCE SEAL

```
PARENT LLM INSIGHT NARRATIVE GENERATOR — ANTIGRAVITY COMPLIANCE SEAL

  PING_STATUS                         = COMPLETE
  LLM_USAGE_CLASSIFICATION            = R28-3 PERMITTED
  LLM_SCOPE                           = NARRATIVE_EXPLAINABILITY_ONLY
  CHAMPIONSHIP_ADVANCED_INTEGRATION   = ACTIVE · GOVERNED
  PARENT_PREDICTIVE_AI_INTEGRATION    = ACTIVE · CONSENT_GATED
  5_LAYER_PROMPT_CONSTRUCTION         = MANDATORY · ENFORCED
  7_GATE_OUTPUT_VALIDATION            = MANDATORY · ENFORCED
  GOVERNANCE_FENCE                    = INJECTED_IN_EVERY_CALL
  PROMPT_REGISTRY                     = MANDATORY · VERSIONED
  PROMPT_LOGGING                      = MANDATORY · IMMUTABLE
  OUTPUT_AUDIT_TRAIL                  = MANDATORY · IMMUTABLE
  MENTOR_REVIEW_RED_NARRATIVES        = MANDATORY · 24H_SLA
  PARENT_CONSENT_GATE                 = MANDATORY · BEFORE_FIRST_DELIVERY
  CROSS_TENANT_NARRATIVE              = FORBIDDEN
  CROSS_CHILD_ACCESS                  = FORBIDDEN
  LLM_SCORING_OR_AWARDING            = FORBIDDEN
  LLM_DIRECT_DB_ACCESS                = FORBIDDEN
  SILENT_PROMPT_MUTATION              = FORBIDDEN
  RAW_ML_EXPOSED_TO_PARENT            = FORBIDDEN
  LOW_CONFIDENCE_CAREER_NARRATIVE     = FORBIDDEN
  REACT_PARENT_NARRATIVES             = FORBIDDEN (Flutter only)
  MULTILINGUAL_GOVERNANCE             = PHASE_1_ACTIVE · BIAS_AUDITED
  GDPR_DPDP_COMPLIANCE               = ACTIVE
  OBSERVABILITY                       = TELEMETRY + DASHBOARDS + ALERTS

  ANTIGRAVITY_RUN_READY               = TRUE
  ANTIGRAVITY_CONFUSION               = IMPOSSIBLE
  MUTATION_POLICY                     = ADD_ONLY_VERSIONED
  QUALITY_SCORE                       = 10 / 10

ANY DEVIATION FROM THIS MODEL = STOP EXECUTION → REPORT → NO PARTIAL OUTPUT
```

---

**Document Version:** PING-v1.0  
**Platform Version:** ECOSKILLER + DOJO v12.0  
**Sealed By:** Master Prompt Authority  
**Mutation Authority:** Version Bump + Governance Board Only  
**Interpretation Authority:** NONE  
**R28 Classification:** R28-3 Permitted — LLM for text generation + explainability only  

---
*END LOCKED ARTIFACT — PARENT LLM INSIGHT NARRATIVE GENERATOR FOR ANTIGRAVITY*
