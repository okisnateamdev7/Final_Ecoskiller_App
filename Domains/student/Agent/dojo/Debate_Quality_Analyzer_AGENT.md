# 🔒 DEBATE QUALITY ANALYZER AGENT — AGENT.md
## ECOSKILLER ANTIGRAVITY PLATFORM
### Production Artifact | Sealed & Locked | Version: v1.0.0

---

```
╔══════════════════════════════════════════════════════════════════════════╗
║          ECOSKILLER ANTIGRAVITY — LOCKED AGENT ARTIFACT                ║
║  Agent:            DEBATE QUALITY ANALYZER AGENT (DQAA)                ║
║  Classification:   PRODUCTION-GRADE / ENTERPRISE / MULTI-TENANT        ║
║  Mutation Policy:  ADD-ONLY / VERSION-BUMPED                            ║
║  Execution Mode:   DETERMINISTIC + VALIDATED                            ║
║  Creative Interpretation by Agent:   FORBIDDEN                          ║
║  Assumption Filling:                 FORBIDDEN                          ║
║  Default Behavior:                   DENY ON AMBIGUITY                  ║
║  Failure Mode:     STOP_EXECUTION + LOG + ESCALATE                      ║
╚══════════════════════════════════════════════════════════════════════════╝
```

---

> **DESIGN PREMISE:**
> A debate is not merely a contest of opinions. It is a structured cognitive and
> communicative performance involving argument construction, logical coherence,
> rebuttal precision, evidence deployment, persuasion mechanics, and real-time
> adversarial adaptability. The Debate Quality Analyzer Agent (DQAA) exists to
> decompose this performance into measurable, auditable, multi-dimensional quality
> signals. It operates across live Dojo matches, recorded Group Discussions,
> championship debate rounds, and asynchronous debate-format submissions.
> It does NOT declare winners. It does NOT render subjective opinions.
> It produces structured, evidence-anchored, confidence-scored quality vectors
> that feed downstream intelligence, skill, ranking, and talent marketplace agents.

---

## ▶ SECTION 1 — AGENT IDENTITY (NON-NEGOTIABLE)

```yaml
AGENT_NAME:            Debate_Quality_Analyzer_Agent
AGENT_ID:              ECOSKILLER-AGT-DQAA-003
AGENT_VERSION:         v1.0.0
SYSTEM_ROLE:           Analyze the structural, logical, rhetorical, and adversarial
                       quality of debate-format human performance across Dojo matches,
                       Group Discussion sessions, championship debate rounds, and
                       structured argumentation submissions. Produce per-participant
                       multi-dimensional quality vectors, argument maps, rebuttal
                       precision scores, and structured audit trails. Feed downstream
                       skill, intelligence, belt, leaderboard, recruiter, and
                       innovation economy agents with debate quality signals.

PRIMARY_DOMAIN:        Debate Intelligence / Argumentation Quality /
                       Adversarial Communication Assessment /
                       Group Discussion Analytics
EXECUTION_MODE:        Deterministic + Validated
DATA_SCOPE:            Full session transcripts (per-speaker, time-stamped),
                       role assignments per participant, topic/motion metadata,
                       scenario rubric definitions, anti-cheat clearance flags,
                       historical debate quality baselines per user per domain,
                       audio quality signals (from transcription agent),
                       mentor observation inputs (in mentor-assisted mode)
TENANT_SCOPE:          Strict Isolation (zero cross-tenant data access)
FAILURE_POLICY:        Halt on ambiguity | Log incident | Escalate to Governance
PLATFORM:              Ecoskiller Antigravity
ARCHITECTURE_MODE:     Microservices + Event-Driven
SCALE_TARGET:          10M–100M users
ML_USAGE:              70–80% traditional ML
AI_USAGE:              20–30% LLM semantic reasoning
STACK_LOCK:            Enforced
DOMAIN_TRACKS_SERVED:  Arts | Commerce | Science | Technology | Administration
```

> **SEAL:** This identity block is frozen. Any rename, re-scoping, or role change
> requires MAJOR version bump + Governance Board approval. No exceptions.

---

## ▶ SECTION 2 — PURPOSE DECLARATION

### 2.1 The Problem This Agent Solves

The Ecoskiller Dojo system runs live Group Discussion (GD) matches, structured
debate championship rounds, and asynchronous argumentation exercises across all
five domain tracks. These are high-signal talent moments — a candidate's debate
performance is more predictive of real-world professional effectiveness than
most credential signals.

However, existing evaluation is:
- Mentor-dependent (subjective, inconsistent, limited bandwidth)
- Convergent (rubric-anchored, measures compliance rather than performance quality)
- Transcript-blind (no structural analysis of argument evolution across the session)
- Non-adversarial (does not measure how well a participant responds to opposition)

The **Debate Quality Analyzer Agent (DQAA)** solves this by:

1. **Decomposing the full session transcript** into per-speaker, per-turn argument units
2. **Scoring seven debate quality dimensions** independently per participant
3. **Building a live argument map** of claim-counterclaim-rebuttal chains
4. **Measuring adversarial adaptability** — how a participant responds when challenged
5. **Detecting logical fallacies and rhetorical manipulation** and flagging them explicitly
6. **Producing confidence-scored, evidence-anchored quality vectors** per participant
7. **Distinguishing debate performance from factual correctness** (DQAA does not fact-check)
8. **Feeding downstream agents** with structured debate quality signals for talent profiling,
   belt progression, championship ranking, and recruiter differentiation

### 2.2 What This Agent Does NOT Do

- It does NOT declare a debate winner (human mentor or structured outcome scoring does this)
- It does NOT fact-check claims made in the debate (factual accuracy = separate agent)
- It does NOT evaluate speaking style aesthetics (accent, voice quality, pace — not in scope)
- It does NOT access cross-tenant transcripts for comparison
- It does NOT make autonomous hiring, belt, or certification decisions
- It does NOT generate subjective narrative opinions about a participant
- It does NOT penalize participants for unpopular positions (position ≠ quality)
- It does NOT analyze non-debate-format content (skill tests, project submissions — use OREA/CDA)

### 2.3 Input Consumed

- Full session transcript with per-speaker attribution and timestamps
- Participant roster with role assignments per participant
- Topic/motion definition and stance assignments (for/against/neutral/GD-open)
- Domain track and scenario rubric metadata
- Session type (live GD match, championship debate, asynchronous debate, mentor-observed)
- Anti-cheat clearance flags per participant (from INTEGRITY_AGENT)
- Historical debate quality baselines per user per domain (from FEATURE_STORE_AGENT)
- Audio transcript confidence scores per speaker (from AUDIO_TRANSCRIPTION_AGENT)
- Mentor observation inputs (structured — if mentor-assisted mode enabled)
- Session timing metadata (turn duration, silence periods, interruption events)

### 2.4 Output Produced

- Per-participant seven-dimension debate quality vector
- Per-participant composite Debate Quality Index (DQI)
- Full session argument map (claim → support → rebuttal → counter-rebuttal chains)
- Logical fallacy detection report (per participant, per occurrence)
- Rhetorical quality analysis (persuasion device inventory per participant)
- Adversarial adaptability score (how well each participant responds to specific challenges)
- Participation equity report (talk-time, turn distribution, interruption dynamics)
- Growth delta per participant (DQI vs. prior baseline)
- Anomaly flags (collusion, scripted responses, passive participation gaming)
- Audit-ready records per participant and per session

### 2.5 Upstream Agents

| Agent | Data Provided |
|---|---|
| `SCENARIO_ENGINE` | Topic/motion, rubric package, scenario metadata, stance assignments |
| `MATCH_ENGINE` | Session context, participant roster, role assignments, session type |
| `AUDIO_TRANSCRIPTION_AGENT` | Per-speaker timestamped transcript, confidence scores |
| `ANTI_CHEAT_AGENT` | Per-participant integrity flags, scripted-response detection, collusion markers |
| `MENTOR_CONTROL_ENGINE` | Structured mentor observation inputs (mentor-assisted mode only) |
| `FEATURE_STORE_AGENT` | Historical DQI baselines per user per domain |
| `INTEGRITY_AGENT` | Fabrication signals, repeat-phrase detection, passive-gaming flags |

### 2.6 Downstream Agents

| Agent | Data Consumed |
|---|---|
| `OREA` | DQI as supplementary signal for holistic session evaluation |
| `BELT_ENGINE` | DQI vector for debate-track belt progression |
| `RATING_ENGINE` | Composite DQI for debate match rating update |
| `INTELLIGENCE_MEASUREMENT_ENGINE` | Debate intelligence dimension contribution |
| `FEATURE_STORE_AGENT` | DQI feature vectors for passive intelligence profile |
| `ANALYTICS_ENGINE` | Session argument maps, DQI trends, fallacy frequency analytics |
| `RECRUITER_SYSTEM` | Debate differentiation signal for candidate profiles |
| `OBSERVABILITY_AGENT` | Performance metrics, error rates, drift signals |
| `GOVERNANCE_AGENT` | Audit records, quarantine events, dispute resolution inputs |
| `GROWTH_ENGINE` | XP triggers, debate achievement badge checks |
| `LEADERBOARD_ENGINE` | DQI ranking signal (championship context) |
| `NOTIFICATION_SERVICE` | Per-participant DQI result delivery |
| `CDA` | Debate content as creativity signal input (conditional on argument novelty) |

---

## ▶ SECTION 3 — THE SEVEN DEBATE QUALITY DIMENSIONS

*These dimensions are locked at v1.0.0. Addition requires MAJOR version bump.*
*No dimension may be removed without full governance cycle and platform impact audit.*

---

### Dimension 1 — ARGUMENT STRUCTURE (ARG)

```yaml
DEFINITION:
  The degree to which each spoken contribution constitutes a well-formed
  argument — containing an identifiable claim, at least one supporting
  reason or evidence unit, and a logical connection between them.
  Measures structural completeness of argument construction.

WHAT IT MEASURES:
  - Claim identification rate (% of speaker turns containing identifiable claims)
  - Support density (reasons/evidence units per claim)
  - Claim-support coherence (logical connection between claim and support)
  - Conclusion presence (whether arguments resolve to a stated position)
  - Warrant explicitness (whether the logical bridge between claim and support is stated)

OBSERVABLE_SIGNALS:
  - Claim detection: declarative position statements per turn
  - Support detection: "because," "since," "given that," "evidence shows," data references
  - Warrant detection: "this means that," "therefore," "which demonstrates"
  - Conclusion detection: position restatement or implication statement at turn close
  - Structural completeness ratio: turns with full claim-support-conclusion vs. bare assertions

ARGUMENT_UNIT_CLASSIFICATION:
  COMPLETE:    Claim + Support + Warrant or Conclusion present
  PARTIAL:     Claim + Support (no warrant or conclusion)
  BARE:        Claim only (no support, no warrant, no conclusion)
  ASSERTION:   Opinion statement with no argument structure
  RHETORICAL:  Persuasion device with no argument structure (counted separately)

EXCLUSION_INDICATORS:
  - Questions that solicit information (not claims)
  - Transition phrases and acknowledgment statements
  - Procedural statements ("I'd like to address...")
  - Bare repetition of prior claims without new support

SCORING_RANGE:   0.0 – 100.0
WEIGHT_IN_DQI:   0.20
```

---

### Dimension 2 — LOGICAL COHERENCE (LOG)

```yaml
DEFINITION:
  The internal consistency and validity of the reasoning chain within
  and across a participant's contributions throughout the session.
  Measures whether the logic holds — not whether the position is correct.

WHAT IT MEASURES:
  - Intra-argument validity (does the support actually lead to the claim?)
  - Cross-argument consistency (does the participant contradict their own prior claims?)
  - Logical fallacy frequency (how many identifiable logical fallacies detected)
  - Premise-conclusion alignment (do stated premises support stated conclusions?)
  - Conditional reasoning accuracy (if-then structures used correctly)

OBSERVABLE_SIGNALS:
  - Fallacy detection (see Fallacy Detection Layer in Section 6.3)
  - Self-contradiction detection: semantic contradiction between current and prior claims
  - Non-sequitur detection: conclusions that do not follow from stated premises
  - Circular reasoning detection: claim used as its own support
  - Conditional chain validation: if-then structures flagged for logical validity

FALLACIES_DETECTED (primary list — locked at v1.0.0):
  - Ad Hominem (attacking the person, not the argument)
  - Straw Man (misrepresenting opponent's position)
  - False Dichotomy (presenting only two options when more exist)
  - Slippery Slope (unsupported chain of consequences)
  - Appeal to Authority (using authority without substance)
  - Hasty Generalization (drawing broad conclusions from limited examples)
  - Circular Reasoning (begging the question)
  - Non-Sequitur (conclusion doesn't follow from premises)
  - Post Hoc (correlation assumed to be causation)
  - Appeal to Emotion (emotion-only argument without logical support)
  - Bandwagon Fallacy (popularity as proof)
  - False Equivalence (treating non-equivalent things as equivalent)

SCORING_IMPACT:
  Each detected fallacy reduces LOG score proportionally to fallacy severity
  SEVERITY_MAP:
    Ad Hominem:          HIGH (direct attack on argument integrity)
    Straw Man:           HIGH (misrepresentation is adversarially unethical)
    False Dichotomy:     MEDIUM
    Slippery Slope:      MEDIUM
    All others:          LOW-MEDIUM (per occurrence)
  Multiple occurrences of same fallacy compound the reduction

EXCLUSION_INDICATORS:
  - Fallacies in rhetorical questions (flagged but penalized at 50% weight)
  - Incomplete utterances (transcription artifacts not penalized)
  - Domain-standard heuristics that resemble fallacies but are accepted field practice

SCORING_RANGE:   0.0 – 100.0
WEIGHT_IN_DQI:   0.18
```

---

### Dimension 3 — REBUTTAL PRECISION (REB)

```yaml
DEFINITION:
  The accuracy and effectiveness with which a participant identifies,
  addresses, and counters specific claims made by opponents.
  Measures adversarial argument engagement quality.
  High rebuttal precision = directly engaging the opponent's actual claim.
  Low rebuttal precision = responding to a tangential or misrepresented version.

WHAT IT MEASURES:
  - Claim targeting accuracy (does rebuttal address the actual opponent claim?)
  - Rebuttal completeness (does the rebuttal provide a reason, not just a denial?)
  - Straw man penalty (rebuttal addresses a misrepresented version of opponent's claim)
  - Dropped argument detection (opponent claims left unaddressed — passive omission)
  - Counter-evidence quality (rebuttal backed by new evidence or reasoning, not repetition)
  - Rebuttal speed (proximity of rebuttal to the original claim — closer = higher value)

ARGUMENT_MAP_INTEGRATION:
  DQAA builds a session-level argument map linking:
    CLAIM (speaker A, turn N) → REBUTTAL (speaker B, turn M) → COUNTER-REBUTTAL (speaker A, turn P)
  REB scoring is anchored to this argument map.
  Unaddressed chains (no rebuttal recorded) count as DROPPED_ARGUMENT events.

DROPPED_ARGUMENT_POLICY:
  If a direct opponent claim is never addressed in the session:
    - Recorded as DROPPED_ARGUMENT in argument map
    - REB score reduced proportionally (severity depends on claim weight in session)
    - Exception: time-constrained GD sessions where turn allocation prevents response

STRAW_MAN_PENALTY:
  If rebuttal is directed at a misrepresented version of opponent claim:
    - Detected via semantic comparison: opponent_claim_embedding vs rebuttal_target_embedding
    - If cosine similarity < STRAW_MAN_THRESHOLD (0.60): flag STRAW_MAN event
    - Counted in LOG dimension as fallacy AND in REB dimension as precision failure

EXCLUSION_INDICATORS:
  - General topic comments not directed at specific opponent claims
  - Opening statements (no prior claims to rebut yet)
  - Closing statements (rebuttal window has passed)

SCORING_RANGE:   0.0 – 100.0
WEIGHT_IN_DQI:   0.20
```

---

### Dimension 4 — EVIDENCE DEPLOYMENT (EVD)

```yaml
DEFINITION:
  The quality, relevance, specificity, and diversity of supporting
  evidence used to anchor claims across the session.
  Measures how well a participant grounds their arguments in
  verifiable, concrete, or domain-appropriate evidence units.

WHAT IT MEASURES:
  - Evidence unit count (distinct evidence items cited per claim)
  - Evidence type diversity (statistics, examples, analogies, citations, case studies)
  - Evidence specificity index (named, quantitative, concrete vs. vague generalizations)
  - Evidence relevance score (is the evidence topically relevant to the claim it supports?)
  - Evidence originality flag (novel evidence vs. clichéd domain-standard examples)
  - Evidence repetition penalty (same evidence unit used multiple times = lower value)

EVIDENCE_TYPE_TAXONOMY (locked at v1.0.0):
  STATISTICAL:    Numerical data, percentages, research-derived figures
  EXAMPLE:        Specific case, named instance, concrete historical example
  ANALOGY:        Comparative reasoning from a different domain (scored on analogy distance)
  CITATION:       Named source, study, authority (scored on source relevance)
  CASE_STUDY:     Extended real-world scenario used as evidence
  HYPOTHETICAL:   Thought experiment (flagged separately — valid but lower evidentiary weight)
  ANECDOTAL:      Personal experience (valid but lowest evidentiary weight per unit)

EVIDENCE_WEIGHT_MAP:
  STATISTICAL:   1.00 (highest — if plausible in context)
  CASE_STUDY:    0.90
  EXAMPLE:       0.80
  CITATION:      0.75 (validity not fact-checked — only relevance and specificity scored)
  ANALOGY:       0.65 (× analogy_distance_multiplier from CDA flexibility model)
  HYPOTHETICAL:  0.50
  ANECDOTAL:     0.40

FACT_CHECK_POLICY:
  DQAA does NOT verify the factual accuracy of evidence claims.
  Factual correctness is out of scope — DQAA scores structural deployment quality.
  If FACT_CHECK_AGENT is available in pipeline: its output may be attached as metadata.
  DQAA scoring remains independent of fact-check result.

EXCLUSION_INDICATORS:
  - Evidence stated with no connection to a claim (floating evidence)
  - Repeated evidence units (same example/statistic cited again = diminishing returns)
  - Evidence in procedural or acknowledgment statements

SCORING_RANGE:   0.0 – 100.0
WEIGHT_IN_DQI:   0.15
```

---

### Dimension 5 — ADVERSARIAL ADAPTABILITY (ADA)

```yaml
DEFINITION:
  The ability of a participant to detect pressure points in their own
  argument position and adapt, refine, or defend under direct opposition.
  Measures real-time cognitive flexibility under adversarial conditions.
  This is distinct from rebuttal precision (which measures attacking
  opponent claims). ADA measures how well a participant defends their own.

WHAT IT MEASURES:
  - Position refinement under pressure (productive update vs. collapse or rigidity)
  - Challenge absorption rate (how many direct challenges received and meaningfully addressed)
  - Concession quality (strategic concessions with maintained core position vs. collapse)
  - Pressure escalation response (does quality maintain, improve, or degrade under pressure?)
  - Argument repair capability (can the participant recover from a successful rebuttal?)
  - Consistency under sustained opposition (maintaining position coherence across multiple challenges)

PRESSURE_EVENT_DETECTION:
  A PRESSURE_EVENT is logged when:
    - Opponent delivers a COMPLETE or PARTIAL rebuttal targeting this participant's claim
    - Mentor issues a challenge prompt (mentor-assisted mode)
    - Multiple opponents address the same claim simultaneously (GD context)
  Each PRESSURE_EVENT creates an ADA scoring opportunity for the challenged participant.

RESPONSE_CLASSIFICATION:
  ADAPTIVE_REFINEMENT:  Participant updates position with new reasoning — POSITIVE
  STRATEGIC_CONCESSION: Participant concedes a sub-point while maintaining core — POSITIVE
  EFFECTIVE_DEFENSE:    Participant defends original claim with new support — POSITIVE
  RIGID_RESTATEMENT:    Participant repeats same claim without new support — NEUTRAL-NEGATIVE
  POSITION_COLLAPSE:    Participant abandons position without strategic rationale — NEGATIVE
  DEFLECTION:           Participant avoids the challenge entirely — NEGATIVE
  TOPIC_SWITCH:         Participant changes subject to escape challenge — NEGATIVE

PRESSURE_TRAJECTORY_ANALYSIS:
  DQAA tracks ADA score across session timeline:
    Early session vs. Late session ADA (does pressure resistance hold or degrade?)
  This produces a PRESSURE_TRAJECTORY_SIGNAL used by downstream belt/intelligence agents.

EXCLUSION_INDICATORS:
  - Natural topic progression (not a response to pressure)
  - Opening arguments (no prior pressure exists)

SCORING_RANGE:   0.0 – 100.0
WEIGHT_IN_DQI:   0.13
```

---

### Dimension 6 — PERSUASION MECHANICS (PER)

```yaml
DEFINITION:
  The quality and diversity of rhetorical and persuasion devices
  deployed by the participant — including structural persuasion
  (argument ordering, framing), emotional resonance (appropriate
  contextual appeal), and audience orientation techniques.
  Measures HOW the argument is constructed to persuade, not WHETHER
  the position is persuasive to any particular evaluator.

WHAT IT MEASURES:
  - Rhetorical device diversity (variety of techniques deployed)
  - Framing quality (how the issue is positioned before argument begins)
  - Signposting clarity (structural markers guiding the audience)
  - Ethos construction (credibility signals — domain knowledge demonstrations)
  - Pathos deployment (contextually appropriate emotional appeal)
  - Logos alignment (logical persuasion consistent with LOG dimension)
  - Call-to-action clarity (does the participant conclude with a clear position?)
  - Contrast and antithesis use (effective use of opposition as persuasion device)

RHETORICAL_DEVICE_INVENTORY (scored per occurrence, diminishing returns on repetition):
  Framing:            Initial positioning of the topic to favor one's argument
  Signposting:        "First..., Second..., Finally..." (structural navigation)
  Anaphora:           Deliberate repetition of opening phrase for emphasis
  Rhetorical Question: Question used to make a point (not seek information)
  Contrast/Antithesis: "Not X, but Y" structures
  Concession-Pivot:   "While X is true, Y outweighs it..." (sophisticated device)
  Analogy:            Comparison to make complex point accessible (also counted in EVD)
  Tricolon:           Rule of three — memorable grouping device
  Ethos Marker:       Domain expertise signal ("As demonstrated by X research...")
  Pathos Marker:      Contextually appropriate appeal to consequence or value
  Strawman Reframe:   NEGATIVE — misrepresentation of opponent's position (penalized)
  Emotional Manipulation: NEGATIVE — non-contextual emotional appeal (penalized)

DEVICE_BOUNDARY_RULES:
  Pathos is scored POSITIVE only when:
    - Contextually relevant to the debate topic
    - Does not substitute for logical argument (if it replaces logic → LOG penalized)
  Emotional Manipulation is scored NEGATIVE when:
    - Emotion is the primary and only argument (no supporting logic)
    - Designed to distract from argument weakness

EXCLUSION_INDICATORS:
  - Filler phrases ("um," "you know," "basically")
  - Acknowledgment statements ("Good point,")
  - Transition phrases with no persuasion function

SCORING_RANGE:   0.0 – 100.0
WEIGHT_IN_DQI:   0.08
```

---

### Dimension 7 — SESSION ENGAGEMENT (ENG)

```yaml
DEFINITION:
  The quality of a participant's active presence and collaborative
  contribution across the full session — measuring equitable participation,
  responsiveness to the group dynamic, and constructive session-building
  behavior. Distinct from argument quality (scored in other dimensions).
  Measures the participant AS A SESSION CONTRIBUTOR, not just as an individual arguer.

WHAT IT MEASURES:
  - Proportional participation (talk-time relative to fair-share in group context)
  - Turn-taking behavior (appropriate entry, respectful of others' turns)
  - Active listening signals (references to prior speaker contributions)
  - Session building (moves that advance the group discussion productively)
  - Interruption behavior (frequency and quality of interruptions)
  - Silence penalty (extended non-participation periods)
  - Cross-speaker acknowledgment (building on others' points vs. ignoring them)
  - Topic coherence contribution (staying on-topic vs. introducing tangents)

PARTICIPATION_EQUITY_ANALYSIS:
  In a GD with N participants:
    FAIR_SHARE_TALK_TIME = total_session_duration / N
    ACTUAL_TALK_TIME tracked per participant
    PARTICIPATION_RATIO = actual / fair_share

  PARTICIPATION_BANDS:
    OVER_PARTICIPATOR:    ratio > 1.8 (dominance signal — ENG penalized moderately)
    BALANCED:             ratio 0.7 – 1.8 (healthy range)
    UNDER_PARTICIPATOR:   ratio < 0.7 (passive signal — ENG penalized significantly)
    SILENT:               ratio < 0.3 (severe engagement failure)

INTERRUPTION_CLASSIFICATION:
  CONSTRUCTIVE: Interruption introduces new relevant evidence or prevents factual error
  ADDITIVE:     Interruption continues and strengthens the interrupted speaker's point
  DISRUPTIVE:   Interruption changes topic or breaks argument flow (penalized)
  DOMINATING:   Repeated interruptions preventing opponent from completing arguments (penalized)

SESSION_TYPE_CALIBRATION:
  GROUP_DISCUSSION:  ENG weight HIGHER — group dynamics are primary evaluation context
  FORMAL_DEBATE:     ENG weight LOWER — structured turns reduce GD-style engagement signal
  CHAMPIONSHIP:      ENG weight MEDIUM — competitive format but group signal still relevant

EXCLUSION_INDICATORS:
  - Technical disconnection events (network drops — not scored as silence)
  - Moderator-enforced silence (time limits, mentor pauses — not penalized)
  - Accessibility accommodations (extended processing time — not penalized)

SCORING_RANGE:   0.0 – 100.0
WEIGHT_IN_DQI:   0.06
```

---

### Dimension Weight Summary

| Dimension | Code | Weight | Core Rationale |
|---|---|---|---|
| Argument Structure | ARG | 0.20 | Foundation of all debate quality — structural completeness |
| Logical Coherence | LOG | 0.18 | Validity of reasoning chain — separates debate from rhetoric |
| Rebuttal Precision | REB | 0.20 | Adversarial engagement — the defining debate competency |
| Evidence Deployment | EVD | 0.15 | Grounding — separates assertion from substantiated argument |
| Adversarial Adaptability | ADA | 0.13 | Real-time cognitive flex under pressure — high talent signal |
| Persuasion Mechanics | PER | 0.08 | Rhetorical craft — amplifier, not foundation |
| Session Engagement | ENG | 0.06 | Group contribution — context-dependent calibration |
| **TOTAL** | | **1.00** | |

> **GOVERNANCE RULE:** Dimension weights are locked at v1.0.0.
> Weight recalibration requires MAJOR version bump, A/B validation on ≥ 10,000
> labeled debate sessions, inter-rater reliability study, and Governance Board approval.

---

## ▶ SECTION 4 — INPUT CONTRACT (STRICT)

### 4.1 Input Schema

```json
INPUT_SCHEMA: {
  "required_fields": [
    "session_id",
    "tenant_id",
    "session_type",
    "session_timestamp_utc",
    "session_duration_seconds",
    "domain_track",
    "topic_motion",
    "session_token",
    "rubric_reference_id",
    "rubric_version",
    "participant_roster",
    "full_transcript",
    "dqaa_trigger_mode",
    "anti_cheat_clearance_per_participant"
  ],
  "optional_fields": [
    "match_id",
    "championship_context_flag",
    "mentor_id",
    "mentor_observation_inputs",
    "stance_assignments",
    "session_timing_metadata",
    "audio_quality_flags",
    "localization_code",
    "prior_dqi_references",
    "accessibility_accommodations"
  ],

  "field_definitions": {

    "session_id": "UUID-v4 — unique identifier for the debate session",
    "tenant_id": "UUID-v4 — must match session_token claims",
    "session_type": "enum [live_gd_match, championship_debate, async_debate_submission, mentor_observed_debate, practice_session]",
    "session_timestamp_utc": "ISO-8601 UTC — session start time",
    "session_duration_seconds": "integer — total session duration",
    "domain_track": "enum [Arts, Commerce, Science, Technology, Administration]",
    "topic_motion": "string — the debate topic or motion, max 2000 chars",
    "session_token": "JWT — validated, non-expired, tenant-bound",
    "rubric_reference_id": "UUID — rubric active for this session",
    "rubric_version": "string — must match active rubric in SCENARIO_ENGINE",

    "participant_roster": [
      {
        "user_id": "UUID-v4",
        "participant_alias": "string (display name for transcript attribution)",
        "role_assignment": "enum [FOR, AGAINST, NEUTRAL, CHAIR, OBSERVER, GD_OPEN]",
        "anti_cheat_clearance": "enum [CLEARED, FLAGGED_LOW, FLAGGED_HIGH, FLAGGED_CRITICAL]"
      }
    ],

    "full_transcript": [
      {
        "turn_id": "integer (sequential, 1-based)",
        "speaker_user_id": "UUID-v4 (must match participant_roster)",
        "turn_start_seconds": "float — time from session start",
        "turn_end_seconds": "float",
        "turn_duration_seconds": "float",
        "utterance_text": "string — normalized speaker utterance",
        "transcript_confidence": "float (0–1) — from AUDIO_TRANSCRIPTION_AGENT",
        "interruption_flag": "boolean — true if speaker interrupted another",
        "interrupted_speaker_id": "UUID-v4 or null"
      }
    ],

    "dqaa_trigger_mode": "enum [realtime_live, async_post_session, batch_scheduled]",

    "stance_assignments": {
      "user_id": "assigned_stance (FOR / AGAINST / NEUTRAL / GD_OPEN)"
    },

    "session_timing_metadata": {
      "planned_duration_seconds": "integer",
      "actual_duration_seconds": "integer",
      "moderator_interruptions": "integer",
      "technical_pause_periods": [{"start": "float", "end": "float"}]
    },

    "mentor_observation_inputs": [
      {
        "mentor_id": "UUID-v4",
        "observation_type": "enum [flag_argument, flag_fallacy, flag_excellent, flag_concern]",
        "target_user_id": "UUID-v4",
        "target_turn_id": "integer",
        "observation_note": "string — max 500 chars",
        "timestamp_utc": "ISO-8601"
      }
    ],

    "prior_dqi_references": {
      "user_id": "prior_dqa_evaluation_id (UUID or null per participant)"
    },

    "accessibility_accommodations": {
      "user_id": "accommodation_type (extended_turn_time / screen_reader / caption_only)"
    }
  },

  "validation_rules": [
    "session_id must be UUID-v4",
    "tenant_id must match session_token claims — strict isolation",
    "session_type must be in allowed enum",
    "domain_track must be in allowed enum",
    "topic_motion must be non-null, non-empty, min 10 chars",
    "rubric_version must resolve as active in SCENARIO_ENGINE",
    "participant_roster must contain minimum 2 participants",
    "participant_roster must contain maximum 15 participants",
    "all user_ids in participant_roster must be active verified users in tenant_id",
    "full_transcript must contain minimum 10 turns",
    "all speaker_user_ids in full_transcript must match participant_roster",
    "turn_ids must be sequential with no gaps",
    "turn_start/end times must be non-overlapping per speaker",
    "anti_cheat_clearance_per_participant must be present for every participant",
    "if any participant has FLAGGED_CRITICAL: that participant excluded from scoring, session proceeds for others",
    "dqaa_trigger_mode must be in allowed enum",
    "if session_type = mentor_observed_debate: mentor_id must be validated certified mentor",
    "rubric_reference_id domain binding must match domain_track"
  ],

  "security_checks": [
    "Tenant isolation: all user_ids must belong to tenant_id",
    "No cross-tenant participant mixing: REJECT if detected",
    "Session token: JWT signature validation + expiry + scope check",
    "PII scan on utterance_text: detect and redact before logging",
    "Rate limit: max 1,000 session evaluations per tenant per day",
    "RBAC: executing service must hold DQAA_ANALYSIS_SERVICE role",
    "Mentor validation: mentor_id must hold CERTIFIED_DEBATE_MENTOR role for domain"
  ],

  "domain_checks": [
    "rubric_reference_id domain binding must match domain_track",
    "topic_motion must pass domain relevance classifier (min 0.60 confidence for domain match)",
    "championship_context_flag can only be true if session_id resolves to active championship event"
  ]
}
```

### 4.2 Anti-Cheat Flag Handling Per Participant

| Flag Level | Action |
|---|---|
| `CLEARED` | Full normal scoring |
| `FLAGGED_LOW` | Score normally; annotate output with LOW_RISK flag |
| `FLAGGED_HIGH` | Score in quarantine mode; suppress belt/leaderboard/innovation events |
| `FLAGGED_CRITICAL` | EXCLUDE participant from scoring entirely; LOG_INTEGRITY_CRITICAL; other participants scored normally |

### 4.3 Transcript Quality Floor

```yaml
MINIMUM_TRANSCRIPT_CONFIDENCE: 0.70 per turn
BELOW_THRESHOLD_HANDLING:
  - Turns with confidence < 0.70: flagged as LOW_CONFIDENCE_TURN
  - If > 30% of a participant's turns are low-confidence:
      composite DQI confidence reduced by 20% for that participant
  - If > 60% of a participant's turns are low-confidence:
      participant marked as INSUFFICIENT_TRANSCRIPT_QUALITY
      DQI not computed for that participant
      LOG_TRANSCRIPT_QUALITY_FAILURE
```

### 4.4 Validation Failure Protocol

```
ON VALIDATION FAILURE:
  1. STOP_EXECUTION immediately — no partial DQI vectors produced
  2. DO NOT write any intermediate scoring state
  3. LOG: validation_failure_event (append-only) → GOVERNANCE_AGENT
  4. EMIT: error_event → OBSERVABILITY_AGENT
  5. RETURN: structured error payload {failure_code, field_path, remediation_hint}
  6. ESCALATE: if failure_code = SECURITY class → COMPLIANCE_AGENT + SECURITY_TEAM
  7. NO silent failures permitted under any condition
```

---

## ▶ SECTION 5 — OUTPUT CONTRACT (STRICT)

### 5.1 Output Schema

```json
OUTPUT_SCHEMA: {
  "session_evaluation_id": "UUID-v4",
  "session_id":            "UUID-v4 (from input)",
  "tenant_id":             "UUID-v4 (from input)",
  "domain_track":          "string",
  "session_type":          "string",
  "total_participants_evaluated": "integer",
  "excluded_participants": ["array of user_ids excluded due to FLAGGED_CRITICAL"],

  "argument_map": {
    "map_id":              "UUID-v4",
    "total_claim_units":   "integer",
    "total_rebuttal_chains": "integer",
    "total_dropped_arguments": "integer",
    "chains": [
      {
        "chain_id":        "integer",
        "initiating_turn_id": "integer",
        "initiating_speaker": "UUID",
        "claim_text_excerpt": "string (max 200 chars)",
        "claim_type":      "enum [COMPLETE, PARTIAL, BARE, ASSERTION]",
        "rebuttal_turns":  [
          {
            "rebuttal_turn_id": "integer",
            "rebutter_id":     "UUID",
            "rebuttal_type":   "enum [DIRECT, STRAW_MAN, PARTIAL, DROPPED]",
            "counter_rebuttal_turn_id": "integer or null"
          }
        ],
        "chain_resolution": "enum [RESOLVED, UNRESOLVED, DROPPED, CONCEDED]"
      }
    ]
  },

  "per_participant_evaluations": [
    {
      "user_id":           "UUID-v4",
      "participant_alias": "string",
      "role_assignment":   "string",
      "anti_cheat_clearance": "string",
      "quarantine_mode":   "boolean",

      "dimension_scores": {
        "argument_structure": {
          "score":            "float (0–100)",
          "confidence":       "float (0–1)",
          "complete_arguments": "integer",
          "partial_arguments":  "integer",
          "bare_assertions":    "integer",
          "evidence_segments":  ["array of transcript excerpts"],
          "rationale":          "string — max 400 chars"
        },
        "logical_coherence": {
          "score":            "float (0–100)",
          "confidence":       "float (0–1)",
          "fallacies_detected": [
            {
              "fallacy_type":   "string",
              "severity":       "enum [LOW, MEDIUM, HIGH]",
              "turn_id":        "integer",
              "excerpt":        "string",
              "score_impact":   "float"
            }
          ],
          "self_contradictions_detected": "integer",
          "reasoning_consistency_score":  "float",
          "rationale":          "string"
        },
        "rebuttal_precision": {
          "score":              "float (0–100)",
          "confidence":         "float (0–1)",
          "rebuttals_delivered": "integer",
          "direct_rebuttals":    "integer",
          "straw_man_rebuttals": "integer",
          "dropped_opponent_claims": "integer",
          "rebuttal_chain_ids":  ["array of chain_ids involving this participant"],
          "rationale":           "string"
        },
        "evidence_deployment": {
          "score":              "float (0–100)",
          "confidence":         "float (0–1)",
          "evidence_units":     [
            {
              "type":           "string (STATISTICAL/EXAMPLE/ANALOGY/etc.)",
              "turn_id":        "integer",
              "excerpt":        "string",
              "relevance_score":"float",
              "weighted_value": "float"
            }
          ],
          "evidence_type_diversity": "integer (distinct types used)",
          "evidence_repetition_count": "integer",
          "rationale":          "string"
        },
        "adversarial_adaptability": {
          "score":              "float (0–100)",
          "confidence":         "float (0–1)",
          "pressure_events_received": "integer",
          "pressure_responses": [
            {
              "pressure_event_turn_id": "integer",
              "response_turn_id":       "integer or null",
              "response_classification": "string",
              "score_contribution":      "float"
            }
          ],
          "pressure_trajectory": "enum [IMPROVING, STABLE, DEGRADING, INSUFFICIENT_DATA]",
          "rationale":          "string"
        },
        "persuasion_mechanics": {
          "score":              "float (0–100)",
          "confidence":         "float (0–1)",
          "devices_inventory":  [
            {
              "device_type":    "string",
              "count":          "integer",
              "turn_ids":       ["array"],
              "score_contribution": "float"
            }
          ],
          "negative_devices_flagged": ["array of manipulative device instances"],
          "rationale":          "string"
        },
        "session_engagement": {
          "score":              "float (0–100)",
          "confidence":         "float (0–1)",
          "talk_time_seconds":  "float",
          "fair_share_seconds": "float",
          "participation_ratio":"float",
          "participation_band": "enum [OVER, BALANCED, UNDER, SILENT]",
          "interruptions_initiated": "integer",
          "interruption_types": {"CONSTRUCTIVE": "int", "DISRUPTIVE": "int", "DOMINATING": "int"},
          "cross_speaker_references": "integer",
          "rationale":          "string"
        }
      },

      "composite_dqi": {
        "score":              "float (0–100)",
        "confidence":         "float (0–1)",
        "debate_quality_band":"enum [DISTINGUISHED, ACCOMPLISHED, DEVELOPING, NOVICE]",
        "domain_percentile":  "float (0–100)"
      },

      "growth_delta": {
        "prior_dqa_evaluation_id": "UUID or null",
        "prior_dqi_score":         "float or null",
        "current_dqi_score":       "float",
        "delta":                   "float",
        "growth_signal":           "enum [BREAKTHROUGH, GROWING, STABLE, REGRESSING, INSUFFICIENT_DATA]"
      },

      "transcript_quality_flag": "enum [SUFFICIENT, LOW_CONFIDENCE_PARTIAL, INSUFFICIENT]",
      "session_turns_analyzed":  "integer"
    }
  ],

  "session_analytics": {
    "participation_equity_index":  "float (0–1) — 1.0 = perfect equal participation",
    "dominant_speaker_id":         "UUID or null",
    "silent_speaker_ids":          ["array of UUID"],
    "total_fallacies_session":     "integer",
    "total_argument_units_session":"integer",
    "most_contested_chain_id":     "integer (chain with most rebuttal activity)",
    "session_argument_depth":      "float (avg chain length across all chains)"
  },

  "model_version":              "string",
  "dqaa_prompt_version":        "string",
  "rubric_version_applied":     "string",
  "audit_reference":            "UUID-v4",
  "evaluation_timestamp_utc":   "ISO-8601",
  "next_trigger_events":        ["array of downstream event keys"]
}
```

### 5.2 Output Guarantees

- All seven dimension scores per participant MUST be present or execution REJECTED
- `composite_dqi.confidence` < 0.62 → flag; DO NOT trigger belt or leaderboard
- `quarantine_mode` = true per participant → suppress belt/leaderboard/innovation events for that participant
- `audit_reference` MANDATORY — no anonymous outputs
- `argument_map` MANDATORY in every session output
- Partial outputs FORBIDDEN — all-or-nothing contract per session
- Session evaluation proceeds for non-FLAGGED_CRITICAL participants even if some are excluded

### 5.3 Debate Quality Band Definitions

| Band | DQI Range | Meaning |
|---|---|---|
| `DISTINGUISHED` | 80 – 100 | Consistently structured, logically precise, strong rebuttal quality — top performer tier |
| `ACCOMPLISHED` | 60 – 79 | Reliable argument construction with clear rebuttal engagement — solid debate competency |
| `DEVELOPING` | 40 – 59 | Emerging structure with identifiable gaps in logic or rebuttal — growth-stage debater |
| `NOVICE` | 0 – 39 | Primarily assertion-based, limited structure, minimal adversarial engagement |

---

## ▶ SECTION 6 — ML / AI LOGIC LAYER

### 6.1 Architecture Split (Enforced)

```
75% Traditional ML — DETECTION, SCORING & MAPPING LAYER:
  - Argument unit detection and classification (NLP classifier)
  - Claim-support-warrant-conclusion boundary detection (sequence labeling)
  - Logical fallacy detection (multi-label classifier per fallacy type)
  - Rebuttal precision matching (claim-rebuttal semantic alignment scoring)
  - Argument map construction (graph-building from claim-rebuttal pairs)
  - Evidence unit detection and type classification
  - Adversarial adaptability scoring (pressure-event-response mapping)
  - Session engagement metrics (talk-time, interruption, participation analysis)
  - Confidence calibration (Platt Scaling on all models)
  - Drift detection (distribution monitoring on DQI outputs)
  - Self-contradiction detection (cross-turn semantic consistency check)
  - Straw man detection (claim-rebuttal semantic distance scoring)

25% LLM / Semantic Reasoning — EVIDENCE, RATIONALE & EDGE CASES:
  - Evidence segment extraction per dimension (specific transcript excerpts)
  - Dimension rationale generation (structured plain-language explanation ≤ 400 chars)
  - Fallacy rationale labeling (naming why a detected fallacy is a fallacy)
  - Ambiguous argument boundary resolution (when ML confidence < threshold)
  - Localization-sensitive interpretation (cultural debate style calibration)
  - Complex rebuttal chain labeling (nuanced chain-resolution classification)
```

> **ABSOLUTE RULE:** LLM layer provides evidence, rationale, and edge-case advisory.
> LLM NEVER computes dimension scores.
> LLM NEVER classifies argument units independently.
> All scores come from ML models only.
> LLM timeout → proceed with ML scores; flag AI_PARTIAL_MODE.

### 6.2 ML Model Specifications

```yaml
ARGUMENT_UNIT_DETECTOR:
  type:       Sequence labeling model (BiLSTM-CRF or transformer-based token classifier)
  task:       Token-level classification: CLAIM / SUPPORT / WARRANT / CONCLUSION / OTHER
  granularity: Per-turn segmentation (not per-session)
  output:     Argument unit boundaries + type labels + confidence per token
  training_data: Annotated debate transcripts (min 50,000 labeled argument units per domain)
  training_frequency: Monthly

FALLACY_DETECTOR:
  type:       Multi-label classifier (one classifier per fallacy type in locked taxonomy)
  input:      Full turn text + prior 3 turns for context window
  output:     Per-fallacy binary flag + confidence + severity score
  training_data: Labeled fallacy instances per type (min 5,000 per fallacy type)
  training_frequency: Monthly
  context_window: 3 prior turns (to detect fallacies requiring conversational context)

REBUTTAL_PRECISION_MATCHER:
  type:       Siamese network / bi-encoder semantic similarity model
  input:      (opponent_claim_embedding, rebuttal_text_embedding) pair
  output:     Precision score (0–1) + straw_man_flag (if similarity < 0.60)
  straw_man_threshold: 0.60 (configurable via governance — default locked)
  training_data: Claim-rebuttal pairs labeled by certified debate experts
  training_frequency: Monthly

ARGUMENT_MAP_BUILDER:
  type:       Graph construction algorithm (rule-based + ML-assisted edge classification)
  input:      All detected claim units + rebuttal precision scores
  output:     Directed argument graph (DAG structure) per session
  algorithm:  Greedy chain builder with ML-scored edge weights
  dropped_arg_detection: Claim nodes with no outgoing or incoming rebuttal edges = DROPPED

EVIDENCE_UNIT_CLASSIFIER:
  type:       Multi-class classifier (evidence taxonomy: STATISTICAL, EXAMPLE, ANALOGY, etc.)
  input:      Support-type argument units from ARG dimension output
  output:     Evidence type label + specificity score + relevance score
  training_frequency: Monthly

ADA_PRESSURE_MAPPER:
  type:       Event-response sequence classifier
  input:      (pressure_event_turn, response_turn) pairs
  output:     Response classification per RESPONSE_CLASSIFICATION taxonomy
  pressure_detection: Rule-based + semantic (opponent claim targeting this participant)
  training_frequency: Monthly

ENGAGEMENT_ANALYTICS_ENGINE:
  type:       Deterministic computation (talk-time, participation ratio, interruption classification)
  input:      Session timing metadata + transcript turn boundaries
  interruption_classifier: Binary classifier (CONSTRUCTIVE vs DISRUPTIVE vs DOMINATING)
  cross_speaker_reference_detector: NER-based mention detection for prior speaker references

CONFIDENCE_CALIBRATION:
  method:     Platt Scaling per model
  target:     Brier score < 0.10 per domain per dimension
  recalibration: Monthly or on drift signal

DRIFT_DETECTION:
  metric:     Population Stability Index (PSI) per dimension per domain
  threshold:  PSI > 0.2 triggers retraining alert
  frequency:  Daily batch
```

### 6.3 Fallacy Detection Operational Layer

```yaml
FALLACY_DETECTION_PIPELINE:

  STEP_1 — PRE_CLASSIFICATION:
    Input: Full utterance text + 3-turn context window
    Model: Multi-label fallacy classifier (12 fallacy types)
    Output: Per-fallacy confidence score (0–1)

  STEP_2 — THRESHOLD_FILTER:
    Minimum confidence to flag: 0.72 per fallacy type
    Below threshold: Not reported (reduces false positives)

  STEP_3 — LLM_RATIONALE (conditional):
    If confidence 0.72 – 0.85: LLM generates rationale and confirms
    If confidence > 0.85: ML classification stands, LLM extracts excerpt only
    LLM cannot REMOVE a flagged fallacy — it may only confirm with evidence

  STEP_4 — SEVERITY_ASSIGNMENT:
    Per SEVERITY_MAP defined in Dimension 2 (LOG)
    Severity modulates score impact on LOG dimension

  STEP_5 — APPEAL:
    If participant (post-session) disputes a fallacy flag:
    → APPEALS workflow triggers (GOVERNANCE_AGENT)
    → Certified human evaluator reviews
    → GOVERNANCE_AGENT decision is final and logged immutably

FALLACY_CONFIDENCE_TRANSPARENCY:
  All flagged fallacies in output include:
    - fallacy_type
    - ML confidence score
    - LLM rationale excerpt
    - turn_id reference
    - score_impact
  Full transparency for disputes and audit
```

### 6.4 LLM Governance

```yaml
LLM_USAGE_SCOPE:
  permitted:
    - Evidence segment extraction (specific transcript excerpts per dimension)
    - Dimension rationale generation (≤ 400 chars per dimension per participant)
    - Fallacy rationale labeling (why this is a fallacy of this type)
    - Ambiguous argument boundary resolution advisory (when ML confidence < 0.65)
    - Rebuttal chain resolution labeling (nuanced chain classification)
    - Localization-sensitive interpretation (debate style calibration for non-EN)

  forbidden:
    - Computing any dimension score
    - Classifying argument units independently
    - Removing ML-detected fallacies from output
    - Determining winner of debate
    - Accessing user identity, history, or profile during analysis
    - Cross-tenant transcript access
    - Making belt, award, or certification decisions
    - Generating narrative opinions about participant character or ability

PROMPT_GOVERNANCE:
  all_prompts_version_tagged:   YES (dqaa_prompt_version in every output)
  temperature:                  0.0 for evidence extraction / 0.1 for rationale generation
  output_format:                JSON-only (no prose outputs from LLM layer)
  max_tokens_per_call:          700 per participant per dimension
  prompt_injection_defense:     Utterance text sanitized before LLM call
  context_isolation:            LLM receives only transcript excerpt + dimension spec
                                NO user identity, NO tenant context, NO session history
  llm_timeout_fallback:         Proceed with ML scores only; flag AI_PARTIAL_MODE
```

---

## ▶ SECTION 7 — SCALABILITY DESIGN

```yaml
EXPECTED_RPS:           800 session evaluations/sec (peak championship)
                        Note: Each session evaluation = N participant evaluations
                        Effective participant evaluations: 800 × avg 6 = ~4,800/sec

LATENCY_TARGET:
  async_post_session:   p95 < 8 seconds per session
  realtime_live:        p95 < 12 seconds per turn batch (incremental scoring)
  batch_scheduled:      Best-effort, no hard SLA

MAX_CONCURRENCY:        5,000 concurrent session evaluation jobs

QUEUE_STRATEGY:
  primary_topic:        Kafka topic per domain_track (5 topics)
  priority_lanes:
    CRITICAL:           Championship live debate evaluations
    HIGH:               Dojo live GD match post-session
    STANDARD:           Practice sessions, async submissions
    BATCH:              Scheduled re-evaluations, migration imports

INCREMENTAL_REALTIME_SCORING:
  mode:                 Enabled for live matches (realtime dqaa_trigger_mode)
  behavior:             Score emitted per completed turn-batch (every 5 turns)
  interim_flag:         INTERIM_SCORE — prevents official downstream events
  final_score:          Computed after full transcript received and validated
  use_case:             Live championship display (audience view only)

SCALING_MODE:           Horizontal auto-scaling, stateless workers per domain_track
IDEMPOTENCY:
  key:                  session_id (re-evaluation of same session returns cached result)
  cache_ttl:            72 hours (Redis)
  cache_invalidation:   On rubric_version change or corpus_version update

RETRY_POLICY:           3 retries, exponential backoff (2s → 8s → 32s)
DEAD_LETTER_QUEUE:      Failed sessions after 3 retries → DLQ + alert OBSERVABILITY_AGENT

SESSION_SIZE_LIMITS:
  min_participants:     2
  max_participants:     15
  max_transcript_turns: 2,000
  max_utterance_length: 5,000 chars per turn
  above_limits:         REJECT with PAYLOAD_TOO_LARGE error
```

---

## ▶ SECTION 8 — SECURITY ENFORCEMENT

```yaml
TENANT_ISOLATION:
  enforcement:          RLS at database layer + application layer
  cross_tenant_check:   All user_ids in participant_roster must belong to tenant_id
  transcript_isolation: Session transcripts never stored in global corpus
  failure_action:       REJECT + LOG_SECURITY_CRITICAL + ESCALATE COMPLIANCE_AGENT

DOMAIN_ISOLATION:
  rule:                 Rubric domain binding must match declared domain_track
  corpus_routing:       Domain-specific baseline used (no cross-domain corpus mixing)
  failure_action:       REJECT + LOG_DOMAIN_VIOLATION

TRANSCRIPT_PRIVACY:
  utterance_text:       PII-scanned before logging — PII redacted in audit records
  raw_transcript:       NOT stored in global corpus (debate transcripts are private per tenant)
  debate_content:       Tenant-owned — not used for DQAA model training without explicit opt-in
  argument_map:         Stored tenant-isolated — accessible only by tenant admin + participant

ROLE_AUTHORIZATION:
  system_role:          DQAA_ANALYSIS_SERVICE
  mentor_access:        CERTIFIED_DEBATE_MENTOR (domain-specific certification required)
  evaluator_role:       EVALUATOR (for quarantine review and fallacy appeals)
  tenant_admin:         Read-only access to session evaluations for their tenant

ENCRYPTION:
  utterance_text_at_rest: AES-256
  argument_map_at_rest:   AES-256
  transit:                TLS 1.3 minimum
  audit_logs:             Encrypted + append-only

ACCESS_LOG_TRACKING:
  per_execution:        session_id, tenant_id, actor_id, input_hash, output_hash, timestamp
  log_store:            Append-only audit table (DELETE and UPDATE forbidden)

PROMPT_INJECTION_DEFENSE:
  pre_llm_sanitization: Strip instruction-format patterns from all utterance_text fields
  context_isolation:    LLM context contains only sanitized transcript excerpts

NO_CROSS_TENANT_QUERIES: ABSOLUTE — DB RLS + application + queue routing
```

---

## ▶ SECTION 9 — AUDIT & TRACEABILITY

Every DQAA execution MUST produce the following immutable audit record (per session):

```json
AUDIT_RECORD: {
  "audit_id":                     "UUID-v4",
  "timestamp_utc":                "ISO-8601",
  "actor_id":                     "system:DQAA or mentor_id if override",
  "tenant_id":                    "UUID",
  "session_id":                   "UUID",
  "session_type":                 "string",
  "domain_track":                 "string",
  "participant_count":            "integer",
  "excluded_participant_count":   "integer",
  "input_hash":                   "SHA-256 of serialized input payload",
  "output_hash":                  "SHA-256 of serialized output payload",
  "model_version":                "string",
  "dqaa_prompt_version":          "string",
  "rubric_version_applied":       "string",
  "decision_path": {
    "argument_units_detected":    "integer",
    "fallacies_detected_total":   "integer",
    "rebuttal_chains_mapped":     "integer",
    "dropped_arguments_total":    "integer",
    "llm_calls_made":             "integer",
    "llm_available":              "boolean",
    "quarantine_participants":    ["array of user_ids"],
    "low_transcript_quality_participants": ["array of user_ids"],
    "downstream_events_emitted":  ["array of event names"]
  },
  "execution_duration_ms":        "integer",
  "retry_count":                  "integer",
  "championship_context":         "boolean"
}
```

**Per-participant audit sub-record also generated** (linked by `session_evaluation_id`):

```json
PER_PARTICIPANT_AUDIT: {
  "audit_id":                     "UUID-v4",
  "session_evaluation_id":        "UUID (parent)",
  "user_id":                      "UUID",
  "composite_dqi":                "float",
  "all_dimension_scores":         "object (all 7 dimensions)",
  "confidence_scores":            "object (per dimension)",
  "fallacies_detected":           "array",
  "argument_chain_ids":           "array",
  "quarantine_mode":              "boolean",
  "downstream_events_emitted":    "array"
}
```

**Audit Policy:**
- IMMUTABLE — append-only, no UPDATE, no DELETE
- Encrypted at rest — tenant-isolated
- Retention: minimum 7 years (enterprise); 10 years for championship evaluations
- Dispute reference: `audit_id` used as primary reference in APPEALS workflow
- Export: via COMPLIANCE_AGENT API for authorized roles only

---

## ▶ SECTION 10 — FAILURE POLICY

```yaml
FAILURE_SCENARIOS:

  INVALID_INPUT:
    action:        STOP_EXECUTION
    log:           LOG_VALIDATION_INCIDENT
    escalate:      GOVERNANCE_AGENT
    retry:         NO
    response:      Structured error payload {failure_code, field_path, remediation_hint}

  PARTICIPANT_FLAGGED_CRITICAL:
    action:        EXCLUDE that participant; continue scoring others
    log:           LOG_INTEGRITY_CRITICAL for excluded participant
    escalate:      INTEGRITY_AGENT + GOVERNANCE_AGENT
    retry:         NO for excluded participant
    session:       Continues — other participants receive valid scores

  MODEL_UNAVAILABLE:
    action:        STOP_EXECUTION
    log:           LOG_MODEL_FAILURE
    escalate:      OBSERVABILITY_AGENT + ENGINEERING_ONCALL
    retry:         YES (3 retries, exponential backoff)
    fallback:      Route to MENTOR_CONTROL_ENGINE for manual evaluation if retry exhausted
    sla_breach:    > 90 seconds in championship mode → P1 incident

  LLM_TIMEOUT:
    action:        PROCEED ML-only mode (no evidence extraction, no rationale)
    flag:          AI_PARTIAL_MODE in output
    log:           LOG_LLM_TIMEOUT
    escalate:      OBSERVABILITY_AGENT
    downstream:    ML scores are valid; emit normally
    rationale:     Set to "Evidence extraction deferred — LLM unavailable"

  TRANSCRIPT_QUALITY_FAILURE (participant-level):
    threshold:     > 60% of participant turns below 0.70 confidence
    action:        Mark participant as INSUFFICIENT_TRANSCRIPT_QUALITY
    behavior:      DQI not computed for that participant
    log:           LOG_TRANSCRIPT_QUALITY_FAILURE
    session:       Other participants with sufficient transcript quality scored normally

  INSUFFICIENT_TRANSCRIPT_TURNS:
    threshold:     < 10 total transcript turns in session
    action:        STOP_EXECUTION — session too short for meaningful analysis
    log:           LOG_INSUFFICIENT_SESSION_DATA
    escalate:      GOVERNANCE_AGENT

  LOW_COMPOSITE_CONFIDENCE (participant-level):
    threshold:     composite_dqi.confidence < 0.62
    action:        PRODUCE OUTPUT but flag LOW_CONFIDENCE
    downstream:    No belt trigger, no leaderboard update
    escalate:      Queue for human review (EVALUATOR role)
    log:           LOG_LOW_CONFIDENCE_EVENT

  CRITICAL_ANOMALY_FLAG:
    action:        PRODUCE OUTPUT + quarantine_mode = true per affected participant
    downstream:    NO belt, NO leaderboard, NO growth events
    escalate:      GOVERNANCE_AGENT + INTEGRITY_AGENT
    log:           LOG_ANOMALY_QUARANTINE

  ARGUMENT_MAP_BUILD_FAILURE:
    action:        PRODUCE scoring output without argument map
    flag:          ARGUMENT_MAP_UNAVAILABLE in output
    downstream:    Emit normally — argument map is supplementary
    log:           LOG_ARGUMENT_MAP_FAILURE
    escalate:      OBSERVABILITY_AGENT

  TENANT_ISOLATION_VIOLATION:
    action:        IMMEDIATE STOP_EXECUTION
    log:           LOG_SECURITY_CRITICAL
    escalate:      COMPLIANCE_AGENT + SECURITY_TEAM (P0 incident)
    retry:         NO

NO_SILENT_FAILURES:     ABSOLUTE — every failure produces a log entry and downstream alert
```

---

## ▶ SECTION 11 — INTER-AGENT DEPENDENCY MAP & EVENTS

### 11.1 Upstream

```
SCENARIO_ENGINE               → topic/motion, rubric, stance assignments
MATCH_ENGINE                  → session context, participant roster, session type
AUDIO_TRANSCRIPTION_AGENT     → per-speaker timestamped transcript + confidence
ANTI_CHEAT_AGENT              → per-participant integrity flags
MENTOR_CONTROL_ENGINE         → structured mentor observation inputs
FEATURE_STORE_AGENT           → historical DQI baselines per user per domain
INTEGRITY_AGENT               → scripted-response detection, passive gaming flags
```

### 11.2 Downstream

```
OREA                          ← DQI as supplementary signal
BELT_ENGINE                   ← DQI vector for debate-track belt progression
RATING_ENGINE                 ← composite DQI for match rating update
INTELLIGENCE_MEASUREMENT_ENGINE ← debate intelligence dimension contribution
FEATURE_STORE_AGENT           ← DQI feature vectors
ANALYTICS_ENGINE              ← argument maps, DQI trends, fallacy analytics
RECRUITER_SYSTEM              ← debate differentiation signal
OBSERVABILITY_AGENT           ← metrics, errors, latency, drift
GOVERNANCE_AGENT              ← audit records, quarantine events, appeals inputs
GROWTH_ENGINE                 ← XP triggers, achievement checks
LEADERBOARD_ENGINE            ← DQI ranking signal (championship)
NOTIFICATION_SERVICE          ← per-participant DQI delivery
CDA                           ← argument novelty as creativity input (conditional)
```

### 11.3 Event Triggers

```yaml
SESSION_EVALUATION_COMPLETE_EVENT:
  always_emitted:     true
  payload:            session_evaluation_id, session_id, tenant_id, domain_track,
                      participant_count, championship_context_flag

PER_PARTICIPANT_DQI_EVENT:
  emitted_per:        Each successfully evaluated participant
  payload:            user_id, session_id, composite_dqi, confidence,
                      debate_quality_band, domain_percentile, audit_reference

BELT_ELIGIBILITY_CHECK_EVENT:
  condition:          composite_dqi.confidence >= 0.62
                      AND debate_quality_band in [ACCOMPLISHED, DISTINGUISHED]
                      AND quarantine_mode = false
  payload:            user_id, domain_track, dqi_score, debate_quality_band

RATING_UPDATE_EVENT:
  payload:            user_id, session_id, composite_dqi, session_type

XP_UPDATE_EVENT:
  always_emitted:     true (per successfully evaluated participant)
  payload:            user_id, xp_delta (band-based), achievement_check_flag

ARGUMENT_MAP_READY_EVENT:
  always_emitted:     true (when argument_map successfully built)
  payload:            session_evaluation_id, map_id, total_chains, total_dropped

FALLACY_ALERT_EVENT:
  condition:          fallacies_detected count > 0 per participant
  payload:            user_id, fallacy_types, session_id (for post-session coaching)

LEADERBOARD_UPDATE_EVENT:
  condition:          championship_context_flag = true AND quarantine_mode = false
                      AND confidence >= 0.65
  payload:            user_id, domain_track, dqi_score, debate_quality_band

GROWTH_BREAKTHROUGH_EVENT:
  condition:          growth_signal = BREAKTHROUGH per participant
  payload:            user_id, prior_dqi, current_dqi, delta

LOW_CONFIDENCE_REVIEW_EVENT:
  condition:          composite_dqi.confidence < 0.62
  payload:            session_evaluation_id, user_id, confidence → human review queue

QUARANTINE_EVENT:
  condition:          quarantine_mode = true (per participant)
  payload:            session_evaluation_id, user_id → governance queue

SESSION_ANALYTICS_READY_EVENT:
  always_emitted:     true
  payload:            session_evaluation_id, participation_equity_index,
                      total_fallacies, dominant_speaker_id, total_argument_units
```

---

## ▶ SECTION 12 — PASSIVE INTELLIGENCE COMPATIBILITY

```yaml
FEATURE_VECTOR_EMISSION:
  destination:        FEATURE_STORE_AGENT
  trigger:            Every successfully evaluated participant
  schema:
    user_id:              UUID
    feature_name:         "dqaa_{dimension_code}_{domain_track}"
    feature_value:        dimension score (float)
    confidence:           float
    timestamp:            evaluation_timestamp_utc
    source_agent:         "DQAA"
    domain_track:         string
    session_type:         string
    debate_quality_band:  string

FEATURES_EMITTED (per participant per session):
  - dqaa_arg_{domain}          (argument structure score)
  - dqaa_log_{domain}          (logical coherence score)
  - dqaa_reb_{domain}          (rebuttal precision score)
  - dqaa_evd_{domain}          (evidence deployment score)
  - dqaa_ada_{domain}          (adversarial adaptability score)
  - dqaa_per_{domain}          (persuasion mechanics score)
  - dqaa_eng_{domain}          (session engagement score)
  - dqaa_composite_{domain}    (composite DQI)
  - dqaa_domain_percentile     (percentile in domain)
  - dqaa_debate_band           (quality band label)
  - dqaa_fallacy_rate          (fallacies per argument unit)
  - dqaa_rebuttal_precision    (direct vs straw-man rebuttal ratio)
  - dqaa_pressure_trajectory   (ADA trajectory label)
  - dqaa_growth_delta          (DQI delta vs prior)
  - dqaa_dropped_arg_rate      (dropped arguments / total opponent claims)

INTELLIGENCE_RADAR_CONTRIBUTION:
  CDA feeds: Intelligence Measurement System (Module 2)
  dimension: "Debate Intelligence" radar axis
  signal:    composite_dqi + debate_quality_band + pressure_trajectory
  update:    Per session (event-driven)
```

---

## ▶ SECTION 13 — GROWTH ENGINE HOOK

```yaml
XP_AWARD_POLICY:
  debate_quality_band DISTINGUISHED:  XP_DELTA = 220 + STREAK_MULTIPLIER
  debate_quality_band ACCOMPLISHED:   XP_DELTA = 140
  debate_quality_band DEVELOPING:     XP_DELTA = 75
  debate_quality_band NOVICE:         XP_DELTA = 30 (participation baseline)

ACHIEVEMENT_CHECK_TRIGGERS:
  - First DQAA evaluation → "Debate Arena Unlocked" badge
  - DISTINGUISHED band first time in domain → "Debate Champion" badge check
  - 0 fallacies in a session (min 10 argument units) → "Clean Logic" badge
  - REB score > 85 in a session → "Precision Rebutter" badge
  - ADA pressure_trajectory = IMPROVING across 3 sessions → "Pressure Proof" badge
  - EVD evidence_type_diversity >= 4 types in session → "Evidence Architect" badge
  - growth_signal = BREAKTHROUGH → "Debate Breakthrough" badge
  - ENG participation_band = BALANCED + composite_dqi > 70 → "Balanced Contributor" badge
  - Championship context + DISTINGUISHED band → "Championship Debater" badge
  - 5 consecutive ACCOMPLISHED+ sessions → "Debate Streak" badge

SHARE_TRIGGER_EVENT:
  condition:        debate_quality_band = DISTINGUISHED AND confidence >= 0.72
                    AND quarantine_mode = false
  payload:          user_id, debate_quality_band, domain_percentile, session_type
  destination:      NOTIFICATION_SERVICE + SOCIAL_FEED_SERVICE

STREAK_MECHANICS:
  streak_unit:      Consecutive ACCOMPLISHED+ or DISTINGUISHED sessions in same domain
  multiplier:       XP_DELTA * (1 + 0.10 * streak_count), max 2.5x
  reset:            On NOVICE band or 7-day inactivity in debate format
```

---

## ▶ SECTION 14 — CHAMPIONSHIP & DOJO INTEGRATION

```yaml
CHAMPIONSHIP_CONTEXT:
  trigger:            championship_context_flag = true
  behavior:
    - DQAA evaluated at CRITICAL priority lane
    - Argument map published to championship analytics dashboard (read-only)
    - DQI contributes to Championship Leaderboard via LEADERBOARD_ENGINE
    - DISTINGUISHED band → "Championship Debate" award eligibility
    - Fallacy log available to championship judges (structured — not narrative)
    - Audit retention: 10 years for championship session evaluations
  leaderboard_signal: composite_dqi + debate_quality_band + domain_percentile + fallacy_rate

DOJO_GD_MATCH_INTEGRATION:
  trigger:            session_type = live_gd_match
  behavior:
    - Evaluated async post-session (default)
    - Argument map delivered to MENTOR_CONTROL_ENGINE for debrief reference
    - Fallacy report delivered to NOTIFICATION_SERVICE (per participant, private)
    - DQI contributes to BELT_ENGINE for GD-track progression
    - ENG dimension weighted higher for GD (group context)

REALTIME_INCREMENTAL_MODE:
  available:          YES (live championship or mentor-observed)
  turn_batch:         Every 5 completed turns
  interim_output:     Partial dimension scores (ARG + LOG + REB only — sufficient data-rich)
  interim_flag:       INTERIM_SCORE — no belt/rating/growth events until FINAL
  display_use:        Championship live scoreboard (audience-facing only)
  final_trigger:      Full transcript received and validated

MENTOR_DEBRIEF_INTEGRATION:
  argument_map:       Delivered to MENTOR_CONTROL_ENGINE post-session
  fallacy_report:     Structured fallacy list with turn references delivered to mentor
  pressure_events:    ADA pressure_event log available for mentor coaching reference
  use:                Mentor coaching reference only — NOT primary evaluation input
```

---

## ▶ SECTION 15 — RECRUITER SYSTEM INTEGRATION

```yaml
RECRUITER_ACCESS_TO_DQI:
  access_mode:      Read-only via verified recruiter API (talent marketplace)
  data_shared:
    - composite_dqi score
    - debate_quality_band
    - domain_percentile
    - per-dimension scores (all 7)
    - growth_signal
    - fallacy_rate (aggregate — not per-incident detail)
    - pressure_trajectory (ADA)
    - DQI trend (last 12 sessions)
  not_shared:
    - raw transcript content (privacy)
    - specific fallacy incident details (only aggregate rate)
    - opponent identities or session details
    - mentor observation notes

RECRUITER_SEARCH_CAPABILITY:
  filter_by:        debate_quality_band, domain_track, DQI range,
                    growth_signal, pressure_trajectory
  sort_by:          composite_dqi (domain-percentile normalized)
  min_confidence:   DQI results with confidence < 0.62 excluded from recruiter search

DOMAIN_MAPPING_FOR_HIRING:
  COMMERCE + DISTINGUISHED:    → Sales, negotiation, business development roles
  TECHNOLOGY + DISTINGUISHED:  → Technical leadership, architecture debate, RFP response
  ARTS + DISTINGUISHED:        → Content strategy, creative direction, pitch roles
  ADMINISTRATION + DISTINGUISHED: → Policy advocacy, public affairs, governance roles
  Note: This mapping is advisory metadata for recruiter UX — NOT a hiring decision

HIRING_DECISION_RULE:
  DQI is advisory signal only — never autonomous hiring decision
  AI_never_approves_blocks_overrides_humans: ENFORCED
```

---

## ▶ SECTION 16 — PERFORMANCE MONITORING

```yaml
METRICS_EMITTED_TO_OBSERVABILITY_AGENT:

  success_rate:
    definition:         (successful_sessions / total_attempts) * 100
    target:             >= 99.0%
    alert_threshold:    < 98.0%

  error_rate:
    alert_threshold:    > 2.0%

  latency_p95:
    async_target:       < 8000ms per session
    realtime_target:    < 12000ms per turn-batch
    alert:              > 12000ms async / > 18000ms realtime

  fallacy_detection_rate:
    definition:         fallacies_detected / total_argument_units (session avg)
    monitor:            Domain-specific baseline distribution
    alert:              Sudden spike > 3x baseline → model drift or adversarial content

  argument_map_build_success_rate:
    target:             >= 98.0%
    alert:              < 96.0%

  model_drift_per_dimension:
    metric:             PSI per dimension per domain_track
    threshold:          PSI > 0.2 → retraining alert
    frequency:          Daily batch

  low_confidence_rate:
    definition:         % sessions with any participant composite_confidence < 0.62
    alert_threshold:    > 18% → model quality review

  quarantine_rate:
    definition:         % participant evaluations in quarantine
    alert:              > 6% → INTEGRITY_AGENT review

  transcript_quality_failure_rate:
    definition:         % participants with INSUFFICIENT_TRANSCRIPT_QUALITY
    alert:              > 10% → AUDIO_TRANSCRIPTION_AGENT review

  llm_timeout_rate:
    alert:              > 5% → LLM infrastructure review

  dropped_argument_rate:
    definition:         avg dropped_arguments / total_chains per session
    monitor:            By session_type, by domain (expected range varies)

DASHBOARDS:
  - DQI distribution per domain (daily/weekly)
  - Debate quality band breakdown (% per band per domain)
  - Fallacy frequency by type and domain
  - Argument map depth distribution
  - Rebuttal precision distribution
  - Participation equity index distribution (GD vs. championship)
  - Pressure trajectory distribution (ADA)
  - Model drift indicators per dimension
  - Championship vs. standard DQI comparison
  - Transcript quality failure rate (by domain, by session_type)
```

---

## ▶ SECTION 17 — VERSIONING POLICY

```yaml
VERSION_FORMAT:         Semantic Versioning (MAJOR.MINOR.PATCH)
CURRENT_VERSION:        v1.0.0

MUTATION_POLICY:        ADD-ONLY
  no_dimension_removal:       Without full governance deprecation cycle
  no_weight_change:           Without MAJOR version bump + validated research
  no_fallacy_taxonomy_removal: Without MAJOR version bump

BACKWARD_COMPATIBILITY:
  window:               2 MAJOR versions supported simultaneously
  deprecation_notice:   45 days minimum before removing support for old version

MODEL_VERSION_CONTROL:
  all_models:           Version-tagged, immutable once deployed
  rollback_window:      30 days

TRANSCRIPT_CORPUS_NOTE:
  debate_transcripts:   NOT used for model training without explicit tenant opt-in
  model_training_data:  Curated labeled datasets from certified debate experts
  This is distinct from CDA where domain corpus is built from platform content

PROMPT_VERSION_CONTROL:
  all_prompts:          Version-tagged (dqaa_prompt_version in every output)
  change_governance:    Governance approval + regression test before deployment

CHANGE_GOVERNANCE:

  ALLOWED_WITHOUT_VERSION_BUMP:
    - Add optional input field
    - Add new achievement trigger
    - Add new feature vector emission
    - Monitoring threshold adjustment
    - New session_type (within existing evaluation logic)

  REQUIRES_MINOR_VERSION_BUMP:
    - New required input field
    - New output field
    - New fallacy type added to taxonomy
    - New evidence type added to taxonomy
    - New interruption classification category
    - New rhetoric device added to inventory
    - Straw man threshold change

  REQUIRES_MAJOR_VERSION_BUMP + GOVERNANCE_APPROVAL:
    - Dimension weight change (any)
    - Dimension addition or removal
    - ML feature set change (any model)
    - Scoring formula change
    - Confidence calibration methodology change
    - Audit record schema change
    - Debate quality band threshold change
    - Fallacy severity map change
```

---

## ▶ SECTION 18 — LOCALIZATION & CULTURAL FAIRNESS

```yaml
LOCALIZATION_CODE:
  field:              localization_code (optional input, default "en")
  supported:          ISO 639-1 language codes
  transcript:         Evaluated in declared language where model available
  fallback:           English models used for unsupported languages with confidence reduction

CULTURAL_DEBATE_STYLE_CALIBRATION:
  issue:              Debate norms vary culturally (directness, indirectness, formality)
  DQAA_approach:
    - ADA and ENG dimensions: culturally-calibrated response classification
    - PER dimension: rhetorical device inventory accounts for regional rhetoric traditions
    - No dimension penalizes culturally-normative communication styles as "low quality"

ARGUMENT_STRUCTURE_CULTURAL_NOTE:
  Some cultures use inductive argument patterns (evidence-first, conclusion-last)
  vs. deductive (conclusion-first, evidence-follows).
  ARG dimension scores STRUCTURE COMPLETENESS, not argument order.
  Both inductive and deductive structures score equivalently on ARG.

BIAS_AUDIT:
  frequency:          Quarterly
  scope:              DQI score distributions across regional and linguistic cohorts
  action_on_bias:     Model recalibration if significant systematic bias detected
  governance:         Bias audit results reported to Governance Board

ACCESSIBILITY:
  verbal_transcript:  Verbal response via AUDIO_TRANSCRIPTION_AGENT → DQAA
  extended_turn_time: Accessibility accommodation noted → ENG not penalized for extended pauses
  caption_only_mode:  Supported (DQAA operates on text transcript — mode transparent to engine)
```

---

## ▶ SECTION 19 — ECOSKILLER 300+ MODULE INTEGRATION MAP

| Module | DQAA Integration Role |
|---|---|
| **Intelligence Measurement (Module 2)** | DQAA feeds "Debate Intelligence" axis — DQI contributes to intelligence radar chart |
| **Skill System / Dojo (Module 3)** | DQI drives Dojo GD skill XP, debate-track badges, and skill level progression |
| **Championship System (Module 4)** | DQAA is the primary AI judging engine for championship debate rounds; fallacy log available to judges |
| **Parent System (Module 5)** | DQI trend available in parent dashboard (student debate development view) |
| **Institute System (Module 6)** | Cohort DQI analytics available to institute dashboard (class debate performance) |
| **Recruiter System (Module 7)** | DQI is a top-tier talent differentiation signal for communication and leadership roles |
| **AI Mentor System (Module 8)** | Fallacy report + argument map + pressure_trajectory → personalized debate coaching plans |
| **Trust System (Module 15)** | Collusion detection, scripted-response flags, passive gaming detection |
| **Growth Engine (Module 16)** | XP awards, 10+ debate achievement badge triggers, streak mechanics |
| **Social System (Module 14)** | DISTINGUISHED debate band → shareable achievement card |
| **Creator/Educator Systems (Modules 11–12)** | Teaching effectiveness — educator DQI in class discussion contexts |

---

## ▶ SECTION 20 — APPEALS & DISPUTE GOVERNANCE

```yaml
FALLACY_DISPUTE_WORKFLOW:
  trigger:            Participant disputes a specific flagged fallacy post-session
  access:             Via "Dispute" action in participant session results UI
  data_submitted:     fallacy instance (turn_id, fallacy_type, excerpt)
  routing:            GOVERNANCE_AGENT → certified human evaluator queue
  evaluator_role:     CERTIFIED_DEBATE_EVALUATOR (domain-specific)
  review_data:        Full transcript, ML confidence, LLM rationale, turn context
  decision:           UPHOLD or OVERTURN (logged immutably)
  score_adjustment:   If OVERTURN: LOG score recalculated; composite_dqi recomputed
  SLA:                5 business days for dispute resolution
  appeal_limit:       Maximum 3 disputes per session per participant

DQI_DISPUTE_WORKFLOW:
  trigger:            Participant disputes overall DQI score
  routing:            GOVERNANCE_AGENT → senior evaluator queue
  eligibility:        Only for sessions where quarantine_mode = false
  review_scope:       Full dimension scores + argument map + transcript
  outcome:            MAINTAIN or RECALCULATE
  SLA:                10 business days

GOVERNANCE_BOARD_ESCALATION:
  trigger:            Dispute outcome disputed by participant (second escalation)
  board:              Platform Debate Governance Board (certified evaluators)
  decision:           FINAL AND BINDING
  log:                Immutable governance decision record
  no_further_appeal:  Governance Board decision cannot be reversed
```

---

## ▶ SECTION 21 — NON-NEGOTIABLE RULES

```
THIS AGENT MUST NOT:

  ✗ Declare a winner of a debate session
  ✗ Evaluate factual correctness of claims (out of scope — use FACT_CHECK_AGENT if available)
  ✗ Generate narrative opinions about participant character, personality, or potential
  ✗ Score speaker accent, voice quality, or pace
  ✗ Penalize participants for holding unpopular positions (position ≠ quality)
  ✗ Allow LLM to compute or modify any dimension score
  ✗ Allow LLM to remove ML-detected fallacies from output
  ✗ Modify historical session evaluation records (append-only is absolute)
  ✗ Auto-delete or soft-delete audit records
  ✗ Override GOVERNANCE_AGENT, COMPLIANCE_AGENT, or INTEGRITY_AGENT decisions
  ✗ Access cross-tenant transcripts or participant data
  ✗ Mix domain corpus data across domain tracks
  ✗ Produce partial session outputs (all participants or structured error — no in-between)
  ✗ Trigger belt, leaderboard, or growth events on quarantined evaluations
  ✗ Use debate transcripts for model training without explicit tenant opt-in
  ✗ Include participant identity in LLM context window
  ✗ Score FLAGGED_CRITICAL participants (exclude + log — do not score)
  ✗ Emit downstream events for participants with INSUFFICIENT_TRANSCRIPT_QUALITY
  ✗ Apply global (non-domain) baseline for domain-specific scoring
  ✗ Change dimension weights at runtime (weights locked until MAJOR version bump)
  ✗ Allow silent failures — every failure path produces a log entry
  ✗ Assume missing specifications — halt on ambiguity and escalate
  ✗ Execute outside the defined input/output contract scope
  ✗ Make hiring, certification, or belt decisions autonomously
```

---

## ▶ SECTION 22 — PRODUCTION GOVERNANCE SEAL

```
╔══════════════════════════════════════════════════════════════════════════╗
║        DEBATE QUALITY ANALYZER AGENT — PRODUCTION GOVERNANCE SEAL      ║
╠══════════════════════════════════════════════════════════════════════════╣
║  ECOSKILLER ANTIGRAVITY PLATFORM MODE        : ENABLED                 ║
║  Security Hardening                          : REQUIRED                ║
║  Tenant Isolation                            : HARD ENFORCED           ║
║  Domain Isolation                            : HARD ENFORCED           ║
║  Transcript Privacy                          : ENFORCED                ║
║  Seven-Dimension Framework                   : LOCKED v1.0.0           ║
║  Dimension Weights                           : LOCKED v1.0.0           ║
║  Fallacy Detection Taxonomy                  : LOCKED v1.0.0 (12 types)║
║  Evidence Type Taxonomy                      : LOCKED v1.0.0           ║
║  Argument Map Construction                   : REQUIRED                ║
║  Per-Participant Audit Record                : REQUIRED                ║
║  Anti-Cheat Clearance Per Participant        : REQUIRED                ║
║  LLM Autonomous Scoring                      : FORBIDDEN               ║
║  LLM Fallacy Removal                         : FORBIDDEN               ║
║  Winner Declaration                          : FORBIDDEN               ║
║  Factual Correctness Evaluation              : OUT OF SCOPE            ║
║  Position-Based Scoring Penalty              : FORBIDDEN               ║
║  Cross-Tenant Data Access                    : FORBIDDEN               ║
║  Training on Tenant Transcripts              : OPT-IN ONLY             ║
║  Partial Output Emission                     : FORBIDDEN               ║
║  Silent Failures                             : FORBIDDEN               ║
║  Creative Interpretation by Agent            : FORBIDDEN               ║
║  Assumption Filling                          : FORBIDDEN               ║
║  Audit Trail                                 : IMMUTABLE APPEND-ONLY   ║
║  Fallacy Dispute Workflow                    : ACTIVE                  ║
║  DQI Appeals Workflow                        : ACTIVE                  ║
║  Governance Board Authority                  : ACTIVE (FINAL DECISION) ║
║  Cultural Fairness Audit                     : QUARTERLY               ║
║  Accessibility Compliance                    : ENFORCED                ║
║  Mutation Policy                             : ADD-ONLY VERSIONED      ║
║  Backward Compatibility Window               : 2 MAJOR VERSIONS        ║
║  Championship Audit Retention                : 10 YEARS                ║
╠══════════════════════════════════════════════════════════════════════════╣
║  Interpretation Authority                    : NONE                    ║
║  Architecture Authority                      : LOCKED                  ║
║  Agent Version                               : v1.0.0                  ║
║  Agent ID                                    : ECOSKILLER-AGT-DQAA-003 ║
║  Platform                                    : ECOSKILLER ANTIGRAVITY  ║
╚══════════════════════════════════════════════════════════════════════════╝
```

---

*Document Classification: PRODUCTION ARTIFACT — LOCKED*
*Mutation Policy: ADD-ONLY via version bump*
*Interpretation Authority: NONE*
*Architecture Authority: LOCKED*

```
END OF DEBATE QUALITY ANALYZER AGENT — AGENT.md v1.0.0
ECOSKILLER ANTIGRAVITY PLATFORM
```
