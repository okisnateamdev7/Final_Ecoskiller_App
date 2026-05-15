import 'package:flutter/material.dart';
import 'package:google_fonts/google_fonts.dart';
import 'package:lucide_icons/lucide_icons.dart';
import 'package:animate_do/animate_do.dart';
import 'package:ecoskiller_mobile_app/core/theme/app_theme.dart';

class ForgotPasswordPage extends StatefulWidget {
  const ForgotPasswordPage({super.key});

  @override
  State<ForgotPasswordPage> createState() => _ForgotPasswordPageState();
}

class _ForgotPasswordPageState extends State<ForgotPasswordPage> {
  final TextEditingController _emailController = TextEditingController();
  bool _isLoading = false;

  void _handleReset() async {
    if (_emailController.text.isEmpty) {
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(content: Text("Enter Identity Email for Recovery.")),
      );
      return;
    }

    setState(() => _isLoading = true);
    // Simulate API call
    await Future.delayed(const Duration(seconds: 2));
    setState(() => _isLoading = false);

    if (mounted) {
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(
          content: Text("Recovery link transmitted to your node."),
          backgroundColor: AppTheme.tealTrust,
        ),
      );
      Navigator.pop(context);
    }
  }

  @override
  Widget build(BuildContext context) {
    bool isDark = Theme.of(context).brightness == Brightness.dark;

    return Scaffold(
      backgroundColor: isDark ? AppTheme.midnightAbyss : AppTheme.skyWhite,
      body: SafeArea(
        child: SingleChildScrollView(
          padding: const EdgeInsets.symmetric(horizontal: 24, vertical: 40),
          child: Column(
            children: [
              Align(
                alignment: Alignment.topLeft,
                child: IconButton(
                  onPressed: () => Navigator.pop(context),
                  icon: Icon(LucideIcons.arrowLeft, color: isDark ? AppTheme.crispText : AppTheme.deepOcean),
                ),
              ),
              const SizedBox(height: 20),
              Container(
                width: 80,
                height: 80,
                decoration: BoxDecoration(
                  shape: BoxShape.circle,
                  color: isDark ? AppTheme.oceanDark : Colors.white,
                  border: Border.all(color: AppTheme.electricYellow.withOpacity(0.3), width: 1),
                ),
                child: const Center(
                  child: Icon(LucideIcons.key, color: AppTheme.electricYellow, size: 36),
                ),
              ),
              const SizedBox(height: 24),
              Text(
                'RECOVERY MODE',
                style: GoogleFonts.inter(
                  color: isDark ? AppTheme.crispText : AppTheme.deepOcean,
                  fontSize: 24,
                  fontWeight: FontWeight.w900,
                  letterSpacing: 4,
                ),
              ),
              const SizedBox(height: 12),
              Padding(
                padding: const EdgeInsets.symmetric(horizontal: 20),
                child: Text(
                  'Initiate neural reset protocol for your sovereign identity.',
                  textAlign: TextAlign.center,
                  style: GoogleFonts.inter(
                    color: (isDark ? AppTheme.crispText : AppTheme.deepOcean).withOpacity(0.6),
                    fontSize: 14,
                    fontWeight: FontWeight.w500,
                  ),
                ),
              ),
              const SizedBox(height: 60),
              _buildLabel('REGISTERED EMAIL'),
              const SizedBox(height: 8),
              _buildInput(LucideIcons.mail, 'Enter your identity email', _emailController),
              const SizedBox(height: 40),
              _buildActionBtn(_isLoading ? 'TRANSMITTING...' : 'INITIATE RESET'),
            ],
          ),
        ),
      ),
    );
  }

  Widget _buildLabel(String text) {
    return Align(
      alignment: Alignment.centerLeft,
      child: Text(
        text,
        style: GoogleFonts.inter(
          color: AppTheme.linkedinBlue.withOpacity(0.6),
          fontSize: 10,
          fontWeight: FontWeight.w900,
          letterSpacing: 1,
        ),
      ),
    );
  }

  Widget _buildInput(IconData icon, String hint, TextEditingController controller) {
    bool isDark = Theme.of(context).brightness == Brightness.dark;
    return Container(
      decoration: BoxDecoration(
        color: isDark ? AppTheme.oceanDark : Colors.white,
        borderRadius: BorderRadius.circular(16),
        border: Border.all(color: AppTheme.linkedinBlue.withOpacity(0.1)),
      ),
      child: TextField(
        controller: controller,
        style: TextStyle(color: isDark ? AppTheme.crispText : AppTheme.deepOcean, fontSize: 15),
        decoration: InputDecoration(
          prefixIcon: Icon(icon, color: AppTheme.linkedinBlue.withOpacity(0.4), size: 20),
          hintText: hint,
          hintStyle: TextStyle(color: Colors.grey.withOpacity(0.3)),
          filled: false,
          border: InputBorder.none,
          contentPadding: const EdgeInsets.symmetric(vertical: 14, horizontal: 16),
        ),
      ),
    );
  }

  Widget _buildActionBtn(String text) {
    return GestureDetector(
      onTap: _isLoading ? null : _handleReset,
      child: Container(
        width: double.infinity,
        padding: const EdgeInsets.symmetric(vertical: 20),
        decoration: BoxDecoration(
          color: AppTheme.electricYellow,
          borderRadius: BorderRadius.circular(20),
          boxShadow: [
            BoxShadow(
              color: AppTheme.electricYellow.withOpacity(0.2),
              blurRadius: 20,
              spreadRadius: 2,
            ),
          ],
        ),
        child: Center(
          child: Text(
            text,
            style: GoogleFonts.inter(
              color: AppTheme.midnightAbyss,
              fontWeight: FontWeight.w900,
              fontSize: 14,
              letterSpacing: 1,
            ),
          ),
        ),
      ),
    );
  }
}
