package com.main.neuronavigator.controllers;

import com.main.neuronavigator.DAOMongoDB.PatientDAOMongoDB;
import com.main.neuronavigator.MainApplication;
import com.main.neuronavigator.models.Patient;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import net.synedra.validatorfx.Validator;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class InfoPatientController implements Initializable {

    private Patient patient=null;
    private Validator validator = new Validator();

    @FXML
    private TextField apellido;

    @FXML
    private DatePicker birth;

    @FXML
    private TextField direccion;

    @FXML
    private Text edad;

    @FXML
    private ListView<LocalDate> listaEvaluaciones;

    @FXML
    private TextField nombre;

    @FXML
    private TextField nombreMadre;

    @FXML
    private TextField nombrePasdre;

    @FXML
    private TextField numeroMadre;

    @FXML
    private TextField numeroPadre;

    @FXML
    private TextArea observaciones;

    @FXML
    private TextField ocupacion;

    @FXML
    private TextArea reason;

    @FXML
    private TextField telefono;

    @FXML
    private Button update;



    @FXML
    void updatePatient(ActionEvent event) {
        if(validator.validate()){
            Patient newPatient = new Patient();
            newPatient.setName(nombre.getText());
            newPatient.setLastName(apellido.getText());
            newPatient.setDadName(nombrePasdre.getText());
            newPatient.setMomName(nombreMadre.getText());
            newPatient.setPhoneD(numeroPadre.getText());
            newPatient.setPhoneM(numeroMadre.getText());
            newPatient.setAddress(direccion.getText());
            newPatient.setPhone(telefono.getText());
            newPatient.setAge(Integer.parseInt(edad.getText()));
            newPatient.setBirth(birth.getValue());
            newPatient.setOccupation(ocupacion.getText());
            newPatient.setObservations(observaciones.getText());
            newPatient.setReason(reason.getText());
            long result = MainController.getPatientDAOMongoDB().updatePatient(patient,newPatient).getModifiedCount();
            if(result==1){
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle(MainApplication.resourceBundle.getString("mainTable_operations_tittle"));
                alert.setContentText(MainApplication.resourceBundle.getString("mainTable_operations_updateSuccess"));
                alert.showAndWait();
            }
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.patient=MainController.patientHolder;
        //fill the fields with the patient data
        nombrePasdre.setText(patient.getDadName());
        nombreMadre.setText(patient.getMomName());
        numeroPadre.setText(patient.getPhoneD());
        numeroMadre.setText(patient.getPhoneM());
        direccion.setText(patient.getAddress());
        telefono.setText(patient.getPhone());
        edad.setText(patient.getAge().toString());
        birth.setValue(patient.getBirth());
        ocupacion.setText(patient.getOccupation());
        observaciones.setText(patient.getObservations());
        reason.setText(patient.getReason());
        nombre.setText(patient.getName());
        apellido.setText(patient.getLastName());
        //fill the list with the dates of the evaluations
        listaEvaluaciones.getItems().addAll(patient.getEvaluations());

        addValidators();
    }

    private void addValidators(){
        validator.createCheck().dependsOn("numero",telefono.textProperty()).withMethod(c->{
            String telefono = c.get("numero");
            if(telefono.isEmpty()){
                c.error(MainApplication.resourceBundle.getString("config_errors_empty"));
            }
        }).decorates(telefono).immediate();
        validator.createCheck().dependsOn("nombre",nombre.textProperty()).withMethod(c->{
            String telefono = c.get("nombre");
            if(telefono.isEmpty()){
                c.error(MainApplication.resourceBundle.getString("config_errors_empty"));
            }
        }).decorates(telefono).immediate();
        validator.createCheck().dependsOn("apellido",apellido.textProperty()).withMethod(c->{
            String telefono = c.get("apellido");
            if(telefono.isEmpty()){
                c.error(MainApplication.resourceBundle.getString("config_errors_empty"));
            }
        }).decorates(telefono).immediate();
    }
}
