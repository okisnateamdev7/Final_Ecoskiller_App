import 'package:flutter/material.dart';
import 'package:ecoskiller_mobile_app/core/theme/app_theme.dart';
import 'package:ecoskiller_mobile_app/features/onboarding/presentation/pages/onboarding_page.dart';
import 'package:ecoskiller_mobile_app/core/config/app_config.dart';

void main() {
  // Initialize Environment (Default to dev, can be changed via --dart-define)
  const String env = String.fromEnvironment('ENV', defaultValue: 'dev');
  
  if (env == 'prod') {
    AppConfig.setEnvironment(Environment.prod);
  } else if (env == 'staging') {
    AppConfig.setEnvironment(Environment.staging);
  } else if (env == 'test') {
    AppConfig.setEnvironment(Environment.test);
  } else {
    AppConfig.setEnvironment(Environment.dev);
  }

  runApp(const EcoSkillerApp());
}

class EcoSkillerApp extends StatelessWidget {
  const EcoSkillerApp({super.key});

  @override
  Widget build(BuildContext context) {
    Widget app = MaterialApp(
      title: 'EcoSkiller',
      debugShowCheckedModeBanner: false,
      theme: AppTheme.lightTheme,
      darkTheme: AppTheme.darkTheme,
      themeMode: ThemeMode.system,
      home: const OnboardingPage(),
    );

    // Show environment banner if not in production
    if (AppConfig.showDebugBanner) {
      return Directionality(
        textDirection: TextDirection.ltr,
        child: Banner(
          location: BannerLocation.topEnd,
          message: AppConfig.name,
          color: _getBannerColor(),
          child: app,
        ),
      );
    }

    return app;
  }

  Color _getBannerColor() {
    switch (AppConfig.environment) {
      case Environment.dev:
        return Colors.red;
      case Environment.test:
        return Colors.orange;
      case Environment.staging:
        return Colors.blue;
      default:
        return Colors.grey;
    }
  }
}
