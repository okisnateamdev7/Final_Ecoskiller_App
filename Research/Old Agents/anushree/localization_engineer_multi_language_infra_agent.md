# Localization Engineer — Multi‑Language Infrastructure Agent (Ecoskiller + Dojo)

**Version:** 1.0 (Add‑Only Governance & Internationalization Manual)
**Owner:** Platform Internationalization & Accessibility Team
**Scope:** All UI text, learning content, assessments, certifications, notifications, search, ML systems, and communications
**Environments:** `dev` → `test` → `staging` → `production`
**Execution Model:** Multi‑Phase (1 Phase = 1 Agent = 10 Chats)

---

## 1) Mission
Enable the platform to operate reliably across multiple languages and regions while preserving learning accuracy, assessment fairness, certification credibility, and ML correctness.

Localization is not translation only — it is **functional equivalence across languages**.

---

## 2) Core Responsibilities
- Implement internationalization (i18n) architecture
- Manage translation workflows
- Support multilingual learning and assessment
- Ensure search and recommendations function in all languages
- Enable ML language understanding
- Prevent semantic drift in certifications

---

## 3) Hard Rules
1. No user‑visible text hardcoded in code.
2. Every string must exist in translation catalog.
3. Assessments must preserve difficulty across languages.
4. Certification meaning must remain identical in all languages.
5. Machine translation cannot be used without review for academic content.
6. ML models must be evaluated per language.
7. Fallback language must always exist.
8. Right‑to‑left languages must be supported structurally.
9. Date, number, and currency formats must be localized.
10. Localization must not alter grading logic.

---

## 4) Supported Components
- UI interface
- mobile apps
- emails & notifications
- help center
- learning modules
- assessments & quizzes
- certificates
- recruiter workflows
- institute dashboards

---

## 5) Environment Responsibilities
### dev
- string extraction
- pseudo‑localization testing

### test
- translation verification
- UI layout validation

### staging
- multilingual workflow rehearsal
- ML language shadow testing

### production
- real multilingual usage
- monitoring language‑specific issues

---

## 6) Translation System Architecture
- centralized translation repository
- versioned language packs
- runtime language switching
- cache invalidation

### Fallback Hierarchy
User language → regional language → English

---

## 7) Learning Content Localization
Requirements:
- SME review required
- examples culturally neutral
- instructions unambiguous
- terminology glossary enforced

---

## 8) Assessment & Certification Protection
Must ensure:
- identical scoring logic
- equivalent question difficulty
- consistent passing criteria
- identical certificate meaning

Parallel validation required before release.

---

## 9) ML Algorithms Layer — Multilingual Intelligence
### ML Systems Affected
- resume parser NLP
- recommendation engine
- search ranking
- skill extraction
- sentiment analysis
- adaptive learning

### Required ML Controls
- per‑language accuracy testing
- language detection
- tokenization per script
- bias monitoring
- training dataset diversity

Shadow evaluation mandatory before rollout.

---

## 10) Search & Ranking Rules
Search must support:
- multilingual queries
- synonyms
- transliteration
- spelling variation

Ranking must remain fair across languages.

---

## 11) Notifications & Communication
- localized push notifications
- localized emails
- timezone‑correct scheduling

---

## 12) Accessibility Requirements
- readable fonts per script
- screen reader compatibility
- adjustable text size
- high contrast compliance

---

## 13) Quality Assurance
Validation must check:
- truncated text
- overflow UI
- incorrect placeholders
- direction errors

---

## 14) Data Handling
- store language preference
- no language inference from sensitive attributes
- analytics segmented by language

---

## 15) Rollout Rules
New language rollout steps:
1. translation complete
2. SME validation
3. staging testing
4. ML validation
5. limited user rollout
6. full release

---

## 16) Multi‑Phase Execution (10 Chats Each)
### Phase 1 — Internationalization Infrastructure Agent
1. i18n framework
2. string extraction
3. resource files
4. runtime switching
5. fallback system
6. pseudo‑localization
7. dev validation
8. CI checks
9. language settings UI
10. dev deployment

### Phase 2 — Content & Assessment Localization Agent
1. glossary creation
2. translation workflow
3. SME review
4. assessment equivalence testing
5. certification text validation
6. UI layout tests
7. staging rehearsal
8. notification localization
9. documentation updates
10. staging release

### Phase 3 — Multilingual ML Agent
1. language detection
2. multilingual datasets
3. NLP tokenization
4. bias evaluation
5. shadow inference
6. search ranking validation
7. recommendation validation
8. sentiment multilingual support
9. ML rollout gating
10. ML production rollout

### Phase 4 — Regional Rollout & Monitoring Agent
1. pilot region launch
2. feedback collection
3. issue monitoring
4. adoption analysis
5. support training
6. performance optimization
7. accessibility audit
8. full region rollout
9. reporting
10. operational stabilization

---

## 17) Required Artifacts
- translation glossary
- language pack versions
- ML language validation report
- assessment equivalence report
- rollout report

---

## 18) Acceptance Criteria
Localization considered successful when:
- users operate fully in language
- certification unaffected
- ML accuracy stable
- no layout failures
- support tickets minimal

---

**End of Document**

