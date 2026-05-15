enum Environment { dev, test, staging, prod }

class AppConfig {
  static Environment _environment = Environment.dev;

  static void setEnvironment(Environment env) {
    _environment = env;
  }

  static Environment get environment => _environment;

  static String get baseUrl {
    switch (_environment) {
      case Environment.dev:
        return 'http://13.234.251.210/api/auth';
      case Environment.test:
        return 'http://test-api.ecoskiller.com:8001';
      case Environment.staging:
        return 'http://staging-api.ecoskiller.com:8001';
      case Environment.prod:
        return 'https://api.ecoskiller.com';
      default:
        return 'http://13.235.4.150:8001';
    }
  }

  static String get name {
    return _environment.toString().split('.').last.toUpperCase();
  }

  static bool get showDebugBanner {
    return _environment != Environment.prod;
  }

  static bool get enableLogging {
    return _environment == Environment.dev || _environment == Environment.test;
  }
}
