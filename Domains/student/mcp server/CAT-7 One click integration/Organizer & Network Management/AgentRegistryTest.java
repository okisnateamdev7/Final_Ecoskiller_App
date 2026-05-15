package com.ecoskiller.mcp26;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CAT-26 — All 28 Agent Tests")
class AgentRegistryTest {

    private static AgentRegistry registry;

    @BeforeAll
    static void setup() { registry = new AgentRegistry(); }

    @ParameterizedTest(name = "[{index}] {0}")
    @MethodSource("agentCases")
    void testAgent(String toolName, String argsJson) throws Exception {
        String result = registry.invoke(toolName, argsJson);
        assertNotNull(result,           "Result must not be null  → " + toolName);
        assertFalse(result.isBlank(),   "Result must not be blank → " + toolName);
        assertFalse(result.contains("\"error\""), "Agent returned error → " + toolName + " : " + result);
        // every successful result must contain the tool name fragment
        assertTrue(result.toUpperCase().contains(toolName.replace("_","").toUpperCase().substring(0,6)),
                   "Agent field missing in result → " + result);
    }

    static Stream<Object[]> agentCases() {
        return Stream.of(
            tc("user_registration",
               arg("user_id","U001","username","alice","email","alice@eco.in","phone","9876543210","role","STUDENT")),
            tc("kyc_verification",
               arg("user_id","U001","document_type","AADHAAR","document_number","123456789012","submitted_by","ADMIN")),
            tc("coordinator_onboarding",
               arg("coordinator_id","C001","full_name","Ravi Kumar","region","Maharashtra","district","Pune","assigned_schools","S1,S2")),
            tc("master_organizer_onboarding",
               arg("organizer_id","MO01","full_name","Priya Sharma","jurisdiction","West Zone","contact_email","priya@eco.in","access_tier","PLATINUM")),
            tc("rural_block_onboarding",
               arg("block_id","BLK001","block_name","Khed Block","state","Maharashtra","district","Pune","coordinator_id","C001")),
            tc("role_assignment",
               arg("user_id","U001","new_role","COORDINATOR","assigned_by","ADMIN","reason","Promotion")),
            tc("household_id_linking",
               arg("user_id","U001","household_id","HH-001","relation","CHILD","head_of_household","U002")),
            tc("society_mapping",
               arg("society_id","SOC01","society_name","Green Valley","zone","Zone-A","total_members","120","coordinator_id","C001")),
            tc("resource_allocation",
               arg("entity_id","C001","entity_type","COORDINATOR","resource_type","BUDGET","quantity","5000","allocated_by","ADMIN")),
            tc("tournament_management",
               arg("tournament_id","T001","title","Coding Olympiad","skill_category","TECH","start_date","2025-07-01","end_date","2025-07-07","max_participants","500")),
            tc("event_calendar_sync",
               arg("event_id","EV001","event_title","Annual Skill Fest","event_date","2025-08-15","event_type","EXPOSITION","sync_target","GOOGLE_CALENDAR")),
            tc("exposition_management",
               arg("exposition_id","EX01","theme","Rural Innovation","venue","Pune Pavilion","organizer_id","MO01","scheduled_date","2025-09-20")),
            tc("workshop_creation",
               arg("workshop_id","W001","title","Python Basics","skill_domain","CODING","trainer_id","TR01","duration_hours","20","max_seats","30")),
            tc("workshop_enrollment",
               arg("enrollment_id","EN001","workshop_id","W001","user_id","U001","enrollment_type","SELF")),
            tc("workshop_attendance_tracking",
               arg("session_id","SES001","workshop_id","W001","user_id","U001","check_in_time","09:00","check_out_time","17:00")),
            tc("trainer_assignment",
               arg("assignment_id","TA001","trainer_id","TR01","workshop_id","W001","skill_area","PYTHON","session_dates","2025-07-01,2025-07-02")),
            tc("skill_category_configuration",
               arg("category_id","SC001","category_name","Digital Literacy","parent_category","TECHNOLOGY","level","2","is_assessable","true")),
            tc("digital_badge_issuance",
               arg("badge_id","B001","user_id","U001","skill","Python","badge_tier","GOLD","issuer_id","MO01")),
            tc("certificate_generation",
               arg("certificate_id","CERT001","user_id","U001","course_name","Full Stack Dev","completion_date","2025-06-30","authority","EcoSkiller Board")),
            tc("commission_distribution_engine",
               arg("transaction_id","TXN001","total_amount","10000","organizer_id","MO01","network_tier","3","currency","INR")),
            tc("payment_gateway_integration",
               arg("payment_id","PAY001","amount","5000","currency","INR","gateway","razorpay","payer_id","U001")),
            tc("revenue_split_automation",
               arg("revenue_event_id","REV001","gross_revenue","20000","split_policy","70_20_10","participants","U001,MO01")),
            tc("cash_flow_stability",
               arg("organizer_id","MO01","period","2025-Q2","inflow","150000","outflow","80000","reserve_threshold","50000")),
            tc("incentive_bonus_calculation",
               arg("user_id","C001","role","COORDINATOR","base_amount","20000","performance_score","88","period","2025-Q2")),
            tc("wallet_balance_tracking",
               arg("wallet_id","WAL001","user_id","U001","transaction_type","CREDIT","amount","1000","description","Workshop refund")),
            tc("refund_processing",
               arg("refund_id","REF001","original_payment_id","PAY001","user_id","U001","reason","duplicate payment","amount","5000")),
            tc("performance_scoring",
               arg("entity_id","C001","entity_type","COORDINATOR","metric_data","{}","scoring_model","BALANCED","period","2025-Q2")),
            tc("longitudinal_impact_analytics",
               arg("report_id","RPT001","cohort_id","COHORT-2024","start_date","2024-01-01","end_date","2025-12-31","dimensions","income,employment,skill"))
        );
    }

    private static Object[] tc(String tool, String args) { return new Object[]{tool, args}; }

    private static String arg(String... kvPairs) {
        StringBuilder sb = new StringBuilder("{");
        for (int i = 0; i < kvPairs.length - 1; i += 2) {
            if (i > 0) sb.append(",");
            sb.append("\"").append(kvPairs[i]).append("\":\"").append(kvPairs[i+1]).append("\"");
        }
        return sb.append("}").toString();
    }
}
