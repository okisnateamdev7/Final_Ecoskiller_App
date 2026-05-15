import 'package:flutter/material.dart';
import '../../features/student/presentation/pages/student_dashboard_page.dart';
import '../../features/candidate/presentation/pages/candidate_dashboard_page.dart';
import '../../features/corporate/presentation/pages/corporate_dashboard_page.dart';
import '../../features/institute/presentation/pages/institute_dashboard_page.dart';
import '../../features/mentor/presentation/pages/mentor_dashboard_page.dart';
import '../../features/parent/presentation/pages/parent_dashboard_page.dart';
import '../../features/recruiter/presentation/pages/recruiter_dashboard_page.dart';
import '../../features/school/presentation/pages/school_dashboard_page.dart';
import '../../features/trainer/presentation/pages/trainer_dashboard_page.dart';
import '../../features/vendor/presentation/pages/vendor_dashboard_page.dart';

class DashboardRouter {
  static Widget getDashboardForRole(String role, Map<String, dynamic> user) {
    switch (role.toUpperCase()) {
      case 'CANDIDATE': return CandidateDashboardPage(user: user);
      case 'CORPORATE': return CorporateDashboardPage(user: user);
      case 'INSTITUTE': return InstituteDashboardPage(user: user);
      case 'MENTOR': return MentorDashboardPage(user: user);
      case 'PARENT': return ParentDashboardPage(user: user);
      case 'RECRUITER': return RecruiterDashboardPage(user: user);
      case 'SCHOOL': return SchoolDashboardPage(user: user);
      case 'TRAINER': return TrainerDashboardPage(user: user);
      case 'VENDOR': return VendorDashboardPage(user: user);
      case 'STUDENT':
      default:
        return StudentDashboardPage(user: user);
    }
  }
}
