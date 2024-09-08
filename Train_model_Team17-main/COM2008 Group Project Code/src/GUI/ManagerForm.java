package GUI;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;

import java.awt.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.EventObject;
import java.util.List;

public class ManagerForm extends JFrame {

    private JTable table;
    private DefaultTableModel tableModel;
    private List<User> userList;
    private DatabaseOperations databaseOperations;

    public ManagerForm() {
        setTitle("User List");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);

        databaseOperations = new DatabaseOperations();
        DatabaseConnectionHandler databaseConnectionHandler = new DatabaseConnectionHandler();

        try {
            databaseConnectionHandler.openConnection();
            userList = databaseOperations.getAllUsers(databaseConnectionHandler.getConnection());
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exception
        }

        

        // Create the table model with column names and data
        Object[] columnNames = {"ID", "Email", "Forename", "Surname", "Role"};
        tableModel = new DefaultTableModel(null, columnNames);
        for (User user : userList) {
            String userRoleAString = "User";
            if(user.getRole() == 1) {
                userRoleAString = "Staff";
            }
            else if(user.getRole() == 2) {
                userRoleAString = "Manager";
            }
            Object[] rowData = {user.getUserID(), user.getEmail(), user.getForename(), user.getSurname(), userRoleAString};
            tableModel.addRow(rowData);
        }

        // Create the JTable with the table model
        table = new JTable(tableModel);

        //Sets the userID table as non-editable
        
        
        JScrollPane scrollPane = new JScrollPane(table);

        getContentPane().add(scrollPane, BorderLayout.CENTER);

        // Create a button to update data to the database
        JButton updateButton = new JButton("Update to Database");
        updateButton.addActionListener(e -> {
            try {
                updateDataToDatabase(scrollPane, databaseConnectionHandler.getConnection());
            } catch (SQLException ex) {
                ex.printStackTrace();
                // Handle exception
            }
        });

        TableColumn roleColumn = table.getColumnModel().getColumn(4);
        roleColumn.setCellEditor(new DefaultCellEditor(createComboBox()));
              
        // Set UserID column uneditable
        TableColumn userIDColumn = table.getColumnModel().getColumn(0);
        userIDColumn.setCellRenderer(createNonEditableRenderer());
        userIDColumn.setCellEditor(createNonEditableEditor());

        // Set Forename column uneditable
        TableColumn forenameColumn = table.getColumnModel().getColumn(2);
        forenameColumn.setCellRenderer(createNonEditableRenderer());
        forenameColumn.setCellEditor(createNonEditableEditor());

        // Set Surname column uneditable
        TableColumn surnameColumn = table.getColumnModel().getColumn(3);
        surnameColumn.setCellRenderer(createNonEditableRenderer());
        surnameColumn.setCellEditor(createNonEditableEditor()); 
        
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

        setupRoleColumn();

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

    private void updateDataToDatabase(JScrollPane panel, Connection connection) throws SQLException {
        int confirm = JOptionPane.showConfirmDialog(this, 
                                                "Are you sure you want to apply changes?", 
                                                "Confirm Changes", 
                                                JOptionPane.YES_NO_OPTION, 
                                                JOptionPane.QUESTION_MESSAGE);
        if (confirm == JOptionPane.YES_OPTION) {
        
        // Get updated data from the table model and update the database
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            
            int userID = (Integer) tableModel.getValueAt(i, 0);
            String email = (String) tableModel.getValueAt(i, 1);
            String forename = (String) tableModel.getValueAt(i, 2);
            String surname = (String) tableModel.getValueAt(i, 3);
            String roleAString = (String) tableModel.getValueAt(i, 4);

            int role;
            if(roleAString.equals("Staff")){
                role = 1;
            }
            else if(roleAString.equals("Manager")){
                role = 2;
            }
            else if(roleAString.equals("User")){
                role = 0;
            }
            else{
                JOptionPane.showMessageDialog(panel, "An insufficient role name has been supplied for user with userID "+userID+". Users role defaulted to 'user' for safety", "Error", JOptionPane.ERROR_MESSAGE);
                role = 0;
            }    
              // Update the user data in the database
            databaseOperations.updateUserData(connection, userID, email, forename, surname, role);
        }

        JOptionPane.showMessageDialog(this, "Data updated to the database");
        refreshTable(connection);
    } else {
        // If user does not confirm, do nothing
        JOptionPane.showMessageDialog(this, "Update cancelled");
        refreshTable(connection);
    }
}

    private void refreshTable(Connection connection) throws SQLException {
        // Clear the existing data in the table model
        tableModel.setRowCount(0);
    
        // Fetch the updated data from the database
        userList = databaseOperations.getAllUsers(connection);
    
        // Populate the table model with the updated data
        for (User user : userList) {
            String userRoleAString = "User";
            if(user.getRole() == 1) {
                userRoleAString = "Staff";
            }
            else if(user.getRole() == 2) {
                userRoleAString = "Manager";
            }
            Object[] rowData = {user.getUserID(), user.getEmail(), user.getForename(), user.getSurname(), userRoleAString};
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
                return false; // Make the cell editor non-editable
            }
        };
    }

    private JComboBox<String> createComboBox() {
        String[] statusOptions = { "User", "Staff", "Manager" };
        JComboBox<String> comboBox = new JComboBox<>(statusOptions);
        comboBox.setEditable(false);
        return comboBox;
    }

    private void setupRoleColumn() {
        TableColumn roleColumn = table.getColumnModel().getColumn(4);
        roleColumn.setCellRenderer(new CustomComboBoxRenderer());
    }
    
    class CustomComboBoxRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
    
            JLabel label = (JLabel) c;
            label.setText(value + " ▼"); 
    
            return label;
        }
    }

}
    

