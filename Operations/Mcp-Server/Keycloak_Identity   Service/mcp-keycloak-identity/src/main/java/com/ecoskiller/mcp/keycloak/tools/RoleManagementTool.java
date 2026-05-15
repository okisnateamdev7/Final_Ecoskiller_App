package com.ecoskiller.mcp.keycloak.tools;

import org.json.JSONObject;
import org.json.JSONArray;

/**
 * Tool: role_management
 *
 * Manages Keycloak RBAC:
 *   - list_roles:       List all realm roles
 *   - create_role:      Create new role (e.g. hiring-manager, campus-admin)
 *   - assign_role:      Assign role(s) to a user
 *   - remove_role:      Remove role from user
 *   - get_user_roles:   Get all roles for a user
 *   - delete_role:      Delete a realm role
 *
 * Standard Ecoskiller Roles (from spec):
 *   candidate, recruiter, admin, hiring-manager, super-admin,
 *   campus-admin, assessment-taker, assessment-reviewer
 */
public class RoleManagementTool implements McpTool {

    private final KeycloakClient kc = KeycloakClient.getInstance();

    @Override public String getName() { return "role_management"; }

    @Override
    public String getDescription() {
        return "Manage Keycloak RBAC (Role-Based Access Control). "
            + "Create/delete realm roles, assign/remove roles from users, "
            + "list all roles or a user's roles. "
            + "Standard roles: candidate, recruiter, admin, hiring-manager, super-admin, campus-admin.";
    }

    @Override
    public JSONObject getInputSchema() {
        return new JSONObject()
            .put("type", "object")
            .put("properties", new JSONObject()
                .put("action", new JSONObject()
                    .put("type", "string")
                    .put("enum", new JSONArray()
                        .put("list_roles").put("create_role").put("assign_role")
                        .put("remove_role").put("get_user_roles").put("delete_role")))
                .put("role_name", new JSONObject()
                    .put("type", "string")
                    .put("description", "Role name (e.g. candidate, recruiter, admin)"))
                .put("role_description", new JSONObject()
                    .put("type", "string")
                    .put("description", "Human-readable role description"))
                .put("user_id", new JSONObject()
                    .put("type", "string")
                    .put("description", "User UUID for assign/remove/get_user_roles"))
                .put("roles", new JSONObject()
                    .put("type", "array")
                    .put("items", new JSONObject().put("type", "string"))
                    .put("description", "List of role names for bulk assignment"))
            )
            .put("required", new JSONArray().put("action"));
    }

    @Override
    public JSONObject execute(JSONObject args) throws Exception {
        return switch (args.getString("action")) {
            case "list_roles"     -> listRoles();
            case "create_role"    -> createRole(args);
            case "assign_role"    -> assignRole(args);
            case "remove_role"    -> removeRole(args);
            case "get_user_roles" -> getUserRoles(args);
            case "delete_role"    -> deleteRole(args);
            default -> throw new IllegalArgumentException("Unknown action: " + args.getString("action"));
        };
    }

    private JSONObject listRoles() throws Exception {
        JSONObject resp = kc.get(kc.adminRoles());
        return new JSONObject()
            .put("status", "success")
            .put("roles", resp.optJSONArray("roles") != null ? resp.optJSONArray("roles") : new JSONArray());
    }

    private JSONObject createRole(JSONObject args) throws Exception {
        String name = requireString(args, "role_name");
        JSONObject role = new JSONObject().put("name", name);
        if (args.has("role_description"))
            role.put("description", args.getString("role_description"));
        kc.post(kc.adminRoles(), role);
        return new JSONObject().put("status", "success").put("action", "create_role").put("role_name", name);
    }

    private JSONObject assignRole(JSONObject args) throws Exception {
        String userId = requireString(args, "user_id");
        JSONArray roleNames = args.has("roles") ? args.getJSONArray("roles")
            : new JSONArray().put(requireString(args, "role_name"));

        JSONArray roleObjects = new JSONArray();
        for (int i = 0; i < roleNames.length(); i++) {
            String rn = roleNames.getString(i);
            JSONObject roleDetail = kc.get(kc.adminRole(rn));
            roleObjects.put(new JSONObject()
                .put("id", roleDetail.getString("id"))
                .put("name", roleDetail.getString("name"))
                .put("composite", false));
        }

        kc.post(kc.adminUser(userId) + "/role-mappings/realm",
            new JSONObject().put("roles", roleObjects));

        return new JSONObject()
            .put("status", "success")
            .put("action", "assign_role")
            .put("user_id", userId)
            .put("roles_assigned", roleNames);
    }

    private JSONObject removeRole(JSONObject args) throws Exception {
        String userId = requireString(args, "user_id");
        String roleName = requireString(args, "role_name");
        JSONObject roleDetail = kc.get(kc.adminRole(roleName));
        JSONArray roleObjects = new JSONArray().put(new JSONObject()
            .put("id", roleDetail.getString("id"))
            .put("name", roleDetail.getString("name"))
            .put("composite", false));

        kc.delete(kc.adminUser(userId) + "/role-mappings/realm?roles=" + roleName);
        return new JSONObject()
            .put("status", "success").put("action", "remove_role")
            .put("user_id", userId).put("role_name", roleName);
    }

    private JSONObject getUserRoles(JSONObject args) throws Exception {
        String userId = requireString(args, "user_id");
        JSONObject resp = kc.get(kc.adminUser(userId) + "/role-mappings/realm");
        return new JSONObject()
            .put("status", "success")
            .put("user_id", userId)
            .put("roles", resp.optJSONArray("mappings") != null
                ? resp.optJSONArray("mappings") : new JSONArray());
    }

    private JSONObject deleteRole(JSONObject args) throws Exception {
        String name = requireString(args, "role_name");
        kc.delete(kc.adminRole(name));
        return new JSONObject().put("status", "success").put("action", "delete_role").put("role_name", name);
    }

    private static String requireString(JSONObject args, String key) {
        if (!args.has(key) || args.getString(key).isBlank())
            throw new IllegalArgumentException("Missing required argument: " + key);
        return args.getString(key);
    }
}
