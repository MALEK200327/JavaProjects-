package com.sheffield;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseOperations {

    // Insert a new book into the database
    public void insertBook(Book newBook, Connection connection) throws SQLException {
        try {
            // Create an SQL INSERT statement
            String insertSQL = "INSERT INTO Books (isbn, title, author_name,"+
            "publication_year, genre, price) VALUES (?, ?, ?, ?, ?, ?)";

            // Prepare and execute the INSERT statement
            PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);
            preparedStatement.setString(1, newBook.getIsbn());
            preparedStatement.setString(2, newBook.getTitle());
            preparedStatement.setString(3, newBook.getAuthorName());
            preparedStatement.setInt(4, newBook.getPublicationYear());
            preparedStatement.setString(5, newBook.getGenre());
            preparedStatement.setBigDecimal(6, newBook.getPrice());

            int rowsAffected = preparedStatement.executeUpdate();
            System.out.println(rowsAffected + " row(s) inserted successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
            throw e; // Re-throw the exception to signal an error.
        }
    }

    // Get all books from the database
    public void getAllBooks(Connection connection) throws SQLException {
        try {
            String selectSQL = "SELECT * FROM Books";
            PreparedStatement preparedStatement = connection.prepareStatement(selectSQL);
            ResultSet resultSet = preparedStatement.executeQuery();
            System.out.println("<=================== GET ALL BOOKS ====================>");
            while (resultSet.next()) {
                // Print each book's information in the specified format
                System.out.println("{" +
                        "isbn='" + resultSet.getString("isbn") + "'" +
                        ", title='" + resultSet.getString("title") + "'" +
                        ", authorName='" + resultSet.getString("author_name") + "'" +
                        ", publicationYear='" + resultSet.getInt("publication_year") + "'" +
                        ", genre='" + resultSet.getString("genre") + "'" +
                        ", price='" + resultSet.getDouble("price") + "'" +
                        "}");
            }
            System.out.println("<======================================================>");
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;// Re-throw the exception to signal an error.
        }
    }

    // Get a book by ISBN
    public void getBookByISBN(String isbn, Connection connection) throws SQLException {
        try {
            String selectSQL = "SELECT * FROM Books WHERE isbn=?";
            PreparedStatement preparedStatement = connection.prepareStatement(selectSQL);
            preparedStatement.setString(1, isbn);
            ResultSet resultSet = preparedStatement.executeQuery();
            System.out.println("<==================== BOOK BY ISBN =====================>");
            if (resultSet.next()) {
                System.out.println("{" +
                        "isbn='" + resultSet.getString("isbn") + "'" +
                        ", title='" + resultSet.getString("title") + "'" +
                        ", authorName='" + resultSet.getString("author_name") + "'" +
                        ", publicationYear='" + resultSet.getInt("publication_year") + "'" +
                        ", genre='" + resultSet.getString("genre") + "'" +
                        ", price='" + resultSet.getDouble("price") + "'" +
                        "}");
            } else {
                System.out.println("Book with ISBN " + isbn + " not found.");
            }
            System.out.println("<=======================================================>");
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;// Re-throw the exception to signal an error.
        }
    }

    // Update an existing book in the database
    public void updateBook(Book newBook, Connection connection) throws SQLException {
        try {
            String updateSQL = "UPDATE Books SET title=?, author_name=?,"+
            "publication_year=?, genre=?, price=? WHERE isbn=?";
            PreparedStatement preparedStatement = connection.prepareStatement(updateSQL);

            preparedStatement.setString(1, newBook.getTitle());
            preparedStatement.setString(2, newBook.getAuthorName());
            preparedStatement.setInt(3, newBook.getPublicationYear());
            preparedStatement.setString(4, newBook.getGenre());
            preparedStatement.setBigDecimal(5, newBook.getPrice());
            preparedStatement.setString(6, newBook.getIsbn());

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println(rowsAffected + " row(s) updated successfully.");
            } else {
                System.out.println("No rows were updated for ISBN: " + newBook.getIsbn());
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;// Re-throw the exception to signal an error.
        }
    }

    // Delete a book from the database by ISBN
    public void deleteBook(String isbn, Connection connection) throws SQLException {
        try {
            String deleteSQL = "DELETE FROM Books WHERE isbn=?";
            PreparedStatement preparedStatement = connection.prepareStatement(deleteSQL);
            preparedStatement.setString(1, isbn);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println(rowsAffected + " row(s) deleted successfully.");
            } else {
                System.out.println("No rows were deleted for ISBN: " + isbn);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;// Re-throw the exception to signal an error.
        }
    }
}
