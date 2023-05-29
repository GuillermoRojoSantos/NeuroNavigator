package com.main.neuronavigator.controllers;

import com.main.neuronavigator.DAOMongoDB.PatientDAOMongoDB;
import com.main.neuronavigator.FTPManager.FTPUtils;
import com.main.neuronavigator.Main;
import com.main.neuronavigator.MainApplication;
import com.main.neuronavigator.PacientesListener;
import com.main.neuronavigator.models.Patient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.result.DeleteResult;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
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
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class MainController implements Initializable, PacientesListener {

    private PatientDAOMongoDB patientDAOMongoDB;
    private FTPUtils ftpUtils;
    public static List<Patient> patientsHolder=new ArrayList<>();
    private Alert alert = new Alert(Alert.AlertType.NONE);

    @FXML
    private MenuItem addFile;

    @FXML
    private MenuItem deleteFile;

    @FXML
    private TableColumn<String, String> doc_name;

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
    private TableColumn<Patient, String> p_lastName;

    @FXML
    private TableView<String> table_docs;

    @FXML
    private TableView<Patient> table_patients;

    @FXML
    private MenuItem itemDelete;

    @FXML
    private MenuItem itemUpdate;


    @FXML
    void getPatient(MouseEvent event) {
        patientsHolder.removeAll(patientsHolder);
        if (table_patients.getSelectionModel().getSelectedItems().size() == 1) {
            itemUpdate.setDisable(false);
            switch (event.getClickCount()) {
                case 1 -> {
                    patientsHolder.add(0,table_patients.getSelectionModel().getSelectedItems().get(0));

                    /*Usar una instancia del controlador SFTP para traerte los documentos necesarios a través del ID del paciente*/
                    try {
                        ObservableList<String> documents = table_docs.getItems();
                        documents.clear();
                        documents.addAll(ftpUtils.listFiles(patientsHolder.get(0)));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
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
            if(patientDAOMongoDB.deletePatient(patientsHolder.get(0)).getDeletedCount()==1){
                alert.setAlertType(Alert.AlertType.CONFIRMATION);
                alert.setContentText(MainApplication.resourceBundle.getString("mainTable_operations_delete_success"));
                alert.setTitle(MainApplication.resourceBundle.getString("mainTable_operations_tittle"));
                alert.show();
            }
        }else {
            if(patientDAOMongoDB.deleteManyPatients(patientsHolder).getDeletedCount()>=1){
                alert.setAlertType(Alert.AlertType.CONFIRMATION);
                alert.setContentText(MainApplication.resourceBundle.getString("mainTable_operations_delete_success"));
                alert.setTitle(MainApplication.resourceBundle.getString("mainTable_operations_tittle"));
                alert.show();
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
            try {
                ftpUtils.uploadFiles(patientsHolder.get(0), files);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @FXML
    void deleteAFile(ActionEvent event) {
        try {
            ftpUtils.deleteFiles(patientsHolder.get(0), table_docs.getSelectionModel().getSelectedItems());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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

        if (Objects.equals(MainApplication.properties.getProperty("configured"), "true")) {
            patientDAOMongoDB = new PatientDAOMongoDB(
                    "mongodb+srv://Admin_grs:guirojo28isthenewDeltha@neuronavigatormongodb.dacxkdt.mongodb.net/?retryWrites=true&w=majority",
                    "NeuroNavigator", "Patients");
            FindIterable<Patient> x = patientDAOMongoDB.getAll();
            for (Patient p : x) {
                table_patients.getItems().add(p);
            }
            ftpUtils = new FTPUtils(MainApplication.properties.getProperty("ftp_host")
                    , MainApplication.properties.getProperty("ftp_port")
                    , MainApplication.properties.getProperty("ftp_fingerprint")
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
}
