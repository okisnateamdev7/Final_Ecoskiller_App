package com.ecoskiller.mcp.longhorn.tools;

import java.util.*;

/**
 * AGENT: VOLUME_PROVISION_AGENT
 *
 * Dynamically provisions a new PersistentVolume (PV) via Longhorn's
 * driver.longhorn.io CSI StorageClass on the Ecoskiller k3s clusters.
 *
 * Supports GCP (asia-south1) and AWS (ap-south-1) node pools.
 * Default replica count: 3 (cross-node, matching Ecoskiller production config).
 */
public class VolumeProvisionTool extends BaseTool {

    private static final Set<String> ALLOWED_CONSUMERS = Set.of(
            "postgresql", "redis", "clickhouse", "opensearch", "kafka",
            "minio", "grafana-tempo", "prometheus", "grafana", "loki", "custom"
    );

    private static final Set<String> ALLOWED_CLOUDS = Set.of("gcp", "aws", "both");

    @Override
    protected String description() {
        return "Provision a new Longhorn PersistentVolume (PV) for a stateful Ecoskiller service. " +
               "Creates a PVC with driver.longhorn.io StorageClass on GCP (asia-south1) or AWS (ap-south-1) k3s cluster. " +
               "Default 3-replica cross-node replication. Supports LUKS encryption flag for PII workloads (OpenSearch, ClickHouse).";
    }

    @Override
    protected Map<String, Object> properties() {
        Map<String, Object> p = new LinkedHashMap<>();
        p.put("volume_name",    strProp("Unique name for the PersistentVolumeClaim (e.g., redis-data-pvc, opensearch-index-pvc)"));
        p.put("namespace",      strProp("Kubernetes namespace (e.g., default, ops, longhorn-system)"));
        p.put("storage_gi",     intProp("Storage size in GiB (1–1000). Longhorn max per node: 100 GiB SSD on Core Worker Pool", 1, 1000));
        p.put("replica_count",  intProp("Number of cross-node replicas (1–5, default: 3)", 1, 5));
        p.put("consumer",       strPropEnum("Service consuming this volume", ALLOWED_CONSUMERS.toArray(new String[0])));
        p.put("cloud_target",   strPropEnum("Target cloud cluster", "gcp", "aws", "both"));
        p.put("encrypted",      boolProp("Apply LUKS encryption (required for OpenSearch PII data, ClickHouse uses AES-128-CTR at app layer)"));
        p.put("access_mode",    strPropEnum("Kubernetes PVC access mode", "ReadWriteOnce", "ReadWriteMany"));
        p.put("fs_type",        strPropEnum("Filesystem type for the block device", "ext4", "xfs"));
        return p;
    }

    @Override
    protected List<String> requiredParams() {
        return List.of("volume_name", "namespace", "storage_gi", "consumer");
    }

    @Override
    public Object execute(Map<String, Object> args) {
        String volumeName   = require(args, "volume_name");
        String namespace    = require(args, "namespace");
        int    storageGi    = optionalInt(args, "storage_gi", 20);
        int    replicas     = optionalInt(args, "replica_count", 3);
        String consumer     = require(args, "consumer");
        String cloud        = optional(args, "cloud_target", "gcp");
        boolean encrypted   = optionalBool(args, "encrypted", false);
        String accessMode   = optional(args, "access_mode", "ReadWriteOnce");
        String fsType       = optional(args, "fs_type", "ext4");

        // Business rule validations
        if (!ALLOWED_CONSUMERS.contains(consumer)) {
            throw new IllegalArgumentException("Unknown consumer: " + consumer);
        }
        if (!ALLOWED_CLOUDS.contains(cloud)) {
            throw new IllegalArgumentException("Unknown cloud target: " + cloud);
        }
        if (storageGi > 100 && !cloud.equals("both")) {
            // Single-node SSD cap is 100 GiB per Ecoskiller spec
            throw new IllegalArgumentException("Single-node SSD cap is 100 GiB. Use cloud_target=both for larger volumes.");
        }
        if ("opensearch".equals(consumer) && !encrypted) {
            throw new IllegalArgumentException("OpenSearch PVCs must have encrypted=true (GDPR compliance requirement).");
        }

        Map<String, Object> extra = new LinkedHashMap<>();
        extra.put("volume_name",   volumeName);
        extra.put("namespace",     namespace);
        extra.put("storage_gi",    storageGi);
        extra.put("replica_count", replicas);
        extra.put("consumer",      consumer);
        extra.put("cloud_target",  cloud);
        extra.put("encrypted",     encrypted);
        extra.put("access_mode",   accessMode);
        extra.put("fs_type",       fsType);
        extra.put("storage_class", "longhorn");
        extra.put("provisioner",   "driver.longhorn.io");
        extra.put("pvc_yaml_hint", buildPvcYaml(volumeName, namespace, storageGi, accessMode, encrypted));

        return success("PersistentVolumeClaim '" + volumeName + "' provisioning initiated on " + cloud.toUpperCase() + " cluster.", extra);
    }

    private String buildPvcYaml(String name, String ns, int gi, String mode, boolean enc) {
        return "apiVersion: v1\\nkind: PersistentVolumeClaim\\n" +
               "metadata:\\n  name: " + name + "\\n  namespace: " + ns +
               (enc ? "\\n  annotations:\\n    longhorn.io/encrypted: \\\"true\\\"" : "") +
               "\\nspec:\\n  accessModes: [" + mode + "]" +
               "\\n  storageClassName: longhorn" +
               "\\n  resources:\\n    requests:\\n      storage: " + gi + "Gi";
    }
}
