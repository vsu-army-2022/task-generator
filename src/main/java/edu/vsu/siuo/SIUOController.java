package edu.vsu.siuo;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class SIUOController implements Initializable {
    private int maxNumberOfTasks = 100;
    private File selectedDirectory = null;
    static int type = 0;

    private double x, y;

    @FXML
    private Button buttonCreateTasks;
    @FXML
    private TextField textFieldNumberOfTasks;
    @FXML
    private Label labelType;
    @FXML
    private Pane paneMain;
    @FXML
    private Pane paneSettings;
    @FXML
    private Label labelSettingsPath;
    @FXML
    private Button buttonChoosePath;
    @FXML
    private TextField textFieldNumberTasks;

    @FXML
    protected void buttonCreateTasksOnClick() {
        System.out.println(selectedDirectory.getAbsolutePath());
        System.out.println("Сгенерировано");
    }

    @FXML
    protected void menuItemNzrLess5Click(){
        paneSettings.setVisible(false);
        paneMain.setVisible(true);
        type = 0;
        labelType.setText("НЗР (ПС < 5-00)");
    }

    @FXML
    protected void  menuItemNzrBigMoveClick(){
        paneSettings.setVisible(false);
        paneMain.setVisible(true);
        type = 1;
        labelType.setText("НЗР (Большое смещение)");
    }

    @FXML
    protected void  menuItemDalnomerLess5Click(){
        paneSettings.setVisible(false);
        paneMain.setVisible(true);
        type = 2;
        labelType.setText("Дальномер (ПС < 5-00)");
    }

    @FXML
    protected void  menuItemDalnomerBigMoveClick(){
        paneSettings.setVisible(false);
        paneMain.setVisible(true);
        type = 3;
        labelType.setText("Дальномер (Большое смещение)");
    }

    @FXML
    protected void menuItemSettingsGeneralClick(){
        paneMain.setVisible(false);
        paneSettings.setVisible(true);
    }

    @FXML
    protected void buttonChoosePathClick(){
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setInitialDirectory(new File("src"));
        this.selectedDirectory = directoryChooser.showDialog(SIUOApplication.getPrimaryStage());
        labelSettingsPath.setText(this.selectedDirectory.getAbsolutePath());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        paneMain.setVisible(true);
        paneSettings.setVisible(false);
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
        textFieldNumberTasks.addEventFilter(KeyEvent.KEY_TYPED, t -> {
            char ar[] = t.getCharacter().toCharArray();
            char ch = ar[t.getCharacter().toCharArray().length - 1];
            int number = 0;
            try {
                number = Integer.parseInt(textFieldNumberOfTasks.getText() + ch);
            } catch (Exception e) {

            }
            if (!(ch >= '0' && ch <= '9' && number <= maxNumberOfTasks)
            ) {
                t.consume();
            }
        });
        textFieldNumberTasks.setText(String.valueOf(maxNumberOfTasks));
    }

    @FXML
    private void close(MouseEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    private void min(MouseEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setIconified(true);
    }

    @FXML
    void dragged(MouseEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setX(event.getScreenX() - x);
        stage.setY(event.getScreenY() - y);
    }

    @FXML
    void pressed(MouseEvent event) {
        x = event.getSceneX();
        y = event.getSceneY();
    }

}