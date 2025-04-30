package gui;

import javax.swing.*;


import utils.StatisticsManager;

import java.awt.*;


public class ScanDashboard extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private StatisticsPanel statisticsPanel;


    public ScanDashboard(String username) {
        setTitle("Vulnerability Scanner Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 700);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Panels
        WelcomePanel welcomePanel = new WelcomePanel(this, username);
        SqlInjectionScanPanel sqliPanel = new SqlInjectionScanPanel(this, username);
        XssScanPanel xssPanel = new XssScanPanel(this, username);
        KeyloggerScanPanel keyloggerPanel = new KeyloggerScanPanel(this, username);
        VulEducationPanel vuleduPanel = new VulEducationPanel(this);
        RealTimeScannerPanel realTimePanel = new RealTimeScannerPanel(this);
        TypingMonitorPanel typingPanel = new TypingMonitorPanel(this);
       // GameMenu gamePanel = new GameMenu(this);
        
        mainPanel.add(new StatisticsPanel(this), "statistics");
        mainPanel.add(new PayloadBuilderPanel(this), "payloadbuilder");
        mainPanel.add(typingPanel, "typing");
        
        mainPanel.add(realTimePanel, "RealTime");

        // Add to main panel
        mainPanel.add(welcomePanel, "Welcome");
        mainPanel.add(sqliPanel, "sql");
        mainPanel.add(xssPanel, "xss");
        mainPanel.add(keyloggerPanel, "keylogger");
        mainPanel.add(vuleduPanel, "education");
      //  mainPanel.add(gamePanel, "game");

        add(mainPanel);
        cardLayout.show(mainPanel, "Welcome");

        setVisible(true);
    }
    public StatisticsPanel getStatisticsPanel() {
        return statisticsPanel;
    }
    

    public void switchToPanel(String panelName) {
        cardLayout.show(mainPanel, panelName);
    }

    public void logout() {
        dispose();
        StatisticsManager.saveStats();
        new LoginForm();
    }
} 
