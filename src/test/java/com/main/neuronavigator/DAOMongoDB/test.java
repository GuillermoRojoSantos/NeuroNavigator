package com.main.neuronavigator.DAOMongoDB;

import com.main.neuronavigator.Libro;
import com.mongodb.*;
import com.mongodb.client.*;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import javafx.scene.control.Alert;
import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.Collections;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;

public class test {
    public static void main(String[] args) {
        String conect = "mongodb+srv://Admin_grs:guirojo28isthenewDeltha@neuronavigatormongodb.dacxkdt.mongodb.net/?retryWrites=true&w=majority";
        //Este nos da los codecs especificos para los POJOs
        CodecRegistry pojoCodecRegistry = fromProviders(PojoCodecProvider.builder().automatic(true).build());
        //Y este nos da los codecs genéricos que tiene el driver de MongoDB
        CodecRegistry codecRegistry = fromProviders(MongoClientSettings.getDefaultCodecRegistry(), pojoCodecRegistry);
        ConnectionString connectionString = new ConnectionString(conect);
        MongoClientSettings clientSettings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .codecRegistry(codecRegistry)
                .build();

        try (MongoClient mongoClient = MongoClients.create(clientSettings)) {
            System.out.println(mongoClient.listDatabaseNames().into(new ArrayList<>()).contains("test"));

            /*MongoDatabase database = mongoClient.getDatabase("test");
            MongoCollection<Libro> libros = database.getCollection("libro", Libro.class);
            FindIterable<Libro> result = libros.find();
            for (Libro libro : result){
                System.out.println(libro.toString());
            }*/


            /*Libro libro = new Libro("Beastars", "Paru Itagaki");
            InsertOneResult result = libros.insertOne(libro);
            System.out.println(result.getInsertedId().asObjectId().getValue());*/

            /*Document libro = new Document("_id",new ObjectId());
            libro.append("nombre","Guía del autoestopista galáctico")
                    .append("autor","Douglas Adams");
            libros.insertOne(libro);*/

            //update one book changing the author
            MongoDatabase database = mongoClient.getDatabase("test");
            MongoCollection<Libro> libros = database.getCollection("libro", Libro.class);
            Bson filter = new Document("nombre","Guía del autoestopista galáctico");
            Bson updateOperation = new Document("$set", new Document("autor",new ArrayList<String>(Collections.singleton("Douglas Adams"))));
            UpdateResult result = libros.updateOne(filter,updateOperation);
            System.out.println(result.getModifiedCount());

        }
    }
}

