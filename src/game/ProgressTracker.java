package game;

import java.io.*;

public class ProgressTracker {
    private int gamesPlayed = 0;
    private int gamesWon = 0;
    private int totalPoints = 0;  // 100 points per win

    public void recordGamePlayed() {
        gamesPlayed++;
    }

    public void recordGameWon() {
        gamesWon++;
        totalPoints += 100;  // Each win gives 100 points
    }

    public int getGamesPlayed() { return gamesPlayed; }
    public int getGamesWon() { return gamesWon; }
    public int getTotalPoints() { return totalPoints; }

    public String getWinRate() {
        if (gamesPlayed == 0) return "0%";
        double rate = (double) gamesWon / gamesPlayed * 100;
        return String.format("%.1f%%", rate);
    }

    // SAVE Progress
    public void saveProgress() {
        try (PrintWriter writer = new PrintWriter(new FileWriter("progress.txt"))) {
            writer.println(gamesPlayed);
            writer.println(gamesWon);
            writer.println(totalPoints);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // LOAD Progress
    public void loadProgress() {
        try (BufferedReader reader = new BufferedReader(new FileReader("progress.txt"))) {
            gamesPlayed = Integer.parseInt(reader.readLine());
            gamesWon = Integer.parseInt(reader.readLine());
            totalPoints = Integer.parseInt(reader.readLine());
        } catch (IOException | NumberFormatException e) {
            System.out.println("No previous progress found. Starting fresh!");
        }
    }
}
