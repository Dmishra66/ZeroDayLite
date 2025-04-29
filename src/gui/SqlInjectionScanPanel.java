package gui;
import scanner.VnScanner;
import utils.AttackAnimation;
import utils.StatisticsManager;

import javax.swing.*;
import java.awt.*;
//import java.awt.event.ActionListener;

public class SqlInjectionScanPanel extends JPanel {
    public JTextArea resultAreaFound, resultAreaNotFound;
    public JButton scanButton, backButton, logoutButton;

    public SqlInjectionScanPanel(ScanDashboard dashboard, String username) {
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

        // Center vertical container
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));

        // Title
        JPanel titlePanel = new JPanel(new GridLayout(2, 1));
        JLabel welcomeLabel = new JLabel("Welcome to Simulated SQL Injection Scan", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 22));
        JLabel scanNameLabel = new JLabel("SQL Injection", SwingConstants.CENTER);
        scanNameLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        titlePanel.add(welcomeLabel);
        titlePanel.add(scanNameLabel);

        // Description and risk
        JTextArea description = new JTextArea(
            "SQL injection is a cyber security vulnerability that occurs when an attacker exploits an application's input to insert malicious SQL code into a database query. This can allow them to manipulate the database, potentially gaining unauthorized access to sensitive data, modifying data, or even executing arbitrary commands on the server.\n" + 
            "How SQL Injection Works:\n" +
         "1. Vulnerable Input:\r\n"+
        "An application allows user input (e.g., form fields, search queries) to be directly incorporated into a SQL query without proper validation or sanitization. \n  2. Malicious Input:\n"+
"An attacker enters a SQL query designed to manipulate the database. "+
"\n3. Query Execution:\n"+    
"The application executes the modified SQL query, unknowingly including the attacker's code."+
"\n4. Exploitation:\n"+
"The attacker can then use the injected code to achieve various malicious goals, such as retrieving sensitive data, modifying data, or even gaining control of the server. "+
"\n\nTypes of SQL Injection:\n"+
"# In-band SQLi:\n"+
"The attacker uses the same communication channel (e.g., HTTP request) to both inject the malicious code and retrieve the results."+
"\n # Blind SQLi:\n"+
"The attacker cannot see the results of the injected SQL code directly. They instead rely on subtle changes in the application's behavior to infer the success of their attacks." +
"\n # Out-of-band SQLi:\n"+
"The attacker uses different channels for injecting the code and retrieving the results, often exploiting database server features like DNS requests. " +
"\n\n\nExamples of SQL Injection:"+
"\nData Retrieval:\nAn attacker could insert a query that retrieves all records from a table, exposing sensitive customer information. " +
"\n # Data Modification:\nAn attacker could insert a query that modifies a user's password or account balance. \n"+
"\n\nPrevention:\n"+
"Input Validation and Sanitization :- Implement robust validation and sanitization techniques to ensure that all user input is checked and cleaned before being used in SQL queries.\n"+
"Parameterized Queries (Prepared Statements) :- Use parameterized queries, which separate the SQL query from the data, preventing the injection of malicious code. "+
"Principle of Least Privilege :- Grant database users only the minimum necessary privileges. "+
"Regular Security Audits and Testing :- Conduct regular security audits and penetration testing to identify and fix vulnerabilities.\n"+ 
"Database Firewall and Monitoring :- Implement database firewalls and monitoring tools to detect and prevent SQL injection attacks." );
        description.setWrapStyleWord(true);
        description.setLineWrap(true);
        description.setEditable(false);
        description.setFont(new Font("Arial", Font.PLAIN, 14));
        description.setBackground(new Color( 208, 204, 130));
        description.setBorder(BorderFactory.createTitledBorder("Scan Description & Risk"));

        // Sample + Scan Button section
        JPanel inputPanel = new JPanel(new BorderLayout(10, 10));

        JTextArea sampleInput = new JTextArea(
            "\n ' OR '1'='1\n" +
            "\n '; DROP TABLE users; --\n" +
            "\n admin' --\n" +
            "\n ' OR 1=1 --\n" +
            "\n normalInput\n" +
            "\n ' UNION SELECT null, null, null --\n" +
            "\n ' AND 1=0 UNION SELECT username, password, NULL FROM users --\n" +
            "\n hello\n" +
            "\n SELECT * FROM users WHERE username = 'admin'\n" +
            "\n DROP TABLE users\n"
        );
        sampleInput.setBorder(BorderFactory.createTitledBorder("Sample Inputs"));
        sampleInput.setEditable(false);
        sampleInput.setFont(new Font("Courier New", Font.PLAIN, 14));
        inputPanel.add(new JScrollPane(sampleInput), BorderLayout.CENTER);

        scanButton = new JButton("Start Scan");
        inputPanel.add(scanButton, BorderLayout.EAST);
        scanButton.addActionListener(e -> {
            String results = VnScanner.scanSQLInjection(username); // This assumes ScanDashboard.username is set at login
            StatisticsManager.incrementScans(); // ✅ Scan done, increment
            StatisticsManager.incrementSqliScan(); 

            // Separate the results
            StringBuilder found = new StringBuilder();
            StringBuilder notFound = new StringBuilder();
        
            
            for (String line : results.split("\n")) {
                if (line.contains("[✓]")) {
                    found.append(line).append("\n");
                } else if (line.contains("[✗]")) {
                    notFound.append(line).append("\n");
                }
            }
        
            // Display the result in your text areas
            resultAreaFound.setText(found.toString());
            resultAreaNotFound.setText(notFound.toString());

            if (!found.toString().isEmpty()) {
                StatisticsManager.incrementVulnerabilitiesFound();
                AttackAnimation.show("sqli"); // Use "sqli" or "keylogger" for other panels
            }
            // Update statistics panel and chart
            StatisticsPanel statsPanel = new StatisticsPanel(dashboard);
            statsPanel.updateStats();
        });
        

        // Results section
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
