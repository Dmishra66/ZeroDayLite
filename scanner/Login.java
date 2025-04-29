package scanner;
import java.sql.*;
import java.util.Scanner;
import utils.PasswordUtil;

public class Login {
    // CLI version
    public static boolean authenticate(Scanner scanner) {
        System.out.print("Username: ");
        String username = scanner.nextLine();

        System.out.print("Password: ");
        String password = scanner.nextLine();

        return authenticate(username, password); // Reuse GUI method
    }
    
    public static boolean authenticate(String username, String password) {
        try (Connection conn = DBConnection.getConnection()) {
            String query = "SELECT password_hash FROM users WHERE username=?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
    
            if (rs.next()) {
                String storedHash = rs.getString("password_hash");
                String inputHash = PasswordUtil.hashPassword(password);
                return storedHash.equals(inputHash);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    
        return false;
    }

    // CLI version
    public static void register(Scanner scanner) {
        System.out.print("Choose a username: ");
        String username = scanner.nextLine();

        System.out.print("Choose a password: ");
        String password = scanner.nextLine();

        if (register(username, password)) {
            System.out.println(" Registration successful! You can now log in.");
        } else {
            System.out.println(" Registration failed.");
        }
    }

     // GUI-compatible version
    public static boolean register(String username, String password) {
        try (Connection conn = DBConnection.getConnection()) {
            // Check if username already exists
            String checkQuery = "SELECT id FROM users WHERE username=?";
            PreparedStatement checkStmt = conn.prepareStatement(checkQuery);
            checkStmt.setString(1, username);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                return false; // User already exists
            }

            String hashedPassword = PasswordUtil.hashPassword(password);
            String insertQuery = "INSERT INTO users (username, password_hash) VALUES (?, ?)";
            PreparedStatement stmt = conn.prepareStatement(insertQuery);
            stmt.setString(1, username);
            stmt.setString(2, hashedPassword);
            stmt.executeUpdate();

            return true; // Successfully registered
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public static int getUserId(String username) {
        int id = -1;
        try {
            Connection conn = DBConnection.getConnection();
            String sql = "SELECT id FROM users WHERE username = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                id = rs.getInt("id");
            }
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return id;
    }
}
