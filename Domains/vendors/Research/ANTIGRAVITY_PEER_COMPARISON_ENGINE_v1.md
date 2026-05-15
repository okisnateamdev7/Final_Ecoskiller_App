# 🔵 ANTIGRAVITY — PEER COMPARISON ENGINE (PCE) v1.0
## ECOSKILLER CHAMPIONSHIP ADVANCED + PARENT PREDICTIVE AI ML LAYER
### AGENT SPECIFICATION · SEALED · LOCKED · GOVERNED · DETERMINISTIC

```
╔══════════════════════════════════════════════════════════════════════╗
║          ANTIGRAVITY PEER COMPARISON ENGINE — DOCUMENT SEAL         ║
╠══════════════════════════════════════════════════════════════════════╣
║  DOCUMENT CLASS      : SEALED PRODUCTION ARTIFACT                   ║
║  ARTIFACT TYPE       : AGENT SYSTEM SPECIFICATION                   ║
║  EXECUTION ENGINE    : ANTIGRAVITY                                   ║
║  MUTATION POLICY     : ADD-ONLY VIA VERSION BUMP                    ║
║  EXECUTION MODE      : DETERMINISTIC                                 ║
║  INTERPRETATION      : FORBIDDEN                                     ║
║  CREATIVE DEVIATION  : FORBIDDEN                                     ║
║  ASSUMPTION FILLING  : FORBIDDEN                                     ║
║  DEFAULT BEHAVIOR    : DENY                                          ║
║  FAILURE MODE        : STOP → REPORT → NO PARTIAL OUTPUT            ║
║  ANTIGRAVITY SAFE    : CONFIRMED                                     ║
║  HUMAN OVERRIDE      : REQUIRED FOR ALL CRITICAL ACTIONS            ║
║  AI AUTONOMY CEILING : ADVISORY + COMPUTATION ONLY                  ║
╚══════════════════════════════════════════════════════════════════════╝
```

---

## ⚡ WHAT IS THE PEER COMPARISON ENGINE

The **Peer Comparison Engine (PCE)** is a sealed Antigravity subsystem that computes, stores, serves, and governs **multi-dimensional peer comparison data** across every user class in Ecoskiller.

It is not a leaderboard. It is not a ranking widget. It is a **full-stack, multi-cohort, multi-intelligence, multi-skill, championship-aware comparative analytics agent** that powers:

- Student-to-peer intelligence and skill benchmarking
- Parent-facing predictive peer position projections
- Championship bracket relative performance mapping
- Recruiter candidate comparative scoring
- Institute cohort health comparison
- Mentor-calibrated normalized scoring comparison
- Anonymized national + global percentile computation

The PCE operates as an **always-on background agent** that continuously refreshes comparison vectors on event triggers and scheduled cycles. It does **not** expose raw peer data — it exposes **normalized, privacy-compliant comparison insights** only.

---

## 🔒 SECTION PCE-0 — MASTER GOVERNANCE SEAL

```
PCE_ENGINE_STATUS              : ACTIVE · ENFORCED · LOCKED
PARENT_ML_LAYER                : ACTIVE · PREDICTIVE · ADVISORY ONLY
CHAMPIONSHIP_ADVANCED_LAYER    : ACTIVE · LIVE + HISTORICAL
HUMAN_OVERRIDE_AUTHORITY       : REQUIRED FOR CRITICAL OUTPUTS
AI_AUTONOMY_CEILING            : COMPUTATION + ADVISORY — NO AUTONOMOUS DECISIONS
PEER_PII_EXPOSURE              : FORBIDDEN — ANONYMIZED VECTORS ONLY
RAW_SCORE_SHARING              : FORBIDDEN ACROSS TENANTS
COHORT_CROSS_CONTAMINATION     : FORBIDDEN — DOMAIN + TENANT ISOLATED
COMPARISON_FAIRNESS_AUDIT      : MANDATORY — QUARTERLY
BIAS_DETECTION                 : ACTIVE — REGIONAL + CULTURAL
PARENT_COMPARISON_DATA         : ADVISORY TONE — NON-SHAMING REQUIRED
MINOR_DATA_PROTECTION          : HARD ENFORCED
INTERPRETATION_AUTHORITY       : NONE
ARCHITECTURE_AUTHORITY         : LOCKED
```

**Absence of any section below → STOP EXECUTION**

---

## 🔒 SECTION PCE-1 — PEER COMPARISON TAXONOMY

### PCE-1.1 — Comparison Dimension Registry (IMMUTABLE)

All comparison dimensions are locked. Addition requires version bump.

| Dimension ID | Dimension Name | Data Source | Scope | Privacy Level |
|---|---|---|---|---|
| DIM-001 | Intelligence Score (Composite) | Intelligence Engine | Cohort | Anonymized |
| DIM-002 | Intelligence Score (Domain-specific) | Intelligence Engine | Cohort | Anonymized |
| DIM-003 | Skill Level (per skill) | Dojo / Skill Engine | Cohort + Global | Anonymized |
| DIM-004 | Skill XP Accumulation Rate | Skill Engine | Cohort | Anonymized |
| DIM-005 | Championship Win Rate | Championship Engine | Institute + Global | Anonymized |
| DIM-006 | Championship Rank Position | Championship Engine | Institute + Global | Public (opt-in) |
| DIM-007 | Match Score (Dojo) | Scoring Engine | Cohort | Anonymized |
| DIM-008 | Belt Progression Velocity | Belt Engine | Cohort | Anonymized |
| DIM-009 | Scenario Pass Rate | Scoring Engine | Cohort | Anonymized |
| DIM-010 | Learning Engagement Rate | Analytics Engine | Cohort | Private |
| DIM-011 | Streak Consistency | Habit Engine | Cohort | Private |
| DIM-012 | Peer Endorsement Count | Social Engine | Institute | Anonymized |
| DIM-013 | Career Readiness Score | AI Career Engine | Cohort + National | Anonymized |
| DIM-014 | Project Completion Rate | Project Engine | Cohort | Private |
| DIM-015 | Recruiter Attraction Score | Recruiter Engine | National | Anonymized |
| DIM-016 | Reliability Score | Trust Engine | Cohort | Anonymized |
| DIM-017 | Improvement Velocity (30-day delta) | Analytics Engine | Cohort | Private |
| DIM-018 | Parent-Reported Engagement | Parent Engine | Family | Private |
| DIM-019 | Institute Rank Position | Institute Engine | Institute | Anonymized |
| DIM-020 | National Percentile | PCE Compute | National | Anonymized |

### PCE-1.2 — Cohort Definition Registry (LOCKED)

Cohort types are locked. Each user belongs to applicable cohorts automatically.

```
COHORT-A  : Same-age peers (±1 year) — Age Cohort
COHORT-B  : Same-institution peers — Institute Cohort
COHORT-C  : Same-domain peers (Arts/Commerce/Science/Tech/Admin) — Domain Cohort
COHORT-D  : Same-belt-level peers — Belt Cohort
COHORT-E  : Same-championship-category peers — Championship Cohort
COHORT-F  : Same-career-path peers — Career Cohort
COHORT-G  : Same-region peers — Regional Cohort
COHORT-H  : National pool — National Cohort
COHORT-I  : Global pool (opt-in only) — Global Cohort
COHORT-J  : Same-skill-track peers — Skill Track Cohort

RULES:
- Each user is assigned to ALL applicable cohorts automatically
- Cross-tenant cohort membership: ALLOWED only for COHORT-G, COHORT-H, COHORT-I
- Institute-scoped cohorts: COHORT-A, COHORT-B, COHORT-C within tenant boundary
- Cohort membership is privacy-transparent to the user
- Parent can see child's cohort memberships in parent dashboard
```

---

## 🔒 SECTION PCE-2 — AGENT ARCHITECTURE

### PCE-2.1 — Agent Identity (LOCKED)

```
AGENT_NAME              : PeerComparisonAgent (PCA)
AGENT_CLASS             : Background Compute Agent + Event-Driven Updater
AGENT_TYPE              : Stateful Scheduled + Real-time Trigger Hybrid
AGENT_RUNTIME           : Kubernetes CronJob (scheduled) + Kafka Consumer (event-driven)
AGENT_LANGUAGE          : Python 3.11 (ML compute) + FastAPI (API serve layer)
AGENT_PERSISTENCE       : PostgreSQL (comparison snapshots) + ClickHouse (time-series)
AGENT_CACHE             : Redis (hot comparison vectors — 15-min TTL)
AGENT_QUEUE             : Apache Kafka (event triggers)
AGENT_ORCHESTRATION     : Apache Airflow (batch workflows)
AGENT_EXPLAINABILITY    : Required on all outputs to parent and student layers
AGENT_BIAS_MONITOR      : Active — regional, cultural, gender-aware audit
AGENT_PII_GUARD         : Active — anonymization pipeline mandatory before output
```

### PCE-2.2 — Agent Execution Modes (LOCKED)

```
MODE-1: REAL-TIME TRIGGER (Event-Driven)
  Trigger Events:
    - match.scored
    - belt.earned
    - championship.result.published
    - skill.level.up
    - intelligence.assessment.completed
    - enrollment.completed
    - project.milestone.verified
  Latency SLA: Comparison vector updated within 60 seconds of trigger
  Scope: Affected user + directly connected cohorts

MODE-2: SCHEDULED BATCH (Airflow CronJob)
  Schedules:
    - Hourly: Hot cohort vectors (active users)
    - Daily: Full cohort recompute (all users)
    - Weekly: National + global percentile recompute
    - Monthly: Career readiness + recruiter attraction recompute
    - Quarterly: Bias audit + fairness review
  Scope: All users + all cohorts

MODE-3: ON-DEMAND (API-triggered)
  Trigger: Parent dashboard load, recruiter search, student comparison view
  Latency SLA: < 500ms (served from cache or fast recompute)
  Scope: Specific user + requested dimension set

MODE-4: PREDICTIVE FORWARD PROJECTION (Parent ML Layer)
  Trigger: Daily parent ML batch
  Output: 14-day forward comparison position estimate
  Confidence: Required — displayed with every projection
  Scope: Child's position relative to cohort — forward-looking
```

### PCE-2.3 — Agent Internal Pipeline (LOCKED)

```
STAGE-1: DATA INGESTION
  Sources:
    - Intelligence Engine → Raw intelligence scores per domain
    - Scoring Engine → Match scores, scenario pass rates
    - Belt Engine → Belt levels, promotion dates
    - Championship Engine → Win rates, rank positions, bracket history
    - Analytics Engine → Engagement rates, streak data
    - Skill Engine → Skill XP, level progression
    - Project Engine → Completion rates, milestone data
    - Recruiter Engine → Attraction scores
    - Trust Engine → Reliability scores
    - Parent Engine → Consent flags, visibility settings
  Method: Kafka event consumption + scheduled DB read
  PII Rule: All ingestion strips direct PII identifiers before compute

STAGE-2: NORMALIZATION
  Method: Z-score normalization per cohort per dimension
  Outlier Handling: Winsorize at 1st and 99th percentile before normalize
  Difficulty Normalization: Apply scenario difficulty factor from Scoring Engine
  Confidence Score: Computed per dimension per user
  Cultural Normalization: Regional adjustment factor applied where applicable
  Bias Check: Gender, region, age group distribution audit per cohort per cycle

STAGE-3: COHORT VECTOR COMPUTATION
  For each user × dimension × cohort:
    - Compute user raw score
    - Apply normalization
    - Compute percentile within cohort
    - Compute delta vs cohort median
    - Compute delta vs cohort 75th percentile
    - Compute delta vs cohort 90th percentile
    - Tag: above_median | below_median | top_quartile | bottom_quartile | top_decile
    - Compute improvement trend (30-day velocity)
    - Compute projected position (14-day forward — Parent ML layer)

STAGE-4: ANONYMIZATION
  Before output:
    - Replace all peer identifiers with anonymous tokens
    - Strip institution names from cross-tenant outputs
    - Apply k-anonymity minimum threshold (k ≥ 5 peers required to serve comparison)
    - If cohort size < 5: suppress dimension for that cohort — return "insufficient cohort"
    - Parent output: further strip — show position only, no peer count detail

STAGE-5: STORAGE
  PostgreSQL: peer_comparison_snapshots (latest per user per dimension per cohort)
  ClickHouse: peer_comparison_history (time-series — all snapshots, queryable by date)
  Redis: peer_comparison_cache (hot vectors — 15-min TTL)

STAGE-6: API SERVE
  All outputs served via PCE API Layer
  Role-based access: student / parent / mentor / recruiter / institute-admin / platform-admin
  Output format: JSON with version tag, confidence score, attribution summary
```

---

## 🔒 SECTION PCE-3 — CHAMPIONSHIP ADVANCED PEER COMPARISON

### PCE-3.1 — Championship Peer Comparison Dimensions

```
CHAMP-DIM-001: BRACKET POSITION COMPARISON
  Description: User's current bracket position vs cohort within same championship tier
  Scope: School → District → State → National → Continental → World
  Computation: Rank percentile within same tier bracket
  Update Trigger: championship.result.published
  Parent View: "Your child is in the top X% of [tier] championship participants"
  Student View: Radar chart — your position vs bracket median
  Anonymization: Peer identities hidden — positions shown as percentile bands

CHAMP-DIM-002: WIN RATE COMPARISON
  Description: Championship win rate vs same belt-level cohort
  Computation: wins / total_matches vs cohort distribution
  Output: Percentile + trend direction (improving / stable / declining)
  Minimum Cohort: k ≥ 10 matches required for valid win rate comparison

CHAMP-DIM-003: SCORE CONSISTENCY COMPARISON
  Description: Variance of match scores vs cohort variance
  Computation: Coefficient of variation (CV) vs cohort CV distribution
  Insight: Low CV = consistent performer | High CV = volatile performer
  Parent Alert Integration: Feeds into PCE→AG-PRED if trend negative

CHAMP-DIM-004: PRESSURE SCENARIO PERFORMANCE
  Description: Performance in pressure-tagged scenarios vs cohort
  Computation: Pressure scenario score percentile vs belt-level cohort
  Championship Relevance: Critical for championship readiness signal
  Minimum Scenarios: 3 pressure scenarios required for valid compute

CHAMP-DIM-005: CHAMPIONSHIP TRAJECTORY PROJECTION
  Description: 14-day forward projection of championship rank position
  Model: LSTM time-series model on championship performance history
  Confidence Floor: 60% minimum — suppressed below threshold
  Parent Output: Advisory projection — non-deterministic framing required
  Update Cycle: Daily batch (Parent ML Layer)

CHAMP-DIM-006: OPPONENT RATING COMPARISON
  Description: Average opponent rating vs cohort average opponent rating
  Purpose: Normalize win rates for opponent quality
  Computation: Strength-of-schedule adjusted win rate
  Insight: Identifies users winning against weak opponents vs strong field

CHAMP-DIM-007: CHAMPIONSHIP IMPROVEMENT VELOCITY
  Description: Rate of championship rank improvement vs cohort improvement rate
  Computation: Rank delta per 30 days vs cohort rank delta distribution
  Insight: Who is improving fastest in the bracket
  Parent View: "Your child is improving faster than X% of peers in their bracket"

CHAMP-DIM-008: HALL OF FAME PROXIMITY SIGNAL
  Description: Distance from Hall of Fame threshold vs current position
  Computation: Points gap to Hall of Fame threshold vs median gap in cohort
  Output: "X points from Hall of Fame — Y% of cohort at similar gap"
  Minimum Condition: User must have > 5 championship matches scored
```

### PCE-3.2 — Championship Live Comparison (Real-Time Mode)

```
LIVE_PCE_MODE: ACTIVE during live championship tournaments only

Live Comparison Outputs (served to spectator + mentor view):
  - Live leaderboard position percentile
  - Live score vs bracket median (anonymized)
  - Live improvement vs prior round
  - Momentum signal (score trend within tournament session)

Live Comparison Rules:
  - Updates every 30 seconds during active tournament
  - No individual peer scores exposed — only aggregated percentile bands
  - Student self-view: own position + cohort band only
  - Mentor view: full student comparison panel
  - Parent view: child position + band — no peer detail
  - Spectator view: anonymized leaderboard only

Live Cache Layer: Redis pub/sub — 30-second TTL
Live Data Source: Scoring Engine real-time feed → PCE Live Consumer
```

---

## 🔒 SECTION PCE-4 — PARENT PREDICTIVE AI PEER COMPARISON LAYER

### PCE-4.1 — Parent ML Comparison Model Architecture (LOCKED)

```
MODEL_NAME              : ParentPeerProjectionModel (PPPM)
MODEL_TYPE              : Gradient Boosted Trees (XGBoost) + LSTM for trajectory
INPUT_WINDOW            : 30-day rolling activity + 90-day historical comparison snapshots
PREDICTION_HORIZON      : 14-day forward peer position projection
OUTPUT                  : Projected percentile position + confidence score + trend label
CONFIDENCE_FLOOR        : 60% — suppressed below this threshold
UPDATE_FREQUENCY        : Daily batch (Airflow) + event-triggered refresh
EXPLAINABILITY          : SHAP values — top 3 contributing features displayed
TONE_POLICY             : Non-shaming, growth-framing language mandatory
BIAS_AUDIT              : Quarterly — gender, region, age group checked
ROLLBACK_POLICY         : Prior model version retained 90 days post-retrain
HUMAN_APPROVAL_GATE     : Required for production deployment of new model version
```

### PCE-4.2 — Parent Comparison Output Catalog (LOCKED)

Each output type below is sealed. Tone rules and format rules are non-negotiable.

```
PARENT-PCE-001: INTELLIGENCE POSITION CARD
  Dimensions: DIM-001, DIM-002 (all intelligence domains)
  Cohort: COHORT-A (age) + COHORT-B (institute)
  Output Format:
    - Radar chart: child's intelligence profile vs cohort average profile
    - Percentile band per domain: top 10% / top 25% / top 50% / below 50%
    - Trend arrow: up / stable / down (30-day)
    - Projected position (14-day) with confidence
    - Top strength domain highlighted (positive framing)
    - Growth opportunity domain highlighted (constructive framing)
  Tone Rule: "Your child shows particular strength in [domain]"
             "There's room to grow in [domain] — here's a suggestion"
  FORBIDDEN: Any language implying failure, falling behind, or inferiority
  Update Cycle: Daily batch

PARENT-PCE-002: SKILL PROGRESSION POSITION CARD
  Dimensions: DIM-003, DIM-004, DIM-008 (skill level, XP rate, belt velocity)
  Cohort: COHORT-B + COHORT-D (institute + belt level)
  Output Format:
    - Belt position vs belt-level cohort percentile
    - Skill XP accumulation rate vs cohort rate
    - Time-to-next-belt estimate vs cohort median time
    - Top 3 strongest skills vs cohort
  Tone Rule: Progress-framed — "moving toward [next belt]"
  Update Cycle: Event-triggered + daily refresh

PARENT-PCE-003: CHAMPIONSHIP STANDING CARD
  Dimensions: DIM-005, DIM-006, DIM-007 (championship comparison)
  Cohort: COHORT-E (championship category)
  Output Format:
    - Bracket position percentile in current active championship
    - Win rate vs same-tier cohort
    - Championship trajectory — improving / stable / declining (30-day)
    - Next milestone proximity ("X wins from State qualifier")
  Tone Rule: Achievement-framed, no ranking shame language
  Update Cycle: Event-triggered on championship.result.published

PARENT-PCE-004: CAREER READINESS POSITION CARD
  Dimensions: DIM-013, DIM-015 (career readiness, recruiter attraction)
  Cohort: COHORT-F + COHORT-H (career path + national)
  Output Format:
    - Career readiness percentile vs national cohort
    - Recruiter attraction index vs career-path peers
    - Skill gap vs top-quartile peers in career path
    - Top 3 recommended skills to close gap
  Tone Rule: Opportunity-framed, not deficit-framed
  Update Cycle: Weekly batch

PARENT-PCE-005: ENGAGEMENT HEALTH COMPARISON CARD
  Dimensions: DIM-010, DIM-011 (engagement rate, streak consistency)
  Cohort: COHORT-A + COHORT-B (age + institute)
  Output Format:
    - Engagement rate vs institute cohort: above/at/below average band
    - Streak consistency percentile
    - Active days per week vs cohort distribution
  Privacy Rule: Most private output — no percentile number shown, only band
  Tone Rule: "Your child is actively engaged" / "Engagement has been lighter recently"
  Update Cycle: Daily batch
  Antigravity Integration: Feeds AG-PRED engagement collapse signal

PARENT-PCE-006: IMPROVEMENT VELOCITY CARD
  Dimensions: DIM-017 (30-day improvement velocity) across all active dimensions
  Cohort: COHORT-B + COHORT-A (institute + age)
  Output Format:
    - Improvement velocity percentile: "Improving faster than X% of peers this month"
    - Fastest-improving dimension highlighted
    - Slowest-improving dimension highlighted (constructive framing)
    - Velocity trend: accelerating / steady / slowing
  Tone Rule: Always lead with positive velocity data first
  Update Cycle: Weekly batch

PARENT-PCE-007: NATIONAL BENCHMARK CARD
  Dimensions: DIM-020 (national percentile) across composite score
  Cohort: COHORT-H (national)
  Output Format:
    - Composite national percentile: "In the top X% nationally in [age group]"
    - Domain breakdown: which domains drive national position
    - Trajectory: moving up / stable / moving down vs last month
  Privacy Rule: National cohort — only aggregate shown, no school/region breakdown
  Update Cycle: Weekly batch

PARENT-PCE-008: PEER GROUP CONTEXT CARD (Read-only — Anonymized)
  Purpose: Help parent understand what top-performing peers look like
  Output Format:
    - Anonymous top-quartile peer profile: "Top 25% peers in your child's cohort average..."
    - Skill profile of top-quartile cohort (anonymized aggregate)
    - Activity pattern of top-quartile cohort (anonymized aggregate)
    - Gap analysis: what your child does vs top-quartile peers
  Privacy Rule: Zero individual peer data — aggregate statistical profile only
  Minimum Cohort: k ≥ 20 peers required to serve this card
  Update Cycle: Weekly batch
```

### PCE-4.3 — Parent Comparison Tone Enforcement (LOCKED)

```
TONE_POLICY_CLASS: NON-NEGOTIABLE ENTERPRISE RULE

FORBIDDEN LANGUAGE PATTERNS:
  - "Your child is falling behind"
  - "Your child ranked [N]th out of [N]"
  - "Your child is in the bottom X%"
  - "Your child is worse than peers in..."
  - "Other students are performing better"
  - Any language implying the child is a failure
  - Direct peer name comparison (even anonymized peers should not be named)

REQUIRED LANGUAGE PATTERNS:
  - "Your child shows strength in..."
  - "Your child is making progress in..."
  - "There's an opportunity to grow in..."
  - "Your child is in the top X% in..."
  - "Your child's [dimension] is above the group average"
  - "Here's what could help your child go further..."

COMPARISON FRAMING RULE:
  - Always lead with strengths before development areas
  - Development areas must always come with a recommended action
  - Percentile position shown as positive bands (top X%) — never bottom X%
  - Below-median position shown as: "approaching the group average" or "developing in this area"
```

---

## 🔒 SECTION PCE-5 — STUDENT-FACING PEER COMPARISON

### PCE-5.1 — Student Comparison Views (LOCKED)

```
STUDENT-PCE-001: MY POSITION DASHBOARD
  Access: Student self only
  Dimensions: All 20 DIM dimensions (role-appropriate subset)
  Cohort Scope: COHORT-A + COHORT-B + COHORT-D + COHORT-E
  Output:
    - Multi-dimension radar chart: self vs cohort average vs top quartile
    - Percentile position per dimension
    - Strength highlights (top 3 dimensions)
    - Growth zones (bottom 3 dimensions with improvement suggestions)
    - 30-day improvement trend per dimension
  Peer Visibility: Zero — only own position vs statistical cohort profile
  Student Motivation Rule: Always show a "next milestone" — not just current position
  Update: Real-time on trigger + daily batch refresh

STUDENT-PCE-002: BELT COHORT COMPARISON
  Access: Student self only
  Scope: Same-belt-level peers globally (COHORT-D)
  Output:
    - How long student has been at current belt vs cohort median time-at-belt
    - Match score percentile vs same-belt cohort
    - Scenario pass rate vs same-belt cohort
    - "If you [action], you could reach [next belt] faster than X% of peers"
  Peer Visibility: Aggregate statistical only — no individual peer data
  Update: Event-triggered on belt.assessment events

STUDENT-PCE-003: CHAMPIONSHIP PERFORMANCE COMPARISON
  Access: Student self only
  Scope: COHORT-E (same championship category)
  Output:
    - Current bracket percentile position
    - Win rate vs tournament cohort
    - Strongest performance scenarios vs tournament peers
    - Trajectory: improving / stable / declining in tournament context
  Update: Event-triggered on championship.result.published

STUDENT-PCE-004: SKILL TRACK COMPARISON
  Access: Student self only
  Scope: COHORT-J (same skill track)
  Output:
    - XP progress percentile within same skill track cohort
    - Skill level position vs track cohort
    - Recommended next skill based on top-quartile peer paths
  Update: Daily batch + skill.level.up trigger

STUDENT-PCE-005: CAREER PATH PEER COMPARISON
  Access: Student (16+ age gate enforced)
  Scope: COHORT-F (same career path) + COHORT-H (national)
  Output:
    - Career readiness score vs career-path national cohort
    - Top 5 skills that differentiate top-quartile peers in career path
    - Placement probability estimate vs cohort
  Age Gate: Enforced — under 16 sees simplified version without career/placement data
  Update: Weekly batch
```

### PCE-5.2 — Student Comparison Gamification Integration (LOCKED)

```
GAMIFICATION_RULE: Peer comparison must motivate — never demoralize

Gamification Outputs from PCE:
  - "You've moved from bottom 50% to top 40% in [dimension] this month" → Badge trigger
  - "You're in the top 25% of [belt level] peers in [skill]" → Achievement trigger
  - "You've outperformed your previous tournament percentile" → Streak bonus trigger
  - "You're improving faster than 70% of peers this week" → Notification push

PCE → Achievement Engine event format:
  {
    event: "pce.milestone.reached",
    user_id: UUID,
    dimension: DIM-ID,
    cohort: COHORT-ID,
    milestone_type: "percentile_gain" | "top_quartile_entry" | "velocity_peak",
    milestone_value: NUMERIC,
    timestamp: ISO8601
  }

Rules:
  - No negative gamification from comparison (no "you fell behind" badges)
  - Improvement-only triggers — position drops do not generate gamification events
  - Maximum 3 PCE-based gamification events per user per day (frequency cap)
```

---

## 🔒 SECTION PCE-6 — MENTOR-FACING PEER COMPARISON

### PCE-6.1 — Mentor Comparison Panel (LOCKED)

```
MENTOR-PCE-001: STUDENT COHORT HEALTH PANEL
  Access: Assigned mentor only (RBAC enforced)
  Scope: All assigned students vs institute cohort
  Output:
    - Per-student comparison summary: percentile position across top 5 dimensions
    - Cohort health map: which students are in top/bottom quartile per dimension
    - Students showing fast improvement vs students showing decline
    - Students at risk of belt plateau (feeds Antigravity Risk Alert Engine)
  Privacy Rule: Mentor sees student names — cannot share across tenants
  Update: Daily batch

MENTOR-PCE-002: CALIBRATION COMPARISON PANEL
  Access: Mentor only
  Scope: Mentor's own scoring patterns vs cohort of mentors
  Output:
    - Mentor score distribution vs calibration benchmark
    - Mentor-awarded scores vs automated scoring percentile
    - Score drift trend (30-day) — is mentor drifting vs calibration standard
    - Override frequency vs mentor cohort
  Feeds: Antigravity MENTOR_RISK_001, MENTOR_RISK_002 signals
  Update: Daily batch

MENTOR-PCE-003: STUDENT PROMOTION READINESS COMPARISON
  Access: Mentor only
  Scope: Students being considered for belt promotion vs historical promotion cohort
  Output:
    - Candidate vs historical successful promotees on all belt criteria dimensions
    - Similarity score: how similar is this candidate to past successful promotees
    - Risk score: dimensions where candidate is weaker than typical promotees
    - Recommendation: mentor-advisory only — human decision required
  Autonomy Ceiling: Advisory only — mentor makes final promotion decision
  Update: On-demand when mentor opens promotion review
```

---

## 🔒 SECTION PCE-7 — RECRUITER-FACING PEER COMPARISON

### PCE-7.1 — Recruiter Comparison Access Rules (LOCKED)

```
RECRUITER_ACCESS_POLICY:
  - Recruiter may access candidate comparison data ONLY for candidates who have applied
    OR for candidates who have opted-in to recruiter discovery
  - No bulk comparison pulls without candidate consent
  - Comparison data shown to recruiter: anonymized percentile bands — NOT raw scores
  - Recruiter cannot compare two specific named candidates side-by-side
    without both candidates consenting to comparison disclosure
  - National percentile accessible to recruiter for opted-in candidates
  - Institute-level percentile: NOT accessible to recruiter (protect institute privacy)

RECRUITER-PCE-001: CANDIDATE COMPARISON PROFILE
  Access: Recruiter + candidate consent required
  Output:
    - National percentile for DIM-003 (skill), DIM-005 (championship), DIM-013 (career readiness)
    - Career path peer comparison: "Top X% of [career path] candidates nationally"
    - Skill reliability score vs national cohort
    - Championship performance tier (if applicable)
    - Improvement velocity trend (advisory)
  Privacy Shield: No peer names, no institute name, no location breakdown
  Update: Weekly batch for opted-in candidates

RECRUITER-PCE-002: COHORT SEARCH FILTER
  Access: Recruiter (with valid subscription)
  Purpose: Search candidates by peer comparison tier
  Allowed Filters:
    - Minimum national percentile threshold (e.g. top 30% in [skill])
    - Championship participation tier
    - Career readiness score range
    - Belt level
    - Improvement velocity band (fast-improving candidates)
  Forbidden Filters:
    - Filter by specific institution
    - Filter by age (age discrimination prevention)
    - Filter by region (cultural bias prevention)
  Results: Anonymized candidate cards — name revealed only on mutual match / application
```

---

## 🔒 SECTION PCE-8 — INSTITUTE ADMIN PEER COMPARISON

### PCE-8.1 — Institute Comparison Panel (LOCKED)

```
INSTITUTE-PCE-001: COHORT HEALTH DASHBOARD
  Access: Institute admin only
  Scope: All students in institute vs national cohort
  Output:
    - Institute cohort average vs national average per dimension
    - Institute percentile position nationally
    - Per-grade / per-track cohort breakdown
    - Students per quartile band (no names — count only unless admin drills down)
    - Improvement velocity: is the cohort improving or declining vs last semester
  Privacy Rule: Student-level drill-down only for own institution's students
  Cross-Tenant: FORBIDDEN — no other institution's student data visible

INSTITUTE-PCE-002: INSTITUTE VS INSTITUTE COMPARISON
  Access: Institute admin only
  Scope: Own institute vs anonymized national institutes in same category
  Output:
    - Institute intelligence score percentile nationally
    - Institute championship win rate vs national institute cohort
    - Institute placement rate vs national institute cohort
    - Institute improvement trend vs national trend
  Privacy Rule: Other institutes anonymized — no names, no locations revealed
  Minimum Cohort: At least 10 institutes in comparison pool required
  Update: Monthly batch

INSTITUTE-PCE-003: TRACK PERFORMANCE COMPARISON
  Access: Institute admin only
  Scope: Own institute's domain tracks vs national track averages
  Output:
    - Per-domain-track average skill level vs national track average
    - Track championship performance vs national track cohort
    - Track dropout / plateau rate vs national track cohort
  Update: Monthly batch
```

---

## 🔒 SECTION PCE-9 — PRIVACY & FAIRNESS GOVERNANCE

### PCE-9.1 — Privacy Enforcement Rules (LOCKED)

```
K_ANONYMITY_MINIMUM         : k ≥ 5 peers required to serve any comparison
SUPPRESSION_RULE            : If cohort size < 5 → suppress dimension → return "insufficient peers"
PII_STRIP_PIPELINE          : Mandatory before any output — all peer identifiers replaced with tokens
CROSS_TENANT_ISOLATION      : Institute cohort data never crosses tenant boundary
MINOR_PROTECTION            : Users under 13 — no peer comparison shown in any form
                              Users 13–15 — limited comparison (no career, no recruiter, no national)
                              Users 16+ — full comparison (with consent)
PARENT_DATA_SCOPE           : Parent sees only own child's comparison — no other child's data
RECRUITER_CONSENT_GATE      : Candidate must opt-in before any comparison data shown to recruiter
RIGHT_TO_WITHDRAW           : User can opt out of all peer comparison — comparison data purged in 48hrs
DATA_RETENTION              : Comparison snapshots retained 24 months — then purged
GDPR_COMPLIANCE             : Full GDPR data subject rights implemented
```

### PCE-9.2 — Fairness & Bias Audit Rules (LOCKED)

```
AUDIT_FREQUENCY             : Quarterly mandatory bias audit
AUDIT_DIMENSIONS            : Gender, region, age group, domain, socioeconomic proxy
AUDIT_METHOD                : Statistical disparity analysis — Cohen's d effect size per group
ACCEPTABLE_DISPARITY        : Cohen's d < 0.2 — flagged for review if exceeded
REMEDIATION_TRIGGER         : If Cohen's d > 0.4 on any protected dimension → model retrain required
FAIRNESS_REPORT             : Published to compliance officer + platform governance board quarterly
CULTURAL_FAIRNESS           : Scenario difficulty regional adjustment applied before comparison
REGIONAL_ADJUSTMENT         : Benchmark normalized for regional education system variance
OUTCOME_VALIDATION          : Annual check — do PCE comparison signals predict real-world outcomes fairly?
```

---

## 🔒 SECTION PCE-10 — DATABASE SCHEMA REQUIREMENTS

```sql
-- Core comparison snapshot (latest)
CREATE TABLE pce_comparison_snapshots (
  snapshot_id         UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  user_id             UUID NOT NULL,
  tenant_id           UUID NOT NULL,
  dimension_id        VARCHAR(10) NOT NULL,        -- DIM-001 through DIM-020
  cohort_id           VARCHAR(10) NOT NULL,         -- COHORT-A through COHORT-J
  raw_score           NUMERIC(10,4),
  normalized_score    NUMERIC(10,4),
  percentile          NUMERIC(5,2),                 -- 0–100
  cohort_median       NUMERIC(10,4),
  cohort_p75          NUMERIC(10,4),
  cohort_p90          NUMERIC(10,4),
  quartile_band       VARCHAR(20),                  -- top_10 | top_25 | top_50 | below_50
  velocity_30d        NUMERIC(10,4),                -- improvement delta over 30 days
  velocity_label      VARCHAR(20),                  -- accelerating | steady | slowing | declining
  confidence_score    NUMERIC(5,2),                 -- 0–100
  cohort_size         INTEGER,
  is_suppressed       BOOLEAN NOT NULL DEFAULT FALSE, -- k-anonymity suppression
  suppression_reason  VARCHAR(100),
  computed_at         TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  UNIQUE (user_id, dimension_id, cohort_id)
);

-- Time-series comparison history (ClickHouse)
-- Table: pce_comparison_history
-- Fields: snapshot_id, user_id, tenant_id, dimension_id, cohort_id,
--         percentile, quartile_band, velocity_30d, confidence_score,
--         cohort_size, computed_at
-- Engine: MergeTree() ORDER BY (user_id, dimension_id, cohort_id, computed_at)

-- Parent projection snapshots
CREATE TABLE pce_parent_projections (
  projection_id       UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  child_user_id       UUID NOT NULL,
  parent_user_id      UUID NOT NULL,
  tenant_id           UUID NOT NULL,
  dimension_id        VARCHAR(10) NOT NULL,
  cohort_id           VARCHAR(10) NOT NULL,
  current_percentile  NUMERIC(5,2),
  projected_percentile NUMERIC(5,2),
  projection_horizon_days INTEGER NOT NULL DEFAULT 14,
  confidence_score    NUMERIC(5,2),
  top_feature_1       VARCHAR(100),
  top_feature_2       VARCHAR(100),
  top_feature_3       VARCHAR(100),
  recommendation_text TEXT,
  computed_at         TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- Cohort registry
CREATE TABLE pce_cohort_definitions (
  cohort_id           VARCHAR(10) PRIMARY KEY,
  cohort_name         VARCHAR(100) NOT NULL,
  scope               VARCHAR(50) NOT NULL,  -- institute | domain | belt | national | global
  cross_tenant        BOOLEAN NOT NULL DEFAULT FALSE,
  min_k_anonymity     INTEGER NOT NULL DEFAULT 5,
  description         TEXT
);

-- Privacy opt-out registry
CREATE TABLE pce_privacy_optouts (
  user_id             UUID PRIMARY KEY,
  opted_out_at        TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  purge_completed_at  TIMESTAMPTZ,
  purge_requested_by  UUID
);

-- Fairness audit log (immutable)
CREATE TABLE pce_fairness_audits (
  audit_id            UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  audit_date          DATE NOT NULL,
  dimension_id        VARCHAR(10) NOT NULL,
  cohort_id           VARCHAR(10) NOT NULL,
  group_dimension     VARCHAR(50) NOT NULL,  -- gender | region | age_group | domain
  cohens_d            NUMERIC(6,4) NOT NULL,
  disparity_flag      BOOLEAN NOT NULL,
  remediation_required BOOLEAN NOT NULL,
  remediation_action  TEXT,
  audited_by          UUID NOT NULL,
  audit_hash          VARCHAR(64) NOT NULL   -- SHA-256 immutability
);
```

---

## 🔒 SECTION PCE-11 — API CONTRACT REGISTRY

```
── STUDENT ENDPOINTS ──
GET    /pce/students/{id}/my-position                  — Student self comparison dashboard
GET    /pce/students/{id}/belt-comparison               — Belt cohort comparison
GET    /pce/students/{id}/championship-comparison       — Championship comparison
GET    /pce/students/{id}/skill-comparison/{skill_id}  — Skill track comparison
GET    /pce/students/{id}/career-comparison            — Career path comparison (16+ gate)

── PARENT ENDPOINTS ──
GET    /pce/parents/{parent_id}/child/{child_id}/intelligence-card   — Intelligence position card
GET    /pce/parents/{parent_id}/child/{child_id}/skill-card          — Skill progression card
GET    /pce/parents/{parent_id}/child/{child_id}/championship-card   — Championship standing card
GET    /pce/parents/{parent_id}/child/{child_id}/career-card         — Career readiness card (16+ gate)
GET    /pce/parents/{parent_id}/child/{child_id}/engagement-card     — Engagement health card
GET    /pce/parents/{parent_id}/child/{child_id}/velocity-card       — Improvement velocity card
GET    /pce/parents/{parent_id}/child/{child_id}/national-benchmark  — National benchmark card
GET    /pce/parents/{parent_id}/child/{child_id}/peer-context        — Anonymous peer context card

── MENTOR ENDPOINTS ──
GET    /pce/mentors/{id}/student-cohort-panel           — All assigned students panel
GET    /pce/mentors/{id}/calibration-panel              — Mentor calibration comparison
GET    /pce/mentors/{id}/student/{student_id}/promotion-readiness — Promotion readiness comparison

── RECRUITER ENDPOINTS ──
GET    /pce/recruiters/{id}/candidate/{candidate_id}/comparison — Candidate comparison (consent required)
POST   /pce/recruiters/{id}/search                     — Cohort-filtered candidate search

── INSTITUTE ADMIN ENDPOINTS ──
GET    /pce/institutes/{id}/cohort-health               — Cohort health dashboard
GET    /pce/institutes/{id}/national-comparison         — Institute vs national comparison
GET    /pce/institutes/{id}/track-comparison            — Track performance comparison

── PLATFORM ADMIN ENDPOINTS ──
GET    /pce/admin/fairness-audit/{audit_id}            — Fairness audit report
GET    /pce/admin/model-health                         — PCE agent model health
POST   /pce/admin/trigger-recompute                    — Manual recompute trigger
GET    /pce/admin/suppression-report                   — k-anonymity suppression report

── INTERNAL SERVICE ENDPOINTS ──
POST   /pce/internal/snapshot                          — Create/update comparison snapshot
POST   /pce/internal/projection                        — Create parent projection
POST   /pce/internal/optout/{user_id}                  — Privacy opt-out trigger
GET    /pce/internal/vector/{user_id}                  — Raw comparison vector (internal only)

AUTHENTICATION: JWT required on all endpoints
AUTHORIZATION: Per-role RBAC enforced — see PCE-9.1
RATE LIMITING: 100 req/min per user, 1000 req/min per service
AUDIT LOGGING: All read events logged with requestor_id and timestamp
```

---

## 🔒 SECTION PCE-12 — FLUTTER UI SCREENS REQUIRED

```
PCE-SCREEN-001: My Peer Position Dashboard (Student)
  Module: Skill_Development_UI
  Role: Student
  Stage: INTELLIGENCE_UI (Stage 2)
  Components:
    - Multi-dimension radar chart (self vs cohort median vs top-quartile)
    - Dimension percentile cards (horizontal scroll — one per active dimension)
    - Trend badges: up / stable / down per dimension
    - Top 3 strength highlights
    - Top 3 growth opportunity areas with "how to improve" CTA
    - "Next milestone" progress bar (belt / championship / career)
  Fixed Section: Identity block + compliance badges
  Customizable: Dimension order, visible dimension set, cohort scope selector

PCE-SCREEN-002: Championship Bracket Position Screen (Student)
  Module: Championship UI (within Skill_Development_UI)
  Role: Student
  Stage: INTELLIGENCE_UI
  Components:
    - Bracket position donut (my percentile highlighted)
    - Win rate bar vs cohort average
    - Score consistency indicator (low/medium/high variance label)
    - Trajectory arrow: improving / stable / declining
    - "What top 25% bracket peers do differently" advisory card

PCE-SCREEN-003: Parent Child Intelligence Position Card (Parent Dashboard)
  Module: ERP_UI (Parent sub-module)
  Role: Parent / Guardian
  Stage: INTELLIGENCE_UI
  Components:
    - Child's intelligence radar vs age cohort radar (overlay)
    - Domain percentile band pills (top 10% / top 25% / developing / etc.)
    - Strength callout: highlighted positive domain
    - Growth suggestion: constructive framing with action link
    - 14-day projection indicator with confidence badge
    - Tone enforcement: UI text pulled from tone-controlled content registry

PCE-SCREEN-004: Parent Championship Standing Card
  Module: ERP_UI (Parent sub-module)
  Role: Parent / Guardian
  Stage: INTELLIGENCE_UI
  Components:
    - Bracket tier position indicator (visual band — not raw rank number)
    - Win trajectory chart (30-day sparkline)
    - Championship readiness signal
    - "Next championship milestone" CTA

PCE-SCREEN-005: Mentor Student Cohort Health Panel
  Module: Group_Discussion_UI (Mentor sub-module)
  Role: Mentor
  Stage: INTELLIGENCE_UI
  Components:
    - Student comparison grid: each student row with percentile per top-5 dimensions
    - Color-coded health bands (green / amber / red — per student per dimension)
    - Sort / filter by dimension, trend, risk flag
    - Drill-down to individual student full comparison panel
    - "At risk" flagged students pinned to top

PCE-SCREEN-006: Recruiter Candidate Comparison Profile
  Module: Job_Portal_UI (Recruiter sub-module)
  Role: Recruiter (with candidate consent)
  Stage: ECOSYSTEM_UI (Stage 3)
  Components:
    - National percentile band display per skill dimension
    - Career readiness tier badge
    - Championship participation indicator (if applicable)
    - Improvement velocity label (fast / steady / slow)
    - Skill gap vs job requirements (computed from job posting vs candidate PCE vector)

PCE-SCREEN-007: Institute Cohort Health Dashboard
  Module: ERP_UI (Institute Admin sub-module)
  Role: Institute Admin
  Stage: ECOSYSTEM_UI
  Components:
    - Cohort quartile distribution chart per dimension
    - National comparison bar (cohort avg vs national avg)
    - Track breakdown panel
    - Improvement trend by semester
    - Drill-down to individual student (own students only)
```

---

## 🔒 SECTION PCE-13 — EVENT INTEGRATION REGISTRY

### PCE-13.1 — Events PCE Consumes (Kafka)

```
Event Topic                     → PCE Action
match.scored                    → Recompute DIM-007, DIM-009 for user
belt.earned                     → Recompute DIM-008, DIM-003 for user + COHORT-D rebalance
championship.result.published   → Recompute all CHAMP-DIM for user
skill.level.up                  → Recompute DIM-003, DIM-004 for user
intelligence.assessment.completed → Recompute DIM-001, DIM-002 for user
enrollment.completed            → Add user to applicable cohorts
project.milestone.verified      → Recompute DIM-014 for user
user.created                    → Initialize comparison baseline snapshot
user.deleted                    → Trigger privacy purge workflow
parent.consent.granted          → Enable parent PCE outputs for child
parent.consent.revoked          → Disable + purge parent PCE outputs for child
candidate.opted.in.recruiter    → Enable recruiter PCE outputs for candidate
candidate.opted.out.recruiter   → Disable + purge recruiter PCE outputs
pce.optout.requested            → Trigger full comparison data purge
```

### PCE-13.2 — Events PCE Publishes (Kafka)

```
Event Topic                     → Consumed By
pce.milestone.reached           → Achievement Engine (gamification trigger)
pce.comparison.updated          → Notification Service (advisory notifications)
pce.risk.signal                 → Antigravity Risk Alert Engine (AG-PRED signals)
pce.parent.projection.ready     → Parent Dashboard Service (push new projection)
pce.recruiter.match.signal      → Recruiter Engine (high-match candidate alert)
pce.bias.audit.completed        → Compliance Service (fairness audit result)
pce.suppression.applied         → Analytics Service (k-anonymity suppression log)
```

---

## 🔒 SECTION PCE-14 — ANTIGRAVITY INTEGRATION REGISTRY

### PCE → Antigravity Risk Alert Engine Signal Map

```
PCE Signal                                 → AG Alert Code           → Alert Class
Engagement rate percentile drops > 30 pts → PARENT_RISK_001         → AG-PRED
Performance percentile drops > 20 pts     → PARENT_RISK_002         → AG-PRED
Championship percentile drops > 20 pts    → PARENT_RISK_003 (input) → AG-PRED
Career readiness percentile < 25th pct    → PARENT_RISK_004 (input) → AG-PRED
Belt velocity percentile < 10th pct       → PARENT_RISK_005 (input) → AG-HIGH
Engagement → 0 while prior pct > 50th    → PARENT_RISK_006 (input) → AG-HIGH
Improvement velocity label = "declining"  → PARENT_RISK_008 (input) → AG-HIGH
All-dimension decline simultaneously      → AG-STRUCT (cohort level) → AG-STRUCT
Cohort bias audit: Cohen's d > 0.4       → AG-STRUCT               → AG-STRUCT
```

### PCE → Antigravity Data Format (LOCKED)

```json
{
  "pce_signal_id": "UUID",
  "signal_type": "comparison_decline | milestone_reach | bias_alert",
  "user_id": "UUID",
  "tenant_id": "UUID",
  "dimension_id": "DIM-XXX",
  "cohort_id": "COHORT-X",
  "current_percentile": 32.5,
  "prior_percentile": 58.2,
  "delta": -25.7,
  "velocity_label": "declining",
  "confidence_score": 78.4,
  "top_features": ["engagement_rate", "streak_count", "session_duration"],
  "ag_alert_code": "PARENT_RISK_002",
  "timestamp": "ISO8601"
}
```

---

## 🔒 SECTION PCE-15 — OBSERVABILITY REQUIREMENTS

```
REQUIRED METRICS:
  pce_comparisons_computed_total{dimension, cohort}        — Computation volume
  pce_comparison_latency_seconds{mode}                     — Per-mode latency histogram
  pce_suppression_rate{cohort}                             — k-anonymity suppression rate
  pce_cache_hit_rate                                       — Redis cache efficiency
  pce_model_confidence_avg{dimension}                      — Average confidence per dimension
  pce_parent_projections_generated_total                   — Parent ML layer volume
  pce_bias_cohens_d{dimension, group_dimension}            — Live fairness metric
  pce_optout_total                                         — Privacy opt-out count
  pce_ag_signals_emitted_total{alert_code}                 — Antigravity signal volume
  pce_kafka_consumer_lag{topic}                            — Event processing lag

REQUIRED DASHBOARDS:
  — PCE Agent Health Monitor (ops team)
  — Peer Comparison Coverage Dashboard (what % of users have fresh vectors)
  — Parent Projection Quality Dashboard (confidence distribution)
  — Privacy & Suppression Dashboard (k-anonymity compliance)
  — Fairness Audit Dashboard (bias monitoring, live Cohen's d)
  — Antigravity Signal Feed Dashboard (PCE → AG signal volume)

REQUIRED ALERTING (ops-level):
  — Kafka consumer lag > 5 min → ops page
  — Comparison freshness < 80% of active users → ops alert
  — Cache hit rate drops < 70% → ops alert
  — Bias audit: Cohen's d exceeds threshold → compliance notification
  — Parent projection confidence avg drops < 65% → ML team notification
  — Privacy purge SLA missed (48hr) → compliance alert
```

---

## 🔒 SECTION PCE-16 — SECURITY HARDENING

```
COMPARISON_PAYLOAD_ENCRYPTION   : AES-256 at rest (PostgreSQL + ClickHouse)
TRANSIT_ENCRYPTION              : TLS 1.3 mandatory on all PCE API endpoints
PII_IN_COMPARISON_DATA          : FORBIDDEN — zero PII in comparison vectors
PEER_TOKEN_ROTATION             : Anonymous peer tokens rotated daily
AUDIT_LOG_IMMUTABILITY          : SHA-256 hash chain on all fairness audits
RBAC_ON_PCE_API                 : Per-role access enforced — no cross-role leakage
RECRUITER_CONSENT_ENFORCEMENT   : API middleware blocks recruiter endpoints without consent flag
MINOR_AGE_GATE_ENFORCEMENT      : API middleware enforces age gates before serving comparison data
TENANT_ISOLATION_ENFORCEMENT    : Row-level security on pce_comparison_snapshots
RATE_LIMITING                   : Per-user + per-service rate limits enforced at Kong API Gateway
DEBUG_MODE_BLOCK                : PCE API never returns raw peer identifiers in any mode
SCREENSHOT_BLOCK                : Championship live comparison screens — screenshot blocked
```

---

## 🔒 SECTION PCE-17 — TEST REQUIREMENTS

```
UNIT TESTS:
  - Normalization function correctness (Z-score + Winsorize)
  - k-anonymity suppression trigger accuracy
  - Percentile computation accuracy vs known distributions
  - Age gate enforcement logic
  - Tone policy compliance (language pattern assertion)
  - Privacy opt-out data purge completeness

INTEGRATION TESTS:
  - Kafka event → snapshot update latency < 60s
  - API → Redis cache serve path
  - API → PostgreSQL direct read path
  - Parent projection model output format validation
  - Antigravity signal emission validation
  - RBAC enforcement per role per endpoint

FAIRNESS TESTS (required quarterly):
  - Cohen's d computation accuracy
  - Disparity detection threshold enforcement
  - Regional adjustment factor application
  - Cultural normalization consistency

LOAD TESTS:
  - 10,000 concurrent on-demand comparison requests < 500ms
  - Real-time event processing throughput: 1,000 events/second
  - Parent dashboard load under 5,000 concurrent parents
  - Live championship comparison feed: 50,000 concurrent spectators

PRIVACY TESTS:
  - k-anonymity suppression: verify < 5 peer cohorts always return suppressed
  - Cross-tenant isolation: confirm zero data leakage between tenants
  - Opt-out purge: confirm all comparison data purged within 48 hours
  - Minor protection: confirm under-13 users return empty comparison
```

---

## 🔒 SECTION PCE-18 — FINAL GOVERNANCE SEAL BLOCK

```
╔══════════════════════════════════════════════════════════════════════╗
║      ANTIGRAVITY PEER COMPARISON ENGINE — FINAL MASTER SEAL         ║
╠══════════════════════════════════════════════════════════════════════╣
║  ENGINE CLASS            : CHAMPIONSHIP ADVANCED + PARENT ML        ║
║  ARTIFACT TYPE           : AGENT SYSTEM SPECIFICATION               ║
║  STATUS                  : LOCKED · SEALED · GOVERNED · FINAL       ║
║  MUTATION POLICY         : ADD-ONLY VIA VERSION BUMP                ║
║  INTERPRETATION          : FORBIDDEN                                 ║
║  CREATIVE DEVIATION      : FORBIDDEN                                 ║
║  ASSUMPTION FILLING      : FORBIDDEN                                 ║
║  HUMAN OVERRIDE          : REQUIRED FOR ALL CRITICAL OUTPUTS         ║
║  AI AUTONOMY CEILING     : COMPUTATION + ADVISORY ONLY              ║
║  PEER PII EXPOSURE       : FORBIDDEN — ANONYMIZED ONLY              ║
║  K-ANONYMITY MINIMUM     : k ≥ 5 — HARD ENFORCED                   ║
║  TONE POLICY             : NON-SHAMING — GROWTH FRAMED REQUIRED     ║
║  CROSS-TENANT ISOLATION  : HARD — ZERO LEAKAGE TOLERANCE            ║
║  MINOR PROTECTION        : HARD — UNDER-13 ZERO COMPARISON          ║
║  BIAS AUDIT              : QUARTERLY — COHEN'S D MANDATORY          ║
║  FAIRNESS REMEDIATION    : ENFORCED ON d > 0.4                      ║
║  ANTIGRAVITY INTEGRATION : ACTIVE — SIGNAL MAP LOCKED               ║
║  SECURITY MODE           : AES-256 + TLS 1.3 + RBAC + RLS          ║
║  OBSERVABILITY           : FULL TELEMETRY REQUIRED                  ║
║  TEST GATE               : MANDATORY BEFORE PRODUCTION DEPLOY        ║
║  ABSENCE OF ANY SECTION  : STOP EXECUTION                           ║
╚══════════════════════════════════════════════════════════════════════╝

ADD THIS BLOCK TO ECOSKILLER MASTER PROMPT:

ANTIGRAVITY PEER COMPARISON ENGINE        : ACTIVE
Championship Advanced PCE Layer           : ACTIVE
Parent Predictive ML Comparison Layer     : ACTIVE
20-Dimension Comparison Registry          : LOCKED
10-Cohort Type Registry                   : LOCKED
4-Mode Agent Execution                    : ACTIVE
K-Anonymity Privacy Guard                 : ENFORCED (k ≥ 5)
Tone Policy Enforcement                   : NON-SHAMING ACTIVE
Minor Age Gate                            : HARD ENFORCED
Bias & Fairness Audit                     : QUARTERLY MANDATORY
Antigravity Signal Integration            : ACTIVE
Recruiter Consent Gate                    : ENFORCED
Cross-Tenant Isolation                    : HARD ENFORCED
Gamification Integration                  : IMPROVEMENT-ONLY TRIGGERS
Human Override Authority                  : ENFORCED
Interpretation Authority                  : NONE
Architecture Authority                    : LOCKED
```

---

*Peer Comparison Engine (PCE) v1.0 — Antigravity Agent Specification*
*Ecoskiller Championship Advanced + Parent Predictive AI ML Layer*
*Sealed under DOJO SAAS PRODUCTION MODE + ECOSKILLER MASTER EXECUTION PROMPT v12.0*
*Add-only. No mutation without version bump. No interpretation. No assumption filling.*
*Antigravity Execution Engine — Deterministic. Governed. Locked.*
