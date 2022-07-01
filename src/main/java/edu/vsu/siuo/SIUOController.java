package edu.vsu.siuo;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.stage.DirectoryChooser;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class SIUOController implements Initializable {
    private int maxNumberOfTasks = 100;

    @FXML
    private Button buttonCreateTasks;
    @FXML
    private TextField textFieldNumberOfTasks;

    @FXML
    protected void buttonCreateTasksOnClick() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setInitialDirectory(new File("src"));
        File selectedDirectory = directoryChooser.showDialog(SIUOApplication.getPrimaryStage());
        System.out.println(selectedDirectory.getAbsolutePath());
        System.out.println("Сгенерировано");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        textFieldNumberOfTasks.addEventFilter(KeyEvent.KEY_TYPED, t -> {
            char ar[] = t.getCharacter().toCharArray();
            char ch = ar[t.getCharacter().toCharArray().length - 1];
            int number = 0;
            try {
                number = Integer.parseInt(textFieldNumberOfTasks.getText() + ch);
            } catch (Exception e) {

            }
            if (!(ch >= '0' && ch <= '9' && number <= maxNumberOfTasks
                    && (textFieldNumberOfTasks.getText() + ch).length() <= Integer.toString(number).length())
            ) {
                t.consume();
            }
        });
    }
}