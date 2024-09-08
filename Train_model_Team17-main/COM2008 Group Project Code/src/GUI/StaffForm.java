package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StaffForm extends JFrame {

    public StaffForm() {
        setTitle("Staff Menu");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(500, 200); // Increased size for a more spacious layout
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(240, 240, 240)); // Light gray background

        // Use GridBagLayout for more control over layout
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 20, 10, 20); // Top, left, bottom, right padding

        // Styling buttons
        JButton updateStockButton = createStyledButton("Update Stock Levels");
        JButton processOrdersButton = createStyledButton("Process Orders");
        JButton addProductButton = createStyledButton("Add Product");

        // Add buttons to the form
        add(updateStockButton, gbc);
        add(processOrdersButton, gbc);
        add(addProductButton, gbc);

        // Add action listeners to the buttons
        updateStockButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openStockLevelForm();
                dispose();
                
            }
        });

        processOrdersButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openProcessOrdersForm();
                dispose();
            }
        });

        addProductButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openAddProductForm();
                dispose();
            }
        });

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new StaffForm());
    }

    private static void openProcessOrdersForm() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                ProcessOrdersForm ordersPage = new ProcessOrdersForm();
                ordersPage.setVisible(true); 
            }
        });
    }

    private static void openStockLevelForm() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                StockLevelForm stockLevel = new StockLevelForm();
                stockLevel.setVisible(true); 
            }
        });
    }

    private static void openAddProductForm() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                AddProductForm addProductForm = new AddProductForm();
                addProductForm.setVisible(true); 
            }
        });
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(new Color(100, 149, 237)); // Cornflower blue
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15)); // Padding
        return button;
    }
}


