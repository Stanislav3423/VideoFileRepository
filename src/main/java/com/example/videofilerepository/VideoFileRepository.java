package com.example.videofilerepository;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.collections.FXCollections;

import java.io.IOException;

/*
 * Клас для запуску програми
 * Викликає контроллер головного вікна
 */
public class VideoFileRepository extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        // Завантаження нової сцени
        FXMLLoader fxmlLoader = new FXMLLoader(VideoFileRepository.class.getResource("MainMenu.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1188, 686);
        stage.setTitle("Video File Explorer");
        Image icon = new Image("D:\\Universe\\Sem3\\Cursova\\Project\\VideoFileRepository\\icon.png");
        stage.getIcons().add(icon);
        stage.setScene(scene);
        Controller controller = fxmlLoader.getController();
        // Опис подій-результатів вводу гарячих клавіш для виводу та вводу інформації файлів
        scene.setOnKeyReleased(event -> {
            if (event.isControlDown()) {
                if (event.getCode().equals(KeyCode.R)) {
                    controller.readingFile();
                } else if (event.getCode().equals(KeyCode.W)) {
                    controller.writeMainClassKey();
                }
            }
        });
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}