
module dutinfo {

    opens dutinfo.javafx.controllers;

    requires javafx.controls;
    requires json.simple;
    requires org.fusesource.jansi;
    requires javafx.fxml;

    exports dutinfo.javafx.controllers;
    exports dutinfo.javafx.views;
}