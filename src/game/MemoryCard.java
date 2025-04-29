package game;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;

import javax.swing.Timer;

import java.awt.event.*;
import java.util.*;

public class MemoryCard extends JFrame {
    private List<JButton> buttons = new ArrayList<>();
    private List<String> terms = Arrays.asList("Phish", "Trojan", "Patch", "SQLi", "XSS", "Malware");
    private String firstChoice = "";
    private JButton firstButton;
    private GameMenu menu;

    public MemoryCard(GameMenu menu) {
        this.menu = menu;

        setTitle("Security Memory Card Game");
        setSize(400, 400);
        setLayout(new GridLayout(4, 3));
        setLocationRelativeTo(null);

        List<String> cardWords = new ArrayList<>(terms);
        cardWords.addAll(terms);
        Collections.shuffle(cardWords);

        for (String word : cardWords) {
            JButton button = new JButton("?");
            button.setFont(new Font("Arial", Font.BOLD, 16));
            button.putClientProperty("word", word);
            button.addActionListener(this::handleButtonClick);
            buttons.add(button);
            add(button);
        }
        setVisible(true);
    }

    private void handleButtonClick(ActionEvent e) {
        JButton clicked = (JButton) e.getSource();
        String word = (String) clicked.getClientProperty("word");
        clicked.setText(word);

        if (firstChoice.isEmpty()) {
            firstChoice = word;
            firstButton = clicked;
        } else {
            if (firstChoice.equals(word) && firstButton != clicked) {
                clicked.setEnabled(false);
                firstButton.setEnabled(false);
                firstChoice = "";
                firstButton = null;

                if (buttons.stream().allMatch(b -> !b.isEnabled())) {
                    menu.addWin("Memory Master");
                    new AnimatedPopup("Great Job! 🎉");
                    dispose();
                }
            } else {
                Timer timer = new Timer(500, event -> {
                    clicked.setText("?");
                    firstButton.setText("?");
                    firstChoice = "";
                    firstButton = null;
                });
                timer.setRepeats(false);
                timer.start();
            }
        }
    }
}
