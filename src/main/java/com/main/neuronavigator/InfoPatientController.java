package com.main.neuronavigator;

import com.main.neuronavigator.models.Patient;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.ListView;
import javafx.scene.text.Text;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class InfoPatientController implements Initializable {

    private Patient patient=null;

    @FXML
    private Text birth;

    @FXML
    private Text birth1;

    @FXML
    private Text direccion;

    @FXML
    private Text edad;

    @FXML
    private Text nombreMadre;

    @FXML
    private Text nombrePasdre;

    @FXML
    private Text numeroMadre;

    @FXML
    private Text numeroPadre;

    @FXML
    private Text ocupacion;

    @FXML
    private Text telefono;

    @FXML
    private Text observaciones;

    @FXML
    private Text reason;

    @FXML
    private ListView<LocalDate> listaEvaluaciones;

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
        birth.setText(patient.getBirth().toString());
        ocupacion.setText(patient.getOccupation());
        observaciones.setText(patient.getObservations());
        reason.setText(patient.getReason());
        //fill the list with the dates of the evaluations
        listaEvaluaciones.getItems().addAll(patient.getEvaluations());
    }
}
