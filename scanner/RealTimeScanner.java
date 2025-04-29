package scanner;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
//import java.util.List;

public class RealTimeScanner {

    // Method to check if the website is using HTTPS
    public static boolean isHttps(String url) {
        return url.startsWith("https");
    }

    // Method to check HTTP status and fetch the page
    public static String getPageStatus(String url) {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("GET");
            int status = connection.getResponseCode();
            if (status == 200) {
                return "Page found (HTTP Status: 200)";
            } else {
                return "Page not found (HTTP Status: " + status + ")";
            }
        } catch (IOException e) {
            return "Error fetching the page: " + e.getMessage();
        }
    }

    // Method to fetch and parse the webpage
    public static Document fetchWebPage(String url) throws IOException {
        return Jsoup.connect(url).get();  // Using Jsoup to fetch and parse HTML
    }

    // Method to detect form fields (potential SQLi/XSS points)
    public static Elements detectForms(Document doc) {
        return doc.select("form input, form textarea, form select");  // Selecting form elements
    }

    // Simulate a SQL Injection attempt
    public static String testSqlInjection(String input) {
        String payload = "' OR 1=1 --";
        String testInput = input + payload;
        // Here we would send the input and check for SQLi vulnerabilities
        // Returning a simple result for demo purposes
        if (testInput.contains(payload)) {
            return "[✓] SQL Injection test successful!";
        } else {
            return "[✗] No SQL Injection vulnerability detected.";
        }
    }

    // Simulate an XSS Injection attempt
    public static String testXSSInjection(String input) {
        String payload = "<script>alert('XSS')</script>";
        String testInput = input + payload;
        // Here we would send the input and check for XSS vulnerabilities
        // Returning a simple result for demo purposes
        if (testInput.contains(payload)) {
            return "[✓] XSS vulnerability detected!";
        } else {
            return "[✗] No XSS vulnerability detected.";
        }
    }

    // Method to analyze headers (optional feature)
    public static String analyzeHeaders(String url) {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("HEAD");
            connection.connect();

            StringBuilder headers = new StringBuilder();
            for (String header : connection.getHeaderFields().keySet()) {
                headers.append(header).append(": ").append(connection.getHeaderField(header)).append("\n");
            }
            return headers.toString();
        } catch (IOException e) {
            return "Error analyzing headers: " + e.getMessage();
        }
    }

    // Main method to trigger the scanner and simulate results
    public static String scanWebsite(String url) {
        StringBuilder result = new StringBuilder();

        // Check if HTTPS
        if (isHttps(url)) {
            result.append("[✓] HTTPS: The website is secure using HTTPS.\n");
        } else {
            result.append("[✗] HTTPS: The website is not using HTTPS.\n");
        }

        // Fetch page status
        result.append(getPageStatus(url)).append("\n");

        try {
            // Fetch and parse webpage
            Document doc = fetchWebPage(url);

            // Detect form elements
            Elements formElements = detectForms(doc);
            if (formElements.isEmpty()) {
                result.append("[✗] No form elements detected.\n");
            } else {
                result.append("[✓] Form elements detected:\n");
                for (Element elem : formElements) {
                    result.append(" - ").append(elem.attr("name")).append("\n");
                }
                result.append("\n");

                // Simulate SQL Injection and XSS on form inputs
                for (Element form : formElements) {
                    String input = form.attr("name");
                    result.append(testSqlInjection(input)).append("\n");
                    result.append(testXSSInjection(input)).append("\n");
                }
            }

            // Optionally analyze headers
            result.append("\nHeader Information:\n");
            result.append(analyzeHeaders(url));

        } catch (IOException e) {
            result.append("Error scanning website: ").append(e.getMessage()).append("\n");
        }

        return result.toString();
    }
}
