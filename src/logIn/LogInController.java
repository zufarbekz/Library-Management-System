package logIn;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import javax.xml.transform.sax.SAXTransformerFactory;
import java.io.IOException;
import java.sql.*;
import java.sql.SQLException;


public class LogInController {
    @FXML
    public TextField txtUsername;
    @FXML
    public PasswordField txtPassword;
    @FXML
    public Label lblNotification;

    Connection connection = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;

    private String role = null;
    private String id = null;

    public void initialize() {

    }

    public LogInController() {
        connection = LogInRepository.connectDB();
    }

    private static String studentID = "";

    public static String getStudentID() {
        return studentID;
    }

    @FXML
    public void handleLogIn(ActionEvent event) {
        if (logIn()) {
            if (role.equals("Admin")) {
                try {
                    Parent root = FXMLLoader.load(getClass().getResource("/Admin/adminWindow.fxml"));
                    Stage stage = new Stage();
                    stage.setTitle("Admin Window");
                    stage.setScene(new Scene(root, 760, 500));
                    stage.getIcons().add(new Image("/books.png"));
                    stage.show();
                    ((Node) (event.getSource())).getScene().getWindow().hide();
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
            } else if (role.equals("Librarian")) {
                try {
                    Parent root = FXMLLoader.load(getClass().getResource("/Librarian/librarianWindow.fxml"));
                    Stage stage = new Stage();
                    stage.setTitle("Librarian Window");
                    stage.setScene(new Scene(root, 760, 500));
                    stage.getIcons().add(new Image("/books.png"));
                    stage.show();
                    ((Node) (event.getSource())).getScene().getWindow().hide();
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
            } else if (role.equals("Student")) {
                try {
                    studentID = id;
                    Parent root = FXMLLoader.load(getClass().getResource("/Student/studentWindow.fxml"));
                    Stage stage = new Stage();
                    stage.setTitle("Student Window");
                    stage.setScene(new Scene(root, 450, 400));
                    stage.getIcons().add(new Image("/books.png"));
                    stage.show();
                    ((Node) (event.getSource())).getScene().getWindow().hide();
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
            }
        }
    }

    private boolean logIn() {
        String userName = txtUsername.getText();
        String passWord = txtPassword.getText();

        String sql = "SELECT * FROM users WHERE userName=? AND passWord=?";

        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, userName);
            preparedStatement.setString(2, passWord);

            resultSet = preparedStatement.executeQuery();

            if (!resultSet.next()) {
                lblNotification.setText("ERROR: Username or Password is incorrect");
                return false;
            } else {
                lblNotification.setTextFill(Color.GREEN);
                lblNotification.setText("Login Successful\t...Redirecting...");

                role = resultSet.getString("role");
                id = resultSet.getString("id");

                return true;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
    }
}