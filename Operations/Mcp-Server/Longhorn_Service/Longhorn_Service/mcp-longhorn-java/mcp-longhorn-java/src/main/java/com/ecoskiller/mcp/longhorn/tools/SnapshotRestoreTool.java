package com.ecoskiller.mcp.longhorn.tools;
import java.util.*;

public class SnapshotRestoreTool extends BaseTool {
    @Override protected String description() {
        return "Restore a PVC from a Longhorn VolumeSnapshot (PITR). " +
               "Creates a new PVC from the snapshot — does NOT overwrite the original. " +
               "Used for cross-cloud disaster recovery and data corruption recovery.";
    }
    @Override protected Map<String, Object> properties() {
        Map<String, Object> p = new LinkedHashMap<>();
        p.put("snapshot_name",      strProp("Name of the VolumeSnapshot to restore from"));
        p.put("namespace",          strProp("Kubernetes namespace of the snapshot"));
        p.put("target_volume_name", strProp("Name of the new PVC to create from the snapshot"));
        p.put("target_namespace",   strProp("Target namespace for restored PVC (can differ from source)"));
        p.put("target_cloud",       strPropEnum("Cloud cluster for restored volume", "gcp", "aws"));
        return p;
    }
    @Override protected List<String> requiredParams() {
        return List.of("snapshot_name", "namespace", "target_volume_name");
    }
    @Override public Object execute(Map<String, Object> args) {
        String snapName   = require(args, "snapshot_name");
        String ns         = require(args, "namespace");
        String targetVol  = require(args, "target_volume_name");
        String targetNs   = optional(args, "target_namespace", ns);
        String targetCloud= optional(args, "target_cloud", "gcp");
        Map<String, Object> extra = new LinkedHashMap<>();
        extra.put("snapshot_name", snapName);     extra.put("source_namespace", ns);
        extra.put("target_volume_name", targetVol); extra.put("target_namespace", targetNs);
        extra.put("target_cloud", targetCloud);   extra.put("restore_type", "new_pvc_from_snapshot");
        extra.put("original_preserved", true);
        extra.put("warning", "Restored PVC must be manually bound to a Pod/StatefulSet.");
        return success("Restore from snapshot '" + snapName + "' to new PVC '" + targetVol + "' initiated.", extra);
    }
}
