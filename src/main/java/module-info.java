module dictionary.mydictionary {
    requires javafx.controls;
    requires javafx.fxml;
    requires jlayer;
    requires javafx.web;

    exports dictionary.mydictionary.controller;
    opens dictionary.mydictionary.controller to javafx.fxml;

    opens dictionary.mydictionary to javafx.fxml;
    exports dictionary.mydictionary;
}