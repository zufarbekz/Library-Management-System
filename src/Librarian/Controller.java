package Librarian;

import ExtraClasses.BookRepository;
import ExtraClasses.Books;
import ExtraClasses.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.util.function.Predicate;

public class Controller {
    @FXML
    public TextField txtGiveBookUserName;
    @FXML
    public PasswordField pwGiveBookPassword;
    @FXML
    public Button btnGiveBook;

    private ObservableList<User> studentList;
    private ObservableList<Books> bookList;

    @FXML
    public TableView<User> tblStudents;

    @FXML
    public ChoiceBox<String> choiceBox;
    @FXML
    public TextField txtFilter;

    //    Student's TextFields
    @FXML
    public TextField txtStudPassword;
    @FXML
    public TextField txtStudFirstName;
    @FXML
    public TextField txtStudLastName;
    @FXML
    public TextField txtStudUsername;

    // Books' TextFields
    @FXML
    public TableView<Books> tblBooks;
    @FXML
    public TextField txtTitle;
    @FXML
    public TextField txtAuthor;
    @FXML
    public TextField txtISBN;
    @FXML
    public TextField txtPublishDate;

    //      Labels For Student Details
    @FXML
    public Label lblStudentFirstName;
    @FXML
    public Label lblStudentId;
    @FXML
    public Label lblStudentLastName;
    @FXML
    public Label lblStudentUsername;
    @FXML
    public Label lblStudentPassword;

    //      Labels For Book Details
    @FXML
    public Label lblBookId;
    @FXML
    public Label lblBookTitle;
    @FXML
    public Label lblBookAuthor;
    @FXML
    public Label lblBookISBN;
    @FXML
    public Label lblBookPubDate;
    @FXML
    public Label lblBookTakenBy;

    private final String DATA_BASE = "jdbc:derby:./db/dataBase";
    private final String sql = "SELECT * FROM users WHERE userName=? AND password=?";

    @FXML
    public void addStud() {
        User user = new User(
                "",
                txtStudPassword.getText(),
                txtStudFirstName.getText(),
                txtStudLastName.getText(),
                txtStudUsername.getText(),
                ""
        );

        try {
            Connection connection = DriverManager.getConnection(DATA_BASE);
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, user.getUserName());
            preparedStatement.setString(2, user.getPassword());

            ResultSet result = preparedStatement.executeQuery();

            if (result.next()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Alert");
                alert.setHeaderText("Existing data:");
                alert.setContentText("This user is already in the system!\nPlease,create another one:)");

                alert.showAndWait();
            } else {
                user.setId(UserRepository.getInstance().addStudent(user));
                this.studentList.add(user);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }


    @FXML
    public void deleteStudent() {
        try {
            User user = (User) tblStudents.getSelectionModel().getSelectedItem();
            UserRepository.getInstance().deleteUser(user.getId());
            this.studentList.remove(user);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }


    @FXML
    public void updateStudent() {
        String userName = txtStudUsername.getText();
        String password = txtStudPassword.getText();
        try {
            Connection connection = DriverManager.getConnection(DATA_BASE);
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, userName);
            preparedStatement.setString(2, password);

            ResultSet result = preparedStatement.executeQuery();

            if (result.next()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Alert");
                alert.setHeaderText("Existing data:");
                alert.setContentText("This user is already in the system!\nPlease,create another one:)");

                alert.showAndWait();
            } else {
                try {
                    User user = (User) tblStudents.getSelectionModel().getSelectedItem();
                    user.setId(user.getId());
                    user.setPassword(txtStudPassword.getText());
                    user.setFirstName(txtStudFirstName.getText());
                    user.setLastName(txtStudLastName.getText());
                    user.setUserName(txtStudUsername.getText());
                    user.setRole(user.getRole());

                    UserRepository.getInstance().update(user);
                    this.studentList.set(studentList.indexOf(user), user);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    //Books' add/delete/edit logic
    @FXML
    public void addBook() {
        Books book = new Books(
                "",
                "1",
                txtTitle.getText(),
                txtAuthor.getText(),
                txtISBN.getText(),
                txtPublishDate.getText()
        );
        try {
            book.setBookID(BookRepository.getInstance().add(book));
            this.bookList.add(book);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    public void updateBook() {
        try {
            Books book = (Books) tblBooks.getSelectionModel().getSelectedItem();
            book.setBookID(book.getBookID());
            book.setTakenBy("1");
            book.setTitle(txtTitle.getText());
            book.setAuthor(txtAuthor.getText());
            book.setIsbn(txtISBN.getText());
            book.setPublishDate(txtPublishDate.getText());

            BookRepository.getInstance().update(book);
            this.bookList.set(bookList.indexOf(book), book);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @FXML
    public void deleteBook() {
        try {
            Books book = (Books) tblBooks.getSelectionModel().getSelectedItem();
            BookRepository.getInstance().delete(book.getBookID());
            this.bookList.remove(book);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void initialize() {
        try {
            this.studentList = UserRepository.getInstance().getAllStudents();
            this.tblStudents.setItems(studentList);
            this.bookList = BookRepository.getInstance().getAllBooks();
            this.tblBooks.setItems(bookList);
            ObservableList<String> choices = FXCollections.observableArrayList("Title", "Author Name");
            this.choiceBox.setItems(choices);
            this.choiceBox.setValue("Title");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @FXML
    public void handleLogOut(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/logIn/logIn.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(root, 400, 350));
            stage.setTitle("Log In");
            stage.getIcons().add(new Image("/books.png"));
            stage.show();
            ((Node) (event.getSource())).getScene().getWindow().hide();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    @FXML
    public void displayStudentDetails() {
        try {
            lblStudentId.setText(tblStudents.getSelectionModel().getSelectedItem().getId());
            lblStudentFirstName.setText(tblStudents.getSelectionModel().getSelectedItem().getFirstName());
            lblStudentLastName.setText(tblStudents.getSelectionModel().getSelectedItem().getLastName());
            lblStudentUsername.setText(tblStudents.getSelectionModel().getSelectedItem().getUserName());
            lblStudentPassword.setText(tblStudents.getSelectionModel().getSelectedItem().getPassword());
        } catch (RuntimeException e) {
            System.out.println("Not selected");
        }
    }

    @FXML
    public void displayBookDetails() {
        try {
            lblBookId.setText(tblBooks.getSelectionModel().getSelectedItem().getBookID());
            lblBookTitle.setText(tblBooks.getSelectionModel().getSelectedItem().getTitle());
            lblBookAuthor.setText(tblBooks.getSelectionModel().getSelectedItem().getAuthor());
            lblBookISBN.setText(tblBooks.getSelectionModel().getSelectedItem().getIsbn());
            lblBookPubDate.setText(tblBooks.getSelectionModel().getSelectedItem().getPublishDate());
            lblBookTakenBy.setText(tblBooks.getSelectionModel().getSelectedItem().getTakenBy());
        } catch (RuntimeException e) {
            System.out.println("Not selected");
        }
    }

    @FXML
    public void filter() {
        FilteredList<Books> filteredList = new FilteredList<>(bookList, book -> true);
        tblBooks.setItems(filteredList);

        txtFilter.setOnKeyReleased(keyEvent ->
        {
            switch (choiceBox.getValue())//Switch on choiceBox value
            {
                case "Author Name":
                    txtFilter.textProperty().addListener(((observableValue, oldValue, newValue) -> {
                        filteredList.setPredicate((Predicate<? super Books>) (Books book) -> {
                            String lowerCaseValue = newValue.toLowerCase();
                            if (newValue.isEmpty() || newValue == null) {
                                return true;
                            } else if (book.getAuthor().toLowerCase().contains(lowerCaseValue)) {
                                return true;
                            }
                            return false;
                        });
                    }));
                    break;
                case "Title":
                    txtFilter.textProperty().addListener(((observableValue, oldValue, newValue) -> {
                        filteredList.setPredicate((Predicate<? super Books>) (Books book) -> {
                            String lowerCaseValue = newValue.toLowerCase();
                            if (newValue.isEmpty() || newValue == null) {
                                return true;
                            } else if (book.getTitle().toLowerCase().contains(lowerCaseValue)) {
                                return true;
                            }
                            return false;
                        });
                    }));
                    break;
            }
        });

        SortedList<Books> sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(tblBooks.comparatorProperty());
        tblBooks.setItems(sortedList);
    }


    @FXML
    public void handleGiveBook() {
        String username = txtGiveBookUserName.getText();
        String password = pwGiveBookPassword.getText();
        String bookID = tblBooks.getSelectionModel().getSelectedItem().getBookID();

        try {
            if (UserRepository.getInstance().checkGiveBooks(username, password, bookID)) {
                System.out.println("Book given");
//                UserRepository.getInstance().update(user);
//                this.librarianList.set(librarianList.indexOf(user),user);


            } else
                System.out.println("No user");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }


}
