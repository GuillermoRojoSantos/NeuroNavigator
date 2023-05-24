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

    public PatientDAOMongoDB(String conect, String db, String colection) {
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

    /*public String conect;
    public String db;
    public String colection;
    String conect = "mongodb+srv://Admin_grs:lXkfzBiZyQ8vhSn2@neuronavigatormongodb.dacxkdt.mongodb.net/?retryWrites=true&w=majority";
    //Este nos da los codecs especificos para los POJOs
    CodecRegistry pojoCodecRegistry = fromProviders(PojoCodecProvider.builder().automatic(true).build());
    //Y este nos da los codecs genéricos que tiene el driver de MongoDB
    CodecRegistry codecRegistry = fromProviders(MongoClientSettings.getDefaultCodecRegistry(),pojoCodecRegistry);
    ConnectionString connectionString = new ConnectionString(conect);
    MongoClientSettings clientSettings = MongoClientSettings.builder().applyConnectionString(connectionString)
            .codecRegistry(codecRegistry)
            .build();
*/
    private MongoCollection<Patient> getCollection(){
        MongoClient client = MongoClients.create(clientSettings);
        MongoDatabase mongodb = client.getDatabase(db);
        return mongodb.getCollection(colection,Patient.class);
    }

    @Override
    public FindIterable<Patient> getAll() {
        MongoCollection<Patient> collection = getCollection();
        return collection.find();
    }

    @Override
    public UpdateResult updatePatient(Patient patient) {
        MongoCollection<Patient> collection = getCollection();

        Bson filter = Filters.eq("_id", patient.get_id());
        Bson updateOperation = new Document("$set", patient);
        return collection.updateOne(filter, updateOperation);
    }

    @Override
    public DeleteResult deletePatient(Patient patient) {
        MongoCollection<Patient> collection = getCollection();
        return collection.deleteOne(eq("_id", patient.get_id()));
    }

    @Override
    public InsertOneResult addPatient(Patient patient) {
        MongoCollection<Patient> collection = getCollection();
        return collection.insertOne(patient);
    }

    @Override
    public FindIterable<Patient> search(List<String> filters, List<String> values) {
        return null;
    }

}
