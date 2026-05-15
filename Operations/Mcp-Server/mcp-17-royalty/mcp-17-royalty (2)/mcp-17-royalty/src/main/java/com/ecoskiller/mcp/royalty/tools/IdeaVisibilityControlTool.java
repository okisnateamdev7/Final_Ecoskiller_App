package com.ecoskiller.mcp.royalty.tools;
import org.json.JSONObject;

/** IDEA_VISIBILITY_CONTROL_AGENT — Tool: idea_visibility_control */
public class IdeaVisibilityControlTool extends BaseTool {
    @Override public String getName() { return "idea_visibility_control"; }
    @Override public JSONObject getSchema() {
        JSONObject p = new JSONObject()
                .put("action",     str("set_visibility|get_visibility|restrict_territory|unlock_for_licensee"))
                .put("idea_id",    str("Idea unique ID"))
                .put("creator_id", str("Creator unique ID"))
                .put("visibility", str("PUBLIC|PRIVATE|LICENSEE_ONLY|TERRITORY_RESTRICTED"))
                .put("territory",  str("Territory restriction"))
                .put("licensee_id",str("Specific licensee to grant access"));
        return schema(getName(),
                "IDEA_VISIBILITY_CONTROL_AGENT — Control marketplace visibility of ideas. " +
                "Restrict by territory, grant exclusive preview to licensees, manage public/private state.",
                p, "action", "idea_id", "creator_id");
    }
    @Override public JSONObject execute(JSONObject a) throws Exception {
        String ideaId = a.getString("idea_id");
        return switch (a.getString("action")) {
            case "set_visibility" -> json(new JSONObject()
                    .put("idea_id",    ideaId)
                    .put("visibility", a.optString("visibility","PUBLIC"))
                    .put("updated_at", now()));
            case "get_visibility" -> json(new JSONObject()
                    .put("idea_id",    ideaId)
                    .put("visibility", "PUBLIC")
                    .put("territories","All")
                    .put("timestamp",  now()));
            case "restrict_territory" -> json(new JSONObject()
                    .put("idea_id",   ideaId)
                    .put("territory", a.optString("territory","EU"))
                    .put("status",    "RESTRICTED")
                    .put("reason",    "Exclusive license active in territory")
                    .put("timestamp", now()));
            case "unlock_for_licensee" -> json(new JSONObject()
                    .put("idea_id",    ideaId)
                    .put("licensee_id",a.optString("licensee_id",""))
                    .put("access",     "GRANTED")
                    .put("timestamp",  now()));
            default -> text("Unknown action: " + a.getString("action"));
        };
    }
}
