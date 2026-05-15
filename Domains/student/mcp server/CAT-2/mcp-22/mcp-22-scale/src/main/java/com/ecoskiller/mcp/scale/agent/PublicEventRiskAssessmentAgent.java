package com.ecoskiller.mcp.scale.agent;

import com.ecoskiller.mcp.scale.model.AgentResponse;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.Map;

/**
 * AGENT-04: PUBLIC_EVENT_RISK_ASSESSMENT_AGENT
 * Assesses risk for public competitions, franchise events, and school onboarding events.
 * Evaluates compliance, financial exposure, and operational risk factors.
 */
@Component
public class PublicEventRiskAssessmentAgent {

    @Tool(name = "event_risk_assess",
          description = "Assess the risk profile of a public competition or marketplace event.")
    public AgentResponse assessEventRisk(
            @ToolParam(description = "Event ID") String eventId,
            @ToolParam(description = "Event type: COMPETITION | FRANCHISE_LAUNCH | SCHOOL_ONBOARDING | MARKETPLACE_SALE") String eventType,
            @ToolParam(description = "Expected participant count") int participantCount,
            @ToolParam(description = "Total prize or financial exposure in INR") double financialExposure,
            @ToolParam(description = "Region: Maharashtra | PAN_INDIA | Global") String region) {

        double riskScore = calculateRiskScore(participantCount, financialExposure, region);
        String riskLevel = riskScore > 70 ? "HIGH" : riskScore > 40 ? "MEDIUM" : "LOW";

        return AgentResponse.ok("PUBLIC_EVENT_RISK_ASSESSMENT_AGENT",
                "Risk assessment completed for event " + eventId,
                Map.of(
                        "eventId",           eventId,
                        "eventType",         eventType,
                        "participantCount",  participantCount,
                        "financialExposure", financialExposure,
                        "region",            region,
                        "riskScore",         riskScore,
                        "riskLevel",         riskLevel,
                        "riskFactors", List.of(
                                Map.of("factor","PARTICIPANT_VOLUME",   "score", participantCount > 500 ? 30 : 10),
                                Map.of("factor","FINANCIAL_EXPOSURE",   "score", financialExposure > 500000 ? 25 : 10),
                                Map.of("factor","REGULATORY_COMPLEXITY","score", "Global".equals(region) ? 20 : 8)
                        ),
                        "mitigationRequired", riskLevel.equals("HIGH"),
                        "assessedAt",         Instant.now().toString()
                ));
    }

    @Tool(name = "event_risk_mitigate",
          description = "Generate a risk mitigation plan for a high-risk event.")
    public AgentResponse mitigateRisk(
            @ToolParam(description = "Event ID") String eventId,
            @ToolParam(description = "Risk level: HIGH | MEDIUM") String riskLevel) {

        return AgentResponse.ok("PUBLIC_EVENT_RISK_ASSESSMENT_AGENT",
                "Mitigation plan generated for event " + eventId,
                Map.of(
                        "eventId",  eventId,
                        "riskLevel", riskLevel,
                        "mitigations", List.of(
                                "Escrow 100% of prize pool before event goes live",
                                "Require KYC verification for all participants",
                                "Enable real-time fraud monitoring via AUDIT_VERIFICATION_AGENT",
                                "Assign dedicated compliance officer for event duration"
                        ),
                        "planId",   "MITIG-" + System.currentTimeMillis(),
                        "status",   "PLAN_GENERATED"
                ));
    }

    private double calculateRiskScore(int participants, double exposure, String region) {
        double score = 0;
        if (participants > 500) score += 30;
        else if (participants > 100) score += 15;
        if (exposure > 500000) score += 25;
        else if (exposure > 100000) score += 12;
        if ("Global".equals(region)) score += 20;
        else if ("PAN_INDIA".equals(region)) score += 10;
        else score += 5;
        return score;
    }
}
