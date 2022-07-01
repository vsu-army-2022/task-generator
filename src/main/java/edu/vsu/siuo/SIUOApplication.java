package edu.vsu.siuo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class SIUOApplication extends Application {
    private static Stage primaryStage;

    @Override
    public void start(Stage stage) throws IOException {
        setPrimaryStage(stage);
        FXMLLoader fxmlLoader = new FXMLLoader(SIUOApplication.class.getResource("mainPage.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Генератор вариантов для СиУО");
        stage.setScene(scene);
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