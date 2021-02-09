package Admin;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class mainAdmin extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("adminWindow.fxml"));
        primaryStage.setTitle("Admin");
        primaryStage.setScene(new Scene(root, 600, 350));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

