package com.sheffield.views;

import com.sheffield.model.DatabaseOperations;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoanTableDisplay extends JFrame {
    private JTable loanTable;

    public LoanTableDisplay(Connection connection) throws SQLException {
        setTitle("Loan Information");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 400);

        // Create a DefaultTableModel for the JTable
        DefaultTableModel tableModel = new DefaultTableModel();
        loanTable = new JTable(tableModel);
        tableModel.addColumn("Member ID");
        tableModel.addColumn("Forename");
        tableModel.addColumn("Surname");
        tableModel.addColumn("Title");
        tableModel.addColumn("ISBN");
        tableModel.addColumn("Copy ID");
        tableModel.addColumn("Due Date");

        DatabaseOperations databaseOperations = new DatabaseOperations();
        ResultSet resultSet = databaseOperations.getBooksOverDueDate(connection);
        // Populate the JTable with the query results
        while (resultSet.next()) {
            tableModel.addRow(new Object[]{
                    resultSet.getInt("memberID"),
                    resultSet.getString("forename"),
                    resultSet.getString("surname"),
                    resultSet.getString("title"),
                    resultSet.getString("isbn"),
                    resultSet.getString("copyID"),
                    resultSet.getDate("dueDate")
            });
        }

        resultSet.close();
        connection.close();
        // Create a JScrollPane to display the table
        JScrollPane scrollPane = new JScrollPane(loanTable);
        getContentPane().add(scrollPane, BorderLayout.CENTER);
    }


}
