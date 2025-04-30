package gui;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import javax.swing.Timer;
import utils.StatisticsManager;

public class VulEducationPanel extends JPanel {

    private final Map<String, String> vulnerabilityDescriptions = new LinkedHashMap<>();

    public VulEducationPanel(ScanDashboard dashboard) {
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

        // Main content panel
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout());

        // Title and Description
        JLabel title = new JLabel("Vulnerability Education Panel", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setForeground(Color.WHITE);
        title.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        JLabel description = new JLabel("<html><center>Explore various cybersecurity vulnerabilities. Click each title to expand and learn about risks, examples, and fun facts.</center></html>", JLabel.CENTER);
        description.setFont(new Font("Arial", Font.ITALIC, 14));
        description.setForeground(Color.WHITE);
        description.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        headerPanel.add(title, BorderLayout.NORTH);
        headerPanel.add(description, BorderLayout.CENTER);
        contentPanel.add(headerPanel, BorderLayout.NORTH);

        // Vulnerabilities section
        populateVulnerabilities();
        // Grid of vulnerability types
        JPanel vulGrid = new JPanel(new GridLayout(5, 2, 15, 15));
        vulGrid.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));

        for (String vulName : vulnerabilityDescriptions.keySet()) {
            JPanel vulCard = createCollapsibleVulnerabilityCard(vulName);
            vulGrid.add(vulCard);
            vulGrid.add(Box.createRigidArea(new Dimension(0, 10)));
        }

        JScrollPane scrollPane = new JScrollPane(vulGrid);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setOpaque(false);
        scrollPane.setBorder(null);
        contentPanel.add(scrollPane, BorderLayout.CENTER);

        // Quiz and Back section
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));
        bottomPanel.setOpaque(false);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 30, 20, 30));

        JLabel quizLabel = new JLabel("Mini Quiz: Which vulnerability exploits buffer overflows?");
        quizLabel.setFont(new Font("Arial", Font.BOLD, 14));
        quizLabel.setForeground( Color.MAGENTA);

        JTextField quizAnswer = new JTextField();
        JButton submitBtn = new JButton("Submit Answer");

        JLabel feedbackLabel = new JLabel(" ");
        feedbackLabel.setFont(new Font("Arial", Font.BOLD, 13));

        submitBtn.addActionListener(e -> {
            String answer = quizAnswer.getText().trim().toLowerCase();
            if (answer.contains("buffer overflow") || answer.contains("stack")) {
                feedbackLabel.setText("✅ Correct! You earned 5 points.");
                feedbackLabel.setForeground(new Color(0, 220, 0));
                StatisticsManager.incrementBadgeEarned(5);
            } else {
                feedbackLabel.setText("❌ Incorrect. Try again!");
                feedbackLabel.setForeground(Color.RED);
                shakeComponent(quizAnswer);
            }
        });

        JButton backBtn = new JButton("Back");
        backBtn.addActionListener(e -> dashboard.switchToPanel("Welcome"));

        bottomPanel.add(quizLabel);
        bottomPanel.add(quizAnswer);
        bottomPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        bottomPanel.add(submitBtn);
        bottomPanel.add(feedbackLabel);
        bottomPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        bottomPanel.add(backBtn);

        contentPanel.add(bottomPanel, BorderLayout.SOUTH);

        contentWrapper.add(contentPanel, BorderLayout.CENTER);
        backgroundLabel.add(contentWrapper, BorderLayout.CENTER);

        // Resize background GIF dynamically with panel
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                Image scaled = originalIcon.getImage().getScaledInstance(getWidth(), getHeight(), Image.SCALE_DEFAULT);
                backgroundLabel.setIcon(new ImageIcon(scaled));
            }
        });
    }

    private JPanel createCollapsibleVulnerabilityCard(String name) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(new Color(255, 255, 255, 180));
        panel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        JButton toggleButton = new JButton("▶ " + name);
        toggleButton.setFocusPainted(false);
        toggleButton.setContentAreaFilled(false);
        toggleButton.setBorderPainted(false);
        toggleButton.setFont(new Font("Arial", Font.BOLD, 14));
        toggleButton.setHorizontalAlignment(SwingConstants.LEFT);

        JTextArea description = new JTextArea(vulnerabilityDescriptions.get(name));
        description.setLineWrap(true);
        description.setWrapStyleWord(true);
        description.setEditable(false);
        description.setBackground(new Color(255, 255, 255, 200));
        description.setFont(new Font("SansSerif", Font.PLAIN, 13));
        description.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        description.setVisible(false);

        toggleButton.addActionListener(e -> {
            boolean isVisible = description.isVisible();
            description.setVisible(!isVisible);
            toggleButton.setText((isVisible ? "▶" : "▼") + " " + name);
            revalidate();
            repaint();
        });

        panel.add(toggleButton, BorderLayout.NORTH);
        panel.add(description, BorderLayout.CENTER);
        return panel;
    }

    private void populateVulnerabilities() {
        vulnerabilityDescriptions.put("Buffer Overflow", 
            "Occurs when data exceeds memory buffer limits.\nExample: Stack smashing.\nFun Fact: The Morris Worm (1988) used this!");
        vulnerabilityDescriptions.put("CSRF (Cross-Site Request Forgery)", 
            "Tricks users into submitting malicious requests.\nExample: Auto money transfer when logged in.\nFun Fact: It's called 'session riding' too.");
        vulnerabilityDescriptions.put("Directory Traversal", 
            "Accesses restricted directories using path tricks.\nExample: ../../etc/passwd\nFun Fact: Common in misconfigured file servers.");
        vulnerabilityDescriptions.put("Race Condition", 
            "Exploits timing between processes to access restricted data.\nExample: Withdraw money twice.\nFun Fact: Named from CPU scheduling races.");
        vulnerabilityDescriptions.put("Clickjacking", 
            "Invisible UI elements trick users into clicks.\nExample: Like button hidden under mouse.\nFun Fact: Facebook was once vulnerable!");
        vulnerabilityDescriptions.put("Broken Authentication", 
            "Improper login/session management.\nExample: Session tokens reused.\nFun Fact: Often due to missing logout endpoints.");
        vulnerabilityDescriptions.put("Insecure Deserialization", 
            "Manipulates serialized objects to run malicious code.\nExample: Java deserialization attacks.\nFun Fact: Can lead to remote code execution.");
        vulnerabilityDescriptions.put("Command Injection", 
            "Executes system-level commands from input.\nExample: `ping; rm -rf /`\nFun Fact: Worse than SQLi if successful.");
        vulnerabilityDescriptions.put("XML External Entity (XXE)", 
            "Abuses XML parsers to access server files.\nExample: <!ENTITY x SYSTEM 'file:///etc/passwd'>\nFun Fact: Many Java libraries had this issue.");
        vulnerabilityDescriptions.put("Unvalidated Redirects", 
            "Redirects users to untrusted URLs.\nExample: phishing?goto=evil.com\nFun Fact: Hard to detect in logs.");
    }

    private void shakeComponent(Component comp) {
        Point original = comp.getLocation();
        Timer timer = new Timer(20, null);
        final int[] dx = {-4, 4, -4, 4, 0};
        final int[] count = {0};

        timer.addActionListener(e -> {
            comp.setLocation(original.x + dx[count[0]], original.y);
            count[0]++;
            if (count[0] >= dx.length) {
                comp.setLocation(original);
                ((Timer) e.getSource()).stop();
            }
        });
        timer.start();
    }
}
