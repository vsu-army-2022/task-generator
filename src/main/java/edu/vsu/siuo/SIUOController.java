package edu.vsu.siuo;

import edu.vsu.siuo.domains.dto.TaskDto;
import edu.vsu.siuo.word.WordManager;
import edu.vsu.siuo.domains.Settings;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.URL;
import java.util.*;

import static edu.vsu.siuo.word.WordManager.GenerateNameFile;

public class SIUOController implements Initializable {

    public static final String HZRSmall = "НЗР (Малое смещение)";
    public static final String HZRBig = "НЗР (Большое смещение)";
    public static final String DalnomerSmall = "Дальномер (Малое смещение)";
    public static final String DalnomerBig = "Дальномер (Большое смещение)";

    private static final Map<Integer, String> map;

    static {
        map = new HashMap<>();
        map.put(0, HZRSmall);
        map.put(1, HZRBig);
        map.put(2, DalnomerSmall);
        map.put(3, DalnomerBig);
    }

    private Settings settings = null;
    private File selectedDirectory = null;
    static int type = 0;
    private Pane selectedPane;

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
    private CheckBox checkBoxOpenFile;

    @FXML
    protected void buttonCreateTasksOnClick() throws IOException {
        WordManager wordManager = new WordManager(this.selectedDirectory.getAbsolutePath(), GenerateNameFile(map.get(type)));
        if (type == 0){
            wordManager.Write(GenerateNZRLess5.generateTasks(Integer.parseInt(textFieldNumberOfTasks.getText())), type);
        } else if (type == 1){
            wordManager.Write(GenerateNZRMore5.generateTasks(Integer.parseInt(textFieldNumberOfTasks.getText())), type);
        } else if (type == 2){
            wordManager.Write(GenerateDalnomerLess5.generateTasks(Integer.parseInt(textFieldNumberOfTasks.getText())), type);
        } else if (type == 3){
            wordManager.Write(GenerateDalnomerMore5.generateTasks(Integer.parseInt(textFieldNumberOfTasks.getText())), type);
        }
        Process p = Runtime.getRuntime().exec(String.format("rundll32 url.dll,FileProtocolHandler %s\\%s",
                  this.selectedDirectory.getAbsolutePath(), GenerateNameFile(map.get(this.type))));
    }

    @FXML
    protected void menuItemNzrLess5Click() {
        selectPane(paneMain);
        type = 0;
        labelType.setText(HZRSmall);
    }

    @FXML
    protected void menuItemNzrBigMoveClick() {
        selectPane(paneMain);
        type = 1;
        labelType.setText(HZRBig);
    }

    @FXML
    protected void menuItemDalnomerLess5Click() {
        selectPane(paneMain);
        type = 2;
        labelType.setText(DalnomerSmall);
    }

    @FXML
    protected void menuItemDalnomerBigMoveClick() {
        selectPane(paneMain);
        type = 3;
        labelType.setText(DalnomerBig);
    }

    @FXML
    protected void menuItemSettingsGeneralClick() {
        labelSettingsPath.setText(settings.getDefaultPath());
        textFieldMaxNumberTasks.setText(String.valueOf(settings.getMaxCountOfTasks()));
        selectPane(paneSettings);
    }

    @FXML
    protected void buttonChoosePathClick() {
        try {
            DirectoryChooser directoryChooser = new DirectoryChooser();
            directoryChooser.setInitialDirectory(new File(settings.getDefaultPath()));
            this.selectedDirectory = directoryChooser.showDialog(SIUOApplication.getPrimaryStage());
            labelSettingsPath.setText(this.selectedDirectory.getAbsolutePath());
        } catch (Exception e) {
            labelSettingsPath.setText(settings.getDefaultPath());
        }
    }

    @FXML
    public void buttonSaveSettingsClick() {
        settings.setDefaultPath(this.selectedDirectory.getAbsolutePath());
        settings.setMaxCountOfTasks(Integer.parseInt(textFieldMaxNumberTasks.getText()));
        settings.setOpenFile(checkBoxOpenFile.isSelected());
        settings.save();

        menuItemNzrLess5Click();
    }

    @FXML
    protected void menuItemAboutUsClick() {
        selectPane(paneAboutUs);
    }

    @FXML
    protected void menuItemDocumentsClick() {
        selectPane(paneDocuments);
    }

    private void selectPane(Pane pane) {
        this.selectedPane.setVisible(false);
        pane.setVisible(true);
        selectedPane = pane;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("settings.dat"))) {
            settings = (Settings) ois.readObject();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        if (settings == null) {
            settings = new Settings();
        }
        selectedDirectory = new File(settings.getDefaultPath());
        labelSettingsPath.setText(settings.getDefaultPath());
        selectedPane = paneMain;
        selectedPane.setVisible(true);
        paneSettings.setVisible(false);
        paneAboutUs.setVisible(false);
        paneDocuments.setVisible(false);
        textFieldNumberOfTasks.addEventFilter(KeyEvent.KEY_TYPED, t -> {
            char[] ar = t.getCharacter().toCharArray();
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
            char[] ar = t.getCharacter().toCharArray();
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
    private void dragged(MouseEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setX(event.getScreenX() - x);
        stage.setY(event.getScreenY() - y);
    }

    @FXML
    private void pressed(MouseEvent event) {
        x = event.getSceneX();
        y = event.getSceneY();
    }
}