package com.ecoskiller.mcp.agents;
import java.util.*;
public class ClickhouseMetricNormalizationAgent extends BaseAgent {
    @Override public String toolName()    { return "clickhouse_metric_normalization"; }
    @Override public String description() { return "Normalize raw ClickHouse metric payloads to a canonical Ecoskiller schema before analytics ingestion."; }
    @Override public Map<String,Object> inputSchema() { return schema("metric_name","string","raw_value","number","unit","string","source_table","string"); }
    @Override public String execute(Map<String,Object> a) {
        String name   = str(a,"metric_name","unknown_metric");
        double raw    = Double.parseDouble(str(a,"raw_value","0"));
        String unit   = str(a,"unit","ms");
        String table  = str(a,"source_table","events");
        // Normalize: ms->seconds, bytes->KB, counts stay
        double norm   = "ms".equals(unit)?raw/1000.0:"bytes".equals(unit)?raw/1024.0:raw;
        String normUnit = "ms".equals(unit)?"s":"bytes".equals(unit)?"KB":unit;
        return result("CLICKHOUSE_METRIC_NORMALIZATION_AGENT","NORMALIZED",
            "metric_name="+name,"source_table="+table,
            String.format("raw_value=%.4f %s",raw,unit),
            String.format("normalized_value=%.6f %s",norm,normUnit),
            "schema_version=v3","timestamp="+new java.util.Date());
    }
}
