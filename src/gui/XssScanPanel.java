package gui;
import scanner.VnScanner;
import utils.AttackAnimation;
import utils.StatisticsManager;

import javax.swing.*;
import java.awt.*;
//import java.awt.event.ActionListener;

public class XssScanPanel extends JPanel {
    public JTextArea resultAreaFound, resultAreaNotFound;
    public JButton scanButton, backButton, logoutButton;

    public XssScanPanel(ScanDashboard dashboard, String username) {
        //this.dashboard = dashboard;
        //this.username = username;
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

        // Title
        JPanel titlePanel = new JPanel(new GridLayout(2, 1));
        JLabel welcomeLabel = new JLabel("Welcome to Simulated XSS Scan", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 22));
        JLabel scanNameLabel = new JLabel("Cross-Site Scripting (XSS)", SwingConstants.CENTER);
        scanNameLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        titlePanel.add(welcomeLabel);
        titlePanel.add(scanNameLabel);

        // Center vertical container
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));

        // Description and risk
        JTextArea description = new JTextArea(
            "Cross Site Scripting (XSS) is a vulnerability in a web application that allows a third party to execute a script in the user’s browser on behalf of the web application.\n" +
            "\n Risk: It can steal cookies, session tokens, or redirect users to harmful sites."+
            "\n\n There are 3 types of XSS –\n"+"Stored XSS:\r\n" + //
           "The malicious script is stored on the server and is then served to all users who visit the page. \r\n" + //
            "Reflected XSS:\r\n" + //
            "The malicious script is reflected back to the victim's browser after they interact with the website (e.g., clicking a malicious link). \r\n" + //
            "DOM-based XSS:\r\n" + //
           "The malicious script modifies the Document Object Model (DOM) within the user's browser, potentially allowing the attacker to hijack interactions with the website. "+
            "\n\n How XSS Works:\r\n" + //
             "1. Injection:\r\n" + //
            "An attacker injects malicious JavaScript code into a website, often by exploiting vulnerabilities in user-generated content or web application logic.\r\n" + //
            "2. Delivery:\r\n" + //
            "The injected code is then delivered to a victim's browser when they visit the affected website.\r\n" + //
            "3. Execution:\r\n" + //
            "The browser executes the malicious script because it considers it part of the legitimate website's code.\r\n" + //
            "4. Exploitation:\r\n" + //
            "The attacker's code can then access the victim's browser data, cookies, and other information, allowing them to steal session tokens, impersonate the user, or perform other actions. \r\n" 
        );
        description.setWrapStyleWord(true);
        description.setLineWrap(true);
        description.setEditable(false);
        description.setFont(new Font("Arial", Font.PLAIN, 14));
        description.setBackground(new Color(165, 105, 189));
        description.setBorder(BorderFactory.createTitledBorder("Scan Description & Risk"));

        // Sample input + Scan button
        JPanel inputPanel = new JPanel(new BorderLayout(10, 10));

        JTextArea sampleInput = new JTextArea(
            "\n <script>alert('XSS')</script>\n" +
            "\n <h1>Welcome</h1>\n" +
            "\n <img src='x.jpg' />\n" +
            "\n <script>stealCookies()</script>\n" +
            "\n <script>alert('XSS')</script>\n" +
            "\n <img src='x' onerror='alert(1)'/>\n" +
            "\n <body onload=alert('XSS')>\n" +
            "\n <iframe src='javascript:alert(2)'>\n" +
            "\n <svg onload=alert('XSS')>\n" +
            "\n <h1>Hello</h1>\n" +
            "\n <b>Not a script</b>\n"
        );
        sampleInput.setBorder(BorderFactory.createTitledBorder("Sample Inputs"));
        sampleInput.setEditable(false);
        sampleInput.setFont(new Font("Courier New", Font.PLAIN, 14));
        inputPanel.add(new JScrollPane(sampleInput), BorderLayout.CENTER);

        scanButton = new JButton("Start Scan");
        inputPanel.add(scanButton, BorderLayout.EAST);
        scanButton.addActionListener(e ->  {
            String results = VnScanner.scanXSS(username); 
            StatisticsManager.incrementScans(); // ✅ Scan done, increment 
            StatisticsManager.incrementXssScan(); // Increment SQLi scan specific counter

            
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
                AttackAnimation.show("xss"); // Use "sqli" or "keylogger" for other panels
            }
           StatisticsManager.incrementVulnerabilitiesFound();
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
        centerPanel.add(new JScrollPane(description));
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
