package GUI;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import org.mindrot.jbcrypt.BCrypt;

public class RegisterForm extends JFrame {

    private JTextField emailTextField, forenameTextField, surnameTextField, houseNumberTextField, streetTextField, cityTextField, postcodeTextField;
    private JPasswordField passwordTextField;

    public RegisterForm() {
        

        setLookAndFeel();

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(350, 500);
        setLocationRelativeTo(null);

        getContentPane().setBackground(new Color(255, 255, 255)); // Set background color to white for cleanliness


        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(getContentPane().getBackground()); // Match the panel background to the frame
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10); // Use consistent padding

        // Add components to panel with modified constraints
        int row = 0;
        addLabelAndTextField(panel, "Email:", emailTextField = new JTextField(), gbc, row++);
        addLabelAndTextField(panel, "Password:", passwordTextField = new JPasswordField(), gbc, row++);
        addLabelAndTextField(panel, "Forename:", forenameTextField = new JTextField(), gbc, row++);
        addLabelAndTextField(panel, "Surname:", surnameTextField = new JTextField(), gbc, row++);
        addLabelAndTextField(panel, "House Number:", houseNumberTextField = new JTextField(), gbc, row++);
        addLabelAndTextField(panel, "Street:", streetTextField = new JTextField(), gbc, row++);
        addLabelAndTextField(panel, "City:", cityTextField = new JTextField(), gbc, row++);
        addLabelAndTextField(panel, "Postcode:", postcodeTextField = new JTextField(), gbc, row++);

        JButton registerButton = new JButton("Register");
        styleButton(registerButton);
        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(registerButton, gbc);
        
    


        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Register user function should be here

                
                String email = emailTextField.getText().toLowerCase();
                String password = new String(passwordTextField.getPassword());
                String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt()); // Hash password with BCrypt
                String forename = forenameTextField.getText();
                String surname = surnameTextField.getText();
                String houseNumberStr = houseNumberTextField.getText();
                String street = streetTextField.getText();
                String city = cityTextField.getText();
                String postcode = postcodeTextField.getText().toUpperCase();

                // Check for SQL Injection in all input fields
                if (inputIsntSQL(email, password, forename, surname, houseNumberStr, street, city, postcode)) {
                    // Validate inputs
                if (email.isEmpty() || !emailHasCorrectFormat(email)) {
                    JOptionPane.showMessageDialog(panel, "Please enter a valid email address.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (password.isEmpty()) {
                    JOptionPane.showMessageDialog(panel, "Password cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (forename.isEmpty() || !forename.matches("[a-zA-Z]+")) {
                    JOptionPane.showMessageDialog(panel, "Please enter a valid forename.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (surname.isEmpty() || !surname.matches("[a-zA-Z]+")) {
                    JOptionPane.showMessageDialog(panel, "Please enter a valid surname.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (houseNumberStr.isEmpty() || !houseNumberStr.matches("\\d+")) {
                    JOptionPane.showMessageDialog(panel, "Please enter a valid house number. It needs to be a number", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                int houseNumber = Integer.parseInt(houseNumberStr);
                if (street.isEmpty()) {
                    JOptionPane.showMessageDialog(panel, "Street cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (city.isEmpty()) {
                    JOptionPane.showMessageDialog(panel, "City cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (!postcodeHasCorrectFormat(postcode)) {
                    JOptionPane.showMessageDialog(panel, "Please enter a valid postcode example: S10 318.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
        
            
                } else {
                    JOptionPane.showMessageDialog(panel, "SQL Injection detected", "Error", JOptionPane.ERROR_MESSAGE);
                }
        
                // Validate inputs
                if (email.isEmpty() || !emailHasCorrectFormat(email)) {
                    JOptionPane.showMessageDialog(panel, "Please enter a valid email address.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (password.isEmpty()) {
                    JOptionPane.showMessageDialog(panel, "Password cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (forename.isEmpty() || !forename.matches("[a-zA-Z]+")) {
                    JOptionPane.showMessageDialog(panel, "Please enter a valid forename.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (surname.isEmpty() || !surname.matches("[a-zA-Z]+")) {
                    JOptionPane.showMessageDialog(panel, "Please enter a valid surname.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (houseNumberStr.isEmpty() || !houseNumberStr.matches("\\d+")) {
                    JOptionPane.showMessageDialog(panel, "Please enter a valid house number. It needs to be a number", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                int houseNumber = Integer.parseInt(houseNumberStr);
                if (street.isEmpty()) {
                    JOptionPane.showMessageDialog(panel, "Street cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (city.isEmpty()) {
                    JOptionPane.showMessageDialog(panel, "City cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (!postcodeHasCorrectFormat(postcode)) {
                    JOptionPane.showMessageDialog(panel, "Please enter a valid postcode example: S10 318.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
        
                // Proceed with registration
                registerUser(panel, email, hashedPassword, forename, surname, houseNumber, street, city, postcode);
            }
        });

        add(panel);
        setVisible(true);
    }

    private void setLookAndFeel() {
            try {
                for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                    if ("Nimbus".equals(info.getName())) {
                        UIManager.setLookAndFeel(info.getClassName());
                        break;
                    }
                }
            } catch (Exception e) {
                // If Nimbus is not available, fall back to default look and feel
            }
        }

    private void addLabelAndTextField(JPanel panel, String labelText, JTextField textField, GridBagConstraints gbc, int row) {
        JLabel label = new JLabel(labelText);
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 1;
        gbc.weightx = 0.5; // you can Adjust weightx to control width of label
        gbc.anchor = GridBagConstraints.LINE_END; 
        panel.add(label, gbc);

        textField.setColumns(10); // Set the desired width of the text field
        gbc.gridx = 1;
        gbc.gridy = row;
        gbc.gridwidth = 1;
        gbc.weightx = 100; // Adjust weightx to control width of text field
        gbc.anchor = GridBagConstraints.LINE_START;
        panel.add(textField, gbc);
    }

    private void styleButton(JButton button) {
        button.setFont(new Font("Segoe UI", Font.BOLD, 14)); 
        button.setBackground(new Color(32, 136, 203));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(32, 106, 173), 1, true),
                BorderFactory.createEmptyBorder(10, 25, 10, 25))); 
    }

    public static boolean emailHasCorrectFormat(String email) {
        String regex = "[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}";
        return email.matches(regex);
    }

    public static boolean postcodeHasCorrectFormat(String postcode) {
        String regex = "^[A-Z]{1,2}[0-9R][0-9A-Z]? [0-9][ABD-HJLNP-UW-Z]{2}$";
        return postcode.matches(regex);
    }


    private static void registerUser(JPanel panel, String email, String password, String forename, String surname, int houseNumber, String street, String city, String postcode) {
        DatabaseOperations databaseOperations = new DatabaseOperations();
        User userToBeAdded = new User(null, email, password, forename, surname);
        Address addressToBeAdded = new Address(null, houseNumber, street, city, postcode);
        DatabaseConnectionHandler databaseConnectionHandler = new DatabaseConnectionHandler();
    
        try {
            databaseConnectionHandler.openConnection();
            
            if (databaseOperations.isEmailAlreadyRegistered(databaseConnectionHandler.getConnection(), email)) {
                JOptionPane.showMessageDialog(panel, "Email already registered!", "Registration Error", JOptionPane.ERROR_MESSAGE);
                return; // Exit method if email is already registered
            }
    
            databaseOperations.insertUser(userToBeAdded, addressToBeAdded, databaseConnectionHandler.getConnection());
            JOptionPane.showMessageDialog(panel, "Registration Successful!", "Registration Successful", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(panel, "Error registering user: " + e.getMessage(), "Registration Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            databaseConnectionHandler.closeConnection();
        }
    }


    

    private static boolean inputIsntSQL(String... inputs) {
        // Regular expression to check for SQL injection patterns
        String sqlPattern = ".*([';\"--]+|(--)+).*";
    
        // Check for SQL injection in all inputs
        for (String input : inputs) {
            if (input.matches(sqlPattern)) {
                return false; // SQL Injection pattern detected
            }
        }
        return true; // No SQL Injection pattern detected
    }

}