package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.regex.Pattern;

public class AccountsForm extends JFrame {

    private JTextField emailTextField, forenameTextField, surnameTextField;

    public AccountsForm(User currentUser) {
        setTitle(currentUser.getForename() + "'s Accounts Page");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(500, 250);
        setLocationRelativeTo(null);
        setResizable(false);
        
        
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Padding

        JLabel titleLabel = new JLabel("User Accounts");
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0)); // Top, left, bottom, right padding

        mainPanel.add(titleLabel);

        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10));

        JLabel emailLabel = new JLabel("Email:");
        emailTextField = new JTextField(currentUser.getEmail());
        emailTextField.setEditable(true);

        JLabel forenameLabel = new JLabel("Forename:");
        forenameTextField = new JTextField(currentUser.getForename());
        forenameTextField.setEditable(true);

        JLabel surnameLabel = new JLabel("Surname:");
        surnameTextField = new JTextField(currentUser.getSurname());
        surnameTextField.setEditable(true);

        formPanel.add(emailLabel);
        formPanel.add(emailTextField);
        formPanel.add(forenameLabel);
        formPanel.add(forenameTextField);
        formPanel.add(surnameLabel);
        formPanel.add(surnameTextField);

        mainPanel.add(formPanel);

        JButton orderHistoryButton = createStyledButton("Order History");
        orderHistoryButton.addActionListener(e -> openUserOrderHistoryForm(currentUser));

        JButton changeAddressButton = createStyledButton("Change Address");
        changeAddressButton.addActionListener(e -> openChangeAddressForm(currentUser));

        JButton saveButton = createStyledButton("Save");
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = emailTextField.getText();
                String forename = forenameTextField.getText();
                String surname = surnameTextField.getText();

                if (isSqlInjectionPresent(email) || isSqlInjectionPresent(forename) || isSqlInjectionPresent(surname)) {
                    JOptionPane.showMessageDialog(AccountsForm.this, "Invalid input detected. Please check your inputs and try again.", "Error", JOptionPane.ERROR_MESSAGE);
                } 
                else {
                    int confirm = JOptionPane.showConfirmDialog(AccountsForm.this, "Do you want to save these changes?", "Confirm Changes", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (confirm == JOptionPane.YES_OPTION) {
                        saveUserData(currentUser, email, forename, surname);
                    }
                    if(confirm == JOptionPane.NO_OPTION) {
                        refreshFormWithOriginalData(currentUser);
                    }
                }
            }
        });

        
        JPanel buttonPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); 
        gbc.gridx = 0; gbc.gridy = 0;
        buttonPanel.add(saveButton, gbc);
        
        gbc.gridx = 1;
        buttonPanel.add(changeAddressButton, gbc);
        
        gbc.gridx = 2;
        buttonPanel.add(orderHistoryButton, gbc);

        // Adding the new button for Bank Details
        JButton bankDetailsButton = createStyledButton("Bank Details");
        bankDetailsButton.addActionListener(e -> openBankDetailsForm(currentUser));

        // Adjust grid coordinates for the new button
        gbc.gridx = 3;
        buttonPanel.add(bankDetailsButton, gbc);

        add(mainPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        setVisible(true);
        
    }

    private void openBankDetailsForm(User currentUser) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                EnterBankDetailsForm bankDetailsForm = new EnterBankDetailsForm(currentUser);
                bankDetailsForm.setVisible(true);
            }
        });
    }

    private void refreshFormWithOriginalData(User currentUser) {
        // Reset the text fields to the original data
        emailTextField.setText(currentUser.getEmail());
        forenameTextField.setText(currentUser.getForename());
        surnameTextField.setText(currentUser.getSurname());
        
    }

    

   private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setBackground(new Color(100, 149, 237)); // Cornflower blue color
        button.setForeground(Color.WHITE); // White text
        button.setOpaque(true);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        return button;
    }

    private void saveUserData(User currentUser, String email, String forename, String surname) {

        if (email.isEmpty() || !isValidEmail(email)) {
            // Handle invalid email address
            JOptionPane.showMessageDialog(null, "Invalid email address.");
            return; 
        }
        else{
            DatabaseOperations databaseOperations = new DatabaseOperations();
            DatabaseConnectionHandler databaseConnectionHandler = new DatabaseConnectionHandler();
            try {
                databaseConnectionHandler.openConnection();
                databaseOperations.updateUserData(databaseConnectionHandler.getConnection(), currentUser.getUserID(), email, forename, surname, currentUser.getRole());
                currentUser.setForename(forename);
                currentUser.setSurname(surname);
                currentUser.setEmail(email);
                JOptionPane.showMessageDialog(this, "Changes saved successfully.");
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Failed to save changes: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            } finally {
                databaseConnectionHandler.closeConnection();
            }
        }
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}$";
        return email.matches(emailRegex);
    }


    private boolean isSqlInjectionPresent(String input) {
        String sqlInjectionPattern = ".*(['\";]+|--).*";
        return Pattern.matches(sqlInjectionPattern, input);
    }
    

    private void openChangeAddressForm(User currentUser) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                ChangeAddressForm changeAddressForm = new ChangeAddressForm(currentUser);
                changeAddressForm.setVisible(true); 
            }
        });
    }

    private void openUserOrderHistoryForm(User currentUser) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                UserOrderHistory userOrderHistory = new UserOrderHistory(currentUser);
                userOrderHistory.setVisible(true);
            }
            
        });
    }

    

    


    
}

