import 'package:flutter/material.dart';
import 'package:google_fonts/google_fonts.dart';
import 'package:lucide_icons/lucide_icons.dart';
import 'package:animate_do/animate_do.dart';
import 'package:ecoskiller_mobile_app/core/theme/app_theme.dart';
import 'package:ecoskiller_mobile_app/features/auth/data/providers/auth_service.dart';
import 'package:ecoskiller_mobile_app/core/utils/dashboard_router.dart';

class SignupPage extends StatefulWidget {
  const SignupPage({super.key});

  @override
  State<SignupPage> createState() => _SignupPageState();
}

enum AuthStep { form, scanning, verifying, complete }

class _SignupPageState extends State<SignupPage> {
  bool isLoading = false;
  AuthStep authStep = AuthStep.form;
  
  final TextEditingController _nameController = TextEditingController();
  final TextEditingController _emailController = TextEditingController();
  final TextEditingController _passwordController = TextEditingController();
  final TextEditingController _confirmPasswordController = TextEditingController();
  String _selectedRole = 'STUDENT';
  bool _obscurePassword = true;
  bool _obscureConfirmPassword = true;

  void _handleSignup() async {
    if (_nameController.text.isEmpty || _emailController.text.isEmpty || _passwordController.text.isEmpty) {
      _showError('Full Neural Data Required for Onboarding.');
      return;
    }

    if (_passwordController.text != _confirmPasswordController.text) {
      _showError('Passwords do not match. Neural verification failed.');
      return;
    }

    setState(() {
      isLoading = true;
      authStep = AuthStep.scanning;
    });

    // UX Simulation: DNA Scan Phase
    await Future.delayed(const Duration(milliseconds: 1500));
    
    setState(() {
      authStep = AuthStep.verifying;
    });

    // UX Simulation: Orchestrator Verification Phase
    await Future.delayed(const Duration(milliseconds: 1500));

    final result = await AuthService().signup(
      name: _nameController.text,
      email: _emailController.text,
      password: _passwordController.text,
      role: _selectedRole,
    );

    if (!mounted) return;

    if (result['status'] == 'success') {
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(
          content: Text("Identity Created. Synced to Orchestrator."),
          backgroundColor: AppTheme.tealTrust,
        ),
      );
      await Future.delayed(const Duration(seconds: 1));
      if (mounted) {
        Navigator.pop(context);
      }
    } else {
      // Improved error message display
      String errorMessage = result['detail'] ?? result['message'] ?? "Identity Conflict Detected.";
      _showError(errorMessage);
      setState(() => authStep = AuthStep.form);
    }

    setState(() {
      isLoading = false;
    });
  }

  void _showError(String message) {
    ScaffoldMessenger.of(context).showSnackBar(
      SnackBar(
        content: Text(message),
        backgroundColor: Colors.redAccent,
      ),
    );
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
              _buildErosLogo(),
              const SizedBox(height: 12),
              Text(
                'ECOSKILLER',
                style: GoogleFonts.inter(
                  color: isDark ? AppTheme.crispText : AppTheme.deepOcean,
                  fontSize: 24,
                  fontWeight: FontWeight.w900,
                  letterSpacing: 4,
                ),
              ),
              const SizedBox(height: 8),
              Text(
                'Create your sovereign identity',
                style: GoogleFonts.inter(
                  color: (isDark ? AppTheme.crispText : AppTheme.deepOcean).withOpacity(0.6),
                  fontSize: 14,
                  fontWeight: FontWeight.w500,
                ),
              ),
              const SizedBox(height: 48),
              
              _buildTabSwitcher(false),
              const SizedBox(height: 48),

              AnimatedSwitcher(
                duration: const Duration(milliseconds: 500),
                child: authStep == AuthStep.form 
                  ? Column(
                      key: const ValueKey('signup_form'),
                      children: [
                        _buildFormFields(),
                        const SizedBox(height: 32),
                        _buildActionBtn('GENERATE DNA PROFILE'),
                      ],
                    )
                  : _buildScanningView(),
              ),
              const SizedBox(height: 32),
            ],
          ),
        ),
      ),
    );
  }

  Widget _buildErosLogo() {
    bool isDark = Theme.of(context).brightness == Brightness.dark;
    return Container(
      width: 80,
      height: 80,
      decoration: BoxDecoration(
        shape: BoxShape.circle,
        color: isDark ? AppTheme.oceanDark : Colors.white,
        boxShadow: [
          BoxShadow(
            color: AppTheme.linkedinBlue.withOpacity(0.2),
            blurRadius: 30,
            spreadRadius: 2,
          ),
        ],
        border: Border.all(color: AppTheme.linkedinBlue.withOpacity(0.1), width: 1),
      ),
      child: const Center(
        child: Text(
          'E',
          style: TextStyle(
            color: AppTheme.linkedinBlue,
            fontSize: 36,
            fontWeight: FontWeight.w900,
            letterSpacing: -1,
          ),
        ),
      ),
    );
  }

  Widget _buildTabSwitcher(bool isLogin) {
    return Container(
      padding: const EdgeInsets.all(6),
      decoration: BoxDecoration(
        color: Theme.of(context).brightness == Brightness.dark ? AppTheme.oceanDark : Colors.white,
        borderRadius: BorderRadius.circular(16),
      ),
      child: Row(
        children: [
          Expanded(
            child: _buildTab('Sign In', isLogin, () {
              Navigator.pop(context);
            }),
          ),
          Expanded(
            child: _buildTab('Sign Up', !isLogin, () {}),
          ),
        ],
      ),
    );
  }

  Widget _buildTab(String title, bool active, VoidCallback onTap) {
    return GestureDetector(
      onTap: onTap,
      child: Container(
        padding: const EdgeInsets.symmetric(vertical: 14),
        decoration: BoxDecoration(
          color: active ? AppTheme.linkedinBlue : Colors.transparent,
          borderRadius: BorderRadius.circular(12),
        ),
        child: Center(
          child: Text(
            title,
            style: GoogleFonts.inter(
              color: active ? Colors.white : Colors.grey.withOpacity(0.5),
              fontSize: 14,
              fontWeight: FontWeight.bold,
            ),
          ),
        ),
      ),
    );
  }

  Widget _buildFormFields() {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        _buildLabel('FULL NAME'),
        const SizedBox(height: 8),
        _buildInput(LucideIcons.user, 'Enter your name', _nameController),
        const SizedBox(height: 24),
        _buildLabel('EMAIL ADDRESS'),
        const SizedBox(height: 8),
        _buildInput(LucideIcons.mail, 'Enter your email', _emailController),
        const SizedBox(height: 24),
        _buildLabel('PASSWORD'),
        const SizedBox(height: 8),
        _buildInput(
          LucideIcons.lock, 
          'Enter your password', 
          _passwordController, 
          isPassword: _obscurePassword,
          suffixIcon: _obscurePassword ? LucideIcons.eyeOff : LucideIcons.eye,
          onSuffixTap: () {
            setState(() {
              _obscurePassword = !_obscurePassword;
            });
          },
        ),
        const SizedBox(height: 24),
        _buildLabel('CONFIRM PASSWORD'),
        const SizedBox(height: 8),
        _buildInput(
          LucideIcons.shieldCheck, 
          'Repeat password', 
          _confirmPasswordController, 
          isPassword: _obscureConfirmPassword,
          suffixIcon: _obscureConfirmPassword ? LucideIcons.eyeOff : LucideIcons.eye,
          onSuffixTap: () {
            setState(() {
              _obscureConfirmPassword = !_obscureConfirmPassword;
            });
          },
        ),
        const SizedBox(height: 24),
        _buildLabel('IDENTITY PERSONA TYPE'),
        const SizedBox(height: 8),
        _buildDropdown(),
      ],
    );
  }

  Widget _buildLabel(String text) {
    return Text(
      text,
      style: GoogleFonts.inter(
        color: AppTheme.linkedinBlue.withOpacity(0.6),
        fontSize: 10,
        fontWeight: FontWeight.w900,
        letterSpacing: 1,
      ),
    );
  }

  Widget _buildInput(IconData icon, String hint, TextEditingController controller, {bool isPassword = false, IconData? suffixIcon, VoidCallback? onSuffixTap}) {
    bool isDark = Theme.of(context).brightness == Brightness.dark;
    return Container(
      decoration: BoxDecoration(
        color: isDark ? AppTheme.oceanDark : Colors.white,
        borderRadius: BorderRadius.circular(16),
        border: Border.all(color: AppTheme.linkedinBlue.withOpacity(0.1)),
      ),
      child: TextField(
        controller: controller,
        obscureText: isPassword,
        style: TextStyle(color: isDark ? AppTheme.crispText : AppTheme.deepOcean, fontSize: 15),
        decoration: InputDecoration(
          prefixIcon: Icon(icon, color: AppTheme.linkedinBlue.withOpacity(0.4), size: 20),
          suffixIcon: suffixIcon != null ? GestureDetector(
            onTap: onSuffixTap,
            child: Icon(suffixIcon, color: AppTheme.linkedinBlue.withOpacity(0.4), size: 18),
          ) : null,
          hintText: hint,
          hintStyle: TextStyle(color: Colors.grey.withOpacity(0.3)),
          filled: false,
          border: InputBorder.none,
          contentPadding: const EdgeInsets.symmetric(vertical: 14, horizontal: 16),
        ),
      ),
    );
  }

  Widget _buildDropdown() {
    bool isDark = Theme.of(context).brightness == Brightness.dark;
    return Container(
      padding: const EdgeInsets.symmetric(horizontal: 16),
      decoration: BoxDecoration(
        color: isDark ? AppTheme.oceanDark : Colors.white,
        borderRadius: BorderRadius.circular(16),
        border: Border.all(color: AppTheme.linkedinBlue.withOpacity(0.1)),
      ),
      child: DropdownButtonHideUnderline(
        child: DropdownButton<String>(
          value: _selectedRole,
          dropdownColor: isDark ? AppTheme.oceanDark : Colors.white,
          icon: Icon(LucideIcons.chevronDown, color: AppTheme.linkedinBlue.withOpacity(0.4)),
          style: TextStyle(color: isDark ? AppTheme.crispText : AppTheme.deepOcean, fontSize: 14, fontWeight: FontWeight.bold),
          isExpanded: true,
          onChanged: (v) => setState(() => _selectedRole = v!),
          items: [
            'CANDIDATE', 'CORPORATE', 'INSTITUTE', 'MENTOR', 'PARENT', 
            'RECRUITER', 'SCHOOL', 'STUDENT', 'TRAINER', 'VENDOR'
          ].map((r) => DropdownMenuItem(value: r, child: Text(r))).toList(),
        ),
      ),
    );
  }

  Widget _buildActionBtn(String text) {
    return GestureDetector(
      onTap: _handleSignup,
      child: Container(
        width: double.infinity,
        padding: const EdgeInsets.symmetric(vertical: 20),
        decoration: BoxDecoration(
          color: AppTheme.linkedinBlue,
          borderRadius: BorderRadius.circular(20),
        ),
        child: Row(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            Text(
              '$text  →',
              style: GoogleFonts.inter(
                color: Colors.white,
                fontWeight: FontWeight.w900,
                fontSize: 14,
                letterSpacing: 1,
              ),
            ),
          ],
        ),
      ),
    );
  }

  Widget _buildScanningView() {
    return Column(
      key: const ValueKey('scanning'),
      children: [
        const SizedBox(height: 40),
        FadeIn(
          duration: const Duration(seconds: 1),
          child: Container(
            padding: const EdgeInsets.all(24),
            decoration: BoxDecoration(
              shape: BoxShape.circle,
              border: Border.all(color: AppTheme.linkedinBlue.withOpacity(0.3), width: 2),
            ),
            child: const CircularProgressIndicator(
              valueColor: AlwaysStoppedAnimation<Color>(AppTheme.linkedinBlue),
              strokeWidth: 2,
            ),
          ),
        ),
        const SizedBox(height: 32),
        Text(
          authStep == AuthStep.scanning ? 'BIO-SCANNING IDENTITY...' : 'NEURAL SYNC VERIFICATION...',
          style: GoogleFonts.inter(
            color: AppTheme.linkedinBlue,
            fontSize: 12,
            fontWeight: FontWeight.bold,
            letterSpacing: 2,
          ),
        ),
        const SizedBox(height: 12),
        const Text(
          'Connecting to Orchestrator...',
          style: TextStyle(color: Colors.grey, fontSize: 10),
        ),
      ],
    );
  }
}
