package gui;

import scanner.RealTimeScanner;
import utils.StatisticsManager;

import javax.swing.*;
import java.awt.*;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;

public class RealTimeScannerPanel extends JPanel {
    private JTextField urlField;
    private JTextArea resultArea;
    private JTextArea scanConsoleArea;
    private JButton scanButton, backButton, logoutButton;
    private JPanel puzzlePanel;
    private JTextField answerField;
    private JLabel puzzleQuestion;

    public RealTimeScannerPanel(ScanDashboard dashboard) {
        setLayout(new BorderLayout(20, 20));
        // Load the original GIF from src/assets
        ImageIcon originalIcon = new ImageIcon(getClass().getResource("/assets/background4.gif"));

        // Create a JLabel to hold the GIF (will be resized later)
        JLabel backgroundLabel = new JLabel();
        backgroundLabel.setLayout(new BorderLayout());
        add(backgroundLabel); // Add background first

        // Main transparent content
        JPanel contentWrapper = new JPanel(new BorderLayout(20, 20));
        contentWrapper.setOpaque(false); // So background shows through
        contentWrapper.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));


        // Website Help Box
        String[] demoSites = {
            "Select a Demo Site...",
            "http://testphp.vulnweb.com",
            "http://zero.webappsecurity.com",
            "https://demo.testfire.net",
            "http://dvwa.local",
            "https://juice-shop.herokuapp.com"
        };
        JComboBox<String> siteDropdown = new JComboBox<>(demoSites);
        siteDropdown.setFont(new Font("Arial", Font.PLAIN, 14));
        siteDropdown.addActionListener(e -> {
            String selectedSite = (String) siteDropdown.getSelectedItem();
            if (!selectedSite.equals("Select a Demo Site...")) {
                urlField.setText(selectedSite);
            }
        });

        // Title
        JPanel titlePanel = new JPanel(new GridLayout(3, 1));
        JLabel titleLabel = new JLabel("Real-Time Web Security Scanner", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        JLabel descLabel = new JLabel("<html><center>Enter a website URL to analyze security risks.<br>Click Scan to begin the simulation.</center></html>", SwingConstants.CENTER);
        descLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        titlePanel.add(titleLabel);
        titlePanel.add(siteDropdown);
        titlePanel.add(descLabel);

        // URL Input
        JPanel inputPanel = new JPanel(new BorderLayout(10, 10));
        urlField = new JTextField("http://example.com");
        scanButton = new JButton("Start Scan 🔍");
        scanButton.setFont(new Font("Arial", Font.BOLD, 16));
        scanButton.setBackground(new Color(66, 133, 244));
        scanButton.setForeground(Color.WHITE);
        scanButton.setFocusPainted(false);
        scanButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        inputPanel.add(new JLabel("Website URL: "), BorderLayout.WEST);
        inputPanel.add(urlField, BorderLayout.CENTER);
        inputPanel.add(scanButton, BorderLayout.EAST);

        // Center Panel
        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        centerPanel.add(inputPanel, BorderLayout.NORTH);

        // Scan Results + Server Console
        JPanel resultPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        
        resultArea = new JTextArea();
        resultArea.setEditable(false);
        resultArea.setFont(new Font("Courier New", Font.PLAIN, 13));
        resultArea.setBorder(BorderFactory.createTitledBorder("Scan Results"));

        scanConsoleArea = new JTextArea();
        scanConsoleArea.setEditable(false);
        scanConsoleArea.setBackground(Color.BLACK);
        scanConsoleArea.setForeground(Color.GREEN);
        scanConsoleArea.setFont(new Font("Consolas", Font.PLAIN, 13));
        scanConsoleArea.setBorder(BorderFactory.createTitledBorder("Server Console"));

        resultPanel.add(new JScrollPane(resultArea));
        resultPanel.add(new JScrollPane(scanConsoleArea));
        
        centerPanel.add(resultPanel, BorderLayout.CENTER);

        // Puzzle/Did You Know Box (hidden initially)
        setupPuzzlePanel();
        puzzlePanel.setVisible(false);
        centerPanel.add(puzzlePanel, BorderLayout.SOUTH);

        // Navigation Panel
        JPanel navPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        navPanel.setOpaque(false);
        backButton = new JButton("Back");
        logoutButton = new JButton("Logout");
        backButton.addActionListener(e -> dashboard.switchToPanel("Welcome"));
        logoutButton.addActionListener(e -> dashboard.logout());
        navPanel.add(backButton);
        navPanel.add(logoutButton);

        // Button Action
        scanButton.addActionListener(e -> {
            startScanning();
        });

        // Assemble Everything
        contentWrapper.add(titlePanel, BorderLayout.NORTH);
        contentWrapper.add(centerPanel, BorderLayout.CENTER);
        contentWrapper.add(navPanel, BorderLayout.SOUTH);
        backgroundLabel.add(contentWrapper, BorderLayout.CENTER);

        // Resize background GIF dynamically with panel
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                Image scaled = originalIcon.getImage().getScaledInstance(getWidth(), getHeight(), Image.SCALE_DEFAULT);
                backgroundLabel.setIcon(new ImageIcon(scaled));
            }
        });
    }

    private void startScanning() {
        String url = urlField.getText().trim();
        if (url.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a URL.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        scanButton.setEnabled(false);
        scanButton.setText("Scanning...");
        resultArea.setText("");
        scanConsoleArea.setText("");

        new Thread(() -> {
            appendToConsole("> Connecting to " + url + "...");
            sleep(1000);

            appendToConsole("> Sending crafted requests...");
            sleep(1000);

            appendToConsole("> Analyzing server response...");
            sleep(1500);

            appendToConsole("> Scan completed.");
            sleep(500);

            String result = RealTimeScanner.scanWebsite(url); // Backend logic
            SwingUtilities.invokeLater(() -> {
                resultArea.setText(result);
                scanButton.setEnabled(true);
                scanButton.setText("Start Scan 🔍");
                puzzlePanel.setVisible(true);
            });
        }).start();
    }

    private void appendToConsole(String text) {
        SwingUtilities.invokeLater(() -> {
            scanConsoleArea.append(text + "\n");
            scanConsoleArea.setCaretPosition(scanConsoleArea.getDocument().getLength());
        });
    }

    private void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    private void setupPuzzlePanel() {
        puzzlePanel = new JPanel(new FlowLayout());
        puzzleQuestion = new JLabel("Did you know? SQL Injection can bypass login pages!");
        answerField = new JTextField(20);
        JButton submitAnswer = new JButton("Answer for Bonus!");

        submitAnswer.addActionListener(e -> checkPuzzleAnswer());

        puzzlePanel.add(puzzleQuestion);
        puzzlePanel.add(answerField);
        puzzlePanel.add(submitAnswer);
    }

    private void checkPuzzleAnswer() {
        String answer = answerField.getText().trim().toLowerCase();
        if (answer.contains("input") || answer.contains("filter")) {
            flashGreen(answerField);
            JOptionPane.showMessageDialog(this, "Correct! 🎉 +10 bonus points awarded!");
            StatisticsManager.incrementBadgeEarned(10);
        } else {
            shakeComponent(answerField);
            JOptionPane.showMessageDialog(this, "Wrong answer! Try again!");
        }
    }

    private void shakeComponent(JComponent comp) {
        Point original = comp.getLocation();
        for (int i = 0; i < 8; i++) {
            int dx = (i % 2 == 0) ? 5 : -5;
            comp.setLocation(original.x + dx, original.y);
            try { Thread.sleep(30); } catch (InterruptedException ex) {}
        }
        comp.setLocation(original);
    }

    private void flashGreen(JComponent comp) {
        Color original = comp.getBackground();
        comp.setBackground(Color.GREEN);
        Timer timer = new Timer(500, e -> comp.setBackground(original));
        timer.setRepeats(false);
        timer.start();
    }
}
