package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.LinkedList;
import java.util.Base64;

import utils.StatisticsManager;
//import java.awt.datatransfer.*;

public class PayloadBuilderPanel extends JPanel {
    private JComboBox<String> typeSelector;
    private JTextArea payloadArea, previewArea;
    private JCheckBox obfuscateToggle;
    private DefaultListModel<String> historyModel;
    private JLabel damageMeter;

    private final LinkedList<String> history = new LinkedList<>();

    public PayloadBuilderPanel(ScanDashboard dashboard) {
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

        JLabel title = new JLabel("💣 Build Your Own Payload");
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setForeground(Color.WHITE);
        title.setHorizontalAlignment(SwingConstants.CENTER);
        contentWrapper.add(title, BorderLayout.NORTH);

        JPanel builderPanel = new JPanel(new BorderLayout(15, 15));
        builderPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        // --- Top controls ---
        JPanel controlsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        controlsPanel.setOpaque(false);

        typeSelector = new JComboBox<>(new String[]{"Select Type", "SQL Injection", "XSS", "Custom"});
        typeSelector.setToolTipText("Choose a payload type to load a sample template");
        typeSelector.addActionListener(e -> loadTemplate());

        obfuscateToggle = new JCheckBox("Obfuscate");
        obfuscateToggle.setOpaque(false);
        obfuscateToggle.setForeground(Color.WHITE);
        obfuscateToggle.setToolTipText("Toggle basic payload obfuscation (e.g., base64)");

        controlsPanel.add(new JLabel("Payload Type:"));
        controlsPanel.add(typeSelector);
        controlsPanel.add(obfuscateToggle);

        builderPanel.add(controlsPanel, BorderLayout.NORTH);

        // --- Center Editor and Preview ---
        JPanel textPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        textPanel.setOpaque(false);

        payloadArea = new JTextArea("Enter your payload here...");
        payloadArea.setLineWrap(true);
        payloadArea.setWrapStyleWord(true);
        payloadArea.setToolTipText("Type or modify your payload");
        payloadArea.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                updatePreview();
            }
        });

        previewArea = new JTextArea();
        previewArea.setEditable(false);
        previewArea.setToolTipText("Live preview of your payload (obfuscated if selected)");

        JScrollPane inputScroll = new JScrollPane(payloadArea);
        JScrollPane previewScroll = new JScrollPane(previewArea);

        textPanel.add(inputScroll);
        textPanel.add(previewScroll);

        builderPanel.add(textPanel, BorderLayout.CENTER);

        // --- Bottom buttons and history ---
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setOpaque(false);

        JButton buildButton = new JButton("🔧 Build Payload");
        buildButton.addActionListener(e -> buildPayload());

        JButton backButton = new JButton("← Back");
        backButton.addActionListener(e -> dashboard.switchToPanel("Welcome"));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setOpaque(false);
        buttonPanel.add(buildButton);
        buttonPanel.add(backButton);

        // Damage meter as a fun indicator
        damageMeter = new JLabel("💥 Risk Level: Low");
        damageMeter.setFont(new Font("Arial", Font.BOLD, 14));
        damageMeter.setForeground(Color.RED);

        // Payload history
        historyModel = new DefaultListModel<>();
        JList<String> historyList = new JList<>(historyModel);
        historyList.setBorder(BorderFactory.createTitledBorder("Recent Payloads"));
        historyList.setToolTipText("Click to reload a past payload");
        historyList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                String selected = historyList.getSelectedValue();
                if (selected != null) {
                    payloadArea.setText(selected);
                    updatePreview();
                }
            }
        });

        bottomPanel.add(damageMeter, BorderLayout.WEST);
        bottomPanel.add(buttonPanel, BorderLayout.EAST);
        bottomPanel.add(new JScrollPane(historyList), BorderLayout.SOUTH);

        builderPanel.add(bottomPanel, BorderLayout.SOUTH);
        contentWrapper.add(builderPanel, BorderLayout.CENTER);
        backgroundLabel.add(contentWrapper, BorderLayout.CENTER);

        // Resize background GIF dynamically with panel
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                Image scaled = originalIcon.getImage().getScaledInstance(getWidth(), getHeight(), Image.SCALE_DEFAULT);
                backgroundLabel.setIcon(new ImageIcon(scaled));
            }
        });
    }

    private void loadTemplate() {
        String selected = (String) typeSelector.getSelectedItem();
        if (selected == null) return;

        switch (selected) {
            case "SQL Injection":
                payloadArea.setText("1' OR '1'='1'; --");
                break;
            case "XSS":
                payloadArea.setText("<script>alert('XSS')</script>");
                break;
            case "Custom":
                payloadArea.setText("");
                break;
        }
        updatePreview();
    }

    private void updatePreview() {
        String text = payloadArea.getText();
        if (obfuscateToggle.isSelected()) {
            text = Base64.getEncoder().encodeToString(text.getBytes());
        }
        previewArea.setText(text);
        updateDamageMeter(text);
    }

    private void buildPayload() {
        String payload = payloadArea.getText();
        if (payload.isEmpty()) return;

        if (history.size() >= 5) history.removeFirst();
        history.add(payload);
        historyModel.clear();
        for (String item : history) historyModel.addElement(item);

        JOptionPane.showMessageDialog(this, "✅ Payload built successfully!");
        updatePreview();
        StatisticsManager.incrementPayloadsBuilt();
    }

    private void updateDamageMeter(String payload) {
        if (payload.contains("<script>") || payload.contains("OR '1'='1")) {
            damageMeter.setText("💥 Risk Level: High");
            damageMeter.setForeground(Color.RED);
        } else if (payload.length() > 20) {
            damageMeter.setText("⚠️ Risk Level: Medium");
            damageMeter.setForeground(Color.ORANGE);
        } else {
            damageMeter.setText("🛡️ Risk Level: Low");
            damageMeter.setForeground(Color.GREEN);
        }
    }
}
