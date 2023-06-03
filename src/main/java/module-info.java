module com.main.neuronavigator {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires lombok;
    requires org.mongodb.driver.sync.client;
    requires org.mongodb.bson;
    requires org.mongodb.driver.core;
    requires com.hierynomus.sshj;
    requires javafx.swing;
    requires jasperreports;
    requires java.sql;

    opens com.main.neuronavigator to javafx.fxml;
    opens com.main.neuronavigator.models;
    exports com.main.neuronavigator;
    exports com.main.neuronavigator.controllers;
    opens com.main.neuronavigator.controllers to javafx.fxml;
}