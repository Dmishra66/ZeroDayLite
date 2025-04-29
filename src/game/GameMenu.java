package game;

import javax.swing.*;
import java.awt.*;
//import java.awt.event.*;

public class GameMenu extends JFrame {
    public int totalWins = 0;
    private BadgeManager badgeManager = new BadgeManager();
    private ProgressTracker progressTracker = new ProgressTracker();

    // Progress labels
    private JLabel gamesPlayedLabel, gamesWonLabel, totalPointsLabel, winRateLabel;
    private JProgressBar progressBar;

    public GameMenu() {
        progressTracker.loadProgress();  // Load previous progress

        setTitle("Cyber Fun Zone");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(600, 500);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JLabel title = new JLabel("Cyber Fun Zone", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 28));
        title.setForeground(new Color(70, 130, 180));

        JPanel buttonPanel = new JPanel(new GridLayout(1, 3, 10, 10));
        JButton memoryGameButton = new JButton("Memory Card Game");
        JButton bugZapperButton = new JButton("Bug Zapper");
        JButton showBadgesButton = new JButton("My Badges");

        JButton saveServerButton = new JButton("Save The Server");

        saveServerButton.addActionListener(e -> tryStartSaveTheServer());
        buttonPanel.add(saveServerButton);
        
        memoryGameButton.addActionListener(e -> tryStartGame("Memory"));
        bugZapperButton.addActionListener(e -> tryStartGame("BugZapper"));
        showBadgesButton.addActionListener(e -> badgeManager.showBadges());

        buttonPanel.add(memoryGameButton);
        buttonPanel.add(bugZapperButton);
        buttonPanel.add(showBadgesButton);

        // Progress Dashboard Panel
        JPanel progressPanel = new JPanel();
        progressPanel.setLayout(new GridLayout(5, 1, 5, 5));
        progressPanel.setBorder(BorderFactory.createTitledBorder("Your Progress"));

        gamesPlayedLabel = new JLabel();
        gamesWonLabel = new JLabel();
        totalPointsLabel = new JLabel();
        winRateLabel = new JLabel();
        progressBar = new JProgressBar(0, 500);
        progressBar.setForeground(new Color(60, 179, 113));

        progressPanel.add(gamesPlayedLabel);
        progressPanel.add(gamesWonLabel);
        progressPanel.add(totalPointsLabel);
        progressPanel.add(winRateLabel);
        progressPanel.add(progressBar);

        refreshProgress();  // fill labels

        add(title, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);
        add(progressPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void tryStartGame(String gameType) {
        if (progressTracker.getTotalPoints() < 100) {
            JOptionPane.showMessageDialog(this, "Reach 500 points to unlock games!\n(Current: " + progressTracker.getTotalPoints() + ")");
            return;
        }
        progressTracker.recordGamePlayed();

        if (gameType.equals("Memory")) {
            new MemoryCard(this);
        } else if (gameType.equals("BugZapper")) {
            new BugZapper(this);
        }
    }

    private void tryStartSaveTheServer() {
        if (progressTracker.getTotalPoints() < 500) {
            JOptionPane.showMessageDialog(this, "Reach 500 points to unlock games!\n(Current: " + progressTracker.getTotalPoints() + ")");
            return;
        }
        progressTracker.recordGamePlayed();
        new SaveTheServer(this);
    }

    public void addWin(String badgeName) {
        totalWins++;
        progressTracker.recordGameWon();
        badgeManager.unlockBadge(badgeName);
        refreshProgress();
        progressTracker.saveProgress();  // Save after win
    }

    private void refreshProgress() {
        gamesPlayedLabel.setText("Games Played: " + progressTracker.getGamesPlayed());
        gamesWonLabel.setText("Games Won: " + progressTracker.getGamesWon());
        totalPointsLabel.setText("Points: " + progressTracker.getTotalPoints());
        winRateLabel.setText("Win Rate: " + progressTracker.getWinRate());
        progressBar.setValue(progressTracker.getTotalPoints());
        progressBar.setStringPainted(true);
        progressBar.setString(progressTracker.getTotalPoints() + "/500 points");
    }
}
