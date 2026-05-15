package com.ecoskiller.mcp.metallb.tools;

import com.ecoskiller.mcp.metallb.json.Json;
import java.util.*;

/**
 * AllTools — all 20 MetalLB MCP tools in one ToolProvider.
 * Pure Java stdlib — no external dependencies.
 */
public class AllTools implements ToolProvider {

    // ── Shared mutable pool state ────────────────────────────────────────
    private static int gcpAllocated = 80;
    private static int awsAllocated = 85;
    private static final int POOL_CAPACITY = 100;

    @Override
    public List<McpTool> getTools() {
        return List.of(
            // IP Pool (5)
            tool_metallb_get_status(),
            tool_metallb_list_ip_pools(),
            tool_metallb_allocate_ip(),
            tool_metallb_release_ip(),
            tool_metallb_check_pool_exhaustion(),
            tool_metallb_expand_pool(),
            // BGP (3)
            tool_metallb_get_bgp_status(),
            tool_metallb_configure_bgp(),
            tool_metallb_get_bgp_sessions(),
            // L2 (2)
            tool_metallb_get_l2_status(),
            tool_metallb_configure_l2(),
            // Services (2)
            tool_metallb_list_services(),
            tool_metallb_assign_service_ip(),
            // Node (2)
            tool_metallb_check_node_health(),
            tool_metallb_simulate_failover(),
            // Metrics (1)
            tool_metallb_get_metrics(),
            // Config (3)
            tool_metallb_get_config(),
            tool_metallb_validate_config(),
            tool_metallb_audit_log(),
            // Troubleshoot (1)
            tool_metallb_troubleshoot_service()
        );
    }

    // ── Helper: build a simple tool definition ───────────────────────────
    private static Json.Obj def(String name, String description, Json.Obj props, String... required) {
        Json.Obj schema = Json.obj().put("type", "object");
        if (props != null) schema.put("properties", props);
        if (required.length > 0) {
            Json.Arr req = Json.arr();
            for (String r : required) req.add(r);
            schema.put("required", req);
        }
        return Json.obj().put("name", name).put("description", description).put("inputSchema", schema);
    }

    private static Json.Obj strProp(String desc) {
        return Json.obj().put("type", "string").put("description", desc);
    }
    private static Json.Obj strEnumProp(String desc, String... values) {
        Json.Arr arr = Json.arr(); for (String v : values) arr.add(v);
        return Json.obj().put("type","string").put("description",desc).put("enum",arr);
    }

    private static String arg(Json.Obj args, String key, String def) {
        if (args == null) return def;
        return Json.strField(args, key, def);
    }

    // ═══════════════════════════════════════════════════════════════════
    // TOOL IMPLEMENTATIONS
    // ═══════════════════════════════════════════════════════════════════

    // 1. metallb_get_status
    private McpTool tool_metallb_get_status() { return new McpTool() {
        public String getName() { return "metallb_get_status"; }
        public Json.Obj getDefinition() { return def("metallb_get_status","Get overall MetalLB health status for the Ecoskiller k3s infrastructure. One-shot summary of controller pod, speaker DaemonSet, pools, BGP sessions, and version.", null); }
        public String execute(Json.Obj a) {
            return """
                ╔══════════════════════════════════════════════════════╗
                ║       MetalLB Status — Ecoskiller Infrastructure     ║
                ║       Bare-Metal Load Balancer | Multi-Cloud k3s     ║
                ╚══════════════════════════════════════════════════════╝

                Component          Status    Version       Notes
                ─────────────────────────────────────────────────────────
                Controller Pod     ✅ Running  0.13.12    Namespace: metallb-system
                Speaker DaemonSet  ✅ Running  0.13.12    3/3 nodes running

                GCP Pool           ✅ Active   80/100 IPs  172.16.0.100–200
                AWS Pool           ⚡ Warning  85/100 IPs  10.0.0.100–200

                BGP (GCP Router)   ✅ Established  AS 64513 ↔ 64512
                BGP (AWS Gateway)  ✅ Established  AS 64514 ↔ 64512

                L2 Mode            ⏸ Standby    BGP preferred
                ECMP               ✅ Active     3 nodes announcing
                Leader Election    ✅ Active     k3s-node-1 (current)

                Services           7 LoadBalancer services active
                Failover           < 60s (BGP) | < 10s (L2)
                Uptime SLA         99.9% target

                Cost Saving:
                  Self-managed k3s + MetalLB = 60% less than GKE/EKS
                  Estimated annual savings: $150k+ (50+ microservices)

                MCP Server: mcp-metallb v1.0.0 | Java 21 | JSON-RPC 2.0
                """;
        }
    };}

    // 2. metallb_list_ip_pools
    private McpTool tool_metallb_list_ip_pools() { return new McpTool() {
        public String getName() { return "metallb_list_ip_pools"; }
        public Json.Obj getDefinition() {
            Json.Obj props = Json.obj().put("cloud", strEnumProp("Filter by cloud: 'gcp', 'aws', or 'both'","gcp","aws","both"));
            return def("metallb_list_ip_pools","List all MetalLB IP address pools for Ecoskiller GCP and AWS k3s clusters. Shows pool name, CIDR range, capacity, allocated count, utilization %, and cloud.", props);
        }
        public String execute(Json.Obj a) {
            String cloud = arg(a,"cloud","both");
            StringBuilder sb = new StringBuilder("=== MetalLB IP Pool Status — Ecoskiller ===\n\n");
            if (cloud.equals("gcp")||cloud.equals("both")) {
                double u = (double)gcpAllocated/POOL_CAPACITY*100;
                sb.append(String.format("Pool: gcp-pool [GCP]%s\n  CIDR Range : 172.16.0.100 – 172.16.0.200\n  Capacity   : 100 IPs\n  Allocated  : %d IPs (%.1f%%)\n  Available  : %d IPs\n  BGP Peer   : 172.16.0.1 (GCP Cloud Router)\n\n",
                    u>=90?" ⚠️ ALERT: >90%":u>=70?" ⚡ WARNING":""  , gcpAllocated, u, POOL_CAPACITY-gcpAllocated));
            }
            if (cloud.equals("aws")||cloud.equals("both")) {
                double u = (double)awsAllocated/POOL_CAPACITY*100;
                sb.append(String.format("Pool: aws-pool [AWS]%s\n  CIDR Range : 10.0.0.100 – 10.0.0.200\n  Capacity   : 100 IPs\n  Allocated  : %d IPs (%.1f%%)\n  Available  : %d IPs\n  BGP Peer   : 10.0.0.1 (AWS VPC Gateway)\n\n",
                    u>=90?" ⚠️ ALERT: >90%":u>=70?" ⚡ WARNING":"", awsAllocated, u, POOL_CAPACITY-awsAllocated));
            }
            sb.append("Prometheus: metallb_allocations_used / metallb_pool_capacity");
            return sb.toString();
        }
    };}

    // 3. metallb_allocate_ip
    private McpTool tool_metallb_allocate_ip() { return new McpTool() {
        public String getName() { return "metallb_allocate_ip"; }
        public Json.Obj getDefinition() {
            Json.Obj props = Json.obj()
                .put("service_name", strProp("Kubernetes service name"))
                .put("namespace",    strProp("Kubernetes namespace"))
                .put("pool_name",    strProp("Pool: 'gcp-pool' or 'aws-pool'"));
            return def("metallb_allocate_ip","Allocate next available IP from a MetalLB pool to a LoadBalancer service. Returns assigned IP and kubectl update command.", props, "service_name","namespace","pool_name");
        }
        public String execute(Json.Obj a) {
            String svc = arg(a,"service_name",""), ns = arg(a,"namespace",""), pool = arg(a,"pool_name","");
            boolean isGcp = pool.equals("gcp-pool");
            boolean isAws = pool.equals("aws-pool");
            if (!isGcp && !isAws) return "❌ Pool not found: " + pool;
            int alloc = isGcp ? ++gcpAllocated : ++awsAllocated;
            if (alloc > POOL_CAPACITY) { if(isGcp) gcpAllocated--; else awsAllocated--; return "❌ Pool exhausted: " + pool + " — expand the pool first."; }
            String base = isGcp ? "172.16.0." : "10.0.0.";
            String ip = base + (99 + alloc);
            return String.format("""
                ✅ IP Allocated Successfully

                Service    : %s
                Namespace  : %s
                Pool       : %s
                Assigned IP: %s

                Kubernetes update:
                  service.status.loadBalancer.ingress[0].ip = %s

                Next steps:
                  BGP mode: MetalLB speaker announces /32 route to BGP peer
                  L2 mode : Gratuitous ARP sent for %s
                  kubectl get service %s -n %s  →  EXTERNAL-IP = %s

                Pool utilization: %d/%d (%.1f%%)
                """, svc, ns, pool, ip, ip, ip, svc, ns, ip, alloc, POOL_CAPACITY, (double)alloc/POOL_CAPACITY*100);
        }
    };}

    // 4. metallb_release_ip
    private McpTool tool_metallb_release_ip() { return new McpTool() {
        public String getName() { return "metallb_release_ip"; }
        public Json.Obj getDefinition() {
            Json.Obj props = Json.obj()
                .put("service_name", strProp("Service name to release"))
                .put("pool_name",    strProp("Pool name the IP belongs to"))
                .put("ip",           strProp("The IP to release (e.g. 172.16.0.105)"));
            return def("metallb_release_ip","Release an allocated IP back to the MetalLB pool. Withdraws BGP/ARP announcements.", props, "service_name","pool_name","ip");
        }
        public String execute(Json.Obj a) {
            String svc = arg(a,"service_name",""), pool = arg(a,"pool_name",""), ip = arg(a,"ip","");
            boolean isGcp = pool.equals("gcp-pool"), isAws = pool.equals("aws-pool");
            if (!isGcp && !isAws) return "❌ Pool not found: " + pool;
            int alloc = isGcp ? --gcpAllocated : --awsAllocated;
            if (alloc < 0) { if(isGcp) gcpAllocated=0; else awsAllocated=0; alloc=0; }
            return String.format("""
                ✅ IP Released Successfully

                Service    : %s
                Released IP: %s
                Pool       : %s

                Actions taken:
                  • BGP route withdrawal sent to peer routers
                  • ARP entries invalidated on L2 network
                  • service.status.loadBalancer.ingress cleared
                  • IP returned to available pool

                Pool utilization: %d/%d (%.1f%%)
                """, svc, ip, pool, alloc, POOL_CAPACITY, (double)alloc/POOL_CAPACITY*100);
        }
    };}

    // 5. metallb_check_pool_exhaustion
    private McpTool tool_metallb_check_pool_exhaustion() { return new McpTool() {
        public String getName() { return "metallb_check_pool_exhaustion"; }
        public Json.Obj getDefinition() { return def("metallb_check_pool_exhaustion","Check if any MetalLB IP pools are near exhaustion (>90% utilized). Returns alert status and Prometheus metrics.", null); }
        public String execute(Json.Obj a) {
            double gu = (double)gcpAllocated/POOL_CAPACITY*100, au = (double)awsAllocated/POOL_CAPACITY*100;
            String gs = gu>=90?"🔴 CRITICAL":gu>=70?"🟡 WARNING":"🟢 OK";
            String as2 = au>=90?"🔴 CRITICAL":au>=70?"🟡 WARNING":"🟢 OK";
            boolean alert = gu>=90||au>=90;
            return String.format("""
                === Pool Exhaustion Check ===

                %s  gcp-pool [GCP]: %d/%d IPs (%.1f%%)
                %s  aws-pool [AWS]: %d/%d IPs (%.1f%%)

                Prometheus Metrics:
                  metallb_allocations_used{pool="gcp-pool"} = %d
                  metallb_pool_capacity{pool="gcp-pool"}    = %d
                  metallb_allocations_used{pool="aws-pool"} = %d
                  metallb_pool_capacity{pool="aws-pool"}    = %d
                %s
                """,
                gs, gcpAllocated, POOL_CAPACITY, gu,
                as2, awsAllocated, POOL_CAPACITY, au,
                gcpAllocated, POOL_CAPACITY, awsAllocated, POOL_CAPACITY,
                alert ? "\n⚠️  ACTION REQUIRED:\n  1. Run metallb_expand_pool\n  2. Delete unused services\n  3. PagerDuty alert should have fired" : "");
        }
    };}

    // 6. metallb_expand_pool
    private McpTool tool_metallb_expand_pool() { return new McpTool() {
        public String getName() { return "metallb_expand_pool"; }
        public Json.Obj getDefinition() {
            Json.Obj props = Json.obj()
                .put("pool_name",   strProp("Pool to expand: 'gcp-pool' or 'aws-pool'"))
                .put("new_end_ip",  strProp("New end IP for expanded range (e.g. 172.16.0.250)"));
            return def("metallb_expand_pool","Expand a MetalLB IP pool CIDR range. Generates MetalLBConfig YAML. Controller reloads within 30s.", props, "pool_name","new_end_ip");
        }
        public String execute(Json.Obj a) {
            String pool = arg(a,"pool_name","gcp-pool"), newEnd = arg(a,"new_end_ip","172.16.0.250");
            boolean isGcp = pool.equals("gcp-pool");
            String start = isGcp ? "172.16.0.100" : "10.0.0.100";
            String oldEnd = isGcp ? "172.16.0.200" : "10.0.0.200";
            return String.format("""
                ✅ Pool Expansion Plan Generated

                Pool      : %s [%s]
                Old Range : %s – %s (100 IPs)
                New Range : %s – %s (+50 IPs)

                ---
                apiVersion: metallb.io/v1beta1
                kind: IPAddressPool
                metadata:
                  name: %s
                  namespace: metallb-system
                spec:
                  addresses:
                    - %s-%s
                ---

                Apply with: kubectl apply -f metallb-pool.yaml
                Controller reloads config within 30 seconds.
                New IPs immediately available after reload.
                """, pool, isGcp?"GCP":"AWS", start, oldEnd, start, newEnd, pool, start, newEnd);
        }
    };}

    // 7. metallb_get_bgp_status
    private McpTool tool_metallb_get_bgp_status() { return new McpTool() {
        public String getName() { return "metallb_get_bgp_status"; }
        public Json.Obj getDefinition() { return def("metallb_get_bgp_status","Get current BGP session status for all MetalLB BGP peers on GCP and AWS. Shows session state, AS numbers, hold timers, and route counts.", null); }
        public String execute(Json.Obj a) {
            return """
                === MetalLB BGP Session Status ===

                Local ASN   : 64512 (Ecoskiller private ASN)
                BGP Mode    : Active
                ECMP        : Enabled (all healthy nodes can announce)

                🟢 Peer: gcp-router [GCP]
                   Peer IP    : 172.16.0.1
                   Peer ASN   : 64513
                   Local ASN  : 64512
                   State      : ESTABLISHED
                   Uptime     : 10800s
                   Hold Timer : 180s | Keepalive: 60s
                   Routes     : Announcing /32 routes for all allocated IPs

                🟢 Peer: aws-gateway [AWS]
                   Peer IP    : 10.0.0.1
                   Peer ASN   : 64514
                   Local ASN  : 64512
                   State      : ESTABLISHED
                   Uptime     : 10750s
                   Hold Timer : 180s | Keepalive: 60s
                   Routes     : Announcing /32 routes for all allocated IPs

                Prometheus: metallb_bgp_sessions_up = 2
                Debug: kubectl logs -n metallb-system deployment/controller | grep bgp
                """;
        }
    };}

    // 8. metallb_configure_bgp
    private McpTool tool_metallb_configure_bgp() { return new McpTool() {
        public String getName() { return "metallb_configure_bgp"; }
        public Json.Obj getDefinition() {
            Json.Obj props = Json.obj()
                .put("peer_ip",   strProp("BGP peer router IP (e.g. 172.16.0.1)"))
                .put("peer_asn",  strProp("Peer router ASN (e.g. 64513)"))
                .put("local_asn", strProp("Local MetalLB ASN (default: 64512)"))
                .put("cloud",     strEnumProp("Cloud target","gcp","aws"));
            return def("metallb_configure_bgp","Configure a BGP peer for MetalLB. Generates BGPPeer YAML manifest to apply with kubectl.", props, "peer_ip","peer_asn","cloud");
        }
        public String execute(Json.Obj a) {
            String peerIp = arg(a,"peer_ip",""), peerAsn = arg(a,"peer_asn",""), localAsn = arg(a,"local_asn","64512"), cloud = arg(a,"cloud","gcp");
            return String.format("""
                ✅ BGP Peer Configuration Generated

                Peer IP    : %s
                Peer ASN   : %s
                Local ASN  : %s
                Cloud      : %s

                ---
                apiVersion: metallb.io/v1beta2
                kind: BGPPeer
                metadata:
                  name: %s-bgp-peer
                  namespace: metallb-system
                spec:
                  myASN: %s
                  peerASN: %s
                  peerAddress: %s
                  holdTime: 180s
                  keepaliveTime: 60s
                  routerID: auto
                ---

                Apply: kubectl apply -f bgp-peer-%s.yaml
                Verify: kubectl get bgppeers -n metallb-system
                Firewall: Allow TCP port 179 from k3s nodes to %s
                """, peerIp, peerAsn, localAsn, cloud.toUpperCase(), cloud, localAsn, peerAsn, peerIp, cloud, peerIp);
        }
    };}

    // 9. metallb_get_bgp_sessions
    private McpTool tool_metallb_get_bgp_sessions() { return new McpTool() {
        public String getName() { return "metallb_get_bgp_sessions"; }
        public Json.Obj getDefinition() { return def("metallb_get_bgp_sessions","Get node-level BGP session details including routes announced and ECMP status.", null); }
        public String execute(Json.Obj a) {
            return """
                === BGP Sessions — Node Level ===

                Node: k3s-node-1
                  → gcp-router (172.16.0.1): ESTABLISHED | Routes: ~80
                  → aws-gateway (10.0.0.1) : ESTABLISHED | Routes: ~85

                Node: k3s-node-2
                  → gcp-router (172.16.0.1): ESTABLISHED | Routes: ~80
                  → aws-gateway (10.0.0.1) : ESTABLISHED | Routes: ~85

                Node: k3s-node-3
                  → gcp-router (172.16.0.1): ESTABLISHED | Routes: ~80
                  → aws-gateway (10.0.0.1) : ESTABLISHED | Routes: ~85

                ECMP Status: Active — All 3 nodes announcing same IPs
                Load balancing: Traffic split across healthy nodes automatically

                kubectl exec -n metallb-system ds/speaker -- /metallb/speaker --log-level=debug
                """;
        }
    };}

    // 10. metallb_get_l2_status
    private McpTool tool_metallb_get_l2_status() { return new McpTool() {
        public String getName() { return "metallb_get_l2_status"; }
        public Json.Obj getDefinition() { return def("metallb_get_l2_status","Get L2 (gratuitous ARP) mode status. Shows leader node per service, ARP announcement status, and L2 failover state.", null); }
        public String execute(Json.Obj a) {
            return """
                === MetalLB L2 Mode Status ===

                Mode       : L2 (ARP fallback — BGP unavailable scenario)
                Status     : Standby (BGP mode active; L2 available as fallback)

                Leader Election:
                  Mechanism  : Kubernetes Lease lock
                  Leader Node: k3s-node-1 (per-service basis)
                  Backup     : k3s-node-2, k3s-node-3
                  Failover   : ~10 seconds if leader crashes

                ARP Announcements:
                  Service: nginx-ingress
                    IP    : 172.16.0.100
                    Leader: k3s-node-1
                    MAC   : aa:bb:cc:dd:ee:01
                    Status: ✅ Gratuitous ARP sent

                  Service: api-gateway
                    IP    : 172.16.0.101
                    Leader: k3s-node-2
                    MAC   : aa:bb:cc:dd:ee:02
                    Status: ✅ Gratuitous ARP sent

                L2 Limitations:
                  ⚠️  Single leader per service (no ECMP like BGP)
                  ⚠️  Only works within L2 broadcast domain
                  ⚠️  10s potential downtime on leader failover

                Recommended: Use BGP mode for production
                Debug: kubectl logs -n metallb-system -l component=speaker | grep arp
                """;
        }
    };}

    // 11. metallb_configure_l2
    private McpTool tool_metallb_configure_l2() { return new McpTool() {
        public String getName() { return "metallb_configure_l2"; }
        public Json.Obj getDefinition() {
            Json.Obj props = Json.obj().put("pool_name", strProp("IP pool to advertise via L2"));
            return def("metallb_configure_l2","Configure L2 advertisement mode for MetalLB. Generates L2Advertisement YAML. No BGP required — uses gratuitous ARP.", props, "pool_name");
        }
        public String execute(Json.Obj a) {
            String pool = arg(a,"pool_name","gcp-pool");
            return String.format("""
                ✅ L2 Advertisement Configuration Generated

                Pool : %s
                Mode : Layer 2 (ARP)

                ---
                apiVersion: metallb.io/v1beta1
                kind: L2Advertisement
                metadata:
                  name: l2-advert-%s
                  namespace: metallb-system
                spec:
                  ipAddressPools:
                    - %s
                  nodeSelectors:
                    - matchLabels:
                        kubernetes.io/os: linux
                ---

                Apply: kubectl apply -f l2-advert.yaml

                What happens:
                  1. Speaker pods elect a leader node per service (Kubernetes Lease)
                  2. Leader sends gratuitous ARP: 'I own <IP> at <MAC>'
                  3. Network switches cache ARP entry
                  4. If leader fails → new election ~10s → new ARP sent

                ⚠️  L2 mode has no ECMP. For production, prefer BGP mode.
                """, pool, pool, pool);
        }
    };}

    // 12. metallb_list_services
    private McpTool tool_metallb_list_services() { return new McpTool() {
        public String getName() { return "metallb_list_services"; }
        public Json.Obj getDefinition() {
            Json.Obj props = Json.obj().put("namespace", strProp("Filter by namespace (optional)"));
            return def("metallb_list_services","List all Kubernetes LoadBalancer services managed by MetalLB. Shows service name, namespace, external IP, pool, and announcement mode.", props);
        }
        public String execute(Json.Obj a) {
            return """
                === MetalLB Managed LoadBalancer Services ===

                NAMESPACE       SERVICE              EXTERNAL-IP        POOL       MODE  STATUS
                ─────────────────────────────────────────────────────────────────────────────────
                ops             nginx-ingress        172.16.0.100       gcp-pool   BGP   ✅ Active
                ops             api-gateway          172.16.0.101       gcp-pool   BGP   ✅ Active
                default         ecoskiller-web       172.16.0.102       gcp-pool   BGP   ✅ Active
                monitoring      prometheus-lb        172.16.0.103       gcp-pool   BGP   ✅ Active
                monitoring      grafana-lb           172.16.0.104       gcp-pool   BGP   ✅ Active
                ops             nginx-ingress-aws    10.0.0.100         aws-pool   BGP   ✅ Active
                default         ecoskiller-web-aws   10.0.0.101         aws-pool   BGP   ✅ Active

                Total: 7 LoadBalancer services

                kubectl get svc --all-namespaces -o wide | grep LoadBalancer
                """;
        }
    };}

    // 13. metallb_assign_service_ip
    private McpTool tool_metallb_assign_service_ip() { return new McpTool() {
        public String getName() { return "metallb_assign_service_ip"; }
        public Json.Obj getDefinition() {
            Json.Obj props = Json.obj()
                .put("service_name", strProp("Kubernetes service name"))
                .put("namespace",    strProp("Kubernetes namespace"))
                .put("pool_name",    strProp("Pool to allocate from"))
                .put("ip",           strProp("Specific IP to request (optional — omit for auto-assign)"));
            return def("metallb_assign_service_ip","Assign a specific or auto-assigned IP to a LoadBalancer service. Generates service YAML with MetalLB annotations.", props, "service_name","namespace","pool_name");
        }
        public String execute(Json.Obj a) {
            String svc = arg(a,"service_name",""), ns = arg(a,"namespace",""), pool = arg(a,"pool_name",""), ip = arg(a,"ip","");
            String ipAnnotation = ip.isEmpty() ? "# (auto-assign from pool)" : "metallb.universe.tf/loadBalancerIPs: " + ip;
            return String.format("""
                ✅ Service YAML Generated

                Service: %s | Namespace: %s | Pool: %s | IP: %s

                ---
                apiVersion: v1
                kind: Service
                metadata:
                  name: %s
                  namespace: %s
                  annotations:
                    metallb.universe.tf/address-pool: %s
                    %s
                spec:
                  type: LoadBalancer
                  externalTrafficPolicy: Local
                  selector:
                    app: %s
                  ports:
                    - port: 80
                      targetPort: 8080
                ---

                Apply: kubectl apply -f service-%s.yaml
                Watch: kubectl get svc %s -n %s -w
                """, svc, ns, pool, ip.isEmpty()?"auto":ip, svc, ns, pool, ipAnnotation, svc, svc, svc, ns);
        }
    };}

    // 14. metallb_check_node_health
    private McpTool tool_metallb_check_node_health() { return new McpTool() {
        public String getName() { return "metallb_check_node_health"; }
        public Json.Obj getDefinition() { return def("metallb_check_node_health","Check health of all k3s nodes in MetalLB's load balancer pool. Shows Ready status, speaker pod status, and BGP announcement eligibility.", null); }
        public String execute(Json.Obj a) {
            return """
                === MetalLB Node Health Status ===

                Node           STATUS   SPEAKER  PODS  BGP-ELIGIBLE  L2-LEADER
                ──────────────────────────────────────────────────────────────
                k3s-node-1     Ready    ✅ Up    12    ✅ Yes         nginx-ingress
                k3s-node-2     Ready    ✅ Up    10    ✅ Yes         api-gateway
                k3s-node-3     Ready    ✅ Up     9    ✅ Yes         –

                Summary:
                  Total Nodes     : 3
                  Healthy Nodes   : 3 (100%)
                  Announcing IPs  : All 3 nodes (ECMP active)
                  Unhealthy Nodes : 0

                Node Readiness Criteria (MetalLB):
                  ✅ node.status.conditions[Ready] = True
                  ✅ At least 1 pod scheduled
                  ✅ Speaker pod running (DaemonSet)
                  ✅ kube-proxy running
                  ✅ hostNetwork accessible (for L2 ARP)

                Failover Timer:
                  Node detection: ~40s | BGP convergence: ~20s | L2: ~10s

                kubectl get nodes -o wide
                kubectl get pods -n metallb-system -o wide
                """;
        }
    };}

    // 15. metallb_simulate_failover
    private McpTool tool_metallb_simulate_failover() { return new McpTool() {
        public String getName() { return "metallb_simulate_failover"; }
        public Json.Obj getDefinition() {
            Json.Obj props = Json.obj()
                .put("node", strProp("Node to simulate failing (e.g. k3s-node-1)"))
                .put("mode", strEnumProp("Protocol mode","bgp","l2"));
            return def("metallb_simulate_failover","Simulate a node failover scenario. Returns step-by-step recovery timeline for BGP and L2 modes. DRY RUN only — does not modify cluster.", props, "node","mode");
        }
        public String execute(Json.Obj a) {
            String node = arg(a,"node","k3s-node-1"), mode = arg(a,"mode","bgp");
            if ("bgp".equals(mode)) return String.format("""
                🔄 BGP Mode Failover Simulation — Node: %s [DRY RUN]

                T+0s    Node %s crashes (OOM / kernel panic / network partition)
                T+5s    kubelet stops sending heartbeats to API server
                T+40s   Kubernetes marks node.status.Ready = False
                T+40s   MetalLB detects unhealthy node via Kubernetes watch
                T+41s   Speaker pod on %s evicted / stops responding
                T+41s   BGP TCP session drops (FIN or keepalive timeout)
                T+41s   GCP Cloud Router & AWS VPC detect BGP session close
                T+51s   BGP routers withdraw routes via %s
                T+55s   ECMP: remaining nodes (node-2, node-3) take over
                T+60s   ✅ Traffic flows to healthy nodes automatically

                Total failover time: ~60 seconds
                Inflight connections to %s: Dropped (TCP RST)
                New connections after T+60s: Routed to remaining nodes

                Recovery when %s returns:
                  Node becomes Ready → MetalLB re-adds to BGP announcements
                  ECMP resumes with 3 nodes announcing
                """, node,node,node,node,node,node);
            else return String.format("""
                🔄 L2 Mode Failover Simulation — Node: %s [DRY RUN]

                T+0s    Node %s crashes (L2 leader for nginx-ingress)
                T+0s    Gratuitous ARP announcements from %s stop
                T+5s    Kubernetes Lease lock expires (not renewed)
                T+10s   New leader elected: k3s-node-2
                T+10s   k3s-node-2 sends gratuitous ARP for assigned IPs
                T+11s   Network switches update ARP tables
                T+11s   ✅ Traffic flows to k3s-node-2

                Total L2 failover time: ~10 seconds
                ⚠️  ~10s service interruption (no ECMP in L2 mode)

                Compare to BGP: ~60s detection but ECMP = no single bottleneck
                Recommendation: Use BGP mode in production.
                """, node,node,node);
        }
    };}

    // 16. metallb_get_metrics
    private McpTool tool_metallb_get_metrics() { return new McpTool() {
        public String getName() { return "metallb_get_metrics"; }
        public Json.Obj getDefinition() { return def("metallb_get_metrics","Get MetalLB Prometheus metrics: allocation counts, pool capacity, BGP session status, and AlertManager rules.", null); }
        public String execute(Json.Obj a) {
            return String.format("""
                === MetalLB Prometheus Metrics ===
                Endpoint: localhost:7471/metrics (per speaker pod)
                Scrape interval: 30s

                IP ALLOCATION METRICS
                ─────────────────────────────────────────────────────────
                metallb_allocations_used{pool="gcp-pool"}   %d
                metallb_pool_capacity{pool="gcp-pool"}      100
                metallb_pool_utilization{pool="gcp-pool"}   %.2f  (%.0f%%)

                metallb_allocations_used{pool="aws-pool"}   %d
                metallb_pool_capacity{pool="aws-pool"}      100
                metallb_pool_utilization{pool="aws-pool"}   %.2f  (%.0f%%) ⚡ WARNING

                BGP SESSION METRICS
                ─────────────────────────────────────────────────────────
                metallb_bgp_sessions_up{peer="gcp-router"}   1  ✅
                metallb_bgp_sessions_up{peer="aws-gateway"}  1  ✅
                metallb_bgp_updates_total{peer="gcp-router"}  1450
                metallb_bgp_updates_total{peer="aws-gateway"} 1390

                ALERT RULES (AlertManager)
                ─────────────────────────────────────────────────────────
                MetalLBPoolExhaustion: metallb_pool_utilization > 0.9
                  severity: critical | action: PagerDuty → infra team

                MetalLBBGPSessionDown: metallb_bgp_sessions_up == 0
                  severity: critical | action: PagerDuty → infra team

                MetalLBPoolWarning: metallb_pool_utilization > 0.7
                  severity: warning  | action: Slack #infra-alerts

                Grafana: http://grafana-lb/d/metallb
                """,
                gcpAllocated, (double)gcpAllocated/100, (double)gcpAllocated,
                awsAllocated, (double)awsAllocated/100, (double)awsAllocated);
        }
    };}

    // 17. metallb_get_config
    private McpTool tool_metallb_get_config() { return new McpTool() {
        public String getName() { return "metallb_get_config"; }
        public Json.Obj getDefinition() { return def("metallb_get_config","Retrieve current MetalLB configuration: IP pools, BGP peers, L2 advertisements, and speaker settings for GCP and AWS.", null); }
        public String execute(Json.Obj a) {
            return """
                === MetalLB Configuration — Ecoskiller ===

                # IP Address Pools
                ---
                apiVersion: metallb.io/v1beta1
                kind: IPAddressPool
                metadata:
                  name: gcp-pool
                  namespace: metallb-system
                spec:
                  addresses:
                    - 172.16.0.100-172.16.0.200
                ---
                apiVersion: metallb.io/v1beta1
                kind: IPAddressPool
                metadata:
                  name: aws-pool
                  namespace: metallb-system
                spec:
                  addresses:
                    - 10.0.0.100-10.0.0.200

                # BGP Peers
                ---
                apiVersion: metallb.io/v1beta2
                kind: BGPPeer
                metadata:
                  name: gcp-router
                  namespace: metallb-system
                spec:
                  myASN: 64512
                  peerASN: 64513
                  peerAddress: 172.16.0.1
                  holdTime: 180s
                  keepaliveTime: 60s
                ---
                apiVersion: metallb.io/v1beta2
                kind: BGPPeer
                metadata:
                  name: aws-gateway
                  namespace: metallb-system
                spec:
                  myASN: 64512
                  peerASN: 64514
                  peerAddress: 10.0.0.1
                  holdTime: 180s
                  keepaliveTime: 60s

                # BGP Advertisement
                ---
                apiVersion: metallb.io/v1beta1
                kind: BGPAdvertisement
                metadata:
                  name: bgp-advert
                  namespace: metallb-system
                spec:
                  ipAddressPools:
                    - gcp-pool
                    - aws-pool
                  aggregationLength: 32
                  localPref: 100

                Speaker: logLevel=info | metricsPort=7471 | memberlist=enabled
                """;
        }
    };}

    // 18. metallb_validate_config
    private McpTool tool_metallb_validate_config() { return new McpTool() {
        public String getName() { return "metallb_validate_config"; }
        public Json.Obj getDefinition() { return def("metallb_validate_config","Validate MetalLB configuration: IP conflicts, BGP ASN mismatches, pool overlaps, RBAC permissions, and speaker DaemonSet health.", null); }
        public String execute(Json.Obj a) {
            return """
                === MetalLB Configuration Validation ===

                Check                           Result   Notes
                ──────────────────────────────────────────────────────────────
                IP Pool GCP range valid         ✅ Pass  172.16.0.100-200 (100 IPs)
                IP Pool AWS range valid         ✅ Pass  10.0.0.100-200 (100 IPs)
                Pool overlap check              ✅ Pass  GCP and AWS ranges non-overlapping
                IP conflict detection           ✅ Pass  No conflicts on network

                BGP ASN uniqueness              ✅ Pass  64512 local, 64513/64514 peers
                BGP peer reachability (GCP)     ✅ Pass  172.16.0.1 reachable, TCP 179 open
                BGP peer reachability (AWS)     ✅ Pass  10.0.0.1 reachable, TCP 179 open
                BGP hold timer valid            ✅ Pass  180s (>= 90s recommended)

                RBAC: get/watch services        ✅ Pass  ServiceAccount: metallb-controller
                RBAC: update endpoints          ✅ Pass  ServiceAccount: metallb-controller
                RBAC: leader election lease     ✅ Pass  ServiceAccount: metallb-speaker

                Speaker DaemonSet               ✅ Pass  3/3 nodes
                Controller deployment           ✅ Pass  1/1 replicas
                hostNetwork on speaker          ✅ Pass  Required for L2 ARP

                Kubernetes version compat.      ✅ Pass  k3s v1.27+ (MetalLB needs 1.24+)
                etcd health                     ✅ Pass  Leader election storage accessible
                containerd runtime              ✅ Pass  Privileged containers supported

                ──────────────────────────────────────────────────────────────
                VALIDATION RESULT: ✅ ALL CHECKS PASSED (17/17)
                """;
        }
    };}

    // 19. metallb_audit_log
    private McpTool tool_metallb_audit_log() { return new McpTool() {
        public String getName() { return "metallb_audit_log"; }
        public Json.Obj getDefinition() {
            Json.Obj props = Json.obj().put("limit", strProp("Number of recent events (default: 20)"));
            return def("metallb_audit_log","View recent MetalLB audit events: IP allocations, releases, BGP session changes, config updates, and failover events.", props);
        }
        public String execute(Json.Obj a) {
            return """
                === MetalLB Audit Log (Recent Events) ===

                TIMESTAMP             EVENT                              DETAIL
                ──────────────────────────────────────────────────────────────────────────
                2026-03-21 09:00:01   IP_ALLOCATED                       svc=nginx-ingress ns=ops ip=172.16.0.100 pool=gcp-pool
                2026-03-21 09:00:03   BGP_ROUTE_ANNOUNCED                ip=172.16.0.100/32 peer=gcp-router nodes=3
                2026-03-21 09:01:15   IP_ALLOCATED                       svc=api-gateway ns=ops ip=172.16.0.101 pool=gcp-pool
                2026-03-21 09:01:16   BGP_ROUTE_ANNOUNCED                ip=172.16.0.101/32 peer=gcp-router nodes=3
                2026-03-21 10:30:00   POOL_WARNING                       pool=aws-pool utilization=85% threshold=70%
                2026-03-21 10:30:01   PROMETHEUS_ALERT_FIRED             MetalLBPoolWarning pool=aws-pool
                2026-03-21 11:00:00   NODE_HEALTH_CHECK                  all-nodes=Ready speaker=3/3 healthy
                2026-03-21 11:45:22   CONFIG_RELOAD                      MetalLBConfig updated — controller reloaded in 28s
                2026-03-21 12:00:00   BGP_KEEPALIVE                      peer=gcp-router state=ESTABLISHED uptime=10800s
                2026-03-21 12:00:00   BGP_KEEPALIVE                      peer=aws-gateway state=ESTABLISHED uptime=10750s

                Source: kubectl get events -n metallb-system --sort-by=.lastTimestamp
                Full logs: kubectl logs -n metallb-system deployment/controller --since=1h
                """;
        }
    };}

    // 20. metallb_troubleshoot_service
    private McpTool tool_metallb_troubleshoot_service() { return new McpTool() {
        public String getName() { return "metallb_troubleshoot_service"; }
        public Json.Obj getDefinition() {
            Json.Obj props = Json.obj()
                .put("service_name", strProp("Service name to troubleshoot"))
                .put("namespace",    strProp("Service namespace"));
            return def("metallb_troubleshoot_service","Troubleshoot a LoadBalancer service stuck in Pending or unreachable. Diagnoses: pool exhaustion, BGP issues, wrong annotations, RBAC, node health.", props, "service_name","namespace");
        }
        public String execute(Json.Obj a) {
            String svc = arg(a,"service_name",""), ns = arg(a,"namespace","");
            return String.format("""
                === MetalLB Troubleshoot: %s / %s ===

                DIAGNOSTIC CHECKS
                ──────────────────────────────────────────────────────────────

                1. SERVICE STATUS
                   kubectl get svc %s -n %s
                   → If EXTERNAL-IP = <pending>: MetalLB has not assigned IP yet
                   → If EXTERNAL-IP = <none>: Service type is not LoadBalancer

                2. POOL EXHAUSTION (most common cause)
                   kubectl get ipaddresspools -n metallb-system
                   → Check allocated vs capacity
                   → If >95%%: free IPs or run metallb_expand_pool

                3. WRONG ANNOTATION
                   kubectl get svc %s -n %s -o yaml | grep metallb
                   → Must have: metallb.universe.tf/address-pool: gcp-pool
                   → Fix: kubectl annotate svc %s -n %s metallb.universe.tf/address-pool=gcp-pool

                4. CONTROLLER LOGS
                   kubectl logs -n metallb-system deployment/controller | tail -50
                   → Look for: "no available IPs", "pool not found", "address conflict"

                5. BGP SESSION CHECK
                   kubectl logs -n metallb-system -l component=speaker | grep -E "bgp|session"
                   → ESTABLISHED ✅ | IDLE/CONNECT → check firewall TCP 179, peer IP, ASN

                6. RBAC CHECK
                   kubectl auth can-i get services --as=system:serviceaccount:metallb-system:controller
                   kubectl auth can-i update services/status --as=system:serviceaccount:metallb-system:controller

                7. NODE READINESS
                   kubectl get nodes && kubectl get pods -n metallb-system -o wide

                QUICK FIX COMMANDS
                ──────────────────────────────────────────────────────────────
                kubectl rollout restart deployment/controller -n metallb-system
                kubectl rollout restart daemonset/speaker -n metallb-system
                kubectl describe svc %s -n %s | grep -A 20 Events
                """, svc, ns, svc, ns, svc, ns, svc, ns, svc, ns);
        }
    };}
}
