package com.ecoskiller.mcp.trust.agents;

import org.springframework.stereotype.Component;
import java.util.*;

/**
 * VENDOR_RISK_EVALUATION_AGENT
 *
 * Evaluates third-party vendors and integration partners for trust,
 * security posture, compliance, and financial risk.
 *
 * Actions:
 *   - EVALUATE_VENDOR     : Run a full vendor risk assessment
 *   - GET_RISK_SCORE      : Get current risk score for a vendor
 *   - FLAG_VENDOR         : Flag a vendor for review
 *   - APPROVE_VENDOR      : Approve vendor after review
 *   - LIST_VENDORS        : List all vendors with risk tiers
 */
@Component
public class VendorRiskEvaluationAgent extends BaseAgent {

    @Override
    public String agentName() {
        return "VENDOR_RISK_EVALUATION_AGENT";
    }

    @Override
    public Map<String, Object> invoke(String action, Map<String, Object> payload,
                                      String tenantId, String userId) {
        log.info("[{}] action={} tenant={}", agentName(), action, tenantId);

        return switch (action.toUpperCase()) {

            case "EVALUATE_VENDOR" -> {
                String vendorId   = str(payload, "vendorId");
                String vendorName = str(payload, "vendorName");
                double secScore   = 74.0;
                double compScore  = 81.0;
                double finScore   = 68.0;
                double overall    = (secScore + compScore + finScore) / 3.0;
                yield result(
                    "vendorId",    vendorId,
                    "vendorName",  vendorName,
                    "overallRisk", overall,
                    "riskTier",    overall >= 75 ? "LOW" : overall >= 50 ? "MEDIUM" : "HIGH",
                    "scores", Map.of(
                        "security",    secScore,
                        "compliance",  compScore,
                        "financial",   finScore,
                        "operational", 72.0
                    ),
                    "recommendation","APPROVED_WITH_CONDITIONS",
                    "conditions",  List.of("Annual security audit required","SOC2 report mandatory"),
                    "evaluatedBy", userId,
                    "evaluatedAt", java.time.Instant.now().toString(),
                    "nextReview",  java.time.Instant.now().plusSeconds(365*24*3600L).toString()
                );
            }

            case "GET_RISK_SCORE" -> {
                String vendorId = str(payload, "vendorId");
                yield result(
                    "vendorId",    vendorId,
                    "riskScore",   74.5,
                    "riskTier",    "LOW",
                    "lastEvaluated","2025-01-01T10:00:00Z",
                    "trend",       "IMPROVING",
                    "delta",       +3.2
                );
            }

            case "FLAG_VENDOR" -> {
                String vendorId = str(payload, "vendorId");
                String reason   = str(payload, "reason");
                yield result(
                    "vendorId",  vendorId,
                    "flagged",   true,
                    "reason",    reason.isEmpty() ? "MANUAL_REVIEW_REQUESTED" : reason,
                    "flaggedBy", userId,
                    "flaggedAt", java.time.Instant.now().toString(),
                    "status",    "UNDER_REVIEW",
                    "accessSuspended", false
                );
            }

            case "APPROVE_VENDOR" -> {
                String vendorId   = str(payload, "vendorId");
                String conditions = str(payload, "conditions");
                yield result(
                    "vendorId",   vendorId,
                    "approved",   true,
                    "conditions", conditions.isEmpty() ? "None" : conditions,
                    "approvedBy", userId,
                    "approvedAt", java.time.Instant.now().toString(),
                    "validUntil", java.time.Instant.now().plusSeconds(365*24*3600L).toString(),
                    "tier",       "APPROVED_VENDOR"
                );
            }

            case "LIST_VENDORS" -> {
                String riskFilter = str(payload, "riskTier");
                yield result(
                    "tenant", tenantId,
                    "filter", riskFilter.isEmpty() ? "ALL" : riskFilter,
                    "vendors", List.of(
                        Map.of("vendorId","VND-001","name","Razorpay","tier","LOW","score",88.0,"status","APPROVED"),
                        Map.of("vendorId","VND-002","name","AWS","tier","LOW","score",95.0,"status","APPROVED"),
                        Map.of("vendorId","VND-003","name","NewVendorXYZ","tier","MEDIUM","score",61.0,"status","UNDER_REVIEW"),
                        Map.of("vendorId","VND-004","name","OldLegacyCo","tier","HIGH","score",38.0,"status","FLAGGED")
                    ),
                    "total", 4
                );
            }

            default -> result(
                "error",   "Unknown action: " + action,
                "allowed", List.of("EVALUATE_VENDOR","GET_RISK_SCORE","FLAG_VENDOR",
                                   "APPROVE_VENDOR","LIST_VENDORS")
            );
        };
    }
}
