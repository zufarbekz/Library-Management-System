package Student;

import ExtraClasses.BookRepository;
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
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import logIn.LogInController;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Predicate;

public class Controller {

    @FXML
    public TableView<Books> tblBooks;
    @FXML
    public TableView<Books> tblMyBooks;

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


    //      Labels For MyBook Details
    @FXML
    public Label lblMyBookId;
    @FXML
    public Label lblMyBookTitle;
    @FXML
    public Label lblMyBookAuthor;
    @FXML
    public Label lblMyBookISBN;
    @FXML
    public Label lblMyBookPubDate;
    @FXML
    public Label lblMyBookTakenBy;

    @FXML
    public ChoiceBox<String> choiceBox;
    @FXML
    public TextField txtFilter;

    private ObservableList<Books> bookList;


    private ObservableList<Books> myBooks;

    public void initialize() {
        try {
            this.bookList = BookRepository.getInstance().getAllBooks();
            this.tblBooks.setItems(bookList);

            ObservableList<String> choices = FXCollections.observableArrayList("Title", "Author Name");
            this.choiceBox.setItems(choices);
            this.choiceBox.setValue("Title");


            this.myBooks = BookRepository.getInstance().getMyBooks(LogInController.getStudentID());
            this.tblMyBooks.setItems(myBooks);
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
    public void displayMyBookDetails() {
        try {
            lblMyBookId.setText(tblMyBooks.getSelectionModel().getSelectedItem().getBookID());
            lblMyBookTitle.setText(tblMyBooks.getSelectionModel().getSelectedItem().getTitle());
            lblMyBookAuthor.setText(tblMyBooks.getSelectionModel().getSelectedItem().getAuthor());
            lblMyBookISBN.setText(tblMyBooks.getSelectionModel().getSelectedItem().getIsbn());
            lblMyBookPubDate.setText(tblMyBooks.getSelectionModel().getSelectedItem().getPublishDate());
            lblMyBookTakenBy.setText(tblMyBooks.getSelectionModel().getSelectedItem().getTakenBy());
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
    public void handleReturnBook() {
        String bookID = tblMyBooks.getSelectionModel().getSelectedItem().getBookID();

        try {
            if (UserRepository.getInstance().returnBooks(bookID)) {
                System.out.println("Book returned");
            } else
                System.out.println("Not returned");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
