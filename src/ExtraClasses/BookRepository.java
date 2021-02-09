package ExtraClasses;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.sql.PreparedStatement;

public class BookRepository {
    public static BookRepository instance;

    private final String DATABASE_URL = "jdbc:derby:./db/dataBase";

    private final String GET_ALL_QUERY = "SELECT * FROM books";
    private final String GET_QUERY = "SELECT * FROM books WHERE bookID=?";
    private final String ADD_QUERY = "INSERT Into books(takenBy, title, author, isbn, publishDate) VALUES(?,?,?,?,?)";
    private final String DELETE_QUERY = "DELETE FROM books WHERE bookID=?";
    private final String GET_LAST_ID = "SELECT MAX(bookID) FROM books";
    private final String UPDATE_QUERY = "UPDATE books SET takenBy=?, title=? ,author=?, isbn=?, publishDate=? WHERE bookID=?";


    private final String GET_MY_BOOKS = "SELECT * from books inner join users on books.takenBy=users.id  WHERE users.id=?";



    private Connection connection;

    private PreparedStatement getAllStmt;
    private PreparedStatement getStmt;
    private PreparedStatement addStmt;
    private PreparedStatement deleteStmt;
    private PreparedStatement getLastIdStmt;
    private PreparedStatement updateStmt;

    private PreparedStatement getMyBooks;

    BookRepository() throws SQLException {
        this.connection = DriverManager.getConnection(DATABASE_URL);

        this.getAllStmt = this.connection.prepareStatement(GET_ALL_QUERY);
        this.getStmt = this.connection.prepareStatement(GET_QUERY);
        this.addStmt = this.connection.prepareStatement(ADD_QUERY);
        this.deleteStmt = this.connection.prepareStatement(DELETE_QUERY);
        this.getLastIdStmt = this.connection.prepareStatement(GET_LAST_ID);
        this.updateStmt = this.connection.prepareStatement(UPDATE_QUERY);

        this.getMyBooks=connection.prepareStatement(GET_MY_BOOKS);
    }

    public static BookRepository getInstance() throws SQLException {
        if (instance == null) {
            instance = new BookRepository();
        }
        return instance;
    }

    public Books getBook(String bookID) throws SQLException {
        ResultSet result = null;
        Books book = null;

        this.getStmt.setString(1, bookID);
        result = this.getStmt.executeQuery();
        if (result.next()) {
            book = new Books(
                    result.getString("bookID"),
                    result.getString("takenBy"),
                    result.getString("title"),
                    result.getString("author"),
                    result.getString("isbn"),
                    result.getString("publishDate")
            );
        }
        return book;
    }

    public ObservableList<Books> getAllBooks() throws SQLException {
        ResultSet result;
        ObservableList<Books> list = FXCollections.observableArrayList();

        result = this.getAllStmt.executeQuery();

        while (result.next()) {
            list.add(new Books(
                    result.getString("bookID"),
                    result.getString("takenBy"),
                    result.getString("title"),
                    result.getString("author"),
                    result.getString("isbn"),
                    result.getString("publishDate")
            ));
        }
        return list;
    }

    public String add(Books book) throws SQLException {
        this.addStmt.setString(1, book.getTakenBy());
        this.addStmt.setString(2, book.getTitle());
        this.addStmt.setString(3, book.getAuthor());
        this.addStmt.setString(4, book.getIsbn());
        this.addStmt.setString(5, book.getPublishDate());

        if (this.addStmt.executeUpdate()>0) {
            ResultSet lastResult = this.getLastIdStmt.executeQuery();
            if (lastResult.next()) {
                return lastResult.getString(1);
            }
        }
        return null;
    }

    public void update(Books book) throws SQLException {
        this.updateStmt.setString(1, book.getTakenBy());
        this.updateStmt.setString(2, book.getTitle());
        this.updateStmt.setString(3, book.getAuthor());
        this.updateStmt.setString(4, book.getIsbn());
        this.updateStmt.setString(5, book.getPublishDate());
        this.updateStmt.setString(6, book.getBookID());

        this.updateStmt.executeUpdate();
    }

    public void delete(String bookID) throws SQLException {
        this.deleteStmt.setString(1, bookID);
        this.deleteStmt.executeUpdate();
    }

    public ObservableList<Books> getMyBooks(String id) throws SQLException{
        this.getMyBooks.setString(1, id);

        ResultSet result;

        ObservableList<Books> books = FXCollections.observableArrayList();

        result = getMyBooks.executeQuery();

        while (result.next()) {
            books.add(new Books(
                    result.getString("bookID"),
                    result.getString("takenBy"),
                    result.getString("title"),
                    result.getString("author"),
                    result.getString("isbn"),
                    result.getString("publishDate")
            ));
        }

        return books;
    }


}
