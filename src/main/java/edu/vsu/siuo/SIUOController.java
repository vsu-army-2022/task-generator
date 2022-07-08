package edu.vsu.siuo;

import edu.vsu.siuo.domains.Settings;
import edu.vsu.siuo.domains.TaskDto;
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
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class SIUOController implements Initializable {
    private static Settings settings = new Settings();

    private File selectedDirectory = null;
    static int type = 0;
    private Pane selectedPane;
    private Boolean isOpenFiles = true;

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
    private Pane paneAboutUs;
    @FXML
    private Pane paneDocuments;
    @FXML
    private Label labelSettingsPath;
    @FXML
    private Button buttonChoosePath;
    @FXML
    private TextField textFieldMaxNumberTasks;

    @FXML
    protected void buttonCreateTasksOnClick() throws IOException {
        System.out.println(selectedDirectory.getAbsolutePath());
        System.out.println("Сгенерировано");
        List<TaskDto> taskDtos = Generate2.generate(1);
        System.out.println(taskDtos);
        if (isOpenFiles){
            Process p = Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler D:\\ArmyProgram\\src\\main\\resources\\edu\\vsu\\siuo\\test.docx");
        }
    }

    @FXML
    protected void menuItemNzrLess5Click(){
        selectPane(paneMain);
        type = 0;
        labelType.setText("НЗР (ПС < 5-00)");
    }

    @FXML
    protected void  menuItemNzrBigMoveClick(){
        selectPane(paneMain);
        type = 1;
        labelType.setText("НЗР (Большое смещение)");
    }

    @FXML
    protected void  menuItemDalnomerLess5Click(){
        selectPane(paneMain);
        type = 2;
        labelType.setText("Дальномер (ПС < 5-00)");
    }

    @FXML
    protected void  menuItemDalnomerBigMoveClick(){
        selectPane(paneMain);
        type = 3;
        labelType.setText("Дальномер (Большое смещение)");
    }

    @FXML
    protected void menuItemSettingsGeneralClick(){
        labelSettingsPath.setText(settings.getDefaultPath());
        textFieldMaxNumberTasks.setText(String.valueOf(settings.getMaxCountOfTasks()));
        selectPane(paneSettings);
    }

    @FXML
    protected void buttonChoosePathClick(){
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setInitialDirectory(new File(settings.getDefaultPath()));
        this.selectedDirectory = directoryChooser.showDialog(SIUOApplication.getPrimaryStage());
        labelSettingsPath.setText(this.selectedDirectory.getAbsolutePath());
    }

    @FXML
    public void buttonSaveSettingsClick() {
        settings.setDefaultPath(this.selectedDirectory.getAbsolutePath());
        settings.setMaxCountOfTasks(Integer.parseInt(textFieldMaxNumberTasks.getText()));
    }

    @FXML
    protected void menuItemAboutUsClick(){
        selectPane(paneAboutUs);
    }

    @FXML
    protected void menuItemDocumentsClick(){
        selectPane(paneDocuments);
    }

    private void selectPane(Pane pane){
        this.selectedPane.setVisible(false);
        pane.setVisible(true);
        selectedPane = pane;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        selectedDirectory = new File(settings.getDefaultPath());
        labelSettingsPath.setText(settings.getDefaultPath());
        selectedPane = paneMain;
        selectedPane.setVisible(true);
        paneSettings.setVisible(false);
        paneAboutUs.setVisible(false);
        paneDocuments.setVisible(false);
        textFieldNumberOfTasks.addEventFilter(KeyEvent.KEY_TYPED, t -> {
            char ar[] = t.getCharacter().toCharArray();
            char ch = ar[t.getCharacter().toCharArray().length - 1];
            int number = 0;
            try {
                number = Integer.parseInt(textFieldNumberOfTasks.getText() + ch);
            } catch (Exception e) {

            }
            if (!(ch >= '0' && ch <= '9' && number <= settings.getMaxCountOfTasks()
                    && (textFieldNumberOfTasks.getText() + ch).length() <= Integer.toString(number).length())
            ) {
                t.consume();
            }
        });
        textFieldMaxNumberTasks.addEventFilter(KeyEvent.KEY_TYPED, t -> {
            char ar[] = t.getCharacter().toCharArray();
            char ch = ar[t.getCharacter().toCharArray().length - 1];
            int number = 0;
            try {
                number = Integer.parseInt(textFieldNumberOfTasks.getText() + ch);
            } catch (Exception e) {

            }
            if (!(ch >= '0' && ch <= '9')
            ) {
                t.consume();
            }
        });
        textFieldMaxNumberTasks.setText(String.valueOf(settings.getMaxCountOfTasks()));
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