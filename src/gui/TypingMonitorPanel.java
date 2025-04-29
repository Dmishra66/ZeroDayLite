package gui;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.Timer;

import utils.StatisticsManager;

public class TypingMonitorPanel extends JPanel {

    private JTextArea typingArea;
    private JLabel keystrokeLabel;
    private JLabel dataSentLabel;
    private JLabel listenerStatusLabel;

    private JTextArea capturedKeystrokesArea;
    private JTextArea serverConsoleArea;
    private java.util.List<String> capturedKeys = new ArrayList<>();

    private java.util.List<Integer> arrowsX = new ArrayList<>();
    private Timer arrowTimer;
    private int keystrokesCaptured = 0;
    private int dataSent = 0; // Simulated data size
    private boolean listenerActive = true;

    public TypingMonitorPanel(ScanDashboard dashboard) {

        setLayout(new BorderLayout());
        initComponents(dashboard);
        initArrowAnimation();
    }

    private void initComponents(ScanDashboard dashboard) {
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

        // Top title and description
        JPanel topPanel = new JPanel(new BorderLayout());
        JButton backButton = new JButton("Back");
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        backButton.addActionListener(e -> dashboard.switchToPanel("Welcome"));
        topPanel.add(backButton, BorderLayout.WEST);

        JLabel titleLabel = new JLabel("Real-Time Typing Monitor Simulation", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        JLabel descriptionLabel = new JLabel("<html><center>Simulating how keyloggers capture data and send it to a server.<br>Type sensitive information below to see the simulation!</center></html>", JLabel.CENTER);
        topPanel.add(titleLabel, BorderLayout.NORTH);
        topPanel.add(descriptionLabel, BorderLayout.SOUTH);
        contentWrapper.add(topPanel, BorderLayout.NORTH);

        // Center typing + visualization
        JPanel centerPanel = new JPanel(new GridLayout(1, 2));

        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBackground(Color.WHITE); // Set your background color if needed
        // Left Typing Area
        JPanel typingPanel = new JPanel(new BorderLayout());
        typingArea = new JTextArea(10, 20);
        typingArea.setLineWrap(true);
        typingArea.setWrapStyleWord(true);
        typingArea.setFont(new Font("Monospaced", Font.PLAIN, 16));
        typingArea.setBackground(new Color( 153, 163, 164));
        typingArea.addKeyListener(new TypingKeyListener());

        JScrollPane typingScrollPane = new JScrollPane(typingArea);

        // Help Dropdown
        String[] suggestions = { "Enter password", "Enter credit card number", "Enter secret code", "Type anything..." };
        JComboBox<String> helpDropdown = new JComboBox<>(suggestions);

        typingPanel.add(helpDropdown, BorderLayout.NORTH);
        typingPanel.add(typingScrollPane, BorderLayout.CENTER);

        leftPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        leftPanel.add(typingPanel);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // --- Challenge Area (Below Typing Area) ---
        JPanel challengePanel = new JPanel();
        challengePanel.setLayout(new BoxLayout(challengePanel, BoxLayout.Y_AXIS));
        challengePanel.setBorder(BorderFactory.createTitledBorder("Challenge Area"));
        challengePanel.setBackground(new Color(240, 248, 255)); // Light blue background

        // Challenge Question
        JLabel challengeQuestionLabel = new JLabel("<html><b>Challenge:</b> <br>What is the risk if typed data is captured in plaintext?</html>");
        challengeQuestionLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        challengeQuestionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Answer Field
        JTextField challengeAnswerField = new JTextField();
        challengeAnswerField.setMaximumSize(new Dimension(250, 25));
        challengeAnswerField.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Submit Button
        JButton submitAnswerButton = new JButton("Submit Answer");
        submitAnswerButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Add everything into Challenge Panel
        challengePanel.add(Box.createRigidArea(new Dimension(0, 10)));
        challengePanel.add(challengeQuestionLabel);
        challengePanel.add(Box.createRigidArea(new Dimension(0, 10)));
        challengePanel.add(challengeAnswerField);
        challengePanel.add(Box.createRigidArea(new Dimension(0, 10)));
        challengePanel.add(submitAnswerButton);

        leftPanel.add(challengePanel);
        // --- ActionListener for Checking Answer ---
        submitAnswerButton.addActionListener(e -> {
             String userAnswer = challengeAnswerField.getText().trim().toLowerCase();
            if (userAnswer.contains("plaintext") || userAnswer.contains("unencrypted") || userAnswer.contains("exposed")) {
                 // Correct Answer
                correctAnswerAnimation(challengeAnswerField);
                badgesEarned("Typing Monitor Simulation");
                JOptionPane.showMessageDialog(leftPanel, 
                    "🎉 Typing Monitor Simulation Completed!\nYou earned 20 points!", 
                         "Success", 
                JOptionPane.INFORMATION_MESSAGE);
            showDidYouKnowFact();
            } else {
             // Wrong Answer
                wrongAnswerShakeAnimation(challengeAnswerField);
            }
        });


        centerPanel.add(leftPanel);

        // Right Visualizer
        JPanel visualizerPanel = new JPanel(new BorderLayout());

        // Captured Keystrokes
        capturedKeystrokesArea = new JTextArea();
        capturedKeystrokesArea.setEditable(false);
        capturedKeystrokesArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        JScrollPane capturedScrollPane = new JScrollPane(capturedKeystrokesArea);
        capturedScrollPane.setPreferredSize(new Dimension(400, 150));
        visualizerPanel.add(capturedScrollPane, BorderLayout.NORTH);

        // Arrows and server console
        JPanel arrowsAndServerPanel = new JPanel(new BorderLayout());
        ArrowAnimationPanel arrowPanel = new ArrowAnimationPanel();
        arrowPanel.setPreferredSize(new Dimension(400, 100));
        arrowsAndServerPanel.add(arrowPanel, BorderLayout.NORTH);

        serverConsoleArea = new JTextArea();
        serverConsoleArea.setEditable(false);
        serverConsoleArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
        serverConsoleArea.setBackground(Color.BLACK);
        serverConsoleArea.setForeground(Color.GREEN);
        serverConsoleArea.setBorder(BorderFactory.createTitledBorder("Server Console(Fake)"));
        JScrollPane serverConsoleScroll = new JScrollPane(serverConsoleArea);
        serverConsoleScroll.setPreferredSize(new Dimension(400, 400));
        arrowsAndServerPanel.add(serverConsoleScroll, BorderLayout.SOUTH);

        visualizerPanel.add(arrowsAndServerPanel, BorderLayout.CENTER);

        centerPanel.add(visualizerPanel);

        contentWrapper.add(centerPanel, BorderLayout.CENTER);

        // Bottom metrics
        JPanel metricsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 5));
        keystrokeLabel = new JLabel("Keystrokes: 0");
        dataSentLabel = new JLabel("Data Sent: 0 KB");
        listenerStatusLabel = new JLabel("Listener: Active");

        metricsPanel.add(keystrokeLabel);
        metricsPanel.add(dataSentLabel);
        metricsPanel.add(listenerStatusLabel);

        contentWrapper.add(metricsPanel, BorderLayout.SOUTH);
        backgroundLabel.add(contentWrapper, BorderLayout.CENTER);

        // Resize background GIF dynamically with panel
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                Image scaled = originalIcon.getImage().getScaledInstance(getWidth(), getHeight(), Image.SCALE_DEFAULT);
                backgroundLabel.setIcon(new ImageIcon(scaled));
            }
        });
    }

    private void initArrowAnimation() {
        // Move arrows every 30ms
        arrowTimer = new Timer(30, e -> {
            for (int i = 0; i < arrowsX.size(); i++) {
                arrowsX.set(i, arrowsX.get(i) + 5); // Move right
            }
            repaint();
        });
        arrowTimer.start();
    }

    private class TypingKeyListener extends KeyAdapter {
        @Override
        public void keyTyped(KeyEvent e) {
            char c = e.getKeyChar();
            capturedKeys.add(String.valueOf(c));
            capturedKeystrokesArea.append(c + " ");
            keystrokesCaptured++;
            dataSent += 2; // Assume 2 bytes per keystroke
            updateMetrics();

            // Occasionally simulate sending data to server
            if (capturedKeys.size()  != 0) {
                simulateServerSend();
            }

            // Add a new arrow
            arrowsX.add(0);
        }
    }

    private void simulateServerSend() {
        StringBuilder packet = new StringBuilder();
        for (String s : capturedKeys) {
            packet.append(s);
        }
        capturedKeys.clear(); // Clear after sending

        serverConsoleArea.append("[Server] Received: \"" + packet.toString() + "\"\n");
    }

    private void updateMetrics() {
        keystrokeLabel.setText("Keystrokes: " + keystrokesCaptured);
        dataSentLabel.setText("Data Sent: " + (dataSent / 1024) + " KB");
        listenerStatusLabel.setText("Listener: " + (listenerActive ? "Active" : "Inactive"));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Draw arrows if any
        g.setColor(Color.BLUE);
        for (int x : arrowsX) {
            g.drawString("➔", x, getHeight() / 2); // Adjust Y position if needed
        }
    }

    private class ArrowAnimationPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setColor(Color.BLUE);
            for (int x : arrowsX) {
                g.drawString("➔", x, getHeight() / 2);
            }
        }
    }
    // --- Animations and Utility Methods ---
private void correctAnswerAnimation(JTextField field) {
    Color original = field.getBackground();
    field.setBackground(Color.GREEN);
    Timer timer = new Timer(500, e -> field.setBackground(original));
    timer.setRepeats(false);
    timer.start();
}

private void wrongAnswerShakeAnimation(JTextField field) {
    Point originalLocation = field.getLocation();
    Timer timer = new Timer(20, new ActionListener() {
        int count = 0;
        @Override
        public void actionPerformed(ActionEvent e) {
            int offset = (count % 2 == 0) ? 5 : -5;
            field.setLocation(originalLocation.x + offset, originalLocation.y);
            count++;
            if (count > 5) {
                ((Timer) e.getSource()).stop();
                field.setLocation(originalLocation);
            }
        }
    });
    timer.start();
}

private void badgesEarned(String simulationName) {
    StatisticsManager.incrementBadgeEarned();
   // utils.UserProgress.completeSimulation(simulationName);
}

private void showDidYouKnowFact() {
    JOptionPane.showMessageDialog(this, 
        "💡 Did you know?\nMost data breaches occur because companies store passwords in plaintext instead of hashing them securely!",
        "Security Fact", 
        JOptionPane.INFORMATION_MESSAGE);
}
}