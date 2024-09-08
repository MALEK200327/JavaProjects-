    package com.sheffield.views;

    import com.sheffield.model.DatabaseOperations;

    import javax.swing.*;
    import java.awt.*;
    import java.awt.event.ActionEvent;
    import java.awt.event.ActionListener;
    import java.sql.Connection;
    import java.sql.SQLException;
    import java.util.Arrays;

    public class LoginView extends JFrame {
        private JTextField usernameField;
        private JPasswordField passwordField;
        public LoginView (Connection connection) throws SQLException {
            // Create the JFrame in the constructor
            this.setTitle("Login Application");
            this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            this.setSize(300, 150);

            // Create a JPanel to hold the components
            JPanel panel = new JPanel();
            this.add(panel);

            // Set a layout manager for the panel (e.g., GridLayout)
            panel.setLayout(new GridLayout(3, 2));

            // Create JLabels for username and password
            JLabel usernameLabel = new JLabel("Username:");
            JLabel passwordLabel = new JLabel("Password:");

            // Create JTextFields for entering username and password
            usernameField = new JTextField(20);
            passwordField = new JPasswordField(20);

            // Create a JButton for the login action
            JButton loginButton = new JButton("Login");

            // Add components to the panel
            panel.add(usernameLabel);
            panel.add(usernameField);
            panel.add(passwordLabel);
            panel.add(passwordField);
            panel.add(new JLabel());  // Empty label for spacing
            panel.add(loginButton);

            // Create an ActionListener for the login button
            loginButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String username = usernameField.getText();
                    char[] passwordChars = passwordField.getPassword();
                    System.out.println(username);
                    System.out.println(new String(passwordChars));
                    DatabaseOperations databaseOperations = new DatabaseOperations();
                    System.out.println(databaseOperations.verifyLogin(connection, username, passwordChars));
                    // Secure disposal of the password
                    Arrays.fill(passwordChars, '\u0000');
                }
            });
        }
    }
