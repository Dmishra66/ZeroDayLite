package utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import scanner.DBConnection;

public class StatisticsManager {
    private static int totalScans = 0;
    private static int vulnerabilitiesFound = 0;
    private static int keystrokesCaptured = 0;
    private static int payloadsBuilt = 0;

    // Scan-specific counters
    private static int sqliScans = 0;
    private static int xssScans = 0;
    private static int keyloggerScans = 0;

    // Real-time counters
    private static int dailyStreak = 1;
    private static int badgesEarned = 0;

    private static int currentUserId = -1; // <-- Add this
    private static int userId; // Logged in user's ID
    private static LocalDate lastLoginDate;

    public static void setCurrentUserId(int userId) {
        currentUserId = userId;
    }
    
    public static int getCurrentUserId() {
        return currentUserId;
    }
    

    // Call this when user logs in
    public static void loadUserStats(int userIdFromLogin) {
        userId = userIdFromLogin;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT * FROM users WHERE id = ?"
             )) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                totalScans = rs.getInt("total_scans");
                vulnerabilitiesFound = rs.getInt("vulnerabilities_found");
                keystrokesCaptured = rs.getInt("keystrokes");
                payloadsBuilt = rs.getInt("payloads_built");
                sqliScans = rs.getInt("sqli_scans");
                xssScans = rs.getInt("xss_scans");
                keyloggerScans = rs.getInt("keylogger_scans");
                dailyStreak = rs.getInt("daily_streak");
                badgesEarned = rs.getInt("badges_earned");
                lastLoginDate = rs.getDate("last_login_date") != null ?
                        rs.getDate("last_login_date").toLocalDate() : null;

                updateDailyStreak();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private static void updateDailyStreak() {
        LocalDate today = LocalDate.now();

        if (lastLoginDate == null) {
            dailyStreak = 1;
        } else {
            long daysBetween = ChronoUnit.DAYS.between(lastLoginDate, today);
            if (daysBetween == 1) {
                dailyStreak++; // Continued streak
            } else if (daysBetween > 1) {
                dailyStreak = 1; // Reset streak
            }
        }

        lastLoginDate = today;
        saveStats(); // Save updated daily streak
    }
    

    public static void incrementScans() {
        totalScans++;
        saveStats();
    }

    public static void incrementVulnerabilitiesFound() {
        vulnerabilitiesFound++;
        saveStats();
    }

    public static void addKeystrokes(int count) {
        keystrokesCaptured += count;
        saveStats();
    }

    public static void incrementPayloadsBuilt() {
        payloadsBuilt++;
        saveStats();
    }

    // Scan type methods
    public static void incrementSqliScan() {
        sqliScans++;
        incrementScans(); // Update total scan count
        saveStats();
    }

    public static void incrementXssScan() {
        xssScans++;
        incrementScans(); // Update total scan count
        saveStats();
    }

    public static void incrementKeyloggerScan() {
        keyloggerScans++;
        incrementScans(); // Update total scan count
        saveStats();
    }

    // Real-time methods
    public static void incrementKeystrokeCaptured() {
        keystrokesCaptured++;
        saveStats();
    }

    public static void incrementBadgeEarned() {
        badgesEarned++;
        saveStats();
    }
    public static void incrementBadgeEarned(int n) {
        badgesEarned+=n;
        saveStats();
    }

    public static int getTotalScans() { return totalScans; }
    public static int getVulnerabilitiesFound() { return vulnerabilitiesFound; }
    public static int getKeystrokesCaptured() { return keystrokesCaptured; }
    public static int getPayloadsBuilt() { return payloadsBuilt; }
    public static int getSqliScans() { return sqliScans; }
    public static int getXssScans() { return xssScans; }
    public static int getKeyloggerScans() { return keyloggerScans; }
    public static int getDailyStreak() { return dailyStreak; }
    public static int getBadgesEarned() { return badgesEarned; }

    // Save back to database
    public static void saveStats() {
        if (currentUserId == -1) return; // No user logged in
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "UPDATE users SET total_scans = ?, keystrokes = ?, payloads_built = ?, daily_streak = ?," +
                             "last_login_date = ?, vulnerabilities_found = ?, sqli_scans = ?, xss_scans = ?, keylogger_scans = ?,  badges_earned = ? WHERE id = ?"
             )) {

            stmt.setInt(1, totalScans);
            stmt.setInt(2, keystrokesCaptured);
            stmt.setInt(3, payloadsBuilt);
            stmt.setInt(4, dailyStreak);
            stmt.setDate(5, Date.valueOf(lastLoginDate));
            stmt.setInt(6, vulnerabilitiesFound);
            stmt.setInt(7, sqliScans);
            stmt.setInt(8, xssScans);
            stmt.setInt(9, keyloggerScans);
            stmt.setInt(10, badgesEarned);
            stmt.setInt(11, userId);

            stmt.executeUpdate();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}