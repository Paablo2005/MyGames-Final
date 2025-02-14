package controllers; 

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import models.User;
import dao.UserDaoImpl;

import org.hibernate.Session;
import utils.HibernateUtil;
import utils.PasswordUtil;
import java.io.IOException;

public class LoginDataController {

    @FXML
    private TextField textEmail;

    @FXML
    private PasswordField textPassword;

    @FXML
    private Label btnLogin;

    @FXML
    private void initialize() {
        btnLogin.setOnMouseClicked(event -> handleLoginClick(event));
    }

    @FXML
    private void handleLoginClick(Event event) {
        String email = textEmail.getText();
        String password = textPassword.getText();

        if (authenticateUser(email, password)) {
            openMainPane();
        } else {
            showAlert("Invalid email or password.", Alert.AlertType.ERROR);
        }
    }

    /**
     * Authenticate the user by comparing the hashed password entered with the one stored in the database.
     */
    private boolean authenticateUser(String email, String password) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            UserDaoImpl userDaoImpl = new UserDaoImpl(session);
            User user = userDaoImpl.getByEmail(email);

            // Compare the hashed password entered with the stored password
            if (user != null && user.getPassword().equals(PasswordUtil.hashPassword(password))) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private void openMainPane() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/MainPane.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            stage.setTitle("MyGames");
            
            MainPaneController mainPaneController = loader.getController();
            User authenticatedUser = getAuthenticatedUser();
            mainPaneController.setUserData(authenticatedUser);
            stage.setResizable(false);
            stage.show();

            Stage currentStage = (Stage) btnLogin.getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Failed to load MainPane.fxml.", Alert.AlertType.ERROR);
        }
    }

    private User getAuthenticatedUser() {
        String email = textEmail.getText();
        @SuppressWarnings("unused")
        String password = textPassword.getText();
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            UserDaoImpl userDaoImpl = new UserDaoImpl(session);
            return userDaoImpl.getByEmail(email);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void showAlert(String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle("Message");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
