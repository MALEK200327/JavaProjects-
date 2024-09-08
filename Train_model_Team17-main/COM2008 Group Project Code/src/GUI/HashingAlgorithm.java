package GUI;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class HashingAlgorithm {
    public static String generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

    public static String hashPassword(String password, String salt) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salt.getBytes());
            byte[] hashedPassword = md.digest(password.getBytes());
            return Base64.getEncoder().encodeToString(hashedPassword);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            // Handle the exception, possibly by returning null or a default value
            return null; // or any appropriate error handling
        }
    }

    public static boolean CheckPassword(String enteredPassword, String hashedPassword, String salt) {
        // Hash the entered password using the same salt
        String hashedEnteredPassword = hashPassword(enteredPassword, salt);

        // Check if the hashed entered password matches the stored hashed password
        return hashedEnteredPassword != null && hashedEnteredPassword.equals(hashedPassword);
    }

}