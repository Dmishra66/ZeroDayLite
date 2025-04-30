package scanner;

import java.io.*;
import java.time.LocalDate;
import java.util.Scanner;

public class Login {

    // Authenticate user based on username and password
    public static boolean authenticate(String username, String password) {
        File file = new File("assets/users.txt"); // Ensure this path exists
        if (!file.exists()) return false;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] creds = line.split("\\|");
                if (creds.length >= 2 && creds[0].equals(username) && creds[1].equals(password)) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Register new user with default stats
    public static boolean register(String username, String password) {
        File file = new File("assets/users.txt");

        // Ensure the file and folder exist
        try {
            File folder = new File("assets");
            if (!folder.exists()) folder.mkdirs();
            if (!file.exists()) file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split("\\|");
                if (parts.length >= 1 && parts[0].equals(username)) {
                    return false; // Username already taken
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        // Write new user line
        try (FileWriter writer = new FileWriter(file, true)) {
            String defaultStats = "0,0,0,0,0,0,0,1,0," + LocalDate.now(); // stats: 10 fields
            writer.write(username + "|" + password + "|" + defaultStats + "\n");
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
