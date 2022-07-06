package edu.vsu.siuo;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class SIUOApplication extends Application {
    private static Stage primaryStage;

    @Override
    public void start(Stage stage) throws IOException {
        setPrimaryStage(stage);
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("mainPage.fxml")));
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setTitle("Генератор вариантов для СиУО");
        stage.setScene(new Scene(root));
        stage.show();
    }

    private void setPrimaryStage(Stage stage) {
        SIUOApplication.primaryStage = stage;
    }

    static public Stage getPrimaryStage() {
        return SIUOApplication.primaryStage;
    }

    public static void main(String[] args) {
        launch();
    }
}