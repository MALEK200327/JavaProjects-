CREATE TABLE Books (
  isbn CHAR(13) NOT NULL,
  title VARCHAR(100) NULL,
  author_name VARCHAR(100) NULL,
  publication_year INT NULL,
  genre VARCHAR(30) NULL,
  price DECIMAL(8,2) NULL,
  PRIMARY KEY (isbn),
);

