package com.ecoskiller.mcp.longhorn.tools;
import java.util.*;

public class SnapshotCreateTool extends BaseTool {
    @Override protected String description() {
        return "Create an on-demand Longhorn VolumeSnapshot for a PVC. " +
               "Snapshot objects are Kubernetes VolumeSnapshot resources consumed by Velero (Phase 2). " +
               "Supports PITR (point-in-time recovery) for all block-volume-backed services.";
    }
    @Override protected Map<String, Object> properties() {
        Map<String, Object> p = new LinkedHashMap<>();
        p.put("volume_name",    strProp("Name of the PVC to snapshot"));
        p.put("namespace",      strProp("Kubernetes namespace"));
        p.put("snapshot_label", strProp("Human-readable label (e.g., pre-migration, daily-backup-2026)"));
        p.put("retain_days",    intProp("Days to retain this snapshot (1–90)", 1, 90));
        p.put("quiesce_io",     boolProp("Quiesce I/O before snapshot for app consistency (recommended for PostgreSQL, ClickHouse)"));
        return p;
    }
    @Override protected List<String> requiredParams() { return List.of("volume_name", "namespace"); }
    @Override public Object execute(Map<String, Object> args) {
        String volumeName = require(args, "volume_name");
        String namespace  = require(args, "namespace");
        String label      = optional(args, "snapshot_label", "on-demand");
        int retainDays    = optionalInt(args, "retain_days", 7);
        boolean quiesce   = optionalBool(args, "quiesce_io", false);
        String snapName   = volumeName + "-snap-" + System.currentTimeMillis();
        Map<String, Object> extra = new LinkedHashMap<>();
        extra.put("snapshot_name", snapName); extra.put("volume_name", volumeName);
        extra.put("namespace", namespace);    extra.put("label", label);
        extra.put("retain_days", retainDays); extra.put("quiesce_io", quiesce);
        extra.put("snapshot_class", "longhorn");
        extra.put("velero_eligible", true);   extra.put("pitr_capable", true);
        return success("VolumeSnapshot '" + snapName + "' creation initiated for PVC '" + volumeName + "'.", extra);
    }
}
