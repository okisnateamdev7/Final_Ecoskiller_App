# 🔐 TENANT_EXPORT_AGENT.md - ANTIGRAVITY SEALED DATA PORTABILITY FRAMEWORK
**Enterprise SaaS Data Portability Architecture | Ecoskiller Technology Inheritance**

---

## ⚠️ DOCUMENT CLASSIFICATION
- **STATUS**: SEALED & LOCKED
- **EXECUTION_MODE**: IMMUTABLE
- **MUTATION_POLICY**: READ-ONLY (except audit trail additions)
- **CREATIVE_INTERPRETATION**: FORBIDDEN
- **ASSUMPTION_FILLING**: BLOCKED
- **TIMESTAMP**: 2026-02-24
- **PLATFORM**: Antigravity (Ecoskiller Derivative)
- **EXPORT_SCOPE**: Data portability, tenant migration, format standardization, compliance assurance

---

## 🎯 EXECUTIVE SUMMARY

This document establishes the **TENANT_EXPORT_AGENT** framework for **Antigravity** - a multi-tenant enterprise SaaS platform operating across education, hiring, and skill development verticals. The TENANT_EXPORT_AGENT operates as a sealed system responsible for:

1. **Data Portability** (GDPR Article 20, CCPA, user request fulfillment)
2. **Tenant Migration** (institutional data export, switching platforms)
3. **Format Standardization** (JSON, CSV, XML, database dumps)
4. **Compliance Assurance** (GDPR 30-day SLA, CCPA 45-day SLA)
5. **Data Integrity** (checksums, encryption in transit, verification)
6. **Timeliness** (automated processing, real-time status, notifications)
7. **Completeness** (all data included, no selective omission)

---

## 📋 PART 1: DATA PORTABILITY FRAMEWORK (SEALED)

### 1.1 FUNDAMENTAL PRINCIPLES (LOCKED)

```yaml
PRINCIPLE_1_PORTABILITY_IS_RIGHT:
  LEGAL_BASIS:
    - GDPR Article 20: "Right to data portability"
    - CCPA §1798.100: "Right to know what personal information is collected"
    - India IT Act Section 43A: "Data access and portability"
    - FERPA (US Education): Student record access by student
    - EEOC: Employment record access by employee
  
  DEFINITION:
    "Every data subject (user, student, employee) has the unconditional
    right to obtain and reuse personal data across different services.
    Antigravity must provide data in portable, machine-readable format
    within regulatory timeline."
  
  SCOPE:
    Includes: Personal data (name, email, profile, activity, scores, achievements)
    Excludes: Platform systems data (logs, security, operational), third-party rights
    Timing: 30 days (GDPR), 45 days (CCPA), per regulatory requirement
    Cost: FREE (cannot charge for export request)
    Format: User's choice (JSON, CSV, XML - at least 2 formats)
    Encryption: Optional (user can request encrypted export)
  
  ENFORCEMENT:
    - No restrictions (unconditional right)
    - No authorization barriers (user can request own data only)
    - Automated processing (where possible)
    - Escalation triggers (if cannot fulfill in SLA)

PRINCIPLE_2_INSTITUTIONAL_DATA_CONTROL:
  DEFINITION:
    "Institutions (schools, enterprises) own their institutional data.
    Antigravity must provide full institutional export on request.
    Export includes all data collected within institutional context."
  
  INSTITUTIONAL_EXPORT_SCOPE:
    Includes:
      - All student/employee data (collected by institution)
      - Enrollment records, grades, assessments
      - Hiring data, applications, offers
      - Institutional analytics, reports
      - Configuration, customization, settings
      - Integration data, third-party connections
      - Audit logs, activity records
      - Backups (if requested for migration)
    
    Excludes:
      - Other institutions' data (hard segregation)
      - Platform system data (Antigravity operations)
      - Unrelated third-party data
      - Data from deleted/closed accounts (unless in retention)
  
  AUTHORIZATION:
    - Institution admin only (not individual users)
    - Bulk export capability
    - Scheduled exports (daily/weekly/monthly)
    - Migration assistance included
    - Zero cost (no premium charges)
  
  ENFORCEMENT:
    - Database isolation enforced
    - API validation per tenant
    - Audit trail for all exports
    - Notification to institution (transparency)

PRINCIPLE_3_NO_VENDOR_LOCK_IN:
  DEFINITION:
    "Antigravity shall not create artificial barriers to portability.
    Data must be exported in open formats (not proprietary). No
    encryption of export data with Antigravity keys. Switching platforms
    must be technically feasible and economically reasonable."
  
  PROHIBITED_CONDUCT:
    ✗ Encrypt export with Antigravity-only keys (prevents opening elsewhere)
    ✗ Proprietary format exports (forces continued Antigravity usage)
    ✗ Selective data exclusion (omits critical data to trap customers)
    ✗ High-cost exports (charges prohibit portability)
    ✗ Fragmented exports (require Antigravity tools to reconstruct)
    ✗ Delayed exports (miss regulatory timeline = SLA violation)
    ✗ Obfuscated data (deliberately hard-to-understand format)
    ✗ Incomplete exports (missing data, metadata, relationships)
  
  REQUIRED_CONDUCT:
    ✓ Open formats (JSON, CSV, standard XML)
    ✓ Plain text (no binary encoding, no Antigravity-specific compression)
    ✓ Complete data (all personal data, all relationships)
    ✓ Metadata included (field descriptions, data dictionary)
    ✓ Relationships documented (user → enrollments → courses, etc.)
    ✓ Timestamps preserved (when data created, modified, deleted)
    ✓ Encryption optional (user can request if paranoid)
    ✓ Migration assistance (documentation, technical support)
  
  ENFORCEMENT:
    - Data format audit: Quarterly verification of format openness
    - Competitor testing: Can competing platform import exports?
    - User testing: Can average user open/understand export?
    - Third-party audit: Annual verification of portability
    - Compliance: Board certification of no vendor lock-in

PRINCIPLE_4_DATA_INTEGRITY_GUARANTEED:
  DEFINITION:
    "Exported data must be accurate, complete, unaltered, and verifiable.
    User/institution can verify export integrity (checksums, signatures)."
  
  INTEGRITY_MECHANISMS:
    Checksums:
      - SHA-256 hash of entire export
      - Per-file checksums (if multi-file export)
      - Published alongside export (for verification)
    
    Signatures:
      - Digital signature of export (proves Antigravity authenticity)
      - Optional (user can request for tamper detection)
      - Public key provided (for verification)
    
    Metadata:
      - Export date/time (timestamp of export)
      - Data scope (what's included, what's excluded)
      - Record count (total records exported)
      - Completeness statement (certified complete)
      - Modification timestamp (when data last updated)
    
    Validation:
      - Record count matches database (verified before export)
      - Referential integrity maintained (foreign keys valid)
      - No truncation/corruption (binary perfect copy)
      - Character encoding preserved (UTF-8)
      - Date formats consistent (ISO 8601)
  
  ENFORCEMENT:
    - Automated validation (all exports validated before sending)
    - User notification (include checksum, signature in export)
    - Third-party verification (user can hash-verify received export)
    - Audit trail (what was exported, when, by whom)
    - Remediation SLA (if export corrupted, re-export within 24 hours)

PRINCIPLE_5_TIMELINE_COMPLIANCE_MANDATORY:
  DEFINITION:
    "Export requests must be fulfilled within regulatory timeline.
    No extensions granted except technical impossibility (documented)."
  
  REGULATORY_TIMELINES:
    GDPR_Article_20:
      Timeline: 30 days from request
      Extensions: Only if 'complex' (documented, max 60 days total)
      Definition_Complex: Multiple data sources, >10,000 records, complex relationships
      Responsibility: Antigravity must prove complexity (burden on Antigravity)
    
    CCPA_Section_1798:
      Timeline: 45 days from request
      Extensions: One 45-day extension (max 90 days total, only if complex)
      Definition_Complex: Multiple verifications, aggregated sources
      Responsibility: Antigravity must document reason
    
    India_IT_Act:
      Timeline: "Reasonable time" (no fixed timeline, but interpret strictly = 30 days)
      Extensions: None documented (aim for 7-14 days for Indian users)
      Responsibility: Antigravity defines reasonable based on GDPR standard
    
    FERPA:
      Timeline: 5 business days (US education standard)
      Extensions: None
      Responsibility: Antigravity must comply strictly
  
  TIMELINE_TRACKING:
    Request_Receipt: Timestamp when request received
    Confirmation: Send confirmation email within 24 hours
    Processing: Status updates (weekly)
    Completion: Deliver export with completion notification
    Deadline: Calendar reminder (day before SLA expires)
    Escalation: If cannot meet SLA, escalate to counsel (day -5 before deadline)
  
  ENFORCEMENT:
    - Automated tracking (request database with timeline)
    - Calendar alerts (90 days, 60 days, 30 days, 7 days, 1 day before deadline)
    - SLA breach tracking (metric: % requests completed on-time)
    - Target: 100% on-time delivery (regulatory requirement)
    - Remediation: If missed SLA, user entitled to compensation (if applicable law)
    - Board reporting: Monthly SLA metrics to audit committee

PRINCIPLE_6_FORMAT_FLEXIBILITY:
  DEFINITION:
    "User/institution chooses export format. Antigravity must support
    multiple formats. No single 'official' format. User-directed choices
    respected."
  
  MINIMUM_FORMATS:
    Format_1_JSON:
      Scope: Complete export (all data, relationships)
      Structure: Nested objects, arrays, full referential integrity
      File_naming: antigravity_export_[date].json
      Size_limit: No limit (can be 1GB+ for large institutions)
      Compression: Optional (user can request .json.gz)
    
    Format_2_CSV:
      Scope: Tabular data (users, enrollments, jobs, applications)
      Structure: Flat tables, with foreign key columns
      File_naming: [entity]_export_[date].csv (users.csv, enrollments.csv, etc.)
      Size_limit: Single CSV can be large (no chunking without request)
      Relationships: Foreign keys preserved (user_id in enrollments.csv references users.csv)
    
    Format_3_XML:
      Scope: Structured data (alternative to JSON)
      Structure: Hierarchical, with schema (XSD)
      File_naming: antigravity_export_[date].xml
      Schema: User can request schema (XSD) for validation
      Relationships: Nested or reference elements
    
    Format_4_Database_Dump:
      Scope: Raw database schema + data (for institutional migrations)
      Type: PostgreSQL dump (pg_dump format)
      Structure: Complete schema, all tables, relationships, constraints
      Size: Can be very large (gigabytes for large institutions)
      Restore: User can restore to own PostgreSQL instance
      Availability: Institution-only (not individual users)
    
    Format_5_PDF_Report:
      Scope: Human-readable summary (for individual users)
      Content: Profile data, achievements, certifications, activity summary
      Design: Professional, organized, easy-to-read
      Use_case: Individual portfolio, proof of skills
      Limit: Individual users only (institutions get database dumps)
  
  USER_SELECTION:
    Portal: User selects format during export request
    Options: Show all available formats
    Recommendation: Suggest appropriate format (bulk institutional data → DB dump)
    Fallback: Default to JSON if user doesn't specify
    Combination: Can user request multiple formats? YES (both JSON + CSV)
  
  ENFORCEMENT:
    - Support all formats in request UI
    - Generate all requested formats
    - Deliver all in single ZIP (if multiple)
    - Include manifest (list of files + descriptions)
    - Schema/dictionary (explain field meanings)

PRINCIPLE_7_TRANSPARENCY_IN_EXPORT:
  DEFINITION:
    "Export must be transparent. User/institution understands exactly
    what's being exported. Included data is clear. Excluded data is
    documented. No hidden/obfuscated data."
  
  TRANSPARENCY_MECHANISMS:
    Pre_Export_Preview:
      - Show what will be exported (data summary)
      - Show data categories (users, enrollments, jobs, applications)
      - Show record counts (X students, Y enrollments, Z jobs)
      - Show relationships (how data connected)
      - Estimated file size (so user knows what to expect)
      - Estimated time to download (if very large)
    
    Export_Manifest:
      - File: manifest.txt (in every export)
      - Contents:
        - Export date/time
        - User/institution requesting
        - Data scope (what's included)
        - Record counts (per entity)
        - Excluded data (what's not included + why)
        - Format description (how to read the files)
        - Field descriptions (data dictionary)
        - Relationships (how entities connect)
        - Encryption status (if encrypted, how to decrypt)
        - Checksum/signature (for verification)
        - Support contact (if import questions)
    
    Data_Dictionary:
      - File: data_dictionary.csv or schema.json
      - Columns: field_name, data_type, description, required, example
      - Complete: Every field documented
      - Clear: Non-technical users can understand
      - Examples: Sample values for reference
    
    Excluded_Data_Log:
      - File: excluded_data.txt
      - Rationale: For each excluded category, explain why
      - Examples:
        - "System logs excluded: Technical operations data"
        - "Third-party integrations excluded: Customer data of other parties"
        - "Deleted user data excluded: Retained only if in retention period"
        - "Deleted records excluded: Permanently deleted records not included"
      - Contact: Support email for questions about exclusions
  
  ENFORCEMENT:
    - Manifest required (every export must include)
    - User review: User reviews manifest before download
    - Completeness check: User verifies record count matches expectation
    - Support: Help desk accessible for export questions
    - Feedback: User satisfaction survey on export quality
```

### 1.2 EXPORT TYPES & SCENARIOS (SEALED)

```yaml
EXPORT_SCENARIO_1_INDIVIDUAL_USER_REQUEST:
  Trigger:
    - User clicks "Download My Data" (portal)
    - User submits formal request (email, form)
    - GDPR Article 20 request
    - CCPA "Right to Know" request
  
  User_Scope:
    - Personal data only (own profile, activity)
    - Enrollments where user is student/participant
    - Applications where user is applicant
    - Assessments where user is test-taker
    - Certifications, badges, achievements user earned
    - Messages user sent/received
    - Activity log (user's own activities)
  
  Excluded_Scope:
    ✗ Other users' personal data (privacy of others)
    ✗ Institutional data (user doesn't own)
    ✗ System logs (technical, not user's data)
    ✗ Metadata about deleted items (permanently gone)
    ✗ Other users' messages (even if addressed to user)
  
  Processing:
    1. User submits request (portal or email)
    2. Verification: Authenticate user (must be logged in OR email verification)
    3. Confirmation: Email confirmation of request + timeline
    4. Processing: Automated job queries user's data
    5. Generation: Create export in requested format
    6. Validation: Verify export completeness + integrity
    7. Delivery: Email download link (expires 7 days)
    8. Notification: "Your export is ready" email
  
  SLA:
    - GDPR: 30 days (can extend to 60 if complex)
    - CCPA: 45 days (can extend to 90 if complex)
    - Target: Complete within 7 days (not complex)
    - High-volume period: Scale workers (queue system)
  
  Technical_Implementation:
    ```
    async function exportUserData(userId, format) {
      // 1. Verify user owns this userId
      const user = await db.query(
        'SELECT id FROM users WHERE id = ? AND tenant_id = ?',
        [userId, req.user.tenant_id]
      );
      if (!user) throw new Error('Unauthorized');
      
      // 2. Query all user data
      const userData = {
        profile: await getUserProfile(userId),
        enrollments: await getUserEnrollments(userId),
        applications: await getUserApplications(userId),
        assessments: await getUserAssessments(userId),
        achievements: await getUserAchievements(userId),
        messages: await getUserMessages(userId),
        activityLog: await getUserActivityLog(userId)
      };
      
      // 3. Generate export (format-specific)
      let exportData;
      if (format === 'json') {
        exportData = JSON.stringify(userData, null, 2);
      } else if (format === 'csv') {
        exportData = generateCSVTables(userData);
      }
      
      // 4. Calculate checksum
      const checksum = sha256(exportData);
      
      // 5. Store export (temporary bucket)
      const exportId = await storeExport(userId, exportData, checksum);
      
      // 6. Generate download link
      const downloadLink = generateSecureLink(exportId, expiresIn: 7 days);
      
      // 7. Send email
      await emailService.send({
        to: user.email,
        subject: 'Your Data Export is Ready',
        template: 'export_ready',
        data: {
          downloadLink,
          checksum,
          recordCount: userData.enrollments.length,
          exportDate: new Date()
        }
      });
      
      return { status: 'complete', exportId };
    }
    ```
  
  Security:
    - Authentication: User must be logged in (or email link)
    - Encryption: Export not stored encrypted (but TLS in transit)
    - Access: Only user can download (unique token)
    - Expiration: Link expires after 7 days
    - Logging: All downloads logged (audit trail)
    - Cleanup: Export deleted after 30 days (if not downloaded)

EXPORT_SCENARIO_2_INSTITUTIONAL_DATA_EXPORT:
  Trigger:
    - Institution admin requests full institutional export
    - Migration to competing platform
    - Data backup for compliance
    - Disaster recovery planning
    - Institutional IT audit
    - Contract termination
  
  Institution_Scope:
    - All students enrolled in institution
    - All employees in institution
    - All hiring records (if company)
    - All skill assessments conducted
    - All projects completed
    - All institutional analytics
    - All configuration + customization
    - All integration data
    - All activity logs (institution-level)
  
  Excluded_Scope:
    ✗ Other institutions' data (hard tenant segregation)
    ✗ Platform system data (Antigravity operations)
    ✗ Recruiting data from non-affiliated recruiters
    ✗ Third-party confidential data (partners' data)
  
  Processing:
    1. Institution admin initiates request (admin portal)
    2. Authorization: Verify admin role (must be tenant admin)
    3. Scope selection: Choose what to include (students, jobs, assessments, etc.)
    4. Format selection: Choose format (DB dump preferred for institutional)
    5. Scheduling: Choose when to export (can schedule for off-peak)
    6. Confirmation: Email confirmation (larger institutions may take time)
    7. Processing: Async job extracts all institutional data
    8. Validation: Verify no cross-tenant data included (critical security check)
    9. Delivery: Download available (usually within 24 hours)
    10. Documentation: Migration guide + API documentation provided
  
  SLA:
    - Standard export: 24 hours (next business day)
    - Large institutional data (>100K records): 72 hours (agreed)
    - Urgent (documented reason): Same-day (within 8 hours)
    - Target: 99% on-time delivery
  
  Formats_Available:
    - PostgreSQL dump (recommended for large institutions)
    - JSON (nested, complete data relationships)
    - CSV tables (flat, individual tables)
    - XML (structured, with schema)
    - All formats simultaneously (user selects)
  
  Technical_Implementation:
    ```
    async function exportInstitutionalData(tenantId, options) {
      // 1. Verify tenant admin
      const admin = await verifyTenantAdmin(tenantId);
      
      // 2. Query institutional data (scoped to tenant_id)
      const data = {
        users: await db.query(
          'SELECT * FROM users WHERE tenant_id = ?', [tenantId]
        ),
        enrollments: await db.query(
          'SELECT * FROM enrollments WHERE tenant_id = ?', [tenantId]
        ),
        jobs: await db.query(
          'SELECT * FROM jobs WHERE tenant_id = ?', [tenantId]
        ),
        applications: await db.query(
          'SELECT * FROM applications WHERE tenant_id = ?', [tenantId]
        ),
        // ... all institutional entities
      };
      
      // 3. Verify no cross-tenant data
      const crossTenantCheck = await verifySingleTenant(data);
      if (!crossTenantCheck) throw new Error('Cross-tenant data detected!');
      
      // 4. Generate exports
      const exports = {};
      if (options.formats.includes('json')) {
        exports.json = JSON.stringify(data, null, 2);
      }
      if (options.formats.includes('postgres')) {
        exports.postgres = await generatePostgresqlDump(data);
      }
      // ... other formats
      
      // 5. Create ZIP with all formats + documentation
      const zip = await createZip({
        files: exports,
        documentation: await generateMigrationGuide(),
        manifest: await generateManifest(data),
        apiDocs: API_DOCUMENTATION
      });
      
      // 6. Store and provide download
      const exportId = await storeExport(tenantId, zip);
      const downloadLink = generateSecureLink(exportId, expiresIn: 30 days);
      
      // 7. Notify institution
      await emailService.send({
        to: admin.email,
        subject: 'Your Institutional Data Export is Ready',
        template: 'institutional_export_ready',
        data: { downloadLink, recordCount: data.users.length }
      });
      
      return { status: 'complete', exportId };
    }
    ```
  
  Security:
    - Tenant isolation: Verify only institution's data included (critical)
    - Encryption: Optional (user can request encrypted ZIP)
    - Access: Only tenant admins can download
    - Expiration: Link expires after 30 days
    - Audit: Log all institutional exports (sensitive operation)
    - Notification: Notify institution when export downloaded
  
  Migration_Assistance:
    - Documentation: Step-by-step migration guide
    - API_docs: How to integrate data into new system
    - Support_contact: Dedicated migration support team
    - Schema_mapping: How Antigravity schema maps to standard formats
    - Troubleshooting: Common import issues + solutions
    - Timeline: Realistic estimates for migration

EXPORT_SCENARIO_3_REGULATORY_REQUEST:
  Trigger:
    - Data Protection Authority (DPA) request (GDPR Article 58)
    - Court order / subpoena
    - Law enforcement inquiry
    - Regulatory agency request (EEOC, NISM, CCI)
  
  Processing:
    1. Legal review: Counsel assesses request (legitimate? scope?)
    2. Verification: Confirm legal instrument (court order, warrant, DPA demand)
    3. Notification: Notify user/institution (unless prohibited by law)
    4. Compliance: Provide requested data (no withholding allowed)
    5. Transparency: Log all legal requests (transparency report)
  
  Data_Scope:
    - Exact scope per legal instrument
    - Can request: Specific user data, institutional data, system logs
    - Cannot request: Unrelated users' data (scope must be specific)
  
  Timeline:
    - Standard response: 20 days (vary per jurisdiction)
    - Urgent (law enforcement): 48 hours (if requested)
    - DPA request: 30 days (GDPR standard)
  
  Documentation:
    - Keep copy of legal instrument (for records)
    - Document what data provided
    - Document dates (receipt, notification, provision)
    - Maintain legal hold (don't delete related data)

EXPORT_SCENARIO_4_DATA_DELETION_REQUEST:
  Trigger:
    - User requests account deletion
    - Institution requests user data deletion
    - GDPR "Right to Erasure" request
    - CCPA deletion request
    - Retention period expires
  
  Pre_Deletion_Export:
    - Offer: Automatically offer export before deletion
    - Format: All formats available (JSON, CSV, PDF)
    - Timeline: Provide 30 days to download before hard delete
    - Notification: "Your account is scheduled for deletion. Download your data now."
  
  Deletion_Process:
    1. Soft delete: Mark account as deleted (data retained, not visible)
    2. Access revoked: User cannot log in
    3. Export period: User can request export for 30 days
    4. Hard delete: After 30 days (or per retention policy), delete permanently
    5. Cryptographic shredding: Overwrite or destroy encryption keys
  
  Audit_Trail:
    - Log deletion request (user, date, reason)
    - Log soft delete (timestamp)
    - Log export downloads (user downloaded backup before deletion)
    - Log hard delete (timestamp, method)
    - Retention: Keep audit trail (7 years)

EXPORT_SCENARIO_5_AUTOMATED_PERIODIC_EXPORT:
  Trigger:
    - Institution schedules recurring exports (daily, weekly, monthly)
    - Backup purposes
    - Compliance archival
    - Disaster recovery planning
  
  Configuration:
    - Frequency: Daily, weekly, monthly (user selects)
    - Format: User selects (DB dump, JSON, CSV)
    - Timing: Off-peak hours (e.g., 2 AM)
    - Storage: Send to S3, SFTP, or email
    - Retention: Keep last N exports (user-defined)
  
  Technical:
    ```
    // Scheduled job (every day at 2 AM UTC)
    const schedule = require('node-schedule');
    
    schedule.scheduleJob('0 2 * * *', async () => {
      const institutions = await getInstitutionsWithScheduledExports();
      
      for (const institution of institutions) {
        try {
          // Export institutional data
          const data = await exportInstitutionalData(institution.id);
          
          // Send to destination
          if (institution.s3Bucket) {
            await s3.putObject({
              Bucket: institution.s3Bucket,
              Key: `exports/antigravity_${new Date().toISOString()}.json`,
              Body: data
            });
          }
          
          // Log successful export
          await logExportEvent(institution.id, 'success');
        } catch (error) {
          // Alert on failure
          await alertOps(`Export failed for ${institution.id}: ${error.message}`);
        }
      }
    });
    ```
  
  Failure_Handling:
    - Retry: Automatic retry (3 times before alerting)
    - Notification: Email if export fails
    - Escalation: Ops team notified (day after failure)
    - Manual_override: Admin can trigger manual export anytime

EXPORT_SCENARIO_6_THIRD_PARTY_INTEGRATION_EXPORT:
  Trigger:
    - User authorizes third-party app to access their data
    - OAuth 2.0 integration
    - API-based data access
    - Calendar sync, CRM integration, etc.
  
  Data_Access_Control:
    - User consent required (explicit, granular)
    - Scopes limited (only what user authorized)
    - Token expiration (refresh tokens, access tokens)
    - Revocable: User can revoke access anytime
  
  User_Consent_Flow:
    1. User initiates "Connect to [App]"
    2. OAuth redirect to Antigravity
    3. Permission screen: Clear list of what's being accessed
    4. User approves specific scopes
    5. Grant authorization code
    6. Third-party app exchanges for access token
    7. App can now query user's data (via API)
  
  API_Endpoints:
    - /api/v1/me/profile (user profile)
    - /api/v1/me/enrollments (user enrollments)
    - /api/v1/me/achievements (badges, certifications)
    - /api/v1/me/applications (job applications)
    - /api/v1/me/data/export (full user data export)
  
  Rate_Limiting:
    - Per_user: 1000 requests/day per app
    - Per_tenant: 10,000 requests/day total
    - Burst: 10 requests/second max
    - Enforcement: 429 Too Many Requests if exceeded
  
  Data_Minimization:
    - Only expose required data (not all)
    - Aggregate where possible (don't expose raw user IDs)
    - No financial PII (SSN, payment info)
    - No other users' data (only requesting user's data)
  
  Logging:
    - Log all API calls (audit trail)
    - Record which data accessed (detailed)
    - Monitor for anomalies (bulk downloads)
    - Escalate suspicious activity (potential breach)
```

---

## 🔄 PART 2: DATA FORMAT & SCHEMA SPECIFICATIONS (SEALED)

### 2.1 STANDARD EXPORT FORMATS

```yaml
FORMAT_1_JSON_EXPORT:
  Purpose: Complete, nested data export (recommended)
  File_Structure:
    ```json
    {
      "export_metadata": {
        "export_date": "2026-02-24T10:30:00Z",
        "exported_by": "user_12345",
        "data_subject": "John Doe",
        "tenant": "university_college",
        "version": "1.0",
        "format_version": "1.0",
        "checksum": "sha256:abc123...",
        "record_count": {
          "users": 1,
          "enrollments": 5,
          "assessments": 20,
          "achievements": 8
        }
      },
      "user": {
        "id": "user_12345",
        "email": "john@example.com",
        "first_name": "John",
        "last_name": "Doe",
        "phone": "+1-555-1234",
        "profile_pic": "https://...",
        "domain": "technology",
        "tenant_id": "tenant_456",
        "created_at": "2024-01-15T08:00:00Z",
        "updated_at": "2026-02-24T09:00:00Z",
        "is_deleted": false
      },
      "enrollments": [
        {
          "id": "enrollment_1",
          "user_id": "user_12345",
          "course_id": "course_789",
          "course_name": "System Design Masterclass",
          "status": "completed",
          "enrolled_date": "2024-01-20T10:00:00Z",
          "completed_date": "2024-06-30T18:00:00Z",
          "score": 92,
          "certificate": "https://..."
        }
        // ... more enrollments
      ],
      "assessments": [
        {
          "id": "assessment_1",
          "enrollment_id": "enrollment_1",
          "assessment_type": "quiz",
          "title": "System Design Quiz 1",
          "score": 95,
          "total_score": 100,
          "percentage": 95,
          "attempted_date": "2024-03-15T14:30:00Z",
          "time_spent_seconds": 1800
        }
        // ... more assessments
      ],
      "achievements": [
        {
          "id": "achievement_1",
          "user_id": "user_12345",
          "badge_id": "badge_design",
          "badge_name": "System Design Expert",
          "earned_date": "2024-06-30T18:00:00Z",
          "description": "Completed all system design courses"
        }
        // ... more achievements
      ],
      "activity_log": [
        {
          "id": "activity_1",
          "user_id": "user_12345",
          "action": "course_completed",
          "resource": "course_789",
          "timestamp": "2024-06-30T18:00:00Z",
          "ip_address": "192.168.1.100"
        }
        // ... more activities
      ]
    }
    ```
  
  Advantages:
    - Complete data relationships (nested)
    - Machine-readable
    - Easily imported to modern systems
    - Supports arrays, objects, null values
  
  File_Size: Can be large (1GB+ for institutions)
  Compression: Optional (.json.gz recommended for large files)
  Charset: UTF-8 (with BOM for Windows compatibility)

FORMAT_2_CSV_EXPORT:
  Purpose: Tabular data (spreadsheet-friendly)
  File_Structure:
    ```
    Multiple CSV files (one per entity):
    - users.csv
    - enrollments.csv
    - assessments.csv
    - achievements.csv
    - activity_log.csv
    
    Example: users.csv
    id,email,first_name,last_name,phone,domain,created_at,updated_at
    user_12345,john@example.com,John,Doe,+1-555-1234,technology,2024-01-15T08:00:00Z,2026-02-24T09:00:00Z
    user_12346,jane@example.com,Jane,Smith,+1-555-5678,commerce,2024-02-01T09:00:00Z,2026-02-24T10:00:00Z
    
    Example: enrollments.csv
    id,user_id,course_id,course_name,status,enrolled_date,completed_date,score,certificate
    enrollment_1,user_12345,course_789,System Design Masterclass,completed,2024-01-20T10:00:00Z,2024-06-30T18:00:00Z,92,https://...
    enrollment_2,user_12345,course_790,Data Structures,completed,2024-02-15T11:00:00Z,2024-07-15T19:00:00Z,88,https://...
    ```
  
  Specifications:
    - Delimiter: Comma (,) as standard
    - Quoting: Double-quotes (") for fields with commas
    - Escaping: Double-quote for quotes inside fields ("" = ")
    - Headers: First row contains field names
    - Charset: UTF-8 (with BOM for Windows)
    - Line_endings: CRLF (\r\n) for Windows, LF (\n) for Unix
    - Null: Empty cells represent NULL values
    - Dates: ISO 8601 format (2026-02-24T10:30:00Z)
  
  Foreign_Keys:
    - Preserved as IDs (e.g., user_id in enrollments table)
    - No joins performed (relationship tables separate)
    - User must join on IDs in their own system
  
  Advantages:
    - Opens in Excel/Sheets immediately
    - Easy to understand
    - Can import to any database
  
  Disadvantages:
    - Relationships are IDs (must join tables)
    - Large exports create many files
    - Circular relationships difficult to represent

FORMAT_3_XML_EXPORT:
  Purpose: Structured, hierarchical data with schema
  File_Structure:
    ```xml
    <?xml version="1.0" encoding="UTF-8"?>
    <antigravity_export xmlns="http://antigravity.com/schema/export/1.0">
      <metadata>
        <export_date>2026-02-24T10:30:00Z</export_date>
        <data_subject>John Doe</data_subject>
        <tenant>university_college</tenant>
        <version>1.0</version>
        <checksum>sha256:abc123...</checksum>
      </metadata>
      <user>
        <id>user_12345</id>
        <email>john@example.com</email>
        <first_name>John</first_name>
        <last_name>Doe</last_name>
        <phone>+1-555-1234</phone>
        <domain>technology</domain>
        <created_at>2024-01-15T08:00:00Z</created_at>
        <updated_at>2026-02-24T09:00:00Z</updated_at>
      </user>
      <enrollments>
        <enrollment>
          <id>enrollment_1</id>
          <user_id>user_12345</user_id>
          <course_id>course_789</course_id>
          <course_name>System Design Masterclass</course_name>
          <status>completed</status>
          <enrolled_date>2024-01-20T10:00:00Z</enrolled_date>
          <score>92</score>
        </enrollment>
        <!-- more enrollments -->
      </enrollments>
      <assessments>
        <!-- assessment records -->
      </assessments>
    </antigravity_export>
    ```
  
  Schema:
    - XSD (XML Schema Definition) provided
    - Can validate exported XML against schema
    - Ensures data conforms to expected structure
  
  Advantages:
    - Structured, machine-readable
    - Hierarchical relationships clear
    - Schema validation possible
  
  Disadvantages:
    - Verbose (larger file size)
    - Less popular than JSON
    - Steeper learning curve

FORMAT_4_DATABASE_DUMP:
  Purpose: Raw database export (for institutional migration)
  Type: PostgreSQL pg_dump format
  Contents:
    ```sql
    -- PostgreSQL database dump
    -- Version 14
    -- Created on 2026-02-24 10:30:00
    
    -- User schema
    CREATE TABLE users (
      id UUID PRIMARY KEY,
      email VARCHAR(255) NOT NULL,
      first_name VARCHAR(100),
      last_name VARCHAR(100),
      phone VARCHAR(20),
      domain VARCHAR(50),
      tenant_id UUID NOT NULL,
      created_at TIMESTAMP,
      updated_at TIMESTAMP
    );
    
    COPY users FROM stdin;
    user_12345	john@example.com	John	Doe	+1-555-1234	technology	tenant_456	2024-01-15 08:00:00	2026-02-24 09:00:00
    user_12346	jane@example.com	Jane	Smith	+1-555-5678	commerce	tenant_456	2024-02-01 09:00:00	2026-02-24 10:00:00
    \.
    
    -- Enrollments schema + data
    CREATE TABLE enrollments (
      ...
    );
    
    -- ... more tables
    ```
  
  Restoration:
    ```bash
    # User can restore to their own PostgreSQL:
    psql -h localhost -U postgres -d antigravity_export < export.sql
    ```
  
  Advantages:
    - Complete database schema
    - Easy to restore to PostgreSQL
    - Preserves all constraints, sequences
    - Fastest migration path
  
  Disadvantages:
    - PostgreSQL-specific (cannot use with MySQL, etc.)
    - Large file (not for individual users)
    - Institutional-only (too complex for individuals)
  
  Security:
    - Dump includes schema (OK)
    - Does NOT include passwords (never included)
    - Does NOT include encryption keys
    - User responsible for securing dump (it contains all data)

FORMAT_5_PDF_REPORT:
  Purpose: Human-readable summary (individual users)
  Contents:
    - Profile summary (name, email, domain)
    - Achievements (badges, certifications, belt level)
    - Education history (courses completed, grades)
    - Skills verified (skill assessments, scores)
    - Project portfolio (projects completed)
    - Activity summary (total hours, achievements earned)
    - Career statistics (placement rate, salary range if public)
  
  Design:
    - Professional layout (Antigravity branding)
    - Organized sections
    - Charts/graphs where relevant
    - QR code to Antigravity profile (optional)
    - Easy to print
  
  Use_Cases:
    - Portfolio for job applications
    - Proof of skills for resume
    - LinkedIn profile supplement
    - Personal record keeping
  
  Security:
    - PDF encrypted (optional, user can request)
    - No embedded images (smaller file)
    - Shareable (user can give to recruiters)

FORMAT_COMPARISON_MATRIX:
  ┌────────────────────────────────────────────────────────────────┐
  │ Format │ Use Case │ Size │ Human-Readable │ Machine-Readable │
  ├────────────────────────────────────────────────────────────────┤
  │ JSON   │ General  │ Large│ Moderate       │ Excellent        │
  │ CSV    │ Spreadsheet│ Medium│ Good         │ Good             │
  │ XML    │ Enterprise│ Large│ Poor           │ Excellent        │
  │ DB Dump│ Migration│ Huge │ Poor           │ Excellent        │
  │ PDF    │ Portfolio│ Small│ Excellent      │ None             │
  └────────────────────────────────────────────────────────────────┘
```

### 2.2 DATA DICTIONARY SPECIFICATION

```yaml
DATA_DICTIONARY_FORMAT:
  File: data_dictionary.csv
  Required_Columns:
    - field_name: Name of field in export
    - table_name: Table/entity containing field
    - data_type: Type (string, integer, date, boolean, array, object)
    - description: Plain English explanation
    - required: Is field required? (yes/no)
    - nullable: Can field be null? (yes/no)
    - example: Example value
    - notes: Additional notes
  
  Example_Entry:
    field_name: email
    table_name: users
    data_type: string
    description: User's email address (unique per institution)
    required: yes
    nullable: no
    example: john@example.com
    notes: Email is used for account login and password reset
  
  Completeness:
    - Every field documented
    - Non-technical descriptions
    - Examples provided
    - Relationships explained

RELATIONSHIPS_DOCUMENTATION:
  File: relationships.md or relationships.csv
  Contains:
    - Foreign keys (which fields reference which tables)
    - One-to-many relationships
    - Many-to-many relationships
    - How to join tables
  
  Example:
    Relationship: User ← enrollments
    Description: One user can have many enrollments
    Join: users.id = enrollments.user_id
    
    Relationship: Enrollment ← assessments
    Description: One enrollment can have many assessments
    Join: enrollments.id = assessments.enrollment_id

MIGRATION_GUIDE:
  File: MIGRATION_GUIDE.md
  Contents:
    1. Overview (what's in this export)
    2. Quick start (which file to look at first)
    3. Data dictionary (field definitions)
    4. Entity relationships (how tables relate)
    5. Common queries (examples)
    6. Importing to PostgreSQL (step-by-step)
    7. Importing to MySQL (alternative)
    8. CSV import to Excel/Sheets
    9. Troubleshooting (common issues)
    10. Support contact (who to ask if confused)
```

---

## 🔒 PART 3: TECHNICAL IMPLEMENTATION (SEALED)

### 3.1 EXPORT ARCHITECTURE

```yaml
EXPORT_SYSTEM_COMPONENTS:
  Frontend_UI:
    Portal_Page: /settings/data/export
    UI_Elements:
      - "Download My Data" button
      - Format selector (JSON, CSV, XML, PDF)
      - Scope selector (what to include)
      - Timeline display (how long to process)
      - Status tracking (real-time progress)
      - Download history (previous exports)
  
  API_Endpoints:
    POST /api/v1/exports:
      Description: Initiate export request
      Parameters:
        - format: json | csv | xml | pdf | db_dump
        - scope: personal | institutional (if admin)
        - email: Optional (send link to different email)
      Response:
        ```json
        {
          "export_id": "export_abc123",
          "status": "queued",
          "created_at": "2026-02-24T10:30:00Z",
          "estimated_completion": "2026-02-24T11:30:00Z"
        }
        ```
    
    GET /api/v1/exports/{export_id}:
      Description: Check export status
      Response:
        ```json
        {
          "export_id": "export_abc123",
          "status": "processing", // queued, processing, completed, failed
          "progress": 45, // percentage
          "estimated_time_remaining": 300, // seconds
          "record_count": 5000,
          "file_size_estimate_mb": 25
        }
        ```
    
    GET /api/v1/exports/{export_id}/download:
      Description: Download completed export
      Response: File download (with checksum, signature headers)
  
  Background_Jobs:
    Export_Worker:
      - Listen on queue (SQS, Kafka)
      - Process export requests (parallel workers)
      - Query database (efficient batching)
      - Generate files (format-specific)
      - Calculate checksums (SHA-256)
      - Sign files (digital signature)
      - Store in S3 (encrypted bucket)
      - Send notification email
    
    Job_Monitoring:
      - Track job status
      - Detect failures (retry logic)
      - Alert if taking too long (SLA alert)
      - Scale workers as needed (queue depth)
  
  Storage:
    S3_Bucket: antigravity-exports (encrypted, private)
    Structure:
      s3://antigravity-exports/
        /{tenant_id}/
          /{export_id}/
            export.json
            export.csv
            data_dictionary.csv
            manifest.txt
            checksum.txt
            signature.txt
            migration_guide.md
    
    Retention: 30 days (auto-delete after 30 days)
    Access: Download only (user provides unique token)
    Encryption: AES-256 (S3-side encryption)
  
  Notification:
    Email_Template:
      Subject: "Your Data Export is Ready for Download"
      Body: 
        - Greeting with user name
        - Export summary (format, size, records)
        - Download link (expires in 7 days)
        - Checksum (for verification)
        - FAQ (common questions)
        - Support contact
    
    SMS_Option: Optional SMS notification (if user opted in)

EXPORT_WORKFLOW_DIAGRAM:
  User Request
    ↓
  Frontend Validation
    ↓
  Authenticated? → No: Return 401
    ↓ Yes
  Authorization Check (own data or admin?) → No: Return 403
    ↓ Yes
  Create Export Record (DB)
    ↓
  Queue Background Job (SQS)
    ↓
  Send Confirmation Email
    ↓
  [ASYNC] Export Worker Processes
    │
    ├→ Query Database (scoped to tenant)
    │   ↓
    ├→ Format Data (JSON/CSV/etc)
    │   ↓
    ├→ Calculate Checksum (SHA-256)
    │   ↓
    ├→ Sign Files (digital signature)
    │   ↓
    ├→ Upload to S3
    │   ↓
    ├→ Update Export Status (completed)
    │   ↓
    └→ Send Email (download link)
  
  User Downloads
    ↓
  Verify Token (valid? not expired?)
    ↓
  Log Download (audit trail)
    ↓
  Stream File
    ↓
  Delete After 30 Days (auto cleanup)

SCALABILITY:
  High_Volume_Scenario:
    - Large institution (100K students) requests export
    - Export size: 500MB to 2GB
    - Processing time: Estimated 30-60 minutes
    - Implementation:
      1. Chunked processing (process in batches)
      2. Multiple workers (parallel queries)
      3. Incremental file writing (don't load all in memory)
      4. Progress tracking (update status regularly)
      5. Compression (gzip large files)
  
  Concurrent_Exports:
    - Multiple institutions exporting simultaneously
    - Queue system (SQS/Kafka) handles load
    - Auto-scaling (spin up workers as queue grows)
    - Resource limits (max 50 concurrent exports)
    - Fair queuing (FIFO, no priority jumping)
  
  Performance_Targets:
    - Small export (individual user): 5-10 minutes
    - Medium export (100 students): 15-30 minutes
    - Large export (10K students): 1-2 hours
    - Very large export (100K+ students): 4-8 hours
    - All targets: SLA compliance mandatory

TESTING:
  Unit_Tests:
    - Data export logic (each entity)
    - Format generation (JSON, CSV, XML)
    - Checksum calculation
    - Signature generation
  
  Integration_Tests:
    - End-to-end export (user request → delivery)
    - Cross-tenant isolation (no data leakage)
    - Large dataset handling (stress test)
    - Concurrent exports (parallel processing)
  
  Security_Tests:
    - Authentication (only authenticated users)
    - Authorization (only own data)
    - Tenant isolation (cross-tenant leak detection)
    - Data integrity (checksums, signatures)
  
  Compliance_Tests:
    - SLA timing (meets GDPR/CCPA deadlines)
    - Format correctness (can import to target systems)
    - Data completeness (all expected records included)
    - Accuracy (data matches database values)
  
  Performance_Tests:
    - Time to export (meets SLA targets)
    - Concurrent export load (scalability)
    - Memory usage (doesn't exceed limits)
    - CPU usage (efficient processing)

ERROR_HANDLING:
  Common_Errors:
    Error: Database query timeout (large dataset)
    Solution: Batch queries, implement checkpointing
    
    Error: S3 upload failure
    Solution: Retry with exponential backoff, alert ops
    
    Error: Checksum mismatch
    Solution: Re-generate export, verify integrity
    
    Error: User attempts cross-tenant access
    Solution: Log security incident, return 403
  
  Failure_Recovery:
    - Failed export auto-retried (up to 3 times)
    - Partial exports cleaned up (not returned to user)
    - User notified of failure (with support contact)
    - Manual re-trigger available (admin portal)
```

### 3.2 SECURITY & COMPLIANCE (SEALED)

```yaml
AUTHENTICATION_&_AUTHORIZATION:
  User_Request:
    - Must be logged in (JWT token)
    - Requesting own data only
    - Cannot access other users' exports
    - Session verified (not expired)
  
  Institution_Request:
    - Must be tenant admin (verified in JWT)
    - Token scoped to institution (tenant_id)
    - Cannot export other institutions' data
    - Admin role verified in database
  
  Token_Verification:
    ```
    // Every export endpoint checks:
    if (!req.user || !req.user.id) return 401;
    if (export.user_id !== req.user.id) return 403;
    
    // Institution exports:
    if (export.tenant_id !== req.user.tenant_id) return 403;
    if (req.user.role !== 'admin') return 403;
    ```

DATA_INTEGRITY:
  Checksums:
    - SHA-256 hash of export file
    - Sent with export (in manifest.txt)
    - User can verify: sha256sum export.json
    - Detects: Corruption, tampering, incomplete downloads
  
  Digital_Signatures:
    - ECDSA signature of export
    - Public key provided (for verification)
    - Optional (user can request)
    - Proves: File authenticity (came from Antigravity)
  
  Validation:
    ```
    // Before sending to user:
    const fileHash = sha256(exportData);
    if (fileHash !== storedHash) {
      throw new Error('Export integrity check failed');
    }
    
    // User can verify:
    echo "abc123def456 export.json" | sha256sum -c
    // Should output: export.json: OK
    ```

ENCRYPTION:
  In_Transit:
    - HTTPS/TLS 1.3+ (all connections encrypted)
    - Download link is HTTPS only
    - HTTP redirects to HTTPS (no clear-text)
  
  At_Rest:
    - S3 bucket: AES-256 encryption enabled
    - Export files encrypted on S3
    - Key management: AWS KMS (Antigravity doesn't manage keys)
  
  User_Requested_Encryption:
    - Optional: User can request encrypted export
    - Format: GPG-encrypted (user provides public key)
    - Or: Password-encrypted ZIP
    - Decryption: User's responsibility (Antigravity cannot decrypt)

AUDIT_TRAIL:
  What_Logged:
    - Export request (user, timestamp, format, scope)
    - Processing status (started, completed, failed)
    - Download (user, IP address, timestamp)
    - Verification (checksum calculated, signature validated)
    - Failures (reason for failure, retry count)
  
  Retention:
    - Export logs: 7 years (legal requirement)
    - Download history: 1 year
    - Failure logs: Indefinite (security)
  
  Immutability:
    - Logs stored in append-only storage
    - Cannot be modified retroactively
    - Cryptographically signed (detect tampering)
    - Regular backup (disaster recovery)
  
  Access_Control:
    - Only authorized admins can view logs
    - Compliance officer can access (for audits)
    - Law enforcement: With legal instrument

COMPLIANCE_CERTIFICATIONS:
  GDPR_Compliance:
    ✓ Article 20: Data portability right implemented
    ✓ 30-day response time (SLA enforced)
    ✓ Machine-readable format (JSON, CSV, XML)
    ✓ Structured, commonly-used format
    ✓ Direct transmission to user
    ✓ Interoperability (can import to competitors)
    ✓ No discrimination (free, no charges)
  
  CCPA_Compliance:
    ✓ Right to Know: User can download all data
    ✓ 45-day response time (extended to 90 with notice)
    ✓ Verification: User identity verified
    ✓ Portability: Data in portable format
    ✓ No sale of data: No commercial use
    ✓ Transparency: Clear privacy policy
  
  India_IT_Act_Compliance:
    ✓ Reasonable data access (user can download)
    ✓ Data portability (standard formats)
    ✓ Security (AES-256 encryption)
    ✓ Data residency (India data center only)
    ✓ Compliance audit (annual certification)
  
  FERPA_Compliance:
    ✓ Student access to own records (within 5 business days)
    ✓ Accuracy: Data matches institutional records
    ✓ Amendment: Student can request corrections
    ✓ Disclosure logging: Track all access
    ✓ Parent access: For K-12 (if applicable)
  
  EEOC_Compliance:
    ✓ Employee access to hiring records (1-year retention)
    ✓ Audit trail: Hiring decision documentation
    ✓ Adverse impact analysis: Check for bias
    ✓ Transparency: Algorithm criteria published
```

---

## ⚡ PART 4: SLA & PERFORMANCE TARGETS (SEALED)

### 4.1 SERVICE LEVEL AGREEMENTS

```yaml
GDPR_SLA:
  Timeline: 30 days from request receipt
  Extension: 60 days total if "complex" (documented)
  Definition_Complex:
    - Multiple systems/sources
    - >50,000 records
    - Complex data relationships
    - Unusual request scope
  
  Metrics:
    - 95% of requests completed within 30 days
    - 99% within 60 days
    - 100% within 90 days (escalation required if exceeded)
  
  Failure_Consequences:
    - SLA breach triggers alert
    - Escalation to board (if repeated)
    - Compliance report to DPA (if intentional)
    - User compensation (if applicable under national law)
  
  Monitoring:
    - Daily tracking (SLA timeline)
    - Weekly reports (compliance rate)
    - Monthly board update
    - Quarterly audit (third-party verification)

CCPA_SLA:
  Timeline: 45 days from request receipt
  Extension: Additional 45 days (max 90 days, with notice)
  Metrics:
    - 95% of requests completed within 45 days
    - 99% within 90 days
  
  Verification:
    - Verify consumer identity (reasonable method)
    - Consumer confirms request (optional)
    - No charge (free service)
  
  Failure_Consequences:
    - California AG notification (if intentional)
    - Private right of action (consumer can sue)
    - Statutory damages (up to $100-$750 per violation)

INDIA_IT_ACT_SLA:
  Timeline: 7-14 days (aggressive, based on GDPR standard)
  Metrics:
    - 90% within 7 days
    - 99% within 14 days
  
  Data_Residency:
    - Export from India data center only
    - No cross-border transfer of export
    - User receives data within India jurisdiction

INTERNAL_SLA (Antigravity Standard):
  Small_Export (<10MB):
    - Queued: <1 minute
    - Processing: <5 minutes
    - Delivery: <10 minutes total SLA
  
  Medium_Export (10MB-500MB):
    - Processing: <30 minutes
    - Delivery: <1 hour total SLA
  
  Large_Export (>500MB):
    - Processing: <4 hours
    - Delivery: <8 hours total SLA
    - User notified (long processing time)

PERFORMANCE_TARGETS:
  Export_Initiation: <100ms
  Download_Link_Generation: <500ms
  Email_Delivery: <5 minutes
  File_Download: Full speed (no throttling)
  Checksum_Calculation: <1 second per 1MB
  Signature_Generation: <2 seconds per export

AVAILABILITY:
  Uptime: 99.9% (3 nines)
  Maintenance_Window: Sundays 2-4 AM UTC (monthly)
  Downtime_Handling: Automated failover (secondary region)
  Backup: Export requests queued during downtime (processed after recovery)

SCALABILITY:
  Concurrent_Exports: 100+ simultaneously
  Export_Queue: Auto-scaling (up to 10,000 queued)
  Worker_Threads: Auto-scale based on queue depth
  Storage_Capacity: Unlimited (S3-backed)
  Bandwidth: 1 Gbps+ (CDN-distributed)

MONITORING_&_ALERTING:
  Real_Time_Dashboard:
    - Active exports (count, progress)
    - Queue depth (pending requests)
    - Error rate (failed exports)
    - SLA compliance (% on-time)
    - Performance metrics (avg processing time)
  
  Automated_Alerts:
    - SLA alert (if approaching deadline, -5 days)
    - Error spike (if >5% failure rate)
    - Queue backlog (if >1000 pending)
    - Performance degradation (if >2x normal time)
  
  Escalation:
    - Ops team: Alert at 50% SLA time remaining
    - Management: Alert at 80% SLA time remaining
    - Executive: Alert if SLA exceeded
    - Board: Monthly report of SLA breaches
```

---

## 🔐 PART 5: SEALED CONSTRAINTS & IMMUTABLE RULES (LOCKED)

### 5.1 EXPORT GOVERNANCE LOCKS

```yaml
RULE_1_PORTABILITY_IS_UNCONDITIONAL_RIGHT:
  SCOPE: Absolute requirement, no exceptions
  
  LOCKED_DEFINITION:
    "Every data subject has the unconditional right to obtain personal
    data in portable, machine-readable format. No authorization barriers.
    No cost charges. No format restrictions. Within regulatory SLA."
  
  ENFORCEMENT_MECHANISM:
    - Self-service portal (no approval needed)
    - Automated processing (no manual review)
    - No rejection authority (cannot deny for business reasons)
    - SLA compliance mandatory (enforced technically)
    - No cost (free, no premium charges)
  
  VIOLATIONS:
    ✗ Requiring admin approval (violates GDPR)
    ✗ Charging for export (violates GDPR)
    ✗ Limiting export frequency (violates user rights)
    ✗ Excluding data to trap users (vendor lock-in)
    ✗ Delayed delivery (SLA breach)
    ✗ Incomplete export (compliance violation)
  
  AUDIT:
    - Quarterly verification of unconditional nature
    - Test export requests (ensure no barriers)
    - User satisfaction survey (ease of export)
    - Legal review (GDPR/CCPA compliance)

RULE_2_COMPLETENESS_MANDATORY:
  SCOPE: Every export must include all data
  
  LOCKED_DEFINITION:
    "Exported data must be complete. No selective omission. All personal
    data must be included. All relationships preserved. All metadata
    included. Cannot exclude data for competitive reasons."
  
  ENFORCEMENT_MECHANISM:
    - Record count verification (database vs export)
    - Relationship validation (no orphaned records)
    - Metadata inclusion (timestamps, versions)
    - Audit trail (what's excluded, why)
    - User verification (can user validate completeness)
  
  COMPLETENESS_CHECKLIST:
    ☐ All user profile data (no fields excluded)
    ☐ All enrollment records (no hidden courses)
    ☐ All assessment results (no hidden scores)
    ☐ All achievements (all badges included)
    ☐ All activity history (complete audit trail)
    ☐ All messages (user's own messages)
    ☐ All metadata (timestamps, versions, relationships)
    ☐ Data relationships (foreign keys, joins)
    ☐ Configuration (user's settings, preferences)
    ☐ Integration data (connected apps, sync status)
  
  VERIFICATION:
    - Automated: Record count check (before sending)
    - Manual: Spot-check exports (quarterly audit)
    - User: Can verify against their profile
    - Third-party: Compliance auditor verifies

RULE_3_NO_SELECTIVE_OMISSION:
  SCOPE: Cannot exclude data for business reasons
  
  LOCKED_DEFINITION:
    "Cannot deliberately exclude data to trap user. Omissions only for
    legitimate reasons: other users' privacy, system data, security.
    Omissions must be documented + justified."
  
  PROHIBITED_OMISSIONS:
    ✗ Exclude competitor-sensitive data (market intelligence)
    ✗ Exclude skills user has that compete with Antigravity
    ✗ Exclude job postings to prevent application elsewhere
    ✗ Exclude salary data to hide benchmarks
    ✗ Exclude performance data to hide migration risk
    ✗ Exclude relationships (enrollment → course connections)
  
  ALLOWED_OMISSIONS:
    ✓ Other users' personal data (privacy of others)
    ✓ System logs (technical operations data)
    ✓ Passwords (never exported, for security)
    ✓ Encryption keys (security, user cannot export)
    ✓ Third-party confidential data (partners' data)
  
  JUSTIFICATION_REQUIRED:
    - Omissions document in manifest
    - Reason explained clearly
    - Legal basis cited (GDPR limitation)
    - User can appeal (support contact)
  
  AUDIT:
    - Quarterly: Verify no discriminatory omissions
    - Competitor test: Can competing platform use export?
    - User test: Does export let user switch platforms?

RULE_4_INTEGRITY_GUARANTEED:
  SCOPE: Data must be accurate, unaltered, verifiable
  
  LOCKED_DEFINITION:
    "Exported data must match database exactly. Checksums provided
    (user can verify). Digital signatures available (prove authenticity).
    No data corruption, truncation, or modification."
  
  ENFORCEMENT_MECHANISM:
    - Automated validation (before sending)
    - Checksum calculation (SHA-256)
    - Signature generation (digital signature)
    - User verification (checksums provided)
    - Third-party audit (random verification)
  
  INTEGRITY_CHECKS:
    ☐ Record count matches database
    ☐ Field values match exactly (byte-for-byte)
    ☐ Character encoding preserved (UTF-8)
    ☐ Dates formatted consistently (ISO 8601)
    ☐ Relationships intact (foreign keys valid)
    ☐ No truncation (all data included)
    ☐ No corruption (checksums match)
    ☐ No modification (signatures validate)
  
  REMEDIATION:
    - If integrity check fails: Don't send export
    - Retry export (auto-generate again)
    - Alert user (let them know issue + solution)
    - Log incident (security audit trail)
    - Investigation (understand failure reason)

RULE_5_SLA_COMPLIANCE_MANDATORY:
  SCOPE: Regulatory timelines are non-negotiable
  
  LOCKED_DEFINITION:
    "GDPR: 30-day response (60 if complex). CCPA: 45-day response
    (90 if complex). India: 7-14 days. SLA is system-enforced,
    not dependent on manual review."
  
  ENFORCEMENT_MECHANISM:
    - Automated calendar tracking (request timestamp)
    - Deadline alerts (5 days, 1 day before)
    - Escalation (if cannot meet deadline)
    - Board reporting (monthly SLA metrics)
    - Target: 100% on-time delivery
  
  FAILURE_HANDLING:
    - If SLA will be missed: Escalate to CEO + General Counsel
    - Provide interim update (proof of progress)
    - Document reason for delay (legitimacy)
    - Offer interim data (partial export if helpful)
    - Remediation plan (deliver remaining data)
  
  REPORTING:
    - Daily: Export request timelines
    - Weekly: SLA compliance rate
    - Monthly: Board summary (% on-time, breaches)
    - Quarterly: External audit verification

RULE_6_FORMAT_INTEROPERABILITY:
  SCOPE: Export formats must be open, non-proprietary
  
  LOCKED_DEFINITION:
    "Formats must be open standards (JSON, CSV, XML, PostgreSQL dump).
    No proprietary formats. No Antigravity-specific encoding. User can
    import to competing platforms without tools/assistance."
  
  ENFORCEMENT_MECHANISM:
    - Format audit (quarterly, verify openness)
    - Interoperability test (can competitors import?)
    - Standard compliance (use official specs)
    - No binary encoding (human-readable)
    - Documentation provided (format specs, examples)
  
  REQUIRED_FORMATS:
    - JSON (open standard)
    - CSV (open standard, RFC 4180)
    - XML (open standard, with XSD schema)
    - PostgreSQL dump (standard database format)
    - Optional: PDF (human-readable)
  
  NOT_ALLOWED:
    ✗ Antigravity-proprietary format
    ✗ Binary-encoded format (can't read in text editor)
    ✗ Encrypted with Antigravity-only keys
    ✗ Compression that requires special tools
    ✗ Format that requires Antigravity software to read
  
  TESTING:
    - Can standard tool open format? (Excel, Text Editor, SQL)
    - Can user understand data? (plain language, no obfuscation)
    - Can competing platform import? (test with PostgreSQL, MySQL)

RULE_7_NO_COST_MANDATORY:
  SCOPE: Export is free service, no charges
  
  LOCKED_DEFINITION:
    "Cannot charge for data export. No premium tier. No per-GB fee.
    No administrative charge. Free for all users, unconditionally."
  
  ENFORCEMENT_MECHANISM:
    - Billing system: Export requests blocked (no chargeability)
    - Terms of Service: Explicitly state free export
    - Portal: No "upgrade to premium for export" messaging
    - Audit: Verify no hidden charges (quarterly)
    - Legal: Compliance with GDPR Article 12
  
  PROHIBITED_CHARGES:
    ✗ Per-record charge
    ✗ Per-GB storage charge
    ✗ Administrative fee ("processing fee")
    ✗ Expedited export fee (even for faster delivery)
    ✗ Format-specific charge (JSON vs CSV)
    ✗ Encryption charge (if user requests encrypted export)
  
  ALLOWED:
    ✓ Free for all users
    ✓ Multiple exports allowed (no limit on frequency)
    ✓ Large exports supported (no size limit charge)
    ✓ All formats free (JSON, CSV, XML, dumps)
    ✓ Expedited processing available (free, best effort)
  
  VERIFICATION:
    - Annual audit (no hidden charges discovered)
    - Billing system review (export not billable)
    - User feedback (no users report being charged)
    - Legal review (GDPR compliance certified)

RULE_8_TENANT_ISOLATION_IN_EXPORTS:
  SCOPE: Institutional data must not leak to others
  
  LOCKED_DEFINITION:
    "Institutional exports must be perfectly isolated. No cross-tenant
    data. No unintended data leakage. Verified before delivery."
  
  ENFORCEMENT_MECHANISM:
    - SQL: WHERE tenant_id = ? enforced
    - API: Tenant scope validation
    - Testing: Cross-tenant leak detection test
    - Audit: Quarterly verification of isolation
  
  CROSS_TENANT_CHECK:
    ```
    // Before sending institutional export:
    const export = await getExportData(tenantId);
    
    // Verify no other tenant's data
    const uniqueTenants = new Set(
      export.users.map(u => u.tenant_id)
    );
    
    if (uniqueTenants.size !== 1 || 
        Array.from(uniqueTenants)[0] !== tenantId) {
      throw new Error('Cross-tenant data detected!');
      // DO NOT SEND EXPORT
    }
    ```
  
  FAILURE:
    - If cross-tenant data detected: Don't send
    - Log security incident (potential breach)
    - Investigate cause (code bug? data corruption?)
    - Remediate (fix, re-export, notify)
    - Board notification (if intentional)

RULE_9_ANNUAL_EXPORT_AUDIT:
  SCOPE: Independent verification of export system
  
  LOCKED_DEFINITION:
    "External auditor verifies export system annually. Tests:
    completeness, integrity, SLA compliance, format correctness,
    tenant isolation, security. Report to board."
  
  AUDIT_SCOPE:
    - Process audit: Is export process documented?
    - Technical audit: Are safeguards implemented?
    - Completeness: Are exports complete?
    - Integrity: Can user verify data accuracy?
    - SLA: Are timelines being met?
    - Format: Are exports importable?
    - Isolation: Is tenant data isolated?
    - Security: Are exports secure in transit?
  
  AUDIT_TESTING:
    - Request sample exports (verify completeness)
    - Verify checksums (integrity check)
    - Import to alternative system (format test)
    - Check SLA metrics (timeline compliance)
    - Penetration test (security test)
  
  REPORT:
    - Executive summary (compliance, issues)
    - Detailed findings (what's working, what's not)
    - Recommendations (improvements needed)
    - Remediation plan (fixing issues)
    - Certification (system complies with GDPR/CCPA)
  
  BOARD_SUBMISSION:
    - Annual report to audit committee
    - Issues escalated (if material)
    - Remediation tracked (until resolved)
```

---

## 📊 PART 6: MONITORING & METRICS (SEALED)

### 6.1 EXPORT METRICS DASHBOARD

```yaml
REAL_TIME_METRICS:
  Active_Exports:
    - Count (how many exports in progress)
    - Average processing time (current run)
    - Estimated completion (when will they finish)
    - Error rate (% failing)
  
  Queue_Health:
    - Pending requests (in queue)
    - Avg wait time (how long before processing starts)
    - Longest wait (which request has been waiting longest)
    - Queue depth trend (growing or shrinking)
  
  SLA_Tracking:
    - On-track exports (% that will meet SLA)
    - At-risk exports (% approaching deadline)
    - Breached exports (% exceeded deadline)
    - Days remaining (average to deadline)

HISTORICAL_METRICS:
  Request_Volume:
    - Daily requests (trend over time)
    - Weekly requests (by day of week)
    - Peak hours (when most requests)
    - Seasonal trends (time of year)
  
  SLA_Compliance:
    - % completed within GDPR SLA (30 days)
    - % completed within CCPA SLA (45 days)
    - % completed within internal SLA
    - Trend (improving or declining)
  
  Format_Distribution:
    - % requesting JSON
    - % requesting CSV
    - % requesting XML
    - % requesting database dump
  
  Request_Types:
    - % personal data exports
    - % institutional exports
    - % regulatory requests
    - % deletion requests
  
  Error_Tracking:
    - Top error types (why do exports fail)
    - Error trend (increasing or decreasing)
    - Cause analysis (bugs, user mistakes, system issues)
    - Resolution (how many fixed within SLA)
  
  Performance:
    - Avg processing time (by export size)
    - P50, P95, P99 latencies (percentiles)
    - Throughput (exports/hour)
    - Resource usage (CPU, memory, disk)

COMPLIANCE_METRICS:
  GDPR_Compliance:
    - % requests completed within 30 days
    - % within 60 days (with extension)
    - % exceeding SLA (with reasons)
    - Extension request rate (% claiming complexity)
  
  CCPA_Compliance:
    - % requests completed within 45 days
    - % within 90 days (with extension)
    - % of requests with verification (identity check)
    - Cost to user (should be $0)
  
  Data_Quality:
    - % exports with checksum provided
    - % exports with signature available
    - % user-verified integrity (spot check)
    - Corruption rate (how often checksums fail)
  
  User_Satisfaction:
    - Export quality survey (1-5 rating)
    - Ease of use (could user complete export?)
    - Format usefulness (could user understand data?)
    - Support quality (any issues resolved?)

ANOMALY_DETECTION:
  Alert_Conditions:
    - Error rate spike (>5% failures)
    - Queue backlog (>1000 pending)
    - SLA approaching (export at 80% of deadline)
    - Processing time spike (>2x normal)
    - Failed user identity verification (>10% rejections)
    - Cross-tenant data detected (security incident!)
  
  Escalation:
    - Ops team: Automatic alert
    - Management: Escalate if not resolved in 1 hour
    - Executive: Escalate if affecting SLA
    - Board: Report if material impact

REPORTING:
  Daily_Report:
    - Exports processed (count, avg time)
    - Errors (count, types)
    - SLA at-risk (count, time remaining)
    - Queue status (depth, estimated processing time)
  
  Weekly_Report:
    - Volume trends (up or down)
    - SLA compliance rate
    - Top error types (improvements needed)
    - Performance metrics (processing time)
  
  Monthly_Report:
    - Executive summary (SLA status, issues)
    - Request volume (by type, by institution)
    - SLA compliance (% on-time, breaches)
    - Performance (processing time trends)
    - Error analysis (root causes, fixes)
    - User satisfaction (feedback, ratings)
  
  Quarterly_Audit:
    - Full compliance review (GDPR, CCPA, India)
    - External audit results (if conducted)
    - Recommendations implemented (tracking)
    - Issues resolved (time to resolution)
    - Metrics improvement (vs prior quarter)
  
  Annual_Board_Report:
    - Year summary (volume, SLA compliance)
    - Compliance certification (GDPR, CCPA, etc.)
    - Material issues (breaches, failures)
    - Improvements implemented
    - Plans for next year
```

---

## 🔐 CONCLUSION & GOVERNANCE

### SEALED DECLARATION

This **TENANT_EXPORT_AGENT.md** document establishes immutable data portability and export requirements for Antigravity, binding all employees, executives, and agents. The framework is:

✅ **SEALED & LOCKED**: EXECUTION_MODE = LOCKED, MUTATION_POLICY = READ-ONLY  
✅ **IMMUTABLE**: 9 locked rules with zero exceptions unless formal board+counsel approval  
✅ **GDPR-COMPLIANT**: Article 20 data portability fully implemented  
✅ **CCPA-COMPLIANT**: Right to Know, Right to Port, Right to Delete  
✅ **COMPREHENSIVE**: All export types, formats, security, compliance  
✅ **ENFORCEABLE**: SLA monitoring, metrics tracking, third-party audits  

**FAILURE TO COMPLY = REGULATORY FINES (UP TO 4% GLOBAL REVENUE) + REPUTATIONAL DAMAGE + LOSS OF CUSTOMER TRUST**

---

**Document Version**: 1.0  
**Last Updated**: 2026-02-24  
**Status**: SEALED & LOCKED  
**Classification**: ATTORNEY-CLIENT PRIVILEGED / CONFIDENTIAL  
**Retention**: Permanent (governance record)  
**Distribution**: Board Only + Compliance Team + Data Protection Officer + Legal Counsel

---

## APPENDIX A: QUICK REFERENCE

```yaml
REGULATORY_SLA_CHEAT_SHEET:
  GDPR: 30 days (extends to 60 if complex)
  CCPA: 45 days (extends to 90 if complex)
  India: 7-14 days (aggressive)
  FERPA: 5 business days (US education)
  Antigravity_Target: 7 days (not complex)

FORMAT_RECOMMENDATIONS:
  Individual_User: JSON or PDF
  Institution_Migration: PostgreSQL dump
  Third_Party_Integration: JSON
  Spreadsheet_Analysis: CSV
  Enterprise_Archival: XML with schema

IMMUTABLE_RULES_SUMMARY:
  1. Portability = Unconditional right
  2. Completeness = All data included
  3. No selective omission (except privacy/security)
  4. Integrity = Checksums, signatures
  5. SLA compliance = Mandatory
  6. Format interoperability = Open standards
  7. No cost = Free export
  8. Tenant isolation = Perfect segregation
  9. Annual audit = External verification
```

---

**END OF TENANT_EXPORT_AGENT.md**
