package ExtraClasses;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javax.xml.transform.Result;
import java.sql.*;

public class UserRepository {
    private static UserRepository instance;

    private final String DATABASE_URL = "jdbc:derby:./db/dataBase";

    private final String GET_Librarian_QUERY = "SELECT * FROM users WHERE role='Librarian'";
    private final String GET_Student_QUERY = "SELECT * FROM users WHERE role='Student'";
    private final String ADD_QUERY = "INSERT Into users(password, firstName, lastName, userName, role) VALUES(?,?,?,?,?)";
    private final String DELETE_QUERY = "DELETE FROM users WHERE id=?";
    private final String GET_LAST_ID = "SELECT MAX(id) FROM users";
    private final String UPDATE_QUERY = "UPDATE users SET password=? ,firstName=?, lastName=?, userName=?, role=? WHERE id=?";

    private final String GIVE_BOOKS = "SELECT * FROM users WHERE userName=? AND password=?";

    private final String RESET_TAKEN = "UPDATE books SET takenBy=? WHERE bookID=?";


    private Connection connection;

    private PreparedStatement getLibrarianStmt;
    private PreparedStatement getStudentStmt;
    private PreparedStatement addStmt;
    private PreparedStatement deleteStmt;
    private PreparedStatement getLastIdStmt;
    private PreparedStatement updateStmt;

    private PreparedStatement giveBooks;

    private PreparedStatement resetTaken;


    UserRepository() throws SQLException {
        connection = DriverManager.getConnection(DATABASE_URL);

        this.getLibrarianStmt = this.connection.prepareStatement(GET_Librarian_QUERY);
        this.getStudentStmt = this.connection.prepareStatement(GET_Student_QUERY);
        this.addStmt = this.connection.prepareStatement(ADD_QUERY);
        this.deleteStmt = this.connection.prepareStatement(DELETE_QUERY);
        this.getLastIdStmt = this.connection.prepareStatement(GET_LAST_ID);
        this.updateStmt = connection.prepareStatement(UPDATE_QUERY);
        this.giveBooks = connection.prepareStatement(GIVE_BOOKS);

        this.resetTaken = connection.prepareStatement(RESET_TAKEN);
    }

    public static UserRepository getInstance() throws SQLException {
        if (instance == null) {
            return new UserRepository();
        }
        return instance;
    }

    //    METHODS TO GET THE VALUES OF USERS
    public ObservableList<User> getAllLibrarians() throws SQLException {
        ResultSet result;

        ObservableList<User> listOfLibrarians = FXCollections.observableArrayList();

        result = getLibrarianStmt.executeQuery();

        while (result.next()) {
            listOfLibrarians.add(new User(
                    result.getString("id"),
                    result.getString("password"),
                    result.getString("firstName"),
                    result.getString("lastName"),
                    result.getString("userName"),
                    result.getString("role")
            ));
        }

        return listOfLibrarians;
    }

    public ObservableList<User> getAllStudents() throws SQLException {
        ResultSet result;

        ObservableList<User> listOfStudents = FXCollections.observableArrayList();

        result = getStudentStmt.executeQuery();

        while (result.next()) {
            listOfStudents.add(new User(
                    result.getString("id"),
                    result.getString("password"),
                    result.getString("firstName"),
                    result.getString("lastName"),
                    result.getString("userName"),
                    result.getString("role")
            ));
        }

        return listOfStudents;
    }

    //      METHODS TO ADD USERS
    public String addLibrarian(User user) throws SQLException {
        this.addStmt.setString(1, user.getPassword());
        this.addStmt.setString(2, user.getFirstName());
        this.addStmt.setString(3, user.getLastName());
        this.addStmt.setString(4, user.getUserName());
        this.addStmt.setString(5, "Librarian");

        if (this.addStmt.executeUpdate() > 0) {
            ResultSet lastResult = this.getLastIdStmt.executeQuery();
            if (lastResult.next()) {
                return lastResult.getString(1);
            }
        }
        return null;
    }

    public String addStudent(User user) throws SQLException {
        this.addStmt.setString(1, user.getPassword());
        this.addStmt.setString(2, user.getFirstName());
        this.addStmt.setString(3, user.getLastName());
        this.addStmt.setString(4, user.getUserName());
        this.addStmt.setString(5, "Student");

        if (this.addStmt.executeUpdate() > 0) {
            ResultSet lastResult = this.getLastIdStmt.executeQuery();
            if (lastResult.next()) {
                return lastResult.getString(1);
            }
        }
        return null;
    }

    //    METHOD TO DELETE USER
    public void deleteUser(String id) throws SQLException {
        this.deleteStmt.setString(1, id);
        this.deleteStmt.executeUpdate();
    }

    //    METHOD TO UPDATE user
    public void update(User user) throws SQLException {
        this.updateStmt.setString(1, user.getPassword());
        this.updateStmt.setString(2, user.getFirstName());
        this.updateStmt.setString(3, user.getLastName());
        this.updateStmt.setString(4, user.getUserName());
        this.updateStmt.setString(5, user.getRole());
        this.updateStmt.setString(6, user.getId());

        this.updateStmt.executeUpdate();
    }



    public boolean checkGiveBooks(String username, String password, String bookID) throws SQLException {
        ResultSet resultset;
        String studentID;

        giveBooks.setString(1, username);
        giveBooks.setString(2, password);
        resultset = giveBooks.executeQuery();
        if (resultset.next()) {
            studentID = resultset.getString("id");
            resetTaken.setString(1, studentID);
            resetTaken.setString(2, bookID);
            resetTaken.executeUpdate();
            return true;
        }
        return false;
    }

    public boolean returnBooks(String bookID) {
        try {
            resetTaken.setString(1, "1");
            resetTaken.setString(2, bookID);
            resetTaken.executeUpdate();
            return true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return false;
    }


}

