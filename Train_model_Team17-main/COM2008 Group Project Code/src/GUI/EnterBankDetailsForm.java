package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.regex.Pattern;

public class EnterBankDetailsForm extends JFrame {

    private JTextField nicknameField, cardHolderNameField, cardNumberField, expiryDateField, securityCodeField;

    public EnterBankDetailsForm(User currentUser) {
        setTitle("Enter Bank Details");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(6, 2, 10, 10));

        JLabel nicknameLabel = new JLabel("Nickname for Card:");
        nicknameField = new JTextField();
        JLabel cardHolderLabel = new JLabel("Card Holder Name:");
        cardHolderNameField = new JTextField();
        JLabel cardNumberLabel = new JLabel("Card Number:");
        cardNumberField = new JTextField();
        JLabel expiryDateLabel = new JLabel("Expiry Date:");
        expiryDateField = new JTextField();
        JLabel securityCodeLabel = new JLabel("Security Code:");
        securityCodeField = new JTextField();

        JButton enterButton = new JButton("Enter");

        enterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent x) {
                // Retrieve values from text fields
                String nickname = nicknameField.getText();
                String cardHolderName = cardHolderNameField.getText();
                String cardNumber = cardNumberField.getText();
                String expiryDate = expiryDateField.getText();
                String securityCode = securityCodeField.getText();
        
                if (inputIsValid()) {
                    try {
        
                        // Assuming BankDetails constructor takes int for cardNumber and securityCode
                        BankDetails bankDetailsToBeAdded = new BankDetails(nickname, cardNumber, cardHolderName, expiryDate, securityCode);
                        
                        // Example: Call a method to save these details in the database
                        saveBankDetails(currentUser, bankDetailsToBeAdded);
        
                        JOptionPane.showMessageDialog(null, "Bank details entered successfully.");
                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(null, "Card number or security code is not in the correct format.");
                    }
                } else {
                    // Do nothing
                }
            }
            
        });
        
        
        
        panel.add(nicknameLabel);
        panel.add(nicknameField);
        panel.add(cardHolderLabel);
        panel.add(cardHolderNameField);
        panel.add(cardNumberLabel);
        panel.add(cardNumberField);
        panel.add(expiryDateLabel);
        panel.add(expiryDateField);
        panel.add(securityCodeLabel);
        panel.add(securityCodeField);
        panel.add(new JLabel()); // Empty label for spacing
        panel.add(enterButton);

        add(panel);
        setVisible(true);
    }

    private boolean inputIsValid() {
        // First, check for SQL injection in all input fields
        String[] fields = { nicknameField.getText(), cardHolderNameField.getText(), 
                            cardNumberField.getText(), expiryDateField.getText(), 
                            securityCodeField.getText() };
    
        String sqlInjectionPattern = ".*(['\";]+|--).*";
        for (String input : fields) {
            if (Pattern.matches(sqlInjectionPattern, input)) {
                JOptionPane.showMessageDialog(null, "Invalid input: SQL Injection patterns detected.");
                return false;
            }
        }
    
        // Validate card number
        try {
            String cardNumber = cardNumberField.getText().replaceAll("\\s", ""); // Remove any spaces
            
            if (cardNumber.length() != 16 || !cardNumber.matches("\\d{16}")) {
                JOptionPane.showMessageDialog(null, "Invalid card number. Please enter a 16-digit card number.");
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid card number");
            return false;
        }
    
        // Validate expiry date
        if (!expiryDateField.getText().matches("\\d{4}-\\d{2}-\\d{2}")) {
            JOptionPane.showMessageDialog(null, "Invalid expiry date: Please use YYYY-MM-DD format.");
            return false;
        }
    
        // Validate security code
        String securityCode = securityCodeField.getText();
        if (securityCode.length() != 3 || !securityCode.matches("\\d{3}")) {
            JOptionPane.showMessageDialog(null, "Invalid security code: Please enter a 3-digit integer.");
            return false;
        }
    
        return true;
    }
    
    
    private void saveBankDetails(User currentUser, BankDetails bankDetails) {
        DatabaseConnectionHandler databaseConnectionHandler = new DatabaseConnectionHandler();
        try {
            databaseConnectionHandler.openConnection();
            DatabaseOperations databaseOperations = new DatabaseOperations();
    
            // Encrypt the card number and security code
            String encryptedCardNumber = databaseOperations.encrypt(bankDetails.getCardNumber());
            String encryptedSecurityCode = databaseOperations.encrypt(bankDetails.getSecurityCode());
    
            // Update the bankDetails object
            bankDetails.setCardNumber(encryptedCardNumber);
            bankDetails.setSecurityCode(encryptedSecurityCode);
    
            databaseOperations.insertBankDetails(databaseConnectionHandler.getConnection(), bankDetails, currentUser);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            databaseConnectionHandler.closeConnection();
        }
    }

    
}

