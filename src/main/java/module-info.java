module edu.vsu.siuo {
    requires javafx.controls;
    requires javafx.fxml;
    requires lombok;

    opens edu.vsu.siuo to javafx.fxml;
    exports edu.vsu.siuo;
}