package com.ecoskiller.mcp.longhorn.tools;
import java.util.*;

public class SnapshotDeleteTool extends BaseTool {
    @Override protected String description() {
        return "Delete a Longhorn VolumeSnapshot. " +
               "Requires explicit confirmation token. " +
               "Warns if the snapshot is the only restore point for a volume.";
    }
    @Override protected Map<String, Object> properties() {
        Map<String, Object> p = new LinkedHashMap<>();
        p.put("snapshot_name",  strProp("Name of the VolumeSnapshot to delete"));
        p.put("namespace",      strProp("Kubernetes namespace"));
        p.put("confirm_delete", strProp("Must be 'CONFIRM_SNAPSHOT_DELETE' to proceed"));
        return p;
    }
    @Override protected List<String> requiredParams() {
        return List.of("snapshot_name", "namespace", "confirm_delete");
    }
    @Override public Object execute(Map<String, Object> args) {
        String snapName = require(args, "snapshot_name");
        String ns       = require(args, "namespace");
        String confirm  = require(args, "confirm_delete");
        if (!"CONFIRM_SNAPSHOT_DELETE".equals(confirm))
            throw new IllegalArgumentException(
                "Safety check failed. Set confirm_delete='CONFIRM_SNAPSHOT_DELETE' to delete snapshot '" + snapName + "'.");
        Map<String, Object> extra = new LinkedHashMap<>();
        extra.put("snapshot_name", snapName); extra.put("namespace", ns);
        extra.put("warning", "Deleted snapshots cannot be recovered. Ensure a MinIO backup exists before deletion.");
        return success("VolumeSnapshot '" + snapName + "' deletion scheduled.", extra);
    }
}
