import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;

public class TestMain {
    public static void main(String[] args) throws Exception {
        // Dữ liệu mẫu
        String username = "Jerry";
        String password = "doesnotreallymatter";
        String salt = "DeliberatelyInsecure1235";

        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            String salted = password + salt + username;
            byte[] hashedPassword = md.digest(salted.getBytes(StandardCharsets.UTF_8));
            System.out.println("Hashed password: " + Base64.getEncoder().encodeToString(hashedPassword));
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
