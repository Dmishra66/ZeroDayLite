package utils;

import java.io.*;
import java.time.LocalDate;
//import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
//import java.util.HashMap;
//import java.util.Map;

public class StatisticsManager {
    private static final String USERS_FILE = "assets/users.txt"; // or "utils/users.txt" if placed there
    private static String currentUsername = null;

    // Stats
    private static int totalScans = 0;
    private static int vulnerabilitiesFound = 0;
    private static int keystrokesCaptured = 0;
    private static int payloadsBuilt = 0;
    private static int sqliScans = 0;
    private static int xssScans = 0;
    private static int keyloggerScans = 0;
    private static int dailyStreak = 1;
    private static int badgesEarned = 0;
    private static LocalDate lastLoginDate = null;

    public static void setCurrentUsername(String username) {
        currentUsername = username;
        loadStats();
    }

    public static void loadStats() {
        try (BufferedReader reader = new BufferedReader(new FileReader(USERS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length >= 3 && parts[0].equals(currentUsername)) {
                    String[] stats = parts[2].split(",");
                    totalScans = Integer.parseInt(stats[0]);
                    sqliScans = Integer.parseInt(stats[1]);
                    xssScans = Integer.parseInt(stats[2]);
                    keyloggerScans = Integer.parseInt(stats[3]);
                    vulnerabilitiesFound = Integer.parseInt(stats[4]);
                    keystrokesCaptured = Integer.parseInt(stats[5]);
                    payloadsBuilt = Integer.parseInt(stats[6]);
                    dailyStreak = Integer.parseInt(stats[7]);
                    badgesEarned = Integer.parseInt(stats[8]);
                    lastLoginDate = LocalDate.parse(stats[9]);

                    updateDailyStreak();
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void updateDailyStreak() {
        LocalDate today = LocalDate.now();

        if (lastLoginDate == null) {
            dailyStreak = 1;
        } else {
            long daysBetween = ChronoUnit.DAYS.between(lastLoginDate, today);
            if (daysBetween == 1) {
                dailyStreak++;
            } else if (daysBetween > 1) {
                dailyStreak = 1;
            }
        }

        lastLoginDate = today;
        saveStats();
    }

    public static void saveStats() {
        if (currentUsername == null) return;

        File inputFile = new File(USERS_FILE);
        File tempFile = new File("users_temp.txt");

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {

            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length >= 3 && parts[0].equals(currentUsername)) {
                    String newStats = totalScans + "," + sqliScans + "," + xssScans + "," + keyloggerScans + ","
                            + vulnerabilitiesFound + "," + keystrokesCaptured + "," + payloadsBuilt + "," + dailyStreak + ","
                            + badgesEarned + "," + lastLoginDate;
                    writer.write(parts[0] + "|" + parts[1] + "|" + newStats);
                } else {
                    writer.write(line);
                }
                writer.newLine();
            }

            inputFile.delete();
            tempFile.renameTo(inputFile);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Increment methods
    public static void incrementScans() { totalScans++; saveStats(); }
    public static void incrementSqliScan() { sqliScans++; incrementScans(); }
    public static void incrementXssScan() { xssScans++; incrementScans(); }
    public static void incrementKeyloggerScan() { keyloggerScans++; incrementScans(); }
    public static void incrementVulnerabilitiesFound() { vulnerabilitiesFound++; saveStats(); }
    public static void addKeystrokes(int count) { keystrokesCaptured += count; saveStats(); }
    public static void incrementPayloadsBuilt() { payloadsBuilt++; saveStats(); }
    public static void incrementKeystrokeCaptured() { keystrokesCaptured++; saveStats(); }
    public static void incrementBadgeEarned() { badgesEarned++; saveStats(); }
    public static void incrementBadgeEarned(int n) { badgesEarned += n; saveStats(); }

    // Getters
    public static int getTotalScans() { return totalScans; }
    public static int getSqliScans() { return sqliScans; }
    public static int getXssScans() { return xssScans; }
    public static int getKeyloggerScans() { return keyloggerScans; }
    public static int getVulnerabilitiesFound() { return vulnerabilitiesFound; }
    public static int getKeystrokesCaptured() { return keystrokesCaptured; }
    public static int getPayloadsBuilt() { return payloadsBuilt; }
    public static int getDailyStreak() { return dailyStreak; }
    public static int getBadgesEarned() { return badgesEarned; }
}
