package game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class BugZapper extends JFrame {
    private GameMenu menu;
    private int bugsZapped = 0;
    private Random random = new Random();

    public BugZapper(GameMenu menu) {
        this.menu = menu;
        setTitle("Bug Zapper Game");
        setSize(500, 500);
        setLocationRelativeTo(null);
        setLayout(null);

        Timer spawnTimer = new Timer(700, e -> spawnBug());
        spawnTimer.start();

        setVisible(true);
    }

    private void spawnBug() {
        JLabel bug = new JLabel("🐞");
        bug.setFont(new Font("Arial", Font.PLAIN, 24));
        bug.setBounds(random.nextInt(450), random.nextInt(450), 30, 30);
        bug.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                bugsZapped++;
                remove(bug);
                repaint();
                if (bugsZapped >= 10) {
                    menu.addWin("Bug Buster");
                    new AnimatedPopup("Bugs Zapped! 🐛✨");
                    dispose();
                }
            }
        });
        add(bug);
        repaint();
    }
}
