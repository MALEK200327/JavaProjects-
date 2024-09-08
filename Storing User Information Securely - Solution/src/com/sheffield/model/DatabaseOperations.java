package com.sheffield.model;

import com.sheffield.util.HashedPasswordGenerator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseOperations {

    public String verifyLogin(Connection connection, String username, char[] enteredPassword) {
        try {
            // Query the database to fetch user information
            String sql = "SELECT userId, password_hash, failed_login_attempts, " +
                    "account_locked FROM Users WHERE username = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String userId = resultSet.getString("userId");
                String storedPasswordHash = resultSet.getString("password_hash");
                int failedLoginAttempts = resultSet.getInt("failed_login_attempts");
                boolean accountLocked = resultSet.getBoolean("account_locked");

                // Check if the account is locked
                if (accountLocked) {
                    return "Account is locked. Please contact support.";
                } else {
                    // Verify the entered password against the stored hash
                    if (verifyPassword(enteredPassword, storedPasswordHash)) {
                        // Update the last login time
                        sql = "UPDATE Users SET last_login = CURRENT_TIMESTAMP, " +
                                "failed_login_attempts = 0 WHERE userId = ?";
                        statement = connection.prepareStatement(sql);
                        statement.setString(1, userId);
                        statement.executeUpdate();
                        return "Login successful for user: " + username;
                    } else {
                        // Incorrect password, update failed login attempts
                        failedLoginAttempts++;
                        sql = "UPDATE Users SET failed_login_attempts = ? WHERE userId = ?";
                        statement = connection.prepareStatement(sql);
                        statement.setInt(1, failedLoginAttempts);
                        statement.setString(2, userId);
                        statement.executeUpdate();

                        return "Incorrect password. Failed login attempts: " + failedLoginAttempts;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "User not found.";
    }

    private static boolean verifyPassword(char[] enteredPassword, String storedPasswordHash) {
        try {
            String hashedEnteredPassword = HashedPasswordGenerator.hashPassword(enteredPassword);
            return hashedEnteredPassword.equals(storedPasswordHash);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
