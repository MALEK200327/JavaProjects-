-- Create a table named 'User' to store user information
CREATE TABLE Users (
      userId VARCHAR(50) NOT NULL PRIMARY KEY,  -- Unique user identifier
      email VARCHAR(100) NOT NULL,               -- User's email address
      username VARCHAR(50) NOT NULL,             -- User's username
      password_hash VARCHAR(64) NOT NULL,        -- Securely hashed password
      last_login TIMESTAMP DEFAULT CURRENT_TIMESTAMP,  -- Timestamp of the last login (default to the current timestamp)
      failed_login_attempts INT DEFAULT 0,       -- Number of failed login attempts (default to 0)
      account_locked BOOLEAN DEFAULT FALSE      -- Flag to indicate if the account is locked (default to false)
);

-- Insert sample data into the 'User' table
INSERT INTO Users (userId, email, username, password_hash, failed_login_attempts, account_locked)
VALUES
    ('ed4fd799-d4ab-4f91-a9ee-75ba3cb06d93', 'john.doe@example.com', 'john_doe', '423e16e053d0121774ce4e0a42556837fbfe0d9f74dcd4ef3966a5e5194ceceb', 0, false),  -- User: John Doe
    ('785e024a-f7bc-4dad-a09d-515365b00e1a', 'jane.smith@example.com', 'jane_smith', '58cad11d1eee75367504642d8fae79d2698f6b7e005fab6aeba456e591037fb8', 3, true),  -- User: Jane Smith
    ('5bb536ba-0901-4abc-8fd5-b9fbdee02e68', 'alice.jackson@example.com', 'alice_jackson', 'da211ef80f3af0de1b5950392f9ec8ec2d90ec9372ff3b5e67d0f5cd1b0c8d48', 0, false);  -- User: Alice Jackson

-- Select all rows from the 'User' table
SELECT * FROM Users;