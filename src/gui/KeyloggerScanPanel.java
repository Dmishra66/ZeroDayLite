package gui;
import scanner.VnScanner;
import utils.AttackAnimation;
import utils.StatisticsManager;

import javax.swing.*;
import java.awt.*;
//import java.awt.event.ActionListener;

public class KeyloggerScanPanel extends JPanel {
    public JTextArea resultAreaFound, resultAreaNotFound;
    public JButton scanButton, backButton, logoutButton;

    public KeyloggerScanPanel(ScanDashboard dashboard, String username) {
        //this.dashboard = dashboard;
        //this.username = username;
        setLayout(new BorderLayout());
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

        // Title
        JPanel titlePanel = new JPanel(new GridLayout(2, 1));
        JLabel welcomeLabel = new JLabel("Welcome to Simulated Keylogger Scan", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 22));
        JLabel scanNameLabel = new JLabel("Keylogger Detection", SwingConstants.CENTER);
        scanNameLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        titlePanel.add(welcomeLabel);
        titlePanel.add(scanNameLabel);

        // Center vertical container
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));

        // Description and risk
        JTextArea description = new JTextArea(
            "A keylogger or keystroke logger/keyboard capturing is a form of malware or hardware that keeps track of and records your keystrokes as you type. It takes the information and sends it to a hacker using a command-and-control (C&C) server.\n" +
            "\n Risk: It can lead to data theft, identity theft, and unauthorized access to personal information.\n" +
            "\n How to Detect A Keylogger?\r\n" + 
            "The simplest way to detect a keylogger is to check your task manager.\n" +
            "Here, you can see which processes are running. It can be tough to know which ones are legitimate and which could be caused by keyloggers, but you can differentiate the safe processes from the threats by looking at each process up on the internet.\n"+
            "How Keyloggers Attack Your Device?\r\n" + //
            "To gain access to your device, a keylogger has to be installed inside it or, in the case of a hardware keylogger, physically connected to your computer. There are a few different ways keyloggers attack your device.\r\n" + 
            "#Spear phishing\r\n" +  "#Drive-by download\r\n" + "#Trojan horse\r\n" +
            "\n\n Problems Caused By Keyloggers\r\n" + 
            "In addition to compromising the security of your device, keyloggers can cause auxiliary issues on the device itself. The effects are somewhat different based on the type of device that has been infected." +
            "\n # Unknown processes consuming computing power\n"+"# Delays during typing\n"+"# Applications freeze randomly"
        );
        description.setWrapStyleWord(true);
        description.setLineWrap(true);
        description.setEditable(false);
        description.setFont(new Font("Arial", Font.PLAIN, 14));
        description.setBackground(new Color( 84, 153, 199));
        description.setBorder(BorderFactory.createTitledBorder("Scan Description & Risk"));

        // Sample input + Scan button
        JPanel inputPanel = new JPanel(new BorderLayout(10, 10));

        JTextArea sampleInput = new JTextArea(
            "\n user typed: hello\n" +
            "\n keystroke pattern: delay 0.1ms\n" +
            "\n input pattern: normal\n" +
            "\n user typed: password123\n" +
            "\n keystroke pattern: delay 0.09ms\n" +
            "\n input pattern: rapid-fire keys\n" +
            "\n suspicious logging of user inputs\n" +
            "\n keystroke logger: detected unusual input frequency\n" +
            "\n key pattern: repetitive inputs detected\n" +
            "\n user typed: 123456\n" +
            "\n keystroke log stored in temp file\n" +
            "\n keystroke logger: detected unusual input frequency\n" 
        );
        sampleInput.setBorder(BorderFactory.createTitledBorder("Sample Inputs"));
        sampleInput.setEditable(false);
        sampleInput.setFont(new Font("Courier New", Font.PLAIN, 14));
        inputPanel.add(new JScrollPane(sampleInput), BorderLayout.CENTER);

        scanButton = new JButton("Start Scan");
        inputPanel.add(scanButton, BorderLayout.EAST);
        scanButton.addActionListener(e ->  {
            String results = VnScanner.scanKeylogger(username);
            StatisticsManager.incrementScans(); // ✅ Scan done, increment
            StatisticsManager.incrementKeyloggerScan(); 
            // Split results into Found and Not Found
            StringBuilder found = new StringBuilder();
            StringBuilder notFound = new StringBuilder();
        
            for (String line : results.split("\n")) {
                if (line.contains("[✓]")) {
                    found.append(line).append("\n");
                } else if (line.contains("[✗]")) {
                    notFound.append(line).append("\n");
                }
            }
        
            resultAreaFound.setText(found.toString());
            resultAreaNotFound.setText(notFound.toString());

            // Trigger the attack animation if anything was found
            if (!found.toString().isEmpty()) {
                StatisticsManager.incrementVulnerabilitiesFound();
                AttackAnimation.show("keylogger"); // Use "sqli" or "keylogger" for other panels
            }
            // Update statistics panel and chart
            StatisticsPanel statsPanel = new StatisticsPanel(dashboard);
            statsPanel.updateStats();
        });
        


        // Results area
        JPanel resultPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        resultAreaFound = new JTextArea();
        resultAreaFound.setBorder(BorderFactory.createTitledBorder("Vulnerabilities Found"));
        resultAreaFound.setBackground(new Color( 254, 0, 0));
        resultAreaFound.setForeground(new Color( 255, 255, 255));
        resultAreaFound.setEditable(false);

        resultAreaNotFound = new JTextArea();
        resultAreaNotFound.setBorder(BorderFactory.createTitledBorder("Vulnerabilities Not Found"));
        resultAreaNotFound.setBackground(new Color( 73, 223, 105 ));
        resultAreaNotFound.setEditable(false);

        resultPanel.add(new JScrollPane(resultAreaFound));
        resultPanel.add(new JScrollPane(resultAreaNotFound));

        
         // Add all components vertically
         centerPanel.add(new JScrollPane(description), BorderLayout.CENTER);
         centerPanel.add(inputPanel);
         centerPanel.add(resultPanel);
 

        // Navigation buttons
        JPanel navPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        navPanel.setOpaque(false);
        backButton = new JButton("Back");
        logoutButton = new JButton("Logout");
        backButton.addActionListener(e -> dashboard.switchToPanel("Welcome"));
        logoutButton.addActionListener(e -> dashboard.logout());
        navPanel.add(backButton);
        navPanel.add(logoutButton);

        // Add to main panel
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
}
