
DROP TABLE BOOKS;
DROP TABLE PATRONS;
DROP TABLE CHECK_OUTS;

CREATE TABLE BOOKS
(
  ISBN               INTEGER CONSTRAINT BOOKS_PK PRIMARY KEY,
  TITLE              VARCHAR(1000), 
  AUTHOR_LAST_NAME   VARCHAR(200),
  AUTHOR_FIRST_NAME  VARCHAR(200)
);

-- Insert a few default records into this table
INSERT INTO BOOKS
  (ISBN, TITLE, AUTHOR_LAST_NAME, AUTHOR_FIRST_NAME)
 VALUES (123456, 'Absolute Java', 'Savitch', 'Walter');
INSERT INTO BOOKS
  (ISBN, TITLE, AUTHOR_LAST_NAME, AUTHOR_FIRST_NAME)
 VALUES (098765, 'The Lost Symbol', 'Brown', 'Dan');
INSERT INTO BOOKS
  (ISBN, TITLE, AUTHOR_LAST_NAME, AUTHOR_FIRST_NAME)
 VALUES (555433, 'Fast Food Nation', 'Schlosser', 'Eric');
COMMIT;

CREATE TABLE PATRONS
(
  SSN         VARCHAR(11)  CONSTRAINT PATRONS_PK PRIMARY KEY,
  LAST_NAME   VARCHAR(200),
  FIRST_NAME  VARCHAR(200),
  DOB         DATE
);

-- ITP 220 insert some default PATRON information

-- ITP 220 create the CHECK_OUTS table with at least the following fields:
--  ISBN (primary key), SSN (of the patron), DUE_DATE
