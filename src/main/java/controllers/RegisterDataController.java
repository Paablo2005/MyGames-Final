package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import models.User;
import dao.UserDaoImpl;

import org.hibernate.Session;
import org.hibernate.Transaction;
import utils.HibernateUtil;
import utils.PasswordUtil;

public class RegisterDataController {

    @FXML
	public TextField textEmail;

    @FXML
    public PasswordField textPassword1;

    @FXML
    public PasswordField textPassword2;

    @FXML
    public TextField textUsername;

    @FXML
    public Label btnLogin;

    @FXML
    private void initialize() {
        btnLogin.setOnMouseClicked(event -> createUser());
    }

    private void createUser() {
        String email = textEmail.getText();
        String password1 = textPassword1.getText();
        String password2 = textPassword2.getText();
        String username = textUsername.getText();

        if (!password1.equals(password2)) {
            showAlert("Passwords do not match.", Alert.AlertType.ERROR);
            return;
        }

        // Encrypt the password before saving
        String hashedPassword = PasswordUtil.hashPassword(password1);

        User user = new User();
        user.setEmail(email);
        user.setPassword(hashedPassword);
        user.setUsername(username);
        
        if (email == null || email.isEmpty() || !email.contains("@")) {
            showAlert("Invalid email address.", Alert.AlertType.ERROR);
            return;
        }
        if (username == null || username.isEmpty()) {
            showAlert("Username cannot be empty.", Alert.AlertType.ERROR);
            return;
        }
        if (password1 == null || password1.isEmpty() || password1.length() < 6) {
            showAlert("Password must be at least 6 characters long.", Alert.AlertType.ERROR);
            return;
        }
        
        // Set the default profile picture
        user.setPicture("/Images/ProfileImage.png");

        saveUser(user);
    }

    private void saveUser(User user) {
        Session session = null;
        Transaction transaction = null;

        try {
            session = HibernateUtil.getSessionFactory().openSession();
            // Start the transaction
            transaction = session.beginTransaction();

            // Check if the user already exists
            UserDaoImpl userDaoImpl = new UserDaoImpl(session);
            User existingUser = userDaoImpl.getByEmail(user.getEmail());
            if (existingUser != null) {
                showAlert("A user with this email already exists.", Alert.AlertType.ERROR);
                return;
            }

            // Save the user
            session.merge(user);
            transaction.commit(); // Commit the transaction
            showAlert("User created successfully!", Alert.AlertType.INFORMATION);
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback(); // Rollback in case of error
            }
            e.printStackTrace();
            showAlert("Failed to create user.", Alert.AlertType.ERROR);
        } finally {
            if (session != null) {
                session.close(); // Close the session
            }
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
