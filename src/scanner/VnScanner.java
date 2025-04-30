package scanner;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class VnScanner {

    public static String scanSQLInjection(String username) {
        String[] testInputs = {
            "' OR '1'='1",
            "'; DROP TABLE users; --",
            "admin' --",
            "' OR 1=1 --",
            "normalInput",
            "' UNION SELECT null, null, null --",
            "' AND 1=0 UNION SELECT username, password, NULL FROM users --",
            "' OR '1'='1",
            "hello",
            "SELECT * FROM users WHERE username = 'admin'",
            "DROP TABLE users"
        };

        System.out.println("\n Simulated SQL inputs:");
        for (String input : testInputs) {
            System.out.println(" - " + input);
        }

        StringBuilder result = new StringBuilder("\n SQL Injection Scan Results:\n");
        for (String input : testInputs) {
            if (input.contains("' OR '") || input.toLowerCase().contains("drop table") || input.toLowerCase().contains("select *")) {
                result.append("[✓] Injection pattern found in: ").append(input).append("\n");
            } else {
                result.append("[✗] Safe input: ").append(input).append("\n");
            }
        }

        String finalResult = result.toString();
        logScan(username, "SQL Injection", finalResult);
        return finalResult;
    }

    public static String scanXSS(String username) {
        String[] htmlInputs = {
            "<script>alert('XSS')</script>",
            "<h1>Welcome</h1>",
            "<img src='x.jpg' />",
            "<script>stealCookies()</script>",
            "<img src='x' onerror='alert(1)'/>",
            "<body onload=alert('XSS')>",
            "<iframe src='javascript:alert(2)'>",
            "<svg onload=alert('XSS')>",
            "<h1>Hello</h1>",
            "<b>Not a script</b>"
        };
        

        System.out.println("\n Simulated HTML inputs:");
        for (String input : htmlInputs) {
            System.out.println(" - " + input);
        }

        StringBuilder result = new StringBuilder("\n XSS Scan Results:\n");
        for (String input : htmlInputs) {
            if (input.toLowerCase().contains("<script>")) {
                result.append("[✓] XSS vulnerability found in: ").append(input).append("\n");
            } else {
                result.append("[✗] Safe input: ").append(input).append("\n");
            }
        }

        String finalResult = result.toString();
        logScan(username, "XSS", finalResult);
        return finalResult;
    }

    public static String scanKeylogger(String username) {
        String[] logs = {
            "user typed: hello",
            "keystroke pattern: delay 0.1ms",
            "input pattern: normal",
            "keystroke logger: detected unusual input frequency",
            "user typed: password123",
            "keystroke pattern: delay 0.09ms",
            "input pattern: rapid-fire keys",
            "suspicious logging of user inputs",
            "keystroke logger: detected unusual input frequency",
            "key pattern: repetitive inputs detected",
            "user typed: 123456",
            "keystroke log stored in temp file"
        };
        

        System.out.println("\n Simulated key input logs:");
        for (String log : logs) {
            System.out.println(" - " + log);
        }

        StringBuilder result = new StringBuilder("\n Keylogger Scan Results:\n");
        for (String log : logs) {
            if (log.toLowerCase().contains("keystroke") || log.toLowerCase().contains("delay")) {
                result.append("[✓] Suspicious keylogging activity: ").append(log).append("\n");
            } else {
                result.append("[✗] No suspicious behavior: ").append(log).append("\n");
            }
        }

        String finalResult = result.toString();
        logScan(username, "Keylogger Detection", finalResult);
        return finalResult;
    }

    private static void logScan(String username, String type, String result) {
    try (FileWriter writer = new FileWriter("assets/scan_logs.txt", true)) {
        String logEntry = String.format("%s | %s | %s | %s%n",
                java.time.LocalDateTime.now(), username, type, result);
        writer.write(logEntry);
    } catch (IOException e) {
        e.printStackTrace();
    }
   }
}

