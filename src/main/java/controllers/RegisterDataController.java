package controllers;

import javafx.fxml.FXML;
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
    private TextField textEmail;

    @FXML
    private PasswordField textPassword1;

    @FXML
    private PasswordField textPassword2;

    @FXML
    private TextField textUsername;

    @FXML
    private Label btnLogin;

    private UserDaoImpl userDaoImpl;

    @FXML
    private void initialize() {
        userDaoImpl = new UserDaoImpl();
        btnLogin.setOnMouseClicked(event -> createUser());
    }

    private void createUser() {
        String email = textEmail.getText();
        String password1 = textPassword1.getText();
        String password2 = textPassword2.getText();
        String username = textUsername.getText();

        if (!password1.equals(password2)) {
            System.out.println("Passwords do not match.");
            return;
        }

        // Encriptar la contraseña antes de guardar
        String hashedPassword = PasswordUtil.hashPassword(password1);

        User user = new User();
        user.setMail(email);
        user.setPassword(hashedPassword);
        user.setUserName(username);
        
        if (email == null || email.isEmpty() || !email.contains("@")) {
          System.out.println("Invalid email address.");
          return;
        }
        if (username == null || username.isEmpty()) {
          System.out.println("Username cannot be empty.");
          return;
        }
        if (password1 == null || password1.isEmpty() || password1.length() < 6) {
          System.out.println("Password must be at least 6 characters long.");
          return;
        }

        // Asignar la foto de perfil por defecto
        user.setProfileImage("/Images/ProfileImage.png");

        saveUser(user);
    }

    private void saveUser(User user) {
      Session session = null;
      Transaction transaction = null;

      try {
          session = HibernateUtil.getSessionFactory().openSession();

          // Iniciar la transacción
          transaction = session.beginTransaction();

          // Verificar si el usuario ya existe
          User existingUser = userDaoImpl.findByMail(session, user.getMail());
          if (existingUser != null) {
              System.out.println("A user with this email already exists.");
              return;
          }

          // Guardar el usuario
          session.merge(user);
          transaction.commit(); // Confirmar la transacción
          System.out.println("User created successfully!");
      } catch (Exception e) {
          if (transaction != null && transaction.isActive()) {
              transaction.rollback(); // Revertir la transacción en caso de error
          }
          e.printStackTrace();
          System.out.println("Failed to create user.");
      } finally {
          if (session != null) {
              session.close(); // Cerrar la sesión
          }
      }
  }

}