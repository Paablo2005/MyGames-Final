package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import models.Collection;
import models.Game;
import models.Genre;
import models.Platforms;
import utils.PasswordUtil;
import java.io.File;
import java.sql.Date;

import javafx.scene.Node;

import org.controlsfx.control.CheckComboBox;
import org.controlsfx.control.Rating;
import org.hibernate.Hibernate;
import org.hibernate.Session;

import dao.CollectionDaoImpl;
import dao.GameDaoImpl;

public class CollectionGameInfoEditPaneController {
	
    /**
     * Instancia única (singleton) de {@link CollectionGameInfoEditPaneController}.
     */
    private static CollectionGameInfoEditPaneController instance;
    
    /**
     * Obtiene la instancia actual de {@link CollectionGameInfoEditPaneController}.
     *
     * @return la instancia actual del controlador
     */
    public static CollectionGameInfoEditPaneController getInstance() {
        return instance;
    }
    
    private String placeholderImg = "https://mrchava.es/wp-content/uploads/2021/09/placeholder.png";


    @FXML
    private DatePicker datePicker;

    @FXML
    private Label btnCancel;

    @FXML
    private Label btnSave;

    @FXML
    private Button btnUploadImage;

    @FXML
    private Button btnUploadImage1;

    @FXML
    private Button btnUploadImage2;

    @FXML
    private Button btnUploadImage3;

    @FXML
    private CheckBox checkPlayed;

    @FXML
    private CheckComboBox<String> comboGenre;

    @FXML
    private CheckComboBox<String> comboPlatform;

    @FXML
    private BorderPane paneImage;

    @FXML
    private BorderPane paneImage1;

    @FXML
    private BorderPane paneImage2;

    @FXML
    private BorderPane paneImage3;

    @FXML
    private Rating rating;

    @FXML
    private TextArea txtCommentary;

    @FXML
    private TextArea txtDescription;

    @FXML
    private TextField txtStudio;

    @FXML
    private TextField txtTitle;
    
    private String image0;
    private String image1;
    private String image2;
    private String image3;
    
    private Game game;
    private Collection collection;
    private Session session;
    
    @FXML
	public void initialize() {
    	instance = this;
    	
        // Añadir elementos al combo de géneros
        comboGenre.getItems().addAll(
            "Action", "Indie", "Adventure", "RPG", "Strategy", "Shooter", "Casual", 
            "Simulation", "Puzzle", "Arcade", "Platformer", "Massively Multiplayer", 
            "Racing", "Sports", "Fighting", "Family", "Board Games", "Educational", "Card"
        );
        
        // Añadir elementos al combo de plataformas
        comboPlatform.getItems().addAll(
            "PC", "PlayStation 5", "Xbox One", "PlayStation 4", "Xbox Series S/X", 
            "Nintendo Switch", "iOS", "Android", "Nintendo 3DS", "Nintendo DS", "Nintendo DSi", 
            "macOS", "Linux", "Xbox 360", "Xbox", "PlayStation 3", "PlayStation 2", 
            "PlayStation", "PS Vita", "PSP", "Wii U", "Wii", "GameCube", "Nintendo 64", 
            "Game Boy Advance", "Game Boy Color", "Game Boy", "SNES", "NES", "Classic Macintosh", 
            "Apple II", "Commodore / Amiga", "Atari 7800", "Atari 5200", "Atari 2600", "Atari Flashback", 
            "Atari 8-bit", "Atari ST", "Atari Lynx", "Atari XEGS", "Genesis", "SEGA Saturn", 
            "SEGA CD", "SEGA 32X", "SEGA Master System", "Dreamcast", "3DO", "Jaguar", "Game Gear", 
            "Neo Geo", "Web"
        );
	}
    
	public void setSession(Session session) {
		this.session = session;
	}
	
	public void setGame(int gameId) {
		try {
			GameDaoImpl dao = new GameDaoImpl(session);
			
			this.game = dao.getByDatabaseId(gameId);
			
	        Hibernate.initialize(this.game.getPlatforms());
	        Hibernate.initialize(this.game.getGenres());

	        updateGameDetails();
		} catch (Exception e) {
			e.printStackTrace();
			this.game = null;
		}
	}
	
	public void updateGameDetails() {
		CollectionDaoImpl colDao = new CollectionDaoImpl(session);
		collection = colDao.getByCompositeKey(PasswordUtil.getLoggedUser().getId(), game.getId());

		txtTitle.setText(game.getName());
		txtStudio.setText(game.getDevelopers());
		txtDescription.setText(game.getDescription());
		
		for (String platform : game.platformsToString())
		    comboPlatform.getCheckModel().check(platform);
		for (String genre : game.genresToString())
		    comboGenre.getCheckModel().check(genre);
		
		loadGameImage(paneImage, (game.getPrincipalImg() == null || game.getPrincipalImg().isEmpty()) ? placeholderImg : game.getPrincipalImg(), image0);
		loadGameImage(paneImage1, (game.getImage1() == null || game.getImage1().isEmpty()) ? placeholderImg : game.getImage1(), image1);
		loadGameImage(paneImage2, (game.getImage2() == null || game.getImage2().isEmpty()) ? placeholderImg : game.getImage2(), image2);
		loadGameImage(paneImage3, (game.getImage3() == null || game.getImage3().isEmpty()) ? placeholderImg : game.getImage3(), image3);
		
		txtCommentary.setText(collection.getReview());
		datePicker.setValue(collection.getFinishDate().toLocalDate());
		rating.setRating(collection.getScore());
		checkPlayed.setSelected(collection.getPlayed());
    	
	}
	
	public void loadGameImage(BorderPane pane, String url, String imgX) {
		// File para comprobar el origen del URL
		File file = new File(url);
		
		if (file.exists() || file.isFile()) {
			pane.setStyle("-fx-background-image: url('"+file.toURI().toString()+"'); "
					+ "-fx-background-size: cover; "
					+ "-fx-background-position: center;");
			imgX = url;
		} else if (game.getApiId() > 0 || url.equals(placeholderImg)) {
			pane.setStyle("-fx-background-image: url('"+url+"'); "
					+ "-fx-background-size: cover; "
					+ "-fx-background-position: center;");
			imgX = url; 
		} else {
			pane.setStyle("-fx-background-image: url('"+file.toURI().toString()+"'); "
					+ "-fx-background-size: cover; "
					+ "-fx-background-position: center;");
			imgX = new File(url).toURI().toString();
		}
	}
    
    @FXML
    void goBack(MouseEvent event) {
    	backToGameDetails();
    }

    @FXML
    void updateChanges(MouseEvent event) {
    	/**
    	 * Actualizamos TODOS los datos del objeto Game a lo rata
    	 * Y lo actualizamos con un Merge a la base de datos
    	 *  - Debemos de impiar la lista de Generos o Plataformas y 
    	 *  rellenarla de nuevo con las opciones elegidas
    	 */
    	game.setName(txtTitle.getText());
    	game.setDescription(txtDescription.getText());
    	game.setDevelopers(txtStudio.getText());
    	
    	collection.setPlayed(checkPlayed.isSelected());
    	collection.setReview(txtCommentary.getText());
    	collection.setScore((int)rating.getRating());
    	collection.setFinishDate(Date.valueOf(datePicker.getValue()));
    	
    	// Imagenes del juego
    	game.setPrincipalImg(image0);
    	game.setImage1(image1);
    	game.setImage2(image2);
    	game.setImage3(image3);
    	
//    	game.getPlatforms().clear();
//		for (String selected : comboPlatform.getCheckModel().getCheckedItems()) {
//			Platforms pl = Platforms.getPlatformByName(selected, session);
//			System.out.println(pl);
//			game.getPlatforms().add(pl);
//		}
//		System.out.println(game.getPlatforms().size());
		
//		game.getGenres().clear();
//		for (String selected : comboGenre.getCheckModel().getCheckedItems()) {
//			game.getGenres().add(Genre.getGenreByName(selected, session));
//		}
//    	System.out.println(game.getGenres().size());
    	
    	session.merge(collection);
    	session.merge(game);
    	session.getTransaction().commit();
    	
    	// Despues de realizar todos los cambios tenemos que volver para atras
    	backToGameDetails();
    }
    
    private void backToGameDetails() {
    	MainPaneController.getInstance().loadGameDetailsFromDatabase(game.getId());
    }

    // --- Métodos ejecutados al intentar insertar una nueva imagen ---
    @FXML
    void uploadImage(MouseEvent event) {
		File file = selectFile(event);
		image0 = (file == null) ? placeholderImg : file.getAbsolutePath();
    	loadGameImage(paneImage, image0, image0);
	}

    @FXML
    void uploadImage1(MouseEvent event) {
    	File file = selectFile(event);
		image1 = (file == null) ? placeholderImg : file.getAbsolutePath();	
    	loadGameImage(paneImage1, image1, image1);
    }

    @FXML
    void uploadImage2(MouseEvent event) {
    	File file = selectFile(event);
		image2 = (file == null) ? placeholderImg : file.getAbsolutePath();
		loadGameImage(paneImage2, image2, image2);
    }

    @FXML
    void uploadImage3(MouseEvent event) {
    	File file = selectFile(event);
		image3 = (file == null) ? placeholderImg : file.getAbsolutePath();
    	loadGameImage(paneImage3, image3, image3);
    }
    
    private File selectFile(MouseEvent event) {
    	FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Select an Image");
		
		fileChooser.getExtensionFilters().add(
				new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
				);
		
		Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		return fileChooser.showOpenDialog(stage);
    }
    // ----------------------------------------------------------------
}
