package com.main.neuronavigator;

import com.main.neuronavigator.DAOMongoDB.PatientDAOMongoDB;
import com.main.neuronavigator.models.DocFTP;
import com.main.neuronavigator.models.Patient;
import com.mongodb.client.FindIterable;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML
    private TableColumn<DocFTP, String> doc_name;

    @FXML
    private TableColumn<DocFTP, String> doc_update;

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
    private TableView<DocFTP> table_docs;

    @FXML
    private TableView<Patient> table_pacients;

    @FXML
    void getPatient(MouseEvent event){
        //get the patient selected
        Patient patient = table_pacients.getSelectionModel().getSelectedItem();
        System.out.println(patient.toString());
    }

    @FXML
    void addPatient(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("add-view.fxml"), MainApplication.resourceBundle);
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            stage.initOwner(((MenuItem) event.getSource()).getParentPopup().getOwnerWindow());
            stage.setScene(new Scene(root));
            stage.initModality(Modality.WINDOW_MODAL);
            stage.setMinWidth(1073);
            stage.setMinHeight(398);
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void openConfig(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("config-view.fxml"), MainApplication.resourceBundle);
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        PatientDAOMongoDB patientDAOMongoDB = new PatientDAOMongoDB(
                "mongodb+srv://Admin_grs:guirojo28isthenewDeltha@neuronavigatormongodb.dacxkdt.mongodb.net/?retryWrites=true&w=majority",
                "NeuroNavigator", "Patients");
        p_name.setCellValueFactory(new PropertyValueFactory<>("name"));
        p_lastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        p_phone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        p_phoneMum.setCellValueFactory(new PropertyValueFactory<>("phoneM"));
        p_phoneDad.setCellValueFactory(new PropertyValueFactory<>("phoneD"));
        p_motive.setCellValueFactory(new PropertyValueFactory<>("reason"));
        doc_name.setCellValueFactory(new PropertyValueFactory<>("name"));
        doc_update.setCellValueFactory(new PropertyValueFactory<>("update"));
        FindIterable<Patient> x = patientDAOMongoDB.getAll();
        for (Patient p : x) {
            table_pacients.getItems().add(p);
        }
    }
}
