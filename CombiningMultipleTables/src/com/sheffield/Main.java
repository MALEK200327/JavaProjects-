// Import necessary libraries and classes
package com.sheffield;

import com.sheffield.model.DatabaseConnectionHandler;
import com.sheffield.views.LoanTableDisplay;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // Create an instance of DatabaseConnectionHandler for managing database connections
        DatabaseConnectionHandler databaseConnectionHandler = new DatabaseConnectionHandler();

        // Execute the Swing GUI application on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            LoanTableDisplay loanTableDisplay = null;
            try {
                // Open a database connection
                databaseConnectionHandler.openConnection();

                // Create and initialize the LoanTableDisplay view using the database connection
                loanTableDisplay = new LoanTableDisplay(databaseConnectionHandler.getConnection());
            } catch (Throwable t) {
                throw new RuntimeException(t);
            } finally {
                // Close the database connection
                databaseConnectionHandler.closeConnection();
            }

            // Make the LoanTableDisplay visible to the user
            loanTableDisplay.setVisible(true);
        });
    }
}