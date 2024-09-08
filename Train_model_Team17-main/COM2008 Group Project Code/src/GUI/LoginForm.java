package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Pattern;


public class LoginForm {


    public static void main(String[] args) {

        DatabaseConnectionHandler databaseConnectionHandler = new DatabaseConnectionHandler();

        try {
            databaseConnectionHandler.openConnection();

            SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
        }
        catch (Throwable e) {
            e.printStackTrace();
        } 
        finally {
            databaseConnectionHandler.closeConnection();
        }

        /*
         * Tasks to implement into main:
         * Opening DatabaseOperations
         * Create products from item in database
         */

    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("Login Form");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(500, 300);
        frame.setLocationRelativeTo(null); // Center the window

        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        frame.add(panel);
        placeComponents(panel);
        frame.setVisible(true);
    }

    private static void placeComponents(JPanel panel) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        JTextField userText = new JTextField(20);
        userText.setPreferredSize(new Dimension(200, 25)); // Increased height
        JLabel userLabel = new JLabel("E-mail:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(userLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        panel.add(userText, gbc);

        JPasswordField passwordText = new JPasswordField(20);
        passwordText.setPreferredSize(new Dimension(200, 25)); // Increased height
        JLabel passwordLabel = new JLabel("Password:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(passwordLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        panel.add(passwordText, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 0, 32, 0); 
        JButton loginButton = createStyledButton("Login");
        panel.add(loginButton, gbc);

        gbc.gridy = 3;
        gbc.insets = new Insets(5, 0, 5, 0); 
        JButton registerButton = createStyledButton("Register");
        panel.add(registerButton, gbc);

        gbc.gridy = 4;
        JButton guestButton = createStyledButton("Browse as Guest");
        panel.add(guestButton, gbc);

        guestButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Define the action for the Guest button here
                JOptionPane.showMessageDialog(panel, "Guest mode activated!");

                //Create a user with a null role
                User guestUser = new User(null, null, null, null, null, 4);
                openShopForm(guestUser);
                
                // Maybe open the ShopForm in guest mode
                //openShopFormAsGuest();
            }
        });



        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username = userText.getText();
                String retrieved_password = new String(passwordText.getPassword());

                if(inputIsntSQL(username, retrieved_password)){
                    loginUser(panel, username, retrieved_password);
                }
                else {
                    JOptionPane.showMessageDialog(panel, "SQL Injection detected", "Error", JOptionPane.ERROR_MESSAGE);
                }

            }
        });

        registerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                openRegisterForm();

                //registerUser(username, password);
            }
        });


    }

    private static boolean inputIsntSQL(String username, String password) {
        // Regular expression to check for SQL injection patterns
        String sqlPattern = ".*([';.-]+|(--)+).*";

        // Check for SQL injection in email and password
        return Pattern.matches(sqlPattern, username) || Pattern.matches(sqlPattern, password);
        
    }

    private static void loginUser(JPanel panel, String username, String password){
        DatabaseOperations databaseOperations = new DatabaseOperations();
        DatabaseConnectionHandler databaseConnectionHandler = new DatabaseConnectionHandler();

        try {
            databaseConnectionHandler.openConnection();
            User userToLogin = databaseOperations.checkUserLogin(databaseConnectionHandler.getConnection(), username, password);
            if(userToLogin != null) {
                JOptionPane.showMessageDialog(panel, "Login Successful!");
                openShopForm(userToLogin); //Pass in the currently logged in user
            }
            else {
                JOptionPane.showMessageDialog(panel, "Invalid username or password. Please try again.");
            }
        }
        catch (Throwable e) {
            e.printStackTrace();
        } 
        finally {
            databaseConnectionHandler.closeConnection();
        }

    }


    private static JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        styleButton(button);
        return button;
    }

    private static void styleButton(JButton button) {
        button.setFont(new Font("Arial", Font.BOLD, 13));
        button.setBackground(new Color(100, 149, 237)); // Cornflower blue
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        
    }

    private static void openShopForm(User userToLogin) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                ShopFrontPageForm example = new ShopFrontPageForm(userToLogin);
                example.setVisible(true); // Show the ShopFrontPageForm
            }
        });
    }

    private static void openRegisterForm() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                RegisterForm example = new RegisterForm();
                example.setVisible(true); 
            }
        });
    }

}
