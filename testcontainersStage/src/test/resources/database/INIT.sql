CREATE TABLE messages
(
    ID    number,
    CONTENT VARCHAR2(255) NOT NULL
);

INSERT INTO messages (content) VALUES ('Hello World From Init Script!');