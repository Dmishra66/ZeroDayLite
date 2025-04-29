package game;

import javax.swing.*;
import java.awt.*;
//import java.awt.event.*;
import java.util.Random;

public class SaveTheServer extends JFrame {
    private int serversToFix = 10;
    private int serversFixed = 0;
    private JLabel statusLabel;
    private Timer hackerTimer;
    private Random random = new Random();
    private GameMenu parentMenu;

    public SaveTheServer(GameMenu parent) {
        this.parentMenu = parent;

        setTitle("Save The Server!");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(null);

        JLabel bgLabel = new JLabel(new ImageIcon("server_room.jpg")); // <-- Add your background image here
        bgLabel.setBounds(0, 0, 800, 600);
        add(bgLabel);

        statusLabel = new JLabel("Fix Servers: 0/10", SwingConstants.CENTER);
        statusLabel.setFont(new Font("Arial", Font.BOLD, 24));
        statusLabel.setForeground(Color.WHITE);
        statusLabel.setBounds(250, 20, 300, 30);
        bgLabel.add(statusLabel);

        for (int i = 0; i < 10; i++) {
            JButton serverButton = new JButton();
            serverButton.setBounds(random.nextInt(700), random.nextInt(400) + 100, 60, 60);
            serverButton.setBackground(Color.GREEN);
            serverButton.setOpaque(true);
            serverButton.setBorderPainted(false);

            serverButton.addActionListener(e -> {
                serverButton.setEnabled(false);
                serverButton.setBackground(Color.GRAY);
                serversFixed++;
                statusLabel.setText("Fix Servers: " + serversFixed + "/10");
                if (serversFixed == serversToFix) {
                    gameWon();
                }
            });

            bgLabel.add(serverButton);
        }

        startHackerAttack(bgLabel);

        setVisible(true);
    }

    private void startHackerAttack(JLabel bgLabel) {
        hackerTimer = new Timer(1500, e -> {
            JLabel hackerFlash = new JLabel();
            hackerFlash.setBackground(Color.RED);
            hackerFlash.setOpaque(true);
            hackerFlash.setBounds(random.nextInt(700), random.nextInt(500), 50, 50);
            bgLabel.add(hackerFlash);
            bgLabel.repaint();

            Timer removeFlash = new Timer(500, ev -> {
                bgLabel.remove(hackerFlash);
                bgLabel.repaint();
            });
            removeFlash.setRepeats(false);
            removeFlash.start();
        });
        hackerTimer.start();

        // Timer for overall game timeout
        Timer timeout = new Timer(60000, e -> gameOver());
        timeout.setRepeats(false);
        timeout.start();
    }

    private void gameWon() {
        hackerTimer.stop();
        JOptionPane.showMessageDialog(this, "🎉 Server Saved Successfully!", "Victory", JOptionPane.INFORMATION_MESSAGE);
        parentMenu.addWin("Server Defender Badge 🛡️");
        dispose();
    }

    private void gameOver() {
        if (serversFixed < serversToFix) {
            hackerTimer.stop();
            JOptionPane.showMessageDialog(this, "💥 Server Crashed... Hackers Won!", "Defeat", JOptionPane.ERROR_MESSAGE);
            dispose();
        }
    }
}
