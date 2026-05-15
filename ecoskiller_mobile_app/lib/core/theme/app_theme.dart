import 'package:flutter/material.dart';
import 'package:google_fonts/google_fonts.dart';

class AppTheme {
  // --- Light Mode Colors (60-30-10 Rule) ---
  static const Color skyWhite = Color(0xFFF0F7FF);      // 60% - Dominant
  static const Color linkedinBlue = Color(0xFF0A66C2);  // 30% - Structure
  static const Color electricYellow = Color(0xFFFFD600); // 10% - Accent
  
  static const Color deepOcean = Color(0xFF1A2E3D);
  static const Color tealTrust = Color(0xFF00BFA5);
  static const Color lightBlueCard = Color(0xFFE3F2FD);

  // --- Dark Mode Colors ---
  static const Color oceanDark = Color(0xFF020E1A);
  static const Color midnightAbyss = Color(0xFF060A10);
  static const Color crispText = Color(0xFFE8F4FF);

  static ThemeData get lightTheme {
    return ThemeData(
      useMaterial3: true,
      scaffoldBackgroundColor: skyWhite,
      colorScheme: ColorScheme.fromSeed(
        seedColor: linkedinBlue,
        primary: linkedinBlue,
        secondary: tealTrust,
        tertiary: electricYellow,
        surface: Colors.white,
        brightness: Brightness.light,
      ),
      textTheme: GoogleFonts.interTextTheme().copyWith(
        displayLarge: GoogleFonts.inter(color: deepOcean, fontWeight: FontWeight.bold),
        displayMedium: GoogleFonts.inter(color: deepOcean, fontWeight: FontWeight.bold),
        headlineMedium: GoogleFonts.inter(color: deepOcean, fontWeight: FontWeight.w600),
      ),
      elevatedButtonTheme: ElevatedButtonThemeData(
        style: ElevatedButton.styleFrom(
          backgroundColor: linkedinBlue,
          foregroundColor: Colors.white,
          minimumSize: const Size(double.infinity, 56),
          shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(12)),
          elevation: 0,
        ),
      ),
      inputDecorationTheme: InputDecorationTheme(
        filled: true,
        fillColor: Colors.white,
        border: OutlineInputBorder(
          borderRadius: BorderRadius.circular(12),
          borderSide: BorderSide(color: linkedinBlue.withOpacity(0.1)),
        ),
        enabledBorder: OutlineInputBorder(
          borderRadius: BorderRadius.circular(12),
          borderSide: BorderSide(color: linkedinBlue.withOpacity(0.1)),
        ),
        focusedBorder: OutlineInputBorder(
          borderRadius: BorderRadius.circular(12),
          borderSide: const BorderSide(color: linkedinBlue, width: 1.5),
        ),
      ),
      cardTheme: CardThemeData(
        color: lightBlueCard,
        elevation: 0,
        shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(16)),
      ),
    );
  }

  static ThemeData get darkTheme {
    return ThemeData(
      useMaterial3: true,
      scaffoldBackgroundColor: midnightAbyss,
      colorScheme: ColorScheme.fromSeed(
        seedColor: linkedinBlue,
        primary: linkedinBlue,
        secondary: tealTrust,
        tertiary: electricYellow,
        surface: oceanDark,
        brightness: Brightness.dark,
      ),
      textTheme: GoogleFonts.interTextTheme(ThemeData.dark().textTheme).copyWith(
        displayLarge: GoogleFonts.inter(color: crispText, fontWeight: FontWeight.bold),
        bodyLarge: GoogleFonts.inter(color: crispText.withOpacity(0.9)),
      ),
      elevatedButtonTheme: ElevatedButtonThemeData(
        style: ElevatedButton.styleFrom(
          backgroundColor: electricYellow,
          foregroundColor: midnightAbyss,
          minimumSize: const Size(double.infinity, 56),
          shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(12)),
          textStyle: const TextStyle(fontWeight: FontWeight.bold),
        ),
      ),
      inputDecorationTheme: InputDecorationTheme(
        filled: true,
        fillColor: oceanDark,
        border: OutlineInputBorder(
          borderRadius: BorderRadius.circular(12),
          borderSide: BorderSide.none,
        ),
        focusedBorder: OutlineInputBorder(
          borderRadius: BorderRadius.circular(12),
          borderSide: const BorderSide(color: electricYellow, width: 1.5),
        ),
      ),
    );
  }
}
