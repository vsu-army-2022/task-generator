module edu.vsu.siuo {
    requires javafx.controls;
    requires javafx.fxml;


    opens edu.vsu.siuo to javafx.fxml;
    exports edu.vsu.siuo;
}