package gui;

import utils.StatisticsManager;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.awt.*;

public class StatisticsPanel extends JPanel {
    private JLabel totalVulnLabel, totalScansLabel, keystrokesCapturedLabel, payloadsBuiltLabel;
    private JLabel sqliScanLabel, xssScanLabel, keyloggerScanLabel;
    private JLabel dailyStreakLabel, badgesEarnedLabel;

    private JPanel chartContainer;
    private DefaultCategoryDataset scanDataset;
    private DefaultCategoryDataset payloadDataset;
    private DefaultCategoryDataset keystrokeDataset;
    private JLabel totalStatsLabel;

    public StatisticsPanel(ScanDashboard dashboard) {
        setLayout(new BorderLayout( ));

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

        JLabel title = new JLabel("Dashboard Statistics");
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setForeground(Color.WHITE);
        contentWrapper.add(title, BorderLayout.NORTH);

        JPanel metricsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 30));
        metricsPanel.setBackground(Color.BLUE);
        // Labels for displaying stats
        totalVulnLabel = new JLabel("Total Vulnerabilities Found : ");
        totalScansLabel = new JLabel("Total Scans Done : ");
        keystrokesCapturedLabel = new JLabel("Keystrokes Captured : ");
        payloadsBuiltLabel = new JLabel("Payloads Build : ");
        sqliScanLabel = new JLabel("SQL Injection Scans : ");
        xssScanLabel = new JLabel("XSS Scans Scans : ");
        keyloggerScanLabel = new JLabel("Keylogger Scans : ");
        dailyStreakLabel = new JLabel("Daily Streak : ");
        badgesEarnedLabel = new JLabel("Badges Earned : ");



        // Setting font and alignment for labels
        totalVulnLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        totalScansLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        keystrokesCapturedLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        payloadsBuiltLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        sqliScanLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        xssScanLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        keyloggerScanLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        dailyStreakLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        badgesEarnedLabel.setFont(new Font("Arial", Font.PLAIN, 16));

        // Refresh and Back Buttons
        JPanel navPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        navPanel.setOpaque(false);

        JButton refreshButton = new JButton("Refresh");
        refreshButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        refreshButton.addActionListener(e -> updateStats());

        JButton backButton = new JButton("Back");
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        backButton.addActionListener(e -> dashboard.switchToPanel("Welcome"));
        navPanel.add(refreshButton);
        navPanel.add(backButton);

        // Total stats label
        totalStatsLabel = new JLabel();
        totalStatsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        updateTotalStatsLabel();
        metricsPanel.add(totalStatsLabel);
        metricsPanel.add(dailyStreakLabel);
        metricsPanel.add(badgesEarnedLabel);
        metricsPanel.add(sqliScanLabel);
        metricsPanel.add(xssScanLabel);
        metricsPanel.add(keyloggerScanLabel);
        metricsPanel.setBackground(new Color(240, 248, 255));

        // Creating the chart container panel
        chartContainer = new JPanel();
        chartContainer.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));

        // Create datasets for the charts
        scanDataset = new DefaultCategoryDataset();
        payloadDataset = new DefaultCategoryDataset();
        keystrokeDataset = new DefaultCategoryDataset();

        // Create charts for individual metrics
        JFreeChart scanChart = createLineChart(scanDataset, "Scan Types Over Time", "Scans", "Count");
        JFreeChart payloadChart = createLineChart(payloadDataset, "Payloads Built Over Time", "Payloads", "Count");
        JFreeChart keystrokeChart = createLineChart(keystrokeDataset, "Keystrokes Captured Over Time", "Keystrokes", "Count");

        // Add chart panels to the container
        chartContainer.add(createChartCard(scanChart));
        chartContainer.add(createChartCard(payloadChart));
        chartContainer.add(createChartCard(keystrokeChart));



        // Create a panel with vertical BoxLayout to hold metrics and charts
        JPanel centerPanel = new JPanel();
        centerPanel.setOpaque(false);
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));

        // Add metrics and chart to center panel
        centerPanel.add(metricsPanel);
        centerPanel.add(chartContainer);

        // Add to content wrapper
        contentWrapper.add(centerPanel, BorderLayout.CENTER);

        contentWrapper.add(navPanel, BorderLayout.SOUTH);
        backgroundLabel.add(contentWrapper, BorderLayout.CENTER);


        updateStats();
        // Resize background GIF dynamically with panel
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                Image scaled = originalIcon.getImage().getScaledInstance(getWidth(), getHeight(), Image.SCALE_DEFAULT);
                backgroundLabel.setIcon(new ImageIcon(scaled));
            }
        });
    }
    // Method to create a line chart
    private JFreeChart createLineChart(DefaultCategoryDataset dataset, String title, String categoryAxisLabel, String valueAxisLabel) {
        return ChartFactory.createLineChart(
                title, // chart title
                categoryAxisLabel, // category axis label
                valueAxisLabel, // value axis label
                dataset // dataset
        );
    }

    private JPanel createChartCard(JFreeChart chart) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 2)); // Light border
        card.setPreferredSize(new Dimension(300, 250)); // Fixed size for a square/rectangle look
    
        ChartPanel chartPanel = new ChartPanel(chart);
        card.add(chartPanel, BorderLayout.CENTER);
    
        return card;
    }
    

    // Update all statistics dynamically
    public void updateStats() {
        totalVulnLabel.setText("🔍 Total Vulnerabilities Found: " + StatisticsManager.getVulnerabilitiesFound());
        totalScansLabel.setText("📊 Total Scans: " + StatisticsManager.getTotalScans());
        keystrokesCapturedLabel.setText("⌨️ Keystrokes Captured: " + StatisticsManager.getKeystrokesCaptured());
        payloadsBuiltLabel.setText("🛠️ Payloads Built: " + StatisticsManager.getPayloadsBuilt());

        sqliScanLabel.setText("SQLi Scans: " + StatisticsManager.getSqliScans());
        xssScanLabel.setText("XSS Scans: " + StatisticsManager.getXssScans());
        keyloggerScanLabel.setText("Keylogger Scans: " + StatisticsManager.getKeyloggerScans());

        dailyStreakLabel.setText("📅 Daily Streak: " + StatisticsManager.getDailyStreak());
        badgesEarnedLabel.setText("🏅 Badges Earned: " + StatisticsManager.getBadgesEarned());
    
        // Clear previous data to avoid stacking new data on top of old data
        scanDataset.clear();
        payloadDataset.clear();
        keystrokeDataset.clear();

        // Update the stats dataset for SQLi, XSS, and Keylogger scans
        scanDataset.addValue(StatisticsManager.getSqliScans(), "SQLi Scans", "Scans");
        scanDataset.addValue(StatisticsManager.getXssScans(), "XSS Scans", "Scans");
        scanDataset.addValue(StatisticsManager.getKeyloggerScans(), "Keylogger Scans", "Scans");

        // Update the payload dataset
        payloadDataset.addValue(StatisticsManager.getPayloadsBuilt(), "Payloads Built", "Count");

        // Update the keystroke dataset
        keystrokeDataset.addValue(StatisticsManager.getKeystrokesCaptured(), "Keystrokes Captured", "Count");

        // Update the total stats label
        updateTotalStatsLabel();

        // Revalidate and repaint the panel to update the charts
        revalidate();
        repaint();
    }

    // Add a method for updating charts based on scan type (SQL, XSS, Keylogger, etc.)
    public void updateChart(String type) {
        // Update chart based on the scan type
        // Example: Update chart when SQLi scan is incremented, etc.
    }
    // Update the total stats label
    private void updateTotalStatsLabel() {
        String totalStatsText = "<html><h2>Total Stats:</h2>" +
                "<p>Total Scans: " + StatisticsManager.getTotalScans() + "</p>" +
                "<p>Total Vulnerabilities Found: " + StatisticsManager.getVulnerabilitiesFound() + "</p>" +
                "<p>Total Keystrokes Captured: " + StatisticsManager.getKeystrokesCaptured() + "</p>" +
                "<p>Total Payloads Built: " + StatisticsManager.getPayloadsBuilt() + "</p></html>";

        totalStatsLabel.setText(totalStatsText);
    }
}
