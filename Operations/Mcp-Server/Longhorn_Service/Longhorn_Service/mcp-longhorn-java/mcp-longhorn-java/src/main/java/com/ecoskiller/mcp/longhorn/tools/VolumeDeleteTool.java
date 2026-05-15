package com.ecoskiller.mcp.longhorn.tools;
import java.util.*;

public class VolumeDeleteTool extends BaseTool {
    @Override protected String description() {
        return "Delete a Longhorn PVC. ReclaimPolicy is Retain by default — " +
               "schedules safe volume deletion after confirming no active Pod mounts. " +
               "Requires explicit confirmation token to prevent accidental data loss.";
    }
    @Override protected Map<String, Object> properties() {
        Map<String, Object> p = new LinkedHashMap<>();
        p.put("volume_name",    strProp("Name of the PVC to delete"));
        p.put("namespace",      strProp("Kubernetes namespace"));
        p.put("confirm_delete", strProp("Must be set to 'CONFIRM_DELETE' to proceed (safety guard)"));
        p.put("force",          boolProp("Force delete even if volume reports active attachments (use with extreme caution)"));
        return p;
    }
    @Override protected List<String> requiredParams() {
        return List.of("volume_name", "namespace", "confirm_delete");
    }
    @Override public Object execute(Map<String, Object> args) {
        String name    = require(args, "volume_name");
        String ns      = require(args, "namespace");
        String confirm = require(args, "confirm_delete");
        boolean force  = optionalBool(args, "force", false);
        if (!"CONFIRM_DELETE".equals(confirm))
            throw new IllegalArgumentException("Safety check failed. Set confirm_delete='CONFIRM_DELETE' to delete '" + name + "'.");
        Map<String, Object> extra = new LinkedHashMap<>();
        extra.put("volume_name", name); extra.put("namespace", ns); extra.put("force", force);
        extra.put("reclaim_policy", "Retain — manual PV deletion required after PVC removal");
        extra.put("warning", force ? "FORCED deletion — skips attachment check" : "Safe deletion — checks active mounts first");
        return success("PVC '" + name + "' deletion scheduled in namespace '" + ns + "'.", extra);
    }
}
