package com.main.neuronavigator.DAOMongoDB;

import com.main.neuronavigator.models.Patient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.InsertOneResult;
import com.mongodb.client.result.UpdateResult;

import java.util.List;

public interface DAOMongoDBInterface {
    public FindIterable<Patient> getAll();
    public UpdateResult updatePatient(Patient patient);
    public DeleteResult deletePatient(Patient patient);
    public InsertOneResult addPatient(Patient patient);
    public FindIterable<Patient> search(List<String> filters, List<String> values);
}
