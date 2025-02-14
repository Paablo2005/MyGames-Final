package controllers;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
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

    private UserDaoImpl userDaoImpl;

    @FXML
    private void initialize() {
        userDaoImpl = new UserDaoImpl();
        btnLogin.setOnMouseClicked(event -> handleLoginClick(event));
    }

    @FXML
    private void handleLoginClick(Event event) {
        String email = textEmail.getText();
        String password = textPassword.getText();

        if (authenticateUser(email, password)) {
            openMainPane();
        } else {
            System.out.println("Invalid email or password.");
        }
    }

    /**
     * Método para autenticar al usuario comparando el hash de la contraseña ingresada
     * con la almacenada en la base de datos.
     */
    private boolean authenticateUser(String email, String password) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            User user = userDaoImpl.findByMail(session, email);

            // Comparar la contraseña encriptada ingresada con la almacenada
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
            stage.setTitle("Main Pane");

            MainPaneController mainPaneController = loader.getController();
            User authenticatedUser = getAuthenticatedUser();
            mainPaneController.setUserData(authenticatedUser);

            stage.show();

            Stage currentStage = (Stage) btnLogin.getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to load MainPane.fxml.");
        }
    }

    private User getAuthenticatedUser() {
        String email = textEmail.getText();
        @SuppressWarnings("unused")
		String password = textPassword.getText();
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return userDaoImpl.findByMail(session, email);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
