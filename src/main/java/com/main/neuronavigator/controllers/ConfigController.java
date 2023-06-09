package com.main.neuronavigator.controllers;

import com.main.neuronavigator.MainApplication;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoSecurityException;
import com.mongodb.MongoTimeoutException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.transport.verification.OpenSSHKnownHosts;
import net.synedra.validatorfx.Validator;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

public class ConfigController implements Initializable {

    private boolean lang_boxChanged = false;
    private static Alert alert = new Alert(Alert.AlertType.NONE);
    private Validator validatorMongo = new Validator();
    private Validator validatorFTP = new Validator();

    @FXML
    private Button btnCLose;

    @FXML
    private Button btnTest;

    @FXML
    private TextField colection;

    @FXML
    private TextField connectionString;

    @FXML
    private TextField database;

    @FXML
    private ToggleButton ddbb;

    @FXML
    private ToggleButton ftp;

    @FXML
    private Text ftp_IP;

    @FXML
    private TextField ftp_host;

    @FXML
    private PasswordField ftp_password;

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
    private ToggleGroup toggleMenu;


    @FXML
    void closeAndSave(ActionEvent event) {
        if (lang_boxChanged) {
            changeLang();
        }
        try (FileOutputStream output = new FileOutputStream(MainApplication.propertiesPath)) {
            // set the properties value
            MainApplication.properties.setProperty("mongoDB_connection", connectionString.getText());
            MainApplication.properties.setProperty("mongoDB_db", database.getText());
            MainApplication.properties.setProperty("mongoDB_collection", colection.getText());
            MainApplication.properties.setProperty("ftp_host", ftp_host.getText());
            MainApplication.properties.setProperty("ftp_port", ftp_port.getText());
            MainApplication.properties.setProperty("ftp_user", ftp_user.getText());
            MainApplication.properties.setProperty("ftp_password", ftp_password.getText());
            MainApplication.properties.store(output, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        alert.setAlertType(Alert.AlertType.WARNING);
        alert.setTitle(MainApplication.resourceBundle.getString("config_warning"));
        alert.setContentText(MainApplication.resourceBundle.getString("config_change"));
        alert.showAndWait();
        Stage stage = (Stage) btnCLose.getScene().getWindow();
        stage.close();
    }

    @FXML
    void testConnectionMongo(ActionEvent event) {
        if (validatorMongo.containsErrors()) {
            alert.setAlertType(Alert.AlertType.ERROR);
            alert.setTitle(MainApplication.resourceBundle.getString("config_errors_title"));
            alert.setContentText(MainApplication.resourceBundle.getString("config_errors_checkFields"));
            alert.showAndWait();
        } else {
            try {
                //Conexion a la base de datos>
                ConnectionString connString = new ConnectionString(connectionString.getText());
                CodecRegistry pojoCodecRegistry = fromProviders(PojoCodecProvider.builder().automatic(true).build());
                CodecRegistry codecRegistry = MongoClientSettings.getDefaultCodecRegistry();
                CodecRegistry registry = fromRegistries(codecRegistry, pojoCodecRegistry);
                MongoClientSettings settings = MongoClientSettings.builder()
                        .applyConnectionString(connString)
                        .codecRegistry(registry)
                        .build();
                MongoClient mongoClient = MongoClients.create(settings);
                if (mongoClient.listDatabaseNames().into(new ArrayList<>()).contains(database.getText())) {
                    if (mongoClient.getDatabase(database.getText()).listCollectionNames().into(new ArrayList<>()).contains(colection.getText())) {
                        alert.setAlertType(Alert.AlertType.INFORMATION);
                        alert.setTitle(MainApplication.resourceBundle.getString("config_success_tittle"));
                        alert.setContentText(MainApplication.resourceBundle.getString("config_success"));
                        alert.showAndWait();
                    } else {
                        alert.setAlertType(Alert.AlertType.ERROR);
                        alert.setTitle(MainApplication.resourceBundle.getString("config_errors_title"));
                        alert.setContentText(MainApplication.resourceBundle.getString("config_errors_mongo_colection"));
                        alert.showAndWait();
                    }
                } else {
                    alert.setAlertType(Alert.AlertType.ERROR);
                    alert.setTitle(MainApplication.resourceBundle.getString("config_errors_title"));
                    alert.setContentText(MainApplication.resourceBundle.getString("config_errors_mongo_db"));
                    alert.showAndWait();
                }
            } catch (MongoSecurityException e) {
                alert.setAlertType(Alert.AlertType.ERROR);
                alert.setTitle(MainApplication.resourceBundle.getString("config_errors_title"));
                alert.setContentText(MainApplication.resourceBundle.getString("config_errors_mongo_auth"));
                alert.showAndWait();
                e.printStackTrace();
            } catch (MongoTimeoutException e) {
                alert.setAlertType(Alert.AlertType.ERROR);
                alert.setTitle(MainApplication.resourceBundle.getString("config_errors_title"));
                alert.setContentText(MainApplication.resourceBundle.getString("config_errors_mongo_timeout"));
                alert.showAndWait();
                e.printStackTrace();
            } catch (Exception e) {
                alert.setAlertType(Alert.AlertType.ERROR);
                alert.setTitle(MainApplication.resourceBundle.getString("config_errors_title"));
                alert.setContentText(MainApplication.resourceBundle.getString("config_errors_general"));
                alert.showAndWait();
                e.printStackTrace();
            }
        }
    }

    @FXML
    void testFTP(ActionEvent event) throws IOException {
        if (!validatorFTP.validate()){
            alert.setAlertType(Alert.AlertType.ERROR);
            alert.setTitle(MainApplication.resourceBundle.getString("config_errors_title"));
            alert.setContentText(MainApplication.resourceBundle.getString("config_errors_checkFields"));
            alert.showAndWait();
        }else {
            try {
                SSHClient client = new SSHClient();
                client.addHostKeyVerifier(new OpenSSHKnownHosts(new File(String.format("%s/.ssh/known_hosts", System.getProperty("user.home")))));
                client.connect(ftp_host.getText(), Integer.parseInt(ftp_port.getText()));
                client.authPassword(ftp_user.getText(), ftp_password.getText());

                if(client.isConnected()){

                    alert.setAlertType(Alert.AlertType.CONFIRMATION);
                    alert.setTitle(MainApplication.resourceBundle.getString("config_success_tittle"));
                    alert.setContentText(MainApplication.resourceBundle.getString("config_FTP_answerOK"));
                    alert.show();
                }
            } catch (Exception e) {
                alert.setAlertType(Alert.AlertType.ERROR);
                alert.setTitle(MainApplication.resourceBundle.getString("config_errors_title"));
                alert.setContentText(MainApplication.resourceBundle.getString("config_FTP_answerWrong"));
                alert.show();
            }
        }
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

        //añadimos los validators para los campos necesarios
        setValidators();

        //añadimos los valores de la configuración si existen:
        fillInputs();
    }


    //Métodos privados para el funcionamiento del controlador
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
                try (FileOutputStream out = new FileOutputStream(MainApplication.propertiesPath)) {
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
                try (FileOutputStream out = new FileOutputStream(MainApplication.propertiesPath)) {
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
                try (FileOutputStream out = new FileOutputStream(MainApplication.propertiesPath)) {
                    MainApplication.properties.setProperty("lang", "en");
                    MainApplication.properties.setProperty("country", "EN");
                    MainApplication.properties.store(out, null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }

    }

    private void setLateralMenuControls() {
        //language
        language.setStyle("-fx-background-color: rgb(75,140,175)");
        language.addEventHandler(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (!language.isSelected()) language.setStyle("-fx-background-color: rgba(96,96,96,0.6)");
            }
        });
        language.addEventHandler(MouseEvent.MOUSE_EXITED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (!language.isSelected()) language.setStyle("-fx-background-color: transparent");
            }
        });

        //ftp
        ftp.addEventHandler(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (!ftp.isSelected()) ftp.setStyle("-fx-background-color: rgba(96,96,96,0.6)");
            }
        });
        ftp.addEventHandler(MouseEvent.MOUSE_EXITED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (!ftp.isSelected()) ftp.setStyle("-fx-background-color: transparent");
            }
        });

        //bbdd
        ddbb.addEventHandler(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (!ddbb.isSelected()) ddbb.setStyle("-fx-background-color: rgba(96,96,96,0.6)");
            }
        });
        ddbb.addEventHandler(MouseEvent.MOUSE_EXITED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (!ddbb.isSelected()) ddbb.setStyle("-fx-background-color: transparent");
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

    private void setValidators() {
        validatorMongo.createCheck().dependsOn("connectionString", connectionString.textProperty()).withMethod(c -> {
            String string = c.get("connectionString");
            if (string.length() == 0) {
                c.error(MainApplication.resourceBundle.getString("config_errors_empty"));
            }
        }).decorates(connectionString).immediate();

        validatorMongo.createCheck().dependsOn("db", database.textProperty()).withMethod(c -> {
            String string = c.get("db");
            if (string.length() == 0) {
                c.error(MainApplication.resourceBundle.getString("config_errors_empty"));
            }
        }).decorates(database).immediate();

        validatorMongo.createCheck().dependsOn("colection", colection.textProperty()).withMethod(c -> {
            String string = c.get("colection");
            if (string.length() == 0) {
                c.error(MainApplication.resourceBundle.getString("config_errors_empty"));
            }
        }).decorates(colection).immediate();

        validatorFTP.createCheck().dependsOn("host", ftp_host.textProperty()).withMethod(c -> {
            String string = c.get("host");
            if (string.length() == 0) {
                c.error(MainApplication.resourceBundle.getString("config_errors_empty"));
            }
        }).decorates(ftp_host).immediate();

        validatorFTP.createCheck().dependsOn("port", ftp_port.textProperty()).withMethod(c -> {
            String string = c.get("port");
            if (string.length() == 0) {
                c.error(MainApplication.resourceBundle.getString("config_errors_empty"));
            }
        }).decorates(ftp_port).immediate();

        validatorFTP.createCheck().dependsOn("user", ftp_user.textProperty()).withMethod(c -> {
            String string = c.get("user");
            if (string.length() == 0) {
                c.error(MainApplication.resourceBundle.getString("config_errors_empty"));
            }
        }).decorates(ftp_user).immediate();

        validatorFTP.createCheck().dependsOn("password", ftp_password.textProperty()).withMethod(c -> {
            String string = c.get("password");
            if (string.length() == 0) {
                c.error(MainApplication.resourceBundle.getString("config_errors_empty"));
            }
        }).decorates(ftp_password).immediate();


    }

    private void fillInputs() {
        Properties p = MainApplication.properties;
        ftp_host.setText(p.getProperty("ftp_host"));
        ftp_port.setText(p.getProperty("ftp_port"));
        ftp_user.setText(p.getProperty("ftp_user"));
        ftp_password.setText(p.getProperty("ftp_password"));
        connectionString.setText(p.getProperty("mongoDB_connection"));
        database.setText(p.getProperty("mongoDB_db"));
        colection.setText(p.getProperty("mongoDB_collection"));
    }
}
