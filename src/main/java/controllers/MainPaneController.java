package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.Cursor;
import javafx.scene.Scene;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import models.User;
import models.Game;

/**
 * Controlador para el panel principal de la aplicación.
 * <p>
 * Esta clase gestiona el diseño principal de la aplicación, incluyendo la navegación entre
 * las vistas de inicio, colección y juegos, así como la visualización de los datos del usuario y
 * la funcionalidad de cierre de sesión. Se encarga de aplicar efectos visuales al pasar el ratón
 * por encima de ciertos elementos y de cargar los diferentes paneles de la interfaz.
 * </p>
 */
public class MainPaneController {

    /**
     * Botón (FlowPane) que dirige a la vista de inicio.
     */
    @FXML
    private FlowPane btnHome;

    /**
     * Botón (FlowPane) que dirige a la vista de colección.
     */
    @FXML
    private FlowPane btnCollection;

    /**
     * Botón (FlowPane) que dirige a la vista de juegos.
     */
    @FXML
    private FlowPane btnGames;

    /**
     * Etiqueta que muestra el nombre de usuario.
     */
    @FXML
    private Label lblUsername;

    /**
     * Etiqueta que muestra el correo electrónico del usuario.
     */
    @FXML
    private Label lblMail;

    /**
     * Etiqueta para la imagen de perfil (posiblemente un marcador de posición).
     */
    @FXML
    private Label lblProfileImg;

    /**
     * Vista de imagen para mostrar la imagen de perfil del usuario.
     */
    @FXML
    private ImageView profileImageView;

    /**
     * Etiqueta que muestra la fecha actual.
     */
    @FXML
    private Label lblDate;

    /**
     * Botón (FlowPane) que permite cerrar sesión.
     */
    @FXML
    private FlowPane ContainerLogOut;

    /**
     * Contenedor (FlowPane) que muestra la imagen de perfil.
     */
    @FXML
    private FlowPane containerImgProfile;

    /**
     * Contenedor (FlowPane) que muestra los datos del usuario.
     */
    @FXML
    private FlowPane containerData;
    
    /**
     * Panel principal que contiene el contenido dinámico de la aplicación.
     */
    @FXML
    private BorderPane panePrincipal;
    
    /**
     * Panel de encabezado que se utiliza para cargar elementos como la barra de búsqueda.
     */
    @FXML
    private BorderPane paneHeader;
    
    /**
     * Panel para la vista de colección.
     */
    @FXML
    private BorderPane paneCollection;
    
    /**
     * Panel para la vista de juegos.
     */
    @FXML
    private BorderPane paneGames;

    /**
     * Método de inicialización que se invoca automáticamente al cargar la vista FXML.
     * <p>
     * Configura los efectos de hover para los botones y contenedores, asigna acciones a los eventos
     * de clic y establece la navegación entre las diferentes vistas de la aplicación.
     * </p>
     */
    @FXML
    public void initialize() {
        setupHoverEffect(btnHome);
        setupHoverEffect(btnCollection);
        setupHoverEffect(btnGames);
        setupHoverEffect(ContainerLogOut);

        setupProfileAndDataHoverEffect(containerImgProfile, containerData);
        setupProfileAndDataHoverEffect(containerData, containerImgProfile);

        ContainerLogOut.setOnMouseClicked(event -> logOutAndGoToLogin());

        btnCollection.setOnMouseClicked(event -> {
            loadHeaderPane();
            loadCollectionPane();
        });
        btnGames.setOnMouseClicked(event -> {
            loadHeaderPane();
            loadGamesPane();
        });
        btnHome.setOnMouseClicked(event -> loadHomePane());

        containerImgProfile.setOnMouseClicked(event -> loadUserDataPane());
        containerData.setOnMouseClicked(event -> loadUserDataPane());
        
        loadHomePane();
    }

    /**
     * Aplica un efecto de hover a un botón.
     * <p>
     * Cambia el estilo del botón y el cursor del ratón cuando este se sitúa o se retira del área del botón.
     * </p>
     *
     * @param button el botón (FlowPane) al que se aplicará el efecto de hover
     */
    private void setupHoverEffect(FlowPane button) {
        button.setOnMouseEntered(event -> {
            button.setStyle("-fx-background-color: #555555;");
            button.getScene().setCursor(Cursor.HAND);
        });
        button.setOnMouseExited(event -> {
            button.setStyle("-fx-background-color: #242424;");
            button.getScene().setCursor(Cursor.DEFAULT);
        });
    }

    /**
     * Configura y muestra los datos del usuario en la interfaz.
     * <p>
     * Actualiza las etiquetas con el nombre de usuario y correo, carga la imagen de perfil y
     * muestra la fecha actual.
     * </p>
     *
     * @param user el objeto {@link models.User} que contiene los datos del usuario
     */
    public void setUserData(User user) {
        lblUsername.setText(user.getUserName());
        lblMail.setText(user.getMail());

        String profileImageUrl = user.getProfileImage();
        Image image = new Image(profileImageUrl);
        profileImageView.setImage(image);

        String currentDateTime = LocalDate.now()
            .format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        lblDate.setText(currentDateTime);
    }

    /**
     * Cierra la sesión actual y redirige al usuario a la pantalla de login.
     * <p>
     * Intenta cerrar la ventana actual y cargar la vista de Login. En caso de error, muestra una alerta.
     * </p>
     */
    private void logOutAndGoToLogin() {
        try {
            Stage currentStage = (Stage) ContainerLogOut.getScene().getWindow();
            currentStage.close();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/Login.fxml"));
            AnchorPane loginPane = loader.load();

            Scene loginScene = new Scene(loginPane);
            Stage loginStage = new Stage();
            loginStage.setScene(loginScene);
            loginStage.setTitle("Login");
            loginStage.show();
        } catch (Exception e) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error al cambiar a la pantalla de Login");
            alert.setContentText("Hubo un problema al cerrar la ventana y abrir el Login.");
            alert.showAndWait();
        }
    }

    /**
     * Aplica un efecto de hover sincronizado entre dos contenedores.
     * <p>
     * Cuando el ratón se sitúa sobre el contenedor objetivo, se cambia el estilo de ambos contenedores y
     * se modifica el cursor. Al salir, se restablecen los estilos y el cursor por defecto.
     * </p>
     *
     * @param target el contenedor sobre el que se detecta el evento de hover
     * @param other  el contenedor cuyo estilo se sincroniza con el contenedor objetivo
     */
    private void setupProfileAndDataHoverEffect(FlowPane target, FlowPane other) {
        target.setOnMouseEntered(event -> {
            target.setStyle("-fx-background-color: #555555;");
            other.setStyle("-fx-background-color: #555555;");
            target.getScene().setCursor(Cursor.HAND);
        });

        target.setOnMouseExited(event -> {
            target.setStyle("-fx-background-color: #242424;");
            other.setStyle("-fx-background-color: #242424;");
            target.getScene().setCursor(Cursor.DEFAULT);
        });
    }
    
    /**
     * Carga el panel de datos del usuario en la zona central del panel principal.
     * <p>
     * Intenta cargar el archivo FXML correspondiente a {@code UserDataPane.fxml} y situarlo en el centro
     * del panel principal. En caso de error, muestra una alerta con la información del problema.
     * </p>
     */
    private void loadUserDataPane() {
        try {
            clearHeaderPane();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/UserDataPane.fxml"));
            BorderPane userDataPane = loader.load();

            panePrincipal.setCenter(userDataPane);

        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error al cargar UserDataPane");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            e.printStackTrace();
        }
    }
    
    /**
     * Carga el panel del encabezado.
     * <p>
     * Intenta cargar el archivo FXML correspondiente a {@code HeaderPane.fxml} y lo sitúa en el centro
     * del panel de encabezado. En caso de error, muestra una alerta.
     * </p>
     */
    @FXML
    private void loadHeaderPane() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/HeaderPane.fxml"));
            BorderPane headerPane = loader.load();

            paneHeader.setCenter(headerPane);
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error al cargar HeaderPane");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            e.printStackTrace();
        }
    }

    /**
     * Limpia el contenido del panel de encabezado.
     */
    @FXML
    private void clearHeaderPane() {
        paneHeader.setCenter(null);
    }
    
    /**
     * Carga el panel de colección en la zona central del panel principal.
     * <p>
     * Intenta cargar el archivo FXML correspondiente a {@code CollectionPane.fxml} y lo sitúa en el centro
     * del panel principal. En caso de error, muestra una alerta.
     * </p>
     */
    @FXML
    private void loadCollectionPane() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/CollectionPane.fxml"));
            BorderPane collectionPane = loader.load();

            panePrincipal.setCenter(collectionPane);
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error al cargar CollectionPane");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            e.printStackTrace();
        }
    }
            
    /**
     * Carga el panel de inicio en la zona central del panel principal.
     * <p>
     * Intenta cargar el archivo FXML correspondiente a {@code HomePane.fxml} y lo sitúa en el centro
     * del panel principal. En caso de error, muestra una alerta.
     * </p>
     */
    public void loadHomePane() {
      try {
          clearHeaderPane();

          FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/HomePane.fxml"));
          BorderPane homePane = loader.load();

          HomePaneController homeController = loader.getController();
          homeController.setMainPaneController(this); // Pasamos la referencia del MainPaneController

          panePrincipal.setCenter(homePane);

      } catch (Exception e) {
          Alert alert = new Alert(Alert.AlertType.ERROR);
          alert.setTitle("Error");
          alert.setHeaderText("Error al cargar HomePane");
          alert.setContentText(e.getMessage());
          alert.showAndWait();
          e.printStackTrace();
      }
  }
    
    /**
     * Carga el panel de juegos en la zona central del panel principal.
     * <p>
     * Intenta cargar el archivo FXML correspondiente a {@code GamesPane.fxml} y lo sitúa en el centro
     * del panel principal. En caso de error, muestra una alerta.
     * </p>
     */
    @FXML
    private void loadGamesPane() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/GamesPane.fxml"));
            BorderPane gamesPane = loader.load();

            GamesPaneController gamesController = loader.getController();
            gamesController.setMainPaneController(this); // 🔹 Pasa la referencia del MainPaneController

            panePrincipal.setCenter(gamesPane);

        } catch (Exception e) {
            System.out.println("Error al cargar GamesPane: " + e.getMessage());
            e.printStackTrace();
        }
    }

    
    public void loadDescriptionPane(Game game) {
      try {
          System.out.println("Cargando DescriptionPane para: " + game.getName()); // Debugging
        
          FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/DescriptionPane.fxml"));
          AnchorPane descriptionPane = loader.load(); // Cambio de BorderPane a AnchorPane

          DescriptionPaneController controller = loader.getController();
          controller.setGameDetails(game);

          panePrincipal.setCenter(descriptionPane); // Mantiene la lógica de carga en el panel principal
          panePrincipal.requestLayout();
          
      } catch (Exception e) {
        System.out.println("Error al cargar DescriptionPane: " + e.getMessage());
        e.printStackTrace();
    }
  }

}