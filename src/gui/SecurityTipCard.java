package gui;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class SecurityTipCard extends JPanel {
    private JLabel iconLabel;
    private JLabel textLabel;
    private List<String> tips = List.of(
        "💡 Sanitize user input before processing.",
        "🔒 Use HTTPS for secure communication.",
        "🔑 Apply the principle of least privilege.",
        "🛡️ Regularly update dependencies.",
        "📦 Use security headers in HTTP responses."
    );
    private int currentTipIndex = 0;
    private float alpha = 1f;

    public SecurityTipCard() {
        setLayout(new BorderLayout(10, 0));
        setBackground(new Color(241, 148, 138));
        setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 255), 2),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        setOpaque(true);


       // setBackground(new Color(240, 248, 255)); // light blue card
        //setBorder(BorderFactory.createCompoundBorder(
          //  BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1),
            //BorderFactory.createEmptyBorder(15, 20, 15, 20)
        //));

        iconLabel = new JLabel();
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 20));
        iconLabel.setVerticalAlignment(SwingConstants.CENTER);

        textLabel = new JLabel(" ");
        textLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        add(iconLabel, BorderLayout.WEST);
        add(textLabel, BorderLayout.CENTER);

        updateTip();
        startRotation();
    }

    private void updateTip() {
        String tip = tips.get(currentTipIndex);
        iconLabel.setText(tip.substring(0, 2)); // emoji/icon
        textLabel.setText(tip.substring(2).trim()); // tip text
        alpha = 0f;
        startFadeIn();
    }

    private void startRotation() {
        Timer switchTimer = new Timer(5000, e -> {
            currentTipIndex = (currentTipIndex + 1) % tips.size();
            updateTip();
        });
        switchTimer.start();
    }

    private void startFadeIn() {
        alpha = 0f;  // Reset alpha at the start
        Timer fadeTimer = new Timer(40, null);  // Smoother animation ~25fps
        fadeTimer.addActionListener(e -> {
            alpha += 0.05f;
            if (alpha >= 1f) {
                alpha = 1f;
                fadeTimer.stop();
            }
            repaint();
        });
        fadeTimer.start();
    }
    

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        super.paintComponent(g2);  // this is safe now
        g2.dispose();
    }
}
