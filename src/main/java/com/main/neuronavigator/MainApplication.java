package com.main.neuronavigator;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.*;
import java.util.*;

public class MainApplication extends Application {
    public static Properties properties;
    public static ResourceBundle resourceBundle;
    public static Locale locale;

    @Override
    public void start(Stage stage) throws IOException {
        properties = new Properties();
        properties.load(MainApplication.class.getClassLoader().getResourceAsStream("config.properties"));
        locale = new Locale(properties.getProperty("lang"), properties.getProperty("country"));
        resourceBundle = ResourceBundle.getBundle("strings", locale);
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("main-view.fxml"), resourceBundle);
        Scene scene = new Scene(fxmlLoader.load(), 1542, 932);
        stage.setTitle("NeuroNavigator");
        stage.getIcons().add(new Image(String.valueOf(MainApplication.class.getResource("NeuroNavigator_Logo.png"))));
        stage.setScene(scene);
        stage.setMinHeight(932);
        stage.setMinWidth(1542);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}