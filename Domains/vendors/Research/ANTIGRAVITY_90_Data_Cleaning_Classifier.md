# 🔒 ANTIGRAVITY — ARTIFACT #90
## DATA CLEANING CLASSIFIER
### Layer: ENTERPRISE OPTIMIZATION + TRUST INFRASTRUCTURE
### Artifact Class: Production Execution Contract — Sealed & Locked
### Mutation Policy: Add-Only via Version Bump
### Execution Mode: Deterministic · Event-Driven · Audit-Enforced
### Interpretation Authority: NONE
### Version: v1.0 — LOCKED

---

> **SEAL DECLARATION**
> This document is a locked production artifact for the ANTIGRAVITY system — the unified intelligence layer governing ECOSKILLER + DOJO SaaS. The Data Cleaning Classifier (DCC) is the authoritative specification for all automated data quality detection, classification, remediation, quarantine, and trust-gate enforcement operations across every data ingestion pathway — migration, integration sync, API input, bulk upload, user registration, and internal pipeline events. No cleaning rule, classification label, severity threshold, remediation strategy, or quarantine policy defined herein may be altered without a formal version bump and Governance Board approval.
> Absence of any declared component → STOP EXECUTION.

---

## SECTION DCC-0 — SYSTEM CONTEXT BINDING

| Dimension | Value |
|---|---|
| Platform | ANTIGRAVITY (ECOSKILLER + DOJO SaaS Unified) |
| Layer | Enterprise Optimization + Trust Infrastructure |
| Artifact Number | 90 |
| Component Name | Data Cleaning Classifier (DCC) |
| Predecessor Artifact | #89 — AI Field Mapping Model (AIFMM) |
| Parent Systems | EUME (Migration Engine) · EIE (Integration Engine) · Scoring Engine · Trust Engine · Billing Engine |
| Execution Stack | Python 3.11 · FastAPI · PostgreSQL 15 · Redis 7 · Redis Streams |
| Trigger Surfaces | Migration import · Integration sync · API write · Bulk upload · Match score event · Billing event · User registration |
| Data Scope | All canonical entities: User · Skill · Score · Match · Certification · WorkDataRecord · BillingEvent · MigrationRecord |
| Auth Binding | JWT + tenant context required on all DCC job invocations |
| Governance Gate | All classifier rule changes require Governance Board approval + version bump |
| Upstream Dependency | AIFMM must complete before DCC runs on migrated data |
| Downstream Dependency | DCC must complete before Import Executor (L8 AIFMM) writes to canonical store |

---

## SECTION DCC-1 — MISSION DECLARATION

The Data Cleaning Classifier is the quality firewall between raw external data and the Antigravity canonical data store. Its mission is threefold:

**Mission 1 — DETECT:** Automatically identify every category of data quality defect across all ingestion pathways — structural defects, semantic defects, trust-breaking defects, and platform-integrity defects.

**Mission 2 — CLASSIFY:** Label every detected defect with a severity class, a remediation strategy, and a trust impact rating. Classification is deterministic: identical input always produces identical classification output.

**Mission 3 — REMEDIATE OR QUARANTINE:** For auto-remediable defects, apply the locked remediation rule and proceed. For non-remediable defects, quarantine the record, log the decision, and route to the correct resolution pathway. No defect may pass silently into the canonical store.

---

## SECTION DCC-2 — DEFECT TAXONOMY (MASTER CLASSIFICATION TREE)

All defects are classified under one of six root categories. The taxonomy is locked. New defect types are added via version bump only.

```
DCC DEFECT TAXONOMY v1.0
│
├── CAT-1 · STRUCTURAL DEFECTS
│   ├── STR-01 · Missing Required Field
│   ├── STR-02 · Null Injection in Non-Nullable Field
│   ├── STR-03 · Type Mismatch
│   ├── STR-04 · Oversized Field Value
│   ├── STR-05 · Malformed JSON / Array Structure
│   ├── STR-06 · Invalid Enum Value
│   ├── STR-07 · Orphaned Foreign Key Reference
│   └── STR-08 · Schema Version Mismatch
│
├── CAT-2 · IDENTITY & FORMAT DEFECTS
│   ├── IDN-01 · Invalid Email Format
│   ├── IDN-02 · Invalid Phone Format
│   ├── IDN-03 · Malformed UUID
│   ├── IDN-04 · Invalid Date / Timestamp Format
│   ├── IDN-05 · Invalid URL Format
│   ├── IDN-06 · Encoding Error (non-UTF-8)
│   ├── IDN-07 · Placeholder / Test Data Detected
│   └── IDN-08 · Reserved Word Collision
│
├── CAT-3 · DUPLICATION DEFECTS
│   ├── DUP-01 · Exact Duplicate Record
│   ├── DUP-02 · Near-Duplicate Record (Fuzzy Match)
│   ├── DUP-03 · Cross-Source Duplicate (Same Entity, Different System)
│   ├── DUP-04 · Duplicate Email Within Tenant
│   ├── DUP-05 · Duplicate Source Record ID Re-import
│   └── DUP-06 · Duplicate Skill Assignment (Same User, Same Skill)
│
├── CAT-4 · SEMANTIC & CONTENT DEFECTS
│   ├── SEM-01 · Empty String Masquerading as Value
│   ├── SEM-02 · Whitespace-Only Field Value
│   ├── SEM-03 · HTML / Script Injection in Text Field
│   ├── SEM-04 · SQL Injection Pattern in Text Field
│   ├── SEM-05 · Contradictory Field Pair (Logical Inconsistency)
│   ├── SEM-06 · Future Date in Historical Field
│   ├── SEM-07 · Negative Value in Non-Negative Field
│   ├── SEM-08 · Out-of-Range Score Value
│   ├── SEM-09 · Junk / Gibberish Text Detection
│   └── SEM-10 · Language Mismatch (Field expects locale X, received Y)
│
├── CAT-5 · TRUST & INTEGRITY DEFECTS
│   ├── TRU-01 · Unverified Email Domain for Institution Claim
│   ├── TRU-02 · Skill Confidence Below Trust Threshold
│   ├── TRU-03 · Score Anomaly (Statistical Outlier)
│   ├── TRU-04 · Match Score Variance Breach
│   ├── TRU-05 · Certification Without Required Match Count
│   ├── TRU-06 · Belt Promotion Without Mentor Certification
│   ├── TRU-07 · Forged Source Record ID
│   ├── TRU-08 · Replay Evidence Missing for Certification Record
│   ├── TRU-09 · Cross-Tenant Data Contamination
│   └── TRU-10 · PII in Non-PII Field
│
└── CAT-6 · ECONOMIC & ABUSE DEFECTS
    ├── ECO-01 · Refund Request Pattern Anomaly
    ├── ECO-02 · Multi-Account Fingerprint Match
    ├── ECO-03 · Billing Event Without Feature Access Record
    ├── ECO-04 · Match Farming Pattern (Rapid Same-Pair Matches)
    ├── ECO-05 · Fake Tournament Loop Signal
    ├── ECO-06 · Mentor Collusion Billing Signal
    ├── ECO-07 · Duplicate Payment Event
    └── ECO-08 · Negative Balance / Impossible Wallet State
```

---

## SECTION DCC-3 — SEVERITY CLASSIFICATION MODEL

Every detected defect is assigned one of four severity levels. Severity determines remediation pathway. Severity assignments are locked.

| Severity Level | Code | Definition | Import Behaviour |
|---|---|---|---|
| CRITICAL | SEV-1 | Data integrity, trust, or security breach. Cannot be auto-remediated. Threatens canonical store correctness. | **BLOCK** — record quarantined, import halted for this record, alert fired |
| HIGH | SEV-2 | Significant quality defect that corrupts downstream analytics, scoring, or certification logic. May be auto-remediable. | **BLOCK or AUTO-REMEDIATE** depending on rule; human review queue if not auto-remediable |
| MEDIUM | SEV-3 | Quality defect that degrades user experience or data completeness but does not corrupt trust gates. Auto-remediable in most cases. | **AUTO-REMEDIATE** with log entry; proceed if remediation succeeds |
| LOW | SEV-4 | Minor formatting or cosmetic issue. No impact on trust, scoring, or certification. Always auto-remediable. | **AUTO-REMEDIATE silently** with log entry |

---

## SECTION DCC-4 — FULL DEFECT CLASSIFICATION REGISTRY

### CAT-1 · STRUCTURAL DEFECTS

| Defect ID | Defect Name | Severity | Detection Method | Remediation Strategy | Trust Impact |
|---|---|---|---|---|---|
| STR-01 | Missing Required Field | SEV-1 | NOT NULL constraint check on canonical schema | **BLOCK** — cannot import without required field | HIGH — blocks certification and scoring gates |
| STR-02 | Null Injection in Non-Nullable Field | SEV-1 | Explicit null check post-AIFMM mapping | **BLOCK** — quarantine record | HIGH |
| STR-03 | Type Mismatch | SEV-2 | Compare inferred type vs canonical type | **AUTO-REMEDIATE** via Type Resolution Engine (AIFMM L4); if coercion fails → BLOCK | MEDIUM |
| STR-04 | Oversized Field Value | SEV-3 | Length check against canonical field max | **AUTO-REMEDIATE** — truncate to max with `…` suffix; log truncation event | LOW |
| STR-05 | Malformed JSON / Array Structure | SEV-2 | JSON parse attempt; array bracket check | **AUTO-REMEDIATE** if parseable; **BLOCK** if unparseable | MEDIUM |
| STR-06 | Invalid Enum Value | SEV-2 | Match against locked Enum Dictionary (AIFMM Section 6.4) | **AUTO-REMEDIATE** via enum fuzzy match if confidence ≥ 0.85; else **BLOCK** | MEDIUM |
| STR-07 | Orphaned Foreign Key Reference | SEV-1 | FK existence check against canonical tables | **BLOCK** — cannot create relational link to non-existent entity | HIGH |
| STR-08 | Schema Version Mismatch | SEV-2 | Compare source payload schema version tag vs current | **AUTO-REMEDIATE** via version adapter if adapter exists; else flag for **HUMAN REVIEW** | MEDIUM |

---

### CAT-2 · IDENTITY & FORMAT DEFECTS

| Defect ID | Defect Name | Severity | Detection Method | Remediation Strategy | Trust Impact |
|---|---|---|---|---|---|
| IDN-01 | Invalid Email Format | SEV-2 | RFC 5322 regex + MX record reachability check | **AUTO-REMEDIATE**: strip leading/trailing whitespace, lowercase, remove invisible chars; re-validate. If still invalid → **BLOCK** | HIGH — email is identity anchor |
| IDN-02 | Invalid Phone Format | SEV-3 | E.164 compliance check + digit count validation | **AUTO-REMEDIATE**: strip non-digits, detect country code, reformat to E.164; if ambiguous → store as-is with `unverified_phone` flag | LOW |
| IDN-03 | Malformed UUID | SEV-2 | RFC 4122 UUID format regex | **AUTO-REMEDIATE**: regenerate UUID if source_record_id (use source system key as fallback); **BLOCK** if canonical `id` field | HIGH |
| IDN-04 | Invalid Date / Timestamp | SEV-2 | ISO 8601 parse attempt + range sanity check | **AUTO-REMEDIATE**: attempt format detection and conversion; if ambiguous → **HUMAN REVIEW** | MEDIUM |
| IDN-05 | Invalid URL Format | SEV-3 | URL regex + scheme validation (http/https only) | **AUTO-REMEDIATE**: strip, re-validate; if invalid → set field to NULL | LOW |
| IDN-06 | Encoding Error (non-UTF-8) | SEV-2 | UTF-8 decode with error detection | **AUTO-REMEDIATE**: attempt UTF-8 transliteration; strip undecodable bytes; log all replacements | MEDIUM |
| IDN-07 | Placeholder / Test Data | SEV-2 | Pattern match against placeholder library (see DCC-5) | **BLOCK** — placeholder data must not enter canonical store | HIGH |
| IDN-08 | Reserved Word Collision | SEV-3 | Match against reserved word list (SQL + system keywords) | **AUTO-REMEDIATE**: prefix with `imported_` when used as field value; log | LOW |

---

### CAT-3 · DUPLICATION DEFECTS

| Defect ID | Defect Name | Severity | Detection Method | Remediation Strategy | Trust Impact |
|---|---|---|---|---|---|
| DUP-01 | Exact Duplicate Record | SEV-1 | SHA-256 hash of canonical field set; check against dedup index | **BLOCK** — skip silently with dedup log entry; do NOT create duplicate | HIGH |
| DUP-02 | Near-Duplicate Record (Fuzzy) | SEV-2 | Jaro-Winkler similarity on full_name + email + phone combination; threshold > 0.92 | **HUMAN REVIEW** — present both records for admin decision | HIGH |
| DUP-03 | Cross-Source Duplicate | SEV-2 | Match on email across `source_system` values within same tenant | **AUTO-REMEDIATE**: merge into existing record, append `source_system` to record's source_history; log merge | MEDIUM |
| DUP-04 | Duplicate Email Within Tenant | SEV-1 | Unique constraint check on `(email, tenant_id)` | **BLOCK** — cannot have two users with same email in same tenant | CRITICAL |
| DUP-05 | Duplicate Source Record Re-import | SEV-2 | Check `(source_record_id, source_system, tenant_id)` against IntegrationSyncLog | **BLOCK** — skip with deduplicate log; update sync timestamp only | MEDIUM |
| DUP-06 | Duplicate Skill Assignment | SEV-3 | Check `(user_id, skill_id)` in UserSkill | **AUTO-REMEDIATE**: keep highest confidence score; log suppressed duplicate | LOW |

---

### CAT-4 · SEMANTIC & CONTENT DEFECTS

| Defect ID | Defect Name | Severity | Detection Method | Remediation Strategy | Trust Impact |
|---|---|---|---|---|---|
| SEM-01 | Empty String as Value | SEV-3 | `TRIM(value) == ""` after whitespace normalization | **AUTO-REMEDIATE**: convert to NULL for nullable fields; BLOCK for required fields | MEDIUM |
| SEM-02 | Whitespace-Only Field | SEV-3 | Regex: `^\s+$` | **AUTO-REMEDIATE**: convert to NULL | LOW |
| SEM-03 | HTML / Script Injection | SEV-1 | DOMPurify-equivalent tag detection + script keyword pattern | **BLOCK** — security threat; quarantine record; fire security alert | CRITICAL — security |
| SEM-04 | SQL Injection Pattern | SEV-1 | Regex pattern library: `DROP`, `SELECT *`, `OR 1=1`, `UNION SELECT`, `--`, `;` sequences | **BLOCK** — security threat; quarantine record; fire P1 security alert | CRITICAL — security |
| SEM-05 | Contradictory Field Pair | SEV-2 | Rule matrix: `hire_date` > `termination_date`; `score` > `max_score`; `start_date` > `end_date` | **BLOCK** — cannot resolve programmatically; **HUMAN REVIEW** | HIGH |
| SEM-06 | Future Date in Historical Field | SEV-2 | Compare field value vs `NOW()`; historical fields: `hire_date`, `created_at`, `dob`, `completed_at` | **BLOCK** — quarantine for human review | HIGH |
| SEM-07 | Negative Value in Non-Negative Field | SEV-2 | Numeric sign check for: `score`, `amount`, `level`, `streak_count`, `match_count` | **BLOCK** for score/certification fields; **AUTO-REMEDIATE** (abs value) for cosmetic numeric fields with log | HIGH |
| SEM-08 | Out-of-Range Score Value | SEV-1 | Range check: Score 0–100; Belt level 1–5; Confidence 0–1; Rating 0–3000 | **BLOCK** — out-of-range score must not enter scoring or certification pipeline | CRITICAL — scoring integrity |
| SEM-09 | Junk / Gibberish Text | SEV-3 | Entropy check on text fields; character n-gram model; random keyboard pattern detection | **AUTO-REMEDIATE**: set to NULL for optional fields; **HUMAN REVIEW** for required text fields | MEDIUM |
| SEM-10 | Language Mismatch | SEV-3 | Language detection (fastText or equivalent) vs expected locale per tenant config | **AUTO-REMEDIATE**: flag with `detected_language` tag; proceed; log for localisation review | LOW |

---

### CAT-5 · TRUST & INTEGRITY DEFECTS

| Defect ID | Defect Name | Severity | Detection Method | Remediation Strategy | Trust Impact |
|---|---|---|---|---|---|
| TRU-01 | Unverified Email Domain for Institution | SEV-2 | Check email domain against Tenant.domain registry; MX record validation | **BLOCK** institution-scoped features; flag `is_verified = FALSE`; allow basic access | HIGH — affects leaderboard, course access |
| TRU-02 | Skill Confidence Below Threshold | SEV-2 | `confidence_score < 0.75` on UserSkill or WorkDataRecord | **AUTO-REMEDIATE**: store record with `is_verified = FALSE`; exclude from hiring marketplace and belt gates | HIGH |
| TRU-03 | Score Anomaly (Statistical Outlier) | SEV-1 | Z-score > 3.0 on score vs cohort distribution for same scenario | **BLOCK** from belt/certification pipeline; send to Governance Board audit queue | CRITICAL — scoring integrity |
| TRU-04 | Match Score Variance Breach | SEV-1 | Peer score standard deviation > 15 points (AIFMM SP-05 threshold) | **BLOCK** from belt pipeline; trigger mentor review workflow (Dojo T2 + T3) | CRITICAL |
| TRU-05 | Certification Without Required Match Count | SEV-1 | Check `match_count >= threshold` per Belt Engine rule (Dojo Section G) | **BLOCK** certification write; cannot issue belt without match count gate | CRITICAL |
| TRU-06 | Belt Promotion Without Mentor Certification | SEV-1 | Check `mentor_certification = TRUE` on Belt promotion record (Dojo Section G) | **BLOCK** — auto-promotion is forbidden per architecture lock | CRITICAL |
| TRU-07 | Forged Source Record ID | SEV-1 | Cross-validate `source_record_id` pattern against source system ID format; detect injection of internal UUIDs | **BLOCK** — quarantine; fire P1 security alert; flag tenant for investigation | CRITICAL — security + trust |
| TRU-08 | Replay Evidence Missing for Certification | SEV-1 | Check `replay_id IS NOT NULL` on Certification records (Dojo T14 binding) | **BLOCK** — no certification without replay evidence | CRITICAL |
| TRU-09 | Cross-Tenant Data Contamination | SEV-1 | Row-level security check: `record.tenant_id != requesting_tenant_id` | **BLOCK** immediately; fire P1 security alert; do not log contaminated data in target tenant | CRITICAL — isolation breach |
| TRU-10 | PII in Non-PII Field | SEV-1 | Regex patterns: email format, phone format, Aadhaar-like pattern, PAN-like pattern in free-text fields | **AUTO-REMEDIATE**: redact detected PII with `[REDACTED]`; log redaction event; fire compliance alert | CRITICAL — data protection |

---

### CAT-6 · ECONOMIC & ABUSE DEFECTS

| Defect ID | Defect Name | Severity | Detection Method | Remediation Strategy | Trust Impact |
|---|---|---|---|---|---|
| ECO-01 | Refund Request Pattern Anomaly | SEV-2 | Rolling 30-day refund rate > 3× cohort average; >3 refunds in 30 days | **BLOCK** further refund processing; flag account for billing ops review | HIGH |
| ECO-02 | Multi-Account Fingerprint Match | SEV-1 | Device fingerprint + behavioural similarity hash > 0.90 across accounts | **BLOCK** all linked accounts from marketplace; flag for trust ops investigation | CRITICAL |
| ECO-03 | Billing Event Without Feature Access | SEV-2 | Check `feature_access_log` for matching event before billing write | **BLOCK** billing write; fire billing middleware integrity alert | HIGH |
| ECO-04 | Match Farming Pattern | SEV-2 | Same pair match count in 7-day window > 5× platform average; rapid succession timestamps | **BLOCK** flagged matches from belt pipeline; send to Governance Board (Dojo T9) | HIGH |
| ECO-05 | Fake Tournament Loop Signal | SEV-2 | Same group tournament participation rate > 5× platform avg in 30 days | **BLOCK** tournament results; flag for governance review (Dojo T15) | HIGH |
| ECO-06 | Mentor Collusion Billing Signal | SEV-2 | Correlated mentor-student billing patterns inconsistent with match history; billing without match record | **BLOCK** billing; flag mentor account; trigger mentor discipline workflow (Dojo T16) | HIGH |
| ECO-07 | Duplicate Payment Event | SEV-1 | Idempotency key check on payment_id + amount + user_id + timestamp window | **BLOCK** — deduplicate; log; do not charge user twice | CRITICAL |
| ECO-08 | Impossible Wallet State | SEV-1 | Wallet balance < 0 after transaction; transaction sum inconsistency | **BLOCK** transaction; fire billing integrity alert; freeze wallet pending review | CRITICAL |

---

## SECTION DCC-5 — PLACEHOLDER & TEST DATA LIBRARY (LOCKED)

The following patterns trigger defect IDN-07 (Placeholder / Test Data Detected). Library is locked. Additions via version bump only.

### Personal Name Patterns
`test`, `Test User`, `John Doe`, `Jane Doe`, `Admin User`, `Sample User`, `Demo User`, `AAAA BBBB`, `Foo Bar`, `asdf`, `qwerty`, `abc`, `xxx`, `yyy`, `zzz`, `First Last`, `FirstName LastName`, `User Name`

### Email Patterns
`test@test.com`, `test@example.com`, `admin@admin.com`, `user@user.com`, `demo@demo.com`, `sample@sample.com`, `foo@bar.com`, `abc@abc.com`, `noreply@noreply.com`, `no-reply@example.com`, any email with `+test`, `+demo`, `+dev`, `+sample` tag subaddress, any `@mailinator.com`, `@guerrillamail.com`, `@throwam.com`, `@yopmail.com`, `@sharklasers.com`, `@trashmail.com`

### Phone Patterns
`0000000000`, `1111111111`, `9999999999`, `1234567890`, `0123456789`, `+10000000000`, `1234567`, any number with >3 consecutive identical digits in critical position

### ID / UUID Patterns
`00000000-0000-0000-0000-000000000000`, `11111111-1111-1111-1111-111111111111`, `ffffffff-ffff-ffff-ffff-ffffffffffff`, `test-id`, `sample-id`, `demo-id`, `12345`

### Score / Numeric Patterns
Any score = exactly `0.00` with no match history, score = exactly `100.00` from unverified single-peer match, skill level = `99` or `0` with no supporting evidence

---

## SECTION DCC-6 — REMEDIATION STRATEGY REFERENCE

### Auto-Remediation Rules (Deterministic — No Human Input Required)

| Rule ID | Applies To | Action | Log Level |
|---|---|---|---|
| REM-01 | Whitespace trim | `TRIM()` all string fields before processing | DEBUG |
| REM-02 | Email normalise | Lowercase + strip whitespace + remove invisible chars | INFO |
| REM-03 | Phone normalise | Strip non-digits + apply E.164 country prefix | INFO |
| REM-04 | Null normalisation | Convert `""`, `"N/A"`, `"—"`, `"null"`, `"NULL"`, `"none"` to SQL NULL | INFO |
| REM-05 | Enum fuzzy map | Jaro-Winkler ≥ 0.85 against enum dictionary → map to canonical value | INFO |
| REM-06 | String truncation | Truncate to field max length + append `…` | WARN |
| REM-07 | Type coercion | Apply AIFMM Type Resolution rules (L4) | INFO |
| REM-08 | Duplicate skill | Keep highest confidence_score entry; discard lower | INFO |
| REM-09 | Cross-source merge | Append new source to `source_history[]`; update `updated_at` | INFO |
| REM-10 | PII redaction | Replace detected PII in free-text with `[REDACTED]`; log field + pattern | WARN |
| REM-11 | URL sanitise | Strip non-http/https schemes; remove tracking params; re-validate | INFO |
| REM-12 | Encoding fix | UTF-8 transliteration; replace undecodable bytes with `?` | WARN |
| REM-13 | Date normalise | Detect format → convert to ISO 8601 → store as TIMESTAMP TZ | INFO |
| REM-14 | Language tag | Detect language; append `detected_language` metadata; proceed | DEBUG |
| REM-15 | Skill confidence flag | Set `is_verified = FALSE`; exclude from trust gates; proceed to store | INFO |

### Block & Quarantine Rules (Human Review Required After Block)

| Rule ID | Trigger Defects | Action | Escalation Path |
|---|---|---|---|
| BLK-01 | STR-01, STR-02, STR-07 | BLOCK + quarantine record | Human Review Queue (admin) |
| BLK-02 | SEM-03, SEM-04 | BLOCK + quarantine + P1 security alert | Security ops + Governance Board |
| BLK-03 | TRU-03, TRU-04, SEM-08 | BLOCK + send to Governance Board audit queue | Governance Board |
| BLK-04 | TRU-05, TRU-06, TRU-08 | BLOCK certification write | Belt Engine + Mentor Board queue |
| BLK-05 | TRU-09, TRU-07 | BLOCK + P1 security alert + freeze import job | Security ops immediate |
| BLK-06 | ECO-02, ECO-07, ECO-08 | BLOCK + freeze affected accounts | Billing ops + Trust ops |
| BLK-07 | DUP-01, DUP-04, DUP-05 | BLOCK silently + log dedup entry | No escalation unless pattern detected |
| BLK-08 | IDN-07 | BLOCK + log placeholder detection | Admin notification |
| BLK-09 | TRU-02 (below hard floor) | BLOCK from trust gates only; allow basic store | No escalation |
| BLK-10 | SEM-05, SEM-06 | BLOCK + Human Review Queue | Admin review |

---

## SECTION DCC-7 — CLEANING PIPELINE EXECUTION ORDER

The DCC runs as a pipeline. Stages are sequential and mandatory. No stage may be skipped.

```
┌──────────────────────────────────────────────────────────────┐
│              DCC PIPELINE EXECUTION (LOCKED)                 │
│                                                              │
│  STAGE 1 ── PRE-FLIGHT SECURITY SCAN                         │
│    ↳ SEM-03 (HTML injection) scan                            │
│    ↳ SEM-04 (SQL injection) scan                             │
│    ↳ TRU-07 (Forged ID) scan                                 │
│    ↳ TRU-09 (Cross-tenant) check                             │
│    → Any hit: STOP PIPELINE · BLOCK · P1 ALERT               │
│                                                              │
│  STAGE 2 ── STRUCTURAL INTEGRITY CHECK                       │
│    ↳ STR-01 through STR-08                                   │
│    → SEV-1: STOP PIPELINE · BLOCK record                     │
│    → SEV-2: attempt auto-remediate · proceed or BLOCK        │
│                                                              │
│  STAGE 3 ── IDENTITY & FORMAT NORMALISATION                  │
│    ↳ IDN-01 through IDN-08                                   │
│    ↳ Apply REM-01 through REM-07, REM-11–REM-13              │
│    → Normalised record produced                              │
│                                                              │
│  STAGE 4 ── DEDUPLICATION ENGINE                             │
│    ↳ DUP-01 through DUP-06                                   │
│    ↳ Exact hash check → Fuzzy match → Cross-source merge     │
│    → Clean record or BLOCK with dedup log                    │
│                                                              │
│  STAGE 5 ── SEMANTIC VALIDATION                              │
│    ↳ SEM-01 through SEM-10                                   │
│    ↳ Apply REM-08, REM-10, REM-14, REM-15                    │
│    → Semantically clean record or BLOCK                      │
│                                                              │
│  STAGE 6 ── TRUST & INTEGRITY GATES                          │
│    ↳ TRU-01 through TRU-10                                   │
│    ↳ Score range gates · Certification gates · PII scan      │
│    → Trust-graded record or BLOCK                            │
│                                                              │
│  STAGE 7 ── ECONOMIC INTEGRITY SCAN                          │
│    ↳ ECO-01 through ECO-08                                   │
│    ↳ Billing dedup · Wallet state · Abuse pattern scan       │
│    → Clean economic record or BLOCK                          │
│                                                              │
│  STAGE 8 ── CLEANING DECISION RECORD WRITE                   │
│    ↳ Write DataCleaningDecision (immutable)                  │
│    ↳ Emit event: data.cleaning.completed                     │
│    → Pass to AIFMM L8 Import Executor                        │
│    OR → Route to Quarantine Store                            │
└──────────────────────────────────────────────────────────────┘
```

**Pipeline rules:**
- Stage 1 failures abort all remaining stages immediately
- A record may accumulate multiple defect labels (multi-defect record)
- Multi-defect records are governed by the highest severity defect present
- Records that pass all 8 stages receive `cleaning_status = CLEAN`
- Records blocked at any stage receive `cleaning_status = QUARANTINED`
- Records auto-remediated receive `cleaning_status = REMEDIATED`

---

## SECTION DCC-8 — DEDUPLICATION ENGINE SPECIFICATION

### 8.1 Exact Deduplication

**Method:** SHA-256 hash of canonical field fingerprint set.

**Fingerprint Field Set (locked per entity):**

| Entity | Fingerprint Fields |
|---|---|
| User | `email` + `tenant_id` |
| UserSkill | `user_id` + `skill_id` |
| Match | `match_id` (already unique by design) |
| Score | `match_id` + `user_id` + `scorer_id` |
| Certification | `user_id` + `skill_id` + `belt_level` + `issued_at` (date-only) |
| WorkDataRecord | `user_id` + `source_system` + `source_record_ref` + `signal_type` |
| BillingEvent | `user_id` + `idempotency_key` |
| MigrationRecord | `source_record_id` + `source_system` + `tenant_id` |

### 8.2 Fuzzy Deduplication

**Method:** Jaro-Winkler similarity scoring on name + contact fields.

**Thresholds:**

| Comparison | Field Set | Threshold | Action |
|---|---|---|---|
| Near-duplicate user | `full_name` (Jaro-Winkler) + `phone` prefix match | > 0.92 | Human Review Queue |
| Near-duplicate organisation | `Tenant.name` (Jaro-Winkler) + `domain` | > 0.88 | Human Review Queue |
| Near-duplicate skill name | `Skill.name` (Jaro-Winkler) | > 0.95 | Auto-map to existing skill; log |

### 8.3 Cross-Source Merge Protocol

When DUP-03 (Cross-Source Duplicate) is detected — same real entity imported from multiple tools:

```
1. Identify master record (earliest created_at wins; verified > unverified)
2. Append incoming source to master record's source_history[] JSON array
3. Merge non-null fields: incoming value overwrites master ONLY if:
   - incoming field has higher confidence_score, OR
   - master field is NULL
4. Increment merge_count on master record
5. Write DataCleaningDecision with outcome = MERGED
6. Discard incoming record (do not create duplicate row)
```

---

## SECTION DCC-9 — QUARANTINE STORE SCHEMA

Records that cannot be auto-remediated are written to the Quarantine Store. The Quarantine Store is a separate schema partition with full audit trail.

```sql
DataCleaningDecision (
  id                    UUID PK,
  migration_job_id      UUID FK NULLABLE,
  integration_conn_id   UUID FK NULLABLE,
  source_entity_type    TEXT,           -- 'User', 'Score', 'BillingEvent', etc.
  source_record         JSONB,          -- original incoming record (before any remediation)
  defects_detected      JSONB,          -- array of { defect_id, severity, field, value }
  remediations_applied  JSONB,          -- array of { rule_id, field, before, after }
  cleaning_status       ENUM(
                          'CLEAN',
                          'REMEDIATED',
                          'QUARANTINED',
                          'BLOCKED_SECURITY',
                          'BLOCKED_TRUST',
                          'BLOCKED_DUPLICATE',
                          'BLOCKED_STRUCTURAL'
                        ),
  highest_severity      ENUM('SEV-1','SEV-2','SEV-3','SEV-4'),
  pipeline_stage_failed TEXT NULLABLE,  -- e.g. 'STAGE-2', 'STAGE-6'
  review_status         ENUM('pending','approved','rejected','escalated') DEFAULT 'pending',
  reviewed_by           UUID FK NULLABLE,
  reviewed_at           TIMESTAMP TZ NULLABLE,
  resolution_notes      TEXT NULLABLE,
  tenant_id             UUID FK,
  executed_at           TIMESTAMP TZ DEFAULT NOW(),
  dcc_version           TEXT DEFAULT '1.0'
)
```

**Immutability enforcement:** The `source_record` and `defects_detected` columns are write-once. No UPDATE permitted on these columns. Resolution is appended via `review_status`, `reviewed_by`, `reviewed_at`, `resolution_notes` columns only.

---

## SECTION DCC-10 — TRUST GATE INTEGRATION (DOWNSTREAM BINDINGS)

The DCC's output `cleaning_status` feeds directly into downstream trust and certification gates. These bindings are locked.

| DCC Status | Belt Engine Gate | Certification Gate | Hiring Marketplace | Scoring Pipeline |
|---|---|---|---|---|
| `CLEAN` | Eligible | Eligible | Eligible | Proceed |
| `REMEDIATED` | Eligible (if no TRU defects) | Eligible (if no TRU defects) | Eligible (if no TRU defects) | Proceed |
| `QUARANTINED` | BLOCKED | BLOCKED | BLOCKED | BLOCKED |
| `BLOCKED_SECURITY` | BLOCKED | BLOCKED | BLOCKED | BLOCKED |
| `BLOCKED_TRUST` | BLOCKED | BLOCKED | BLOCKED | BLOCKED |
| `BLOCKED_DUPLICATE` | BLOCKED (primary record used) | BLOCKED (primary record used) | Eligible (primary) | Primary proceeds |
| `BLOCKED_STRUCTURAL` | BLOCKED | BLOCKED | BLOCKED | BLOCKED |

**Additional trust gate rules:**
- Any record with TRU-03 or TRU-04 defect → belt pipeline blocked regardless of cleaning_status
- Any record with TRU-05 or TRU-06 → certification gate blocked regardless of cleaning_status
- Any record with ECO-02 → full platform feature block pending investigation
- `BLOCKED_SECURITY` status → P1 alert fires regardless of any other condition

---

## SECTION DCC-11 — SCORING & MATCH DATA CLEANING RULES

The Dojo scoring pipeline has specific additional cleaning rules beyond the standard DCC taxonomy. These are supplementary to CAT-1 through CAT-6 and apply exclusively to `Score`, `Match`, and `Belt` entities.

| Rule ID | Rule Name | Trigger Condition | Action | Dojo Section Binding |
|---|---|---|---|---|
| SCR-01 | Score Out of Range | `score_value < 0 OR score_value > 100` | SEV-1 BLOCK — cannot enter scoring pipeline | Dojo Section F |
| SCR-02 | Peer Score Count Mismatch | Fewer peer scores than required by scenario config | SEV-2 — flag match as incomplete; block from belt | Dojo T2 |
| SCR-03 | Self-Score Without Peer/Mentor | Self-score present but peer and mentor scores absent | SEV-2 — block from weighted merge; flag for review | Dojo Section F |
| SCR-04 | Score Submitted After Match End +15min | `submission_timestamp > match_end_time + 15 minutes` | SEV-2 — flag as late submission; reduce confidence | Dojo Section P6 |
| SCR-05 | Mentor Score Without Certification | Mentor submitted score but `mentor_certification = FALSE` | SEV-1 BLOCK — uncertified mentor score invalid | Dojo T7 |
| SCR-06 | Override Without Audit Log | Score override exists but no immutable override log entry | SEV-1 BLOCK — override without audit trail is invalid | Dojo Section H |
| SCR-07 | Replay Missing for Ranked Match | Ranked match record has no associated `replay_id` | SEV-1 BLOCK from belt pipeline — replay mandatory | Dojo Section P6 |
| SCR-08 | Duplicate Score Submission | Same `(match_id, scorer_id, score_type)` already in Score table | SEV-2 — keep earlier submission; discard later; log | Dojo Section F |
| SCR-09 | Match Result Without Analytics Event | Match `status = completed` but no analytics pipeline event emitted | SEV-2 — trigger analytics re-emission; alert P5 | Dojo Section P6 |
| SCR-10 | Belt Promoted Without Pressure Scenario Pass | `pressure_scenario_pass = FALSE` on belt promotion record | SEV-1 BLOCK — belt requires pressure scenario (Dojo G) | Dojo Section G |

---

## SECTION DCC-12 — REAL-TIME vs BATCH PROCESSING MODES

The DCC operates in two processing modes depending on the ingestion pathway.

### Real-Time Mode (API / Sync Events)

| Trigger | Max Latency SLA | Parallelism | Failure Behaviour |
|---|---|---|---|
| User registration API | 200ms | Per-request | BLOCK request with 422 response + error detail |
| Integration sync event | 500ms | Per-event on Redis Streams consumer | BLOCK event; emit `data.cleaning.failed` event |
| Score submission API | 300ms | Per-match room socket event | BLOCK score; notify match room of rejection |
| Billing event | 200ms | Per-event | BLOCK billing write; return error to payment layer |
| Match start event | 300ms | Per-match | BLOCK match start if player data fails DCC |

### Batch Mode (Migration Jobs)

| Batch Size | Processing Model | Failure Behaviour |
|---|---|---|
| ≤ 1,000 records | Sequential per record | Per-record block/quarantine; rest continue |
| 1,001 – 50,000 | Parallel workers (10 workers max) | Per-record; worker failure → retry × 3 then quarantine |
| > 50,000 | Chunked batches of 5,000 | Per-chunk error isolation; partial success permitted |
| Any size | Stage 1 (security scan) runs on ALL records before Stage 2 begins | Any Stage 1 hit pauses entire batch for review |

---

## SECTION DCC-13 — API CONTRACTS (LOCKED)

```
POST   /dcc/validate              → Run DCC pipeline on submitted record (real-time)
POST   /dcc/batch                 → Submit batch of records for DCC (migration mode)
GET    /dcc/job/{job_id}/status   → Poll batch DCC job status
GET    /dcc/job/{job_id}/report   → Full cleaning report: decisions, remediations, quarantine list
GET    /dcc/quarantine            → List quarantined records for tenant (paginated)
GET    /dcc/quarantine/{id}       → Get single quarantined record + defect detail
POST   /dcc/quarantine/{id}/review  → Submit admin review decision (approve/reject/modify)
GET    /dcc/decisions             → Query DataCleaningDecision log (governance admin only)
GET    /dcc/stats                 → Cleaning statistics: defect rate by category, remediation rate
GET    /dcc/rules                 → List all active cleaning rules (read-only)
```

All endpoints require JWT auth. Quarantine review endpoints require `cleaning_ops` or `governance_admin` role. Decision log endpoint requires `governance_admin` role. Rule listing is read-only to all authenticated admin roles.

---

## SECTION DCC-14 — OBSERVABILITY & ALERTING BINDINGS

| Alert ID | Trigger Condition | Severity | Route |
|---|---|---|---|
| DCC-ALT-01 | SEM-03 or SEM-04 detected | P1 CRITICAL | Security ops + Engineering lead + immediate |
| DCC-ALT-02 | TRU-09 detected | P1 CRITICAL | Security ops + Governance Board |
| DCC-ALT-03 | ECO-02 detected | P1 HIGH | Trust ops + Account team |
| DCC-ALT-04 | Overall defect rate > 15% in batch | HIGH | Data ops + Migration team |
| DCC-ALT-05 | TRU-03 score anomaly cluster (>3 in 1 hour) | HIGH | Governance Board + Scoring team |
| DCC-ALT-06 | ECO-04 match farming cluster | HIGH | Governance Board |
| DCC-ALT-07 | DCC pipeline latency breach (real-time SLA) | MEDIUM | Engineering + Infra |
| DCC-ALT-08 | Quarantine queue depth > 500 records | MEDIUM | Data ops team |
| DCC-ALT-09 | DataCleaningDecision write failure | P1 CRITICAL | STOP all imports; engineering alert |
| DCC-ALT-10 | IDN-07 placeholder rate > 5% of batch | MEDIUM | Migration ops — suspect test data import |
| DCC-ALT-11 | DUP-04 rate spike (email duplicates) | HIGH | Data ops — suspect bulk re-import |
| DCC-ALT-12 | SCR-05 mentor score without cert (any count) | HIGH | Mentor certification team |

---

## SECTION DCC-15 — SECURITY & COMPLIANCE BINDINGS

| Requirement | Binding | Enforcement |
|---|---|---|
| PII redaction in quarantine store | `source_record` JSONB stored with PII encrypted at rest | Dojo P1 / Ecoskiller R10 |
| Quarantine store tenant isolation | Row-level security on `DataCleaningDecision.tenant_id` | Ecoskiller R10 / Dojo P11 |
| Stage 1 security scan mandatory | No bypass permitted under any operational mode | Architecture lock — Dojo P1 |
| Immutable audit log | `source_record` + `defects_detected` columns: no UPDATE/DELETE | Dojo P1 Section H |
| WAF upstream of DCC API | Kong OSS + WAF; SEM-03/04 patterns also checked at WAF layer | Ecoskiller R1 stack |
| Rate limiting | DCC /validate: 100 req/min per tenant; /batch: 10 concurrent jobs per tenant | Ecoskiller R10 |
| GDPR: right to erasure | Erasure request → `source_record` in DataCleaningDecision anonymised; defect metadata retained | Ecoskiller R15 |
| Secrets management | No plaintext credentials in DCC service config | Hashicorp Vault OSS binding |

---

## SECTION DCC-16 — DATA QUALITY SCORECARD

The DCC produces a tenant-level Data Quality Scorecard updated after every batch job and on a rolling 24-hour basis for real-time mode.

```
DataQualityScorecard {
  tenant_id:              UUID,
  period_start:           TIMESTAMP TZ,
  period_end:             TIMESTAMP TZ,
  total_records_processed: INTEGER,
  clean_count:            INTEGER,
  remediated_count:       INTEGER,
  quarantined_count:      INTEGER,
  blocked_security_count: INTEGER,

  defect_rate_by_category: {
    structural:   DECIMAL(5,2),   // % of records with CAT-1 defects
    identity:     DECIMAL(5,2),
    duplication:  DECIMAL(5,2),
    semantic:     DECIMAL(5,2),
    trust:        DECIMAL(5,2),
    economic:     DECIMAL(5,2)
  },

  top_defects_by_frequency: [{ defect_id, count, pct }],
  auto_remediation_rate:    DECIMAL(5,2),  // % of defects auto-remediated
  human_review_pending:     INTEGER,
  overall_quality_score:    DECIMAL(5,2),  // 100 - weighted defect impact score
  dcc_version:              TEXT
}
```

**Overall Quality Score formula (locked):**
```
quality_score = 100 - (
  (blocked_security_count / total × 40) +
  (blocked_trust_count    / total × 30) +
  (quarantined_count      / total × 20) +
  (remediated_count       / total × 10)
) × 100
```

**Quality Score Gates:**

| Score | Classification | Action |
|---|---|---|
| 95–100 | GOLD | Full pipeline access |
| 85–94 | CLEAN | Full pipeline access |
| 70–84 | ACCEPTABLE | Access with monitoring; data ops notified |
| 50–69 | DEGRADED | Migration ops review required before next batch |
| < 50 | BLOCKED | Import pipeline suspended; Governance Board review |

---

## SECTION DCC-17 — FINAL LOCK SEAL

```
╔═══════════════════════════════════════════════════════════════════╗
║      ANTIGRAVITY — ARTIFACT #90                                   ║
║      DATA CLEANING CLASSIFIER (DCC)                               ║
║      ENTERPRISE OPTIMIZATION + TRUST INFRASTRUCTURE               ║
║                                                                   ║
║  Status:  SEALED · LOCKED · PRODUCTION ACTIVE                     ║
║  Version: v1.0                                                    ║
║                                                                   ║
║  Defect Taxonomy Categories:         6  (CAT-1 through CAT-6)     ║
║  Total Defect Types Defined:        52  (all classified)          ║
║  Severity Levels:                    4  (SEV-1 through SEV-4)     ║
║  Auto-Remediation Rules:            15  (REM-01 through REM-15)   ║
║  Block & Quarantine Rules:          10  (BLK-01 through BLK-10)   ║
║  Pipeline Stages:                    8  (all mandatory)           ║
║  Scoring-Specific Rules:            10  (SCR-01 through SCR-10)   ║
║  Placeholder / Test Patterns:       50+ (locked library)          ║
║  API Contracts Declared:            10                            ║
║  Observability Alerts:              12                            ║
║  Processing Modes:                   2  (real-time + batch)       ║
║                                                                   ║
║  STAGE 1 SECURITY SCAN:         MANDATORY — NO BYPASS PERMITTED   ║
║  AUDIT LOG:                     IMMUTABLE (APPEND-ONLY)           ║
║  PII PROTECTION:                MANDATORY (P1 BLOCK IF BREACH)    ║
║  TENANT ISOLATION:              ROW-LEVEL SECURITY ENFORCED       ║
║  CROSS-TENANT BLOCK:            P1 CRITICAL — IMMEDIATE HALT      ║
║                                                                   ║
║  DOJO SCORING INTEGRITY:        BOUND (SCR-01–10)                 ║
║  DOJO TRUST INFRASTRUCTURE:     BOUND (TRU-01–10)                 ║
║  DOJO ECONOMIC ABUSE CONTROLS:  BOUND (ECO-01–08)                 ║
║  ECOSKILLER DATA MODEL FREEZE:  BOUND                             ║
║  AIFMM ARTIFACT #89:            UPSTREAM DEPENDENCY BOUND         ║
║  IMPORT EXECUTOR (AIFMM L8):    DOWNSTREAM DEPENDENCY BOUND       ║
║                                                                   ║
║  PREDECESSOR:  Artifact #89 — AI Field Mapping Model              ║
║  SUCCESSOR:    Artifact #91 (forthcoming)                         ║
║                                                                   ║
║  Interpretation Authority: NONE                                   ║
║  Mutation Policy: Add-Only via Version Bump                       ║
║  Architecture Authority: LOCKED                                   ║
║  Governance Board Approval: Required for any rule change          ║
╚═══════════════════════════════════════════════════════════════════╝
```

---

*End of ANTIGRAVITY Artifact #90 — Data Cleaning Classifier v1.0 — SEALED & LOCKED*
*All ingestion pathways (migration, sync, API, bulk upload, scoring, billing) must route through the DCC pipeline before writing to the canonical store.*
*No record may enter the canonical store without a corresponding DataCleaningDecision audit entry.*
*STOP EXECUTION if any pipeline stage, validation gate, or audit log write is absent or bypassed.*
