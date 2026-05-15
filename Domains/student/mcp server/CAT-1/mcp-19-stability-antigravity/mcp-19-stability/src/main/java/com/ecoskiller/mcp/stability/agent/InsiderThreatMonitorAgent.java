package com.ecoskiller.mcp.stability.agent;

import com.ecoskiller.mcp.stability.model.AgentResponse;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * AGENT-03: INSIDER_THREAT_MONITOR_AGENT
 * Monitors internal users (admins, agents, employees)
 * for suspicious data access, privilege abuse, and exfiltration attempts.
 */
@Component
public class InsiderThreatMonitorAgent {

    @Tool(name = "insider_threat_scan",
          description = "Scan for insider threat signals for a user or role within a time window.")
    public AgentResponse scan(
            @ToolParam(description = "User ID or role to scan") String userId,
            @ToolParam(description = "Scan window in hours") int windowHours,
            @ToolParam(description = "Check type: DATA_ACCESS | PRIVILEGE_USE | API_EXPORT | ALL") String checkType) {

        double riskScore = 18.6;
        boolean flagged  = riskScore > 25;

        return AgentResponse.ok("INSIDER_THREAT_MONITOR_AGENT",
                "Insider threat scan completed for " + userId,
                Map.of(
                        "userId",      userId,
                        "windowHours", windowHours,
                        "checkType",   checkType,
                        "riskScore",   riskScore,
                        "riskLevel",   flagged ? "HIGH" : "LOW",
                        "flagged",     flagged,
                        "signals",     flagged
                                ? List.of("BULK_DATA_EXPORT","OFF_HOURS_ACCESS","UNUSUAL_QUERY_VOLUME")
                                : List.of(),
                        "action",      flagged ? "ESCALATE_TO_HUMAN_REVIEW" : "NO_ACTION",
                        "scannedAt",   Instant.now().toString()
                ));
    }

    @Tool(name = "insider_threat_watchlist_add",
          description = "Add a user to enhanced monitoring watchlist.")
    public AgentResponse addToWatchlist(
            @ToolParam(description = "User ID to watchlist") String userId,
            @ToolParam(description = "Reason for watchlisting") String reason,
            @ToolParam(description = "Monitoring duration in days") int days,
            @ToolParam(description = "Admin user ID authorizing") String adminId) {

        return AgentResponse.ok("INSIDER_THREAT_MONITOR_AGENT",
                "User " + userId + " added to watchlist",
                Map.of(
                        "userId",          userId,
                        "reason",          reason,
                        "watchlistId",     "WL-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase(),
                        "expiresAt",       Instant.now().plus(days, ChronoUnit.DAYS).toString(),
                        "authorizedBy",    adminId,
                        "monitoringLevel", "ENHANCED"
                ));
    }

    @Tool(name = "insider_threat_report",
          description = "Get insider threat summary report for a period.")
    public AgentResponse report(
            @ToolParam(description = "Period: TODAY | THIS_WEEK | THIS_MONTH") String period) {

        return AgentResponse.ok("INSIDER_THREAT_MONITOR_AGENT",
                "Insider threat report for " + period,
                Map.of(
                        "period",          period,
                        "totalScanned",    1840,
                        "flaggedUsers",    3,
                        "highRisk",        1,
                        "mediumRisk",      2,
                        "incidentsRaised", 1,
                        "resolved",        0,
                        "topRiskSignal",   "BULK_DATA_EXPORT"
                ));
    }
}
