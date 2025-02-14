package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import models.Game;

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
        paneImg.setStyle("-fx-background-image: url('" + game.getPrincipalImg() + "'); " +
                           "-fx-background-size: cover; " +
                           "-fx-background-position: center;");
        labelName.setText(game.getName());
    }
}
