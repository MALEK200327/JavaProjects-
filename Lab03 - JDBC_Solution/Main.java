import java.math.BigDecimal;
import com.sheffield.Book;
import com.sheffield.DatabaseConnectionHandler;
import com.sheffield.DatabaseOperations;

public class Main {
    public static void main(String[] args) {
        DatabaseConnectionHandler databaseConnectionHandler = new DatabaseConnectionHandler();
        DatabaseOperations databaseOperations = new DatabaseOperations();
        try {
            databaseConnectionHandler.openConnection();

            // // Adding a book to the database.
            Book book1 = new Book("9780099549482", "To Kill a Mockingbird", "Harper Lee", 1960, "Fiction", 
                    new BigDecimal(12.99));
            databaseOperations.insertBook(book1, databaseConnectionHandler.getConnection());

            // // Adding another book to the database
            Book book2 = new Book("9780198185215", "1984", "George Orwell", 1949, "Science Fiction", 
                    new BigDecimal(9.99));
            databaseOperations.insertBook(book2, databaseConnectionHandler.getConnection());

            // Retrieving all the books and printing them out.
            databaseOperations.getAllBooks(databaseConnectionHandler.getConnection());

            // Updating the publication year for the book1
            book1.setPublicationYear(1970);
            databaseOperations.getBookByISBN(book1.getIsbn(), databaseConnectionHandler.getConnection());

            // Updating the price of book2
            book2.setPrice(BigDecimal.valueOf(19.00));
            databaseOperations.getBookByISBN(book2.getIsbn(), databaseConnectionHandler.getConnection());

            // Deleting book1 and book2
            databaseOperations.deleteBook(book1.getIsbn(), databaseConnectionHandler.getConnection());
            databaseOperations.deleteBook(book2.getIsbn(), databaseConnectionHandler.getConnection());
            databaseOperations.getAllBooks(databaseConnectionHandler.getConnection());

        } catch (Throwable e) {
            e.printStackTrace();
        } finally {
            databaseConnectionHandler.closeConnection();
        }
    }
}
