package utils;

import javax.swing.*;
import java.awt.*;

public class AttackAnimation {

    public static void show(String vulnType) {
        JFrame frame = new JFrame("Attack Simulation: " + vulnType);
        frame.setSize(500, 300);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JTextArea animationText = new JTextArea();
        animationText.setEditable(false);
        animationText.setFont(new Font("Monospaced", Font.PLAIN, 14));

        JScrollPane scrollPane = new JScrollPane(animationText);
        frame.add(scrollPane);

        frame.setVisible(true);

        // Simulated animation
        new Thread(() -> {
            String[] steps;
            switch (vulnType.toLowerCase()) {
                case "sqli":
                    steps = new String[]{
                        "Connecting to database...",
                        "Injecting payload: ' OR 1=1 --",
                        "Bypassing login authentication...",
                        "Extracting user data...",
                        "[✓] SQL Injection attack simulated!"
                    };
                    break;
                case "xss":
                    steps = new String[]{
                        "Injecting script: <script>alert('XSS')</script>",
                        "Script executed in victim’s browser...",
                        "Session cookie accessed: JSESSIONID=abc123",
                        "Redirecting to malicious site...",
                        "[✓] XSS attack simulated!"
                    };
                    break;
                case "keylogger":
                    steps = new String[]{
                        "Keylogger injected into login form...",
                        "Recording keystrokes: u, s, e, r, p, a, s, s",
                        "Sending logs to remote server...",
                        "[✓] Keystrokes captured successfully!"
                    };
                    break;
                default:
                    steps = new String[]{"Simulating attack..."};
            }

            for (String step : steps) {
                try {
                    SwingUtilities.invokeLater(() -> animationText.append(step + "\n"));
                    Thread.sleep(800);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
