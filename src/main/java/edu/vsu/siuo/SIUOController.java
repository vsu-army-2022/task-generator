package edu.vsu.siuo;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class SIUOController {
    @FXML
    private Button buttonCreateTasks;

    @FXML
    protected void buttonCreateTasksOnClick() {
        System.out.println("Сгенерировано");
    }
}