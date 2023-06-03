package com.main.neuronavigator.controllers;

import com.main.neuronavigator.MainApplication;
import com.main.neuronavigator.PacientesListener;
import com.main.neuronavigator.models.Patient;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.swing.JRViewer;
import net.synedra.validatorfx.Validator;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Objects;
import java.util.ResourceBundle;

public class InfoPatientController implements Initializable {

    private PacientesListener pacientesListener;
    private Patient patient=null;
    private Validator validator = new Validator();
    private LocalDate today = LocalDate.now();
    Patient newPatient = new Patient();

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
    private Button print;

    @FXML
    private TextArea reason;

    @FXML
    private TextField telefono;

    @FXML
    private Button update;

    @FXML
    private Button btnEvaluation;

    @FXML
    void addEvaluation(ActionEvent event) {
        if(!(patient.getEvaluations().contains(today))){
            patient.getEvaluations().add(today);
            listaEvaluaciones.getItems().add(today);
        }
    }

    @FXML
    void printPatient(ActionEvent event) throws JRException {
        HashMap hm = new HashMap();

        hm.put("Nombre", patient.getName());
        hm.put("Apellido", patient.getLastName());
        hm.put("Edad", patient.getAge());
        hm.put("Direccion", patient.getAddress());
        hm.put("Telefono", patient.getPhone());
        hm.put("Nacimiento", patient.getBirth());
        hm.put("Ocupacion", patient.getOccupation());
        hm.put("observaciones", patient.getObservations());
        hm.put("Razon", patient.getReason());
        hm.put("nombrePa", patient.getDadName());
        hm.put("nombreMom", patient.getMomName());
        hm.put("numDad", patient.getPhoneD());
        hm.put("numMom", patient.getPhoneM());

        String report = MainApplication.class.getResource("PatientResume.jasper").getFile();

        JasperPrint jasperPrint = JasperFillManager.fillReport(
                report,
                hm,new JREmptyDataSource());

        JRViewer viewer = new JRViewer(jasperPrint);

        JFrame frame = new JFrame("Menu");
        frame.getContentPane().add(viewer);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.pack();
        frame.setVisible(true);
    }

    @FXML
    void updatePatient(ActionEvent event) {
        if(validator.validate()){
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
            newPatient.getEvaluations().addAll(patient.getEvaluations());
            newPatient.setReason(reason.getText());
            long result = MainController.getPatientDAOMongoDB().updatePatient(patient,newPatient).getModifiedCount();
            pacientesListener.actualizarTabla();
            if(!Objects.equals(newPatient.getPhone(), patient.getPhone())){
               new Thread(()->{
                   try {
                       MainController.getFtpUtils().renameFile(patient.getPhone(),newPatient.getPhone());
                   } catch (IOException e) {
                       throw new RuntimeException(e);
                   }
               }).start();
            }
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

    public void setPacientesListener(PacientesListener pacientesListener) {
        this.pacientesListener = pacientesListener;
    }
}
