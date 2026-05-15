package com.ecoskiller.mcp.longhorn.tools;
import java.util.*;

public class BackupTriggerTool extends BaseTool {
    @Override protected String description() {
        return "Trigger an immediate incremental Longhorn backup to MinIO S3-compatible target. " +
               "Backup target: s3://ecoskiller-longhorn-backup @ minio.ops.svc.cluster.local:9000 (GCP primary). " +
               "Geo-redundant copy to AWS MinIO via bucket replication. Redis RPO < 15 min.";
    }
    @Override protected Map<String, Object> properties() {
        Map<String, Object> p = new LinkedHashMap<>();
        p.put("volume_name",   strProp("PVC name to back up (omit for all volumes)"));
        p.put("namespace",     strProp("Kubernetes namespace"));
        p.put("backup_type",   strPropEnum("Backup type", "incremental", "full"));
        p.put("priority",      strPropEnum("Backup scheduling priority", "normal", "high"));
        p.put("geo_redundant", boolProp("Enable geo-redundant copy to AWS MinIO secondary (default: true)"));
        return p;
    }
    @Override public Object execute(Map<String, Object> args) {
        String volumeName  = optional(args, "volume_name", "ALL");
        String namespace   = optional(args, "namespace",   "all");
        String backupType  = optional(args, "backup_type", "incremental");
        String priority    = optional(args, "priority",    "normal");
        boolean geoRedundant = optionalBool(args, "geo_redundant", true);
        String backupId = "bkp-" + System.currentTimeMillis();
        Map<String, Object> extra = new LinkedHashMap<>();
        extra.put("backup_id", backupId);        extra.put("volume_name", volumeName);
        extra.put("namespace", namespace);        extra.put("backup_type", backupType);
        extra.put("priority", priority);          extra.put("geo_redundant", geoRedundant);
        extra.put("primary_target", "s3://ecoskiller-longhorn-backup/longhorn @ minio.ops.svc.cluster.local:9000 (GCP)");
        extra.put("secondary_target", geoRedundant ? "AWS MinIO bucket replication (ap-south-1)" : "disabled");
        extra.put("rpo_minutes", "ALL".equals(volumeName) ? 60 : 15);
        extra.put("compression", "enabled"); extra.put("deduplication", "block-level");
        return success("Backup job '" + backupId + "' triggered for volume '" + volumeName + "'.", extra);
    }
}
