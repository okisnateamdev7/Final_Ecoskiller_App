# ERP_GD_REPORT_INTEGRATION_AGENT
## Ecoskiller Platform — Sealed & Locked Production Agent Specification
### Agent Class: Analytics & ERP Intelligence Layer — GD Report Integration Engine
### Execution Engine: ANTIGRAVITY (Google)

---

```
ARTIFACT_CLASS        = PRODUCTION_AGENT_SPEC
AGENT_ID              = ERP_GD_REPORT_INTEGRATION_AGENT
VERSION               = v1.0.0
STATUS                = FINAL · SEALED · LOCKED · GOVERNED · DETERMINISTIC
MUTATION_POLICY       = ADD-ONLY VIA VERSION BUMP
CREATIVE_INTERP       = FORBIDDEN
ASSUMPTION_FILLING    = FORBIDDEN
EXECUTION_ENGINE      = ANTIGRAVITY
DEFAULT_BEHAVIOR      = DENY
FAILURE_MODE          = STOP_EXECUTION → REPORT → NO_PARTIAL_OUTPUT
INTERPRETATION_AUTH   = NONE
EXECUTION_AUTH        = HUMAN DECLARATION ONLY
DETERMINISM_RULE      = IDENTICAL SESSION DATA → IDENTICAL REPORT OUTPUT — ALWAYS
IDEMPOTENCY_RULE      = REPORT GENERATION EXECUTES EXACTLY ONCE PER SESSION/BATCH/PERIOD
```

---

## ⚠️ ANTIGRAVITY BINDING DIRECTIVE (READ BEFORE ANY EXECUTION)

```
⚠️ Antigravity MUST NOT reinterpret GD scoring formulas, report templates,
   or ERP data routing under any circumstance.

⚠️ Antigravity MUST NOT generate duplicate reports. Idempotency law IDP-16 applies:
   report generation executes exactly once per (session_id | batch_id | period) key.

⚠️ Antigravity MUST NOT allow cross-tenant report access. Every report, every PDF,
   every API response is tenant-scoped and role-gated at all layers.

⚠️ Antigravity MUST NOT expose PII (candidate names, phone, email) in any
   cross-role report unless the consuming role has explicit data access consent logged.

⚠️ Antigravity MUST NOT skip the PDF security encoding rules (Section 10).
   Unsafe export generation is a hard STOP per system output encoding law.

⚠️ Antigravity MUST NOT skip consent verification before generating any report that
   includes candidate behavioral data. Feature gating by consent is non-negotiable.

⚠️ Absence of any required component:
   → STOP EXECUTION
   → REPORT ERP-GD-REPORT-AGENT-INCOMPLETE
   → NO DEPLOYMENT CLAIM PERMITTED
```

---

## SECTION 1 — AGENT IDENTITY & PURPOSE (NON-NEGOTIABLE)

### 1.1 Agent Name
`ERP_GD_REPORT_INTEGRATION_AGENT`

### 1.2 Platform Context
This agent operates inside the **Ecoskiller Unified SaaS Platform** — a multi-tenant
enterprise system. It sits at the intersection of four platform layers:

- **Voice GD Orchestrator** — deterministic session execution, turn engine, forced mute/unmute, numeric capture
- **Scoring Engine (LOCKED)** — weighted rule-based GD scores, immutable audit logs
- **Analytics Service** — ClickHouse metric consumer, ERP dashboard feeder
- **ERP Layer** — Institute ERP, Corporate Hiring ERP, Compliance & Audit ERP, Analytics & ROI dashboards

This agent is the **authoritative integration point** that transforms raw GD session data
into structured, role-appropriate ERP reports — consumed by:
- **Institute ERP admins & TPOs** (Training & Placement Officers)
- **Corporate hiring ERP managers** — campus hiring, mass screening
- **Recruiter / HR users** — GD batch outcomes for hiring pipeline
- **Platform admins** — compliance, audit, governance
- **Students** — personal session scorecards
- **Parents** — monthly progress report (consent-gated)

### 1.3 Agent Role
This agent is the **single authority** responsible for:

1. **Session Report Generation** — Producing per-session GD scorecards for each participant immediately after session closure
2. **Batch Report Generation** — Aggregating all sessions within a GD batch into a structured batch report for institute/recruiter ERP
3. **Monthly ERP Report Generation** — Producing monthly GD performance summaries per tenant, institute, recruiter, and student
4. **PDF Report Assembly & Storage** — Generating signed PDFs, storing in MinIO, emitting download links via Notification Service
5. **ERP Dashboard Data Feed** — Pushing structured JSON metric payloads to the ERP analytics layer (ClickHouse / Grafana)
6. **TPO Cohort Analytics Integration** — Serving institute-level cohort placement readiness reports for TPO dashboards
7. **Recruiter GD Outcome Feeds** — Delivering structured hiring-decision-ready GD summaries per batch and per candidate
8. **Parent Progress Report Integration** — Generating consent-gated monthly student GD performance summaries
9. **Compliance & Audit Export** — Producing immutable, auditable GD session export packages for compliance officers
10. **Idempotency Enforcement** — Guaranteeing every report generates exactly once regardless of retry or replay

### 1.4 Agent Classification

```
AGENT_TYPE            = ERP Integration + Report Generation + Analytics Feed
SCOPE                 = Platform-Wide (All Tenants, All GD-Consuming Roles)
REPORT_FORMATS        = PDF · JSON · CSV · Structured ERP Payload
STORAGE_ENGINE        = MinIO (report files) + PostgreSQL (report metadata) + ClickHouse (ERP metrics)
PIPELINE_ENGINE       = Apache Airflow (scheduled reports) + Kafka (event-driven reports)
PDF_ENGINE            = WeasyPrint (self-hosted, open-source — no paid SaaS PDF services)
AUDIT_CLASS           = Immutable · Append-only · Consent-logged
IDEMPOTENCY_CLASS     = IDP-16 compliant (exactly-once report generation)
CONSENT_CLASS         = Feature-gated (consent required for candidate behavioral data in cross-role reports)
```

---

## SECTION 2 — SCOPE BOUNDARIES (HARD LOCK)

### 2.1 In Scope — ALL REQUIRED — ANTIGRAVITY MUST IMPLEMENT ALL

| Report Type | Trigger | Consumer Role(s) | Format |
|---|---|---|---|
| Per-Session Participant Scorecard | GD session closes | Student (self) | PDF + JSON |
| Per-Session Admin Summary | GD session closes | Recruiter, TPO, Institute ERP | PDF + JSON |
| GD Batch Report | All sessions in batch close | Recruiter, Institute ERP admin, HR | PDF + CSV + JSON |
| Monthly Student GD Summary | 1st of month (Airflow) | Student (self) | PDF (email) |
| Monthly Parent Progress Report | 1st of month (Airflow) | Parent (consent-gated) | PDF (email) |
| Monthly Institute GD Cohort Report | 1st of month (Airflow) | Institute ERP admin, TPO | PDF + CSV |
| Monthly Recruiter GD Outcomes Report | 1st of month (Airflow) | Recruiter, HR manager | PDF + JSON |
| Monthly Platform GD Analytics | 1st of month (Airflow) | Platform admin | Dashboard JSON |
| Compliance Audit Export | On-demand (admin trigger) | Compliance officer, Platform admin | PDF + CSV (immutable) |
| ERP Dashboard Metric Feed | After every session + daily | Grafana, ERP UI (Flutter/Web) | JSON (ClickHouse-backed) |
| TPO Cohort Readiness Report | Weekly (Airflow) | TPO, Institute ERP | PDF + JSON |
| Recruiter Hiring Decision Pack | On batch close | Recruiter (per batch) | Structured JSON + PDF |

### 2.2 Explicitly Out of Scope — FORBIDDEN — ANTIGRAVITY MUST NOT IMPLEMENT

```
❌ Audio or video content in any report (GD is voice-only; no recordings stored per system design)
❌ Tone, sentiment, accent, or personality inference in any report output
❌ AI/LLM-generated narrative text in reports (rule-based summaries only — R28 compliance)
❌ Cross-tenant candidate data in any single report
❌ Recruiter access to individual candidate scores without candidate consent
❌ Direct frontend PDF generation (all PDFs generated server-side by WeasyPrint — never client-side)
❌ Report generation on unauthenticated requests
❌ Report delivery to parent without student consent record verified first
❌ Manual score override in report output without immutable audit record
❌ Deletion of generated reports (MinIO lifecycle policies only — no manual delete via API)
❌ CSV injection in any export (output encoding law: formula-trigger chars must be escaped)
```

---

## SECTION 3 — SYSTEM ARCHITECTURE (NON-NEGOTIABLE)

### 3.1 Service Identity

```
Service Name:         erp-gd-report-integration-service
Namespace:            analytics (Kubernetes)
Language:             Python 3.11 (FastAPI for report API surface)
PDF Engine:           WeasyPrint (self-hosted — analytics namespace)
Template Engine:      Jinja2 (HTML → PDF pipeline via WeasyPrint)
Storage:              MinIO (report files) — bucket: erp-gd-reports
Report Metadata DB:   PostgreSQL (report registry, delivery log, idempotency keys)
Metrics Sink:         ClickHouse (ERP metric payloads)
Event Source:         Apache Kafka (gd.completed, gd.batch.closed, billing events)
Pipeline Orchestrator: Apache Airflow (monthly, weekly scheduled reports)
Notification:         Notification Service (email + push — async, not direct)
Auth Boundary:        Keycloak JWT (role-gated: STUDENT | RECRUITER | TPO | INSTITUTE_ERP | PLATFORM_ADMIN | PARENT)
Secret Store:         HashiCorp Vault
```

### 3.2 Architecture Topology (LOCKED)

```
┌──────────────────────────────────────────────────────────────────────────┐
│                    KAFKA EVENT BUS                                        │
│  gd.completed · gd.batch.closed · reputation.signal.applied             │
│  interview.completed · invoice.generated · user.consent.updated          │
└─────────────────────────┬────────────────────────────────────────────────┘
                          │ Kafka Consumer
                          ▼
┌──────────────────────────────────────────────────────────────────────────┐
│           REPORT TRIGGER ROUTER                                           │
│  Reads event_type → determines report type → checks idempotency key     │
│  If key exists → skip (idempotent)                                       │
│  If key new → enqueue report generation job (RabbitMQ background worker) │
└─────────────────────────┬────────────────────────────────────────────────┘
                          │ RabbitMQ background job
                          ▼
┌──────────────────────────────────────────────────────────────────────────┐
│           REPORT GENERATION WORKER (per report type)                     │
│                                                                           │
│  Step 1: Load session/batch data from PostgreSQL + ClickHouse            │
│  Step 2: Verify consent gates (candidate behavioral data in cross-role)  │
│  Step 3: Apply GD scoring display rules (rule-based — no AI narrative)  │
│  Step 4: Render Jinja2 HTML template (report-type-specific)              │
│  Step 5: Generate PDF via WeasyPrint (server-side only)                  │
│  Step 6: Apply PDF security rules (encoding, no script injection)        │
│  Step 7: Upload PDF to MinIO (bucket: erp-gd-reports)                   │
│  Step 8: Write report metadata to PostgreSQL (gd_reports registry)       │
│  Step 9: Emit Kafka: gd.report.generated                                 │
│  Step 10: Push ERP metric payload to ClickHouse (structured JSON)        │
│  Step 11: Trigger Notification Service (async — email + push)            │
│  Step 12: Mark idempotency key as COMPLETE in Redis                      │
└─────────────────────────┬────────────────────────────────────────────────┘
                          │ MinIO storage + PostgreSQL registry
                          ▼
┌──────────────────────────────────────────────────────────────────────────┐
│           REPORT API SURFACE (FastAPI — analytics namespace)             │
│  - Role-gated report download endpoints (pre-signed MinIO URLs)         │
│  - ERP dashboard metric query endpoints                                  │
│  - TPO cohort analytics endpoints                                        │
│  - Recruiter batch outcome endpoints                                     │
│  - Compliance audit export endpoints                                     │
│  - Report regeneration (admin only — idempotency override required)     │
└──────────────────────────────────────────────────────────────────────────┘
```

### 3.3 Architecture Principles (HARD LOCK)

```
PRINCIPLE-R-01: Report generation is ALWAYS async (background worker) — never blocking API
PRINCIPLE-R-02: Every report generation is idempotent — identical (session_id|batch_id|period+tenant_id)
                produces one report — retries never produce duplicates (IDP-16)
PRINCIPLE-R-03: GD scoring in reports is READ-ONLY from Scoring Engine output —
                this agent never recomputes scores
PRINCIPLE-R-04: No PII in any cross-role report without verified consent record
PRINCIPLE-R-05: All PDFs generated server-side by WeasyPrint — never client-side
PRINCIPLE-R-06: All PDF output is security-encoded (XSS-safe, no JS injection, no formula injection)
PRINCIPLE-R-07: Report files are stored in MinIO — never served directly from API filesystem
PRINCIPLE-R-08: Pre-signed MinIO URLs used for downloads — TTL = 15 minutes (not permanent links)
PRINCIPLE-R-09: ERP metric payloads written to ClickHouse are append-only (no correction records —
                corrections require a new versioned report with audit trail)
PRINCIPLE-R-10: Airflow monthly/weekly DAGs are the sole mechanism for scheduled reports —
                no manual cron, no ad-hoc scripts
PRINCIPLE-R-11: Report generation kickoff acknowledged ≤ 1 second (Commerce domain SLA compliance)
PRINCIPLE-R-12: PDF delivery to user via Notification Service email ≤ 5 minutes of session closure
```

---

## SECTION 4 — GD SCORING DISPLAY RULES (SEALED — NON-NEGOTIABLE)

### 4.1 Score Display Architecture

The GD Orchestrator and Scoring Engine are the **sole source of truth** for scores.
This agent reads scores — it never computes, adjusts, or infers them.

The following display rules are locked for all report templates:

```
DISPLAY-S-01: Overall GD Score displayed as: integer (0–100), score band label,
              score band color token (CRITICAL/LOW/MEDIUM/HIGH/ELITE per normalization agent)

DISPLAY-S-02: Score breakdown displayed as:
              - Turn Completion Rate (%)
              - Speaking Time Utilization (%)
              - Interrupt Rate (events count)
              - Silence Events (count)
              - Network Reliability (drops count)
              - Compliance Score (used all turns, no early exit: binary)

DISPLAY-S-03: Scoring formula displayed in every report (transparency requirement):
              score =
                + (used_all_turns ? 10 : 0)
                + (spoke_full_time ? 10 : 0)
                - (interrupt_attempts × 2)
              [extended weighted formula from Scoring Engine applied on top]
              Formula version tag displayed on report footer.

DISPLAY-S-04: Peer feedback score displayed as: 0–10 average (if ≥ 3 peer ratings exist)
              If < 3 peer ratings: displayed as "Insufficient peer data"

DISPLAY-S-05: Trend display (last 5 GD sessions): score progression line graph.
              If fewer than 5 sessions: display available sessions only,
              annotate "X of 5 sessions available"

DISPLAY-S-06: Score percentile rank within batch displayed (candidate's position
              relative to others in same session batch — anonymized peer comparison)

DISPLAY-S-07: Improvement recommendations: rule-based only (NO AI/LLM narrative).
              Locked recommendation rules:
                - interrupt_rate > 0.3  → "Reduce interruptions: waited-turn discipline"
                - mic_utilization < 0.5 → "Use full speaking window: develop point depth"
                - turns_skipped > 0     → "Complete all turns: preparation and readiness"
                - silence_events > 2    → "Reduce silence: prepare structured arguments"
                - early_exit = 1        → "Complete all sessions: reliability signal"

DISPLAY-S-08: No confidence score, no leadership label, no personality trait displayed.
              Only compliance and participation metrics (system design principle).
```

### 4.2 Report Score Data Source Map (LOCKED)

```
Data Field                  Source                        Table
──────────────────────────────────────────────────────────────────────────
gd_score                    Scoring Engine                gd_sessions (PostgreSQL)
score_band                  Normalization Agent           gd_session_metrics (ClickHouse)
turns_completed             GD Orchestrator               participation_events (PostgreSQL)
turns_skipped               GD Orchestrator               participation_events (PostgreSQL)
turns_total                 GD Orchestrator               gd_sessions (PostgreSQL)
mic_open_seconds            GD Orchestrator               participation_events (PostgreSQL)
token_window_seconds        GD Orchestrator               gd_sessions (PostgreSQL)
interrupt_attempts          GD Orchestrator               participation_events (PostgreSQL)
silence_events              GD Orchestrator               participation_events (PostgreSQL)
network_drops               GD Orchestrator               participation_events (PostgreSQL)
early_exit                  GD Orchestrator               participation_events (PostgreSQL)
peer_feedback_score         Scoring Engine (peer merge)   peer_ratings (PostgreSQL)
batch_rank_position         This agent (computed)         Derived from gd_session_metrics
trend_last_5_sessions       This agent (fetched)          gd_session_metrics (ClickHouse)
```

---

## SECTION 5 — REPORT TYPE SPECIFICATIONS (ALL REQUIRED — FULLY LOCKED)

### 5.1 Per-Session Participant Scorecard (STUDENT view)

```
TRIGGER:        Kafka event: gd.completed
RECIPIENT:      Student (self only — not exposed to recruiter without consent)
FORMAT:         PDF + JSON
IDEMPOTENCY KEY: report:session:{session_id}:participant:{user_id}:student
GENERATION SLA: ≤ 5 minutes after session closure

CONTENT SECTIONS (ALL REQUIRED):
  Header:
    - Platform name, report type: "GD Session Scorecard"
    - Session date, topic (anonymized topic_id if topic is confidential batch)
    - Batch ID (masked: first 8 chars of UUID)
    - Report generated timestamp + formula_version tag

  Section A — Your Performance Summary:
    - Overall GD Score (large focal metric — DISPLAY-S-01)
    - Score band with color indicator
    - Your percentile in this batch (DISPLAY-S-06)
    - Trend graph: last 5 sessions (DISPLAY-S-05)

  Section B — Score Breakdown:
    - Turn Completion Rate: X / Y turns (Z%)
    - Speaking Time Utilization: X of Ys allocated (Z%)
    - Interrupt Attempts: N (deduct N×2 from score)
    - Silence Events: N
    - Network Drops: N
    - Early Exit: Yes/No
    - Peer Feedback Score: X.X / 10 or "Insufficient peer data"

  Section C — Rule-Based Improvement Flags:
    - Up to 3 flags (DISPLAY-S-07 rules applied)
    - Each flag: signal name + plain-English guidance (no AI narrative)

  Section D — Session Context:
    - Round structure: Intro (60s) / Rebuttal (30s) / Conclusion (45s) / Open
    - Total session duration
    - Participants in batch: N (anonymized count only)

  Footer:
    - Scoring formula version
    - "Scores are rule-based and reproducible. No AI inference applied."
    - Download timestamp + MinIO object hash (for integrity verification)

PDF SECURITY RULES (MANDATORY):
  - No JavaScript in PDF
  - No external URL references
  - All candidate name/ID rendered as plain text (escaped via WeasyPrint encoding)
  - No formula-trigger characters in any CSV export variant
  - MinIO object tagged: report_type=STUDENT_SCORECARD, tenant_id, user_id
```

### 5.2 Per-Session Admin Summary (RECRUITER / TPO view)

```
TRIGGER:        Kafka event: gd.completed
RECIPIENT:      Recruiter, TPO, Institute ERP admin (role-gated)
FORMAT:         PDF + JSON
IDEMPOTENCY KEY: report:session:{session_id}:admin:{batch_recruiter_id}
CONSENT GATE:   Candidate behavioral data (scores) require: hiring_visibility_consent = TRUE
                per candidate. Candidates without consent → score row displayed as "[Consent Pending]"

CONTENT SECTIONS (ALL REQUIRED):
  Header:
    - Session metadata: date, topic, batch_id, recruiter/institute name
    - Total participants: N, completed: N, dropped: N, no-show: N

  Section A — Participant Score Table:
    Columns: Candidate ID (UUID masked) | Score | Band | Turns | Mic% | Interrupts | Status
    Rows: One per participant (consent-verified candidates only show score)
    Non-consented rows: show "[Consent Pending]" in score column
    Sorting: Default by score descending

  Section B — Batch Statistics:
    - Avg score, P50 score, P90 score
    - Completion rate (%)
    - No-show count + dropout count
    - Interrupt rate (batch average)
    - Top 3 candidates (UUID masked — no name unless recruiter has profile access)

  Section C — Session Protocol Summary:
    - Round durations used
    - Enforced rule set version
    - Network incident count (batch-level)

  Footer:
    - Consent compliance statement
    - Formula version
    - "No recruiter can override or modify scores. Immutable audit log reference: {audit_log_id}"

ROLE RESTRICTIONS (HARD):
  RECRUITER:        Can see batch aggregate + masked candidate IDs
                    Can see individual scores ONLY if candidate has active hiring_visibility_consent
  TPO:              Same as recruiter for their institute's students
  INSTITUTE_ERP:    Can see all institute students' scores (institutional consent covers)
  PLATFORM_ADMIN:   Full view, all tenants
```

### 5.3 GD Batch Report (INSTITUTE ERP / RECRUITER HR)

```
TRIGGER:        Kafka event: gd.batch.closed (all sessions in batch finalized)
RECIPIENT:      Recruiter, Institute ERP admin, HR Manager, TPO
FORMAT:         PDF + CSV + JSON
IDEMPOTENCY KEY: report:batch:{batch_id}:erp:{tenant_id}
GENERATION SLA: ≤ 10 minutes after gd.batch.closed event

CONTENT SECTIONS (ALL REQUIRED):
  Header:
    - Batch ID, topic category, start/end dates
    - Hiring round context (if linked to job_id: display job title)
    - Total sessions in batch, total unique candidates

  Section A — Batch Completion Dashboard:
    - Sessions: planned / completed / failed
    - Candidates: registered / attended / completed / dropped / no-show
    - Batch completion rate (%)
    - Avg score across all sessions
    - Score distribution table (band counts: CRITICAL/LOW/MEDIUM/HIGH/ELITE)

  Section B — Candidate Ranking Table (consent-filtered):
    Columns: Rank | Candidate ID | Sessions Attended | Avg Score | Best Score | Band | Status
    Note: Only consent-verified candidates ranked; others listed as [Consent Pending]
    Export: This table is the CSV export payload (CSV-injection-safe encoding)

  Section C — Session-by-Session Summary:
    For each session: session_id | date | participants | avg_score | completion_rate

  Section D — Hiring Recommendation Layer (rule-based — NO AI):
    Locked recommendation tiers:
      Score ≥ 80 (ELITE):    "Proceed to next round" flag
      Score 60–79 (HIGH):    "Consider for next round" flag
      Score 40–59 (MEDIUM):  "Reserve / waitlist" flag
      Score < 40:            "Did not meet threshold" flag
    NOTE: These are informational flags only. Hiring decision authority rests with recruiter.
    Label displayed: "System flag — final hiring decision is recruiter's sole authority"

  Section E — Anomaly Flags (from Scoring Engine audit):
    Any session with: score variance anomaly detected / integrity flag / manual override applied
    Displayed with audit_log_id reference for recruiter review

  CSV Export Rules:
    - Delimiter: comma
    - Encoding: UTF-8 with BOM
    - Formula-trigger prefix escaping: any cell starting with =, +, -, @ → prepend single quote
    - No raw UUIDs in first column (prevents Excel formula injection)
    - Headers in plain ASCII

  Footer:
    - Batch audit trail hash (SHA-256 of all session scores in batch — integrity seal)
    - Formula version + normalization version
    - MinIO storage path for compliance retention
```

### 5.4 Monthly Student GD Summary (Auto-generated)

```
TRIGGER:        Airflow DAG: gd_monthly_student_reports (1st of month, 04:00 UTC)
RECIPIENT:      Student (email via Notification Service)
FORMAT:         PDF (email attachment + MinIO download link)
IDEMPOTENCY KEY: report:monthly:{YYYY-MM}:student:{user_id}
MINIMUM DATA:   ≥ 1 GD session in the month (skip + log if zero sessions)

CONTENT SECTIONS (ALL REQUIRED):
  Header:
    - "Your GD Performance Report — {Month} {Year}"
    - Student display name (first name only — full name requires GDPR consent display flag)

  Section A — Month Summary:
    - GD sessions attended (count)
    - Avg score this month
    - Score change vs prior month (delta with arrow: ▲ +X or ▼ -X)
    - Best session score + date
    - Total turns completed this month
    - Total speaking time (minutes)
    - Streak: consecutive months with ≥ 1 GD session

  Section B — Score Progression Chart:
    - Line graph: month-by-month avg score (last 6 months)
    - Band annotations per month

  Section C — Participation Health:
    - Completion rate this month (%)
    - Interrupt events this month
    - Silence events this month
    - No-shows this month (if any: flagged with guidance)

  Section D — Improvement Flags (rule-based — max 3):
    DISPLAY-S-07 rules applied across the month's aggregate

  Section E — Next Month Goals (static, rule-based):
    Based on current score band:
      CRITICAL/LOW:   "Attend at least 2 GD sessions next month"
      MEDIUM:         "Target ≥ 80% turn completion rate"
      HIGH:           "Reduce interrupt rate below 1 per session"
      ELITE:          "Maintain consistency — 3+ sessions next month"

  Footer:
    - "This report was auto-generated on {date}. Scores are rule-based and auditable."
    - Download link: pre-signed MinIO URL (TTL: 7 days for monthly reports)
```

### 5.5 Monthly Parent Progress Report (Consent-Gated)

```
TRIGGER:        Airflow DAG: gd_monthly_parent_reports (1st of month, 05:00 UTC)
RECIPIENT:      Parent (email — ONLY if student has active parent_report_consent = TRUE)
FORMAT:         PDF (email only — no dashboard access for parents)
IDEMPOTENCY KEY: report:monthly:{YYYY-MM}:parent:{student_user_id}
CONSENT GATE:   parent_report_consent = TRUE in user_preferences (mandatory — hard block)
                Feature gating: generate_scores = TRUE (DISPLAY-S-08 requires consent)

CONTENT SECTIONS (ALL REQUIRED):
  Header:
    - "Student Placement Report — {Month} {Year}"
    - Student display name (first name only)
    - Institute name (if institute_id is linked)

  Section A — Placement Journey Tracker:
    - Profile completeness (%)
    - GDs attended this month / this year
    - Interview invitations this month
    - Applications submitted this month
    - Placement Probability (from Scoring Engine + Analytics Service — 0–100%)
    - Probability change vs prior month (delta)

  Section B — GD Performance Summary:
    - Sessions attended this month: N
    - Avg GD score: X / 100 (band label)
    - Sessions completed vs dropped
    - Simple language summary:
        Score ≥ 80: "Performing excellently in group discussions"
        60–79:      "Performing well — consistent participation"
        40–59:      "Moderate performance — room for improvement"
        < 40:       "Below threshold — recommend additional practice"
      NOTE: Plain English band description. No detailed behavioral breakdown exposed to parent.

  Section C — Activity This Month:
    - Total platform activities (GD + Dojo + Applications + Courses)
    - Skill development: courses completed (count)
    - Interview calls received: count

  Section D — Red Flags (system-generated — rule-based):
    Only generated if ANY of these conditions are true:
      - Profile incomplete for > 30 days
      - Zero applications despite ≥ 5 matched jobs available
      - Skipped ≥ 2 scheduled interviews
      - Placement probability declined by > 10% vs prior month
      - No skill development activity for > 30 days
    Each flag: plain-English label + "Encourage student to discuss with TPO"
    NOTE: Red flags are surfaced to parent only — no flag stored on student profile

  Footer:
    - "Report generated with student's consent. Student controls this report."
    - Consent revocation notice: "Student can withdraw consent at any time in Settings"
    - "This report is informational. Placement outcomes depend on many factors."
    - No download link (email-only per parent access policy)
```

### 5.6 Monthly Institute GD Cohort Report (TPO / Institute ERP)

```
TRIGGER:        Airflow DAG: gd_monthly_institute_reports (1st of month, 06:00 UTC)
RECIPIENT:      Institute ERP admin, TPO
FORMAT:         PDF + CSV
IDEMPOTENCY KEY: report:monthly:{YYYY-MM}:institute:{institute_id}
MINIMUM DATA:   ≥ 5 GD sessions involving institute students in the month

CONTENT SECTIONS (ALL REQUIRED):
  Header:
    - Institute name, reporting period
    - Total students enrolled on platform this month
    - Total students active (≥ 1 GD session)

  Section A — Cohort GD Performance Overview:
    - Score distribution: CRITICAL/LOW/MEDIUM/HIGH/ELITE (count + %)
    - Avg cohort GD score this month
    - Score change vs prior month
    - Cohort completion rate (%)
    - Cohort no-show rate (%)

  Section B — Hiring Readiness Segmentation (rule-based):
    Segment students by score band — counts only (no individual names in this section):
      ELITE+HIGH (≥60):  "Hiring-ready cohort: N students"
      MEDIUM (40–59):    "Developing cohort: N students — recommend practice sessions"
      CRITICAL+LOW (<40): "High-support cohort: N students — recommend TPO intervention"

  Section C — Top Performing Students (consent-filtered):
    Top 10 students by avg GD score this month
    Display: masked student ID | avg score | band | sessions attended
    Only students with active hiring_visibility_consent shown by name to TPO
    Others: masked UUID

  Section D — Session Activity Log:
    - Sessions conducted this month (count, by week)
    - Batches completed vs scheduled
    - Recruiter-linked batches (which recruiters ran GD sessions with this institute)

  Section E — Month-on-Month Cohort Trend:
    - 6-month avg score trend (line chart)
    - 6-month completion rate trend

  Section F — Recruiter Demand Signal:
    - Recruiters who ran GD batches with this institute this month
    - Total candidates screened via GD for hiring
    - Top 3 hiring domains this month

  CSV Export (for TPO import into institute LMS/HRIS):
    Columns: student_masked_id | sessions_attended | avg_score | band | completion_rate | hiring_ready_flag
    CSV-injection-safe encoding enforced
```

### 5.7 Monthly Recruiter GD Outcomes Report

```
TRIGGER:        Airflow DAG: gd_monthly_recruiter_reports (1st of month, 06:30 UTC)
RECIPIENT:      Recruiter, HR Manager
FORMAT:         PDF + JSON
IDEMPOTENCY KEY: report:monthly:{YYYY-MM}:recruiter:{recruiter_id}

CONTENT SECTIONS (ALL REQUIRED):
  Section A — GD Activity Summary:
    - Batches run this month: N
    - Total candidates screened via GD: N
    - Sessions completed: N, failed: N, no-shows: N
    - Avg batch completion rate (%)

  Section B — Candidate Pipeline Outcome:
    - ELITE+HIGH candidates (≥60): N — "Proceed eligible"
    - MEDIUM candidates (40–59): N — "Reserve eligible"
    - LOW+CRITICAL candidates (<40): N — "Did not meet threshold"
    - Conversion: GD screened → progressed to interview (%)

  Section C — Batch-by-Batch Summary:
    batch_id | date | topic_category | candidates | avg_score | completion_rate | hired_count

  Section D — Reliability Metrics (feeds Recruiter Reputation Score):
    - Ghost offer rate this month (%)
    - JD accuracy rating (avg from candidates — if ≥ 5 ratings)
    - Response rate to applications (%)
    NOTE: These feed the Recruiter Reputation Score formula — data is read-only here

  Footer:
    - "Hiring flags are informational. All hiring decisions are recruiter's sole authority."
    - Formula version, normalization version
```

### 5.8 Compliance Audit Export (On-Demand — Admin Only)

```
TRIGGER:        API: POST /api/v1/reports/gd/audit-export (PLATFORM_ADMIN only)
RECIPIENT:      Compliance officer, Platform admin
FORMAT:         PDF + CSV (immutable — no post-generation modification)
IDEMPOTENCY KEY: report:audit:{export_id}:{requested_by_admin_id}
RETENTION:      MinIO lifecycle: 7 years (compliance retention)

CONTENT (ALL REQUIRED):
  - Full session log for requested scope (tenant + date range)
  - Per-session: participants, scores, audit_log_id references, formula version
  - Any admin overrides in scope: override_reason, admin_id, old_score, new_score
  - Consent log references per candidate
  - Idempotency key log for all report generations in scope
  - Batch audit hash (SHA-256 integrity seal per batch)
  - Export itself is hashed and hash stored in PostgreSQL (export integrity record)

SECURITY:
  - Export encrypted at rest in MinIO (AES-256)
  - Pre-signed URL TTL: 60 minutes only
  - Access logged: every export download logged with admin_id, timestamp, IP
  - Export is marked COMPLIANCE_EXPORT in MinIO object metadata
```

---

## SECTION 6 — IDEMPOTENCY ENFORCEMENT (IDP-16 — NON-NEGOTIABLE)

### 6.1 Idempotency Key Registry (PostgreSQL — LOCKED)

```sql
CREATE TABLE gd_report_idempotency_keys (
    id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    idempotency_key     TEXT NOT NULL UNIQUE,
    report_type         TEXT NOT NULL,
    tenant_id           UUID NOT NULL,
    entity_id           UUID,                       -- session_id | batch_id | user_id
    status              TEXT NOT NULL DEFAULT 'PENDING', -- PENDING | IN_PROGRESS | COMPLETE | FAILED
    report_id           UUID,                       -- FK to gd_reports once generated
    attempt_count       INT DEFAULT 1,
    failure_reason      TEXT,
    created_at          TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    completed_at        TIMESTAMPTZ
);

CREATE INDEX idx_grip_key ON gd_report_idempotency_keys(idempotency_key);
CREATE INDEX idx_grip_status ON gd_report_idempotency_keys(status);
CREATE INDEX idx_grip_tenant ON gd_report_idempotency_keys(tenant_id);
```

### 6.2 Idempotency Enforcement Protocol (LOCKED)

```
ON REPORT TRIGGER (Kafka event or Airflow DAG task):

  1. Compute idempotency_key (deterministic — per key patterns in Section 5)
  2. Check Redis cache: GET idem:{idempotency_key}
     → Cache HIT + status=COMPLETE → skip generation, return existing report_id
     → Cache HIT + status=IN_PROGRESS → wait (exponential backoff, max 3 checks)
     → Cache HIT + status=FAILED → log + re-queue if attempt_count < 3
     → Cache MISS → proceed

  3. SET Redis: idem:{idempotency_key} = IN_PROGRESS (TTL: 3600s)
  4. INSERT into gd_report_idempotency_keys (status: IN_PROGRESS)
  5. Generate report (Steps 1–12 in architecture)
  6. On success:
     → UPDATE gd_report_idempotency_keys SET status=COMPLETE
     → SET Redis: idem:{idempotency_key} = COMPLETE (TTL: 30 days)
  7. On failure:
     → UPDATE gd_report_idempotency_keys SET status=FAILED, failure_reason
     → SET Redis: idem:{idempotency_key} = FAILED
     → Emit Kafka: gd.report.generation.failed
     → Alert: Prometheus counter gd_report_generation_failure_total++

ADMIN REGENERATION (override — L3 authority only):
  - Requires: POST /api/v1/reports/gd/regenerate
  - Body: { idempotency_key, reason }
  - Auth: PLATFORM_ADMIN JWT (L3 MFA-confirmed session)
  - Action: Marks old key as SUPERSEDED, generates new key with suffix :regen:{timestamp}
  - Old report marked as superseded in gd_reports table
  - Audit record written: admin_id, reason, old_report_id, new_report_id
```

---

## SECTION 7 — DATA MODEL (POSTGRESQL — ALL TABLES REQUIRED)

### 7.1 GD Reports Registry (LOCKED)

```sql
CREATE TABLE gd_reports (
    id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id           UUID NOT NULL,
    report_type         TEXT NOT NULL,
    -- STUDENT_SCORECARD | ADMIN_SESSION_SUMMARY | BATCH_REPORT |
    -- MONTHLY_STUDENT | MONTHLY_PARENT | MONTHLY_INSTITUTE |
    -- MONTHLY_RECRUITER | MONTHLY_PLATFORM | AUDIT_EXPORT |
    -- TPO_COHORT | RECRUITER_HIRING_PACK
    entity_type         TEXT NOT NULL,              -- SESSION | BATCH | STUDENT | INSTITUTE | RECRUITER
    entity_id           UUID NOT NULL,
    reporting_period    TEXT,                       -- YYYY-MM for monthly | NULL for session/batch
    idempotency_key     TEXT NOT NULL,
    idempotency_key_id  UUID REFERENCES gd_report_idempotency_keys(id),

    -- File storage
    minio_bucket        TEXT NOT NULL DEFAULT 'erp-gd-reports',
    minio_object_key    TEXT NOT NULL,              -- path in MinIO
    minio_object_hash   TEXT,                       -- SHA-256 of file (integrity)
    file_format         TEXT NOT NULL,              -- PDF | CSV | JSON
    file_size_bytes     BIGINT,

    -- Report metadata
    formula_version     TEXT NOT NULL,
    normalization_version TEXT NOT NULL,
    consent_snapshot    JSONB,                      -- consent states at generation time
    data_sources        JSONB,                      -- {sessions: [...], clickhouse_tables: [...]}

    -- Status
    status              TEXT NOT NULL DEFAULT 'GENERATED',
    -- GENERATED | DELIVERED | SUPERSEDED | COMPLIANCE_HOLD
    superseded_by       UUID REFERENCES gd_reports(id),
    supersede_reason    TEXT,

    generated_at        TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    delivered_at        TIMESTAMPTZ,
    expires_at          TIMESTAMPTZ                 -- for temporary reports (pre-signed URL expiry)
);

ALTER TABLE gd_reports ENABLE ROW LEVEL SECURITY;
CREATE POLICY tenant_isolation ON gd_reports
    USING (tenant_id = current_setting('app.tenant_id')::UUID);

CREATE INDEX idx_gdreports_entity ON gd_reports(entity_id, report_type);
CREATE INDEX idx_gdreports_tenant ON gd_reports(tenant_id);
CREATE INDEX idx_gdreports_period ON gd_reports(reporting_period, tenant_id);
CREATE INDEX idx_gdreports_idem ON gd_reports(idempotency_key);
```

### 7.2 Report Delivery Log (LOCKED — IMMUTABLE)

```sql
CREATE TABLE gd_report_delivery_log (
    id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    report_id           UUID NOT NULL REFERENCES gd_reports(id),
    tenant_id           UUID NOT NULL,
    delivered_to_user   UUID NOT NULL,
    delivered_to_role   TEXT NOT NULL,
    delivery_channel    TEXT NOT NULL,              -- EMAIL | PUSH | IN_APP | API_DOWNLOAD
    delivery_status     TEXT NOT NULL,              -- SENT | DELIVERED | FAILED | OPENED
    presigned_url_ttl   INT,                        -- seconds, for API_DOWNLOAD
    notification_id     UUID,                       -- FK to Notification Service record
    delivered_at        TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- Immutability enforcement
CREATE RULE no_update_delivery_log AS ON UPDATE TO gd_report_delivery_log DO INSTEAD NOTHING;
CREATE RULE no_delete_delivery_log AS ON DELETE TO gd_report_delivery_log DO INSTEAD NOTHING;

ALTER TABLE gd_report_delivery_log ENABLE ROW LEVEL SECURITY;
CREATE POLICY tenant_isolation ON gd_report_delivery_log
    USING (tenant_id = current_setting('app.tenant_id')::UUID);
```

### 7.3 Report Consent Snapshot Table (LOCKED)

```sql
-- Consent states are snapshotted at generation time — consent can change later
-- but the report reflects consent at the moment of generation
CREATE TABLE gd_report_consent_snapshots (
    id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    report_id           UUID NOT NULL REFERENCES gd_reports(id),
    user_id             UUID NOT NULL,
    tenant_id           UUID NOT NULL,
    hiring_visibility_consent   BOOLEAN NOT NULL,
    parent_report_consent       BOOLEAN NOT NULL,
    ai_analysis_consent         BOOLEAN NOT NULL,
    generate_scores_consent     BOOLEAN NOT NULL,
    consent_version             TEXT NOT NULL,
    snapshotted_at              TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_gdrcs_report ON gd_report_consent_snapshots(report_id);
```

### 7.4 ERP GD Metrics Payload Table (ClickHouse — LOCKED)

```sql
-- Structured ERP metric payload emitted by this agent after every report generation
CREATE TABLE erp_gd_report_metrics (
    metric_id               UUID DEFAULT generateUUIDv4(),
    tenant_id               UUID,
    report_type             LowCardinality(String),
    entity_type             LowCardinality(String),
    entity_id               UUID,
    reporting_period        LowCardinality(String),

    -- Aggregated GD metrics from the report
    sessions_total          UInt32,
    sessions_completed      UInt32,
    sessions_dropped        UInt32,
    no_show_count           UInt32,
    total_candidates        UInt32,
    completion_rate         Float32,
    avg_gd_score            Float32,
    p50_gd_score            Float32,
    p90_gd_score            Float32,
    elite_count             UInt32,
    high_count              UInt32,
    medium_count            UInt32,
    low_count               UInt32,
    critical_count          UInt32,
    hiring_ready_count      UInt32,         -- ELITE + HIGH
    avg_mic_utilization     Float32,
    avg_interrupt_rate      Float32,

    -- Report metadata
    formula_version         LowCardinality(String),
    report_id               UUID,
    generated_at            DateTime64(3),
    report_date             Date MATERIALIZED toDate(generated_at)
)
ENGINE = MergeTree()
PARTITION BY (tenant_id, toYYYYMM(report_date))
ORDER BY (tenant_id, report_type, entity_type, entity_id, report_date)
TTL report_date + INTERVAL 3 YEAR DELETE
SETTINGS index_granularity = 8192;
```

---

## SECTION 8 — AIRFLOW DAG SPECIFICATIONS (ALL REQUIRED)

### 8.1 DAG: `gd_monthly_student_reports`

```python
dag_id          = 'gd_monthly_student_reports'
schedule        = '0 4 1 * *'          # 04:00 UTC on 1st of each month
catchup         = False
max_active_runs = 1

Task 1: query_active_students_with_gd_activity    # PostgreSQL: users with ≥1 GD session prior month
Task 2: check_idempotency_keys_bulk               # Redis: skip already-generated reports
Task 3: fetch_student_gd_metrics_batch            # ClickHouse: gd_session_metrics for prior month
Task 4: generate_student_scorecards_parallel      # WeasyPrint PDF generation (parallel workers)
Task 5: upload_pdfs_to_minio_bulk                 # MinIO: erp-gd-reports bucket
Task 6: write_gd_reports_registry_bulk            # PostgreSQL: gd_reports table
Task 7: trigger_notification_service_bulk         # Kafka: gd.report.generated (per student)
Task 8: write_erp_metrics_clickhouse              # ClickHouse: erp_gd_report_metrics
Task 9: mark_idempotency_keys_complete            # Redis + PostgreSQL
Task 10: audit_write                              # normalization_audit_log
```

### 8.2 DAG: `gd_monthly_parent_reports`

```python
dag_id          = 'gd_monthly_parent_reports'
schedule        = '0 5 1 * *'          # 05:00 UTC
catchup         = False
max_active_runs = 1

Task 1: query_students_with_parent_consent        # PostgreSQL: parent_report_consent = TRUE only
Task 2: check_idempotency_keys_bulk
Task 3: fetch_student_monthly_metrics             # ClickHouse + PostgreSQL
Task 4: verify_consent_current_status             # Re-verify consent has not been revoked since month start
Task 5: generate_parent_pdfs_parallel             # WeasyPrint (parent template — no detailed breakdown)
Task 6: upload_pdfs_minio
Task 7: write_gd_reports_registry
Task 8: trigger_notification_email_only           # Parent receives email-only (no dashboard link)
Task 9: mark_idempotency_complete
```

### 8.3 DAG: `gd_monthly_institute_reports`

```python
dag_id          = 'gd_monthly_institute_reports'
schedule        = '0 6 1 * *'          # 06:00 UTC
catchup         = False
max_active_runs = 1

Task 1: query_active_institutes                   # PostgreSQL: institutes with ≥5 GD sessions prior month
Task 2: check_idempotency_keys_bulk
Task 3: fetch_cohort_metrics_per_institute        # ClickHouse: gd_session_metrics aggregated
Task 4: compute_cohort_segmentation               # Rule-based hiring readiness bands
Task 5: fetch_tpo_ranking_data                    # erp_daily_snapshots from normalization agent
Task 6: generate_institute_pdfs_and_csvs          # WeasyPrint PDF + CSV generation
Task 7: upload_to_minio
Task 8: write_gd_reports_registry
Task 9: trigger_notifications_tpo_erp             # Email to TPO + in-app notification
Task 10: write_erp_metrics_clickhouse
Task 11: mark_idempotency_complete
```

### 8.4 DAG: `gd_weekly_tpo_cohort_readiness`

```python
dag_id          = 'gd_weekly_tpo_cohort_readiness'
schedule        = '0 7 * * 1'          # 07:00 UTC every Monday
catchup         = False
max_active_runs = 1

Task 1: query_institutes_with_active_tpo
Task 2: fetch_7day_gd_activity_per_institute      # ClickHouse: last 7 days
Task 3: compute_hiring_readiness_trend            # Week-on-week cohort score delta
Task 4: generate_tpo_readiness_json               # JSON only (no PDF for weekly)
Task 5: push_to_erp_dashboard_api                 # Direct ClickHouse write for live dashboard
Task 6: trigger_tpo_in_app_notification           # Notification Service: in-app only (no email)
```

---

## SECTION 9 — KAFKA EVENT TRIGGERS (ALL REQUIRED)

### 9.1 Event Consumption Map (LOCKED)

```
Kafka Topic                     Action
────────────────────────────────────────────────────────────────────────────
gd.completed                    → Trigger: Per-Session Participant Scorecard (5.1)
                                → Trigger: Per-Session Admin Summary (5.2)
                                → Check: Is this the last session in batch?
                                  If YES → emit: gd.batch.closed internally

gd.batch.closed                 → Trigger: GD Batch Report (5.3)
                                → Trigger: Recruiter Hiring Decision Pack (JSON)

reputation.signal.applied       → Read-only: update student score trend data
                                  (no report generated — feeds trend chart in next scorecard)

user.consent.updated            → If consent revoked:
                                  Mark future parent reports as blocked for this student
                                  Do NOT retroactively modify historical reports (consent is non-retroactive)

invoice.generated               → No report generated — billing reports handled by Billing Agent
```

### 9.2 Kafka Events Emitted by This Agent (ALL REQUIRED)

```
Event Topic                             Payload
────────────────────────────────────────────────────────────────────────────
gd.report.generated                     report_id, report_type, entity_id, tenant_id, ts
gd.report.delivered                     report_id, delivered_to_user, channel, ts
gd.report.generation.failed             report_type, entity_id, reason, attempt_count, ts
gd.report.batch.completed               batch_id, tenant_id, report_count, ts
gd.report.consent.blocked               entity_id, report_type, user_id, ts
gd.report.superseded                    old_report_id, new_report_id, admin_id, reason, ts
```

---

## SECTION 10 — PDF SECURITY RULES (MANDATORY — NO EXCEPTIONS)

### 10.1 WeasyPrint Output Security (ALL REQUIRED — STOP EXECUTION IF ABSENT)

```
PDF-S-01: NO JavaScript allowed in any PDF output
          WeasyPrint config: WEASYPRINT_QUIET=1, no JS engine loaded

PDF-S-02: NO external URL references in PDF content
          All images: base64-embedded in HTML template before WeasyPrint render
          No <img src="https://..."> — only <img src="data:image/...;base64,...">

PDF-S-03: ALL candidate data rendered through Jinja2 auto-escaping
          Template config: Environment(autoescape=True) — mandatory on all templates
          No raw {{ variable }} without |e filter explicitly confirmed

PDF-S-04: CSV injection prevention (for all CSV exports):
          Any cell value starting with: = + - @ TAB CR
          → Prepend with single-quote character (')
          → Applied via central csv_safe_cell() utility (no ad-hoc escaping in business logic)

PDF-S-05: No score data editable in PDF (all score fields rendered as static text, not form fields)

PDF-S-06: PDF metadata sanitization:
          Title: report_type + report_id (no PII)
          Author: "Ecoskiller Platform"
          Creator: "Ecoskiller WeasyPrint Engine v{version}"
          Subject: tenant_id + period (no candidate names)

PDF-S-07: MinIO object tags (ALL required on every upload):
          report_type = {report_type}
          tenant_id   = {tenant_id}
          formula_version = {version}
          generated_at    = {ISO timestamp}
          security_encoded = TRUE

PDF-S-08: Pre-signed URL policy:
          Session scorecard downloads: TTL = 15 minutes
          Monthly report downloads: TTL = 7 days
          Compliance audit exports: TTL = 60 minutes
          No permanent public URLs for any report
```

---

## SECTION 11 — REPORT API SURFACE (ALL REQUIRED)

### 11.1 Report Download APIs

```
GET  /api/v1/reports/gd/session/{session_id}/scorecard
     Auth: JWT (STUDENT — own session only | RECRUITER/TPO — consent-gated)
     Returns: pre-signed MinIO URL (TTL 15 min) or 403 if consent not met

GET  /api/v1/reports/gd/session/{session_id}/admin-summary
     Auth: JWT (RECRUITER | TPO | INSTITUTE_ERP | PLATFORM_ADMIN)
     Returns: pre-signed MinIO URL (PDF) + JSON payload

GET  /api/v1/reports/gd/batch/{batch_id}
     Auth: JWT (RECRUITER | INSTITUTE_ERP | HR_MANAGER | PLATFORM_ADMIN)
     Query: ?format=pdf|csv|json
     Returns: pre-signed MinIO URL for requested format

GET  /api/v1/reports/gd/monthly/student/{user_id}
     Auth: JWT (STUDENT — own only | PLATFORM_ADMIN)
     Query: ?period=YYYY-MM
     Returns: pre-signed MinIO URL (TTL 7 days)

GET  /api/v1/reports/gd/monthly/institute/{institute_id}
     Auth: JWT (INSTITUTE_ERP | TPO | PLATFORM_ADMIN)
     Query: ?period=YYYY-MM&format=pdf|csv
     Returns: pre-signed MinIO URL

GET  /api/v1/reports/gd/monthly/recruiter/{recruiter_id}
     Auth: JWT (RECRUITER — own only | PLATFORM_ADMIN)
     Query: ?period=YYYY-MM
     Returns: pre-signed MinIO URL

GET  /api/v1/reports/gd/tpo/cohort/{institute_id}
     Auth: JWT (TPO | INSTITUTE_ERP | PLATFORM_ADMIN)
     Query: ?from=date&to=date
     Returns: JSON cohort readiness payload

POST /api/v1/reports/gd/audit-export
     Auth: JWT (PLATFORM_ADMIN — L3 MFA confirmed)
     Body: { tenant_id, date_from, date_to, scope }
     Returns: export_id + pre-signed URL (TTL 60 min)

POST /api/v1/reports/gd/regenerate
     Auth: JWT (PLATFORM_ADMIN — L3 MFA confirmed)
     Body: { idempotency_key, reason }
     Returns: { new_report_id, queued: true }

GET  /api/v1/reports/gd/erp/dashboard
     Auth: JWT (INSTITUTE_ERP | RECRUITER | PLATFORM_ADMIN | GRAFANA_DATASOURCE)
     Query: ?tenant_id=&from=&to=&report_type=&entity_id=
     Returns: erp_gd_report_metrics JSON (ClickHouse-backed)
```

---

## SECTION 12 — MINIO BUCKET STRUCTURE (LOCKED)

```
Bucket: erp-gd-reports

Directory Structure (LOCKED):
  /student-scorecards/{tenant_id}/{YYYY}/{MM}/{session_id}_{user_id}.pdf
  /admin-summaries/{tenant_id}/{YYYY}/{MM}/{session_id}_admin.pdf
  /batch-reports/{tenant_id}/{YYYY}/{MM}/{batch_id}.pdf
  /batch-reports/{tenant_id}/{YYYY}/{MM}/{batch_id}.csv
  /monthly-student/{tenant_id}/{YYYY}/{MM}/{user_id}_student_report.pdf
  /monthly-parent/{tenant_id}/{YYYY}/{MM}/{user_id}_parent_report.pdf
  /monthly-institute/{tenant_id}/{YYYY}/{MM}/{institute_id}_cohort.pdf
  /monthly-institute/{tenant_id}/{YYYY}/{MM}/{institute_id}_cohort.csv
  /monthly-recruiter/{tenant_id}/{YYYY}/{MM}/{recruiter_id}_outcomes.pdf
  /audit-exports/{tenant_id}/{YYYY}/{MM}/{export_id}_audit.pdf
  /audit-exports/{tenant_id}/{YYYY}/{MM}/{export_id}_audit.csv

Lifecycle Rules (LOCKED):
  student-scorecards:    Retain 2 years → expire
  admin-summaries:       Retain 2 years → expire
  batch-reports:         Retain 3 years → expire
  monthly-student:       Retain 2 years → expire
  monthly-parent:        Retain 1 year → expire (parent reports ephemeral)
  monthly-institute:     Retain 3 years → expire
  monthly-recruiter:     Retain 3 years → expire
  audit-exports:         Retain 7 years (compliance — no auto-expire)

Encryption:
  All objects: AES-256 server-side encryption (MinIO SSE)
  Audit exports: Additional client-side encryption key (stored in Vault)
```

---

## SECTION 13 — OBSERVABILITY (NON-OPTIONAL)

### 13.1 Prometheus Metrics (ALL REQUIRED)

```
gd_report_generation_total{tenant, report_type, status}
gd_report_generation_duration_seconds{report_type}
gd_report_pdf_size_bytes{report_type}
gd_report_idempotency_skip_total{report_type}          -- deduplicated (already generated)
gd_report_idempotency_collision_total{report_type}     -- IN_PROGRESS collision
gd_report_generation_failure_total{report_type, reason}
gd_report_delivery_total{report_type, channel, status}
gd_report_minio_upload_duration_seconds{report_type}
gd_report_consent_blocked_total{report_type}           -- reports blocked by missing consent
gd_report_presigned_url_issued_total{report_type, role}
gd_airflow_dag_run_duration_seconds{dag_id}
gd_airflow_dag_failure_total{dag_id}
```

### 13.2 Grafana Dashboards (ALL REQUIRED)

```
Dashboard 1: Report Generation Health
  - Reports generated per hour (by type)
  - Generation duration P50/P90 (target: PDF ≤ 30s per report)
  - Failure rate (target: < 0.1%)
  - Idempotency skip rate (normal signal — healthy deduplication)
  - Consent block rate (trend — rising = consent policy issue)

Dashboard 2: Airflow DAG Health
  - Monthly DAG run status (all 4 DAGs: green/red)
  - DAG execution duration
  - Task failure breakdown
  - Student reports generated vs expected count

Dashboard 3: MinIO Storage Health
  - Bucket size growth per month (by report type)
  - Upload success rate
  - Pre-signed URL issuance rate (by role)

Dashboard 4: ERP Report Delivery
  - Email delivery success rate (via Notification Service)
  - Download (API) request rate (by report_type, role)
  - Report access by tenant (ERP executive usage signal)
```

### 13.3 Alert Rules (LOCKED)

```
ALERT: gd_report_generation_failure_total > 5 in 10min  → SEVERITY: HIGH
ALERT: gd_airflow_dag_failure_total (any monthly DAG)    → SEVERITY: CRITICAL
ALERT: gd_report_delivery email failure rate > 5%        → SEVERITY: HIGH
ALERT: consent_blocked_total spikes > 50/hour            → SEVERITY: MEDIUM (consent policy review needed)
ALERT: minio_upload_duration > 60s                       → SEVERITY: MEDIUM
ALERT: generation_duration P90 > 120s                    → SEVERITY: MEDIUM (WeasyPrint performance)
ALERT: monthly DAG not started by 07:00 UTC on 1st       → SEVERITY: CRITICAL
```

---

## SECTION 14 — SECURITY & COMPLIANCE (NON-OPTIONAL)

```
SEC-01: JWT authentication required on all API endpoints — no anonymous access
SEC-02: RBAC enforced via Open Policy Agent — no hardcoded role checks in business logic
SEC-03: Tenant isolation enforced: gd_reports RLS active, all queries include tenant_id
SEC-04: Pre-signed MinIO URLs only — no permanent object URLs served
SEC-05: Consent verification is blocking (hard gate) for parent reports and cross-role
        behavioral data — missing consent = 403 (not 404) with reason in body
SEC-06: PDF output XSS-safe: Jinja2 autoescape=True, WeasyPrint no-JS mode
SEC-07: CSV export injection-safe: central csv_safe_cell() utility (CI-enforced lint rule)
SEC-08: Compliance audit exports encrypted at rest with Vault-managed key
        (path: secret/ecoskiller/audit-export-key)
SEC-09: All delivery log records immutable (PostgreSQL RULE: no UPDATE, no DELETE)
SEC-10: MinIO credentials stored in Vault (path: secret/ecoskiller/minio-credentials)
SEC-11: Report regeneration (admin override) requires L3 JWT role + MFA-confirmed session
SEC-12: Wazuh alert rule: abnormal pre-signed URL issuance rate (> 1000/hour per tenant)
SEC-13: GDPR: student_id is UUID — name appears in report only if user display consent active
        On right-to-forget: report files retained but user_id → anonymized UUID in registry
        (MinIO objects not deleted — user_id mapping broken in PostgreSQL)
SEC-14: Consent snapshot captured at generation time — stored immutably in
        gd_report_consent_snapshots. Retroactive consent revocation does NOT alter
        historical reports (consent is non-retroactive per system consent law)
```

---

## SECTION 15 — INTERN EXECUTION CHECKLIST (REQUIRED — ALL ITEMS MANDATORY)

```
☐ 1.  PostgreSQL tables created: gd_reports, gd_report_idempotency_keys,
       gd_report_delivery_log (with immutability RULE), gd_report_consent_snapshots
☐ 2.  ClickHouse table created: erp_gd_report_metrics (MergeTree, correct partition, TTL)
☐ 3.  MinIO bucket created: erp-gd-reports with all lifecycle rules configured
☐ 4.  MinIO SSE encryption enabled on bucket (AES-256)
☐ 5.  WeasyPrint installed in analytics namespace — no JS engine, autoescape confirmed
☐ 6.  All 8 Jinja2 report templates created (one per report type in Section 5)
       Each template tested with: valid data, missing consent, empty session set
☐ 7.  Jinja2 autoescape=True enforced — CI lint rule added to block autoescape=False
☐ 8.  csv_safe_cell() utility created — formula-trigger prefix escaping tested
       Test cases: cell starting with =, +, -, @, TAB, CR — all must prepend '
☐ 9.  Idempotency key logic tested: same key submitted twice → only one PDF generated
☐ 10. Consent gate tested: parent report attempt without consent → 403, report not generated
☐ 11. All 4 Airflow DAGs registered in analytics namespace, tested on staging
       Monthly DAG verified with 30 days of synthetic GD session data
☐ 12. Kafka consumer registered for all 4 topics in Section 9.1
☐ 13. All 7 Kafka events emitted by this agent tested (Section 9.2)
☐ 14. RabbitMQ background workers configured for async report generation
       Worker queue: gd-report-generation
☐ 15. All 9 MinIO object directory paths created with correct naming convention
☐ 16. Pre-signed URL TTLs confirmed: 15min (scorecard), 7d (monthly), 60min (audit)
☐ 17. Report regeneration endpoint tested: old report marked SUPERSEDED, audit log written
☐ 18. PDF security rules tested (PDF-S-01 through PDF-S-08):
       - PDF with <script> tag in template → test fails
       - PDF with external img src → test fails
       - PDF with unescaped variable → test fails (autoescape catches)
☐ 19. All 12 Prometheus metrics registered and scraped
☐ 20. All 4 Grafana dashboards created and tested with live data
☐ 21. All 7 alert rules configured in Alertmanager with correct routing
☐ 22. Compliance audit export: encrypted file verified in MinIO,
       SHA-256 hash stored in PostgreSQL export integrity record
☐ 23. RLS policy tested: query from tenant_A cannot see tenant_B reports
☐ 24. GDPR UUID rotation tested: rotate user_id → report registry entry no longer
       linkable to original user
☐ 25. Flyway migration files created for all PostgreSQL tables (versioned)
☐ 26. OpenAPI 3.1 spec generated for all 9 API endpoints — contract validator passes
☐ 27. Vault secret paths confirmed: minio-credentials, audit-export-key
☐ 28. Report kickoff SLA confirmed: trigger → RabbitMQ enqueue ≤ 1 second
☐ 29. PDF generation SLA confirmed: WeasyPrint render ≤ 30s per report (load tested)
☐ 30. Monthly DAG alert tested: if not started by 07:00 UTC on 1st → CRITICAL alert fires

Absence of any checkbox → STOP EXECUTION → REPORT ERP-GD-REPORT-CHECKLIST-INCOMPLETE
```

---

## SECTION 16 — ANTIGRAVITY RUN COMMAND (REQUIRED)

```
ANTIGRAVITY_AGENT_SPEC      = ERP_GD_REPORT_INTEGRATION_AGENT
ANTIGRAVITY_VERSION         = v1.0.0
ANTIGRAVITY_MODE            = PRODUCTION
ANTIGRAVITY_STACK           = Python 3.11 | FastAPI | WeasyPrint | Jinja2 |
                              Apache Airflow | Kafka | RabbitMQ | PostgreSQL |
                              ClickHouse | MinIO | Redis | Keycloak |
                              HashiCorp Vault | Prometheus | Grafana |
                              Kubernetes (analytics namespace)

ANTIGRAVITY_OUTPUTS_REQUIRED =
  [1]  Kafka consumer service (4 topics)
  [2]  RabbitMQ background worker (gd-report-generation queue)
  [3]  Report trigger router (event → report type → idempotency check)
  [4]  Idempotency enforcement engine (Redis + PostgreSQL — IDP-16 compliant)
  [5]  All 8 Jinja2 HTML report templates (one per report type)
  [6]  WeasyPrint PDF generation pipeline (server-side, no JS, autoescape)
  [7]  csv_safe_cell() utility (central, CI-enforced)
  [8]  MinIO upload service (with object tagging and SSE)
  [9]  Pre-signed URL generation service (TTL-controlled per report type)
  [10] 4 Airflow DAGs (monthly×3 + weekly TPO)
  [11] All 4 PostgreSQL tables (with RLS, indexes, immutability rules where required)
  [12] ClickHouse table: erp_gd_report_metrics
  [13] FastAPI report API — all 9 endpoints (with tenant isolation middleware)
  [14] Consent gate middleware (blocking gate for parent + cross-role reports)
  [15] All 12 Prometheus metrics
  [16] All 4 Grafana dashboards
  [17] All 7 Alertmanager alert rules
  [18] Helm chart (analytics namespace)
  [19] Flyway migration files (all PostgreSQL tables)
  [20] Intern-executable deployment checklist (30 items)

ANTIGRAVITY_FORBIDDEN =
  ❌ AI/LLM narrative generation in any report section (R28 — rule-based only)
  ❌ Confidence, personality, or leadership scoring in any report
  ❌ Cross-tenant report access without PLATFORM_ADMIN role
  ❌ PII exposure in cross-role reports without verified consent record
  ❌ Client-side PDF generation (WeasyPrint server-side only)
  ❌ Permanent MinIO URLs (pre-signed TTL-controlled only)
  ❌ Parent report generation without student consent verification
  ❌ Score recomputation in this agent (read Scoring Engine output only)
  ❌ CSV injection (missing csv_safe_cell() utility → STOP EXECUTION)
  ❌ Duplicate report generation (idempotency violation = IDP-16 hard failure)
  ❌ Manual cron for scheduled reports (Airflow DAGs only)
  ❌ Retroactive consent application to historical reports

READY_FOR = ANTIGRAVITY_PRODUCTION
✔ ANTIGRAVITY_SAFE
✔ ANTIGRAVITY COMPATIBLE
```

---

```
═══════════════════════════════════════════════════════════════════════════════════
AGENT SEAL: ERP_GD_REPORT_INTEGRATION_AGENT v1.0.0
STATUS:     FINAL · SEALED · LOCKED · GOVERNED · DETERMINISTIC
PLATFORM:   ECOSKILLER
ENGINE:     ANTIGRAVITY
MUTATION:   ADD-ONLY VIA VERSION BUMP
AUTHORITY:  HUMAN DECLARATION ONLY
RULE:       IDENTICAL SESSION DATA → IDENTICAL REPORT OUTPUT — ALWAYS
IDEMPOTENCY: REPORT GENERATION EXECUTES EXACTLY ONCE PER SESSION/BATCH/PERIOD
═══════════════════════════════════════════════════════════════════════════════════
```
