package controllers;

import org.hibernate.Session;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import models.Collection;
import models.Game;
import utils.ApiUtils;
import utils.HibernateUtil;
import utils.PasswordUtil;

public class DescriptionPaneController {
	
    @FXML
    private Button btnAdd;

    @FXML
    private TextArea gameDescription;

    @FXML
    private Label gameDeveloper;

    @FXML
    private Label metaScore;

    @FXML
    private ImageView gameImage;

    @FXML
    private Label gameTitle;

    @FXML
    private ImageView screenshot1;

    @FXML
    private ImageView screenshot2;

    @FXML
    private ImageView screenshot3;
    
    private Session session;
    private Game game;
	
    /**
     * Instancia única (singleton) de {@link DescriptionPaneController}.
     */
    private static DescriptionPaneController instance;
    
    /**
     * Obtiene la instancia actual de {@link DescriptionPaneController}.
     *
     * @return la instancia actual del controlador
     */
    public static DescriptionPaneController getInstance() {
        return instance;
    }
    
    
	
    @FXML
	public void initialize() {
    	instance = this;
    	adjustTextAreaHeight();
	}
    
    public Game getGame() {
		return game;
	}

	public void setGame(int apiId) {
		game = ApiUtils.loadGameData(apiId);
		updateDetails();
	}
	
	private void updateDetails() {
		gameTitle.setText(game.getName());
		gameDescription.setText(game.getDescription());
		gameDeveloper.setText("Game number: "+game.getApiId());
		metaScore.setText(game.getReleaseDate().toString());
		
		Image image;
		try {
			image = new Image(game.getPrincipalImg());
		} catch (Exception e) {
			image = new Image("images/placeholder.png");
		}
		gameImage.setImage(image);
		
		try {
			image = new Image(game.getImage1());
		} catch (Exception e) {
			image = new Image("images/placeholder.png");
		}
		screenshot1.setImage(image);
		
		try {
			image = new Image(game.getImage2());
		} catch (Exception e) {
			image = new Image("images/placeholder.png");
		}
		screenshot2.setImage(image);
		
		try {
			image = new Image(game.getImage3());
		} catch (Exception e) {
			image = new Image("images/placeholder.png");
		}
		screenshot3.setImage(image);
	}
	
	// Ajustar la altura en base al contenido del texto
    private void adjustTextAreaHeight() {
      gameDescription.setPrefHeight(gameDescription.getText().split("\n").length * 20 + 100);
    }

    
    
	// Método para guardar el juego en la base de datos
	@FXML
	private void addToCollection() {
		try (Session addSession = HibernateUtil.getSessionFactory().openSession()) {
			if (game.getDescription().length() > 120)
				game.setDescription(game.getDescription().substring(0, 120));
			
			addSession.beginTransaction();
			Game persisted = addSession.merge(game);
			
			Collection col = new Collection(PasswordUtil.getLoggedUser().getId(), persisted.getId());
			addSession.merge(col);
			
			addSession.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Session getSession() {
		
    	if (session != null || session.isOpen() || session.isConnected()) {
    	    session = HibernateUtil.getSessionFactory().openSession();
    	}
		return session;
	}
	
	public void setSession(Session session) {
		this.session = session;
	}
}
