package com.main.neuronavigator;

import com.main.neuronavigator.DAOMongoDB.PatientDAOMongoDB;
import com.main.neuronavigator.models.Patient;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import net.synedra.validatorfx.Validator;

import java.net.URL;
import java.time.LocalDate;
import java.util.Locale;
import java.util.ResourceBundle;

public class AddController implements Initializable {

    private Validator validator = new Validator();
    private LocalDate today = LocalDate.now();
    private static Alert alert = new Alert(Alert.AlertType.NONE);
    private PatientDAOMongoDB patientDAOMongoDB;

    @FXML
    private CheckBox boolActualDate;

    @FXML
    private Button btnReset;

    @FXML
    private Button btnSave;

    @FXML
    private DatePicker dateBirth;

    @FXML
    private TitledPane secondaryData;

    @FXML
    private TextField txtAddress;

    @FXML
    private TextField txtDerivedBy;

    @FXML
    private TextField txtLastName;

    @FXML
    private TextField txtNFather;

    @FXML
    private TextField txtNMother;

    @FXML
    private TextField txtName;

    @FXML
    private TextArea txtObservations;

    @FXML
    private TextField txtOccupation;

    @FXML
    private TextField txtPhone;

    @FXML
    private TextField txtPhoneF;

    @FXML
    private TextField txtPhoneM;

    @FXML
    private TextArea txtReason;

    @FXML
    void reset(MouseEvent event) {

    }

    @FXML
    void savePatient(MouseEvent event) {
        if (validator.validate()) {
            //create a patient object with the data
            Patient patient = new Patient();
            patient.setName(txtName.getText());
            patient.setLastName(txtLastName.getText());
            patient.setBirth(dateBirth.getValue());
            patient.setAge(today.getYear() - dateBirth.getValue().getYear());
            patient.setAddress(txtAddress.getText());
            patient.setPhone(txtPhone.getText());
            patient.setPhoneM(txtPhoneM.getText());
            patient.setPhoneD(txtPhoneF.getText());
            patient.setOccupation(txtOccupation.getText());
            patient.setMomName(txtNMother.getText());
            patient.setDadName(txtNFather.getText());
            patient.setSender(txtDerivedBy.getText());
            patient.setReason(txtReason.getText());
            patient.setObservations(txtObservations.getText());
            if (boolActualDate.isSelected()) {
                patient.addEvaluation(today);
            }
            if (patientDAOMongoDB.addPatient(patient).getInsertedId() != null) {
                alert.setAlertType(Alert.AlertType.CONFIRMATION);
                alert.setTitle(MainApplication.resourceBundle.getString("add_succesfullTitle"));
                alert.setContentText(MainApplication.resourceBundle.getString("add_succesfullContent"));
                alert.show();
            }
        } else {
            alert.setAlertType(Alert.AlertType.WARNING);
            alert.setTitle(MainApplication.resourceBundle.getString("config_warning"));
            alert.setContentText(MainApplication.resourceBundle.getString("add_errors_checkFields"));
            alert.show();
        }
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        patientDAOMongoDB = new PatientDAOMongoDB(
                "mongodb+srv://Admin_grs:guirojo28isthenewDeltha@neuronavigatormongodb.dacxkdt.mongodb.net/?retryWrites=true&w=majority",
                "NeuroNavigator", "Patients");
        //Hay que ponerle el locale a la aplicación para que el alertType del alert se ponga en el idioma correcto
        Locale.setDefault(MainApplication.locale);
        secondaryData.expandedProperty().addListener((expandedProperty, wasExpanded, isNowExpanded) -> {
            if (isNowExpanded) {
                secondaryData.getScene().getWindow().setHeight(secondaryData.getScene().getWindow().getHeight() + 270);
            } else {
                secondaryData.getScene().getWindow().setHeight(secondaryData.getScene().getWindow().getHeight() - 270);
            }
        });
        addValidators();
    }

    private void addValidators() {

        /*Name validator*/
        validator.createCheck().dependsOn("name", txtName.textProperty()).withMethod(c -> {
            String userName = c.get("name");
            if (userName.length() < 2) {
                c.error(MainApplication.resourceBundle.getString("add_errors_name1"));
            }
        }).decorates(txtName).immediate();

        /*Last name validator*/
        validator.createCheck().dependsOn("lastName", txtLastName.textProperty()).withMethod(c -> {
            String userLastName = c.get("lastName");
            if (userLastName.length() < 2) {
                c.error(MainApplication.resourceBundle.getString("add_errors_lastName1"));
            }
        }).decorates(txtLastName).immediate();

        /*Address validator*/
        validator.createCheck().dependsOn("address", txtAddress.textProperty()).withMethod(c -> {
            String userAddress = c.get("address");
            if (userAddress.length() < 2) {
                c.error(MainApplication.resourceBundle.getString("add_errors_emptyField"));
            }
        }).decorates(txtAddress).immediate();

        /*Phone validator*/
        validator.createCheck().dependsOn("phone", txtPhone.textProperty()).withMethod(c -> {
            String userPhone = c.get("phone");
            if (userPhone.length() <= 8) {
                c.error(MainApplication.resourceBundle.getString("add_errors_phone1"));
            }
        }).decorates(txtPhone).immediate();

        /*Ocupation validator*/
        validator.createCheck().dependsOn("occupation", txtOccupation.textProperty()).withMethod(c -> {
            String userOccupation = c.get("occupation");
            if (userOccupation.length() < 2) {
                c.error(MainApplication.resourceBundle.getString("add_errors_emptyField"));
            }
        }).decorates(txtOccupation).immediate();

        /*Derived by validator*/
        validator.createCheck().dependsOn("derivedBy", txtDerivedBy.textProperty()).withMethod(c -> {
            String userDerivedBy = c.get("derivedBy");
            if (userDerivedBy.length() < 2) {
                c.error(MainApplication.resourceBundle.getString("add_errors_emptyField"));
            }
        }).decorates(txtDerivedBy).immediate();

        /*Reason of consultation validator*/
        validator.createCheck().dependsOn("reason", txtReason.textProperty()).withMethod(c -> {
            String userReason = c.get("reason");
            if (userReason.length() < 2) {
                c.error(MainApplication.resourceBundle.getString("add_errors_emptyField"));
            }
        }).decorates(txtReason).immediate();

        /*Date validator*/
        validator.createCheck().dependsOn("date", dateBirth.valueProperty()).withMethod(c -> {
            LocalDate userDate = c.get("date");
            if (userDate.isAfter(today)) {
                c.error(MainApplication.resourceBundle.getString("add_errors_date1"));
            }
        }).decorates(dateBirth).immediate();
    }

}
