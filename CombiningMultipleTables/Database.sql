CREATE TABLE BookTitle (
       isbn VARCHAR(13) NOT NULL PRIMARY KEY,   -- ISBN (unique identifier)
       title VARCHAR(30),                      -- Title of the book
       author VARCHAR(20),                     -- Author of the book
       publisher VARCHAR(30),                  -- Publisher of the book
       datePublished DATE                       -- Date the book was published
);

-- Table to store information about individual copies of books.
CREATE TABLE BookCopy (
      copyID CHAR(30) NOT NULL PRIMARY KEY,    -- Copy ID (unique identifier)
      isbn VARCHAR(13),                        -- ISBN of the book this copy belongs to
      FOREIGN KEY (isbn) REFERENCES BookTitle(isbn)  -- Foreign key relationship with BookTitle
);

-- Table to store information about library members/borrowers.
CREATE TABLE Borrower (
      memberID INT(10) NOT NULL PRIMARY KEY,   -- Member ID (unique identifier)
      forename VARCHAR(50),                   -- First name of the borrower
      surname VARCHAR(50),                    -- Last name of the borrower
      email VARCHAR(50)                       -- Email address of the borrower
);

-- Table to manage book loans.
CREATE TABLE Loan (
  memberID INT(10) NOT NULL,              -- Member ID of the borrower
  copyID CHAR(30) NOT NULL,               -- Copy ID of the book being loaned
  issueDate DATE,                         -- Date when the book was borrowed
  dueDate DATE,                           -- Due date for returning the book
  PRIMARY KEY (memberID, copyID),         -- Composite primary key
  FOREIGN KEY (memberID) REFERENCES Borrower(memberID),   -- Foreign key relationship with Borrower
  FOREIGN KEY (copyID) REFERENCES BookCopy(copyID)        -- Foreign key relationship with BookCopy
);

-- Insert records into the BookTitle table
INSERT INTO BookTitle (isbn, title, author, publisher, datePublished) VALUES
('9780451524935', 'To Kill a Mockingbird', 'Harper Lee', 'PublisherA', '1960-07-11'),
('9780061120084', '1984', 'George Orwell', 'PublisherB', '1949-06-08'),
('9780062390747', 'The Great Gatsby', 'F. Scott Fitzgerald', 'PublisherC', '1925-04-10'),
('9780141983767', 'Animal Farm', 'George Orwell', 'PublisherB', '1945-08-17');

-- Insert records into the BookCopy table
INSERT INTO BookCopy (copyID, isbn) VALUES
('ABC123456789', 9780451524935),    -- Copy of 'To Kill a Mockingbird'
('DEF987654321', 9780451524935),    -- Another copy of 'To Kill a Mockingbird'
('GHI123987654', 9780061120084),    -- Copy of '1984'
('JKL456123789', 9780061120084),    -- Another copy of '1984'
('MNO789654321', 9780141983767),    -- Copy of 'Animal Farm'
('PQR654987321', 9780141983767);    -- Another copy of 'Animal Farm'

-- Insert records into the Borrower table
INSERT INTO Borrower (memberID, forename, surname, email) VALUES
(1, 'John', 'Doe', 'john.doe@email.com'),  -- John Doe
(2, 'Jane', 'Smith', 'jane.smith@email.com'),  -- Jane Smith
(3, 'Bob', 'Johnson', 'bob.johnson@email.com'),  -- Bob Johnson
(4, 'Alice', 'Brown', 'alice.brown@email.com');  -- Alice Brown

-- Insert records into the Loan table
INSERT INTO Loan (memberID, copyID, issueDate, dueDate) VALUES
(1, 'MNO789654321', '2023-10-01', DATE_ADD('2023-10-01', INTERVAL 14 DAY)),  -- John Doe borrows a book
(1, 'ABC123456789', '2023-10-15', DATE_ADD('2023-10-15', INTERVAL 14 DAY)),  -- John Doe borrows a book
(2, 'DEF987654321', '2023-10-20', DATE_ADD('2023-10-20', INTERVAL 14 DAY)),  -- Jane Smith borrows a book
(3, 'GHI123987654', '2023-10-25', DATE_ADD('2023-10-25', INTERVAL 14 DAY)),  -- Bob Johnson borrows a book
(4, 'JKL456123789', '2023-10-30', DATE_ADD('2023-10-30', INTERVAL 14 DAY));  -- Alice Brown borrows a book


-- Select information about overdue books and their borrowers
SELECT b.memberID, b.forename, b.surname, t.title, t.isbn, c.copyID, m.dueDate
FROM Borrower b, BookTitle t, BookCopy c, Loan m
-- Filter the results to find books with due dates earlier than the current date
WHERE m.dueDate < CURRENT_DATE
  AND m.copyID = c.copyID          -- Join BookCopy table to Loan table
  AND c.isbn = t.isbn              -- Join BookTitle table to BookCopy table
  AND m.memberID = b.memberID;     -- Join Borrower table to Loan table
