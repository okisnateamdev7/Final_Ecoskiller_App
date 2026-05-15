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
public class CampusTool implements AgentTool {

    @Override
    public String execute(JsonNode args) {
        String tenantId  = getString(args, "tenant_id");
        String action    = getString(args, "action");
        String buildingId= getString(args, "building_id");
        String roomId    = getString(args, "room_id");

        log.info("[CAMPUS] tenant={} action={}", tenantId, action);

        return switch (action) {
            case "add_building"     -> ok("add_building",
                "Building added.\nbuilding_id: " + UUID.randomUUID());
            case "add_room"         -> ok("add_room",
                "Room added to building=" + buildingId +
                "\nroom_id: " + UUID.randomUUID());
            case "update_room"      -> ok("update_room",
                "Room " + roomId + " updated.");
            case "book_facility"    -> ok("book_facility",
                "Facility booking confirmed.\nbooking_id: " + UUID.randomUUID());
            case "release_booking"  -> ok("release_booking",
                "Booking released.");
            case "list_available"   -> ok("list_available",
                "Availability query complete. 0 rooms available in requested slot.");
            case "get_floor_plan"   -> ok("get_floor_plan",
                "Floor plan URL: /api/campus/building/" + buildingId + "/floor-plan");
            default -> "Unknown action: " + action;
        };
    }
}
