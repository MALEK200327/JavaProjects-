package GUI;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class AddProductForm extends JFrame {
    private JTextField brandNameField, productNameField, priceField, quantityField;
    private JComboBox<String> productTypeComboBox, gaugeComboBox, eraComboBox, dccComboBox;

    public AddProductForm() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400, 350);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(9, 2, 10, 10));

        JLabel productTypeLabel = new JLabel("Product Type:");
        String[] productTypes = {"Locomotive", "Rolling Stock", "Track Piece", "Controller"};
        productTypeComboBox = new JComboBox<>(productTypes);

        JLabel brandNameLabel = new JLabel("Brand Name:");
        brandNameField = new JTextField();
        JLabel productNameLabel = new JLabel("Product Name:");
        productNameField = new JTextField();
        JLabel priceLabel = new JLabel("Price:");
        priceField = new JTextField();
        JLabel gaugeLabel = new JLabel("Gauge:");
        String[] gaugeOptions = {"OO", "TT", "N"};
        gaugeComboBox = new JComboBox<>(gaugeOptions);
        JLabel eraLabel = new JLabel("Era:");
        String[] eraOptions = {"ERA1", "ERA2", "ERA3", "ERA4"};
        eraComboBox = new JComboBox<>(eraOptions);
        JLabel dccLabel = new JLabel("DCC:");
        String[] dccOptions = {"ANALOGUE", "FITTED", "READY", "SOUND"};
        dccComboBox = new JComboBox<>(dccOptions);
        JLabel quantityLabel = new JLabel("Quantity:");
        quantityField = new JTextField();

        JButton addButton = new JButton("Add New Product");

        addButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(
                    AddProductForm.this,
                    "Are you sure you want to add this product?",
                    "Confirm Addition",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE
            );
        
            if (confirm == JOptionPane.YES_OPTION) {
                DatabaseOperations databaseOperations = new DatabaseOperations();
                DatabaseConnectionHandler databaseConnectionHandler = new DatabaseConnectionHandler();
                try {
                    databaseConnectionHandler.openConnection();
                    String brand_name = brandNameField.getText();
                    String product_name = productNameField.getText();
                    double price = Double.parseDouble(priceField.getText());
                    int quantity = Integer.parseInt(quantityField.getText());
                    String product_type = (String) productTypeComboBox.getSelectedItem();
                    
                    // Extract selected values from combo boxes
                    String gauge = (String) gaugeComboBox.getSelectedItem();
                    String era = (String) eraComboBox.getSelectedItem();
                    String dcc = (String) dccComboBox.getSelectedItem();
        
                    databaseOperations.addNewProduct(databaseConnectionHandler.getConnection(), brand_name, product_name, price, gauge, quantity, product_type, era, dcc);
        
                    JOptionPane.showMessageDialog(AddProductForm.this, "Product added successfully");
        
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(AddProductForm.this, "Error adding product: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                } finally {
                    databaseConnectionHandler.closeConnection();
                }
            }
        });

        
        panel.add(productTypeLabel);
        panel.add(productTypeComboBox);
        panel.add(brandNameLabel);
        panel.add(brandNameField);
        panel.add(productNameLabel);
        panel.add(productNameField);
        panel.add(priceLabel);
        panel.add(priceField);
        panel.add(gaugeLabel);
        panel.add(gaugeComboBox);
        panel.add(eraLabel);
        panel.add(eraComboBox);
        panel.add(dccLabel);
        panel.add(dccComboBox);
        panel.add(quantityLabel);
        panel.add(quantityField);
        panel.add(new JLabel()); 
        panel.add(addButton);
        styleButton(addButton);


        add(panel);
        setVisible(true);
        setupUI();
    }

    private void setupUI() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(450, 400);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Padding
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5); // External padding of components

        
        styleComponent(brandNameField);
        styleComponent(productNameField);
        styleComponent(priceField);
        styleComponent(quantityField);
        styleComboBox(productTypeComboBox);
        styleComboBox(gaugeComboBox);
        styleComboBox(eraComboBox);
        styleComboBox(dccComboBox);
        

        
        add(panel);
        setVisible(true);
    }

    private void styleComponent(JComponent component) {
        component.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        if (component instanceof JTextField) {
            component.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        }
    }

    private void styleComboBox(JComboBox<String> comboBox) {
        comboBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        comboBox.setBackground(Color.WHITE);
    }

    private void styleButton(JButton button) {
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(new Color(100, 149, 237)); // Cornflower blue
        button.setForeground(Color.WHITE);
        button.setOpaque(true);
        button.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
    }


    

}

