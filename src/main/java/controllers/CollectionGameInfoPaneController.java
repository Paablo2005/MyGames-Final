package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import models.Collection;
import models.Game;
import utils.HibernateUtil;
import utils.PasswordUtil;

import java.io.File;

import org.controlsfx.control.Rating;
import org.hibernate.Hibernate;
import org.hibernate.Session;

import dao.CollectionDaoImpl;
import dao.GameDaoImpl;

public class CollectionGameInfoPaneController {
	
    /**
     * Instancia única (singleton) de {@link CollectionGameInfoPaneController}.
     */
    private static CollectionGameInfoPaneController instance;
    
    /**
     * Obtiene la instancia actual de {@link CollectionGameInfoPaneController}.
     *
     * @return la instancia actual del controlador
     */
    public static CollectionGameInfoPaneController getInstance() {
        return instance;
    }
    
    private String placeholderImg = "https://mrchava.es/wp-content/uploads/2021/09/placeholder.png";

    @FXML
    private Label btnEdit;
    
    @FXML
    private Label btnBack;

    @FXML
    private Label lblCommentary;

    @FXML
    private Label lblDescription;

    @FXML
    private Label lblFinishDates;

    @FXML
    private Label lblGenre;

    @FXML
    private Label lblPlatform;

    @FXML
    private Label lblStudio;

    @FXML
    private Label lblTitle;

    @FXML
    private BorderPane paneImage;

    @FXML
    private BorderPane paneImage1;

    @FXML
    private BorderPane paneImage2;

    @FXML
    private BorderPane paneImage3;

    @FXML
    private CheckBox isPlayed;
    
    @FXML
    private Rating rating;

    private Game game;
    private Collection collection;
    private Session session;
    
    private String image0;
    private String image1;
    private String image2;
    private String image3;
    
    @FXML
	public void initialize() {
    	instance = this;
        
    	rating.setMouseTransparent(true);
    	isPlayed.setMouseTransparent(true);
	}

	public Game getGame() {
		return game;
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
	
	public Session getSession() {
		
    	if (session != null || session.isOpen() || session.isConnected()) {
    	    session = HibernateUtil.getSessionFactory().openSession();
    	}
		return session;
	}
	
	public Session setSession() {
		// Si tengo una sesión válida
    	if (session != null) {
    		if (session.isOpen() || session.isConnected()) {
    			return session;
    		} else {
        	    session = HibernateUtil.getSessionFactory().openSession();
        	    return session;
    		}
    	} else {
    	    session = HibernateUtil.getSessionFactory().openSession();
    	    return session;
    	}
	}
	
	public void updateGameDetails() {
		CollectionDaoImpl colDao = new CollectionDaoImpl(session);
		collection = colDao.getByCompositeKey(PasswordUtil.getLoggedUser().getId(), game.getId());

		lblTitle.setText(game.getName());
		lblStudio.setText(game.getDevelopers());
		lblDescription.setText(game.getDescription());
		
		lblPlatform.setText(String.join(", ", game.platformsToString()));
		lblGenre.setText(String.join(", ", game.genresToString()));
		
		loadGameImage(paneImage, (game.getPrincipalImg() == null || game.getPrincipalImg().isEmpty()) ? placeholderImg : game.getPrincipalImg(), image0);
		loadGameImage(paneImage1, (game.getImage1() == null || game.getImage1().isEmpty()) ? placeholderImg : game.getImage1(), image1);
		loadGameImage(paneImage2, (game.getImage2() == null || game.getImage2().isEmpty()) ? placeholderImg : game.getImage2(), image2);
		loadGameImage(paneImage3, (game.getImage3() == null || game.getImage3().isEmpty()) ? placeholderImg : game.getImage3(), image3);
		
		lblCommentary.setText(collection.getReview());
		lblFinishDates.setText(collection.getFinishDate().toString());
		rating.setRating(collection.getScore());
		isPlayed.setSelected(collection.getPlayed());
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
    void backToCollection(MouseEvent event) {
    	MainPaneController.getInstance().reloadCollectionPane();
    }

    @FXML
    void goToEditMode(MouseEvent event) {
    	MainPaneController.getInstance().editDatabaseGame();
    }
}
