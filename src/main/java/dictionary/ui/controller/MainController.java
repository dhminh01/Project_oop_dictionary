package dictionary.ui.controller;

import dictionary.AppMain;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.Objects;

public class MainController extends AppMain {
    @FXML
    protected BorderPane borderPane;

    @FXML
    protected Button homebtn;

    @FXML
    protected Button informationbtn;

    @FXML
    protected AnchorPane root;

    @FXML
    protected Button settingbtn;

    @FXML
    protected Button userbtn;

    @FXML
    protected VBox navBar;

    @FXML
    public void pressedHome(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("home-view.fxml"));
        AnchorPane view = loader.load();
        borderPane.setCenter(view);

        HomeViewController homeViewController = loader.getController();
        homeViewController.setHomeViewPane(view);
        homeViewController.initComponents(view);
        homeViewController.readData();
        homeViewController.loadWordList();
    }

    @FXML
    void pressedUser(MouseEvent event) throws IOException {
        AnchorPane userPage = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("user-view.fxml")));
        borderPane.setCenter(userPage);
    }

    @FXML
    void pressedInformation(MouseEvent event) throws IOException {
        AnchorPane informationPage = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("information-view.fxml")));
        borderPane.setCenter(informationPage);
    }

    @FXML
    void pressedSetting(MouseEvent event) throws IOException {
        AnchorPane settingPage = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("setting-view.fxml")));
        borderPane.setCenter(settingPage);
    }
}