# 🔒 REGULATORY_COMPLIANCE_MAPPING_AGENT
## ECOSKILLER ANTIGRAVITY — Intelligence & Innovation Core

---

```
AGENT_ID              : RCMA-003
VERSION               : v1.0.0
STATUS                : SEALED · LOCKED · GOVERNED · DETERMINISTIC · ADD-ONLY
MUTATION_POLICY       : ADD-ONLY via version bump — no regulation mapping is ever deleted
INTERPRETATION        : NONE PERMITTED — regulations are mapped as declared, not interpreted
CREATIVE_AUTONOMY     : FORBIDDEN
ASSUMPTION_FILLING    : FORBIDDEN
DEFAULT_BEHAVIOR      : DENY — reject any data operation without verified compliance clearance
FAILURE_MODE          : STOP → BLOCK → LOG → ESCALATE → NO SILENT COMPLIANCE FAILURE
PLATFORM              : Ecoskiller Antigravity · Multi-Tenant Enterprise SaaS · 10M–100M Users
PRIMARY_JURISDICTION  : India (DPDP Act 2023 · IT Act 2000/2008 · UGC · AICTE · RBI · GST)
SECONDARY_JURISDICTION: EU (GDPR) · US (FERPA · COPPA · CCPA) · Global (ISO 27001 · SOC 2)
LEGAL_DISCLAIMER      : This agent maps regulations to system controls. It does NOT constitute
                        legal advice. All compliance determinations must be reviewed by qualified
                        legal counsel licensed in the relevant jurisdiction.
ML_USAGE              : 15% — Compliance Gap Detection + Regulatory Change Signal Scoring
AI_USAGE              : 0% in compliance path — LLM FORBIDDEN in any compliance decision
```

---

## §1 AGENT IDENTITY (MANDATORY)

| KEY | VALUE |
|---|---|
| `AGENT_NAME` | REGULATORY_COMPLIANCE_MAPPING_AGENT |
| `AGENT_ID` | RCMA-003 |
| `SYSTEM_ROLE` | Centralised Regulatory Compliance Mapper — Continuously maps all applicable regulations to Ecoskiller Antigravity system controls, data flows, user types, and geographic jurisdictions. Generates compliance status, gap reports, evidence packages, and breach notifications. |
| `PRIMARY_DOMAIN` | Regulatory Compliance · Data Protection · Privacy Law · Educational Law · Labour Law · Payment Compliance · Minor Protection · Cross-Border Data Governance |
| `EXECUTION_MODE` | Deterministic + Validated + Regulation-Gated |
| `DATA_SCOPE` | Regulation Registry · Control Mappings · User PII Classification · Data Flow Maps · Consent Records · Breach Logs · Audit Evidence Packages · DPO Notifications · Regulatory Filing Records |
| `TENANT_SCOPE` | **STRICT ISOLATION** — Each tenant's compliance posture is isolated. Cross-tenant compliance data NEVER shared. Platform-level compliance applies universally as the compliance floor. |
| `FAILURE_POLICY` | **HALT ON AMBIGUITY** — If a data operation cannot be mapped to a lawful basis under the applicable regulation, the operation is BLOCKED until legal basis is established |
| `MUTATION_POLICY` | **ADD-ONLY** — Regulation entries and control mappings are never deleted. Superseded regulations are archived with effective dates |
| `LEGAL_AUTHORITY` | NONE — This agent maps regulations to technical controls. It does not constitute legal advice. Human legal counsel must review all compliance determinations before regulatory filings. |
| `CREATIVE_INTERPRETATION` | **FORBIDDEN** |
| `ASSUMPTION_FILLING` | **FORBIDDEN** |
| `DEFAULT_BEHAVIOR` | **DENY** — If legal basis for a data operation is absent or unverifiable, operation is blocked |

> ⛔ This agent **MUST NOT** interpret ambiguous regulatory language. When regulatory mapping is uncertain, the agent flags the uncertainty, BLOCKS the data operation, and **ESCALATES TO DPO + LEGAL COUNSEL**. No autonomous compliance determination on ambiguous rules.

---

## §2 PURPOSE DECLARATION

### 2.1 Problem Statement

Ecoskiller Antigravity processes highly sensitive personal data across nine user role types, five domain tracks, and multiple geographies. The platform handles:

- **Minor user data** (Students — potentially under 18) requiring parental consent under DPDP Act 2023 (India) and COPPA (US)
- **Educational records** protected under UGC/AICTE guidelines (India) and FERPA (US)
- **Employment and recruitment data** governed by labour laws, equal opportunity regulations, and background check statutes
- **Financial transaction data** regulated by RBI Payment Aggregator guidelines, PCI-DSS, and GST compliance
- **Sensitive personal data** including intelligence profiles, health/body data (BodyQuest module), caste/religion indicators in scholarship contexts — all requiring heightened legal basis
- **Biometric/behavioural data** from Dojo GD sessions, proctoring, and intelligence detection engine
- **Parent visibility data** — read-only trust layer with its own consent and data minimisation obligations
- **Cross-border data transfers** when platform expands to EU, US, or SEA markets

Without a governed, continuously updated, agent-driven compliance layer:

- A single DPDP Act 2023 breach (India) can trigger ₹250 crore per incident penalties
- GDPR violations for EU-resident users can reach €20M or 4% global turnover
- COPPA violations for US minor users: $51,744 per violation per day (FTC)
- Processing minor data without verified parental consent creates criminal liability for directors
- Educational record breaches trigger mandatory UGC/AICTE reporting obligations
- Payment data non-compliance with RBI PA guidelines can result in licence revocation
- Missing Data Protection Impact Assessments (DPIAs) before high-risk processing create per-incident liability
- Inadequate breach notification timelines (72 hours under GDPR, 72 hours under DPDP Act) result in compounded penalties

### 2.2 What This Agent Solves

- Maintains the **Regulation Registry** — an add-only, versioned catalogue of all applicable regulations mapped to Ecoskiller's data flows, user types, processing activities, and geographies
- Performs **real-time lawful basis checks** before any data processing operation — blocking operations without established legal basis
- Manages the **Consent Lifecycle** — parental consent for minors, user consent for sensitive data, withdrawal processing, and consent audit trail
- Generates and maintains **Records of Processing Activities (RoPA)** as required under GDPR Article 30 and DPDP Act 2023
- Executes **Data Protection Impact Assessments (DPIAs)** for high-risk processing automatically, flagging human review requirements
- Manages **Data Subject Rights** fulfillment — access, correction, erasure, portability, objection — with SLA tracking
- Handles **Breach Detection, Classification, and Notification** workflows within statutory timelines
- Maps all **cross-border data transfer** mechanisms (SCCs, adequacy decisions, data localisation requirements)
- Generates **compliance evidence packages** for regulatory audits and certifications (ISO 27001, SOC 2, DPDP compliance)
- Tracks **regulatory change signals** and alerts governance team of impending regulatory updates

### 2.3 System Context

| | |
|---|---|
| **INPUT CONSUMES** | Data operation requests · User role + jurisdiction context · Consent records · Processing activity declarations · Breach notifications from security agents · Regulatory change feeds · DPO instructions · Tenant compliance profiles |
| **OUTPUT PRODUCES** | ALLOW / BLOCK data operation decision · Lawful basis citation · Consent status · Compliance gap report · DPIA result · RoPA entry · Breach notification package · DSR fulfillment instruction · Evidence package for audit · Regulatory change alert |
| **UPSTREAM AGENTS** | IDENTITY_AGENT · RBAC_AGENT · SECURITY_AGENT · PVCA-002 · AUDIT_AGENT · TENANT_ISOLATION_AGENT · BILLING_AGENT · INCIDENT_MANAGER_AGENT |
| **DOWNSTREAM AGENTS** | ALL data-processing agents · NOTIFICATION_AGENT (breach alerts) · OBSERVABILITY_AGENT · FEATURE_STORE_AGENT · INCIDENT_MANAGER_AGENT · GOVERNANCE_BOARD_AGENT · DPO_NOTIFICATION_SERVICE |
| **HUMAN ESCALATION** | Data Protection Officer (DPO) · Legal Counsel · CERT-In (India breach regulator) · Data Protection Board of India · EU Supervisory Authority (if applicable) |

---

## §3 REGULATION REGISTRY (COMPLETE · LOCKED · ADD-ONLY)

### 3.1 Primary Jurisdiction — India

| REG_ID | REGULATION | APPLICABILITY TO ECOSKILLER | KEY OBLIGATIONS | PENALTY CEILING |
|---|---|---|---|---|
| `IN-DPDP-2023` | **Digital Personal Data Protection Act, 2023** | ALL personal data processing of Indian residents. Core framework. | Lawful basis, consent management, data principal rights, breach notification within 72h to DPBI, data localisation for sensitive data, children's data requires verifiable parental consent | ₹250 crore per instance of non-compliance |
| `IN-IT-2000` | Information Technology Act, 2000 | All electronic data processing, digital contracts, electronic records | Data protection due diligence, electronic signatures, lawful interception obligations | Imprisonment + fines (Sections 43, 66, 72) |
| `IN-IT-SPDI-2011` | IT (Reasonable Security Practices) Rules, 2011 | Sensitive Personal Data (passwords, biometric, health, financial info) | Security practices, data collection notice, consent, third-party transfer rules, grievance officer | Civil liability + regulatory action |
| `IN-CERT-2014` | CERT-In Cybersecurity Directions, 2022 | All ICT systems. Mandatory for Ecoskiller as cloud SaaS. | Report incidents to CERT-In within 6 hours of detection, maintain logs 180 days, ICT asset inventory, maintain system clock synced to NTP | Up to ₹1 crore + criminal liability |
| `IN-RBI-PA-2020` | RBI Payment Aggregator Guidelines, 2020 | Billing module, payment processing, marketplace transactions | PA licence or use of licensed PA, merchant due diligence, escrow accounts, data storage restrictions, PCI-DSS compliance | Licence revocation + fines |
| `IN-GST-2017` | Goods and Services Tax Act, 2017 | SaaS subscription billing, marketplace transactions, B2B invoicing | GST registration, invoice compliance, e-invoicing (IRP), GSTR filings, HSN classification for SaaS | Penalties + interest on unpaid GST |
| `IN-POSH-2013` | POSH Act (Sexual Harassment at Workplace), 2013 | All workplace interactions — trainers, evaluators, enterprise users | Internal Complaints Committee, annual report, complaint mechanism, awareness | ₹50,000 fine + licence cancellation |
| `IN-UGC-2023` | UGC Regulations, Academic Data Guidelines | Institute tenants (colleges/universities), student academic records | Academic data integrity, student consent for records sharing, right to academic records | Affiliation loss |
| `IN-AICTE-2020` | AICTE Regulations | Technical institute tenants | Online learning compliance, certification standards, foreign collaboration rules | Approval withdrawal |
| `IN-JJ-2015` | Juvenile Justice (Care and Protection) Act, 2015 | Student users who may be minors (under 18) | Special protection for children, mandatory reporting of abuse, restricted data use | Criminal liability |
| `IN-POCSO-2012` | POCSO Act, 2012 | Any platform accessible to minors | Mandatory reporting of sexual offences against children, platform liability for hosting CSAM | Imprisonment (mandatory) |
| `IN-CPA-2019` | Consumer Protection Act, 2019 (E-commerce Rules 2020) | All B2C transactions, marketplace, job portal | Grievance redressal, cancellation/refund policy, display of seller info, prohibition of dark patterns | ₹10 lakh first offence; ₹50 lakh repeat |
| `IN-LABOUR-2020` | Labour Codes 2020 (Code on Wages, IR Code, etc.) | Recruitment module, enterprise hiring workflows | Non-discriminatory hiring data, equal remuneration data, contractor engagement compliance | Varies by code |
| `IN-PE-2020` | Personal Data of Employees Protection (draft provisions) | All employer-employee and recruiter-candidate data flows | Candidate consent for background checks, data minimisation in job applications, retention limits | Under DPDP Act |

### 3.2 Secondary Jurisdiction — European Union

| REG_ID | REGULATION | APPLICABILITY | KEY OBLIGATIONS | PENALTY CEILING |
|---|---|---|---|---|
| `EU-GDPR-2018` | **General Data Protection Regulation** | Any EU-resident user (students studying abroad, EU enterprise clients) | Lawful basis, data subject rights (Art. 15-22), DPIA (Art. 35), RoPA (Art. 30), DPO appointment (Art. 37), breach notification 72h, SCCs for transfers | €20M or 4% global turnover |
| `EU-EPRIVACY-2002` | ePrivacy Directive (as amended) | Cookies, tracking pixels, push notifications, marketing emails | Cookie consent, opt-in for marketing, subscriber data protection | Member state penalties |
| `EU-AI-ACT-2024` | EU AI Act, 2024 | AI systems in hiring decisions (job matching, candidate scoring) | Prohibited practices (subliminal manipulation), high-risk AI system registration, transparency obligations, human oversight mandatory | €35M or 7% global turnover |
| `EU-NIS2-2022` | NIS2 Directive, 2022 | If platform operates in EU as essential/important entity | Cybersecurity risk management, incident reporting within 24h (early warning) + 72h (notification) | €10M or 2% global turnover |

### 3.3 Secondary Jurisdiction — United States

| REG_ID | REGULATION | APPLICABILITY | KEY OBLIGATIONS | PENALTY CEILING |
|---|---|---|---|---|
| `US-FERPA-1974` | Family Educational Rights and Privacy Act | Any US educational institution tenant, student academic records | Parental/student rights to records, consent for disclosure, annual notification | Loss of federal funding |
| `US-COPPA-1998` | Children's Online Privacy Protection Act | Any US minor user (under 13) | Verifiable parental consent before collecting personal info from under-13s, privacy notice, no behavioural advertising | $51,744 per violation per day |
| `US-CCPA-2018` | California Consumer Privacy Act (+ CPRA 2023) | California-resident users | Right to know, right to delete, right to opt-out of sale, no discrimination for exercising rights | $2,500 per unintentional; $7,500 per intentional violation |
| `US-ADA-1990` | Americans with Disabilities Act | Accessibility for US users | WCAG 2.1 AA compliance for all user interfaces, accessible job application processes | $75,000–$150,000 per violation |
| `US-EEOC` | Equal Employment Opportunity Commission Rules | Recruitment module for US employers | Non-discriminatory job matching, no adverse impact screening, lawful use of AI in hiring | Civil suits + EEOC action |

### 3.4 Global Standards & Certifications

| STD_ID | STANDARD | APPLICABILITY | SCOPE | AUDIT CYCLE |
|---|---|---|---|---|
| `ISO-27001-2022` | ISO/IEC 27001:2022 | Full platform | Information Security Management System (ISMS) | Annual surveillance + 3-year recertification |
| `ISO-27701-2019` | ISO/IEC 27701:2019 | Full platform | Privacy Information Management System (PIMS) extension to ISO 27001 | Annual |
| `SOC2-T2` | SOC 2 Type II | Full platform (SaaS) | Trust Service Criteria: Security, Availability, Confidentiality, Processing Integrity, Privacy | Annual |
| `PCI-DSS-4` | PCI DSS v4.0 | Payment processing module | Cardholder data environment protection | Annual QSA assessment |
| `WCAG-2-1-AA` | Web Content Accessibility Guidelines 2.1 AA | All UI — Flutter + Next.js | Accessibility for disabled users | Continuous automated + annual manual |
| `OWASP-TOP10` | OWASP Top 10 Web Application Security | All APIs and web interfaces | Application security baseline | Every release cycle |

---

## §4 INPUT CONTRACT (STRICT)

### 4.1 Primary Input Schema — Compliance Check Request

> Every data processing request evaluated by this agent **MUST** carry the following envelope. Missing fields → **IMMEDIATE BLOCK** + `LAWFUL_BASIS_ABSENT`.

| FIELD | TYPE | REQ | DESCRIPTION |
|---|---|---|---|
| `operation_id` | UUID | **YES** | Unique identifier for this data operation |
| `operation_type` | ENUM | **YES** | COLLECT \| STORE \| PROCESS \| TRANSFER \| SHARE \| DELETE \| EXPORT \| PROFILE \| INFER \| PUBLISH |
| `data_subject_id` | UUID | **YES** | User whose data is being processed |
| `data_subject_role` | ENUM | **YES** | STUDENT \| TRAINER \| EVALUATOR \| INSTITUTE \| ENTERPRISE \| RECRUITER \| ADMIN \| PARENT \| AUTOMATION_AGENT |
| `data_subject_age_verified` | BOOLEAN | **YES** | Verified age status. If FALSE for STUDENT → minor protocol applies |
| `data_subject_jurisdiction` | ISO3166 | **YES** | Country code of data subject's legal residence e.g. IN, DE, US-CA |
| `tenant_id` | UUID | **YES** | Tenant performing the processing |
| `tenant_jurisdiction` | ISO3166 | **YES** | Country of tenant's legal establishment |
| `processing_purpose` | ENUM | **YES** | See §4.2 — Processing Purpose Registry |
| `data_categories` | ARRAY | **YES** | See §4.3 — Data Category Classification. Every category being processed must be declared. |
| `legal_basis_claimed` | ENUM | **YES** | CONSENT \| CONTRACT \| LEGAL_OBLIGATION \| VITAL_INTEREST \| PUBLIC_TASK \| LEGITIMATE_INTEREST |
| `consent_record_id` | UUID | **COND** | Required if legal_basis_claimed = CONSENT. Must reference a valid, active consent record. |
| `transfer_destination` | ISO3166\|NULL | **COND** | Required if operation_type = TRANSFER or SHARE. Target jurisdiction. |
| `transfer_mechanism` | ENUM | **COND** | Required if cross-border transfer. ADEQUACY \| SCC \| BCR \| DEROGATION \| DATA_LOCALISATION |
| `retention_period_days` | INT | **YES** | Declared retention period for this data operation |
| `third_party_recipient_id` | UUID\|NULL | **COND** | Required if SHARE with external processor. Must reference a registered data processor agreement. |
| `automated_decision` | BOOLEAN | **YES** | TRUE if this operation feeds into automated decision-making (job matching, scoring, ranking) |
| `profiling_indicator` | BOOLEAN | **YES** | TRUE if this operation involves profiling (intelligence engine, engagement scoring) |
| `request_timestamp` | ISO8601 | **YES** | UTC. Must be within ±60 seconds of server UTC. |
| `requesting_agent_id` | STRING | **YES** | Agent initiating this data operation e.g. `INTELLIGENCE_CORE`, `JOB_MATCHING_AGENT` |
| `idempotency_key` | UUID | **YES** | Dedup window: 24 hours |

### 4.2 Processing Purpose Registry (Locked Enumeration)

| PURPOSE_CODE | DESCRIPTION | HIGH_RISK | REQUIRES_DPIA |
|---|---|---|---|
| `ACCOUNT_MANAGEMENT` | User registration, profile management, authentication | LOW | NO |
| `LEARNING_DELIVERY` | Course access, study room, skill development content | LOW | NO |
| `JOB_MATCHING` | AI-driven job matching, eligibility scoring, application tracking | HIGH | **YES** |
| `CANDIDATE_EVALUATION` | Recruiter access to candidate profiles, skill scores | HIGH | **YES** |
| `INTELLIGENCE_PROFILING` | Gardner intelligence type detection, behavioural analysis via Dojo | CRITICAL | **YES** |
| `REPUTATION_SCORING` | Trust scores, influence scores, leaderboard ranking | MEDIUM | Conditional |
| `PAYMENT_PROCESSING` | Subscription billing, marketplace transactions, refunds | HIGH | **YES** (financial data) |
| `PARENTAL_OVERSIGHT` | Parent read-only access to child's activity, scores, attendance | MEDIUM | **YES** (minor data) |
| `CERTIFICATE_ISSUANCE` | Skill certifications, legacy archive, verification portal | LOW | NO |
| `ANALYTICS_INTERNAL` | Platform health analytics, engagement metrics (anonymised) | LOW | NO |
| `ANALYTICS_TENANT` | Institute/enterprise dashboard analytics on their users | MEDIUM | Conditional |
| `DOJO_EVALUATION` | GD session recording, scoring, anti-cheat enforcement | HIGH | **YES** (behavioural + potential video/audio) |
| `MARKETING_COMMS` | Promotional emails, push notifications, referral campaigns | MEDIUM | NO (consent required) |
| `RESEARCH_PRODUCT` | Anonymised/pseudonymised product research | LOW | NO (if properly anonymised) |
| `LEGAL_COMPLIANCE` | Audit logs, regulatory filing, court order response | LOW | NO |
| `BREACH_RESPONSE` | Incident investigation, breach containment, notification | LOW | NO |
| `THIRD_PARTY_INTEGRATION` | API integrations with external platforms (LinkedIn, etc.) | HIGH | **YES** |
| `BACKGROUND_VERIFICATION` | Employment background checks for enterprise clients | CRITICAL | **YES** |
| `MINOR_DATA_PROCESSING` | Any processing where data_subject_age_verified = FALSE | CRITICAL | **YES** + parental consent mandatory |

### 4.3 Data Category Classification (Risk Tiers)

| CATEGORY_CODE | DATA TYPE | RISK TIER | SPECIAL CATEGORY (INDIA) | SPECIAL CATEGORY (GDPR Art.9) |
|---|---|---|---|---|
| `BASIC_IDENTITY` | Name, email, phone, username | TIER_1 | NO | NO |
| `CONTACT_LOCATION` | Address, city, pin code, IP (hashed) | TIER_1 | NO | NO |
| `ACADEMIC_RECORDS` | Grades, certificates, course completions | TIER_2 | YES (DPDP) | NO |
| `EMPLOYMENT_HISTORY` | Work experience, skills, portfolio | TIER_2 | YES (DPDP) | NO |
| `FINANCIAL_DATA` | Payment methods, transaction history, billing | TIER_3 | YES (SPDI Rules + DPDP) | NO |
| `GOVERNMENT_ID` | Aadhaar (India), PAN, passport, driving licence | TIER_3 | YES (DPDP — sensitive) | YES (Art. 9 — official ID) |
| `BIOMETRIC_BEHAVIOURAL` | Dojo session recordings, typing patterns, facial detection | TIER_3 | YES (SPDI + DPDP) | YES (Art. 9 — biometric) |
| `INTELLIGENCE_PROFILE` | Gardner intelligence scores, cognitive assessment results | TIER_3 | YES (DPDP — health adjacent) | YES (Art. 9 — mental health adjacent) |
| `HEALTH_BODY_DATA` | BodyQuest module — fitness, physical metrics | TIER_3 | YES (SPDI + DPDP) | YES (Art. 9 — health data) |
| `MINOR_DATA` | Any data where data_subject_age_verified = FALSE | TIER_CRITICAL | YES (JJ Act + POCSO + DPDP) | YES (special protection) |
| `PARENT_CHILD_LINK` | Parent-to-child relationship record | TIER_CRITICAL | YES (DPDP + JJ Act) | YES |
| `CASTE_RELIGION_SC` | Caste/community data in scholarship/reservation contexts | TIER_3 | YES (DPDP — sensitive) | YES (Art. 9 — racial/ethnic origin) |
| `COMMUNICATION_CONTENT` | Messages, chat history in Dojo rooms | TIER_2 | YES (IT Act) | NO |
| `DEVICE_TECHNICAL` | Device ID, browser fingerprint, OS version | TIER_1 | NO | NO |
| `BEHAVIOURAL_ANALYTICS` | Click patterns, session duration, feature usage | TIER_2 | Conditional | Conditional (profiling) |
| `CREDENTIALS` | Passwords (hashed only), MFA seeds | TIER_3 | YES (SPDI) | NO |

---

## §5 OUTPUT CONTRACT (STRICT)

### 5.1 Compliance Decision Object

| FIELD | TYPE | DESCRIPTION |
|---|---|---|
| `compliance_decision` | ENUM | ALLOW \| BLOCK \| ALLOW_WITH_CONDITIONS \| PENDING_DPIA \| PENDING_CONSENT |
| `operation_id` | UUID | Echo of input — traceability |
| `applicable_regulations` | ARRAY | All regulation IDs that apply to this operation e.g. `["IN-DPDP-2023", "EU-GDPR-2018"]` |
| `lawful_basis_confirmed` | ENUM\|NULL | Confirmed legal basis per primary regulation. NULL if BLOCK. |
| `lawful_basis_citations` | ARRAY | `[{ regulation_id, article_section, basis_type }]` — exact legal citations |
| `consent_status` | ENUM\|NULL | VALID \| EXPIRED \| WITHDRAWN \| ABSENT \| NOT_REQUIRED |
| `dpia_required` | BOOLEAN | TRUE if processing purpose is high-risk |
| `dpia_status` | ENUM\|NULL | NOT_STARTED \| IN_PROGRESS \| APPROVED \| REJECTED — NULL if not required |
| `minor_protocol_active` | BOOLEAN | TRUE if data_subject_age_verified = FALSE |
| `parental_consent_status` | ENUM\|NULL | VERIFIED \| PENDING \| ABSENT — populated if minor_protocol_active |
| `data_localisation_required` | BOOLEAN | TRUE if data must remain in India under DPDP Act or sector rules |
| `transfer_mechanism_valid` | BOOLEAN\|NULL | NULL if no transfer. TRUE/FALSE if cross-border. |
| `retention_compliance` | OBJECT | `{ declared_days, max_allowed_days, compliant, regulation_source }` |
| `conditions` | ARRAY | If ALLOW_WITH_CONDITIONS — explicit conditions that must be satisfied |
| `blocking_reasons` | ARRAY | If BLOCK — specific regulatory violations preventing the operation |
| `ropa_entry_id` | UUID | Created/updated RoPA entry reference |
| `audit_reference` | UUID | Immutable audit event ID |
| `compliance_score` | FLOAT | 0.0–1.0 — overall compliance posture score for this operation |
| `next_review_date` | ISO8601 | When this compliance mapping should be re-evaluated |
| `model_version` | STRING | `RCMA-003-v1.0.0-20240101` |

### 5.2 BLOCK Response — Regulatory Violation Envelope

```json
{
  "compliance_decision": "BLOCK",
  "blocking_reasons": [
    {
      "regulation_id": "IN-DPDP-2023",
      "violation_type": "LAWFUL_BASIS_ABSENT",
      "article_section": "Section 4",
      "human_readable": "Processing of personal data requires a lawful basis under DPDP Act 2023. No valid consent record found and no other lawful basis established.",
      "remediation": "Obtain valid, informed, free, specific, and unambiguous consent from the data principal before proceeding.",
      "escalate_to": "DPO"
    }
  ],
  "operation_blocked_at": "2025-01-01T00:00:00Z",
  "audit_reference": "uuid",
  "support": "https://compliance.ecoskiller.com/violations"
}
```

---

## §6 LAWFUL BASIS ENGINE

### 6.1 Lawful Basis Determination Matrix

> For each `{processing_purpose} × {data_category} × {jurisdiction}` combination, the agent determines the primary and fallback lawful basis. If NO basis exists, the operation is BLOCKED.

| PURPOSE | DATA TIER | IN-DPDP-2023 BASIS | GDPR BASIS | NOTES |
|---|---|---|---|---|
| ACCOUNT_MANAGEMENT | TIER_1 | Consent (S.6) | Contract (Art.6.1.b) | Contract basis preferred for core service |
| JOB_MATCHING | TIER_2 | Consent (S.6) | Consent (Art.6.1.a) + Legitimate Interest (Art.6.1.f) | LI requires balancing test documentation |
| CANDIDATE_EVALUATION | TIER_2 | Consent (S.6) + Contract (S.7) | Contract (Art.6.1.b) | Recruiter-candidate contractual relationship |
| INTELLIGENCE_PROFILING | TIER_3 | **Explicit Consent (S.6) — MANDATORY** | **Explicit Consent (Art.9.2.a) — MANDATORY** | Special category. No other basis permitted. |
| PAYMENT_PROCESSING | TIER_3 | Contract (S.7) + Legal Obligation (RBI) | Contract (Art.6.1.b) + Legal Obligation (Art.6.1.c) | RBI PA Guidelines mandate data collection |
| MINOR_DATA_PROCESSING | TIER_CRITICAL | **Verifiable Parental Consent MANDATORY** | **Parental Consent (Art.8) MANDATORY** | No processing until consent verified |
| DOJO_EVALUATION | TIER_3 | **Explicit Consent (S.6)** | **Explicit Consent (Art.9.2.a) if biometric** | Biometric/behavioural = special category |
| BACKGROUND_VERIFICATION | TIER_3 | Consent (S.6) + Contract (S.7) | Consent + Legitimate Interest | Must document necessity and proportionality |
| ANALYTICS_INTERNAL | TIER_1 | Legitimate Use (S.7) / Anonymised | Not applicable if truly anonymised | Must verify anonymisation is irreversible |
| LEGAL_COMPLIANCE | ANY | Legal Obligation (S.7) | Legal Obligation (Art.6.1.c) | Court orders, regulatory demands |
| MARKETING_COMMS | TIER_1/2 | **Consent MANDATORY (opt-in)** | **Consent MANDATORY (ePrivacy)** | No legitimate interest for direct marketing |
| HEALTH_BODY_DATA | TIER_3 | **Explicit Consent MANDATORY** | **Explicit Consent Art.9.2.a MANDATORY** | Health data — no other basis |
| PARENTAL_OVERSIGHT | TIER_CRITICAL | Parental Consent + Child's assent (age-appropriate) | Art.8 parental consent | Must not process more than parent can see |

### 6.2 Minor Data Protocol (MANDATORY — ZERO BYPASS)

```
TRIGGER: data_subject_age_verified = FALSE
         OR data_subject_role = STUDENT AND age_group = MINOR

PROTOCOL:
  Step 1: BLOCK all non-essential processing immediately
  Step 2: Collect only data necessary for age verification
  Step 3: Send parental consent request to registered parent email
  Step 4: Parent completes verifiable consent flow (not just checkbox):
          - Confirm identity (email OTP minimum; Aadhaar eKYC preferred for IN)
          - Review data categories to be collected
          - Review processing purposes
          - Review data sharing with institutes/recruiters
          - Provide explicit consent per purpose (granular — not bundled)
  Step 5: Store consent record in append-only consent_store with:
          - Parent user_id
          - Consent timestamp
          - IP hash
          - Consent method
          - Data categories consented to
          - Purposes consented to
          - Version of privacy notice shown
  Step 6: ALLOW only consented operations for this minor user

ONGOING:
  - Re-consent required if: new processing purpose added, minor turns 18 (transition flow),
    parent withdraws consent, privacy notice materially changes
  - Parent can withdraw consent at any time → immediate processing HALT for minor
  - At age 18: minor gains independent data subject rights, parent access auto-revoked
    after 30-day transition period with notification to both parties

ABSOLUTE RULES FOR MINOR DATA:
  - NEVER use minor data for advertising or marketing profiling
  - NEVER share minor data with third parties without explicit per-purpose parental consent
  - NEVER retain minor data beyond the service relationship without separate consent
  - NEVER apply behavioural advertising to minor users
  - NEVER expose minor data through public-facing SEO pages (Next.js) even with parental consent
  - Mandatory DPIA for ALL minor data processing — no exceptions
```

### 6.3 Consent Lifecycle Management

```
Consent States:
  REQUESTED → PENDING → ACTIVE → MODIFIED → WITHDRAWN → EXPIRED

Consent Record Schema:
{
  consent_id:           UUID PRIMARY KEY,
  data_subject_id:      UUID NOT NULL,
  consent_grantor_id:   UUID NOT NULL,        // data_subject or parent for minor
  consent_type:         ENUM,                  // STANDARD | PARENTAL | EXPLICIT_SPECIAL_CATEGORY
  data_categories:      JSONB NOT NULL,        // exact categories consented to
  purposes:             JSONB NOT NULL,        // exact purposes consented to — never bundled
  lawful_basis:         VARCHAR(32) NOT NULL,
  regulation_ids:       JSONB NOT NULL,        // regulations this consent satisfies
  privacy_notice_version: VARCHAR(32) NOT NULL,// version of notice shown at consent time
  consent_method:       ENUM,                  // WEB_FORM | EMAIL_OTP | AADHAAR_EKYC | PAPER
  ip_hash:              VARCHAR(64) NOT NULL,
  user_agent_hash:      VARCHAR(64),
  timestamp_utc:        TIMESTAMPTZ NOT NULL,
  expiry_date:          TIMESTAMPTZ,           // NULL = no expiry, reviewed annually
  withdrawal_timestamp: TIMESTAMPTZ,
  withdrawal_method:    ENUM,
  status:               ENUM NOT NULL,
  audit_ref:            UUID NOT NULL          // links to audit.consent_log
}

Rules:
  - Consent is always purpose-specific and data-category-specific (no blanket consent)
  - Consent request must be in plain language (reading level: Class 8 equivalent for India)
  - Consent must be freely given — platform access MUST NOT be conditional on non-essential consent
  - Withdrawal must be as easy as giving consent — single-click withdrawal in settings
  - Withdrawal must be processed within 72 hours for all downstream agents
  - Historical consent records are NEVER deleted — withdrawal creates a new WITHDRAWN record
```

---

## §7 DATA SUBJECT RIGHTS FULFILLMENT ENGINE

### 7.1 Rights Matrix by Regulation

| RIGHT | IN-DPDP-2023 | EU-GDPR | US-CCPA | SLA | AUTOMATED |
|---|---|---|---|---|---|
| **Right to Access** | S.11 — Know what data is held | Art.15 — Access + copy | Right to Know | 30 days (IN/EU) · 45 days (US) | Partial (full copy needs human review) |
| **Right to Correction** | S.12 — Correct inaccurate data | Art.16 | Not explicitly | 30 days | YES — verified fields auto-corrected |
| **Right to Erasure** | S.13 — Erasure on withdrawal/purpose completion | Art.17 ("Right to be Forgotten") | Right to Delete | 30 days | Partial (audit logs exempt — retained per legal obligation) |
| **Right to Portability** | Not explicit in DPDP 2023 | Art.20 — Machine-readable format | Right to Data Portability | 30 days | YES — structured JSON/CSV export |
| **Right to Object to Profiling** | S.14 — Grievance mechanism | Art.21 + Art.22 | Right to Opt-Out of Sale/Sharing | Immediate BLOCK on objection | YES — profiling flag set |
| **Right to Nomination** | S.14 — Nominate another to exercise rights on death/incapacity | Not applicable | Not applicable | 30 days | NO — human review required |
| **Right to Grievance** | S.13 — Grievance Officer within 48h acknowledgement | Art.77 — Supervisory Authority | Attorney General complaint | 48h acknowledgement (IN) | YES — acknowledgement automated |
| **Right to Withdraw Consent** | S.7 — Withdraw at any time | Art.7.3 | Opt-out | Effective within 72h | YES |

### 7.2 DSR Fulfillment Workflow

```
FUNCTION process_dsr(request):

  Step 1: VERIFY requester identity (JWT + MFA for sensitive DSRs)
  Step 2: CONFIRM requester is data subject or authorised representative
  Step 3: CLASSIFY request by right_type + applicable regulations
  Step 4: LOG DSR intake → create DSR record with intake_timestamp
  Step 5: SET SLA deadline based on regulation (30 days default, 45 days US)
  Step 6: SCAN all data stores for data_subject_id:
          - PostgreSQL: user profiles, academic records, job applications, consent records
          - Redis: active session data, rate limit counters (anonymised — note exclusion)
          - MinIO: uploaded documents, certificates, Dojo session recordings
          - OpenSearch: indexed profiles, job postings
          - Audit logs: EXEMPT from erasure per legal obligation basis

  Step 7: FOR ERASURE requests:
          IF data held under LEGAL_OBLIGATION basis → RETAIN (note exemption in response)
          IF data held under CONSENT basis → ERASE from live systems
          IF data is in audit logs → RETAIN with pseudonymisation where possible
          PSEUDONYMISE rather than delete where full deletion would break audit integrity

  Step 8: COMPILE response package in machine-readable format (JSON)
  Step 9: DELIVER to data subject via secure channel (authenticated download link, 48h TTL)
  Step 10: LOG completion → audit.dsr_fulfillment_log
  Step 11: IF SLA missed → AUTO_ESCALATE to DPO + compliance alert

EXEMPTIONS FROM ERASURE (must be documented per request):
  - Audit trail records (legal obligation — CERT-In 180 days, SOC 2, ISO 27001)
  - Financial transaction records (GST — 7 years; RBI — varies)
  - Academic certification records (UGC — permanent)
  - Court order / legal hold data
  - Data actively needed for pending dispute resolution
```

---

## §8 DATA PROTECTION IMPACT ASSESSMENT (DPIA) ENGINE

### 8.1 DPIA Trigger Matrix

| TRIGGER CONDITION | REGULATION | DPIA TYPE | APPROVER |
|---|---|---|---|
| `processing_purpose` = INTELLIGENCE_PROFILING | DPDP + GDPR Art.35 | FULL_DPIA | DPO + Legal Counsel |
| `processing_purpose` = DOJO_EVALUATION with biometric/audio/video | DPDP + GDPR Art.35 | FULL_DPIA | DPO + Legal Counsel |
| `processing_purpose` = JOB_MATCHING with automated decision | EU AI Act + GDPR Art.22 | AI_SYSTEM_DPIA | DPO + AI Governance Officer |
| `data_categories` contains MINOR_DATA | DPDP + JJ Act + COPPA | MINOR_DPIA | DPO + Legal Counsel + Designated Child Safety Officer |
| `processing_purpose` = BACKGROUND_VERIFICATION | DPDP + Labour Codes | EMPLOYMENT_DPIA | DPO + HR Legal |
| `data_categories` contains GOVERNMENT_ID (Aadhaar) | IT Act + UIDAI Regulations | AADHAAR_DPIA | DPO + UIDAI Compliance |
| `data_categories` contains HEALTH_BODY_DATA | DPDP + SPDI Rules | HEALTH_DPIA | DPO + Medical Advisor |
| New processing of TIER_3 data at scale > 100,000 subjects | DPDP + GDPR | SCALE_DPIA | DPO |
| Cross-border transfer to non-adequate country | GDPR Art.35 + DPDP | TRANSFER_DPIA | DPO + Legal Counsel |
| `automated_decision` = TRUE affecting legal/significant interests | GDPR Art.22 + EU AI Act | AUTOMATED_DPIA | DPO + AI Officer |

### 8.2 DPIA Record Schema

```
TABLE: compliance.dpia_register  -- IMMUTABLE: NO UPDATE, NO DELETE

  dpia_id               UUID PRIMARY KEY,
  dpia_type             VARCHAR(32) NOT NULL,
  processing_purpose    VARCHAR(64) NOT NULL,
  data_categories       JSONB NOT NULL,
  applicable_regulations JSONB NOT NULL,
  scope_description     TEXT NOT NULL,
  necessity_assessment  TEXT NOT NULL,         // Why is this processing necessary?
  proportionality_check TEXT NOT NULL,         // Is it proportionate to the purpose?
  risk_identified       JSONB NOT NULL,        // List of identified risks + likelihood + severity
  risk_mitigation       JSONB NOT NULL,        // Controls applied to mitigate each risk
  residual_risk         ENUM NOT NULL,         // LOW | MEDIUM | HIGH | UNACCEPTABLE
  dpo_reviewed_by       UUID NOT NULL,
  dpo_review_date       TIMESTAMPTZ NOT NULL,
  legal_counsel_signoff BOOLEAN NOT NULL,
  outcome               ENUM NOT NULL,         // APPROVED | APPROVED_WITH_CONDITIONS | REJECTED
  conditions_applied    JSONB,
  supervisory_authority_consulted BOOLEAN DEFAULT FALSE, // If residual_risk = HIGH — must consult
  sa_consultation_ref   VARCHAR(256),
  created_at            TIMESTAMPTZ NOT NULL,
  review_due_date       TIMESTAMPTZ NOT NULL,  // DPIAs reviewed when circumstances change or every 3 years
  agent_id_requesting   VARCHAR(64) NOT NULL,
  audit_ref             UUID NOT NULL
```

> If `residual_risk` = UNACCEPTABLE → **processing is BLOCKED unconditionally** until risk is reduced to acceptable level. This cannot be overridden by any operator, governance board, or business decision.

---

## §9 BREACH NOTIFICATION ENGINE

### 9.1 Breach Classification Matrix

| BREACH_TYPE | EXAMPLES | SEVERITY | NOTIFICATION TIMELINE |
|---|---|---|---|
| `CONFIDENTIALITY_BREACH` | Unauthorised access to student records, job applications | DEPENDS ON DATA | See §9.2 |
| `INTEGRITY_BREACH` | Modification of academic certificates, job scores | HIGH | Immediate internal; 72h regulatory |
| `AVAILABILITY_BREACH` | Data destruction, ransomware, system outage causing data loss | HIGH | Immediate internal; assess regulatory need |
| `MINOR_DATA_BREACH` | Any breach involving minor users' data | **CRITICAL** | Immediate internal; 6h CERT-In; 72h DPBI; immediate parent notification |
| `FINANCIAL_DATA_BREACH` | Payment card data, bank account details exposed | **CRITICAL** | Immediate RBI reporting; 6h CERT-In; immediate user notification |
| `GOVERNMENT_ID_BREACH` | Aadhaar, PAN, passport data exposed | **CRITICAL** | Immediate UIDAI notification (Aadhaar); 6h CERT-In; 72h DPBI |
| `BULK_BREACH` | > 1,000 data subjects affected | HIGH | 6h CERT-In; 72h DPBI / GDPR supervisory authority |
| `BIOMETRIC_BREACH` | Dojo session recordings, intelligence profile data exposed | **CRITICAL** | All applicable regulators within 72h |

### 9.2 Breach Notification SLA Matrix

| REGULATOR | REGULATION | TIMELINE | NOTIFICATION METHOD | THRESHOLD |
|---|---|---|---|---|
| **CERT-In** | IT (Amendment) Act + CERT-In Directions 2022 | **6 hours from detection** | CERT-In incident reporting portal | All cybersecurity incidents |
| **Data Protection Board of India (DPBI)** | DPDP Act 2023 | **72 hours from detection** | DPBI prescribed format | Personal data breach |
| **Data Subjects (India)** | DPDP Act 2023 S.8 | **Without undue delay** (72h target) | Email + in-app notification | If breach likely to cause harm |
| **RBI** | RBI Cyber Security Framework | **6 hours (initial)** + 24h detailed | RBI secure email + portal | All cyber incidents in payment systems |
| **UIDAI** | Aadhaar (Data Security) Regulations | **Immediate** | UIDAI CISO direct notification | Any Aadhaar data compromise |
| **EU Supervisory Authority** | GDPR Art.33 | **72 hours from awareness** | National DPA portal | Personal data breach (EU residents) |
| **EU Data Subjects** | GDPR Art.34 | **Without undue delay** | Email + in-app | High risk to rights and freedoms |
| **FTC (US)** | COPPA + FTC Act | **Without unreasonable delay** | FTC reporting form | COPPA violations involving minors |
| **California AG** | CCPA/CPRA | **Expedient** | AG notification + affected users | California residents — 500+ affected |

### 9.3 Automated Breach Response Workflow

```
TRIGGER: INCIDENT_MANAGER_AGENT emits BREACH_DETECTED event

Step 1: CLASSIFY breach (type, severity, data categories, affected count, jurisdictions)
Step 2: DETERMINE applicable regulators and timelines from §9.2 matrix
Step 3: IMMEDIATELY (< 30 min):
        → Alert DPO (email + FCM push)
        → Alert CISO
        → Alert Legal Counsel
        → Create BREACH_RECORD in compliance.breach_register
        → HALT all non-essential processing of affected data
        → Preserve all logs (lock audit records — prevent any deletion)
Step 4: WITHIN 6 HOURS:
        → File CERT-In report (if applicable)
        → File RBI report (if payment data affected)
        → Notify UIDAI (if Aadhaar data affected)
        → Notify parents of affected minors
Step 5: WITHIN 72 HOURS:
        → File DPBI notification
        → File GDPR supervisory authority notification (if EU residents affected)
        → Begin data subject notification (individual emails)
Step 6: WITHIN 30 DAYS:
        → Complete forensic investigation report
        → Implement remediation controls
        → File final incident report with all regulators
        → Conduct DPIA review for affected processing activities
        → Update breach register with lessons learned
```

---

## §10 CROSS-BORDER DATA TRANSFER GOVERNANCE

### 10.1 Transfer Mechanism Registry

| FROM | TO | MECHANISM | STATUS | NOTES |
|---|---|---|---|---|
| IN | EU | **Standard Contractual Clauses (SCCs)** | REQUIRED | EU SCCs (2021) must be executed with any EU cloud provider/processor |
| IN | US | **SCCs + Case-by-case DPA** | REQUIRED | No adequacy decision. SCCs + Supplementary Measures per Schrems II |
| IN | IN | **Data Localisation** | PREFERRED | DPDP Act mandates certain sensitive data remains in India |
| IN | Adequate countries (list from DPBI) | **Adequacy Decision** | ALLOWED | Once DPBI publishes adequacy list under DPDP Act 2023 |
| IN | SEA | **SCCs** | REQUIRED | No adequacy decisions with most SEA countries |
| EU | US | **EU-US Data Privacy Framework** | ALLOWED (certified entities only) | Must verify US recipient is DPF-certified |

### 10.2 Data Localisation Requirements

| DATA CATEGORY | REGULATION | LOCALISATION REQUIREMENT |
|---|---|---|
| Payment data (cards, UPI, netbanking) | RBI Storage of Payment Data Guidelines | **MUST be stored only in India** — no exceptions |
| Aadhaar-linked data | Aadhaar Act + UIDAI regulations | **MUST be stored in India** — UIDAI can prescribe further restrictions |
| Student academic records (government institution tenants) | UGC + State education rules | Strongly preferred in India; cross-border requires UGC approval |
| CERT-In incident logs | CERT-In Directions 2022 | **MUST be stored in India** for 180 days |
| General personal data | DPDP Act 2023 | Central Government can notify categories requiring localisation — monitor for notifications |

---

## §11 RECORDS OF PROCESSING ACTIVITIES (RoPA)

### 11.1 RoPA Schema (GDPR Art.30 + DPDP Best Practice)

```sql
TABLE: compliance.ropa_register  -- IMMUTABLE: NO UPDATE, NO DELETE

  ropa_entry_id         UUID PRIMARY KEY,
  processing_activity   VARCHAR(256) NOT NULL,
  processing_purpose    VARCHAR(64) NOT NULL,     -- links to §4.2 purpose registry
  data_controller_id    VARCHAR(256) NOT NULL,    -- Ecoskiller entity name + address
  dpo_contact           VARCHAR(256) NOT NULL,
  joint_controller_id   VARCHAR(256),             -- if applicable
  data_categories       JSONB NOT NULL,            -- from §4.3 registry
  data_subject_roles    JSONB NOT NULL,
  legal_basis           VARCHAR(64) NOT NULL,
  special_category_basis VARCHAR(64),             -- if special category (GDPR Art.9)
  recipients            JSONB NOT NULL,            -- internal + external recipients
  third_country_transfers JSONB,                  -- if cross-border
  transfer_safeguards   JSONB,                    -- SCC ref, adequacy decision, etc.
  retention_period      VARCHAR(256) NOT NULL,
  retention_basis       TEXT NOT NULL,
  technical_measures    JSONB NOT NULL,            -- encryption, pseudonymisation, etc.
  organisational_measures JSONB NOT NULL,         -- access controls, training, etc.
  created_at            TIMESTAMPTZ NOT NULL,
  last_reviewed_at      TIMESTAMPTZ NOT NULL,
  review_due_date       TIMESTAMPTZ NOT NULL,     -- at least annual review
  agent_id              VARCHAR(64) NOT NULL,     -- agent responsible for this processing
  tenant_id             UUID,                     -- NULL = platform-level activity
  audit_ref             UUID NOT NULL
```

> RoPA entries are created/updated on every compliance-checked data operation. The RoPA is available for regulatory inspection on demand within 24 hours.

---

## §12 ML / ALGORITHM LOGIC LAYER

### 12.1 Lawful Basis Resolution Algorithm (Deterministic Core)

```
FUNCTION resolve_lawful_basis(operation):

  regulation_set = identify_applicable_regulations(
    data_subject_jurisdiction, tenant_jurisdiction,
    data_categories, processing_purpose
  )

  FOR each regulation in regulation_set:
    basis_options = lookup_basis_matrix(
      regulation.id, operation.processing_purpose,
      operation.data_categories
    )

    IF basis_options is EMPTY:
      RETURN { basis: NULL, decision: BLOCK, reason: "NO_LAWFUL_BASIS_FOR_PURPOSE" }

    FOR each basis_option in basis_options:
      IF basis_option.type == CONSENT:
        consent = fetch_consent_record(operation.consent_record_id)
        IF consent.status != ACTIVE:
          CONTINUE  // try next basis
        IF consent.purposes does not include operation.processing_purpose:
          CONTINUE
        IF consent.data_categories does not include operation.data_categories:
          CONTINUE
        RETURN { basis: CONSENT, citation: basis_option.article, decision: ALLOW }

      IF basis_option.type == CONTRACT:
        contract = verify_contractual_relationship(
          operation.data_subject_id, operation.tenant_id
        )
        IF contract.active AND operation.processing_purpose in contract.scope:
          RETURN { basis: CONTRACT, citation: basis_option.article, decision: ALLOW }

      IF basis_option.type == LEGAL_OBLIGATION:
        obligation = lookup_legal_obligation(regulation.id, operation.processing_purpose)
        IF obligation.confirmed:
          RETURN { basis: LEGAL_OBLIGATION, citation: obligation.statute, decision: ALLOW }

  RETURN { basis: NULL, decision: BLOCK, reason: "ALL_BASIS_OPTIONS_EXHAUSTED" }
```

### 12.2 ML-Assisted Compliance Gap Detection (Async — Advisory Only)

> ML model runs **asynchronously** — NEVER delays or overrides the synchronous lawful basis decision.

| SIGNAL | MODEL | THRESHOLD | ACTION |
|---|---|---|---|
| `REGULATION_CHANGE_RISK` | NLP classifier on regulatory change feeds | Confidence > 0.8 | Emit REGULATORY_UPDATE_ALERT to DPO + Legal |
| `CONSENT_DECAY_RISK` | Time-series predictor on consent expiry | > 20% of consents expiring in 30 days | Emit CONSENT_RENEWAL_BATCH_ALERT |
| `DPIA_GAP_RISK` | Classification on new processing activities vs DPIA register | New pattern without DPIA | Flag for DPIA_REQUIRED review |
| `RETENTION_OVERAGE_RISK` | Anomaly detection on data retention durations | Data held > declared retention + 30 days buffer | Emit RETENTION_BREACH_RISK |
| `CROSS_BORDER_DRIFT` | Transfer pattern monitor | Transfer to jurisdiction with no registered mechanism | BLOCK immediately + escalate |
| `MINOR_AGE_INCONSISTENCY` | Age signal cross-reference (if available) | age_verified=TRUE but signals suggest minor | FLAG for manual age reverification |

### 12.3 ML Model Governance

- **MODEL_TYPE:** Ensemble — NLP (regulatory change), Survival Analysis (consent expiry), Anomaly Detection (retention + transfer drift)
- **FEATURES_USED:** `regulation_text_delta`, `consent_age_days`, `processing_pattern_vector`, `data_retention_delta`, `transfer_jurisdiction_delta`, `age_signal_inconsistency_score`
- **TRAINING_FREQUENCY:** Monthly retrain on regulatory update corpus + quarterly full retrain with legal team validation
- **DRIFT_DETECTION:** Regulatory landscape changes trigger mandatory model review regardless of schedule
- **AI USAGE SCOPE:** Gap detection + alert ONLY. No ML model autonomously blocks, allows, or classifies a legal basis
- **LLM / GENERATIVE AI:** **FORBIDDEN** in any compliance decision path. No summarisation of regulations used as input to decision engine. Only structured regulation registry entries are used.

---

## §13 SCALABILITY DESIGN

| KEY | VALUE |
|---|---|
| `EXPECTED_CHECK_RPS` | 5,000–50,000 compliance checks/min (every data operation is checked) |
| `LATENCY_TARGET` | P99 < 15ms for ALLOW (cache hit) \| P99 < 50ms for complex multi-regulation check |
| `CACHE_STRATEGY` | Lawful basis results cached per `{purpose}×{data_category}×{jurisdiction}×{user_id}` for 300s. Invalidated on consent change. |
| `CONSENT_STORE` | PostgreSQL (primary) + Redis (active consent cache, 60s TTL per user) |
| `RoPA_STORE` | PostgreSQL (immutable, append-only) |
| `BREACH_STORE` | PostgreSQL (immutable) + real-time alert pipeline (Redis Streams → NOTIFICATION_AGENT) |
| `DPIA_STORE` | PostgreSQL (immutable) + MinIO (full DPIA document archive) |
| `REGULATION_REGISTRY` | PostgreSQL read replica (regulation data is low-change, high-read) |
| `MINOR_PROTOCOL_CACHE` | Redis per `{user_id}` — minor_flag and parental_consent_status cached 120s |
| `STATELESS_EXEC` | Fully stateless. All state in PostgreSQL + Redis. Kubernetes HPA for compute. |
| `GEO_ROUTING` | Compliance checks for EU-resident users routed to EU-region instance for data residency compliance |

---

## §14 SECURITY ENFORCEMENT

### 14.1 Zero-Trust Security Controls for Compliance Data

| CONTROL | ENFORCEMENT | VIOLATION RESPONSE |
|---|---|---|
| DPO-only access to full RoPA | RBAC role: `DPO_OFFICER` required for RoPA read | BLOCK 403 + LOG_ACCESS_ATTEMPT |
| Consent record immutability | REVOKE UPDATE, DELETE on consent_store | Any modification → CRITICAL_ALERT |
| Breach record immutability | REVOKE UPDATE, DELETE on breach_register | Any modification → CRITICAL_SECURITY_INCIDENT |
| DPIA document confidentiality | AES-256 encryption at rest + DPO-role gated access | Unauthorised access → SECURITY_INCIDENT |
| Aadhaar data special controls | UIDAI regulations — tokenised reference only (not raw Aadhaar) | Raw Aadhaar storage → LEGAL_VIOLATION + immediate deletion |
| Minor data access restriction | PARENT role can only access own child's data via parent-child consent link | Cross-child access → PRIVACY_VIOLATION |
| DSR request authentication | MFA required for all DSR submissions (prevent fraudulent erasure requests) | Unauthenticated DSR → REJECT |
| Regulatory filing confidentiality | Breach notifications to regulators are logged but payload is DPO-only | General staff cannot access regulatory filing content |
| Legal hold enforcement | Data subject to legal hold is EXEMPT from DSR erasure requests | Legal hold overrides erasure — documented in DSR response |
| Pen test records | Annual pen test reports stored immutably with certification evidence | Required for ISO 27001 and SOC 2 |

### 14.2 Aadhaar Compliance (India — UIDAI Special Requirements)

- **NEVER** store raw 12-digit Aadhaar number — only store Virtual ID (VID) or tokenised reference
- Aadhaar-based eKYC must use **Authentication User Agency (AUA)** / **KYC User Agency (KUA)** licensed by UIDAI
- Aadhaar data MUST be purged within the period specified by UIDAI after use
- Aadhaar XML / e-Aadhaar may be used for verification but the document must not be retained after purpose completion
- Any Aadhaar data breach → immediate UIDAI notification (no delay permitted)

---

## §15 AUDIT & TRACEABILITY

### 15.1 Audit Log Schema — `audit.compliance_log`

```sql
TABLE: audit.compliance_log  -- IMMUTABLE: NO UPDATE, NO DELETE

  audit_ref               UUID PRIMARY KEY,
  timestamp_utc           TIMESTAMPTZ NOT NULL DEFAULT now(),
  operation_id            UUID NOT NULL,
  operation_type          VARCHAR(32) NOT NULL,
  compliance_decision     VARCHAR(32) NOT NULL,
  requesting_agent_id     VARCHAR(64) NOT NULL,
  data_subject_id         UUID NOT NULL,
  data_subject_role       VARCHAR(32) NOT NULL,
  data_subject_jurisdiction VARCHAR(10) NOT NULL,
  tenant_id               UUID NOT NULL,
  processing_purpose      VARCHAR(64) NOT NULL,
  data_categories         JSONB NOT NULL,
  applicable_regulations  JSONB NOT NULL,
  lawful_basis_confirmed  VARCHAR(64),
  lawful_basis_citations  JSONB,
  consent_record_id       UUID,
  consent_status          VARCHAR(32),
  dpia_required           BOOLEAN NOT NULL,
  dpia_id                 UUID,
  minor_protocol_active   BOOLEAN NOT NULL,
  parental_consent_status VARCHAR(32),
  transfer_destination    VARCHAR(10),
  transfer_mechanism      VARCHAR(32),
  retention_compliance    JSONB NOT NULL,
  blocking_reasons        JSONB,
  compliance_score        NUMERIC(4,3),
  model_version           VARCHAR(64) NOT NULL,
  ropa_entry_id           UUID,
  jaeger_trace_id         VARCHAR(64),
  idempotency_key         UUID
```

### 15.2 Compliance Evidence Package (For Regulatory Audits)

On regulatory audit request, the agent generates a **Compliance Evidence Package** containing:

1. Current RoPA (Records of Processing Activities) — all entries for tenant scope
2. Active DPIA register with outcomes and risk assessments
3. Consent records audit trail for specified data subjects
4. Breach register with notification evidence (timestamps, regulator receipts)
5. DSR fulfillment log with SLA compliance metrics
6. Data flow diagram (auto-generated from compliance log analysis)
7. Third-party processor agreements index
8. Cross-border transfer mechanism documentation
9. Privacy notice version history
10. Staff training records (compliance awareness)
11. Penetration test and vulnerability assessment reports
12. ISO 27001 / SOC 2 certification status

> Package is generated in PDF and structured JSON format. Available within **24 hours** of DPO-authenticated request. Regulators may be entitled to faster access under applicable statutes (e.g., CERT-In: immediate access to logs).

---

## §16 INTER-AGENT DEPENDENCY MAP

### 16.1 Upstream Agents

| AGENT | DATA PROVIDED | COMPLIANCE ROLE |
|---|---|---|
| IDENTITY_AGENT | User identity, age verification status, jurisdiction | Establishes data subject identity for lawful basis check |
| RBAC_AGENT | Role claims | Determines applicable regulation set and data subject rights scope |
| SECURITY_AGENT | Breach detection events | Triggers breach notification workflow |
| PVCA-002 | Policy version changes | Re-evaluates compliance posture when policies change |
| AUDIT_AGENT | Audit log access | Evidence collection for DSR access requests and regulatory audits |
| INCIDENT_MANAGER_AGENT | Security incidents | Triggers breach classification and notification |
| TENANT_ISOLATION_AGENT | Tenant jurisdiction, licensed domains | Determines applicable regulation by tenant geography |
| BILLING_AGENT | Subscription status, payment data presence | Determines RBI / PCI-DSS applicability |

### 16.2 Downstream Agents Notified by This Agent

| AGENT | TRIGGER | PAYLOAD |
|---|---|---|
| NOTIFICATION_AGENT | BREACH_NOTIFICATION_REQUIRED | data_subject_ids, breach_type, notification_text |
| NOTIFICATION_AGENT | DSR_ACKNOWLEDGEMENT | data_subject_id, dsr_id, sla_deadline |
| NOTIFICATION_AGENT | CONSENT_EXPIRY_WARNING | user_id, consent_ids, expiry_dates |
| NOTIFICATION_AGENT | PARENTAL_CONSENT_REQUEST | parent_email, child_user_id, consent_package |
| OBSERVABILITY_AGENT | COMPLIANCE_METRICS_UPDATE | compliance_score, block_rate, dpia_count, dsr_sla_status |
| FEATURE_STORE_AGENT | COMPLIANCE_FEATURE_VECTOR | consent_health_score, dpia_coverage_pct, breach_risk_score |
| INCIDENT_MANAGER_AGENT | REGULATORY_BREACH_INCIDENT | regulation_id, violation_type, affected_subjects, timeline |
| GOVERNANCE_BOARD_AGENT | REGULATORY_CHANGE_ALERT | regulation_id, change_description, impact_assessment, deadline |
| ALL DATA AGENTS | PROCESSING_BLOCK_ORDER | operation_type, data_subject_id, blocking_reason, exemption_conditions |

---

## §17 PERFORMANCE MONITORING

### 17.1 Prometheus Metrics

| METRIC | TYPE | ALERT THRESHOLD |
|---|---|---|
| `rcma_compliance_checks_total{decision,purpose,regulation}` | Counter | Informational |
| `rcma_block_rate_pct{regulation,reason}` | Gauge | > 10% → WARN — high block rate may indicate consent collection failure |
| `rcma_check_latency_ms` | Histogram | P99 > 50ms → WARN \| P99 > 100ms → CRITICAL |
| `rcma_dpia_pending_count` | Gauge | > 5 pending > 30 days → ESCALATE TO DPO |
| `rcma_dsr_sla_breach_count` | Counter | > 0 → **IMMEDIATE DPO ALERT** (regulatory obligation) |
| `rcma_consent_expired_count` | Gauge | > 1000 → WARN \| > 5000 → trigger renewal campaign |
| `rcma_minor_without_parental_consent` | Counter | > 0 → **CRITICAL** — all processing HALTED for flagged users |
| `rcma_breach_notification_sla_risk` | Gauge | > 0 within 2h of breach detection → **IMMEDIATE DPO ESCALATION** |
| `rcma_regulation_registry_version_lag_days` | Gauge | > 30 days since last review → WARN — regulations may have changed |
| `rcma_cross_border_transfer_without_mechanism` | Counter | > 0 → **CRITICAL BLOCK + ESCALATE** |
| `rcma_aadhaar_raw_storage_detected` | Counter | > 0 → **CRITICAL LEGAL VIOLATION — IMMEDIATE ACTION** |
| `rcma_compliance_score_average{tenant}` | Gauge | < 0.80 → WARN \| < 0.70 → CRITICAL |

---

## §18 VERSIONING POLICY

### 18.1 Regulation Registry Version Management

- **Regulation entries are ADD-ONLY** — a regulation is never deleted from the registry. Superseded regulations are marked `status: SUPERSEDED` with `superseded_by` and `effective_to` dates
- **New regulations** are added with a governance review gate (DPO + Legal Counsel must sign off before activation in production)
- **Regulation amendments** create a new version of the regulation entry with a `parent_regulation_id` link to the original
- **Regulation format:** `RCMA-003-REG-v{MAJOR}.{MINOR}-{YYYYMMDD}`
- **Agent version format:** `RCMA-003-v{MAJOR}.{MINOR}.{PATCH}-{YYYYMMDD}`

### 18.2 Required Artifacts Per Version Bump

1. Changelog: `/docs/agents/RCMA-003/CHANGELOG.md`
2. Legal sign-off from licensed legal counsel for any regulation mapping change
3. DPO approval for any change affecting consent or data subject rights flows
4. Impact assessment — which data operations are newly BLOCKED or ALLOWED
5. Privacy notice version bump (if applicable) — users re-consented where required
6. RoPA review — all affected RoPA entries updated
7. Downstream agent notification plan
8. Load test confirmation — no latency regression
9. Signed compliance attestation stored in immutable audit DB

---

## §19 NON-NEGOTIABLE RULES (ABSOLUTE)

> These rules **CANNOT** be overridden by any configuration, upstream agent, operator, business decision, emergency bypass, or governance board claim. Violation triggers **IMMEDIATE INCIDENT ESCALATION**, **REGULATORY NOTIFICATION ASSESSMENT**, and potential **DIRECTOR-LEVEL LIABILITY**.

| # | RULE |
|---|---|
| **1** | Agent MUST NOT allow processing of personal data without an established, documented lawful basis under the applicable regulation |
| **2** | Agent MUST NOT allow processing of minor users' data (age_verified = FALSE) without verified parental consent — no partial processing permitted |
| **3** | Agent MUST NOT allow special category data (intelligence profiles, biometric, health, Aadhaar, caste) processing without explicit, purpose-specific, granular consent |
| **4** | Agent MUST NOT store raw Aadhaar numbers — only VID or UIDAI-approved tokenised reference |
| **5** | Agent MUST NOT allow cross-border data transfer without a registered, valid transfer mechanism (adequacy, SCC, BCR, or approved derogation) |
| **6** | Agent MUST NOT allow payment card data or UPI credentials to be stored outside India (RBI data localisation rule) |
| **7** | Agent MUST NOT delay breach notification beyond statutory timelines — 6h CERT-In, 72h DPBI/GDPR, immediate for UIDAI/minor breaches |
| **8** | Agent MUST NOT allow erasure of audit trail records, financial records, or legally held data even on valid DSR erasure request — exemptions must be documented in DSR response |
| **9** | Agent MUST NOT permit self-approval of DPIAs — DPO review is mandatory and independent |
| **10** | Agent MUST NOT autonomously determine that a DPIA is not required for CRITICAL processing purposes — human DPO confirmation is mandatory |
| **11** | Agent MUST NOT suppress, delay, or route around breach notifications to regulators for any business reason |
| **12** | Agent MUST NOT use LLM or generative AI in any compliance determination, lawful basis resolution, or regulatory classification |
| **13** | Agent MUST NOT allow consent to be bundled — each processing purpose and data category requires separate, granular consent |
| **14** | Agent MUST NOT make consent a condition of platform access for non-essential processing — core service access MUST NOT be withheld for refusal of optional consent |
| **15** | Agent MUST NOT modify, delete, or overwrite any consent record, DPIA, RoPA entry, breach record, or compliance audit log |
| **16** | Agent MUST NOT process EU-resident data without appointing a GDPR Article 27 EU representative if the platform lacks an EU establishment |
| **17** | Agent MUST NOT allow automated decisions with significant legal effects on data subjects without providing meaningful human review on request (GDPR Art.22 + EU AI Act) |
| **18** | Agent MUST NOT apply a less protective compliance standard to any tenant than the platform-level compliance floor — tenants can only exceed protections, never reduce them |
| **19** | Agent MUST NOT process data beyond the declared retention period without a separately documented legal basis for extended retention |
| **20** | Agent MUST NOT treat this specification as legal advice — all compliance determinations involving regulatory ambiguity must be referred to qualified legal counsel licensed in the relevant jurisdiction |
| **21** | Agent MUST NOT allow Dojo session recordings, intelligence profiles, or behavioural assessment data to be shared with any third party without explicit, specific, informed consent from the data subject (or parent if minor) — no legitimate interest basis applies to this category |
| **22** | Agent MUST NOT permit any processing of children's data for behavioural advertising, profiling for commercial purposes, or sale/sharing for consideration — absolute prohibition regardless of consent |

---

```
╔═══════════════════════════════════════════════════════════════════════════╗
║            AGENT SPECIFICATION COMPLETE                                  ║
║            RCMA-003 · v1.0.0 · SEALED · LOCKED · ADD-ONLY               ║
║            Ecoskiller Antigravity — Intelligence & Innovation Core       ║
║                                                                          ║
║  ⚖  LEGAL DISCLAIMER: This specification maps technical controls to     ║
║     regulatory obligations. It does NOT constitute legal advice.         ║
║     All compliance determinations must be reviewed by qualified          ║
║     legal counsel licensed in the relevant jurisdiction before           ║
║     any regulatory filing or public compliance claim is made.            ║
╚═══════════════════════════════════════════════════════════════════════════╝
```
