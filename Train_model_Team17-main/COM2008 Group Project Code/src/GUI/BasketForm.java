package GUI;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;

import GUI.Order.STATUS;

public class BasketForm extends JFrame {
    private DefaultTableModel tableModel;
    private Map<Product, Integer> selectedItems;
    private BasketUpdateCallback callback;
    private JLabel totalPriceLabel;



    public BasketForm(Map<Product, Integer> selectedItems, User currentUser) {
        this.selectedItems = selectedItems; // Store the passed selectedItems
        setTitle("Shopping Basket");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setPreferredSize(new Dimension(600, 400));
        setLocationRelativeTo(null);

        // Create and populate table model with products and quantities
        tableModel = new DefaultTableModel(new Object[]{"Icon", "Product Name", "Quantity"}, 0){
            @Override
            public boolean isCellEditable(int row, int column) {
                // Prevent editing of any cell
                return false;
            }
        };
        

        this.selectedItems.forEach((product, quantity) -> {
            ImageIcon icon = getProductImageIcon(product.getProductCode());
            tableModel.addRow(new Object[]{icon, product.getProductName(), quantity});
        });

        JTable table = new JTable(tableModel);
        styleTable(table); 
        table.getTableHeader().setReorderingAllowed(false); // Disable column reordering

        JButton increaseButton = new JButton("+");
        JButton decreaseButton = new JButton("-");

        JButton checkoutButton = new JButton("Checkout");
        checkoutButton.setFont(new Font("Arial", Font.BOLD, 16));
        checkoutButton.setForeground(Color.WHITE);
        checkoutButton.setBackground(new Color(13, 102, 72)); // Dark green background
        checkoutButton.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25)); // Top, left, bottom, right padding
        checkoutButton.setFocusPainted(false); // Remove focus ring
        checkoutButton.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Change cursor to hand on hover

        checkoutButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                checkoutButton.setBackground(checkoutButton.getBackground().darker());
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                checkoutButton.setBackground(new Color(13, 102, 72));
            }
        });

        checkoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent x) {
                if (!selectedItems.isEmpty()) {
                    // Confirmation dialog
                    int confirmation = JOptionPane.showConfirmDialog(
                        BasketForm.this,
                        "Are you sure you want to proceed to checkout with current bank details?",
                        "Confirm Checkout",
                        JOptionPane.YES_NO_OPTION
                    );
        
                    if (confirmation == JOptionPane.YES_OPTION) {
                        // User confirmed the checkout
                        performCheckout(currentUser);
                    } else {
                        // User canceled the checkout
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Error: Basket is empty so order cannot be placed");
                }
            }
        });

        // Create a label to display the total price
        BigDecimal totalPrice = new BigDecimal(calculateTotalPrice(selectedItems));
        totalPrice = totalPrice.setScale(2, RoundingMode.HALF_UP); // Round to 2 decimal places
        totalPriceLabel = new JLabel("Total: £" + totalPrice.toString());
    

        // Panel to hold the increase, decrease, and checkout buttons
        JPanel buttonPanel = new JPanel(new BorderLayout());
        JPanel modifyQuantityPanel = new JPanel(); // This will hold increase and decrease buttons
        modifyQuantityPanel.add(increaseButton);
        modifyQuantityPanel.add(decreaseButton);

        buttonPanel.add(modifyQuantityPanel, BorderLayout.CENTER);
        buttonPanel.add(totalPriceLabel, BorderLayout.WEST); // Add the total price label to the left
        buttonPanel.add(checkoutButton, BorderLayout.EAST); // Add the checkout button to the right

        // Add the combined button panel to the frame at the bottom
        this.add(buttonPanel, BorderLayout.SOUTH);

        increaseButton.addActionListener(e -> updateQuantity(table, 1)); // Increase quantity
        decreaseButton.addActionListener(e -> updateQuantity(table, -1)); // Decrease quantity

        // Modify the table's appearance
        styleTable(table);

        // Define a fixed size for the icon
        final int iconWidth = 50; 
        final int iconHeight = 50; 

        table.setRowHeight(iconHeight); // Update the table row height to match the icon height

        table.getColumn("Icon").setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                if (value instanceof ImageIcon) {
                    ImageIcon icon = (ImageIcon) value;
                    Image image = icon.getImage();
                    // Scale it to fit the fixed width and height
                    Image newimg = image.getScaledInstance(iconWidth, iconHeight, Image.SCALE_SMOOTH);
                    setIcon(new ImageIcon(newimg));
                    setText("");
                } else {
                    setText("No Icon"); 
                    setIcon(null);
                }
                setHorizontalAlignment(JLabel.CENTER); // Center the icon in the cell
                return this;
            }
        });

        

        // Improved look for cells
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, 
                                                           boolean isSelected, boolean hasFocus, 
                                                           int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(0xf2f2f2)); // Alternate row color
                c.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                ((JLabel) c).setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
                return c;
            }
        });

        // Adjust column widths
        TableColumnModel columnModel = table.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(iconWidth); // Set the icon column width to match the fixed icon width
        // Add the table to a scroll pane
        JScrollPane scrollPane = new JScrollPane(table);
        getContentPane().add(scrollPane);

        // Adjust and pack the components before making the frame visible
        pack();
        setVisible(true);

        // Add a window listener to update the basket contents in the main form when this form is closed
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                if (callback != null) {
                    callback.onBasketUpdated(selectedItems);
                }
            }
        });

    }

    public static double calculateTotalPrice(Map<Product, Integer> productsMap) {
            double totalPrice = 0.0;
    
            // Iterate through each entry in the HashMap
            for (Map.Entry<Product, Integer> entry : productsMap.entrySet()) {
                Product product = entry.getKey();
                int quantity = entry.getValue();
    
                // Calculate price for each product and update the total
                totalPrice += product.getPrice() * quantity;
            }
    
            return totalPrice;
        }

    public static List<OrderLine> createOrderLines(Map<Product, Integer> productsMap) {

        List<OrderLine> orderLines = new ArrayList<>();

        for (Map.Entry<Product, Integer> entry : productsMap.entrySet()) {

                OrderLine orderlineToAdd = new OrderLine(null, null, entry.getKey().getProductCode(), entry.getValue());
                orderLines.add(orderlineToAdd);

        }
        return orderLines;

    }

    private void styleTable(JTable table) {
        table.setRowHeight(40); // Set a bigger row height for modern look
        table.setFillsViewportHeight(true); // Fill the viewport height
    
        // Style the table headers
        JTableHeader tableHeader = table.getTableHeader();
        tableHeader.setBackground(new Color(0xE1E1E1)); // A light gray color
        tableHeader.setFont(new Font("Dialog", Font.BOLD, 12));
    
        // Set column widths
        TableColumnModel columnModel = table.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(50); // Icon column
        columnModel.getColumn(1).setPreferredWidth(200); // Product Name column
        columnModel.getColumn(2).setPreferredWidth(50); // Quantity column
    
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        columnModel.getColumn(2).setCellRenderer(centerRenderer); // Corrected to index 2 for Quantity

        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        
    }

    public void setBasketUpdateCallback(BasketUpdateCallback callback) {
        this.callback = callback;
    }

    private static void openEnterBankDetailsForm(User currentUser) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                EnterBankDetailsForm enterBankDetailsForm = new EnterBankDetailsForm(currentUser);
                enterBankDetailsForm.setVisible(true); 
            }
        });
    }


    private void updateQuantity(JTable table, int change) {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            Product product = findProductByName((String) tableModel.getValueAt(selectedRow, 1));
            if (product != null) {
                int currentQuantity = selectedItems.getOrDefault(product, 0);
                int newQuantity = Math.max(0, currentQuantity + change);
    
                if (newQuantity > 0) {
                    selectedItems.put(product, newQuantity);
                    tableModel.setValueAt(newQuantity, selectedRow, 2);
                } else {
                    selectedItems.remove(product);
                    tableModel.removeRow(selectedRow);
                }
            
                callback.onBasketUpdated(selectedItems);
                updateTotalPriceLabel(); 
            }
            if (callback != null) {
                callback.onBasketUpdated(selectedItems);
            }

            
        }
        
    }

    private Product findProductByName(String productName) {
        for (Product product : selectedItems.keySet()) {
            if (product.getProductName().equals(productName)) {
                return product;
            }
        }
        return null;
    }


    public interface BasketUpdateCallback {
        void onBasketUpdated(Map<Product, Integer> updatedBasket);
    }

    public void updateBasketContents(Map<Product, Integer> updatedSelectedItems) {
        this.selectedItems = updatedSelectedItems; // update the current map
        tableModel.setRowCount(0); // clear the table
        // repopulate the table
        for (Map.Entry<Product, Integer> entry : updatedSelectedItems.entrySet()) {
            tableModel.addRow(new Object[]{"COM2008 Group Project Code/src/GUI/images/test.png", entry.getKey().getProductName(), entry.getValue()});

        }

    }

    public void updateTotalPriceLabel() {
        if (totalPriceLabel != null) {
            double totalPrice = calculateTotalPrice(selectedItems);
            totalPriceLabel.setText("Total: £" + totalPrice);
        } else {
            
        }
    }


    private void performCheckout(User currentUser) {
        if(!selectedItems.isEmpty()) {

            DatabaseConnectionHandler databaseConnectionHandler = new DatabaseConnectionHandler();
            DatabaseOperations databaseOperations = new DatabaseOperations();

            try{
                databaseConnectionHandler.openConnection();

                if(!databaseOperations.hasBankDetails(databaseConnectionHandler.getConnection(), currentUser.getUserID())){
                    //If the user does not have a record of bank details in the database
                    openEnterBankDetailsForm(currentUser);

                    
                }
                else {
                    //checkout the customer!!
                    //map is called selectedItems

                    //Before creating order lines, must insert an order into the table to retrieve an orderID


                    //Generate current date:
                    LocalDate currentDate = LocalDate.now();

                    // Format the date as YYYY-MM-DD
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    String formattedDate = currentDate.format(formatter);



                    //Create order
                    Order checkoutOrder = new Order(null,formattedDate, calculateTotalPrice(selectedItems), STATUS.valueOf("pending"), currentUser.getUserID());
                    

                    //Before order is inserted, check that there is enough stock of each product in the database.
                    //Use the orderlines, compare the productID and quantity in each line to the productID and quantity in stock

                    //Also change stock levels after an order is placed



                    //Add order to database
                    try{
                        databaseOperations.insertOrder(databaseConnectionHandler.getConnection(), checkoutOrder, createOrderLines(selectedItems));
                        SwingUtilities.invokeLater(() ->
                            JOptionPane.showMessageDialog(null, "Order placed successfully, thank you for shopping with us")
                        );
                        
                        selectedItems.clear();
                        tableModel.setRowCount(0); 
                        updateTotalPriceLabel(); 
                        callback.onBasketUpdated(selectedItems); 
                    
                        
                    }
                    catch(SQLException e) {
                        SwingUtilities.invokeLater(() ->
                            JOptionPane.showMessageDialog(null, "Error: Not all products are in stock. Order not placed.")
                        );
                    }

                    callback.onBasketUpdated(selectedItems);

                }
                
            }
            catch (Throwable e) {
                e.printStackTrace();
            } 
            finally {
                databaseConnectionHandler.closeConnection();
            }
            
        }
        else{
            SwingUtilities.invokeLater(() ->
                            JOptionPane.showMessageDialog(null, "Error: Basket is empty so order can not be placed")
                        );
        }
    }

    private ImageIcon getProductImageIcon(String productCode) {
        String imagePath;
    
        switch (productCode.charAt(0)) {
            case 'L': 
                imagePath = "/GUI/images/locomotive.jpg";
                break;
            case 'S': 
                imagePath = "/GUI/images/rollingstock.jpeg";
                break;
            case 'R': 
                imagePath = "/GUI/images/trackpiece.jpg";
                break;
            case 'M': 
                imagePath = "/GUI/images/trainset.jpg";
                break;
            case 'P': 
                imagePath = "/GUI/images/trackpack.jpg";
                break;
            case 'C': 
                imagePath = "/GUI/images/controller.jpg";
                break;
            default:
                imagePath = "/GUI/images/default.jpg"; // Default image if no match
                break;
        }
    
        URL imageUrl = getClass().getResource(imagePath);
        if (imageUrl != null) {
            return new ImageIcon(imageUrl);
        } else {
            // Handle missing resource
            System.err.println("Resource not found: " + imagePath);
            return null;
        }
    }

}
