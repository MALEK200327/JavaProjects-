package GUI;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;



import GUI.Order.STATUS;

import java.awt.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.EventObject;
import java.util.List;

public class ProcessOrdersForm extends JFrame {

    private JTable table;
    private DefaultTableModel tableModel;
    private List<GUI.Order> orderList;
    private DatabaseOperations databaseOperations;

    public ProcessOrdersForm() {
        setTitle("Order List");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);

        databaseOperations = new DatabaseOperations();
        DatabaseConnectionHandler databaseConnectionHandler = new DatabaseConnectionHandler();

        try {
            databaseConnectionHandler.openConnection();
            orderList = databaseOperations.getAllOrders(databaseConnectionHandler.getConnection());
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exception
        }

        

        // Create the table model with column names and data
        Object[] columnNames = {"Order ID", "Date", "Total", "Status", "User ID"};
        tableModel = new DefaultTableModel(null, columnNames);
        for (Order order : orderList) {
            
            Object[] rowData = {order.getOrderID(), order.getOrderDate(), order.getPriceTotal(), order.getOrderStatus(), order.getUserID()};
            tableModel.addRow(rowData);
        }

        // Create the JTable with the table model
        table = new JTable(tableModel);

        //Sets the userID table as non-editable
        
        
        JScrollPane scrollPane = new JScrollPane(table);

        getContentPane().add(scrollPane, BorderLayout.CENTER);

        // Create a button to update data to the database
        JButton updateButton = new JButton("Update to Database");
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int confirm = JOptionPane.showConfirmDialog(
                    ProcessOrdersForm.this,
                    "Are you sure you want to update the database?",
                    "Confirm Update",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
                );
        
                if (confirm == JOptionPane.YES_OPTION) {
                    try {
                        updateDataToDatabase(scrollPane, databaseConnectionHandler.getConnection());
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        // Handle exception
                    }
                } else {
                    try {
                        refreshTable(databaseConnectionHandler.getConnection());
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        // Handle exception
                    }
                }
            }
        });

        TableColumn statusColumn = table.getColumnModel().getColumn(3);
        statusColumn.setCellEditor(new DefaultCellEditor(createComboBox()));
              
        // Set orderID column uneditable
        TableColumn orderIDColumn = table.getColumnModel().getColumn(0);
        orderIDColumn.setCellRenderer(createNonEditableRenderer());
        orderIDColumn.setCellEditor(createNonEditableEditor());

        // Set date column uneditable
        TableColumn dateColumn = table.getColumnModel().getColumn(1);
        dateColumn.setCellRenderer(createNonEditableRenderer());
        dateColumn.setCellEditor(createNonEditableEditor());

        // Set total column uneditable
        TableColumn totalColumn = table.getColumnModel().getColumn(2);
        totalColumn.setCellRenderer(createNonEditableRenderer());
        totalColumn.setCellEditor(createNonEditableEditor()); 

        // Set userID column uneditable
        TableColumn userIDColumn = table.getColumnModel().getColumn(4);
        userIDColumn.setCellRenderer(createNonEditableRenderer());
        userIDColumn.setCellEditor(createNonEditableEditor()); 
        
        // Enhance button appearance
        updateButton.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        updateButton.setBackground(new Color(0x5b9bd5)); // Blue
        updateButton.setForeground(Color.WHITE);
        updateButton.setFocusPainted(false);
        updateButton.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
        updateButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        getContentPane().add(updateButton, BorderLayout.SOUTH);


       JTableHeader header = table.getTableHeader();
        header.setBackground(new Color(0xe1e1e1)); // Light gray
        header.setForeground(Color.BLACK);
        header.setFont(new Font("Segoe UI", Font.BOLD, 12));

        // Alternate row colors (its for better visibility)
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(0xf2f2f2)); // Lighter gray
                return c;
            }
        });

        table.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                int row = table.rowAtPoint(evt.getPoint());
                if (row > -1) {
                    table.clearSelection();
                    table.setRowSelectionInterval(row, row);
                } else {
                    table.clearSelection();
                }
            }
        });

        table.setFont(new Font("Roboto", Font.PLAIN, 12));
        table.setRowHeight(25);
        table.setShowHorizontalLines(true);
        table.setShowVerticalLines(false);
        table.setGridColor(new Color(0xdddddd));

        JTableHeader tableHeader = table.getTableHeader();
        tableHeader.setBackground(new Color(0x2c3e50)); // Dark blue
        tableHeader.setForeground(Color.WHITE);
        tableHeader.setFont(new Font("Roboto", Font.BOLD, 14));
        tableHeader.setReorderingAllowed(false);

        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        renderer.setHorizontalAlignment(SwingConstants.CENTER);
        renderer.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(renderer);
        }


        setupOrderStatusColumn();
        
        // Styling the update button
        updateButton.setFont(new Font("Roboto", Font.BOLD, 14));
        updateButton.setBackground(new Color(0x3498db)); // Bright blue
        updateButton.setForeground(Color.WHITE);
        updateButton.setOpaque(true);
        updateButton.setBorderPainted(false);
        updateButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        updateButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                updateButton.setBackground(new Color(0x2980b9)); // Darker blue on hover
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                updateButton.setBackground(new Color(0x3498db)); // Original color
            }
        });

        table.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                int selectedRow = table.rowAtPoint(e.getPoint());
                if (selectedRow != -1 && e.getClickCount() == 1) {
                    int orderID = (Integer) table.getValueAt(selectedRow, 0);
                    List<OrderLine> orderLines = null; // Fetch order lines using the orderID
                    try {
                        orderLines = databaseOperations.getOrderLinesByOrderID(databaseConnectionHandler.getConnection(), orderID);
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        // Handle exception
                    }
        
                    if (orderLines != null && !orderLines.isEmpty()) {
                        displayOrderLines(orderLines);
                    } else {
                        JOptionPane.showMessageDialog(ProcessOrdersForm.this, "No order lines found for selected order ID: " + orderID);
                    }
                }
            }
        });
    }


    //Edit this !!!!
    private void updateDataToDatabase(JScrollPane panel, Connection connection) throws SQLException {
        // Get updated data from the table model and update the database
        for (int i = 0; i < tableModel.getRowCount(); i++) {

            

            int orderID = (Integer) tableModel.getValueAt(i, 0);
            String order_status = (String) tableModel.getValueAt(i, 3).toString();

            //If order_status is not an accepted value it will not update 
            if(order_status.equals("decline")) {
                databaseOperations.deleteOrder(connection, orderID);
                //Also needs to add stock back into database!
            }
            else if (!order_status.equals("pending") && !order_status.equals("fulfilled") && !order_status.equals("confirmed")) {
                JOptionPane.showMessageDialog(panel, "Problem with order " + orderID + ". Order status can only be pending, fulfilled, or confirmed", "Input is of wrong format", JOptionPane.ERROR_MESSAGE);
            }
            else{
                databaseOperations.updateOrderStatus(connection, STATUS.valueOf(order_status), orderID);
            }


        }

        JOptionPane.showMessageDialog(this, "Data updated to the database");
        refreshTable(connection);
    }

    private void setupOrderStatusColumn() {
        TableColumn statusColumn = table.getColumnModel().getColumn(3);
        statusColumn.setCellRenderer(new CustomComboBoxRenderer());
    }
    
    // Custom renderer to display a symbol to indicate the combo box
    class CustomComboBoxRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
    
            JLabel label = (JLabel) c;
            label.setText(value + " ▼"); // Add the symbol next to the displayed value
    
            return label;
        }
    }

    private void refreshTable(Connection connection) throws SQLException {
        // Clear the existing data in the table model
        tableModel.setRowCount(0);
    
        // Fetch the updated data from the database
        orderList = databaseOperations.getAllOrders(connection);
    
        // Populate the table model with the updated data
        for (Order order : orderList) {
            
            Object[] rowData = {order.getOrderID(), order.getOrderDate(), order.getPriceTotal(), order.getOrderStatus(), order.getUserID()};
            tableModel.addRow(rowData);
        }
    }

    private void displayOrderLines(List<OrderLine> orderLines) {
        // Create a dialog or update a separate section in the GUI to display the order lines
        // Example: Display order lines in a JOptionPane
        StringBuilder orderLinesText = new StringBuilder("Order Lines:\n");
        for (OrderLine orderLine : orderLines) {
            orderLinesText.append("Product ID: ").append(orderLine.getProduct_code()).append(", Quantity: ").append(orderLine.getQuantity()).append("\n");
        }
        JOptionPane.showMessageDialog(this, orderLinesText.toString(), "Order Lines", JOptionPane.INFORMATION_MESSAGE);
    }

    private JComboBox<String> createComboBox() {
        String[] statusOptions = { "pending", "confirmed", "fulfilled", "decline" };
        JComboBox<String> comboBox = new JComboBox<>(statusOptions);
        comboBox.setEditable(false);
        return comboBox;
    }

    
        



    private DefaultTableCellRenderer createNonEditableRenderer() {
        return new DefaultTableCellRenderer() {
         @Override
         public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            setEnabled(false); // Make the cell not editable
            return c;
            }
        };
    }

    private DefaultCellEditor createNonEditableEditor() {
        return new DefaultCellEditor(new JTextField()) {
            @Override
            public boolean isCellEditable(EventObject e) {
                return false; // Make the cell editor non-editable
            }
        };
    }}
    

