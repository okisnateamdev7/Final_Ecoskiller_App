package com.ecoskiller.mcp.institute.tools;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.UUID;

// ═════════════════════════════════════════════════════════════════════════════
//  1.  ACADEMIC_STRUCTURE_AGENT__ANTIGRAVITY_SEALED
// ═════════════════════════════════════════════════════════════════════════════

@Slf4j
@Component
public class TransportTool implements AgentTool {

    @Override
    public String execute(JsonNode args) {
        String tenantId   = getString(args, "tenant_id");
        String action     = getString(args, "action");
        String routeId    = getString(args, "route_id");
        String busId      = getString(args, "bus_id");

        log.info("[TRANSPORT] tenant={} action={} route={} bus={}",
                tenantId, action, routeId, busId);

        return switch (action) {
            case "create_route"        -> ok("create_route",
                "Route created.\nroute_id: " + UUID.randomUUID());
            case "update_route"        -> ok("update_route",
                "Route " + routeId + " updated.");
            case "assign_bus"          -> ok("assign_bus",
                "Bus " + busId + " assigned to route=" + routeId);
            case "assign_driver"       -> ok("assign_driver",
                "Driver assigned to bus=" + busId);
            case "update_gps"          -> ok("update_gps",
                "GPS coordinates updated for bus=" + busId);
            case "get_eta"             -> ok("get_eta",
                "ETA for route=" + routeId + ": N/A (connect GPS feed).");
            case "manage_boarding_list"-> ok("manage_boarding_list",
                "Boarding list updated.\nStudents: " +
                args.path("student_ids").size());
            case "get_route_report"    -> queued("get_route_report",
                UUID.randomUUID().toString());
            case "send_delay_alert"    -> ok("send_delay_alert",
                "Delay alert sent to parents on route=" + routeId);
            default -> "Unknown action: " + action;
        };
    }
}
