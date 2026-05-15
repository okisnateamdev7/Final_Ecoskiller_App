package com.ecoskiller.mcp.interview.model;

/**
 * Extracted security context from a validated Keycloak JWT.
 */
public record SecurityContext(
    String userId,
    String tenantId,
    String role,          // RECRUITER | INTERVIEWER | CANDIDATE | ADMIN
    String recruiterId,
    String candidateId,
    String email
) {}
