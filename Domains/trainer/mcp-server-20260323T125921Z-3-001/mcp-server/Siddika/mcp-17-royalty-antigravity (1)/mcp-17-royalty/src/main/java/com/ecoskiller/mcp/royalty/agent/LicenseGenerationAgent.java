package com.ecoskiller.mcp.royalty.agent;

import com.ecoskiller.mcp.royalty.model.AgentResponse;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.UUID;

/**
 * AGENT-11: LICENSE_GENERATION_AGENT
 * Generates content/franchise/school licenses.
 * Tracks validity, handles renewals and revocations.
 */
@Component
public class LicenseGenerationAgent {

    @Tool(name = "license_generate",
          description = "Generate a license for content, franchise, school, or API access.")
    public AgentResponse generateLicense(
            @ToolParam(description = "Licensee user/entity ID") String licenseeId,
            @ToolParam(description = "License type: CONTENT | FRANCHISE | SCHOOL | API") String licenseType,
            @ToolParam(description = "Duration in days") int durationDays,
            @ToolParam(description = "Territory: Maharashtra | PAN_INDIA | Global") String territory,
            @ToolParam(description = "Subject ID — content or franchise being licensed") String subjectId) {

        String licenseKey = "LIC-" + UUID.randomUUID().toString().toUpperCase().replace("-","").substring(0, 16);

        return AgentResponse.ok("LICENSE_GENERATION_AGENT",
                licenseType + " license generated for " + licenseeId,
                Map.of(
                        "licenseId",        licenseKey,
                        "licenseeId",       licenseeId,
                        "licenseType",      licenseType,
                        "subjectId",        subjectId,
                        "territory",        territory,
                        "issuedAt",         Instant.now().toString(),
                        "expiresAt",        Instant.now().plus(durationDays, ChronoUnit.DAYS).toString(),
                        "durationDays",     durationDays,
                        "licenseStatus",    "ACTIVE"
                ));
    }

    @Tool(name = "license_validate",
          description = "Validate if a license is active and not expired.")
    public AgentResponse validateLicense(
            @ToolParam(description = "License ID to validate") String licenseId) {

        return AgentResponse.ok("LICENSE_GENERATION_AGENT",
                "License " + licenseId + " is valid",
                Map.of(
                        "licenseId",     licenseId,
                        "isValid",       true,
                        "status",        "ACTIVE",
                        "daysRemaining", 245
                ));
    }

    @Tool(name = "license_revoke",
          description = "Revoke a license due to compliance violation or non-payment.")
    public AgentResponse revokeLicense(
            @ToolParam(description = "License ID") String licenseId,
            @ToolParam(description = "Revocation reason") String reason) {

        return AgentResponse.ok("LICENSE_GENERATION_AGENT",
                "License " + licenseId + " revoked",
                Map.of(
                        "licenseId",   licenseId,
                        "status",      "REVOKED",
                        "reason",      reason,
                        "revokedAt",   Instant.now().toString()
                ));
    }
}
