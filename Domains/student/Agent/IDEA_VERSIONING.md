# 🔒 IDEA_VERSIONING.md — ANTIGRAVITY SYSTEM SPEC
**Status:** SEALED · LOCKED · GOVERNED · DETERMINISTIC  
**Version:** 1.0.0  
**Mutation Policy:** Add-only via version bump  
**Execution Authority:** Human declaration only  
**Last Updated:** 2025-02-25  

---

## 🏢 EXECUTIVE CONTEXT

**Platform:** Ecoskiller Antigravity  
**Scale Target:** 10M–100M users  
**Architecture:** Microservices + Event-Driven  
**Versioning Strategy:** Semantic versioning (MAJOR.MINOR.PATCH)  
**Mutation Policy:** Add-only (no retroactive modifications)  
**Data Integrity:** Immutable idea snapshots at every version  
**Compliance:** Append-only audit trail for all versions  

---

## SECTION A — VERSIONING SYSTEM IDENTITY

### 1.1 AGENT SPECIFICATION (MANDATORY)

```
AGENT_NAME = IDEA_VERSIONING_MANAGER
SYSTEM_ROLE = Idea iteration, evolution tracking, and version control
PRIMARY_DOMAIN = Idea Lifecycle Management & Version History
EXECUTION_MODE = Deterministic + Validated
DATA_SCOPE = Idea versions, snapshots, diffs, lineage
TENANT_SCOPE = Strict isolation (multi-tenant)
FAILURE_POLICY = Halt on ambiguity, LOG, ESCALATE
ENVIRONMENT = Dev / Test / Staging / Production
DEPLOYMENT_MODEL = Stateless microservice
```

### 1.2 PURPOSE DECLARATION

**Problem Solved:**
- Track idea evolution over time
- Enable collaboration and iteration on ideas
- Preserve complete audit trail of changes
- Support rollback to previous versions
- Maintain intellectual property lineage
- Enable diff and comparison workflows
- Support branching for idea derivatives
- Enable idea forking and remixing

**Input Consumed:**
- Base idea record (from IDEA_SUBMISSION_MANAGER)
- Update request (title, description, attachments, status)
- User metadata (ID, tenant, role, permissions)
- Version context (parent version, change reason)

**Output Produced:**
- New version record (immutable)
- Version snapshot (complete state at that moment)
- Diff report (changes from previous version)
- Version tree (lineage visualization)
- Audit reference (immutable trace)

**Downstream Agents:**
- RANKING_ENGINE (version history affects discovery)
- NOTIFICATION_AGENT (change notifications to followers)
- FEATURE_STORE_AGENT (versioning activity metrics)
- COMPLIANCE_AGENT (version governance checks)
- IDEA_DNA_AGENT (originality re-check if significant changes)

**Upstream Agents:**
- IDEA_SUBMISSION_MANAGER (base idea data)
- IDENTITY_AGENT (user authentication)
- RBAC_AGENT (permission validation)
- TENANT_ISOLATION_AGENT (multi-tenant scoping)

---

## SECTION B — VERSIONING STRATEGY

### 2.1 VERSION NUMBERING SCHEME

```yaml
Version_Format: MAJOR.MINOR.PATCH-{status}+{metadata}

Components:

  MAJOR:
    - Incremented when: fundamental idea change or complete rewrite
    - Example: v1.0.0 → v2.0.0 (pivoted from "Mobile App" to "Desktop App")
    - Scope: breaking changes to core concept
    - Backward Compatibility: NOT guaranteed
    - Immutability: Previous major versions archived forever
  
  MINOR:
    - Incremented when: new features added or significant enhancements
    - Example: v1.0.0 → v1.1.0 (added video tutorial to documentation)
    - Scope: non-breaking enhancements
    - Backward Compatibility: GUARANTEED
    - Content: all previous features still present
  
  PATCH:
    - Incremented when: bug fixes, typos, clarifications
    - Example: v1.0.1 → v1.0.2 (fixed typo in description)
    - Scope: non-breaking, minimal changes
    - Backward Compatibility: GUARANTEED
    - Content: no feature additions, only corrections
  
  Status_Tag:
    - DRAFT: Work in progress, not yet submitted
    - SUBMITTED: Initial version, under review
    - ACTIVE: Published and visible
    - DEPRECATED: Superseded, kept for reference
    - ARCHIVED: No longer maintained
    - DELETED: Removed per legal hold
  
  Metadata:
    - Timestamp: ISO8601 UTC
    - Author: user_id
    - Branch: if from fork or derivative
    - Parent: version_id of previous version

Version_Examples:
  - 1.0.0-DRAFT
  - 1.0.0-SUBMITTED
  - 1.0.0-ACTIVE
  - 1.1.0-ACTIVE+fork-science-project-123
  - 2.0.0-ACTIVE+major-pivot-from-mobile-to-web
  - 1.0.1-DEPRECATED+superseded-by-2.0.0
```

### 2.2 VERSION LIFECYCLE STATES

```yaml
State_Machine:

  DRAFT:
    ├─ Can edit freely (no publication)
    ├─ Can delete (no audit trail kept)
    ├─ Transition: → SUBMITTED
    ├─ Duration: unlimited
    └─ Visibility: PRIVATE (author only)
  
  SUBMITTED:
    ├─ Under review (locked for editing)
    ├─ Can request changes (via reviewer feedback)
    ├─ Transition: → ACTIVE or → REJECTED
    ├─ Duration: max 30 days
    └─ Visibility: INTERNAL (reviewers + author)
  
  ACTIVE:
    ├─ Published and discoverable
    ├─ Can create new versions (minor/patch only from here)
    ├─ Can create major version (requires full resubmission)
    ├─ Can receive comments and engagement
    ├─ Transition: → DEPRECATED or → ARCHIVED
    ├─ Duration: unlimited
    └─ Visibility: PUBLIC (based on tenant settings)
  
  DEPRECATED:
    ├─ Marked as superseded
    ├─ Still discoverable (with deprecation notice)
    ├─ Read-only (cannot edit)
    ├─ Indicates successor version
    ├─ Transition: → ARCHIVED
    ├─ Duration: min 6 months
    └─ Visibility: PUBLIC (with warning)
  
  ARCHIVED:
    ├─ Historical reference only
    ├─ Read-only (permanent)
    ├─ Not discoverable (search excluded)
    ├─ Immutable forever
    ├─ No transitions
    ├─ Duration: 7 years (legal hold)
    └─ Visibility: PRIVATE (owner only)
  
  DELETED:
    ├─ Removed per legal/compliance hold
    ├─ Soft delete (not hard deleted from database)
    ├─ Immutable forever
    ├─ Audit trail preserved
    ├─ No transitions
    ├─ Duration: 7 years (legal hold)
    └─ Visibility: HIDDEN (audit only)

State_Transition_Rules:
  
  DRAFT → SUBMITTED:
    - Requires: all mandatory fields filled
    - Action: lock for editing, mark timestamp
  
  SUBMITTED → ACTIVE:
    - Requires: reviewer approval
    - Action: publish, make discoverable
  
  SUBMITTED → REJECTED:
    - Requires: reviewer rejection + feedback
    - Action: return to DRAFT, keep feedback
  
  ACTIVE → DEPRECATED:
    - Requires: successor version specified
    - Action: mark deprecated, add warning label
  
  DEPRECATED → ARCHIVED:
    - Requires: 6+ months since deprecation
    - Action: remove from discovery, keep historical
  
  * → DELETED:
    - Requires: compliance officer approval
    - Action: soft delete, retain audit trail
  
  No_Direct_Transitions_Allowed:
    - Cannot jump states (must follow chain)
    - Cannot revert states (forward-only)
    - State changes immutable (no rollback)
```

### 2.3 VERSION HIERARCHY & BRANCHING

```yaml
Version_Tree_Model:

  Linear_Versioning (default):
    v1.0.0 (ACTIVE)
       ↓
    v1.1.0 (ACTIVE) [added attachments]
       ↓
    v1.2.0 (ACTIVE) [added video]
       ↓
    v1.2.1 (ACTIVE) [fixed typo]
       ↓
    v2.0.0 (ACTIVE) [major rewrite]
    
    All versions linked via parent_version_id
    Each version points to exactly one parent (except v1.0.0)
    Creates linear audit trail

  Branching_Model (for forks/derivatives):
    
    Original:
      v1.0.0 (ACTIVE) - "Mobile App Marketplace"
         ├─→ v1.1.0 (ACTIVE)
         │      └─→ v1.2.0 (ACTIVE)
         └─→ v2.0.0 (ACTIVE) [complete rewrite]
    
    Fork 1 (created by User_B):
      v1.0.0-fork-123 (ACTIVE) - "Mobile App + Web"
         ├─→ v1.1.0-fork-123 (ACTIVE)
         └─→ v2.0.0-fork-123 (ACTIVE)
    
    Fork 2 (created by User_C):
      v1.0.0-fork-456 (ACTIVE) - "Mobile App for Education"
         ├─→ v1.1.0-fork-456 (ACTIVE)
         └─→ v2.0.0-fork-456 (ACTIVE)
    
    Each fork has its own version tree
    Forks can exist independently or reference parent
    Fork lineage tracked via fork_parent_id
    Royalties may flow to original author (configurable)

  Merge_Model (combining versions):
    
    Source Branch:
      v1.0.0-fork-123 (best features)
    
    Target Branch:
      v1.2.0 (current)
    
    Merge Operation:
      - Identify conflicting changes
      - Manual conflict resolution
      - Create new version: v1.3.0-merged
      - Link both parents: merge_sources = [v1.2.0, v1.0.0-fork-123]
      - Mark as merge in metadata
    
    Merge_Commit:
      {
        "version_id": "idea-merge-789",
        "version_number": "1.3.0",
        "status": "ACTIVE",
        "merge_operation": true,
        "merge_sources": ["v1.2.0", "v1.0.0-fork-123"],
        "merged_by": "user-999",
        "merge_timestamp": "2025-02-25T15:30:00Z",
        "conflict_resolution": "manual" | "auto"
      }
```

---

## SECTION C — VERSION SCHEMA

### 3.1 VERSION RECORD SCHEMA

```json
{
  "version_metadata": {
    "idea_id": "UUID (immutable, same for all versions)",
    "version_id": "UUID (unique per version)",
    "version_number": "string (e.g., '1.2.3-ACTIVE')",
    "major": "integer",
    "minor": "integer",
    "patch": "integer",
    "status": "enum (DRAFT|SUBMITTED|ACTIVE|DEPRECATED|ARCHIVED|DELETED)",
    "user_id": "UUID (version creator)",
    "tenant_id": "UUID (immutable)",
    "created_at": "ISO8601 (immutable)",
    "updated_at": "ISO8601 (last modification)",
    "published_at": "ISO8601 (when moved to ACTIVE, null if not published)",
    "deprecated_at": "ISO8601 (when marked deprecated)",
    "archived_at": "ISO8601 (when moved to archive)",
    "deleted_at": "ISO8601 (when soft deleted)"
  },

  "version_hierarchy": {
    "parent_version_id": "UUID (previous version, null if first)",
    "root_version_id": "UUID (original version, for lineage)",
    "is_fork": "boolean",
    "fork_parent_id": "UUID (if forked from another idea)",
    "fork_name": "string (e.g., 'Science Education Edition')",
    "is_merge": "boolean",
    "merge_sources": ["UUID", "UUID"],
    "successor_version_id": "UUID (next version if deprecated)",
    "branch": "string (e.g., 'main', 'experimental', 'fork-123')"
  },

  "version_content": {
    "idea_title": "string (255 char max, immutable snapshot)",
    "idea_description": "string (10,000 char max, immutable snapshot)",
    "category": "string (immutable snapshot)",
    "domain": "string (immutable snapshot)",
    "tags": ["array of strings (immutable snapshot)"],
    "attachments": {
      "documents": ["URLs (immutable snapshot)"],
      "images": ["URLs (immutable snapshot)"],
      "videos": ["URLs (immutable snapshot)"]
    },
    "estimated_impact": "enum (low|medium|high, immutable snapshot)",
    "target_audience": ["array of user types (immutable snapshot)"],
    "implementation_timeline": "enum (immediate|3_months|6_months|1_year)"
  },

  "change_metadata": {
    "change_type": "enum (CREATION|MINOR_EDIT|MAJOR_EDIT|BUG_FIX|STATUS_CHANGE|FORK|MERGE|ARCHIVE)",
    "change_reason": "string (required, why this version was created)",
    "change_description": "string (detailed changelog entry)",
    "edited_fields": ["array of field names that changed"],
    "breaking_changes": "boolean (true if not backward compatible)",
    "reviewer_id": "UUID (if required approval)",
    "reviewer_comment": "string (feedback from reviewer)",
    "approval_timestamp": "ISO8601"
  },

  "version_metrics": {
    "engagement_count": "integer (views + comments + shares for this version)",
    "comment_count": "integer",
    "share_count": "integer",
    "implementation_count": "integer (teams implementing this exact version)",
    "fork_count": "integer (ideas forked from this version)",
    "quality_score": "float (recalculated at publication)",
    "originality_score": "float (recalculated if significant changes)"
  },

  "version_integrity": {
    "content_hash": "SHA-256 (of idea content, immutable proof)",
    "version_hash": "SHA-256 (of entire version record)",
    "audit_reference": "UUID (link to audit log)",
    "immutable_timestamp": "ISO8601 (when locked)",
    "git_commit": "string (git hash if version controlled)"
  },

  "royalty_impact": {
    "affects_royalty": "boolean (if content changes affect payment)",
    "royalty_multiplier": "float (1.0 = original, 1.2 = 20% increase)",
    "royalty_reason": "string (why royalty changed)",
    "previous_royalty_percent": "float",
    "current_royalty_percent": "float"
  }
}
```

### 3.2 VERSION SNAPSHOT RECORD

```json
{
  "snapshot": {
    "snapshot_id": "UUID (unique per version)",
    "version_id": "UUID (reference to version)",
    "captured_at": "ISO8601",
    "full_state": {
      "all fields from version_content above": "...",
      "metadata at time of capture": "...",
      "calculated_metrics at capture time": "..."
    },
    "storage_location": "s3://ecoskiller-version-snapshots/{tenant_id}/{idea_id}/{version_id}/snapshot.json.gz",
    "snapshot_size_bytes": "integer",
    "compression": "gzip",
    "encryption": "AES-256-KMS",
    "immutable": "true"
  }
}
```

### 3.3 VERSION DIFF RECORD

```json
{
  "diff": {
    "diff_id": "UUID",
    "from_version_id": "UUID",
    "to_version_id": "UUID",
    "generated_at": "ISO8601",
    "diff_summary": {
      "fields_added": ["field_name"],
      "fields_removed": ["field_name"],
      "fields_modified": ["field_name"],
      "modification_count": "integer"
    },
    "detailed_diff": {
      "idea_title": {
        "from": "old title",
        "to": "new title",
        "change_type": "MODIFIED"
      },
      "idea_description": {
        "from": "old description (first 100 chars)...",
        "to": "new description (first 100 chars)...",
        "change_type": "MODIFIED",
        "chars_added": 150,
        "chars_removed": 50,
        "diff_ratio": 0.65
      },
      "attachments": {
        "added": ["new_video_url"],
        "removed": ["old_image_url"],
        "change_type": "MODIFIED"
      }
    },
    "storage_location": "s3://ecoskiller-version-diffs/{tenant_id}/{idea_id}/{from_version}/{to_version}/diff.json",
    "human_readable_summary": "Added video tutorial, fixed typos in description, updated timeline"
  }
}
```

---

## SECTION D — VERSION CREATION & MANAGEMENT

### 4.1 CREATING A NEW VERSION

```yaml
Create_Version_Request:
  
  Input_Schema:
    {
      "idea_id": "UUID (required, existing idea)",
      "version_number": "string (optional, auto-calculated if not provided)",
      "change_type": "enum (required, MINOR_EDIT|MAJOR_EDIT|BUG_FIX|FORK)",
      "change_reason": "string (required, min 10 chars)",
      "edited_fields": ["field_name"] (required, which fields changed),
      "new_content": {
        "idea_title": "string (optional, if changed)",
        "idea_description": "string (optional, if changed)",
        "category": "string (optional, if changed)",
        "domain": "string (optional, if changed)",
        "tags": ["array"] (optional, if changed),
        "attachments": {
          "documents": ["URLs"] (optional),
          "images": ["URLs"] (optional),
          "videos": ["URLs"] (optional)
        },
        "estimated_impact": "enum (optional, if changed)",
        "target_audience": ["array"] (optional, if changed)"
      },
      "fork_metadata": {
        "is_fork": "boolean",
        "fork_parent_id": "UUID (if forking from another idea)",
        "fork_name": "string (e.g., 'Education Edition')"
      }
    }

Validation_Rules:
  
  Step_1_Idea_Exists:
    - Verify idea_id exists
    - Verify user has edit permission
    - Verify tenant matches user's tenant
  
  Step_2_Change_Type_Validation:
    - MINOR_EDIT: only non-breaking field changes
      * Cannot change category/domain (breaking)
      * Cannot change core concept
    - MAJOR_EDIT: substantial content changes allowed
      * Can change category/domain
      * Requires resubmission if from ACTIVE
    - BUG_FIX: only fixes (typos, descriptions)
      * No feature additions
      * Auto-approved (no review needed)
    - FORK: creates independent version tree
      * Requires fork_parent_id
      * Must be from ACTIVE version
  
  Step_3_Edited_Fields_Validation:
    - All specified edited_fields must have changes
    - All changed fields must be in edited_fields list
    - No unlisted fields can be modified
  
  Step_4_Content_Validation:
    - XSS sanitization on all text fields
    - Profanity filter
    - Length validation
    - Domain schema validation
    - User type validation
  
  Step_5_Permission_Check:
    - User must own the idea OR be ADMIN
    - User must not be suspended
    - Tenant must allow versioning
  
  Step_6_Version_Number_Calculation:
    - If not provided, auto-calculate based on change_type:
      * MINOR_EDIT: increment minor (1.0.0 → 1.1.0)
      * BUG_FIX: increment patch (1.1.0 → 1.1.1)
      * MAJOR_EDIT: increment major (1.0.0 → 2.0.0)
      * FORK: add branch suffix (1.0.0-fork-123)
    - Validate version number is unique for this idea
  
  Step_7_State_Transition:
    - Determine new state based on change_type:
      * BUG_FIX from ACTIVE → new version is ACTIVE (auto-approved)
      * MINOR_EDIT from ACTIVE → new version is SUBMITTED (needs review)
      * MAJOR_EDIT from ACTIVE → new version is SUBMITTED (full resubmission)
      * FORK → new version is ACTIVE (independent idea)

Create_Version_Output:
  {
    "status": "ACCEPTED|REJECTED|REVIEW_REQUIRED",
    "version_id": "UUID",
    "version_number": "string (e.g., '1.1.0-ACTIVE')",
    "idea_id": "UUID",
    "parent_version_id": "UUID (previous version)",
    "state": "enum (DRAFT|SUBMITTED|ACTIVE)",
    "next_steps": ["string"],
    "approval_required": "boolean",
    "expected_review_time_hours": "integer (if approval required)",
    "audit_reference": "UUID"
  }

Version_Creation_Examples:
  
  Example_1_Bug_Fix:
    Input:
      idea_id: "idea-123"
      change_type: "BUG_FIX"
      change_reason: "Fixed typos in description section 2"
      edited_fields: ["idea_description"]
      new_content:
        idea_description: "Updated text with corrections..."
    
    Output:
      status: "ACCEPTED"
      version_number: "1.0.1-ACTIVE"
      state: "ACTIVE" (auto-approved)
      next_steps: []
  
  Example_2_Feature_Addition:
    Input:
      idea_id: "idea-123"
      change_type: "MINOR_EDIT"
      change_reason: "Added video tutorial link"
      edited_fields: ["attachments"]
      new_content:
        attachments:
          videos: ["https://youtube.com/watch?v=xxx"]
    
    Output:
      status: "REVIEW_REQUIRED"
      version_number: "1.1.0-SUBMITTED"
      state: "SUBMITTED"
      approval_required: true
      expected_review_time_hours: 24
  
  Example_3_Fork_Creation:
    Input:
      idea_id: "idea-123" (original mobile app)
      change_type: "FORK"
      change_reason: "Adapting idea for education sector"
      fork_metadata:
        is_fork: true
        fork_parent_id: "idea-123"
        fork_name: "Education Edition"
      new_content:
        idea_title: "Mobile App for K-12 Education"
        target_audience: ["school_student", "education_trainer"]
    
    Output:
      status: "ACCEPTED"
      version_number: "1.0.0-fork-789-ACTIVE"
      state: "ACTIVE" (independent version tree)
      fork_parent_id: "idea-123"
```

### 4.2 EDITING & VERSIONING POLICIES

```yaml
Edit_Timeline:
  
  DRAFT_Version:
    - Who_Can_Edit: author + tenant admins
    - Edit_Limit: unlimited
    - Approval_Required: no
    - Implication: changes not tracked until published
    - History: discarded if not submitted
  
  SUBMITTED_Version:
    - Who_Can_Edit: author only (request changes)
    - Edit_Limit: limited (max 3 revisions)
    - Approval_Required: yes
    - Implication: can iterate before approval
    - History: changes tracked in submission notes
    - Timeout: 30 days max (auto-archived if not approved)
  
  ACTIVE_Version:
    - Who_Can_Edit: author only (via new version)
    - Edit_Limit: unlimited new versions
    - Approval_Required: depends on change_type
      * BUG_FIX: no (auto-approved)
      * MINOR_EDIT: yes (24-48 hour review)
      * MAJOR_EDIT: yes (full resubmission)
    - Implication: previous version remains accessible
    - History: creates new version record
  
  DEPRECATED_Version:
    - Who_Can_Edit: no (read-only)
    - Edit_Limit: 0
    - Approval_Required: N/A
    - Implication: successor version must be used
    - History: preserved with deprecation notice
  
  ARCHIVED_Version:
    - Who_Can_Edit: no (read-only)
    - Edit_Limit: 0
    - Approval_Required: N/A
    - Implication: historical reference only
    - History: immutable forever

Concurrent_Edit_Handling:
  
  Scenario: Two users try to edit simultaneously
  
  Solution:
    1. First editor acquires lock (30-minute timeout)
    2. Second editor receives "locked" response
    3. Second editor can read but not modify
    4. First editor finishes, lock released
    5. Second editor can then modify (becomes new version)
  
  Optimistic_Locking:
    - Store version_hash with every request
    - If hash mismatch: reject with conflict error
    - Client resolves conflict and retries
    - Prevents lost updates
  
  Conflict_Resolution:
    - Manual: user must resolve conflicts explicitly
    - Auto: system merges non-overlapping changes
    - Merge_Conflict: if overlapping, manual review required
```

### 4.3 VERSION APPROVAL WORKFLOW

```yaml
Approval_Workflow:

  Trigger: When version moves to SUBMITTED state
  
  Step_1_Notify_Reviewers:
    - Query RBAC: who has IDEA_REVIEWER role for this domain
    - Send notification: "New idea version awaiting review"
    - Set deadline: 48 hours from submission
  
  Step_2_Reviewer_Assessment:
    Reviewer_Can:
      - View version content
      - Compare with previous version (diff view)
      - Request clarifications (async)
      - Approve with confidence score
      - Reject with detailed feedback
    
    Assessment_Criteria:
      - Content quality improved (or maintained)
      - No policy violations introduced
      - No plagiarism concerns
      - No security/privacy issues
      - Changes align with change_reason
  
  Step_3_Approval_Decision:
    
    If_APPROVED:
      - Transition version to ACTIVE
      - Notify submitter: "Your version was approved"
      - Update rank/discovery
      - Emit VERSION_ACTIVATED event
    
    If_REJECTED:
      - Return to DRAFT
      - Provide detailed feedback
      - Notify submitter with remediation steps
      - Emit VERSION_REJECTED event
      - Allow 3 revision attempts
    
    If_CHANGES_REQUESTED:
      - Version stays in SUBMITTED
      - Request specific clarifications
      - Set 7-day deadline for response
      - Notify submitter
    
    If_NO_RESPONSE:
      - After 48 hours with no action
      - Auto-archive SUBMITTED version
      - Notify submitter: "Version auto-archived due to timeout"
      - Previous ACTIVE version remains published
  
  Approval_Roles:
    - IDEA_REVIEWER: 1+ required
    - DOMAIN_EXPERT: optional (for technical review)
    - COMPLIANCE_OFFICER: required if sensitive changes
  
  Approval_Audit:
    {
      "approval_id": "UUID",
      "version_id": "UUID",
      "reviewer_id": "UUID",
      "decision": "APPROVED|REJECTED|CHANGES_REQUESTED",
      "confidence_score": "float (0-1.0)",
      "review_timestamp": "ISO8601",
      "feedback": "string",
      "decision_factors": ["array of reasoning"]
    }
```

---

## SECTION E — VERSION COMPARISON & DIFF

### 5.1 VERSION DIFF GENERATION

```yaml
Diff_Generation:
  
  Trigger: User requests "Compare versions" or automatic on approval
  
  Input:
    - from_version_id: "UUID"
    - to_version_id: "UUID"
  
  Process:
    
    Step_1_Retrieve_Snapshots:
      - Get immutable snapshot of from_version
      - Get immutable snapshot of to_version
      - Verify both versions exist and belong to same idea
    
    Step_2_Field_Level_Diff:
      For each field:
        - idea_title: exact string comparison
        - idea_description: line-by-line diff (like git diff)
        - category: exact enum comparison
        - domain: exact enum comparison
        - tags: array diff (added/removed/modified)
        - attachments: URL comparison
        - metadata fields: timestamp comparison
    
    Step_3_Calculate_Metrics:
      - chars_added: count of new characters
      - chars_removed: count of deleted characters
      - fields_modified: count of changed fields
      - breaking_changes: boolean (category/domain change)
      - significance_score: float (0-1.0, how major is change)
      
      significance_score = (fields_modified / total_fields) * change_weight
      where change_weight = 1.0 for content, 0.5 for metadata
    
    Step_4_Generate_Human_Readable:
      - Create summary sentence per field
      - "Changed title from 'X' to 'Y'"
      - "Added 3 new attachments"
      - "Modified description (150 chars added, 50 removed)"
      - "No breaking changes"
    
    Step_5_Store_Diff:
      - Save to S3: version_diffs/{idea_id}/{from}/{to}/diff.json
      - Cache in PostgreSQL for frequent diffs
      - TTL: 7 days (then re-generate from snapshots)

Diff_Output:
  {
    "diff_id": "UUID",
    "from_version": "1.0.0",
    "to_version": "1.1.0",
    "significance_score": 0.35,
    "breaking_changes": false,
    "summary": "Added video tutorial and updated implementation timeline",
    "field_diffs": {
      "idea_title": {
        "status": "UNCHANGED"
      },
      "idea_description": {
        "status": "MODIFIED",
        "chars_added": 120,
        "chars_removed": 15,
        "diff_preview": "- Old text here\n+ New text here"
      },
      "attachments": {
        "status": "MODIFIED",
        "added": ["https://youtube.com/watch?v=xxx"],
        "removed": []
      },
      "estimated_impact": {
        "status": "UNCHANGED"
      }
    },
    "approval_impact": {
      "likely_approval": true,
      "approval_reason": "Minor content enhancement, no breaking changes"
    }
  }

Diff_Use_Cases:
  
  UC1_Reviewer_Assessment:
    - Reviewer clicks "View Diff"
    - System shows side-by-side comparison
    - Highlights: added (green), removed (red), modified (yellow)
    - Enables fast review process
  
  UC2_Author_Review:
    - Before submitting, author reviews what changed
    - Confirms changes are intentional
    - Catches unintended modifications
  
  UC3_Audit & Compliance:
    - Diff stored immutably
    - Used in compliance audits
    - Proves what changed and why
  
  UC4_Notification:
    - Followers notified: "New version available"
    - Diff summary shown in notification
    - Helps decide to update
```

### 5.2 VERSION COMPARISON MATRIX

```yaml
Comparison_Matrix:
  
  Feature: Allow comparing 2-5 versions side-by-side
  
  Input:
    - version_ids: ["v1.0.0", "v1.1.0", "v1.2.0"]
    - fields_to_compare: ["idea_title", "idea_description", "attachments"]
  
  Output:
    - Table view with one row per version
    - One column per selected field
    - Highlights differences (color-coded)
    - Shows change magnitude
    - Timeline visualization
  
  Example_Output:
    
    Field: idea_description
    ├─ v1.0.0: "Original description... (500 chars)"
    ├─ v1.1.0: "Updated description... (620 chars)" [+120 chars]
    ├─ v1.2.0: "Final description... (615 chars)" [-5 chars]
    
    Field: attachments
    ├─ v1.0.0: document.pdf, image.png (2 items)
    ├─ v1.1.0: document.pdf, image.png, video.mp4 (3 items) [+1]
    ├─ v1.2.0: document.pdf, image.png, video.mp4 (3 items) [no change]
  
  Visualization:
    - Timeline showing progression
    - Point of highest activity
    - Trend lines (getting longer/shorter)
    - Change frequency graph
```

---

## SECTION F — VERSION ROLLBACK & RECOVERY

### 6.1 ROLLBACK CAPABILITIES

```yaml
Rollback_Policy:

  What_Can_Be_Rolled_Back:
    - ✅ ACTIVE version → previous ACTIVE version
    - ✅ SUBMITTED version → previous ACTIVE version
    - ✅ DEPRECATED version → un-deprecate previous version
    - ❌ ARCHIVED version → cannot rollback (immutable)
    - ❌ DELETED version → cannot rollback (legal hold)

  Rollback_Trigger:
    - Manual: author or admin clicks "Rollback"
    - Automatic: none (manual review required)

  Rollback_Process:
    
    Step_1_Verify_Permission:
      - Only author or ADMIN can rollback
      - Must provide reason (audit trail)
      - Reason stored immutably
    
    Step_2_Identify_Target:
      - User selects version to rollback to
      - System shows diff between current and target
      - User confirms before proceeding
    
    Step_3_Create_Rollback_Version:
      - Do NOT revert to old version
      - Instead, CREATE NEW VERSION with old content
      - Version number: continue incrementing
        * Previous: 1.2.3
        * Rollback creates: 1.2.4 (not 1.2.2)
        * Reason: "Rollback to v1.2.1 content"
    
    Step_4_State_Transition:
      - New rollback version inherits parent's state
      - If parent was ACTIVE → new version is ACTIVE
      - If parent was SUBMITTED → new version is SUBMITTED
    
    Step_5_Audit_Trail:
      - Log: ROLLBACK action
      - Record: source version, target version, reason
      - Timestamp: when rollback occurred
      - Never delete original version (full history preserved)

  Rollback_Example:
    
    Original Progression:
      v1.0.0 → v1.1.0 → v1.2.0 → v1.2.1 (current, ACTIVE)
    
    User Action: Rollback to v1.1.0
    
    System Response:
      - Create v1.2.2 with v1.1.0 content
      - Mark v1.2.2 as rollback version
      - Link to source version (v1.1.0)
      - Current state: v1.2.2 (ACTIVE)
      - History preserved:
        v1.0.0 → v1.1.0 → v1.2.0 → v1.2.1 → v1.2.2 [rollback]
    
    Advantages:
      - Full audit trail maintained
      - Can see rollback reason
      - Can rollback again if needed
      - No data loss

  Rollback_Restrictions:
    - Cannot rollback more than 1 major version back (v2.0.0 → v1.x.0 not allowed)
    - Can rollback within same major: v1.5.0 → v1.2.0 allowed
    - Cross-major rollback requires admin approval
    - Rollback windows:
      * ACTIVE: 180 days back
      * SUBMITTED: 30 days back
      * Beyond window: must contact compliance
```

### 6.2 VERSION RECOVERY (Deleted/Archived)

```yaml
Recovery_Policy:

  Deleted_Version_Recovery:
    - DELETED versions soft-deleted (not hard deleted)
    - Recoverable for 7 years (legal hold)
    - Requires: compliance officer approval
    - Process:
      1. Admin requests recovery
      2. Compliance officer reviews reason
      3. If approved: restore version metadata (content from backup)
      4. Create audit entry: recovery reason + approver
      5. Version restored to ARCHIVED state (not ACTIVE)
  
  Archived_Version_Reactivation:
    - Compliance officer can reactivate archived versions
    - Must provide reason (e.g., "Legal discovery")
    - Reactivated versions marked with [RECOVERED] label
    - Original timestamps preserved
    - New recovery audit entry created

  Backup & Recovery:
    - All versions: daily backup to S3 + Glacier
    - Snapshots: immutable, encrypted at rest
    - RPO: 24 hours (max data loss)
    - RTO: 4 hours (max recovery time)
    - Tested quarterly
```

---

## SECTION G — FORKING & DERIVATIVES

### 7.1 FORK MECHANICS

```yaml
Fork_Creation:

  Prerequisites:
    - Source idea must be ACTIVE
    - User must have INNOVATOR role
    - Must provide fork_name (identifier)
    - Must provide fork_reason (audit)
  
  Fork_Request:
    {
      "idea_id": "UUID (source idea)",
      "fork_name": "string (e.g., 'Education Edition')",
      "fork_reason": "string (why forking)",
      "preserve_attribution": "boolean (credit original?)",
      "royalty_sharing": "enum (none|shared|revenue_split)"
    }
  
  Fork_Process:
    
    Step_1_Create_Independent_Copy:
      - Create new idea_id (not same as source)
      - Copy all content from source v1.0.0
      - Link fork_parent_id to source
      - Preserve original metadata (creator, creation date)
    
    Step_2_Version_Tagging:
      - Fork version: 1.0.0-fork-{random_id}
      - Example: 1.0.0-fork-abc123
      - Immutably linked to parent
    
    Step_3_Metadata_Preservation:
      - Fork preserves source idea's category
      - Fork preserves source idea's domain
      - Fork can modify title (required change)
      - Attribution link shown to users
    
    Step_4_Permissions:
      - Fork creator is owner (ADMIN of fork)
      - Can modify freely (independent version tree)
      - Source creator notified of fork
      - Fork is independent (no sync with source)
    
    Step_5_Discovery:
      - Fork discoverable separately
      - Shows "Based on [source idea]" label
      - Source and forks cross-linked

  Fork_Output:
    {
      "status": "ACCEPTED",
      "new_idea_id": "UUID (forked idea)",
      "fork_name": "Education Edition",
      "fork_parent_id": "UUID (source idea)",
      "version_number": "1.0.0-fork-abc123",
      "fork_url": "https://ecoskiller.com/ideas/fork-abc123"
    }

Fork_Examples:
  
  Example_1_Sector_Adaptation:
    Source: "Mobile App Marketplace"
    Fork: "Mobile App Marketplace for Healthcare" (fork-001)
    Changes: Modified target_audience to healthcare workers
  
  Example_2_Geographic_Adaptation:
    Source: "Job Board for US Market"
    Fork: "Job Board for India" (fork-002)
    Changes: Localized, added local languages, regional features
  
  Example_3_Feature_Experimentation:
    Source: "Skill Platform v2.0.0"
    Fork: "Skill Platform with AI Tutoring" (fork-003)
    Changes: Added AI tutoring features (experimental)
```

### 7.2 FORK ATTRIBUTION & ROYALTIES

```yaml
Fork_Attribution:

  Visual_Attribution:
    - Badge: "Based on [Original Idea Name]"
    - Link: clickable to original idea
    - Shown: on fork detail page
    - Visible: to all users
  
  Creator_Attribution:
    - Original creator listed: "Original by [Name]"
    - Fork creator listed: "Adapted by [Name]"
    - Both credited
    - Both receive engagement metrics

  Community_Attribution:
    - Forks contribute to original idea's "impact score"
    - Shows: "This idea inspired 15 other ideas"
    - Ranking boost: for popular source ideas
    - Discovery: "Ideas based on this one" section

Fork_Royalty_Sharing:

  Model_1_No_Sharing:
    - Fork creator: receives 100% of fork's royalties
    - Original creator: receives nothing from fork
    - Use case: major adaptations, different market
  
  Model_2_Shared_Revenue:
    - Fork creator: receives 70% of fork's royalties
    - Original creator: receives 30% of fork's royalties
    - Use case: minor modifications, same market
  
  Model_3_Revenue_Split:
    - Fork creator: receives 90% initially
    - Original creator: receives 10% initially
    - Adjustment: if fork exceeds source in revenue
      * Original creator: increases to 15%
      * Fork creator: decreases to 85%
    - Use case: derivative works that outperform source
  
  Configuration:
    - Set at fork creation time
    - Immutable (cannot change later)
    - Stored in fork_parent_agreement
    - Auditable (all splits logged)
  
  Implementation:
    - Royalty engine queries fork_parent_id
    - If found: apply split logic
    - Payment: automatic monthly
    - Dispute resolution: escalate to compliance
```

---

## SECTION H — VERSION LIFECYCLE EVENTS

### 8.1 VERSION EVENTS EMITTED

```yaml
Event_Bus_Events:

  Event_1_VERSION_CREATED:
    Trigger: New version created (any state)
    Payload:
      {
        "event_id": "UUID",
        "version_id": "UUID",
        "idea_id": "UUID",
        "version_number": "string",
        "creator_id": "UUID",
        "change_type": "string",
        "timestamp": "ISO8601"
      }
    Consumers:
      - AUDIT_AGENT (log change)
      - FEATURE_STORE_AGENT (update user metrics)
      - NOTIFICATION_AGENT (notify watchers - optional)
  
  Event_2_VERSION_SUBMITTED:
    Trigger: Version moves to SUBMITTED state
    Payload:
      {
        "event_id": "UUID",
        "version_id": "UUID",
        "idea_id": "UUID",
        "creator_id": "UUID",
        "timestamp": "ISO8601",
        "review_deadline": "ISO8601"
      }
    Consumers:
      - NOTIFICATION_AGENT (notify reviewers)
      - AUDIT_AGENT (log submission)
  
  Event_3_VERSION_APPROVED:
    Trigger: Reviewer approves, version moves to ACTIVE
    Payload:
      {
        "event_id": "UUID",
        "version_id": "UUID",
        "idea_id": "UUID",
        "reviewer_id": "UUID",
        "approval_timestamp": "ISO8601",
        "confidence_score": "float"
      }
    Consumers:
      - RANKING_ENGINE (update ranking)
      - NOTIFICATION_AGENT (notify creator)
      - FEATURE_STORE_AGENT (record approval event)
  
  Event_4_VERSION_REJECTED:
    Trigger: Reviewer rejects, version returns to DRAFT
    Payload:
      {
        "event_id": "UUID",
        "version_id": "UUID",
        "reviewer_id": "UUID",
        "feedback": "string",
        "rejection_timestamp": "ISO8601"
      }
    Consumers:
      - NOTIFICATION_AGENT (notify creator with feedback)
      - AUDIT_AGENT (log rejection)
  
  Event_5_VERSION_DEPRECATED:
    Trigger: Version marked as deprecated
    Payload:
      {
        "event_id": "UUID",
        "version_id": "UUID",
        "successor_version_id": "UUID",
        "deprecation_reason": "string",
        "timestamp": "ISO8601"
      }
    Consumers:
      - RANKING_ENGINE (update ranking, reduce visibility)
      - NOTIFICATION_AGENT (notify users following this version)
  
  Event_6_VERSION_ARCHIVED:
    Trigger: Deprecated version moved to archive
    Payload:
      {
        "event_id": "UUID",
        "version_id": "UUID",
        "archived_timestamp": "ISO8601",
        "retention_until": "ISO8601"
      }
    Consumers:
      - AUDIT_AGENT (finalize audit)
      - SEARCH_INDEX_AGENT (remove from search)
  
  Event_7_VERSION_ROLLED_BACK:
    Trigger: Version rolled back to previous
    Payload:
      {
        "event_id": "UUID",
        "rolled_back_version_id": "UUID",
        "target_version_id": "UUID",
        "new_version_id": "UUID",
        "rollback_reason": "string",
        "timestamp": "ISO8601"
      }
    Consumers:
      - AUDIT_AGENT (log rollback)
      - NOTIFICATION_AGENT (notify watchers)
  
  Event_8_VERSION_FORKED:
    Trigger: New fork created from version
    Payload:
      {
        "event_id": "UUID",
        "source_version_id": "UUID",
        "new_fork_idea_id": "UUID",
        "fork_name": "string",
        "fork_creator_id": "UUID",
        "timestamp": "ISO8601"
      }
    Consumers:
      - NOTIFICATION_AGENT (notify source creator)
      - RANKING_ENGINE (add fork to source's related ideas)
      - FEATURE_STORE_AGENT (record fork activity)
```

### 8.2 EVENT IDEMPOTENCY

```yaml
Idempotency_Key_Strategy:

  Every_Event_Includes:
    - event_id: UUID (unique, immutable)
    - timestamp: ISO8601 (monotonic)
    - idempotency_key: { event_id + timestamp }
  
  Consumer_Handling:
    - Store idempotency_key in database
    - If duplicate event received: discard (already processed)
    - Ensures: single event processed exactly once
    - Window: 30 days (then safe to discard)

  Event_Replay_Safety:
    - All consumers must be idempotent
    - Processing same event twice = same result
    - No side effects from duplicate processing
```

---

## SECTION I — VERSION STORAGE & ARCHIVAL

### 9.1 VERSION STORAGE STRATEGY

```yaml
Storage_Tiering:

  Hot_Storage (PostgreSQL):
    - All ACTIVE versions
    - Recent SUBMITTED versions (< 30 days)
    - Retention: unlimited
    - Latency: milliseconds
    - Replicas: 3 (HA)
  
  Warm_Storage (S3 Standard):
    - ACTIVE snapshots (monthly backups)
    - DEPRECATED versions (full copies)
    - ARCHIVED versions (recent, < 2 years)
    - Retention: 2 years
    - Latency: seconds to minutes
    - Cost: standard S3
  
  Cold_Storage (S3 Glacier):
    - ARCHIVED versions (older, > 2 years)
    - DELETED versions (legal hold, 7 years)
    - Retention: 7 years (legal requirement)
    - Latency: hours
    - Cost: 10% of standard S3
  
  Archive_Location:
    s3://ecoskiller-version-archive/{tenant_id}/{idea_id}/{version_id}/
    ├── snapshot.json.gz (immutable full state)
    ├── metadata.json (version metadata)
    ├── audit_trail.ndjson (all changes)
    └── diff_history.json (all diffs)

Storage_Encryption:
  - At rest: AES-256-KMS (per-tenant key)
  - In transit: TLS 1.3
  - Keys: managed by AWS KMS, rotated annually
  - Backup keys: separate jurisdiction (GDPR compliance)

Immutability_Guarantee:
  - PostgreSQL: immutable records (no UPDATE, only INSERT)
  - S3: versioning enabled, MFA delete
  - S3: Object Lock enabled (GOVERNANCE mode, 7 years)
  - Prevents: accidental or malicious deletion
```

### 9.2 VERSION GARBAGE COLLECTION & RETENTION

```yaml
Retention_Policy:

  ACTIVE_Versions:
    - Retained: indefinitely
    - Deleted: only via hard delete (compliance required)
    - Backups: daily to S3
  
  DEPRECATED_Versions:
    - Kept: minimum 6 months
    - Can_Archive: after 6 months
    - Backups: weekly
  
  ARCHIVED_Versions:
    - Kept: 7 years (legal hold)
    - Cannot_Delete: before 7 years
    - Moved to: Glacier after 2 years
    - Backups: monthly
  
  SUBMITTED_Versions (Not Approved):
    - Kept: 30 days
    - Auto_Archive: if not approved within 30 days
    - Backups: weekly
  
  DELETED_Versions:
    - Soft_Delete: mark as deleted, not hard deleted
    - Kept: 7 years
    - Backups: immutable
    - Recovery: possible with compliance approval

Cleanup_Schedule:
  - Daily: remove draft versions (no approvals, > 90 days old)
  - Weekly: archive submitted (not approved, > 30 days)
  - Monthly: move archived (> 2 years) to Glacier
  - Yearly: verify 7-year legal hold compliance

Garbage_Collection_Jobs:
  ```python
  def cleanup_old_drafts():
    # Delete DRAFT versions older than 90 days
    # Only if no submission history
    cutoff = now() - 90.days
    query = "SELECT version_id FROM versions
             WHERE status = 'DRAFT' AND created_at < cutoff
             AND submission_count = 0"
  
  def archive_expired_submitted():
    # Archive SUBMITTED versions older than 30 days
    cutoff = now() - 30.days
    query = "SELECT version_id FROM versions
             WHERE status = 'SUBMITTED' AND created_at < cutoff"
    # Move to ARCHIVED state
  
  def move_to_glacier():
    # Move ARCHIVED (> 2 years) to Glacier
    cutoff = now() - 2.years
    query = "SELECT s3_object_key FROM versions
             WHERE status = 'ARCHIVED' AND archived_at < cutoff"
    # Execute S3 transition via Glacier lifecycle policy
  ```
```

---

## SECTION J — VERSION SECURITY & COMPLIANCE

### 10.1 VERSION ACCESS CONTROL

```yaml
Access_Control_Matrix:

  | Action | OWNER | COLLABORATOR | REVIEWER | ADMIN | PUBLIC |
  |--------|-------|--------------|----------|-------|--------|
  | Read DRAFT | ✅ | ❌ | ❌ | ✅ | ❌ |
  | Read SUBMITTED | ✅ | ✅ | ✅ | ✅ | ❌ |
  | Read ACTIVE | ✅ | ✅ | ✅ | ✅ | ✅ |
  | Read DEPRECATED | ✅ | ✅ | ✅ | ✅ | ✅ |
  | Read ARCHIVED | ✅ | ❌ | ❌ | ✅ | ❌ |
  | Create Version | ✅ | ✅ | ❌ | ✅ | ❌ |
  | Edit DRAFT | ✅ | ✅ | ❌ | ✅ | ❌ |
  | Edit ACTIVE | ❌ | ❌ | ❌ | ✅ | ❌ |
  | Approve Version | ❌ | ❌ | ✅ | ✅ | ❌ |
  | Rollback | ✅ | ❌ | ❌ | ✅ | ❌ |
  | Archive | ❌ | ❌ | ❌ | ✅ | ❌ |
  | Delete (soft) | ❌ | ❌ | ❌ | ✅ | ❌ |
  
  Notes:
  - OWNER: idea creator
  - COLLABORATOR: invited to work on idea (future feature)
  - REVIEWER: approved to review idea versions
  - ADMIN: tenant admin with all permissions
  - PUBLIC: not logged in users

Version_Visibility:
  
  DRAFT:
    - Visible to: OWNER + ADMIN
    - Discoverable: NO (not in search)
    - Shareable: via direct link only
  
  SUBMITTED:
    - Visible to: OWNER + COLLABORATORS + REVIEWERS + ADMIN
    - Discoverable: NO
    - Shareable: NO (except to assigned reviewers)
  
  ACTIVE:
    - Visible to: Everyone (based on tenant settings)
    - Discoverable: YES (in search)
    - Shareable: YES (public link)
  
  DEPRECATED:
    - Visible to: Everyone (with deprecation label)
    - Discoverable: YES (but lower rank)
    - Shareable: YES (with successor link)
  
  ARCHIVED:
    - Visible to: OWNER + ADMIN
    - Discoverable: NO
    - Shareable: NO
```

### 10.2 VERSION AUDIT TRAIL

```yaml
Audit_Logging:

  Event_Audit_Schema:
    {
      "audit_id": "UUID (immutable)",
      "timestamp_utc": "ISO8601 (immutable)",
      "version_id": "UUID",
      "idea_id": "UUID",
      "actor_id": "UUID",
      "actor_role": "string",
      "action": "enum (CREATE|EDIT|SUBMIT|APPROVE|REJECT|ARCHIVE|DELETE|ROLLBACK)",
      "before_state": {JSON snapshot},
      "after_state": {JSON snapshot},
      "change_details": "string",
      "result": "enum (SUCCESS|FAILURE)",
      "error_code": "string (if failure)"
    }

  Audit_Log_Storage:
    - Primary: PostgreSQL (append-only table)
    - Archive: S3 (daily exports)
    - Retention: 7 years
    - Immutability: no DELETE privilege
    - Access: audit trail itself audited

  Audit_Queries (examples):
    
    # All changes to version by date
    SELECT * FROM audit_logs
    WHERE version_id = 'v-123' AND action IN ('EDIT', 'APPROVE')
    ORDER BY timestamp DESC
    
    # Who edited this version and when
    SELECT DISTINCT actor_id, COUNT(*) as edit_count
    FROM audit_logs
    WHERE version_id = 'v-123' AND action = 'EDIT'
    GROUP BY actor_id
    
    # All rollbacks in tenant
    SELECT * FROM audit_logs
    WHERE tenant_id = 't-456' AND action = 'ROLLBACK'
    
    # Approve chain for version
    SELECT actor_id, timestamp, result
    FROM audit_logs
    WHERE version_id = 'v-123' AND action = 'APPROVE'
    ORDER BY timestamp
```

---

## SECTION K — VERSION ANALYTICS & INSIGHTS

### 11.1 VERSION METRICS

```yaml
Version_Metrics_Tracked:

  Engagement_Metrics:
    - views_count: total views of this version
    - unique_views: unique users who viewed
    - comment_count: comments on this version
    - share_count: times shared
    - implementation_count: teams building this version
    - fork_count: ideas forked from this version
  
  Quality_Metrics:
    - quality_score: ML-calculated (0-1.0)
    - originality_score: similarity-based (0-1.0)
    - clarity_score: readability assessment (0-1.0)
    - completeness_score: how complete is documentation (0-1.0)
  
  Temporal_Metrics:
    - creation_date: when version created
    - time_to_approval: days from SUBMITTED to ACTIVE
    - time_to_deprecation: days from ACTIVE to DEPRECATED
    - age_days: how old is version
    - revision_frequency: avg days between versions
  
  Change_Metrics:
    - edits_count: total number of edits made
    - fields_modified: which fields changed most often
    - breaking_changes_count: major pivots
    - rollback_count: times rolled back
  
  Collaboration_Metrics:
    - reviewer_count: unique reviewers
    - revision_rounds: how many review cycles
    - feedback_items: suggestions received
    - implementation_partners: who's building

Version_Analytics_Dashboard:
  - Trend lines: how metrics evolve over versions
  - Heatmap: which fields change most often
  - Timeline: version creation frequency
  - Adoption curve: time to reach peak engagement
  - Retention: what % of users stick with newer versions
```

### 11.2 VERSION RANKING & DISCOVERY

```yaml
Version_Ranking_Factors:

  Content_Quality:
    - quality_score: 30% weight
    - originality_score: 20% weight
    - clarity_score: 10% weight
  
  Engagement:
    - views_count: 15% weight
    - implementation_count: 15% weight
    - share_count: 5% weight
  
  Recency:
    - age_decay: newer versions boosted
    - decay_formula: 1 / (1 + log(age_days))
    - window: boost for first 30 days
  
  Stability:
    - rollback_count: penalize frequent rollbacks
    - rejection_count: penalize rejections
    - approval_confidence: higher confidence = boost
  
  Version_Rank_Score:
    rank_score = (
      quality * 0.30 +
      originality * 0.20 +
      clarity * 0.10 +
      (views / max_views) * 0.15 +
      (implementations / max_implementations) * 0.15 +
      (shares / max_shares) * 0.05 +
      recency_boost +
      stability_factor
    )
  
  Discovery_Features:
    - Latest version: pinned at top
    - Popular version: sorted by engagement
    - Trending: spikes in activity
    - Recommended: personalized by user interests
    - Related versions: from same author or domain
```

---

## SECTION L — VERSIONING BEST PRACTICES

### 12.1 VERSION MANAGEMENT GUIDELINES

```yaml
Recommended_Practices:

  1_Semantic_Versioning:
    - Use MAJOR.MINOR.PATCH consistently
    - Bump MAJOR only for breaking changes
    - Bump MINOR for features
    - Bump PATCH for fixes
    - Avoid: jumping versions (1.0 → 3.0) without reason
  
  2_Clear_Change_Reasons:
    - Always document why version created
    - Minimum 10 characters (enforced)
    - Examples:
      * "Added video tutorial for better clarity"
      * "Fixed grammatical errors in description"
      * "Adapted for education sector market"
    - Avoid: "Minor changes", "Updated", "Fixed stuff"
  
  3_Meaningful_Commits:
    - One logical change per version
    - Avoid: bundling unrelated changes
    - Example: Don't combine feature + bug fix
    - Reason: easier to review, easier to rollback
  
  4_Review_Before_Submitting:
    - Always review diff before submission
    - Check: nothing unintended changed
    - Catch: typos before approval
    - Saves: reviewer's time
  
  5_Responsive_to_Feedback:
    - Address reviewer feedback quickly
    - Respond to comments within 7 days
    - Submit revisions promptly
    - Avoid: abandoned submissions
  
  6_Version_Lifecycle_Management:
    - Deprecate outdated versions promptly
    - Archive after 6+ months of deprecation
    - Link successor version clearly
    - Notify users of deprecation
  
  7_Collaboration_Etiquette:
    - Don't edit during review (confusing reviewers)
    - Request feedback, don't demand approval
    - Be responsive to questions
    - Test thoroughly before submission
  
  8_Release_Notes:
    - For every version, write release notes
    - Format: bullet points, readable summary
    - Include: what changed, why, migration guide
    - Helps: users understand new features
  
  9_Breaking_Change_Warnings:
    - When bumping MAJOR version: clearly warn users
    - Provide: migration guide for implementation
    - Offer: grace period (keep old version accessible)
    - Plan: smooth transition path
  
  10_Archive_Policy:
    - Don't delete versions (even if not popular)
    - Archive instead (preserves history)
    - Reason: audit trail, compliance, learning

Anti_Patterns_to_Avoid:

  ❌ Vague_Change_Reasons:
    - "Updated", "Modified", "Tweaked"
    - Better: Be specific about what changed
  
  ❌ Too_Many_Versions:
    - Submitting new version every day
    - Better: Batch related changes, reduce noise
  
  ❌ Unreviewed_Submissions:
    - Submitting without testing
    - Better: Self-review, catch issues first
  
  ❌ Ignoring_Feedback:
    - Not responding to reviewer comments
    - Better: Engage with reviewers, address concerns
  
  ❌ Scope_Creep:
    - Bundling features + fixes + refactor in one version
    - Better: One logical change per version
  
  ❌ Abandoned_Submissions:
    - Leaving SUBMITTED versions hanging
    - Better: Respond within 7 days, ask for help
  
  ❌ Version_Hoarding:
    - Creating versions but never publishing
    - Better: Publish or delete DRAFT versions
  
  ❌ Unclear_Deprecation:
    - Deprecating without indicating successor
    - Better: Link to next version, explain why
```

---

## SECTION M — NON-NEGOTIABLE RULES

### 13.1 FORBIDDEN ACTIONS

```yaml
Rule_1: VERSION_MODIFICATION_IS_IMMUTABLE
  
  ❌ Forbidden:
    - Edit existing version record
    - Change version number after creation
    - Retroactively adjust version metadata
    - Modify immutable snapshots
    - Delete version content
  
  ✅ Required:
    - All changes: new version
    - Version numbers: never change
    - Snapshots: immutable forever
    - Old versions: preserved

Rule_2: NO_HIDDEN_VERSION_HISTORY
  
  ❌ Forbidden:
    - Skip version numbers (1.0 → 2.0 with no 1.1)
    - Hide revisions from audit trail
    - Suppress failed submissions
    - Delete approval records
  
  ✅ Required:
    - All versions listed
    - Full submission history shown
    - All approvals audited
    - Reject reasons documented

Rule_3: NO_RETROACTIVE_STATE_CHANGES
  
  ❌ Forbidden:
    - Change version status after creation
    - Mark ACTIVE as SUBMITTED (reverse state)
    - Undo deprecation without new version
    - Reactivate ARCHIVED without approval
  
  ✅ Required:
    - States are forward-only
    - State changes immutable
    - Any reversal: create new version
    - Audit all state transitions

Rule_4: NO_CROSS_IDEA_VERSION_MIXING
  
  ❌ Forbidden:
    - Merge versions from different ideas
    - Cross-reference versions from different tenants
    - Transfer version ownership between ideas
    - Mix parent/child versions
  
  ✅ Required:
    - Versions belong to exactly one idea
    - Versions belong to exactly one tenant
    - Parent-child links immutable
    - No cross-idea relationships

Rule_5: NO_SELECTIVE_VISIBILITY
  
  ❌ Forbidden:
    - Hide versions from audit trail
    - Make versions selectively visible to users
    - Create "secret" versions
    - Bypass approval for some versions
  
  ✅ Required:
    - Visibility rules consistent
    - State determines visibility (not secrets)
    - All versions in audit trail
    - No exceptions to workflows

Rule_6: NO_APPROVAL_BYPASS
  
  ❌ Forbidden:
    - Auto-approve versions without review
    - Skip approval for admin-created versions
    - Bypass reviewer (except BUG_FIX auto-approval)
    - Force approval without consensus
  
  ✅ Required:
    - All MAJOR/MINOR: review required
    - BUG_FIX: only auto-approval (policy enforcement)
    - Multi-reviewer: for sensitive domains
    - Documented approval criteria

Rule_7: NO_ROYALTY_MANIPULATION
  
  ❌ Forbidden:
    - Change royalty percent after version creation
    - Retroactively adjust fork royalties
    - Hide royalty calculations
    - Manipulate version significance for royalties
  
  ✅ Required:
    - Royalty terms: set at version creation
    - Immutable: cannot change later
    - Transparent: all calculations logged
    - Auditable: royalty trail preserved

Rule_8: NO_SCOPE_VIOLATION
  
  ❌ Forbidden:
    - Modify user accounts from version agent
    - Change idea owner via versioning
    - Create ideas from versioning (only versions)
    - Access other tenants' versions
  
  ✅ Required:
    - Strict scope: versions only
    - Tenant isolation: enforced
    - No side effects: pure versioning
    - No privilege escalation
```

---

## SECTION N — SEALED GOVERNANCE

### 14.1 MODIFICATION RULES

```yaml
Modification_Policy: ADD-ONLY VIA VERSION BUMP

This_Document_Is:
  - ✅ Sealed: No in-document modifications allowed
  - ✅ Locked: Requires version bump to change anything
  - ✅ Versioned: Every change tracked (v1.0.0 → v1.1.0)
  - ✅ Audited: All changes logged with timestamp & approver
  - ✅ Immutable: Previous versions preserved forever

Change_Request_Process:
  
  Step_1_Proposal:
    - Write change request document
    - Specify: what_changing, why_changing, impact_analysis
    - Include: backwards_compatibility assessment
  
  Step_2_Review:
    - Architecture review (3 approvers minimum)
    - Compliance review (if affecting version policies)
    - Product review (if affecting user workflows)
    - Implementation review (if affecting systems)
  
  Step_3_Approval:
    - All reviewers must approve
    - Document approval timestamps
    - Link to change request in git history
  
  Step_4_Implementation:
    - Version bump semantic versioning
    - Create git tag (immutable, signed)
    - Update /config/service_manifest.yaml
    - Prepare migration guide if breaking change
  
  Step_5_Deployment:
    - Deploy to staging first
    - Validate behavior
    - Document any behavioral changes
    - Deploy to production with canary
  
  Step_6_Documentation:
    - Update CHANGELOG.md
    - Update this spec (new version file)
    - Archive previous version
    - Communicate to stakeholders

Approval_Authority:
  - Architecture: CTO + 2 architects
  - Compliance: Compliance officer + legal
  - Product: Product manager + UX lead
  - Implementation: Tech lead
  - All must sign off before deployment
```

### 14.2 SEAL DECLARATION

```
🔒 SEAL CERTIFICATION

Document: IDEA_VERSIONING.md
Version: 1.0.0
Status: SEALED & LOCKED
Sealed_Date: 2025-02-25
Sealed_By: [AUTHORIZED_SIGNER]
Sealed_Timestamp: 2025-02-25T16:30:00Z

This document defines the IDEA_VERSIONING_MANAGER agent for Ecoskiller
Antigravity platform. It represents the complete, authoritative specification
for idea version management and lifecycle.

SEALED guarantees:
✅ No modifications without version bump
✅ All changes require multi-level approval
✅ Previous versions archived immutably
✅ Audit trail of all changes maintained
✅ Backwards compatibility rules enforced
✅ Governance oversight mandatory

Authorized_Signatories:
- [CTO Name] - Architecture Authority
- [Compliance Officer Name] - Legal Authority
- [Product Lead Name] - Product Authority
- [Tech Lead Name] - Implementation Authority

This seal is cryptographically bound to git commit hash:
Commit: [GIT_COMMIT_HASH_SHA256]
Tree: [GIT_TREE_HASH]
Sign: [GPG_SIGNATURE]

Any modification requires explicit re-sealing and re-certification.

Seal Integrity: VERIFIED ✅
```

---

## APPENDIX A — VERSION STATE MACHINE DIAGRAM

```
DRAFT                      SUBMITTED
  │                           │
  │─────→ (submit) ──────────→│
  │                           │
  │                    (approve) │ (reject)
  │                    │         │
  │←──────(request changes)     │
  │                    ↓         ↓
  └──────────────────→ ACTIVE   DRAFT
                        │        
                   (deprecate)   
                        │         
                        ↓         
                    DEPRECATED    
                        │         
                    (archive)     
                        │         
                        ↓         
                    ARCHIVED      
                        │         
                   (legal hold)   
                        │         
                        ↓         
                     DELETED      

Note: State transitions are forward-only
      No reversal allowed (only new versions)
      All state changes immutable
```

---

## APPENDIX B — VERSION NUMBERING EXAMPLES

```yaml
Progression_Example_1_Linear:
  1.0.0 (initial submission)
  └─→ 1.0.1 (typo fixes)
      └─→ 1.1.0 (added attachments)
          └─→ 1.1.1 (minor fixes)
              └─→ 1.2.0 (added video)
                  └─→ 2.0.0 (major rewrite)

Progression_Example_2_With_Fork:
  1.0.0 (original mobile app)
  └─→ 1.1.0 (added features)
  
  Fork from 1.0.0:
  1.0.0-fork-123 (education edition)
  └─→ 1.1.0-fork-123 (customizations)
      └─→ 1.2.0-fork-123 (major changes)

Progression_Example_3_With_Merge:
  Original: 1.0.0 → 1.1.0 → 1.2.0
  Fork: 1.0.0-fork → 1.1.0-fork (diverges)
  
  Merge fork back:
  1.2.0 → 1.3.0 (merged features from fork)

Progression_Example_4_With_Rollback:
  1.0.0 → 1.1.0 → 1.2.0 (bad version)
  Rollback to 1.1.0:
  1.0.0 → 1.1.0 → 1.2.0 → 1.2.1 (rollback, has 1.1.0 content)
```

---

## SUMMARY

This IDEA_VERSIONING.md specification defines a production-grade, enterprise-scale system for managing idea versions on Ecoskiller Antigravity.

**Key Characteristics:**
- ✅ Deterministic (same input → same output)
- ✅ Immutable (versions never modified, only created)
- ✅ Auditable (append-only audit trail)
- ✅ Compliant (governance & approval workflows)
- ✅ Secure (tenant isolation, access control)
- ✅ Scalable (100M+ versions, fast retrieval)
- ✅ Observable (comprehensive metrics & analytics)
- ✅ Sealed (locked specification)

**Non-Negotiable:**
- No version modification (only creation)
- No hidden history (all versions visible)
- No retroactive changes (immutable)
- No approval bypass (except BUG_FIX)
- No scope violations (versions only)
- No royalty manipulation (terms fixed)
- No hidden logic (transparent workflows)
- No cross-tenant access (strict isolation)

**Execution Authority:** Human declaration only
**Mutation Policy:** Add-only via version bump
**Status:** SEALED & LOCKED
**Effective Date:** 2025-02-25

---

*End of Specification*
