package com.ecoskiller.mcp.tools;

import com.ecoskiller.mcp.models.KafkaEventPublisher;
import com.ecoskiller.mcp.protocol.JsonUtil;
import com.ecoskiller.mcp.security.AuthSecurityConfig;

import java.time.Instant;
import java.util.*;

/** Tools: get_oauth_authorization_code, exchange_oauth_code */
class OAuthTools {

    public String getAuthorizationCode(Map<String, Object> args) {
        String accessToken = AuthSecurityConfig.requireString(args, "access_token");
        String tenantId    = AuthSecurityConfig.requireString(args, "tenant_id");
        String clientId    = AuthSecurityConfig.requireString(args, "client_id");
        String redirectUri = AuthSecurityConfig.requireString(args, "redirect_uri");
        String scope       = AuthSecurityConfig.sanitise(AuthSecurityConfig.getString(args, "scope"), "scope");
        String state       = AuthSecurityConfig.sanitise(AuthSecurityConfig.getString(args, "state"), "state");

        Map<String, Object> claims = AuthSecurityConfig.validateToken(accessToken);
        String userId = (String) claims.getOrDefault("sub", "");

        // Validate redirect_uri (must be https in prod)
        if (!redirectUri.startsWith("http"))
            throw new IllegalArgumentException("redirect_uri must be a valid HTTP/HTTPS URL");

        // Scope validation
        if (scope == null) scope = "openid profile";
        Set<String> allowedScopes = Set.of("openid", "profile", "email",
                "view-assessments", "submit-responses", "grade-submissions");
        for (String s : scope.split(" ")) {
            if (!allowedScopes.contains(s.trim()))
                throw new IllegalArgumentException("Invalid scope: " + s + ". Allowed: " + allowedScopes);
        }

        String code = AuthSecurityConfig.generateAuthCode(userId, tenantId, clientId, scope);

        AuthSecurityConfig.audit("OAUTH_CODE_ISSUED", userId, tenantId, null,
                "client=" + clientId + " scope=" + scope, true);

        return resp("OK", "Authorization code issued (10-minute TTL, single-use)", Map.of(
                "code",         code,
                "state",        state != null ? state : "",
                "redirect_uri", redirectUri + "?code=" + code + (state != null ? "&state=" + state : ""),
                "expires_in",   600,
                "note",         "Exchange via exchange_oauth_code within 10 minutes"));
    }

    public String exchangeCode(Map<String, Object> args) {
        String code         = AuthSecurityConfig.requireString(args, "code");
        String clientId     = AuthSecurityConfig.requireString(args, "client_id");
        String clientSecret = AuthSecurityConfig.getString(args, "client_secret");
        String tenantId     = AuthSecurityConfig.requireString(args, "tenant_id");

        // Exchange auth code for tokens
        Map<String, Object> tokens = AuthSecurityConfig.exchangeAuthCode(code, clientId);

        AuthSecurityConfig.audit("OAUTH_CODE_EXCHANGED", (String) tokens.getOrDefault("user_id",""),
                tenantId, null, "client=" + clientId, true);

        return resp("OK", "Authorization code exchanged for tokens", tokens);
    }

    private String resp(String status, String message, Object data) {
        Map<String, Object> r = new LinkedHashMap<>();
        r.put("status", status); r.put("message", message); r.put("data", data);
        r.put("service", "auth-service");
        return JsonUtil.toJson(r);
    }
}

/** Tools: get_user_session, list_user_sessions, invalidate_all_sessions */
class SessionTools {

    private final KafkaEventPublisher kafka = KafkaEventPublisher.getInstance();

    public String getSession(Map<String, Object> args) {
        String accessToken = AuthSecurityConfig.requireString(args, "access_token");
        String tenantId    = AuthSecurityConfig.requireString(args, "tenant_id");
        String sessionId   = AuthSecurityConfig.requireString(args, "session_id");

        Map<String, Object> claims = AuthSecurityConfig.validateToken(accessToken);
        String callerId  = (String) claims.getOrDefault("sub", "");
        boolean isAdmin  = AuthSecurityConfig.hasRole(claims, AuthSecurityConfig.ROLE_ADMIN)
                        || AuthSecurityConfig.hasRole(claims, AuthSecurityConfig.ROLE_SUPER_ADMIN);

        Optional<Map<String, Object>> sessionOpt = AuthSecurityConfig.getSession(sessionId);
        if (sessionOpt.isEmpty()) throw new IllegalArgumentException("Session not found: " + sessionId);

        Map<String, Object> session = sessionOpt.get();
        String ownerId = (String) session.get("user_id");

        if (!isAdmin && !callerId.equals(ownerId))
            throw new SecurityException("Cannot view another user's session without ADMIN role");

        return resp("OK", "Session retrieved", session);
    }

    public String listSessions(Map<String, Object> args) {
        String accessToken   = AuthSecurityConfig.requireString(args, "access_token");
        String tenantId      = AuthSecurityConfig.requireString(args, "tenant_id");
        String targetUserId  = AuthSecurityConfig.getString(args, "target_user_id");

        Map<String, Object> claims = AuthSecurityConfig.validateToken(accessToken);
        String callerId = (String) claims.getOrDefault("sub", "");
        boolean isAdmin = AuthSecurityConfig.hasRole(claims, AuthSecurityConfig.ROLE_ADMIN)
                       || AuthSecurityConfig.hasRole(claims, AuthSecurityConfig.ROLE_SUPER_ADMIN);

        String lookupId = (targetUserId != null && !targetUserId.isBlank()) ? targetUserId : callerId;
        if (!isAdmin && !callerId.equals(lookupId))
            throw new SecurityException("Cannot list another user's sessions without ADMIN role");

        List<Map<String, Object>> sessions = AuthSecurityConfig.getUserSessions(lookupId);

        return resp("OK", sessions.size() + " active session(s) found", Map.of(
                "user_id",      lookupId,
                "session_count",sessions.size(),
                "sessions",     sessions));
    }

    public String invalidateAllSessions(Map<String, Object> args) {
        String accessToken  = AuthSecurityConfig.requireString(args, "access_token");
        String tenantId     = AuthSecurityConfig.requireString(args, "tenant_id");
        String targetUserId = AuthSecurityConfig.getString(args, "target_user_id");
        String reason       = AuthSecurityConfig.sanitise(
                AuthSecurityConfig.getString(args, "reason"), "reason");

        Map<String, Object> claims = AuthSecurityConfig.validateToken(accessToken);
        String callerId = (String) claims.getOrDefault("sub", "");
        boolean isAdmin = AuthSecurityConfig.hasRole(claims, AuthSecurityConfig.ROLE_ADMIN)
                       || AuthSecurityConfig.hasRole(claims, AuthSecurityConfig.ROLE_SUPER_ADMIN);

        String targetId = (targetUserId != null && !targetUserId.isBlank()) ? targetUserId : callerId;
        if (!isAdmin && !callerId.equals(targetId))
            throw new SecurityException("Cannot invalidate another user's sessions without ADMIN role");

        int count = AuthSecurityConfig.invalidateAllUserSessions(targetId);
        String invalidationReason = reason != null ? reason : "USER_REQUEST";

        AuthSecurityConfig.audit("ALL_SESSIONS_INVALIDATED", targetId, tenantId, null,
                "by=" + callerId + " count=" + count + " reason=" + invalidationReason, true);
        kafka.publish("auth.user_logout", Map.of("user_id", targetId, "tenant_id", tenantId,
                "reason", invalidationReason + "_ALL_DEVICES"));

        return resp("OK", count + " session(s) invalidated across all devices", Map.of(
                "target_user_id",      targetId,
                "invalidated_by",      callerId,
                "sessions_invalidated",count,
                "reason",              invalidationReason));
    }

    private String resp(String status, String message, Object data) {
        Map<String, Object> r = new LinkedHashMap<>();
        r.put("status", status); r.put("message", message); r.put("data", data);
        r.put("service", "auth-service");
        return JsonUtil.toJson(r);
    }
}

/** Tools: check_permission, get_security_audit_log */
class RbacAuditTools {

    // Ecoskiller RBAC matrix: role → resource → allowed actions
    private static final Map<String, Map<String, Set<String>>> RBAC_MATRIX;
    static {
        RBAC_MATRIX = new LinkedHashMap<>();
        RBAC_MATRIX.put("CANDIDATE", Map.of(
                "assessment", Set.of("read"),
                "application",Set.of("read","write"),
                "candidate",  Set.of("read"),
                "job",        Set.of("read")));
        RBAC_MATRIX.put("RECRUITER", Map.of(
                "assessment", Set.of("read","grade"),
                "application",Set.of("read","write","delete"),
                "candidate",  Set.of("read"),
                "job",        Set.of("read","write","delete")));
        RBAC_MATRIX.put("ADMIN", Map.of(
                "assessment", Set.of("read","write","grade","delete","admin"),
                "application",Set.of("read","write","grade","delete","admin"),
                "candidate",  Set.of("read","write","delete","admin"),
                "job",        Set.of("read","write","delete","admin")));
        RBAC_MATRIX.put("SUPER_ADMIN", Map.of(
                "assessment", Set.of("read","write","grade","delete","admin"),
                "application",Set.of("read","write","grade","delete","admin"),
                "candidate",  Set.of("read","write","delete","admin"),
                "job",        Set.of("read","write","delete","admin")));
    }

    public String checkPermission(Map<String, Object> args) {
        String accessToken = AuthSecurityConfig.requireString(args, "access_token");
        String tenantId    = AuthSecurityConfig.requireString(args, "tenant_id");
        String resource    = AuthSecurityConfig.requireString(args, "resource").toLowerCase();
        String action      = AuthSecurityConfig.requireString(args, "action").toLowerCase();

        Map<String, Object> claims = AuthSecurityConfig.validateToken(accessToken);
        String userId = (String) claims.getOrDefault("sub", "");

        // Tenant isolation
        String tokenTenant = (String) claims.get("tenant_id");
        boolean isSuperAdmin = AuthSecurityConfig.hasRole(claims, "SUPER_ADMIN");
        if (!isSuperAdmin && tokenTenant != null && !tenantId.equals(tokenTenant))
            throw new SecurityException("Tenant mismatch");

        // Evaluate RBAC
        List<?> roles = (List<?>) claims.getOrDefault("roles", List.of());
        boolean permitted = false;
        String grantingRole = null;

        for (Object roleObj : roles) {
            String role = roleObj.toString().toUpperCase();
            Map<String, Set<String>> resourceMap = RBAC_MATRIX.get(role);
            if (resourceMap != null) {
                Set<String> allowedActions = resourceMap.get(resource);
                if (allowedActions != null && allowedActions.contains(action)) {
                    permitted = true; grantingRole = role; break;
                }
            }
        }

        Map<String, Object> data = new LinkedHashMap<>();
        data.put("permitted",     permitted);
        data.put("user_id",       userId);
        data.put("resource",      resource);
        data.put("action",        action);
        data.put("granting_role", grantingRole);
        data.put("user_roles",    roles);
        data.put("tenant_id",     tenantId);
        if (!permitted) {
            data.put("reason", "No role in " + roles + " permits action '" + action + "' on resource '" + resource + "'");
        }

        return resp(permitted ? "PERMITTED" : "DENIED",
                permitted ? "Access permitted" : "Access denied", data);
    }

    public String getAuditLog(Map<String, Object> args) {
        String accessToken   = AuthSecurityConfig.requireString(args, "access_token");
        String tenantId      = AuthSecurityConfig.requireString(args, "tenant_id");
        String userIdFilter  = AuthSecurityConfig.getString(args, "user_id_filter");
        String eventType     = AuthSecurityConfig.getString(args, "event_type");
        int limit            = Math.min(JsonUtil.getInt(args, "limit", 100), 500);

        Map<String, Object> claims = AuthSecurityConfig.validateToken(accessToken);
        AuthSecurityConfig.requireRole(claims, AuthSecurityConfig.ROLE_ADMIN,
                AuthSecurityConfig.ROLE_SUPER_ADMIN);

        List<Map<String, Object>> log = AuthSecurityConfig.getAuditLog(tenantId, userIdFilter, limit);

        // Filter by event type if specified
        if (eventType != null && !eventType.isBlank()) {
            final String ft = eventType.toUpperCase();
            log = log.stream()
                    .filter(e -> ft.equals(String.valueOf(e.get("event_type")).toUpperCase()))
                    .collect(java.util.stream.Collectors.toList());
        }

        Map<String, Object> resultData = new LinkedHashMap<>();
        resultData.put("total",          log.size());
        resultData.put("limit",          limit);
        resultData.put("user_filter",    userIdFilter != null ? userIdFilter : "none");
        resultData.put("event_filter",   eventType    != null ? eventType    : "none");
        resultData.put("entries",        log);
        resultData.put("compliance_note","Immutable audit log per SOC 2 Type II / GDPR Article 30. Retained 3 years.");
        return resp("OK", log.size() + " audit entries retrieved", resultData);
    }

    private String resp(String status, String message, Object data) {
        Map<String, Object> r = new LinkedHashMap<>();
        r.put("status", status); r.put("message", message); r.put("data", data);
        r.put("service", "auth-service");
        return JsonUtil.toJson(r);
    }
}
