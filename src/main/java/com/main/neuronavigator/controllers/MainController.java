package com.main.neuronavigator.controllers;

import com.main.neuronavigator.DAOMongoDB.PatientDAOMongoDB;
import com.main.neuronavigator.FTPManager.FTPUtils;
import com.main.neuronavigator.MainApplication;
import com.main.neuronavigator.PacientesListener;
import com.main.neuronavigator.models.Patient;
import com.mongodb.client.FindIterable;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.ConnectException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class MainController implements Initializable, PacientesListener {

    private static PatientDAOMongoDB patientDAOMongoDB;
    private static FTPUtils ftpUtils;
    public static List<Patient> patientsHolder = new ArrayList<>();
    public static Patient patientHolder = null;
    private Alert alert = new Alert(Alert.AlertType.NONE);


    @FXML
    private MenuItem addFile;

    @FXML
    private MenuItem deleteFile;

    @FXML
    private TableColumn<String, String> doc_name;

    @FXML
    private MenuItem downloadFile;

    @FXML
    private MenuItem itemDelete;

    @FXML
    private MenuItem itemUpdate;

    @FXML
    private TableColumn<Patient, String> p_lastName;

    @FXML
    private TableColumn<Patient, String> p_motive;

    @FXML
    private TableColumn<Patient, String> p_name;

    @FXML
    private TableColumn<Patient, String> p_phone;

    @FXML
    private TableColumn<Patient, String> p_phoneDad;

    @FXML
    private TableColumn<Patient, String> p_phoneMum;

    @FXML
    private TableView<String> table_docs;

    @FXML
    private TableView<Patient> table_patients;


    @FXML
    void getPatient(MouseEvent event) {
        if (table_patients.getSelectionModel().getSelectedItems().size() == 1) {
            patientHolder = table_patients.getSelectionModel().getSelectedItem();
            itemUpdate.setDisable(false);
            switch (event.getClickCount()) {
                case 1 -> {
                    Task<ObservableList<String>> task = new Task<>() {
                        @Override
                        protected ObservableList<String> call() throws Exception {
                            return FXCollections.observableArrayList(ftpUtils.listFiles(patientHolder.getPhone()));
                        }
                    };

                    task.setOnSucceeded(e -> {
                        if (task.getValue().isEmpty()) {
                            deleteFile.setDisable(true);
                            downloadFile.setDisable(true);
                        } else {
                            deleteFile.setDisable(false);
                            downloadFile.setDisable(false);
                        }
                        ObservableList<String> documents = task.getValue();
                        table_docs.getItems().clear();
                        table_docs.getItems().addAll(documents);
                    });

                    task.setOnFailed(e -> {
                        Throwable exception = task.getException();
                        boolean b = exception instanceof IOException;
                        if(!(exception instanceof ConnectException)){
                            new Thread(()->{
                                try {
                                    ftpUtils.createDir(patientHolder.getPhone());
                                } catch (IOException ex) {
                                    throw new RuntimeException(ex);
                                }
                            }).start();
                        }
                    });

                    Thread thread = new Thread(task);
                    thread.setDaemon(true); // Marcar el hilo como demonio para que se cierre automáticamente al salir de la aplicación
                    thread.start();
                }
                case 2 -> {

                    try {
                        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("infoPatient-view.fxml"), MainApplication.resourceBundle);
                        Parent root = fxmlLoader.load();
                        Stage stage = new Stage();
                        stage.setTitle(MainApplication.resourceBundle.getString("add_windowTitle1"));
                        stage.getIcons().add(new Image(String.valueOf(MainApplication.class.getResource("NeuroNavigator_Logo.png"))));
                        stage.initOwner(((TableView) event.getSource()).getScene().getWindow());
                        stage.setScene(new Scene(root));
                        stage.initModality(Modality.WINDOW_MODAL);
                        stage.setResizable(false);
                        InfoPatientController infoPatientController=fxmlLoader.getController();
                        infoPatientController.setPacientesListener(this);
                        stage.show();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        } else if (table_patients.getSelectionModel().getSelectedItems().size() >= 2) {

            itemUpdate.setDisable(true);
            patientsHolder.addAll(table_patients.getSelectionModel().getSelectedItems());
        }
    }

    @FXML
    void addPatient(ActionEvent event) {
        try {
            //print the path of the resource from the class loader
            FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("add-view.fxml"), MainApplication.resourceBundle);
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle(MainApplication.resourceBundle.getString("add_windowTitle"));
            stage.getIcons().add(new Image(String.valueOf(MainApplication.class.getResource("NeuroNavigator_Logo.png"))));
            stage.initOwner(((MenuItem) event.getSource()).getParentPopup().getOwnerWindow());
            stage.setScene(new Scene(root));
            stage.initModality(Modality.WINDOW_MODAL);
            stage.setMinWidth(1073);
            stage.setMinHeight(398);
            stage.setResizable(false);
            AddController addController = fxmlLoader.getController();
            addController.setPacientesListener(this);
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void delete(ActionEvent event) {
        if (table_patients.getSelectionModel().getSelectedItems().size() == 1) {
            if (patientDAOMongoDB.deletePatient(patientHolder).getDeletedCount() == 1) {
                new Thread(()->{
                    try {
                        ftpUtils.delDir(patientHolder.getPhone());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }).start();
                alert.setAlertType(Alert.AlertType.CONFIRMATION);
                alert.setContentText(MainApplication.resourceBundle.getString("mainTable_operations_delete_success"));
                alert.setTitle(MainApplication.resourceBundle.getString("mainTable_operations_tittle"));
                alert.showAndWait();
            }
        } else {
            if (patientDAOMongoDB.deleteManyPatients(patientsHolder).getDeletedCount() >= 1) {
                patientsHolder.forEach(p->{
                    new Thread(()->{
                        try {
                            ftpUtils.delDir(p.getPhone());
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }).start();
                });
                alert.setAlertType(Alert.AlertType.CONFIRMATION);
                alert.setContentText(MainApplication.resourceBundle.getString("mainTable_operations_delete_success"));
                alert.setTitle(MainApplication.resourceBundle.getString("mainTable_operations_tittle"));
                alert.showAndWait();
            }
        }
        actualizarTabla();
    }

    @FXML
    void openConfig(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("config-view.fxml"), MainApplication.resourceBundle);
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle(MainApplication.resourceBundle.getString("config_windowTitle"));
            stage.getIcons().add(new Image(String.valueOf(MainApplication.class.getResource("NeuroNavigator_Logo.png"))));
            stage.initOwner(((MenuItem) event.getSource()).getParentPopup().getOwnerWindow());
            stage.setScene(new Scene(root));
            stage.initModality(Modality.WINDOW_MODAL);
            stage.setMinWidth(1359);
            stage.setMinHeight(650);
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void update(ActionEvent event) {
        actualizarTabla();
        table_docs.getItems().removeAll();
    }

    @FXML
    void addAFile(ActionEvent event) {
        //filejooser with multiple selection and save the files in a list
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(MainApplication.resourceBundle.getString("menu_I_addFiles"));
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Files", "*.*"),
                new FileChooser.ExtensionFilter("PDF", "*.pdf"),
                new FileChooser.ExtensionFilter("DOCX", "*.docx"),
                new FileChooser.ExtensionFilter("DOC", "*.doc"),
                new FileChooser.ExtensionFilter("XLSX", "*.xlsx"),
                new FileChooser.ExtensionFilter("XLS", "*.xls"),
                new FileChooser.ExtensionFilter("PPTX", "*.pptx"),
                new FileChooser.ExtensionFilter("PPT", "*.ppt"),
                new FileChooser.ExtensionFilter("TXT", "*.txt"),
                new FileChooser.ExtensionFilter("PNG", "*.png"),
                new FileChooser.ExtensionFilter("JPG", "*.jpg"),
                new FileChooser.ExtensionFilter("JPEG", "*.jpeg")
        );
        List<File> files = fileChooser.showOpenMultipleDialog(((MenuItem) event.getSource()).getParentPopup().getOwnerWindow());
        if (files != null) {
            new Thread(()->{
                try {
                    ftpUtils.uploadFiles(patientHolder.getPhone(), files);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }).start();
        }
    }

    @FXML
    void deleteAFile(ActionEvent event) {
        new Thread(()->{
            try {
                ftpUtils.deleteFiles(patientHolder.getPhone(), table_docs.getSelectionModel().getSelectedItems());
                table_docs.getItems().removeAll(table_docs.getSelectionModel().getSelectedItems());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }

    @FXML
    void descargar(ActionEvent event) {
        List<String> files = new ArrayList<>();
        files.addAll(table_docs.getSelectionModel().getSelectedItems());
        new Thread(()->{
            try {
                ftpUtils.downloadFiles(patientHolder.getPhone(),files);
                alert.setAlertType(Alert.AlertType.CONFIRMATION);
                alert.setTitle(MainApplication.resourceBundle.getString("config_success_tittle"));
                alert.setContentText(MainApplication.resourceBundle.getString("menu_I_downloadFile_success"));
                alert.showAndWait();
            }catch (IOException e){
                alert.setAlertType(Alert.AlertType.ERROR);
                alert.setTitle(MainApplication.resourceBundle.getString("config_errors_title"));
                alert.setContentText(MainApplication.resourceBundle.getString("menu_I_downloadFile_error"));
                alert.showAndWait();
            }
        }).start();
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        p_name.setCellValueFactory(new PropertyValueFactory<>("name"));
        p_lastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        p_phone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        p_phoneMum.setCellValueFactory(new PropertyValueFactory<>("phoneM"));
        p_phoneDad.setCellValueFactory(new PropertyValueFactory<>("phoneD"));
        p_motive.setCellValueFactory(new PropertyValueFactory<>("reason"));
        doc_name.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()));

        /*Permite escoger varios elementos de las dos tablas*/
        table_patients.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        table_docs.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        deleteFile.setDisable(true);
        downloadFile.setDisable(true);

        if (Objects.equals(MainApplication.properties.getProperty("configured"), "true")) {
            try {
                patientDAOMongoDB = new PatientDAOMongoDB(
                        MainApplication.properties.getProperty("mongoDB_connection"),
                        MainApplication.properties.getProperty("mongoDB_db"), MainApplication.properties.getProperty("mongoDB_collection"));
                FindIterable<Patient> x = patientDAOMongoDB.getAll();
                for (Patient p : x) {
                    table_patients.getItems().add(p);
                }
            } catch (Exception e) {
                alert.setAlertType(Alert.AlertType.ERROR);
                alert.setContentText(MainApplication.resourceBundle.getString("config_errors_mongo_auth"));
                alert.setTitle(MainApplication.resourceBundle.getString("config_errors_title"));
                alert.initModality(Modality.WINDOW_MODAL);
                alert.showAndWait();
                e.printStackTrace();
            }

            ftpUtils = new FTPUtils(MainApplication.properties.getProperty("ftp_host")
                    , MainApplication.properties.getProperty("ftp_port")
                    , MainApplication.properties.getProperty("ftp_user")
                    , MainApplication.properties.getProperty("ftp_password"));
        }
    }

    @Override
    public void actualizarTabla() {
        table_patients.getItems().clear();
        FindIterable<Patient> x = patientDAOMongoDB.getAll();
        for (Patient p : x) {
            table_patients.getItems().add(p);
        }
    }

    public static PatientDAOMongoDB getPatientDAOMongoDB() {
        return patientDAOMongoDB;
    }
    public static FTPUtils getFtpUtils() {return ftpUtils;}
}
