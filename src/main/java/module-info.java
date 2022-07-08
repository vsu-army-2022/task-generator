module edu.vsu.siuo {
    requires javafx.controls;
    requires javafx.fxml;
    requires lombok;
    requires spire.doc.free;
    requires java.desktop;

    opens edu.vsu.siuo to javafx.fxml;
    exports edu.vsu.siuo;
}