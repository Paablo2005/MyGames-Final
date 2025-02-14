package controllers;

import javafx.fxml.FXML;

import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.Cursor;
import javafx.scene.Scene;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import models.User;
import utils.PasswordUtil;

public class MainPaneController {
	
    /**
     * Instancia única (singleton) de {@link MainPaneController}.
     */
    private static MainPaneController instance;
    
    /**
     * Obtiene la instancia actual de {@link MainPaneController}.
     *
     * @return la instancia actual del controlador
     */
    public static MainPaneController getInstance() {
        return instance;
    }

    @FXML
    private FlowPane btnHome;

    @FXML
    private FlowPane btnCollection;

    @FXML
    private FlowPane btnGames;

    @FXML
    private Label lblUsername;

    @FXML
    private Label lblMail;

    @FXML
    private Label lblProfileImg;

    @FXML
    private ImageView profileImageView;

    @FXML
    private Label lblDate;

    @FXML
    private FlowPane ContainerLogOut;

    @FXML
    private FlowPane containerImgProfile;

    @FXML
    private FlowPane containerData;
    
    @FXML
    private BorderPane panePrincipal;
    
    @FXML
    private BorderPane paneHeader;
    
    @FXML
    private BorderPane paneCollection;
    
    @FXML
    private BorderPane paneGames;
    
    private BorderPane headerPane;
    private HeaderPaneController headerPaneController;
    
    private BorderPane collectionGameInfoPane;
    private CollectionGameInfoPaneController collectionGameInfoController;
    
    private BorderPane collectionGameInfoEditPane;
    private CollectionGameInfoEditPaneController collectionGameInfoEditPaneController;

    @FXML
    public void initialize() {
    	instance = this;
    	containerImgProfile.layoutBoundsProperty().addListener((obs, oldBounds, newBounds) -> {
            double radius = Math.min(newBounds.getWidth(), newBounds.getHeight()) / 2;
            Circle clip = new Circle(newBounds.getWidth() / 2, newBounds.getHeight() / 2, radius);
            containerImgProfile.setClip(clip);
        });
        
       setupHoverEffect(btnHome);
        setupHoverEffect(btnCollection);
        setupHoverEffect(btnGames);
        setupHoverEffect(ContainerLogOut);

        setupProfileAndDataHoverEffect(containerImgProfile);
        setupProfileAndDataHoverEffect(containerData);

        ContainerLogOut.setOnMouseClicked(event -> logOutAndGoToLogin());

        headerPane = loadHeaderPane();
        
        preloadGameInfoDatabasePane();
    	collectionGameInfoController.setSession();
    	collectionGameInfoEditPaneController.setSession(collectionGameInfoController.getSession());
    	
        clearHeaderPane();
        // Configurar los clics en los botones
        btnCollection.setOnMouseClicked(event -> {
        	headerPaneController.setGamesCurrentScreen(false);
            loadCollectionPane();
        });
        btnGames.setOnMouseClicked(event -> {
        	headerPaneController.setGamesCurrentScreen(true);
            loadGamesPane();
        });
        btnHome.setOnMouseClicked(event -> loadHomePane());

        // Configura los clics en los contenedores para cargar UserDataPane
        containerImgProfile.setOnMouseClicked(event -> loadUserDataPane());
        containerData.setOnMouseClicked(event -> loadUserDataPane());
        
        loadHomePane();
    }

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

    public void setUserData(User user) {
    	PasswordUtil.setLoggedUser(user);
        lblUsername.setText(user.getUsername());
        lblMail.setText(user.getEmail());

        String profileImageUrl = user.getPicture();
        Image image;
        try {
        	image = new Image(profileImageUrl);        	
        } catch (Exception e) {
        	image = new Image("images/ProfileImage.png");
        }
        profileImageView.setImage(image);

        String currentDateTime = LocalDate.now()
            .format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        lblDate.setText(currentDateTime);
    }

    public void logOutAndGoToLogin() {
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

    private void setupProfileAndDataHoverEffect(FlowPane target) {
        target.setOnMouseEntered(event -> {
            target.setStyle("-fx-background-color: #555555;");
            target.getScene().setCursor(Cursor.HAND);
        });

        target.setOnMouseExited(event -> {
            target.setStyle("-fx-background-color: #242424;");
            target.getScene().setCursor(Cursor.DEFAULT);
        });
    }
    
    public void loadUserDataPane() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/UserDataPane.fxml"));
            BorderPane userDataPane = loader.load();

            // Obtener el controlador del UserDataPane
            UserDataPaneController userDataController = loader.getController();

            // Pasar el usuario autenticado usando la variable currentUser
            userDataController.setUserData(PasswordUtil.getLoggedUser());
            // Pasar la referencia de este controlador principal
            userDataController.setMainController(this);

            clearHeaderPane();
            
            // Mostrar el pane en la zona central
            panePrincipal.setCenter(userDataPane);

        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error al cargar UserDataPane");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    private BorderPane loadHeaderPane() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/HeaderPane.fxml"));
            BorderPane headerPane = loader.load();

            paneHeader.setCenter(headerPane);
            headerPaneController = loader.getController();
            return headerPane;
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error al cargar HeaderPane");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            e.printStackTrace();
            return null;
        }
    }

    @FXML
    private void clearHeaderPane() {
        paneHeader.setCenter(null);
    }
    
    private void setHeaderPane() {
    	paneHeader.setCenter(headerPane);
    }
    
    public void reloadCollectionPane() {
    	loadCollectionPane();
    }
    
    @FXML
    private void loadCollectionPane() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/CollectionPane.fxml"));
            BorderPane collectionPane = loader.load();

            panePrincipal.setCenter(collectionPane);
            setHeaderPane();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error al cargar CollectionPane");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            e.printStackTrace();
        }
    }
            
    private void loadHomePane() {
        try {
        	clearHeaderPane();
        	
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/HomePane.fxml"));
            BorderPane homePane = loader.load();

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
    
    @FXML
    private void loadGamesPane() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/GamesPane.fxml"));
            BorderPane gamesPane = loader.load();

            panePrincipal.setCenter(gamesPane);
            setHeaderPane();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error al cargar GamesPane");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            e.printStackTrace();
        }
    }
    
    private void preloadGameInfoDatabasePane() {
    	try {
    		FXMLLoader infoLoader = new FXMLLoader(getClass().getResource("/views/CollectionGameInfoPane.fxml"));

			collectionGameInfoPane = infoLoader.load();
			collectionGameInfoController = infoLoader.getController();
			
			FXMLLoader editLoader = new FXMLLoader(getClass().getResource("/views/CollectionGameInfoEditPane.fxml"));

			collectionGameInfoEditPane = editLoader.load();
			collectionGameInfoEditPaneController = editLoader.getController();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    protected void loadGameDetailsFromDatabase(int gameId) {
    	clearHeaderPane();
        try {
        	// Tenemos única conexión a la BBDD antes de cargar los detalles un juego.
	    	collectionGameInfoController.setGame(gameId);
	    	collectionGameInfoEditPaneController.setGame(gameId);
	    	
	    	panePrincipal.setCenter(collectionGameInfoPane);
	    } catch (Exception e) {
	        Alert alert = new Alert(Alert.AlertType.ERROR);
	        alert.setTitle("Error");
	        alert.setHeaderText("Error al cargar GamesPane");
	        alert.setContentText(e.getMessage());
	        alert.showAndWait();
	        e.printStackTrace();
	    }
    }

	public void editDatabaseGame() {		
		panePrincipal.setCenter(collectionGameInfoEditPane);
	}
}
