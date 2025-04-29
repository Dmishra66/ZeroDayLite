package game;

import javax.swing.*;
import java.awt.*;

public class AnimatedPopup extends JFrame {
    public AnimatedPopup(String message) {
        setUndecorated(true);
        setSize(300, 150);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JLabel label = new JLabel(message, SwingConstants.CENTER);
        label.setFont(new Font("Comic Sans MS", Font.BOLD, 20));
        label.setForeground(Color.BLUE);

        add(label, BorderLayout.CENTER);

        Timer timer = new Timer(2000, e -> dispose());
        timer.setRepeats(false);
        timer.start();

        setVisible(true);
    }
}
