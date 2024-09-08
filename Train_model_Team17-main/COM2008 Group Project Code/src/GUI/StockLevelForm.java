package GUI;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;


import java.awt.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.EventObject;
import java.util.HashMap;
import java.util.Map;

public class StockLevelForm extends JFrame {

    private JTable table;
    private DefaultTableModel tableModel;
    private HashMap<String, Integer> stockMap;
    private DatabaseOperations databaseOperations;

    public StockLevelForm() {
        setTitle("Stock List");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);
        

        databaseOperations = new DatabaseOperations();
        DatabaseConnectionHandler databaseConnectionHandler = new DatabaseConnectionHandler();

        try {
            databaseConnectionHandler.openConnection();
            stockMap = databaseOperations.getAllStock(databaseConnectionHandler.getConnection());
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exception
        }

        

        // Create the table model with column names and data
        Object[] columnNames = {"ProductCode", "Quantity in Stock"};
        tableModel = new DefaultTableModel(null, columnNames);
        for (Map.Entry<String, Integer> entry : stockMap.entrySet()) {
            
            Object[] rowData = {entry.getKey(),  entry.getValue()};

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
                    StockLevelForm.this,
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

              
        // Set orderID column uneditable
        TableColumn productIDColumn = table.getColumnModel().getColumn(0);
        productIDColumn.setCellRenderer(createNonEditableRenderer());
        productIDColumn.setCellEditor(createNonEditableEditor());

        
        
        // Enhance button appearance
        updateButton.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        updateButton.setBackground(new Color(0x5b9bd5)); // Blue
        updateButton.setForeground(Color.WHITE);
        updateButton.setFocusPainted(false);
        updateButton.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
        updateButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        getContentPane().add(updateButton, BorderLayout.SOUTH);


        JTableHeader header = table.getTableHeader();
        header.setBackground(new Color(0xe1e1e1)); 
        header.setForeground(Color.BLACK);
        header.setFont(new Font("Segoe UI", Font.BOLD, 12));

        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(0xf2f2f2)); 
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

            

            String productID = (String) tableModel.getValueAt(i, 0);

            String quantityStr = (String) tableModel.getValueAt(i, 1).toString();

            try {
                int quantity = Integer.parseInt(quantityStr);

                //CHECK IF QUANTITY IS GREATER THAN 0!
                if (quantity >= 0) {
                    databaseOperations.updateStockLevels(connection, productID, quantity);
                } else {

                }
            } catch (NumberFormatException e) {
                // Handle the case where 'quantityStr' is not a valid integer
                e.printStackTrace();
            }


        }

        JOptionPane.showMessageDialog(this, "Data updated to the database");
        refreshTable(connection);
    }

    

    private void refreshTable(Connection connection) throws SQLException {
        // Clear the existing data in the table model
        tableModel.setRowCount(0);

    
        // Fetch the updated data from the database
        stockMap = databaseOperations.getAllStock(connection);

        stockMap = databaseOperations.getAllStock(connection);

        // Iterate through the stockMap and update the existing tableModel
        for (Map.Entry<String, Integer> entry : stockMap.entrySet()) {
            Object[] rowData = {entry.getKey(), entry.getValue()};
            tableModel.addRow(rowData);
        }
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
                return false; 
            }
        };
    }}
    

