package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

import java.io.IOException;

public class LoginController {
    @FXML
    private GridPane paneBtnLogin;

    @FXML
    private GridPane paneBtnRegister;

    @FXML
    private BorderPane paneData;

    @FXML
    public void initialize() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/LoginDataPane.fxml"));
            Node loginPanel = loader.load();
            paneData.setCenter(loginPanel);
        } catch (IOException e) {
            e.printStackTrace();
        }
        paneBtnLogin.setOnMouseClicked(event -> showLoginContent());
        paneBtnRegister.setOnMouseClicked(event -> showRegisterContent());
    }

    private void showLoginContent() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/LoginDataPane.fxml"));
            Node loginPanel = loader.load();
            paneData.setCenter(loginPanel);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showRegisterContent() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/RegisterDataPane.fxml"));
            Node loginPanel = loader.load();
            paneData.setCenter(loginPanel);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
