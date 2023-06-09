package com.main.neuronavigator;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.*;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;

public class MainApplication extends Application {
    public static Properties properties;
    public static ResourceBundle resourceBundle;
    public static Locale locale;
    public static String propertiesPath;
    private Alert alert=new Alert(Alert.AlertType.NONE);

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
                properties.setProperty("configured","false");
                properties.setProperty("country","EN");
                properties.setProperty("lang","en");
                properties.setProperty("ftp_host","");
                properties.setProperty("ftp_password","");
                properties.setProperty("ftp_port","");
                properties.setProperty("ftp_user","");
                properties.setProperty("mongoDB_connection","");
                properties.setProperty("mongoDB_db","");
                properties.setProperty("mongoDB_collection","");
                properties.setProperty("ddbb_user","");
                properties.setProperty("ddbb_password","");
                properties.setProperty("ddbb_host","");
                properties.store(fos,null);

                /*Cargar el idioma de la aplicación*/
                locale = new Locale(properties.getProperty("lang"), properties.getProperty("country"));
                resourceBundle = ResourceBundle.getBundle("strings", locale);
                Locale.setDefault(locale);

                /*Avisar al usuario de que tiene que configurar la aplicación*/
                alert.setAlertType(Alert.AlertType.WARNING);
                alert.setTitle(MainApplication.resourceBundle.getString("config_warning"));
                alert.setContentText(MainApplication.resourceBundle.getString("config_createdProp"));
                alert.showAndWait();
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