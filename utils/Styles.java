// File: utils/StyleUtils.java
package utils;

import javax.swing.JButton;
import javax.swing.BorderFactory;
import java.awt.*;

public class Styles{

    public static void styleButton(JButton button) {
        button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        button.setFocusPainted(false);
        button.setBackground(new Color(26, 188, 156)); // Google blue
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
}
