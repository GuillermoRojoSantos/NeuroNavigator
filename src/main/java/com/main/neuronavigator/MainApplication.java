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
    public static String propertiesPath;

    @Override
    public void start(Stage stage) throws IOException {
        propertiesPath = String.format("%s\\Documents\\NeuroNavigator\\config.properties", System.getProperty("user.home"));
        createProperties();

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

    private void createProperties() {
        File file = new File(propertiesPath);
        if (!file.exists()) {
            try {
                File dir = new File(propertiesPath);
                dir.getParentFile().mkdirs();
                FileOutputStream fos = new FileOutputStream(propertiesPath);
                properties=new Properties();
                properties.setProperty("configured","true");
                properties.setProperty("country","ES");
                properties.setProperty("lang","es");
                properties.setProperty("ftp_IP","");
                properties.setProperty("ftp_password","");
                properties.setProperty("ftp_path","");
                properties.setProperty("ftp_port","");
                properties.setProperty("ftp_user","");
                properties.store(fos,null);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }else {
            try {
                FileInputStream fis = new FileInputStream(propertiesPath);
                properties=new Properties();
                properties.load(fis);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }
}