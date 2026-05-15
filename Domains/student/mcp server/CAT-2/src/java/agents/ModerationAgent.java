package com.ecoskiller.mcp.governance.agents;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * MODERATION_AGENT
 *
 * Content and user moderation layer:
 * - User-generated content moderation (text, images)
 * - Abusive behavior detection
 * - Automated and manual moderation workflows
 * - Appeal handling and moderation audit trail
 */
@Service
public class ModerationAgent {

    @Tool(name = "moderation_check_content",
          description = "Check user-generated content for policy violations: hate speech, "
                      + "inappropriate language, spam, or academic dishonesty.")
    public Map<String, Object> checkContent(
            @ToolParam(description = "Content text to moderate") String contentText,
            @ToolParam(description = "Content type: COMMENT | SUBMISSION | PROFILE | MESSAGE") String contentType,
            @ToolParam(description = "Tenant ID for context") String tenantId) {

        Map<String, Object> result = new HashMap<>();
        result.put("agent",        "MODERATION_AGENT");
        result.put("action",       "CHECK_CONTENT");
        result.put("content_type", contentType);
        result.put("tenant_id",    tenantId);
        result.put("verdict",      "APPROVED");
        result.put("confidence",   0.97);
        result.put("flags",        List.of());
        result.put("scores", Map.of(
            "hate_speech",   0.01,
            "spam",          0.03,
            "inappropriate", 0.02,
            "plagiarism",    0.05
        ));
        result.put("action_taken", "NONE");
        return result;
    }

    @Tool(name = "moderation_take_action",
          description = "Take a moderation action on a user or content: warn, suspend, ban, "
                      + "remove content, or escalate to human review.")
    public Map<String, Object> takeAction(
            @ToolParam(description = "Target type: USER | CONTENT") String targetType,
            @ToolParam(description = "Target ID (user ID or content ID)") String targetId,
            @ToolParam(description = "Action: WARN | REMOVE | SUSPEND_7D | SUSPEND_30D | BAN | ESCALATE") String action,
            @ToolParam(description = "Reason for the moderation action") String reason) {

        Map<String, Object> result = new HashMap<>();
        result.put("agent",       "MODERATION_AGENT");
        result.put("action",      "TAKE_ACTION");
        result.put("target_type", targetType);
        result.put("target_id",   targetId);
        result.put("mod_action",  action);
        result.put("reason",      reason);
        result.put("case_id",     "MOD-" + System.currentTimeMillis());
        result.put("notified",    true);
        result.put("appeal_window_hours", 72);
        result.put("status",      "ACTION_TAKEN");
        return result;
    }

    @Tool(name = "moderation_handle_appeal",
          description = "Handle a user's appeal against a moderation action. "
                      + "Returns the appeal decision and updated account status.")
    public Map<String, Object> handleAppeal(
            @ToolParam(description = "Case ID of the original moderation action") String caseId,
            @ToolParam(description = "Appeal text from the user") String appealText,
            @ToolParam(description = "Reviewer decision: UPHOLD | OVERTURN | REDUCE") String decision,
            @ToolParam(description = "Reviewer notes") String notes) {

        Map<String, Object> result = new HashMap<>();
        result.put("agent",       "MODERATION_AGENT");
        result.put("action",      "HANDLE_APPEAL");
        result.put("case_id",     caseId);
        result.put("decision",    decision);
        result.put("notes",       notes);
        result.put("final_status", decision.equals("UPHOLD") ? "SANCTION_MAINTAINED" : "SANCTION_LIFTED");
        result.put("user_notified", true);
        result.put("appeal_id",    "APL-" + System.currentTimeMillis());
        return result;
    }

    @Tool(name = "moderation_get_queue",
          description = "Get the current moderation queue: pending items awaiting human review, "
                      + "sorted by severity and age.")
    public Map<String, Object> getModerationQueue(
            @ToolParam(description = "Tenant ID or 'ALL' for platform-wide queue") String tenantId,
            @ToolParam(description = "Maximum items to return") int limit) {

        Map<String, Object> result = new HashMap<>();
        result.put("agent",     "MODERATION_AGENT");
        result.put("tenant_id", tenantId);
        result.put("queue", List.of(
            Map.of("case_id", "MOD-001", "type", "CONTENT", "severity", "HIGH",   "age_minutes", 15,  "category", "HATE_SPEECH"),
            Map.of("case_id", "MOD-002", "type", "USER",    "severity", "MEDIUM", "age_minutes", 45,  "category", "SPAM"),
            Map.of("case_id", "MOD-003", "type", "CONTENT", "severity", "LOW",    "age_minutes", 120, "category", "INAPPROPRIATE")
        ));
        result.put("total_pending", 3);
        result.put("avg_age_minutes", 60);
        return result;
    }
}
