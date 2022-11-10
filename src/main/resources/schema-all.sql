DROP TABLE coffee IF EXISTS;

CREATE TABLE coffee (
     id int  AUTO_INCREMENT PRIMARY KEY,
     blend VARCHAR(20),
     strength VARCHAR(20),
     origin VARCHAR(20)
);
