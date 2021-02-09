package Student;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class mainStudent extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("studentWindow.fxml"));
        primaryStage.setTitle("Student");
        primaryStage.setScene(new Scene(root, 600, 350));
        primaryStage.getIcons().add(new Image("/books.png"));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

