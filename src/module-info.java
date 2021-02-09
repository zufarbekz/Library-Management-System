module group.project.compilemile {
    requires javafx.fxml;
    requires javafx.controls;
    requires java.sql;
//    requires javafx.graphics;

    opens logIn;
    opens Admin;
    opens Librarian;
    opens Student;
    opens ExtraClasses;
}