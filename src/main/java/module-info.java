module com.btl {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires javafx.media;

    requires java.sql;
    requires simple.google.translate.api;
    requires java.net.http;
    requires com.google.gson;
    requires java.mail;
    
    opens com.btl to javafx.fxml;
    exports com.btl;
}
