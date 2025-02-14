package controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import models.User;
import utils.PasswordUtil;
import org.hibernate.Session;
import dao.UserDaoImpl;
import utils.HibernateUtil;
import java.io.File;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.scene.control.Button;

public class UserDataPaneController implements Initializable{

    @FXML
    private TextField txtUsername;

    @FXML
    private TextField txtMail;

    @FXML
    private PasswordField password;
    
    @FXML
    private Pane paneImgProfile;

    @FXML
    private Button btnUploadImage;
    
    @FXML
    private Label btnEdit;
    
    @FXML
    private Label btnSave;
    
    @FXML
    private Label btnCancel;
    
    @FXML
    private Label btnDelete;

    // Variable para guardar la URL de la imagen seleccionada
    private String selectedImageUrl;
    
    // Variables para almacenar los valores originales
    private String originalUsername;
    private String originalEmail;
    private String originalPictureUrl;
    
    // Referencia al controlador principal
    private MainPaneController mainController;
    
    // Referencia al usuario actual (se asigna en setUserData)
    private User currentUser;
    
    // Bandera para agregar los listeners solo una vez
    private boolean listenersAdded = false;

    // Setter para asignar el controlador principal
    public void setMainController(MainPaneController mainController) {
        this.mainController = mainController;
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        paneImgProfile.layoutBoundsProperty().addListener((obs, oldBounds, newBounds) -> {
            double radius = Math.min(newBounds.getWidth(), newBounds.getHeight()) / 2;
            Circle clip = new Circle(newBounds.getWidth() / 2, newBounds.getHeight() / 2, radius);
            paneImgProfile.setClip(clip);
        });
    }
    
    /**
     * Método que se invoca al pulsar el botón de editar.
     * Habilita la edición de los campos y muestra los controles correspondientes.
     * En este caso, btnSave se inicia oculto y se mostrará solo si se detecta una modificación.
     */
    @FXML
    private void handleBtnEditClick(MouseEvent event) {
        txtUsername.setDisable(false);
        txtMail.setDisable(false);
        password.setDisable(false);
        btnUploadImage.setVisible(true);
        btnEdit.setVisible(false);
        btnSave.setVisible(false); // Se inicia oculto
        btnCancel.setVisible(true);
    }
    
    /**
     * Cancela la edición y recarga la información original.
     */
    @FXML
    private void handleBtnCancelClick(MouseEvent event) {
        if (mainController != null) {
            mainController.loadUserDataPane();
        }
    }
    
    /**
     * Método que se invoca al pulsar el botón de guardar.
     * Actualiza la información del usuario en la base de datos.
     */
    @FXML
    private void handleBtnSaveClick(MouseEvent event) {
        // Obtener datos de los campos
        String newUsername = txtUsername.getText().trim();
        String newEmail = txtMail.getText().trim();
        String newPassword = password.getText().trim();

        // Validaciones básicas
        if (newUsername.isEmpty() || newEmail.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error de validación");
            alert.setHeaderText(null);
            alert.setContentText("El nombre de usuario y el correo no pueden estar vacíos.");
            alert.showAndWait();
            return;
        }
        
        // Validar formato del email (debe contener una '@')
        if (!newEmail.contains("@")) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error de validación");
            alert.setHeaderText(null);
            alert.setContentText("El correo electrónico debe contener una '@'.");
            alert.showAndWait();
            return;
        }
        
        // Validar la longitud de la nueva contraseña (si se ingresa)
        if (!newPassword.isEmpty() && newPassword.length() < 6) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error de validación");
            alert.setHeaderText(null);
            alert.setContentText("La contraseña debe tener al menos 6 caracteres.");
            alert.showAndWait();
            return;
        }
        
        // Actualizar el objeto currentUser con los nuevos datos
        currentUser.setUsername(newUsername);
        currentUser.setEmail(newEmail);
        
        // Si se ingresó una nueva contraseña, la hasheamos y actualizamos
        if (!newPassword.isEmpty()) {
            currentUser.setPassword(PasswordUtil.hashPassword(newPassword));
        }
        
        // Si se subió una nueva imagen, se actualiza el campo 'picture'
        if (selectedImageUrl != null) {
            currentUser.setPicture(selectedImageUrl);
        }

        // Actualizar el usuario en la base de datos
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            UserDaoImpl userDao = new UserDaoImpl(session);
            userDao.update(currentUser);

            // Recargar la información del usuario en el MainPane
            if(mainController != null) {
                mainController.setUserData(currentUser);
            }

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Actualización exitosa");
            alert.setHeaderText(null);
            alert.setContentText("La información del usuario se ha actualizado correctamente.");
            alert.showAndWait();

            // Restaurar el estado de la interfaz: deshabilitar los campos y ajustar botones
            txtUsername.setDisable(true);
            txtMail.setDisable(true);
            password.setDisable(true);
            btnUploadImage.setVisible(false);
            btnEdit.setVisible(true);
            btnSave.setVisible(false);
            btnCancel.setVisible(false);

        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Fallo al actualizar");
            alert.setContentText("Ocurrió un error al actualizar la información: " + e.getMessage());
            alert.showAndWait();
        } finally {
            session.close();
        }
    }
    
    /**
     * Maneja la subida de imagen. Abre un FileChooser para seleccionar la imagen,
     * actualiza el fondo del pane y llama a checkForModifications para detectar el cambio.
     */
    @FXML
    private void handleBtnUploadImage(MouseEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select an image");
        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("Imágenes", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );
        
        Stage stage = (Stage) btnUploadImage.getScene().getWindow();
        File file = fileChooser.showOpenDialog(stage);
        
        if (file != null) {
            // Guardamos la URL de la imagen seleccionada
            selectedImageUrl = file.toURI().toString();
            Image image = new Image(selectedImageUrl);
            
            BackgroundImage backgroundImage = new BackgroundImage(
                image,
                BackgroundRepeat.NO_REPEAT, 
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER, 
                new BackgroundSize(100, 100, true, true, true, false)
            );
            paneImgProfile.setBackground(new Background(backgroundImage));
            checkForModifications();
        }
    }

    @FXML
    private void handleBtnDelete(MouseEvent event) {
        if (currentUser == null) return;
        
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Delete account");
        dialog.setHeaderText("Enter your password to confirm the account deletion.");
        
        ButtonType confirmButtonType = new ButtonType("Confirm", ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(confirmButtonType, ButtonType.CANCEL);
        
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        
        VBox vbox = new VBox();
        vbox.setSpacing(10);
        vbox.getChildren().addAll(new Label("Password:"), passwordField);
        dialog.getDialogPane().setContent(vbox);
        
        Platform.runLater(() -> passwordField.requestFocus());
        
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == confirmButtonType) {
                return passwordField.getText();
            }
            return null;
        });
        
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(inputPassword -> {
            if (PasswordUtil.hashPassword(inputPassword).equals(currentUser.getPassword())) {
                try {
                    deleteAccount();
                    
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Account deleted");
                    alert.setHeaderText(null);
                    alert.setContentText("The account has been successfully deleted.");
                    alert.showAndWait();
                    
                    if (mainController != null) {
                        mainController.logOutAndGoToLogin();
                    }
                } catch (Exception e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Account Deletion Failed");
                    alert.setContentText(e.getMessage());
                    alert.showAndWait();
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Incorrect password");
                alert.setHeaderText(null);
                alert.setContentText("The password entered is incorrect.");
                alert.showAndWait();
            }
        });
    }

    @SuppressWarnings("deprecation")
    private void deleteAccount() throws Exception {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            session.beginTransaction();

            // 1. Recargar el usuario desde la base de datos
            User user = session.get(User.class, currentUser.getId());
            if (user == null) {
                throw new Exception("The user does not exist.");
            }

            // 2. Eliminar las colecciones asociadas
            String sqlDeleteCollections = "DELETE FROM collections WHERE userId = :userId";
            session.createNativeQuery(sqlDeleteCollections)
                   .setParameter("userId", user.getId())
                   .executeUpdate();

            // 3. Eliminar el usuario
            session.delete(user);

            session.getTransaction().commit();
        } catch (Exception e) {
            if (session.getTransaction().isActive()) {
                session.getTransaction().rollback();
            }
            throw e;
        } finally {
            session.close();
        }
    }

    /**
     * Se invoca para asignar los datos del usuario y, además, guarda los valores originales
     * para poder comparar y detectar modificaciones.
     */
    public void setUserData(User user) {
        if (user == null) return;

        this.currentUser = user;
        
        txtUsername.setText(user.getUsername());
        txtMail.setText(user.getEmail());
        // Para la contraseña, normalmente se deja el campo vacío al editar
        password.setText("");
        
        // Guardamos los valores originales
        originalUsername = user.getUsername();
        originalEmail = user.getEmail();
        originalPictureUrl = user.getPicture();
        
        // Configurar la imagen de perfil
        Image userImage;
        try {
        	userImage = new Image(user.getPicture());        	
        } catch (Exception e) {
        	userImage = new Image("images/ProfileImage.png");
        }
        BackgroundImage backgroundImage = new BackgroundImage(
                userImage,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(100, 100, true, true, true, false)
        );
        paneImgProfile.setBackground(new Background(backgroundImage));
        
        // Agregar los listeners para detectar cambios (si aún no se han agregado)
        if (!listenersAdded) {
            setupListeners();
            listenersAdded = true;
        }
    }
    
	public void loadUserImage(Pane pane, String url) {
		// File para comprobar el origen del URL
		File file = new File(url);
		File defaultImage = new File("src/main/resources/Images/ProfileImage.png");
		
		if (file.exists() || file.isFile()) {
			pane.setStyle("-fx-background-image: url('"+file.toURI().toString()+"'); "
					+ "-fx-background-size: cover; "
					+ "-fx-background-position: center;");
		} else {
			pane.setStyle("-fx-background-image: url('"+defaultImage.toURI().toString()+"'); "
					+ "-fx-background-size: cover; "
					+ "-fx-background-position: center;");
		}
	}
    
    /**
     * Agrega listeners a los campos de texto para detectar modificaciones.
     */
    private void setupListeners() {
        txtUsername.textProperty().addListener((obs, oldValue, newValue) -> checkForModifications());
        txtMail.textProperty().addListener((obs, oldValue, newValue) -> checkForModifications());
        password.textProperty().addListener((obs, oldValue, newValue) -> checkForModifications());
    }
    
    /**
     * Comprueba si se han realizado modificaciones en alguno de los campos.
     * Si es así, hace visible el botón btnSave.
     */
    private void checkForModifications() {
        // Solo se comprueba si se está en modo edición (por ejemplo, si el campo no está deshabilitado)
        if (!txtUsername.isDisabled()) {
            boolean modified = false;
            if (!txtUsername.getText().equals(originalUsername)) {
                modified = true;
            }
            if (!txtMail.getText().equals(originalEmail)) {
                modified = true;
            }
            // Si el campo de contraseña tiene algún valor, se considera modificado
            if (!password.getText().isEmpty()) {
                modified = true;
            }
            // Si se subió una nueva imagen (y es distinta a la original)
            if (selectedImageUrl != null && !selectedImageUrl.equals(originalPictureUrl)) {
                modified = true;
            }
            btnSave.setVisible(modified);
        }
    }
}
