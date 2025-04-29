package gui;

import scanner.Login;
import utils.StatisticsManager;

import javax.swing.*;
import java.awt.*;

public class LoginForm extends JFrame {
    public JTextField usernameField;
    private JPasswordField passwordField;

    public LoginForm() {
        setTitle("Vulnerability Scanner - Login");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel userLabel = new JLabel("Username:");
        JLabel passLabel = new JLabel("Password:");
        usernameField = new JTextField();
        passwordField = new JPasswordField();

        JButton loginButton = new JButton("Login");
        JButton registerButton = new JButton("Register");

        loginButton.addActionListener(e -> handleLogin());
        registerButton.addActionListener(e -> handleRegister());

        panel.add(userLabel);
        panel.add(usernameField);
        panel.add(passLabel);
        panel.add(passwordField);
        panel.add(loginButton);
        panel.add(registerButton);

        add(panel);
        setVisible(true);
    }

    private void handleLogin() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        if (Login.authenticate(username, password)) {
            int userId = Login.getUserId(username); // <-- this must get id from database
            StatisticsManager.setCurrentUserId(userId);
            StatisticsManager.loadUserStats(userId);
        
            JOptionPane.showMessageDialog(this, "Login successful!");
            dispose();
            SwingUtilities.invokeLater(() -> new ScanDashboard(username).setVisible(true));
    } else {
        JOptionPane.showMessageDialog(this, "Invalid credentials.", "Error", JOptionPane.ERROR_MESSAGE);
    }
    }
    

    private void handleRegister() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        if (Login.register(username, password)) {
            JOptionPane.showMessageDialog(this, "Registration successful! You can now log in.");
        } else {
            JOptionPane.showMessageDialog(this, "Registration failed. Try a different username.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginForm().setVisible(true));
    }
}

