package com.example.mp3playerproject;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("Scene.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Angel's MP3 Player!");
        stage.setScene(scene);
        stage.show();

        stage.setOnCloseRequest(event -> {
            Platform.exit();
            System.out.println(0);
        });
    }

    public static void main(String[] args) {
        launch();
    }
}