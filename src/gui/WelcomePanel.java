package gui;

import utils.Styles;
import javax.swing.*;
import java.awt.*;

public class WelcomePanel extends JPanel {

    public ImageIcon scaledBackgroundIcon;

    public WelcomePanel(ScanDashboard dashboard, String username) {
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

        JLabel titleLabel = new JLabel("Welcome to the Vulnerability Scanner " + username + "!", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        contentWrapper.add(titleLabel, BorderLayout.NORTH);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);

        JTextArea descText = new JTextArea(
            "Hi! " + username + "\n\nWelcome to the vulnerability explorer\n" +
            "\n\nThis tool helps demonstrate and educate about common vulnerabilities in a gamified manner.\n\n" +
            "This tool provides simulation scanning for 3 vulnerabilities. Click on them and learn about them individually."
        );
        descText.setWrapStyleWord(true);
        descText.setLineWrap(true);
        descText.setEditable(false);
        descText.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        descText.setBackground(new Color( 130, 224, 170));
        descText.setForeground(new Color(23, 32, 42));
        descText.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        SecurityTipCard tipCard = new SecurityTipCard();
        tipCard.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel buttonPanel = new JPanel(new GridLayout(3, 2, 20, 20));
        buttonPanel.setOpaque(false);

        JButton sqlScanButton = new JButton("SQL Injection Scan");
        Styles.styleButton(sqlScanButton);
        sqlScanButton.addActionListener(e -> dashboard.switchToPanel("sql"));

        JButton xssScanButton = new JButton("XSS Scan");
        Styles.styleButton(xssScanButton);
        xssScanButton.addActionListener(e -> dashboard.switchToPanel("xss"));

        JButton keyloggerScanButton = new JButton("Keylogger Scan");
        Styles.styleButton(keyloggerScanButton);
        keyloggerScanButton.addActionListener(e -> dashboard.switchToPanel("keylogger"));

        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> dashboard.logout());

        JButton vulnEduButton = new JButton("Vulnerability Education");
        Styles.styleButton(vulnEduButton);
        vulnEduButton.addActionListener(e -> dashboard.switchToPanel("education"));

        JButton realTimeScanButton = new JButton("Real-Time Web Security Scanner");
        realTimeScanButton.setFont(new Font("Arial", Font.BOLD, 18));
        realTimeScanButton.setBackground(new Color(26, 188, 156));
        realTimeScanButton.setForeground(Color.WHITE);
        realTimeScanButton.addActionListener(e -> dashboard.switchToPanel("RealTime"));

        JButton inputCheckerButton = new JButton("Real-Time Typing Monitor");
        inputCheckerButton.addActionListener(e -> dashboard.switchToPanel("typing"));

        JButton payloadBuilderButton = new JButton("Build Your Own Payload");
        payloadBuilderButton.setFont(new Font("Arial", Font.BOLD, 18));
        payloadBuilderButton.addActionListener(e -> dashboard.switchToPanel("payloadbuilder"));

        JButton statsButton = new JButton("View Statistics");
        statsButton.setFont(new Font("Arial", Font.BOLD, 18));
        statsButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        statsButton.addActionListener(e -> dashboard.switchToPanel("statistics"));

        buttonPanel.add(statsButton);
        buttonPanel.add(payloadBuilderButton);
        buttonPanel.add(inputCheckerButton);
        buttonPanel.add(realTimeScanButton);
        buttonPanel.add(sqlScanButton);
        buttonPanel.add(xssScanButton);
        buttonPanel.add(keyloggerScanButton);
        buttonPanel.add(vulnEduButton);
        buttonPanel.add(logoutButton);

        contentPanel.add(descText);
        contentPanel.add(Box.createVerticalStrut(10));
        contentPanel.add(tipCard);
        contentPanel.add(Box.createVerticalStrut(10));
        contentPanel.add(buttonPanel);

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
}
