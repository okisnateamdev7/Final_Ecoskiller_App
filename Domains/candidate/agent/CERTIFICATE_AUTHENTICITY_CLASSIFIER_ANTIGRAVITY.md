# 🔒 CERTIFICATE AUTHENTICITY CLASSIFIER — ANTIGRAVITY PROTOCOL
## ENTERPRISE OPTIMIZATION + TRUST INFRASTRUCTURE
### ECOSKILLER × DOJO SAAS — SEALED PRODUCTION ARTIFACT v1.0

```
Artifact Class:       Enterprise Certificate Trust Subsystem
System Codename:      ANTIGRAVITY — VAULT SEAL
Module Code:          AG-CAC-002
Mutation Policy:      Add-only via version bump
Execution Mode:       Deterministic
Stack Lock:           Enforced
Interpretation Auth:  NONE
Seal Status:          LOCKED · FINAL · GOVERNED · PRODUCTION-READY
Parent System:        ANTIGRAVITY Trust Infrastructure Layer
Sibling Modules:      AG-EPDM-001 (Excel Pattern Detection)
                      AG-FPDM-001 (Fake Profile Detection / PHANTOM SHIELD)
```

---

# ⚠️ VAULT SEAL — SYSTEM IDENTITY DECLARATION

## What VAULT SEAL Is

VAULT SEAL is the Certificate Authenticity Classifier (CAC) embedded within the ANTIGRAVITY Enterprise Optimization + Trust Infrastructure layer of the Ecoskiller Unified Talent OS. It is a cryptographic, AI-governed, multi-layer classification engine that verifies, validates, scores, and permanently anchors the authenticity of every certificate issued within or submitted to the Ecoskiller ecosystem — including Dojo Belt Certificates, Skill Certifications, Course Completion Certificates, Championship Medals, Trainer Legacy Records, and externally imported third-party credentials.

VAULT SEAL does not issue certificates. It classifies, scores, and seals them. Issuance authority belongs exclusively to the Dojo Belt Engine, Skill Engine, and Mentor Governance Board.

## Why It Exists

Ecoskiller's competitive moat is verified, trustworthy skill proof. A hiring organization, recruiter, parent, or institution that relies on an Ecoskiller certificate must have absolute certainty that certificate represents a real, earned, governed assessment outcome. Without VAULT SEAL, certificates can be fabricated, backdated, cloned, version-mismatched, mentor-bypassed, or issued via collusion. VAULT SEAL eliminates every attack vector against certificate integrity and transforms Ecoskiller certificates into the most tamper-evident skill credentials in the market.

## What It Is Not

```
✗ Not a certificate issuance engine — issuance is Dojo + Skill Engine authority
✗ Not a legal notarization service — it classifies authenticity, not legal validity
✗ Not a replacement for mentor sign-off — it validates that sign-off exists
✗ Not accessible to students or non-admin roles for raw output
✗ Not a scoring engine — it feeds Trust Score and Certification Integrity only
```

## Authority Boundary

```
VAULT SEAL reads from:      cert_audit_log (immutable Dojo record)
                             belt_engine output tables (read replica)
                             mentor_commands audit log
                             match_results tables (read replica)
                             phantom_profiles (AG-FPDM-001 signals)
                             excel_uploads pattern results (AG-EPDM-001)
                             external_cert_submissions (enterprise uploads)

VAULT SEAL writes to:       cert_authenticity_records (append-only)
                             cert_seal_log (immutable)
                             cert_revocation_queue (pending human action)
                             trust_flags (shared with PHANTOM SHIELD)

VAULT SEAL signals to:      Hiring Marketplace visibility gate
                             Blockchain Anchoring Service
                             Public Verification Portal
                             Governance Board alert queue
                             Enterprise Admin dashboard

VAULT SEAL does NOT:        Issue, modify, or delete certificates
                             Override mentor certification decisions
                             Directly revoke certificates (queues for human action)
                             Access cross-tenant certificate data
```

---

# 🔒 SECTION A — VAULT SEAL STACK LOCK (NON-NEGOTIABLE)

## A1. Classification Processing Backend

```
Language:               Python 3.11
Framework:              FastAPI microservice (isolated service boundary)
Cryptographic Engine:   hashlib (SHA-256, SHA-3-256) + cryptography library
                        Ed25519 signature verification
Blockchain Anchor:      Web3.py — Polygon PoS (low-cost, high-throughput)
                        Fallback: IPFS content addressing (CIDv1)
AI Classification:      Claude API (claude-sonnet) — narrative + metadata analysis
ML Runtime:             scikit-learn — anomaly detection on issuance patterns
OCR Engine:             Tesseract 5 + PaddleOCR — external cert image parsing
Document Parser:        pdfplumber + PyMuPDF — PDF cert extraction
QR/Barcode Engine:      zxing-python — embedded code extraction
Event Broker:           Redis Streams (Ecoskiller Event Bus)
Database Read:          PostgreSQL 15 (read replica — core Dojo tables)
Database Write:         PostgreSQL 15 (vault_schema — isolated write schema)
Cache:                  Redis 7 (verification result cache, TTL-controlled)
Queue:                  Celery + Redis (async classification jobs)
Object Storage:         MinIO (external cert staging — time-limited)
Search Index:           OpenSearch 2.x (certificate fingerprint search + dedup)
```

## A2. Client Surface Integration Points

```
Flutter App:            Certificate Vault viewer + authenticity badge display
                        Enterprise admin — certificate review + revocation queue
React Web:              Public Verification Portal (SEO-indexed, no auth required)
                        Enterprise Trust Portal — bulk cert audit
API Gateway:            Kong OSS — /api/v1/trust/vault/*
Auth:                   JWT + RBAC (trust_admin | governance_board | public_verify)
                        public_verify = unauthenticated read for public portal
Notification:           Postal (email — certificate sealed confirmation)
                        FCM (push — revocation alert to certificate holder)
```

## A3. Module Isolation Rules

```
VAULT SEAL runs as:       Isolated FastAPI microservice
Port boundary:            Internal only except /public/verify (external)
DB access:                Read replica for Dojo/Skill/Match tables
                          Dedicated write schema: vault_schema
Cross-module reads:       AG-FPDM-001 phantom_profiles (via Event Bus query)
                          AG-EPDM-001 pattern_results (via Event Bus query)
No direct DB writes to:   cert_audit_log, belt_engine tables, match_results
                          (these are Dojo Engine territory — read-only for VAULT SEAL)
Blockchain calls:         Rate-limited — batch anchor every 15 minutes
                          Emergency single anchor: TIER 5 cert fraud events only
```

🚫 VAULT SEAL cannot issue certificates
🚫 VAULT SEAL cannot modify Dojo Belt Engine records
🚫 VAULT SEAL cannot access billing or payment data
🚫 VAULT SEAL cannot write to cross-tenant vault schemas

Stack split is LOCKED.

---

# 🔒 SECTION B — CERTIFICATE TYPE REGISTRY (FROZEN)

All certificate types classified by VAULT SEAL. Type registry is add-only.

```
CERT_TYPE_01:   DOJO_BELT
                Source: Dojo Belt Engine
                Issuer: Mentor + Belt Engine (joint)
                Binding: SECTION G — Belt & Certification Lock
                Blockchain anchor: MANDATORY

CERT_TYPE_02:   SKILL_CERTIFICATION
                Source: Skill Engine
                Issuer: Skill Engine + Mentor board
                Binding: SECTION T1 — Skill Validity Framework Lock
                Blockchain anchor: MANDATORY

CERT_TYPE_03:   COURSE_COMPLETION
                Source: Education Module
                Issuer: Trainer / Course Engine
                Binding: SECTION R88 — Course Analytics Law
                Blockchain anchor: OPTIONAL (enterprise plan: MANDATORY)

CERT_TYPE_04:   CHAMPIONSHIP_MEDAL
                Source: Tournament Engine
                Issuer: Tournament Engine (automated) + Governance review
                Binding: SECTION T5 — Match Fairness Engine Lock
                Blockchain anchor: MANDATORY

CERT_TYPE_05:   TRAINER_LEGACY_RECORD
                Source: Trainer Legacy System (R90)
                Issuer: Platform + Governance Board
                Binding: SECTION R90 — Trainer Legacy & Archival Lock
                Blockchain anchor: MANDATORY (permanent)

CERT_TYPE_06:   MENTOR_CERTIFICATION
                Source: Mentor Certification Track
                Issuer: Governance Board
                Binding: SECTION T7 — Mentor Training & Certification Lock
                Blockchain anchor: MANDATORY

CERT_TYPE_07:   INTELLIGENCE_ASSESSMENT
                Source: Intelligence Measurement System (Module 2)
                Issuer: Assessment Engine
                Binding: Module 2 — Intelligence Measurement System
                Blockchain anchor: OPTIONAL

CERT_TYPE_08:   EXTERNAL_THIRD_PARTY
                Source: Enterprise upload (user-submitted)
                Issuer: External institution (unverified by default)
                Binding: Enterprise Integration Layer
                Blockchain anchor: FORBIDDEN until AUTHENTIC verdict

CERT_TYPE_09:   ENTERPRISE_SKILL_PROOF
                Source: Enterprise Excel + Tool Integration (AG-EPDM-001)
                Issuer: ANTIGRAVITY Enrichment Engine
                Binding: Excel Pattern Detection Model (AG-EPDM-001)
                Blockchain anchor: CONDITIONAL (TIER 1 confidence only)
```

---

# 🔒 SECTION C — CLASSIFICATION ENGINE — 15 VERIFICATION LAYERS (FROZEN)

Every certificate classification runs all applicable layers in sequence. Layers are NOT parallel — they are ordered. A hard-FAIL on any BLOCKING layer halts classification and returns INAUTHENTIC immediately.

---

### LAYER 01 — CRYPTOGRAPHIC HASH VERIFICATION ★ BLOCKING
```
Purpose:      Verify certificate content has not been tampered with post-issuance
Method:
  1. Recompute SHA-256 hash of cert payload (canonical JSON form)
  2. Compare against stored hash in cert_audit_log (immutable Dojo record)
  3. If cert carries Ed25519 signature: verify against Ecoskiller platform public key
  4. For external certs: compute hash of submitted file; store as reference
Inputs:
  cert_payload_json             (canonical serialization)
  cert_audit_log.cert_hash      (authoritative reference)
  cert_signature                (Ed25519 bytes, if present)
  platform_public_key           (from secrets manager)
Output:
  hash_match                    (BOOL)
  signature_valid               (BOOL | NULL if unsigned)
  hash_verification_score       (1.0 = match | 0.0 = mismatch)
FAIL condition:   hash_match = FALSE → INAUTHENTIC (no further layers run)
```

---

### LAYER 02 — ISSUANCE AUTHORITY VALIDATION ★ BLOCKING
```
Purpose:      Confirm certificate was issued by an authorized entity
Method:
  1. Retrieve issuer_id from cert record
  2. Validate issuer_id holds valid issuer role at time of issuance
     (mentor: check certification status at cert.issued_at)
     (trainer: check trainer_verified_at < cert.issued_at)
     (tournament engine: check tournament_id exists and is finalized)
  3. For CERT_TYPE_01/02: verify mentor_sign_off exists in mentor_commands audit log
  4. For CERT_TYPE_06: verify governance_board_decision exists for this mentor
Inputs:
  cert.issuer_id
  cert.issued_at
  mentor_certification_audit_log
  governance_board_decisions
Output:
  issuer_authorized_at_issuance  (BOOL)
  issuer_authority_evidence_id   (audit_record_id | NULL)
  authority_validation_score     (0.0–1.0)
FAIL condition:  issuer_authorized_at_issuance = FALSE → INAUTHENTIC
Dojo Binding:    SECTION T7 — Mentor Training & Certification Lock
                 SECTION G — Belt & Certification Lock
```

---

### LAYER 03 — BELT PROGRESSION INTEGRITY CHECK ★ BLOCKING (CERT_TYPE_01 only)
```
Purpose:      Verify belt level is consistent with documented progression history
Method:
  1. Load full belt history for user_token
  2. Confirm each belt level has a preceding lower belt (no skips without override)
  3. Verify each belt has required match_count met at time of award
  4. Verify each belt has required score_threshold met
  5. Verify pressure_scenario_pass recorded in match_results
  6. Auto-promotion check: ensure no belt was granted without mentor_commands entry
Inputs:
  belt_history[]                (ordered by awarded_at)
  match_count_at_cert_date      (INT)
  score_threshold_config        (from Belt Engine config — versioned)
  pressure_scenario_results[]
  mentor_commands audit entries
Output:
  progression_valid             (BOOL)
  skipped_belt_levels[]         (belt_level[])
  auto_promotion_detected       (BOOL)
  progression_integrity_score   (0.0–1.0)
FAIL condition:  auto_promotion_detected = TRUE → INAUTHENTIC
                 skipped_belt_levels not approved by mentor → INAUTHENTIC
Dojo Binding:    SECTION G — Belt & Certification Lock (auto promotion forbidden)
```

---

### LAYER 04 — RUBRIC VERSION CONSISTENCY CHECK ★ BLOCKING
```
Purpose:      Verify certificate was assessed against the correct rubric version active at issuance time
Method:
  1. Load cert.rubric_version_tag
  2. Load rubric_version_at(cert.issued_at) from content governance system
  3. Check these match exactly
  4. If rubric major version changed after issuance → flag for re-certification
  5. Check cert.belt_model_version_tag matches Belt Engine version active at issuance
Inputs:
  cert.rubric_version_tag
  cert.belt_model_version_tag
  rubric_version_history[]       (from SECTION T8 Content Governance)
  belt_version_history[]         (from SECTION T17 Belt Version Governance)
Output:
  rubric_version_match           (BOOL)
  belt_version_match             (BOOL)
  recertification_required       (BOOL)
  version_integrity_score        (0.0–1.0)
FAIL condition:  rubric_version_match = FALSE AND cert issued after version change → INAUTHENTIC
Dojo Binding:    SECTION T17 — Belt Version Governance Lock
                 SECTION T8  — Content Governance Lock
```

---

### LAYER 05 — MATCH EVIDENCE CROSS-REFERENCE ★ BLOCKING (CERT_TYPE_01/02/04)
```
Purpose:      Verify that the matches claimed as evidence for the certificate actually exist and count
Method:
  1. Load match_ids[] referenced in cert.evidence_matches
  2. For each match_id: verify match_status = COMPLETED in match_results
  3. Verify match played by certified user_token (not a substitute)
  4. Verify match was not flagged by AG-FPDM-001 CLASS_09 (rating farming)
  5. Verify match_score met threshold required for cert level
  6. Verify match was NOT in a flagged-for-audit state at cert issuance time
Inputs:
  cert.evidence_matches[]       (match_id[])
  match_results table           (read replica)
  phantom_profiles signal_class_09 results
  match_audit_flags[]
Output:
  evidence_matches_valid        (BOOL)
  invalid_match_ids[]           (match_id[])
  farming_contaminated_matches  (INT count)
  match_evidence_score          (0.0–1.0)
FAIL condition:  > 20% of evidence matches invalid or farming-contaminated → INAUTHENTIC
Dojo Binding:    SECTION T9 — Collusion & Manipulation Detection Lock
```

---

### LAYER 06 — SCENARIO DIFFICULTY CALIBRATION CHECK
```
Purpose:      Verify the scenarios used in cert-qualifying matches were calibrated, not deprecated
Method:
  1. Load scenario_ids[] from cert.evidence_matches
  2. For each scenario: check scenario_status at cert.issued_at
     (ACTIVE | UNDER_REVIEW | DEPRECATED | RETIRED)
  3. Verify difficulty_label was data-derived (not author-declared) at issuance time
  4. Check scenario abandonment_rate and pass_rate at time of use
  5. Scenarios with pass_rate > 95% (too easy) flagged for validity concern
Inputs:
  scenario_ids[]                (from evidence matches)
  scenario_difficulty_history[]
  scenario_calibration_records[]
Output:
  all_scenarios_active          (BOOL)
  deprecated_scenario_count     (INT)
  difficulty_validity_flag      (BOOL)
  scenario_calibration_score    (0.0–1.0)
FAIL condition:  deprecated_scenario_count > 0 in cert evidence → SUSPECT
Dojo Binding:    SECTION T4 — Scenario Difficulty Calibration Lock
```

---

### LAYER 07 — INTER-RATER RELIABILITY CHECK
```
Purpose:      Verify scoring had acceptable inter-rater agreement at time of cert award
Method:
  1. Load peer_scores[], mentor_score, self_score for cert qualifying matches
  2. Compute scorer variance: Var(scores)
  3. Compare against variance_threshold for cert level (from T2 config)
  4. Check confidence_score_per_match_result
  5. Low-confidence scores that triggered cert → flag for mentor board review check
  6. Check mentor calibration_score at time of signing (T3 check)
Inputs:
  match_scoring_records[]       (peer + mentor + self)
  scoring_variance_thresholds[] (from Scoring Governance Lock)
  mentor_calibration_scores[]   (from T3 calibration records)
Output:
  variance_within_threshold     (BOOL)
  low_confidence_match_count    (INT)
  mentor_calibration_valid      (BOOL)
  rater_reliability_score       (0.0–1.0)
FAIL condition:  mentor_calibration_valid = FALSE at time of cert issuance → SUSPECT
Dojo Binding:    SECTION T2 — Scoring Validity & Reliability Lock
                 SECTION T3 — Rater Calibration Lock
```

---

### LAYER 08 — TEMPORAL CONSISTENCY CHECK ★ BLOCKING
```
Purpose:      Detect impossible or suspicious timestamps in certificate lifecycle
Method:
  1. cert.issued_at MUST be > last_required_match.completed_at
  2. cert.issued_at MUST be > mentor_sign_off.created_at
  3. cert.issued_at MUST NOT predate user account creation date
  4. cert.issued_at MUST NOT be in the future (clock skew tolerance: ± 60 seconds)
  5. For external certs: compare claimed_issued_at against institution founding date
  6. Check cert version stamp against platform version active at issued_at
Inputs:
  cert.issued_at
  match_results.completed_at (latest qualifying match)
  mentor_commands.created_at (sign-off record)
  user.account_created_at
  institution.verified_founding_date (external certs)
Output:
  temporal_valid                (BOOL)
  impossible_timestamp_flags[]  (STRING[])
  temporal_consistency_score    (0.0–1.0)
FAIL condition:  ANY impossible timestamp detected → INAUTHENTIC
```

---

### LAYER 09 — DUPLICATE & CLONE DETECTION ★ BLOCKING
```
Purpose:      Detect certificates cloned across profiles or issued multiple times to same profile
Method:
  1. Compute canonical cert fingerprint: SHA-256(cert_type + level + user_token + issued_at + rubric_version)
  2. Search OpenSearch cert fingerprint index for matches
  3. If fingerprint found on different user_token → CLONE DETECTED
  4. If same user_token holds same cert_type + level twice with same rubric version → DUPLICATE
  5. Check cert_id uniqueness in cert_audit_log
Inputs:
  cert_fingerprint              (computed canonical hash)
  opensearch_cert_fingerprint_index
  cert_audit_log
Output:
  is_duplicate                  (BOOL)
  is_clone                      (BOOL)
  matching_cert_ids[]           (cert_id[])
  clone_user_tokens[]           (user_token[])
  uniqueness_score              (0.0–1.0)
FAIL condition:  is_clone = TRUE → INAUTHENTIC (immediate)
                 is_duplicate = TRUE → SUSPECT (human review)
```

---

### LAYER 10 — COLLUSION CONTAMINATION CHECK ★ BLOCKING
```
Purpose:      Verify certificate was not earned through colluded or farmed matches
Method:
  1. Load AG-FPDM-001 CLASS_09 results for user_token (rating farming signal)
  2. Check if any cert.evidence_matches appear in suspicious_match_ids from CLASS_09
  3. Check if user_token appears in farming_pair_ids from CLASS_09
  4. Check PHANTOM SHIELD composite_risk_score at time of cert issuance
  5. If composite_risk_score > 0.60 at issuance: cert is retroactively SUSPECT
Inputs:
  phantom_profile.signal_class_09_results
  phantom_profile.composite_risk_score (at cert.issued_at snapshot)
  cert.evidence_matches[]
Output:
  collusion_contaminated        (BOOL)
  contaminated_match_count      (INT)
  risk_score_at_issuance        (FLOAT)
  collusion_contamination_score (0.0–1.0)
FAIL condition:  collusion_contaminated = TRUE AND contaminated_match_count > 1 → INAUTHENTIC
Dojo Binding:    SECTION T9 — Collusion & Manipulation Detection Lock
Module Binding:  AG-FPDM-001 PHANTOM SHIELD — CLASS_09 signal
```

---

### LAYER 11 — EXTERNAL CERTIFICATE AI ANALYSIS (CERT_TYPE_08 only)
```
Purpose:      Classify externally submitted third-party certificates for authenticity signals
Method:
  1. OCR extraction from PDF/image cert file (Tesseract + PaddleOCR)
  2. Extract: issuing_institution, cert_title, issued_date, holder_name, cert_id
  3. Claude API — institutional legitimacy classifier:
       - Is institution name recognizable and consistent with claimed domain?
       - Are formatting artifacts consistent with genuine institutional output?
       - Is cert_title plausible for claimed level?
       - Is language/grammar consistent with professional institution?
  4. Cross-reference institution name against verified institution registry
  5. QR/barcode extraction: attempt auto-verification against issuer URL
  6. Image metadata analysis: creation software, digital artifacts, font consistency
  7. Duplicate check: has this cert_id been submitted by a different user?
AI Tool:        Claude API — institutional authenticity narrative classification
OCR Tools:      Tesseract 5 + PaddleOCR (ensemble)
Inputs:
  cert_file                     (PDF or image, staged in MinIO)
  verified_institution_registry
  external_cert_submission_log
Output:
  institution_recognized        (BOOL)
  ocr_extracted_fields          (JSON)
  ai_authenticity_probability   (FLOAT 0.0–1.0)
  barcode_verified              (BOOL | NULL)
  duplicate_submission_flag     (BOOL)
  external_cert_score           (0.0–1.0)
NOTE: External certs can only reach PROBABLE_AUTHENTIC maximum without issuer API verification.
      AUTHENTIC verdict for external certs requires issuer API integration.
```

---

### LAYER 12 — INSTITUTIONAL VERIFICATION CHECK (CERT_TYPE_03/05/06/08)
```
Purpose:      Verify institution listed on certificate is a legitimate, verified entity
Method:
  1. Load institution_id from cert.issuer_institution_id
  2. Verify institution exists in verified_institution_registry
  3. Verify institution.verified_at < cert.issued_at
  4. For external certs: fuzzy match institution name to registry (similarity ≥ 0.85)
  5. Check AG-FPDM-001 CLASS_11 results for institution (Institute Verification Signal)
  6. Verify institution was ACTIVE (not suspended) at time of cert issuance
Inputs:
  cert.issuer_institution_id
  verified_institution_registry
  phantom_profiles.signal_class_11 (institute verification signal)
Output:
  institution_verified          (BOOL)
  institution_active_at_issue   (BOOL)
  institutional_trust_score     (0.0–1.0)
FAIL condition:  institution_verified = FALSE → SUSPECT (CERT_TYPE_03/05/06)
                                             → INAUTHENTIC (CERT_TYPE_08 external)
Module Binding:  AG-FPDM-001 PHANTOM SHIELD — CLASS_11 signal
```

---

### LAYER 13 — BEHAVIORAL CONSISTENCY AUDIT
```
Purpose:      Detect pattern inconsistencies between cert-holder behavior and earned credentials
Method:
  1. Load user learning_velocity (from AG-EPDM-001 CLASS_11 Learning Velocity Pattern)
  2. Compare cert acquisition speed vs population norm for same cert type + level
  3. Compute expected_time_to_cert based on historical cohort data
  4. Flag if actual_time_to_cert < expected_time_to_cert × 0.40 (suspiciously fast)
  5. Cross-check with Dojo drill engagement rate and match replay quality signals
  6. Check if user-completed drills align with cert skill domain
Inputs:
  user.cert_acquisition_history[]
  population_norm_cert_times[]  (by cert_type + level)
  drill_engagement_records[]
  excel_pattern_results (AG-EPDM-001 CLASS_11)
Output:
  acquisition_speed_ratio       (FLOAT — actual/expected)
  speed_anomaly_flag            (BOOL)
  drill_domain_alignment_score  (FLOAT)
  behavioral_consistency_score  (0.0–1.0)
FAIL condition:  acquisition_speed_ratio < 0.30 (acquired in < 30% of expected time)
                 AND drill_domain_alignment_score < 0.50 → SUSPECT
Module Binding:  AG-EPDM-001 Excel Pattern Detection — CLASS_11 signal
```

---

### LAYER 14 — ECONOMIC INTEGRITY CHECK
```
Purpose:      Detect certs obtained through financial manipulation or billing fraud
Method:
  1. Check if any cert fees were charged then refunded after cert issuance
  2. Verify certification fees were paid and not reversed for CERT_TYPE_01/02/06
  3. Check if user_token appears in AG-FPDM-001 CLASS_13 (Economic Abuse Signal)
  4. Verify tournament entry fees valid for CERT_TYPE_04 (championship medals)
  5. Check for free-trial cycling that enabled certification pathway
Inputs:
  billing_ledger (read-only — fee payment records only, no raw payment data)
  phantom_profiles.signal_class_13 (economic abuse signal)
Output:
  fee_integrity_valid           (BOOL)
  post_cert_refund_detected     (BOOL)
  economic_abuse_flag           (BOOL)
  economic_integrity_score      (0.0–1.0)
FAIL condition:  post_cert_refund_detected = TRUE → SUSPECT (retroactive review)
                 economic_abuse_flag = TRUE → SUSPECT
Module Binding:  AG-FPDM-001 PHANTOM SHIELD — CLASS_13 signal
Dojo Binding:    SECTION T15 — Economic Abuse Controls Lock
```

---

### LAYER 15 — BLOCKCHAIN ANCHOR VERIFICATION
```
Purpose:      Verify certificate's blockchain anchor record matches on-chain state
Method:
  1. Load cert.blockchain_tx_hash from cert_authenticity_records
  2. Query Polygon PoS via Web3.py for tx_hash status
  3. Retrieve on-chain payload: cert_fingerprint, issued_at, platform_signature
  4. Compare on-chain cert_fingerprint to LAYER 01 computed hash
  5. Verify block_confirmation_count ≥ minimum_confirmations (12 blocks)
  6. For certs pre-dating blockchain anchor: verify IPFS CID instead
  7. If no on-chain record exists (cert should have one): flag as MISSING_ANCHOR
Inputs:
  cert.blockchain_tx_hash       (if exists)
  cert.ipfs_cid                 (fallback)
  polygon_rpc_endpoint          (from secrets manager)
  minimum_confirmations: 12
Output:
  anchor_found                  (BOOL)
  on_chain_hash_match           (BOOL)
  block_confirmations           (INT)
  anchor_verification_score     (0.0–1.0)
  missing_anchor_flag           (BOOL)
FAIL condition:  anchor_found = TRUE AND on_chain_hash_match = FALSE → INAUTHENTIC
                 missing_anchor_flag = TRUE (mandatory anchor types) → SUSPECT
Applies to:      CERT_TYPE_01, 02, 04, 05, 06 (mandatory blockchain)
```

---

# 🔒 SECTION D — VAULT SEAL PROCESSING PIPELINE (FROZEN)

```
[Classification Trigger]
  ↓  (triggers: cert issuance | external submission | scheduled audit | manual request)
[Certificate Tokenizer] — PII masked; cert_holder → user_token only
  ↓
[Cert Type Router] — routes to applicable layer subset per cert type
  ↓
[Layer Executor — Sequential]
  ↓
  [LAYER 01 — Cryptographic Hash]         ← BLOCKING
  ↓ (pass)
  [LAYER 02 — Issuance Authority]         ← BLOCKING
  ↓ (pass)
  [LAYER 03 — Belt Progression]           ← BLOCKING (CERT_TYPE_01 only)
  ↓ (pass)
  [LAYER 04 — Rubric Version]             ← BLOCKING
  ↓ (pass)
  [LAYER 05 — Match Evidence]             ← BLOCKING (01/02/04 only)
  ↓ (pass)
  [LAYER 06 — Scenario Calibration]       ← ADVISORY
  ↓
  [LAYER 07 — Inter-Rater Reliability]    ← ADVISORY
  ↓
  [LAYER 08 — Temporal Consistency]       ← BLOCKING
  ↓ (pass)
  [LAYER 09 — Duplicate & Clone]          ← BLOCKING
  ↓ (pass)
  [LAYER 10 — Collusion Contamination]    ← BLOCKING
  ↓ (pass)
  [LAYER 11 — External AI Analysis]       ← CERT_TYPE_08 only
  ↓
  [LAYER 12 — Institutional Verification] ← BLOCKING (external) / ADVISORY (internal)
  ↓
  [LAYER 13 — Behavioral Consistency]     ← ADVISORY
  ↓
  [LAYER 14 — Economic Integrity]         ← ADVISORY
  ↓
  [LAYER 15 — Blockchain Anchor]          ← BLOCKING (mandatory anchor types)
  ↓
[Authenticity Score Aggregator] — weighted layer fusion (Section F)
  ↓
[Verdict Classifier] — assigns AUTHENTIC | PROBABLE_AUTHENTIC | SUSPECT | INAUTHENTIC
  ↓
[Seal Writer] — writes CertAuthenticityRecord + CertSealLog
  ↓
[Blockchain Anchor Dispatcher] — queues new/confirmed certs for on-chain anchor
  ↓
[Event Publisher] — Redis Streams: cert_authenticated | cert_suspect | cert_inauthentic
  ↓
[Visibility Gate Notifier] — Hiring Marketplace | Public Portal | Trust Score modifier
  ↓
[Alert Dispatcher] — Governance Board (INAUTHENTIC) | Trust Admin (SUSPECT)
  ↓
[Revocation Queue Writer] — INAUTHENTIC certs queued for human-governed revocation
```

BLOCKING layer FAIL → immediate INAUTHENTIC verdict → skip remaining layers.
ADVISORY layer FAIL → contributes to score only → pipeline continues.
Any pipeline step failure → STOP → REPORT → NO CLASSIFICATION STORED.

---

# 🔒 SECTION E — DATA MODEL FREEZE

## E1. Primary Entities (Non-renameable)

```
CertAuthenticityRecord     — master record per classification run
LayerResult                — per-layer result for each run
CertSealLog                — immutable seal event log (every step)
BlockchainAnchorRecord     — on-chain anchor mapping
RevocationQueueItem        — pending human-action revocation queue
CertPublicVerification     — public-facing verification record (no PII)
```

## E2. CertAuthenticityRecord Schema

```sql
CREATE TABLE cert_authenticity_records (
    record_id               UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    cert_id                 UUID NOT NULL,              -- FK to source cert system
    cert_type               TEXT NOT NULL,              -- CERT_TYPE_01 through 09
    user_token              TEXT NOT NULL,              -- tokenized, never raw user_id
    tenant_id               UUID NOT NULL,
    classification_run_at   TIMESTAMPTZ DEFAULT NOW(),
    triggered_by            TEXT NOT NULL,              -- ISSUANCE|SUBMISSION|SCHEDULED|MANUAL
    layers_applied          TEXT[] NOT NULL,            -- which layers ran
    layers_blocked          TEXT[],                     -- which layers returned BLOCKING FAIL
    authenticity_score      NUMERIC(4,3) NOT NULL,      -- 0.000–1.000
    verdict                 TEXT NOT NULL,              -- AUTHENTIC|PROBABLE_AUTHENTIC|SUSPECT|INAUTHENTIC
    blocking_fail_layer     TEXT,                       -- NULL if no blocking fail
    reviewed                BOOLEAN DEFAULT FALSE,
    reviewed_by             UUID,
    reviewed_at             TIMESTAMPTZ,
    final_verdict           TEXT,                       -- post-human-review
    revocation_queued       BOOLEAN DEFAULT FALSE,
    run_duration_ms         INT
);
```

## E3. LayerResult Schema

```sql
CREATE TABLE layer_results (
    layer_result_id         UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    record_id               UUID NOT NULL REFERENCES cert_authenticity_records(record_id),
    layer_code              TEXT NOT NULL,              -- LAYER_01 through LAYER_15
    layer_status            TEXT NOT NULL,              -- PASS|FAIL|SKIP|NOT_APPLICABLE
    layer_score             NUMERIC(4,3),               -- 0.000–1.000
    fail_type               TEXT,                       -- BLOCKING|ADVISORY|NULL
    evidence_payload        JSONB NOT NULL,             -- no raw PII
    created_at              TIMESTAMPTZ DEFAULT NOW()
    -- NO UPDATE PERMITTED
    -- NO DELETE PERMITTED
);
```

## E4. CertSealLog Schema

```sql
CREATE TABLE cert_seal_log (
    log_id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    record_id               UUID NOT NULL,
    cert_id                 UUID NOT NULL,
    pipeline_step           TEXT NOT NULL,
    step_status             TEXT NOT NULL,              -- SUCCESS|FAILURE|SKIP
    step_payload            JSONB,
    actor_system            TEXT NOT NULL DEFAULT 'VAULT_SEAL',
    created_at              TIMESTAMPTZ DEFAULT NOW()
    -- IMMUTABLE: no UPDATE, no DELETE ever
);
```

## E5. BlockchainAnchorRecord Schema

```sql
CREATE TABLE blockchain_anchor_records (
    anchor_id               UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    cert_id                 UUID NOT NULL,
    cert_type               TEXT NOT NULL,
    cert_fingerprint        TEXT NOT NULL,              -- canonical SHA-256 hash
    blockchain_network      TEXT NOT NULL DEFAULT 'polygon-pos',
    tx_hash                 TEXT,                       -- NULL until mined
    block_number            BIGINT,
    block_confirmations     INT DEFAULT 0,
    ipfs_cid                TEXT,                       -- fallback anchor
    anchor_status           TEXT NOT NULL,              -- PENDING|CONFIRMED|FAILED
    anchored_at             TIMESTAMPTZ,
    created_at              TIMESTAMPTZ DEFAULT NOW()
    -- tx_hash and cert_fingerprint are immutable once set
);
```

## E6. CertPublicVerification Schema

```sql
CREATE TABLE cert_public_verifications (
    verification_token      TEXT PRIMARY KEY,           -- short opaque token (not cert_id)
    cert_type               TEXT NOT NULL,
    cert_level              TEXT NOT NULL,              -- belt level, skill name, etc.
    issuer_name             TEXT NOT NULL,              -- institution display name
    issued_date             DATE NOT NULL,              -- date only, no time
    verdict                 TEXT NOT NULL,              -- AUTHENTIC|PROBABLE_AUTHENTIC only
                                                        -- SUSPECT/INAUTHENTIC never shown publicly
    blockchain_tx_hash      TEXT,
    verified_as_of          TIMESTAMPTZ NOT NULL,
    expires_cache_at        TIMESTAMPTZ NOT NULL        -- public cache TTL: 24 hours
    -- No user_token, no user name, no email ever in this table
);
```

Fields may extend — never mutate.

---

# 🔒 SECTION F — AUTHENTICITY SCORE ENGINE (FROZEN)

## F1. Layer Weights (v1.0)

```
Layer       Name                            Weight   Type
────────────────────────────────────────────────────────────────
LAYER_01    Cryptographic Hash              0.15     BLOCKING
LAYER_02    Issuance Authority              0.14     BLOCKING
LAYER_03    Belt Progression Integrity      0.10     BLOCKING (CT_01 only)
LAYER_04    Rubric Version Consistency      0.08     BLOCKING
LAYER_05    Match Evidence Cross-Reference  0.10     BLOCKING (CT_01/02/04)
LAYER_06    Scenario Difficulty Calibration 0.04     ADVISORY
LAYER_07    Inter-Rater Reliability         0.05     ADVISORY
LAYER_08    Temporal Consistency            0.10     BLOCKING
LAYER_09    Duplicate & Clone Detection     0.08     BLOCKING
LAYER_10    Collusion Contamination         0.07     BLOCKING
LAYER_11    External AI Analysis            0.05     ADVISORY (CT_08 only)
LAYER_12    Institutional Verification      0.06     BLOCKING/ADVISORY
LAYER_13    Behavioral Consistency          0.04     ADVISORY
LAYER_14    Economic Integrity              0.03     ADVISORY
LAYER_15    Blockchain Anchor               0.06     BLOCKING (mandatory types)
────────────────────────────────────────────────────────────────
            Weights adjusted for N/A layers — sum always = 1.00
```

## F2. Score Aggregation Formula

```
authenticity_score = Σ (layer_score[i] × adjusted_weight[i]) for applicable layers

adjusted_weight[i] = weight[i] / Σ(weight[j] for applicable layers j)
(normalizes to 1.00 when some layers are N/A for cert type)

HARD FAIL OVERRIDE:
If ANY blocking layer returns FAIL:
  → authenticity_score = 0.00 (floor override)
  → verdict = INAUTHENTIC (immediate, regardless of other layers)

CONFIDENCE BOOST:
If LAYER_01 (hash), LAYER_02 (authority), LAYER_15 (blockchain) all PASS:
  → authenticity_score += 0.05 (cryptographic triple-confirmation bonus)
  → Rationale: cryptographic + authority + on-chain = highest confidence triad
```

## F3. Score Modifiers

```
+0.08   — Certificate has valid Ed25519 platform signature (signed, not just hashed)
+0.06   — Blockchain anchor confirmed with ≥ 50 block confirmations
+0.05   — Zero PHANTOM SHIELD flags ever on this user_token
+0.04   — Issuer has > 100 successfully verified certificates (trusted issuer history)
-0.06   — User_token is currently TIER 3+ in PHANTOM SHIELD
-0.08   — Any match in evidence set was flagged by CLASS_09 (farming signal)
-0.10   — Same cert level already exists for this user_token (pre-duplicate context)
-0.15   — External cert from unregistered institution (CERT_TYPE_08)
```

---

# 🔒 SECTION G — VERDICT CLASSIFICATION SYSTEM (FROZEN)

## G1. Verdict Definitions

```
AUTHENTIC           (0.85–1.00)
  Meaning:    Certificate passes all applicable blocking layers and scores highly
              across advisory layers. Cryptographically verifiable. Blockchain
              anchored and confirmed.
  Visibility: Green VERIFIED AUTHENTIC badge on public profile + Public Portal
  Action:     Immediate blockchain anchor (if not already anchored)
              Hiring marketplace visibility: FULL
              Trust Score: +10 points
              Certificate sealed in VAULT SEAL permanently

PROBABLE_AUTHENTIC  (0.65–0.84)
  Meaning:    Certificate passes all blocking layers but has advisory layer
              concerns (e.g., slightly fast acquisition, minor behavioral
              inconsistency). No cryptographic failure.
  Visibility: Blue VERIFIED badge on public profile (no "AUTHENTIC" label)
  Action:     Scheduled re-evaluation in 30 days
              Hiring marketplace visibility: FULL (with advisory note to recruiter)
              Trust Score: +5 points

SUSPECT             (0.35–0.64)
  Meaning:    Certificate has no blocking layer fail but advisory layers raise
              significant concerns, OR one borderline blocking layer issue
              caught by human-review threshold logic.
  Visibility: Amber PENDING REVIEW badge (hidden from public portal)
  Action:     Trust admin review within 5 business days (MANDATORY)
              Hiring marketplace visibility: HIDDEN until review
              Trust Score: -8 points (temporary, restores if CLEARED)
              Certificate holder notified with appeal rights

INAUTHENTIC         (0.00–0.34 OR any BLOCKING FAIL)
  Meaning:    Certificate fails one or more blocking layers, OR score is below
              minimum threshold. May indicate fabrication, clone, backdating,
              farming contamination, or unauthorized issuance.
  Visibility: Certificate hidden from all public surfaces
  Action:     Governance Board alerted within 2 hours (MANDATORY)
              Hiring marketplace visibility: BLOCKED
              Trust Score: -25 points
              Revocation queue entry created (human-governed revocation)
              PHANTOM SHIELD notified (feeds composite risk score)
              All matching certificates from same user_token: re-classification triggered
              Certificate holder notified with appeal rights (T16 Appeals System)
```

## G2. Verdict Transition Rules

```
SUSPECT → AUTHENTIC:        Trust admin review + evidence validates cert
SUSPECT → INAUTHENTIC:      Trust admin review + evidence condemns cert
INAUTHENTIC → SUSPECT:      Governance Board finds new exculpatory evidence
INAUTHENTIC → AUTHENTIC:    Governance Board full exoneration (rare — requires unanimous board)
AUTHENTIC → SUSPECT:        New evidence surfaces post-seal (triggers re-classification)
AUTHENTIC → INAUTHENTIC:    Only via Governance Board order (not automatic)
```

## G3. Appeal Rights (SECTION T16 Binding)

```
SUSPECT verdict:    Certificate holder may appeal within 14 days
INAUTHENTIC:        Certificate holder may appeal within 7 days
                    Employer/Recruiter who relied on cert: may request audit within 30 days
All appeals:        Routed to Governance Board via T16 Appeals & Governance Board Lock
Appeal decisions:   Logged and versioned (immutable in PhantomAuditLog)
```

---

# 🔒 SECTION H — PUBLIC VERIFICATION PORTAL (LOCKED)

## H1. Portal Design

The Public Verification Portal is the most critical external-facing surface of VAULT SEAL. It allows any third party — recruiters, employers, parents, institutions — to verify an Ecoskiller certificate without creating an account.

```
URL Pattern:    https://verify.ecoskiller.com/{verification_token}
Auth Required:  NONE (public, unauthenticated)
SEO:            INDEXED for institution + cert type (not individual)
Rate Limit:     100 verification requests / minute per IP
Cache TTL:      24 hours per verification_token (Redis)
Response time:  p95 < 500ms (cached) | p95 < 2 seconds (uncached)
```

## H2. Portal Data Display Rules

```
DISPLAYED:
  ✔ Certificate type and level (e.g., "Dojo Black Belt — Sales Negotiation")
  ✔ Issuing institution display name
  ✔ Issue date (date only — no timestamp)
  ✔ Verdict badge: AUTHENTIC | PROBABLE_AUTHENTIC
  ✔ Blockchain transaction hash (link to Polygon explorer)
  ✔ Verification timestamp ("Verified as of: [date]")
  ✔ VAULT SEAL verification badge

NEVER DISPLAYED:
  ✗ Certificate holder name (privacy by default)
  ✗ Certificate holder email or any contact
  ✗ user_token or internal IDs
  ✗ SUSPECT or INAUTHENTIC status (these are not publicly surfaced)
  ✗ Internal scoring or layer results
  ✗ Other certificates held by same user
  ✗ Match IDs or evidence references
```

## H3. Certificate holder controls their name visibility

```
visibility_setting: ANONYMOUS | DISPLAY_NAME | FULL_NAME
Default:            ANONYMOUS
Setting location:   Flutter App → Profile Settings → Certificate Privacy
Portal renders:     Based on holder's preference at verification time
```

## H4. QR Code Integration

```
Every AUTHENTIC / PROBABLE_AUTHENTIC certificate generates:
  QR_CODE → encodes verification_token URL
  Embedded in: certificate PDF export
              Flutter certificate card widget
              React public profile portfolio page

QR scan on mobile:  Opens verify.ecoskiller.com/{verification_token}
QR scan result:     Live portal page (always fresh from cache)
Offline PDF:        QR code remains valid; portal verifies live
```

---

# 🔒 SECTION I — BLOCKCHAIN ANCHORING SYSTEM (LOCKED)

## I1. Anchoring Architecture

```
Network:            Polygon PoS (primary)
Fallback:           IPFS CIDv1 (content-addressed, no gas cost)
Batch interval:     Every 15 minutes (normal operations)
Emergency single:   Immediately (TIER 5 events, urgent revocations)
Gas strategy:       EIP-1559 dynamic fee — maxPriorityFeePerGas = 30 gwei cap
Wallet:             Platform-controlled multisig (3-of-5 governance keys)
Smart contract:     EcoskillerCertRegistry (audited, non-upgradeable)
```

## I2. On-Chain Payload (Minimal — Privacy First)

```solidity
// What is stored ON CHAIN (no PII, no user data):
struct CertAnchor {
    bytes32 cert_fingerprint;   // SHA-256 of canonical cert JSON
    uint256 issued_at;          // Unix timestamp
    bytes32 platform_sig_hash;  // Hash of Ed25519 platform signature
    uint8   cert_type_code;     // CERT_TYPE_01–09 as integer
}
// What is NOT stored on chain: user name, user ID, email, match IDs, scores
```

## I3. Anchoring Rules by Cert Type

```
CERT_TYPE_01 (Dojo Belt):           MANDATORY — anchor within 1 hour of AUTHENTIC verdict
CERT_TYPE_02 (Skill Cert):          MANDATORY — anchor within 1 hour
CERT_TYPE_04 (Championship Medal):  MANDATORY — anchor within 1 hour
CERT_TYPE_05 (Trainer Legacy):      MANDATORY — anchor immediately (permanent record)
CERT_TYPE_06 (Mentor Cert):         MANDATORY — anchor within 1 hour
CERT_TYPE_03 (Course Completion):   OPTIONAL (enterprise plan: MANDATORY)
CERT_TYPE_07 (Intelligence):        OPTIONAL
CERT_TYPE_08 (External):            FORBIDDEN until AUTHENTIC verdict
CERT_TYPE_09 (Enterprise Proof):    CONDITIONAL — TIER 1 confidence (AG-EPDM-001) only
```

## I4. Revocation Anchoring

```
When Governance Board orders revocation:
  → Revocation event anchored on-chain: cert_fingerprint + revoked_at + revocation_reason_hash
  → On-chain revocation is permanent and publicly visible
  → Public portal immediately shows: "THIS CERTIFICATE HAS BEEN REVOKED"
  → blockchain_anchor_records.anchor_status → REVOKED
```

---

# 🔒 SECTION J — DETECTION TRIGGER SYSTEM (FROZEN)

```
TRIGGER_01:   CERT_ISSUANCE
              → Fires when Belt Engine or Skill Engine issues any cert
              → Layers: 01, 02, 03 (CT_01), 04, 05, 07, 08, 09, 10, 14, 15
              → Path: BLOCKING (cert display held until AUTHENTIC/PROBABLE_AUTHENTIC)
              → SLA: < 30 seconds

TRIGGER_02:   EXTERNAL_CERT_SUBMISSION
              → Fires when user submits a third-party certificate
              → Layers: 01 (hash store), 08, 09, 11, 12, 13, 14
              → Path: ASYNC (cert marked PENDING during analysis)
              → SLA: < 5 minutes

TRIGGER_03:   SCHEDULED_WEEKLY_AUDIT
              → Full re-classification of all AUTHENTIC certs older than 30 days
              → All 15 layers (applicable per cert type)
              → Path: BATCH (background, no SLA — user not blocked)
              → Priority: SUSPECT → PROBABLE_AUTHENTIC → AUTHENTIC

TRIGGER_04:   PHANTOM_SHIELD_ESCALATION
              → Fires when AG-FPDM-001 escalates a user to TIER 4+
              → Triggers re-classification of ALL certs held by user_token
              → Layers: 05, 07, 09, 10, 13, 14
              → Path: ASYNC (cert status updated within 2 hours)

TRIGGER_05:   MATCH_AUDIT_FLAG
              → Fires when a match_id is flagged post-hoc
              → Triggers re-classification of any cert using that match_id as evidence
              → Layers: 05, 10
              → Path: ASYNC (< 1 hour)

TRIGGER_06:   MANUAL_REVIEW_REQUEST
              → Governance Board or Trust Admin request
              → All 15 applicable layers, fresh run (no cache)
              → Path: BLOCKING (immediate priority queue)
              → SLA: < 10 minutes

TRIGGER_07:   HIRING_MARKETPLACE_ACTIVATION
              → Fires when profile activates for recruiter visibility
              → Confirms all certs are AUTHENTIC or PROBABLE_AUTHENTIC
              → Layers: 01, 02, 08, 09, 15
              → Path: BLOCKING gate on marketplace activation
              → SLA: < 15 seconds

TRIGGER_08:   RUBRIC_VERSION_CHANGE
              → Fires when Content Governance publishes new rubric version
              → Triggers LAYER_04 re-check for all affected cert types
              → Flags certs requiring re-certification
              → Path: BATCH (no SLA — not user-blocking)

TRIGGER_09:   EMPLOYER_VERIFICATION_REQUEST
              → Fires when enterprise employer requests cert verification via API
              → Runs fresh classification (no cache) + generates verification report
              → All applicable layers
              → Path: BLOCKING (employer-facing, SLA: < 60 seconds)
```

---

# 🔒 SECTION K — API SURFACE LOCK (FROZEN)

All endpoints below are frozen. Extension by version bump only.

```
POST   /api/v1/trust/vault/classify
         Body:   { cert_id, cert_type, trigger_type }
         Auth:   trust_admin | system_service
         Returns: { record_id, verdict, authenticity_score, layers_applied[] }

GET    /api/v1/trust/vault/{record_id}
         Auth:   trust_admin | governance_board
         Returns: full CertAuthenticityRecord + all 15 LayerResults

GET    /api/v1/trust/vault/cert/{cert_id}/history
         Auth:   trust_admin | governance_board
         Returns: all classification runs for cert_id, paginated

GET    /api/v1/trust/vault/cert/{cert_id}/latest
         Auth:   trust_admin | issuer_service | cert_holder (own cert only)
         Returns: latest verdict + score (no layer detail for cert_holder)

POST   /api/v1/trust/vault/{record_id}/review
         Body:   { verdict, reasoning, action }
         Auth:   trust_admin (SUSPECT) | governance_board (INAUTHENTIC)
         Returns: updated CertAuthenticityRecord

GET    /api/v1/trust/vault/{record_id}/audit
         Auth:   governance_board only
         Returns: immutable CertSealLog for run

POST   /api/v1/trust/vault/revoke
         Body:   { cert_id, reason, governance_order_id }
         Auth:   governance_board only
         Returns: RevocationQueueItem + triggers blockchain revocation anchor

GET    /api/v1/trust/vault/blockchain/{cert_id}
         Auth:   trust_admin | governance_board | cert_holder (own)
         Returns: BlockchainAnchorRecord + Polygon explorer link

GET    /public/verify/{verification_token}
         Auth:   NONE (public unauthenticated)
         Returns: CertPublicVerification (sanitized — no PII)
         Cache:  Redis 24 hours

POST   /api/v1/trust/vault/external/submit
         Body:   { cert_file (base64), cert_type: CERT_TYPE_08, claimed_fields{} }
         Auth:   authenticated user (own submission only)
         Returns: { record_id, status: PENDING }

GET    /api/v1/trust/vault/employer/verify
         Body:   { verification_token, employer_token }
         Auth:   verified_employer
         Returns: detailed verification report (no PII) + classification evidence summary

GET    /api/v1/trust/vault/dashboard/summary
         Auth:   trust_admin | governance_board
         Returns: vault health metrics (aggregated, anonymized)
```

Rate limiting: 200 classification requests / minute per service token
Public portal: 100 verification requests / minute per IP
Employer verify: 500 requests / day per employer token

---

# 🔒 SECTION L — SECURITY HARDENING (VAULT SEAL-SPECIFIC)

## L1. Cryptographic Security

```
Certificate hash algorithm:   SHA-256 (primary) + SHA-3-256 (secondary, stored separately)
Platform signature algorithm: Ed25519 (fast, high-security, non-malleable)
Platform key storage:         AWS KMS / HashiCorp Vault (never in env plaintext)
Key rotation policy:          Annual (new key — old key retained for historical verification)
Blockchain wallet:            3-of-5 multisig (5 governance keyholders, 3 required to anchor)
Blockchain private keys:      Hardware Security Module (HSM) — never in software
```

## L2. PII Protection

```
user_id → user_token:         HMAC-SHA256 (same scheme as AG-FPDM-001)
Certificate holder name:      Stored in cert_authenticity_records ONLY as user_token
                               Name never enters VAULT SEAL tables
Public portal:                Name shown only if holder opts-in (Section H3)
External cert OCR output:     Holder name extracted then immediately hashed/dropped
Layer evidence payloads:      Never contain full names, emails, or exact locations
```

## L3. Access Controls

```
VAULT SEAL DB schema (vault_schema):    Isolated from Ecoskiller core schema
trust_admin:                            Read + review SUSPECT records
governance_board:                       Read all + review all + revoke
cert_holder:                            Read own latest verdict only (no layer detail)
verified_employer:                      Read public verification + verification report only
public (unauthenticated):               Public portal only — CertPublicVerification table only
No other role:                          ANY access to VAULT SEAL internal data
```

## L4. Immutability Enforcement

```
CertSealLog:                  DB trigger blocks UPDATE and DELETE
LayerResult:                  DB trigger blocks UPDATE and DELETE
BlockchainAnchorRecord:       cert_fingerprint and tx_hash fields are write-once
CertAuthenticityRecord:       UPDATE permitted ONLY on: reviewed, reviewed_by, reviewed_at,
                               final_verdict, revocation_queued fields
                               All other fields: immutable after insert
```

---

# 🔒 SECTION M — FLUTTER + REACT UI SURFACE LOCK

## M1. Flutter App — Certificate Surfaces (Required)

```
Certificate Vault Screen
  — User's own certificates listed with verdict badges
  — AUTHENTIC: green seal badge + blockchain tx link
  — PROBABLE_AUTHENTIC: blue badge
  — SUSPECT: amber "Under Review" badge (no detail shown to user)
  — Share certificate (generates QR code + deep link)

Certificate Detail Screen
  — Certificate level, issuer, issue date
  — Blockchain confirmation count + explorer link
  — Verify button (opens Public Portal)
  — Download PDF (with embedded QR code)

Trust Admin — Cert Review Screen
  — SUSPECT queue with SLA timer
  — Layer result cards (15 layers, pass/fail/NA)
  — Evidence payload viewer (no raw PII)
  — Verdict submission (mandatory reasoning)

Trust Admin — Revocation Queue Screen
  — INAUTHENTIC certs awaiting Governance Board action
  — Board order reference field
  — Revoke action (governance_board role only)

Public Verification Deeplink Handler
  — Opens verify.ecoskiller.com URL in in-app browser
  — Or renders native verification card if offline-capable QR
```

## M2. React Web — Certificate Surfaces (Required)

```
/verify/{verification_token}
  — Public Verification Portal (SSR, SEO-indexed for cert type + institution)
  — AUTHENTIC: full green seal + blockchain details
  — PROBABLE_AUTHENTIC: blue verified badge + note
  — SUSPECT/INAUTHENTIC: "Certificate status unavailable" (never reveals verdict)
  — Schema.org EducationalOccupationalCredential markup

/profile/{public_id}/certificates
  — Public portfolio page — lists AUTHENTIC certs only
  — Each cert links to /verify/{verification_token}
  — No PII beyond holder opt-in display name

/enterprise/vault
  — Enterprise Trust Portal — bulk cert audit table
  — Filter by: verdict | cert_type | date_range | issuer
  — Export: PDF report | CSV for compliance

/enterprise/vault/{record_id}
  — Detailed classification record (trust_admin / governance_board only)

/enterprise/vault/employer/verify
  — Employer-facing certificate verification form (enter verification_token)
  — Returns: structured verification report
  — Suitable for ATS record attachment
```

🚫 SUSPECT or INAUTHENTIC verdicts never rendered on any public-facing React page
🚫 Internal record IDs never exposed in public-facing URLs

---

# 🔒 SECTION N — INTEGRATION WITH ECOSKILLER TALENT OS

```
MODULE 1 — Identity System:
  → Blockchain Certificates (feature 21): VAULT SEAL generates + anchors these
  → Verified Identity (feature 7): AUTHENTIC cert status contributes to verified status
  → Certificates Vault (feature 18): Flutter vault screen = feature 18 implementation

MODULE 3 — Skill System (Dojo):
  → Skill Certifications (feature 14): CERT_TYPE_02 — VAULT SEAL governs authenticity
  → Skill Verification (feature 15): VAULT SEAL is the verification engine
  → Skill Reliability Score (feature 19): AUTHENTIC cert history boosts reliability score

MODULE 4 — Championship System:
  → Championship Certificates (feature 32): CERT_TYPE_04 — VAULT SEAL mandatory
  → Championship Badges (feature 33): tied to AUTHENTIC championship cert
  → AI Judging (feature 37): VAULT SEAL LAYER_05 cross-references AI judging output

MODULE 6 — Institute System:
  → Placement Tracking (feature 7): recruiters trust AUTHENTIC skill certs
  → Institute Achievement (feature 20): aggregate AUTHENTIC cert rate = institute metric

MODULE 7 — Recruiter System:
  → Candidate Reliability Scores (feature 12): AUTHENTIC cert history is key signal
  → Hiring Decisions must reference verified match data (T14): VAULT SEAL enforces this
  → Assessment Authenticity Seal (T14): VAULT SEAL IS the authenticity seal

MODULE 15 — Trust System:
  → Verified Certificates (feature 4): VAULT SEAL = the verification engine
  → Verified Skills (feature 5): skill certs + VAULT SEAL = verified skill
  → Trust Score (feature 9): AUTHENTIC cert = +10 | INAUTHENTIC = -25

MODULE 9 — Integration System:
  → Enterprise tool integration yields CERT_TYPE_09 (Enterprise Skill Proof)
  → VAULT SEAL LAYER 13 (behavioral consistency) uses AG-EPDM-001 CLASS_11 signals

MODULE 10 — Migration System:
  → All migrated certificates enter TRIGGER_02 (external submission) flow
  → Migrated certs held at PENDING for 14-day observation (per Migration Trust rules)
  → VAULT SEAL validates historical cert timestamps against migration source metadata
```

---

# 🔒 SECTION O — DOJO TRUST LOCK ALIGNMENT TABLE

| VAULT SEAL Layer / Rule | Dojo Section | Binding Description |
|---|---|---|
| LAYER_02 Issuance Authority | T7 — Mentor Training & Certification | Uncertified mentors cannot certify |
| LAYER_03 Belt Progression | G — Belt & Certification Lock | Auto-promotion forbidden |
| LAYER_04 Rubric Version | T17 — Belt Version Governance | Re-cert triggers on major rubric change |
| LAYER_04 Rubric Version | T8 — Content Governance Lock | No silent rubric change |
| LAYER_05 Match Evidence | T9 — Collusion Detection Lock | Flagged matches cannot count toward belts |
| LAYER_06 Scenario Calibration | T4 — Scenario Difficulty Calibration | Difficulty labels data-derived |
| LAYER_07 Inter-Rater Reliability | T2 — Scoring Validity & Reliability | Low confidence → mentor board review |
| LAYER_07 Mentor Calibration | T3 — Rater Calibration Lock | Drift detection mandatory |
| LAYER_09 Duplicate/Clone | T14 — Talent Marketplace Trust Lock | Hiring decisions reference verified data |
| LAYER_10 Collusion | T9 — Collusion Detection | Same binding as PHANTOM SHIELD CLASS_09 |
| LAYER_12 Institutional | T13 — Outcome Validation Lock | Belts must have predictive validity |
| LAYER_14 Economic | T15 — Economic Abuse Controls | Billing fraud = cert review |
| INAUTHENTIC verdict | T16 — Appeals & Governance Board | All decisions logged + appealable |
| AUTHENTIC verdict | T14 — Talent Marketplace Trust | Assessment authenticity seal = VAULT SEAL |
| Revocation + Blockchain | T17 — Belt Version Governance | Version stamp immutability enforced |

---

# 🔒 SECTION P — TEST GATE LOCK (VAULT SEAL-SPECIFIC)

```
Cryptographic Layer Tests:
  ✔ Valid hash → LAYER_01 PASS
  ✔ Tampered cert payload → LAYER_01 BLOCKING FAIL → INAUTHENTIC
  ✔ Valid Ed25519 signature → signature_valid = TRUE
  ✔ Corrupted signature bytes → signature_valid = FALSE

Issuance Authority Tests:
  ✔ Cert issued by certified mentor → LAYER_02 PASS
  ✔ Cert issued by uncertified mentor → LAYER_02 BLOCKING FAIL
  ✔ Mentor certification expired at issuance date → LAYER_02 BLOCKING FAIL

Belt Progression Tests:
  ✔ White → Yellow → Green → Black: valid progression → PASS
  ✔ White → Black with no intermediates, no override → BLOCKING FAIL
  ✔ Auto-promotion (no mentor entry) → auto_promotion_detected = TRUE → FAIL

Temporal Tests:
  ✔ cert.issued_at > match.completed_at → PASS
  ✔ cert.issued_at < match.completed_at → BLOCKING FAIL (impossible timestamp)
  ✔ cert.issued_at predates account creation → BLOCKING FAIL
  ✔ cert.issued_at is future date → BLOCKING FAIL

Duplicate/Clone Tests:
  ✔ Same fingerprint on different user_token → is_clone = TRUE → INAUTHENTIC
  ✔ Same cert twice on same user → is_duplicate = TRUE → SUSPECT
  ✔ Unique cert → uniqueness_score = 1.0

Collusion Contamination Tests:
  ✔ Evidence match in farming_pair_ids → collusion_contaminated = TRUE
  ✔ User PHANTOM SHIELD composite_risk > 0.60 at issuance → SUSPECT

Blockchain Tests:
  ✔ Valid tx_hash with ≥ 12 confirmations → LAYER_15 PASS
  ✔ tx_hash present but on-chain fingerprint mismatch → LAYER_15 BLOCKING FAIL
  ✔ Missing anchor on mandatory cert type → missing_anchor_flag = TRUE

External Cert Tests:
  ✔ PDF with valid institution → institution_recognized = TRUE
  ✔ PDF with unknown institution → SUSPECT minimum
  ✔ Cert image with GAN-detected tampering → low ai_authenticity_probability
  ✔ Duplicate cert_id submitted by second user → duplicate_submission_flag = TRUE

Score Engine Tests:
  ✔ All blocking layers PASS, all advisory layers PASS → score ≥ 0.85 → AUTHENTIC
  ✔ Any blocking layer FAIL → score = 0.00 → INAUTHENTIC
  ✔ Cryptographic triple-confirmation bonus applied correctly
  ✔ Score modifiers applied within 0.000–1.000 bounds

Public Portal Tests:
  ✔ AUTHENTIC cert → full green verification page
  ✔ SUSPECT cert → "status unavailable" (no verdict leakage)
  ✔ INAUTHENTIC cert → "status unavailable" (no verdict leakage)
  ✔ QR code → resolves to correct verification_token page

Immutability Tests:
  ✔ CertSealLog UPDATE → rejected at DB level
  ✔ LayerResult DELETE → rejected at DB level
  ✔ BlockchainAnchorRecord.cert_fingerprint → write-once enforced

PII Tests:
  ✔ user_id never appears in any vault_schema table
  ✔ Holder name never stored in LayerResult evidence
  ✔ Public portal: zero PII for ANONYMOUS setting
  ✔ OCR extracted name → immediately hashed/dropped before storage
```

---

# 🔒 SECTION Q — OBSERVABILITY LOCK (VAULT SEAL-SPECIFIC)

```
Required Metrics:
  - vault_classifications_total (counter, by cert_type, by verdict)
  - vault_classification_duration_seconds (histogram, by trigger_type)
  - blocking_layer_fail_rate (gauge, by layer_code)
  - authentic_cert_rate (gauge, by cert_type, by tenant)
  - inauthentic_cert_rate (gauge — key fraud signal)
  - suspect_review_queue_depth (gauge)
  - suspect_sla_breach_count (counter)
  - blockchain_anchor_success_rate (gauge)
  - blockchain_anchor_pending_count (gauge)
  - public_portal_requests_total (counter)
  - public_portal_response_time_seconds (histogram)
  - revocation_queue_depth (gauge)

Dashboards Required (Grafana):
  - VAULT SEAL Authenticity Overview (cert type × verdict distribution)
  - Blocking Layer Failure Heatmap (15 layers × time)
  - Blockchain Anchor Health Dashboard
  - Review Queue SLA Dashboard (SUSPECT: 5-day | INAUTHENTIC: 2-hour board alert)
  - Public Portal Traffic + Response Time
  - Weekly Certificate Authenticity Report (auto-generated PDF)
  - External Cert Submission Analysis

Alerts Required:
  - INAUTHENTIC cert detected → Governance Board PagerDuty (P1) within 2 hours
  - SUSPECT review SLA breach → Trust Admin Slack (P2)
  - Blockchain anchor failure rate > 5% → Infra PagerDuty (P2)
  - Missing anchor on mandatory cert type → P1 alert
  - CertSealLog write failure → P0 CRITICAL (data integrity)
  - Public portal p95 response > 1 second → Performance alert
  - Clone detected (LAYER_09) → Immediate Governance Board notification
```

---

# 🔒 SECTION R — CHANGE GOVERNANCE RULES

## Allowed (No Version Bump Required):
```
Add new institution to verified_institution_registry
Add new evidence field to any layer output (non-breaking)
Add new observable metric to Section Q
Add new test case to Section P
Add new canonical alias in Layer 11 institutional matching
Add new cert_type to registry (CERT_TYPE_10+) with full layer mapping
```

## Requires Version Bump (Controlled Change):
```
Change any layer output schema
Change authenticity score formula or layer weights
Change verdict threshold boundaries
Change any trigger SLA target
Change any API endpoint path, method, or schema
Change blockchain network or anchoring rules
Change public portal display rules
Add a new classification layer (LAYER_16+)
Change PII masking protocol
```

## Forbidden (Governance Board Decision Required):
```
Remove any classification layer
Remove any API endpoint
Lower INAUTHENTIC threshold (making system less strict)
Allow public portal to display SUSPECT or INAUTHENTIC verdict
Allow any role below trust_admin to access internal classification data
Modify CertSealLog or LayerResult immutability constraints
Allow automatic certificate revocation (must remain human-governed)
Change blockchain wallet key management (requires security audit)
```

---

# 🔒 SECTION S — MASTER PROMPT SEAL BLOCK

Add this block to the Ecoskiller Master Prompt (SECTION K of Dojo SaaS Artifact) after the ANTIGRAVITY PHANTOM SHIELD seal block:

```
═══════════════════════════════════════════════════════════════════════
ANTIGRAVITY MODULE — VAULT SEAL
CERTIFICATE AUTHENTICITY CLASSIFIER — PRODUCTION ENABLED
Module Code: AG-CAC-002

Certificate Type Registry:        9 types (CERT_TYPE_01–09) LOCKED
Classification Layers:            15 sequential layers LOCKED
                                  (8 BLOCKING + 7 ADVISORY)
Authenticity Verdict System:      4 verdicts LOCKED
                                  (AUTHENTIC | PROBABLE_AUTHENTIC | SUSPECT | INAUTHENTIC)
Composite Score Engine:           Weighted layer fusion + hard fail override + triple-confirm bonus
Blockchain Anchoring:             Polygon PoS + IPFS fallback — mandatory for CERT_TYPE_01/02/04/05/06
Detection Triggers:               9 triggers LOCKED (blocking + async + batch + scheduled)
Public Verification Portal:       SSR React — zero PII — QR-enabled — SEO-indexed
PII Protocol:                     user_token only — holder name never in vault_schema
Cryptographic Engine:             SHA-256 + SHA-3-256 + Ed25519 — HSM wallet
Immutability:                     CertSealLog + LayerResult — no UPDATE/DELETE ever
Dojo Trust Bindings:              T2, T3, T4, T7, T8, T9, T13, T14, T15, T16, T17 LOCKED
                                  Sections G, F (Scoring), H (Security) LOCKED
PHANTOM SHIELD Integration:       CLASS_09 (farming), CLASS_11 (institute),
                                  CLASS_13 (economic), composite_risk_score bindings ACTIVE
Excel Pattern Detection Binding:  AG-EPDM-001 CLASS_11 (learning velocity) ACTIVE
Trust Score Integration:          AUTHENTIC = +10 | INAUTHENTIC = -25 | SUSPECT = -8
Hiring Marketplace Gate:          AUTHENTIC / PROBABLE_AUTHENTIC only — TRIGGER_07 enforced
Tournament Entry Gate:            All certs validated before championship eligibility
API Surface:                      12 endpoints LOCKED
Flutter UI:                       5 certificate surfaces LOCKED
React UI:                         5 certificate portal pages LOCKED (1 public, 4 enterprise)
Talent OS Integration:            Modules 1, 3, 4, 6, 7, 9, 10, 15 mapped
Change Authority:                 Governance Board for forbidden changes
Interpretation Authority:         NONE
Architecture Authority:           LOCKED
Mutation Policy:                  Add-only via version bump

VAULT SEAL DOES NOT:
  — Issue certificates
  — Automatically revoke certificates
  — Override mentor certification decisions
  — Store raw PII in any vault_schema table
  — Display SUSPECT/INAUTHENTIC status publicly
  — Access cross-tenant certificate data

VAULT SEAL IS:
  — A 15-layer certificate authenticity classification engine
  — A cryptographic certificate integrity guardian
  — A blockchain anchoring and public verification service
  — A hiring marketplace certificate gate
  — A first-class Trust Infrastructure citizen in Ecoskiller Talent OS
  — The mechanism by which Ecoskiller certificates become
    the most tamper-evident skill credentials in the market

SEAL: LOCKED · FINAL · GOVERNED · DETERMINISTIC · PRODUCTION-READY
═══════════════════════════════════════════════════════════════════════
```

---

# 🔒 FINAL GOVERNANCE SEAL

```
VAULT SEAL — CERTIFICATE AUTHENTICITY CLASSIFIER
ANTIGRAVITY ENTERPRISE OPTIMIZATION + TRUST INFRASTRUCTURE
ECOSKILLER × DOJO SAAS — PRODUCTION ARTIFACT v1.0

✔ 9 Certificate Types Registered & Frozen
✔ 15 Classification Layers Defined & Frozen
   (8 BLOCKING + 7 ADVISORY — sequential pipeline)
✔ Processing Pipeline Frozen (14 steps)
✔ 9 Detection Triggers Defined & Locked
✔ 4-Verdict System Locked (AUTHENTIC → INAUTHENTIC)
✔ Verdict Transition Rules Frozen
✔ Composite Authenticity Score Engine Frozen (15 weights + modifiers)
✔ 6 Data Entity Schemas Frozen (immutable audit enforcement)
✔ Blockchain Anchoring System Locked (Polygon PoS + IPFS)
✔ Ed25519 + SHA-256 Cryptographic Engine Locked
✔ Public Verification Portal Architecture Sealed
✔ QR Code Integration Locked
✔ Dojo Trust Lock Alignment (15 section bindings)
✔ PHANTOM SHIELD (AG-FPDM-001) Integration Sealed
✔ Excel Pattern Detection (AG-EPDM-001) Integration Sealed
✔ Trust Score Impact Table Locked
✔ Hiring Marketplace Gate Locked
✔ 12 API Endpoints Frozen (1 public unauthenticated)
✔ PII Protection Protocol Locked
✔ Cryptographic Security Architecture Locked
✔ Access Segregation Architecture Locked
✔ Flutter Certificate UI Locked (5 surfaces)
✔ React Certificate Portal Locked (5 pages)
✔ Talent OS Module Integration Mapped (8 modules)
✔ Test Gate Requirements Sealed
✔ Observability Layer Sealed (metrics + dashboards + alerts)
✔ Change Governance Rules Locked (3 tiers)
✔ Master Prompt Seal Block Issued

Interpretation Authority:   NONE
Execution Authority:        Human declaration only
Architecture Authority:     LOCKED
Mutation Policy:            Add-only via version bump
Revocation Model:           Human-governed — no automatic revocation

VAULT SEAL STATUS: ██████████ LOCKED ██████████
```

---

*Document generated under Ecoskiller Enterprise Optimization + Trust Infrastructure governance.*
*Dojo SaaS Production Mode Active. ANTIGRAVITY Layer Engaged. VAULT SEAL Module Online.*
*Stack Locked. Mutation Add-Only Versioned. Architecture Interpretation Forbidden.*
*Sibling modules: AG-EPDM-001 (Excel Pattern Detection) | AG-FPDM-001 (Fake Profile Detection)*
