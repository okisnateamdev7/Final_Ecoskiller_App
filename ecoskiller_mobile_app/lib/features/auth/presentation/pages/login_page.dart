import 'package:flutter/material.dart';
import 'package:google_fonts/google_fonts.dart';
import 'package:lucide_icons/lucide_icons.dart';
import 'package:animate_do/animate_do.dart';
import 'package:ecoskiller_mobile_app/core/theme/app_theme.dart';
import 'package:ecoskiller_mobile_app/features/auth/data/providers/auth_service.dart';
import 'signup_page.dart';
import 'forgot_password_page.dart';
import '../../../student/presentation/pages/student_dashboard_page.dart';

class LoginPage extends StatefulWidget {
  const LoginPage({super.key});

  @override
  State<LoginPage> createState() => _LoginPageState();
}

enum AuthStep { form, scanning, verifying }

class _LoginPageState extends State<LoginPage> {
  bool isLoading = false;
  AuthStep authStep = AuthStep.form;
  
  final TextEditingController _emailController = TextEditingController();
  final TextEditingController _passwordController = TextEditingController();
  String _selectedRole = 'STUDENT';
  bool _obscurePassword = true;

  void _handleLogin() async {
    if (_emailController.text.isEmpty || _passwordController.text.isEmpty) {
      _showError("Identity Credentials Required.");
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

    final result = await AuthService().login(
      email: _emailController.text,
      password: _passwordController.text,
    );

    if (!mounted) return;

    if (result['status'] == 'success') {
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(
          content: Text("Welcome back, ${result['user']['name']}"),
          backgroundColor: AppTheme.tealTrust,
        ),
      );
      
      Navigator.pushAndRemoveUntil(
        context,
        MaterialPageRoute(
          builder: (context) => StudentDashboardPage(user: result['user']),
        ),
        (route) => false,
      );
    } else {
      // Improved error message display
      String errorMessage = result['detail'] ?? result['message'] ?? "Neural Sync Failure.";
      _showError(errorMessage);
    }

    setState(() {
      authStep = AuthStep.form;
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
                'Sign in to your sovereign node',
                style: GoogleFonts.inter(
                  color: (isDark ? AppTheme.crispText : AppTheme.deepOcean).withOpacity(0.6),
                  fontSize: 14,
                  fontWeight: FontWeight.w500,
                ),
              ),
              const SizedBox(height: 48),
              
              _buildTabSwitcher(true),
              const SizedBox(height: 48),

              AnimatedSwitcher(
                duration: const Duration(milliseconds: 500),
                child: authStep == AuthStep.form 
                  ? Column(
                      key: const ValueKey('form'),
                      children: [
                        _buildFormFields(),
                        const SizedBox(height: 32),
                        _buildActionBtn('INITIATE SESSION'),
                        Align(
                          alignment: Alignment.centerRight,
                          child: TextButton(
                            onPressed: () {
                              Navigator.push(
                                context,
                                MaterialPageRoute(builder: (context) => const ForgotPasswordPage()),
                              );
                            },
                            child: const Text("Forgot Password?", style: TextStyle(color: AppTheme.linkedinBlue)),
                          ),
                        ),
                        const SizedBox(height: 32),
                        _buildFooterLink(),
                      ],
                    )
                  : _buildScanningView(),
              ),
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
            child: _buildTab('Sign In', isLogin, () {}),
          ),
          Expanded(
            child: _buildTab('Sign Up', !isLogin, () {
              Navigator.push(
                context,
                MaterialPageRoute(builder: (context) => const SignupPage()),
              );
            }),
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
      onTap: _handleLogin,
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

  Widget _buildFooterLink() {
    return GestureDetector(
      onTap: () {
        Navigator.push(
          context,
          MaterialPageRoute(builder: (context) => const SignupPage()),
        );
      },
      child: RichText(
        text: TextSpan(
          style: GoogleFonts.inter(color: Colors.grey, fontSize: 13),
          children: [
            const TextSpan(text: 'New to Ecoskiller? '),
            const TextSpan(
              text: 'Create Identity',
              style: TextStyle(color: AppTheme.linkedinBlue, fontWeight: FontWeight.bold),
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
