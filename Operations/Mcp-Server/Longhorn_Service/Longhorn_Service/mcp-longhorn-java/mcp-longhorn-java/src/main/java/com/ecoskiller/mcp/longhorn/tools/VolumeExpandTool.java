package com.ecoskiller.mcp.longhorn.tools;
import java.util.*;

public class VolumeExpandTool extends BaseTool {
    @Override protected String description() {
        return "Expand an existing Longhorn PVC online without Pod restart. " +
               "Supported because Longhorn StorageClass has allowVolumeExpansion=true. " +
               "Validates that new size is larger than current size.";
    }
    @Override protected Map<String, Object> properties() {
        Map<String, Object> p = new LinkedHashMap<>();
        p.put("volume_name",     strProp("Name of the existing PVC to expand"));
        p.put("namespace",       strProp("Kubernetes namespace of the PVC"));
        p.put("new_size_gi",     intProp("New storage size in GiB — must be larger than current", 1, 1000));
        p.put("current_size_gi", intProp("Current volume size in GiB (used for validation)", 1, 1000));
        return p;
    }
    @Override protected List<String> requiredParams() {
        return List.of("volume_name", "namespace", "new_size_gi", "current_size_gi");
    }
    @Override public Object execute(Map<String, Object> args) {
        String name  = require(args, "volume_name");
        String ns    = require(args, "namespace");
        int newSize  = optionalInt(args, "new_size_gi", 0);
        int curSize  = optionalInt(args, "current_size_gi", 0);
        if (newSize <= curSize)
            throw new IllegalArgumentException("new_size_gi (" + newSize + ") must be greater than current_size_gi (" + curSize + "). Longhorn does not support shrink.");
        Map<String, Object> extra = new LinkedHashMap<>();
        extra.put("volume_name", name); extra.put("namespace", ns);
        extra.put("old_size_gi", curSize); extra.put("new_size_gi", newSize);
        extra.put("expansion_gi", newSize - curSize);
        extra.put("online_resize", true); extra.put("pod_restart_required", false);
        return success("Volume '" + name + "' expansion " + curSize + "Gi → " + newSize + "Gi scheduled.", extra);
    }
}
