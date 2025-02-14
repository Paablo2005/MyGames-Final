package controllers;

import java.io.File;

import org.hibernate.Session;

import dao.GameDaoImpl;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import models.Game;
import utils.HibernateUtil;
import javafx.scene.input.MouseEvent;

/**
 * Controlador para el contenedor de visualización de un juego.
 * <p>
 * Este controlador se encarga de gestionar la interfaz que muestra la imagen y el nombre
 * de un objeto {@link models.Game} en la aplicación. Utiliza componentes de JavaFX para
 * renderizar los elementos visuales.
 * </p>
 */
public class GameContainerController {

    /**
     * Panel que contiene la imagen del juego.
     */
    @FXML
    private Pane paneImg;

    /**
     * Etiqueta que muestra el nombre del juego.
     */
    @FXML
    private Label labelName;

    private Game game;
    
    private String placeholderImg = "https://mrchava.es/wp-content/uploads/2021/09/placeholder.png";

    /**
     * Inicializa el contenedor de la imagen del juego.
     * <p>
     * Configura un recorte (clip) con bordes redondeados para el {@code paneImg}, 
     * limitando la visualización de la imagen a un rectángulo de dimensiones 156 x 97.6
     * con arcos de 40 en ambos ejes. Este método se invoca automáticamente al cargar la
     * vista FXML asociada.
     * </p>
     */
    public void initialize() {
        Rectangle clipImg = new Rectangle(156, 97.6);
        clipImg.setArcHeight(40);
        clipImg.setArcWidth(40);
        paneImg.setClip(clipImg);
    }

    /**
     * Configura la información del juego en el contenedor.
     * <p>
     * Establece la imagen de fondo del panel {@code paneImg} utilizando la URL obtenida de
     * {@link models.Game#getPrincipalImg()} y ajusta su tamaño y posición para cubrir
     * adecuadamente el área del panel. Además, actualiza el texto de la etiqueta
     * {@code labelName} con el nombre del juego obtenido de {@link models.Game#getName()}.
     * </p>
     *
     * @param game el objeto {@link models.Game} que contiene los datos del juego a mostrar.
     */
    public void setGame(Game game) {
    	this.game = game;
		File file = new File(game.getPrincipalImg());
		
		if (file.exists() || file.isFile()) {
			paneImg.setStyle("-fx-background-image: url('"+file.toURI().toString()+"'); "
					+ "-fx-background-size: cover; "
					+ "-fx-background-position: center;");
		} else if (game.getApiId() > 0 || game.getPrincipalImg().equals(placeholderImg)) {
			paneImg.setStyle("-fx-background-image: url('"+game.getPrincipalImg()+"'); "
					+ "-fx-background-size: cover; "
					+ "-fx-background-position: center;");
		} else {
			paneImg.setStyle("-fx-background-image: url('"+file.toURI().toString()+"'); "
					+ "-fx-background-size: cover; "
					+ "-fx-background-position: center;");
		}
    	labelName.setText(game.getName());
    }

	public Game getGame() {
		return game;
	}
    
    @FXML
    void handleDetails(MouseEvent event) {
    	/**
    	 * Si tenemos en nuestra BBDD un juego con la ID de la api del juego que tenemos nosotros, directamente
    	 * nos traemos la informacion de nuestra BBDD y no de internet
    	 */
    	try (Session session = HibernateUtil.getSessionFactory().openSession()) {
    		GameDaoImpl dao = new GameDaoImpl(session);
    		
    		if (game.getApiId() > 0) {
    			if (dao.getByApiId(game.getApiId()) != null) {
    				MainPaneController.getInstance().loadGameDetailsFromDatabase(game.getId());
    			} else {
    				MainPaneController.getInstance().loadGameDetailsFromApi(game.getApiId());
    			}
    		} else {
    			MainPaneController.getInstance().loadGameDetailsFromDatabase(game.getId());
    		}
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }
}
