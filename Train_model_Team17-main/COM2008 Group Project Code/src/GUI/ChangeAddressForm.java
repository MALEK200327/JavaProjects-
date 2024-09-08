package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class ChangeAddressForm extends JFrame {

    private JTextField houseNumberTextField, streetTextField, cityTextField, postcodeTextField;

    public ChangeAddressForm(User currentUser) {
        setTitle("Update Address");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400, 350);
        setLocationRelativeTo(null);
        setResizable(false);

        

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Padding

        // Improved form panel using GridBagLayout for better control
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5); // Padding
        gbc.gridx = 1;
        gbc.gridy = 1;

        // Labels and Textfieldss
        String[] labels = {"House Number:", "Street:", "City:", "Postcode:"};
        JTextField[] textFields = {houseNumberTextField = new JTextField(30),
                                   streetTextField = new JTextField(30),
                                   cityTextField = new JTextField(30),
                                   postcodeTextField = new JTextField(30)};

        for (int i = 0; i < labels.length; i++) {
            formPanel.add(new JLabel(labels[i]), gbc);
            gbc.gridy++;
            formPanel.add(textFields[i], gbc);
            gbc.gridy++;
        }

        

        mainPanel.add(formPanel);
        refreshFormWithOriginalAddressData(currentUser);
        

        // Styling and adding the save button
        JButton saveButton = new JButton("Save");
        saveButton.setFont(new Font("Arial", Font.BOLD, 12));
        saveButton.setBackground(new Color(100, 149, 237)); // Cornflower blue
        saveButton.setForeground(Color.WHITE);
        saveButton.setOpaque(true);
        saveButton.setBorderPainted(false);
        saveButton.setFocusPainted(false);
        saveButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String houseNumberStr = houseNumberTextField.getText();
                String street = streetTextField.getText();
                String city = cityTextField.getText();
                String postcode = postcodeTextField.getText();
        
                if (!inputIsntSQL(houseNumberStr, street, city, postcode)) {
                    JOptionPane.showMessageDialog(mainPanel, "SQL Injection detected", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
        
                int confirm = JOptionPane.showConfirmDialog(mainPanel, "Are you sure you want to save these changes?", "Confirm Changes", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (confirm == JOptionPane.YES_OPTION) {
                    try {
                        int houseNumber = Integer.parseInt(houseNumberStr);
                        DatabaseConnectionHandler databaseConnectionHandler = new DatabaseConnectionHandler();
                        try {
                            databaseConnectionHandler.openConnection();
                            DatabaseOperations databaseOperations = new DatabaseOperations();
                            Address newAddress = new Address(null, houseNumber, street, city, postcode);
                            databaseOperations.updateAddress(databaseConnectionHandler.getConnection(), currentUser.getUserID(), newAddress);
                            JOptionPane.showMessageDialog(mainPanel, "Address updated successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(mainPanel, "Failed to update address.", "Error", JOptionPane.ERROR_MESSAGE);
                        } finally {
                            databaseConnectionHandler.closeConnection();
                        }
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(mainPanel, "Invalid house number format", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else if (confirm == JOptionPane.NO_OPTION) {
                    refreshFormWithOriginalAddressData(currentUser); // Refresh form data if 'No' is chosen
                }
            }
        });
        

        

        mainPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Spacer
        mainPanel.add(saveButton);

        add(mainPanel);
        setVisible(true);

        // Create a JScrollPane that scrolls the mainPanel
        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        // Add the JScrollPane to the JFrame
        add(scrollPane);

    }

    private boolean inputIsntSQL(String... inputs) {
        String sqlPattern = ".*([';\"--]+|(--)+).*";
        for (String input : inputs) {
            if (input.matches(sqlPattern)) {
                return false; // SQL Injection pattern detected
            }
        }
        return true; // No SQL Injection pattern detected
    }

    private void refreshFormWithOriginalAddressData(User currentUser) {
        // Reset the texxt fields to the original data

        DatabaseConnectionHandler databaseConnectionHandler = new DatabaseConnectionHandler();
        DatabaseOperations databaseOperations = new DatabaseOperations();
        try {

            databaseConnectionHandler.openConnection();
            int addressID  = databaseOperations.getAddressIDFromUserID(databaseConnectionHandler.getConnection(), currentUser.getUserID());
            Address address = databaseOperations.getAddressDetails(databaseConnectionHandler.getConnection(), addressID);

            houseNumberTextField.setText(String.valueOf(address.getHouseNumber()));
            streetTextField.setText(address.getStreet());
            cityTextField.setText(address.getCity());
            postcodeTextField.setText(address.getPostcode());


        }
        catch(SQLException e) {
            e.printStackTrace();
        }
        

        
    }
    
}
