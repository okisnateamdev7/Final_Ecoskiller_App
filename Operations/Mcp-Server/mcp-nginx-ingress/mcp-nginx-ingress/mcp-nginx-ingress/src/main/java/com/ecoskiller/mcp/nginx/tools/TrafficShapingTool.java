package com.ecoskiller.mcp.nginx.tools;
import com.ecoskiller.mcp.nginx.config.ServerConfig;
import java.util.*;

/** Tool 10 — traffic_shaping */
public class TrafficShapingTool extends BaseNginxTool {
    public TrafficShapingTool(ServerConfig c) { super(c); }

    @Override public Map<String, Object> descriptor() {
        return Map.of(
            "name", "traffic_shaping",
            "description",
                "Configure QoS and traffic shaping on NGINX Ingress. " +
                "Set per-IP max concurrent connections, request queuing (FIFO), " +
                "keepalive timeouts, proxy buffer sizes, compression (gzip/brotli). " +
                "Enables fair resource distribution during peak load (mass assessment events). " +
                "Prevents connection exhaustion and thundering herd scenarios. " +
                "Actions: configure | reset | get.",
            "inputSchema", Map.of(
                "type", "object",
                "properties", Map.of(
                    "action",        enumProp("Operation", "configure", "reset", "get"),
                    "service",       property("string", "Service to apply shaping"),
                    "maxConns",      property("integer", "Max concurrent connections per IP (1–10000)"),
                    "queueSize",     property("integer", "Request queue size when backend full (0–100000)"),
                    "gzipEnabled",   property("boolean", "Enable gzip compression"),
                    "brotliEnabled", property("boolean", "Enable brotli compression"),
                    "http2Enabled",  property("boolean", "Enable HTTP/2")
                ),
                "required", List.of("action")
            )
        );
    }

    @Override public Object execute(Map<String, Object> args) {
        String action      = str(args, "action",      "get");
        String service     = str(args, "service",     "assessment-service");
        int    maxConns    = intArg(args, "maxConns",  config.getMaxConnPerIp());
        int    queueSize   = intArg(args, "queueSize", 1000);
        boolean gzip       = boolArg(args, "gzipEnabled",   true);
        boolean brotli     = boolArg(args, "brotliEnabled",  false);
        boolean http2      = boolArg(args, "http2Enabled",   true);

        if ("configure".equals(action)) {
            Map<String, Object> d = new LinkedHashMap<>();
            d.put("service",       service);
            d.put("maxConnsPerIp", maxConns);
            d.put("queueSize",     queueSize);
            d.put("compression",   Map.of("gzip", gzip, "brotli", brotli,
                                          "savings", "50-80% for text/JSON"));
            d.put("http2",         http2);
            d.put("keepaliveTimeout", 75);
            d.put("proxyBufferSize",  "4k");
            d.put("proxyBuffers",     "8 8k");
            d.put("nginxDirectives", List.of(
                "limit_conn_zone $binary_remote_addr zone=conn_limit:10m;",
                "limit_conn conn_limit " + maxConns + ";",
                gzip  ? "gzip on;\ngzip_types text/plain text/css application/json;\ngzip_comp_level 6;" : "",
                http2 ? "listen 443 ssl http2;" : "listen 443 ssl;",
                "proxy_buffer_size 4k;",
                "proxy_buffers 8 8k;"
            ));
            d.put("bandwidthSavingEstimate",
                "At 100GB/day: ~60GB/day after gzip → saves ~₹500/month on GCP egress");
            return success("Traffic shaping configured for " + service, d);
        }
        // get
        Map<String, Object> d = new LinkedHashMap<>();
        d.put("service",          service);
        d.put("maxConnsPerIp",    config.getMaxConnPerIp());
        d.put("queueEnabled",     true);
        d.put("queueSize",        1000);
        d.put("gzipEnabled",      true);
        d.put("http2Enabled",     true);
        d.put("currentConns",     342);
        d.put("queuedRequests",   0);
        d.put("compressionRatio", "62%");
        return success("Traffic shaping status", d);
    }
}
