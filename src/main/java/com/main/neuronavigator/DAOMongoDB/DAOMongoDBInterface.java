package com.main.neuronavigator.DAOMongoDB;

import com.main.neuronavigator.models.Patient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.InsertOneResult;
import com.mongodb.client.result.UpdateResult;

import java.util.List;

public interface DAOMongoDBInterface {
    public FindIterable<Patient> getAll();
    public UpdateResult updatePatient(Patient oldpatient, Patient newPatient);
    public DeleteResult deletePatient(Patient patient);
    public InsertOneResult addPatient(Patient patient);
    public DeleteResult deleteManyPatients(List<Patient> patients);
}
