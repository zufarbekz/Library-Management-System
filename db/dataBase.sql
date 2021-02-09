DROP TABLE books;
DROP TABLE users;

CREATE TABLE users
(
    id        INT         NOT NULL GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    password  VARCHAR(20) NOT NULL,
    firstName VARCHAR(20) NOT NULL,
    lastName  VARCHAR(20) NOT NULL,
    userName  VARCHAR(20) NOT NULL,
    role      VARCHAR(10) NOT NULL
);
CREATE TABLE books
(
    bookID      INT         NOT NULL GENERATED ALWAYS AS IDENTITY,
    takenBy     INT         NOT NULL REFERENCES users (id),
    title       VARCHAR(20) NOT NULL,
    author      VARCHAR(20) NOT NULL,
    isbn        VARCHAR(20) NOT NULL,
    publishDate VARCHAR(20) NOT NULL
);

INSERT INTO users (password, firstName, lastName, userName, role)
VALUES ('123', 'Ulugbek', 'Yarkinov', 'Ulugbek', 'Admin'),
       ('456', 'Zufarbek', 'Zufarbekov', 'Zufar', 'Librarian'),
       ('789', 'Bois', 'Boiskhonov', 'Bois', 'Student');

INSERT INTO books (takenBy, title, author, isbn, publishDate)
VALUES (1, 'Academic English 3', 'Williams', '500520', '12.01.2009'),
       (1, 'OOP2', 'Deitel', '100123', '03.12.2015'),
       (1, 'Calculus', 'Markov', '100123', '03.12.2015'),
       (1, 'Death to Smoochy', 'Prentice Grishenkov', '767639980-7', '13.06.2020'),
       (1, 'Gunfighters', 'Beltran Bretton', '718976933-1', '27.07.2020'),
       (1, 'Meatballs III', 'Cecile Kaminski', '284457806-3', '03.03.2019'),
       (1, 'Babbitt', 'Bryn Canny', '769084988-1', '14.04.2019'),
       (1, 'This Christmas', 'Corliss Whitloe', '499415320-5', '25.12.2020'),
       (1, 'Machine Gun Preacher', 'Evangelina Millier', '759082658-8', '26.02.2020'),
       (1, 'Hellbenders', 'Ignace Tribbeck', '169754199-2', '23.04.2020');

