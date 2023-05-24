package com.main.neuronavigator;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.FileOutputStream;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

public class ConfigController implements Initializable {

    private boolean lang_boxChanged = false;
    private static Alert alert = new Alert(Alert.AlertType.NONE);

    @FXML
    private Button btnCLose;

    @FXML
    private ToggleButton ddbb;

    @FXML
    private ToggleButton ftp;

    @FXML
    private Text ftp_IP;

    @FXML
    private Text ftp_answer;

    @FXML
    private TextField ftp_ip;

    @FXML
    private PasswordField ftp_password;

    @FXML
    private TextField ftp_path;

    @FXML
    private TextField ftp_port;

    @FXML
    private Button ftp_test;

    @FXML
    private TextField ftp_user;

    @FXML
    private AnchorPane holderBBDD;

    @FXML
    private AnchorPane holderFTP;

    @FXML
    private Group holderGroup;

    @FXML
    private AnchorPane holderLang;

    @FXML
    private ComboBox<String> lang_box;

    @FXML
    private ToggleButton language;

    @FXML
    private TextField pathProperties;

    @FXML
    private ToggleGroup toggleMenu;

    @FXML
    private Button resetPath;




    @FXML
    void closeAndSave(ActionEvent event) {
        if (lang_boxChanged) {
            changeLang();
            alert.setAlertType(Alert.AlertType.WARNING);
            alert.setTitle(MainApplication.resourceBundle.getString("config_warning"));
            alert.setContentText(MainApplication.resourceBundle.getString("config_change"));
            alert.show();
        }
        Stage stage = (Stage) btnCLose.getScene().getWindow();
        stage.close();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //Hay que ponerle el locale a la aplicación para que el alertType del alert se ponga en el idioma correcto 
        Locale.setDefault(MainApplication.locale);

        //llenar el comboBox con los idiomas disponibles
        fillLang_box();
        lang_box.getSelectionModel().selectedIndexProperty().addListener((observable -> {
            lang_boxChanged = true;
        }));

        //Añadimos los eventos y listeners a los botones del menú lateral
        setLateralMenuControls();



    }

    private void fillLang_box() {
        ObservableList<String> langs = FXCollections.observableArrayList();
        switch (MainApplication.properties.getProperty("country")) {
            case "ES":
                langs.addAll(MainApplication.resourceBundle.getString("langs_es"),
                        MainApplication.resourceBundle.getString("langs_fr"),
                        MainApplication.resourceBundle.getString("langs_en"));
                lang_box.getSelectionModel().select(MainApplication.resourceBundle.getString("langs_es"));
                break;
            case "FR":
                langs.addAll(MainApplication.resourceBundle.getString("langs_es"),
                        MainApplication.resourceBundle.getString("langs_fr"),
                        MainApplication.resourceBundle.getString("langs_en"));
                lang_box.getSelectionModel().select(MainApplication.resourceBundle.getString("langs_fr"));
                break;
            case "EN":
                langs.addAll(MainApplication.resourceBundle.getString("langs_es"),
                        MainApplication.resourceBundle.getString("langs_fr"),
                        MainApplication.resourceBundle.getString("langs_en"));
                lang_box.getSelectionModel().select(MainApplication.resourceBundle.getString("langs_en"));
                break;
        }
        lang_box.getItems().addAll(langs);
    }

    private void changeLang() {
        String lang = lang_box.getSelectionModel().getSelectedItem();
        switch (lang) {
            case "Español":
            case "Espagnol":
            case "Spanish":
                //NECESITAS EL ABSOLUTE PATH
                try (FileOutputStream out = new FileOutputStream("./src/main/resources/config.properties")) {
                    MainApplication.properties.setProperty("lang", "es");
                    MainApplication.properties.setProperty("country", "ES");
                    MainApplication.properties.store(out, null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case "Francés":
            case "Français":
            case "French":
                try (FileOutputStream out = new FileOutputStream("./src/main/resources/config.properties")) {
                    MainApplication.properties.setProperty("lang", "fr");
                    MainApplication.properties.setProperty("country", "FR");
                    MainApplication.properties.store(out, null);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;
            case "Inglés":
            case "Anglais":
            case "English":
                try (FileOutputStream out = new FileOutputStream("./src/main/resources/config.properties")) {
                    MainApplication.properties.setProperty("lang", "en");
                    MainApplication.properties.setProperty("country", "EN");
                    MainApplication.properties.store(out, null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }

    }

    private void setLateralMenuControls(){
        //language
        language.setStyle("-fx-background-color: rgb(75,140,175)");
        language.addEventHandler(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (!language.isSelected())language.setStyle("-fx-background-color: rgba(96,96,96,0.6)");
            }
        });
        language.addEventHandler(MouseEvent.MOUSE_EXITED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if(!language.isSelected())language.setStyle("-fx-background-color: transparent");
            }
        });

        //ftp
        ftp.addEventHandler(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if(!ftp.isSelected()) ftp.setStyle("-fx-background-color: rgba(96,96,96,0.6)");
            }
        });
        ftp.addEventHandler(MouseEvent.MOUSE_EXITED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if(!ftp.isSelected()) ftp.setStyle("-fx-background-color: transparent");
            }
        });

        //bbdd
        ddbb.addEventHandler(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if(!ddbb.isSelected()) ddbb.setStyle("-fx-background-color: rgba(96,96,96,0.6)");
            }
        });
        ddbb.addEventHandler(MouseEvent.MOUSE_EXITED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if(!ddbb.isSelected()) ddbb.setStyle("-fx-background-color: transparent");
            }
        });

        //Añadir las acciones del menú lateral
        toggleMenu.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                oldValue.setSelected(true);
            } else {
                if (newValue.equals(language)) {
                    language.setStyle("-fx-background-color: rgb(75,140,175)");
                    ddbb.setStyle("-fx-background-color: transparent");
                    ftp.setStyle("-fx-background-color: transparent");
                    holderGroup.getChildren().get(0).setVisible(true);
                    holderGroup.getChildren().get(1).setVisible(false);
                    holderGroup.getChildren().get(2).setVisible(false);
                } else if (newValue.equals(ddbb)) {
                    ddbb.setStyle("-fx-background-color: rgb(75,140,175)");
                    language.setStyle("-fx-background-color: transparent");
                    ftp.setStyle("-fx-background-color: transparent");
                    holderGroup.getChildren().get(0).setVisible(false);
                    holderGroup.getChildren().get(1).setVisible(false);
                    holderGroup.getChildren().get(2).setVisible(true);
                } else if (newValue.equals(ftp)) {
                    ftp.setStyle("-fx-background-color: rgb(75,140,175)");
                    language.setStyle("-fx-background-color: transparent");
                    ddbb.setStyle("-fx-background-color: transparent");
                    holderGroup.getChildren().get(0).setVisible(false);
                    holderGroup.getChildren().get(1).setVisible(true);
                    holderGroup.getChildren().get(2).setVisible(false);
                }
            }
        });
    }
}
