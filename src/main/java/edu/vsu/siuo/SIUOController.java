package edu.vsu.siuo;

import edu.vsu.siuo.word.WordManager;
import edu.vsu.siuo.domains.Settings;
import edu.vsu.siuo.domains.TaskDto;
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
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import static edu.vsu.siuo.word.WordManager.GenerateNameFile;

public class SIUOController implements Initializable {

    public static final String HZRSmall = "НЗР (ПС < 5-00)";
    public static final String HZRBig = "НЗР (Большое смещение)";
    public static final String DalnomerSmall = "Дальномер (ПС < 5-00)";
    public static final String DalnomerBig = "Дальномер (Большое смещение)";

    private static final Map<Integer, String> map;
    static {
        map = new HashMap<>();
        map.put(0, HZRSmall);
        map.put(1, HZRBig);
        map.put(2, DalnomerSmall);
        map.put(3, DalnomerBig);
    }

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
        List<TaskDto> taskDtos = Generate2.generate(Integer.parseInt(textFieldNumberOfTasks.getText()));
        if (isOpenFiles){
            Process p = Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler D:\\ArmyProgram\\src\\main\\resources\\edu\\vsu\\siuo\\test.docx");
        }
    }

    @FXML
    protected void menuItemNzrLess5Click(){
        selectPane(paneMain);
        type = 0;
        labelType.setText(HZRSmall);
    }

    @FXML
    protected void  menuItemNzrBigMoveClick(){
        selectPane(paneMain);
        type = 1;
        labelType.setText(HZRBig);
    }

    @FXML
    protected void  menuItemDalnomerLess5Click(){
        selectPane(paneMain);
        type = 2;
        labelType.setText(DalnomerSmall);
    }

    @FXML
    protected void  menuItemDalnomerBigMoveClick(){
        selectPane(paneMain);
        type = 3;
        labelType.setText(DalnomerBig);
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