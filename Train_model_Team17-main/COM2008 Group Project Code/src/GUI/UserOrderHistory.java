package GUI;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;

import GUI.Order.STATUS;

import java.awt.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.EventObject;
import java.util.List;

public class UserOrderHistory extends JFrame {

    private JTable table;
    private DefaultTableModel tableModel;
    private List<GUI.Order> orderList;
    private DatabaseOperations databaseOperations;

    public UserOrderHistory(User currentUser) {
        setTitle("Order History");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);

        databaseOperations = new DatabaseOperations();
        DatabaseConnectionHandler databaseConnectionHandler = new DatabaseConnectionHandler();

        try {
            databaseConnectionHandler.openConnection();
            orderList = databaseOperations.getAllOrdersByUserID(databaseConnectionHandler.getConnection(), currentUser.getUserID()); //CHANGE THIS METHOD
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exception
        }

        

        // Create the table model with column names and data
        Object[] columnNames = {"Order ID", "Date", "Total", "Status"};
        tableModel = new DefaultTableModel(null, columnNames);
        for (Order order : orderList) {
            
            Object[] rowData = {order.getOrderID(), order.getOrderDate(), order.getPriceTotal(), order.getOrderStatus()};
            tableModel.addRow(rowData);
        }

        // Create the JTable with the table model
        table = new JTable(tableModel);

        //Sets the userID table as non-editable
        
        
        JScrollPane scrollPane = new JScrollPane(table);

        getContentPane().add(scrollPane, BorderLayout.CENTER);

        // Create a button to update data to the database
        JButton updateButton = new JButton("Confirm changes to your order");
        updateButton.addActionListener(e -> {
            try {
                updateDataToDatabase(scrollPane, databaseConnectionHandler.getConnection());
            } catch (SQLException ex) {
                ex.printStackTrace();
                // Handle exception
            }
        });

        // Set order status column uneditable when status is 'fulfilled' or 'confirmed'
        TableColumn statusColumn = table.getColumnModel().getColumn(3);

        statusColumn.setCellEditor(new DefaultCellEditor(createComboBox()) {
            @Override
            public boolean isCellEditable(EventObject e) {
                int selectedRow = table.getSelectedRow();
                String status = table.getValueAt(selectedRow, 3).toString();
                return !("fulfilled".equals(status) || "confirmed".equals(status)); // Allow editing for other statuses
            }
        });

        statusColumn.setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                String status = value.toString();

                if ("fulfilled".equals(status) || "confirmed".equals(status)) {
                    setForeground(Color.GRAY); // Change text color for 'fulfilled' or 'confirmed' status
                } else {
                    setForeground(Color.BLACK); // Set default text color
                }

                return c;
            }
        });
              
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

        
        // Enhance button appearance
        updateButton.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        updateButton.setBackground(new Color(0x5b9bd5)); // Blue
        updateButton.setForeground(Color.WHITE);
        updateButton.setFocusPainted(false);
        updateButton.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
        updateButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        getContentPane().add(updateButton, BorderLayout.SOUTH);


        // Enhance table appearance
        JTableHeader header = table.getTableHeader();
        header.setBackground(new Color(0xe1e1e1)); // Light gray
        header.setForeground(Color.BLACK);
        header.setFont(new Font("Segoe UI", Font.BOLD, 12));

        // Alternate row colors
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(0xf2f2f2)); // Lighter gray
                return c;
            }
        });

        // Mouse hover effect
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

        // Styling the JTable
        table.setFont(new Font("Roboto", Font.PLAIN, 12));
        table.setRowHeight(25);
        table.setShowHorizontalLines(true);
        table.setShowVerticalLines(false);
        table.setGridColor(new Color(0xdddddd));

        // Styling the table header
        JTableHeader tableHeader = table.getTableHeader();
        tableHeader.setBackground(new Color(0x2c3e50)); // Dark blue
        tableHeader.setForeground(Color.WHITE);
        tableHeader.setFont(new Font("Roboto", Font.BOLD, 14));
        tableHeader.setReorderingAllowed(false);

        // Add padding to the cells
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        renderer.setHorizontalAlignment(SwingConstants.CENTER);
        renderer.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(renderer);
        }

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
    }


    //Edit this !!!!
    private void updateDataToDatabase(JScrollPane panel, Connection connection) throws SQLException {
        // Get updated data from the table model and update the database
        for (int i = 0; i < tableModel.getRowCount(); i++) {

            

            int orderID = (Integer) tableModel.getValueAt(i, 0);
            String order_status = (String) tableModel.getValueAt(i, 3).toString();

            //If order_status is not an accepted value it will not update 
            if(order_status.equals("cancel")) {
                databaseOperations.deleteOrder(connection, orderID);
                //Also needs to add stock back into database!
            }
            else{
                databaseOperations.updateOrderStatus(connection, STATUS.valueOf(order_status), orderID);
            }


        }

        JOptionPane.showMessageDialog(this, "Data updated to the database");
        refreshTable(connection);
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

    private JComboBox<String> createComboBox() {
        String[] statusOptions = { "pending" ,"cancel" };
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
    

