
module dutinfo {

    opens dutinfo.javafx.controllers;

    requires javafx.controls;

    requires org.fusesource.jansi;
    requires javafx.fxml;
    requires javafx.media;
    requires com.google.gson;

    exports dutinfo.javafx.controllers;
    exports dutinfo.javafx.views;
    
}