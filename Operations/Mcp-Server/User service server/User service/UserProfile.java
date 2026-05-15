package com.ecoskiller.userservice.model;

import java.time.Instant;
import java.util.*;

/**
 * UserProfile — Ecoskiller user-service domain model.
 *
 * Covers all user types from the documentation:
 *   CANDIDATE, RECRUITER, MENTOR, ADMIN, TRAINER, EVALUATOR, PARENT
 *
 * Data tables represented:
 *   users, user_profiles, user_skills, user_preferences,
 *   profile_completions, user_goals, profile_showcases,
 *   daily_streaks, user_scenario_unlocks
 *
 * Security: tenant isolation (tenantId), domain-track isolation,
 *           soft-delete only (archivedAt), RLS enforcement at model level.
 */
public class UserProfile {

    // ── Enums ─────────────────────────────────────────────────────────────────

    public enum UserType {
        CANDIDATE, RECRUITER, MENTOR, ADMIN, TRAINER, EVALUATOR, PARENT
    }

    public enum DomainTrack {
        ARTS, COMMERCE, SCIENCE, TECHNOLOGY, ADMINISTRATION
    }

    public enum SkillLevel {
        BEGINNER, INTERMEDIATE, EXPERT
    }

    public enum PrivacyLevel {
        PUBLIC, RECRUITER_VISIBLE, PRIVATE
    }

    public enum GoalType {
        MATCH_COUNT, SKILL_CERT, INTERVIEW_PASS
    }

    // ── Core identity (users table — immutable) ───────────────────────────────
    private final String  id;
    private final String  tenantId;       // RLS boundary — never null
    private final String  email;
    private final UserType userType;
    private final Instant createdAt;

    // ── Profile (user_profiles table) ─────────────────────────────────────────
    private String  fullName;
    private String  bio;
    private DomainTrack domainTrack;
    private String  avatarUrl;
    private String  educationJson;        // structured JSON column
    private String  certificationsJson;
    private String  experienceJson;
    private Instant updatedAt;
    private Instant archivedAt;           // soft delete — never hard delete

    // ── Skills (user_skills table) ────────────────────────────────────────────
    private final List<Skill>   skills        = new ArrayList<>();

    // ── Preferences (user_preferences table) ──────────────────────────────────
    private String  activeThemeId     = "default";
    private boolean notificationEmail  = true;
    private boolean notificationSms    = false;
    private boolean notificationPush   = true;
    private String  languageCode       = "en";
    private String  timezone           = "Asia/Kolkata";
    private PrivacyLevel privacyLevel  = PrivacyLevel.RECRUITER_VISIBLE;

    // ── Profile completion (profile_completions table) ────────────────────────
    private int     completionPercentage = 0;
    private boolean educationComplete    = false;
    private boolean experienceComplete   = false;
    private boolean skillsComplete       = false;
    private boolean verifiedEmail        = false;
    private boolean verifiedPhone        = false;

    // ── Gamification ──────────────────────────────────────────────────────────
    private int     currentStreakDays  = 0;
    private int     longestStreakDays  = 0;
    private final List<String>      unlockedScenarios  = new ArrayList<>();
    private final List<UserGoal>    goals              = new ArrayList<>();
    private final List<String>      showcaseAchievements = new ArrayList<>(); // max 3
    private final List<String>      unlockedThemes     = new ArrayList<>();
    private final List<String>      badges             = new ArrayList<>();

    // ── Audit log ─────────────────────────────────────────────────────────────
    private final List<AuditEntry>  auditLog = new ArrayList<>();

    // ── Constructor ───────────────────────────────────────────────────────────

    public UserProfile(String id, String tenantId, String email, UserType userType) {
        if (id == null || id.isBlank())       throw new IllegalArgumentException("id required");
        if (tenantId == null || tenantId.isBlank()) throw new IllegalArgumentException("tenantId required (RLS)");
        if (email == null || email.isBlank()) throw new IllegalArgumentException("email required");
        this.id        = id;
        this.tenantId  = tenantId;
        this.email     = email;
        this.userType  = userType;
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    // ── Getters ───────────────────────────────────────────────────────────────
    public String      getId()                    { return id; }
    public String      getTenantId()              { return tenantId; }
    public String      getEmail()                 { return email; }
    public UserType    getUserType()              { return userType; }
    public Instant     getCreatedAt()             { return createdAt; }
    public String      getFullName()              { return fullName; }
    public String      getBio()                   { return bio; }
    public DomainTrack getDomainTrack()           { return domainTrack; }
    public String      getAvatarUrl()             { return avatarUrl; }
    public String      getEducationJson()         { return educationJson; }
    public String      getCertificationsJson()    { return certificationsJson; }
    public String      getExperienceJson()        { return experienceJson; }
    public Instant     getUpdatedAt()             { return updatedAt; }
    public Instant     getArchivedAt()            { return archivedAt; }
    public boolean     isArchived()               { return archivedAt != null; }
    public List<Skill> getSkills()                { return Collections.unmodifiableList(skills); }
    public String      getActiveThemeId()         { return activeThemeId; }
    public boolean     isNotificationEmail()      { return notificationEmail; }
    public boolean     isNotificationSms()        { return notificationSms; }
    public boolean     isNotificationPush()       { return notificationPush; }
    public String      getLanguageCode()          { return languageCode; }
    public String      getTimezone()              { return timezone; }
    public PrivacyLevel getPrivacyLevel()         { return privacyLevel; }
    public int         getCompletionPercentage()  { return completionPercentage; }
    public boolean     isEducationComplete()      { return educationComplete; }
    public boolean     isExperienceComplete()     { return experienceComplete; }
    public boolean     isSkillsComplete()         { return skillsComplete; }
    public boolean     isVerifiedEmail()          { return verifiedEmail; }
    public boolean     isVerifiedPhone()          { return verifiedPhone; }
    public int         getCurrentStreakDays()     { return currentStreakDays; }
    public int         getLongestStreakDays()     { return longestStreakDays; }
    public List<String> getUnlockedScenarios()   { return Collections.unmodifiableList(unlockedScenarios); }
    public List<UserGoal> getGoals()             { return Collections.unmodifiableList(goals); }
    public List<String> getShowcaseAchievements(){ return Collections.unmodifiableList(showcaseAchievements); }
    public List<String> getUnlockedThemes()      { return Collections.unmodifiableList(unlockedThemes); }
    public List<String> getBadges()              { return Collections.unmodifiableList(badges); }
    public List<AuditEntry> getAuditLog()        { return Collections.unmodifiableList(auditLog); }

    // ── Setters ───────────────────────────────────────────────────────────────
    public void setFullName(String v)         { fullName = v;          touch(); }
    public void setBio(String v)              { bio = v;               touch(); }
    public void setDomainTrack(DomainTrack v) { domainTrack = v;       touch(); }
    public void setAvatarUrl(String v)        { avatarUrl = v;         touch(); }
    public void setEducationJson(String v)    { educationJson = v;     touch(); }
    public void setCertificationsJson(String v){certificationsJson = v;touch(); }
    public void setExperienceJson(String v)   { experienceJson = v;    touch(); }
    public void setActiveThemeId(String v)    { activeThemeId = v;     touch(); }
    public void setNotificationEmail(boolean v){ notificationEmail = v;touch(); }
    public void setNotificationSms(boolean v)  { notificationSms = v;  touch(); }
    public void setNotificationPush(boolean v) { notificationPush = v; touch(); }
    public void setLanguageCode(String v)     { languageCode = v;      touch(); }
    public void setTimezone(String v)         { timezone = v;          touch(); }
    public void setPrivacyLevel(PrivacyLevel v){ privacyLevel = v;     touch(); }
    public void setVerifiedEmail(boolean v)   { verifiedEmail = v;     recalcCompletion(); }
    public void setVerifiedPhone(boolean v)   { verifiedPhone = v;     recalcCompletion(); }

    public void addSkill(Skill s) { skills.add(s); recalcCompletion(); touch(); }
    public void removeSkill(String skillName) {
        skills.removeIf(s -> s.getSkillName().equalsIgnoreCase(skillName)); recalcCompletion(); touch();
    }

    public void setEducationComplete(boolean v)  { educationComplete = v;  recalcCompletion(); }
    public void setExperienceComplete(boolean v) { experienceComplete = v; recalcCompletion(); }

    public void softDelete() { archivedAt = Instant.now(); touch(); }

    public void addBadge(String badge)          { badges.add(badge); touch(); }
    public void unlockScenario(String scenarioId){ if(!unlockedScenarios.contains(scenarioId)) unlockedScenarios.add(scenarioId); touch(); }
    public void unlockTheme(String themeId)      { if(!unlockedThemes.contains(themeId)) unlockedThemes.add(themeId); touch(); }

    public void addGoal(UserGoal g)              { goals.add(g); touch(); }

    public void setShowcaseAchievements(List<String> ids) {
        if (ids.size() > 3) throw new IllegalArgumentException("Max 3 showcase achievements allowed");
        showcaseAchievements.clear(); showcaseAchievements.addAll(ids); touch();
    }

    public void updateStreak(int current, int longest) {
        currentStreakDays = current;
        if (current > longest) longestStreakDays = current;
        else longestStreakDays = longest;
        touch();
    }

    public void recordAudit(String actorId, String action, String delta) {
        auditLog.add(new AuditEntry(actorId, action, delta));
    }

    /** Recalculate profile completion % — triggers recruiter visibility boost at 50%+ */
    public void recalcCompletion() {
        int score = 0;
        if (fullName != null && !fullName.isBlank())         score += 15;
        if (bio != null && !bio.isBlank())                   score += 10;
        if (educationComplete)                               score += 20;
        if (experienceComplete)                              score += 15;
        if (!skills.isEmpty())                               score += 15;
        if (skillsComplete)                                  score += 5;
        if (verifiedEmail)                                   score += 10;
        if (verifiedPhone)                                   score += 5;
        if (!badges.isEmpty())                               score += 3;
        if (!showcaseAchievements.isEmpty())                 score += 2;
        completionPercentage = Math.min(100, score);
        skillsComplete = skills.size() >= 3;
        touch();
    }

    private void touch() { updatedAt = Instant.now(); }

    // ── Inner types ───────────────────────────────────────────────────────────

    /** user_skills table row — includes pgvector placeholder. */
    public static class Skill {
        private final String skillName;
        private final SkillLevel level;
        private final String skillVector; // pgvector 1536-dim placeholder
        private final Instant lastUpdated;

        public Skill(String skillName, SkillLevel level) {
            this.skillName   = skillName;
            this.level       = level;
            this.skillVector = "[pgvector:1536-dim embedding for '" + skillName + "']";
            this.lastUpdated = Instant.now();
        }

        public String     getSkillName()   { return skillName; }
        public SkillLevel getLevel()       { return level; }
        public String     getSkillVector() { return skillVector; }
        public Instant    getLastUpdated() { return lastUpdated; }
    }

    /** user_goals table row. */
    public static class UserGoal {
        public final String   id;
        public final GoalType type;
        public final int      targetValue;
        public       int      currentValue;
        public       boolean  completed;
        public final Instant  deadline;
        public final Instant  createdAt;

        public UserGoal(String id, GoalType type, int targetValue, Instant deadline) {
            this.id           = id;
            this.type         = type;
            this.targetValue  = targetValue;
            this.currentValue = 0;
            this.completed    = false;
            this.deadline     = deadline;
            this.createdAt    = Instant.now();
        }

        public void updateProgress(int value) {
            currentValue = value;
            if (currentValue >= targetValue) completed = true;
        }
    }

    /** user_audit_log row — immutable. */
    public static class AuditEntry {
        public final String  actorId;
        public final String  action;
        public final String  delta;
        public final Instant timestamp;

        public AuditEntry(String actorId, String action, String delta) {
            this.actorId   = actorId;
            this.action    = action;
            this.delta     = delta;
            this.timestamp = Instant.now();
        }
    }
}
