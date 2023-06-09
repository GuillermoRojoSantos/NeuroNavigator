package com.main.neuronavigator.DAOMongoDB;

import com.main.neuronavigator.models.Patient;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.InsertOneResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.conversions.Bson;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;


public class PatientDAOMongoDB implements DAOMongoDBInterface {

    private String conect;
    private String db;
    private String colection;
    private CodecRegistry pojoCodecRegistry;
    private CodecRegistry codecRegistry;
    private ConnectionString connectionString;
    private MongoClientSettings clientSettings;

    public PatientDAOMongoDB(String conect, String db, String colection) throws NoSuchAlgorithmException {
        this.conect = conect;
        this.db = db;
        this.colection = colection;
        this.pojoCodecRegistry = fromProviders(PojoCodecProvider.builder().automatic(true).build());
        this.codecRegistry = fromProviders(MongoClientSettings.getDefaultCodecRegistry(), pojoCodecRegistry);
        this.connectionString = new ConnectionString(conect);
        this.clientSettings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .codecRegistry(codecRegistry)
                .build();
    }

    private MongoCollection<Patient> getCollection() {
        MongoClient client = MongoClients.create(clientSettings);
        MongoDatabase mongodb = client.getDatabase(db);
        return mongodb.getCollection(colection, Patient.class);
    }

    @Override
    public FindIterable<Patient> getAll() {
        MongoCollection<Patient> collection = getCollection();
        FindIterable<Patient> result = collection.find();
        return result;
    }

    @Override
    public UpdateResult updatePatient(Patient oldpatient, Patient newPatient) {
        MongoCollection<Patient> collection = getCollection();
        Bson filter = eq("phone", oldpatient.getPhone());
        Bson updateOperationDocument = new Document("$set", newPatient);
        return collection.updateOne(filter, updateOperationDocument);
    }

    @Override
    public DeleteResult deletePatient(Patient patient) {
        MongoCollection<Patient> collection = getCollection();
        return collection.deleteOne(eq("phone", patient.getPhone()));
    }

    @Override
    public InsertOneResult addPatient(Patient patient) {
        MongoCollection<Patient> collection = getCollection();
        return collection.insertOne(patient);
    }

    @Override
    public DeleteResult deleteManyPatients(List<Patient> patients) {
        List<String> list = new ArrayList<>();
        for (Patient p : patients) {
            list.add(p.getPhone());
        }
        MongoCollection<Patient> collection = getCollection();
        Bson filter = Filters.in("phone", list);
        return collection.deleteMany(filter);
    }
}
