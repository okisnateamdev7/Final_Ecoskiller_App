import 'package:flutter/foundation.dart';
import 'package:ecoskiller_mobile_app/core/config/app_config.dart';

class AppLogger {
  static void log(String message, {String? tag}) {
    if (AppConfig.enableLogging) {
      if (kDebugMode) {
        print('[${AppConfig.name}]${tag != null ? ' [$tag]' : ''}: $message');
      }
    }
  }

  static void error(String message, {dynamic error, StackTrace? stackTrace}) {
    if (AppConfig.enableLogging) {
      if (kDebugMode) {
        print('❌ [${AppConfig.name}] ERROR: $message');
        if (error != null) print('Error Details: $error');
        if (stackTrace != null) print('Stacktrace: $stackTrace');
      }
    }
  }

  static void debug(String message) {
    log('DEBUG: $message');
  }
}
